package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.vo.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 元模型Service接口
 *
 * @author zdw
 * @date 2022-09-23
 */
public interface TMetaModelService {
    /**
     * 根据id查询业务元数据信息
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    MetadataBuzInfoVo selectTMetaModelBuzById(Long modelId, String modelVersion);

    /**
     * 查询所有类目及包括的元模型树
     *
     * @return
     */
    List<MetadataModelVo> selectMetaModelTree();


    /**
     * 查询所有类目及包括的元模型树
     *
     * @return
     */
    List<MetadataModelVo> selectMetaModelTreeByTableName(TMetaModel tMetaModel);

    List<MetadataModelVo> jobInsertTree();

    /**
     * 查询所有类目及包括的元模型树，带有元模型版本号
     *
     * @return
     */
    List<TreeVo> selectMetaModelTreeWithVersion(TMetaModel tMetaModel);

    List<TreeVo> selectMetaModelTreeWithVersionMiddle(TMetaModel tMetaModel);

    List<TreeVo> selectMetaModelTreeForDataShare(TMetaModel tMetaModel);

    /**
     * 新增元模型
     *
     * @param metadataBuzInfoVo
     * @return
     */
    int insertTMetaModel(MetadataBuzInfoVo metadataBuzInfoVo);

    /**
     * 发布元模型
     *
     * @param modelId
     * @param modelVersion
     * @param updateLog
     * @param remark
     * @param tableName
     * @return
     */
    int insertTMetaModelRelease(Long modelId, String modelVersion, String updateLog, String remark, String tableName);

    /**
     * 删除编辑元模型相关数据
     *
     * @param modelId
     * @param tableName
     * @return
     */
    int deleteMetaMode(Long modelId, String tableName);

    /**
     * 复制发布的元模型
     *
     * @param modelId
     * @param modelVersion
     * @param tableName
     * @param tableNameCn
     * @return
     */
    int copyTMetaModelRelease(Long modelId, String modelVersion, String tableName, String tableNameCn);

    /**
     * 查询该元模型的历史版本号
     *
     * @param modelId
     * @return
     */
    List<String> selectMetaModelVersionList(Long modelId);

    /**
     * 查询元模型
     *
     * @param id 元模型主键
     * @return 元模型
     */
    public TMetaModel selectTMetaModelById(Long id);

    /**
     * 查询元模型列表
     *
     * @param tMetaModel 元模型
     * @return 元模型集合
     */
    public List<TMetaModel> selectTMetaModelList(TMetaModel tMetaModel);

    /**
     * 新增元模型
     *
     * @param tMetaModel 元模型
     * @return 结果
     */
    public int insertTMetaModel(TMetaModel tMetaModel);

    /**
     * 修改元模型
     *
     * @param tMetaModel 元模型
     * @return 结果
     */
    public int updateTMetaModel(TMetaModel tMetaModel);

    /**
     * 批量删除元模型
     *
     * @param ids 需要删除的元模型主键集合
     * @return 结果
     */
    public int deleteTMetaModelByIds(Long[] ids);

    /**
     * 删除元模型信息
     *
     * @param id 元模型主键
     * @return 结果
     */
    public int deleteTMetaModelById(Long id);

    /**
     * 根据id保存业务元数据信息
     *
     * @param modelId
     * @param metadataBuzInfoVo
     * @param oldTableName
     * @return
     */
    int keepTMetaModelBuzById(Long modelId, MetadataBuzInfoVo metadataBuzInfoVo, String oldTableName);

    /**
     * 根据编号查询元模型详细信息包括数据转换的主表
     *
     * @param id
     * @return
     */
    MetaModelDetailVo selectTMetaModelDetail(@Param("id") Long id);

    ModelVo getModelId(String tableName);

    String whetherTheTableExists(String tableName);

    /**
     * 查询元模型关联主表的数据库信息
     * @param id    元模型id
     * @return
     */
    Map<String, Object> getMiddleDbInfo(Long id);

    /**
     * 查询元模型字段关联的源数据库信息
     * @param id    元模型id
     * @return
     */
    List<Map<String, Object>> getRelationDbInfo(Long id);

    List<TMetaModel> selectMetaModelWithoutIdRuleConfig(String category);

}
