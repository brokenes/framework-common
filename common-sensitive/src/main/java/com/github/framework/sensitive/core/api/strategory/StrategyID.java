package com.github.framework.sensitive.core.api.strategory;

import com.github.framework.sensitive.api.IContext;
import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.core.util.strategy.SensitiveStrategyUtils;
import com.github.houbb.heaven.util.lang.ObjectUtil;

import java.util.Objects;

/**
 * 身份证件号脱敏 大陆，香港，澳门通用
 * 脱敏规则
 *
 */
public class StrategyID implements IStrategy {

    @Override
    public Object des(final Object original, final IContext context) {
        final String id = ObjectUtil.objectToString(original);

        if (Objects.nonNull(id)) {
            if (id.length() > 8) {
                return SensitiveStrategyUtils.cnID(id);
            } else {
                return SensitiveStrategyUtils.hkID(id);
            }
        }
        return "";
    }

}
