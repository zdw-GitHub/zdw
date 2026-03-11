package com.mshz.cloud.tms.business.vo;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 公共树形组件-资源数据树信息对象 t_quality_public_tree_info
 *
 * @author zxx
 * @date 2023-05-29
 */
@ApiModel(description = "公共树形组件-资源数据树信息对象")
public class QualityPublicTreeInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "公共资源树id", name = "id")
    private Long id;

    /** 公共资源树名称 */
    @Excel(name = "公共资源树名称")
    @ApiModelProperty(value = "公共资源树名称", name = "name")
    private String name;

    /** 元模型id */
    @Excel(name = "元模型id")
    @ApiModelProperty(value = "元模型id", name = "metaModeId")
    private String metaModeId;

    /** 公共资源树描述 */
    @Excel(name = "公共资源树描述")
    @ApiModelProperty(value = "公共资源树描述", name = "treeDesc")
    private String treeDesc;

    /** 状态。0：启动；1：禁用 */
    @Excel(name = "状态。0：启动；1：禁用")
    @ApiModelProperty(value = "状态。0：启动；1：禁用", name = "state")
    private String state;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setMetaModeId(String metaModeId)
    {
        this.metaModeId = metaModeId;
    }

    public String getMetaModeId()
    {
        return metaModeId;
    }
    public void setTreeDesc(String treeDesc)
    {
        this.treeDesc = treeDesc;
    }

    public String getTreeDesc()
    {
        return treeDesc;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("metaModeId", getMetaModeId())
            .append("treeDesc", getTreeDesc())
            .append("state", getState())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
