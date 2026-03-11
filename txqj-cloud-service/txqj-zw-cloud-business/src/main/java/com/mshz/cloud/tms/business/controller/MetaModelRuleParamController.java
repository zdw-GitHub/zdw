package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.MetaModelRuleParam;
import com.mshz.cloud.tms.business.service.IMetaModelRuleParamService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 元模型字段规则参数Controller
 *
 * @author zdw
 * @date 2023-08-17
 */
@RestController
@RequestMapping("/param")
@Api(value = "param", tags = {"param"}, description = "元模型字段数据规则参数")
public class MetaModelRuleParamController extends BaseController {

    @Autowired
    private IMetaModelRuleParamService metaModelRuleParamService;

    /**
     * 查询元模型字段规则参数列表
     */
   //@RequiresPermissions("metadata:param:list")
    @GetMapping("/list")
    public TableDataInfo list(MetaModelRuleParam metaModelRuleParam)
    {
        startPage();
        List<MetaModelRuleParam> list = metaModelRuleParamService.selectMetaModelRuleParamList(metaModelRuleParam);
        return getDataTable(list);
    }

    /**
     * 导出元模型字段规则参数列表
     */
   //@RequiresPermissions("metadata:param:export")
    //@Log(title = "元模型字段规则参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MetaModelRuleParam metaModelRuleParam) throws IOException {
        List<MetaModelRuleParam> list = metaModelRuleParamService.selectMetaModelRuleParamList(metaModelRuleParam);
        ExcelUtil<MetaModelRuleParam> util = new ExcelUtil<MetaModelRuleParam>(MetaModelRuleParam.class);
        util.exportExcel(response, list, "元模型字段规则参数数据");
    }

    /**
     * 获取元模型字段规则参数详细信息
     */
   //@RequiresPermissions("metadata:param:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(metaModelRuleParamService.selectMetaModelRuleParamById(id));
    }

    /**
     * 新增元模型字段规则参数
     */
   //@RequiresPermissions("metadata:param:add")
    //@Log(title = "元模型字段规则参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MetaModelRuleParam metaModelRuleParam)
    {
        return toAjax(metaModelRuleParamService.insertMetaModelRuleParam(metaModelRuleParam));
    }

    /**
     * 修改元模型字段规则参数
     */
   //@RequiresPermissions("metadata:param:edit")
    //@Log(title = "元模型字段规则参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MetaModelRuleParam metaModelRuleParam)
    {
        return toAjax(metaModelRuleParamService.updateMetaModelRuleParam(metaModelRuleParam));
    }

    /**
     * 删除元模型字段规则参数
     */
   //@RequiresPermissions("metadata:param:remove")
    //@Log(title = "元模型字段规则参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(metaModelRuleParamService.deleteMetaModelRuleParamByIds(ids));
    }
}
