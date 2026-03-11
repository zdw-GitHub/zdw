package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import lombok.Data;

/**
 * 采集元模型查询条件对象 t_db_table_condition
 *
 * @author zdw
 * @date 2023-02-15
 */

@Data
public class TDbTableCondition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 所属表编号 */
    @Excel(name = "所属表编号")
    private Long tableId;

    /** 同步条件名称 */
    @Excel(name = "同步条件名称")
    private String conditionName;

    /** 数据查询条件 */
    @Excel(name = "数据查询条件")
    private String searchCondition;

    private String sourceDataCount;


    private String targetDataCount;


    /** 同步时是否删除旧数据（0：不删除；1：删除） */
    @Excel(name = "同步时是否删除旧数据", readConverterExp = "0=：不删除；1：删除")
    private String deleteFlag;


}

