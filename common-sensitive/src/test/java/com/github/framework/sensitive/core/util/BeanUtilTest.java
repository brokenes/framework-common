package com.github.framework.sensitive.core.util;

import com.github.framework.sensitive.core.DataPrepareTest;
import com.github.framework.sensitive.model.sensitive.entry.UserGroup;
import org.junit.Assert;
import org.junit.Test;


public class BeanUtilTest {

    @Test
    public void deepCopyTest() {
        final UserGroup userGroup = DataPrepareTest.buildUserGroup();
        final UserGroup copyUserGroup = BeanUtil.deepCopy(userGroup);
        System.out.println("******"+userGroup.toString());
        Assert.assertEquals(copyUserGroup.toString(), userGroup.toString());
    }

}
