package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型对应采集源数据唯一标识 对象 t_meta_source_uid
 *
 * @author zdw
 * @date 2023-03-02
 */
public class TMetaSourceUid extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 源数据对应的转换元模型编号 */
    @Excel(name = "源数据对应的转换元模型编号")
    private Long metaModelId;

    /** 源数据库id */
    @Excel(name = "源数据库id")
    private Long dbId;

    /** 源模型id */
    @Excel(name = "源模型id")
    private Long tableId;

    /** 技术元数据属性编号 */
    @Excel(name = "技术元数据属性编号")
    private Long columnId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String columnName;

    /** 属性名称中文 */
    @Excel(name = "属性名称中文")
    private String columnNameCn;

    /** 字段顺序 */
    @Excel(name = "字段顺序")
    private String columnOrder;

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
    public void setMetaModelId(Long metaModelId)
    {
        this.metaModelId = metaModelId;
    }

    public Long getMetaModelId()
    {
        return metaModelId;
    }
    public void setDbId(Long dbId)
    {
        this.dbId = dbId;
    }

    public Long getDbId()
    {
        return dbId;
    }
    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public Long getTableId()
    {
        return tableId;
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
    public void setColumnOrder(String columnOrder)
    {
        this.columnOrder = columnOrder;
    }

    public String getColumnOrder()
    {
        return columnOrder;
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
            .append("metaModelId", getMetaModelId())
            .append("dbId", getDbId())
            .append("tableId", getTableId())
            .append("columnId", getColumnId())
            .append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn())
            .append("columnOrder", getColumnOrder())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }
}
