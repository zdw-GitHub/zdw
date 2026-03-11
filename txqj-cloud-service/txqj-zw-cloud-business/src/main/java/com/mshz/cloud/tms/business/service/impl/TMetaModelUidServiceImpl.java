package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaModelUid;
import com.mshz.cloud.tms.business.mapper.TMetaModelUidMapper;
import com.mshz.cloud.tms.business.util.ModifyDataBatchUtil;
import com.mshz.cloud.tms.business.service.ITMetaModelUidService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元模型唯一标识Service业务层处理
 *
 * @author zdw
 * @date 2023-03-02
 */
@Service
public class TMetaModelUidServiceImpl implements ITMetaModelUidService {

    @Resource
    private TMetaModelUidMapper tMetaModelUidMapper;

    @Resource
    private ModifyDataBatchUtil batchUtil;

    /**
     * 查询元模型唯一标识
     *
     * @param id 元模型唯一标识主键
     * @return 元模型唯一标识
     */
    @Override
    public TMetaModelUid selectTMetaModelUidById(Long id)
    {
        return tMetaModelUidMapper.selectTMetaModelUidById(id);
    }

    /**
     * 查询元模型唯一标识列表
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 元模型唯一标识
     */
    @Override
    public List<TMetaModelUid> selectTMetaModelUidList(TMetaModelUid tMetaModelUid)
    {
        return tMetaModelUidMapper.selectTMetaModelUidList(tMetaModelUid);
    }

    /**
     * 新增元模型唯一标识
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 结果
     */
    @Override
    public int insertTMetaModelUid(TMetaModelUid tMetaModelUid)
    {
        return tMetaModelUidMapper.insertTMetaModelUid(tMetaModelUid);
    }

    /**
     * 修改元模型唯一标识
     *
     * @param tMetaModelUid 元模型唯一标识
     * @return 结果
     */
    @Override
    public int updateTMetaModelUid(TMetaModelUid tMetaModelUid)
    {
        return tMetaModelUidMapper.updateTMetaModelUid(tMetaModelUid);
    }

    /**
     * 批量删除元模型唯一标识
     *
     * @param ids 需要删除的元模型唯一标识主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelUidByIds(Long[] ids)
    {
        return tMetaModelUidMapper.deleteTMetaModelUidByIds(ids);
    }

    /**
     * 删除元模型唯一标识信息
     *
     * @param id 元模型唯一标识主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelUidById(Long id)
    {
        return tMetaModelUidMapper.deleteTMetaModelUidById(id);
    }

    /**
     * 根据元模型id查询该元模型的组成唯一表示的所有字段
     *
     * @param modelId 元模型id
     * @return
     */
    @Override
    public List<TMetaModelUid> selectTMetaModelUidListByMetaId(Long modelId) {
        return tMetaModelUidMapper.selectTMetaModelUidListByMetaId(modelId);
    }

    /**
     * 根据元模型id查询元模型信息
     * @param modelId
     * @return
     */
    @Override
    public Map<String, Object> selectModelInfoByModelId(Long modelId) {
        return tMetaModelUidMapper.selectModelInfoByModelId(modelId);
    }

    /**
     * 根据元模型id查询该元模型的组成唯一表示的所有字段信息
     *
     * @param modelId 元模型id
     * @return
     */
    @Override
    public List<Map<String, String>> selectMetaModelUidListByMetaId(Long modelId) {
        return tMetaModelUidMapper.selectMetaModelUidListByMetaId(modelId);
    }

    /**
     * 根据元模型id查询该元模型的字段信息
     *
     * @param modelId
     * @return
     */
    @Override
    public List<Map<String, String>> selectModelColumnInfoByModelId(Long modelId) {
        return tMetaModelUidMapper.selectModelColumnInfoByModelId(modelId);
    }

    /**
     * 批量新增或更新传入id的元模型唯一标识数据
     *
     * @param modelId
     * @param metaModelUidList
     * @return
     */
    @Override
    public int saveOrUpdateDataBatch(Long modelId, List<TMetaModelUid> metaModelUidList) {
        int returnNum = 0;

        int newDataNum = 0;
        int updateDataNum = 0;
        int deleteDataNum = 0;
        int noChangeDataNum = 0;

        List<TMetaModelUid> newDataList;
        List<TMetaModelUid> updateDataList;
        List<TMetaModelUid> deleteDataList;

        // 查询该元模型当前的唯一标识数据
        List<TMetaModelUid> currentDataList = tMetaModelUidMapper.selectTMetaModelUidListByMetaId(modelId);
        if (CollectionUtils.isEmpty(currentDataList)) {
            // 该元模型当前不存在唯一标识数据，直接新增
            newDataNum = metaModelUidList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(metaModelUidList, TMetaModelUidMapper.class, (metaModelUid, tMetaModelUidMapper) -> tMetaModelUidMapper.insertTMetaModelUid(metaModelUid));
            returnNum = newDataNum;
        } else {
            // 该元模型当前存在唯一标识数据，需要更新为本次传入数据
            // 新增组成唯一标识字段数据（当前没有的字段）
            newDataList = metaModelUidList.stream().filter(newData -> currentDataList.stream().noneMatch(currentData ->
                    newData.getColumnId().equals(currentData.getColumnId())
            )).collect(Collectors.toList());

            // 更新组成唯一标识字段数据（字段顺序有所调整）
            updateDataList = currentDataList.stream().filter(currentData -> metaModelUidList.stream().anyMatch(newData -> {
                        boolean t = false;
                        if (newData.getColumnId().equals(currentData.getColumnId())
                                && !newData.getColumnOrder().equals(currentData.getColumnOrder())) {
                            currentData.setColumnOrder(newData.getColumnOrder());
                            currentData.setColumnName(newData.getColumnName());
                            currentData.setColumnNameCn(newData.getColumnNameCn());
                            currentData.setModelVersion(newData.getModelVersion());
                            t = true;
                        }
                        return t;
                    }
            )).collect(Collectors.toList());

            // 删除的已经取消的唯一标识字段数据（本次传入数据没有的字段）
            deleteDataList = currentDataList.stream().filter(currentData -> metaModelUidList.stream().noneMatch(newData ->
                    newData.getColumnId().equals(currentData.getColumnId())
            )).collect(Collectors.toList());

            newDataNum = newDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(newDataList, TMetaModelUidMapper.class, (metaModelUid, tMetaModelUidMapper) -> tMetaModelUidMapper.insertTMetaModelUid(metaModelUid));
            updateDataNum = updateDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(updateDataList, TMetaModelUidMapper.class, (metaModelUid, tMetaModelUidMapper) -> tMetaModelUidMapper.updateTMetaModelUid(metaModelUid));
            deleteDataNum = deleteDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(deleteDataList, TMetaModelUidMapper.class, (metaModelUid, tMetaModelUidMapper) -> tMetaModelUidMapper.deleteTMetaModelUidById(metaModelUid.getId()));

            // 没有变动的数据（字段顺序未调整）
            noChangeDataNum = currentDataList.size() - newDataNum - updateDataNum - deleteDataNum;
            returnNum = newDataNum + updateDataNum + deleteDataNum;
            if (returnNum == 0 && noChangeDataNum == currentDataList.size()) {
                returnNum = noChangeDataNum;
            }

        }

        return returnNum;
    }

    @Override
    public String getMaxIdSerial(String tableName) {
        return tMetaModelUidMapper.getIdGenerationRule(tableName)+tMetaModelUidMapper.getMaxIdSerial(tableName);
    }


}
