package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.MetaModelRuleParam;
import com.mshz.cloud.tms.business.mapper.MetaModelRuleParamMapper;
import com.mshz.cloud.tms.business.util.ModifyDataBatchUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.service.IMetaModelRuleParamService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元模型字段规则参数Service业务层处理
 *
 * @author zdw
 * @date 2023-08-17
 */
@Service
public class MetaModelRuleParamServiceImpl implements IMetaModelRuleParamService {

    @Resource
    private MetaModelRuleParamMapper metaModelRuleParamMapper;

    @Resource
    private ModifyDataBatchUtil batchUtil;

    /**
     * 查询元模型字段规则参数
     *
     * @param id 元模型字段规则参数主键
     * @return 元模型字段规则参数
     */
    @Override
    public MetaModelRuleParam selectMetaModelRuleParamById(Long id)
    {
        return metaModelRuleParamMapper.selectMetaModelRuleParamById(id);
    }

    /**
     * 查询元模型字段规则参数列表
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 元模型字段规则参数
     */
    @Override
    public List<MetaModelRuleParam> selectMetaModelRuleParamList(MetaModelRuleParam metaModelRuleParam)
    {
        return metaModelRuleParamMapper.selectMetaModelRuleParamList(metaModelRuleParam);
    }

    /**
     * 新增元模型字段规则参数
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 结果
     */
    @Override
    public int insertMetaModelRuleParam(MetaModelRuleParam metaModelRuleParam)
    {
        return metaModelRuleParamMapper.insertMetaModelRuleParam(metaModelRuleParam);
    }

    /**
     * 修改元模型字段规则参数
     *
     * @param metaModelRuleParam 元模型字段规则参数
     * @return 结果
     */
    @Override
    public int updateMetaModelRuleParam(MetaModelRuleParam metaModelRuleParam)
    {
        return metaModelRuleParamMapper.updateMetaModelRuleParam(metaModelRuleParam);
    }

    /**
     * 批量删除元模型字段规则参数
     *
     * @param ids 需要删除的元模型字段规则参数主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleParamByIds(Long[] ids)
    {
        return metaModelRuleParamMapper.deleteMetaModelRuleParamByIds(ids);
    }

    /**
     * 删除元模型字段规则参数信息
     *
     * @param id 元模型字段规则参数主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleParamById(Long id)
    {
        return metaModelRuleParamMapper.deleteMetaModelRuleParamById(id);
    }

    /**
     * 元模型字段规则参数信息批量入库
     *
     * @param modelRuleId
     * @param ruleParamList
     * @return
     */
    @Override
    public int saveOrUpdateRuleParamBatch(Long modelRuleId, List<MetaModelRuleParam> ruleParamList) {
        int returnNum = 0;

        int newDataNum = 0;
        int updateDataNum = 0;
        int deleteDataNum = 0;
        int noChangeDataNum = 0;

        List<MetaModelRuleParam> newRuleParamList = new ArrayList<>();
        List<MetaModelRuleParam> updateRuleParamList = new ArrayList<>();
        List<MetaModelRuleParam> deleteRuleParamList = new ArrayList<>();

        // 查询当前元模型字段数据的参数信息
        MetaModelRuleParam param = new MetaModelRuleParam();
        param.setModelRuleId(modelRuleId);
        List<MetaModelRuleParam> currentRuleParamList = metaModelRuleParamMapper.selectMetaModelRuleParamList(param);

        if (CollectionUtils.isEmpty(currentRuleParamList)) {
            ruleParamList.forEach(ruleParam -> ruleParam.setId(IdUtil.getSnowflakeNextId()));//
            // 库中没有当前字段规则的参数信息，数据新增入库
            newDataNum = ruleParamList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(ruleParamList, MetaModelRuleParamMapper.class,
                    (ruleParam, metaModelRuleParamMapper) -> metaModelRuleParamMapper.insertMetaModelRuleParam(ruleParam));
        } else {
            // 库中存在当前字段规则的参数信息，数据比对入库
            // 本次新增规则参数
            newRuleParamList = ruleParamList.stream().filter(newData -> currentRuleParamList.stream().noneMatch(currentData ->
                            newData.getParamTableId().equals(currentData.getParamTableId())
                                    && newData.getParamColumnId()!=null&& newData.getParamColumnId().equals(currentData.getParamColumnId())
                                    && newData.getParamCondition()!=null&& newData.getParamCondition().equals(currentData.getParamCondition())
                    )
            ).collect(Collectors.toList());
            newRuleParamList.forEach(newRuleParam -> newRuleParam.setId(IdUtil.getSnowflakeNextId()));//

            // 本次更新规则参数
            updateRuleParamList = ruleParamList.stream().filter(newData -> currentRuleParamList.stream().anyMatch(currentData -> {
                        boolean t = false;
                        if (newData.getParamTableId().equals(currentData.getParamTableId())
                                && newData.getParamColumnId()!=null&&newData.getParamColumnId().equals(currentData.getParamColumnId())
                                && newData.getParamCondition()!=null&& newData.getParamCondition().equals(currentData.getParamCondition())) {
                            newData.setId(currentData.getId());//
                            newData.setModelRuleId(currentData.getModelRuleId());
                            newData.setCreateDate(currentData.getCreateDate());
                            t = true;
                        }
                        return t;
                    })
            ).collect(Collectors.toList());
            // 本次编辑中删除的规则参数
            deleteRuleParamList = currentRuleParamList.stream().filter(currentData -> ruleParamList.stream().noneMatch(newData ->
                            newData.getParamTableId().equals(currentData.getParamTableId())
                                    && newData.getParamColumnId()!=null&& newData.getParamColumnId().equals(currentData.getParamColumnId())
                                    && newData.getParamCondition()!=null&& newData.getParamCondition().equals(currentData.getParamCondition())
                    )
            ).collect(Collectors.toList());

            // 本次编辑，规则参数新增、更新及删除
            newDataNum = newRuleParamList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(newRuleParamList, MetaModelRuleParamMapper.class,
                    (ruleParam, metaModelRuleParamMapper) -> metaModelRuleParamMapper.insertMetaModelRuleParam(ruleParam));
            updateDataNum = updateRuleParamList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(updateRuleParamList, MetaModelRuleParamMapper.class,
                    (ruleParam, metaModelRuleParamMapper) -> metaModelRuleParamMapper.updateMetaModelRuleParam(ruleParam));
            deleteDataNum = deleteRuleParamList.isEmpty() ? 0
                    : batchUtil.batchInsertOrUpdate(deleteRuleParamList, MetaModelRuleParamMapper.class,
                    (ruleParam, metaModelRuleParamMapper) -> metaModelRuleParamMapper.deleteMetaModelRuleParamById(ruleParam.getId()));

            // 没有变动的数据
            noChangeDataNum = currentRuleParamList.size() - newDataNum - updateDataNum - deleteDataNum;
        }

        returnNum = newDataNum + updateDataNum + deleteDataNum;
        if (returnNum == 0 && noChangeDataNum == currentRuleParamList.size()) {
            returnNum = noChangeDataNum;
        }
        return returnNum;
    }
}
