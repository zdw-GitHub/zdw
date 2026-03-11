package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * @author Jerry
 * 数据地图联线模型
 */
@Data
public class MapLinkVo {
    private String sourceId;
    private String source;
    private String targetId;
    private String target;

}
