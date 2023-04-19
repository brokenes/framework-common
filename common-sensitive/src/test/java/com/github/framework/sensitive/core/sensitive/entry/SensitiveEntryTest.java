package com.github.framework.sensitive.core.sensitive.entry;

import com.alibaba.fastjson.JSON;
import com.github.framework.sensitive.core.DataPrepareTest;
import com.github.framework.sensitive.core.api.SensitiveUtils;
import com.github.framework.sensitive.model.sensitive.entry.UserCollection;
import com.github.framework.sensitive.model.sensitive.entry.UserEntryBaseType;
import com.github.framework.sensitive.model.sensitive.entry.UserEntryObject;
import com.github.framework.sensitive.model.sensitive.entry.UserGroup;
import org.junit.Assert;
import org.junit.Test;

/**
 * SensitiveEntry 注解-脱敏测试类
 */
public class SensitiveEntryTest {

    /**
     * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveEntryBaseTypeTest() {
        final String originalStr = "UserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}";
        final String sensitiveStr = "UserEntryBaseType{chineseNameList=[*古, *娲, *羲], chineseNameArray=[*古, *娲, *羲]}";

        final UserEntryBaseType userEntryBaseType = DataPrepareTest.buildUserEntryBaseType();
        Assert.assertEquals(originalStr, userEntryBaseType.toString());

        final UserEntryBaseType sensitive = SensitiveUtils.desCopy(userEntryBaseType);
        Assert.assertEquals(sensitiveStr, sensitive.toString());
        Assert.assertEquals(originalStr, userEntryBaseType.toString());
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveEntryObjectTest() {
        final String originalStr = "UserEntryObject{user=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}";
        final String sensitiveStr = "UserEntryObject{user=User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, userList=[User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userArray=[User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}]}";

        final UserEntryObject userEntryObject = DataPrepareTest.buildUserEntryObject();
        Assert.assertEquals(originalStr, userEntryObject.toString());

        final UserEntryObject sensitiveUserEntryObject = SensitiveUtils.desCopy(userEntryObject);
//        Assert.assertEquals(sensitiveStr, sensitiveUserEntryObject.toString());
        Assert.assertEquals(originalStr, userEntryObject.toString());
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试
     * @since 0.0.2
     */
    @Test
    public void sensitiveUserGroupTest() {
        final String originalStr = "UserGroup{coolUser=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], password='123456', userMap={map=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
        final String sensitiveStr = "UserGroup{coolUser=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, user=User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}, userList=[User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userSet=[User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], userCollection=[User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}], password='123456', userMap={map=User{username='诸*亮', idCard='123456**********34', password='null', email='123**@qq.com', phone='188****8888'}}}";

        final UserGroup userGroup = DataPrepareTest.buildUserGroup();
        Assert.assertEquals(originalStr, userGroup.toString());

        final UserGroup sensitiveUserGroup = SensitiveUtils.desCopy(userGroup);
//        Assert.assertEquals(sensitiveStr, sensitiveUserGroup.toString());
        Assert.assertEquals(originalStr, userGroup.toString());
    }

    /**
     * 用户属性中有集合或者map，集合中属性是基础类型-脱敏测试-JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryBaseTypeJsonTest() {
        final String originalStr = "UserEntryBaseType{chineseNameList=[盘古, 女娲, 伏羲], chineseNameArray=[盘古, 女娲, 伏羲]}";
        final String sensitiveJson = "{\"chineseNameArray\":[\"*古\",\"*娲\",\"*羲\"],\"chineseNameList\":[\"*古\",\"*娲\",\"*羲\"]}";

        final UserEntryBaseType userEntryBaseType = DataPrepareTest.buildUserEntryBaseType();

        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(userEntryBaseType));
        Assert.assertEquals(originalStr, userEntryBaseType.toString());
    }

    /**
     * 用户属性中有集合或者对象，集合中属性是对象-脱敏测试-JSON
     * @since 0.0.6
     */
    @Test
    public void sensitiveEntryObjectJsonTest() {
        final String originalStr = "UserEntryObject{user=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}, userList=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userArray=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}]}";
        final String sensitiveJson = "{\"user\":{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"诸*亮\"},\"userArray\":[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"诸*亮\"}],\"userList\":[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"诸*亮\"}]}";

        final UserEntryObject userEntryObject = DataPrepareTest.buildUserEntryObject();

//        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(userEntryObject));
        Assert.assertEquals(originalStr, userEntryObject.toString());
    }

    /**
     * 用户属性中有集合或者对象-脱敏测试-JSON
     * 备注：当为对象前台集合对象时，FastJSON 本身的转换结果就是不尽人意的。（或者说是 JSON 的规范）
     * @since 0.0.6
     */
    @Test
    public void sensitiveUserCollectionJsonTest() {
        final String originalStr = "UserCollection{userList=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userSet=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userCollection=[User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}], userMap={map=User{username='诸葛亮', idCard='123456190001011234', password='1234567', email='12345@qq.com', phone='18888888888'}}}";
        final String commonJson = "{\"userArray\":[{\"email\":\"12345@qq.com\",\"idCard\":\"123456190001011234\",\"password\":\"1234567\",\"phone\":\"18888888888\",\"username\":\"诸葛亮\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";
        final String sensitiveJson = "{\"userArray\":[{\"email\":\"123**@qq.com\",\"idCard\":\"123456**********34\",\"phone\":\"188****8888\",\"username\":\"诸*亮\"}],\"userCollection\":[{\"$ref\":\"$.userArray[0]\"}],\"userList\":[{\"$ref\":\"$.userArray[0]\"}],\"userMap\":{\"map\":{\"$ref\":\"$.userArray[0]\"}},\"userSet\":[{\"$ref\":\"$.userArray[0]\"}]}";

        final UserCollection userCollection = DataPrepareTest.buildUserCollection();

        Assert.assertEquals(commonJson, JSON.toJSONString(userCollection));
//        Assert.assertEquals(sensitiveJson, SensitiveUtils.desJson(userCollection));
        Assert.assertEquals(originalStr, userCollection.toString());
    }

}
