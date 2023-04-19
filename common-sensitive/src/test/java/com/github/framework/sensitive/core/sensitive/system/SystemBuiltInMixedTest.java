package com.github.framework.sensitive.core.sensitive.system;

import com.github.framework.sensitive.core.DataPrepareTest;
import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInMixed;
import org.junit.Assert;
import org.junit.Test;

/**
 * 混合模式测试
 */
public class SystemBuiltInMixedTest {

    /**
     * 系统内置+Sensitive注解测试
     */
    @Test
    public void systemBuiltInAndSensitiveTest() {
        final String originalStr = "SystemBuiltInMixed{testField='混合'}";
        final String sensitiveStr = "SystemBuiltInMixed{testField='null'}";
        final SystemBuiltInMixed entry = DataPrepareTest.buildSystemBuiltInMixed();
        Assert.assertEquals(originalStr, entry.toString());

        final SystemBuiltInMixed sensitive = SensitiveUtils.desCopy(entry);
//        Assert.assertEquals(sensitiveStr, sensitive.toString());
//        Assert.assertEquals(originalStr, entry.toString());
    }

    /**
     * 系统内置+Sensitive注解测试JSON
     * @since 0.0.6
     */
    @Test
    public void systemBuiltInAndSensitiveJsonTest() {
        final String originalStr = "SystemBuiltInMixed{testField='混合'}";
        final String sensitiveJson = "{}";
        final SystemBuiltInMixed entry = DataPrepareTest.buildSystemBuiltInMixed();

//        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(entry));
//        Assert.assertEquals(originalStr, entry.toString());
    }

}
