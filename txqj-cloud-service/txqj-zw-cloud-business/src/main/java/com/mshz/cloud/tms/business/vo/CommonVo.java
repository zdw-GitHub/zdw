
package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "通用查询模型")
public class CommonVo {

    @ApiModelProperty(value = "表名", name = "tableName", required = false, example = "")
    private String tableName;

    @ApiModelProperty(value = "页码", name = "pageIndex", required = false, example = "")
    private int pageIndex;

    @ApiModelProperty(value = "分页大小", name = "pageSize", required = false, example = "")
    private int pageSize;

    private int offset;

    @ApiModelProperty(value = "sql", name = "sql", required = false, example = "")
    private String sql;

    @ApiModelProperty(value = "attribute", name = "attribute", required = false, example = "")
    private String attributeName;

    @ApiModelProperty(value = "数据库名", name = "dbName", required = false, example = "")
    private String dbName;

}
