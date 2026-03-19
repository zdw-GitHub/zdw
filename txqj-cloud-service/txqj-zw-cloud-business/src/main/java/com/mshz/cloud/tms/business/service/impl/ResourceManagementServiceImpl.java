package com.mshz.cloud.tms.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mshz.cloud.tms.business.annotation.MetaDataDeptScope;
import com.mshz.cloud.tms.business.config.MetadataDBConfig;
import com.mshz.cloud.tms.business.config.PathConfig;
import com.mshz.cloud.tms.business.mapper.ResourceManagementMapper;
import com.mshz.cloud.tms.business.util.AtrrNameUtils;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.util.ExcelUtilTool;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.vo.DataMaintainAttributeVo;
import com.mshz.cloud.tms.business.vo.DataMaintainParaVo;
import com.mshz.cloud.tms.business.service.ResourceManagementService;
import com.mshz.cloud.tms.business.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 通信站基本信息Service业务层处理
 *
 * @author zdw
 * @date 2022-11-02
 */
@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

    @Resource
    private ResourceManagementMapper resourceManagementMapper;

    @Resource
    private MetadataDBConfig metadataDBConfig;

    @Resource
    private PathConfig pathConfig;


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public List<GuideVo> selectGuideVoListByModel(String modelId, String modelVersion) {
        return resourceManagementMapper.selectGuideVoListByModel(modelId, modelVersion);
    }

    @Override
    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo) {
        return resourceManagementMapper.selectMetaModelShowVoList(metaModelShowVo);
    }

    @Override
    public GridInitDataVo initGridData(MetaModelShowVo metadataShowVo) {

        GridInitDataVo data = new GridInitDataVo();
        List<MetaModelShowVo> attributes = resourceManagementMapper.selectMetaModelShowVoList(metadataShowVo);

        // 查询条件属性
        List<GridColumnVo> searchAttributes = new ArrayList<>();
        // 列表属性
        List<GridColumnVo> columns = new ArrayList<>();
        for (int i = 0; i < attributes.size(); i++) {
            MetaModelShowVo record = attributes.get(i);
            GridColumnVo vo = new GridColumnVo();
            vo.setId(record.getId());//
            String atrr = AtrrNameUtils.camel(record.getColumnName().toLowerCase());
            vo.setAttributeName(atrr);
            vo.setAttributeNameUnderline(record.getColumnName());
            vo.setAttributeNameCn(record.getColumnNameCn());
            vo.setModelId(record.getModelId());
            vo.setAttType(record.getColumnType());
            vo.setIsNotNull(record.getIsNotNull());
            vo.setSortable(record.getSortable());
            vo.setIsJumpLink(record.getIsJumpLink());
            vo.setMenuId(record.getMenuId());
            vo.setMenuPath(record.getMenuPath());
            vo.setLinkReMetaModelId(record.getLinkReMetaModelId());
            vo.setLinkReMetaModel(record.getLinkReMetaModel());
            vo.setLinkReMetaModelCn(record.getLinkReMetaModelCn());
            vo.setLinkReColumnId(record.getLinkReColumnId());
            vo.setLinkReColumnName(record.getLinkReColumnName());
            vo.setLinkReColumnNameCn(record.getLinkReColumnNameCn());
            vo.setSortNum(record.getSortNum());
            vo.setIsMergeRows(record.getIsMergeRows());
            vo.setRelateSearchCondition(record.getRelateSearchCondition());
            vo.setRelateSearchAttributeId(record.getRelateSearchAttributeId());
            vo.setRelateSearchAttribute(record.getRelateSearchAttribute());
            vo.setRelateSearchName(record.getRelateSearchName());
            vo.setIcon(record.getIcon());
            vo.setIsStyle(record.getIsStyle());
            vo.setStyleList(record.getStyleList());
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
    public List<Map> selectData(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageExportSql(packageTableName(modelName, metadataDBConfig.getDatabaseName()), showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        Map attrs = new LinkedHashMap();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            attrs.put(metaModelShowVo.getColumnName(), "");
        }
        List<Map> data = resourceManagementMapper.selectAll(vo);
        return data;
    }

    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public List<Map> QueryData(DataMaintainParaVo para) {
        // 从参数中获取当前用户的单位查询
        String companyQuerySql = "";
        if (para.getParams() != null && para.getParams().containsKey("metaDataDeptScope")) {
            companyQuerySql = String.valueOf(para.getParams().get("metaDataDeptScope"));
        }

        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        para.setTableName(modelName);

        //解决查询慢问题
        this.solvingTheProblemOfSlowQueries(para,modelName,companyQuerySql);

        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getDatabaseName()), companyQuerySql, showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> data = resourceManagementMapper.selectAll(vo);

        data = this.setData2StringByType(data);

//        data = this.processingDataList(data,modelName);

        return data;
    }

    private List<Map> processingDataList(List<Map> data, String modelName) {
        if("SG_TCDEV_FIBER_B".equals(modelName)){
            if (data.size() > 0 && data.get(0).get("ID") != null) {
                //查询光缆所属一次线路
                List<String> ids = data.stream().map(m -> m.get("ID").toString()).collect(Collectors.toList());
                List<Map> fiberParLine =  resourceManagementMapper.selectFiberParLineByIds(ids);
                //查询一次线路dw_tcdev_oneline
                List<Map> oneline =  resourceManagementMapper.selectOneline();
                for (Map datum : data) {
                    Map lineMap = fiberParLine.stream().filter(f -> Objects.equals(f.get("id"), datum.get("ID"))).findFirst().orElse(null);
                    if (lineMap == null) continue;
                    String parLine = (String) lineMap.get("parLine");
                    if (StringUtils.isEmpty(parLine)) continue;
                    List<String> parLineList = new ArrayList<>();
                    for (String s : parLine.split(",")) {
                        Map map = oneline.stream().filter(f -> Objects.equals(f.get("id"), s)).findFirst().orElse(null);
                        if (map != null){
                            parLineList.add(map.get("name").toString());
                        }
                    }
                    datum.put("PAR_LINE",String.join(",",parLineList));
                }
            }

        }
        return data;
    }

    private void solvingTheProblemOfSlowQueries(DataMaintainParaVo para, String modelName, String companyQuerySql) {
        if("SG_TCCON_TCBUZ_B".equals(modelName)){
            MetaModelShowVo metaModelShowVo = new MetaModelShowVo();
            metaModelShowVo.setColumnName("ID");
            metaModelShowVo.setColumnNameCn("ID");
            List<MetaModelShowVo> metaModelShowVos = new ArrayList<>();
            metaModelShowVos.add(metaModelShowVo);
            String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getUserName()), companyQuerySql, metaModelShowVos);
            CommonVo vo = new CommonVo();
            vo.setSql(sql);
            List<Map> data = resourceManagementMapper.selectAll(vo);
            String ids = "空";
            if (data.size() > 0){
                List<String> idList = data.stream().map(map -> map.get("ID").toString()).collect(Collectors.toList());
                ids = String.join(",",idList);
            }
            DataMaintainAttributeVo dataMaintainAttributeVo = new DataMaintainAttributeVo();
            dataMaintainAttributeVo.setAttributeName("ID");
            dataMaintainAttributeVo.setAttributeValue(ids);
            List<DataMaintainAttributeVo> dataMaintainAttributeVos = new ArrayList<>();
            dataMaintainAttributeVos.add(dataMaintainAttributeVo);
            para.setWhereAttributes(dataMaintainAttributeVos);
            para.setPageIndex(1);
        }
    }

    // 转义特殊类型数据
    public List<Map> setData2StringByType(List<Map> data) {
        try {
            for (Map<String, Object> map : data) {
                for (String key : map.keySet()) {
                    // 处理日期类型
                    // Date转String
                    Object obj = map.get(key);
                    if (obj instanceof Date) {
                        String dateStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, (Date)obj);
                        map.replace(key, dateStr);
                    }
                    // LocalDateTime转String
                    if (obj instanceof LocalDateTime) {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD);
                        LocalDateTime localDateTime = (LocalDateTime)obj;
                        String dateStr = localDateTime.format(fmt);
                        map.replace(key, dateStr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public Map<String, Object> packageOne(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageOne(packageTableName(modelName, metadataDBConfig.getUserName()), showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        Map attrs = new LinkedHashMap();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            attrs.put(metaModelShowVo.getColumnName(), "");
        }
        Map<String, String> data = resourceManagementMapper.selectOne(vo);
        return processingData(modelName,data);
    }

    private Map<String, Object> processingData(String modelName, Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {
            Object value = stringStringEntry.getValue();
            String key = stringStringEntry.getKey();
                if (Objects.nonNull(value)){
                    if("SG_TCDEV_FIBER_B".equals(modelName) && "PAR_LINE".equals(key)){
                    String valueString = value.toString();
                    if (StringUtils.hasText(valueString)){
                        List<String> list = Arrays.asList(valueString.split(","));
                        map.put(key,list);
                    }
                }else {
                        map.put(key, value.toString());
              }
            }
        }
        return map;
    }

    /***
     * 获取指定数据库的表单数据
     *
     * @param para
     * @return
     */
    @Override
    public Map<String, Object> packageOneByType(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        // 去除指定字段
        List<String> removeCol = new ArrayList<>();
        removeCol.add("DELETE_FLAG");
        Iterator<MetaModelShowVo> iterator = showAttributes.iterator();
        while (iterator.hasNext()){
            MetaModelShowVo obj = iterator.next();
            if (removeCol.contains(obj.getColumnName())) {
                iterator.remove();
            }
        }
        String sql = para.packageOne(packageTableName(modelName, para.getDbName()), showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        Map attrs = new LinkedHashMap();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            attrs.put(metaModelShowVo.getColumnName(), "");
        }
        Map<String, String> data = resourceManagementMapper.selectOne(vo);

        return processingData(modelName,data);
    }

    @Override
    public String exportExcel(DataMaintainParaVo para) throws IOException {
        String fileName = "";
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        String modelNameCn = resourceManagementMapper.selectTableNameCnByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageExportSql(packageTableName(modelName, metadataDBConfig.getUserName()), showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        Map attrs = new LinkedHashMap();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            attrs.put(metaModelShowVo.getColumnName(), "");
        }
        List<Map> data = resourceManagementMapper.selectAll(vo);
        List<Map> excelData = new ArrayList<>();
        for (Map map : data) {
            for (Object object : attrs.keySet()) {
                if (map.get(object) != null) {
                    attrs.put(object, map.get(object));
                }
            }
            excelData.add(attrs);
        }
        validationIsNull(data);
        // //拼装excel头部
        String[] headers = new String[showAttributes.size()];
        for (int i = 0; i < showAttributes.size(); i++) {
            MetaModelShowVo record = showAttributes.get(i);
            String attributeName = record.getColumnName();
            String attributeNamCn = record.getColumnNameCn();
            if (attributeNamCn.equals("")) {
                headers[i] = attributeName;
            } else {
                headers[i] = attributeNamCn;
            }

        }
        fileName = ExcelUtilTool.writer(pathConfig.getExcelPath(), excelData, headers,modelNameCn);
        return fileName;
    }

    @Override
    public String QueryexportExcel(DataMaintainParaVo para) throws IOException {
        String fileName = "";
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        para.setTableName(modelName);
        String modelNameCn = resourceManagementMapper.selectTableNameCnByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getUserName()), null,  showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        Map attrs = new LinkedHashMap();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            attrs.put(metaModelShowVo.getColumnName(), "");
        }
        List<Map> data = resourceManagementMapper.selectAll(vo);
        data = this.processingDataList(data,modelName);
        List<Map> excelData = new ArrayList<>();
        for (Map map : data) {
            for (Object object : attrs.keySet()) {
                if (map.get(object) != null) {
                    attrs.put(object, map.get(object));
                }else {
                    attrs.put(object,"");
                }
            }
            Map tmp = new LinkedHashMap();
            tmp.putAll(attrs);
            excelData.add(tmp);
        }
        validationIsNull(data);
        // //拼装excel头部
        String[] headers = new String[showAttributes.size()];
        for (int i = 0; i < showAttributes.size(); i++) {
            MetaModelShowVo record = showAttributes.get(i);
            String attributeName = record.getColumnName();
            String attributeNamCn = record.getColumnNameCn();
            if (attributeNamCn.equals("")) {
                headers[i] = attributeName;
            } else {
                headers[i] = attributeNamCn;
            }

        }
        fileName = ExcelUtilTool.writer(pathConfig.getExcelPath(), excelData, headers, modelNameCn);
        return fileName;
    }

    @Override
    public void importExcel(DataMaintainParaVo vo) {
        // List<Object> list = ExcelReadUtil.readExcelDataRow0(vo.getFilePath(),null);
        // String modelId = vo.getModelId();
        // MetaModel model = metaModelService.queryById(modelId);
        // MetaModelAttribute entity = new MetaModelAttribute();
        // entity.setModelId(Long.valueOf(modelId));
        // //找出展示字段
        // List<MetaModelAttribute> attributes = metaModelAttributeService.queryList(entity);
        // String primaryAttributeName = null;
        // //整理属性列 标记出主键
        // if(attributes!=null && attributes.size()>0){
        // attributesFor:for (int i = 0; i < attributes.size(); i++) {
        // MetaModelAttribute record = attributes.get(i);
        // String isPrimaryKey = record.getIsPrimaryKey();
        // if(isPrimaryKey.equals("1")){
        // primaryAttributeName = record.getAttributeName();
        // break attributesFor;
        // }//if isPrimaryKey
        // } //for attributes
        //
        // StringBuilder insertSB = new StringBuilder("insert into ");
        // insertSB.append(packageTableName(model.getModelName(),metardbUserName));
        // insertSB.append(" ( ");
        //
        // StringBuilder updateSB = new StringBuilder("update ");
        // updateSB.append(packageTableName(model.getModelName(),metardbUserName));
        // updateSB.append(" set ");
        //
        // StringBuilder selectSB = new StringBuilder("select * from ");
        // selectSB.append(packageTableName(model.getModelName(),metardbUserName));
        // //selectSB.append(" where ");
        //
        // StringBuilder whereSB = new StringBuilder();
        // whereSB.append(" where ");
        //
        // Map<Integer,String> attributeNameMap = new LinkedHashMap<>();
        //
        // boolean isHavePrimary = false; //
        // //与excel列表头部比对
        // if(list!=null){
        // if(list.size()>=1){
        // Map headerMap = (Map)list.get(0);
        // StringBuilder insertSBMid = new StringBuilder();
        // headerMapFor:for(Object k: headerMap.keySet()){
        // int key = (int)k;
        // String header = headerMap.get(key) + "";
        // attributesFor:for (int i = 0; i < attributes.size(); i++) {
        // MetaModelAttribute record = attributes.get(i);
        // String attributeName = record.getAttributeName();
        // String attributeNamCn = record.getAttributeNameCn();
        // String isPrimary = record.getIsPrimaryKey();
        // if(header.equals(attributeName)||header.equals(attributeNamCn)){
        // insertSBMid.append("`").append(attributeName).append("`").append(",");
        // attributeNameMap.put(key, attributeName);
        // if(isPrimary.equals("1")){
        // isHavePrimary = true;
        // whereSB.append(attributeName).append(" = ");
        // }
        // break attributesFor;
        // }//if
        // }//for attributesFor
        // //如果找不到，就不进行插入
        //
        // }//for headerMap
        // if(!isHavePrimary&&primaryAttributeName!=null)insertSBMid.append("`").append(primaryAttributeName).append("`").append(",");
        // String insertAttributeStr = insertSBMid.toString();
        // insertAttributeStr = insertAttributeStr.substring(0, insertAttributeStr.length()-1);
        // insertSB.append(insertAttributeStr).append(" ) ");
        // insertSB.append(" values ( ");
        // }//if(list.size()>1)
        //
        // //匹配数据插入
        // if(list.size()>=2){
        // for (int i = 1; i < list.size(); i++) {
        // Map valueMap = (Map)list.get(i);
        // StringBuilder insertSBMid = new StringBuilder();
        // StringBuilder updateSBMid = new StringBuilder();
        // String whereSql = whereSB.toString();
        // valueMapFor:for(Object k: valueMap.keySet()){
        // int key = (int)k;
        // String value = valueMap.get(key) + "";
        // if(value!=null&&value.equals("null"))value="";
        // String attributeName = attributeNameMap.get(key);
        // if(attributeName!=null){
        // if(isHavePrimary&&primaryAttributeName!=null&&primaryAttributeName.equals(attributeName)){
        // insertSBMid.append("'").append(value).append("'").append(",");
        // whereSql = whereSql + "'" + value + "'";
        // //whereSB.append("'").append(value).append("'");
        // }else{
        // insertSBMid.append("'").append(value).append("'").append(",");
        // updateSBMid.append(attributeName).append(" = ").append("'").append(value).append("'").append(",");
        // }
        // }//if
        // }//for valueMapFor
        // //因为excel如果有数据的字段后面为空，则无法获取数据 需要自己手动补全
        // if(valueMap.size()!=attributeNameMap.size()){
        // int k = attributeNameMap.size() - valueMap.size();
        // for (int j = 0; j < k; j++) {
        // insertSBMid.append("'").append("'").append(",");
        // }
        // }//
        // if(!isHavePrimary&&primaryAttributeName!=null)insertSBMid.append("'").append(IdUtil.getSnowflakeNextId()).append("'").append(",");
        // String insertValueStr = insertSBMid.toString();
        // insertValueStr = insertValueStr.substring(0, insertValueStr.length()-1);
        // String insertSql =insertSB.toString() + insertValueStr + " ) ";
        // //insertSB.append(insertValueStr).append(" ) ");
        //
        //
        // String upateSetStr = updateSBMid.toString();
        // upateSetStr = upateSetStr.substring(0, upateSetStr.length()-1);
        // String updateSql = updateSB.toString() + upateSetStr + whereSql;
        // //updateSB.append(upateSetStr).append(whereSB.toString());
        //
        // String selectSql = selectSB.toString() + whereSql;
        //
        // CommonVo comVO = new CommonVo();
        // //判断数据表中是否存在数据，存在更新，不存在添加
        // if(!isHavePrimary){
        // comVO.setSql(insertSql);
        // commonService.insertSql(comVO);
        // }else{
        // comVO.setSql(selectSql);
        // List midList = commonService.selectAll(comVO);
        // if(midList!=null&&midList.size()>0){
        // comVO.setSql(updateSql);
        // commonService.updateSql(comVO);
        // }else{
        // comVO.setSql(insertSql);
        // commonService.insertSql(comVO);
        // }
        // }
        // }//for list
        //
        // }//if(list.size()>2)
        // }//if list!=null
        //
        // }//if(attributes!=null && attributes.size()>0)
    }

    @Override
    public long queryCount(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        String sql = para.packageCountSql(packageTableName(modelName, metadataDBConfig.getUserName()), para.getIsPage());
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> list = resourceManagementMapper.selectAll(vo);
        long result = (long)list.get(0).get("COUNTNUM");
        return result;
    }

    @Override
    @MetaDataDeptScope
    public long advancedQueryCount(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);

        // 从参数中获取当前用户的单位查询
        String companyQuerySql = "";
        if (para.getParams() != null && para.getParams().containsKey("metaDataDeptScope")) {
            companyQuerySql = String.valueOf(para.getParams().get("metaDataDeptScope"));
        }

        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // entity.setIsVisible("1");
        // //找出展示字段
        List<MetaModelShowVo> showAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        String sql = para.packageQueryCountSql(packageTableName(modelName, metadataDBConfig.getUserName()), para.getIsPage(), showAttributes, companyQuerySql);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> list = resourceManagementMapper.selectAll(vo);
        long result = (long)list.get(0).get("COUNTNUM");
        return result;
    }


    @Override
    public void delete(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        String sql = para.packageDeleteSql(packageTableName(modelName, metadataDBConfig.getUserName()));
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        resourceManagementMapper.deleteSql(vo);
    }

    private void deleteVerification(String modelName, String id) {
        Long count;
        switch (modelName){
            case "SG_TCDEV_TCCARD_B":
                count = resourceManagementMapper.selectCount("`tms`.SG_TCDEV_TCPORT_B","PAR_CARD",id);
                if (count != 0) throw new RuntimeException("板卡下存在端口");
                break;
            case "SG_TCDEV_TCSLOT_B":
                count = resourceManagementMapper.selectCount("`tms`.SG_TCDEV_TCCARD_B","PAR_SLOT",id);
                if (count != 0) throw new RuntimeException("插槽下存在板卡");
                break;
            case "SG_TCDEV_TCSHELF_B":
                count = resourceManagementMapper.selectCount("`tms`.SG_TCDEV_TCSLOT_B","PAR_SHELF",id);
                if (count != 0) throw new RuntimeException("机框下存在插槽");
                break;
        }
    }

    @Override
    public void update(DataMaintainParaVo para) {
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        int i = resourceManagementMapper.doesTheColumnNameExist(modelName,"STAMP");
        if(i == 1){
            DataMaintainAttributeVo record = new DataMaintainAttributeVo();
            record.setAttributeName("STAMP");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.setAttributeValue(sdf.format(new Date()));
            para.getValueAttributes().add(record);
        }
        String sql = para.packageUpdateSql(packageTableName(modelName, metadataDBConfig.getUserName()));
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        resourceManagementMapper.updateSql(vo);
    }

    @Override
    public String create(DataMaintainParaVo para) {

        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        // 检测是否有主键 有主键设置主键值
        String id = null;
        List<DataMaintainAttributeVo> valueAttributes = para.getValueAttributes();
        for (int i = 0; i < valueAttributes.size(); i++) {
            DataMaintainAttributeVo record = valueAttributes.get(i);
            String isPrimaryKey = record.getIsPrimaryKey();
            if (isPrimaryKey != null && isPrimaryKey.equals("1")) {
                id = String.valueOf(UUID.randomUUID());
                record.setAttributeValue(id);
            }

        } // for valueAttributes

        // 检测是否有更新标志
        int i = resourceManagementMapper.doesTheColumnNameExist(modelName,"STAMP");
        if(i == 1){
            DataMaintainAttributeVo record = new DataMaintainAttributeVo();
            record.setAttributeName("STAMP");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.setAttributeValue(sdf.format(new Date()));
            para.getValueAttributes().add(record);
        }

        String sql = para.packageInsertSql(packageTableName(modelName, metadataDBConfig.getUserName()));
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        resourceManagementMapper.insertSql(vo);
        return id;
    }

    private String setOtherAttributes(List<DataMaintainAttributeVo> valueAttributes, String modelName,String maxIdRange) {
        String id = null;
        Long maxId = resourceManagementMapper.selectMaxId("`tms`."+modelName,maxIdRange);
        maxId = maxId + 1;
        String guid;
        String parentGuid;
        String sn = "";
        switch (modelName){
            case "SG_TCDEV_TCPORT_B":
                String parCard = "";
                for (DataMaintainAttributeVo valueAttribute : valueAttributes) {
                    String attributeName = valueAttribute.getAttributeName().toUpperCase();
                    if ("PAR_CARD".equals(attributeName)){
                        parCard = valueAttribute.getAttributeValue();
                    }
                    if ("SN".equals(attributeName)){
                        sn = valueAttribute.getAttributeValue();
                    }
                }
                if (StringUtils.hasText(parCard)){
                    parentGuid = resourceManagementMapper.selectPortGuid(parCard);
                    guid = parentGuid + ",PORT=SDH|" + sn;
                }else {
                    throw new RuntimeException("所属板卡不能为空");
                }
                break;
            case "SG_TCDEV_TCCARD_B":
                String parSlot = "";
                for (DataMaintainAttributeVo valueAttribute : valueAttributes) {
                    String attributeName = valueAttribute.getAttributeName().toUpperCase();
                    if ("PAR_SLOT".equals(attributeName)){
                        parSlot = valueAttribute.getAttributeValue();
                    }
                    if ("SN".equals(attributeName)){
                        sn = valueAttribute.getAttributeValue();
                    }
                }
                if (StringUtils.hasText(parSlot)){
                    parentGuid = resourceManagementMapper.selectCardGuid(parSlot);
                    guid = parentGuid + ",CARD=" + sn;
                }else {
                    throw new RuntimeException("所属插槽不能为空");
                }
                break;
            case "SG_TCDEV_TCSLOT_B":
                String parShelf = "";
                for (DataMaintainAttributeVo valueAttribute : valueAttributes) {
                    String attributeName = valueAttribute.getAttributeName().toUpperCase();
                    if ("PAR_SHELF".equals(attributeName)){
                        parShelf = valueAttribute.getAttributeValue();
                    }
                    if ("SN".equals(attributeName)){
                        sn = valueAttribute.getAttributeValue();
                    }
                }
                if (StringUtils.hasText(parShelf)){
                    parentGuid = resourceManagementMapper.selectSlotGuid(parShelf);
                    guid = parentGuid + ",SLOT=" + sn;
                }else {
                    throw new RuntimeException("所属机框不能为空");
                }
                break;
            case "SG_TCDEV_TCSHELF_B":
                String parNe = "";
                for (DataMaintainAttributeVo valueAttribute : valueAttributes) {
                    String attributeName = valueAttribute.getAttributeName().toUpperCase();
                    if ("PAR_NE".equals(attributeName)){
                        parNe = valueAttribute.getAttributeValue();
                    }
                    if ("SN".equals(attributeName)){
                        sn = valueAttribute.getAttributeValue();
                    }
                }
                if (StringUtils.hasText(parNe)){
                    parentGuid = resourceManagementMapper.selectShelfGuid(parNe);
                    guid = parentGuid + ",SHELF=" + sn;
                }else {
                    throw new RuntimeException("所属设备不能为空");
                }
                break;
            default:
                throw new RuntimeException(modelName + "表不能新增");
        }
        String createTime = DateUtils.getTime();
        valueAttributes.add(new DataMaintainAttributeVo("CREATE_TIME",createTime));
        valueAttributes.add(new DataMaintainAttributeVo("GUID",guid));
        valueAttributes.add(new DataMaintainAttributeVo("PARENT_GUID",parentGuid));
        valueAttributes.add(new DataMaintainAttributeVo("DELETE_FLAG","0"));
        valueAttributes.add(new DataMaintainAttributeVo("OWNER","330000"));
        valueAttributes.add(new DataMaintainAttributeVo("STAMP","3300000001_"+createTime));
        valueAttributes.add(new DataMaintainAttributeVo("IS_TY","1"));

        for (int i = 0; i < valueAttributes.size(); i++) {
            DataMaintainAttributeVo record = valueAttributes.get(i);
            String isPrimaryKey = record.getIsPrimaryKey();
            if (isPrimaryKey != null && isPrimaryKey.equals("1")) {
                record.setAttributeValue(maxId + "");
                id = record.getAttributeValue();
            }
        } // for valueAttributes
        return id;
    }

    private String packageTableName(String tableName, String dbName) {
        StringBuilder sb = new StringBuilder();
        if(!StringUtils.isEmpty(dbName)){
            sb.append(dbName).append(".");
        }
        sb.append(tableName.toLowerCase());
        return sb.toString();
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


    @Override
    public Map getNewTabInfo(String id, String tableName, String attribute) {
        Map res = new HashMap<>();
        String value = resourceManagementMapper.selectAttribute(id, tableName, attribute);
        if (StringUtils.hasText(value) && value.length() > 4){
            String prefixEncodingDefault = value.substring(0,4);
            Map map = resourceManagementMapper.selectTMetaModelIdRuleByPrefixEncodingDefault(prefixEncodingDefault);
            if (map != null){
                String modelNameCn = map.get("modelNameCn").toString();
                modelNameCn = modelNameCn.replaceAll("基本信息表","详情");
                modelNameCn = modelNameCn.replaceAll("基本信息","详情");
                res.put("modelName",map.get("modelName"));
                res.put("title", modelNameCn);
                res.put("id",value);
            }

        }
        return res;
    }

    @Override
    public Map<String, String> selectTableMessage(String modelId) {
        return resourceManagementMapper.selectTableMessage(modelId);
    }


    @Override
    public String downloadImportTemplate(ImportTemplateParaVo para) {
        String modelId = para.getModelId();
        // 1. 核心修改：查询模型【所有字段】（取消isVisible='1'限制，导出全字段）
//        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        String modelNameCn = resourceManagementMapper.selectTableNameCnByModelId(modelId);

        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        // 关键：删除entity.setIsVisible("1")，查询该模型的所有字段（无论是否展示）
        List<MetaModelShowVo> allAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);

        if (CollectionUtil.isEmpty(allAttributes)) {
            throw new RuntimeException("该模型未配置任何字段，无法生成导入模板");
        }

        // 2. 构建3个核心参数：表头数组、必填列索引、字典数据映射
        String[] headers = new String[allAttributes.size()]; // 表头（中文名称）
        int[] requiredIndexs = new int[allAttributes.size()]; // 必填字段列索引
        Map<Integer, String[]> dictDataMap = new LinkedHashMap<>(); // 字典映射：列索引→字典选项数组
        int requiredCount = 0;

        for (int i = 0; i < allAttributes.size(); i++) {
            MetaModelShowVo field = allAttributes.get(i);
            // 2.1 拼装表头（优先中文名称，无则用数据库列名）
            headers[i] = StrUtil.isNotBlank(field.getColumnNameCn()) ? field.getColumnNameCn() : field.getColumnName();

            // 2.2 收集必填字段索引（is_not_null='1'为必填）
            if ("1".equals(field.getIsNotNull())) {
                requiredIndexs[requiredCount++] = i;
            }
            // 2.3 解析字典数据（核心：从现有配置中获取字典选项，适配2种常见字典关联方式）
            String[] dictOptions = resolveDictOptions(field);
            if (dictOptions != null && dictOptions.length > 0) {
                dictDataMap.put(i, dictOptions);
            }
        }

        // 裁剪必填索引数组（去除空元素）
        requiredIndexs = Arrays.copyOf(requiredIndexs, requiredCount);

        // 3. 调用增强版工具类生成模板（含全字段、必填批注、字典下拉框）
        try {
            return ExcelUtilTool.generateImportTemplate(
                    pathConfig.getExcelPath(),
                    headers,
                    requiredIndexs,
                    dictDataMap,
                    modelNameCn + "-全字段导入模板"
            );
        } catch (Exception e) {
            logger.error("导入模板生成失败，原因：", e); // 打印完整异常栈（含行号，方便排查）
            // 可选：返回错误提示/抛出自定义业务异常
            // throw new BusinessException("模板生成失败：" + e.getMessage());
            return "模板生成失败：" + e.getMessage();
        }
    }

    /**
     * 解析字段字典选项（核心适配方法，基于现有元数据配置，支持2种常见字典关联方式）
     * @param field 模型字段配置
     * @return 字典选项数组（如：["0-禁用","1-启用"] 或 ["10kV","35kV","110kV"]）
     */
    private String[] resolveDictOptions(MetaModelShowVo field) {
        try {
            // 方式1：适配现有字典表关联（如sg_dic_*系列字典表，通过linkRe相关配置关联）
            if (StrUtil.isNotBlank(field.getRelMetaMode()) &&StrUtil.isNotBlank(field.getRelNameMetaModelColumn()) && StrUtil.isNotBlank(field.getRelIdMetaModelColumn())) {
                // 构建字典查询参数（复用现有查询逻辑，查询字典表的编码+名称）
                DataMaintainParaVo dictPara = new DataMaintainParaVo();
                dictPara.setModelId(field.getModelId().toString());
                dictPara.setIsPage(field.getIsPage());
                // 查询字典表所有有效数据
                List<Map> dictData = this.QueryData(dictPara);
                if (CollectionUtil.isNotEmpty(dictData)) {
                    // 拼接字典选项：编码-名称（如：1-高压，2-低压）
                    String[] options = dictData.stream()
                            .map(map -> {
                                String code = map.get(field.getLinkReColumnName()) == null ? "" : map.get(field.getLinkReColumnName()).toString();
                                String name = map.get(field.getLinkReColumnNameCn()) == null ? "" : map.get(field.getLinkReColumnNameCn()).toString();
                                return StrUtil.isNotBlank(name) ? code + "-" + name : code;
                            })
                            .filter(StrUtil::isNotBlank)
                            .toArray(String[]::new);
                    return options;
                }
            }

            // 方式2：适配自定义字典（如通过customizedSql配置的字典逻辑）
            if (StrUtil.isNotBlank(field.getCustomizedSql())) {
                CommonVo vo = new CommonVo();
                vo.setSql(field.getCustomizedSql());
                List<Map> dictData = resourceManagementMapper.selectAll(vo);
                if (CollectionUtil.isNotEmpty(dictData)) {
                    return dictData.stream()
                            .map(map -> map.values().iterator().next().toString())
                            .filter(StrUtil::isNotBlank)
                            .toArray(String[]::new);
                }
            }
        } catch (Exception e) {
            logger.warn("字段【{}】字典解析失败，跳过下拉框配置：", field.getColumnNameCn(), e);
        }
        // 无字典配置返回null
        return null;
    }

    @Override
    public ImportResultVo importDataFromExcel(MultipartFile file, String modelId) throws Exception {
        ImportResultVo result = new ImportResultVo();
        List<String> errorMsgList = new ArrayList<>();
        int totalCount = 0, successCount = 0, failCount = 0;

        // 1. 基础校验：文件非空、Excel格式
        if (file == null || file.isEmpty()) {
            errorMsgList.add("上传文件不能为空");
            result.setErrorMsgList(errorMsgList);
            return result;
        }
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls")) {
            errorMsgList.add("仅支持上传.xlsx/.xls格式的Excel文件");
            result.setErrorMsgList(errorMsgList);
            return result;
        }

        // 2. 查询模型全字段配置（与模板生成逻辑一致，保证字段匹配）
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        MetaModelShowVo entity = new MetaModelShowVo();
        entity.setModelId(Long.valueOf(modelId));
        List<MetaModelShowVo> allAttributes = resourceManagementMapper.selectMetaModelShowVoList(entity);
        MetaModelShowVo primaryKey = resourceManagementMapper.selectPrimaryKeyByModelId(modelId);
        if (CollectionUtil.isEmpty(allAttributes)) {
            errorMsgList.add("该模型未配置任何字段，无法导入");
            result.setErrorMsgList(errorMsgList);
            return result;
        }

        // 3. 解析Excel文件（表头=全字段中文名称，映射=数据库列名）
        String[] excelHeaders = allAttributes.stream()
                .map(vo -> StrUtil.isNotBlank(vo.getColumnNameCn()) ? vo.getColumnNameCn() : vo.getColumnName())
                .toArray(String[]::new);
        String[] dbColumnNames = allAttributes.stream()
                .map(MetaModelShowVo::getColumnName)
                .toArray(String[]::new);
        List<Map<String, Object>> excelDataList = ExcelUtilTool.readExcel(
                file.getInputStream(),
                excelHeaders,
                dbColumnNames
        );
        totalCount = excelDataList.size();
        if (CollectionUtil.isEmpty(excelDataList)) {
            errorMsgList.add("Excel文件中无有效数据");
            result.setErrorMsgList(errorMsgList);
            return result;
        }

        // 4. 批量处理数据：动态校验+动态入库（复用现有create方法的动态SQL）
        for (int i = 0; i < excelDataList.size(); i++) {
            int rowNum = i + 2;
            Map<String, Object> rowData = excelDataList.get(i);
            try {
                // 4.1 动态校验：必填字段非空校验（基于模型配置，通用化）
                for (MetaModelShowVo field : allAttributes) {
                    if ("1".equals(field.getIsNotNull())) {
                        Object fieldValue = rowData.get(field.getColumnName());
                        if (fieldValue == null || StrUtil.isBlank(fieldValue.toString().trim())) {
                            throw new RuntimeException(field.getColumnNameCn() + "（必填）不能为空");
                        }
                    }
                }

                // 4.2 动态入库：复用现有create方法，拼接动态插入SQL（无需硬编码）
                this.dynamicInsertData(modelId, rowData, allAttributes);
                successCount++;
            } catch (Exception e) {
                logger.error("导入第{}行数据异常：", rowNum, e);
                errorMsgList.add("第" + rowNum + "行：" + e.getMessage());
                failCount++;
            }
        }

        // 5. 封装结果
        result.setTotalCount(totalCount);
        result.setSuccessCount(successCount);
        result.setFailCount(failCount);
        result.setErrorMsgList(errorMsgList);
        return result;
    }

    /**
     * 动态插入数据（通用化，适配所有模型，复用现有create方法的动态SQL拼接逻辑）
     * @param modelId 模型ID
     * @param rowData Excel解析的行数据（key=数据库列名，value=值）
     * @param allAttributes 模型全字段配置
     */
    private void dynamicInsertData(String modelId, Map<String, Object> rowData, List<MetaModelShowVo> allAttributes) {
        // 构建create方法所需的DataMaintainParaVo参数
        DataMaintainParaVo para = new DataMaintainParaVo();
        para.setModelId(modelId);
        List<DataMaintainAttributeVo> valueAttributes = new ArrayList<>();

        // 遍历模型字段，拼接插入属性
        for (MetaModelShowVo field : allAttributes) {
            String dbColName = field.getColumnName();
            Object fieldValue = rowData.get(dbColName);
            // 空值处理：转为空字符串，避免SQL异常
            String value = fieldValue == null ? "" : fieldValue.toString().trim();

            DataMaintainAttributeVo attrVo = new DataMaintainAttributeVo();
            attrVo.setAttributeName(dbColName);
            attrVo.setAttributeValue(value);
            attrVo.setIsPrimaryKey(field.getIsPrimaryKey()); // 标记主键
            valueAttributes.add(attrVo);
        }

        // 复用现有create方法，自动拼接动态SQL并入库（含主键生成、STAMP字段自动填充等原有逻辑）
        para.setValueAttributes(valueAttributes);
        this.create(para);
    }



}
