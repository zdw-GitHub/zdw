package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaModelRelease;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/10/10
 */
public interface TMetaModelReleaseService {
    /**
     *  历史版本管理
     * @param tMetaModelRelease
     * @param startTime
     * @param endTime
     * @return
     */
    List<TMetaModelRelease> selectHistoricalVersionManagement(TMetaModelRelease tMetaModelRelease, String startTime, String endTime);
}
