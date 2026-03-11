package com.mshz.cloud.tms.business.service.impl;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.mapper.TLabelMetaMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelReLabelMapper;
import com.mshz.cloud.tms.business.entity.TLabel;
import com.mshz.cloud.tms.business.service.TLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 标签Service业务层处理
 *
 * @author zxx
 * @date 2022-09-22
 */
@Service
public class TLabelServiceImpl  implements TLabelService
{
    @Autowired
    private TLabelMetaMapper tLabelMetaMapper;
    @Autowired
    private TMetaModelReLabelMapper tMetaModelReLabelMapper;

    /**
     * 查询标签
     *
     * @param id 标签主键
     * @return 标签
     */
    @Override
    public TLabel selectTLabelById(Long id)
    {
        return tLabelMetaMapper.selectTLabelById(id);
    }

    /**
     * 查询标签列表
     *
     * @param tLabel 标签
     * @return 标签
     */
    @Override
    public List<TLabel> selectTLabelList(TLabel tLabel)
    {
        return tLabelMetaMapper.selectTLabelList(tLabel);
    }

    /**
     * 新增标签
     *
     * @param tLabel 标签
     * @return 结果
     */
    @Override
    public int insertTLabel(TLabel tLabel)
    {

        tLabel.setCreateDate(DateUtil.now());
        tLabel.setId(IdUtil.getSnowflakeNextId());
        return tLabelMetaMapper.insertTLabel(tLabel);
    }

    /**
     * 修改标签
     *
     * @param tLabel 标签
     * @return 结果
     */
    @Override
    public int updateTLabel(TLabel tLabel)
    {
        tLabel.setUpdateDate(DateUtil.now());
        return tLabelMetaMapper.updateTLabel(tLabel);
    }

    /**
     * 批量删除标签
     *
     * @param ids 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteTLabelByIds(Long[] ids)
    {
        return tLabelMetaMapper.deleteTLabelByIds(ids);
    }

    /**
     * 删除标签信息
     *
     * @param id 标签主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTLabelById(Long id)
    {
        TLabel tLabel = tLabelMetaMapper.selectTLabelById(id);
        int a = tLabelMetaMapper.deleteTLabelById(id);
       int b =tMetaModelReLabelMapper.updateLabelNameByLabelId(id,tLabel.getLabelName());
        return a+b;
    }

    @Override
    public String checkTLabelCodeUnique(String labelCode) {

        return tLabelMetaMapper.checkTLabelCodeUnique(labelCode);
    }

    @Override
    public String checkTLabelNameUnique(String labelName) {
        return tLabelMetaMapper.checkTLabelNameUnique(labelName);
    }

    /**
     * 查询标签名称及其id
     * @return
     */
    @Override
    public List<TLabel> selectNameId() {
        List<TLabel> labelList = tLabelMetaMapper.selectNameId();
        List<TLabel> isDeleteLabelList =  tMetaModelReLabelMapper.selectIsDeleteLabelList();
        labelList.addAll(isDeleteLabelList);
        return labelList;
    }
}
