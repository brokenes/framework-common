package com.github.framework.lock.enums;


import com.github.framework.core.IEnum;

public enum LockType implements IEnum<String> {

    MUTEX("MUTEX", "独占锁"),
    SHARED("SHARED", "共享锁"),
    SEMAPHORE("SEMAPHORE", "信号量"),
    READ_WRITE("READ_WRITE", "读写锁");

    String value;
    String named;

    private LockType(String value, String named) {
        this.value = value;
        this.named = named;
    }

    public String value() {
        return this.value;
    }

    public String named() {
        return this.named;
    }

}
