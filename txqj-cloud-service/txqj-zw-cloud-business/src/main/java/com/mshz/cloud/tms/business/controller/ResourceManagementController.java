package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.service.OptionsService;
import com.mshz.cloud.tms.business.service.ResourceManagementService;
import com.mshz.cloud.tms.business.vo.*;
import com.mshz.cloud.tms.common.modules.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通信站基本信息Controller
 *
 * @author zdw
 * @date 2022-11-02
 */
@RestController
@RequestMapping("/resourceManage")
public class ResourceManagementController extends BaseController {
    @Autowired
    private ResourceManagementService managementService;

    @Autowired
    private OptionsService optionsService;

    /**
     * 查询通信站基本信息列表
     */

   //@RequiresPermissions("system:b:list")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody DataMaintainParaVo para) {
        startPage();
        List<Map> data = null;
        long countNum = 0;
        try {
            List<DataMaintainAttributeVo> whereAttributes = para.getWhereAttributes();//解决查询慢问题会修改查询条件，下面查询总条数需要使用查询条件
            data = managementService.QueryData(para);
            para.setWhereAttributes(whereAttributes);
            countNum = managementService.advancedQueryCount(para);
        } catch (Exception e) {
            throw new RuntimeException("查询失败，请联系管理员！");
        }
        TableDataInfo tableDataInfo = getDataTable(data);
        tableDataInfo.setTotal(countNum);
        return tableDataInfo;
    }

    /**
     * 初始化展示列表 metadataShowVo 传modeId 和 版本modelVersion
     */
   //@RequiresPermissions("system:b:list")
    @PostMapping("/initgriddatasite")
    public AjaxResult initgriddatasite(@RequestBody MetaModelShowVo metaModelShowVo) {
        GridInitDataVo data = null;
        try {
            data = managementService.initGridData(metaModelShowVo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询异常：", e);
            return AjaxResult.error("查询异常");
        }
        return AjaxResult.success(data);
    }

    /**
     * 导出通信站基本信息列表
     */
   //@RequiresPermissions("system:b:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult exportExcel(@RequestBody DataMaintainParaVo para) {
        String fileName = "";
        try {
            fileName = managementService.QueryexportExcel(para);
        } catch (Exception e) {
            logger.error("导出异常：", e);
            e.printStackTrace();
            return AjaxResult.error("服务器内部错误");
        }
        return AjaxResult.success(fileName);
    }

    /**
     * 获取通信站基本信息详细信息
     */
   //@RequiresPermissions("system:b:list")
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody DataMaintainParaVo para) {

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isNotEmpty(para.getType())) {
            map = managementService.packageOneByType(para);
        }
        if (map == null || map.size() == 0) {
            map = managementService.packageOne(para);
        }
        return AjaxResult.success(map);
    }

    /**
     * 新增通信站基本信息
     */
   //@RequiresPermissions("system:b:add")
    @PostMapping("add")
    public AjaxResult add(@RequestBody DataMaintainParaVo para) {
        String id = null;
        try {
            id = managementService.create(para);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增异常：", e);
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(id);
    }

    /**
     * 批量新增通信站基本信息
     */
   //@RequiresPermissions("system:b:add")
    @PostMapping("batchAdd")
    public AjaxResult batchAdd(@RequestBody BatchDataMaintainParaVo paras) {
        try {
            for (DataMaintainParaVo para:paras.getParas() ) {
                managementService.create(para);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增异常：", e);
            return AjaxResult.error("新增异常");
        }
        return AjaxResult.success();
    }
    /**
     * 修改通信站基本信息
     */

    @PostMapping("update")
   //@RequiresPermissions("system:b:update")
    public AjaxResult update(@RequestBody DataMaintainParaVo para) {
        try {
            managementService.update(para);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改异常：", e);
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();

    }

    /**
     * 删除通信站基本信息
     */
   //@RequiresPermissions("system:b:remove")
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody DataMaintainParaVo para) {
        try {
            managementService.delete(para);
        } catch (Exception e) {
            logger.error("删除异常：", e);
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    @GetMapping("/addGuide")
   //@RequiresPermissions("system:b:list")
    public AjaxResult addGuide(String modelId, String modelVersion) {

        List<GuideVo> list = managementService.selectGuideVoListByModel(modelId, modelVersion);

        Map<String, List<GuideVo>> map = new HashMap<>();
        List<GuideVo> requiredList = new ArrayList<>();
        List<GuideVo> notRequiredList = new ArrayList<>();
        for (GuideVo vo : list) {
            if (vo.getIsNotNull() == 1) {
                requiredList.add(vo);
            }
            if (vo.getIsNotNull() == 0) {
                notRequiredList.add(vo);
            }
        }
        map.put("requiredList", requiredList);
        map.put("notRequiredList", notRequiredList);

        return AjaxResult.success(map);
    }

    @GetMapping("/gridOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getGridOptions(String name) {
        List<OptionsVo> list = optionsService.getGridOptions(name);
        return AjaxResult.success(list);
    }

    @GetMapping("/devTypeOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult devTypeOptions(String name) {
        List<OptionsVo> list = optionsService.devTypeOptions(name);
        return AjaxResult.success(list);
    }

    @GetMapping("/companyOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult companyOptions(String name) {
        List<OptionsVo> list = optionsService.companyOptions(name);
        return AjaxResult.success(list);
    }

    @GetMapping("/getModelId")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getModelId(String tableName) {
        return AjaxResult.success(optionsService.getModelId(tableName));
    }

    @GetMapping("/getVoltageOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getVoltageOptions(String name) {
        return AjaxResult.success(optionsService.getVoltageOptions(name));
    }

    @GetMapping("/getColunmId")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getColunmId(BigDecimal modelId, String colunmName) {
        BigDecimal n = optionsService.getColunmId(modelId, colunmName);
        return AjaxResult.success(n.toString());
    }

    @GetMapping("/getSiteStatusOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getSiteStatusOptions(String name) {
        return AjaxResult.success(optionsService.getSiteStatusOptions(name));
    }

    @GetMapping("/getSiteLevelOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getSiteLevelOptions(String name) {
        return AjaxResult.success(optionsService.getSiteLevelOptions(name));
    }

    @GetMapping("/getSiteTypeOptions")
   //@RequiresPermissions("system:b:list")
    public AjaxResult getSiteTypeOptions(String name) {
        return AjaxResult.success(optionsService.getSiteTypeOptions(name));
    }


    @GetMapping("/getNewTabInfo")
    public AjaxResult getNewTabInfo(String id,String tableName, String attribute) {
        return AjaxResult.success(managementService.getNewTabInfo(id,tableName,attribute));
    }

//    @RequiresPermissions("system:b:list")
    @PostMapping("/getPrimaryKeyName")
    public AjaxResult getPrimaryKeyName(String modeId) {
        Map<String, String> map = managementService.selectTableMessage(modeId);
        return AjaxResult.success(map.get("cloumnName"));
    }

    /**
     * 下载通信站导入模板（自动生成全字段+必填批注+字典下拉框）
     */
    @PostMapping("/import/template")
    public AjaxResult downloadImportTemplate(@RequestBody DataMaintainParaVo para) {
        // 1. 基础参数校验
        if (StringUtils.isEmpty(para.getModelId())) {
            throw new RuntimeException("模板生成失败：元模型ID不能为空");
        }
            // 2. 构建Service层所需参数VO
            ImportTemplateParaVo paraVo = new ImportTemplateParaVo();
            paraVo.setModelId(para.getModelId());
            // 3. 调用Service层已实现的方法，生成模板并返回文件名
            String templateFileName = "";
            try {
                templateFileName = managementService.downloadImportTemplate(paraVo);
            } catch (Exception e) {
                logger.error("导出异常：", e);
                e.printStackTrace();
                return AjaxResult.error("服务器内部错误");
            }
            return AjaxResult.success(templateFileName);
        }

    /**
     * 导入通信站数据（复用Service层importDataFromExcel方法，自动解析+校验+批量入库）
     * @param file Excel文件（multipart/form-data，必填）
     * @param modelId 元模型ID（必填，匹配模板模型）
     * @return 导入结果（总条数/成功/失败/失败详情）
     */
    @PostMapping("/import/submit")
    public AjaxResult importDataByExcel(@RequestParam("file") MultipartFile file,
                                        @RequestParam("modelId") String modelId) {
        try {
            // 直接调用Service层已实现的导入方法，返回结构化结果
            ImportResultVo importResult = managementService.importDataFromExcel(file, modelId);
            // 封装为项目统一的AjaxResult返回格式
            return AjaxResult.success("数据导入完成", importResult);
        } catch (Exception e) {
            logger.error("Excel数据导入失败：", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

}
