package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.MetaModelRuleParam;

import java.util.List;

/**
 * 元模型字段规则参数Mapper接口
 *
 * @author zdw
 * @date 2023-08-17
 */
public interface MetaModelRuleParamMapper
{
    /**
     * 查询元模型字段规则参数
     *
     * @param id 元模型字段规则参数主键
     * @return 元模型字段规则参数
     */
    public MetaModelRuleParam selectMetaModelRuleParamById(Long id);

    /**
     * 查询元模型字段规则参数列表
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 元模型字段规则参数集合
     */
    public List<MetaModelRuleParam> selectMetaModelRuleParamList(MetaModelRuleParam metaModelRuleParam);

    /**
     * 新增元模型字段规则参数
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 结果
     */
    public int insertMetaModelRuleParam(MetaModelRuleParam metaModelRuleParam);

    /**
     * 修改元模型字段规则参数
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 结果
     */
    public int updateMetaModelRuleParam(MetaModelRuleParam metaModelRuleParam);

    /**
     * 删除元模型字段规则参数
     *
     * @param id 元模型字段规则参数主键
     * @return 结果
     */
    public int deleteMetaModelRuleParamById(Long id);

    /**
     * 批量删除元模型字段规则参数
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMetaModelRuleParamByIds(Long[] ids);
}
