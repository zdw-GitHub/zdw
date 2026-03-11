package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 唯一标识vo
 * @author Liuyue
 */
@Data
@ApiModel(description = "唯一标识vo")
public class MetaUidVo {

    /**
     * 元模型id
     */
    @ApiModelProperty(value = "元模型id", name = "modelId", required = true, notes = "当前数据表为元模型表时，值为元模型id；当前数据为源数据模型表时，值为源数据表转换映射的元模型id")
    private Long modelId;

    @ApiModelProperty(value = "元模型唯一标识信息集合", name = "metaUidTableVoList", notes = "包括组成元模型唯一标识的字段信息，和组成元模型映射源数据模型的唯一标识信息。组成元模型唯一标识的字段信息固定为集合第一个元素")
    private List<MetaUidTableVo> metaUidTableVoList;

}
