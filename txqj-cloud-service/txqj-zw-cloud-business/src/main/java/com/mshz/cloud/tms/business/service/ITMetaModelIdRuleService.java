package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TMetaModelIdRule;
import com.mshz.cloud.tms.business.vo.IDConversionReturnVo;
import java.util.List;

/**
 * 模型ID生成规则配置Service接口
 *
 * @author zdw
 * @date 2023-08-09
 */
public interface ITMetaModelIdRuleService
{
    /**
     * 查询模型ID生成规则配置
     *
     * @param id 模型ID生成规则配置主键
     * @return 模型ID生成规则配置
     */
    public TMetaModelIdRule selectTMetaModelIdRuleById(Long id);

    /**
     * 查询模型ID生成规则配置列表
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 模型ID生成规则配置集合
     */
    public List<TMetaModelIdRule> selectTMetaModelIdRuleList(TMetaModelIdRule tMetaModelIdRule);

    /**
     * 查询模型ID生成规则配置列表 没有默认配置信息
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 模型ID生成规则配置集合
     */
    public List<TMetaModelIdRule> selectTMetaModelIdRuleListWithoutDefault(TMetaModelIdRule tMetaModelIdRule);

    /**
     * 新增模型ID生成规则配置
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 结果
     */
    public int insertTMetaModelIdRule(TMetaModelIdRule tMetaModelIdRule);

    /**
     * 修改模型ID生成规则配置
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 结果
     */
    public int updateTMetaModelIdRule(TMetaModelIdRule tMetaModelIdRule);

    /**
     * 批量删除模型ID生成规则配置
     *
     * @param ids 需要删除的模型ID生成规则配置主键集合
     * @return 结果
     */
    public int deleteTMetaModelIdRuleByIds(Long[] ids);

    /**
     * 删除模型ID生成规则配置信息
     *
     * @param id 模型ID生成规则配置主键
     * @return 结果
     */
    public int deleteTMetaModelIdRuleById(Long id);

    /**
     * 根据表名获取ID
     * @param modelName
     * @return IDConversionReturnVo
     */
    public IDConversionReturnVo getIDByTableName(String modelName);

    /**
     * 批量更新ID
     * @param modelNames
     * @return IDConversionReturnVo
     */
    List<IDConversionReturnVo> batchConversionId(String[] modelNames);


     TMetaModelIdRule getTMetaModelIdRuleByModelName(String  modelName);

}
