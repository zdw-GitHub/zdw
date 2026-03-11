package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 转换条件管理对象 t_meta_transformation_condition
 *
 * @author zdw
 * @date 2023-06-20
 */
public class TMetaTransformationCondition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 条件编号 */
    private Long id;

    /** 条件组编号 */
    @Excel(name = "条件组编号")
    private Long groupId;

    /** 条件类型 conditon_type_metar_model 元模型同步条件，conditon_type_attribute_transformation 属性转换条件，conditon_type_sql_transformation sql转换条件， */
    @Excel(name = "条件类型 conditon_type_metar_model 元模型同步条件，conditon_type_attribute_transformation 属性转换条件，conditon_type_sql_transformation sql转换条件，")
    private String conditionType;

    /** 条件内容 */
    @Excel(name = "条件内容")
    private String conditionContent;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getGroupId()
    {
        return groupId;
    }
    public void setConditionType(String conditionType)
    {
        this.conditionType = conditionType;
    }

    public String getConditionType()
    {
        return conditionType;
    }
    public void setConditionContent(String conditionContent)
    {
        this.conditionContent = conditionContent;
    }

    public String getConditionContent()
    {
        return conditionContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("conditionType", getConditionType())
            .append("conditionContent", getConditionContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
