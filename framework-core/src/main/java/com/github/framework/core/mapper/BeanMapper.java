package com.github.framework.core.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.framework.core.collection.CustomList;
import com.github.framework.core.reflect.ClassWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * 
 * 1. 持有Mapper的单例.
 * 2. 返回值类型转换.
 * 3. 批量转换Collection中的所有对象.
 * 4.区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 *
 */
public class BeanMapper {

    private final static Logger log = LoggerFactory.getLogger(BeanMapper.class);

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static Mapper MAPPER = buildMapper();

    /**
     * 基于Dozer转换对象值映射与copy.
     * @param source 源对象
     * @param destinationClass 目标对象Class
     */
    public static <S, T> T map(final S source, final Class<T> destinationClass) {
        if (Objects.isNull(source)) {
            return null;
        }
        // 处理 hashmap -> hashmap
        T target = (T) map2map(source, destinationClass);
        if (target != null) {
            return target;
        }

        // 处理 bean -> hashmap
        target = (T) bean2map(source, destinationClass);
        if (target != null) {
            return target;
        }

        return MAPPER.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换对象值映射与copy.
     * @param source 源对象
     * @param destinationClass 目标对象Class
     * @param withOutFields 不需要copy的属性值
     * @author Sam
     */
    public static <S, T> T mapWithOut(final S source, final Class<T> destinationClass, final String... withOutFields) {
        if (Objects.isNull(source)) {
            return null;
        }
        if (withOutFields == null && withOutFields.length <= 0) {
            return map(source, destinationClass);
        }
        final Map<String, ?> middleValue = map(source, Map.class);
        for (final String withOutField : withOutFields) {
            middleValue.remove(withOutField);
        }
        return map(middleValue, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <S, T> List<T> mapList(final Collection<S> sourceList, final Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList();
        }
        final List<T> destinationList = CustomList.arrayList();
        for (final Object sourceObject : sourceList) {
            destinationList.add(map(sourceObject, destinationClass));
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(final Object source, final Object destinationObject) {
        if (Objects.isNull(source) || Objects.isNull(destinationObject)) {
            return;
        }

        if (map2map(source, destinationObject)) {
            return;
        }

        if (bean2map(source, destinationObject)) {
            return;
        }

        MAPPER.map(source, destinationObject);
    }

    /**
     * 构建Mapper
     * @return
     */
    private static Mapper buildMapper() {
        // 如果有自定义的mapping.xml文件则加载，否则直接new一个新的。
        try {

            return DozerBeanMapperBuilder.create().withMappingFiles("mapping.xml").build();
        }
        catch (final Exception e) {
            final DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
            return dozerBeanMapperBuilder.build();
        }
    }

    /**
     * hashmap 到 map的 copy
     * @param source
     * @param targetClass
     * @return
     */
    private static Object map2map(final Object source, final Class targetClass) {
        ClassWrapper targetType = ClassWrapper.wrap(targetClass);
        final boolean isMap2Map = (source instanceof Map) && (targetType.isOf(Map.class) || targetType.is(Map.class));
        if (isMap2Map) {
            if (targetType.is(Map.class)) {
                targetType = ClassWrapper.wrap(HashMap.class);
            }
            final Map targetMap = (Map) targetType.newOne();
            targetMap.putAll((Map) source);
            return targetMap;
        }
        return null;
    }

    /**
     * hashmap 到 map的 copy
     * @param source
     * @param target
     * @return
     */
    private static boolean map2map(final Object source, final Object target) {
        if (source instanceof Map && target instanceof Map) {
            ((Map) target).putAll(((Map) source));
            return true;
        }
        return false;
    }

    /**
     * bean -> hashmap copy
     * @param source
     * @param target
     * @return
     */
    private static boolean bean2map(final Object source, final Object target) {
        if (!(source instanceof Map) && target instanceof Map) {
            ((Map) target).putAll(BeanMap.create(source));
            return true;
        }
        return false;
    }

    /**
     * bean 到  hashmap 的转换
     * @param source
     * @param targetClass
     * @return
     */
    private static Object bean2map(final Object source, final Class targetClass) {
        ClassWrapper targetType = ClassWrapper.wrap(targetClass);
        if (!(source instanceof Map) && (targetType.isOf(Map.class) || targetType.is(Map.class))) {
            final BeanMap beanMap = BeanMap.create(source);
            if (targetType.is(Map.class)) {
                targetType = ClassWrapper.wrap(HashMap.class);
            }
            final Map targetMap = (Map) targetType.newOne();
            targetMap.putAll(beanMap);
            return targetMap;
        }
        return null;
    }

}