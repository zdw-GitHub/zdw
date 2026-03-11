package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNodeColumn;
import java.util.List;

/**
 * 公共树形组件-树节点查询数据属性Service接口
 *
 * @author ruoyi
 * @date 2023-05-29
 */
public interface IQualityPublicTreeNodeColumnService {

    /**
     * 查询公共树形组件-树节点查询数据属性
     *
     * @param id 公共树形组件-树节点查询数据属性主键
     * @return 公共树形组件-树节点查询数据属性
     */
    public QualityPublicTreeNodeColumn selectQualityPublicTreeNodeColumnById(Long id);

    /**
     * 查询公共树形组件-树节点查询数据属性列表
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 公共树形组件-树节点查询数据属性集合
     */
    public List<QualityPublicTreeNodeColumn> selectQualityPublicTreeNodeColumnList(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn);

    /**
     * 新增公共树形组件-树节点查询数据属性
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 结果
     */
    public int insertQualityPublicTreeNodeColumn(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn);

    /**
     * 修改公共树形组件-树节点查询数据属性
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 结果
     */
    public int updateQualityPublicTreeNodeColumn(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn);

    /**
     * 批量删除公共树形组件-树节点查询数据属性
     *
     * @param ids 需要删除的公共树形组件-树节点查询数据属性主键集合
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeColumnByIds(Long[] ids);

    /**
     * 删除公共树形组件-树节点查询数据属性信息
     *
     * @param id 公共树形组件-树节点查询数据属性主键
     * @return 结果
     */
    public int deleteQualityPublicTreeNodeColumnById(Long id);

    /**
     * 批量新增或更新树节点查询数据属性信息
     * @param nodeId            所属节点id
     * @param nodeColumnList    节点查询数据属性数据集合
     * @return
     */
    public int saveOrUpdateDataBatch(Long nodeId, List<QualityPublicTreeNodeColumn> nodeColumnList);
}
