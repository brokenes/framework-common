package com.github.framework.sensitive.model.sensitive.system;

import com.github.framework.sensitive.annotation.Sensitive;
import com.github.framework.sensitive.annotation.strategy.SensitiveStrategyChineseName;
import com.github.framework.sensitive.core.api.strategory.StrategyPassword;

/**
 * 系统内置+Sensitive注解混合模式
 */
public class SystemBuiltInMixed {

    /**
     * 测试字段
     * 1.当多种注解混合的时候，为了简化逻辑，优先选择 @Sensitive 注解。
     */
    @SensitiveStrategyChineseName
    @Sensitive(strategy = StrategyPassword.class)
    private String testField;

    public String getTestField() {
        return testField;
    }

    public void setTestField(final String testField) {
        this.testField = testField;
    }

    @Override
    public String toString() {
        return "SystemBuiltInMixed{" + "testField='" + testField + '\'' + '}';
    }

}
