package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * Create by hzy 2022/11/30
 **/
@Data
public class QuantityStatistics {

    private String synchronizedQuantity;

    private String unsynchronizedQuantity;

    private String numberFailures;

    private String total;

    private String errorDescribe;

    public String getErrorDescribe() {
        if (errorDescribe!=null&&errorDescribe.length()>1900){
            return errorDescribe.substring(0,1900);
        }else {
            return errorDescribe;
        }

    }
}
