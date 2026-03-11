package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TMetaModelRelease;
import com.mshz.cloud.tms.business.vo.MetadataBuzInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 元模型发布Mapper接口
 *
 * @author zxx
 * @date 2022-09-28
 */
@Mapper
public interface TMetaModelReleaseMapper
{
    /**
     * 查询元模型发布-拷贝
     *
     * @param modelId 元模型发布主键
     * @param modelVersion
     * @return 元模型发布
     */
    public TMetaModelRelease selectTMetaModelReleaseCopy(@Param("modelId") Long modelId, @Param("modelVersion")  String modelVersion);
    /**
     * 新增元模型发布
     *
     * @param tMetaModelRelease 元模型发布
     * @return 结果
     */
    public int insertTMetaModelRelease(TMetaModelRelease tMetaModelRelease);

    /**
     * 查询元模型发布列表
     *
     * @param tMetaModelRelease 元模型发布
     * @return 元模型发布集合
     */
    public List<TMetaModelRelease> selectTMetaModelReleaseList(TMetaModelRelease tMetaModelRelease);

    /**
     *  历史版本管理
     * @param tMetaModelRelease
     * @param startTime
     * @param endTime
     * @return
     */
    List<TMetaModelRelease> selectHistoricalVersionManagement(@Param("r") TMetaModelRelease tMetaModelRelease,@Param("start")  String startTime,@Param("end")  String endTime);

    MetadataBuzInfoVo selectTMetaModelRelease(@Param("modelId")Long modelId,@Param("modelVersion") String modelVersion);
}
