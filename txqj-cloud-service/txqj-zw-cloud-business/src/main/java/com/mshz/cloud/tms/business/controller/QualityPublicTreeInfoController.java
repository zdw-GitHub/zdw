package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.annotation.Log;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNode;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNodeColumn;
import com.mshz.cloud.tms.business.enums.BusinessType;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeInfoService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeModelSetService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeColumnService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeService;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import com.mshz.cloud.tms.business.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 公共树形组件-资源数据树信息Controller
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@RestController
@RequestMapping("/tree")
@Api(value = "tree", tags = {"tree"}, description = "公共树形组件-资源数据树信息")
public class QualityPublicTreeInfoController extends BaseController {

    @Resource
    private IQualityPublicTreeInfoService qualityPublicTreeInfoService;

    @Resource
    private IQualityPublicTreeNodeService treeNodeService;

    @Resource
    private IQualityPublicTreeNodeColumnService treeNodeColumnService;

    @Resource
    private IQualityPublicTreeModelSetService treeModelSetService;

    /**
     * 查询公共树形组件-资源数据树信息列表，包含树的节点信息
     */
    @RequiresPermissions("quality:tree:list")
    @GetMapping("/treeList")
    @ApiOperation(value = "资源数据树信息（包含节点信息）分页列表查询",notes = "资源数据树信息（包含节点信息）分页列表查询")
    public TableDataInfo treeList(QualityPublicTreeInfo qualityPublicTreeInfo) {
        startPage();
        // 返回资源树vo集合
        return getDataTable(getTreeInfoVoList(qualityPublicTreeInfo));
    }

    /**
     * 根据传入的元模型信息，查询其所有公共树结构
     * @param qualityPublicTreeInfo
     * @return
     */
    private List<TreeInfoVo> getTreeInfoVoList(QualityPublicTreeInfo qualityPublicTreeInfo) {
        List<TreeInfoVo> treeInfoVoList = new ArrayList<>();

        // 查询资源树信息
        List<QualityPublicTreeInfo> treeList = qualityPublicTreeInfoService.selectQualityPublicTreeInfoList(qualityPublicTreeInfo);
        if (!CollectionUtils.isEmpty(treeList)) {
            // 查询资源树的节点信息
            for (QualityPublicTreeInfo tree : treeList) {
                TreeInfoVo treeInfoVo = new TreeInfoVo();
                treeInfoVo.setTreeInfo(tree);

                QualityPublicTreeNode treeNodeParam = new QualityPublicTreeNode();
                treeNodeParam.setTreeId(tree.getId());

//                List<QualityPublicTreeNode> treeNodeList = treeNodeService.selectTreeNodeList(treeNodeParam);
//                if (!CollectionUtils.isEmpty(treeNodeList)) {
//                    // 确认根节点
//                    Optional<QualityPublicTreeNode> rootTreeNodeOptional = treeNodeList.stream().filter(treeNode -> "1".equals(treeNode.getNodeLevel())).findFirst();
//                    if (rootTreeNodeOptional.isPresent()) {
//                        QualityPublicTreeNode rootTreeNode = rootTreeNodeOptional.get();
//                        TreeNodeVo rootNode = ModelConvertUtil.modelConvert(rootTreeNode, TreeNodeVo.class);
//
//                        treeNodeService.packageTreeNodeVo(rootNode, treeNodeList);
//
//                        // 设置树节点信息
//                        treeInfoVo.setTreeNodeInfo(rootNode);
//                    }
//                } else {
//                    // 当前公共资源树还未配置节点信息
//                }

                List<Map<String, String>> treeNodeList = treeNodeService.selectTreeNodeListByParam(treeNodeParam);
                if (!CollectionUtils.isEmpty(treeNodeList)) {
                    // 确认根节点
                    Optional<Map<String, String>> rootTreeNodeOptional = treeNodeList.stream().filter(treeNode -> "1".equals(treeNode.get("nodeLevel"))).findFirst();
                    if (rootTreeNodeOptional.isPresent()) {
                        Map<String, String> rootTreeNode = rootTreeNodeOptional.get();
                        TreeNodeVo rootNode = ModelConvertUtil.mapConvert(rootTreeNode, TreeNodeVo.class);

                        treeNodeService.packageTreeNodeVoByMap(rootNode, treeNodeList);
                        // 设置树节点信息
                        treeInfoVo.setTreeNodeInfo(rootNode);
                    }
                } else {
                    // 当前公共资源树还未配置节点信息
                }

                treeInfoVoList.add(treeInfoVo);
            }
        } else {
            // 当前元模型没有配置公共资源树
        }
        return treeInfoVoList;
    }


    /**
     * 查询公共树形组件-资源数据树信息列表
     */
    @RequiresPermissions("quality:tree:list")
    @GetMapping("/list")
    @ApiOperation(value = "资源数据树信息分页列表查询",notes = "资源数据树信息分页列表查询")
    public TableDataInfo list(QualityPublicTreeInfo qualityPublicTreeInfo) {
        startPage();
        List<QualityPublicTreeInfo> list = qualityPublicTreeInfoService.selectQualityPublicTreeInfoList(qualityPublicTreeInfo);
        return getDataTable(list);
    }

    /**
     * 导出公共树形组件-资源数据树信息列表
     */
    @RequiresPermissions("quality:tree:export")
    @Log(title = "公共树形组件-资源数据树信息", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "资源数据树信息导出",notes = "资源数据树信息导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityPublicTreeInfo qualityPublicTreeInfo) throws IOException {
        List<QualityPublicTreeInfo> list = qualityPublicTreeInfoService.selectQualityPublicTreeInfoList(qualityPublicTreeInfo);
        ExcelUtil<QualityPublicTreeInfo> util = new ExcelUtil<QualityPublicTreeInfo>(QualityPublicTreeInfo.class);
        util.exportExcel(response, list, "公共树形组件-资源数据树信息数据");
    }

    /**
     * 获取公共树形组件-资源数据树信息详细信息
     */
    @RequiresPermissions("quality:tree:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "资源数据树信息id查询",notes = "根据id资源数据树信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(qualityPublicTreeInfoService.selectQualityPublicTreeInfoById(id));
    }

    /**
     * 新增公共树形组件-资源数据树信息
     */
    @RequiresPermissions("quality:tree:add")
    @Log(title = "公共树形组件-资源数据树信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增资源数据树信息",notes = "新增资源数据树信息")
    @PostMapping
    public AjaxResult add(@RequestBody QualityPublicTreeInfo qualityPublicTreeInfo) {
        return toAjax(qualityPublicTreeInfoService.insertQualityPublicTreeInfo(qualityPublicTreeInfo));
    }

    /**
     * 修改公共树形组件-资源数据树信息
     */
    @RequiresPermissions("quality:tree:edit")
    @Log(title = "公共树形组件-资源数据树信息", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "更新资源数据树信息",notes = "更新资源数据树信息")
    @PutMapping
    public AjaxResult edit(@RequestBody QualityPublicTreeInfo qualityPublicTreeInfo) {
        return toAjax(qualityPublicTreeInfoService.updateQualityPublicTreeInfo(qualityPublicTreeInfo));
    }

    /**
     * 删除公共树形组件-资源数据树信息
     */
    @RequiresPermissions("quality:tree:remove")
    @Log(title = "公共树形组件-资源数据树信息", businessType = BusinessType.DELETE)
    @ApiOperation(value = "批量删除资源数据树信息",notes = "批量删除资源数据树信息")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(qualityPublicTreeInfoService.deleteQualityPublicTreeInfoByIds(ids));
    }

    /**
     * 删除公共树形组件-资源数据树信息
     */
    @RequiresPermissions("quality:tree:remove")
    @Log(title = "公共树形组件-资源数据树信息", businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据id删除资源数据树信息",notes = "根据id删除资源数据树信息")
    @DeleteMapping("/removeById/{id}")
    public AjaxResult removeById(@PathVariable Long id) {
        int rows = 0;
        // 删除资源树，以及下的节点信息及节点查询字段信息
        // 查询待删除资源树信息下的所有节点信息
        QualityPublicTreeNode treeNodeParam = new QualityPublicTreeNode();
        treeNodeParam.setTreeId(id);
        List<QualityPublicTreeNode> treeNodeList = treeNodeService.selectQualityPublicTreeNodeList(treeNodeParam);
        if (!CollectionUtils.isEmpty(treeNodeList)) {
            // 待删除的树节点id数组
            Long[] treeNodeIds = treeNodeList.stream().map(QualityPublicTreeNode::getId).toArray(Long[]::new);
            rows += treeNodeService.deleteQualityPublicTreeNodeByIds(treeNodeIds);
        }

        // 查询待删除资源树信息下的所有节点查询字段信息
        QualityPublicTreeNodeColumn treeNodeColumnParam = new QualityPublicTreeNodeColumn();
        treeNodeColumnParam.setTreeId(id);
        List<QualityPublicTreeNodeColumn> treeNodeColumnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(treeNodeColumnParam);
        if (!CollectionUtils.isEmpty(treeNodeColumnList)) {
            // 待删除的树节点查询字段id数组
            Long[] treeNodeColumnIds = treeNodeColumnList.stream().map(QualityPublicTreeNodeColumn::getId).toArray(Long[]::new);
            rows += treeNodeColumnService.deleteQualityPublicTreeNodeColumnByIds(treeNodeColumnIds);
        }

        // 根据资源树id，查询所有该资源树节点与元模型字段关联信息
        QualityPublicTreeModelSet param = new QualityPublicTreeModelSet();
        param.setTreeId(id);
        List<QualityPublicTreeModelSet> treeModelSetList = treeModelSetService.selectQualityPublicTreeModelSetList(param);
        if (!CollectionUtils.isEmpty(treeModelSetList)) {
            // 删除所有该资源树节点与元模型字段关联信息
            Long [] treeModelSetIds = treeModelSetList.stream().map(QualityPublicTreeModelSet::getId).toArray(Long[]::new);
            rows += treeModelSetService.deleteQualityPublicTreeModelSetByIds(treeModelSetIds);
        }

        // 删除资源树信息
        rows += qualityPublicTreeInfoService.deleteQualityPublicTreeInfoById(id);

        return toAjax(rows);
    }

    /**
     * 新增公共树形组件-树节点查询数据属性
     */
    @RequiresPermissions("quality:tree:list")
    @ApiOperation(value = "获取当前元模型字段的资源树节点绑定信息",notes = "获取当前元模型字段的资源树节点绑定信息")
    @PostMapping("/getMetaTechTreeNodeInfo")
    public AjaxResult getMetaTechTreeNodeInfo(@RequestBody QualityPublicTreeModelSet treeNodeModelSet) {
        // TODO 返回数据需要有元模型下的所有资源树（包括节点）
        //  以及 和树有关的元模型字段（需要区分哪些字段是最下级节点需要下拉菜单展示树的，哪些字段是树节点但非最下级子节点、需要设置文本框为不可编辑的）
        ResManageVo resultVo = new ResManageVo();

        // 查询当前元模型设置的字段与资源树关系
        List<QualityPublicTreeModelSet> metaModelTreeSetList = treeModelSetService.selectQualityPublicTreeModelSetList(treeNodeModelSet);

        // 查询传入元模型的公共树结构信息
        QualityPublicTreeInfo qualityPublicTreeInfo = new QualityPublicTreeInfo();
        qualityPublicTreeInfo.setMetaModeId(String.valueOf(treeNodeModelSet.getMetaModelId()));
        List<TreeInfoVo> treeInfoVoList = getTreeInfoVoList(qualityPublicTreeInfo);

        resultVo.setMetaModelTreeSetList(metaModelTreeSetList);
        resultVo.setTreeInfoVoList(treeInfoVoList);

        return AjaxResult.success(resultVo);
    }



}
