package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/10/8
 */
@Data
public class MetadataRelationColumnVo {

     private  Long relationId;
    /*技术元数据表*/
    /**技术属性id(技术元数据表ID)*/
    private  Long techId;

    /**属性名称*/
    @ApiModelProperty("属性名称")
    private  String columnName;

    /**属性名称中文*/
    @ApiModelProperty("属性名称中文")
    private  String columnNameCn;

    @ApiModelProperty("类型1字段关联，2sql关联")
    private String relationType;

    /** 属性类型 */
    @ApiModelProperty("属性类型")
    private String columnType;

    /** 属性长度 */
    @ApiModelProperty("属性长度")
    private String columnLength;

    /*组合关联表*/
    /** 数据库编号 */
    @ApiModelProperty("数据库编号")
    private Long relationDbId;
    private String relationDbName;

    /** 关联源模型表编号 */
    @ApiModelProperty("关联源模型表编号")
    private Long relationTableId;
    private String relationTableName;

    /** 关联源模型表别名 */
    @ApiModelProperty("关联源模型表别名")
    private String relationTableAlias;

    /** 关联源模型表属性编号 */
    @ApiModelProperty("关联源模型表属性编号")
    private Long relationColumnId;
    private String relationColumnName;

    /**sql查询**/
    @ApiModelProperty("sql查询")
    private String sqlContent;

    private Integer isExternalConnection;
    private String  relationCondition;
    private String  updateDate;
    private String  updateOrNot;


}
