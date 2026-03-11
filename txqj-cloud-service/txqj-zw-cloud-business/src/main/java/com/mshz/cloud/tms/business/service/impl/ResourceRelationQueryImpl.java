package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.annotation.MetaDataDeptScope;
import com.mshz.cloud.tms.business.config.MetadataDBConfig;
import com.mshz.cloud.tms.business.config.PathConfig;
import com.mshz.cloud.tms.business.mapper.ResourceManagementMapper;
import com.mshz.cloud.tms.business.util.ExcelUtilTool;
import com.mshz.cloud.tms.business.mapper.ResourceRelationQueryMapper;
import com.mshz.cloud.tms.business.service.ResourceRelationQueryService;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.vo.DataMaintainParaVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.vo.ResParaVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Creat By  Jerry on 2023/7/18.
 */
@Service
public class ResourceRelationQueryImpl implements ResourceRelationQueryService {

    @Resource
    private ResourceRelationQueryMapper mapper;


    @Resource
    private ResourceManagementMapper resourceManagementMapper;


    @Resource
    private MetadataDBConfig metadataDBConfig;


    @Resource
    private PathConfig pathConfig;
    /**
     * 根据光缆编号获取光缆关联的一次线路
     * @param para
     * @return
     */
    @Override
    public List<Map> selectOneLineByFiberId(ResParaVo para){
        return  mapper.selectOneLineByFiberId(para);
    }

    /**
     * 获取所有与光缆不关联的一次线路
     * @param para
     * @return
     */
    @Override
    public List<Map> selectOneLineNotReFiberId(ResParaVo para){
        return  mapper.selectOneLineNotReFiberId(para);
    }


    @Override
    public List<Map> selectLineSn(ResParaVo para) {
        return mapper.selectLineSn(para);
    }

    @Override
    public String selectCarryingOpticalPathById(ResParaVo para) {
        List<String> ids = mapper.selectCarryingOpticalPathById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectCarryingBusinessById(ResParaVo para) {
        List<String> ids = mapper.selectCarryingBusinessById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectCarryingChannelLinkById(ResParaVo para) {
        List<String> ids = mapper.selectCarryingChannelLinkById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }
    @Override
    public String selectCarryingChannelLinkBySDHId(ResParaVo para) {
        List<String> ids = mapper.selectCarryingChannelLinkBySDHId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }
    @Override
    public String selectCarryingChannelLinkByBuzId(ResParaVo para) {
        List<String> ids = mapper.selectCarryingChannelLinkByBuzId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }
    @Override
    public String selectCarryingChannelLinkByGlId(ResParaVo para) {
        List<String> ids = mapper.selectCarryingChannelLinkByGlId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectSDHCarryingBusinessById(ResParaVo para) {
        List<String> ids =   mapper.selectSDHCarryingBusinessById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectSDHCarryingOpticalPathById(ResParaVo para) {
        List<String> ids =  mapper.selectSDHCarryingOpticalPathById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }
    @Override
    public String selectBuzCarryingOpticalPathById(ResParaVo para) {
        List<String> ids =  mapper.selectBuzCarryingOpticalPathById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectGLCarryingBusinessById(ResParaVo para) {
        List<String> ids = mapper.selectGLCarryingBusinessById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectBKCarryingBusinessById(ResParaVo para) {
        List<String> ids = mapper.selectBKCarryingBusinessById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String selectBKCarryingOpticalPathById(ResParaVo para) {
        List<String> ids = mapper.selectBKCarryingOpticalPathById(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String cabinetAssociatedSites(ResParaVo para) {
        List<String> ids = mapper.cabinetAssociatedSites(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public String opticalCableLoadBearingLightPathExport(DataMaintainParaVo para) throws IOException {

        // 从参数中获取当前用户的单位查询
        String companyQuerySql = "";
        if (para.getParams() != null && para.getParams().containsKey("metaDataDeptScope")) {
            companyQuerySql = String.valueOf(para.getParams().get("metaDataDeptScope"));
        }
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        List<MetaModelShowVo> showAttributes = new ArrayList<>();
        MetaModelShowVo metaModelShowVo = new MetaModelShowVo();
        metaModelShowVo.setColumnName("ID");
        showAttributes.add(metaModelShowVo);
        String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getUserName()), companyQuerySql,  showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> data = resourceManagementMapper.selectAll(vo);

        List<Map> maps = new ArrayList<>();
        if(data!=null&&data.size()>0){
            List<String> inParams = data.stream()
                    .map(map -> "'" + map.get("ID") + "'") // 将每个Map对象的"value"提取为一个String集合
                    .collect(Collectors.toList()); // 将结果收集为一个List<String>对象
            String ids = String.join(",", inParams);
            maps = mapper.selectCarryingOpticalPathByIds(ids);

            for (int i = 0; i < maps.size(); i++) {
                Map map = maps.get(i);
                map.put("sn",i+1);
            }
        }
        Map attrs = new LinkedHashMap();
        attrs.put("sn","序号");
        attrs.put("fiberName","光缆名称");
        attrs.put("lineName","所属线路");
        attrs.put("totalCore","总芯数");
        attrs.put("remainingCore","剩余可用芯数");
        attrs.put("stateGrid","承载的国网光路");
        attrs.put("eastChina","承载的华东光路");
        attrs.put("provincialNetwork","承载的省网光路");
        attrs.put("landCity","承载的地市光路");
        attrs.put("dedicated","专用纤芯保护及其他");
        attrs.put("fiberLength","长度(km)");
        attrs.put("des","备注");
        attrs.put("lineVoltageClass","电压等级");
        //拼装excel头部
        String[] headers = new String[13];
        int num = 0;
        for (Object object : attrs.keySet()) {
            String o = String.valueOf(attrs.get(object));
            headers[num++] = o;
        }
        List<Map> excelData = new ArrayList<>();
        for (Map map : maps) {
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
        validationIsNull(excelData);
        return ExcelUtilTool.opticalCableLoadBearingLightPathExport(pathConfig.getExcelPath(),excelData, headers);

    }


    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public String exportOpticalPath(DataMaintainParaVo para) throws IOException {
        List<Map> data = getDataByDataMaintainParaVo(para);

        List<Map> maps = new ArrayList<>();
        if(data!=null&&data.size()>0){
            List<String> inParams = data.stream()
                    .map(map -> "'" + map.get("ID") + "'") // 将每个Map对象的"value"提取为一个String集合
                    .collect(Collectors.toList()); // 将结果收集为一个List<String>对象
            String ids = String.join(",", inParams);
            maps = mapper.selectOpticalPathByIds(ids);
            List<Map> aPortList = mapper.selectAPortList(ids);
            List<Map> zPortList = mapper.selectZPortList(ids);
            List<Map> mergeList = mapper.selectMergeList(ids);
            for (int i = 0; i < maps.size(); i++) {
                Map map = maps.get(i);
                String id = (String) map.get("ID");
                Map aPort = aPortList.stream().filter(a -> id.equals(a.get("ID"))).findFirst().orElse(null);
                if(aPort!=null){
                    map.put("aPort",aPort.get("aPort"));
                }
                Map zPort = zPortList.stream().filter(a -> id.equals(a.get("ID"))).findFirst().orElse(null);
                if(zPort!=null){
                    map.put("zPort",zPort.get("zPort"));
                }
            }
            for (Map merge : mergeList) {
                String mergeId = (String) merge.get("merge");
                String[] split = mergeId.split(",");
                Map map1 = maps.stream().filter(a -> split[0].equals(a.get("ID"))).findFirst().orElse(null);
                Map map2 = maps.stream().filter(a -> split[1].equals(a.get("ID"))).findFirst().orElse(null);
                map1.put("aPort",map1.get("aPort")+" / "+map2.get("aPort"));
                map1.put("zPort",map1.get("zPort")+" / "+map2.get("zPort"));
                maps.remove(map2);
            }
            for (int i = 0; i < maps.size(); i++) {
                Map map = maps.get(i);
                map.put("sn", i + 1);
            }
        }
        Map attrs = new LinkedHashMap();
        attrs.put("sn","序号");
        attrs.put("name","光路名称");
        attrs.put("rate","速率");
        attrs.put("tcplane","传输系统");
        attrs.put("aResobj","起始站点");
        attrs.put("aNe","起始设备");
        attrs.put("aPort","起始端口");
        attrs.put("zResobj","终始站点");
        attrs.put("zNe","终始设备");
        attrs.put("zPort","终始端口");
        attrs.put("opticalRouting","光路路由");
        //拼装excel头部
        String[] headers = new String[11];
        int num = 0;
        for (Object object : attrs.keySet()) {
            String o = String.valueOf(attrs.get(object));
            headers[num++] = o;
        }
        List<Map> excelData = new ArrayList<>();
        for (Map map : maps) {
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
        validationIsNull(excelData);
        return ExcelUtilTool.exportOpticalPath(pathConfig.getExcelPath(),excelData, headers);
    }

    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public String carryingBusinessExport(DataMaintainParaVo para, String id, String resType, String exportType) throws IOException {

        ResParaVo resParaVo = new ResParaVo();
        resParaVo.setResId(id);

        String name = "";
        List<String> ids = new ArrayList<>();
        if("光缆".equals(resType)){
            name = mapper.selectFiberNameById(id);
            ids = mapper.selectCarryingBusinessById(resParaVo);
        }else if ("SDH".equals(resType)){
            name = mapper.selectSDHNameById(id);
            ids = mapper.selectSDHCarryingBusinessById(resParaVo);
        }else if ("OTN".equals(resType)){
            name = mapper.selectOTNNameById(id);
            ids = mapper.selectSDHCarryingBusinessById(resParaVo);
        }else if ("板卡".equals(resType)){
            name = mapper.selectCardNameById(id);
            ids = mapper.selectBKCarryingBusinessById(resParaVo);
        }else if ("光路".equals(resType)){
            name = mapper.selectGLNameById(id);
            ids = mapper.selectGLCarryingBusinessById(resParaVo);
        }
        name = name.replaceAll("[\\\"<>/\\\\:*?|]", "_");

        if("条件".equals(exportType)){
            List<Map> data = getDataByDataMaintainParaVo(para);
            ids = data.stream().map(m->m.get("ID").toString()).collect(Collectors.toList());
        }

        List<Map> maps = new ArrayList<>();
        if(ids.size() != 0){
            maps = mapper.carryingBusinessExport(ids);
            for (int i = 0; i < maps.size(); i++) {
                maps.get(i).put("sn",i+1);
            }
        }

        Map attrs = new LinkedHashMap();
        attrs.put("sn","序号");
        attrs.put("name","业务名称");
        attrs.put("channelRoute","业务路由");
        attrs.put("buzType","业务类型");
        attrs.put("dspLevel","调度等级");
        //拼装excel头部
        String[] headers = new String[11];
        int num = 0;
        for (Object object : attrs.keySet()) {
            String o = String.valueOf(attrs.get(object));
            headers[num++] = o;
        }
        List<Map> excelData = new ArrayList<>();
        for (Map map : maps) {
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
        validationIsNull(excelData);

        return ExcelUtilTool.writer(pathConfig.getExcelPath(),excelData, headers,name+" 承载业务");
    }
    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public String carryingOpticalPathExport(DataMaintainParaVo para, String id, String resType, String exportType) throws IOException {
        ResParaVo resParaVo = new ResParaVo();
        resParaVo.setResId(id);

        String name = "";
        List<String> ids = new ArrayList<>();
        if("光缆".equals(resType)){
            name = mapper.selectFiberNameById(id);
            ids = mapper.selectCarryingOpticalPathById(resParaVo);
        }else if ("SDH".equals(resType)){
            name = mapper.selectSDHNameById(id);
            ids = mapper.selectSDHCarryingOpticalPathById(resParaVo);
        }else if ("OTN".equals(resType)){
            name = mapper.selectOTNNameById(id);
            ids = mapper.selectSDHCarryingOpticalPathById(resParaVo);
        }else if ("板卡".equals(resType)){
            name = mapper.selectCardNameById(id);
            ids = mapper.selectBKCarryingOpticalPathById(resParaVo);
        }else if ("业务".equals(resType)){
            name = mapper.selectBuzNameById(id);
            ids = mapper.selectBuzCarryingOpticalPathById(resParaVo);
        }
        name = name.replaceAll("[\\\"<>/\\\\:*?|]", "_");


        if("条件".equals(exportType)){
            List<Map> data = getDataByDataMaintainParaVo(para);
            ids = data.stream().map(m->m.get("ID").toString()).collect(Collectors.toList());
        }


        List<Map> maps = new ArrayList<>();
        if(ids.size() != 0){
            maps = mapper.carryingOpticalPathExport(ids);
            for (int i = 0; i < maps.size(); i++) {
                maps.get(i).put("sn",i+1);
            }
        }

        Map attrs = new LinkedHashMap();
        attrs.put("sn","序号");
        attrs.put("name","光路名称");
        attrs.put("aResId","A端设备");
        attrs.put("aPort","A端端口");
        attrs.put("zResId","Z端设备");
        attrs.put("zPort","Z端端口");
        attrs.put("dspLevel","调度等级");
        //拼装excel头部
        String[] headers = new String[11];
        int num = 0;
        for (Object object : attrs.keySet()) {
            String o = String.valueOf(attrs.get(object));
            headers[num++] = o;
        }
        List<Map> excelData = new ArrayList<>();
        for (Map map : maps) {
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
        validationIsNull(excelData);

        return ExcelUtilTool.writer(pathConfig.getExcelPath(),excelData, headers,name+" 承载光路");
    }

    private List<Map> getDataByDataMaintainParaVo(DataMaintainParaVo para) {
        // 从参数中获取当前用户的单位查询
        String companyQuerySql = "";
        if (para.getParams() != null && para.getParams().containsKey("metaDataDeptScope")) {
            companyQuerySql = String.valueOf(para.getParams().get("metaDataDeptScope"));
        }
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        List<MetaModelShowVo> showAttributes = new ArrayList<>();
        MetaModelShowVo metaModelShowVo = new MetaModelShowVo();
        metaModelShowVo.setColumnName("ID");
        showAttributes.add(metaModelShowVo);
        String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getUserName()), companyQuerySql,  showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        return resourceManagementMapper.selectAll(vo);
    }


    @Override
    @MetaDataDeptScope(tableAlias = "t")
    public List<String> obtainIDBasedOnFilteringCriteria(DataMaintainParaVo para) {
        // 从参数中获取当前用户的单位查询
        String companyQuerySql = "";
        if (para.getParams() != null && para.getParams().containsKey("metaDataDeptScope")) {
            companyQuerySql = String.valueOf(para.getParams().get("metaDataDeptScope"));
        }
        String modelId = para.getModelId();
        String modelName = resourceManagementMapper.selectTableNameByModelId(modelId);
        List<MetaModelShowVo> showAttributes = new ArrayList<>();
        MetaModelShowVo metaModelShowVo = new MetaModelShowVo();
        metaModelShowVo.setColumnName("ID");
        showAttributes.add(metaModelShowVo);
        String sql = para.packageQuerySql(packageTableName(modelName, metadataDBConfig.getUserName()), companyQuerySql,  showAttributes);
        CommonVo vo = new CommonVo();
        vo.setSql(sql);
        List<Map> data = resourceManagementMapper.selectAll(vo);
        List<String> inParams = data.stream()
                .map(map -> (String)map.get("ID")) // 将每个Map对象的"value"提取为一个String集合
                .collect(Collectors.toList()); // 将结果收集为一个List<String>对象
        return inParams;
    }

    private String packageTableName(String tableName, String dbUserName) {
        StringBuilder sb = new StringBuilder();
        sb.append(dbUserName).append(".");
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
    public String getSdhBySiteId(ResParaVo para) {
        List<String> ids = mapper.getSdhBySiteId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String getOtnBySiteId(ResParaVo para) {
        List<String> ids = mapper.getOtnBySiteId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String getFiberBySiteId(ResParaVo para) {
        List<String> ids = mapper.getFiberBySiteId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String getFiberByGlId(ResParaVo para) {
        List<String> ids = mapper.getFiberByGlId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }

    @Override
    public String getFiberByBuzId(ResParaVo para) {
        List<String> ids = mapper.getFiberByBuzId(para);
        if(ids.size() != 0){
            return String.join(",", ids);
        }else {
            return "空";
        }
    }


    @Override
    public Map getRoomListBySiteId(String id) {
        Map site = mapper.selectSiteInfoById(id);
        List<Map> roomList = mapper.selectRoomListBySiteId(id);
        Map res = new HashMap<>();
        res.put("site",site);
        res.put("roomList",roomList);
        return res;
    }

    @Override
    public List<Map> getNeByRackId(String id) {
        return mapper.getNeByRackId(id);
    }
}
