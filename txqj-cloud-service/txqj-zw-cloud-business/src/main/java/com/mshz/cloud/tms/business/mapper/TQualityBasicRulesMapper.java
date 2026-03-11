package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TQualityBasicRules;
import java.util.List;

/**
 * 数据质量-基础规则Mapper接口
 *
 * @author hzy
 * @date 2022-10-22
 */
public interface TQualityBasicRulesMapper
{
    /**
     * 查询数据质量-基础规则
     *
     * @param id 数据质量-基础规则主键
     * @return 数据质量-基础规则
     */
    public TQualityBasicRules selectTQualityBasicRulesById(Long id);

    /**
     * 查询数据质量-基础规则列表
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 数据质量-基础规则集合
     */
    public List<TQualityBasicRules> selectTQualityBasicRulesList(TQualityBasicRules tQualityBasicRules);

    /**
     * 新增数据质量-基础规则
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 结果
     */
    public int insertTQualityBasicRules(TQualityBasicRules tQualityBasicRules);

    /**
     * 修改数据质量-基础规则
     *
     * @param tQualityBasicRules 数据质量-基础规则
     * @return 结果
     */
    public int updateTQualityBasicRules(TQualityBasicRules tQualityBasicRules);

    /**
     * 删除数据质量-基础规则
     *
     * @param id 数据质量-基础规则主键
     * @return 结果
     */
    public int deleteTQualityBasicRulesById(Long id);

    /**
     * 批量删除数据质量-基础规则
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTQualityBasicRulesByIds(Long[] ids);
}
