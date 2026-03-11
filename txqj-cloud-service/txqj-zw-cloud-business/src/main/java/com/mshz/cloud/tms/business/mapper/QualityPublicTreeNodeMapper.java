package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNode;
import com.mshz.cloud.tms.business.vo.TreeNodeVo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共树形组件-树节点Mapper接口
 *
 * @author ruoyi
 * @date 2023-05-29
 */
public interface QualityPublicTreeNodeMapper {

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
     * 删除公共树形组件-树节点
     *
     * @param id 公共树形组件-树节点主键
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeById(Long id);

    /**
     * 批量删除公共树形组件-树节点
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeByIds(Long[] ids);

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
     * 根据传入节点查询其直接子节点信息
     * @param parentNode
     * @return
     */
    public QualityPublicTreeNode selectTreeNodeByParent(QualityPublicTreeNode parentNode);


    /**
     * 根据传入节点查询其所有父级节点信息
     * @param lastNodeId
     * @return
     */
    public List<TreeNodeVo> getTreeNodeListByLastNode(Long lastNodeId);

    List<QualityPublicTreeNode> getTreeNodeListByTreeID(long treeId);

    LinkedHashMap nodeDataSqlMap(String sql);

    List<LinkedHashMap> nodeDataSql(String sql);
}
