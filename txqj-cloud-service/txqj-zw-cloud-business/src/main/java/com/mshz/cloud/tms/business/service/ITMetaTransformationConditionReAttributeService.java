package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionReAttribute;

import java.util.List;

/**
 * 条件关联属性管理Service接口
 *
 * @author zdw
 * @date 2023-06-20
 */
public interface ITMetaTransformationConditionReAttributeService
{
    /**
     * 查询条件关联属性管理
     *
     * @param id 条件关联属性管理主键
     * @return 条件关联属性管理
     */
    public TMetaTransformationConditionReAttribute selectTMetaTransformationConditionReAttributeById(Long id);

    /**
     * 查询条件关联属性管理列表
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 条件关联属性管理集合
     */
    public List<TMetaTransformationConditionReAttribute> selectTMetaTransformationConditionReAttributeList(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute);

    /**
     * 新增条件关联属性管理
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 结果
     */
    public int insertTMetaTransformationConditionReAttribute(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute);

    /**
     * 修改条件关联属性管理
     *
     * @param tMetaTransformationConditionReAttribute 条件关联属性管理
     * @return 结果
     */
    public int updateTMetaTransformationConditionReAttribute(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute);

    /**
     * 批量删除条件关联属性管理
     *
     * @param ids 需要删除的条件关联属性管理主键集合
     * @return 结果
     */
    public int deleteTMetaTransformationConditionReAttributeByIds(Long[] ids);

    /**
     * 删除条件关联属性管理信息
     *
     * @param id 条件关联属性管理主键
     * @return 结果
     */
    public int deleteTMetaTransformationConditionReAttributeById(Long id);
}
