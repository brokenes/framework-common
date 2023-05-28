package com.github.framework.lock.annotation;

import com.github.framework.lock.enums.LockProviderType;
import com.github.framework.lock.enums.LockType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Locking {

    /**
     * 锁id,同时支持spel表达式
     * @return
     */
    String id() default "";

    /**
     * 支付zookeeper，也支持redis
     * @return
     */
    String module();

    /**
     * 锁类型
     * @return
     */
    LockType lockType() default LockType.MUTEX;

    /**
     * 获取锁的等待时间，默认等待时间为5分钟，时间单位为微秒
     * @return
     */
    long waitTime() default  5 * 60 * 1000;

    /***
     * 锁的失效时间，此时间必须大于waitTime，默认过期时间为30分钟，过了这个时间这个锁就无效了，时间单位为微秒
     * @return
     */
    long expiredTime() default 30 * 60 * 1000;

    /**
     * 条件，支持SPEL，条件成功才会加锁
     * @return
     */
    String condition() default "";

    /**
     * 锁的提供方式，可以是redis，也可以是zookeeper，根据实际的场景来做选择
     * @return
     */
    LockProviderType provider() default LockProviderType.REDIS;

    /**
     * 如果被锁了(即获取锁的时间超过了waitTime)要提示什么信息给用户，默认为“有用户正执行此操作，请稍候重试！”
     * @return
     */
    String lockedAlert() default "当前操作正在执行，请稍候重试！";
}
