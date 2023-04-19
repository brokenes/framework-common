package com.github.framework.sensitive.model.condition;

import com.github.framework.sensitive.annotation.Sensitive;
import com.github.framework.sensitive.core.api.strategory.*;
import com.github.framework.sensitive.core.condition.ConditionFooPassword;


public class SensitiveConditionPassword {

    @Sensitive(strategy = StrategyChineseName.class)
    private String username;

    @Sensitive(strategy = StrategyCardNo.class)
    private String idCard;

    @Sensitive(condition = ConditionFooPassword.class, strategy = StrategyPassword.class)
    private String password;

    @Sensitive(strategy = StrategyEmail.class)
    private String email;

    @Sensitive(strategy = StrategyPhone.class)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(final String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", idCard='" + idCard + '\'' + ", password='" + password
                + '\'' + ", email='" + email + '\'' + ", phone='" + phone + '\'' + '}';
    }

}
