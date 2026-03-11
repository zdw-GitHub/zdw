package com.mshz.cloud.tms.business.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公共资源树信息vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "公共资源树-公共资源树信息vo")
public class TreeInfoVo {

    @ApiModelProperty(value = "资源数据树信息对象", name = "treeInfo")
    private QualityPublicTreeInfo treeInfo;

    @ApiModelProperty(value = "节点信息vo-根节点", name = "treeNodeInfo")
    private TreeNodeVo treeNodeInfo;

}
