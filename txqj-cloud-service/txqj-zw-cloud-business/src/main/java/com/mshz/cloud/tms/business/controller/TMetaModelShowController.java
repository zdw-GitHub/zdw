package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.service.TMetaModelShowService;
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
 * 展示配置Controller
 *
 * @author zdw
 * @date 2022-09-23
 */
@RestController
@RequestMapping("/show")
@Api(value = "tMetaModelShow", tags = {"tMetaModelShow"}, description = "展示配置")
public class TMetaModelShowController extends BaseController {
    @Autowired
    private TMetaModelShowService tMetaModelShowService;

    /**
     * 查询展示配置列表
     */
   //@RequiresPermissions("mm:show:listwithcolumninfo")
    @GetMapping("/listwithcolumninfo")
    public TableDataInfo listWithColumnInfo(MetaModelShowVo metaModelShowVo) {
        startPage();
        List<MetaModelShowVo> list = tMetaModelShowService.selectMetaModelShowVoList(metaModelShowVo);
        return getDataTable(list);
    }

    /**
     * 查询展示配置列表新
     */
   //@RequiresPermissions("mm:show:listwithcolumninfo")
    @GetMapping("/listwithcolumninfonew")
    public TableDataInfo listWithColumnInfoNew(MetaModelShowVo metaModelShowVo) {
        startPage();
        List<MetaModelShowVo> list = tMetaModelShowService.selectMetaModelShowVoListNew(metaModelShowVo);
        return getDataTable(list);
    }
    /**
     * 查询展示配置基本信息
     */
   //@RequiresPermissions("mm:show:listwithcolumninfo")
    @GetMapping("/allocationBasicInformation")
    public AjaxResult allocationBasicInformation(String modelId) {
        return AjaxResult.success(tMetaModelShowService.selectBasicInformation(modelId));
    }


   //@RequiresPermissions("mm:show:listwithcolumninfo")
    @GetMapping("/listwithcolumninfoQuery")
    public AjaxResult listwithcolumninfoQuery(MetaModelShowVo metaModelShowVo) {
        List<MetaModelShowVo> list = tMetaModelShowService.selectMetaModelShowVoList(metaModelShowVo);
        return AjaxResult.success(list);
    }

    /**
     * 查询展示配置列表
     */
   //@RequiresPermissions("mm:show:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaModelShow tMetaModelShow) {
        startPage();
        List<TMetaModelShow> list = tMetaModelShowService.selectTMetaModelShowList(tMetaModelShow);
        return getDataTable(list);
    }

    /**
     * 导出展示配置列表
     */
   //@RequiresPermissions("mm:show:export")
    //@Log(title = "展示配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaModelShow tMetaModelShow) throws IOException {
        List<TMetaModelShow> list = tMetaModelShowService.selectTMetaModelShowList(tMetaModelShow);
        ExcelUtil<TMetaModelShow> util = new ExcelUtil<TMetaModelShow>(TMetaModelShow.class);
        util.exportExcel(response, list, "展示配置数据");
    }

    /**
     * 获取展示配置详细信息
     */
   //@RequiresPermissions("mm:show:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tMetaModelShowService.selectTMetaModelShowById(id));
    }

    /**
     * 新增展示配置
     */
   //@RequiresPermissions("mm:show:add")
    //@Log(title = "展示配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaModelShow tMetaModelShow) {
        return toAjax(tMetaModelShowService.insertTMetaModelShow(tMetaModelShow));
    }

    /**
     * 修改展示配置
     */
   //@RequiresPermissions("mm:show:edit")
    //@Log(title = "展示配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaModelShow tMetaModelShow) {
        return AjaxResult.success(tMetaModelShowService.updateTMetaModelShow(tMetaModelShow));
    }

    /**
     * 删除展示配置
     */
   //@RequiresPermissions("mm:show:remove")
    //@Log(title = "展示配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tMetaModelShowService.deleteTMetaModelShowByIds(ids));
    }

}
