package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.MetaModelRuleConversionLog;
import com.mshz.cloud.tms.business.service.IMetaModelRuleConversionLogService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 元模型字段规则转换日志记录Controller
 *
 * @author zdw
 * @date 2023-08-23
 */
@RestController
@RequestMapping("/ruleLog")
@Api(value = "ruleLog", tags = {"ruleLog"}, description = "元模型字段数据规则转换日志")
public class MetaModelRuleConversionLogController extends BaseController {

    @Resource
    private IMetaModelRuleConversionLogService metaModelRuleConversionLogService;

    /**
     * 查询元模型字段规则转换日志记录列表
     */
   //@RequiresPermissions("system:log:list")
    @GetMapping("/list")
    public TableDataInfo list(MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        startPage();
        List<MetaModelRuleConversionLog> list = metaModelRuleConversionLogService.selectMetaModelRuleConversionLogList(metaModelRuleConversionLog);
        return getDataTable(list);
    }

    /**
     * 导出元模型字段规则转换日志记录列表
     */
   //@RequiresPermissions("system:log:export")
    //@Log(title = "元模型字段规则转换日志记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MetaModelRuleConversionLog metaModelRuleConversionLog) throws IOException {
        List<MetaModelRuleConversionLog> list = metaModelRuleConversionLogService.selectMetaModelRuleConversionLogList(metaModelRuleConversionLog);
        ExcelUtil<MetaModelRuleConversionLog> util = new ExcelUtil<MetaModelRuleConversionLog>(MetaModelRuleConversionLog.class);
        util.exportExcel(response, list, "元模型字段规则转换日志记录数据");
    }

    /**
     * 获取元模型字段规则转换日志记录详细信息
     */
   //@RequiresPermissions("system:log:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(metaModelRuleConversionLogService.selectMetaModelRuleConversionLogById(id));
    }

    /**
     * 新增元模型字段规则转换日志记录
     */
   //@RequiresPermissions("system:log:add")
    //@Log(title = "元模型字段规则转换日志记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        return toAjax(metaModelRuleConversionLogService.insertMetaModelRuleConversionLog(metaModelRuleConversionLog));
    }

    /**
     * 修改元模型字段规则转换日志记录
     */
   //@RequiresPermissions("system:log:edit")
    //@Log(title = "元模型字段规则转换日志记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MetaModelRuleConversionLog metaModelRuleConversionLog)
    {
        return toAjax(metaModelRuleConversionLogService.updateMetaModelRuleConversionLog(metaModelRuleConversionLog));
    }

    /**
     * 删除元模型字段规则转换日志记录
     */
   //@RequiresPermissions("system:log:remove")
    //@Log(title = "元模型字段规则转换日志记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(metaModelRuleConversionLogService.deleteMetaModelRuleConversionLogByIds(ids));
    }
}
