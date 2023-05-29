package com.github.framework.lock.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @Description 分布式锁自动配置实现
 */
@Configuration
@Import(LockConfig.class)
//如果不加上指定扫码包会加载不了
@ComponentScan(basePackages="com.github.framework.lock")
public class LockAutoConfig {


}
