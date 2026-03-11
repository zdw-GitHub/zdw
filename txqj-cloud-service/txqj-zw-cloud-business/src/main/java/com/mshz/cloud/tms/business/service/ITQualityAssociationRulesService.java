package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TQualityAssociationRules;
import java.util.List;

/**
 * 数据质量-关联规则Service接口
 *
 * @author hzy
 * @date 2022-10-22
 */
public interface ITQualityAssociationRulesService
{
    /**
     * 查询数据质量-关联规则
     *
     * @param id 数据质量-关联规则主键
     * @return 数据质量-关联规则
     */
    public TQualityAssociationRules selectTQualityAssociationRulesById(Long id);

    /**
     * 查询数据质量-关联规则列表
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 数据质量-关联规则集合
     */
    public List<TQualityAssociationRules> selectTQualityAssociationRulesList(TQualityAssociationRules tQualityAssociationRules);

    /**
     * 新增数据质量-关联规则
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 结果
     */
    public int insertTQualityAssociationRules(TQualityAssociationRules tQualityAssociationRules);

    /**
     * 修改数据质量-关联规则
     *
     * @param tQualityAssociationRules 数据质量-关联规则
     * @return 结果
     */
    public int updateTQualityAssociationRules(TQualityAssociationRules tQualityAssociationRules);

    /**
     * 批量删除数据质量-关联规则
     *
     * @param ids 需要删除的数据质量-关联规则主键集合
     * @return 结果
     */
    public int deleteTQualityAssociationRulesByIds(Long[] ids);

    /**
     * 删除数据质量-关联规则信息
     *
     * @param id 数据质量-关联规则主键
     * @return 结果
     */
    public int deleteTQualityAssociationRulesById(Long id);
}
