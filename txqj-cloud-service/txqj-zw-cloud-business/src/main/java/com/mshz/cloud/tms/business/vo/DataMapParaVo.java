package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jerry
 * @date 2023/07/31
 *
 * 数据地图参数
 */
@Data
public class DataMapParaVo {
    /**
     *  编号id
     */
    @ApiModelProperty(value = "编号id", name = "id", required = false, example = "")
    private Long id;

    /**
     *  数据类目  type_meta_model  type_meta_category
     */
    @ApiModelProperty(value = "数据类目  type_meta_model  type_meta_category", name = "dataType", required = false, example = "")
    private String dataType;

    /**
     *  数据名称
     */
    private String dataName;

}
