package com.mshz.cloud.tms.business.vo;

import lombok.Data;

@Data
public class ColumnNameInfoVo {
    /**列名称*/
    private String columnName;

    /**数据类型*/
    private String dataType;

    /**字符最大长度*/
    private Long characterMaximumLength;

    /**列类型*/
    private String columnType;

    /**列关键字*/
    private String columnKey;

    /**可为null*/
    private String isNullable;

    /**列注释*/
    private String columnComment;
}
