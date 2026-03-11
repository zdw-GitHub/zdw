package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.service.ITQualityVerificationResultService;
import com.mshz.cloud.tms.business.service.ITQualityVerificationRulesService;
import com.mshz.cloud.tms.business.vo.CheckResultVo;
import com.mshz.cloud.tms.business.vo.DictionariesVo;
import com.mshz.cloud.tms.business.vo.GuideVo;
import com.mshz.cloud.tms.business.vo.TableRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据质量-校验结果Controller
 *
 * @author hzy
 * @date 2022-10-14
 */
@RestController
@RequestMapping("/result")
@Api(value = "result", tags = {"result"}, description = "校验结果")
public class TQualityVerificationResultController extends BaseController {
    @Autowired
    private ITQualityVerificationResultService tQualityVerificationResultService;

    @Autowired
    private ITQualityVerificationRulesService itQualityVerificationRulesService;

    /**
     * 查询数据质量-校验结果列表
     */
   //@RequiresPermissions("quality:result:list")
    @GetMapping("/list")
    public TableDataInfo list(TQualityVerificationResult tQualityVerificationResult) {
        startPage();
        List<TQualityVerificationResult> list = tQualityVerificationResultService.selectTQualityVerificationResultList(tQualityVerificationResult);
        return getDataTable(list);
    }

   //@RequiresPermissions("quality:result:list")
    @GetMapping("/downDrop")
    public AjaxResult downDrop() {
        List<DictionariesVo> list = tQualityVerificationResultService.queryRes();
        return AjaxResult.success(list);
    }

    /**
     * 导出数据质量-校验结果列表
     */
   //@RequiresPermissions("quality:result:export")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TQualityVerificationResult tQualityVerificationResult) throws IOException {
        List<TQualityVerificationResult> list = tQualityVerificationResultService.selectTQualityVerificationResultList(tQualityVerificationResult);
        ExcelUtil<TQualityVerificationResult> util = new ExcelUtil<TQualityVerificationResult>(TQualityVerificationResult.class);
        util.exportExcel(response, list, "数据质量-校验结果数据");
    }

    /**
     * 获取数据质量-校验结果详细信息
     */
   //@RequiresPermissions("quality:result:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tQualityVerificationResultService.selectTQualityVerificationResultById(id));
    }

    /**
     * 新增数据质量-校验结果
     */
   //@RequiresPermissions("quality:result:add")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQualityVerificationResult tQualityVerificationResult) {
        TQualityVerificationResult verificationResult = tQualityVerificationResultService.selectTQualityVerificationResultByCondition(
            tQualityVerificationResult.getVerificationRuleId(), tQualityVerificationResult.getErrorId(), tQualityVerificationResult.getErrorColumnName());
        if (verificationResult != null) {
            tQualityVerificationResult.setId(verificationResult.getId());//
            tQualityVerificationResultService.updateTQualityVerificationResult(tQualityVerificationResult);
        } else {
            tQualityVerificationResultService.insertTQualityVerificationResult(tQualityVerificationResult);
        }
        return AjaxResult.success();
    }

    /**
     * 新增数据质量-校验结果
     */
   //@RequiresPermissions("quality:result:add")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.INSERT)
    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody List<TQualityVerificationResult> tQualityVerificationResults) {
        for (TQualityVerificationResult tQualityVerificationResult : tQualityVerificationResults) {
            tQualityVerificationResultService.insertTQualityVerificationResult(tQualityVerificationResult);
        }
        return AjaxResult.success();
    }

    /**
     * 修改更新错误数据
     */
   //@RequiresPermissions("quality:result:add")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.INSERT)
    @PostMapping(value = "/createToUpdateRule")
    public AjaxResult createToUpdateRule(@RequestBody List<TQualityVerificationResult> tQualityVerificationResults) {
        if (tQualityVerificationResults != null && tQualityVerificationResults.size() != 0) {
            tQualityVerificationResultService.deleteTQualityVerificationResultBySiteId(tQualityVerificationResults.get(0).getErrorId());
            for (TQualityVerificationResult tQualityVerificationResult : tQualityVerificationResults) {
                tQualityVerificationResultService.insertTQualityVerificationResult(tQualityVerificationResult);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 引导删除错误数据
     */
   //@RequiresPermissions("quality:result:remove")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.INSERT)
    @PostMapping(value = "/deleteRule")
    public AjaxResult deleteRule(String[] names, String siteId) {
        for (String name : names) {
            tQualityVerificationResultService.deleteTQualityVerificationResultBySiteIdName(siteId, name);
        }
        return AjaxResult.success();
    }

    /**
     * 修改数据质量-校验结果
     */
   //@RequiresPermissions("quality:result:edit")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQualityVerificationResult tQualityVerificationResult) {
        return toAjax(tQualityVerificationResultService.updateTQualityVerificationResult(tQualityVerificationResult));
    }

    /**
     * 删除数据质量-校验结果
     */
   //@RequiresPermissions("quality:result:remove")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tQualityVerificationResultService.deleteTQualityVerificationResultByIds(ids));
    }

   //@RequiresPermissions("quality:result:remove")
   //@Log(title = "数据质量-校验结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeResult")
    public AjaxResult removeResult(String siteId) {
        tQualityVerificationResultService.deleteTQualityVerificationResultBySiteId(siteId);
        return AjaxResult.success();
    }

   //@RequiresPermissions("quality:result:list")
    @GetMapping("/CheckResult")
    @ApiOperation("问题数据展示")
    public TableDataInfo CheckResult(String siteName, String ownerId, String bRuleId, String type, String columnName, String modelId) {
        startPage();
        List<CheckResultVo> list = tQualityVerificationResultService.selectCheckResult(siteName, ownerId, bRuleId, type, columnName, modelId);
        return getDataTable(list);
    }

   //@RequiresPermissions("quality:result:list")
    @GetMapping("/selectCompany")
    @ApiOperation("维护单位")
    public AjaxResult selectCompany(String name) {
        List<DictionariesVo> list = tQualityVerificationResultService.selectCompany(name);
        return AjaxResult.success(list);
    }

   //@RequiresPermissions("quality:result:list")
    @GetMapping("/selectType")
    @ApiOperation("问题种类")
    public AjaxResult selectType() {
        List<DictionariesVo> list = tQualityVerificationResultService.selectType();
        return AjaxResult.success(list);
    }

   //@RequiresPermissions("quality:result:list")
    @GetMapping("/guide")
    @ApiOperation("引导")
    public TableDataInfo guide(String siteId) {
        startPage2();
        // 通过站点id获取站点错误数据
        List<GuideVo> list = itQualityVerificationRulesService.selectGuide(siteId);
        for (GuideVo guideVo : list) {
            // 判断错误数据是否属于 字典规则 范畴
            if (guideVo.getEnglishName().indexOf("DicRangeVerification") != -1) {
                guideVo.setType("select");
                String[] ruleIds = guideVo.getRuleId().split(":");
                String[] vRuleIds = guideVo.getVruleId().split(":");
                String[] englishNames = guideVo.getEnglishName().split(":");
                String ruleId = null;
                for (int i = 0; i < ruleIds.length - 1; i++) {
                    String id = ruleIds[i];
                    String englishName = englishNames[i];
                    // 是字典规则范畴 为规则id赋值
                    if (englishName.equals("DicRangeVerification")) {
                        ruleId = vRuleIds[i];
                    }
                }

                // 获取规则具体信息
                List<TableRuleVo> resultList = itQualityVerificationRulesService.selectByTableIdVersion(null, null, ruleId);

                // 通过规则信息取得字典范围
                for (TableRuleVo tableRuleVo : resultList) {
                    List<DictionariesVo> dictionariesVos = new ArrayList<>();
                    // 根据选择类型来获取不同方式字典值
                    if (tableRuleVo.getSelectType() != null && !tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                        List<Map<String, Object>> mapList = itQualityVerificationRulesService.useDefinedVerSql(tableRuleVo.getCustomizedSql());
                        for (Map<String, Object> map : mapList) {
                            DictionariesVo dictionariesVo = new DictionariesVo();
                            dictionariesVo.setCode((String)map.get("code"));
                            dictionariesVo.setName((String)map.get("name"));
                            dictionariesVos.add(dictionariesVo);
                        }
                    } else if (tableRuleVo.getSelectType() != null && tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                        List<Map<String, String>> mapList = itQualityVerificationRulesService.selectDictionaries(
                            tableRuleVo.getTableName(), tableRuleVo.getColumnIdName(), tableRuleVo.getColumnName());
                        for (Map<String, String> map : mapList) {
                            DictionariesVo dictionariesVo = new DictionariesVo();
                            dictionariesVo.setCode(map.get("code"));
                            dictionariesVo.setName(map.get("name"));
                            dictionariesVos.add(dictionariesVo);
                        }
                    }
                    // 为 错误数据 的范围赋值
                    guideVo.setList(dictionariesVos);
                }
            }
        }
        return getDataTable(list);
    }
}
