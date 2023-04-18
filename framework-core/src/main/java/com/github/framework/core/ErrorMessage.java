package com.github.framework.core;

/**
 * 错误消息接口定义，所有异常信息都要实现此接口
 */
public interface ErrorMessage extends Message<String>{

    /**
     * 返回错误明细信息
     * @return String 错误消息
     */
    String getErrorDetail();

    String DEFAULT_ERROR_CODE = "0001";

    String DEFAULT_ERROR_MESSAGE = "系统内部错误！";
}
