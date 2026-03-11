package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元模型关联标签对象 t_meta_model_re_label
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelReLabel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类目编号 */
    @Excel(name = "类目编号")
    private Long modelId;

    /** 便签编号 */
    @Excel(name = "便签编号")
    private Long labelId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDelete;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String labelName;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

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
    public void setLabelId(Long labelId)
    {
        this.labelId = labelId;
    }

    public Long getLabelId()
    {
        return labelId;
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
            .append("labelId", getLabelId())
                .append("labelName", getLabelName())
            .append("modelVersion", getModelVersion())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
