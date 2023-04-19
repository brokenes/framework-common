package com.github.framework.core.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.framework.core.lang.CustomDateUtils;
import com.github.framework.core.lang.CustomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 迁移自：
 * https://github.com/vipshop/vjtools/blob/master/vjkit/src/main/java/com/vip/vjtools/vjkit/mapper/JsonMapper.java
 *
 * 简单封装Jackson，实现JSON String<->Java Object转换的Mapper.
 *
 * 可以直接使用公共示例JsonMapper.INSTANCE, 也可以使用不同的builder函数创建实例，封装不同的输出风格,
 *
 * 不要使用GSON, 在对象稍大时非常缓慢.
 *
 * 注意: 需要参考本模块的POM文件，显式引用jackson.
 *
 */
public class JsonMapper {
    private final static Logger log = LoggerFactory.getLogger(JsonMapper.class);

    public static final JsonMapper INSTANCE = defaultMapper();
    public static final ObjectMapper OBJECT_MAPPER = objectMapper();

    private final ObjectMapper objectMapper;

    public JsonMapper(final ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public JsonMapper(final JsonInclude.Include include) {
        objectMapper = new ObjectMapper();
        // 设置输出时包含属性的风格
        if (include != null) {
            objectMapper.setSerializationInclusion(include);
        }
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 创建只输出非Null的属性到Json字符串的Mapper.
     */
    public static JsonMapper nonNullMapper() {
        return new JsonMapper(JsonInclude.Include.NON_NULL);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
     *
     * 注意，要小心使用, 特别留意empty的情况.
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * 
     * @Title: objectMapper 
     * @Description: 通用的ObjectMapper
     * @return 
     * @throws 
     * @author vanlin
     * @date 2019年4月20日 上午11:26:23
     */
    public static ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final SimpleModule longModule = new SimpleModule("LongJsonSerializer");
        longModule.addSerializer(new LongJsonSerializer());
        objectMapper.registerModule(longModule);

        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CustomDateUtils.YYYY_MM_DD_T_HHMMSS_SSS_Z);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(CustomDateUtils.YYYY_MM_DD);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(CustomDateUtils.HHMMSS);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    /**
     * 默认的Mapper，定义好一个标准处理json的方式，所有项目都使用这个来实现
     */
    public static JsonMapper defaultMapper() {

        return new JsonMapper(objectMapper());
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(final Object object) {

        try {
            return objectMapper.writeValueAsString(object);
        }
        catch (final IOException e) {
            log.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     *
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     *
     * @see #parse(String, JavaType)
     */
    public <T> T parse(final String jsonString, final Class<T> clazz) {
        if (CustomStringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, clazz);
        }
        catch (final IOException e) {
            log.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, contructCollectionType()或contructMapType()构造类型, 然后调用本函数.
     *
     */
    public <T> T parse(final String jsonString, final JavaType javaType) {
        if (CustomStringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) objectMapper.readValue(jsonString, javaType);
        }
        catch (final IOException e) {
            log.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 解析为列表集合
     * @param jsonString json字符串
     * @param type 列表集合item的类型
     * @return List集合数据
     */
    public <T> List<T> parseList(final String jsonString, final Class<T> type) {
        return parse(jsonString, buildCollectionType(List.class, type));
    }

    /**
     * 解析为数组对象
     * @return
     */
    public <T> T[] parseArray(final String jsonString, final Class<T> type) {
        return parse(jsonString, buildArrayType(type));
    }

    /**
     * 构造数组类型
     */
    public JavaType buildArrayType(final Class type) {
        return objectMapper.getTypeFactory().constructArrayType(type);
    }

    /**
     * 构造Collection类型.
     */
    public JavaType buildCollectionType(final Class<? extends Collection> collectionClass,
            final Class<?> elementClass) {
        return objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 构造Map类型.
     */
    public JavaType buildMapType(final Class<? extends Map> mapClass, final Class<?> keyClass,
            final Class<?> valueClass) {
        return objectMapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * 当JSON里只含有Bean的部分属性時，更新一個已存在Bean，只覆盖該部分的属性.
     */
    public void update(final String jsonString, final Object object) {
        try {
            objectMapper.readerForUpdating(object).readValue(jsonString);
        }
        catch (final JsonProcessingException e) {
            log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        catch (final IOException e) {
            log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(final String functionName, final Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public void enableEnumUseToString() {
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return objectMapper;
    }
}
