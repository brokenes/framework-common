package com.github.framework.service;

import com.github.framework.lock.LockManager;
import com.github.framework.lock.annotation.Locking;
import com.github.framework.lock.enums.LockProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockDemoService {

    @Autowired
    BizDemoService bizDemoService;

    LockManager lockManager;

    @Locking(id="hello",module="hellp")
    public  void hello(String hello){
        System.out.println("hello");
    }

    @Locking(id = "'num'",module = "hello",waitTime = 10)
    public void sayNum(Integer num) {
        System.out.printf("Thread:%s,Num:%d\n" , Thread.currentThread().getName() , num );
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Locking(id = "'nestInvoke'",module = "hello",waitTime = 10,expiredTime = 5000,provider = LockProviderType.REDIS)
    public void nestInvoke() {

        bizDemoService.nestInvoke(Thread.currentThread().getName());
    }

    @Locking(id = "#hello",module = "hello",provider = LockProviderType.REDIS)
    public void helloRedis(String hello,String name){
        System.out.println("hello,"+hello);
    }

    @Locking(id = "#simple.id",module = "simple",provider = LockProviderType.REDIS)
    public void saveSimple(Simple simple) {
        System.out.println(simple + "-------->is saved!");
    }

    public static class Simple {
        private String id;
        private String name;

        public Simple(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Simple{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
