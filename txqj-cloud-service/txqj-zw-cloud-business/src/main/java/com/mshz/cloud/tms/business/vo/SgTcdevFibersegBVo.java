package com.mshz.cloud.tms.business.vo;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegB;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SgTcdevFibersegBVo extends SgTcdevFibersegB {

    @ApiModelProperty(" 审核状态 0无审核 1正在审核 2已审核")
    @Excel(name = "审核状态 0无审核  1正在审核  2已审核")
    private String auditStatus;

    @ApiModelProperty("所属光缆")
    @Excel(name = "所属光缆")
    private String fiberId;

}
