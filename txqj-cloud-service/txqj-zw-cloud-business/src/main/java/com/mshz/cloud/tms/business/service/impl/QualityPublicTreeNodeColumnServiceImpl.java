package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.entity.QualityPublicTreeNodeColumn;
import com.mshz.cloud.tms.business.mapper.QualityPublicTreeNodeColumnMapper;
import com.mshz.cloud.tms.business.service.IQualityPublicTreeNodeColumnService;
import com.mshz.cloud.tms.business.util.ModifyDataBatchUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公共树形组件-树节点查询数据属性Service业务层处理
 *
 * @author ruoyi
 * @date 2023-05-29
 */
@Service
public class QualityPublicTreeNodeColumnServiceImpl implements IQualityPublicTreeNodeColumnService {

    @Resource
    private QualityPublicTreeNodeColumnMapper qualityPublicTreeNodeColumnMapper;

    @Resource
    private ModifyDataBatchUtil batchUtil;

    /**
     * 查询公共树形组件-树节点查询数据属性
     *
     * @param id 公共树形组件-树节点查询数据属性主键
     * @return 公共树形组件-树节点查询数据属性
     */
    @Override
    public QualityPublicTreeNodeColumn selectQualityPublicTreeNodeColumnById(Long id)
    {
        return qualityPublicTreeNodeColumnMapper.selectQualityPublicTreeNodeColumnById(id);
    }

    /**
     * 查询公共树形组件-树节点查询数据属性列表
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 公共树形组件-树节点查询数据属性
     */
    @Override
    public List<QualityPublicTreeNodeColumn> selectQualityPublicTreeNodeColumnList(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        return qualityPublicTreeNodeColumnMapper.selectQualityPublicTreeNodeColumnList(qualityPublicTreeNodeColumn);
    }

    /**
     * 新增公共树形组件-树节点查询数据属性
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 结果
     */
    @Override
    public int insertQualityPublicTreeNodeColumn(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        qualityPublicTreeNodeColumn.setCreateTime(DateUtils.getNowDate());
        return qualityPublicTreeNodeColumnMapper.insertQualityPublicTreeNodeColumn(qualityPublicTreeNodeColumn);
    }

    /**
     * 修改公共树形组件-树节点查询数据属性
     *
     * @param qualityPublicTreeNodeColumn 公共树形组件-树节点查询数据属性
     * @return 结果
     */
    @Override
    public int updateQualityPublicTreeNodeColumn(QualityPublicTreeNodeColumn qualityPublicTreeNodeColumn)
    {
        qualityPublicTreeNodeColumn.setUpdateTime(DateUtils.getNowDate());
        return qualityPublicTreeNodeColumnMapper.updateQualityPublicTreeNodeColumn(qualityPublicTreeNodeColumn);
    }

    /**
     * 批量删除公共树形组件-树节点查询数据属性
     *
     * @param ids 需要删除的公共树形组件-树节点查询数据属性主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeNodeColumnByIds(Long[] ids)
    {
        return qualityPublicTreeNodeColumnMapper.deleteQualityPublicTreeNodeColumnByIds(ids);
    }

    /**
     * 删除公共树形组件-树节点查询数据属性信息
     *
     * @param id 公共树形组件-树节点查询数据属性主键
     * @return 结果
     */
    @Override
    public int deleteQualityPublicTreeNodeColumnById(Long id)
    {
        return qualityPublicTreeNodeColumnMapper.deleteQualityPublicTreeNodeColumnById(id);
    }

    /**
     * 批量新增或更新树节点查询数据属性信息
     *
     * @param nodeId         所属节点id
     * @param nodeColumnList 节点查询数据属性数据集合
     * @return
     */
    @Override
    public int saveOrUpdateDataBatch(Long nodeId, List<QualityPublicTreeNodeColumn> nodeColumnList) {
        int returnNum = 0;

        int newDataNum = 0;
        int updateDataNum = 0;
        int deleteDataNum = 0;
        int noChangeDataNum = 0;

        // 新增、更新、删除的节点查询属性数据集合
        List<QualityPublicTreeNodeColumn> newDataList = new ArrayList<>();
        List<QualityPublicTreeNodeColumn> updateDataList = new ArrayList<>();
        List<QualityPublicTreeNodeColumn> deleteDataList = new ArrayList<>();

        // 查询该节点当前的查询数据属性集合
        QualityPublicTreeNodeColumn param = new QualityPublicTreeNodeColumn();
        param.setNodeId(nodeId);
        List<QualityPublicTreeNodeColumn> currentDataList = qualityPublicTreeNodeColumnMapper.selectQualityPublicTreeNodeColumnList(param);
        if (CollectionUtils.isEmpty(currentDataList)) {
            // 当前节点目前没有查询数据属性集合，新增
            nodeColumnList.forEach(nodeColumn -> {
                nodeColumn.setNodeId(nodeId);
            });
            newDataNum = nodeColumnList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(nodeColumnList, QualityPublicTreeNodeColumnMapper.class, (nodeColumn, qualityPublicTreeNodeColumnMapper) ->
                    qualityPublicTreeNodeColumnMapper.insertQualityPublicTreeNodeColumn(nodeColumn));
            returnNum = newDataNum;
        } else {
            // 当前节点目前有查询数据属性集合
            newDataList = nodeColumnList.stream().filter(newData -> currentDataList.stream().noneMatch(currentData ->
                            newData.getMetaDataColumnId().equals(currentData.getMetaDataColumnId())
                                    && newData.getNodeQueryColumn().equals(currentData.getNodeQueryColumn())
                                    && newData.getNodeQueryColumnAlias().equals(currentData.getNodeQueryColumnAlias())
                    )
            ).collect(Collectors.toList());
            newDataList.forEach(nodeColumn -> {
                nodeColumn.setNodeId(nodeId);
            });

            updateDataList = currentDataList.stream().filter(currentData -> nodeColumnList.stream().anyMatch(newData -> {
                            boolean t = false;
                            if (!newData.getNodeQueryColumn().equals(currentData.getNodeQueryColumn())
                                    && newData.getNodeQueryColumnAlias().equals(currentData.getNodeQueryColumnAlias())
                            ) {
                                currentData.setNodeQueryColumn(newData.getNodeQueryColumn());
                                t = true;
                            }
                            return t;
                        }
                    )
            ).collect(Collectors.toList());

            deleteDataList = currentDataList.stream().filter(currentData -> nodeColumnList.stream().noneMatch(newData ->
                            newData.getMetaDataColumnId().equals(currentData.getMetaDataColumnId())
                                    && newData.getNodeQueryColumn().equals(currentData.getNodeQueryColumn())
                                    && newData.getNodeQueryColumnAlias().equals(currentData.getNodeQueryColumnAlias())
                    )
            ).collect(Collectors.toList());

            newDataNum = newDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(newDataList, QualityPublicTreeNodeColumnMapper.class, (nodeColumn, qualityPublicTreeNodeColumnMapper) ->
                    qualityPublicTreeNodeColumnMapper.insertQualityPublicTreeNodeColumn(nodeColumn));
            updateDataNum = updateDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(updateDataList, QualityPublicTreeNodeColumnMapper.class, (nodeColumn, qualityPublicTreeNodeColumnMapper) ->
                    qualityPublicTreeNodeColumnMapper.updateQualityPublicTreeNodeColumn(nodeColumn));
            deleteDataNum = deleteDataList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(deleteDataList, QualityPublicTreeNodeColumnMapper.class, (nodeColumn, qualityPublicTreeNodeColumnMapper) ->
                    qualityPublicTreeNodeColumnMapper.deleteQualityPublicTreeNodeColumnById(nodeColumn.getId()));

            // 没有变动的数据
            noChangeDataNum = currentDataList.size() - newDataNum - updateDataNum - deleteDataNum;
            returnNum = newDataNum + updateDataNum + deleteDataNum;
            if (returnNum == 0 && noChangeDataNum == currentDataList.size()) {
                returnNum = noChangeDataNum;
            }

        }

        return returnNum;
    }
}
