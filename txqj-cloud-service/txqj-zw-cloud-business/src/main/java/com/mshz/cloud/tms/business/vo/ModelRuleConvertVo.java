package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 元模型数据字段规则执行转换vo
 * @author Liuyue
 */
@Data
public class ModelRuleConvertVo {

    /** 规则编号 */
    @ApiModelProperty(value = "规则id", name = "ruleId", required = false, example = "")
    private Long ruleId;

    /** 表编号 */
    @ApiModelProperty(value = "元模型id", name = "tableId", required = false, example = "")
    private Long tableId;

    /** 属性编号 */
    @ApiModelProperty(value = "元模型字段id", name = "columnId", required = false, example = "")
    private Long columnId;

    /** 待转换元数据id集合 */
    @ApiModelProperty(value = "待转换元数据id集合", name = "metaDataIdList", required = false, example = "")
    private List<String> metaDataIdList;

    /** 转换结果。0：失败；1：成功 */
    @ApiModelProperty(value = "转换结果。0：失败；1：成功", name = "conversionResult", required = false, example = "")
    private String conversionResult;

    /** 转换时间 */
    @ApiModelProperty(value = "转换时间", name = "conversionTime", required = false, example = "")
    private String conversionTime;

    /** 结果信息 */
    @ApiModelProperty(value = "结果信息", name = "resultInfo", required = false, example = "")
    private String resultInfo;

}
