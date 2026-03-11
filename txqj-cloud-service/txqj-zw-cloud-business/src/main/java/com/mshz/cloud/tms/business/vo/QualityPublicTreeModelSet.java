package com.mshz.cloud.tms.business.vo;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 公共树形组件-元模型树形组件配置对象 t_quality_public_tree_model_set
 *
 * @author zxx
 * @date 2023-06-02
 */
public class QualityPublicTreeModelSet extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id", name = "id", required = false, example = "")
    private Long id;

    /** 节点对应字段所属的元模型字段id */
    @Excel(name = "节点对应字段所属的元模型id")
    @ApiModelProperty(value = "节点对应字段所属的元模型id", name = "metaModelId", required = false, example = "")
    private Long metaModelId;

    /** 节点对应字段的技术元模型id */
    @Excel(name = "节点对应字段的技术元模型id")
    @ApiModelProperty(value = "节点对应字段的技术元模型id", name = "metaModelTechId", required = false, example = "")
    private Long metaModelTechId;

    /** 所属公共资源树信息id */
    @Excel(name = "所属公共资源树信息id")
    @ApiModelProperty(value = "所属公共资源树信息id", name = "treeId", required = false, example = "")
    private Long treeId;

    /** 对应公共资源树节点id */
    @Excel(name = "对应公共资源树节点id")
    @ApiModelProperty(value = "对应公共资源树节点id", name = "nodeId", required = false, example = "")
    private Long nodeId;

    /** 维护时是否使用资源树获取数据（字段不使用） */
    @Excel(name = "维护时是否使用资源树获取数据", readConverterExp = "字段不使用")
    private String useTreeData;

    /** 使用节点是否最终叶子节点 */
    @Excel(name = "使用节点是否最终叶子节点")
    @ApiModelProperty(value = "使用节点是否最终叶子节点", name = "isLastNode", required = false, example = "")
    private String isLastNode;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMetaModelId(Long metaModelId)
    {
        this.metaModelId = metaModelId;
    }

    public Long getMetaModelId()
    {
        return metaModelId;
    }
    public void setMetaModelTechId(Long metaModelTechId)
    {
        this.metaModelTechId = metaModelTechId;
    }

    public Long getMetaModelTechId()
    {
        return metaModelTechId;
    }
    public void setTreeId(Long treeId)
    {
        this.treeId = treeId;
    }

    public Long getTreeId()
    {
        return treeId;
    }
    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }

    public Long getNodeId()
    {
        return nodeId;
    }
    public void setUseTreeData(String useTreeData)
    {
        this.useTreeData = useTreeData;
    }

    public String getUseTreeData()
    {
        return useTreeData;
    }
    public void setIsLastNode(String isLastNode)
    {
        this.isLastNode = isLastNode;
    }

    public String getIsLastNode()
    {
        return isLastNode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("metaModelId", getMetaModelId())
            .append("metaModelTechId", getMetaModelTechId())
            .append("treeId", getTreeId())
            .append("nodeId", getNodeId())
            .append("useTreeData", getUseTreeData())
            .append("isLastNode", getIsLastNode())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
