package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Jerry
 * 数据地图模型
 */
@Data
public class DataMapReturnVo {
    private List<MapDataVo> data;
    private List<MapLinkVo> links;
}
