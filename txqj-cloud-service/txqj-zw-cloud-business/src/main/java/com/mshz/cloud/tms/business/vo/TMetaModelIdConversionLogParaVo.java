package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.tms.business.entity.TMetaModelIdConversionLog;
import lombok.Data;

/**
 * id中转换日志对象 t_meta_model_id_conversion_log
 *
 * @author zdw
 * @date 2023-08-09
 */
@Data
public class TMetaModelIdConversionLogParaVo extends TMetaModelIdConversionLog
{

    private String startTime;

    private String endTime;

}
