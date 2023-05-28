package com.github.framework.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class EmptyDistributionLock implements DistributionLock {

    public EmptyDistributionLock() {
    }

    public void close() {
    }

    public void lock() {
    }

    public void lockInterruptibly() {
    }

    public boolean tryLock() {
        return true;
    }

    public boolean tryLock(long time, TimeUnit unit) {
        return true;
    }

    public void unlock() {
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
