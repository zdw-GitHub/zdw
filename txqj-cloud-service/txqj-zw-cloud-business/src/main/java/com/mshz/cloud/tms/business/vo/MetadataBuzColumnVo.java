package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/24
 */
@Data
public class MetadataBuzColumnVo {
    /*技术元数据表*/
    /**技术属性id(技术元数据表ID)*/
    private  Long techId;
    /**属性名称*/
    @ApiModelProperty("属性名称")
    private  String columnName;
    /**属性名称中文*/
    @ApiModelProperty("属性名称中文")
    private  String columnNameCn;

    /*业务元数据表*/
    /**业务属性id(业务元数据表ID)*/
    @ApiModelProperty("业务属性id(业务元数据表ID)")
    private  Long buzId;
    /**数值范围类型*/
    @ApiModelProperty("数值范围类型")
    private  String rangeTypeNo;
    /**取值条件**/
    @ApiModelProperty("取值条件")
    private  String dataRange;
    /**业务属性备注*/
    @ApiModelProperty("业务属性备注")
    private  String remark;
}
