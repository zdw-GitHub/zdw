package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2022/11/14
 **/
@Data
public class DropDownVo {
    private String name;
    private List<DictionariesVo> list = new ArrayList<>();
}
