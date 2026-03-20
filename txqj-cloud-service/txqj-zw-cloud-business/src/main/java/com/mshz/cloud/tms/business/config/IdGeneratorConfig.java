package com.mshz.cloud.tms.business.config;

import com.mshz.cloud.tms.business.util.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author david
 * * @date 2026/3/19
 */
@Configuration
public class IdGeneratorConfig {
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        // 可通过配置文件传入 workerId 和 datacenterId，避免自动生成带来的不确定性
        return new SnowflakeIdGenerator();
    }
}
