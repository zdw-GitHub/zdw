package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2022/11/1
 **/
@Data
public class TableRuleVo {
    private String modificationSuggestions;
    private String alarmLevel;
    private String selectType;
    private String tableName;
    private String columnIdName;
    private String columnName;
    private String ruleColumnName;
    private String customizedSql;
    private String ruleId;
    private String ruleName;
    private String ruleDescription;
    private String errorName;
    private String englishName;
    private List<DictionariesVo> dictionaries = new ArrayList<>();
}
