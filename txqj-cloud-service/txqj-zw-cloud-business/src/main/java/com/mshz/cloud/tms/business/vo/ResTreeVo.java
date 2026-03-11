package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通信资源维护-公共树形组件数据查询vo
 * @author Liuyue
 */
@Data
public class ResTreeVo {

    /** 元模型id */
    @ApiModelProperty(value = "元模型id", name = "metaModeId")
    private String metaModeId;

    @ApiModelProperty(value = "树id", name = "treeId", required = false, example = "")
    private String treeId;

    @ApiModelProperty(value = "节点id", name = "nodeId")
    private String nodeId;

    @ApiModelProperty(value = "节点层级", name = "nodeLevel", required = false, example = "")
    private String nodeLevel;

    @ApiModelProperty(value = "当前节点数据-编码", name = "dataCode", required = false, example = "")
    private String dataCode;

    @ApiModelProperty(value = "当前节点数据-名称", name = "dataName", required = false, example = "")
    private String dataName;


    /** 资源树数据查询分页用参数 */
    @ApiModelProperty(value = "当前页数，资源树数据查询分页参数", name = "pageNum", required = false, example = "")
    private String pageNum;

    @ApiModelProperty(value = "每页数据量，资源树数据查询分页参数", name = "pageSize", required = false, example = "")
    private String pageSize;

}
