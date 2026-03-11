package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 唯一标识组成字段所在表vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "唯一标识组成字段所在表vo")
public class MetaUidTableVo {

    /**
     * 元模型id
     * 当前数据表为元模型表时，值为元模型id；当前数据为源数据模型表时，值为源数据表转换映射的元模型id
     */
    @ApiModelProperty(value = "元模型id", name = "modelId", required = true, notes = "当前数据表为元模型表时，值为元模型id；当前数据为源数据模型表时，值为源数据表转换映射的元模型id")
    private Long modelId;

    /**
     * 表类型，meta：元模型；source：采集源模型
     * 用于标识传入参数数据表示的是元模型还是采集源模型
     */
    @ApiModelProperty(value = "模型类型，meta：元模型；source：采集源模型", name = "modelType", required = true, example = "meta")
    private String modelType;

    /**
     * 采集源数据库id
     */
    @ApiModelProperty(value = "采集源数据库id或元模型所在模型id", name = "dbId", required = false, example = "")
    private Long dbId;

    /**
     * 源数据库名
     */
    @ApiModelProperty(value = "源数据库名或元模型所在模型名", name = "dbName", required = false)
    private String dbName;

    /**
     * 采集源模型id
     */
    @ApiModelProperty(value = "采集源模型id", name = "tableId", required = false, example = "")
    private Long tableId;

    /**
     * 表名
     */
    @ApiModelProperty(value = "表名", name = "tableName", required = false)
    private String tableName;

    /**
     * 表中文名
     */
    @ApiModelProperty(value = "表中文名", name = "tableNameCn", required = false)
    private String tableNameCn;

    /**
     * 唯一标识组成字段集合（集合元素按配置顺序排列）
     */
    @ApiModelProperty(value = "唯一标识组成字段集合", name = "uidColumnList", required = false)
    private List<MetaUidColumnVo> uidColumnList;

}
