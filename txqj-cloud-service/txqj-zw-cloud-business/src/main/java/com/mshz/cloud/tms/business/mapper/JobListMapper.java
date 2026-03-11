package com.mshz.cloud.tms.business.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author david
 * * @date 2023/8/30
 */

@Mapper
public interface JobListMapper {
    List<String> getOpticalList();

    List<Map> selectChannelNumList();

    List<Map> selectSegNumList();

    List<Map> selectSegLineNumList();

    List<Map> selectRouteLengthList();

    int updataSgTcconTcopticalroadS(@Param("id") String id,@Param("channelNum")long channelNum,@Param("segNum") long segNum,@Param("segLineNum") long segLineNum,@Param("routeLength") BigDecimal routeLength);

    int insertSgTcconTcopticalroadS(@Param("id") String id,@Param("channelNum")long channelNum,@Param("segNum") long segNum,@Param("segLineNum") long segLineNum,@Param("routeLength") BigDecimal routeLength);

    String selectSgTcconTcopticalroadSById(String optical);
}
