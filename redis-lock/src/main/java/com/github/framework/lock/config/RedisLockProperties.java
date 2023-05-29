package com.github.framework.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 *  基于spring的配置
 *
 */
@Component
@ConfigurationProperties(prefix = "lock.redis")
@Data
public class RedisLockProperties {

//    @Value("${lock.redis.servers}")
    private List<String> servers;

}
