package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 公共资源树vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "公共资源树-节点信息vo")
public class TreeNodeVo {

    @ApiModelProperty(value = "节点编号", name = "id", required = false, example = "")
    private String id;

    @ApiModelProperty(value = "节点所属树信息id", name = "treeId", required = false, example = "")
    private String treeId;

    @ApiModelProperty(value = "标识", name = "name", required = false, example = "")
    private String name;

    @ApiModelProperty(value = "节点层级", name = "nodeLevel", required = false, example = "")
    private String nodeLevel;

    @ApiModelProperty(value = "父级节点id", name = "parentId", required = false, example = "")
    private String parentId;

    @ApiModelProperty(value = "父级节点名称", name = "parentName", required = false, example = "")
    private String parentName;

    @ApiModelProperty(value = "当前节点数据查询sql", name = "nodeDataSql", required = false, example = "")
    private String nodeDataSql;

    @ApiModelProperty(value = "节点父级信息字段", name = "parentColumn", required = false, example = "")
    private String parentColumn;

    @ApiModelProperty(value = "节点信息备注", name = "remark", required = false, example = "")
    private String remark;

    @ApiModelProperty(value = "当前节点是否是树的最终叶子节点", name = "isLastNode", required = false, example = "")
    private String isLastNode;

    @ApiModelProperty(value = "当前节点是否多级搜索", name = "isMultistageSearch", required = false, example = "")
    private String isMultistageSearch;



    @ApiModelProperty(value = "节点对应字段所属的元模型id", name = "metaModelId", required = false, example = "")
    private String metaModelId;

    @ApiModelProperty(value = "节点对应字段的技术元模型id", name = "metaModelTechId", required = false, example = "")
    private String metaModelTechId;


    @ApiModelProperty(value = "子节点集合", name = "children", required = false, example = "")
    private List<TreeNodeVo> children;

    @ApiModelProperty(value = "当前节点查询数据字段信息", name = "nodeColumnList", required = false, example = "")
    private List<TreeNodeColumnVo>  nodeColumnList;


    @ApiModelProperty(value = "当前节点的数据-编码值", name = "dataCodeValue", required = false, example = "")
    private String dataCodeValue;

    @ApiModelProperty(value = "当前节点的数据-名称值", name = "dataNameValue", required = false, example = "")
    private String dataNameValue;

    @ApiModelProperty(value = "当前节点的数据-表名", name = "tableName", required = false, example = "")
    private String tableName;
}
