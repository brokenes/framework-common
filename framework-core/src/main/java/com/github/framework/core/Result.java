package com.github.framework.core;

import lombok.Data;

import java.io.Serializable;

/***
 * 通用结构集数据结构，每个结果都有一个成功或者失败的标记状态
 *
 */

@Data
public class Result<T> implements Message<String>, Serializable {
    /**
     * 默认成功代码
     */
    public static final String DEFAULT_SUCCESS_CODE = "00000000";

    /**
     * 默认成功消息
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "success";

    /**
     * 业务编号或错误编码,当success为false时,code不能为空,代表的是错误编码
     * success为true时,也可以设置code,具体看场景和业务需求
     */
    protected String code;

    /**
     * 业务消息或错误消息,当success为false时,message不能为空,代表的是错误消息
     * success为true时,也可以设置message,具体看场景和业务需求
     */
    protected String message;

    /**
     * 请求状态
     * true-成功
     * false-失败
     */
    protected boolean success;

    /**
     * 结果集数据
     */
    protected T data;

    /**
     * 当结果集为错误结果集时，传输的错误明细
     */
    protected String errorDetail;

    /**
     * 创建一个空的成功结果
     *
     * @return
     */
    public static Result ok() {
        return ok(null);
    }

    /**
     * 创建一个含有data数据的结果对象
     *
     * @param data 结果集数据对象,可以是任意对象
     * @param <T>  结果集数据对象类型
     * @return DataResult
     */
    public static <T> Result<T> ok(final T data) {
        return ok(data, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 创建一个含有data数据的结果对象
     *
     * @param data 结果集数据对象,可以是任意对象
     * @param <T>  结果集数据对象类型
     * @return DataResult
     */
    public static <T> Result<T> ok(final T data, final String code, final String message) {
        final Result<T> result = new Result<>();
        result.setData(data);
        result.setSuccess(true);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 创建一个错误的结果集
     *
     * @return
     */
    public static Result fail() {
        return fail(ErrorMessage.DEFAULT_ERROR_CODE, ErrorMessage.DEFAULT_ERROR_MESSAGE, null);
    }

    /**
     * 创建一个错误的结果集
     *
     * @param message 错误消息
     * @return
     */
    public static Result fail(final String message) {
        return fail(ErrorMessage.DEFAULT_ERROR_CODE, message);
    }

    /**
     * 创建一个错误的结果集
     *
     */
    public static Result fail(ErrorMessage error){
        String code = error.getCode();
        String errorMsg = error.getMessage();
        String errorDetailMsg = error.getErrorDetail();
        return fail(code, errorMsg,errorDetailMsg);
    }



    /**
     * 创建一个错误的结果集
     *
     * @param code    错误编码
     * @param message 错误消息
     * @return
     */
    public static Result fail(final String code, final String message) {
        return fail(code, message, null);
    }

    /**
     * 创建一个错误的结果集
     *
     * @param code    错误编码
     * @param message 错误消息
     * @param detail  错误消息明细信息
     * @return ErrorResult
     */
    public static Result fail(final String code, final String message, final String detail) {
        final Result errorResult = new Result();
        errorResult.setCode(code);
        errorResult.setMessage(message);
        errorResult.setErrorDetail(detail);
        errorResult.setSuccess(false);
        return errorResult;
    }
}