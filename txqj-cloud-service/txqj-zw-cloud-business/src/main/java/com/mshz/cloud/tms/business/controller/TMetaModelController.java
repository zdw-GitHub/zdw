package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.entity.TMetaModelVersion;
import com.mshz.cloud.tms.business.vo.MetadataBuzInfoVo;
import com.mshz.cloud.tms.business.vo.MetadataModelVo;
import com.mshz.cloud.tms.business.vo.TreeVo;
import com.mshz.cloud.tms.business.service.TMetaModelService;
import com.mshz.cloud.tms.business.service.TMetaModelTechService;
import com.mshz.cloud.tms.business.service.TMetaModelVersionService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 元模型Controller
 *
 * @author zxx
 * @date 2022-09-23
 */
@RestController
@RequestMapping("/model")
@Api(value = "tMetaModel", tags = {"tMetaModel"}, description = "元模型")
public class TMetaModelController extends BaseController {
    @Autowired
    private TMetaModelService tMetaModelService;
    @Autowired
    private TMetaModelVersionService tMetaModelVersionService;
    @Autowired
    private TMetaModelTechService tMetaModelTechService;

    /**
     * 根据id查询元数据信息
     */
   //@RequiresPermissions("mm:model:show")
    @GetMapping("/show")
    @ApiOperation(value = "根据id查询元数据信息")
    public AjaxResult show(Long modelId, String modelVersion) {
        if ("".equals(modelVersion) || modelVersion == null) {
            modelVersion = "edit";
        }
        return AjaxResult.success(tMetaModelService.selectTMetaModelBuzById(modelId, modelVersion));
    }

    /**
     * 查询所有类目及包括的元模型树
     */
   //@RequiresPermissions("mm:model:tree")
    @GetMapping("/tree")
    @ApiOperation(value = "查询所有类目及包括的元模型(目录树)")
    public AjaxResult selectMetaModelTree() {
        List<MetadataModelVo> metadataModelVos = tMetaModelService.selectMetaModelTree();
        return AjaxResult.success(metadataModelVos);
    }

    /**
     * 查询所有类目及包括的元模型树
     */
   //@RequiresPermissions("mm:model:tree")
    @GetMapping("/treeByTableName")
    @ApiOperation(value = "查询所有类目及包括的元模型(目录树)")
    public AjaxResult selectMetaModelTreeByTableName(TMetaModel tMetaModel) {
        List<MetadataModelVo> metadataModelVos = tMetaModelService.selectMetaModelTreeByTableName(tMetaModel);
        return AjaxResult.success(metadataModelVos);
    }

    /**
     * 查询所有类目及包括的元模型树，带有元模型版本号
     */
   //@RequiresPermissions("mm:model:treewithversion")
    @GetMapping("/treewithversion")
    @ApiOperation(value = "查询所有类目及包括的元模型树带有元模型版本号")
    public AjaxResult selectMetaModelTreeWithVersion() {
        List<TreeVo> metadataModelVos = tMetaModelService.selectMetaModelTreeWithVersion(null);
        return AjaxResult.success(metadataModelVos);
    }

   //@RequiresPermissions("mm:model:treewithversion")
    @GetMapping("/treewithversionMiddle")
    @ApiOperation(value = "查询所有类目及包括的元模型树带有元模型版本号")
    public AjaxResult selectMetaModelTreeMiddle() {
        TMetaModel tMetaModel = new TMetaModel();
        // tMetaModel.setAuditMode("1");
        List<TreeVo> metadataModelVos = tMetaModelService.selectMetaModelTreeWithVersionMiddle(tMetaModel);
        return AjaxResult.success(metadataModelVos);
    }

    /**
     * 查询该元模型的历史版本号
     */
   //@RequiresPermissions("mm:model:versions")
    @GetMapping("/versions")
    @ApiOperation(value = "查询该元模型的历史版本号")
    public AjaxResult versions(Long modelId) {
        List<String> list = tMetaModelService.selectMetaModelVersionList(modelId);
        return AjaxResult.success(list);
    }

    /**
     * 新增元模型
     */
   //@RequiresPermissions("mm:model:add")
    @PostMapping
    @ApiOperation(value = "新增元模型")
    public AjaxResult add(@RequestBody MetadataBuzInfoVo metadataBuzInfoVo) {
        TMetaModel tMetaModel = new TMetaModel();
        tMetaModel.setTableName(metadataBuzInfoVo.getTableName());
        if (tMetaModelService.selectTMetaModelList(tMetaModel).size() > 0) {
            return AjaxResult.error("元模型已存在");
        }
        return toAjax(tMetaModelService.insertTMetaModel(metadataBuzInfoVo));
    }

   //@RequiresPermissions("mm:model:edit")
    //@Log(title = "元模型", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改元模型")
    public AjaxResult edit(@RequestBody TMetaModel tMetaModel) {
        return toAjax(tMetaModelService.updateTMetaModel(tMetaModel));
    }

    /**
     * 发布元模型
     */
   //@RequiresPermissions("mm:model:release")
    @PostMapping("/release")
    @ApiOperation(value = "发布元模型")
    public AjaxResult add(Long modelId, String modelVersion, String updateLog, String remark, String tableName) {
        // 校检版本号
        TMetaModelVersion tMetaModelVersion = new TMetaModelVersion();
        tMetaModelVersion.setModelVersion(modelVersion);
        tMetaModelVersion.setModelId(modelId);
        if (tMetaModelVersionService.selectTMetaModelVersionList(tMetaModelVersion).size() > 0) {
            return AjaxResult.error("版本号已存在");
        }
        return toAjax(tMetaModelService.insertTMetaModelRelease(modelId, modelVersion, updateLog, remark, tableName));
    }

    /**
     * 删除编辑元模型相关数据
     */
   //@RequiresPermissions("mm:model:remove")
    @DeleteMapping("/{modelId}")
    @ApiOperation(value = "删除编辑元模型相关数据")
    public AjaxResult remove(@PathVariable Long modelId, String tableName) {
        return toAjax(tMetaModelService.deleteMetaMode(modelId, tableName));
    }

    /**
     * 复制发布的元模型
     */
   //@RequiresPermissions("mm:model:copy")
    @PostMapping("/copy")
    @ApiOperation(value = "复制发布的元模型")
    public AjaxResult copy(Long modelId, String modelVersion, String tableName, String tableNameCn) { // 复制之前先删除正在编辑的记录
        int a = tMetaModelService.copyTMetaModelRelease(modelId, modelVersion, tableName, tableNameCn);
        return toAjax(a);
    }

    /**
     * 查询元模型列表
     */
   //@RequiresPermissions("mm:model:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaModel tMetaModel) {
        startPage();
        List<TMetaModel> list = tMetaModelService.selectTMetaModelList(tMetaModel);
        return getDataTable(list);
    }

    /**
     * 查询元模型列表
     */
   //@RequiresPermissions("mm:model:list")
    @GetMapping("/listDownDrop")
    public AjaxResult listDownDrop(TMetaModel tMetaModel) {
        List<TMetaModel> list = tMetaModelService.selectTMetaModelList(tMetaModel);
        return AjaxResult.success(list);
    }

    /**
     * 导出元模型列表
     */
   //@RequiresPermissions("mm:model:export")
    //@Log(title = "元模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaModel tMetaModel) throws IOException {
        List<TMetaModel> list = tMetaModelService.selectTMetaModelList(tMetaModel);
        ExcelUtil<TMetaModel> util = new ExcelUtil<TMetaModel>(TMetaModel.class);
        util.exportExcel(response, list, "元模型数据");
    }

    /**
     * 获取元模型详细信息
     */
   //@RequiresPermissions("mm:model:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tMetaModelService.selectTMetaModelById(id));
    }

   //@RequiresPermissions("mm:model:query")
    @GetMapping(value = "/getOne")
    public AjaxResult getOne(Long id) {
        return AjaxResult.success(tMetaModelService.selectTMetaModelDetail(id));
    }

    /**
     * 根据id保存业务元数据信息
     */
   //@RequiresPermissions("mm:model:keep")
    @PostMapping("/keep")
    @ApiOperation(value = "根据id保存业务元数据信息")
    public AjaxResult keep(Long modelId, @RequestBody MetadataBuzInfoVo metadataBuzInfoVo, String oldTableName) {
        if (metadataBuzInfoVo.getAuditMode() != null && !"0".equals(metadataBuzInfoVo.getAuditMode())) {
            String table = tMetaModelService.whetherTheTableExists(metadataBuzInfoVo.getTableName().toLowerCase());
            if (table == null) {
                List<TMetaModelTech> teches =
                    tMetaModelTechService.selectTMetaModelTechList(modelId, null, metadataBuzInfoVo.getModelVersion());
                TMetaModelTech tech = new TMetaModelTech();
                tech.setColumnName("AUDIT_STATUS");
                tech.setColumnType("varchar");
                tech.setColumnLength("5");
                tech.setColumnNameCn("审核状态 0无审核  1正在审核  2已审核");
                tech.setIsNotNull("0");
                teches.add(tech);
            }
        }

        // 更新元模型表的"审计模式"、"接入模式"、"同步方式"、"关联方式"字段
        TMetaModel tmetamodelPara = new TMetaModel();
        tmetamodelPara.setTableName(metadataBuzInfoVo.getTableName());
        List<TMetaModel> TMetaModellist = tMetaModelService.selectTMetaModelList(tmetamodelPara);
        if (TMetaModellist != null && TMetaModellist.size() > 0) {
            TMetaModel TMetaModelObj = TMetaModellist.get(0);
            TMetaModelObj.setAuditMode(metadataBuzInfoVo.getAuditMode());
            TMetaModelObj.setAccessMode(metadataBuzInfoVo.getAccessMode());
            TMetaModelObj.setDataRelationType(metadataBuzInfoVo.getDataRelationType());
            TMetaModelObj.setDataStorageMode(metadataBuzInfoVo.getDataStorageMode());
            tMetaModelService.updateTMetaModel(TMetaModelObj);
        }

        // TMetaModel tMetaModel = new TMetaModel();
        // tMetaModel.setTableName(metadataBuzInfoVo.getTableName());
        // if (!oldTableName.equals(metadataBuzInfoVo.getTableName())) {
        // if (tMetaModelService.selectTMetaModelList(tMetaModel).size() > 0) {
        // return AjaxResult.error("元模型已存在");
        // }
        // }
        return toAjax(tMetaModelService.keepTMetaModelBuzById(modelId, metadataBuzInfoVo, oldTableName));
    }

   //@RequiresPermissions("mm:model:tree")
    @GetMapping("/metaModelTreeForDataShare")
    @ApiOperation(value = "查询所有类目及包括的元模型树(数据分享使用)")
    public AjaxResult selectMetaModelTreeForDataShare() {
        TMetaModel tMetaModel = new TMetaModel();
        // tMetaModel.setAuditMode("1");
        List<TreeVo> metadataModelVos = tMetaModelService.selectMetaModelTreeForDataShare(tMetaModel);
        return AjaxResult.success(metadataModelVos);
    }

    /**
     * 通过类目名称查询模型
     */
   //@RequiresPermissions("metadata:rule:query")
    @GetMapping(value = "/selectMetaModelWithoutIdRuleConfig/{category}")
    public AjaxResult selectMetaModelWithoutIdRuleConfig(@PathVariable("category") String category) {

        return AjaxResult.success(tMetaModelService.selectMetaModelWithoutIdRuleConfig(category));
    }
}
