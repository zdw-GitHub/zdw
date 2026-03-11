package com.mshz.cloud.tms.common.modules.mybatis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @msg 查询参数
 * @author wangzt
 */
@Data
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;

    @ApiModelProperty(value = "需要驼峰转换蛇形", notes = "一般不做处理，如果数据库中就是蛇形，则这块需要处理。")
    private Boolean notConvert;
}
