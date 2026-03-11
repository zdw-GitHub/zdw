package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.mapper.QualityPublicTreeModelSetMapper;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.vo.QualityPublicTreeModelSet;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeModelSetService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 公共树形组件-元模型树形组件配置Service业务层处理
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Service
public class QualityPublicTreeModelSetServiceImpl implements IQualityPublicTreeModelSetService {

    @Resource
    private QualityPublicTreeModelSetMapper qualityPublicTreeModelSetMapper;

    /**
     * 查询公共树形组件-元模型树形组件配置
     *
     * @param id 公共树形组件-元模型树形组件配置主键
     * @return 公共树形组件-元模型树形组件配置
     */
    @Override
    public QualityPublicTreeModelSet selectQualityPublicTreeModelSetById(Long id)
    {
        return qualityPublicTreeModelSetMapper.selectQualityPublicTreeModelSetById(id);
    }

    /**
     * 查询公共树形组件-元模型树形组件配置列表
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 公共树形组件-元模型树形组件配置
     */
    @Override
    public List<QualityPublicTreeModelSet> selectQualityPublicTreeModelSetList(QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        return qualityPublicTreeModelSetMapper.selectQualityPublicTreeModelSetList(qualityPublicTreeModelSet);
    }

    /**
     * 新增公共树形组件-元模型树形组件配置
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 结果
     */
    @Override
    public int insertQualityPublicTreeModelSet(QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        qualityPublicTreeModelSet.setCreateTime(DateUtils.getNowDate());
        return qualityPublicTreeModelSetMapper.insertQualityPublicTreeModelSet(qualityPublicTreeModelSet);
    }

    /**
     * 修改公共树形组件-元模型树形组件配置
     *
     * @param qualityPublicTreeModelSet 公共树形组件-元模型树形组件配置
     * @return 结果
     */
    @Override
    public int updateQualityPublicTreeModelSet(QualityPublicTreeModelSet qualityPublicTreeModelSet)
    {
        qualityPublicTreeModelSet.setUpdateTime(DateUtils.getNowDate());
        return qualityPublicTreeModelSetMapper.updateQualityPublicTreeModelSet(qualityPublicTreeModelSet);
    }

    /**
     * 批量删除公共树形组件-元模型树形组件配置
     *
     * @param ids 需要删除的公共树形组件-元模型树形组件配置主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeModelSetByIds(Long[] ids)
    {
        return qualityPublicTreeModelSetMapper.deleteQualityPublicTreeModelSetByIds(ids);
    }

    /**
     * 删除公共树形组件-元模型树形组件配置信息
     *
     * @param id 公共树形组件-元模型树形组件配置主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeModelSetById(Long id)
    {
        return qualityPublicTreeModelSetMapper.deleteQualityPublicTreeModelSetById(id);
    }

    /**
     * 根据元模型树形节点配置查询对应信息
     *
     * @param param
     * @return
     */
    @Override
    public QualityPublicTreeModelSet selectTreeNodeModelSetByTech(QualityPublicTreeModelSet param) {
        return qualityPublicTreeModelSetMapper.selectTreeNodeModelSetByTech(param);
    }

    /**
     * 根据父级信息查询节点与元模型字段关联关系
     *
     * @param param
     * @return
     */
    @Override
    public QualityPublicTreeModelSet selectTreeNodeModelSetByPNode(Map<String, String> param) {
        return qualityPublicTreeModelSetMapper.selectTreeNodeModelSetByPNode(param);
    }
}
