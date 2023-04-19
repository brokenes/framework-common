package com.github.framework.sensitive.core.api.strategory;

import com.github.framework.sensitive.api.IContext;
import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.core.util.strategy.SensitiveStrategyUtils;
import com.github.houbb.heaven.util.lang.ObjectUtil;

/**
 * 邮箱脱敏策略
 * 脱敏规则：
 * 保留前三位，中间隐藏4位。其他正常显示
 */
public class StrategyEmail implements IStrategy {

    @Override
    public Object des(Object original, IContext context) {
        return SensitiveStrategyUtils.email(ObjectUtil.objectToString(original));
    }

}
