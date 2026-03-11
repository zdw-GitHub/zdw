package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2022/11/4
 **/
@Data
public class GuideVo {
    private String id;
    private String name;
    private String label;
    private String ruleId;
    private String vruleId;
    private String rules;
    private String type;
    private String englishName;
    private String columnName;
    private String columnNameCn;
    private Integer isNotNull;
    private List<DictionariesVo> list = new ArrayList<>();
}
