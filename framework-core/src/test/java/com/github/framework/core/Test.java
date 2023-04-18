package com.github.framework.core;

public class Test {

    public static void main(String[] args) {
        Result result = Result.fail(ErrorMsgEnum.ADMIN_ERROR);

        System.out.println(result.getCode() + "---" + result.getErrorDetail());
    }
}
