package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.vo.MetadataTechInfoVo;
import com.mshz.cloud.tms.business.service.TMetaModelTechService;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 技术元模型属性Controller
 *
 * @author zxx
 * @date 2022-09-23
 */
@RestController
@RequestMapping("/tech")
@Api(value = "tMetaModelTech", tags = {"tMetaModelTech"}, description = "技术元模型")
public class TMetaModelTechController extends BaseController
{
    @Autowired
    private TMetaModelTechService tMetaModelTechService;
    /**
     * 根据id查询技术元模型信息
     */
   //@RequiresPermissions("mm:tech:show")
    @GetMapping("/show")
    @ApiOperation(value = "根据id查询技术元模型信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="modelId",value="元模型id",required=true)
    })
    public AjaxResult show(Long modelId,String modelVersion)
    {
        MetadataTechInfoVo metadataTechInfoVo=tMetaModelTechService.selectTMetaModelTechById(modelId,modelVersion);

        return AjaxResult.success(metadataTechInfoVo);
    }

    /**
     * 查询技术元模型属性列表
     */
   //@RequiresPermissions("mm:tech:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询技术元模型属性列表")
    public TableDataInfo list(Long modelId,String columnName,String modelVersion)
    {
        if("".equals(modelVersion)||modelVersion == null){
            modelVersion="edit";
        }
        startPage();
         List<TMetaModelTech> list = tMetaModelTechService.selectTMetaModelTechList(modelId,columnName,modelVersion);
        return getDataTable(list);
    }


    /**
     * 查询技术元模型属性列表
     */
   //@RequiresPermissions("mm:tech:list")
    @GetMapping("/listDownDrop")
    @ApiOperation(value = "查询技术元模型属性列表下拉")
    public AjaxResult listDownDrop(Long modelId,String columnName,String modelVersion)
    {
        if("".equals(modelVersion)||modelVersion == null){
            modelVersion="edit";
        }
        List<TMetaModelTech> list = tMetaModelTechService.selectTMetaModelTechList(modelId,columnName,modelVersion);
        return AjaxResult.success(list);
    }

    /**
     * 新增技术元模型属性
     */
   //@RequiresPermissions("mm:tech:add")
    @ApiOperation(value = "新增技术元模型属性")
    @PostMapping
    public AjaxResult add(@RequestBody TMetaModelTech tMetaModelTech,Long modelId,String tableName) {
        if(tMetaModelTechService.selectTMetaModelTechOne(modelId,tMetaModelTech.getColumnName(),"edit").size()>0){
            return AjaxResult.error("属性名称已存在");
        }

        if ("datetime".equals(tMetaModelTech.getColumnType())||
                "".equals(tMetaModelTech.getColumnLength())||
                tMetaModelTech.getColumnLength()==null){
            tMetaModelTech.setColumnLength("0");
        }
        // TODO 当前在模型管理中对元模型表结构做出字段新增或修改操作后，元模型映射中间库中对应表的字段并未联动修改
        return toAjax(tMetaModelTechService.insertTMetaModelTech(tMetaModelTech,modelId,tableName));
    }

    /**
     * 修改技术元模型属性
     */
   //@RequiresPermissions("mm:tech:edit")
    @ApiOperation(value = "修改技术元模型属性(编辑)id必传")
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaModelTech tMetaModelTech,String tableName,String oldColumnName)
    {
        if(!oldColumnName.equals(tMetaModelTech.getColumnName())) {
            if (tMetaModelTechService.selectTMetaModelTechList(tMetaModelTech.getModelId(), tMetaModelTech.getColumnName(), "edit").size() > 0) {
                return AjaxResult.error("属性名称已存在");
            }
        }
        return toAjax(tMetaModelTechService.updateTMetaModelTech(tMetaModelTech,tableName,oldColumnName));
    }

    /**
     * 修改技术元模型属性
     */
   //@RequiresPermissions("mm:tech:edit")
    @ApiOperation(value = "修改技术元模型属性")
    @PostMapping("updatedata")
    public AjaxResult updatedata(@RequestBody TMetaModelTech tMetaModelTech)
    {
        return toAjax(tMetaModelTechService.updateTMetaModelTechToData(tMetaModelTech));
    }



    /**
     * 删除技术元模型属性
     */
   //@RequiresPermissions("mm:tech:remove")
    @ApiOperation(value = "删除技术元模型属性")
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id,String modelId, String tableName,String columnName) {
        return toAjax(tMetaModelTechService.deleteTMetaModelTechById(id,modelId,tableName,columnName));
    }

    /**
     * 根据id保存技术元模型信息
     */
   //@RequiresPermissions("mm:tech:keep")
    @PostMapping("/keep")
    @ApiOperation(value = "根据id保存技术元模型信息")
    public AjaxResult keep(Long modelId,@RequestBody MetadataTechInfoVo metadataTechInfoVo)
    {
        return toAjax(tMetaModelTechService.keepTMetaModelBuzById(modelId,metadataTechInfoVo));
    }
    /**
     * 查询技术属性类型int，char
     */
   //@RequiresPermissions("mm:tech:type")
    @GetMapping("/type")
    @ApiOperation(value = "查询技术属性类型int,char")
    public AjaxResult typeName()
    {
        return AjaxResult.success(tMetaModelTechService.selectTMetaModelTechType());
    }

}
