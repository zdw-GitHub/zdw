package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 导入模板下载参数VO
 */
@Data
public class ImportTemplateParaVo {
    /**
     * 模型ID（核心参数，关联t_meta_model表）
     */
    private String modelId;

    @ApiModelProperty(value = "是否分页  1 是 0 否 ", name = "isPage", required = false, example = "")
    private int isPage = 1;

    @ApiModelProperty(value = "查询条件", name = "whereAttributes", required = false, example = "")
    private List<DataMaintainAttributeVo> whereAttributes;
}
