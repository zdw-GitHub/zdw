package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.entity.vo.ResultFrom;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.service.ResourceRelationQueryService;
import com.mshz.cloud.tms.business.vo.DataMaintainParaVo;
import com.mshz.cloud.tms.business.vo.ResParaVo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.mshz.cloud.tms.business.util.PageUtils.startPage;

/**
 * 资源通用查询Controller（光缆/光路/业务相关）
 *
 * @author Jerry
 * @date 2023-7-18
 */
@RestController
@RequestMapping("/resCommon")
// 3. 去掉若依的BaseController继承（避免依赖）
public class ResourceRelationQueryController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceRelationQueryController.class);

    @Resource
    private ResourceRelationQueryService relationQueryService;  // 服务层暂时不改，先保证能注入


    /**
     * 根据光缆编号获取光缆关联的一次线路
     */
//   //@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectOneLineByFiberId")
    public AjaxResult selectOneLineByFiberId(@RequestBody ResParaVo para) {
        List<Map> list = relationQueryService.selectOneLineByFiberId(para);
        Set<String> lineNames = new HashSet<>();
        for (Map map : list) {
            String lineName = (String) map.get("lineName");
            if (StringUtils.hasText(lineName)){
                lineNames.add(lineName);
            }
        }
        Map map = new HashMap<>();
        map.put("list",list);
        map.put("lineNames",String.join("、",lineNames));
        return AjaxResult.success(map);
    }


    /**
     * 获取所有与光缆不关联的一次线路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectOneLineNotReFiberId")
    public ResultFrom<List<Map>> selectOneLineNotReFiberId(@RequestBody ResParaVo para) {
        startPage();
        List<Map> list = relationQueryService.selectOneLineNotReFiberId(para);
        return ResultFrom.success(list);
    }

    /**
     * 光缆承载光路导出
     */
   ////@RequiresPermissions("site:resCommon:export")
    @PostMapping("/opticalCableLoadBearingLightPathExport")
    public AjaxResult opticalCableLoadBearingLightPathExport(@RequestBody DataMaintainParaVo para) {
        String fileName = "";
        try {
            fileName = relationQueryService.opticalCableLoadBearingLightPathExport(para);
        } catch (Exception e) {
            logger.error("导出异常：", e);
            e.printStackTrace();
            return AjaxResult.error("服务器内部错误");
        }
        return AjaxResult.success(fileName);
    }

    /**
     * 光缆承载光路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingOpticalPathById")
    public AjaxResult selectCarryingOpticalPathById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingOpticalPathById(para);
        return AjaxResult.success(ids);
    }

    /**
     * 查询光路占用光缆纤芯
     */
    @PostMapping("/selectLineSn")
    public AjaxResult selectLineSn(@RequestBody ResParaVo para) {
        List<Map> list = relationQueryService.selectLineSn(para);
        return AjaxResult.success(list);
    }

    /**
     * 光缆承载业务
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingBusinessById")
    public AjaxResult selectCarryingBusinessById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingBusinessById(para);
        return AjaxResult.success(ids);
    }

    /**
     * 光缆承载通道链路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingChannelLinkById")
    public AjaxResult selectCarryingChannelLinkById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingChannelLinkById(para);
        return AjaxResult.success(ids);
    }

    /**
     * SDH承载通道链路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingChannelLinkBySDHId")
    public AjaxResult selectCarryingChannelLinkBySDHId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingChannelLinkBySDHId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 业务承载通道链路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingChannelLinkByBuzId")
    public AjaxResult selectCarryingChannelLinkByBuzId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingChannelLinkByBuzId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 光路承载通道链路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectCarryingChannelLinkByGlId")
    public AjaxResult selectCarryingChannelLinkByGlId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectCarryingChannelLinkByGlId(para);
        return AjaxResult.success(ids);
    }

    /**
     * SDH设备承载光路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectSDHCarryingOpticalPathById")
    public AjaxResult selectSDHCarryingOpticalPathById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectSDHCarryingOpticalPathById(para);
        return AjaxResult.success(ids);
    }

    /**
     * 业务设备承载光路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectBuzCarryingOpticalPathById")
    public AjaxResult selectBuzCarryingOpticalPathById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectBuzCarryingOpticalPathById(para);
        return AjaxResult.success(ids);
    }

    /**
     * SDH设备承载业务
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectSDHCarryingBusinessById")
    public AjaxResult selectSDHCarryingBusinessById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectSDHCarryingBusinessById(para);
        return AjaxResult.success(ids);
    }
    /**
     * 光路承载业务
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectGLCarryingBusinessById")
    public AjaxResult selectGLCarryingBusinessById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectGLCarryingBusinessById(para);
        return AjaxResult.success(ids);
    }
    /**
     * 板卡承载业务
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectBKCarryingBusinessById")
    public AjaxResult selectBKCarryingBusinessById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectBKCarryingBusinessById(para);
        return AjaxResult.success(ids);
    }
    /**
     * 板卡承载光路
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/selectBKCarryingOpticalPathById")
    public AjaxResult selectBKCarryingOpticalPathById(@RequestBody ResParaVo para) {
        String ids = relationQueryService.selectBKCarryingOpticalPathById(para);
        return AjaxResult.success(ids);
    }

    /**
     * 机柜关联站点
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/cabinetAssociatedSites")
    public AjaxResult cabinetAssociatedSites(@RequestBody ResParaVo para) {
        String ids = relationQueryService.cabinetAssociatedSites(para);
        return AjaxResult.success(ids);
    }

    @PostMapping("/exportOpticalPath")
    @ApiOperation(value = "导出光路")
   ////@RequiresPermissions("site:resCommon:export")
    public AjaxResult exportOpticalPath(@RequestBody DataMaintainParaVo para) {
        String fileName = "";
        try {
            fileName = relationQueryService.exportOpticalPath(para);
        } catch (Exception e) {
            logger.error("导出异常：", e);
            e.printStackTrace();
            return AjaxResult.error("服务器内部错误");
        }
        return AjaxResult.success(fileName);
    }

    /**
     * 承载光路导出
     */
    @PostMapping("/carryingOpticalPathExport")
    public AjaxResult carryingOpticalPathExport(@RequestBody DataMaintainParaVo para,String id,String resType,String exportType) {
        String fileName = "";
        try {
            fileName = relationQueryService.carryingOpticalPathExport(para,id,resType,exportType);
        } catch (Exception e) {
            logger.error("导出异常：", e);
            e.printStackTrace();
            return AjaxResult.error("服务器内部错误");
        }
        return AjaxResult.success(fileName);
    }

    /**
     * 承载业务导出
     */
    @PostMapping("/carryingBusinessExport")
    public AjaxResult carryingBusinessExport(@RequestBody DataMaintainParaVo para,String id,String resType,String exportType) {
        String fileName = "";
        try {
            fileName = relationQueryService.carryingBusinessExport(para,id,resType,exportType);
        } catch (Exception e) {
            logger.error("导出异常：", e);
            e.printStackTrace();
            return AjaxResult.error("服务器内部错误");
        }
        return AjaxResult.success(fileName);
    }

    /**
     * 根据筛选条件获取ID
     */
   ////@RequiresPermissions("site:resCommon:list")
    @PostMapping("/obtainIDBasedOnFilteringCriteria")
    public AjaxResult obtainIDBasedOnFilteringCriteria(@RequestBody DataMaintainParaVo para) {
        List<String> list = relationQueryService.obtainIDBasedOnFilteringCriteria(para);
        return AjaxResult.success(list);
    }

    /**
     * 站点关联SDH
     */
    @PostMapping("/getSdhBySiteId")
    public AjaxResult getSdhBySiteId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.getSdhBySiteId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 站点关联Otn
     */
    @PostMapping("/getOtnBySiteId")
    public AjaxResult getOtnBySiteId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.getOtnBySiteId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 站点关联光缆
     */
    @PostMapping("/getFiberBySiteId")
    public AjaxResult getFiberBySiteId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.getFiberBySiteId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 光路关联光缆
     */
    @PostMapping("/getFiberByGlId")
    public AjaxResult getFiberByGlId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.getFiberByGlId(para);
        return AjaxResult.success(ids);
    }

    /**
     * 业务关联光缆
     */
    @PostMapping("/getFiberByBuzId")
    public AjaxResult getFiberByBuzId(@RequestBody ResParaVo para) {
        String ids = relationQueryService.getFiberByBuzId(para);
        return AjaxResult.success(ids);
    }
    @GetMapping("/getRoomListBySiteId")
    public AjaxResult getRoomListBySiteId(String id) {
        Map res = relationQueryService.getRoomListBySiteId(id);
        return AjaxResult.success(res);
    }
    @GetMapping("/getNeByRackId")
    public AjaxResult getNeByRackId(String id) {
        List<Map> res = relationQueryService.getNeByRackId(id);
        return AjaxResult.success(res);
    }
}
