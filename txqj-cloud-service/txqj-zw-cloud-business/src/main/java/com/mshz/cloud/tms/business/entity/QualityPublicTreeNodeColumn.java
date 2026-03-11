package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 公共树形组件-树节点查询数据属性对象 t_quality_public_tree_node_column
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@ApiModel(description = "公共树形组件-树节点查询数据属性对象")
public class QualityPublicTreeNodeColumn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 所属公共资源树信息id */
    @Excel(name = "所属公共资源树信息id")
    @ApiModelProperty(value = "所属公共资源树信息id", name = "treeId")
    private Long treeId;

    /** 对应公共资源树节点id */
    @Excel(name = "对应公共资源树节点id")
    @ApiModelProperty(value = "对应公共资源树节点id", name = "nodeId")
    private Long nodeId;

    /** 对应节点查询字段名称 */
    @Excel(name = "对应节点查询字段名称")
    @ApiModelProperty(value = "对应节点查询字段名称", name = "nodeQueryColumn")
    private String nodeQueryColumn;

    /** 对应节点查询字段中文名(备注) */
    @Excel(name = "对应节点查询字段别名")
    @ApiModelProperty(value = "对应节点查询字段别名", name = "nodeQueryColumnAlias")
    private String nodeQueryColumnAlias;

    /** 数据对应的元模型字段id */
    @ApiModelProperty(value = "数据对应的元模型字段id", name = "metaDataColumnId")
    private Long metaDataColumnId;

    /** 数据对应的元模型字段名称 */
    @ApiModelProperty(value = "数据对应的元模型字段名称", name = "metaDataColumnName")
    private String metaDataColumnName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setNodeQueryColumn(String nodeQueryColumn)
    {
        this.nodeQueryColumn = nodeQueryColumn;
    }

    public String getNodeQueryColumn()
    {
        return nodeQueryColumn;
    }

    public String getNodeQueryColumnAlias() {
        return nodeQueryColumnAlias;
    }

    public void setNodeQueryColumnAlias(String nodeQueryColumnAlias) {
        this.nodeQueryColumnAlias = nodeQueryColumnAlias;
    }

    public Long getMetaDataColumnId() {
        return metaDataColumnId;
    }

    public void setMetaDataColumnId(Long metaDataColumnId) {
        this.metaDataColumnId = metaDataColumnId;
    }

    public String getMetaDataColumnName() {
        return metaDataColumnName;
    }

    public void setMetaDataColumnName(String metaDataColumnName) {
        this.metaDataColumnName = metaDataColumnName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("treeId", getTreeId())
            .append("nodeId", getNodeId())
            .append("nodeQueryColumn", getNodeQueryColumn())
            .append("nodeQueryColumnAlias", getNodeQueryColumnAlias())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
