package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型属性发布对象 t_meta_model_column_release
 *
 * @author zxx
 * @date 2022-09-28
 */
public class TMetaModelColumnRelease extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 技术元数据属性编号 */
    @Excel(name = "技术元数据属性编号")
    private Long columnId;

    /** 元模型编号 */
    @Excel(name = "元模型编号")
    private Long modelId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String columnName;

    /** 属性名称中文 */
    @Excel(name = "属性名称中文")
    private String columnNameCn;

    /** 属性类型int，char */
    @Excel(name = "属性类型int，char")
    private String columnType;

    /** 属性长度 */
    @Excel(name = "属性长度")
    private Long columnLength;

    /** 是否必填1是，0否 */
    @Excel(name = "是否必填1是，0否")
    private String isNotNull;

    /** 是否主键1是，0否 */
    @Excel(name = "是否主键1是，0否")
    private String isPrimaryKey;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnId()
    {
        return columnId;
    }
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
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
    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }

    public String getColumnType()
    {
        return columnType;
    }
    public void setColumnLength(Long columnLength)
    {
        this.columnLength = columnLength;
    }

    public Long getColumnLength()
    {
        return columnLength;
    }
    public void setIsNotNull(String isNotNull)
    {
        this.isNotNull = isNotNull;
    }

    public String getIsNotNull()
    {
        return isNotNull;
    }
    public void setIsPrimaryKey(String isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getIsPrimaryKey()
    {
        return isPrimaryKey;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("columnId", getColumnId())
            .append("modelId", getModelId())
            .append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn())
            .append("columnType", getColumnType())
            .append("columnLength", getColumnLength())
            .append("isNotNull", getIsNotNull())
            .append("isPrimaryKey", getIsPrimaryKey())
            .append("modelVersion", getModelVersion())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
                .append("createDate", getCreateDate())
            .toString();
    }
}
