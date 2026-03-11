package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNode;
import com.mshz.cloud.tms.business.vo.TreeNodeVo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共树形组件-树节点Service接口
 *
 * @author ruoyi
 * @date 2023-05-29
 */
public interface IQualityPublicTreeNodeService {

    /**
     * 查询公共树形组件-树节点
     *
     * @param id 公共树形组件-树节点主键
     * @return 公共树形组件-树节点
     */
    public QualityPublicTreeNode selectQualityPublicTreeNodeById(Long id);

    /**
     * 查询公共树形组件-树节点列表
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 公共树形组件-树节点集合
     */
    public List<QualityPublicTreeNode> selectQualityPublicTreeNodeList(QualityPublicTreeNode qualityPublicTreeNode);

    /**
     * 新增公共树形组件-树节点
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 结果
     */
    public int insertQualityPublicTreeNode(QualityPublicTreeNode qualityPublicTreeNode);

    /**
     * 修改公共树形组件-树节点
     *
     * @param qualityPublicTreeNode 公共树形组件-树节点
     * @return 结果
     */
    public int updateQualityPublicTreeNode(QualityPublicTreeNode qualityPublicTreeNode);

    /**
     * 批量删除公共树形组件-树节点
     *
     * @param ids 需要删除的公共树形组件-树节点主键集合
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeByIds(Long[] ids);

    /**
     * 删除公共树形组件-树节点信息
     *
     * @param id 公共树形组件-树节点主键
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeById(Long id);

    /**
     * @param qualityPublicTreeNode
     * @return
     */
    public List<QualityPublicTreeNode> selectTreeNodeList(QualityPublicTreeNode qualityPublicTreeNode);

    /**
     * @param qualityPublicTreeNode
     * @return
     */
    public List<Map<String, String>> selectTreeNodeListByParam(QualityPublicTreeNode qualityPublicTreeNode);

    /**
     * 根据根节点及树所有节点信息数据，组装资源树节点
     * @param parentNode
     * @param treeNodeList
     */
    public void packageTreeNodeVo(TreeNodeVo parentNode, List<QualityPublicTreeNode> treeNodeList);

    /**
     * 组装传入的节点的子节点信息
     * @param parent
     * @param childrenNode
     */
    public void packageChildTreeNodeVo(TreeNodeVo parent, List<QualityPublicTreeNode> childrenNode);

    /**
     * 根据根节点及树所有节点信息数据，组装资源树节点
     * @param parentNode
     * @param treeNodeMapList
     */
    public void packageTreeNodeVoByMap(TreeNodeVo parentNode, List<Map<String, String>> treeNodeMapList);

    /**
     * 组装传入的节点的子节点信息
     * @param parent
     * @param childrenNodeMap
     */
    public void packageChildTreeNodeVoByMap(TreeNodeVo parent, List<Map<String, String>> childrenNodeMap);

    /**
     * 根据传入节点查询其直接子节点信息
     * @param parentNode
     * @return
     */
    public QualityPublicTreeNode selectTreeNodeByParent(QualityPublicTreeNode parentNode);

    /**
     * 执行节点数据查询sql
     * @param sql
     * @return
     */
    public List<LinkedHashMap> nodeDataSql(String sql);

    /**
     * 根据传入节点查询其所有父级节点信息
     * @param lastNodeId
     * @return
     */
    public List<TreeNodeVo> getTreeNodeListByLastNode(Long lastNodeId);

    List<QualityPublicTreeNode> getTreeNodeListByTreeID(long treeId);

    LinkedHashMap nodeDataSqlMap(String string);
}
