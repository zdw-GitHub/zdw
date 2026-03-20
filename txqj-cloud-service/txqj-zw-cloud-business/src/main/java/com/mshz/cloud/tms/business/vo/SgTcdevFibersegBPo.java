package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegB;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SgTcdevFibersegBPo extends SgTcdevFibersegB {

    @ApiModelProperty(" 拼接的主键字符串")
    @Excel(name = "拼接的主键字符串")
    private String idstr;

    @ApiModelProperty(" 拼接的主键字符串")
    @Excel(name = "拼接的主键字符串")
    private String notinidstr;
}
