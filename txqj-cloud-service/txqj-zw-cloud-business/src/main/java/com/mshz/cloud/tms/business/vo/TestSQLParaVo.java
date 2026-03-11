package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TestSQLParaVo {

    @ApiModelProperty(value = "元模型编号", name = "metaModelId", required = false, example = "")
    private String metaModelId;

    @ApiModelProperty(value = "元模型版本号", name = "modelVersion", required = false, example = "")
    private String metaModelVersion;

    @ApiModelProperty(value = "条件类型 conditon_type_meta_model 元模型同步条件，conditon_type_attribute_transformation 属性转换条件，conditon_type_sql_transformation sql转换条件", name = "conditionType", required = false, example = "")
    private String conditionType;

    @ApiModelProperty(value = "条件内容", name = "conditionContent", required = false, example = "")
    private String conditionContent;

    @ApiModelProperty(value = "页码", name = "pageIndex", required = false, example = "")
    private int pageIndex;

    @ApiModelProperty(value = "分页大小", name = "pageSize", required = false, example = "")
    private int pageSize;

    @ApiModelProperty(value = "要执行的sql", name = "sql", required = false, example = "")
    private String sql;
}
