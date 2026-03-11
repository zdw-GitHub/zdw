package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaModelIdConversionLog;
import com.mshz.cloud.tms.business.vo.TMetaModelIdConversionLogParaVo;
import com.mshz.cloud.tms.business.service.ITMetaModelIdConversionLogService;
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
 * id中转换日志Controller
 *
 * @author zdw
 * @date 2023-08-09
 */
@RestController
@RequestMapping("/idConversionLog")
public class TMetaModelIdConversionLogController extends BaseController
{
    @Autowired
    private ITMetaModelIdConversionLogService tMetaModelIdConversionLogService;

    /**
     * 查询id中转换日志列表
     */
   //@RequiresPermissions("metadata:idconversionlog:list")
    @GetMapping("/list")
    public TableDataInfo list(TMetaModelIdConversionLogParaVo tMetaModelIdConversionLog)
    {
        startPage();
        List<TMetaModelIdConversionLog> list = tMetaModelIdConversionLogService.selectTMetaModelIdConversionLogList(tMetaModelIdConversionLog);
        return getDataTable(list);
    }

    /**
     * 导出id中转换日志列表
     */
   //@RequiresPermissions("metadata:idconversionlog:export")
    //@Log(title = "id中转换日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMetaModelIdConversionLogParaVo tMetaModelIdConversionLog) throws IOException {
        List<TMetaModelIdConversionLog> list = tMetaModelIdConversionLogService.selectTMetaModelIdConversionLogList(tMetaModelIdConversionLog);
        ExcelUtil<TMetaModelIdConversionLog> util = new ExcelUtil<>(TMetaModelIdConversionLog.class);
        util.exportExcel(response, list, "id中转换日志数据");
    }

    /**
     * 获取id中转换日志详细信息
     */
   //@RequiresPermissions("metadata:idconversionlog:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tMetaModelIdConversionLogService.selectTMetaModelIdConversionLogById(id));
    }

    /**
     * 新增id中转换日志
     */
   //@RequiresPermissions("metadata:idconversionlog:add")
    //@Log(title = "id中转换日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMetaModelIdConversionLog tMetaModelIdConversionLog)
    {
        return toAjax(tMetaModelIdConversionLogService.insertTMetaModelIdConversionLog(tMetaModelIdConversionLog));
    }

    /**
     * 修改id中转换日志
     */
   //@RequiresPermissions("metadata:idconversionlog:edit")
    //@Log(title = "id中转换日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMetaModelIdConversionLog tMetaModelIdConversionLog)
    {
        return toAjax(tMetaModelIdConversionLogService.updateTMetaModelIdConversionLog(tMetaModelIdConversionLog));
    }

    /**
     * 删除id中转换日志
     */
   //@RequiresPermissions("metadata:idconversionlog:remove")
    //@Log(title = "id中转换日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMetaModelIdConversionLogService.deleteTMetaModelIdConversionLogByIds(ids));
    }
}
