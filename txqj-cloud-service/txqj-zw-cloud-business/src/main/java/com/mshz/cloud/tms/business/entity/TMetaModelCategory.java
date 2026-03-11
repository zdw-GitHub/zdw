package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 元数据类目对象 t_meta_model_category
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 类别名称 */
    @Excel(name = "类别名称")
    private String categoryName;

    /** 类别名称中文 */
    @Excel(name = "类别名称中文")
    private String categoryNameCn;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private String updateDate;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    public void setCategoryNameCn(String categoryNameCn)
    {
        this.categoryNameCn = categoryNameCn;
    }

    public String getCategoryNameCn()
    {
        return categoryNameCn;
    }
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryName", getCategoryName())
            .append("categoryNameCn", getCategoryNameCn())
            .append("remark", getRemark())
            .append("updateDate", getUpdateDate())
            .append("createDate", getCreateDate())
            .toString();
    }
}
