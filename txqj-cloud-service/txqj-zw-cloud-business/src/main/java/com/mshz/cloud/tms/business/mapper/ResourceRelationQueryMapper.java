package com.mshz.cloud.tms.business.mapper;


import com.mshz.cloud.tms.business.vo.ResParaVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Creat By  cuiHy on 2022/11/3.
 */
@Mapper
public interface ResourceRelationQueryMapper {

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

    List<String> selectCarryingOpticalPathById(ResParaVo para);

    List<String> selectCarryingBusinessById(ResParaVo para);

    List<String> selectCarryingChannelLinkById(ResParaVo para);
    List<String> selectCarryingChannelLinkByGlId(ResParaVo para);
    List<String> selectCarryingChannelLinkBySDHId(ResParaVo para);
    List<String> selectCarryingChannelLinkByBuzId(ResParaVo para);

    List<String> selectSDHCarryingBusinessById(ResParaVo para);
    List<String> selectSDHCarryingOpticalPathById(ResParaVo para);
    List<String> selectBuzCarryingOpticalPathById(ResParaVo para);
    List<String> selectGLCarryingBusinessById(ResParaVo para);
    List<String> selectBKCarryingBusinessById(ResParaVo para);
    List<String> selectBKCarryingOpticalPathById(ResParaVo para);
    List<String> cabinetAssociatedSites(ResParaVo para);

    List<Map> selectCarryingOpticalPathByIds(String ids);

    List<Map> selectOpticalPathByIds(String ids);

    List<Map> selectAPortList(String ids);

    List<Map> selectZPortList(String ids);

    List<Map> selectMergeList(String ids);
    List<Map> carryingOpticalPathExport(List<String> ids);

    List<Map> carryingBusinessExport(List<String> ids);

    String selectFiberNameById(String id);
    String selectSDHNameById(String id);
    String selectOTNNameById(String id);
    String selectBuzNameById(String id);
    String selectCardNameById(String id);
    String selectGLNameById(String id);

    List<Map> selectLineSn(ResParaVo para);

    List<String> getSdhBySiteId(ResParaVo para);
    List<String> getOtnBySiteId(ResParaVo para);
    List<String> getFiberBySiteId(ResParaVo para);

    List<String> getFiberByGlId(ResParaVo para);
    List<String> getFiberByBuzId(ResParaVo para);

    Map selectSiteInfoById(String id);

    List<Map> selectRoomListBySiteId(String id);

    List<Map> getNeByRackId(String id);
}
