package com.github.framework.core.exception;

import com.github.framework.core.IEnum;

/**
 * 错误消息码
 */
public enum ErrorCode implements IEnum<String> {
    /**
     * 认证错误编码信息
     */
    AUTH("认证异常","3000"),

    BUSINESS("业务异常","5000"),

    CRYPTO("加密解密或加签验证异常","9005"),

    NETWORK("网络异常","9103"),

    PERMISSION("没有权限","5100"),

    SYSTEM("系统错误","9002"),

    VIOLATION("验证异常","9001"),

    PERSISTANCE("持久化异常","9003"),

    TIMEOUT("服务超时异常","9999"),



    ;
    String named;
    String value;

    ErrorCode(String name,String value) {
        this.named = name;
        this.value = value;
    }


    @Override
    public String named() {
        return named;
    }

    @Override
    public String value() {
        return value;
    }

    public static final String BASE_LAYER_ERROR_CODE ="00009999";

    public static final String BASE_LAYER_APP_CODE = "0000";
}
