package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TQualityBasicRules;
import com.mshz.cloud.tms.business.mapper.TQualityBasicRulesMapper;
import com.mshz.cloud.tms.business.service.ITQualityBasicRulesService;
import com.mshz.cloud.tms.business.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据质量-基础规则Service业务层处理
 *
 * @author hzy
 * @date 2022-10-22
 */
@Service
public class TQualityBasicRulesServiceImpl implements ITQualityBasicRulesService
{
    @Autowired
    private TQualityBasicRulesMapper tQualityBasicRulesMapper;

    /**
     * 查询数据质量-基础规则
     *
     * @param id 数据质量-基础规则主键
     * @return 数据质量-基础规则
     */
    @Override
    public TQualityBasicRules selectTQualityBasicRulesById(Long id)
    {
        return tQualityBasicRulesMapper.selectTQualityBasicRulesById(id);
    }

    /**
     * 查询数据质量-基础规则列表
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 数据质量-基础规则
     */
    @Override
    public List<TQualityBasicRules> selectTQualityBasicRulesList(TQualityBasicRules tQualityBasicRules)
    {
        return tQualityBasicRulesMapper.selectTQualityBasicRulesList(tQualityBasicRules);
    }

    /**
     * 新增数据质量-基础规则
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 结果
     */
    @Override
    public int insertTQualityBasicRules(TQualityBasicRules tQualityBasicRules)
    {
        tQualityBasicRules.setCreateTime(DateUtils.getNowDate());
        return tQualityBasicRulesMapper.insertTQualityBasicRules(tQualityBasicRules);
    }

    /**
     * 修改数据质量-基础规则
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 结果
     */
    @Override
    public int updateTQualityBasicRules(TQualityBasicRules tQualityBasicRules)
    {
        tQualityBasicRules.setUpdateTime(DateUtils.getNowDate());
        return tQualityBasicRulesMapper.updateTQualityBasicRules(tQualityBasicRules);
    }

    /**
     * 批量删除数据质量-基础规则
     *
     * @param ids 需要删除的数据质量-基础规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityBasicRulesByIds(Long[] ids)
    {
        return tQualityBasicRulesMapper.deleteTQualityBasicRulesByIds(ids);
    }

    /**
     * 删除数据质量-基础规则信息
     *
     * @param id 数据质量-基础规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityBasicRulesById(Long id)
    {
        return tQualityBasicRulesMapper.deleteTQualityBasicRulesById(id);
    }
}
