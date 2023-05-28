package com.github.framework.lock.config;

import com.github.framework.lock.LockManager;
import com.github.framework.lock.aop.DistributionLockAspect;
import com.github.framework.lock.exception.LockException;
import com.github.framework.lock.support.LockManagerImpl;
import com.github.framework.lock.support.redis.RedisLockProvider;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
public class LockConfig {

    @Bean
    @ConditionalOnMissingBean
    public DistributionLockAspect distributionLockAspect() {
        return new DistributionLockAspect();
    }


    @Bean
    @ConditionalOnMissingBean
    public LockManager lockManager() {
        LockManagerImpl lockManager =  new LockManagerImpl();
        return lockManager;
    }


    //------------------------------------redis---------------------------------------

    /** redisson client */
    @Bean
    @ConditionalOnBean(RedisLockProperties.class)
    public RedissonClient redissonClient(RedisLockProperties properties) {
        if (CollectionUtils.isEmpty(properties.getServers())) {
            throw new LockException("0001","Redis Lock Config err,Please set 'lock.redis.server=yourservers' in your spring config file!!");
        }
        Config config = new Config();
        if (properties.getServers().size() == 1) {
            config.useSingleServer().setAddress(properties.getServers().get(0));
        } else {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            clusterServersConfig.addNodeAddress((String[])properties.getServers().toArray());
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnBean(RedisLockProperties.class)
    public RedisLockProvider redisLockProvider() {
        RedisLockProvider redisLockProvider = new RedisLockProvider();
        return redisLockProvider;
    }

    @Bean
    @ConditionalOnProperty(prefix = "lock.redis",name = "enable",havingValue = "true")
    public RedisLockProperties redisLockProperties() {
        return new RedisLockProperties();
    }




}