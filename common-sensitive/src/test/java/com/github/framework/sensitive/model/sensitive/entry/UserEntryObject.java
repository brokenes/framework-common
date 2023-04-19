package com.github.framework.sensitive.model.sensitive.entry;

import com.github.framework.sensitive.annotation.SensitiveEntry;
import com.github.framework.sensitive.model.sensitive.User;

import java.util.Arrays;
import java.util.List;

/**
 * 对象中有列表，列表中放置的为对象
 */
public class UserEntryObject {

    @SensitiveEntry
    private User user;

    @SensitiveEntry
    private List<User> userList;

    @SensitiveEntry
    private User[] userArray;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(final List<User> userList) {
        this.userList = userList;
    }

    public User[] getUserArray() {
        return userArray;
    }

    public void setUserArray(final User[] userArray) {
        this.userArray = userArray;
    }

    @Override
    public String toString() {
        return "UserEntryObject{" + "user=" + user + ", userList=" + userList + ", userArray="
                + Arrays.toString(userArray) + '}';
    }
}
