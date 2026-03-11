package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/26
 */
@Data
public class MetadataTechInfoVo {
    /*元模型表*/
    /**
     *所属数据库编号
     */
    @ApiModelProperty("所属数据库编号")
    private  Long dbId;
    /**
     *表名称
     */
    @ApiModelProperty("表名称")
    private String tableName;
    /*数据库配置表*/
    /**
     * 数据库类型
     */
    @ApiModelProperty("数据库类型")
    private String dbType;
    /**
     * 数据库连接
     */
    @ApiModelProperty("数据库连接名")
    private String dbJdbc;
    /**
     * 数据库名称
     */
    @ApiModelProperty("实例名")
    private String dbName;

}
