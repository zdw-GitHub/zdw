package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.vo.QualityPublicTreeInfo;

import java.util.List;

/**
 * 公共树形组件-资源数据树信息Mapper接口
 *
 * @author ruoyi
 * @date 2023-05-29
 */
public interface QualityPublicTreeInfoMapper
{
    /**
     * 查询公共树形组件-资源数据树信息
     *
     * @param id 公共树形组件-资源数据树信息主键
     * @return 公共树形组件-资源数据树信息
     */
    public QualityPublicTreeInfo selectQualityPublicTreeInfoById(Long id);

    /**
     * 查询公共树形组件-资源数据树信息列表
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 公共树形组件-资源数据树信息集合
     */
    public List<QualityPublicTreeInfo> selectQualityPublicTreeInfoList(QualityPublicTreeInfo qualityPublicTreeInfo);

    /**
     * 新增公共树形组件-资源数据树信息
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 结果
     */
    public int insertQualityPublicTreeInfo(QualityPublicTreeInfo qualityPublicTreeInfo);

    /**
     * 修改公共树形组件-资源数据树信息
     *
     * @param qualityPublicTreeInfo 公共树形组件-资源数据树信息
     * @return 结果
     */
    public int updateQualityPublicTreeInfo(QualityPublicTreeInfo qualityPublicTreeInfo);

    /**
     * 删除公共树形组件-资源数据树信息
     *
     * @param id 公共树形组件-资源数据树信息主键
     * @return 结果
     */
    public int deleteQualityPublicTreeInfoById(Long id);

    /**
     * 批量删除公共树形组件-资源数据树信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQualityPublicTreeInfoByIds(Long[] ids);
}
