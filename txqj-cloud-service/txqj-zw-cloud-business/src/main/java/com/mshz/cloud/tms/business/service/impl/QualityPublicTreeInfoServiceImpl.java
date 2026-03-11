package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.vo.QualityPublicTreeInfo;
import com.mshz.cloud.tms.business.mapper.QualityPublicTreeInfoMapper;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公共树形组件-资源数据树信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@Service
public class QualityPublicTreeInfoServiceImpl implements IQualityPublicTreeInfoService
{
    @Autowired
    private QualityPublicTreeInfoMapper qualityPublicTreeInfoMapper;

    /**
     * 查询公共树形组件-资源数据树信息
     *
     * @param id 公共树形组件-资源数据树信息主键
     * @return 公共树形组件-资源数据树信息
     */
    @Override
    public QualityPublicTreeInfo selectQualityPublicTreeInfoById(Long id)
    {
        return qualityPublicTreeInfoMapper.selectQualityPublicTreeInfoById(id);
    }

    /**
     * 查询公共树形组件-资源数据树信息列表
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 公共树形组件-资源数据树信息
     */
    @Override
    public List<QualityPublicTreeInfo> selectQualityPublicTreeInfoList(QualityPublicTreeInfo qualityPublicTreeInfo)
    {
        return qualityPublicTreeInfoMapper.selectQualityPublicTreeInfoList(qualityPublicTreeInfo);
    }

    /**
     * 新增公共树形组件-资源数据树信息
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 结果
     */
    @Override
    public int insertQualityPublicTreeInfo(QualityPublicTreeInfo qualityPublicTreeInfo)
    {
        qualityPublicTreeInfo.setCreateTime(DateUtils.getNowDate());
        return qualityPublicTreeInfoMapper.insertQualityPublicTreeInfo(qualityPublicTreeInfo);
    }

    /**
     * 修改公共树形组件-资源数据树信息
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 结果
     */
    @Override
    public int updateQualityPublicTreeInfo(QualityPublicTreeInfo qualityPublicTreeInfo)
    {
        qualityPublicTreeInfo.setUpdateTime(DateUtils.getNowDate());
        return qualityPublicTreeInfoMapper.updateQualityPublicTreeInfo(qualityPublicTreeInfo);
    }

    /**
     * 批量删除公共树形组件-资源数据树信息
     *
     * @param ids 需要删除的公共树形组件-资源数据树信息主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeInfoByIds(Long[] ids)
    {
        return qualityPublicTreeInfoMapper.deleteQualityPublicTreeInfoByIds(ids);
    }

    /**
     * 删除公共树形组件-资源数据树信息信息
     *
     * @param id 公共树形组件-资源数据树信息主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeInfoById(Long id)
    {
        return qualityPublicTreeInfoMapper.deleteQualityPublicTreeInfoById(id);
    }
}
