package com.github.framework.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/***
 * 常用Locale常量
 */
public class Locales {

    // 香港繁体中文
    public static final Locale zh_HK = new Locale("zh", "HK");
    // 简体中文
    public static final Locale zh_CN = new Locale("zh", "CN");
    // 澳门繁体中文
    public static final Locale zh_MO = new Locale("zh", "MO");
    // 葡萄牙语
    public static final Locale pt_PT = new Locale("pt", "PT");

    public static final Locale en_US = new Locale("en", "US");

    private static final Map<String, Locale> localeMap = new HashMap<String, Locale>();
    static {
        localeMap.put("zh_HK", zh_HK);
        localeMap.put("zh_CN", zh_CN);
        localeMap.put("zh_MO", zh_MO);
        localeMap.put("pt_PT", pt_PT);
        localeMap.put("en_US", en_US);
    }

    public static Locale parse(final String localeStr) {
        final Locale locale = localeMap.get(localeStr);
        if (locale != null) {
            return locale;
        } else {
            final String[] localeStrs = localeStr.split("_");
            if (localeStrs.length == 2) {
                return new Locale(localeStrs[0], localeStrs[1]);
            }
            return locale;
        }
    }
}
