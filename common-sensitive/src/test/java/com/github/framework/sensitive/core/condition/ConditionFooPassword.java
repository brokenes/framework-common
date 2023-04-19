package com.github.framework.sensitive.core.condition;

import com.github.framework.sensitive.api.ICondition;
import com.github.framework.sensitive.api.IContext;

import java.lang.reflect.Field;


public class ConditionFooPassword implements ICondition {
    @Override
    public boolean valid(final IContext context) {
        try {
            final Field field = context.getCurrentField();
            final Object currentObj = context.getCurrentObject();
            final String name = (String) field.get(currentObj);
            return !name.equals("123456");
        }
        catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
