package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.mapper.TMetaModelOperatorLogMapper;
import com.mshz.cloud.tms.business.entity.TMetaModelOperatorLog;
import com.mshz.cloud.tms.business.service.TMetaModelOperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模型操作日志Service业务层处理
 *
 * @author zxx
 * @date 2022-09-27
 */
@Service
public class TMetaModelOperatorLogServiceImpl implements TMetaModelOperatorLogService
{
    @Autowired
    private TMetaModelOperatorLogMapper tMetaModelOperatorLogMapper;


    /**
     * 查询模型操作日志列表
     *
     * @param tMetaModelOperatorLog 模型操作日志
     * @param startTime
     * @param endTime
     * @return 模型操作日志
     */
    @Override
    public List<TMetaModelOperatorLog> selectTMetaModelOperatorLogList(TMetaModelOperatorLog tMetaModelOperatorLog, String startTime, String endTime)
    {
        return tMetaModelOperatorLogMapper.selectTMetaModelOperatorLogList(tMetaModelOperatorLog,startTime,endTime);
    }


}
