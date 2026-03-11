package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.entity.TQualityAssRuleVerification;
import com.mshz.cloud.tms.business.service.ITQualityAssRuleVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据质量-关联规则校验Controller
 *
 * @author hzy
 * @date 2022-10-22
 */
@RestController
@RequestMapping("/verification")
public class TQualityAssRuleVerificationController extends BaseController
{
    @Autowired
    private ITQualityAssRuleVerificationService tQualityAssRuleVerificationService;

    /**
     * 查询数据质量-关联规则校验列表
     */
   //@RequiresPermissions("quality:verification:list")
    @GetMapping("/list")
    public TableDataInfo list(TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        startPage();
        List<TQualityAssRuleVerification> list = tQualityAssRuleVerificationService.selectTQualityAssRuleVerificationList(tQualityAssRuleVerification);
        return getDataTable(list);
    }

    /**
     * 导出数据质量-关联规则校验列表
     */
   //@RequiresPermissions("quality:verification:export")
   //@Log(title = "数据质量-关联规则校验", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TQualityAssRuleVerification tQualityAssRuleVerification) throws IOException {
        List<TQualityAssRuleVerification> list = tQualityAssRuleVerificationService.selectTQualityAssRuleVerificationList(tQualityAssRuleVerification);
        ExcelUtil<TQualityAssRuleVerification> util = new ExcelUtil<TQualityAssRuleVerification>(TQualityAssRuleVerification.class);
        util.exportExcel(response, list, "数据质量-关联规则校验数据");
    }

    /**
     * 获取数据质量-关联规则校验详细信息
     */
   //@RequiresPermissions("quality:verification:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tQualityAssRuleVerificationService.selectTQualityAssRuleVerificationById(id));
    }

    /**
     * 新增数据质量-关联规则校验
     */
   //@RequiresPermissions("quality:verification:add")
   //@Log(title = "数据质量-关联规则校验", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        return toAjax(tQualityAssRuleVerificationService.insertTQualityAssRuleVerification(tQualityAssRuleVerification));
    }

    /**
     * 修改数据质量-关联规则校验
     */
   //@RequiresPermissions("quality:verification:edit")
   //@Log(title = "数据质量-关联规则校验", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQualityAssRuleVerification tQualityAssRuleVerification)
    {
        return toAjax(tQualityAssRuleVerificationService.updateTQualityAssRuleVerification(tQualityAssRuleVerification));
    }

    /**
     * 删除数据质量-关联规则校验
     */
   //@RequiresPermissions("quality:verification:remove")
   //@Log(title = "数据质量-关联规则校验", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tQualityAssRuleVerificationService.deleteTQualityAssRuleVerificationByIds(ids));
    }
}
