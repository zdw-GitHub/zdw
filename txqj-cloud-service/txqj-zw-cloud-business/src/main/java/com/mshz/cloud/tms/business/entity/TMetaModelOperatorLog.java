package com.mshz.cloud.tms.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 模型操作日志对象 t_meta_model_operator_log
 *
 * @author zxx
 * @date 2022-09-27
 */
public class TMetaModelOperatorLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 元模型编号 */
    @Excel(name = "元模型编号")
    private Long modelId;

    /** 元模型名称 */
    @Excel(name = "元模型名称")
    private String modelName;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operatorTime;

    /** 操作属性 */
    @Excel(name = "操作属性")
    private String operatorColumn;

    /** 操作类型（备注操作类型:增 删 改 查 发布 保存） */
    @Excel(name = "操作类型", readConverterExp = "备注操作类型:增,删,改,查,发布,保存")
    private String operateTypeNo;

    /** 操作信息 */
    @Excel(name = "操作信息")
    private String operateContent;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

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
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getModelName()
    {
        return modelName;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setOperatorTime(Date operatorTime)
    {
        this.operatorTime = operatorTime;
    }

    public Date getOperatorTime()
    {
        return operatorTime;
    }
    public void setOperatorColumn(String operatorColumn)
    {
        this.operatorColumn = operatorColumn;
    }

    public String getOperatorColumn()
    {
        return operatorColumn;
    }
    public void setOperateTypeNo(String operateTypeNo)
    {
        this.operateTypeNo = operateTypeNo;
    }

    public String getOperateTypeNo()
    {
        return operateTypeNo;
    }
    public void setOperateContent(String operateContent)
    {
        this.operateContent = operateContent;
    }

    public String getOperateContent()
    {
        return operateContent;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
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
            .append("modelId", getModelId())
            .append("modelName", getModelName())
            .append("operator", getOperator())
            .append("operatorTime", getOperatorTime())
            .append("operatorColumn", getOperatorColumn())
            .append("operateTypeNo", getOperateTypeNo())
            .append("operateContent", getOperateContent())
            .append("modelVersion", getModelVersion())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
