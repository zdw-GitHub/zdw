package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TQualityAssRuleVerification;
import com.mshz.cloud.tms.business.service.ITQualityAssRuleVerificationService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.mapper.TQualityAssRuleVerificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 数据质量-关联规则校验Service业务层处理
 *
 * @author hzy
 * @date 2022-10-22
 */
@Service
public class TQualityAssRuleVerificationServiceImpl implements ITQualityAssRuleVerificationService
{
    @Autowired
    private TQualityAssRuleVerificationMapper tQualityAssRuleVerificationMapper;

    /**
     * 查询数据质量-关联规则校验
     *
     * @param id 数据质量-关联规则校验主键
     * @return 数据质量-关联规则校验
     */
    @Override
    public TQualityAssRuleVerification selectTQualityAssRuleVerificationById(Long id)
    {
        return tQualityAssRuleVerificationMapper.selectTQualityAssRuleVerificationById(id);
    }

    /**
     * 查询数据质量-关联规则校验列表
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 数据质量-关联规则校验
     */
    @Override
    public List<TQualityAssRuleVerification> selectTQualityAssRuleVerificationList(TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        return tQualityAssRuleVerificationMapper.selectTQualityAssRuleVerificationList(tQualityAssRuleVerification);
    }

    /**
     * 新增数据质量-关联规则校验
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 结果
     */
    @Override
    public int insertTQualityAssRuleVerification(TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        tQualityAssRuleVerification.setCreateTime(DateUtils.getNowDate());
        return tQualityAssRuleVerificationMapper.insertTQualityAssRuleVerification(tQualityAssRuleVerification);
    }

    /**
     * 修改数据质量-关联规则校验
     *
     * @param tQualityAssRuleVerification 数据质量-关联规则校验
     * @return 结果
     */
    @Override
    public int updateTQualityAssRuleVerification(TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        tQualityAssRuleVerification.setUpdateTime(DateUtils.getNowDate());
        return tQualityAssRuleVerificationMapper.updateTQualityAssRuleVerification(tQualityAssRuleVerification);
    }

    /**
     * 批量删除数据质量-关联规则校验
     *
     * @param ids 需要删除的数据质量-关联规则校验主键
     * @return 结果
     */
    @Override
    public int deleteTQualityAssRuleVerificationByIds(Long[] ids)
    {
        return tQualityAssRuleVerificationMapper.deleteTQualityAssRuleVerificationByIds(ids);
    }

    /**
     * 删除数据质量-关联规则校验信息
     *
     * @param id 数据质量-关联规则校验主键
     * @return 结果
     */
    @Override
    public int deleteTQualityAssRuleVerificationById(Long id)
    {
        return tQualityAssRuleVerificationMapper.deleteTQualityAssRuleVerificationById(id);
    }
}
