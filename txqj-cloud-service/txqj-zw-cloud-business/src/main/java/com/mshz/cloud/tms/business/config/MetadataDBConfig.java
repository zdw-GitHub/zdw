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
@ConfigurationProperties(prefix = "metadb")
public class MetadataDBConfig {
    private String driverClass;

    private String databaseName;

    private String databaseIp;

    private String databasePort;

    private String databaseType;

    private String userName;

    private String userPassword;

    @Override
    public String toString() {
        return "JDBCProperties [driverClass=" + driverClass + ", databaseName=" + databaseName + ", databaseIp="
            + databaseIp + ", databasePort=" + databasePort + ", databaseType=" + databaseType + ", userName="
            + userName + ", userPassword=" + userPassword + "]";
    }

}
