package com.github.framework.core;

public enum ErrorMsgEnum implements ErrorMessage{

    ADMIN_ERROR("01","错误","提交用户错误");


    private String code;
    private String message;
    private String errorDetail;

    ErrorMsgEnum(String code, String message, String errorDetail) {
        this.code = code;
        this.message = message;
        this.errorDetail = errorDetail;
    }

    @Override
    public String getErrorDetail() {
        return this.errorDetail;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
