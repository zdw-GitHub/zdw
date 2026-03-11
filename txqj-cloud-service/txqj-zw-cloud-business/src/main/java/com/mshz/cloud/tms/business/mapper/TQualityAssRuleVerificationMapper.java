package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TQualityAssRuleVerification;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 数据质量-关联规则校验Mapper接口
 *
 * @author hzy
 * @date 2022-10-22
 */
public interface TQualityAssRuleVerificationMapper
{
    /**
     * 查询数据质量-关联规则校验
     *
     * @param id 数据质量-关联规则校验主键
     * @return 数据质量-关联规则校验
     */
    public TQualityAssRuleVerification selectTQualityAssRuleVerificationById(Long id);

    /**
     * 查询数据质量-关联规则校验列表
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 数据质量-关联规则校验集合
     */
    public List<TQualityAssRuleVerification> selectTQualityAssRuleVerificationList(TQualityAssRuleVerification tQualityAssRuleVerification);

    /**
     * 新增数据质量-关联规则校验
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 结果
     */
    public int insertTQualityAssRuleVerification(TQualityAssRuleVerification tQualityAssRuleVerification);

    /**
     * 修改数据质量-关联规则校验
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 结果
     */
    public int updateTQualityAssRuleVerification(TQualityAssRuleVerification tQualityAssRuleVerification);

    /**
     * 删除数据质量-关联规则校验
     *
     * @param id 数据质量-关联规则校验主键
     * @return 结果
     */
    public int deleteTQualityAssRuleVerificationById(Long id);

    /**
     * 批量删除数据质量-关联规则校验
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTQualityAssRuleVerificationByIds(Long[] ids);

    int insertTQualityAssRuleVerificationList(@Param("list") List<TQualityAssRuleVerification> tQualityAssRuleVerificationList);

}
