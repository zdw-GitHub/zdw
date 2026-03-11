package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据质量-基础校验规则对象 t_quality_verification_rules
 *
 * @author zxx
 * @date 2022-10-22
 */
public class TQualityVerificationRules extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 元模型id */
    @Excel(name = "元模型id")
    private String metaModeId;

    /** 技术元模型属性id */
    @Excel(name = "技术元模型属性id")
    private String metaModelColumnId;

    /** 基础规制id */
    @Excel(name = "基础规制id")
    private String ruleId;

    /** 告警级别   0。告警 1.提醒 4.通用校验 */
    @Excel(name = "告警级别   0。告警 1.提醒 4.通用校验")
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
    private String superiorUnitId;

    /** 选择类型   0 自定义sql 1 选择   2 填报 */
    @Excel(name = "选择类型   0 自定义sql 1 选择   2 填报")
    private String selectType;

    /** 符号  说明：大于 &gt; 小于 &lt;  等于=  范围    字典 in   不等于 ！=   大于等于&gt;=  小于等于&lt;=   关键字   左包含like  精度 unit   */
    @Excel(name = "符号  说明：大于 &gt; 小于 &lt;  等于=  范围    字典 in   不等于 ！=   大于等于&gt;=  小于等于&lt;=   关键字   左包含like  精度 unit  ")
    private String symbol;

    /** 参数 （多个参数逗号隔开 从小到大    精度传小数点后位数）  */
    @Excel(name = "参数 ", readConverterExp = "多=个参数逗号隔开,从=小到大,精=度传小数点后位数")
    private String param;

    /** 关联元模型id */
    @Excel(name = "关联元模型id")
    private String relMetaModeId;

    /** 关联技术元模型属性id */
    @Excel(name = "关联技术元模型属性id")
    private String relIdMetaModelColumnId;

    /** 关联技术元模型属性id */
    @Excel(name = "关联技术元模型属性id")
    private String relNameMetaModelColumnId;

    /** 自定义sql */
    @Excel(name = "自定义sql")
    private String customizedSql;

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
    public void setMetaModelColumnId(String metaModelColumnId)
    {
        this.metaModelColumnId = metaModelColumnId;
    }

    public String getMetaModelColumnId()
    {
        return metaModelColumnId;
    }
    public void setRuleId(String ruleId)
    {
        this.ruleId = ruleId;
    }

    public String getRuleId()
    {
        return ruleId;
    }
    public void setAlarmLevel(String alarmLevel)
    {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmLevel()
    {
        return alarmLevel;
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
    public void setSuperiorUnitId(String superiorUnitId)
    {
        this.superiorUnitId = superiorUnitId;
    }

    public String getSuperiorUnitId()
    {
        return superiorUnitId;
    }
    public void setSelectType(String selectType)
    {
        this.selectType = selectType;
    }

    public String getSelectType()
    {
        return selectType;
    }
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }
    public void setParam(String param)
    {
        this.param = param;
    }

    public String getParam()
    {
        return param;
    }
    public void setRelMetaModeId(String relMetaModeId)
    {
        this.relMetaModeId = relMetaModeId;
    }

    public String getRelMetaModeId()
    {
        return relMetaModeId;
    }

    public String getRelIdMetaModelColumnId() {
        return relIdMetaModelColumnId;
    }

    public void setRelIdMetaModelColumnId(String relIdMetaModelColumnId) {
        this.relIdMetaModelColumnId = relIdMetaModelColumnId;
    }

    public String getRelNameMetaModelColumnId() {
        return relNameMetaModelColumnId;
    }

    public void setRelNameMetaModelColumnId(String relNameMetaModelColumnId) {
        this.relNameMetaModelColumnId = relNameMetaModelColumnId;
    }

    public void setCustomizedSql(String customizedSql)
    {
        this.customizedSql = customizedSql;
    }

    public String getCustomizedSql()
    {
        return customizedSql;
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
                .append("metaModelColumnId", getMetaModelColumnId())
                .append("ruleId", getRuleId())
                .append("alarmLevel", getAlarmLevel())
                .append("ruleDescription", getRuleDescription())
                .append("modificationSuggestions", getModificationSuggestions())
                .append("state", getState())
                .append("superiorUnitId", getSuperiorUnitId())
                .append("selectType", getSelectType())
                .append("symbol", getSymbol())
                .append("param", getParam())
                .append("relMetaModeId", getRelMetaModeId())
                .append("relMetaModelColumnId", getRelIdMetaModelColumnId())
                .append("customizedSql", getCustomizedSql())
                .append("modelVersion", getModelVersion())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
