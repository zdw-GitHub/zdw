package com.mshz.cloud.tms.business.entity;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 公共树形组件-树节点对象 t_quality_public_tree_node
 *
 * @author ruoyi
 * @date 2023-05-30
 */
@ApiModel(description = "公共树形组件-树节点对象")
public class QualityPublicTreeNode extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 所属公共资源树信息id */
    @Excel(name = "所属公共资源树信息id")
    private Long treeId;

    /** 节点名称 */
    @Excel(name = "节点名称")
    private String name;

    /** 节点层级 */
    @Excel(name = "节点层级")
    private String nodeLevel;

    /** 父级节点id */
    @Excel(name = "父级节点id")
    private Long parentId;

    /** 节点数据查询sql */
    @Excel(name = "节点数据查询sql")
    private String nodeDataSql;

    /** sql查询数据时使用的父级属性字段id */
    @Excel(name = "sql查询数据时使用的父级属性字段id")
    private Long parentColumnId;

    /** 查询数据时使用的父级属性字段名 */
    @Excel(name = "查询数据时使用的父级属性字段名")
    private String parentColumn;

    @Excel(name = "当前节点是否是树的最终叶子节点")
    private String isLastNode;

    @Excel(name = "当前节点是否支持多级搜索")
    private String isMultistageSearch;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setNodeLevel(String nodeLevel)
    {
        this.nodeLevel = nodeLevel;
    }

    public String getNodeLevel()
    {
        return nodeLevel;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setNodeDataSql(String nodeDataSql)
    {
        this.nodeDataSql = nodeDataSql;
    }

    public String getNodeDataSql()
    {
        return nodeDataSql;
    }
    public void setParentColumnId(Long parentColumnId)
    {
        this.parentColumnId = parentColumnId;
    }

    public Long getParentColumnId()
    {
        return parentColumnId;
    }
    public void setParentColumn(String parentColumn)
    {
        this.parentColumn = parentColumn;
    }

    public String getParentColumn()
    {
        return parentColumn;
    }

    public String getIsLastNode() {
        return isLastNode;
    }

    public void setIsLastNode(String isLastNode) {
        this.isLastNode = isLastNode;
    }

    public String getIsMultistageSearch() {
        return isMultistageSearch;
    }

    public void setIsMultistageSearch(String isMultistageSearch) {
        this.isMultistageSearch = isMultistageSearch;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("treeId", getTreeId())
                .append("name", getName())
                .append("nodeLevel", getNodeLevel())
                .append("parentId", getParentId())
                .append("nodeDataSql", getNodeDataSql())
                .append("parentColumnId", getParentColumnId())
                .append("parentColumn", getParentColumn())
                .append("isLastNode", getIsLastNode())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
