package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionReAttribute;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionReAttributeService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 条件关联属性管理Controller
 *
 * @author zdw
 * @date 2023-06-20
 */
@RestController
@RequestMapping("/transformationConditionReAttribute")
public class TMetaTransformationConditionReAttributeController extends BaseController
{
    @Autowired
    private ITMetaTransformationConditionReAttributeService tMetaTransformationConditionReAttributeService;

    /**
     * 查询条件关联属性管理列表
     */
   //@RequiresPermissions("metadata:attribute:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        startPage();
        List<TMetaTransformationConditionReAttribute> list = tMetaTransformationConditionReAttributeService.selectTMetaTransformationConditionReAttributeList(tMetaTransformationConditionReAttribute);
        return getDataTable(list);
    }

    /**
     * 导出条件关联属性管理列表
     */
   //@RequiresPermissions("metadata:attribute:export")
    //@Log(title = "条件关联属性管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute) throws IOException {
        List<TMetaTransformationConditionReAttribute> list = tMetaTransformationConditionReAttributeService.selectTMetaTransformationConditionReAttributeList(tMetaTransformationConditionReAttribute);
        ExcelUtil<TMetaTransformationConditionReAttribute> util = new ExcelUtil<TMetaTransformationConditionReAttribute>(TMetaTransformationConditionReAttribute.class);
        util.exportExcel(response, list, "条件关联属性管理数据");
    }

    /**
     * 获取条件关联属性管理详细信息
     */
   //@RequiresPermissions("metadata:attribute:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaTransformationConditionReAttributeService.selectTMetaTransformationConditionReAttributeById(id));
    }

    /**
     * 新增条件关联属性管理
     */
   //@RequiresPermissions("metadata:attribute:add")
    //@Log(title = "条件关联属性管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        return toAjax(tMetaTransformationConditionReAttributeService.insertTMetaTransformationConditionReAttribute(tMetaTransformationConditionReAttribute));
    }

    /**
     * 修改条件关联属性管理
     */
   //@RequiresPermissions("metadata:attribute:edit")
    //@Log(title = "条件关联属性管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaTransformationConditionReAttribute tMetaTransformationConditionReAttribute)
    {
        return toAjax(tMetaTransformationConditionReAttributeService.updateTMetaTransformationConditionReAttribute(tMetaTransformationConditionReAttribute));
    }

    /**
     * 删除条件关联属性管理
     */
   //@RequiresPermissions("metadata:attribute:remove")
    //@Log(title = "条件关联属性管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaTransformationConditionReAttributeService.deleteTMetaTransformationConditionReAttributeByIds(ids));
    }
}
