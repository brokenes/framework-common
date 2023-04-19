package com.github.framework.sensitive.annotation.custom;

import com.github.framework.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.framework.sensitive.core.custom.CustomPasswordStrategy;

import java.lang.annotation.*;

/**
 * 自定义密码脱敏策略
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveStrategy(CustomPasswordStrategy.class)
public @interface SensitiveCustomPasswordStrategy {}
