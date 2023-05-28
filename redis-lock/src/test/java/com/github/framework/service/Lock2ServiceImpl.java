package com.github.framework.service;


import com.github.framework.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


@Component
public class Lock2ServiceImpl {

    private Logger logger = LoggerFactory.getLogger(Lock2ServiceImpl.class);

    @Autowired
    private Lock3ServiceImpl lock3Service;

    @Autowired
    private LockManager lockManager;

//    @Async
//    @Locking(id = "#lockName", module = "lock.test")
    public void lock(String lockName){
        Lock lock = lockManager.createLock("lock.test", lockName);
        logger.info(this.getClass().getName() + "-------lock...." + lockName);
        lock3Service.lock(lockName);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(this.getClass().getName() + "-------release");
        lock.unlock();
    }
}
