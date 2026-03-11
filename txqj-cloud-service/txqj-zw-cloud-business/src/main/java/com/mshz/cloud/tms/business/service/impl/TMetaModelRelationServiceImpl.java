package com.mshz.cloud.tms.business.service.impl;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import com.mshz.cloud.tms.business.entity.TDbTableColumn;
import com.mshz.cloud.tms.business.entity.TMetaModelRelation;
import com.mshz.cloud.tms.business.mapper.TMetaModelRelationMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelTechMapper;
import com.mshz.cloud.tms.business.service.TMetaModelRelationService;
import com.mshz.cloud.tms.business.vo.MetadataRelationColumnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组合关联Service业务层处理
 * <p>
 *
 * @author zxx
 * @since 2022/10/8
 */
@Service
public class TMetaModelRelationServiceImpl implements TMetaModelRelationService {
    @Autowired
    private TMetaModelRelationMapper tMetaModelRelationMapper;
    @Autowired
    private TMetaModelTechMapper tMetaModelTechMapper;

    /**
     * 根据id查询组合关联列表-字段关联
     *
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    @Override
    public List<MetadataRelationColumnVo> selectMetadataRelation(Long modelId, String columnName, String modelVersion) {

        return tMetaModelRelationMapper.selectMetadataRelation(modelId, columnName, modelVersion);
    }

    /**
     * 关联数据源下拉框
     *
     * @return
     */
    @Override
    public List<TDbConfig> selectRelationDbSelect() {
        return tMetaModelRelationMapper.selectRelationDbSelect();
    }

    /**
     * 根据关联数据源查询关联表下拉框
     *
     * @return
     * @param dbId
     */
    @Override
    public List<TDbTable> selectRelationTableSelect(Long dbId) {
        return tMetaModelRelationMapper.selectRelationTableSelect(dbId);
    }

    /**
     * 根据id编辑组合关联
     *
     * @param metadataRelationColumnVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMetadataRelationColumnVo(MetadataRelationColumnVo metadataRelationColumnVo) {
        String time = DateUtil.now();

        TMetaModelRelation tMetaModelRelation = new TMetaModelRelation();
        tMetaModelRelation.setId(metadataRelationColumnVo.getRelationId());//
        tMetaModelRelation.setRelationDbId(metadataRelationColumnVo.getRelationDbId());
        tMetaModelRelation.setRelationTableAlias(metadataRelationColumnVo.getRelationTableAlias());
        tMetaModelRelation.setRelationTableId(metadataRelationColumnVo.getRelationTableId());
        tMetaModelRelation.setRelationColumnId(metadataRelationColumnVo.getRelationColumnId());
        tMetaModelRelation.setIsExternalConnection(metadataRelationColumnVo.getIsExternalConnection());
        tMetaModelRelation.setRelationCondition(metadataRelationColumnVo.getRelationCondition());
        tMetaModelRelation.setUpdateDate(time);
        tMetaModelRelation.setRelationType(metadataRelationColumnVo.getRelationType());
        tMetaModelRelation.setUpdateOrNot(metadataRelationColumnVo.getUpdateOrNot());
        int b = tMetaModelRelationMapper.updateTMetaModelRelation(tMetaModelRelation);
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMetadataRelationColumnVo(MetadataRelationColumnVo metadataRelationColumnVo,Long modelId) {
            String time = DateUtil.now();
            Long columnId = IdUtil.getSnowflakeNextId();
            TMetaModelRelation tMetaModelRelation = new TMetaModelRelation();
            tMetaModelRelation.setId(columnId);//
            tMetaModelRelation.setModelId(modelId);
            tMetaModelRelation.setColumnId(metadataRelationColumnVo.getTechId());
            tMetaModelRelation.setRelationDbId(metadataRelationColumnVo.getRelationDbId());
            tMetaModelRelation.setRelationTableAlias(metadataRelationColumnVo.getRelationTableAlias());
            tMetaModelRelation.setRelationTableId(metadataRelationColumnVo.getRelationTableId());
            tMetaModelRelation.setRelationColumnId(metadataRelationColumnVo.getRelationColumnId());
            tMetaModelRelation.setIsExternalConnection(metadataRelationColumnVo.getIsExternalConnection());
            tMetaModelRelation.setRelationCondition(metadataRelationColumnVo.getRelationCondition());
            tMetaModelRelation.setUpdateOrNot(metadataRelationColumnVo.getUpdateOrNot());
            tMetaModelRelation.setRelationType("1");
            tMetaModelRelation.setIsUsed(1);
            tMetaModelRelation.setModelVersion("edit");
        int b = tMetaModelRelationMapper.insertTMetaModelRelation(tMetaModelRelation);
            return b;
    }

    /**
     * 根据关联表查询关联属性下拉框
     *
     * @param tableId
     * @return
     */
    @Override
    public List<TDbTableColumn> selectRelationColumnSelect(Long tableId) {
        return tMetaModelRelationMapper.selectRelationColumnSelect(tableId);
    }

    /**
     * 根据id查询组合关联-sql关联
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    @Override
    public String selectMetadataRelationSQL(Long modelId, String modelVersion) {
        return tMetaModelRelationMapper.selectMetadataRelationSQL(modelId, modelVersion);
    }

    /**
     * 根据关联表查询关联属性信息列表
     *
     * @param tableId
     * @param columnName
     * @return
     */
    @Override
    public List<TDbTableColumn> selectRelationColumnInfoSelect(Long tableId, String columnName) {
        return tMetaModelRelationMapper.selectRelationColumnInfoSelect(tableId, columnName);
    }

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
    @Override
    public int updateRelationColumnResult(Long relationId, Long relationDbId, Long relationTableId,
        Long relationColumnId, Integer isExternalConnection, String relationCondition, String relationTableAlias) {
        String time = DateUtil.now();
        TMetaModelRelation tMetaModelRelation = new TMetaModelRelation();
        tMetaModelRelation.setId(relationId);//
        tMetaModelRelation.setRelationDbId(relationDbId);
        tMetaModelRelation.setRelationTableId(relationTableId);
        tMetaModelRelation.setRelationColumnId(relationColumnId);
        tMetaModelRelation.setIsExternalConnection(isExternalConnection);
        tMetaModelRelation.setRelationCondition(relationCondition);
        tMetaModelRelation.setRelationTableAlias(relationTableAlias);
        tMetaModelRelation.setUpdateDate(time);
        return tMetaModelRelationMapper.updateTMetaModelRelation(tMetaModelRelation);
    }

    /**
     * 关联后的sql结果保存(先删后增)
     *
     * @param modelId
     * @param sqlContent
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRelationSqlResult(Long modelId, List<Long> columnIdList, String sqlContent) {
        String time = DateUtil.now();
        // relation_type='2'，版本号为空的情况下的情况下，根据modelId删除组合关联表数据
        // 组合关联表新增
        List<TMetaModelRelation> tMetaModelRelationList = new ArrayList<>();
        for (Long columnId : columnIdList) {
            Long tMetaModelRelationId = IdUtil.getSnowflakeNextId();
            TMetaModelRelation tMetaModelRelation = new TMetaModelRelation();
            tMetaModelRelation.setId(tMetaModelRelationId);//
            tMetaModelRelation.setModelId(modelId);
            tMetaModelRelation.setColumnId(columnId);
            tMetaModelRelation.setCreateDate(time);
            tMetaModelRelation.setModelVersion("edit");
            tMetaModelRelation.setRelationType("2");
            tMetaModelRelation.setSqlContent(sqlContent);
            tMetaModelRelation.setIsUsed(1);
            String relationId = tMetaModelRelationMapper.selectMetadataRelationByModeId(modelId,columnId);
            if(relationId!=null&&!"".equals(relationId)){
                tMetaModelRelation.setId(Long.valueOf(relationId));//
                tMetaModelRelationMapper.updateTMetaModelRelation(tMetaModelRelation);
            }else {
                tMetaModelRelationMapper.insertTMetaModelRelation(tMetaModelRelation);
            }
        }
       return 1;
    }

    @Override
    public int updateColumnOrSql(Long modelId, String relationType, String notIsUsed) {

        return tMetaModelRelationMapper.updateColumnOrSql(modelId, relationType, notIsUsed);
    }

    @Override
    public int deleteTMetaModelRelationById(Long id) {
        return tMetaModelRelationMapper.deleteTMetaModelRelationById(id);
    }

}
