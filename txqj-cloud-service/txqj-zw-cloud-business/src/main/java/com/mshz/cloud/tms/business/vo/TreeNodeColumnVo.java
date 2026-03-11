package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公共资源树-节点查询资源数据vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "公共资源树-节点查询资源数据vo")
public class TreeNodeColumnVo {

    @ApiModelProperty(value = "节点编号", name = "id")
    private String id;

    @ApiModelProperty(value = "所属公共资源树信息id", name = "treeId")
    private Long treeId;

    @ApiModelProperty(value = "对应公共资源树节点id", name = "nodeId")
    private Long nodeId;

    @ApiModelProperty(value = "对应节点查询字段名称", name = "nodeQueryColumn")
    private String nodeQueryColumn;

    @ApiModelProperty(value = "对应节点查询字段别名", name = "nodeQueryColumnAlias")
    private String nodeQueryColumnAlias;

    /** 数据对应的元模型字段id */
    @ApiModelProperty(value = "数据对应的元模型字段id", name = "metaDataColumnId")
    private Long metaDataColumnId;

    /** 数据对应的元模型字段名称 */
    @ApiModelProperty(value = "数据对应的元模型字段名称", name = "metaDataColumnName")
    private String metaDataColumnName;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
}
