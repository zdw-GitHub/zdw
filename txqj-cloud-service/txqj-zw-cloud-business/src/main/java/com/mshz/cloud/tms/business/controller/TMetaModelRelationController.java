package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.service.MetaDataSearchService;
import com.mshz.cloud.tms.business.service.TMetaModelRelationService;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.vo.MetadataRelationColumnVo;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import com.mshz.cloud.tms.business.entity.TDbTableColumn;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组合关联Controller
 * <p>
 *
 * @author zxx
 * @since 2022/10/8
 */
@RestController
@RequestMapping("/relation")
@Api(value = "tMetaModelRelation", tags = {"tMetaModelRelation"}, description = "组合关联")
public class TMetaModelRelationController extends BaseController {
    @Resource
    private TMetaModelRelationService tMetaModelRelationService;
    @Resource
    private MetaDataSearchService metaDataSearchService;

    /**
     * 根据id查询组合关联列表-字段关联
     */
   //@RequiresPermissions("mm:relation:list")
    @GetMapping("/list")
    @ApiOperation(value = "根据id查询组合关联列表-字段关联")
    public TableDataInfo list(Long modelId, String columnName, String modelVersion) {
        if ("".equals(modelVersion) || modelVersion == null) {
            modelVersion = "edit";
        }
        startPage();
        List<MetadataRelationColumnVo> list =
            tMetaModelRelationService.selectMetadataRelation(modelId, columnName, modelVersion);
        return getDataTable(list);
    }

    /**
     * 根据模型id查询组合关联的表-字段关联
     */
   //@RequiresPermissions("mm:relation:list")
    @GetMapping("/reTableList")
    @ApiOperation(value = "根据模型id查询组合关联的表-字段关联")
    public List<MetadataRelationColumnVo> getReTablelist(Long modelId, String modelVersion) {
        if ("".equals(modelVersion) || modelVersion == null) {
            modelVersion = "edit";
        }
        List<MetadataRelationColumnVo> list =
                tMetaModelRelationService.selectMetadataRelation(modelId, "", modelVersion);
        Map m = new HashMap<String, MetadataRelationColumnVo>();
        for (int i = 0; i < list.size(); i++) {
            MetadataRelationColumnVo vo  = list.get(i);
            String key = vo.getRelationDbId()+"&&"+vo.getRelationTableId();
            if(m.get(key)==null){
                m.put(key,list.get(i));
            }
        }
        List<MetadataRelationColumnVo> result = new ArrayList<>(m.values());
        return result;
    }


   //@RequiresPermissions("mm:relation:list")
    @GetMapping("/sqlList")
    @ApiOperation(value = "根据id查询组合关联列表-字段关联")
    public AjaxResult sqlList(Long modelId, String modelVersion) {
        if ("".equals(modelVersion) || modelVersion == null) {
            modelVersion = "edit";
        }
        List<MetadataRelationColumnVo> list =
                tMetaModelRelationService.selectMetadataRelation(modelId, null, modelVersion);
        return AjaxResult.success(list);
    }


    /**
     * 根据id查询组合关联-sql关联
     */
   //@RequiresPermissions("mm:relation:sql")
    @GetMapping("/sql")
    @ApiOperation(value = "根据id查询组合关联-sql关联")
    public AjaxResult sql(Long modelId, String modelVersion) {
        if ("".equals(modelVersion) || modelVersion == null) {
            modelVersion = "edit";
        }
        Object list = tMetaModelRelationService.selectMetadataRelationSQL(modelId, modelVersion);
        return AjaxResult.success(list);
    }

    /**
     * 关联数据源下拉框
     */
   //@RequiresPermissions("mm:relation:getDbs")
    @GetMapping("/getDbs")
    @ApiOperation(value = "关联数据源下拉框")
    public AjaxResult getDbs() {
        List<TDbConfig> list = tMetaModelRelationService.selectRelationDbSelect();
        return AjaxResult.success(list);
    }

    /**
     * 根据关联数据源查询关联表下拉框
     */
   //@RequiresPermissions("mm:relation:getTables")
    @GetMapping("/getTables")
    @ApiOperation(value = "根据关联数据源查询关联表下拉框")
    public AjaxResult getTables(Long dbId) {
        List<TDbTable> list = tMetaModelRelationService.selectRelationTableSelect(dbId);
        return AjaxResult.success(list);
    }

    /**
     * 根据关联表查询关联属性下拉框
     */
   //@RequiresPermissions("mm:relation:getColumns")
    @GetMapping("/getColumns")
    @ApiOperation(value = "根据关联表查询关联属性下拉框")
    public AjaxResult getColumns(Long tableId) {
        List<TDbTableColumn> list = tMetaModelRelationService.selectRelationColumnSelect(tableId);
        return AjaxResult.success(list);
    }

    /**
     * 根据关联表查询关联属性信息列表
     */
   //@RequiresPermissions("mm:relation:getColumnInfoList")
    @GetMapping("/getColumnInfoList")
    @ApiOperation(value = "根据关联表查询关联属性信息列表")
    public AjaxResult getColumnInfoList(Long tableId, String columnName) {
        List<TDbTableColumn> list = tMetaModelRelationService.selectRelationColumnInfoSelect(tableId, columnName);
        return AjaxResult.success(list);
    }


    /**
     * 根据id编辑组合关联
     */
   //@RequiresPermissions("mm:relation:edit")
    @PutMapping
    @ApiOperation(value = "据id编辑组合关联")
    public AjaxResult edit(@RequestBody MetadataRelationColumnVo metadataRelationColumnVo, Long modelId) {
        choose(modelId, "1");
        return toAjax(tMetaModelRelationService.updateMetadataRelationColumnVo(metadataRelationColumnVo));
    }

    /**
     * 新增组合关联
     */
   //@RequiresPermissions("mm:relation:add")
    @PutMapping("add")
    @ApiOperation(value = "新增组合关联")
    public AjaxResult add(@RequestBody MetadataRelationColumnVo metadataRelationColumnVo,Long modelId) {
        choose(modelId, "1");
        return toAjax(tMetaModelRelationService.insertMetadataRelationColumnVo(metadataRelationColumnVo,modelId));
    }

    /**
     * 关联后的结果保存
     */
   //@RequiresPermissions("mm:relation:relationEdit")
    @PutMapping("relationEdit")
    @ApiOperation(value = "关联后的结果保存")
    public AjaxResult relationEdit(Long relationId, Long relationDbId, Long relationTableId, Long relationColumnId,
        Integer isExternalConnection, String relationCondition, String relationTableAlias, Long modelId) {
        choose(modelId, "1");
        return toAjax(tMetaModelRelationService.updateRelationColumnResult(relationId, relationDbId, relationTableId,
            relationColumnId, isExternalConnection, relationCondition, relationTableAlias));
    }

    /**
     * 关联后的sql结果保存(先删后增)
     */
   //@RequiresPermissions("mm:relation:sqlEdit")
    @PutMapping("sqlEdit")
    @ApiOperation(value = "关联后的sql结果保存(先删后增)")
    public AjaxResult sqlEdit(Long modelId, String columnIdList, String sqlContent) {
        List<Long> TechIdList = new ArrayList<>();
        String[] split = columnIdList.split(",");
        for (int i = 0; i < split.length; i++) {
            TechIdList.add(Long.valueOf(split[i]));
        }
        String str = URLDecoder.decode(sqlContent);
        int a = tMetaModelRelationService.updateRelationSqlResult(modelId, TechIdList, str);
        choose(modelId, "2");
        return toAjax(a);
    }

    /**
     * 字段关联和sql关联选择
     */
   //@RequiresPermissions("mm:relation:choose")
    @PutMapping("choose")
    @ApiOperation(value = "字段关联和sql关联选择")
    public AjaxResult choose(Long modelId, String relationType) {
        String notIsUsed = null;
        if ("1".equals(relationType)) {
            notIsUsed = "2";
        } else if ("2".equals(relationType)) {
            notIsUsed = "1";
        }
        tMetaModelRelationService.updateColumnOrSql(modelId, relationType, notIsUsed);
        return AjaxResult.success();
    }

   //@RequiresPermissions("mm:relation:remove")
    @PostMapping("delete")
    @ApiOperation(value = "删除关联")
    public AjaxResult delete(String relationId) {

        tMetaModelRelationService.deleteTMetaModelRelationById(Long.valueOf(relationId));
        return AjaxResult.success();
    }

    /**
     * 获取关联类型
     */
   //@RequiresPermissions("mm:relation:relationtype")
    @PostMapping("/relationtype")
    public AjaxResult getRelationType(@RequestBody MetaModelShowVo metadataShowVo) {
        String result = metaDataSearchService.getRelationType(metadataShowVo);
        return AjaxResult.success(result);
    }
}
