package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaSourceUid;

import java.util.List;
import java.util.Map;

/**
 * 元模型对应采集源数据唯一标识 Service接口
 *
 * @author zdw
 * @date 2023-03-02
 */
public interface ITMetaSourceUidService {

    /**
     * 查询元模型对应采集源数据唯一标识
     *
     * @param id 元模型对应采集源数据唯一标识 主键
     * @return 元模型对应采集源数据唯一标识
     */
    public TMetaSourceUid selectTMetaSourceUidById(Long id);

    /**
     * 查询元模型对应采集源数据唯一标识 列表
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 元模型对应采集源数据唯一标识 集合
     */
    public List<TMetaSourceUid> selectTMetaSourceUidList(TMetaSourceUid tMetaSourceUid);

    /**
     * 新增元模型对应采集源数据唯一标识
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 结果
     */
    public int insertTMetaSourceUid(TMetaSourceUid tMetaSourceUid);

    /**
     * 修改元模型对应采集源数据唯一标识
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 结果
     */
    public int updateTMetaSourceUid(TMetaSourceUid tMetaSourceUid);

    /**
     * 批量删除元模型对应采集源数据唯一标识
     *
     * @param ids 需要删除的元模型对应采集源数据唯一标识 主键集合
     * @return 结果
     */
    public int deleteTMetaSourceUidByIds(Long[] ids);

    /**
     * 删除元模型对应采集源数据唯一标识 信息
     *
     * @param id 元模型对应采集源数据唯一标识 主键
     * @return 结果
     */
    public int deleteTMetaSourceUidById(Long id);

    /**
     * 根据元模型id查询该元模型数据转换对应的所有采集源模型的 唯一表示所有字段
     * @param modelId   元模型id
     * @return
     */
    public List<TMetaSourceUid> selectTMetaSourceUidListByMetaId(Long modelId);

    /**
     * 根据源数据表id查询源数据表信息
     * @param tableId
     * @return
     */
    public Map<String, String> selectSourceTableInfo(Long tableId);

    /**
     * 根据元模型id查询该元模型字段关联的源数据表信息
     * @param modelId
     * @return
     */
    public List<Map<String, Object>> selectSourceTableInfoByRelation(Long modelId);

    /**
     * 根据元模型id查询该元模型数据转换对应的所有采集源模型的 唯一表示所有字段
     * @param modelId   元模型id
     * @return
     */
    public List<Map<String, String>> selectMetaSourceUidListByMetaId(Long modelId);

    /**
     * 根据表id获取元模型转换映射的源数据表字段详细信息
     * @param tableId
     * @return
     */
    public List<Map<String, String>> selectSourceColumnInfoByTableId(Long tableId);

    /**
     * 批量新增或更新传入元模型id映射的源数据模型表唯一标识数据
     * @param modelId
     * @param tableId
     * @param sourceModelUidList
     * @return
     */
    public int saveOrUpdateDataBatch(Long modelId, Long tableId, List<TMetaSourceUid> sourceModelUidList);

}
