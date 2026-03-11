package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.MetaModelRuleConversionLog;

import java.util.List;

/**
 * 元模型字段规则转换日志记录Mapper接口
 *
 * @author zdw
 * @date 2023-08-17
 */
public interface MetaModelRuleConversionLogMapper
{
    /**
     * 查询元模型字段规则转换日志记录
     *
     * @param id 元模型字段规则转换日志记录主键
     * @return 元模型字段规则转换日志记录
     */
    public MetaModelRuleConversionLog selectMetaModelRuleConversionLogById(Long id);

    /**
     * 查询元模型字段规则转换日志记录列表
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 元模型字段规则转换日志记录集合
     */
    public List<MetaModelRuleConversionLog> selectMetaModelRuleConversionLogList(MetaModelRuleConversionLog metaModelRuleConversionLog);

    /**
     * 新增元模型字段规则转换日志记录
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 结果
     */
    public int insertMetaModelRuleConversionLog(MetaModelRuleConversionLog metaModelRuleConversionLog);

    /**
     * 修改元模型字段规则转换日志记录
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 结果
     */
    public int updateMetaModelRuleConversionLog(MetaModelRuleConversionLog metaModelRuleConversionLog);

    /**
     * 删除元模型字段规则转换日志记录
     *
     * @param id 元模型字段规则转换日志记录主键
     * @return 结果
     */
    public int deleteMetaModelRuleConversionLogById(Long id);

    /**
     * 批量删除元模型字段规则转换日志记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMetaModelRuleConversionLogByIds(Long[] ids);
}
