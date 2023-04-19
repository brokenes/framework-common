package com.github.framework.core.lang;

import com.github.framework.core.collection.CustomList;
import com.github.framework.core.collection.CustomMap;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 *
 *  杂合工具类，对于无法归类等的工具方法都放于此类中
 */
public class Lang {

    /**
     * 基于Object类型进行判断对象是否为空
     * @param o 任何类型
     *
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }

        if (o instanceof CharSequence) {
            return CustomStringUtils.isEmpty((CharSequence)o);
        }

        if (o instanceof Collection) {
            return CustomList.isEmpty(o);
        }

        if (o instanceof Map) {
            return CustomMap.isEmpty((Map)o);
        }

        //数组的话，要查看是否空数组，如果有任何一个item不为空，则表示数组不为空
        if (o.getClass().isArray()) {
            final int length = Array.getLength(o);
            for (int i = 0; i < length; i++) {
                if (!isEmpty(Array.get(o, i))) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
