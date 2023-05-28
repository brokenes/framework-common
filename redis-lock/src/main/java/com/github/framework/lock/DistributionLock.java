package com.github.framework.lock;

import java.util.concurrent.locks.Lock;

public interface DistributionLock extends Lock,AutoCloseable {

    /**
     * 继承自AutoCloseable，并去掉Exception声明
     */
    @Override
    void close();
}
