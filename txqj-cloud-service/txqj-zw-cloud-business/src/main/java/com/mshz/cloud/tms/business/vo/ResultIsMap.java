package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by hzy 2022/10/14
 **/
@Data
public class ResultIsMap {
    @ApiModelProperty(value = "对象id")
    private String ke;

    @ApiModelProperty(value = "名称")
    private String value;

    @ApiModelProperty(value = "属性值")
    private String att;
}
