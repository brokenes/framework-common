package com.github.framework.sensitive.core.sensitive;

import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.core.DataPrepareTest;
import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.core.api.strategory.StrategyEmail;
import com.github.framework.sensitive.model.sensitive.User;
import org.junit.Assert;
import org.junit.Test;


public class SensitiveTest {

    /**
     * 单个属性测试
     * @since 0.0.1
     */
    @Test
    public void singleSensitiveTest() {
        final String email = "123456@qq.com";
        final String exceptResult = "123***@qq.com";
        final IStrategy strategy = new StrategyEmail();
        final String emailSensitive = (String) strategy.des(email, null);
//        Assert.assertEquals(exceptResult, emailSensitive);
    }

    /**
     * 普通脱敏测试
     * @since 0.0.1
     */
    @Test
    public void commonSensitiveTest() {
        final String originalStr = "User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}";
        final String sensitiveStr = "User{username='诸*亮', idCard='1234********011234', password='null', email='123**@qq.com', phone='188****8888'}";

        final User user = DataPrepareTest.buildUser();
        Assert.assertEquals(originalStr, user.toString());

        final User sensitiveUser = SensitiveUtils.desCopy(user);
//        System.out.println(sensitiveUser.toString());
//        Assert.assertEquals(sensitiveStr, sensitiveUser.toString());
        Assert.assertEquals(originalStr, user.toString());
    }

}
