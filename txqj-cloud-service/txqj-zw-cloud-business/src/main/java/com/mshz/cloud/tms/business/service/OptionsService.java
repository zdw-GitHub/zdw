package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.vo.DictionariesVo;
import com.mshz.cloud.tms.business.vo.DictionaryInfoVo;
import com.mshz.cloud.tms.business.vo.MapMessage;
import com.mshz.cloud.tms.business.vo.ModelVo;
import com.mshz.cloud.tms.business.vo.OptionsVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Creat By  cuiHy on 2022/11/3.
 */
public interface OptionsService {

    List<OptionsVo> getGridOptions(String name);

    List<OptionsVo> devTypeOptions(String name);

    List<OptionsVo> companyOptions(String name);

    ModelVo getModelId(String tableName);

    List<OptionsVo> getVoltageOptions(String name);

    BigDecimal getColunmId(BigDecimal modelId, String colunmName);

    List<OptionsVo> getSiteStatusOptions(String name);

    List<OptionsVo> getSiteLevelOptions(String name);

    List<OptionsVo> getSiteTypeOptions(String name);

    List<MapMessage> getModelConditionDisplay(String modelId, String modelVersion);

    List<Map<String,String>> selectDictionaries(String table, String id, String name);

    List<Map<String,Object>> useDefinedVerSql(String sql);

   DictionaryInfoVo getDictionaryInformation(String modelId, String modelVersion, String columnId);

   List<DictionariesVo> getMapData(DictionaryInfoVo dictionaryInfoVo);
}
