package com.github.framework.lock.constants;

import com.github.framework.lock.enums.LockProviderType;
import org.github.framework.common.lang.CustomDateUtils;

public interface LockConstants {

    /** 默认锁的等待时间为5分钟 */
    Long DEFAULT_WAIT_TIME = 5 * CustomDateUtils.MILLIS_PER_MINUTE;

    /** 默认锁的超时时间，过了这个时间这个锁就可以被干掉 */
    Long DEFAULT_EXPIRED_TIME = 30 * CustomDateUtils.MILLIS_PER_MINUTE;

    /** 默认锁的提供方式为'redis' */
    LockProviderType DEFAULT_LOCK_PROVIDER = LockProviderType.REDIS;

    /** Redis默认的key前缀 */
    String DEFAULT_REDIS_KEY_PREFIX = "lock:";

    /** Zookeeper默认的路径前缀 */
    String DEFAULT_ZOOKEEPER_PATH_PREFIX = "/lock";

    /** 默认的错误提示 */
    String DEFAULT_LOCKED_ALERT = "有用户正在执行此操作，请稍候重试！";

    /** lock 拦截顺序 */
    int ASPECT_ORDER = 3;

}
