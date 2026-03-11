package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by hzy 2022/12/8
 **/
@Data
public class DictionaryInfoVo {

    @ApiModelProperty("关联选择类型：1 填报   2 表属性选择 ")
    private String type;

    @ApiModelProperty("关联模型属性 id 名称")
    private String id;

    @ApiModelProperty("关联模型属性 name 名称")
    private String name;

    @ApiModelProperty("自定义sql")
    private String cuzSql;

    @ApiModelProperty("关联表模型名称")
    private String tableName;

    @ApiModelProperty("首字母")
    private String initial;
}
