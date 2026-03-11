package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jerry
 * 数据地图数据模型
 */
@Data
public class MapDataVo {
    private String id;
    private String name;
    /**
     *  数据类目  type_meta_model  type_meta_category
     */
    @ApiModelProperty(value = "数据类目  type_meta_model  type_meta_category   type_table type_db", name = "dataType", required = false, example = "")
    private String dataType;
}
