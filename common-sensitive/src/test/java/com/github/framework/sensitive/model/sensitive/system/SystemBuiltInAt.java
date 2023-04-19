package com.github.framework.sensitive.model.sensitive.system;

import com.github.framework.sensitive.annotation.strategy.*;


public class SystemBuiltInAt {

    @SensitiveStrategyPhone
    private String phone;

    @SensitiveStrategyPassword
    private String password;

    @SensitiveStrategyChineseName
    private String name;

    @SensitiveStrategyEmail
    private String email;

    @SensitiveStrategyCardNo
    private String cardId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(final String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "SystemBuiltInAt{" + "phone='" + phone + '\'' + ", password='" + password + '\'' + ", name='" + name
                + '\'' + ", email='" + email + '\'' + ", cardId='" + cardId + '\'' + '}';
    }
}
