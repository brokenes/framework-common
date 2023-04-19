package com.github.framework.sensitive.core.util.strategy;

import com.github.houbb.heaven.constant.PunctuationConst;
import com.github.houbb.heaven.util.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏策略工具类
 * （1）提供常见的脱敏策略
 * （2）主要供单独的字符串处理使用
 */
public final class SensitiveStrategyUtils {

    private SensitiveStrategyUtils() {}

    /**
     * 脱敏密码
     * @param password 原始密码
     * @return 结果
     */
    public static String password(final String password) {
        return null;
    }

    /**
     * 脱敏电话号码
     * @param phone 电话号码
     * @return 结果
     */
    public static String phone(final String phone) {
        final int prefixLength = 3;
        final String middle = "****";
        return StringUtil.buildString(phone, middle, prefixLength);
    }

    /**
     * 脱敏邮箱
     * @param email 邮箱
     * @return 结果
     */
    public static String email(final String email) {
        if (StringUtil.isEmpty(email)) {
            return null;
        }

        final int prefixLength = 3;

        final int atIndex = email.indexOf(PunctuationConst.AT);
        String middle = "****";

        if (atIndex > 0) {
            final int middleLength = atIndex - prefixLength;
            middle = StringUtil.repeat(PunctuationConst.STAR, middleLength);
        }
        return StringUtil.buildString(email, middle, prefixLength);
    }

    /**
     * 脱敏中文名称
     * @param chineseName 中文名称
     * @return 脱敏后的结果
     */
    public static String chineseName(final String chineseName) {
        if (StringUtil.isEmpty(chineseName)) {
            return chineseName;
        }

        final int nameLength = chineseName.length();
        if (1 == nameLength) {
            return chineseName;
        }

        if (2 == nameLength) {
            return PunctuationConst.STAR + chineseName.charAt(1);
        }

        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(chineseName.charAt(0));
        for (int i = 0; i < nameLength - 2; i++) {
            stringBuffer.append(PunctuationConst.STAR);
        }
        stringBuffer.append(chineseName.charAt(nameLength - 1));
        return stringBuffer.toString();
    }

    /**
     * 
     * @Title: englishName 
     * @Description: 英文名脱敏
     * @param englishName
     *
     */
    public static String englishName(final String englishName) {
        if (englishName.length() < 5) {
            return englishName;
        }

        final String start = StringUtils.substring(englishName, 0, 2);
        final String end = StringUtils.substring(englishName, englishName.length() - 2, englishName.length());
        final String result = englishName.substring(2, englishName.length() - 2).replaceAll("[^ ]", "*");

        return start + result + end;
    }

    /**
     * 脱敏卡号
     * @param cardId 卡号
     * @return 脱敏结果
     */
    public static String cardId(final String cardId) {
        final int prefixLength = 6;
        final String middle = "**********";
        return StringUtil.buildString(cardId, middle, prefixLength);
    }

    /**
     * 
     * @Title: accountNo 
     * @Description: 脱敏账号
     * @param accountNo
     */
    public static String accountNo(final String accountNo) {
        // 12位账户号
        final int prefixLength = 1;
        final String middle = "*****";
        return StringUtil.buildString(accountNo, middle, prefixLength);
    }

    /**
     * 
     * @Title: cardNo 
     * @Description: 脱敏卡号
     *
     */
    public static String cardNo(final String accountNo) {
        // 16位卡号
        final int prefixLength = 4;
        final String middle = "********";
        return StringUtil.buildString(accountNo, middle, prefixLength);
    }

    /**
     * 
     * @Title: customerNo 
     * @Description: 脱敏客户号
     *
     */
    public static String customerNo(final String accountNo) {
        // 10位客户号
        final int prefixLength = 2;
        final String middle = "****";
        return StringUtil.buildString(accountNo, middle, prefixLength);
    }

    /** 
     * @Title: cnPhone 
     * @Description: 脱敏大陆电话号码
     *
     */
    public static String cnPhone(final String phone) {
        final int prefixLength = 3;
        final String middle = "****";
        return StringUtil.buildString(phone, middle, prefixLength);
    }

    /**
     * 脱敏香港电话号码
     * @param phone 电话号码
     * @return 结果
     */
    public static String hkPhone(final String phone) {
        final int prefixLength = 2;
        final String middle = "****";
        return StringUtil.buildString(phone, middle, prefixLength);
    }

    /**
     * 脱敏澳门电话号码
     * @param phone 电话号码
     * @return 结果
     */
    public static String moPhone(final String phone) {
        final int prefixLength = 2;
        final String middle = "****";
        return StringUtil.buildString(phone, middle, prefixLength);
    }

    /**
     * 脱敏大陆身份证号
     * @param 身份证号
     * @return 结果
     */
    public static String cnID(final String id) {
        final int prefixLength = 10;
        final String middle = "******";
        return StringUtil.buildString(id, middle, prefixLength);
    }

    /**
     * 脱敏香港身份证号
     * @param 身份证号
     * @return 结果
     */
    public static String hkID(final String id) {
        final int prefixLength = 3;
        final String middle = "***";
        return StringUtil.buildString(id, middle, prefixLength);
    }

    /**
     * 脱敏澳门身份证号
     * @param 身份证号
     * @return 结果
     */
    public static String moID(final String id) {
        final int prefixLength = 3;
        final String middle = "***";
        return StringUtil.buildString(id, middle, prefixLength);
    }

}
