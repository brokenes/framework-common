package com.github.framework.lock.support.redis;

import com.github.framework.lock.BaseDistributionLock;
import com.github.framework.lock.DistributionLock;
import com.github.framework.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;


/**
 * 基于redisson的分布式可重入锁
 */
@Slf4j
public class RedisDistrbutionLock extends BaseDistributionLock implements DistributionLock {
    @Autowired
    private RLock rLock;
    @Autowired
    private LockInfo lockInfo;

    public RedisDistrbutionLock(RLock rLock, LockInfo lockInfo) {
        this.lockInfo = lockInfo;
        this.rLock = rLock;
    }

    public void lock() {
        this.rLock.lock(this.lockInfo.getWaitTime(), TimeUnit.MILLISECONDS);
    }

    public void lockInterruptibly() throws InterruptedException {
        this.rLock.lockInterruptibly();
    }

    public boolean tryLock() {
        log.info("尝试获取redission分布式锁");
        return this.rLock.tryLock();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.rLock.tryLock(time, unit);
    }

    public void unlock() {
        this.rLock.unlock();
    }

    public Condition newCondition() {
        return this.rLock.newCondition();
    }
}
