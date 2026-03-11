package com.mshz.cloud.tms.business.vo;

import lombok.Data;

@Data
public class IDConversionReturnVo {

    private String id;

    /**
     * 结果编码 success fail
     */
    private String code;

    /**
     * 结果信息
     */
    private String msg;

    private long modelId;

    private String modelName;

    private String modelNameCn;
}
