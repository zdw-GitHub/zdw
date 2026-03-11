package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.service.TLabelService;
import com.mshz.cloud.tms.business.entity.TLabel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签Controller
 *
 * @author zxx
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/label")
@Api(value = "tLabel", tags = {"tLabel"}, description = "标签")
public class TLabelController extends BaseController
{
    @Autowired
    private TLabelService tLabelService;

    /**
     * 查询标签列表
     */
   //@RequiresPermissions("mm:label:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询标签列表")
    public TableDataInfo list(TLabel tLabel)
    {
        startPage();
        List<TLabel> list = tLabelService.selectTLabelList(tLabel);
        return getDataTable(list);
    }

    /**
     * 查询标签名称及其id
     */
   //@RequiresPermissions("mm:label:selectNameId")
    @GetMapping("/selectNameId")
    @ApiOperation(value = "查询标签名称及其id")
    public AjaxResult selectNameId()
    {
        List<TLabel> list = tLabelService.selectNameId();
        return AjaxResult.success(list);
    }

    /**
     * 获取标签详细信息
     */
   //@RequiresPermissions("mm:label:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取标签详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tLabelService.selectTLabelById(id));
    }

    /**
     * 新增标签
     */
   //@RequiresPermissions("mm:label:add")
    @PostMapping
    @ApiOperation(value = "新增标签")
    public AjaxResult add(@RequestBody TLabel tLabel)
    {
        if(tLabelService.checkTLabelCodeUnique(tLabel.getLabelCode())!=null){
            return AjaxResult.error("新增标签编码" + tLabel.getLabelCode()+"失败，标签编码已存在");
        }
        if(tLabelService.checkTLabelNameUnique(tLabel.getLabelName())!=null){
            return AjaxResult.error("新增标签名称" + tLabel.getLabelName()+"失败，标签名称已存在");
        }
        return toAjax(tLabelService.insertTLabel(tLabel));
    }

    /**
     * 修改标签
     */
   //@RequiresPermissions("mm:label:edit")
    @PutMapping
    @ApiOperation(value = "修改标签")
    public AjaxResult edit(@RequestBody TLabel tLabel)
    {

        return toAjax(tLabelService.updateTLabel(tLabel));
    }

    /**
     * 删除标签
     */
   //@RequiresPermissions("mm:label:remove")
	@DeleteMapping("/{id}")
    @ApiOperation(value = "删除标签")
    public AjaxResult remove(@PathVariable Long id)
    {
        return  toAjax( tLabelService.deleteTLabelById(id));
    }

}
