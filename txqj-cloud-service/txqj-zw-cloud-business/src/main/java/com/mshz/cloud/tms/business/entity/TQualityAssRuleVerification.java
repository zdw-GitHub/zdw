package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据质量-关联规则校验对象 t_quality_ass_rule_verification
 *
 * @author zxx
 * @date 2022-10-24
 */
public class TQualityAssRuleVerification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 元模型id */
    @Excel(name = "元模型id")
    private String metaModeId;

    /** 关联校验规则类型 0.关联规则 1.自定义sql校验 （sql条件添加 id = #{id}  例子  select * from t_spc_site where id = #{id}) */
    @Excel(name = "关联校验规则类型 0.关联规则 1.自定义sql校验 ", readConverterExp = "关联校验规则类型 0.关联规则 1.自定义sql校验 （sql条件添加 id = #{id}  例子  select * from t_spc_site where id = #{id})")
    private String verificationRuleType;

    /** 关联校验规则id */
    @Excel(name = "关联校验规则id")
    private String verificationRuleId;

    /** 告警级别   0。告警 1.提醒 2.通用校验 */
    @Excel(name = "告警级别   0。告警 1.提醒 2.通用校验")
    private String alarmLevel;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String ruleDescription;

    /** 治理建议 */
    @Excel(name = "治理建议")
    private String modificationSuggestions;

    /** 状态  0 启动 1禁用 */
    @Excel(name = "状态  0 启动 1禁用")
    private String state;

    /** 规则归属上级单位id */
    @Excel(name = "规则归属上级单位id")
    private String useDefinedVerSql;

    /** 自定义校验sql */
    @Excel(name = "自定义校验sql")
    private String superiorUnitId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMetaModeId(String metaModeId)
    {
        this.metaModeId = metaModeId;
    }

    public String getMetaModeId()
    {
        return metaModeId;
    }
    public void setVerificationRuleType(String verificationRuleType)
    {
        this.verificationRuleType = verificationRuleType;
    }

    public String getVerificationRuleType()
    {
        return verificationRuleType;
    }
    public void setVerificationRuleId(String verificationRuleId)
    {
        this.verificationRuleId = verificationRuleId;
    }

    public String getVerificationRuleId() {
        return verificationRuleId;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
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
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setUseDefinedVerSql(String useDefinedVerSql)
    {
        this.useDefinedVerSql = useDefinedVerSql;
    }

    public String getUseDefinedVerSql()
    {
        return useDefinedVerSql;
    }
    public void setSuperiorUnitId(String superiorUnitId)
    {
        this.superiorUnitId = superiorUnitId;
    }

    public String getSuperiorUnitId()
    {
        return superiorUnitId;
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
            .append("metaModeId", getMetaModeId())
            .append("verificationRuleType", getVerificationRuleType())
            .append("verificationRuleId", getVerificationRuleId())
            .append("ruleDescription", getRuleDescription())
            .append("modificationSuggestions", getModificationSuggestions())
            .append("state", getState())
            .append("useDefinedVerSql", getUseDefinedVerSql())
            .append("superiorUnitId", getSuperiorUnitId())
            .append("modelVersion", getModelVersion())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
