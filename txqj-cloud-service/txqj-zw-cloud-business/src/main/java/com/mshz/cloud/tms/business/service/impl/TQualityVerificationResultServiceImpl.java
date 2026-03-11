package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.service.ITQualityVerificationResultService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.vo.CheckResultVo;
import com.mshz.cloud.tms.business.vo.DictionariesVo;
import com.mshz.cloud.tms.business.mapper.TQualityVerificationResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据质量-校验结果Service业务层处理
 *
 * @author hzy
 * @date 2022-10-14
 */
@Service
public class TQualityVerificationResultServiceImpl implements ITQualityVerificationResultService
{
    @Autowired
    private TQualityVerificationResultMapper tQualityVerificationResultMapper;

    /**
     * 查询数据质量-校验结果
     *
     * @param id 数据质量-校验结果主键
     * @return 数据质量-校验结果
     */
    @Override
    public TQualityVerificationResult selectTQualityVerificationResultById(Long id)
    {
        return tQualityVerificationResultMapper.selectTQualityVerificationResultById(id);
    }

    @Override
    public TQualityVerificationResult selectTQualityVerificationResultByCondition(String ruleId, String errorId, String commName) {
        return tQualityVerificationResultMapper.selectTQualityVerificationResultByCondition(ruleId, errorId, commName);
    }

    /**
     * 查询数据质量-校验结果列表
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 数据质量-校验结果
     */
    @Override
    public List<TQualityVerificationResult> selectTQualityVerificationResultList(TQualityVerificationResult tQualityVerificationResult)
    {
        return tQualityVerificationResultMapper.selectTQualityVerificationResultList(tQualityVerificationResult);
    }

    /**
     * 新增数据质量-校验结果
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 结果
     */
    @Override
    public int insertTQualityVerificationResult(TQualityVerificationResult tQualityVerificationResult)
    {
        tQualityVerificationResult.setCreateTime(DateUtils.dateTimeNow("yyyy-MM-dd hh:mm:ss"));
        return tQualityVerificationResultMapper.insertTQualityVerificationResult(tQualityVerificationResult);
    }

    /**
     * 修改数据质量-校验结果
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 结果
     */
    @Override
    public int updateTQualityVerificationResult(TQualityVerificationResult tQualityVerificationResult)
    {
        tQualityVerificationResult.setUpdateTime(DateUtils.dateTimeNow("yyyy-MM-dd hh:mm:ss"));
        return tQualityVerificationResultMapper.updateTQualityVerificationResult(tQualityVerificationResult);
    }

    /**
     * 批量删除数据质量-校验结果
     *
     * @param ids 需要删除的数据质量-校验结果主键
     * @return 结果
     */
    @Override
    public int deleteTQualityVerificationResultByIds(Long[] ids)
    {
        return tQualityVerificationResultMapper.deleteTQualityVerificationResultByIds(ids);
    }

    @Override
    public void deleteTQualityVerificationResultBySiteId(String errorId) {
        tQualityVerificationResultMapper.deleteTQualityVerificationResultBySiteId(errorId);
    }

    @Override
    public void deleteTQualityVerificationResultBySiteIdName(String errorId, String errorName) {
        tQualityVerificationResultMapper.deleteTQualityVerificationResultBySiteIdName(errorId, errorName);
    }

    /**
     * 删除数据质量-校验结果信息
     *
     * @param id 数据质量-校验结果主键
     * @return 结果
     */
    @Override
    public int deleteTQualityVerificationResultById(Long id)
    {
        return tQualityVerificationResultMapper.deleteTQualityVerificationResultById(id);
    }

    @Override
    public List<CheckResultVo> selectCheckResult(String siteName, String ownerId, String bRuleId, String type, String columnName, String modelId) {
        return tQualityVerificationResultMapper.selectCheckResult(siteName, ownerId, bRuleId,type,columnName, modelId);
    }

    @Override
    public List<DictionariesVo> selectCompany(String name) {
        return tQualityVerificationResultMapper.selectCompany(name);
    }

    @Override
    public List<DictionariesVo> selectType() {
        return tQualityVerificationResultMapper.selectType();
    }

    @Override
    public List<DictionariesVo> queryRes() {
        return tQualityVerificationResultMapper.queryRes();
    }
}
