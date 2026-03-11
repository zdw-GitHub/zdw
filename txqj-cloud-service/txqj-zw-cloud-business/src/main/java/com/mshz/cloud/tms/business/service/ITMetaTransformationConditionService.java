package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.vo.TestSQLParaVo;
import com.mshz.cloud.tms.business.entity.TMetaTransformationCondition;
import com.mshz.cloud.tms.business.vo.TestSQLResultVo;

import java.util.List;

/**
 * 转换条件管理Service接口
 *
 * @author zdw
 * @date 2023-06-20
 */
public interface ITMetaTransformationConditionService
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
    public long insertTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition);

    /**
     * 修改转换条件管理
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 结果
     */
    public int updateTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition);

    /**
     * 批量删除转换条件管理
     *
     * @param ids 需要删除的转换条件管理主键集合
     * @return 结果
     */
    public int deleteTMetaTransformationConditionByIds(Long[] ids);

    /**
     * 删除转换条件管理信息
     *
     * @param id 转换条件管理主键
     * @return 结果
     */
    public int deleteTMetaTransformationConditionById(Long id);

    /**
     * SQL测试方法
     * @param para SQL测试入参
     * @return TestSQLResultVo  SQL测试的结果
     */
    public TestSQLResultVo testSQL(TestSQLParaVo para);


}
