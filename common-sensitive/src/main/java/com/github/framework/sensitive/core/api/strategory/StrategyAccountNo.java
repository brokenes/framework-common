/**   
 * @Title: StrategyCnPhone.java 
 * @Package com.welllink.framework.common.sensitive.core.api.strategory 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author vanlin
 * @date 2019年11月1日 上午10:38:05  
 */
package com.github.framework.sensitive.core.api.strategory;

import com.github.framework.sensitive.api.IContext;
import com.github.framework.sensitive.api.IStrategy;
import com.github.framework.sensitive.core.util.strategy.SensitiveStrategyUtils;
import com.github.houbb.heaven.util.lang.ObjectUtil;



public class StrategyAccountNo implements IStrategy {


    @Override
    public Object des(final Object original, final IContext context) {
        return SensitiveStrategyUtils.accountNo(ObjectUtil.objectToString(original));
    }

}
