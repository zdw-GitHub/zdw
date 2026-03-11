package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaModelRelease;
import com.mshz.cloud.tms.business.mapper.TMetaModelReleaseMapper;
import com.mshz.cloud.tms.business.service.TMetaModelReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/10/10
 */
@Service
public class TMetaModelReleaseServiceImpl implements TMetaModelReleaseService {
    @Autowired
    private TMetaModelReleaseMapper tMetaModelReleaseMapper;

    /**
     *  历史版本管理
     * @param tMetaModelRelease
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<TMetaModelRelease> selectHistoricalVersionManagement(TMetaModelRelease tMetaModelRelease, String startTime, String endTime) {

        return tMetaModelReleaseMapper.selectHistoricalVersionManagement(tMetaModelRelease,   startTime, endTime);
    }
}
