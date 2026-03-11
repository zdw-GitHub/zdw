package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TMetaModelOperatorLog;
import java.util.List;

/**
 * 模型操作日志Service接口
 *
 * @author zxx
 * @date 2022-09-27
 */
public interface TMetaModelOperatorLogService
{

    /**
     * 查询模型操作日志列表
     *
     * @param tMetaModelOperatorLog 模型操作日志
     * @param startTime
     * @param endTime
     * @return 模型操作日志集合
     */
    public List<TMetaModelOperatorLog> selectTMetaModelOperatorLogList(TMetaModelOperatorLog tMetaModelOperatorLog, String startTime, String endTime);




}
