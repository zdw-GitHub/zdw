package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.entity.TQualityVerificationResult;
import com.mshz.cloud.tms.business.entity.TQualityAssociationRules;
import com.mshz.cloud.tms.business.service.ITQualityAssociationRulesService;
import com.mshz.cloud.tms.business.service.ITQualityVerificationResultService;
import com.mshz.cloud.tms.business.service.ITQualityVerificationRulesService;
import com.mshz.cloud.tms.business.service.ParentRule;
import com.mshz.cloud.tms.business.service.impl.TQualityAssRuleVerificationServiceImpl;
import com.mshz.cloud.tms.business.vo.QualityVerificationRulesVo;
import com.mshz.cloud.tms.business.entity.TQualityAssRuleVerification;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据质量-关联规则Controller
 *
 * @author hzy
 * @date 2022-10-22
 */
@RestController
@RequestMapping("/assrules")
public class TQualityAssociationRulesController extends BaseController
{
    @Autowired
    private ITQualityAssociationRulesService tQualityAssociationRulesService;

    @Autowired
    private TQualityAssRuleVerificationServiceImpl tQualityAssRuleVerificationService;

    @Autowired
    private ITQualityVerificationRulesService tQualityVerificationRulesService;

    @Autowired
    private ITQualityVerificationResultService tQualityVerificationResultService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询数据质量-关联规则列表
     */
   //@RequiresPermissions("quality:assrules:list")
    @GetMapping("/list")
    public TableDataInfo list(TQualityAssociationRules tQualityAssociationRules)
    {
        startPage();
        List<TQualityAssociationRules> list = tQualityAssociationRulesService.selectTQualityAssociationRulesList(tQualityAssociationRules);
        return getDataTable(list);
    }

    /**
     * 导出数据质量-关联规则列表
     */
   //@RequiresPermissions("quality:assrules:export")
   //@Log(title = "数据质量-关联规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TQualityAssociationRules tQualityAssociationRules) throws IOException {
        List<TQualityAssociationRules> list = tQualityAssociationRulesService.selectTQualityAssociationRulesList(tQualityAssociationRules);
        ExcelUtil<TQualityAssociationRules> util = new ExcelUtil<TQualityAssociationRules>(TQualityAssociationRules.class);
        util.exportExcel(response, list, "数据质量-关联规则数据");
    }

    /**
     * 获取数据质量-关联规则详细信息
     */
   //@RequiresPermissions("quality:assrules:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tQualityAssociationRulesService.selectTQualityAssociationRulesById(id));
    }

    /**
     * 新增数据质量-关联规则
     */
   //@RequiresPermissions("quality:assrules:add")
   //@Log(title = "数据质量-关联规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TQualityAssociationRules tQualityAssociationRules)
    {
        return toAjax(tQualityAssociationRulesService.insertTQualityAssociationRules(tQualityAssociationRules));
    }

    /**
     * 修改数据质量-关联规则
     */
   //@RequiresPermissions("quality:assrules:edit")
   //@Log(title = "数据质量-关联规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TQualityAssociationRules tQualityAssociationRules)
    {
        return toAjax(tQualityAssociationRulesService.updateTQualityAssociationRules(tQualityAssociationRules));
    }

    /**
     * 删除数据质量-关联规则
     */
   //@RequiresPermissions("quality:assrules:remove")
   //@Log(title = "数据质量-关联规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tQualityAssociationRulesService.deleteTQualityAssociationRulesByIds(ids));
    }

   //@RequiresPermissions({"quality:assrules:list"})
    @GetMapping({"/assVerification"})
    @ApiOperation("关联规则手动校验")
    public AjaxResult assVerification() {
        List<TQualityVerificationResult> resultListALL = new ArrayList();
        TQualityAssRuleVerification tQualityAssRuleVerification = new TQualityAssRuleVerification();
        tQualityAssRuleVerification.setState("0");
        List<TQualityAssRuleVerification> associationRules = tQualityAssRuleVerificationService.selectTQualityAssRuleVerificationList(tQualityAssRuleVerification);
        try{
            for(int i = 0; i < associationRules.size(); ++i) {
                Long id = associationRules.get(i).getId();
                //通过id获取规则信息
                QualityVerificationRulesVo verificationRule = tQualityVerificationRulesService.selectAssRuleVer(String.valueOf(id));
                //通过规则英文名称 获取规则校验类  经行数据校验
                ParentRule parentRule = (ParentRule)applicationContext.getBean(verificationRule.getEnglishName(), ParentRule.class);
                List<TQualityVerificationResult> resultIsMaps = parentRule.method(verificationRule);
                //添加错误数据到错误集合
                resultListALL.addAll(resultIsMaps);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        List<TQualityVerificationResult> resultList = (List)resultListALL.stream().distinct().collect(Collectors.toList());

        //通过关联规则id+错误数据id+错误字段名称  来判断错误数据是否存在  存在更新  不存在新增
        for (TQualityVerificationResult result:resultList) {
            TQualityVerificationResult verificationResult =   tQualityVerificationResultService.selectTQualityVerificationResultByCondition(result.getVerificationRuleId(),result.getErrorId(),result.getErrorColumnName());
            if(verificationResult!=null){
                result.setId(verificationResult.getId());//
                tQualityVerificationResultService.updateTQualityVerificationResult(result);
            }else {
                tQualityVerificationResultService.insertTQualityVerificationResult(result);
            }
        }

        return AjaxResult.success();
    }
}
