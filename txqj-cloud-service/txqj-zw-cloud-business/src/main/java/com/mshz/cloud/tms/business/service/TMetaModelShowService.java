package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import com.mshz.cloud.tms.business.vo.MetaDataSyncVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowUpdateResultVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/29
 */

public interface TMetaModelShowService {

    /**
     * 获取数据同步基础模型数据
     *
     * @param metaModelShowVo
     * @return
     */
    public List<MetaDataSyncVo> selectMetaDataSyncVoList(MetaModelShowVo metaModelShowVo);

    public List<MetaDataSyncVo> selectMetaDataSyncVoListFiber(MetaModelShowVo metaModelShowVo);

    /**
     * 查询展示配置
     *
     * @param id 展示配置主键
     * @return 展示配置
     */
    public MetaModelShowVo selectTMetaModelShowById(Long id);

    /**
     * 查询展示配置列表带有属性信息
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo);


    /**
     * 查询展示配置列表带有属性信息新
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    public List<MetaModelShowVo> selectMetaModelShowVoListNew(MetaModelShowVo metaModelShowVo);

    /**
     * 查询展示配置列表
     *
     * @param tMetaModelShow 展示配置
     * @return 展示配置集合
     */
    public List<TMetaModelShow> selectTMetaModelShowList(TMetaModelShow tMetaModelShow);

    /**
     * 新增展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    public int insertTMetaModelShow(TMetaModelShow tMetaModelShow);

    /**
     * 修改展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    public MetaModelShowUpdateResultVo updateTMetaModelShow(TMetaModelShow tMetaModelShow);

    /**
     * 批量删除展示配置
     *
     * @param ids 需要删除的展示配置主键集合
     * @return 结果
     */
    public int deleteTMetaModelShowByIds(Long[] ids);

    /**
     * 删除展示配置信息
     *
     * @param id 展示配置主键
     * @return 结果
     */
    public int deleteTMetaModelShowById(Long id);

    /**
     * 查询配置基本信息
     * @param
     * @return
     */
    Map<String, String> selectBasicInformation(String modelId);
}
