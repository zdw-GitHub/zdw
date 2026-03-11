package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型字段规则参数对象 t_meta_model_rule_param
 *
 * @author zdw
 * @date 2023-08-17
 */
public class MetaModelRuleParam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 所属字段规则id */
    @Excel(name = "所属字段规则id")
    private Long modelRuleId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String paramName;

    /** 参数中文名称 */
    @Excel(name = "参数中文名称")
    private String paramNameCn;

    /** 参数来源表id */
    @Excel(name = "参数来源表id")
    private Long paramTableId;

    /** 参数来源表名 */
    @Excel(name = "参数来源表名")
    private String paramTableName;

    /** 参数来源表中文名 */
    @Excel(name = "参数来源表中文名")
    private String paramTableNameCn;

    /** 参数来源字段id */
    @Excel(name = "参数来源字段id")
    private Long paramColumnId;

    /** 参数来源字段名 */
    @Excel(name = "参数来源字段名")
    private String paramColumnName;

    /** 参数来源字段中文名 */
    @Excel(name = "参数来源字段中文名")
    private String paramColumnNameCn;

    /** 参数来源条件 */
    @Excel(name = "参数来源条件")
    private String paramCondition;

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
    public void setModelRuleId(Long modelRuleId)
    {
        this.modelRuleId = modelRuleId;
    }

    public Long getModelRuleId()
    {
        return modelRuleId;
    }
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public String getParamName()
    {
        return paramName;
    }
    public void setParamNameCn(String paramNameCn)
    {
        this.paramNameCn = paramNameCn;
    }

    public String getParamNameCn()
    {
        return paramNameCn;
    }
    public void setParamTableId(Long paramTableId)
    {
        this.paramTableId = paramTableId;
    }

    public Long getParamTableId()
    {
        return paramTableId;
    }
    public void setParamTableName(String paramTableName)
    {
        this.paramTableName = paramTableName;
    }

    public String getParamTableName()
    {
        return paramTableName;
    }
    public void setParamTableNameCn(String paramTableNameCn)
    {
        this.paramTableNameCn = paramTableNameCn;
    }

    public String getParamTableNameCn()
    {
        return paramTableNameCn;
    }
    public void setParamColumnId(Long paramColumnId)
    {
        this.paramColumnId = paramColumnId;
    }

    public Long getParamColumnId()
    {
        return paramColumnId;
    }
    public void setParamColumnName(String paramColumnName)
    {
        this.paramColumnName = paramColumnName;
    }

    public String getParamColumnName()
    {
        return paramColumnName;
    }
    public void setParamColumnNameCn(String paramColumnNameCn)
    {
        this.paramColumnNameCn = paramColumnNameCn;
    }

    public String getParamColumnNameCn()
    {
        return paramColumnNameCn;
    }
    public void setParamCondition(String paramCondition)
    {
        this.paramCondition = paramCondition;
    }

    public String getParamCondition()
    {
        return paramCondition;
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
            .append("modelRuleId", getModelRuleId())
            .append("paramName", getParamName())
            .append("paramNameCn", getParamNameCn())
            .append("paramTableId", getParamTableId())
            .append("paramTableName", getParamTableName())
            .append("paramTableNameCn", getParamTableNameCn())
            .append("paramColumnId", getParamColumnId())
            .append("paramColumnName", getParamColumnName())
            .append("paramColumnNameCn", getParamColumnNameCn())
            .append("paramCondition", getParamCondition())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }
}
