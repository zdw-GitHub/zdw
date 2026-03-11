package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.common.util.StringUtils;
import com.mshz.cloud.tms.business.controller.ResourceRelationQueryController;
import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import com.mshz.cloud.tms.business.entity.TMetaModelShowStyle;
import com.mshz.cloud.tms.business.vo.MetaDataSyncVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowUpdateResultVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.mapper.TMetaModelShowMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelShowStyleMapper;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.service.TMetaModelShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/29
 */
@Service
public class TMetaModelShowServiceImpl implements TMetaModelShowService {
    @Resource
    private TMetaModelShowMapper tMetaModelShowMapper;
    @Resource
    private TMetaModelShowStyleMapper tMetaModelShowStyleMapper;

    private static final Logger log = LoggerFactory.getLogger(TMetaModelShowServiceImpl.class);

    @Override
    public List<MetaDataSyncVo> selectMetaDataSyncVoList(MetaModelShowVo metaModelShowVo) {
        return tMetaModelShowMapper.selectMetaDataSyncVoList(metaModelShowVo);
    }

    @Override
    public List<MetaDataSyncVo> selectMetaDataSyncVoListFiber(MetaModelShowVo metaModelShowVo) {
        return tMetaModelShowMapper.selectMetaDataSyncVoListFiber(metaModelShowVo);
    }

    /**
     * 查询展示配置
     *
     * @param id 展示配置主键
     * @return 展示配置
     */
    @Override
    public MetaModelShowVo selectTMetaModelShowById(Long id) {
        return tMetaModelShowMapper.selectTMetaModelShowById(id);
    }

    /**
     * 查询展示配置列表带有属性信息
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    @Override
    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo) {
        return tMetaModelShowMapper.selectMetaModelShowVoList(metaModelShowVo);
    }

    /**
     * 查询展示配置列表带有属性信息新
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    @Override
    public List<MetaModelShowVo> selectMetaModelShowVoListNew(MetaModelShowVo metaModelShowVo) {
        return tMetaModelShowMapper.selectMetaModelShowVoListNew(metaModelShowVo);
    }


    /**
     * 查询展示配置列表
     *
     * @param tMetaModelShow 展示配置
     * @return 展示配置
     */
    @Override
    public List<TMetaModelShow> selectTMetaModelShowList(TMetaModelShow tMetaModelShow) {
        return tMetaModelShowMapper.selectTMetaModelShowList(tMetaModelShow);
    }

    @Override
    public Map<String, String> selectBasicInformation(String modelId) {
        return tMetaModelShowMapper.selectBasicInformation(modelId);
    }

    /**
     * 新增展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    @Override
    public int insertTMetaModelShow(TMetaModelShow tMetaModelShow) {
        Date d = new Date();
        String dateStr = DateUtil.now();
        tMetaModelShow.setCreateDate(dateStr);
        tMetaModelShow.setUpdateDate(dateStr);
        return tMetaModelShowMapper.insertTMetaModelShow(tMetaModelShow);
    }

    /**
     * 修改展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    @Override
    public MetaModelShowUpdateResultVo updateTMetaModelShow(TMetaModelShow tMetaModelShow) {
        MetaModelShowUpdateResultVo result = new MetaModelShowUpdateResultVo();
        result.setSuccess(false);

        // 1. 前置参数校验
        if (tMetaModelShow == null || tMetaModelShow.getModelId() == null || tMetaModelShow.getColumnId() == null) {
            result.setErrorMsg("更新失败：模型ID/列ID不能为空");
            return result;
        }

        try {
            // 2. 设置更新时间
            tMetaModelShow.setUpdateDate(DateUtil.now());

            // 3. 删除旧样式
            int deleteCount = tMetaModelShowStyleMapper.deleteTMetaModelShowStyleByModelId(
                    tMetaModelShow.getModelId(), tMetaModelShow.getColumnId());
            result.setDeleteStyleCount(deleteCount);

            // 4. 新增新样式
            int insertCount = 0;
            List<TMetaModelShowStyle> styleList = tMetaModelShow.getStyleList();
            if (styleList != null && !styleList.isEmpty()) {
                styleList = styleList.stream().map(m -> {
                    m.setId(IdUtil.getSnowflakeNextId());
                    m.setModelId(tMetaModelShow.getModelId());
                    m.setColumnId(tMetaModelShow.getColumnId());
                    m.setModelVersion(tMetaModelShow.getModelVersion());
                    return m;
                }).collect(Collectors.toList());
                insertCount = tMetaModelShowStyleMapper.insertTMetaModelShowStyleList(styleList);
                result.setInsertStyleCount(insertCount);
            }

            // 5. 更新主表
            int updateCount = tMetaModelShowMapper.updateTMetaModelShow(tMetaModelShow);
            result.setUpdateCount(updateCount);

            // 6. 标记成功状态
            result.setSuccess(updateCount > 0);
            if (result.isSuccess()) {
                result.setErrorMsg(null);
            } else {
                result.setErrorMsg("更新失败：主表配置未更新");
            }

        } catch (Exception e) {
            result.setErrorMsg("更新失败：" + e.getMessage());
        }

        return result;
    }



    /**
     * 批量删除展示配置
     *
     * @param ids 需要删除的展示配置主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelShowByIds(Long[] ids) {
        return tMetaModelShowMapper.deleteTMetaModelShowByIds(ids);
    }

    /**
     * 删除展示配置信息
     *
     * @param id 展示配置主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelShowById(Long id) {
        return tMetaModelShowMapper.deleteTMetaModelShowById(id);
    }
}
