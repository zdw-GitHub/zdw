package com.mshz.cloud.tms.common.modules.system.doc.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysDictVo {
    private Integer id;
    private Integer pid;   // 父ID
    private String code;
    private String name;
    private Integer sort;
    private List<SysDictVo> children = new ArrayList<>();


    public void addChild(SysDictVo child) {
        this.children.add(child);
    }
}
