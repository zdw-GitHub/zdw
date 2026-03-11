package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 t_meta_model_safe
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelSafe extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 技术元数据属性编号 */
    @Excel(name = "技术元数据属性编号")
    private Long columnId;

    /** 权限说明 */
    @Excel(name = "权限说明")
    private String permissionDescription;

    /** 权限类别 */
    @Excel(name = "权限类别")
    private String permissionType;

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
    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnId()
    {
        return columnId;
    }
    public void setPermissionDescription(String permissionDescription)
    {
        this.permissionDescription = permissionDescription;
    }

    public String getPermissionDescription()
    {
        return permissionDescription;
    }
    public void setPermissionType(String permissionType)
    {
        this.permissionType = permissionType;
    }

    public String getPermissionType()
    {
        return permissionType;
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
            .append("columnId", getColumnId())
            .append("permissionDescription", getPermissionDescription())
            .append("permissionType", getPermissionType())
            .append("modelVersion", getModelVersion())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
