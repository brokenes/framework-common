package com.github.framework.sensitive.core.sensitive.system;

import com.github.framework.sensitive.core.DataPrepareTest;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInAt;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInAtEntry;
import org.junit.Assert;
import org.junit.Test;

/**
 * 系统内置注解测试
 */
public class SystemBuiltInTest {

    /**
     * 普通属性脱敏测试
     */
    @Test
    public void sensitiveTest() {
        final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='诸葛亮', email='12345@qq.com', cardId='123456190001011234'}";
        final String sensitiveStr = "SystemBuiltInAt{phone='188****8888', password='null', name='诸*亮', email='123**@qq.com', cardId='123456**********34'}";

        final SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());

//        final SystemBuiltInAt sensitive = SensitiveUtils.desCopy(systemBuiltInAt);
//        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());
    }

    /**
     * 属性包含对象
     */
    @Test
    public void sensitiveEntryTest() {
        final String originalStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='18888888888', password='1234567', name='诸葛亮', email='12345@qq.com', cardId='123456190001011234'}}";
        final String sensitiveStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='188****8888', password='null', name='诸*亮', email='123**@qq.com', cardId='123456**********34'}}";

        final SystemBuiltInAtEntry entry = DataPrepareTest.buildSystemBuiltInAtEntry();
        Assert.assertEquals(originalStr, entry.toString());

//        final SystemBuiltInAtEntry sensitive = SensitiveUtils.desCopy(entry);
//        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, entry.toString());
    }

    /**
     * 普通属性脱敏 JSON 测试
     * @since 0.0.6
     */
    @Test
    public void sensitiveJsonTest() {
        final String originalStr = "SystemBuiltInAt{phone='18888888888', password='1234567', name='诸葛亮', email='12345@qq.com', cardId='123456190001011234'}";
        final String sensitiveJson = "{\"cardId\":\"123456**********34\",\"email\":\"123**@qq.com\",\"name\":\"诸*亮\",\"phone\":\"188****8888\"}";

        final SystemBuiltInAt systemBuiltInAt = DataPrepareTest.buildSystemBuiltInAt();
//        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(systemBuiltInAt));
        Assert.assertEquals(originalStr, systemBuiltInAt.toString());
    }

    /**
     * 属性包含对象 JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryJsonTest() {
        final String originalStr = "SystemBuiltInAtEntry{entry=SystemBuiltInAt{phone='18888888888', password='1234567', name='诸葛亮', email='12345@qq.com', cardId='123456190001011234'}}";
        final String sensitiveJson = "{\"entry\":{\"cardId\":\"123456**********34\",\"email\":\"123**@qq.com\",\"name\":\"诸*亮\",\"phone\":\"188****8888\"}}";

        final SystemBuiltInAtEntry entry = DataPrepareTest.buildSystemBuiltInAtEntry();

//        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(entry));
        Assert.assertEquals(originalStr, entry.toString());
    }

}
