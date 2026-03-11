package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型字段规则对象 t_meta_model_rule
 *
 * @author zdw
 * @date 2023-08-17
 */
public class MetaModelRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类目编号 */
    @Excel(name = "类目编号")
    private Long categoryId;

    /** 类目名称 */
    @Excel(name = "类目名称")
    private String categoryName;

    /** 表编号 */
    @Excel(name = "表编号")
    private Long tableId;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 表中文名 */
    @Excel(name = "表中文名")
    private String tableNameCn;

    /** 属性编号 */
    @Excel(name = "属性编号")
    private Long columnId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String columnName;

    /** 属性名称中文*/
    @Excel(name = "属性名称中文 ")
    private String columnNameCn;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String generateRuleName;

    /** 规则内容 */
    @Excel(name = "规则内容")
    private String generateRule;

    /** 规则范例 */
    @Excel(name = "规则范例")
    private String generateRuleExample;

    /** 是否定期执行 */
    @Excel(name = "是否定期执行")
    private String regularExecution;

    /** 参数规则-设置 */
    @Excel(name = "参数规则设置")
    private String paramRuleSet;

    /** 参数规则-指定目标表 */
    @Excel(name = "参数规则设置")
    private String targetTable;

    /** 转换规则-sql */
    @Excel(name = "转换规则-sql")
    private String convertRuleSql;

    /** 转换规则-批量sql */
    @Excel(name = "转换规则-批量sql")
    private String convertRuleBatchSql;

    /** 字段使用规则。0：配置规则；1：转换规则 */
    @Excel(name = "字段使用规则。0：配置规则；1：转换规则")
    private String ruleUsed;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
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
    public void setGenerateRuleName(String generateRuleName)
    {
        this.generateRuleName = generateRuleName;
    }

    public String getGenerateRuleName()
    {
        return generateRuleName;
    }
    public void setGenerateRule(String generateRule)
    {
        this.generateRule = generateRule;
    }

    public String getGenerateRule()
    {
        return generateRule;
    }
    public void setGenerateRuleExample(String generateRuleExample)
    {
        this.generateRuleExample = generateRuleExample;
    }

    public String getGenerateRuleExample()
    {
        return generateRuleExample;
    }

    public String getParamRuleSet() {
        return paramRuleSet;
    }

    public void setParamRuleSet(String paramRuleSet) {
        this.paramRuleSet = paramRuleSet;
    }

    public void setConvertRuleSql(String convertRuleSql)
    {
        this.convertRuleSql = convertRuleSql;
    }

    public String getConvertRuleSql()
    {
        return convertRuleSql;
    }
    public void setConvertRuleBatchSql(String convertRuleBatchSql)
    {
        this.convertRuleBatchSql = convertRuleBatchSql;
    }

    public String getConvertRuleBatchSql()
    {
        return convertRuleBatchSql;
    }
    public void setRuleUsed(String ruleUsed)
    {
        this.ruleUsed = ruleUsed;
    }

    public String getRuleUsed()
    {
        return ruleUsed;
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

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("tableNameCn", getTableNameCn())
            .append("columnId", getColumnId())
            .append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn())
            .append("generateRuleName", getGenerateRuleName())
            .append("generateRule", getGenerateRule())
            .append("generateRuleExample", getGenerateRuleExample())
            .append("paramRuleSet", getParamRuleSet())
            .append("convertRuleSql", getConvertRuleSql())
            .append("convertRuleBatchSql", getConvertRuleBatchSql())
            .append("ruleUsed", getRuleUsed())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }

    public String getRegularExecution() {
        return regularExecution;
    }

    public void setRegularExecution(String regularExecution) {
        this.regularExecution = regularExecution;
    }
}
