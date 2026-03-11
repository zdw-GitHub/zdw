package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 转换条件组管理对象 t_meta_transformation_condition_group
 *
 * @author zdw
 * @date 2023-06-24
 */
public class TMetaTransformationConditionGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 条件组编号 */
    private Long id;

    /** 元模型编号 */
    @Excel(name = "元模型编号")
    private Long metaModelId;

    /** 元模型版本 */
    @Excel(name = "元模型版本")
    private String metaModelVersion;

    /** 条件组名称 */
    @Excel(name = "条件组名称")
    private String conditionGroupName;

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
    public void setMetaModelVersion(String metaModelVersion)
    {
        this.metaModelVersion = metaModelVersion;
    }

    public String getMetaModelVersion()
    {
        return metaModelVersion;
    }
    public void setConditionGroupName(String conditionGroupName)
    {
        this.conditionGroupName = conditionGroupName;
    }

    public String getConditionGroupName()
    {
        return conditionGroupName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("metaModelId", getMetaModelId())
            .append("metaModelVersion", getMetaModelVersion())
            .append("conditionGroupName", getConditionGroupName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
