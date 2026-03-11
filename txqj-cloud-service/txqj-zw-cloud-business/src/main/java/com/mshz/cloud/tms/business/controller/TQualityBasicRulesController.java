package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.entity.TQualityBasicRules;
import com.mshz.cloud.tms.business.service.ITQualityBasicRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据质量-基础规则Controller
 *
 * @author hzy
 * @date 2022-10-22
 */
@RestController
@RequestMapping("/basicRules")
public class TQualityBasicRulesController extends BaseController
{
    @Autowired
    private ITQualityBasicRulesService tQualityBasicRulesService;

    /**
     * 查询数据质量-基础规则列表
     */
   //@RequiresPermissions("quality:basicrule:list")
    @GetMapping("/list")
    public TableDataInfo list(TQualityBasicRules tQualityBasicRules)
    {
        startPage();
        List<TQualityBasicRules> list = tQualityBasicRulesService.selectTQualityBasicRulesList(tQualityBasicRules);
        return getDataTable(list);
    }

    /**
     * 导出数据质量-基础规则列表
     */
   //@RequiresPermissions("quality:basicrule:export")
   //@Log(title = "数据质量-基础规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TQualityBasicRules tQualityBasicRules) throws IOException {
        List<TQualityBasicRules> list = tQualityBasicRulesService.selectTQualityBasicRulesList(tQualityBasicRules);
        ExcelUtil<TQualityBasicRules> util = new ExcelUtil<TQualityBasicRules>(TQualityBasicRules.class);
        util.exportExcel(response, list, "数据质量-基础规则数据");
    }

    /**
     * 获取数据质量-基础规则详细信息
     */
   //@RequiresPermissions("quality:basicrule:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tQualityBasicRulesService.selectTQualityBasicRulesById(id));
    }

    /**
     * 新增数据质量-基础规则
     */
   //@RequiresPermissions("quality:basicrule:add")
   //@Log(title = "数据质量-基础规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQualityBasicRules tQualityBasicRules)
    {
        return toAjax(tQualityBasicRulesService.insertTQualityBasicRules(tQualityBasicRules));
    }

    /**
     * 修改数据质量-基础规则
     */
   //@RequiresPermissions("quality:basicrule:edit")
   //@Log(title = "数据质量-基础规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQualityBasicRules tQualityBasicRules)
    {
        return toAjax(tQualityBasicRulesService.updateTQualityBasicRules(tQualityBasicRules));
    }

    /**
     * 删除数据质量-基础规则
     */
   //@RequiresPermissions("quality:basicrule:remove")
   //@Log(title = "数据质量-基础规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tQualityBasicRulesService.deleteTQualityBasicRulesByIds(ids));
    }
}
