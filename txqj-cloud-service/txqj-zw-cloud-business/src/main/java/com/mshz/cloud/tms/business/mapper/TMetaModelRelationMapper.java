package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import com.mshz.cloud.tms.business.entity.TDbTableColumn;
import com.mshz.cloud.tms.business.entity.TMetaModelRelation;
import com.mshz.cloud.tms.business.vo.MetadataRelationColumnVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合关系Mapper接口
 *
 * @author zxx
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelRelationMapper
{
    /**
     * 查询组合关系
     *
     * @param id 组合关系主键
     * @return 组合关系
     */
    public TMetaModelRelation selectTMetaModelRelationById(Long id);

    /**
     * 查询组合关系列表
     *
     * @param tMetaModelRelation 组合关系
     * @return 组合关系集合
     */
    public List<TMetaModelRelation> selectTMetaModelRelationList(TMetaModelRelation tMetaModelRelation);

    /**
     * 新增组合关系
     *
     * @param tMetaModelRelation 组合关系
     * @return 结果
     */
    public int insertTMetaModelRelation(TMetaModelRelation tMetaModelRelation);

    /**
     * 修改组合关系
     *
     * @param tMetaModelRelation 组合关系
     * @return 结果
     */
    public int updateTMetaModelRelation(TMetaModelRelation tMetaModelRelation);

    /**
     * 删除组合关系
     *
     * @param id 组合关系主键
     * @return 结果
     */
    public int deleteTMetaModelRelationById(Long id);

    /**
     * 批量删除组合关系
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelRelationByIds(Long[] ids);

    /**
     * 组合关系表复制，根据modelId和版本号
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<TMetaModelRelation> selectMetaModelRelationListCopy(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     *  组合关系表复制-新增组合关系集合
     * @param tMetaModelRelationList
     * @return
     */
    int insertTMetaModelRelationList(@Param("list") List<TMetaModelRelation> tMetaModelRelationList);

    /**
     * 根据id查询组合关联列表-字段关联
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    List<MetadataRelationColumnVo> selectMetadataRelation(@Param("modelId") Long modelId, @Param("columnName") String columnName, @Param("modelVersion") String modelVersion);

    /**
     *  关联数据源下拉框
     * @return
     */
    List<TDbConfig> selectRelationDbSelect();

    /**
     *  根据关联数据源查询关联表下拉框
     * @param dbId
     * @return
     */
    List<TDbTable> selectRelationTableSelect(@Param("dbId")Long dbId);

    /**
     * 根据关联表查询关联属性下拉框
     * @param tableId
     * @return
     */
    List<TDbTableColumn> selectRelationColumnSelect(@Param("tableId")Long tableId);

    /**
     *  根据id查询组合关联-sql关联
     * @param modelId
     * @param modelVersion
     * @return
     */
    String selectMetadataRelationSQL(@Param("modelId")Long modelId,@Param("modelVersion") String modelVersion);

    String selectMetadataRelationByModeId(@Param("modelId")Long modelId,@Param("colId") Long colId);

    /**
     * 根据关联表查询关联属性信息列表
     * @param tableId
     * @param columnName
     * @return
     */
    List<TDbTableColumn> selectRelationColumnInfoSelect(@Param("tableId") Long tableId,@Param("columnName")String columnName);

    /**
     *  清空编辑下sql关联下的属性
     * @param modelId
     * @return
     */
    int deleteTMetaModelRelationByType(@Param("modelId")Long modelId,@Param("columnId")Long columnId);

    int updateColumnOrSql(@Param("modelId") Long modelId, @Param("relationType") String relationType,@Param("notIsUsed")  String notIsUsed);
}
