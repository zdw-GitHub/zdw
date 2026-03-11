package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 元模型字段规则参数vo
 * @author Liuyue
 */
@Data
public class ModelRuleParamVo {

    private Long id;

    @ApiModelProperty(name = "所属字段规则id")
    private Long parModelRuleId;

    /** 参数名称 */
    @ApiModelProperty(name = "参数名称")
    private String paramName;

    /** 参数中文名称 */
    @ApiModelProperty(name = "参数中文名称")
    private String paramNameCn;

    /** 参数来源表id */
    @ApiModelProperty(name = "参数来源表id")
    private Long paramTableId;

    /** 参数来源表名 */
    @ApiModelProperty(name = "参数来源表名")
    private String paramTableName;

    /** 参数来源表中文名 */
    @ApiModelProperty(name = "参数来源表中文名")
    private String paramTableNameCn;

    /** 参数来源字段id */
    @ApiModelProperty(name = "参数来源字段id")
    private Long paramColumnId;

    /** 参数来源字段名 */
    @ApiModelProperty(name = "参数来源字段名")
    private String paramColumnName;

    /** 参数来源字段中文名 */
    @ApiModelProperty(name = "参数来源字段中文名")
    private String paramColumnNameCn;

    /** 参数来源条件 */
    @ApiModelProperty(name = "参数来源条件")
    private String paramCondition;

}
