package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNode;
import com.mshz.cloud.tms.business.mapper.QualityPublicTreeNodeMapper;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeService;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import com.mshz.cloud.tms.business.vo.TreeNodeVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共树形组件-树节点Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@Service
public class QualityPublicTreeNodeServiceImpl implements IQualityPublicTreeNodeService {

    @Resource
    private QualityPublicTreeNodeMapper qualityPublicTreeNodeMapper;

    /**
     * 查询公共树形组件-树节点
     *
     * @param id 公共树形组件-树节点主键
     * @return 公共树形组件-树节点
     */
    @Override
    public QualityPublicTreeNode selectQualityPublicTreeNodeById(Long id)
    {
        return qualityPublicTreeNodeMapper.selectQualityPublicTreeNodeById(id);
    }

    /**
     * 查询公共树形组件-树节点列表
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 公共树形组件-树节点
     */
    @Override
    public List<QualityPublicTreeNode> selectQualityPublicTreeNodeList(QualityPublicTreeNode qualityPublicTreeNode)
    {
        return qualityPublicTreeNodeMapper.selectQualityPublicTreeNodeList(qualityPublicTreeNode);
    }

    /**
     * 新增公共树形组件-树节点
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 结果
     */
    @Override
    public int insertQualityPublicTreeNode(QualityPublicTreeNode qualityPublicTreeNode)
    {
        qualityPublicTreeNode.setCreateTime(DateUtils.getNowDate());
        return qualityPublicTreeNodeMapper.insertQualityPublicTreeNode(qualityPublicTreeNode);
    }

    /**
     * 修改公共树形组件-树节点
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 结果
     */
    @Override
    public int updateQualityPublicTreeNode(QualityPublicTreeNode qualityPublicTreeNode)
    {
        qualityPublicTreeNode.setUpdateTime(DateUtils.getNowDate());
        return qualityPublicTreeNodeMapper.updateQualityPublicTreeNode(qualityPublicTreeNode);
    }

    /**
     * 批量删除公共树形组件-树节点
     *
     * @param ids 需要删除的公共树形组件-树节点主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeNodeByIds(Long[] ids)
    {
        return qualityPublicTreeNodeMapper.deleteQualityPublicTreeNodeByIds(ids);
    }

    /**
     * 删除公共树形组件-树节点信息
     *
     * @param id 公共树形组件-树节点主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeNodeById(Long id)
    {
        return qualityPublicTreeNodeMapper.deleteQualityPublicTreeNodeById(id);
    }

    /**
     * @param qualityPublicTreeNode
     * @return
     */
    @Override
    public List<QualityPublicTreeNode> selectTreeNodeList(QualityPublicTreeNode qualityPublicTreeNode) {
        return qualityPublicTreeNodeMapper.selectTreeNodeList(qualityPublicTreeNode);
    }

    /**
     * @param qualityPublicTreeNode
     * @return
     */
    @Override
    public List<Map<String, String>> selectTreeNodeListByParam(QualityPublicTreeNode qualityPublicTreeNode) {
        return qualityPublicTreeNodeMapper.selectTreeNodeListByParam(qualityPublicTreeNode);
    }

    /**
     * 根据根节点及树所有节点信息数据，组装资源树节点
     *
     * @param parentNode
     * @param treeNodeList
     */
    @Override
    public void packageTreeNodeVo(TreeNodeVo parentNode, List<QualityPublicTreeNode> treeNodeList) {

        List<QualityPublicTreeNode> childrenNode = new ArrayList<>();
        // 根节点的子一级节点设置
        for (QualityPublicTreeNode treeNode : treeNodeList) {
            if (treeNode.getParentId() != null && treeNode.getParentId().equals(Long.parseLong(parentNode.getId()))) {
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
    @Override
    public void packageChildTreeNodeVo(TreeNodeVo parent, List<QualityPublicTreeNode> childrenNode) {
        // 子节点设置
        List<TreeNodeVo> childrenNodeList = new ArrayList<>();
        for (QualityPublicTreeNode treeNode : childrenNode) {
            TreeNodeVo child = new TreeNodeVo();
            child = ModelConvertUtil.modelConvert(treeNode, TreeNodeVo.class);
            childrenNodeList.add(child);
        }
        parent.setChildren(childrenNodeList);
    }

    /**
     * 根据根节点及树所有节点信息数据，组装资源树节点
     *
     * @param parentNode
     * @param treeNodeMapList
     */
    @Override
    public void packageTreeNodeVoByMap(TreeNodeVo parentNode, List<Map<String, String>> treeNodeMapList) {
        List<Map<String, String>> childrenNode = new ArrayList<>();
        // 根节点的子一级节点设置
        for (Map<String, String> treeNodeMap : treeNodeMapList) {
            if (treeNodeMap.get("parentId") != null && String.valueOf(treeNodeMap.get("parentId")).equals(parentNode.getId())) {
                childrenNode.add(treeNodeMap);
            }
        }
        packageChildTreeNodeVoByMap(parentNode, childrenNode);

        // 之后的子节点设置
        List<TreeNodeVo> nextLevelNodeList = parentNode.getChildren();
        if (!CollectionUtils.isEmpty(nextLevelNodeList)) {
            for (TreeNodeVo nextLevelNode : nextLevelNodeList) {
                packageTreeNodeVoByMap(nextLevelNode, treeNodeMapList);
            }
        }
    }

    /**
     * 组装传入的节点的子节点信息
     *
     * @param parent
     * @param childrenNodeMap
     */
    @Override
    public void packageChildTreeNodeVoByMap(TreeNodeVo parent, List<Map<String, String>> childrenNodeMap) {
        // 子节点设置
        List<TreeNodeVo> childrenNodeList = new ArrayList<>();
        for (Map<String, String> treeNodeMap : childrenNodeMap) {
            TreeNodeVo child = ModelConvertUtil.mapConvert(treeNodeMap, TreeNodeVo.class);
            childrenNodeList.add(child);
        }
        parent.setChildren(childrenNodeList);
    }

    /**
     * 根据传入节点查询其直接子节点信息
     *
     * @param parentNode
     * @return
     */
    @Override
    public QualityPublicTreeNode selectTreeNodeByParent(QualityPublicTreeNode parentNode) {
        return qualityPublicTreeNodeMapper.selectTreeNodeByParent(parentNode);
    }

    /**
     * 执行节点数据查询sql
     *
     * @param sql
     * @return
     */
    @Override
    public List<LinkedHashMap> nodeDataSql(String sql) {
        return qualityPublicTreeNodeMapper.nodeDataSql(sql);
    }

    /**
     * 根据传入节点查询其所有父级节点信息
     *
     * @param lastNodeId
     * @return
     */
    @Override
    public List<TreeNodeVo> getTreeNodeListByLastNode(Long lastNodeId) {
        return qualityPublicTreeNodeMapper.getTreeNodeListByLastNode(lastNodeId);
    }

    @Override
    public List<QualityPublicTreeNode> getTreeNodeListByTreeID(long treeId) {
        return qualityPublicTreeNodeMapper.getTreeNodeListByTreeID(treeId);
    }

    @Override
    public LinkedHashMap nodeDataSqlMap(String sql) {
        return qualityPublicTreeNodeMapper.nodeDataSqlMap(sql);
    }
}
