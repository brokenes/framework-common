package com.github.framework.sensitive.core.sensitive.system;

import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.model.sensitive.system.SensitiveErrorSystemBuiltInModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * 错误的使用系统内置类测试
 */
public class SensitiveErrorSystemBiTest {

    /**
     * 错误的使用系统的内置类
     */
    @Test
    public void errorSystemBuiltInTest() {
        try {
            final SensitiveErrorSystemBuiltInModel model = new SensitiveErrorSystemBuiltInModel();
            final SensitiveErrorSystemBuiltInModel copy = SensitiveUtils.desCopy(model);
        }
        catch (final Exception e) {
            Assert.assertEquals("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!", e.getMessage());
        }
    }

    /**
     * 错误的使用系统的内置类JSON
     * @since 0.0.6
     */
    @Test
    public void errorSystemBuiltInJsonTest() {
        try {
            final SensitiveErrorSystemBuiltInModel model = new SensitiveErrorSystemBuiltInModel();
            final String json = SensitiveUtils.desJson(model);
        }
        catch (final Exception e) {
            Assert.assertEquals("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!", e.getMessage());
        }
    }

}
