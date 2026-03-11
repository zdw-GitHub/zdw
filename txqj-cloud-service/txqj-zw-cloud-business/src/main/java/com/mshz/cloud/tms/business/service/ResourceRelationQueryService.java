package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.vo.DataMaintainParaVo;
import com.mshz.cloud.tms.business.vo.ResParaVo;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Creat By  Jerry on 2023/7/18.
 */
public interface ResourceRelationQueryService {

    /**
     * 根据光缆编号获取光缆关联的一次线路
     * @param para
     * @return
     */
    List<Map> selectOneLineByFiberId(ResParaVo para);

    /**
     * 获取所有与光缆不关联的一次线路
     * @param para
     * @return
     */
    List<Map> selectOneLineNotReFiberId(ResParaVo para);

    String selectCarryingOpticalPathById(ResParaVo para);

    String selectCarryingBusinessById(ResParaVo para);

    String selectCarryingChannelLinkById(ResParaVo para);
    String selectCarryingChannelLinkBySDHId(ResParaVo para);
    String selectCarryingChannelLinkByBuzId(ResParaVo para);

    String selectCarryingChannelLinkByGlId(ResParaVo para);

    String selectSDHCarryingBusinessById(ResParaVo para);

    String selectSDHCarryingOpticalPathById(ResParaVo para);
    String selectBuzCarryingOpticalPathById(ResParaVo para);

    String selectGLCarryingBusinessById(ResParaVo para);

    String selectBKCarryingBusinessById(ResParaVo para);

    String selectBKCarryingOpticalPathById(ResParaVo para);

    String opticalCableLoadBearingLightPathExport(DataMaintainParaVo para) throws IOException;

    String exportOpticalPath(DataMaintainParaVo para) throws IOException;

    List<String> obtainIDBasedOnFilteringCriteria(DataMaintainParaVo para);

    String cabinetAssociatedSites(ResParaVo para);

    String carryingOpticalPathExport(DataMaintainParaVo para, String id, String resType, String exportType) throws IOException;

    String carryingBusinessExport(DataMaintainParaVo para, String id, String resType, String exportType) throws IOException;

    List<Map> selectLineSn(ResParaVo para);

    String getSdhBySiteId(ResParaVo para);

    String getOtnBySiteId(ResParaVo para);

    String getFiberBySiteId(ResParaVo para);

    String getFiberByGlId(ResParaVo para);
    String getFiberByBuzId(ResParaVo para);

    Map getRoomListBySiteId(String id);

    List<Map> getNeByRackId(String id);
}
