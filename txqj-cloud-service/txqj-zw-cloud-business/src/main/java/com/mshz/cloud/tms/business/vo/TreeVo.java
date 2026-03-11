package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wh
 * @date 2022/10/17
 */
@Data
public class TreeVo {

    @ApiModelProperty(value = "编号", name = "id", required = false, example = "")
    private String id;

    @ApiModelProperty(value = "标识", name = "name", required = false, example = "")
    private String name;

    @ApiModelProperty(value = "名称", name = "label", required = false, example = "")
    private String label;

    @ApiModelProperty(value = "节点类型", name = "nodeType", required = false, example = "")
    private String type;

    private String parentId;

    private String auditMode;

    private Long rlModelId;

    @ApiModelProperty(value = "子节点", name = "children", required = false, example = "")
    private List<TreeVo> children;



}
