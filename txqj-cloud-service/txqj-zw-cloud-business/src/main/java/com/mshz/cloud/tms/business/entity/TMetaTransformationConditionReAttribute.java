package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 条件关联属性管理对象 t_meta_transformation_condition_re_attribute
 *
 * @author zdw
 * @date 2023-06-20
 */
public class TMetaTransformationConditionReAttribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 条件编号 */
    private Long id;

    /** 条件编号 */
    @Excel(name = "条件编号")
    private Long conditionId;

    /** 数据库编号 */
    @Excel(name = "数据库编号")
    private Long dbId;

    /** 数据库名称 */
    @Excel(name = "数据库名称")
    private String dbName;

    /** 表编号 */
    @Excel(name = "表编号")
    private Long tableId;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 表类型 metar_model 元模型 access_table 接入表 */
    @Excel(name = "表类型 metar_model 元模型 access_table 接入表")
    private String tableType;

    /** 属性编号 */
    @Excel(name = "属性编号")
    private Long attributeId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String attributeName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setConditionId(Long conditionId)
    {
        this.conditionId = conditionId;
    }

    public Long getConditionId()
    {
        return conditionId;
    }
    public void setDbId(Long dbId)
    {
        this.dbId = dbId;
    }

    public Long getDbId()
    {
        return dbId;
    }
    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public String getDbName()
    {
        return dbName;
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
    public void setTableType(String tableType)
    {
        this.tableType = tableType;
    }

    public String getTableType()
    {
        return tableType;
    }
    public void setAttributeId(Long attributeId)
    {
        this.attributeId = attributeId;
    }

    public Long getAttributeId()
    {
        return attributeId;
    }
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("conditionId", getConditionId())
            .append("dbId", getDbId())
            .append("dbName", getDbName())
            .append("tableId", getTableId())
            .append("tableName", getTableName())
            .append("tableType", getTableType())
            .append("attributeId", getAttributeId())
            .append("attributeName", getAttributeName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
