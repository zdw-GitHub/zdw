package com.mshz.cloud.tms.business.mapper;



import com.mshz.cloud.tms.business.entity.TMetaModelVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版本管理Mapper接口
 *
 * @author zxx
 * @date 2022-10-11
 */
@Mapper
public interface TMetaModelVersionMapper
{
    /**
     * 查询版本管理
     *
     * @param id 版本管理主键
     * @return 版本管理
     */
    public TMetaModelVersion selectTMetaModelVersionById(Long id);

    /**
     * 查询版本管理列表
     *
     * @param tMetaModelVersion 版本管理
     * @return 版本管理集合
     */
    public List<TMetaModelVersion> selectTMetaModelVersionList(TMetaModelVersion tMetaModelVersion);

    /**
     * 新增版本管理
     *
     * @param tMetaModelVersion 版本管理
     * @return 结果
     */
    public int insertTMetaModelVersion(TMetaModelVersion tMetaModelVersion);

    /**
     * 修改版本管理
     *
     * @param tMetaModelVersion 版本管理
     * @return 结果
     */
    public int updateTMetaModelVersion(TMetaModelVersion tMetaModelVersion);

    /**
     * 删除版本管理
     *
     * @param id 版本管理主键
     * @return 结果
     */
    public int deleteTMetaModelVersionById(Long id);

    /**
     * 批量删除版本管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelVersionByIds(Long[] ids);

    /**
     * 首先根据modelId和modelVersion查询版本别名
     * @param modelVersion
     * @param modelId
     * @return
     */
    String selectModelVersionAlias(@Param("modelVersion") String modelVersion, @Param("modelId") Long modelId);
}
