package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.annotation.Log;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.enums.BusinessType;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeModelSetService;
import com.mshz.cloud.tms.business.vo.QualityPublicTreeModelSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 公共树形组件-元模型树形组件配置Controller
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@RestController
@RequestMapping("/modelset")
public class QualityPublicTreeModelSetController extends BaseController
{
    @Autowired
    private IQualityPublicTreeModelSetService qualityPublicTreeModelSetService;

    /**
     * 查询公共树形组件-元模型树形组件配置列表
     */
    @RequiresPermissions("quality:modelset:list")
    @GetMapping("/list")
    public TableDataInfo list(QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        startPage();
        List<QualityPublicTreeModelSet> list = qualityPublicTreeModelSetService.selectQualityPublicTreeModelSetList(qualityPublicTreeModelSet);
        return getDataTable(list);
    }

    /**
     * 导出公共树形组件-元模型树形组件配置列表
     */
    @RequiresPermissions("quality:modelset:export")
    @Log(title = "公共树形组件-元模型树形组件配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityPublicTreeModelSet qualityPublicTreeModelSet) throws IOException {
        List<QualityPublicTreeModelSet> list = qualityPublicTreeModelSetService.selectQualityPublicTreeModelSetList(qualityPublicTreeModelSet);
        ExcelUtil<QualityPublicTreeModelSet> util = new ExcelUtil<QualityPublicTreeModelSet>(QualityPublicTreeModelSet.class);
        util.exportExcel(response, list, "公共树形组件-元模型树形组件配置数据");
    }

    /**
     * 获取公共树形组件-元模型树形组件配置详细信息
     */
    @RequiresPermissions("quality:modelset:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(qualityPublicTreeModelSetService.selectQualityPublicTreeModelSetById(id));
    }

    /**
     * 新增公共树形组件-元模型树形组件配置
     */
    @RequiresPermissions("quality:modelset:add")
    @Log(title = "公共树形组件-元模型树形组件配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        return toAjax(qualityPublicTreeModelSetService.insertQualityPublicTreeModelSet(qualityPublicTreeModelSet));
    }

    /**
     * 修改公共树形组件-元模型树形组件配置
     */
    @RequiresPermissions("quality:modelset:edit")
    @Log(title = "公共树形组件-元模型树形组件配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        return toAjax(qualityPublicTreeModelSetService.updateQualityPublicTreeModelSet(qualityPublicTreeModelSet));
    }

    /**
     * 删除公共树形组件-元模型树形组件配置
     */
    @RequiresPermissions("quality:modelset:remove")
    @Log(title = "公共树形组件-元模型树形组件配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qualityPublicTreeModelSetService.deleteQualityPublicTreeModelSetByIds(ids));
    }
}
