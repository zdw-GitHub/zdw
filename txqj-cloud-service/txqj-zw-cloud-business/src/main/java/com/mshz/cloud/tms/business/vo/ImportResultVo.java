package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.List;

/**
 * Excel导入结果VO
 */
@Data
public class ImportResultVo {
    /**
     * 总上传条数
     */
    private Integer totalCount;
    /**
     * 成功入库条数
     */
    private Integer successCount;
    /**
     * 失败条数
     */
    private Integer failCount;
    /**
     * 失败行错误信息（例：["第2行：名称不能为空", "第5行：电压等级编码不存在"]）
     */
    private List<String> errorMsgList;
}
