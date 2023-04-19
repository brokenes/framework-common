package com.github.framework.sensitive.core.sensitive.custom;

import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.model.custom.CustomPasswordEntryModel;
import com.github.framework.sensitive.model.custom.CustomPasswordModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义注解测试
 */
public class CustomAnnotationTest {

    /**
     * 自定义注解测试
     */
    @Test
    public void customAnnotationTest() {
        final String originalStr = "CustomPasswordModel{password='hello', fooPassword='123456'}";
        final String sensitiveStr = "CustomPasswordModel{password='**********************', fooPassword='123456'}";
        final CustomPasswordModel model = buildCustomPasswordModel();
        Assert.assertEquals(originalStr, model.toString());

        final CustomPasswordModel sensitive = SensitiveUtils.desCopy(model);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试
     */
    @Test
    public void customAnnotationEntryTest() {
        final String originalStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='hello', fooPassword='123456'}}";
        final String sensitiveStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='**********************', fooPassword='123456'}}";
        final CustomPasswordModel entry = buildCustomPasswordModel();
        final CustomPasswordEntryModel model = new CustomPasswordEntryModel();
        model.setEntry(entry);

        Assert.assertEquals(originalStr, model.toString());

        final CustomPasswordEntryModel sensitive = SensitiveUtils.desCopy(model);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试 JSON
     * @since 0.0.6
     */
    @Test
    public void customAnnotationJsonTest() {
        final String originalStr = "CustomPasswordModel{password='hello', fooPassword='123456'}";
        final String sensitiveJson = "{\"fooPassword\":\"123456\",\"password\":\"**********************\"}";
        final CustomPasswordModel model = buildCustomPasswordModel();

        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(model));
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 自定义注解测试 JSON
     * @since 0.0.6
     */
    @Test
    public void customAnnotationEntryJsonTest() {
        final String originalStr = "CustomPasswordEntryModel{entry=CustomPasswordModel{password='hello', fooPassword='123456'}}";
        final String sensitiveJson = "{\"entry\":{\"fooPassword\":\"123456\",\"password\":\"**********************\"}}";
        final CustomPasswordModel entry = buildCustomPasswordModel();
        final CustomPasswordEntryModel model = new CustomPasswordEntryModel();
        model.setEntry(entry);

        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(model));
        Assert.assertEquals(originalStr, model.toString());
    }

    /**
     * 构建自定义密码对象
     * @return 对象
     */
    private CustomPasswordModel buildCustomPasswordModel() {
        final CustomPasswordModel model = new CustomPasswordModel();
        model.setPassword("hello");
        model.setFooPassword("123456");
        return model;
    }

}
