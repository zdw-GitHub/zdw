package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据质量-关联规则对象 t_quality_association_rules
 *
 * @author zxx
 * @date 2022-10-24
 */
public class TQualityAssociationRules extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 英文名称 */
    @Excel(name = "英文名称")
    private String englishName;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String ruleDescription;

    /** 规则类型 0.通信站 */
    @Excel(name = "规则类型 0.通信站")
    private String ruleType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    public String getRuleName()
    {
        return ruleName;
    }
    public void setEnglishName(String englishName)
    {
        this.englishName = englishName;
    }

    public String getEnglishName()
    {
        return englishName;
    }
    public void setRuleDescription(String ruleDescription)
    {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleDescription()
    {
        return ruleDescription;
    }
    public void setRuleType(String ruleType)
    {
        this.ruleType = ruleType;
    }

    public String getRuleType()
    {
        return ruleType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ruleName", getRuleName())
            .append("englishName", getEnglishName())
            .append("ruleDescription", getRuleDescription())
            .append("ruleType", getRuleType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
