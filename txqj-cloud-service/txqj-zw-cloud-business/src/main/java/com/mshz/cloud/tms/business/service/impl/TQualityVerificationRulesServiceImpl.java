package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import com.mshz.cloud.tms.business.service.ITQualityVerificationRulesService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.vo.*;
import com.mshz.cloud.tms.business.mapper.TQualityVerificationRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 数据质量-基础校验规则Service业务层处理
 *
 * @author hzy
 * @date 2022-10-22
 */
@Service
public class TQualityVerificationRulesServiceImpl implements ITQualityVerificationRulesService {
    @Autowired
    private TQualityVerificationRulesMapper tQualityVerificationRulesMapper;

    /**
     * 查询数据质量-基础校验规则
     *
     * @param id 数据质量-基础校验规则主键
     * @return 数据质量-基础校验规则
     */
    @Override
    public TQualityVerificationRules selectTQualityVerificationRulesById(Long id) {
        return tQualityVerificationRulesMapper.selectTQualityVerificationRulesById(id);
    }

    /**
     * 查询技术元模型属性列表
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    @Override
    public List<TMetaModelTech> selectTMetaModelTechList(Long modelId, String columnName, String modelVersion)
    {
        return tQualityVerificationRulesMapper.selectTMetaModelTechList(modelId,columnName,modelVersion);
    }

    /**
     * 查询数据质量-基础校验规则列表
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 数据质量-基础校验规则
     */
    @Override
    public List<TQualityVerificationRules> selectTQualityVerificationRulesList(TQualityVerificationRules tQualityVerificationRules) {
        return tQualityVerificationRulesMapper.selectTQualityVerificationRulesList(tQualityVerificationRules);
    }

    /**
     * 新增数据质量-基础校验规则
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 结果
     */
    @Override
    public int insertTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules) {
        tQualityVerificationRules.setCreateTime(DateUtils.getNowDate());
        return tQualityVerificationRulesMapper.insertTQualityVerificationRules(tQualityVerificationRules);
    }

    /**
     * 修改数据质量-基础校验规则
     *
     * @param tQualityVerificationRules 数据质量-基础校验规则
     * @return 结果
     */
    @Override
    public int updateTQualityVerificationRules(TQualityVerificationRules tQualityVerificationRules) {
        tQualityVerificationRules.setUpdateTime(DateUtils.getNowDate());
        return tQualityVerificationRulesMapper.updateTQualityVerificationRules(tQualityVerificationRules);
    }

    /**
     * 批量删除数据质量-基础校验规则
     *
     * @param ids 需要删除的数据质量-基础校验规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityVerificationRulesByIds(Long[] ids) {
        return tQualityVerificationRulesMapper.deleteTQualityVerificationRulesByIds(ids);
    }

    /**
     * 删除数据质量-基础校验规则信息
     *
     * @param id 数据质量-基础校验规则主键
     * @return 结果
     */
    @Override
    public int deleteTQualityVerificationRulesById(Long id) {
        return tQualityVerificationRulesMapper.deleteTQualityVerificationRulesById(id);
    }

    @Override
    public List<SelectRulesVo> selectRules(String modelId) {
        return tQualityVerificationRulesMapper.selectRules(modelId);
    }

    @Override
    public QualityVerificationRulesVo selectVerificationRules(String var1) {
        return tQualityVerificationRulesMapper.selectVerificationRules(var1);
    }

    @Override
    public QualityVerificationRulesVo selectAssRuleVer(String ruleId) {
        return tQualityVerificationRulesMapper.selectAssRuleVer(ruleId);
    }

    @Override
    public List<GuideVo> selectGuide(String siteId) {
        return tQualityVerificationRulesMapper.selectGuide(siteId);
    }

    @Override
    public List<TableRuleVo> selectByTableIdVersion(String modeId, String modeVersion, String ruleId) {
        return tQualityVerificationRulesMapper.selectByTableIdVersion(modeId, modeVersion, ruleId, null);
    }

    @Override
    public List<TableRuleVo> selectByTableIdVersion(String modeId, String modeVersion, String ruleId, String attribute) {
        return tQualityVerificationRulesMapper.selectByTableIdVersion(modeId, modeVersion, ruleId, attribute);
    }

    @Override
    public List<Map<String, String>> selectDictionaries(String table, String id, String name) {
        return tQualityVerificationRulesMapper.selectDictionaries(table.toLowerCase(), id, name);
    }

    @Override
    public List<Map<String, String>> selectDictionariesForLIMIT(String table, String id, String name, String code, String codename, Integer pageNum, Integer pageSize,
        String orderBy, String constraint) {
        return tQualityVerificationRulesMapper.selectDictionariesForLIMIT(table.toLowerCase(), id, name, code, codename, pageNum, pageSize, orderBy, constraint);
    }

    @Override
    public int selectDictionariesForLIMITCount(String table, String id, String name, String code, String codename, String constraint) {
        return tQualityVerificationRulesMapper.selectDictionariesForLIMITCount(table.toLowerCase(), id, name, code, codename, constraint);
    }

    @Override
    public List<Map<String, Object>> useDefinedVerSql(String sql) {
        return tQualityVerificationRulesMapper.useDefinedVerSql(sql);
    }

    @Override
    public List<Map<String, Object>> useDefinedVerSqlForLIMIT(String sql, String code, String codename, Integer pageNum, Integer pageSize, String orderBy) {
        return tQualityVerificationRulesMapper.useDefinedVerSqlForLIMIT(sql, code, codename, pageNum, pageSize, orderBy);
    }

    @Override
    public int useDefinedVerSqlForLIMITCount(String sql, String code, String codename) {
        return tQualityVerificationRulesMapper.useDefinedVerSqlForLIMITCount(sql, code, codename);
    }

    @Override
    public List<QualityVerificationRulesVo> selectVerificationRuleOne(OneCommRule commRule) {
        return tQualityVerificationRulesMapper.selectVerificationRuleOne(commRule);
    }

    @Override
    public List<TQualityVerificationResult> selectByTableIdVersion1(String errorId, String modeVersion) {
        return tQualityVerificationRulesMapper.selectByTableIdVersion1(errorId, modeVersion);
    }

    @Override
    public List<TQualityVerificationResult> selectByTableIdVersionForCreate(String modeId, String modeVersion) {
        return tQualityVerificationRulesMapper.selectByTableIdVersionForCreate(modeId, modeVersion);
    }

    @Override
    public List<Map> selectSiteMaintenanceUnit(List<String> codes) {
        return tQualityVerificationRulesMapper.selectSiteMaintenanceUnit(codes);
    }

    @Override
    public List<Map> selectConnectParSite(List<String> codes) {
        return tQualityVerificationRulesMapper.selectConnectParSite(codes);
    }

    /**
     * 查询数据质量-校验规则列表
     *
     * @param qualityVerificationRulesVo 数据质量-校验规则
     * @return 数据质量-校验规则
     */
    @Override
    public List<QualityVerificationRulesVo> selectQualityVerificationRulesListVo(QualityVerificationRulesVo qualityVerificationRulesVo)
    {
        return tQualityVerificationRulesMapper.selectQualityVerificationRulesListVo(qualityVerificationRulesVo);
    }

    @Override
    public List<SelectVo> selectSymbolList(String englishName) {
        return tQualityVerificationRulesMapper.selectSymbolList(englishName);
    }
}
