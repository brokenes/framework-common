package com.github.framework.sensitive.core.util.strategy;

import com.github.framework.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.framework.sensitive.annotation.strategy.*;
import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.api.impl.SensitiveStrategyBuiltIn;
import com.github.framework.sensitive.core.api.strategory.*;
import com.github.framework.sensitive.core.exception.SensitiveRuntimeException;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.github.houbb.heaven.util.util.Optional;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统中内置的策略映射
 * 1. 注解和实现之间映射
 */
public final class SensitiveStrategyBuiltInUtils {

    private SensitiveStrategyBuiltInUtils() {}

    /**
     * 注解和实现策略的映射关系
     */
    private static final Map<Class<? extends Annotation>, IStrategy> MAP = new HashMap<Class<? extends Annotation>, IStrategy>();

    static {
        MAP.put(SensitiveStrategyPassword.class, new StrategyPassword());
        MAP.put(SensitiveStrategyPhone.class, new StrategyPhone());
        MAP.put(SensitiveStrategyChineseName.class, new StrategyChineseName());
        MAP.put(SensitiveStrategyAccountNo.class, new StrategyAccountNo());
        MAP.put(SensitiveStrategyCardNo.class, new StrategyCardNo());
        MAP.put(SensitiveStrategyCnID.class, new StrategyCnID());
        MAP.put(SensitiveStrategyHkID.class, new StrategyHkID());
        MAP.put(SensitiveStrategyMoID.class, new StrategyMoID());
        MAP.put(SensitiveStrategyID.class, new StrategyID());
        MAP.put(SensitiveStrategyCnPhone.class, new StrategyCnPhone());
        MAP.put(SensitiveStrategyHkPhone.class, new StrategyHkPhone());
        MAP.put(SensitiveStrategyMoPhone.class, new StrategyMoPhone());
        MAP.put(SensitiveStrategyEnglishName.class, new StrategyEnglishName());
    }

    /**
     * 获取对应的系统内置实现
     * @param annotationClass 注解实现类
     * @return 对应的实现方式
     */
    public static IStrategy require(final Class<? extends Annotation> annotationClass) {
        final IStrategy strategy = MAP.get(annotationClass);
        if (ObjectUtil.isNull(strategy)) {
            throw new SensitiveRuntimeException("不支持的系统内置方法，用户请勿在自定义注解中使用[SensitiveStrategyBuiltIn]!");
        }
        return strategy;
    }

    /**
     * 获取策略
     *
     * @param annotations 字段对应注解
     * @return 策略
     *
     */
    public static Optional<IStrategy> getStrategyOpt(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            final SensitiveStrategy sensitiveStrategy = annotation.annotationType()
                    .getAnnotation(SensitiveStrategy.class);
            if (ObjectUtil.isNotNull(sensitiveStrategy)) {
                final Class<? extends IStrategy> clazz = sensitiveStrategy.value();
                IStrategy strategy = null;
                if (SensitiveStrategyBuiltIn.class.equals(clazz)) {
                    strategy = SensitiveStrategyBuiltInUtils.require(annotation.annotationType());
                } else {
                    strategy = ClassUtil.newInstance(clazz);
                }
                return Optional.ofNullable(strategy);
            }
        }
        return Optional.empty();
    }

}
