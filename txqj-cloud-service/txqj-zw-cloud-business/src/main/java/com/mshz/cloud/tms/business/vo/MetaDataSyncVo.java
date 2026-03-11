package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * @author wh
 * @date 2022/10/20
 */
@Data
public class MetaDataSyncVo {

    private String dbUserName;

    private String tableName;

    private String relationTableAlias;

    private int isExternalConnection;

    private String columnName;

    private String columnType;

    private String modelVersionAlias;

    private String targetColumnName;

    private String targetColumnType;

    private String relationCondition;

    private String relationType;

    private String sqlContent;

    private String isPrimaryKey;

    private String updateOrNot;


}
