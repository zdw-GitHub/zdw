package com.mshz.cloud.tms.business.controller;

import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import com.mshz.cloud.tms.business.vo.ModelRuleConvertVo;
import com.mshz.cloud.tms.business.vo.ModelRuleParamVo;
import com.mshz.cloud.tms.business.vo.ModelRuleVo;
import com.mshz.cloud.tms.business.entity.MetaModelRule;
import com.mshz.cloud.tms.business.entity.MetaModelRuleParam;
import com.mshz.cloud.tms.business.service.IMetaModelRuleParamService;
import com.mshz.cloud.tms.business.service.IMetaModelRuleService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 元模型字段规则Controller
 *
 * @author zdw
 * @date 2023-08-17
 */
@RestController
@RequestMapping("/rule")
@Api(value = "rule", tags = {"rule"}, description = "元模型字段数据规则")
public class MetaModelRuleController extends BaseController {

    @Resource
    private IMetaModelRuleService metaModelRuleService;

    @Resource
    private IMetaModelRuleParamService metaModelRuleParamService;

    /**
     * 查询元模型字段规则列表
     */
   //@RequiresPermissions("metadata:rule:list")
    @GetMapping("/list")
    public TableDataInfo list(MetaModelRule metaModelRule)
    {
        startPage();
        List<MetaModelRule> list = metaModelRuleService.selectMetaModelRuleList(metaModelRule);
        return getDataTable(list);
    }

    /**
     * 导出元模型字段规则列表
     */
   //@RequiresPermissions("metadata:rule:export")
    //@Log(title = "元模型字段规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MetaModelRule metaModelRule)
    {
        List<MetaModelRule> list = metaModelRuleService.selectMetaModelRuleList(metaModelRule);
        ExcelUtil<MetaModelRule> util = new ExcelUtil<MetaModelRule>(MetaModelRule.class);
        try {
            util.exportExcel(response, list, "元模型字段规则数据");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取元模型字段规则详细信息
     */
   //@RequiresPermissions("metadata:rule:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(metaModelRuleService.selectMetaModelRuleById(id));
    }

    /**
     * 新增元模型字段规则
     */
   //@RequiresPermissions("metadata:rule:add")
    //@Log(title = "元模型字段规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MetaModelRule metaModelRule)
    {
        return toAjax(metaModelRuleService.insertMetaModelRule(metaModelRule));
    }

    /**
     * 修改元模型字段规则
     */
   //@RequiresPermissions("metadata:rule:edit")
    //@Log(title = "元模型字段规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MetaModelRule metaModelRule)
    {
        return toAjax(metaModelRuleService.updateMetaModelRule(metaModelRule));
    }

    /**
     * 删除元模型字段规则
     */
   //@RequiresPermissions("metadata:rule:remove")
    //@Log(title = "元模型字段规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(metaModelRuleService.deleteMetaModelRuleByIds(ids));
    }

    /**
     * 查询元模型字段规则信息列表
     */
   //@RequiresPermissions("metadata:rule:list")
    @GetMapping("/getModelRuleList")
    public TableDataInfo getModelRuleList(MetaModelRule metaModelRule) {
        startPage();
        List<ModelRuleVo> list = metaModelRuleService.getModelRuleList(metaModelRule);
        return getDataTable(list);
    }

    /**
     * 获取元模型字段规则的详细信息
     */
   //@RequiresPermissions("metadata:rule:query")
    @GetMapping(value = "/getModelRuleInfo")
    public AjaxResult getModelRuleInfo(ModelRuleVo ruleVo)  {
        ModelRuleVo resultVo = new ModelRuleVo();
        List<ModelRuleParamVo> ruleParamVoList = new ArrayList<>();

        MetaModelRule ruleInfo = metaModelRuleService.selectMetaModelRuleById(ruleVo.getRuleId());
        ruleVo = ModelConvertUtil.modelConvert(ruleInfo, ModelRuleVo.class);
        ruleVo.setRuleId(ruleInfo.getId());

        // 查询传入的元模型规则字段的参数列表信息
        // 查询当前元模型字段数据的参数信息
        MetaModelRuleParam param = new MetaModelRuleParam();
        param.setModelRuleId(ruleVo.getRuleId());
        List<MetaModelRuleParam> currentRuleParamList = metaModelRuleParamService.selectMetaModelRuleParamList(param);
        if (!CollectionUtils.isEmpty(currentRuleParamList)) {
            currentRuleParamList.forEach(currentRuleParam -> {
                ModelRuleParamVo ruleParamVo = ModelConvertUtil.modelConvert(currentRuleParam, ModelRuleParamVo.class);
                ruleParamVo.setParModelRuleId(currentRuleParam.getModelRuleId());
                ruleParamVoList.add(ruleParamVo);
            });
        }
        ruleVo.setRuleParamVoList(ruleParamVoList);

        return AjaxResult.success(ruleVo);
    }

    /**
     * 新增元模型字段规则
     */
   //@RequiresPermissions("metadata:rule:modifyMetaRule")
    @PostMapping("/modifyMetaRule")
    @ApiOperation(value = "新增或编辑元模型字段规则信息")
    public AjaxResult modifyMetaRule(@RequestBody ModelRuleVo modelRuleVo) {
        int result = 0;
        try {
            // 转换元模型字段规则对象
            MetaModelRule modelRule = ModelConvertUtil.modelConvert(modelRuleVo, MetaModelRule.class);
            // 规则若为sql转换规则，入库前规范sql写法，将可能存在的多个连续空格处理为一个空格
            if ("1".equals(modelRule.getRuleUsed())) {
                String sql = modelRule.getConvertRuleSql();
                // 规范sql，将可能存在的多个空格，替换为一个空格
                sql = sql.replaceAll("\\s+", " ");
                modelRule.setConvertRuleSql(sql);
            }
            // 元模型字段规则数据入库
            if (modelRuleVo.getRuleId() != null) {
                // 编辑数据
                modelRule.setId(modelRuleVo.getRuleId());//
                metaModelRuleService.updateMetaModelRule(modelRule);
            } else {
                // 新增数据
                // 新增元模型字段规则数据
                modelRule.setId(IdUtil.getSnowflakeNextId());//
                 metaModelRuleService.insertMetaModelRule(modelRule);
            }

            if (modelRule.getId() == null) {
                modelRule = metaModelRuleService.selectMetaModelRuleList(modelRule).get(0);
            }
            // 元模型字段规则参数数据入库
            // 待入库的元模型字段规则参数对象集合
            List<MetaModelRuleParam> modelRuleParamList = new ArrayList<>();
            List<ModelRuleParamVo> ruleParamVoList = modelRuleVo.getRuleParamVoList();
            if (!CollectionUtils.isEmpty(ruleParamVoList)) {
                for (ModelRuleParamVo modelRuleParamVo : ruleParamVoList) {
                    MetaModelRuleParam ruleParam = ModelConvertUtil.modelConvert(modelRuleParamVo, MetaModelRuleParam.class);
                    ruleParam.setModelRuleId(modelRule.getId());
                    modelRuleParamList.add(ruleParam);
                }
            }
            // 元模型字段规则参数数据入库
            metaModelRuleParamService.saveOrUpdateRuleParamBatch(modelRule.getId() , modelRuleParamList);
            result  = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toAjax(result);
    }


    /**
     * 根据规则转换传入的元模型数据
     */
   //@RequiresPermissions("metadata:rule:convertMetaModelDataByRule")
    @PostMapping("/convertMetaModelDataByRule")
    @ApiOperation(value = "根据规则转换传入的元模型数据")
    public AjaxResult convertMetaModelDataByRule(@RequestBody ModelRuleConvertVo modelRuleConvertVo) {
        return AjaxResult.success(metaModelRuleService.convertMetaModelData(modelRuleConvertVo));
    }

    /**
     * 删除元模型字段规则
     */
   //@RequiresPermissions("metadata:rule:remove")
    //@Log(title = "元模型字段规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteRules")
    public AjaxResult deleteRules(Long[] ids) {
        int result = 0;
        List<Long> idList = Arrays.asList(ids);

        // 查询传入规则的对应参数
        List<MetaModelRuleParam> deleteRuleParamList = new ArrayList<MetaModelRuleParam>();
        idList.forEach(id -> {
            MetaModelRuleParam param = new MetaModelRuleParam();
            param.setModelRuleId(id);
            List<MetaModelRuleParam> ruleParamList = metaModelRuleParamService.selectMetaModelRuleParamList(param);
            if (!CollectionUtils.isEmpty(ruleParamList)) {
                deleteRuleParamList.addAll(ruleParamList);
            }
        });

        // 删除参数数据
        if (deleteRuleParamList.size() > 0) {
            Long [] ruleParamIds = deleteRuleParamList.stream().map(MetaModelRuleParam::getId).toArray(Long[]::new);
            result += metaModelRuleParamService.deleteMetaModelRuleParamByIds(ruleParamIds);
        }

        // 删除规则数据
        result += metaModelRuleService.deleteMetaModelRuleByIds(ids);

        return toAjax(result);
    }

}
