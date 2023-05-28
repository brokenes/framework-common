package com.github.framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BizDemoService {

    @Autowired
    LockDemoService lockDemoService;


    private AtomicInteger atomicInteger =new AtomicInteger(0);

    public  void nestInvoke(String hello){
        if(atomicInteger.incrementAndGet() == 1){
            lockDemoService.nestInvoke();
        }

        System.out.println("bizDemoservice nestInvoke method print->" +  hello);
    }
}
