package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2022/12/6
 **/
@Data
public class MapMessage {


    @ApiModelProperty("表属性id")
    private String id;

    @ApiModelProperty("表模型id")
    private String modelId;

    @ApiModelProperty("表版本")
    private String modelVersion;

    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("属性中文名称")
    private String label;

    @ApiModelProperty("是否按首字母排序")
    private String ifQuery;

    @ApiModelProperty("是否展开更多-前台需要")
    private Boolean ifOpen;

    @ApiModelProperty("是否多选-前台需要")
    private Boolean ifMoreChoose;

    @ApiModelProperty("首字母")
    private String initial;

    @ApiModelProperty("排序")
    private String sort;

    @ApiModelProperty("字典数据")
    private List<DictionariesVo> list = new ArrayList<>();
}
