package com.github.framework.sensitive.core.api;

import com.github.houbb.heaven.support.instance.impl.Instances;

/**
 * 脱敏工具类
 */
public final class SensitiveUtils {

    private SensitiveUtils(){}

    /**
     * 脱敏对象
     *
     * 每次都创建一个新的对象，避免线程问题
     * 可以使用 {@link ThreadLocal} 简单优化。
     * @param object 原始对象
     * @param <T> 泛型
     * @return 脱敏后的对象
     * @since 0.0.4 以前用的是单例。建议使用 spring 等容器管理 ISensitive 实现。
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T desCopy(T object) {
        return (T) Instances.singletion(SensitiveService.class)
                .desCopy(object);
    }

    /**
     * 返回脱敏后的对象 json
     * null 对象，返回字符串 "null"
     * @param object 对象
     * @return 结果 json
     * @since 0.0.6
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String desJson(Object object) {
        return Instances.singletion(SensitiveService.class)
                .desJson(object);
    }

}
