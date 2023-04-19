package com.github.framework.sensitive.core.util;


import com.github.framework.core.mapper.BeanMapper;

/**
 * bean 工具类
 *
 * 如果需要属性拷贝，可以参考：
 * [bean-mapping](https://github.com/houbb/bean-mapping)
 */
public final class BeanUtil {

    private BeanUtil() {}

    /**
     * 深度复制
     * 1. 为了避免深拷贝要求用户实现 clone 和 序列化的相关接口
     * 2. 为了避免使用 dozer 这种比较重的工具
     * 3. 自己实现暂时工作量比较大
     *
     * 暂时使用 fastJson 作为实现深度拷贝的方式
     * @param object 对象
     * @param <T> 泛型
     * @return 深拷贝后的对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T deepCopy(final T object) {
        /*         final Class clazz = object.getClass();
         final String jsonString = JSON.toJSONString(object);
         return (T) JSON.parseObject(jsonString, clazz);*/
        // 直接使用 dozer
        return (T) BeanMapper.map(object, object.getClass());
    }

}
