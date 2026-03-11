package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.mapper.*;
import com.mshz.cloud.tms.business.service.*;
import com.mshz.cloud.tms.business.vo.*;
import com.mshz.cloud.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author wh
 * @date 2022/10/18
 */
@Service
public class MetaDataSearchServiceImpl implements MetaDataSearchService {

    @Resource
    private CommonMetaMapper commonMetaMapper;
    @Resource
    private TMetaModelShowService showService;

    @Resource
    private TMetaModelVersionMapper versionMapper;

    @Resource
    private TMetaModelService modelService;

    private static Logger logger = LoggerFactory.getLogger(MetaDataSearchServiceImpl.class);

    @Override
    public GridInitDataVo initGridData(MetaModelShowVo metadataShowVo) {

        GridInitDataVo data = new GridInitDataVo();
        List<MetaModelShowVo> attributes = showService.selectMetaModelShowVoList(metadataShowVo);

        // 查询条件属性
        List<GridColumnVo> searchAttributes = new ArrayList<>();
        // 列表属性
        List<GridColumnVo> columns = new ArrayList<>();
        for (int i = 0; i < attributes.size(); i++) {
            MetaModelShowVo record = attributes.get(i);
            GridColumnVo vo = new GridColumnVo();
            vo.setId(record.getId());//
            vo.setAttributeName(record.getColumnName());
            vo.setAttributeNameCn(record.getColumnNameCn());
            vo.setModelId(record.getModelId());
            String isShow = record.getIsVisible();
            if (isShow != null && isShow.equals("1")) {
                columns.add(vo);
            }
            String isSearch = record.getIsQuery();
            if (isSearch != null && isSearch.equals("1")) {
                searchAttributes.add(vo);
            }
        }
        data.setColumns(columns);
        data.setSearchAttributes(searchAttributes);
        return data;
    }

    @Override
    public List<Map> queryList(MetaDataParaVo para) {
        Long modelId = para.getModelId();
        String alias = versionMapper.selectModelVersionAlias(para.getModelVersion(), modelId);
        String sql = para.packageListSqlLike(alias, para.getIsPage());
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> result = commonMetaMapper.selectAll(vo);
        validationIsNull(result);
        return result;
    }


    @Override
    public long queryCount(MetaDataParaVo para) {
        Long modelId = para.getModelId();
        String alias = versionMapper.selectModelVersionAlias(para.getModelVersion(), modelId);
        String sql = para.packageCountSqlLike(alias, para.getIsPage());
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> list = commonMetaMapper.selectAll(vo);
        long result = (long)list.get(0).get("COUNTNUM");
        return result;
    }


    private void validationIsNull(List<Map> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Map m = data.get(i);
                for (Object key : m.keySet()) {
                    String value = m.get(key) + "";
                    if (value != null && value.equals("null")) {
                        m.put(key, "");
                    }
                } // for m
            } // for data
        } // if if(data!=null&&data.size()>0)
    }

    private String packageKey(String dbName, String modelName) {
        return dbName + "." + modelName;
    }

    @Override
    public String getRelationType(MetaModelShowVo para) {
        String type = null;
        List<MetaDataSyncVo> metaDataSyncVoList = showService.selectMetaDataSyncVoList(para);
        if (metaDataSyncVoList != null && metaDataSyncVoList.size() > 0) {
            type = metaDataSyncVoList.get(0).getRelationType();
        }
        return type;
    }


    @Override
    public TestSQLResultVo calcSQl(long modelId, String modelVersion, int relationType) {
        TestSQLResultVo resultVo = new TestSQLResultVo();
        MetaModelShowVo metadataShowVo  = new MetaModelShowVo();
        metadataShowVo.setModelId(modelId);
        metadataShowVo.setModelVersion(modelVersion);
        // 查询当前版本次元模型关联的源数据模型表的所有字段映射信息（计算表字段信息）
        List<MetaDataSyncVo> metaDataSyncVoList = showService.selectMetaDataSyncVoList(metadataShowVo);
        if (metaDataSyncVoList == null || metaDataSyncVoList.size() == 0) {
            resultVo.setCode("fail");
            resultVo.setError("当前未找到组合属性值");
            return resultVo;
        }
        MetaDataSyncVo firstEntity = metaDataSyncVoList.get(0);
        if (!"1".equals(relationType)) {
            if (StringUtils.isEmpty(firstEntity.getSqlContent())) {
                resultVo.setCode("fail");
                resultVo.setError("当前未找到SQL关联查询SQL");
                return resultVo;
            }
            resultVo.setCode("success");
            resultVo.setSql(firstEntity.getSqlContent());
            return resultVo;
        }

        // 存储别名，判断别名是否已经存在。key:表别名，value:dbName.ModelName
        Map<String, String> isExistMap = new HashMap<>();
        // 别名Map key：dbName.ModelName；value：alias
        Map<String, String> aliasMap = new HashMap<>();
        MetaModelDetailVo modelDetailVo = modelService.selectTMetaModelDetail(modelId);
        String relationMainDbUserNam = modelDetailVo.getRelationMainDbUserName();
        String relationMainTableName = modelDetailVo.getRelationMainTableName();
        String relationMainTableAlias = modelDetailVo.getRelationMainTableAlias();

        if (relationMainTableAlias != null && !relationMainTableAlias.equals("")) {
            String key = packageKey(relationMainDbUserNam, relationMainTableName);
            aliasMap.put(key, relationMainTableAlias);
            isExistMap.put(relationMainTableAlias, key);
        }
        // 计算表的别名
        for (int i = 0; i < metaDataSyncVoList.size(); i++) {
            MetaDataSyncVo record = metaDataSyncVoList.get(i);
            String dbName = record.getDbUserName();
            String tableName = record.getTableName();
            String tableAlias = record.getRelationTableAlias();
            // 集合存储表与别名键值对
            // 拼接key 数据库名.表名，用于存储
            String key = packageKey(dbName, tableName);
            if (tableAlias != null && !"".equals(tableAlias)) {
                // 此处value为 数据库名.表名
                String value = isExistMap.get(tableAlias);
                if (value == null) {
                    isExistMap.put(tableAlias, key);
                    // 根据数据库名.表名，在别名map中查询对应表别名是否存在，不存在则置入
                    aliasMap.putIfAbsent(key, tableAlias);
                } else {
                    // 根据表名获取已存储对应的表别名
                    String midAlias = aliasMap.get(value);
                    if (!midAlias.equals(tableAlias)) {
                        // 表别名重复
                        resultVo.setCode("fail");
                        resultVo.setError("表别名重复");
                        return resultVo;
                    }
                }
            } // if tableAlias null ""
        }


        // 拼装select sql
        StringBuilder selectSB = new StringBuilder();
        selectSB.append("select ");
        Map<String, List<MetaDataSyncVo>> relationInfoMap = new HashMap<>();


        // 拼装查询属性 计算关联关系
        for (int i = 0; i < metaDataSyncVoList.size(); i++) {
            MetaDataSyncVo record = metaDataSyncVoList.get(i);
            String dbName = record.getDbUserName();
            String tableName = record.getTableName();
            String tableAlias = record.getRelationTableAlias();
            String condition = record.getRelationCondition();

            // 添加select 的属性
            String relationColumnName = record.getColumnName();
            if (relationColumnName != null && !"".equals(relationColumnName)) {
                // FUNC-MO 2023-04-08 适配元数据同步过程中不同关联方式采集源数据查询sql查询字段大小写不一致的情况，字段匹配查询采集源数据字段默认小写，sql匹配查询字段前端识别强制大写
                selectSB.append(tableAlias).append(".`").append(record.getColumnName().toUpperCase(Locale.ROOT)).append("`").append(" ").append(record.getTargetColumnName())
                        .append(",");
            }
            if (StringUtils.isEmpty(tableName)) {
                continue;
            }
            String key = packageKey(dbName, tableName);
            List<MetaDataSyncVo> recordList = relationInfoMap.get(key);
            if (recordList == null) {
                recordList = new ArrayList<MetaDataSyncVo>();
                recordList.add(record);
                relationInfoMap.put(key, recordList);
            } else {
                if (condition != null && !"".equals(condition)) {
                    recordList.add(record);
                }
            }

        }


        // from的表的信息
        if (relationMainTableAlias != null && !"".equals(relationMainTableAlias)) {
            // 从主表查询采集源数据
            selectSB.append(" from ").append(relationMainDbUserNam).append(".").append(relationMainTableName).append(" ").append(relationMainTableAlias);

        } else {
            // 未配置主表信息时，直接从查询元模型字段关联关系的第一个元素中获取采集源数据表查询
            selectSB.append(" from ").append(firstEntity.getDbUserName()).append(".").append(firstEntity.getTableName()).append(" ").append(firstEntity.getRelationTableAlias());
        }
        // keySet获取map集合key的集合 然后在遍历key即可
        for (String key : relationInfoMap.keySet()) {
            List<MetaDataSyncVo> attributesR = relationInfoMap.get(key);
            if (attributesR != null && attributesR.size() > 0) {
                MetaDataSyncVo firstRelation = attributesR.get(0);

                if (!CollectionUtils.isEmpty(attributesR) && !"".equals(attributesR.get(0).getRelationCondition())) {
                    int firstIsExternalConnection = firstRelation.getIsExternalConnection();
                    if (firstIsExternalConnection == 1) {
                        selectSB.append(" left ");
                    }
                    String relationDBUserName = firstRelation.getDbUserName();

                    selectSB.append(" join ").append(relationDBUserName).append(".").append(firstRelation.getTableName()).append(" ").append(firstRelation.getRelationTableAlias());
                    selectSB.append(" on ").append(firstRelation.getRelationCondition());
                    for (int i = 1; i < attributesR.size(); i++) {
                        selectSB.append(" and ").append(firstRelation.getRelationCondition());
                    }
                }
            } // if attributesR!=null&&attributesR.size()>0
        }
        resultVo.setCode("success");
        resultVo.setSql(selectSB.toString());
        return resultVo;
    }
}
