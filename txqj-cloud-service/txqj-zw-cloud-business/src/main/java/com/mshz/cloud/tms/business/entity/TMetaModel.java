package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型对象 t_meta_model
 *
 * @author zdw
 * @date 2022-10-19
 */
public class TMetaModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类目编号 */
    @Excel(name = "类目编号")
    private Long categoryNameId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 状态,0未发布1发布 */
    @Excel(name = "状态,0未发布1发布")
    private String modelStatus;

    /** 所属数据库编号 */
    @Excel(name = "所属数据库编号")
    private Long dbId;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 表名称中文 */
    @Excel(name = "表名称中文")
    private String tableNameCn;

    /** 安全等级 */
    @Excel(name = "安全等级")
    private String safetyLevel;

    /** 更新日记 */
    @Excel(name = "更新日记")
    private String updateLog;

    /** 1是已删除，0是未删除 */
    @Excel(name = "1是已删除，0是未删除")
    private Integer isDelete;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    /** 关联主表数据库id */
    @Excel(name = "关联主表数据库id")
    private Long relationMainDbId;

    /** 关联主表id */
    @Excel(name = "关联主表id")
    private Long relationMainTableId;

    /** 关联主表别名 */
    @Excel(name = "关联主表别名")
    private String relationMainTableAlias;

    /** 审计模式  0无审计 1手动审计 2自动审计 */
    @Excel(name = "审计模式  0无审计 1手动审计 2自动审计")
    private String auditMode;

    /** 接入模式  1新增 2更新 3删除 */
    @Excel(name = "接入模式  1新增 2更新 3删除")
    private String accessMode;

    /** 数据入库模式 0：入库时做数据匹配，1：不比对旧数据直接入库 */
    @Excel(name = "数据入库模式 0：入库时做数据匹配，1：不比对旧数据直接入库")
    private String dataStorageMode;

    /** 数据同步关联类型；1字段关联，2sql关联，3混合关联 */
    @Excel(name = "数据同步关联类型；1字段关联，2sql关联，3混合关联")
    private String dataRelationType;

    public String getAuditMode() {
        return auditMode;
    }

    public void setAuditMode(String auditMode) {
        this.auditMode = auditMode;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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

    public void setRelationMainDbId(Long relationMainDbId)
    {
        this.relationMainDbId = relationMainDbId;
    }

    public Long getRelationMainDbId()
    {
        return relationMainDbId;
    }
    public void setRelationMainTableId(Long relationMainTableId)
    {
        this.relationMainTableId = relationMainTableId;
    }

    public Long getRelationMainTableId()
    {
        return relationMainTableId;
    }
    public void setRelationMainTableAlias(String relationMainTableAlias)
    {
        this.relationMainTableAlias = relationMainTableAlias;
    }

    public String getRelationMainTableAlias()
    {
        return relationMainTableAlias;
    }

    public String getDataStorageMode() {
        return dataStorageMode;
    }

    public void setDataStorageMode(String dataStorageMode) {
        this.dataStorageMode = dataStorageMode;
    }

    public String getDataRelationType() {
        return dataRelationType;
    }

    public void setDataRelationType(String dataRelationType) {
        this.dataRelationType = dataRelationType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryNameId", getCategoryNameId())
            .append("modelVersion", getModelVersion())
            .append("modelStatus", getModelStatus())
            .append("dbId", getDbId())
            .append("tableName", getTableName())
            .append("tableNameCn", getTableNameCn())
            .append("safetyLevel", getSafetyLevel())
            .append("updateLog", getUpdateLog())
            .append("isDelete", getIsDelete())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .append("relationMainDbId", getRelationMainDbId())
            .append("relationMainTableId", getRelationMainTableId())
            .append("relationMainTableAlias", getRelationMainTableAlias())
            .append("dataStorageMode", getDataStorageMode())
            .append("dataRelationType", getDataRelationType())
            .toString();
    }
}
