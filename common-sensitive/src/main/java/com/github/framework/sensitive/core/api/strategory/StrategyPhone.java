package com.github.framework.sensitive.core.api.strategory;

import com.github.framework.sensitive.api.IContext;
import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.core.util.strategy.SensitiveStrategyUtils;
import com.github.houbb.heaven.util.lang.ObjectUtil;

import java.util.Objects;

/**
 * 手机号脱敏 大陆，香港，澳门通用
 * 脱敏规则
 *
 *
 */
public class StrategyPhone implements IStrategy {

    @Override
    public Object des(final Object original, final IContext context) {
        final String phone = ObjectUtil.objectToString(original);

        if (Objects.nonNull(phone)) {
            if (phone.length() > 8) {
                return SensitiveStrategyUtils.cnPhone(phone);
            } else {
                return SensitiveStrategyUtils.hkPhone(phone);
            }
        }
        return "";
    }

}
