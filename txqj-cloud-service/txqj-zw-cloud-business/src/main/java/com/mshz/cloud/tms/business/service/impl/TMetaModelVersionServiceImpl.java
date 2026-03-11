package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaModelVersion;
import com.mshz.cloud.tms.business.mapper.TMetaModelVersionMapper;
import com.mshz.cloud.tms.business.service.TMetaModelVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 版本管理Service业务层处理
 *
 * @author zxx
 * @date 2022-10-11
 */
@Service
public class TMetaModelVersionServiceImpl implements TMetaModelVersionService
{
    @Autowired
    private TMetaModelVersionMapper tMetaModelVersionMapper;

    /**
     * 查询版本管理
     *
     * @param id 版本管理主键
     * @return 版本管理
     */
    @Override
    public TMetaModelVersion selectTMetaModelVersionById(Long id)
    {
        return tMetaModelVersionMapper.selectTMetaModelVersionById(id);
    }

    /**
     * 查询版本管理列表
     *
     * @param tMetaModelVersion 版本管理
     * @return 版本管理
     */
    @Override
    public List<TMetaModelVersion> selectTMetaModelVersionList(TMetaModelVersion tMetaModelVersion)
    {
        return tMetaModelVersionMapper.selectTMetaModelVersionList(tMetaModelVersion);
    }

    /**
     * 新增版本管理
     *
     * @param tMetaModelVersion 版本管理
     * @return 结果
     */
    @Override
    public int insertTMetaModelVersion(TMetaModelVersion tMetaModelVersion)
    {
        return tMetaModelVersionMapper.insertTMetaModelVersion(tMetaModelVersion);
    }

    /**
     * 修改版本管理
     *
     * @param tMetaModelVersion 版本管理
     * @return 结果
     */
    @Override
    public int updateTMetaModelVersion(TMetaModelVersion tMetaModelVersion)
    {
        return tMetaModelVersionMapper.updateTMetaModelVersion(tMetaModelVersion);
    }

    /**
     * 批量删除版本管理
     *
     * @param ids 需要删除的版本管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelVersionByIds(Long[] ids)
    {
        return tMetaModelVersionMapper.deleteTMetaModelVersionByIds(ids);
    }

    /**
     * 删除版本管理信息
     *
     * @param id 版本管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelVersionById(Long id)
    {
        return tMetaModelVersionMapper.deleteTMetaModelVersionById(id);
    }
}
