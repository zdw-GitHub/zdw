package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表属性对象 t_db_table_column
 *
 * @author zdw
 * @date 2022-10-09
 */
public class TDbTableColumn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 表属性名称 */
    @Excel(name = "表属性名称")
    private String columnName;

    /** 表属性名称中文 */
    @Excel(name = "表属性名称中文")
    private String columnNameCn;

    /** 表属性类型 */
    @Excel(name = "表属性类型")
    private String columnType;

    /** 表属性长度 */
    @Excel(name = "表属性长度")
    private Long columnLength;

    /** 所属表编号 */
    @Excel(name = "所属表编号")
    private Long tableId;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public Long getTableId()
    {
        return tableId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn())
            .append("columnType", getColumnType())
            .append("columnLength", getColumnLength())
            .append("tableId", getTableId())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
