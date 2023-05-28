package com.github.framework.lock.config;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 *
 *  基于spring的配置
 *
 */

public class RedisLockProperties {

    @Value("${lock.redis.servers}")
    private List<String> servers;

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }
}
