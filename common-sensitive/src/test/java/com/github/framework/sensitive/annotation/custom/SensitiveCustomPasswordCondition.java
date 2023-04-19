package com.github.framework.sensitive.annotation.custom;

import com.github.framework.sensitive.annotation.metadata.SensitiveCondition;
import com.github.framework.sensitive.core.condition.ConditionFooPassword;

import java.lang.annotation.*;

/**
 * 自定义密码脱敏策略生效条件
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SensitiveCondition(ConditionFooPassword.class)
public @interface SensitiveCustomPasswordCondition {}
