package com.github.framework.lock;

import java.util.concurrent.locks.Condition;

public abstract class BaseDistributionLock implements DistributionLock,Cloneable,AutoCloseable{

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw  new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw   new UnsupportedOperationException();
    }

    @Override
    public void close() {
        unlock();
    }
}
