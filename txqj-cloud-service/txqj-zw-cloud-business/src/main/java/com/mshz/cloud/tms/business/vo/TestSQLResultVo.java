package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.List;

/**
 * SQL测试的结果
 */
@Data
public class TestSQLResultVo {
    /**
     * success 成功， fail 失败
     */
    private String code;

    private String sql;

    private String error;

    private List data;
}
