package com.mshz.cloud.tms.business.entity;


import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型发布对象 t_meta_model_release
 *
 * @author zxx
 * @date 2022-10-10
 */
public class TMetaModelRelease extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 元模型id */
    private Long modelId;

    /** 类目编号 */
    private Long categoryNameId;

    /** 版本号 */
    private String modelVersion;

    /** 状态,0未发布1发布 */
    private String modelStatus;

    /** 所属数据库编号 */
    private Long dbId;

    /** 表名称 */
    private String tableName;

    /** 表名称中文 */
    private String tableNameCn;

    /** 安全等级 */
    private String safetyLevel;

    /** 更新日记 */
    private String updateLog;

    /** 操作人 */
    private String operator;

    /** 创建时间 */
    private String createDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setCategoryNameId(Long categoryNameId)
    {
        this.categoryNameId = categoryNameId;
    }

    public Long getCategoryNameId()
    {
        return categoryNameId;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
    }
    public void setModelStatus(String modelStatus)
    {
        this.modelStatus = modelStatus;
    }

    public String getModelStatus()
    {
        return modelStatus;
    }
    public void setDbId(Long dbId)
    {
        this.dbId = dbId;
    }

    public Long getDbId()
    {
        return dbId;
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
    public void setSafetyLevel(String safetyLevel)
    {
        this.safetyLevel = safetyLevel;
    }

    public String getSafetyLevel()
    {
        return safetyLevel;
    }
    public void setUpdateLog(String updateLog)
    {
        this.updateLog = updateLog;
    }

    public String getUpdateLog()
    {
        return updateLog;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("modelId", getModelId())
            .append("categoryNameId", getCategoryNameId())
            .append("modelVersion", getModelVersion())
            .append("modelStatus", getModelStatus())
            .append("dbId", getDbId())
            .append("tableName", getTableName())
            .append("tableNameCn", getTableNameCn())
            .append("safetyLevel", getSafetyLevel())
            .append("updateLog", getUpdateLog())
            .append("operator", getOperator())
            .append("createDate", getCreateDate())
            .append("remark", getRemark())
            .toString();
    }
}
