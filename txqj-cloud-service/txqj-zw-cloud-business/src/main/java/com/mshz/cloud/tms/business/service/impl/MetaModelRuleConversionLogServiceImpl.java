package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.MetaModelRuleConversionLog;
import com.mshz.cloud.tms.business.mapper.MetaModelRuleConversionLogMapper;
import com.mshz.cloud.tms.business.service.IMetaModelRuleConversionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元模型字段规则转换日志记录Service业务层处理
 *
 * @author zdw
 * @date 2023-08-17
 */
@Service
public class MetaModelRuleConversionLogServiceImpl implements IMetaModelRuleConversionLogService
{
    @Autowired
    private MetaModelRuleConversionLogMapper metaModelRuleConversionLogMapper;

    /**
     * 查询元模型字段规则转换日志记录
     *
     * @param id 元模型字段规则转换日志记录主键
     * @return 元模型字段规则转换日志记录
     */
    @Override
    public MetaModelRuleConversionLog selectMetaModelRuleConversionLogById(Long id)
    {
        return metaModelRuleConversionLogMapper.selectMetaModelRuleConversionLogById(id);
    }

    /**
     * 查询元模型字段规则转换日志记录列表
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 元模型字段规则转换日志记录
     */
    @Override
    public List<MetaModelRuleConversionLog> selectMetaModelRuleConversionLogList(MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        return metaModelRuleConversionLogMapper.selectMetaModelRuleConversionLogList(metaModelRuleConversionLog);
    }

    /**
     * 新增元模型字段规则转换日志记录
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 结果
     */
    @Override
    public int insertMetaModelRuleConversionLog(MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        return metaModelRuleConversionLogMapper.insertMetaModelRuleConversionLog(metaModelRuleConversionLog);
    }

    /**
     * 修改元模型字段规则转换日志记录
     *
     * @param metaModelRuleConversionLog 元模型字段规则转换日志记录
     * @return 结果
     */
    @Override
    public int updateMetaModelRuleConversionLog(MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        return metaModelRuleConversionLogMapper.updateMetaModelRuleConversionLog(metaModelRuleConversionLog);
    }

    /**
     * 批量删除元模型字段规则转换日志记录
     *
     * @param ids 需要删除的元模型字段规则转换日志记录主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleConversionLogByIds(Long[] ids)
    {
        return metaModelRuleConversionLogMapper.deleteMetaModelRuleConversionLogByIds(ids);
    }

    /**
     * 删除元模型字段规则转换日志记录信息
     *
     * @param id 元模型字段规则转换日志记录主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleConversionLogById(Long id)
    {
        return metaModelRuleConversionLogMapper.deleteMetaModelRuleConversionLogById(id);
    }
}
