package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import com.mshz.cloud.tms.business.vo.QualityVerificationRulesVo;
import com.mshz.cloud.tms.business.vo.SelectVo;

import java.util.List;

/**
 * 数据质量-校验规则Service接口
 *
 * @author zxx
 * @date 2022-10-19
 */
public interface TQualityVerificationRulesService
{
    /**
     * 查询数据质量-校验规则
     *
     * @param id 数据质量-校验规则主键
     * @return 数据质量-校验规则
     */
    public TQualityVerificationRules selectTQualityVerificationRulesById(Long id);

    /**
     * 查询数据质量-校验规则列表
     *
     * @param qualityVerificationRulesVo 数据质量-校验规则
     * @return 数据质量-校验规则集合
     */
    public List<QualityVerificationRulesVo> selectQualityVerificationRulesListVo(QualityVerificationRulesVo qualityVerificationRulesVo);
    /**
     * 查询数据质量-校验规则列表
     *
     * @param tQualityVerificationRules 数据质量-校验规则
     * @return 数据质量-校验规则集合
     */
    public List<TQualityVerificationRules> selectTQualityVerificationRulesList(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 新增数据质量-校验规则
     *
     * @param tQualityVerificationRules 数据质量-校验规则
     * @return 结果
     */
    public int insertTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 修改数据质量-校验规则
     *
     * @param tQualityVerificationRules 数据质量-校验规则
     * @return 结果
     */
    public int updateTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 批量删除数据质量-校验规则
     *
     * @param ids 需要删除的数据质量-校验规则主键集合
     * @return 结果
     */
    public int deleteTQualityVerificationRulesByIds(Long[] ids);

    /**
     * 删除数据质量-校验规则信息
     *
     * @param id 数据质量-校验规则主键
     * @return 结果
     */
    public int deleteTQualityVerificationRulesById(Long id);

    List<SelectVo> selectSymbolCategory();

    List<SelectVo> selectSymbolList(String englishName);
}
