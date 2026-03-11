package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 数据质量-校验结果对象 t_quality_verification_result
 *
 * @author hzy
 * @date 2022-10-14
 */
public class TQualityVerificationResult implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 规则类别 0.基础规则 1.关联规则 */
    @Excel(name = "规则类别 0.基础规则 1.关联规则")
    private String ruleCategory;

    /** 校验规则id */
    @Excel(name = "校验规则id")
    private String verificationRuleId;

    /** 错误数据id */
    @Excel(name = "错误数据id")
    private String errorId;

    /** 错误属性中文名称 */
    @Excel(name = "错误属性中文名称")
    private String errorColumnNameCn;

    /** 错误属性名称 */
    @Excel(name = "错误属性名称")
    private String errorColumnName;

    /** 错误属性值 */
    @Excel(name = "错误属性值")
    private String errorColumnValue;

    /** 错误数据 */
    @Excel(name = "错误数据")
    private String errorData;

    /** 资源类型 */
    @Excel(name = "资源类型")
    private String resourceType;

    /** 问题种类 */
    @Excel(name = "问题种类")
    private String problemType;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String ruleDescription;

    /** 治理建议 */
    @Excel(name = "治理建议")
    private String modificationSuggestions;

    private String createTime;

    private String updateTime;



    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRuleCategory(String ruleCategory)
    {
        this.ruleCategory = ruleCategory;
    }

    public String getRuleCategory()
    {
        return ruleCategory;
    }
    public void setVerificationRuleId(String verificationRuleId)
    {
        this.verificationRuleId = verificationRuleId;
    }

    public String getVerificationRuleId()
    {
        return verificationRuleId;
    }
    public void setErrorId(String errorId)
    {
        this.errorId = errorId;
    }

    public String getErrorId()
    {
        return errorId;
    }
    public void setErrorColumnNameCn(String errorColumnNameCn)
    {
        this.errorColumnNameCn = errorColumnNameCn;
    }

    public String getErrorColumnNameCn()
    {
        return errorColumnNameCn;
    }
    public void setErrorColumnName(String errorColumnName)
    {
        this.errorColumnName = errorColumnName;
    }

    public String getErrorColumnName()
    {
        return errorColumnName;
    }
    public void setErrorColumnValue(String errorColumnValue)
    {
        this.errorColumnValue = errorColumnValue;
    }

    public String getErrorColumnValue()
    {
        return errorColumnValue;
    }
    public void setErrorData(String errorData)
    {
        this.errorData = errorData;
    }

    public String getErrorData()
    {
        return errorData;
    }
    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getResourceType()
    {
        return resourceType;
    }
    public void setProblemType(String problemType)
    {
        this.problemType = problemType;
    }

    public String getProblemType()
    {
        return problemType;
    }
    public void setRuleDescription(String ruleDescription)
    {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleDescription()
    {
        return ruleDescription;
    }
    public void setModificationSuggestions(String modificationSuggestions)
    {
        this.modificationSuggestions = modificationSuggestions;
    }

    public String getModificationSuggestions()
    {
        return modificationSuggestions;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ruleCategory", getRuleCategory())
                .append("verificationRuleId", getVerificationRuleId())
                .append("errorId", getErrorId())
                .append("errorColumnNameCn", getErrorColumnNameCn())
                .append("errorColumnName", getErrorColumnName())
                .append("errorColumnValue", getErrorColumnValue())
                .append("errorData", getErrorData())
                .append("resourceType", getResourceType())
                .append("problemType", getProblemType())
                .append("ruleDescription", getRuleDescription())
                .append("modificationSuggestions", getModificationSuggestions())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
