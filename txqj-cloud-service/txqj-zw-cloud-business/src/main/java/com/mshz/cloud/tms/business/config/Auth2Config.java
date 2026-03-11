package com.mshz.cloud.tms.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成相关配置
 *
 * @author zdw
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2")
public class Auth2Config {
    private String userMessage;

    @Override
    public String toString() {
        return "Auth2Config{" +
                "userMessage='" + userMessage + '\'' +
                '}';
    }
}
