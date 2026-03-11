package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Create by hzy 2023/1/11
 *
 * @author Liuyue
 * */
@Data
public class ProcessVo {

    private Map<String, Object> variables;

    private String modeId;

    private List<String> selectList;

    /**
     * 2023-03-13添加。存放流程选中数据对应的元模型唯一标识字段的键值对
     */
    private List<Map<String, String>> selectUidList;

    private Object data;

    private String type;

}
