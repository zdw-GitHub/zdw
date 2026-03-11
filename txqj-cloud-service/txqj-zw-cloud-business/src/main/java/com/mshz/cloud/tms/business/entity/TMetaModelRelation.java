package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 组合关系对象 t_meta_model_relation
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 技术元数据属性编号 */
    @Excel(name = "技术元数据属性编号")
    private Long columnId;

    /** 数据库编号 */
    @Excel(name = "数据库编号")
    private Long relationDbId;

    /** 关联源模型表编号 */
    @Excel(name = "关联源模型表编号")
    private Long relationTableId;

    /** 关联源模型表别名 */
    @Excel(name = "关联源模型表别名")
    private String relationTableAlias;

    /** 关联源模型表属性编号 */
    @Excel(name = "关联源模型表属性编号")
    private Long relationColumnId;

    /** 是否外连接 0 否 1是 */
    @Excel(name = "是否外连接 0 否 1是")
    private Integer isExternalConnection;

    /** 关联条件 */
    @Excel(name = "关联条件")
    private String relationCondition;

    /** sql查询 */
    @Excel(name = "sql查询")
    private String sqlContent;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 类型（字段关联，sql关联） */
    @Excel(name = "类型（字段关联，sql关联）")
    private String relationType;

    @Excel(name = "是否启用")
    private Integer isUsed;

    /** 是否更新  0否1是 */
    @Excel(name = "是否更新  0否1是")
    private String updateOrNot;

    public String getUpdateOrNot() {
        return updateOrNot;
    }

    public void setUpdateOrNot(String updateOrNot) {
        this.updateOrNot = updateOrNot;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }



    public String getRelaionType() {
        return relationType;
    }

    public void setRelaionType(String relaionType) {
        this.relationType = relaionType;
    }

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
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnId()
    {
        return columnId;
    }
    public void setRelationDbId(Long relationDbId)
    {
        this.relationDbId = relationDbId;
    }

    public Long getRelationDbId()
    {
        return relationDbId;
    }
    public void setRelationTableId(Long relationTableId)
    {
        this.relationTableId = relationTableId;
    }

    public Long getRelationTableId()
    {
        return relationTableId;
    }
    public void setRelationTableAlias(String relationTableAlias)
    {
        this.relationTableAlias = relationTableAlias;
    }

    public String getRelationTableAlias()
    {
        return relationTableAlias;
    }
    public void setRelationColumnId(Long relationColumnId)
    {
        this.relationColumnId = relationColumnId;
    }

    public Long getRelationColumnId()
    {
        return relationColumnId;
    }
    public void setIsExternalConnection(Integer isExternalConnection)
    {
        this.isExternalConnection = isExternalConnection;
    }

    public Integer getIsExternalConnection()
    {
        return isExternalConnection;
    }
    public void setRelationCondition(String relationCondition)
    {
        this.relationCondition = relationCondition;
    }

    public String getRelationCondition()
    {
        return relationCondition;
    }
    public void setSqlContent(String sqlContent)
    {
        this.sqlContent = sqlContent;
    }

    public String getSqlContent()
    {
        return sqlContent;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("modelId", getModelId())
            .append("columnId", getColumnId())
            .append("relationDbId", getRelationDbId())
            .append("relationTableId", getRelationTableId())
            .append("relationTableAlias", getRelationTableAlias())
            .append("relationColumnId", getRelationColumnId())
            .append("isExternalConnection", getIsExternalConnection())
            .append("relationCondition", getRelationCondition())
            .append("sqlContent", getSqlContent())
            .append("modelVersion", getModelVersion())
            .append("relationType", getRelaionType())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
