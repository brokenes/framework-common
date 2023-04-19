package com.github.framework.sensitive.model.sensitive.entry;

import com.github.framework.sensitive.annotation.SensitiveEntry;
import com.github.framework.sensitive.model.sensitive.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UserCollection {

    @SensitiveEntry
    private User[] userArray;

    @SensitiveEntry
    private List<User> userList;

    @SensitiveEntry
    private Set<User> userSet;

    @SensitiveEntry
    private Collection<User> userCollection;

    /**
     * SensitiveEntry 注解不会生效
     */
    @SensitiveEntry
    private Map<String, User> userMap;

    public User[] getUserArray() {
        return userArray;
    }

    public void setUserArray(final User[] userArray) {
        this.userArray = userArray;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(final List<User> userList) {
        this.userList = userList;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(final Set<User> userSet) {
        this.userSet = userSet;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(final Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(final Map<String, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public String toString() {
        return "UserCollection{" + "userList=" + userList + ", userSet=" + userSet + ", userCollection="
                + userCollection + ", userMap=" + userMap + '}';
    }

}
