package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 标签对象 t_label
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TLabel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签id */
    private Long id;

    /** 便签编码 */
    @Excel(name = "便签编码")
    private String labelCode;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String labelName;

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
    public void setLabelCode(String labelCode)
    {
        this.labelCode = labelCode;
    }

    public String getLabelCode()
    {
        return labelCode;
    }
    public void setLabelName(String labelName)
    {
        this.labelName = labelName;
    }

    public String getLabelName()
    {
        return labelName;
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
            .append("labelCode", getLabelCode())
            .append("labelName", getLabelName())
            .append("remark", getRemark())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
