package com.github.framework.core.lang;


import com.github.framework.core.exception.Ex;
import com.github.framework.core.exception.IError;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类，用于断言程序中的参数，确保参数的可用性，避免NPE
 *
 */
public class CheckUtils {

    private CheckUtils() {}

    /**
     * 断言值为true
     * @param isTrue 被断言的值
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isTrue(final boolean isTrue,final String message,final Object...params) {
        if (!isTrue) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言值为true
     * @param isTrue 被断言的值
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isTrue(final boolean isTrue, final IError error, final Object...params) {
        if (!isTrue) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言对象为null
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isNull(final Object object,final String message,final Object...params) {
        if (object != null) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言对象为null
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isNull(final Object object,final IError error,final Object...params) {
        if (object != null) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言对象不为null
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notNull(final Object object,final String message,final Object...params) {
        if (object == null) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言对象不为null
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notNull(final Object object,final IError error,final Object...params) {
        if (object == null) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言String对象不为空
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void hasText(final String object,final String message,final Object...params) {
        if (CustomStringUtils.isBlank(object)) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言String对象不为空
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void hasText(final String object,final IError error,final Object...params) {
        if (CustomStringUtils.isBlank(object)) {
            throw error.makeException(params);
        }
    }


    /**
     * 断言集合对象不为空
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Collection object, final String message, final String...params) {
        if (CollectionUtils.isEmpty(object)) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言集合对象不为空
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Collection object, final IError error, final String...params) {
        if (CollectionUtils.isEmpty(object)) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言Map对象不为空
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Map object, final String message, final String...params) {
        if (CollectionUtils.isEmpty(object)) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言Map对象不为空
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Map object, final IError error, final String...params) {
        if (CollectionUtils.isEmpty(object)) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言数组对象不为空
     * @param array 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Object[] array, final String message,final String...params) {
        if (array == null || array.length == 0) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言数组对象不为空
     * @param array 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void notEmpty(final Object[] array, final IError error,final String...params) {
        if (array == null || array.length == 0) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言某字符串为邮箱格式
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isEmail(final String object,final String message,final String...params) {
        if (!CustomStringUtils.isEmail(object)) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言某字符串为邮箱格式
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isEmail(final String object,final IError error,final String...params) {
        if (!CustomStringUtils.isEmail(object)) {
            throw error.makeException(params);
        }
    }

    /**
     * 断言某字符串为手机格式
     * @param object 被断言的对象
     * @param message 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isMobile(final String object,final String message,final String...params) {
        if (!CustomStringUtils.isMobile(object)) {
            throw Ex.violation(message,params);
        }
    }

    /**
     * 断言某字符串为手机格式
     * @param object 被断言的对象
     * @param error 不符合断言要抛出的异常消息
     * @param params 不符合断言要抛出的异常消息参数
     */
    public static void isMobile(final String object,final IError error,final String...params) {
        if (!CustomStringUtils.isMobile(object)) {
            throw error.makeException(params);
        }
    }

}
