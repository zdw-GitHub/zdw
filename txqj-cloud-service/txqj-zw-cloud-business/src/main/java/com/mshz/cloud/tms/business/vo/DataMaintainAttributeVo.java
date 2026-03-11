package com.mshz.cloud.tms.business.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据维护查询模型")
public class DataMaintainAttributeVo {

    @ApiModelProperty(value = "属性名称", name = "attributeName", required = false, example = "")
    private String attributeName;

    @ApiModelProperty(value = "属性值", name = "attributeValue", required = false, example = "")
    private String attributeValue;

    @ApiModelProperty(value = "逻辑关系", name = "logicalRelationSymbol", required = false, example = "")
    private String logicalRelationSymbol;

    @ApiModelProperty(value = "操作符", name = "operator", required = false, example = "")
    private String operator;

//    @ExcelProperty(value = {"是否主键  0 否  1 是","是否主键   0 否  1 是"})
    private String isPrimaryKey;

    /** 关联查询条件  and   or */
    @ApiModelProperty(value = "关联查询条件  and   or", name = "relateSearchCondition", required = false, example = "")
    private String relateSearchCondition;

    /** 关联查询属性 */
    @ApiModelProperty(value = "关联查询属性", name = "relateSearchAttribute", required = false, example = "")
    private String relateSearchAttribute;

    public DataMaintainAttributeVo() {
    }

    public DataMaintainAttributeVo(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
