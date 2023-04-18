/**   
 * @Title: IError.java 
 * @Package com.welllink.framework.common 
 * @Description: 业务异常枚举父接口
 * @author vanlin
 * @date 2019年10月14日 上午10:44:51  
 */
package com.github.framework.core.exception;

import com.github.framework.core.IEnum;
import com.github.framework.core.i18n.I18N;

/**
 * 业务异常枚举父接口
 */
public interface IError extends IEnum<String> {

    /**
     * 业务错误码
     * @return
     */
    String code();

    /**
     * 业务错描述
     * @return
     */
    String message();

    @Override
    default String value() {
        return code();
    }

    @Override
    default String named() {
        return message();
    }

    /**
     * 创建异常实例
     * @return BusinessException, 创建异常实例
     */
    default BusinessException makeException(final Object... objects) {
        final String code = code();
        return Ex.business(code, I18N.resolve(code, objects));
    }
}
