package com.github.framework.sensitive.annotation;

import com.github.framework.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.framework.sensitive.api.impl.SensitiveStrategyBuiltIn;

import java.lang.annotation.*;

/**
 * 模拟用户使用禁止使用的 SensitiveStrategyBuiltIn 来声明策略
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(SensitiveStrategyBuiltIn.class)
public @interface SensitiveErrorSystemBuiltIn {}
