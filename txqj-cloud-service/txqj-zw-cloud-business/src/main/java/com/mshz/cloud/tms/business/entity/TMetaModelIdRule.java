package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 模型ID生成规则配置对象 t_meta_model_id_rule
 *
 * @author zdw
 * @date 2023-08-09
 */
public class TMetaModelIdRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型类别编号 */
    @Excel(name = "模型类别编号")
    private Long categoryId;

    /** 模型类别名称中文 */
    @Excel(name = "模型类别名称中文")
    private String categoryName;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 模型名称   默认配置为 default  */
    @Excel(name = "模型名称   默认配置为 default ")
    private String modelName;

    /** 模型名称中文 */
    @Excel(name = "模型名称中文")
    private String modelNameCn;

    /** ID范例 文本 */
    @Excel(name = "ID范例 文本")
    private String idDemo;

    /** ID生成规则文本 */
    @Excel(name = "ID生成规则文本")
    private String idGenerationRule;

    /** 前缀编码默认 */
    @Excel(name = "前缀编码默认")
    private String prefixEncodingDefault;

    /** 前缀编码属性名称 */
    @Excel(name = "前缀编码属性名称")
    private String prefixEncodingAttributeName;

    /** 数据管理机构编码位数 */
    @Excel(name = "数据管理机构编码位数")
    private String dataManagementOrgCodeDigit;

    /** 数据管理机构编码默认 */
    @Excel(name = "数据管理机构编码默认")
    private String dataManagementOrgCodeDefault;

    /** 是否含有数据管理机构字段 */
    @Excel(name = "是否含有数据管理机构字段")
    private String isHaveDataManagementOrgCode;

    /** 数据管理机构字段 默认为TMS_SN */
    @Excel(name = "数据管理机构字段 默认为TMS_SN")
    private String dataManagementOrgCodeAttributeName;

    /** 序列号位数 */
    @Excel(name = "序列号位数")
    private String serialNumberDigit;

    /** ID接口SQL */
    @Excel(name = "ID接口SQL")
    private String idInterfaceSql;

    /** ID批量更新SQL */
    @Excel(name = "ID批量更新SQL")
    private String batchUpdateSql;

    /** 最后一次转换结果 0 否 1是 */
    @Excel(name = "最后一次转换结果 0 否 1是")
    private String lastConversionResult;

    /** 最后一次转换时间 */
    @Excel(name = "最后一次转换时间")
    private String lastConversionTime;

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
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
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
    public void setIdDemo(String idDemo)
    {
        this.idDemo = idDemo;
    }

    public String getIdDemo()
    {
        return idDemo;
    }
    public void setIdGenerationRule(String idGenerationRule)
    {
        this.idGenerationRule = idGenerationRule;
    }

    public String getIdGenerationRule()
    {
        return idGenerationRule;
    }
    public void setPrefixEncodingDefault(String prefixEncodingDefault)
    {
        this.prefixEncodingDefault = prefixEncodingDefault;
    }

    public String getPrefixEncodingDefault()
    {
        return prefixEncodingDefault;
    }
    public void setPrefixEncodingAttributeName(String prefixEncodingAttributeName)
    {
        this.prefixEncodingAttributeName = prefixEncodingAttributeName;
    }

    public String getPrefixEncodingAttributeName()
    {
        return prefixEncodingAttributeName;
    }
    public void setDataManagementOrgCodeDigit(String dataManagementOrgCodeDigit)
    {
        this.dataManagementOrgCodeDigit = dataManagementOrgCodeDigit;
    }

    public String getDataManagementOrgCodeDigit()
    {
        return dataManagementOrgCodeDigit;
    }
    public void setDataManagementOrgCodeDefault(String dataManagementOrgCodeDefault)
    {
        this.dataManagementOrgCodeDefault = dataManagementOrgCodeDefault;
    }

    public String getDataManagementOrgCodeDefault()
    {
        return dataManagementOrgCodeDefault;
    }
    public void setIsHaveDataManagementOrgCode(String isHaveDataManagementOrgCode)
    {
        this.isHaveDataManagementOrgCode = isHaveDataManagementOrgCode;
    }

    public String getIsHaveDataManagementOrgCode()
    {
        return isHaveDataManagementOrgCode;
    }
    public void setDataManagementOrgCodeAttributeName(String dataManagementOrgCodeAttributeName)
    {
        this.dataManagementOrgCodeAttributeName = dataManagementOrgCodeAttributeName;
    }

    public String getDataManagementOrgCodeAttributeName()
    {
        return dataManagementOrgCodeAttributeName;
    }
    public void setSerialNumberDigit(String serialNumberDigit)
    {
        this.serialNumberDigit = serialNumberDigit;
    }

    public String getSerialNumberDigit()
    {
        return serialNumberDigit;
    }
    public void setIdInterfaceSql(String idInterfaceSql)
    {
        this.idInterfaceSql = idInterfaceSql;
    }

    public String getIdInterfaceSql()
    {
        return idInterfaceSql;
    }
    public void setBatchUpdateSql(String batchUpdateSql)
    {
        this.batchUpdateSql = batchUpdateSql;
    }

    public String getBatchUpdateSql()
    {
        return batchUpdateSql;
    }
    public void setLastConversionResult(String lastConversionResult)
    {
        this.lastConversionResult = lastConversionResult;
    }

    public String getLastConversionResult()
    {
        return lastConversionResult;
    }
    public void setLastConversionTime(String lastConversionTime)
    {
        this.lastConversionTime = lastConversionTime;
    }

    public String getLastConversionTime()
    {
        return lastConversionTime;
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
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("modelId", getModelId())
            .append("modelName", getModelName())
            .append("modelNameCn", getModelNameCn())
            .append("idDemo", getIdDemo())
            .append("idGenerationRule", getIdGenerationRule())
            .append("prefixEncodingDefault", getPrefixEncodingDefault())
            .append("prefixEncodingAttributeName", getPrefixEncodingAttributeName())
            .append("dataManagementOrgCodeDigit", getDataManagementOrgCodeDigit())
            .append("dataManagementOrgCodeDefault", getDataManagementOrgCodeDefault())
            .append("isHaveDataManagementOrgCode", getIsHaveDataManagementOrgCode())
            .append("dataManagementOrgCodeAttributeName", getDataManagementOrgCodeAttributeName())
            .append("serialNumberDigit", getSerialNumberDigit())
            .append("idInterfaceSql", getIdInterfaceSql())
            .append("batchUpdateSql", getBatchUpdateSql())
            .append("lastConversionResult", getLastConversionResult())
            .append("lastConversionTime", getLastConversionTime())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("remark", getRemark())
            .toString();
    }
}
