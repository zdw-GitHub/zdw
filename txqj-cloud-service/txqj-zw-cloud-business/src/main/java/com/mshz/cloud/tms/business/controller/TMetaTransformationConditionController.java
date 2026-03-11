package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.vo.TestSQLParaVo;
import com.mshz.cloud.tms.business.entity.TMetaTransformationCondition;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionService;
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
 * 转换条件管理Controller
 *
 * @author zdw
 * @date 2023-06-20
 */
@RestController
@RequestMapping("/transformationCondition")
public class TMetaTransformationConditionController extends BaseController
{
    @Autowired
    private ITMetaTransformationConditionService tMetaTransformationConditionService;

    /**
     * 查询转换条件管理列表
     */
   //@RequiresPermissions("metadata:condition:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaTransformationCondition tMetaTransformationCondition)
    {
        startPage();
        List<TMetaTransformationCondition> list = tMetaTransformationConditionService.selectTMetaTransformationConditionList(tMetaTransformationCondition);
        return getDataTable(list);
    }

    /**
     * 导出转换条件管理列表
     */
   //@RequiresPermissions("metadata:condition:export")
    //@Log(title = "转换条件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaTransformationCondition tMetaTransformationCondition)
    {
        List<TMetaTransformationCondition> list = tMetaTransformationConditionService.selectTMetaTransformationConditionList(tMetaTransformationCondition);
        ExcelUtil<TMetaTransformationCondition> util = new ExcelUtil<TMetaTransformationCondition>(TMetaTransformationCondition.class);
        try {
            util.exportExcel(response, list, "转换条件管理数据");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取转换条件管理详细信息
     */
   //@RequiresPermissions("metadata:condition:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaTransformationConditionService.selectTMetaTransformationConditionById(id));
    }

    /**
     * 新增转换条件管理
     */
   //@RequiresPermissions("metadata:condition:add")
    //@Log(title = "转换条件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaTransformationCondition tMetaTransformationCondition)
    {
        return AjaxResult.success(tMetaTransformationConditionService.insertTMetaTransformationCondition(tMetaTransformationCondition));
    }

    /**
     * 修改转换条件管理
     */
   //@RequiresPermissions("metadata:condition:edit")
    //@Log(title = "转换条件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaTransformationCondition tMetaTransformationCondition)
    {
        return toAjax(tMetaTransformationConditionService.updateTMetaTransformationCondition(tMetaTransformationCondition));
    }

    /**
     * 删除转换条件管理
     */
   //@RequiresPermissions("metadata:condition:remove")
    //@Log(title = "转换条件管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaTransformationConditionService.deleteTMetaTransformationConditionByIds(ids));
    }


    /**
     * SQL测试方法
     */
   //@RequiresPermissions("metadata:condition:testsql")
    @GetMapping("/testSQL")
    public AjaxResult testSQL(TestSQLParaVo para){
        return AjaxResult.success(tMetaTransformationConditionService.testSQL(para));
    }
}
