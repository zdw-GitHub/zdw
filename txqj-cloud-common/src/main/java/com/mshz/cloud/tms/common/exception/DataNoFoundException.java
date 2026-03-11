package com.mshz.cloud.tms.common.exception;

/**
 * 无数据
 * @author yjr
 * @version 1.0.0
 * @date 2024/6/5 15:55
 **/
public class DataNoFoundException extends RuntimeException{
    public DataNoFoundException(String message) {
        super(message);
    }
}
