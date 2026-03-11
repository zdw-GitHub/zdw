package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TMetaTransformationCondition;

import java.util.List;

/**
 * 转换条件管理Mapper接口
 *
 * @author zdw
 * @date 2023-06-21
 */
public interface TMetaTransformationConditionMapper
{
    /**
     * 查询转换条件管理
     *
     * @param id 转换条件管理主键
     * @return 转换条件管理
     */
    public TMetaTransformationCondition selectTMetaTransformationConditionById(Long id);

    /**
     * 查询转换条件管理列表
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 转换条件管理集合
     */
    public List<TMetaTransformationCondition> selectTMetaTransformationConditionList(TMetaTransformationCondition tMetaTransformationCondition);

    /**
     * 新增转换条件管理
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 结果
     */
    public int insertTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition);

    /**
     * 修改转换条件管理
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 结果
     */
    public int updateTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition);

    /**
     * 删除转换条件管理
     *
     * @param id 转换条件管理主键
     * @return 结果
     */
    public int deleteTMetaTransformationConditionById(Long id);

    /**
     * 批量删除转换条件管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaTransformationConditionByIds(Long[] ids);
}
