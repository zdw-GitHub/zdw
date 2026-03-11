package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaSourceUid;
import com.mshz.cloud.tms.business.mapper.TMetaSourceUidMapper;
import com.mshz.cloud.tms.business.util.ModifyDataBatchUtil;
import com.mshz.cloud.tms.business.service.ITMetaSourceUidService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元模型对应采集源数据唯一标识 Service业务层处理
 *
 * @author zdw
 * @date 2023-03-02
 */
@Service
public class TMetaSourceUidServiceImpl implements ITMetaSourceUidService {

    @Resource
    private TMetaSourceUidMapper tMetaSourceUidMapper;

    @Resource
    private ModifyDataBatchUtil batchUtil;

    /**
     * 查询元模型对应采集源数据唯一标识
     *
     * @param id 元模型对应采集源数据唯一标识 主键
     * @return 元模型对应采集源数据唯一标识
     */
    @Override
    public TMetaSourceUid selectTMetaSourceUidById(Long id)
    {
        return tMetaSourceUidMapper.selectTMetaSourceUidById(id);
    }

    /**
     * 查询元模型对应采集源数据唯一标识 列表
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 元模型对应采集源数据唯一标识
     */
    @Override
    public List<TMetaSourceUid> selectTMetaSourceUidList(TMetaSourceUid tMetaSourceUid)
    {
        return tMetaSourceUidMapper.selectTMetaSourceUidList(tMetaSourceUid);
    }

    /**
     * 新增元模型对应采集源数据唯一标识
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 结果
     */
    @Override
    public int insertTMetaSourceUid(TMetaSourceUid tMetaSourceUid)
    {
        return tMetaSourceUidMapper.insertTMetaSourceUid(tMetaSourceUid);
    }

    /**
     * 修改元模型对应采集源数据唯一标识
     *
     * @param tMetaSourceUid 元模型对应采集源数据唯一标识
     * @return 结果
     */
    @Override
    public int updateTMetaSourceUid(TMetaSourceUid tMetaSourceUid)
    {
        return tMetaSourceUidMapper.updateTMetaSourceUid(tMetaSourceUid);
    }

    /**
     * 批量删除元模型对应采集源数据唯一标识
     *
     * @param ids 需要删除的元模型对应采集源数据唯一标识 主键
     * @return 结果
     */
    @Override
    public int deleteTMetaSourceUidByIds(Long[] ids)
    {
        return tMetaSourceUidMapper.deleteTMetaSourceUidByIds(ids);
    }

    /**
     * 删除元模型对应采集源数据唯一标识 信息
     *
     * @param id 元模型对应采集源数据唯一标识 主键
     * @return 结果
     */
    @Override
    public int deleteTMetaSourceUidById(Long id)
    {
        return tMetaSourceUidMapper.deleteTMetaSourceUidById(id);
    }

    /**
     * 根据元模型id查询该元模型数据转换对应的所有采集源模型的 唯一表示所有字段
     *
     * @param modelId 元模型id
     * @return
     */
    @Override
    public List<TMetaSourceUid> selectTMetaSourceUidListByMetaId(Long modelId) {
        return tMetaSourceUidMapper.selectTMetaSourceUidListByMetaId(modelId);
    }

    /**
     * 根据源数据表id查询源数据表信息
     * @param tableId
     * @return
     */
    @Override
    public Map<String, String> selectSourceTableInfo(Long tableId) {
        return tMetaSourceUidMapper.selectSourceTableInfo(tableId);
    }

    /**
     * 根据元模型id查询该元模型字段关联的源数据表信息
     *
     * @param modelId
     * @return
     */
    @Override
    public List<Map<String, Object>> selectSourceTableInfoByRelation(Long modelId) {
        return tMetaSourceUidMapper.selectSourceTableInfoByRelation(modelId);
    }

    /**
     * 根据元模型id查询该元模型数据转换对应的所有采集源模型的 唯一表示所有字段
     *
     * @param modelId 元模型id
     * @return
     */
    @Override
    public List<Map<String, String>> selectMetaSourceUidListByMetaId(Long modelId) {
        return tMetaSourceUidMapper.selectMetaSourceUidListByMetaId(modelId);
    }

    /**
     * 根据表id获取元模型转换映射的源数据表字段详细信息
     *
     * @param tableId
     * @return
     */
    @Override
    public List<Map<String, String>> selectSourceColumnInfoByTableId(Long tableId) {
        return tMetaSourceUidMapper.selectSourceColumnInfoByTableId(tableId);
    }

    /**
     * 批量新增或更新传入元模型id映射的源数据模型表唯一标识数据
     *
     * @param modelId
     * @param tableId
     * @param sourceModelUidList
     * @return
     */
    @Override
    public int saveOrUpdateDataBatch(Long modelId, Long tableId, List<TMetaSourceUid> sourceModelUidList) {
        int returnNum = 0;

        int newDataNum = 0;
        int updateDataNum = 0;
        int deleteDataNum = 0;
        int noChangeDataNum = 0;

        List<TMetaSourceUid> newDataList;
        List<TMetaSourceUid> updateDataList;
        List<TMetaSourceUid> deleteDataList;

        // 查询该源数据模型当前的唯一标识数据
        TMetaSourceUid queryParam = new TMetaSourceUid();
        queryParam.setTableId(tableId);
        List<TMetaSourceUid> currentDataList = tMetaSourceUidMapper.selectTMetaSourceUidList(queryParam);
        if (CollectionUtils.isEmpty(currentDataList)) {
            // 该源数据模型当前不存在唯一标识数据，直接新增
            newDataNum = sourceModelUidList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(sourceModelUidList, TMetaSourceUidMapper.class, (sourceModelUid, tMetaSourceUidMapper) -> tMetaSourceUidMapper.insertTMetaSourceUid(sourceModelUid));
            returnNum = newDataNum;
        } else {
            // 该源数据模型当前存在唯一标识数据，需要更新为本次传入数据
            // 新增组成唯一标识字段数据（当前没有的字段）
            newDataList = sourceModelUidList.stream().filter(newData -> currentDataList.stream().noneMatch(currentData ->
                    newData.getColumnId().equals(currentData.getColumnId())
            )).collect(Collectors.toList());
            // 更新组成唯一标识字段数据（字段顺序有所调整）
            updateDataList = currentDataList.stream().filter(currentData -> sourceModelUidList.stream().anyMatch(newData -> {
                        boolean t = false;
                        if (newData.getColumnId().equals(currentData.getColumnId())
                                && !newData.getColumnOrder().equals(currentData.getColumnOrder())) {
                            currentData.setColumnOrder(newData.getColumnOrder());
                            currentData.setColumnName(newData.getColumnName());
                            currentData.setColumnNameCn(newData.getColumnNameCn());
                            t = true;
                        }
                        return t;
                    }
            )).collect(Collectors.toList());
            // 删除的已经取消的唯一标识字段数据（本次传入数据没有的字段）
            deleteDataList = currentDataList.stream().filter(currentData -> sourceModelUidList.stream().noneMatch(newData ->
                    newData.getColumnId().equals(currentData.getColumnId())
            )).collect(Collectors.toList());

            // 没有变动的数据（字段顺序未调整）
            noChangeDataNum = currentDataList.size() - newDataNum - updateDataNum - deleteDataNum;

            newDataNum = newDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(newDataList, TMetaSourceUidMapper.class, (sourceModelUid, tMetaSourceUidMapper) -> tMetaSourceUidMapper.insertTMetaSourceUid(sourceModelUid));
            updateDataNum = updateDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(updateDataList, TMetaSourceUidMapper.class, (sourceModelUid, tMetaSourceUidMapper) -> tMetaSourceUidMapper.updateTMetaSourceUid(sourceModelUid));
            deleteDataNum = deleteDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(deleteDataList, TMetaSourceUidMapper.class, (sourceModelUid, tMetaSourceUidMapper) -> tMetaSourceUidMapper.deleteTMetaSourceUidById(sourceModelUid.getId()));

            returnNum = newDataNum + updateDataNum + deleteDataNum;
            if (returnNum == 0 && noChangeDataNum == currentDataList.size()) {
                returnNum = noChangeDataNum;
            }

        }

        return returnNum;
    }

}
