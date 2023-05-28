package com.github.framework.lock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @Description 分布式锁自动配置实现
 */
@Configuration
@Import(LockConfig.class)
public class LockAutoConfig {

}
