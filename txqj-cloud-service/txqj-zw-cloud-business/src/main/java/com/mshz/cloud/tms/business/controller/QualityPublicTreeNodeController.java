package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.util.StringUtils;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import com.mshz.cloud.tms.business.vo.ResTreeVo;
import com.mshz.cloud.tms.business.vo.TreeNodeColumnVo;
import com.mshz.cloud.common.util.ExcelUtil;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.annotation.Log;
import com.mshz.cloud.tms.business.enums.BusinessType;
import com.mshz.cloud.tms.business.annotation.RequiresPermissions;
import com.mshz.cloud.tms.business.vo.QualityPublicTreeModelSet;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNode;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNodeColumn;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeInfoService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeModelSetService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeColumnService;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeService;
import com.mshz.cloud.tms.business.vo.TreeNodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 公共树形组件-树节点Controller
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@RestController
@RequestMapping("/treenode")
@Api(value = "treenode", tags = {"treenode"}, description = "公共树形组件-资源数据树节点")
public class QualityPublicTreeNodeController extends BaseController {

    @Resource
    private IQualityPublicTreeInfoService treeService;

    @Resource
    private IQualityPublicTreeNodeService treeNodeService;

    @Resource
    private IQualityPublicTreeNodeColumnService treeNodeColumnService;

    @Resource
    private IQualityPublicTreeModelSetService treeModelSetService;

    /**
     * 获取公共树形组件-资源树查询
     */
    @RequiresPermissions("quality:treenode:query")
    @ApiOperation(value = "根据资源树id查询资源数据树",notes = "根据资源树id查询资源数据树节点信息")
    @GetMapping(value = "/getNodeByTreeInfo")
    public AjaxResult getNodeByTreeInfo(Long treeId) {
        TreeNodeVo treeNodeVo = new TreeNodeVo();

        // 查询资源树节点信息数据
        QualityPublicTreeNode treeNodeParam = new QualityPublicTreeNode();
        treeNodeParam.setTreeId(treeId);
        List<QualityPublicTreeNode> treeNodeList = treeNodeService.selectTreeNodeList(treeNodeParam);

        if (!CollectionUtils.isEmpty(treeNodeList)) {
            // 确认根节点
            Optional<QualityPublicTreeNode> rootTreeNodeOptional = treeNodeList.stream().filter(treeNode -> "1".equals(treeNode.getNodeLevel())).findFirst();
            if (rootTreeNodeOptional.isPresent()) {
                QualityPublicTreeNode rootTreeNode = rootTreeNodeOptional.get();
                treeNodeVo = ModelConvertUtil.modelConvert(rootTreeNode, TreeNodeVo.class);

                treeNodeService.packageTreeNodeVo(treeNodeVo, treeNodeList);
            }
        } else {
            // 当前公共资源树还未配置节点信息
        }
        return AjaxResult.success(treeNodeVo);
    }

    /**
     * 获取公共树形组件-树节点详细信息
     */
    @RequiresPermissions("quality:treenode:query")
    @ApiOperation(value = "根据Id查询资源数据树节点及查询数据字段信息",notes = "根据Id查询资源数据树节点及查询数据字段信息")
    @GetMapping(value = "/getNodeInfo/{id}")
    public AjaxResult getNodeInfo(@PathVariable("id") Long nodeId) {
        TreeNodeVo treeNodeVo = new TreeNodeVo();

        QualityPublicTreeNode treeNode = treeNodeService.selectQualityPublicTreeNodeById(nodeId);
        if (treeNode != null) {
            treeNodeVo = ModelConvertUtil.modelConvert(treeNode, TreeNodeVo.class);

            // 获取节点查询资源数据字段信息
            QualityPublicTreeNodeColumn columnParam = new QualityPublicTreeNodeColumn();
            columnParam.setNodeId(nodeId);
            List<QualityPublicTreeNodeColumn> nodeColumnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(columnParam);
            if (!CollectionUtils.isEmpty(nodeColumnList)) {
                List<TreeNodeColumnVo> nodeColumnVoList = new ArrayList<>();
                nodeColumnList.forEach(nodeColumn -> {
                    TreeNodeColumnVo treeNodeColumnVo = new TreeNodeColumnVo();
                    treeNodeColumnVo = ModelConvertUtil.modelConvert(nodeColumn, TreeNodeColumnVo.class);
                    nodeColumnVoList.add(treeNodeColumnVo);
                });
                treeNodeVo.setNodeColumnList(nodeColumnVoList);
            }

        }
        return AjaxResult.success(treeNodeVo);
    }

    /**
     * 新增/更新公共树形组件-树节点
     */
    @RequiresPermissions("quality:treenode:add")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增或编辑资源数据树节点及查询字段信息",notes = "新增或编辑资源数据树节点及查询字段信息")
    @PostMapping("/addOrUpdateTreeNode")
    public AjaxResult addOrUpdateTreeNode(@RequestBody TreeNodeVo treeNodeVo) {
        int rows = 0;

        // 获取节点信息
        QualityPublicTreeNode treeNode = ModelConvertUtil.modelConvert(treeNodeVo, QualityPublicTreeNode.class);
        treeNode.setRemark(treeNodeVo.getRemark());
//        if (StringUtils.isEmpty(treeNodeVo.getParentId())) {
//            treeNode.setParentId(null);
//        }
        if (StringUtils.isNotEmpty(treeNodeVo.getId())) {
            // 更新数据
            // 更新节点数据
            rows += treeNodeService.updateQualityPublicTreeNode(treeNode);
        } else {
            // 新增数据
            // 新增节点数据
            rows += treeNodeService.insertQualityPublicTreeNode(treeNode);
        }
        // 新增或更新后的节点数据
        treeNode = treeNodeService.selectQualityPublicTreeNodeList(treeNode).get(0);

        // 新增/编辑当前节点对应的技术元模型信息
        if (StringUtils.isNotEmpty(treeNodeVo.getMetaModelId()) && StringUtils.isNotEmpty(treeNodeVo.getMetaModelTechId())) {
            QualityPublicTreeModelSet nodeModelSet = new QualityPublicTreeModelSet();
            nodeModelSet.setNodeId(treeNode.getId());
            nodeModelSet.setTreeId(treeNode.getTreeId());
            nodeModelSet.setIsLastNode(treeNodeVo.getIsLastNode());
            nodeModelSet.setMetaModelId(Long.parseLong(treeNodeVo.getMetaModelId()));
            nodeModelSet.setMetaModelTechId(Long.parseLong(treeNodeVo.getMetaModelTechId()));
            QualityPublicTreeModelSet currData = treeModelSetService.selectTreeNodeModelSetByTech(nodeModelSet);
            if (currData != null) {
                nodeModelSet.setId(currData.getId());//
                treeModelSetService.updateQualityPublicTreeModelSet(nodeModelSet);
            } else {
                treeModelSetService.insertQualityPublicTreeModelSet(nodeModelSet);
            }
        }

        // 新增/编辑节点查询数据字段
        List<QualityPublicTreeNodeColumn> treeNodeColumnList = new ArrayList<>();
        // 获取传入的节点查询字段信息
        List<TreeNodeColumnVo> nodeColumnVoList = treeNodeVo.getNodeColumnList();
        for (TreeNodeColumnVo treeNodeColumnVo : nodeColumnVoList) {
            QualityPublicTreeNodeColumn treeNodeColumn = ModelConvertUtil.modelConvert(treeNodeColumnVo, QualityPublicTreeNodeColumn.class);
            treeNodeColumnList.add(treeNodeColumn);
        }
        // 批量处理节点查询字段数据
        rows += treeNodeColumnService.saveOrUpdateDataBatch(treeNode.getId(), treeNodeColumnList);

        return toAjax(rows);
    }

    /**
     * 查询公共树形组件-树节点列表
     */
    @RequiresPermissions("quality:treenode:list")
    @ApiOperation(value = "资源数据树节点分页列表查询",notes = "资源数据树节点分页列表查询")
    @GetMapping("/list")
    public TableDataInfo list(QualityPublicTreeNode qualityPublicTreeNode) {
        startPage();
        List<QualityPublicTreeNode> list = treeNodeService.selectQualityPublicTreeNodeList(qualityPublicTreeNode);
        return getDataTable(list);
    }

    /**
     * 导出公共树形组件-树节点列表
     */
    @RequiresPermissions("quality:treenode:export")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "资源数据树节点导出",notes = "资源数据树节点导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityPublicTreeNode qualityPublicTreeNode) throws IOException {
        List<QualityPublicTreeNode> list = treeNodeService.selectQualityPublicTreeNodeList(qualityPublicTreeNode);
        ExcelUtil<QualityPublicTreeNode> util = new ExcelUtil<QualityPublicTreeNode>(QualityPublicTreeNode.class);
        util.exportExcel(response, list, "公共树形组件-树节点数据");
    }

    /**
     * 获取公共树形组件-树节点详细信息
     */
    @RequiresPermissions("quality:treenode:query")
    @ApiOperation(value = "根据Id查询资源数据树节点",notes = "根据Id查询资源数据树节点")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(treeNodeService.selectQualityPublicTreeNodeById(id));
    }

    /**
     * 新增公共树形组件-树节点
     */
    @RequiresPermissions("quality:treenode:add")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增资源数据树节点",notes = "新增资源数据树节点")
    @PostMapping
    public AjaxResult add(@RequestBody QualityPublicTreeNode qualityPublicTreeNode) {
        return toAjax(treeNodeService.insertQualityPublicTreeNode(qualityPublicTreeNode));
    }

    /**
     * 修改公共树形组件-树节点
     */
    @RequiresPermissions("quality:treenode:edit")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "更新资源数据树节点",notes = "更新资源数据树节点")
    @PutMapping
    public AjaxResult edit(@RequestBody QualityPublicTreeNode qualityPublicTreeNode)
    {
        return toAjax(treeNodeService.updateQualityPublicTreeNode(qualityPublicTreeNode));
    }

    /**
     * 删除公共树形组件-树节点
     */
    @RequiresPermissions("quality:treenode:remove")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.DELETE)
    @ApiOperation(value = "批量删除资源数据树节点",notes = "批量删除资源数据树节点")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(treeNodeService.deleteQualityPublicTreeNodeByIds(ids));
    }

    /**
     * 删除公共树形组件-树节点
     */
    @RequiresPermissions("quality:treenode:remove")
    @Log(title = "公共树形组件-树节点", businessType = BusinessType.DELETE)
    @ApiOperation(value = "根据id删除资源数据树节点及子节点信息",notes = "根据id删除资源数据树节点及子节点信息")
    @DeleteMapping("/removeNodeById/{id}")
    public AjaxResult removeNodeById(@PathVariable Long id) {
        // 删除传入id节点，以及其子节点信息和他们的节点查询字段信息
        int rows = 0;
        // 传入的待删除节点
        QualityPublicTreeNode deleteNode = treeNodeService.selectQualityPublicTreeNodeById(id);

        // 查询传入节点的所有子节点（同样待删除）
        QualityPublicTreeNode param = new QualityPublicTreeNode();
        param.setParentId(id);
        List<QualityPublicTreeNode> deleteNodeList = treeNodeService.selectTreeNodeList(param);
        if (deleteNodeList == null) {
            deleteNodeList = new ArrayList<>();
        }
        // 将传入的节点本身信息放入集合
        deleteNodeList.add(deleteNode);

        // 查询所有待删除节点的节点查询数据字段信息
        List<QualityPublicTreeNodeColumn> deleteColumnList = new ArrayList<>();
        // 查询所有待删除的树节点关联元模型字段信息
        List<QualityPublicTreeModelSet> deleteModelSetList = new ArrayList<>();

        // 遍历节点信息查询
        for (QualityPublicTreeNode treeNode : deleteNodeList) {
            QualityPublicTreeNodeColumn ncParam = new QualityPublicTreeNodeColumn();
            ncParam.setNodeId(treeNode.getId());
            List<QualityPublicTreeNodeColumn> nodeColumnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(ncParam);
            deleteColumnList.addAll(nodeColumnList);

            QualityPublicTreeModelSet msParam = new QualityPublicTreeModelSet();
            msParam.setNodeId(treeNode.getId());
            List<QualityPublicTreeModelSet> treeModelSetList = treeModelSetService.selectQualityPublicTreeModelSetList(msParam);
            deleteModelSetList.addAll(treeModelSetList);
        }

        // 删除传入节点及其子节点的节点查询字段数据
        Long[] nodeColumnIds = deleteColumnList.stream().map(QualityPublicTreeNodeColumn::getId).toArray(Long[]::new);
        rows += treeNodeColumnService.deleteQualityPublicTreeNodeColumnByIds(nodeColumnIds);

        // 删除传入节点的节点关联元模型字段信息
        Long [] treeModelSetIds = deleteModelSetList.stream().map(QualityPublicTreeModelSet::getId).toArray(Long[]::new);
        rows += treeModelSetService.deleteQualityPublicTreeModelSetByIds(treeModelSetIds);

        // 删除传入节点及其子节点数据
        Long[] nodeIds = deleteNodeList.stream().map(QualityPublicTreeNode::getId).toArray(Long[]::new);
        rows += treeNodeService.deleteQualityPublicTreeNodeByIds(nodeIds);

        return toAjax(rows);
    }

    /**
     * 通信资源维护-点击公共树形组件时各级节点数据查询
     * @param resTreeVo
     *      dataCode    点击展开树节点的当前节点的数据编码
     *      dataName    树节点名称模糊查询参数
     *      metaModeId  需要树组件的元模型id
     *      nodeId      点击展开树节点的当前节点id（若为点击展示树的所有1级节点的话，该参数不传递）
     *      nodeLevel   点击展开树节点的当前节点级别（如当前要展开一个2级节点，则传2；若为点击展示树的所有1级节点的话，该参数传 0）
     *      pageNum     分页参数，页数
     *      pageSize    分页参数，每页大小
     *      treeId      需要调用的树的id
     */
    @RequiresPermissions("quality:tree:query")
    @ApiOperation(value = "通信资源维护-点击公共树形组件时各级节点数据查询",notes = "通信资源维护-点击公共树形组件时各级节点数据查询")
    @PostMapping("/queryResDataTree")
    public AjaxResult queryResDataTree(@RequestBody ResTreeVo resTreeVo) {
        TreeNodeVo selectNode = new TreeNodeVo();

        List<LinkedHashMap> nodeDataList = new ArrayList<>();
        // 查询传入的树节点层级的下一级的数据，需要查询下一级的节点信息和查询字段信息
        QualityPublicTreeNode parentParam = new QualityPublicTreeNode();
        parentParam.setTreeId(Long.parseLong(resTreeVo.getTreeId()));
        if (!StringUtils.isEmpty(resTreeVo.getNodeId())) {
            parentParam.setParentId(Long.parseLong(resTreeVo.getNodeId()));
        }
        parentParam.setNodeLevel(String.valueOf(Integer.parseInt(resTreeVo.getNodeLevel()) + 1));
        QualityPublicTreeNode queryNodeInfo = treeNodeService.selectTreeNodeByParent(parentParam);
        if (queryNodeInfo != null) {
            // 获取子节点查询字段信息，用于拼接子节点查询条件
            QualityPublicTreeNodeColumn treeNodeColumnParam = new QualityPublicTreeNodeColumn();
            treeNodeColumnParam.setNodeId(queryNodeInfo.getId());
            List<QualityPublicTreeNodeColumn> columnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(treeNodeColumnParam);

            // 查询当前节点绑定元模型字段id信息
            Map<String, String> setParam = new HashMap<>();
            setParam.put("treeId", resTreeVo.getTreeId());
//            setParam.put("nodeId", resTreeVo.getNodeId());
            setParam.put("parentId", resTreeVo.getNodeId());
            setParam.put("nodeLevel", queryNodeInfo.getNodeLevel());
            QualityPublicTreeModelSet modelSetInfo = treeModelSetService.selectTreeNodeModelSetByPNode(setParam);

            // 获取到配置的子节点数据查询sql
            String dataSql = queryNodeInfo.getNodeDataSql();
            // 需要实际执行的数据查询sql
            StringBuilder querySql = new StringBuilder();
            querySql.append("select ");
            // sql查询条件（父级查询条件）
            StringBuilder condition = new StringBuilder();
            condition.append(" where 1=1");

            // 拼接外侧查询字段
            for (QualityPublicTreeNodeColumn column : columnList) {
                querySql.append(column.getNodeQueryColumnAlias()).append(", ");
                // 拼接父级查询条件（当前节点非根节点，且字段中有存储父级信息字段）
                if (Integer.parseInt(resTreeVo.getNodeLevel()) >= 1 && "parent_column".equals(column.getNodeQueryColumnAlias())) {
                    // 为增加分页查询，修改拼接查询条件的字段，将条件查询部分放置到内部子查询
//                    condition.append(" and ").append(column.getNodeQueryColumn()).append(" = '").append(resTreeVo.getDataCode()).append("'");
                    condition.append(" and ").append(column.getNodeQueryColumnAlias()).append(" = '").append(resTreeVo.getDataCode()).append("'");
                }
                // 资源树数据模糊查询
                if (StringUtils.isNotEmpty(resTreeVo.getDataName()) && "name".equals(column.getNodeQueryColumnAlias())) {
                    // 若有传入名称，拼接查询条件
//                    condition.append(" and ").append(column.getNodeQueryColumn()).append(" like '%").append(resTreeVo.getDataName()).append("%'");
                    condition.append(" and ").append(column.getNodeQueryColumnAlias()).append(" like '%").append(resTreeVo.getDataName()).append("%'");
                }
            }


            // 内测子查询拼接查询分页参数，若未传入分页参数，默认第1页，每页20条
            if (StringUtils.isEmpty(resTreeVo.getPageSize())) {
                resTreeVo.setPageSize("20");
            }
            if (StringUtils.isEmpty(resTreeVo.getPageNum())) {
                resTreeVo.setPageNum("1");
            }
            int start = (Integer.parseInt(resTreeVo.getPageNum()) - 1) * Integer.parseInt(resTreeVo.getPageSize());
            condition.append(" limit ").append(String.valueOf(start)).append(", ").append(resTreeVo.getPageSize());

            // 拼接节点的id和层级信息
            querySql.append("'").append(queryNodeInfo.getId()).append("' as nodeId, '").append(queryNodeInfo.getNodeLevel()).append("' as nodeLevel");
            // 拼接节点关联元模型字段信息
            String metaModelTechId = "";
            if (modelSetInfo != null) {
                metaModelTechId = String.valueOf(modelSetInfo.getMetaModelTechId());
            } else {
                metaModelTechId = "0";
            }
            querySql.append(", ").append(metaModelTechId).append(" as metaModelTechId");

            // 拼接查询sql
            querySql.append(" from (").append(dataSql).append(") mid ").append(condition);
//            querySql.append(" from (").append(dataSql).append(condition).append(") mid ");


            // 执行sql查询数据
            nodeDataList = treeNodeService.nodeDataSql(querySql.toString());
            if("1".equals(queryNodeInfo.getIsLastNode())){
                nodeDataList.forEach(f -> f.put("leaf",true));
            }
        }

        return AjaxResult.success(nodeDataList);
    }

    /**
     * 通信资源维护-编辑数据时公共树形组件各节点数据回显
     *  @param resTreeVo
     *      dataCode       待回显的树的最底层节点的数据code
     *      metaModeId     需要树组件的元模型id
     *      nodeId         待回显的树的最底层节点id
     *      nodeLevel      待回显的树的最底层节点级别
     *      treeId         需要调用的树的id
     */
    @RequiresPermissions("quality:tree:query")
    @ApiOperation(value = "通信资源维护-编辑数据时公共树形组件各节点数据回显",notes = "通信资源维护-编辑数据时公共树形组件各节点数据回显")
    @PostMapping("/resDataTreeEcho")
    public AjaxResult resDataTreeEcho(@RequestBody ResTreeVo resTreeVo) {
        // 返回的选中数据的所在的树的根节点vo
        TreeNodeVo rootNode = new TreeNodeVo();

        List<TreeNodeVo> dataNodeList = new ArrayList<>();

        // 获取传入节点的所有父节点信息
        List<TreeNodeVo> treeNodeVoList = treeNodeService.getTreeNodeListByLastNode(Long.parseLong(resTreeVo.getNodeId()));

        // 存储本节点的父级节点的数据的id
        String childDataParentId = resTreeVo.getDataCode();
        for (TreeNodeVo treeNodeVo : treeNodeVoList) {
//            // 当前节点vo
//            TreeNodeVo treeNodeVo = ModelConvertUtil.modelConvert(treeNode, TreeNodeVo.class);

            // 查询当前节点vo的查询字段信息，用于拼装查询sql
            QualityPublicTreeNodeColumn columnParam = new QualityPublicTreeNodeColumn();
            columnParam.setNodeId(Long.parseLong(treeNodeVo.getId()));
            List<QualityPublicTreeNodeColumn> columnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(columnParam);

            // 获取sql，拼接查询sql并查询数据
            // 需要实际执行的数据查询sql
            StringBuilder querySql = new StringBuilder();
            querySql.append("select ");

            // 获取当前节点数据查询sql
            String dataSql = treeNodeVo.getNodeDataSql();
            // 拼接查询字段
            for (QualityPublicTreeNodeColumn column : columnList) {
                querySql.append(column.getNodeQueryColumnAlias()).append(", ");
            }

//            // 查询当前节点绑定元模型字段id信息
//            Map<String, String> setParam = new HashMap<>();
//            setParam.put("treeId", resTreeVo.getTreeId());
//            setParam.put("nodeId", resTreeVo.getNodeId());
//            setParam.put("nodeLevel", resTreeVo.getNodeLevel());
//            QualityPublicTreeModelSet modelSetInfo = treeModelSetService.selectTreeNodeModelSetByPNode(setParam);
//            // 拼接节点关联元模型字段信息
//            String metaModelTechId = "";
//            if (modelSetInfo != null) {
//                metaModelTechId = String.valueOf(modelSetInfo.getMetaModelTechId());
//            } else {
//                metaModelTechId = "0";
//            }
//            querySql.append(metaModelTechId).append(" as metaModelTechId, ");

            // sql查询条件（当前层级查询条件）
            String condition = "where 1=1";
            // 拼接当前层级查询条件
            // 若循环当前节点是传入的最下层级子节点，查询传入的数据编码
            // 若循环当前节点非最下级节点，查询条件为编码字段下一级节点的父级字段数据
            condition = condition.concat(" and code = '").concat(childDataParentId + "'");

            // 拼接节点的id和层级信息
            querySql.append("'").append(treeNodeVo.getId()).append("' as nodeId, '").append(treeNodeVo.getNodeLevel()).append("' as nodeLevel");
            // 拼接查询sql
            querySql.append(" from (").append(dataSql).append(") mid ").append(condition);
            // 执行sql查询数据
            List<LinkedHashMap> nodeDataList = treeNodeService.nodeDataSql(querySql.toString());

            treeNodeVo.setDataCodeValue(String.valueOf(nodeDataList.get(0).get("code")));
            treeNodeVo.setDataNameValue(String.valueOf(nodeDataList.get(0).get("name")));
            treeNodeVo.setTableName(String.valueOf(nodeDataList.get(0).get("tableName")));
//            treeNodeVo.setMetaModelTechId(String.valueOf(nodeDataList.get(0).get("metaModelTechId")));
            dataNodeList.add(treeNodeVo);

            // 获取本层节点的数据的父级数据编码
            childDataParentId = String.valueOf(nodeDataList.get(0).get("parent_column"));
        }

        if (!CollectionUtils.isEmpty(dataNodeList)) {
            rootNode = dataNodeList.stream().filter(treeNodeVo -> "1".equals(treeNodeVo.getNodeLevel())).findFirst().orElse(null);
            dataNodeList.remove(rootNode);
            packageTreeNodeVo(rootNode, dataNodeList);
        }

        // 根据节点信息和最下层子节点的实际数据，查询以上各级节点的的数据
        return AjaxResult.success(rootNode);
    }


    @RequiresPermissions("quality:tree:query")
    @ApiOperation(value = "通信资源维护-公共树形组件节点数据查询",notes = "通信资源维护-公共树形组件节点数据查询")
    @PostMapping("/queryResDataTreeAll")
    public AjaxResult queryResDataTreeAll(@RequestBody ResTreeVo resTreeVo) {
        // 返回的选中数据的所在的树的根节点vo
        List<LinkedHashMap> nodeDataList = new ArrayList<>();

        //防止卡死
        if (StringUtils.isEmpty(resTreeVo.getDataName())
                || resTreeVo.getDataName().length() == 1
                || resTreeVo.getDataName().matches("\\d+")
                || resTreeVo.getDataName().matches("[a-zA-Z]+")) return AjaxResult.success(nodeDataList);
        int nodeLevel = 0;

        // 按树ID获取树节点列表
        List<QualityPublicTreeNode> qualityPublicTreeNodeList = treeNodeService.getTreeNodeListByTreeID(Long.parseLong(resTreeVo.getTreeId()));
        for (QualityPublicTreeNode queryNodeInfo : qualityPublicTreeNodeList) {
            if (!"1".equals(queryNodeInfo.getIsMultistageSearch())) continue;
            // 获取子节点查询字段信息，用于拼接子节点查询条件
            QualityPublicTreeNodeColumn treeNodeColumnParam = new QualityPublicTreeNodeColumn();
            treeNodeColumnParam.setNodeId(queryNodeInfo.getId());
            List<QualityPublicTreeNodeColumn> columnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(treeNodeColumnParam);

            // 查询当前节点绑定元模型字段id信息
            Map<String, String> setParam = new HashMap<>();
            setParam.put("treeId", String.valueOf(queryNodeInfo.getTreeId()));
            setParam.put("nodeLevel", queryNodeInfo.getNodeLevel());
            QualityPublicTreeModelSet modelSetInfo = treeModelSetService.selectTreeNodeModelSetByPNode(setParam);

            // 获取到配置的子节点数据查询sql
            String dataSql = queryNodeInfo.getNodeDataSql();
            // 需要实际执行的数据查询sql
            StringBuilder querySql = new StringBuilder();
            querySql.append("select ");
            for (QualityPublicTreeNodeColumn column : columnList) {
                querySql.append(column.getNodeQueryColumnAlias()).append(", ");
            }
            // 拼接节点的id和层级信息
            querySql.append("'").append(queryNodeInfo.getId()).append("' as nodeId, '").append(queryNodeInfo.getNodeLevel()).append("' as nodeLevel");
            // 拼接节点关联元模型字段信息
            String metaModelTechId = "0";
            if (modelSetInfo != null) {
                metaModelTechId = String.valueOf(modelSetInfo.getMetaModelTechId());
            }
            querySql.append(", ").append(metaModelTechId).append(" as metaModelTechId");

            // 拼接查询sql
            querySql.append(" from (").append(dataSql).append(") mid ").append(" where name like '%").append(resTreeVo.getDataName()).append("%'");
            // 执行sql查询数据
            nodeDataList = treeNodeService.nodeDataSql(querySql.toString());
            if (!CollectionUtils.isEmpty(nodeDataList)){
                nodeLevel = Integer.parseInt(queryNodeInfo.getNodeLevel());
                break;
            }
        }
        if(!CollectionUtils.isEmpty(nodeDataList) && nodeLevel != 1){
            for (int i = nodeLevel-1; i >= 1; i--) {
                List<LinkedHashMap> nodeData = new ArrayList<>();
                for (LinkedHashMap linkedHashMap : nodeDataList) {
                    String level = String.valueOf(i);
                    QualityPublicTreeNode queryNodeInfo = qualityPublicTreeNodeList.stream()
                            .filter(qualityPublicTreeNode -> level.equals(qualityPublicTreeNode.getNodeLevel())).findFirst().orElse(null);
                    // 获取子节点查询字段信息，用于拼接子节点查询条件
                    QualityPublicTreeNodeColumn treeNodeColumnParam = new QualityPublicTreeNodeColumn();
                    treeNodeColumnParam.setNodeId(queryNodeInfo.getId());
                    List<QualityPublicTreeNodeColumn> columnList = treeNodeColumnService.selectQualityPublicTreeNodeColumnList(treeNodeColumnParam);

                    // 查询当前节点绑定元模型字段id信息
                    Map<String, String> setParam = new HashMap<>();
                    setParam.put("treeId", String.valueOf(queryNodeInfo.getTreeId()));
                    setParam.put("nodeLevel", queryNodeInfo.getNodeLevel());
                    QualityPublicTreeModelSet modelSetInfo = treeModelSetService.selectTreeNodeModelSetByPNode(setParam);

                    // 获取到配置的子节点数据查询sql
                    String dataSql = queryNodeInfo.getNodeDataSql();
                    // 需要实际执行的数据查询sql
                    StringBuilder querySql = new StringBuilder();
                    querySql.append("select ");
                    // 拼接外侧查询字段
                    for (QualityPublicTreeNodeColumn column : columnList) {
                        querySql.append(column.getNodeQueryColumnAlias()).append(", ");
                    }
                    // 拼接节点的id和层级信息
                    querySql.append("'").append(queryNodeInfo.getId()).append("' as nodeId, '").append(queryNodeInfo.getNodeLevel()).append("' as nodeLevel");
                    // 拼接节点关联元模型字段信息
                    String metaModelTechId = "0";
                    if (modelSetInfo != null) {
                        metaModelTechId = String.valueOf(modelSetInfo.getMetaModelTechId());
                    }
                    querySql.append(", ").append(metaModelTechId).append(" as metaModelTechId");
                    // 拼接查询sql
                    querySql.append(" from (").append(dataSql).append(") mid ").append(" where  code = '").append(linkedHashMap.get("parent_column")).append("'");
                    // 执行sql查询数据
                    LinkedHashMap map = treeNodeService.nodeDataSqlMap(querySql.toString());

                    if (map != null){
                        LinkedHashMap ored = nodeData.stream()
                                .filter(a -> Objects.equals(linkedHashMap.get("parent_column"),a.get("code"))).findFirst().orElse(null);
                        if(Objects.isNull(ored)){
                            List<LinkedHashMap> linkedHashMaps = new ArrayList<>();
                            linkedHashMaps.add(linkedHashMap);
                            map.put("children",linkedHashMaps);
                            nodeData.add(map);
                        }else {
                            List<LinkedHashMap> children = (List<LinkedHashMap>) ored.get("children");
                            children.add(linkedHashMap);
                        }
                    }
                }
                nodeDataList = nodeData;
            }
        }
        return AjaxResult.success(nodeDataList);
    }

    public void packageTreeNodeVo(TreeNodeVo parentNode, List<TreeNodeVo> treeNodeList) {
        List<TreeNodeVo> childrenNode = new ArrayList<>();
        // 根节点的子一级节点设置
        for (TreeNodeVo treeNode : treeNodeList) {
            if (treeNode.getParentId() != null && treeNode.getParentId().equals(parentNode.getId())) {
                childrenNode.add(treeNode);
            }
        }
        packageChildTreeNodeVo(parentNode, childrenNode);

        // 之后的子节点设置
        List<TreeNodeVo> nextLevelNodeList = parentNode.getChildren();
        if (!CollectionUtils.isEmpty(nextLevelNodeList)) {
            for (TreeNodeVo nextLevelNode : nextLevelNodeList) {
                packageTreeNodeVo(nextLevelNode, treeNodeList);
            }
        }
    }
    /**
     * 组装传入的节点的子节点信息
     *
     * @param parent
     * @param childrenNode
     */
    public void packageChildTreeNodeVo(TreeNodeVo parent, List<TreeNodeVo> childrenNode) {
        // 子节点设置
        List<TreeNodeVo> childrenNodeList = new ArrayList<>();
        for (TreeNodeVo treeNode : childrenNode) {
            TreeNodeVo child = new TreeNodeVo();
            child = ModelConvertUtil.modelConvert(treeNode, TreeNodeVo.class);
            childrenNodeList.add(child);
        }
        parent.setChildren(childrenNodeList);
    }


}
