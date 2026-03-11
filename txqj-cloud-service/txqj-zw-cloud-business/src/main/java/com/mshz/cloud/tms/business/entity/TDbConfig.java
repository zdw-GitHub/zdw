package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源管理对象 t_db_config
 *
 * @author zdw
 * @date 2022-10-09
 */
public class TDbConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dbName;

    /** 数据库名称中文 */
    @Excel(name = "数据库名称中文")
    private String dbNameCn;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String dbType;

    /** 数据库版本 */
    @Excel(name = "数据库版本")
    private String dbVersion;

    /** 数据库连接 */
    @Excel(name = "数据库连接")
    private String dbJdbc;

    /** 数据库驱动 */
    @Excel(name = "数据库驱动")
    private String dbDriver;

    /** 数据库地址 */
    @Excel(name = "数据库地址")
    private String dbIp;

    /** 数据库端口 */
    @Excel(name = "数据库端口")
    private String dbPort;

    /** 数据库用户名 */
    @Excel(name = "数据库用户名")
    private String dbUsername;

    /** 数据库密码 */
    @Excel(name = "数据库密码")
    private String dbPassword;

    /**
     * 连接方式（0:iP,1:连接串）
     */
    private String connectionMethod;
    /**
     * 连接串
     */
    private String connectionString;


    public String getConnectionMethod() {
        return connectionMethod;
    }

    public void setConnectionMethod(String connectionMethod) {
        this.connectionMethod = connectionMethod;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public String getDbName()
    {
        return dbName;
    }
    public void setDbNameCn(String dbNameCn)
    {
        this.dbNameCn = dbNameCn;
    }

    public String getDbNameCn()
    {
        return dbNameCn;
    }
    public void setDbType(String dbType)
    {
        this.dbType = dbType;
    }

    public String getDbType()
    {
        return dbType;
    }
    public void setDbVersion(String dbVersion)
    {
        this.dbVersion = dbVersion;
    }

    public String getDbVersion()
    {
        return dbVersion;
    }
    public void setDbJdbc(String dbJdbc)
    {
        this.dbJdbc = dbJdbc;
    }

    public String getDbJdbc()
    {
        return dbJdbc;
    }
    public void setDbDriver(String dbDriver)
    {
        this.dbDriver = dbDriver;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }
    public void setDbIp(String dbIp)
    {
        this.dbIp = dbIp;
    }

    public String getDbIp()
    {
        return dbIp;
    }
    public void setDbPort(String dbPort)
    {
        this.dbPort = dbPort;
    }

    public String getDbPort()
    {
        return dbPort;
    }
    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }
    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dbName", getDbName())
            .append("dbNameCn", getDbNameCn())
            .append("dbType", getDbType())
            .append("dbVersion", getDbVersion())
            .append("dbJdbc", getDbJdbc())
            .append("dbDriver", getDbDriver())
            .append("dbIp", getDbIp())
            .append("dbPort", getDbPort())
            .append("dbUsername", getDbUsername())
            .append("dbPassword", getDbPassword())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
