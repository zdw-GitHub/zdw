package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaTransformationConditionGroup;
import com.mshz.cloud.tms.business.mapper.TMetaTransformationConditionGroupMapper;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionGroupService;
import com.mshz.cloud.tms.business.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 转换条件组管理Service业务层处理
 *
 * @author zdw
 * @date 2023-06-24
 */
@Service
public class TMetaTransformationConditionGroupServiceImpl implements ITMetaTransformationConditionGroupService {
    @Autowired
    private TMetaTransformationConditionGroupMapper tMetaTransformationConditionGroupMapper;

    /**
     * 查询转换条件组管理
     *
     * @param id 转换条件组管理主键
     * @return 转换条件组管理
     */
    @Override
    public TMetaTransformationConditionGroup selectTMetaTransformationConditionGroupById(Long id) {
        return tMetaTransformationConditionGroupMapper.selectTMetaTransformationConditionGroupById(id);
    }

    /**
     * 查询转换条件组管理列表
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 转换条件组管理
     */
    @Override
    public List<TMetaTransformationConditionGroup> selectTMetaTransformationConditionGroupList(TMetaTransformationConditionGroup tMetaTransformationConditionGroup) {
        return tMetaTransformationConditionGroupMapper.selectTMetaTransformationConditionGroupList(tMetaTransformationConditionGroup);
    }

    /**
     * 新增转换条件组管理
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 结果
     */
    @Override
    public long insertTMetaTransformationConditionGroup(TMetaTransformationConditionGroup tMetaTransformationConditionGroup) {
        tMetaTransformationConditionGroup.setCreateTime(DateUtils.getNowDate());
        tMetaTransformationConditionGroupMapper.insertTMetaTransformationConditionGroup(tMetaTransformationConditionGroup);
        return tMetaTransformationConditionGroup.getId();
    }

    /**
     * 修改转换条件组管理
     *
     * @param tMetaTransformationConditionGroup 转换条件组管理
     * @return 结果
     */
    @Override
    public int updateTMetaTransformationConditionGroup(TMetaTransformationConditionGroup tMetaTransformationConditionGroup) {
        tMetaTransformationConditionGroup.setUpdateTime(DateUtils.getNowDate());
        return tMetaTransformationConditionGroupMapper.updateTMetaTransformationConditionGroup(tMetaTransformationConditionGroup);
    }

    /**
     * 批量删除转换条件组管理
     *
     * @param ids 需要删除的转换条件组管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionGroupByIds(Long[] ids) {
        return tMetaTransformationConditionGroupMapper.deleteTMetaTransformationConditionGroupByIds(ids);
    }

    /**
     * 删除转换条件组管理信息
     *
     * @param id 转换条件组管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionGroupById(Long id) {
        return tMetaTransformationConditionGroupMapper.deleteTMetaTransformationConditionGroupById(id);
    }
}
