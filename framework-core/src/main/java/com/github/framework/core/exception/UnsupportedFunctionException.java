/**   
 * @Title: UnsupportedFunctionException.java 
 * @Package com.welllink.framework.common.exception 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author vanlin
 * @date 2019年4月8日 下午2:06:14  
 */
package com.github.framework.core.exception;

/** 
 * 未提供支持的功能
 *  
 */
public class UnsupportedFunctionException extends BaseException {


    private static final long serialVersionUID = -8167040012412519303L;


    public UnsupportedFunctionException(final String message) {
        super(message);
    }

}
