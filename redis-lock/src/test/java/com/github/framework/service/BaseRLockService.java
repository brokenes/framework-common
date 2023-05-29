
package com.github.framework.service;


import com.github.framework.lock.annotation.Locking;
import com.github.framework.lock.enums.LockProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class BaseRLockService {
    @Autowired
    LockDemoService lockDemoService;

    private AtomicInteger incr = new AtomicInteger(0);

    @Locking(id = "'simpleLock:' + #hello",module = "baseRlock",provider = LockProviderType.REDIS)
    public void simpleLock(String hello){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*********************hello world!!!!****************************"+hello);
    }
}
