package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import com.mshz.cloud.tms.business.vo.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 数据质量-基础校验规则Mapper接口
 *
 * @author hzy
 * @date 2022-10-22
 */
public interface TQualityVerificationRulesMapper {
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
     * 删除数据质量-基础校验规则
     *
     * @param id 数据质量-基础校验规则主键
     * @return 结果
     */
    public int deleteTQualityVerificationRulesById(Long id);

    /**
     * 批量删除数据质量-基础校验规则
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTQualityVerificationRulesByIds(Long[] ids);

    List<ResultIsMap> queryNullAttributeValue(Parameter var1);

    List<ResultIsMap> dicRangeVerification(Parameter var1);

    List<ResultIsMap> rangeCheck(Parameter var1);

    List<ResultIsMap> keywordVerification(Parameter var1);

    List<ResultIsMap> comparisonCheck(Parameter var1);

    List<ResultIsMap> accuracyVerification(Parameter var1);

    List<Map<String, Object>> verifyReturnTimeAss();

    List<TableRuleVo> selectByTableIdVersion(@Param("modeId") String modeId, @Param("modeVersion") String modeVersion, @Param("vRuleId") String vRuleId,
        @Param("attribute") String attribute);

    List<TQualityVerificationResult> selectByTableIdVersion1(@Param("errorId") String errorId, @Param("modeVersion") String modeVersion);

    List<TQualityVerificationResult> selectByTableIdVersionForCreate(@Param("modeId") String modeId, @Param("modeVersion") String modeVersion);

    List<SelectRulesVo> selectRules(String modelId);

    List<Map<String, Object>> checkCommStationName();

    List<GuideVo> selectGuide(String siteId);

    QualityVerificationRulesVo selectVerificationRules(String ruleId);

    List<QualityVerificationRulesVo> selectVerificationRuleOne(OneCommRule commRule);

    QualityVerificationRulesVo selectAssRuleVer(String ruleId);

    List<Map<String, String>> customizedSql(String sql);

    List<String> selectParams(@Param("comm") String comm, @Param("table") String table);

    int dataDicVerification(@Param("comm") String comm, @Param("table") String table, @Param("param") String param);

    Object selectSomeOneById(@Param("table") String table, @Param("id") String id);

    List<Map<String, Object>> useDefinedVerSql(String sql);

    List<Map<String, Object>> useDefinedVerSqlForLIMIT(@Param("sql") String sql, @Param("code") String code, @Param("codename") String codename, @Param("pageNum") Integer pageNum,
        @Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy);

    List<Map<String, String>> selectDictionaries(@Param("table") String table, @Param("id") String id, @Param("name") String name);

    List<Map<String, String>> selectDictionariesForLIMIT(@Param("table") String table, @Param("id") String id, @Param("name") String name, @Param("code") String code,
        @Param("codename") String codename, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("orderBy") String orderBy,
        @Param("constraint") String constraint);

    int selectDictionariesForLIMITCount(@Param("table") String table, @Param("id") String id, @Param("name") String name, @Param("code") String code,
        @Param("codename") String codename, @Param("constraint") String constraint);

    int useDefinedVerSqlForLIMITCount(@Param("sql") String sql, @Param("code") String code, @Param("codename") String codename);


    List<Map> selectSiteMaintenanceUnit(List<String> codes);


    List<Map> selectConnectParSite(List<String> codes);

    /**
     *  批量新增
     * @param tQualityVerificationRulesList
     * @return
     */
    int insertTQualityVerificationRulesList(@Param("list") List<TQualityVerificationRules> tQualityVerificationRulesList);

    int deleteTQualityVerificationRulesByColumnId(@Param("id") String id);

    int deleteTQualityResultByColumnId(@Param("columnId") String columnId,@Param("modelId") String modelId);

    /**
     * 查询数据质量-校验规则列表
     *
     * @param qualityVerificationRulesVo 数据质量-校验规则
     * @return 数据质量-校验规则集合
     */
    public List<QualityVerificationRulesVo> selectQualityVerificationRulesListVo(QualityVerificationRulesVo qualityVerificationRulesVo);

    List<SelectVo> selectSymbolList(@Param("englishName") String englishName);
}
