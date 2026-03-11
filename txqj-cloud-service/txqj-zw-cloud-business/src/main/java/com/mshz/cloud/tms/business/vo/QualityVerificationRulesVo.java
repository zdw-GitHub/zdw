package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by hzy 2022/10/14
 **/
@Data
public class QualityVerificationRulesVo extends TQualityVerificationRules {
    @ApiModelProperty(value = "基础规则id")
    private String ruleId;

    @ApiModelProperty(value = "校验规则id")
    private String vruleId;

    @ApiModelProperty(value = "基础规则名称")
    private String ruleName;

    @ApiModelProperty(value = "基础规则英文名称")
    private String englishName;

    @ApiModelProperty(value = "表名称")
    private String tableName;

    @ApiModelProperty(value = "关联表名称")
    private String rtableName;

    @ApiModelProperty(value = "属性名称")
    private String columnName;

    @ApiModelProperty(value = "属性名称中文")
    private String columnNameCn;

    @ApiModelProperty(value = "属性类型")
    private String columnType;

    @ApiModelProperty(value = "关联属性名称")
    private String rcolumnName;

    @ApiModelProperty(value = "关联属性名称中文")
    private String rcolumnNameCn;

    @ApiModelProperty(value = "关联属性类型")
    private String rcolumnType;

    @ApiModelProperty(value = "关联属性名称1")
    private String rcolumnName1;

    @ApiModelProperty(value = "关联属性名称中文1")
    private String rcolumnNameCn1;

    @ApiModelProperty(value = "关联属性类型1")
    private String rcolumnType1;

    @ApiModelProperty(value = "治理建议")
    private String modificationSuggestions;

    @ApiModelProperty(value = "规则描述")
    private String ruleDescription;

    @ApiModelProperty(value = "选择类型")
    private String selectType;


    @ApiModelProperty(value = "符号")
    private String symbol;

    @ApiModelProperty(value = "参数属性名称")
    private String metaModelColomn;

    @ApiModelProperty(value = "参数")
    private String param;



    @ApiModelProperty(value = "自定义sql")
    private String customizedSql;

    private String modelId;

    @ApiModelProperty("属性长度")
    private String columnLength;
}
