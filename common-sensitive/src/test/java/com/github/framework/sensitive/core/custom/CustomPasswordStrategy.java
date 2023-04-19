package com.github.framework.sensitive.core.custom;

import com.github.framework.sensitive.api.IContext;
import com.github.framework.sensitive.api.IStrategy;


public class CustomPasswordStrategy implements IStrategy {

    @Override
    public Object des(final Object original, final IContext context) {
        return "####################";
    }

}
