package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionGroup;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionGroupService;
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
 * 转换条件组管理Controller
 *
 * @author zdw
 * @date 2023-06-24
 */
@RestController
@RequestMapping("/transformationConditionGroup")
public class TMetaTransformationConditionGroupController extends BaseController
{
    @Autowired
    private ITMetaTransformationConditionGroupService tMetaTransformationConditionGroupService;

    /**
     * 查询转换条件组管理列表
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaTransformationConditionGroup tMetaTransformationConditionGroup)
    {
        startPage();
        List<TMetaTransformationConditionGroup> list = tMetaTransformationConditionGroupService.selectTMetaTransformationConditionGroupList(tMetaTransformationConditionGroup);
        return getDataTable(list);
    }

    /**
     * 导出转换条件组管理列表
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:export")
    //@Log(title = "转换条件组管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaTransformationConditionGroup tMetaTransformationConditionGroup) throws IOException {
        List<TMetaTransformationConditionGroup> list = tMetaTransformationConditionGroupService.selectTMetaTransformationConditionGroupList(tMetaTransformationConditionGroup);
        ExcelUtil<TMetaTransformationConditionGroup> util = new ExcelUtil<TMetaTransformationConditionGroup>(TMetaTransformationConditionGroup.class);
        util.exportExcel(response, list, "转换条件组管理数据");
    }

    /**
     * 获取转换条件组管理详细信息
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaTransformationConditionGroupService.selectTMetaTransformationConditionGroupById(id));
    }

    /**
     * 新增转换条件组管理
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:add")
    //@Log(title = "转换条件组管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaTransformationConditionGroup tMetaTransformationConditionGroup)
    {
        return AjaxResult.success(tMetaTransformationConditionGroupService.insertTMetaTransformationConditionGroup(tMetaTransformationConditionGroup));
    }

    /**
     * 修改转换条件组管理
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:edit")
    //@Log(title = "转换条件组管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaTransformationConditionGroup tMetaTransformationConditionGroup)
    {
        return toAjax(tMetaTransformationConditionGroupService.updateTMetaTransformationConditionGroup(tMetaTransformationConditionGroup));
    }

    /**
     * 删除转换条件组管理
     */
   //@RequiresPermissions("metadata:transformationConditionGroup:remove")
    //@Log(title = "转换条件组管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaTransformationConditionGroupService.deleteTMetaTransformationConditionGroupByIds(ids));
    }
}
