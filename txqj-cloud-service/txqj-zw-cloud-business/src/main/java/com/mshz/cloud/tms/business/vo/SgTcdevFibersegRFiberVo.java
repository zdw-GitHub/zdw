package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegRFiber;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SgTcdevFibersegRFiberVo extends SgTcdevFibersegRFiber {

    @ApiModelProperty("审核状态 0无审核 1正在审核 2已审核")
    @Excel(name = "审核状态 0无审核  1正在审核  2已审核")
    private String auditStatus;

}
