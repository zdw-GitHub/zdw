package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaModelIdRule;
import com.mshz.cloud.tms.business.service.ITMetaModelIdRuleService;
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
 * 模型ID生成规则配置Controller
 *
 * @author zdw
 * @date 2023-08-08
 */
@RestController
@RequestMapping("/idRule")
public class TMetaModelIdRuleController extends BaseController
{
    @Autowired
    private ITMetaModelIdRuleService tMetaModelIdRuleService;

    /**
     * 查询模型ID生成规则配置列表
     */
   //@RequiresPermissions("metadata:rule:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaModelIdRule tMetaModelIdRule)
    {
        startPage();
        List<TMetaModelIdRule> list = tMetaModelIdRuleService.selectTMetaModelIdRuleListWithoutDefault(tMetaModelIdRule);
        return getDataTable(list);
    }

    /**
     * 导出模型ID生成规则配置列表
     */
   //@RequiresPermissions("metadata:rule:export")
    //@Log(title = "模型ID生成规则配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaModelIdRule tMetaModelIdRule) throws IOException {
        List<TMetaModelIdRule> list = tMetaModelIdRuleService.selectTMetaModelIdRuleList(tMetaModelIdRule);
        ExcelUtil<TMetaModelIdRule> util = new ExcelUtil<TMetaModelIdRule>(TMetaModelIdRule.class);
        util.exportExcel(response, list, "模型ID生成规则配置数据");
    }

    /**
     * 获取模型ID生成规则配置详细信息
     */
   //@RequiresPermissions("metadata:rule:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaModelIdRuleService.selectTMetaModelIdRuleById(id));
    }


    /**
     * 通过模型名称获取模型ID生成规则配置详细信息
     */
   //@RequiresPermissions("metadata:rule:query")
    @GetMapping(value = "/infoByModelName/{modelName}")
    public AjaxResult getInfoByModelName(@PathVariable("modelName") String modelName)
    {
        TMetaModelIdRule rule = tMetaModelIdRuleService.getTMetaModelIdRuleByModelName(modelName);
        if (rule!=null) {
            return AjaxResult.success(rule);
        } else {
            return AjaxResult.error("没有找到模型ID生成规则配置");
        }
    }



    /**
     * 新增模型ID生成规则配置
     */
   //@RequiresPermissions("metadata:rule:add")
    //@Log(title = "模型ID生成规则配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaModelIdRule tMetaModelIdRule)
    {
        return toAjax(tMetaModelIdRuleService.insertTMetaModelIdRule(tMetaModelIdRule));
    }


    /**
     * 新增默认模型ID生成规则配置
     */
   //@RequiresPermissions("metadata:rule:add")
    //@Log(title = "新增默认模型ID生成规则配置", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addDefault")
    public AjaxResult addDefault(@RequestBody TMetaModelIdRule tMetaModelIdRule)
    {
        String modelName = tMetaModelIdRule.getModelName();
        if(modelName.equals("default")){
            TMetaModelIdRule rule = new TMetaModelIdRule();
            rule.setModelName(modelName);
            List<TMetaModelIdRule> list = tMetaModelIdRuleService.selectTMetaModelIdRuleList(rule);
            if (list!=null&&list.size() > 0) {
                tMetaModelIdRule.setId(list.get(0).getId());//
                return AjaxResult.success(tMetaModelIdRuleService.updateTMetaModelIdRule(tMetaModelIdRule));
            } else {
                return AjaxResult.success(tMetaModelIdRuleService.insertTMetaModelIdRule(tMetaModelIdRule));
            }
        }else{
            return AjaxResult.error("非默认规则");
        }

    }

    /**
     * 修改模型ID生成规则配置
     */
   //@RequiresPermissions("metadata:rule:edit")
    //@Log(title = "模型ID生成规则配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaModelIdRule tMetaModelIdRule)
    {
        return toAjax(tMetaModelIdRuleService.updateTMetaModelIdRule(tMetaModelIdRule));
    }

    /**
     * 删除模型ID生成规则配置
     */
   //@RequiresPermissions("metadata:rule:remove")
    //@Log(title = "模型ID生成规则配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaModelIdRuleService.deleteTMetaModelIdRuleByIds(ids));
    }

    /**
     * 根据表名获取ID
     * @param modelName
     * @return
     */
    @GetMapping(value = "/getIDByTableName/{modelName}")
    public AjaxResult getIDByTableName(String modelName){

        return AjaxResult.success(tMetaModelIdRuleService.getIDByTableName(modelName));
    }


    /**
     * 批量更新ID
     * @param modelNames
     * @return
     */
   //@RequiresPermissions("metadata:rule:batchConversionId")
    @GetMapping(value = "/batchConversionId/{modelNames}")
    public AjaxResult batchConversionId(@PathVariable String[] modelNames){

        return AjaxResult.success(tMetaModelIdRuleService.batchConversionId(modelNames));
    }
}
