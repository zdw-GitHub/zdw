package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionGroup;

import java.util.List;

/**
 * 转换条件组管理Service接口
 *
 * @author zdw
 * @date 2023-06-24
 */
public interface ITMetaTransformationConditionGroupService
{
    /**
     * 查询转换条件组管理
     *
     * @param id 转换条件组管理主键
     * @return 转换条件组管理
     */
    public TMetaTransformationConditionGroup selectTMetaTransformationConditionGroupById(Long id);

    /**
     * 查询转换条件组管理列表
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 转换条件组管理集合
     */
    public List<TMetaTransformationConditionGroup> selectTMetaTransformationConditionGroupList(TMetaTransformationConditionGroup tMetaTransformationConditionGroup);

    /**
     * 新增转换条件组管理
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 结果
     */
    public long insertTMetaTransformationConditionGroup(TMetaTransformationConditionGroup tMetaTransformationConditionGroup);

    /**
     * 修改转换条件组管理
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 结果
     */
    public int updateTMetaTransformationConditionGroup(TMetaTransformationConditionGroup tMetaTransformationConditionGroup);

    /**
     * 批量删除转换条件组管理
     *
     * @param ids 需要删除的转换条件组管理主键集合
     * @return 结果
     */
    public int deleteTMetaTransformationConditionGroupByIds(Long[] ids);

    /**
     * 删除转换条件组管理信息
     *
     * @param id 转换条件组管理主键
     * @return 结果
     */
    public int deleteTMetaTransformationConditionGroupById(Long id);
}
