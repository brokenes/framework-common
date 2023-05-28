package com.github.framework.lock;

public interface LockManager {

    /**
     * 创建一个锁
     *
     */
    DistributionLock createLock(LockInfo lockInfo);

    /**
     * 创建一个新的分布式锁
     * @param module 模块
     * @param id 锁ID
     *
     */
    DistributionLock createLock(String module, String id);

    /**
     * 创建一个新的分布式锁
     * @param module 模块
     * @param id 锁ID
     * @param locked 成功获取锁后调用lock方法
     *
     */
    DistributionLock createLock(String module, String id,boolean locked);

    /**
     * 创建一个新的分布式读写锁
     * @param lockInfo
     *
     */
    DistributionReadWriteLock createReadWriteLock(LockInfo lockInfo);
}
