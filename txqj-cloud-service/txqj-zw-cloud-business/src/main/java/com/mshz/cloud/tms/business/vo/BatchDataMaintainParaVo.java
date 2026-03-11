package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "批量数据维护入参")
public class BatchDataMaintainParaVo {
    private List<DataMaintainParaVo>  paras;
}
