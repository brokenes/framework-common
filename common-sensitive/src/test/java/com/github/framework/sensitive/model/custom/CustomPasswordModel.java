package com.github.framework.sensitive.model.custom;

import com.github.framework.sensitive.annotation.custom.SensitiveCustomPasswordCondition;
import com.github.framework.sensitive.annotation.custom.SensitiveCustomPasswordStrategy;
import com.github.framework.sensitive.annotation.strategy.SensitiveStrategyPassword;


public class CustomPasswordModel {

    @SensitiveCustomPasswordCondition
    @SensitiveCustomPasswordStrategy
    private String password;

    @SensitiveCustomPasswordCondition
    @SensitiveStrategyPassword
    private String fooPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFooPassword() {
        return fooPassword;
    }

    public void setFooPassword(final String fooPassword) {
        this.fooPassword = fooPassword;
    }

    @Override
    public String toString() {
        return "CustomPasswordModel{" + "password='" + password + '\'' + ", fooPassword='" + fooPassword + '\'' + '}';
    }
}
