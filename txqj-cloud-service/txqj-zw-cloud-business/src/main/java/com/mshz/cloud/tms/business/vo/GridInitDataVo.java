package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GridInitDataVo {

    @ApiModelProperty(value = "搜索条件", name = "searchAttributes", required = false, example = "")
    private List<GridColumnVo> searchAttributes;

    @ApiModelProperty(value = "列表属性", name = "columnName", required = false, example = "")
    private List<GridColumnVo> columns;
}
