package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据维护查询模型")
public class MetaDataAttributeVo {

    @ApiModelProperty(value = "属性名称", name = "attributeName", required = false, example = "")
    private String attributeName;

    @ApiModelProperty(value = "属性值", name = "attributeValue", required = false, example = "")
    private String attributeValue;

    @ApiModelProperty(value = "是否主键  0 否  1 是", required = false, example = "")
    private String isPrimaryKey;
}
