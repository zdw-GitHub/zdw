package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.annotation.Log;
import com.mshz.cloud.tms.business.enums.BusinessType;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNodeColumn;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeInfoService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeColumnService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公共树形组件-树节点查询数据属性Controller
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@RestController
@RequestMapping("/treenodecolumn")
public class QualityPublicTreeNodeColumnController extends BaseController {

    @Resource
    private IQualityPublicTreeNodeColumnService qualityPublicTreeNodeColumnService;

    @Resource
    private IQualityPublicTreeInfoService treeService;

    @Resource
    private IQualityPublicTreeNodeService treeNodeService;

    /**
     * 查询公共树形组件-树节点查询数据属性列表
     */
    @RequiresPermissions("quality:treenodecolumn:list")
    @GetMapping("/list")
    public TableDataInfo list(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        startPage();
        List<QualityPublicTreeNodeColumn> list = qualityPublicTreeNodeColumnService.selectQualityPublicTreeNodeColumnList(qualityPublicTreeNodeColumn);
        return getDataTable(list);
    }

    /**
     * 导出公共树形组件-树节点查询数据属性列表
     */
    @RequiresPermissions("quality:treenodecolumn:export")
    @Log(title = "公共树形组件-树节点查询数据属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn) throws IOException {
        List<QualityPublicTreeNodeColumn> list = qualityPublicTreeNodeColumnService.selectQualityPublicTreeNodeColumnList(qualityPublicTreeNodeColumn);
        ExcelUtil<QualityPublicTreeNodeColumn> util = new ExcelUtil<QualityPublicTreeNodeColumn>(QualityPublicTreeNodeColumn.class);
        util.exportExcel(response, list, "公共树形组件-树节点查询数据属性数据");
    }

    /**
     * 获取公共树形组件-树节点查询数据属性详细信息
     */
    @RequiresPermissions("quality:treenodecolumn:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(qualityPublicTreeNodeColumnService.selectQualityPublicTreeNodeColumnById(id));
    }

    /**
     * 新增公共树形组件-树节点查询数据属性
     */
    @RequiresPermissions("quality:treenodecolumn:add")
    @Log(title = "公共树形组件-树节点查询数据属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        return toAjax(qualityPublicTreeNodeColumnService.insertQualityPublicTreeNodeColumn(qualityPublicTreeNodeColumn));
    }

    /**
     * 修改公共树形组件-树节点查询数据属性
     */
    @RequiresPermissions("quality:treenodecolumn:edit")
    @Log(title = "公共树形组件-树节点查询数据属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        return toAjax(qualityPublicTreeNodeColumnService.updateQualityPublicTreeNodeColumn(qualityPublicTreeNodeColumn));
    }

    /**
     * 删除公共树形组件-树节点查询数据属性
     */
    @RequiresPermissions("quality:treenodecolumn:remove")
    @Log(title = "公共树形组件-树节点查询数据属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qualityPublicTreeNodeColumnService.deleteQualityPublicTreeNodeColumnByIds(ids));
    }

}
