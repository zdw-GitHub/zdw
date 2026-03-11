package com.mshz.cloud.tms.business.service.impl;


import com.mshz.cloud.tms.business.entity.TMetaModelCategory;
import com.mshz.cloud.tms.business.mapper.TMetaModelCategoryMapper;
import com.mshz.cloud.tms.business.service.TMetaModelCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元数据类目Service业务层处理
 *
 * @author zdw
 * @date 2022-09-23
 */
@Service
public class TMetaModelCategoryServiceImpl implements TMetaModelCategoryService
{
    @Autowired
    private TMetaModelCategoryMapper tMetaModelCategoryMapper;

    /**
     * 查询元数据类目
     *
     * @param id 元数据类目主键
     * @return 元数据类目
     */
    @Override
    public TMetaModelCategory selectTMetaModelCategoryById(Long id)
    {
        return tMetaModelCategoryMapper.selectTMetaModelCategoryById(id);
    }

    /**
     * 查询元数据类目列表
     *
     * @param tMetaModelCategory 元数据类目
     * @return 元数据类目
     */
    @Override
    public List<TMetaModelCategory> selectTMetaModelCategoryList(TMetaModelCategory tMetaModelCategory)
    {
        return tMetaModelCategoryMapper.selectTMetaModelCategoryList(tMetaModelCategory);
    }

    /**
     * 新增元数据类目
     *
     * @param tMetaModelCategory 元数据类目
     * @return 结果
     */
    @Override
    public int insertTMetaModelCategory(TMetaModelCategory tMetaModelCategory)
    {

        return tMetaModelCategoryMapper.insertTMetaModelCategory(tMetaModelCategory);
    }

    /**
     * 修改元数据类目
     *
     * @param tMetaModelCategory 元数据类目
     * @return 结果
     */
    @Override
    public int updateTMetaModelCategory(TMetaModelCategory tMetaModelCategory)
    {
        return tMetaModelCategoryMapper.updateTMetaModelCategory(tMetaModelCategory);
    }

    /**
     * 批量删除元数据类目
     *
     * @param ids 需要删除的元数据类目主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelCategoryByIds(Long[] ids)
    {
        return tMetaModelCategoryMapper.deleteTMetaModelCategoryByIds(ids);
    }

    /**
     * 删除元数据类目信息
     *
     * @param id 元数据类目主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelCategoryById(Long id)
    {
        return tMetaModelCategoryMapper.deleteTMetaModelCategoryById(id);
    }


}
