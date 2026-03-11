package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.vo.MetaModelDetailVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.vo.MetadataBuzInfoVo;
import com.mshz.cloud.tms.business.vo.ModelVo;
import com.mshz.cloud.tms.business.vo.TableAssociationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

;

/**
 * 元模型Mapper接口
 *
 * @author zxx
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelMapper {
    /**
     * 根据id查询业务元数据信息
     *
     * @param modelId
     * @return
     */
    MetadataBuzInfoVo selectTMetaModelBuzById(@Param("modelId") Long modelId);

    /**
     * 查询所有类目及包括的元模型
     *
     * @return
     */
    List<TMetaModel> selectMetaModelTree();

    /**
     * 查询说有类目
     *
     * @return
     */
    List<String> selectCategoryNames();

    /**
     * 查询元模型表拷贝到元模型发布表的数据
     *
     * @param modelId
     * @return
     */
    TMetaModel selectMetaModelCopy(@Param("modelId") Long modelId);

    /**
     * 根据编号查询元模型详细信息包括数据转换的主表
     *
     * @param id
     * @return
     */
    MetaModelDetailVo selectTMetaModelDetail(@Param("id") Long id);

    /**
     * //将版本号写入元模型表关联标签，技术元模型属性表，业务元数据表， // 组合关系表，元模型操作日志，安全元数据表，展示配置表
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    int setModelVersion(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     * 根据id逻辑删除元模型表
     *
     * @param modelId
     * @return
     */
    int setMetaModelDeleteById(@Param("modelId") Long modelId);

    /**
     * 删除编辑元模型相关数据
     *
     * @param modelId
     * @return
     */
    int deleteMetaMode(@Param("modelId") Long modelId);

    /**
     * 查询该元模型的历史版本号
     *
     * @param modelId
     * @return
     */
    List<String> selectMetaModelVersionList(@Param("modelId") Long modelId);

    /**
     * 校检元模型名字是否重复
     *
     * @param tableName
     * @return
     */
    String selectMetaModelName(@Param("tableName") String tableName);

    /**
     * 查询元模型
     *
     * @param id 元模型主键
     * @return 元模型
     */
    public TMetaModel selectTMetaModelById(Long id);

    public TableAssociationVo getTableAssociation(MetaModelShowVo metaModelShowVo);

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
     * 删除元模型
     *
     * @param id 元模型主键
     * @return 结果
     */
    public int deleteTMetaModelById(Long id);

    /**
     * 批量删除元模型
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelByIds(Long[] ids);

    List<TMetaModel> selectMetaModelCategory();

    List<TMetaModel>  selectMetaModelCategoryByTableName(TMetaModel tMetaModel);

    List<TMetaModel> selectMetaModelCategoryJob();

    List<TMetaModel>  selectMetaModelWithoutIdRuleConfig();

    ModelVo getModelId(String tableName);

    String getMiddleTableName(String tableId);


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


}
