package com.github.framework.lock;

import com.github.framework.lock.enums.LockProviderType;

/**
 * 锁提供者，用于使用具体的技术（redis,zk)来创建锁，以供LockManager使用
 */
public interface LockProvider {

    DistributionLock createMutexLock(LockInfo lockInfo);

    DistributionReadWriteLock createReadWriteLock(LockInfo lockInfo);

    LockProviderType getLockProviderType();
}
