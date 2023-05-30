package com.github.framework.core.lang;

import com.github.framework.core.IEnum;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Enums
 * @Description 根据Enum接口实现一些枚举操作的类
 */
public class Enums {

    private Enums() {}
    /**
     *
     * @Title: parse
     * @Description: 根据枚举类型的值，解析出枚举类
     * @param clazz
     * @param value
     * @return 枚举类
     */
    public static <T extends IEnum<V>, V> T parse(final Class<T> clazz, final V value) {
        if (value == null) {
            return null;
        }

        for (final T t : clazz.getEnumConstants()) {
            if (value.equals(t.value())) {
                return t;
            }
        }
        return null;
    }

    /**
     *
     * @Title: valueOfNamed
     * @Description: 根据枚举型定义的名称或枚举性的值 获取枚举型
     * @param clazz
     * @param named
     * @return 枚举类
     */
    public static <T extends IEnum<?>> T valueOfObject(final Class<T> clazz, final Object named) {
        if (named == null || StringUtils.isBlank(named.toString())) {

            return null;
        }
        // 需要继承枚举类型和我们自己的接口
        if (!(IEnum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz))) {

            return null;
        }

        final T[] arrEnums = clazz.getEnumConstants();
        if (arrEnums[0] instanceof Enum) {
            for (final T t : arrEnums) {
                final Enum<?> tmpEnum = (Enum<?>) t;
                if (named.equals(tmpEnum.name())) {
                    return t;
                }
            }
        }

        for (final T t : arrEnums) {
            if (named.equals(t.named()) || named.equals(t.value())) {
                return t;
            }
        }

        return null;
    }

    /**
     *
     * @Title: getEnumMapWithNameKey
     * @Description: name为Key获取 EnumMap
     * @param enumClazz
     */
    public static <T extends IEnum<?>> Map<String, T> getEnumMapWithNameKey(final Class<T> enumClazz) {
        final T[] arrEnums = enumClazz.getEnumConstants();
        final Map<String, T> tmpMap = new HashMap<>(arrEnums.length);
        if (isEmptyObject(arrEnums) == false && arrEnums[0] instanceof Enum) {
            for (final T t : arrEnums) {
                final Enum<?> tmpEnum = (Enum<?>) t;
                tmpMap.put(tmpEnum.name(), t);
            }
        }

        return tmpMap;
    }

    /**
     *
     * @Title: isEmptyObject
     * @Description: 空对象判断
     * @param object
     */
    public static boolean isEmptyObject(final Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StringUtils.isBlank((String) object);
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else {
            if (object.getClass().isArray()) {
                final int k = Array.getLength(object);
                for (int i = 0; i < k; i++) {
                    if (isEmptyObject(Array.get(object, i)) == false) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     *
     * @Title: getEnumMap
     * @Description: 使用枚举型的值返回MAP
     * @param enumClazz 枚举对象
     * @return 使用枚举的值作为Key，枚举作为Value的MAP
     */
    public static <T extends IEnum<?>> Map<Object, T> getEnumMap(final Class<T> enumClazz) {
        final T[] arrEnums = enumClazz.getEnumConstants();
        final Map<Object, T> tmpMap = new HashMap<>(arrEnums.length);
        for (final T t : arrEnums) {
            tmpMap.put(t.value(), t);
        }

        return tmpMap;
    }
}
