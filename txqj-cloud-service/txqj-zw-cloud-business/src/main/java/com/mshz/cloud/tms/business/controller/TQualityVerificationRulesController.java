package com.mshz.cloud.tms.business.controller;
import com.github.pagehelper.util.StringUtil;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.entity.TQualityVerificationRules;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.service.ITQualityVerificationResultService;
import com.mshz.cloud.tms.business.service.ITQualityVerificationRulesService;
import com.mshz.cloud.tms.business.service.ParentRule;
import com.mshz.cloud.tms.business.util.AtrrNameUtils;
import com.mshz.cloud.tms.business.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据质量-基础校验规则Controller
 *
 * @author hzy
 * @date 2022-10-22
 */
@RestController
@RequestMapping("/verrules")
@Api(description = "数据质量-基础校验规则")
public class TQualityVerificationRulesController extends BaseController {
    @Autowired
    private ITQualityVerificationRulesService tQualityVerificationRulesService;
    @Autowired
    private ITQualityVerificationResultService tQualityVerificationResultService;
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 查询数据质量-基础校验规则列表
     */
   //@RequiresPermissions("quality:verrules:list")
    @GetMapping("/list")
    public TableDataInfo list(TQualityVerificationRules tQualityVerificationRules) {
        startPage();
        List<TQualityVerificationRules> list = tQualityVerificationRulesService.selectTQualityVerificationRulesList(tQualityVerificationRules);
        return getDataTable(list);
    }

    /**
     * 查询数据质量-校验规则列表
     */
    @RequiresPermissions("mm:verificationRules:listVo")
    @GetMapping("/listVo")
    public TableDataInfo list(QualityVerificationRulesVo tQualityVerificationRulesVo)
    {
        if("".equals(tQualityVerificationRulesVo.getModelVersion())||tQualityVerificationRulesVo.getModelVersion() == null){
            tQualityVerificationRulesVo.setModelVersion("edit");
        }
        if(tQualityVerificationRulesVo.getMetaModeId()==null||"".equals(tQualityVerificationRulesVo.getMetaModeId())){
            List<QualityVerificationRulesVo> list = new ArrayList<>();
            return getDataTable(list);
        }
        startPage();
        List<QualityVerificationRulesVo> list = tQualityVerificationRulesService.selectQualityVerificationRulesListVo(tQualityVerificationRulesVo);
        return getDataTable(list);
    }

    /**
     * 导出数据质量-基础校验规则列表
     */
   //@RequiresPermissions("quality:verrules:export")
   //@Log(title = "数据质量-基础校验规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TQualityVerificationRules tQualityVerificationRules) throws IOException {
        List<TQualityVerificationRules> list = tQualityVerificationRulesService.selectTQualityVerificationRulesList(tQualityVerificationRules);
        ExcelUtil<TQualityVerificationRules> util = new ExcelUtil<TQualityVerificationRules>(TQualityVerificationRules.class);
        util.exportExcel(response, list, "数据质量-基础校验规则数据");
    }

    /**
     * 获取数据质量-基础校验规则详细信息
     */
   //@RequiresPermissions("quality:verrules:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tQualityVerificationRulesService.selectTQualityVerificationRulesById(id));
    }

    /**
     * 新增数据质量-基础校验规则
     */
   //@RequiresPermissions("quality:verrules:add")
   //@Log(title = "数据质量-基础校验规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQualityVerificationRules tQualityVerificationRules) {
        return toAjax(tQualityVerificationRulesService.insertTQualityVerificationRules(tQualityVerificationRules));
    }

    /**
     * 修改数据质量-基础校验规则
     */
   //@RequiresPermissions("quality:verrules:edit")
   //@Log(title = "数据质量-基础校验规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQualityVerificationRules tQualityVerificationRules) {
        return toAjax(tQualityVerificationRulesService.updateTQualityVerificationRules(tQualityVerificationRules));
    }

    /**
     * 删除数据质量-基础校验规则
     */
   //@RequiresPermissions("quality:verrules:remove")
   //@Log(title = "数据质量-基础校验规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tQualityVerificationRulesService.deleteTQualityVerificationRulesByIds(ids));
    }

   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/manualVerification"})
    @ApiOperation("基础规则手动校验")
    public AjaxResult manualVerification() {
        List<TQualityVerificationResult> resultListALL = new ArrayList();
        TQualityVerificationRules verificationRules = new TQualityVerificationRules();
        verificationRules.setState("0");
        List<TQualityVerificationRules> rules = tQualityVerificationRulesService.selectTQualityVerificationRulesList(verificationRules);
        try {
            for (int i = 0; i < rules.size(); ++i) {
                Long id = rules.get(i).getId();
                // 通过规则id获取规则信息
                QualityVerificationRulesVo verificationRule = tQualityVerificationRulesService.selectVerificationRules(String.valueOf(id));
                // 通过规则英文名称使用相对应校验接口
                ParentRule parentRule = (ParentRule)applicationContext.getBean(verificationRule.getEnglishName(), ParentRule.class);
                List<TQualityVerificationResult> resultIsMaps = parentRule.method(verificationRule);
                resultListALL.addAll(resultIsMaps);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TQualityVerificationResult> resultList = (List)resultListALL.stream().distinct().collect(Collectors.toList());

        for (TQualityVerificationResult result : resultList) {
            // 待修改加 判断是修改还是新增判断
            TQualityVerificationResult verificationResult =
                tQualityVerificationResultService.selectTQualityVerificationResultByCondition(result.getVerificationRuleId(), result.getErrorId(), result.getErrorColumnName());
            if (verificationResult != null) {
                result.setId(verificationResult.getId());//
                tQualityVerificationResultService.updateTQualityVerificationResult(result);
            } else {
                tQualityVerificationResultService.insertTQualityVerificationResult(result);
            }
        }

        return AjaxResult.success();
    }

   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/selectByModeIdVersion"})
    @ApiOperation("通信资源展示维护-规则-修改")
    public AjaxResult selectByModeIdVersion(String siteId, String modeVersion) {
        List<TQualityVerificationResult> resultList = tQualityVerificationRulesService.selectByTableIdVersion1(siteId, modeVersion);
        return AjaxResult.success(resultList);
    }

   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/selectByModeIdVersionforCreate"})
    @ApiOperation("通信资源展示维护-规则-新增")
    public AjaxResult selectByModeIdVersionforCreate(String modelId, String modeVersion) {
        List<TQualityVerificationResult> resultList = tQualityVerificationRulesService.selectByTableIdVersionForCreate(modelId, modeVersion);
        return AjaxResult.success(resultList);
    }

   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/tableDropDownBox"})
    @ApiOperation("某表下拉框")
    public AjaxResult tableDropDownBox(String modelId, String modeVersion) {
        // 获取规则具体信息
        List<TableRuleVo> resultInputList = tQualityVerificationRulesService.selectByTableIdVersion(modelId, modeVersion, null);
        List<DropDownVo> dropDownVos = new ArrayList<>();

        // 通过规则信息取得字典范围
        for (TableRuleVo tableRuleVo : resultInputList) {
            List<DictionariesVo> dictionariesVos = new ArrayList<>();
            // 根据选择类型来获取不同方式字典值
            if (tableRuleVo.getSelectType() != null && !tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                List<Map<String, Object>> mapList = tQualityVerificationRulesService.useDefinedVerSql(tableRuleVo.getCustomizedSql());
                for (Map<String, Object> map : mapList) {
                    DictionariesVo dictionariesVo = new DictionariesVo();
                    dictionariesVo.setCode((String)map.get("code"));
                    dictionariesVo.setName((String)map.get("name"));
                    dictionariesVos.add(dictionariesVo);
                }
            } else if (tableRuleVo.getSelectType() != null && tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                List<Map<String, String>> mapList = tQualityVerificationRulesService.selectDictionaries(
                    tableRuleVo.getTableName(), tableRuleVo.getColumnIdName(), tableRuleVo.getColumnName());
                for (Map<String, String> map : mapList) {
                    DictionariesVo dictionariesVo = new DictionariesVo();
                    if (map != null) {
                        dictionariesVo.setCode(String.valueOf(map.get("code")));
                        dictionariesVo.setName(map.get("name"));
                    }
                    dictionariesVos.add(dictionariesVo);
                }
            }
            if (!dictionariesVos.isEmpty()) {
                DropDownVo dropDownVo = new DropDownVo();
                dropDownVo.setName(AtrrNameUtils.camel(tableRuleVo.getRuleColumnName().toLowerCase()));
                dropDownVo.setList(dictionariesVos);
                dropDownVos.add(dropDownVo);
            }
        }
        AjaxResult ajaxresult = AjaxResult.success(dropDownVos);
        return ajaxresult;
    }

    /***
     * 根据具体表属性名称获取该属性的指定数量下拉框内容
     *
     * @param modelId 表id
     * @param modeVersion 表版本
     * @param attribute 属性名
     * @param code 属性值
     * @param name 属性值转义名
     * @param pageNum 分页起始数字
     * @param pageSize 分页大小
     * @param orderBy 排序字符
     * @return
     */
   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/tableDropDownBoxForOne"})
    @ApiOperation("某表某属性下拉框")
    public TableDataInfo tableDropDownBoxForOne(String modelId, String modeVersion, String attribute, String code, String name, Integer pageNum, Integer pageSize, String orderBy) {
        name = name != null ? name.replace(" ", "%") : name ;
        startPage();
        // 处理分页数据
        if (pageNum == null || pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (orderBy == null) {
            orderBy = "name";
        }

        List<DictionariesVo> dictionariesVos = new ArrayList<>();
        int totle = 0;
        if (!StringUtil.isEmpty(attribute)) {
            // 获取规则具体信息
            List<TableRuleVo> resultInputList = tQualityVerificationRulesService.selectByTableIdVersion(modelId, modeVersion, null, attribute);
            if (resultInputList != null && resultInputList.size() > 0) {
                // 通过规则信息取得字典范围
                TableRuleVo tableRuleVo = resultInputList.get(0);
                // 根据选择类型来获取不同方式字典值
                if (tableRuleVo.getSelectType() != null && !tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                    // 通过自定义sql查询翻译字段
                    if (tableRuleVo.getCustomizedSql() != null && tableRuleVo.getCustomizedSql().length() > 0) {
                        String sql = tableRuleVo.getCustomizedSql();
                        List<Map<String, Object>> mapList = tQualityVerificationRulesService.useDefinedVerSqlForLIMIT(sql, code, name, pageNum, pageSize, orderBy);
                        totle = tQualityVerificationRulesService.useDefinedVerSqlForLIMITCount(sql, code, name);
                        for (Map<String, Object> map : mapList) {
                            DictionariesVo dictionariesVo = new DictionariesVo();
                            dictionariesVo.setCode(String.valueOf(map.get("code")));
                            dictionariesVo.setName(String.valueOf(map.get("name")));
                            dictionariesVos.add(dictionariesVo);
                        }
                    }
                } else if (tableRuleVo.getSelectType() != null && tableRuleVo.getSelectType().equals("2") && tableRuleVo.getEnglishName().equals("DicRangeVerification")) {
                    // 通过字典表获取翻译
                    List<Map<String, String>> mapList =
                        tQualityVerificationRulesService.selectDictionariesForLIMIT(tableRuleVo.getTableName(),
                            tableRuleVo.getColumnIdName(), tableRuleVo.getColumnName(), code, name, pageNum, pageSize, orderBy, null);
                    totle = tQualityVerificationRulesService.selectDictionariesForLIMITCount(tableRuleVo.getTableName(),
                        tableRuleVo.getColumnIdName(), tableRuleVo.getColumnName(), code, name, null);



                    List<Map> manageDeptList = new ArrayList<>();
                    if (mapList.size() > 0){
                        List<String> codeList = mapList.stream().map(m -> String.valueOf(m.get("code"))).collect(Collectors.toList());
                        //站点添加维护单位
                        if("SG_TCCON_TCSITE_B".equals(tableRuleVo.getTableName())){
                            manageDeptList = tQualityVerificationRulesService.selectSiteMaintenanceUnit(codeList);
                        }
                        //接续装置添加所属通信站
                        if("SG_TCCON_FBRCONNECT_B".equals(tableRuleVo.getTableName())){
                            manageDeptList = tQualityVerificationRulesService.selectConnectParSite(codeList);
                        }
                    }


                    for (Map<String, String> map : mapList) {
                        DictionariesVo dictionariesVo = new DictionariesVo();
                        if (map != null) {
                            String id =  String.valueOf(map.get("code"));
                            dictionariesVo.setCode(id);
                            dictionariesVo.setName(map.get("name"));

                            Map manageDep = manageDeptList.stream().filter(f -> Objects.equals(id, f.get("id"))).findFirst().orElse(null);
                            if (manageDep != null){
                                dictionariesVo.setManageDept(String.valueOf(manageDep.get("name")));
                            }
                        }
                        dictionariesVos.add(dictionariesVo);
                    }
                }
            }
        }

        TableDataInfo rspData = getDataTable(dictionariesVos);
        rspData.setTotal(totle);
        return rspData;
    }


   //@RequiresPermissions({"quality:verrules:list"})
    @GetMapping({"/singleAttributeVerification"})
    @ApiOperation("新增修改属性校验")
    public AjaxResult singleAttributeVerification(OneCommRule oneCommRule) {
        // 根据 元模型id，技术员模型id 获取 规则信息
        List<QualityVerificationRulesVo> verificationRule = tQualityVerificationRulesService.selectVerificationRuleOne(oneCommRule);
        List<TQualityVerificationResult> verificationResults = new ArrayList<>();
        if (verificationRule != null && verificationRule.size() > 0) {
            for (QualityVerificationRulesVo rulesVo : verificationRule) {
                try {
                    if (rulesVo != null) {
                        // 通过规则英文名称使用相对应校验接口
                        ParentRule parentRule = (ParentRule)applicationContext.getBean(rulesVo.getEnglishName(), ParentRule.class);
                        List<TQualityVerificationResult> resultIsMaps = parentRule.method(oneCommRule, rulesVo);
                        for (TQualityVerificationResult qualityVerificationResult : resultIsMaps) {
                            verificationResults.add(qualityVerificationResult);
                        }
                    }
                } catch (Exception e) {
                    logger.info("校验异常：{}", e);
                }

            }
        }
        return AjaxResult.success(verificationResults);

    }


    /**
     * 查询数据质量-校验规则列表
     */
    @RequiresPermissions("mm:verificationRules:symbolList")
    @GetMapping("/symbolList")
    @ApiOperation(value = "符号列表")
    public TableDataInfo symbolList(String englishName)
    {
        List<SelectVo> list = tQualityVerificationRulesService.selectSymbolList(englishName);
        return getDataTable(list);
    }

}
