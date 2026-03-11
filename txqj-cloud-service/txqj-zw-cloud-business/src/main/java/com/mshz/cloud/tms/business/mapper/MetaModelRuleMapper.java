package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.MetaModelRule;
import com.mshz.cloud.tms.business.vo.ModelRuleVo;
import com.mshz.cloud.tms.business.vo.ModelRuleConvertVo;

import java.util.List;

/**
 * 元模型字段规则Mapper接口
 *
 * @author zdw
 * @date 2023-08-17
 */
public interface MetaModelRuleMapper
{
    /**
     * 查询元模型字段规则
     *
     * @param id 元模型字段规则主键
     * @return 元模型字段规则
     */
    public MetaModelRule selectMetaModelRuleById(Long id);

    /**
     * 查询元模型字段规则列表
     *
     * @param metaModelRule 元模型字段规则
     * @return 元模型字段规则集合
     */
    public List<MetaModelRule> selectMetaModelRuleList(MetaModelRule metaModelRule);

    /**
     * 新增元模型字段规则
     *
     * @param metaModelRule 元模型字段规则
     * @return 结果
     */
    public int insertMetaModelRule(MetaModelRule metaModelRule);

    /**
     * 修改元模型字段规则
     *
     * @param metaModelRule 元模型字段规则
     * @return 结果
     */
    public int updateMetaModelRule(MetaModelRule metaModelRule);

    /**
     * 删除元模型字段规则
     *
     * @param id 元模型字段规则主键
     * @return 结果
     */
    public int deleteMetaModelRuleById(Long id);

    /**
     * 批量删除元模型字段规则
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMetaModelRuleByIds(Long[] ids);

    /**
     * 查询元模型字段规则列表
     * @param param
     * @return 规则vo集合
     */
    public List<ModelRuleVo> getModelRuleList(MetaModelRule param);

    /**
     * 获取传入元模型的key
     * @param modelId
     * @return
     */
    public String getTableKeyName(long modelId);

    List<ModelRuleConvertVo> getColumRuleList();

}
