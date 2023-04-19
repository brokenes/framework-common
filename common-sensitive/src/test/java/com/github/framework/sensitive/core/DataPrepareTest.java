package com.github.framework.sensitive.core;


import com.github.framework.sensitive.model.sensitive.User;
import com.github.framework.sensitive.model.sensitive.entry.UserCollection;
import com.github.framework.sensitive.model.sensitive.entry.UserEntryBaseType;
import com.github.framework.sensitive.model.sensitive.entry.UserEntryObject;
import com.github.framework.sensitive.model.sensitive.entry.UserGroup;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInAt;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInAtEntry;
import com.github.framework.sensitive.model.sensitive.system.SystemBuiltInMixed;

import java.util.*;



public final class DataPrepareTest {

    /**
     * 构建用户-属性为列表，列表中为基础属性
     * @return 构建嵌套信息
     * @since 0.0.2
     */
    public static UserEntryBaseType buildUserEntryBaseType() {
        UserEntryBaseType userEntryBaseType = new UserEntryBaseType();
        userEntryBaseType.setChineseNameList(Arrays.asList("盘古", "女娲", "伏羲"));
        userEntryBaseType.setChineseNameArray(new String[]{"盘古", "女娲", "伏羲"});
        return userEntryBaseType;
    }

    /**
     * 构建用户-属性为列表，数组。列表中为对象。
     * @return 构建嵌套信息
     * @since 0.0.2
     */
    public static UserEntryObject buildUserEntryObject() {
        UserEntryObject userEntryObject = new UserEntryObject();
        User user = buildUser();
        User user2 = buildUser();
        User user3 = buildUser();
        userEntryObject.setUser(user);
        userEntryObject.setUserList(Arrays.asList(user2));
        userEntryObject.setUserArray(new User[]{user3});
        return userEntryObject;
    }

    /**
     * 构建用户-属性为列表，数组，对象
     * @return 对象
     * @since 0.0.2
     */
    public static UserGroup buildUserGroup() {
        UserGroup userGroup = new UserGroup();
        User user = buildUser();
        User coolUser = buildUser();

        userGroup.setPassword("123456");
        userGroup.setCoolUser(coolUser);
        userGroup.setUser(user);
        userGroup.setUserCollection(Collections.singletonList(user));
        userGroup.setUserList(Arrays.asList(user));
        userGroup.setUserSet(new HashSet<User>(Arrays.asList(user)));
        Map<String, User> map = new HashMap<String, User>();
        map.put("map", user);
        userGroup.setUserMap(map);
        return userGroup;
    }

    /**
     * 构建测试用户对象
     * @return 创建后的对象
     * @since 0.0.1
     */
    public static User buildUser() {
        User user = new User();
        user.setUsername("诸葛亮");
        user.setPassword("1234567");
        user.setEmail("12345@qq.com");
        user.setIdCard("123456190001011234");
        user.setPhone("18888888888");
        return user;
    }

    /**
     * 构建系统内置对象
     * @return 构建后的对象
     * @since 0.0.3
     */
    public static SystemBuiltInAt buildSystemBuiltInAt() {
        SystemBuiltInAt systemBuiltInAt = new SystemBuiltInAt();
        systemBuiltInAt.setName("诸葛亮");
        systemBuiltInAt.setPassword("1234567");
        systemBuiltInAt.setEmail("12345@qq.com");
        systemBuiltInAt.setCardId("123456190001011234");
        systemBuiltInAt.setPhone("18888888888");
        return systemBuiltInAt;
    }

    /**
     * 构建系统内置对象
     * @return 构建后的对象
     * @since 0.0.3
     */
    public static SystemBuiltInAtEntry buildSystemBuiltInAtEntry() {
        SystemBuiltInAt systemBuiltInAt = buildSystemBuiltInAt();
        SystemBuiltInAtEntry systemBuiltInAtEntry = new SystemBuiltInAtEntry();
        systemBuiltInAtEntry.setEntry(systemBuiltInAt);
        return systemBuiltInAtEntry;
    }

    /**
     * 构建系统内置+Sensitive 注解混合测试
     * @return 混合
     * @since 0.0.3
     */
    public static SystemBuiltInMixed buildSystemBuiltInMixed() {
        SystemBuiltInMixed mixed = new SystemBuiltInMixed();
        mixed.setTestField("混合");
        return mixed;
    }

    /**
     * 构建用户-属性为列表，数组，对象、数组
     * @return 对象
     * @since 0.0.6
     */
    public static UserCollection buildUserCollection() {
        UserCollection userCollection = new UserCollection();
        User user = buildUser();

        userCollection.setUserCollection(Collections.singletonList(user));
        userCollection.setUserList(Arrays.asList(user));
        userCollection.setUserSet(new HashSet<User>(Arrays.asList(user)));
        userCollection.setUserArray(new User[]{user});
        Map<String, User> map = new HashMap<String, User>();
        map.put("map", user);
        userCollection.setUserMap(map);
        return userCollection;
    }

}
