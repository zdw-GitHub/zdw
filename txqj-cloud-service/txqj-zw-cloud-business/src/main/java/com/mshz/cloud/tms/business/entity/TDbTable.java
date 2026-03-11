package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

/**
 * 数据库表清单对象 t_db_table
 *
 * @author zdw
 * @date 2022-10-09
 */
public class TDbTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 表名称 */
    @Excel(name = "表名称")
    private String tableName;

    /** 表名称中文 */
    @Excel(name = "表名称中文")
    private String tableNameCn;

    /** 所属数据库编号 */
    @Excel(name = "所属数据库编号")
    private Long dbId;

    /** 查询条件 */
    @Excel(name = "查询条件")
    private String searchCondition;

    private String conditionName;

    @Transient
    private Boolean disabled;

    /**
     * 源数据同步删除标记，用于决定同步任务对旧数据的处理，是存量还是增量同步
     * 0：不删除旧数据；1：删除旧数据
     * 该值存储到源数据同步条件表 t_db_table_condition表中，此处值只用于接口参数传递
     */
    @Transient
    private String deleteFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setDbId(Long dbId)
    {
        this.dbId = dbId;
    }

    public Long getDbId()
    {
        return dbId;
    }
    public void setSearchCondition(String searchCondition)
    {
        this.searchCondition = searchCondition;
    }

    public String getSearchCondition()
    {
        return searchCondition;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }


    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableName", getTableName())
            .append("tableNameCn", getTableNameCn())
            .append("dbId", getDbId())
            .append("remark", getRemark())
            .append("searchCondition", getSearchCondition())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
