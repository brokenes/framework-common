package com.github.framework.sensitive.annotation.strategy;

import com.github.framework.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.framework.sensitive.api.impl.SensitiveStrategyBuiltIn;

import java.lang.annotation.*;

/**
 * 手机号脱敏注解
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(SensitiveStrategyBuiltIn.class)
public @interface SensitiveStrategyEnglishName {
}
