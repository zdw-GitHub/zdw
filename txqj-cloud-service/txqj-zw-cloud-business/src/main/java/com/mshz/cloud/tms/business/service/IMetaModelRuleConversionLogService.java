package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.MetaModelRuleConversionLog;

import java.util.List;

/**
 * 元模型字段规则转换日志记录Service接口
 *
 * @author zdw
 * @date 2023-08-17
 */
public interface IMetaModelRuleConversionLogService {

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
     * 批量删除元模型字段规则转换日志记录
     *
     * @param ids 需要删除的元模型字段规则转换日志记录主键集合
     * @return 结果
     */
    public int deleteMetaModelRuleConversionLogByIds(Long[] ids);

    /**
     * 删除元模型字段规则转换日志记录信息
     *
     * @param id 元模型字段规则转换日志记录主键
     * @return 结果
     */
    public int deleteMetaModelRuleConversionLogById(Long id);
}
