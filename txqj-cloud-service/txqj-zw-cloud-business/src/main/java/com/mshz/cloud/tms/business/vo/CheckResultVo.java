package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * Create by hzy 2022/11/2
 **/
@Data
public class CheckResultVo {
    private String id;
    private String name;
    private String siteId;
    private String tmsName;
    private String columnName;
    private String columnNameCn;
    private String ruleName;
    private String ruleDescription;
    private String modificationSuggestions;
    private String resourceType;
    private String modelId;
}
