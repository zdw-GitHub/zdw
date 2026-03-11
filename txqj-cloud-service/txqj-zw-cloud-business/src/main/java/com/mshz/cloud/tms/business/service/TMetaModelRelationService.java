package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import com.mshz.cloud.tms.business.entity.TDbTableColumn;
import com.mshz.cloud.tms.business.vo.MetadataRelationColumnVo;
import java.util.List;

/**
 * <p>
 * 组合关联service标签
 * <p>
 *
 * @author zxx
 * @since 2022/10/8
 */
public interface TMetaModelRelationService {
    /**
     * 根据id查询组合关联列表-字段关联
     *
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    List<MetadataRelationColumnVo> selectMetadataRelation(Long modelId, String columnName, String modelVersion);

    /**
     * 关联数据源下拉框
     *
     * @return
     */
    List<TDbConfig> selectRelationDbSelect();

    /**
     * 根据关联数据源查询关联表下拉框
     *
     * @return
     * @param dbId
     */
    List<TDbTable> selectRelationTableSelect(Long dbId);

    /**
     * 根据id编辑组合关联
     *
     * @param metadataRelationColumnVo
     * @return
     */
    int updateMetadataRelationColumnVo(MetadataRelationColumnVo metadataRelationColumnVo);

    int insertMetadataRelationColumnVo(MetadataRelationColumnVo metadataRelationColumnVo,Long modelId);

    /**
     * 根据关联表查询关联属性下拉框
     *
     * @param tableId
     * @return
     */
    List<TDbTableColumn> selectRelationColumnSelect(Long tableId);

    /**
     * 根据id查询组合关联-sql关联
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    String selectMetadataRelationSQL(Long modelId, String modelVersion);

    /**
     * 根据关联表查询关联属性信息列表
     *
     * @param tableId
     * @param columnName
     * @return
     */
    List<TDbTableColumn> selectRelationColumnInfoSelect(Long tableId, String columnName);

    /**
     * 关联后的结果保存
     *
     * @param relationId
     * @param relationDbId
     * @param relationTableId
     * @param relationColumnId
     * @param isExternalConnection
     * @param relationCondition
     * @param relationTableAlias
     * @return
     */
    int updateRelationColumnResult(Long relationId, Long relationDbId, Long relationTableId, Long relationColumnId,
        Integer isExternalConnection, String relationCondition, String relationTableAlias);

    /**
     * 关联后的sql结果保存(先删后增)
     *
     * @param modelId
     * @param columnIdList
     * @param sqlContent
     * @return
     */
    int updateRelationSqlResult(Long modelId, List<Long> columnIdList, String sqlContent);

    int updateColumnOrSql(Long modelId, String relationType, String notIsUsed);

    public int deleteTMetaModelRelationById(Long id);
}
