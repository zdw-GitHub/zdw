package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionReAttribute;
import com.mshz.cloud.tms.business.mapper.TMetaTransformationConditionReAttributeMapper;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionReAttributeService;
import com.mshz.cloud.tms.business.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 条件关联属性管理Service业务层处理
 *
 * @author zdw
 * @date 2023-06-20
 */
@Service
public class TMetaTransformationConditionReAttributeServiceImpl implements ITMetaTransformationConditionReAttributeService
{
    @Autowired
    private TMetaTransformationConditionReAttributeMapper tMetaTransformationConditionReAttributeMapper;

    /**
     * 查询条件关联属性管理
     *
     * @param id 条件关联属性管理主键
     * @return 条件关联属性管理
     */
    @Override
    public TMetaTransformationConditionReAttribute selectTMetaTransformationConditionReAttributeById(Long id)
    {
        return tMetaTransformationConditionReAttributeMapper.selectTMetaTransformationConditionReAttributeById(id);
    }

    /**
     * 查询条件关联属性管理列表
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 条件关联属性管理
     */
    @Override
    public List<TMetaTransformationConditionReAttribute> selectTMetaTransformationConditionReAttributeList(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        return tMetaTransformationConditionReAttributeMapper.selectTMetaTransformationConditionReAttributeList(tMetaTransformationConditionReAttribute);
    }

    /**
     * 新增条件关联属性管理
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 结果
     */
    @Override
    public int insertTMetaTransformationConditionReAttribute(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        tMetaTransformationConditionReAttribute.setCreateTime(DateUtils.getNowDate());
        return tMetaTransformationConditionReAttributeMapper.insertTMetaTransformationConditionReAttribute(tMetaTransformationConditionReAttribute);
    }

    /**
     * 修改条件关联属性管理
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 结果
     */
    @Override
    public int updateTMetaTransformationConditionReAttribute(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        tMetaTransformationConditionReAttribute.setUpdateTime(DateUtils.getNowDate());
        return tMetaTransformationConditionReAttributeMapper.updateTMetaTransformationConditionReAttribute(tMetaTransformationConditionReAttribute);
    }

    /**
     * 批量删除条件关联属性管理
     *
     * @param ids 需要删除的条件关联属性管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionReAttributeByIds(Long[] ids)
    {
        return tMetaTransformationConditionReAttributeMapper.deleteTMetaTransformationConditionReAttributeByIds(ids);
    }

    /**
     * 删除条件关联属性管理信息
     *
     * @param id 条件关联属性管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionReAttributeById(Long id)
    {
        return tMetaTransformationConditionReAttributeMapper.deleteTMetaTransformationConditionReAttributeById(id);
    }
}
