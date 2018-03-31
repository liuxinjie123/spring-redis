package com.dream.spring.redis.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.dream.spring.redis")
@EnableAspectJAutoProxy
public class AopConfig {
}
