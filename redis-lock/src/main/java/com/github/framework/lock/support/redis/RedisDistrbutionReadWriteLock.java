package com.github.framework.lock.support.redis;

import com.github.framework.lock.DistributionLock;
import com.github.framework.lock.DistributionReadWriteLock;
import com.github.framework.lock.LockInfo;
import org.redisson.api.RReadWriteLock;

public class RedisDistrbutionReadWriteLock implements DistributionReadWriteLock {

    private RReadWriteLock readWriteLock;

    private LockInfo lockInfo;

    public RedisDistrbutionReadWriteLock(RReadWriteLock readWriteLock,LockInfo lockInfo) {

        this.readWriteLock = readWriteLock;
        this.lockInfo = lockInfo;
    }

    @Override
    public DistributionLock readLock() {
        return new RedisDistrbutionLock(readWriteLock.readLock(),lockInfo);
    }

    @Override
    public DistributionLock writeLock() {
        return new RedisDistrbutionLock(readWriteLock.writeLock(),lockInfo);
    }
}
