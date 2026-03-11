package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.vo.QualityPublicTreeModelSet;

import java.util.List;
import java.util.Map;

/**
 * 公共树形组件-元模型树形组件配置Mapper接口
 *
 * @author ruoyi
 * @date 2023-06-02
 */
public interface QualityPublicTreeModelSetMapper
{
    /**
     * 查询公共树形组件-元模型树形组件配置
     *
     * @param id 公共树形组件-元模型树形组件配置主键
     * @return 公共树形组件-元模型树形组件配置
     */
    public QualityPublicTreeModelSet selectQualityPublicTreeModelSetById(Long id);

    /**
     * 查询公共树形组件-元模型树形组件配置列表
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 公共树形组件-元模型树形组件配置集合
     */
    public List<QualityPublicTreeModelSet> selectQualityPublicTreeModelSetList(QualityPublicTreeModelSet qualityPublicTreeModelSet);

    /**
     * 新增公共树形组件-元模型树形组件配置
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 结果
     */
    public int insertQualityPublicTreeModelSet(QualityPublicTreeModelSet qualityPublicTreeModelSet);

    /**
     * 修改公共树形组件-元模型树形组件配置
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 结果
     */
    public int updateQualityPublicTreeModelSet(QualityPublicTreeModelSet qualityPublicTreeModelSet);

    /**
     * 删除公共树形组件-元模型树形组件配置
     *
     * @param id 公共树形组件-元模型树形组件配置主键
     * @return 结果
     */
    public int deleteQualityPublicTreeModelSetById(Long id);

    /**
     * 批量删除公共树形组件-元模型树形组件配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQualityPublicTreeModelSetByIds(Long[] ids);

    /**
     * 根据元模型树形节点配置查询对应信息
     * @param param
     * @return
     */
    public QualityPublicTreeModelSet selectTreeNodeModelSetByTech(QualityPublicTreeModelSet param);

    /**
     * 根据父级信息查询节点与元模型字段关联关系
     * @param param
     * @return
     */
    public QualityPublicTreeModelSet selectTreeNodeModelSetByPNode(Map<String, String> param);

}
