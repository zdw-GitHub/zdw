package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.vo.GuideVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 通信站基本信息Mapper接口
 *
 * @author zdw
 * @date 2022-11-02
 */
public interface ResourceManagementMapper
{
    List<GuideVo> selectGuideVoListByModel(@Param("modelId") String modelId, @Param("modelVersion")  String modelVersion);

    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo);

    public String selectTableNameByModelId(String  modelId);
    public String selectTableNameCnByModelId(String  modelId);

    public List<Map> selectAll(CommonVo vo);


    public Map<String,String> selectOne(CommonVo vo);

    public void updateSql(CommonVo vo);

    public void insertSql(CommonVo vo);

    public void deleteSql(CommonVo vo);


    List<Map> selectOneline();


    List<Map> selectFiberParLineByIds(List<String> ids);

    Long selectMaxId(@Param("modelName") String modelName,@Param("maxIdRange") String maxIdRange);

    String selectPortGuid(String parCard);

    String selectCardGuid(String parSlot);

    String selectSlotGuid(String parShelf);
    String selectShelfGuid(String parNe);


    Long selectCount(@Param("tableName")String tableName, @Param("columnName")String columnName, @Param("columnValue")String columnValue);
    String selectAttribute(@Param("id")String id,@Param("tableName")String tableName, @Param("attribute")String attribute);

    Map selectTMetaModelIdRuleByPrefixEncodingDefault(String prefixEncodingDefault);

    int doesTheColumnNameExist(@Param("tableName")String tableName, @Param("columnName")String columnName);

    Map<String, String> selectTableMessage(String modelId);

    /**
     * 根据模型ID查询表主键字段（t_meta_model_tech中is_primary_key='1'的字段）
     * @param modelId 模型ID
     * @return 主键字段信息
     */
    MetaModelShowVo selectPrimaryKeyByModelId(String modelId);
}
