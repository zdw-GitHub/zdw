package com.mshz.cloud.tms.business.vo;

import lombok.Data;

/**
 * 元模型展示配置更新结果VO
 * Create by 你的名字 2026/01/23
 **/
@Data
public class MetaModelShowUpdateResultVo {
    /**
     * 整体操作是否成功
     */
    private boolean success;

    /**
     * 主表（TMetaModelShow）更新行数
     */
    private int updateCount;

    /**
     * 样式表（TMetaModelShowStyle）删除条数
     */
    private int deleteStyleCount;

    /**
     * 样式表（TMetaModelShowStyle）新增条数
     */
    private int insertStyleCount;

    /**
     * 失败原因（成功时为null）
     */
    private String errorMsg;

    // 可选扩展字段（根据你的业务需求保留/删除）
    private String operateTime;
    private String modelId;
    private String columnId;
}
