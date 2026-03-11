package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.vo.DictionaryInfoVo;
import com.mshz.cloud.tms.business.vo.MapMessage;
import com.mshz.cloud.tms.business.vo.ModelVo;
import com.mshz.cloud.tms.business.vo.OptionsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Creat By  cuiHy on 2022/11/3.
 */
@Mapper
public interface OptionsMapper {

    List<OptionsVo> getGridOptions(String name);

    List<OptionsVo> devTypeOptions(String name);

    List<OptionsVo> companyOptions(String name);

    ModelVo getModelId(String tableName);

    List<OptionsVo> getVoltageOptions(String name);

    BigDecimal getColunmId(@Param("modelId") BigDecimal modelId,@Param("columnName") String columnName);

    List<OptionsVo> getSiteStatusOptions(String name);

    List<OptionsVo> getSiteLevelOptions(String name);

    List<OptionsVo> getSiteTypeOptions(String name);

    List<MapMessage> getModelConditionDisplay(@Param("modelId")String modelId, @Param("modelVersion")String modelVersion);

    DictionaryInfoVo getDictionaryInformation(@Param("modelId")String modelId, @Param("modelVersion")String modelVersion, @Param("columnId")String columnId);

    List<Map<String,Object>> useDefinedVerSql(String sql);

    List<Map<String,String>> selectDictionaries(@Param("table")String table,@Param("id")String id,@Param("name")String name);
}
