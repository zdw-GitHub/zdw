package com.mshz.cloud.tms.business.controller;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.annotation.Log;
import com.mshz.cloud.tms.business.annotation.MetaDataDeptScope;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.enums.BusinessType;
import com.mshz.cloud.tms.business.service.ISgTcdevFibersegBVoService;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegBVo;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 光缆段基本信息Controller
 *
 * @author ruoyi
 * @date 2023-03-07
 */
@RestController
@RequestMapping("/sgTcdevFibersegB")
@Api(description = "资源维护-光缆段")
public class SgTcdevFibersegBVoController extends BaseController {
    @Resource
    private ISgTcdevFibersegBVoService sgTcdevFibersegBVoService;

    @ApiOperation(value = "查询正式库-所有光缆段")
    // @RequiresPermissions("resources:sgTcdevFibersegB:list")
    @GetMapping("/alllistmeta")
    public TableDataInfo alllistmeta(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        startPage();
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectAllFibersegListMeta(sgTcdevFibersegBVo);
        return getDataTable(list);
    }

    @ApiOperation(value = "查询已绑定列表-正式库-光缆段")
    // @RequiresPermissions("resources:sgTcdevFibersegB:list")
    @GetMapping("/listmeta")
    public TableDataInfo listmeta(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        startPage();
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectSgTcdevFibersegBListMeta(sgTcdevFibersegBVo);
        if(list!=null){
            for (int i = 0; i < list.size(); i++) {
                SgTcdevFibersegBVo seg = list.get(i);
                seg.setFiberType(seg.getFiberTypeName());
                seg.setProducerName(seg.getProducerNameLabel());
                seg.setaResobjid(seg.getaResobjidName());
                seg.setzResobjid(seg.getzResobjidName());
            }
        }

        return getDataTable(list);
    }

    @ApiOperation(value = "查询已绑定列表-正式库-光缆段ids")
    @GetMapping("/getSegIdsByFiberId")
    public AjaxResult getSegIdsByFiberId(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectSgTcdevFibersegBListMeta(sgTcdevFibersegBVo);
        String ids = "空";
        if(list.size()>0){
            List<String> idList = list.stream().map(item -> item.getId()).collect(Collectors.toList());
            ids = String.join(",", idList);
        }
        return AjaxResult.success(ids);
    }

    @ApiOperation(value = "查询未绑定列表-正式库-光缆段")
    // @RequiresPermissions("resources:sgTcdevFibersegB:list")
    @GetMapping("/selectdatameta")
    @MetaDataDeptScope
    public TableDataInfo selectdatameta(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        startPage();
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectSgTcdevFibersegBListMetaNotIn(sgTcdevFibersegBVo);
        return getDataTable(list);
    }

    @ApiOperation(value = "查询已绑定列表-待处理(全部)-光缆段")
    // @RequiresPermissions("resources:sgTcdevFibersegB:list")
    @GetMapping("/listslave")
    public TableDataInfo list(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        startPage();
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectSgTcdevFibersegBListSlave(sgTcdevFibersegBVo);
        return getDataTable(list);
    }

    @ApiOperation(value = "查询未绑定列表-待处理(全部)-光缆段")
    // @RequiresPermissions("resources:sgTcdevFibersegB:list")
    @GetMapping("/selectdataAll")
    public TableDataInfo selectdataAll(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        startPage();
        List<SgTcdevFibersegBVo> list = sgTcdevFibersegBVoService.selectSgTcdevFibersegBListSlaveNotIn(sgTcdevFibersegBVo);
        return getDataTable(list);
    }

    /**
     * 新增光缆段基本信息
     */
    @ApiOperation(value = "新增光缆段基本信息")
    @RequiresPermissions("resources:sgTcdevFibersegB:add")
    @Log(title = "光缆段基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SgTcdevFibersegBVo sgTcdevFibersegB) {
        return toAjax(sgTcdevFibersegBVoService.insertSgTcdevFibersegB(sgTcdevFibersegB));
    }

    /**
     * 修改光缆段基本信息
     */
    @ApiOperation(value = "修改光缆段基本信息")
    @RequiresPermissions("resources:sgTcdevFibersegB:edit")
    @Log(title = "光缆段基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SgTcdevFibersegBVo sgTcdevFibersegB) {
        return toAjax(sgTcdevFibersegBVoService.updateSgTcdevFibersegB(sgTcdevFibersegB));
    }

    /**
     * 删除光缆段基本信息
     */
    @ApiOperation(value = "删除光缆段基本信息")
    @RequiresPermissions("resources:sgTcdevFibersegB:remove")
    @Log(title = "光缆段基本信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestBody SgTcdevFibersegBVo sgTcdevFibersegB) {
        return toAjax(sgTcdevFibersegBVoService.deleteSgTcdevFibersegB(sgTcdevFibersegB));
    }

    /**
     * 批量删除光缆段基本信息
     */
    @ApiOperation(value = "批量删除光缆段基本信息")
    @RequiresPermissions("resources:sgTcdevFibersegB:remove")
    @Log(title = "光缆段基本信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeAll/{ids}")
    public AjaxResult removeAll(@PathVariable String[] ids) {
        return toAjax(sgTcdevFibersegBVoService.deleteSgTcdevFibersegBByIds(ids));
    }


}
