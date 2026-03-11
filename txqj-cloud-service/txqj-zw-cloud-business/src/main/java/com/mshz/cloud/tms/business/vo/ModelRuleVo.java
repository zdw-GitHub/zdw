package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 元模型字段规则vo
 * @author Liuyue
 */
@Data
public class ModelRuleVo {

    /*
     * 规则信息部分
     */
    /** 规则编号 */
    @ApiModelProperty(value = "规则id", name = "ruleId", required = false, example = "")
    private Long ruleId;

    /** 类目编号 */
    @ApiModelProperty(value = "类目id", name = "categoryId", required = false, example = "")
    private Long categoryId;

    /** 类目名称 */
    @ApiModelProperty(value = "类目名称", name = "categoryName", required = false, example = "")
    private String categoryName;

    /** 表编号 */
    @ApiModelProperty(value = "表编号", name = "tableId", required = false, example = "")
    private Long tableId;

    /** 表名称 */
    @ApiModelProperty(value = "表名称", name = "tableName", required = false, example = "")
    private String tableName;

    /** 表中文名 */
    @ApiModelProperty(value = "表中文名", name = "tableNameCn", required = false, example = "")
    private String tableNameCn;

    /** 属性编号 */
    @ApiModelProperty(value = "属性编号", name = "columnId", required = false, example = "")
    private Long columnId;

    /** 属性名称 */
    @ApiModelProperty(value = "属性名称", name = "columnName", required = false, example = "")
    private String columnName;

    /** 属性名称中文*/
    @ApiModelProperty(value = "属性名称中文", name = "columnNameCn", required = false, example = "")
    private String columnNameCn;

    /** 规则名称 */
    @ApiModelProperty(value = "规则名称", name = "generateRuleName", required = false, example = "")
    private String generateRuleName;

    /** 配置规则-内容 */
    @ApiModelProperty(value = "规则内容", name = "generateRule", required = false, example = "")
    private String generateRule;

    /** 规则范例 */
    @ApiModelProperty(value = "规则范例", name = "generateRuleExample", required = false, example = "")
    private String generateRuleExample;

    /** 是否定期执行 */
    @ApiModelProperty(value = "是否定期执行", name = "regularExecution", required = false, example = "")
    private String regularExecution;

    /** 参数规则设置 */
    @ApiModelProperty(value = "参数规则设置", name = "paramRuleSet", required = false, example = "")
    private String paramRuleSet;

    /** 转换规则-sql */
    @ApiModelProperty(value = "转换规则-sql", name = "convertRuleSql", required = false, example = "")
    private String convertRuleSql;

    /** 转换规则-批量sql */
    @ApiModelProperty(value = "转换规则-批量sql", name = "convertRuleBatchSql", required = false, example = "")
    private String convertRuleBatchSql;

    /** 转换规则-指定目标库名表名 */
    @ApiModelProperty(value = "转换规则-指定目标库名表名", name = "targetTable", required = false, example = "")
    private String targetTable;


    /*
     * 规则参数信息部分
     */
    /** 规则参数信息vo集合 */
    List<ModelRuleParamVo> ruleParamVoList;

    /*
     * 规则转换结果、规则转换日志部分
     */
    /** 字段使用规则。0：配置规则；1：转换规则 */
    @ApiModelProperty(value = "字段使用规则。0：配置规则；1：转换规则", name = "ruleUsed", required = false, example = "")
    private String ruleUsed;

    /** 规则转换日志编号 */
    @ApiModelProperty(value = "规则转换日志编号", name = "ruleLogId", required = false, example = "")
    private Long ruleLogId;

    /** 是否批量转换。0：否；1：是 */
    @ApiModelProperty(value = "是否批量转换。0：否；1：是", name = "isBatch", required = false, example = "")
    private String isBatch;

    /** 转换结果。0：失败；1：成功 */
    @ApiModelProperty(value = "转换结果。0：失败；1：成功", name = "conversionResult", required = false, example = "")
    private String conversionResult;

    /** 转换时间 */
    @ApiModelProperty(value = "转换时间", name = "conversionTime", required = false, example = "")
    private String conversionTime;

    /** 转换日志 */
    @ApiModelProperty(value = "转换日志", name = "convertLog", required = false, example = "")
    private String convertLog;
}
