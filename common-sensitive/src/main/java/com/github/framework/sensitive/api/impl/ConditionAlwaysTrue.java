package com.github.framework.sensitive.api.impl;

import com.github.framework.sensitive.api.ICondition;
import com.github.framework.sensitive.api.IContext;

/**
 * 一致返回真的条件
 */
public class ConditionAlwaysTrue implements ICondition {
    @Override
    public boolean valid(IContext context) {
        return true;
    }
}
