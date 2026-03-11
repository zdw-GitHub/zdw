package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaModelCategory;
import com.mshz.cloud.tms.business.service.TMetaModelCategoryService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 元数据类目Controller
 *
 * @author zdw
 * @date 2022-09-23
 */
@RestController
@RequestMapping("/category")
@Api(value = "tMetaModelCategory", tags = {"tMetaModelCategory"}, description = "元数据类目")
public class TMetaModelCategoryController extends BaseController
{
    @Autowired
    private TMetaModelCategoryService tMetaModelCategoryService;

        /**
         * 查询元数据类目列表
         */
   //@RequiresPermissions("mm:category:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaModelCategory tMetaModelCategory)
    {
        startPage();
        List<TMetaModelCategory> list = tMetaModelCategoryService.selectTMetaModelCategoryList(tMetaModelCategory);
        return getDataTable(list);
    }

    /**
     * 获取元数据类目详细信息
     */
   //@RequiresPermissions("mm:category:query")
    @GetMapping
    public AjaxResult getAllHaveOther()
    {
        List<TMetaModelCategory> list = tMetaModelCategoryService.selectTMetaModelCategoryList(null);
        TMetaModelCategory other = new TMetaModelCategory();
        other.setCategoryNameCn("其他");
        other.setCategoryName("Other");
        list.add(other);
        return AjaxResult.success(list);
    }

    /**
     * 获取元数据类目详细信息
     */
   //@RequiresPermissions("mm:category:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaModelCategoryService.selectTMetaModelCategoryById(id));
    }

    /**
     * 新增元数据类目
     */
   //@RequiresPermissions("mm:category:add")
    //@Log(title = "元数据类目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaModelCategory tMetaModelCategory)
    {
        tMetaModelCategory.setCreateDate(DateUtil.now());
        tMetaModelCategory.setId(IdUtil.getSnowflakeNextId());//
        return toAjax(tMetaModelCategoryService.insertTMetaModelCategory(tMetaModelCategory));
    }

    /**
     * 修改元数据类目
     */
   //@RequiresPermissions("mm:category:edit")
    //@Log(title = "元数据类目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaModelCategory tMetaModelCategory)
    {
        return toAjax(tMetaModelCategoryService.updateTMetaModelCategory(tMetaModelCategory));
    }

    /**
     * 删除元数据类目
     */
   //@RequiresPermissions("mm:category:remove")
    //@Log(title = "元数据类目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaModelCategoryService.deleteTMetaModelCategoryByIds(ids));
    }
}
