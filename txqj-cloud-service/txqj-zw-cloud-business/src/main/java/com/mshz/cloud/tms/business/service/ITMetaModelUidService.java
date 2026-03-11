package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaModelUid;

import java.util.List;
import java.util.Map;

/**
 * 元模型唯一标识Service接口
 *
 * @author zdw
 * @date 2023-03-02
 */
public interface ITMetaModelUidService {
    /**
     * 查询元模型唯一标识
     *
     * @param id 元模型唯一标识主键
     * @return 元模型唯一标识
     */
    public TMetaModelUid selectTMetaModelUidById(Long id);

    /**
     * 查询元模型唯一标识列表
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 元模型唯一标识集合
     */
    public List<TMetaModelUid> selectTMetaModelUidList(TMetaModelUid tMetaModelUid);

    /**
     * 新增元模型唯一标识
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 结果
     */
    public int insertTMetaModelUid(TMetaModelUid tMetaModelUid);

    /**
     * 修改元模型唯一标识
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 结果
     */
    public int updateTMetaModelUid(TMetaModelUid tMetaModelUid);

    /**
     * 批量删除元模型唯一标识
     *
     * @param ids 需要删除的元模型唯一标识主键集合
     * @return 结果
     */
    public int deleteTMetaModelUidByIds(Long[] ids);

    /**
     * 删除元模型唯一标识信息
     *
     * @param id 元模型唯一标识主键
     * @return 结果
     */
    public int deleteTMetaModelUidById(Long id);

    /**
     * 根据元模型id查询该元模型的组成唯一表示的所有字段
     * @param modelId   元模型id
     * @return
     */
    public List<TMetaModelUid> selectTMetaModelUidListByMetaId(Long modelId);

    /**
     * 根据元模型id查询元模型信息
     * @param modelId
     * @return
     */
    public Map<String, Object> selectModelInfoByModelId(Long modelId);

    /**
     * 根据元模型id查询该元模型的组成唯一表示的所有字段信息
     * @param modelId   元模型id
     * @return
     */
    public List<Map<String, String>> selectMetaModelUidListByMetaId(Long modelId);

    /**
     * 根据元模型id查询该元模型的字段信息
     * @param modelId
     * @return
     */
    public List<Map<String, String>> selectModelColumnInfoByModelId(Long modelId);

    /**
     * 批量新增或更新传入id的元模型唯一标识数据
     * @param modelId
     * @param metaModelUidList
     * @return
     */
    public int saveOrUpdateDataBatch(Long modelId, List<TMetaModelUid> metaModelUidList);

    String getMaxIdSerial(String tableName);
}
