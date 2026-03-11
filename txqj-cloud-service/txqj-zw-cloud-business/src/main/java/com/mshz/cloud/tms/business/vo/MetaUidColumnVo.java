package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 唯一标识组成字段vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "唯一标识组成字段vo")
public class MetaUidColumnVo {

    /**
     * 元模型id
     */
    @ApiModelProperty(value = "元模型id", name = "modelId", required = true, notes = "当前数据表为元模型表时，值为元模型id；当前数据为源数据模型表时，值为源数据表转换映射的元模型id")
    private Long modelId;

    /**
     * 模型类型，meta：元模型；source：采集源模型
     * 用于标识传入参数数据表示的是元模型还是采集源模型
     */
    @ApiModelProperty(value = "模型类型，meta：元模型；source：采集源模型", name = "modelType", required = true, example = "meta")
    private String modelType;

    /**
     * 采集源数据库id
     */
    @ApiModelProperty(value = "采集源数据库id", name = "dbId", required = false, example = "")
    private Long dbId;

    /**
     * 采集源模型id
     */
    @ApiModelProperty(value = "采集源模型id", name = "tableId", required = false, example = "")
    private Long tableId;

    /**
     * 组成 唯一标识 的表字段id（元模型或采集源模型）
     */
    @ApiModelProperty(value = "组成唯一标识的表字段id（元模型或采集源模型）", name = "columnId", required = false, example = "")
    private Long columnId;

    /**
     * 组成唯一标识的表字段名称（元模型或采集源模型）
     */
    @ApiModelProperty(value = "组成唯一标识的表字段名称（元模型或采集源模型）", name = "columnName", required = false, example = "")
    private String columnName;

    /**
     * 组成唯一标识的表字段名称（元模型或采集源模型）
     */
    @ApiModelProperty(value = "组成唯一标识的表字段中文名（元模型或采集源模型）", name = "columnNameCn", required = false, example = "")
    private String columnNameCn;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "元模型版本号", name = "modelVersion", required = false, example = "")
    private String modelVersion;



}
