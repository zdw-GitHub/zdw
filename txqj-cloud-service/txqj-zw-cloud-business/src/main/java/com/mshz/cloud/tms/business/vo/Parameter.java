package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2022/10/14
 **/
@Data
public class Parameter {
    @ApiModelProperty(value = "主属性")
    private String attribute;

    @ApiModelProperty(value = "关联属性")
    private String rattribute;

    @ApiModelProperty(value = "主表")
    private String surface;

    @ApiModelProperty(value = "关联表")
    private String rsurface;

    @ApiModelProperty(value = "主参数属性")
    private String comm;

    @ApiModelProperty(value = "关联参数属性")
    private String rcomm;

    @ApiModelProperty(value = "主参数值")
    private String param;

    @ApiModelProperty(value = "参数值集合")
    private List<String> listParam = new ArrayList<>();

    @ApiModelProperty(value = "最大参数值")
    private String maxParam;

    @ApiModelProperty(value = "最小参数值")
    private String minParam;

    @ApiModelProperty(value = "数字类型参数")
    private Integer numParam;

    @ApiModelProperty(value = "符号")
    private String symbol;

    @ApiModelProperty(value = "符号类型")
    private String symbolCategory;
}
