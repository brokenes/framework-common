package com.github.framework.core.exception;

import com.github.framework.core.ErrorMessage;
import com.github.framework.core.lang.CustomStringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用法:throw Ex.business(
 */
public class Ex {
    /**
     * 创建一个业务异常
     * @param code
     * @param msg
     * @return
     */
    public static BusinessException business(String code,String msg,Object...params) {
        return new BusinessException(code,buildMessage(msg,params));
    }


    /**
     * 创建一个业务异常
     * @param errorMessage
     * @return
     */
    public static BusinessException business(ErrorMessage errorMessage) {
        String code = errorMessage.getCode();
        String msg = errorMessage.getMessage();
        String params = errorMessage.getErrorDetail();
        return new BusinessException(code,buildMessage(msg,params));
    }

    /**
     * 创建一个验证异常
     * @param msg
     * @return
     */
    public static ViolationException violation(String msg, Object...params) {
        return new ViolationException(buildMessage(msg,params));
    }


    public static CryptoException crypto(String msg,Object...params) {
        return new CryptoException(buildMessage(msg,params));
    }

    public static CryptoException crypto(Throwable throwable,String msg,Object...params) {
        String message = buildMessage(msg,params);
        if (CustomStringUtils.isNotBlank(message)) {
            return new CryptoException(message,throwable);
        }
        return new CryptoException(throwable);
    }

    /**
     * 创建一个系统错误异常
     * @param throwable
     * @param msg
     * @param params
     * @return
     */
    public static SystemErrorException systemError(Throwable throwable,String msg,Object...params) {
        String message = buildMessage(msg,params);
        if (CustomStringUtils.isNotBlank(message)) {
            return new SystemErrorException(message,throwable);
        }
        return new SystemErrorException(throwable);
    }

    /**
     * 创建一个系统错误异常
     * @param msg
     * @param params
     * @return
     */
    public static SystemErrorException systemError(String msg,Object...params) {
        String message = buildMessage(msg,params);
        return new SystemErrorException(message);
    }

    //--------------------------------------------exception处理----------------

    /**
     *
     * @Title: getStackTraceAsString
     * @Description: 将ErrorStack转化为String.
     * @param throwable
     * @return
     * @throws
     */
    public static String getStackTraceAsString(final Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     *
     * @Title: isCausedBy
     * @Description: 判断异常是否由某些底层的异常引起.
     * @param exception
     * @param causeExceptionClasses
     * @return
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(final Exception exception,
                                     final Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = exception.getCause();
        while (cause != null) {
            for (final Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     *
     * @Title: isCauseWlbankException
     * @Description: 判断是否是一个 BaseException 异常
     * @param exception
     * @return
     * @throws
     */
    public static boolean isCauseWlbankException(final Exception exception) {
        if (exception.getCause() != null && exception.getCause() instanceof BaseException) {
            return true;
        }
        return false;
    }

    /**
     * 构建格化式消息
     * @param msg 指定消息模板
     * @param params 指定消息模板参数
     * @return
     */
    private static String buildMessage(String msg,Object...params) {
        if (params == null || params.length <= 0) {
            return msg;
        }
        return String.format(msg,params);
    }

}
