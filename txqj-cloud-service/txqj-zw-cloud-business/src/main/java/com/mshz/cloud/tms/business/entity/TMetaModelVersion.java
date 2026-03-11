package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 版本管理对象 t_meta_model_version
 *
 * @author zdw
 * @date 2022-10-11
 */
public class TMetaModelVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 版本号别名(t_meta_20221011164951 ) */
    @Excel(name = "版本号别名(t_meta_20221011164951 )")
    private String modelVersionAlias;

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
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
    }
    public void setModelVersionAlias(String modelVersionAlias)
    {
        this.modelVersionAlias = modelVersionAlias;
    }

    public String getModelVersionAlias()
    {
        return modelVersionAlias;
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
            .append("modelVersion", getModelVersion())
            .append("modelVersionAlias", getModelVersionAlias())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
