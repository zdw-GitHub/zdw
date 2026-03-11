package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型字段规则转换日志记录对象 t_meta_model_rule_conversion_log
 *
 * @author zdw
 * @date 2023-08-17
 */
public class MetaModelRuleConversionLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 所属字段规则id */
    @Excel(name = "所属字段规则id")
    @ApiModelProperty(value = "所属字段规则id", name = "modelRuleId", required = false, example = "")
    private Long modelRuleId;

    /** 表编号 */
    @Excel(name = "表编号")
    @ApiModelProperty(value = "表编号", name = "tableId", required = false, example = "")
    private Long tableId;

    /** 表名称 */
    @Excel(name = "表名称")
    @ApiModelProperty(value = "表名称", name = "tableName", required = false, example = "")
    private String tableName;

    /** 表中文名 */
    @Excel(name = "表中文名")
    @ApiModelProperty(value = "表中文名", name = "tableNameCn", required = false, example = "")
    private String tableNameCn;

    /** 属性编号 */
    @Excel(name = "属性编号")
    @ApiModelProperty(value = "属性编号", name = "columnId", required = false, example = "")
    private Long columnId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    @ApiModelProperty(value = "属性名称", name = "columnName", required = false, example = "")
    private String columnName;

    /** 属性名称中文*/
    @Excel(name = "属性名称中文 ")
    @ApiModelProperty(value = "属性名称中文", name = "columnNameCn", required = false, example = "")
    private String columnNameCn;

    /** 字段使用规则。0：配置规则；1：转换规则 */
    @Excel(name = "字段使用规则。0：配置规则；1：转换规则")
    @ApiModelProperty(value = "字段使用规则", name = "ruleUsed", required = false, example = "")
    private String ruleUsed;

    /** 是否批量转换。0：否；1：是 */
    @Excel(name = "是否批量转换。0：否；1：是")
    @ApiModelProperty(value = "是否批量转换", name = "isBatch", required = false, example = "")
    private String isBatch;

    /** 转换结果。0：失败；1：成功 */
    @Excel(name = "转换结果。0：失败；1：成功")
    @ApiModelProperty(value = "转换结果", name = "conversionResult", required = false, example = "")
    private String conversionResult;

    /** 转换时间 */
    @Excel(name = "转换时间")
    @ApiModelProperty(value = "转换时间", name = "conversionTime", required = false, example = "")
    private String conversionTime;

    /** 转换日志 */
    @Excel(name = "转换日志")
    @ApiModelProperty(value = "转换日志", name = "convertLog", required = false, example = "")
    private String convertLog;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    @ApiModelProperty(value = "时间范围查询条件-开始时间", name = "startTime", required = false, example = "")
    private String startTime;

    @ApiModelProperty(value = "时间范围查询条件-结束时间", name = "endTime", required = false, example = "")
    private String endTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setModelRuleId(Long modelRuleId)
    {
        this.modelRuleId = modelRuleId;
    }

    public Long getModelRuleId()
    {
        return modelRuleId;
    }
    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public Long getTableId()
    {
        return tableId;
    }
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }
    public void setTableNameCn(String tableNameCn)
    {
        this.tableNameCn = tableNameCn;
    }

    public String getTableNameCn()
    {
        return tableNameCn;
    }
    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnId()
    {
        return columnId;
    }
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnName()
    {
        return columnName;
    }
    public void setColumnNameCn(String columnNameCn)
    {
        this.columnNameCn = columnNameCn;
    }

    public String getColumnNameCn()
    {
        return columnNameCn;
    }
    public void setRuleUsed(String ruleUsed)
    {
        this.ruleUsed = ruleUsed;
    }

    public String getRuleUsed()
    {
        return ruleUsed;
    }
    public void setIsBatch(String isBatch)
    {
        this.isBatch = isBatch;
    }

    public String getIsBatch()
    {
        return isBatch;
    }
    public void setConversionResult(String conversionResult)
    {
        this.conversionResult = conversionResult;
    }

    public String getConversionResult()
    {
        return conversionResult;
    }
    public void setConversionTime(String conversionTime)
    {
        this.conversionTime = conversionTime;
    }

    public String getConversionTime()
    {
        return conversionTime;
    }

    public String getConvertLog() {
        return convertLog;
    }

    public void setConvertLog(String convertLog) {
        this.convertLog = convertLog;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("modelRuleId", getModelRuleId())
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("tableNameCn", getTableNameCn())
            .append("columnId", getColumnId())
            .append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn())
            .append("ruleUsed", getRuleUsed())
            .append("isBatch", getIsBatch())
            .append("conversionResult", getConversionResult())
            .append("conversionTime", getConversionTime())
            .append("convertLog", getConvertLog())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }
}
