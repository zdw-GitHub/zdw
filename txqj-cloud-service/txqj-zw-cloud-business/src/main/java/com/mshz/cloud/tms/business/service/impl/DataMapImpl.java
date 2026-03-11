package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.vo.MapDataVo;
import com.mshz.cloud.tms.business.vo.MapLinkVo;
import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.entity.TMetaModelRelation;
import com.mshz.cloud.tms.business.mapper.TMetaModelMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelRelationMapper;
import com.mshz.cloud.tms.business.vo.DataMapParaVo;
import com.mshz.cloud.tms.business.vo.DataMapReturnVo;
import com.mshz.cloud.tms.business.service.IDataMapService;
import com.mshz.cloud.common.util.StringUtils;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @date 2023/07/31
 */
@Service
public class DataMapImpl implements IDataMapService {
    @Resource
    private TMetaModelMapper metaModelMapper;
    @Resource
    private TMetaModelRelationMapper relationMapper;
    @Resource
    private CommonMetaMapper commonMetaMapper ;

    @Override
    public DataMapReturnVo getDataMap(DataMapParaVo para) {
        DataMapReturnVo returnVo = new DataMapReturnVo();
        String dataType = para.getDataType();
        String dataName = para.getDataName();
        List<MapDataVo> data = new ArrayList<>();
        List<MapLinkVo> links = new ArrayList<>();
        Map<String,MapDataVo> dataMap = new HashMap<>();
        Map<String,MapLinkVo> linkMap = new HashMap<>();

        //计算元模型数据为map
        List<TMetaModel> tMetaModelList = metaModelMapper.selectTMetaModelList(null);
        Map<Long, List<TMetaModel>> metaModelMap = new HashMap<>();
        for(TMetaModel record : tMetaModelList) {
            Long category = record.getCategoryNameId();
            List<TMetaModel> recordList = metaModelMap.get(category);
            if (recordList == null) {
                recordList = new ArrayList<>();
                recordList.add(record);
                metaModelMap.put(category, recordList);
            } else {
                recordList.add(record);
            }
        }


        //计算relataion数据为map
        List<TMetaModelRelation> relationList = relationMapper.selectTMetaModelRelationList(null);
        Map<Long, List<TMetaModelRelation>> relationMap = new HashMap<>();
        for(TMetaModelRelation record : relationList) {
            long id = record.getModelId();
            List<TMetaModelRelation> recordList = relationMap.get(id);
            if (recordList == null) {
                recordList = new ArrayList<>();
                recordList.add(record);
                relationMap.put(id, recordList);
            } else {
                recordList.add(record);
            }
        }
        //计算table数据为map
        List<TDbTable> tableList = commonMetaMapper.selectTDbTableList(null);
        Map<Long, TDbTable> tableMap = new HashMap<>();
        for(TDbTable record : tableList) {
            long id = record.getId();
            TDbTable table = tableMap.get(id);
            if (table == null) {
                tableMap.put(id, record);
            }
        }
        //计算db数据为map
        List<TDbConfig> dbConfigList = commonMetaMapper.selectTDbConfigList(null);
        Map<Long, TDbConfig> dbMap = new HashMap<>();
        for(TDbConfig record : dbConfigList) {
            long id = record.getId();
            TDbConfig db = dbMap.get(id);
            if (db == null) {
                dbMap.put(id, record);
            }
        }

        List<TMetaModel> modelList = new ArrayList<>();
        //封装地图数据
        switch(dataType){
            case "type_meta_category":
                MapDataVo categoryMapDataVo = new MapDataVo();
                categoryMapDataVo.setId(para.getId()+"");//
                categoryMapDataVo.setName(para.getDataName());
                categoryMapDataVo.setDataType("type_meta_category");
                //data.add(categoryMapDataVo);
                addData(data, dataMap, categoryMapDataVo);
                if(dataType.equals("其他")){
                    modelList = metaModelMap.get(null);
                }else{
                    modelList = metaModelMap.get(para.getId());
                }
                break;
            case "type_meta_model":
                TMetaModel model = new TMetaModel();
                model.setId(para.getId());//
                model.setTableNameCn(para.getDataName());
                modelList.add(model);
                break;
        }
        //获取源数据表
        if(modelList!=null){
            for (TMetaModel model:modelList) {
                if(dataType.equals("type_meta_category")){
                    //添加link
                    MapLinkVo cRmLinkVo = new MapLinkVo();
                    cRmLinkVo.setSourceId(para.getId()+"");
                    cRmLinkVo.setSource(para.getDataName());
                    cRmLinkVo.setTargetId(model.getId()+"");
                    cRmLinkVo.setTarget(getModelName(model));
                    addLink(links,linkMap, cRmLinkVo);
                }

                //添加model data
                MapDataVo modelMapDataVo = new MapDataVo();
                modelMapDataVo.setId(model.getId()+"");//
                modelMapDataVo.setName(getModelName(model));
                modelMapDataVo.setDataType("type_meta_model");
                addData(data, dataMap, modelMapDataVo);

                //获取通过关联关系获取数据表和数据库信息
                List<TMetaModelRelation> recordRelations =  relationMap.get(model.getId());
                if(recordRelations!=null){
                    for (TMetaModelRelation relation: recordRelations) {

                        if(relation.getRelationTableId()!=null&&tableMap.get(relation.getRelationTableId())!=null){
                            //获取表数据
                            TDbTable table = tableMap.get(relation.getRelationTableId());

                            //封装model 和 table 关系 link
                            MapLinkVo mRtLinkVo = new MapLinkVo();
                            mRtLinkVo.setSourceId(model.getId()+"");
                            mRtLinkVo.setSource(getModelName(model));
                            mRtLinkVo.setTargetId(table.getId()+"");
                            mRtLinkVo.setTarget(getTableName(table));
                            addLink(links,linkMap, mRtLinkVo);

                            //添加表 data
                            MapDataVo tableMapDataVo = new MapDataVo();
                            tableMapDataVo.setId(table.getId()+"");//
                            tableMapDataVo.setName(getTableName(table));
                            tableMapDataVo.setDataType("type_table");
                            addData(data, dataMap, tableMapDataVo);


                            //封装  表 和 数据库 关系 link
                            TDbConfig db = dbMap.get(relation.getRelationDbId());
                            MapLinkVo tRdLinkVo = new MapLinkVo();
                            tRdLinkVo.setSourceId(table.getId()+"");
                            tRdLinkVo.setSource(getTableName(table));
                            tRdLinkVo.setTargetId(db.getId()+"");
                            tRdLinkVo.setTarget(getDbName(db));
                            addLink(links,linkMap, tRdLinkVo);
                            //添加数据库 data
                            MapDataVo dbMapDataVo = new MapDataVo();
                            dbMapDataVo.setId(db.getId()+"");//
                            dbMapDataVo.setName(getDbName(db));
                            dbMapDataVo.setDataType("type_db");
                            addData(data, dataMap, dbMapDataVo);

                        }// if  relation.getRelationTableId()!=null
                    }//if relation.getRelationTableId()!=null
                }//if recordRelations!=null
            }//for modelList
        }// if modelList!=null

        //获取元模型
        returnVo.setData(data);
        returnVo.setLinks(links);
        return returnVo;
    }

    private void addData(List<MapDataVo> data,Map<String,MapDataVo> dataMap, MapDataVo vo){
        String key =packageKey(vo.getId(),vo.getDataType());
        if(dataMap.get(key)==null){
            dataMap.put(key,vo);
            data.add(vo);
        }
    }

    private String packageKey(String str1,String str2){
        return str1+"$$"+str2;
    }

    private void addLink(List<MapLinkVo> links,Map<String,MapLinkVo> map, MapLinkVo vo){
        String key =packageKey(vo.getSourceId(),vo.getTargetId());
        if(map.get(key)==null){
            map.put(key,vo);
            links.add(vo);
        }
    }


    private String getModelName(TMetaModel model){
        if(StringUtils.isEmpty(model.getTableNameCn())) {
            return model.getTableName();
        }else{
            return model.getTableNameCn();
        }
    }

    private String getTableName(TDbTable table){
        if(StringUtils.isEmpty(table.getTableNameCn())) {
            return table.getTableName();
        }else{
            return table.getTableNameCn();
        }
    }

    private String getDbName(TDbConfig db){
        if(StringUtils.isEmpty(db.getDbNameCn())) {
            return db.getDbName();
        }else{
            return db.getDbNameCn();
        }
    }
}
