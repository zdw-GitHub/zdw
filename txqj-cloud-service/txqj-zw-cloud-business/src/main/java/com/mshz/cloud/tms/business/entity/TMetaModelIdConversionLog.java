package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * id中转换日志对象 t_meta_model_id_conversion_log
 *
 * @author zdw
 * @date 2023-08-09
 */
public class TMetaModelIdConversionLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 模型名称   默认配置为 default  */
    @Excel(name = "模型名称   默认配置为 default ")
    private String modelName;

    /** 模型名称中文 */
    @Excel(name = "模型名称中文")
    private String modelNameCn;

    /** 最后一次转换结果 0 否 1是 */
    @Excel(name = "最后一次转换结果 0 否 1是")
    private String conversionResult;

    /** 最后一次转换时间 */
    @Excel(name = "最后一次转换时间")
    private String conversionTime;

    /** 错误日志 */
    @Excel(name = "错误日志")
    private String errorLog;

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
    public void setModelNameCn(String modelNameCn)
    {
        this.modelNameCn = modelNameCn;
    }

    public String getModelNameCn()
    {
        return modelNameCn;
    }
    public void setConversionResult(String conversionResult)
    {
        this.conversionResult = conversionResult;
    }

    public String getConversionResult()
    {
        return conversionResult;
    }
    public void setConversionTime(String conversionTime)
    {
        this.conversionTime = conversionTime;
    }

    public String getConversionTime()
    {
        return conversionTime;
    }
    public void setErrorLog(String errorLog)
    {
        this.errorLog = errorLog;
    }

    public String getErrorLog()
    {
        return errorLog;
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
            .append("modelNameCn", getModelNameCn())
            .append("conversionResult", getConversionResult())
            .append("conversionTime", getConversionTime())
            .append("errorLog", getErrorLog())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }
}
