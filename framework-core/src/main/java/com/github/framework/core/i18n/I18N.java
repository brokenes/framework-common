/**   
 * @Title: I18N.java 
 * @Package com.welllink.framework.common.i18n 
 * @Description: 多语言处理
 * @author vanlin
 * @date 2019年10月16日 下午7:39:22  
 */
package com.github.framework.core.i18n;

import com.github.framework.core.spring.SpringContextUtils;

import java.util.Locale;
import java.util.Objects;

/**
 *  多语言处理
 */
public interface I18N {


    Locale locale();

    /**
     * 解析消息
     * @param code
     * @param objects
     * @param locale
     * @return
     */
    String resolveMessage(final String code, final Object[] objects, Locale locale);

    /**
     * 多语言解析
     * @param code
     * @param objects
     * @return
     */
    public static String resolve(final String code, final Object[] objects) {
        return resolve(code, objects, null);
    }

    /**
     * 多语言解析
     * @param code
     * @param objects
     * @param locale
     * @return
     */
    public static String resolve(final String code, final Object[] objects, Locale locale) {
        final I18N i18n = SpringContextUtils.getBean(I18N.class);

        if (Objects.isNull(i18n)) {
            return null;
        }
        if (Objects.isNull(locale)) {
            locale = i18n.locale();
        }
        if (Objects.isNull(locale)) {
            locale = Locales.zh_HK;
        }

        return i18n.resolveMessage(code, objects, locale);
    }
}
