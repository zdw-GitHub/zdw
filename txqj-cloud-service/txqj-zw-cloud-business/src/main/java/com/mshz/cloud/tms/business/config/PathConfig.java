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
@ConfigurationProperties(prefix = "path")
public class PathConfig {
    private String excelPath;

    // @RequestMapping("/show")
    public String show() {
        return toString();
    }



}
