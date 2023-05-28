package com.github.framework.lock.support.redis;

import com.github.framework.lock.DistributionLock;
import com.github.framework.lock.DistributionReadWriteLock;
import com.github.framework.lock.LockInfo;
import com.github.framework.lock.LockProvider;
import com.github.framework.lock.enums.LockProviderType;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RedisLockProvider implements LockProvider {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public DistributionLock createMutexLock(LockInfo lockInfo) {
        RLock rLock = redissonClient.getLock(lockInfo.getLockURI());
        return new RedisDistrbutionLock(rLock,lockInfo);
    }

    @Override
    public DistributionReadWriteLock createReadWriteLock(LockInfo lockInfo) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockInfo.getLockURI());
        return new RedisDistrbutionReadWriteLock(readWriteLock,lockInfo);
    }

    @Override
    public LockProviderType getLockProviderType() {
        return LockProviderType.REDIS;
    }
}
