package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TQualityAssociationRules;
import com.mshz.cloud.tms.business.service.ITQualityAssociationRulesService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.mapper.TQualityAssociationRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 数据质量-关联规则Service业务层处理
 *
 * @author hzy
 * @date 2022-10-22
 */
@Service
public class TQualityAssociationRulesServiceImpl implements ITQualityAssociationRulesService
{
    @Autowired
    private TQualityAssociationRulesMapper tQualityAssociationRulesMapper;

    /**
     * 查询数据质量-关联规则
     *
     * @param id 数据质量-关联规则主键
     * @return 数据质量-关联规则
     */
    @Override
    public TQualityAssociationRules selectTQualityAssociationRulesById(Long id)
    {
        return tQualityAssociationRulesMapper.selectTQualityAssociationRulesById(id);
    }

    /**
     * 查询数据质量-关联规则列表
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 数据质量-关联规则
     */
    @Override
    public List<TQualityAssociationRules> selectTQualityAssociationRulesList(TQualityAssociationRules tQualityAssociationRules)
    {
        return tQualityAssociationRulesMapper.selectTQualityAssociationRulesList(tQualityAssociationRules);
    }

    /**
     * 新增数据质量-关联规则
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 结果
     */
    @Override
    public int insertTQualityAssociationRules(TQualityAssociationRules tQualityAssociationRules)
    {
        tQualityAssociationRules.setCreateTime(DateUtils.getNowDate());
        return tQualityAssociationRulesMapper.insertTQualityAssociationRules(tQualityAssociationRules);
    }

    /**
     * 修改数据质量-关联规则
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 结果
     */
    @Override
    public int updateTQualityAssociationRules(TQualityAssociationRules tQualityAssociationRules)
    {
        tQualityAssociationRules.setUpdateTime(DateUtils.getNowDate());
        return tQualityAssociationRulesMapper.updateTQualityAssociationRules(tQualityAssociationRules);
    }

    /**
     * 批量删除数据质量-关联规则
     *
     * @param ids 需要删除的数据质量-关联规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityAssociationRulesByIds(Long[] ids)
    {
        return tQualityAssociationRulesMapper.deleteTQualityAssociationRulesByIds(ids);
    }

    /**
     * 删除数据质量-关联规则信息
     *
     * @param id 数据质量-关联规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityAssociationRulesById(Long id)
    {
        return tQualityAssociationRulesMapper.deleteTQualityAssociationRulesById(id);
    }
}
