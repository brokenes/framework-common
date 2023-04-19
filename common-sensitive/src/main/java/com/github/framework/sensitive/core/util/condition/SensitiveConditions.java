package com.github.framework.sensitive.core.util.condition;

import com.github.framework.sensitive.annotation.metadata.SensitiveCondition;
import com.github.framework.sensitive.api.ICondition;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.github.houbb.heaven.util.util.Optional;

import java.lang.annotation.Annotation;


public final class SensitiveConditions {

    private SensitiveConditions(){}

    /**
     * 获取用户自定义条件
     *
     * @param annotations 字段上的注解
     * @return 对应的用户自定义条件
     *
     */
    public static Optional<ICondition> getConditionOpt(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveCondition sensitiveCondition = annotation.annotationType().getAnnotation(SensitiveCondition.class);
            if (ObjectUtil.isNotNull(sensitiveCondition)) {
                Class<? extends ICondition> customClass = sensitiveCondition.value();
                ICondition condition =  ClassUtil.newInstance(customClass);
                return Optional.ofNullable(condition);
            }
        }
        return Optional.empty();
    }

}
