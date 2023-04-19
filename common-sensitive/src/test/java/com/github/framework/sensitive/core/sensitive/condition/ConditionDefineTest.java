package com.github.framework.sensitive.core.sensitive.condition;

import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.model.condition.SensitiveConditionPassword;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义条件测试
 */
public class ConditionDefineTest {

    /**
     * 条件测试
     */
    @Test
    public void conditionPasswordTest() {
        final String originalStr = "User{username='诸葛亮', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='诸*亮', idCard='123456**********34', password='123456', email='123**@qq.com', phone='188****8888'}";

        final SensitiveConditionPassword user = buildUser();
        final SensitiveConditionPassword sensitive = SensitiveUtils.desCopy(user);

        Assert.assertEquals(originalStr, user.toString());
//        Assert.assertEquals(sensitiveStr, sensitive.toString());
    }

    /**
     * 条件测试 JSON
     *
     */
    @Test
    public void conditionPasswordJsonTest() {
        final String originalStr = "User{username='诸葛亮', idCard='123456190001011234', password='123456', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveJson = "{\"email\":\"123**@qq.com\",\"idCard\":\"1234********011234\",\"password\":\"123456\",\"phone\":\"188****8888\",\"username\":\"诸*亮\"}";

        final SensitiveConditionPassword user = buildUser();

        System.out.println(SensitiveUtils.desJson(user));
        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(user));
        Assert.assertEquals(originalStr, user.toString());
    }

    /**
     * 构建测试用户对象
     * @return 创建后的对象
     * @since 0.0.1
     */
    private static SensitiveConditionPassword buildUser() {
        final SensitiveConditionPassword user = new SensitiveConditionPassword();
        user.setUsername("诸葛亮");
        user.setPassword("123456");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }
}
