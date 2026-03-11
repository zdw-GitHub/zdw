package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.vo.CheckResultVo;
import com.mshz.cloud.tms.business.vo.DictionariesVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 数据质量-校验结果Mapper接口
 *
 * @author hzy
 * @date 2022-10-14
 */
public interface TQualityVerificationResultMapper
{
    /**
     * 查询数据质量-校验结果
     *
     * @param id 数据质量-校验结果主键
     * @return 数据质量-校验结果
     */
    public TQualityVerificationResult selectTQualityVerificationResultById(Long id);

    public TQualityVerificationResult selectTQualityVerificationResultByCondition(@Param("ruleId") String ruleId,@Param("errorId") String errorId,@Param("commName") String commName);

    /**
     * 查询数据质量-校验结果列表
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 数据质量-校验结果集合
     */
    public List<TQualityVerificationResult> selectTQualityVerificationResultList(TQualityVerificationResult tQualityVerificationResult);

    /**
     * 新增数据质量-校验结果
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 结果
     */
    public int insertTQualityVerificationResult(TQualityVerificationResult tQualityVerificationResult);

    /**
     * 修改数据质量-校验结果
     *
     * @param tQualityVerificationResult 数据质量-校验结果
     * @return 结果
     */
    public int updateTQualityVerificationResult(TQualityVerificationResult tQualityVerificationResult);

    /**
     * 删除数据质量-校验结果
     *
     * @param id 数据质量-校验结果主键
     * @return 结果
     */
    public int deleteTQualityVerificationResultById(Long id);

    /**
     * 批量删除数据质量-校验结果
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTQualityVerificationResultByIds(Long[] ids);

    public void deleteTQualityVerificationResultBySiteId(String errorId);

    public void deleteTQualityVerificationResultBySiteIdName(@Param("errorId")String errorId,@Param("errorName")String errorName);

    List<CheckResultVo> selectCheckResult(@Param("siteName")String siteName, @Param("ownerId")String ownerId, @Param("bRuleId")String bRuleId, @Param("type")String type, @Param("columnName")String columnName, @Param("modelId")String modelId);

    List<DictionariesVo> selectCompany(String name);

    List<DictionariesVo> selectType();

    List<DictionariesVo> queryRes();
}
