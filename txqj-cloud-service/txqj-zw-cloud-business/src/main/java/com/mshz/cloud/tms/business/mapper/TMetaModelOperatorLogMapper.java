package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TMetaModelOperatorLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型操作日志Mapper接口
 *
 * @author zxx
 * @date 2022-09-27
 */
@Mapper
public interface TMetaModelOperatorLogMapper
{


    /**
     * 查询模型操作日志列表
     *
     * @param tMetaModelOperatorLog 模型操作日志
     * @param startTime
     * @param endTime
     * @return 模型操作日志集合
     */
    public List<TMetaModelOperatorLog> selectTMetaModelOperatorLogList(@Param("o") TMetaModelOperatorLog tMetaModelOperatorLog, @Param("start")  String startTime, @Param("end")  String endTime);


}
