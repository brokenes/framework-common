package com.github.framework.core.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * map处理工具类
 */
public class CustomMap {

    /**
     * 创建hashmpa的默认大小
     */
    public static final int DEFAULT_SIZE = 16;

    public static <K,V> boolean isEmpty(Map<K,V> m) {
        return m == null || m.isEmpty();
    }

    public static <K,V> boolean isNotEmpty(Map<K,V> m) {
        return !isEmpty(m);
    }

    public static <K,V>Map<K,V> hashmap() {
        return hashmap(DEFAULT_SIZE);
    }

    public static <K,V>Map<K,V> hashmap(int size) {
        return new HashMap<>(size);
    }

    /**
     * 根据一组可变参数的数组对象生成一个Map，用法如下：
     * <p>
     * <pre>
     * 	Maps.hashmap(key,value,key,value....);
     * </pre>
     *
     * @param keyValues 可变参数数，如果为单数，则最后一个被忽略,如果长度小于2,则返回Null
     * @return
     */
    public static <K, V> Map<K, V> hashmap(Object... keyValues) {
        if (keyValues != null && keyValues.length > 1) {
            Class<?> kClass = keyValues[0].getClass();
            Class<?> vClass = keyValues[1].getClass();
            return (Map<K, V>) hashmap(kClass, vClass, keyValues);
        }
        return null;
    }

    /**
     * 根据一组可变参数的数组对象生成一个Map，并且有强制类型化，用法如下：
     * <p>
     * <pre>
     * Maps.hashmap(key.class,value.class,key,value,key,value,key,value......);
     * </pre>
     *
     * @param kClass    key类型
     * @param vClass    value类型
     * @param keyValues 可变参数数，如果为单数，则最后一个被忽略,如果长度小于2,则返回Null
     * @return
     */
    public static <K, V> Map<K, V> hashmap(Class<K> kClass, Class<V> vClass, Object... keyValues) {
        Map<K, V> m = new HashMap<>(16);
        int i = 1;
        Object preObj = null;
        for (Object o : keyValues) {
            if (i % 2 == 0) {
                K k = kClass.cast(preObj);
                V v = vClass.cast(o);
                m.put(k, v);
            }
            preObj = o;
            i++;
        }
        return m;
    }

    /**
     * 根据一组可变参数的数组对象生成一个Map，但不会对K，V使用泛型，用法如下：
     * <p>
     * <pre>
     * Maps.hashmap(key,value,key,value,key,value......);
     * </pre>
     *
     * @param keyValues 可变参数数，如果为单数，则最后一个被忽略,如果长度小于2,则返回Null
     * @return
     */
    public static Map<Object, Object> hashmapAny(Object... keyValues) {
        Map<Object, Object> m = new HashMap<>(20);
        int i = 1;
        Object key = null;
        for (Object value : keyValues) {
            if (i % 2 == 0) {
                m.put(key, value);
            }
            key = value;
            i++;
        }
        return m;
    }

}
