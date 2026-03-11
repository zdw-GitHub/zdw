package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import com.mshz.cloud.tms.business.vo.*;
import java.util.List;
import java.util.Map;

/**
 * 数据质量-基础校验规则Service接口
 *
 * @author hzy
 * @date 2022-10-22
 */
public interface ITQualityVerificationRulesService {
    /**
     * 查询数据质量-基础校验规则
     *
     * @param id 数据质量-基础校验规则主键
     * @return 数据质量-基础校验规则
     */
    public TQualityVerificationRules selectTQualityVerificationRulesById(Long id);

    /**
     * 查询数据质量-基础校验规则列表
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 数据质量-基础校验规则集合
     */
    public List<TQualityVerificationRules> selectTQualityVerificationRulesList(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 新增数据质量-基础校验规则
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 结果
     */
    public int insertTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 修改数据质量-基础校验规则
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 结果
     */
    public int updateTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules);

    /**
     * 批量删除数据质量-基础校验规则
     *
     * @param ids 需要删除的数据质量-基础校验规则主键集合
     * @return 结果
     */
    public int deleteTQualityVerificationRulesByIds(Long[] ids);

    /**
     * 删除数据质量-基础校验规则信息
     *
     * @param id 数据质量-基础校验规则主键
     * @return 结果
     */
    public int deleteTQualityVerificationRulesById(Long id);

    List<SelectRulesVo> selectRules(String modelId);

    QualityVerificationRulesVo selectVerificationRules(String var1);

    QualityVerificationRulesVo selectAssRuleVer(String ruleId);

    List<GuideVo> selectGuide(String siteId);

    List<TableRuleVo> selectByTableIdVersion(String modeId, String modeVersion, String ruleId);

    List<TableRuleVo> selectByTableIdVersion(String modeId, String modeVersion, String ruleId, String attribute);

    List<Map<String, String>> selectDictionaries(String table, String id, String name);

    List<Map<String, String>> selectDictionariesForLIMIT(String table, String id, String name, String code, String codename, Integer pageNum, Integer pageSize, String orderBy,
        String constraint);

    int selectDictionariesForLIMITCount(String table, String id, String name, String code, String codename, String constraint);

    List<Map<String, Object>> useDefinedVerSql(String sql);

    List<Map<String, Object>> useDefinedVerSqlForLIMIT(String sql, String code, String codename, Integer pageNum, Integer pageSize, String orderBy);

    int useDefinedVerSqlForLIMITCount(String sql, String code, String codename);

    List<QualityVerificationRulesVo> selectVerificationRuleOne(OneCommRule commRule);

    List<TQualityVerificationResult> selectByTableIdVersion1(String errorId, String modeVersion);

    List<TQualityVerificationResult> selectByTableIdVersionForCreate(String modeId, String modeVersion);

    List<Map> selectSiteMaintenanceUnit(List<String> codes);
    List<Map> selectConnectParSite(List<String> codes);

    /**
     * 查询数据质量-校验规则列表
     *
     * @param qualityVerificationRulesVo 数据质量-校验规则
     * @return 数据质量-校验规则集合
     */
    public List<QualityVerificationRulesVo> selectQualityVerificationRulesListVo(QualityVerificationRulesVo qualityVerificationRulesVo);

    List<SelectVo> selectSymbolList(String englishName);
}
