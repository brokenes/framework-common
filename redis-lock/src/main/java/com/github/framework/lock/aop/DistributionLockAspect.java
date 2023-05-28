package com.github.framework.lock.aop;

import com.github.framework.lock.DistributionLock;
import com.github.framework.lock.LockInfo;
import com.github.framework.lock.LockManager;
import com.github.framework.lock.annotation.Locking;
import com.github.framework.lock.constants.LockConstants;
import com.github.framework.lock.exception.LockException;
import com.github.framework.lock.support.SimpleLockInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.github.framework.common.lang.CustomStringUtils;
import org.github.framework.common.reflect.ClassWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

@Aspect
@Order(LockConstants.ASPECT_ORDER)
@Slf4j
@EnableAspectJAutoProxy
public class DistributionLockAspect extends ApplicationObjectSupport {

    @Autowired
    LockManager lockManager;

    @Pointcut("@annotation(com.github.framework.lock.annotation.Locking)")
    public void pointcut(){
        // 这里不执行任何逻辑，只是用于定义Pointcut
    }

    @Around(value="pointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();

        DistributionLock lock = createLock(proceedingJoinPoint, targetClass);
        try  {

            if (lock != null) {
                lock.lock();
            }
            return proceedingJoinPoint.proceed();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }

    }

    /** 根据被拦截的方法参数创建一个分布式锁 */
    private DistributionLock createLock(JoinPoint jp, Class<?> clazz) {
        LockInfo lockInfo = createLockID(jp);
        if (lockInfo != null) {
            return lockManager.createLock(lockInfo);
        }
        return null;
    }

    /** 根据被拦截的参数生成一个锁定义 */
    private LockInfo createLockID(JoinPoint joinPoint) {
        Class<? extends Object> targetClass = joinPoint.getTarget().getClass();
        String methodName                   = joinPoint.getSignature().getName();
        Object[] arguments                  = joinPoint.getArgs();
        Method method                       = ClassWrapper.wrap(targetClass).getMethod(methodName, Locking.class);
        if (method != null) {
            Locking locking = method.getAnnotation(Locking.class);

            if (locking.expiredTime() < locking.waitTime()) {
                throw new LockException("锁的失效时间不能小于等待时间!!!");
            }

            //构建 SPEL
            ExpressionParser parser           = new SpelExpressionParser();
            StandardEvaluationContext context =
                    new MethodBasedEvaluationContext(
                            joinPoint.getTarget(),method,arguments,new DefaultParameterNameDiscoverer());
            context.setBeanResolver(new BeanFactoryResolver(getApplicationContext()));

            //condition为false时不加锁
            if (CustomStringUtils.isNotBlank(locking.condition())) {
                boolean conditionValue = parser.parseExpression(locking.condition()).getValue(context,boolean.class);
                if (!conditionValue) {
                    return null;
                }
            }

            String lockId = locking.id();
            if(CustomStringUtils.isBlank(lockId)){
                throw new LockException(String.format("不能创建锁，获取不到要指定参数为'%s'的锁ID值","lockId"));
            }
            if(lockId.contains("#")){
                lockId       = parser.parseExpression(locking.id()).getValue(context,String.class);
            }
            // 执行spel，获取 lock id 的值
            log.info("****************分布式lockId:{}******************",lockId);
            if (CustomStringUtils.isBlank(lockId)) {
                throw new LockException(String.format("不能创建锁，获取不到要指定参数为'%s'的锁ID值",locking.id()));
            }

            return SimpleLockInfo.of(locking,lockId);
        }
        return null;

    }


}