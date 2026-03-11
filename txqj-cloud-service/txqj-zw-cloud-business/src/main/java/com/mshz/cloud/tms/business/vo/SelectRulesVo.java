package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * Create by hzy 2022/11/2
 **/
@Data
public class SelectRulesVo {
    private String id;
    private String columnName;
    private String columnNameCn;
    private String ruleDescription;
    private String alarmLevel;
    private String modificationSuggestions;
    private String ruleName;
    private String customizedSql;
    private String superiorUnitId;
    private String ruleCategory;
}
