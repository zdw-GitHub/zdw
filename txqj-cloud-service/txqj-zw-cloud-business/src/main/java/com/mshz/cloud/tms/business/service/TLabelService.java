package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.entity.TLabel;

import java.util.List;


/**
 * 标签Service接口
 *
 * @author zxx
 * @date 2022-09-22
 */
public interface TLabelService
{
    /**
     * 查询标签
     *
     * @param id 标签主键
     * @return 标签
     */
    public TLabel selectTLabelById(Long id);

    /**
     * 查询标签列表
     *
     * @param tLabel 标签
     * @return 标签集合
     */
    public List<TLabel> selectTLabelList(TLabel tLabel);

    /**
     * 新增标签
     *
     * @param tLabel 标签
     * @return 结果
     */
    public int insertTLabel(TLabel tLabel);

    /**
     * 修改标签
     *
     * @param tLabel 标签
     * @return 结果
     */
    public int updateTLabel(TLabel tLabel);

    /**
     * 批量删除标签
     *
     * @param ids 需要删除的标签主键集合
     * @return 结果
     */
    public int deleteTLabelByIds(Long[] ids);

    /**
     * 删除标签信息
     *
     * @param id 标签主键
     * @return 结果
     */
    public int deleteTLabelById(Long id);

    /**
     * 校验标签编码是否唯一
     * @param labelCode
     * @return
     */
    String checkTLabelCodeUnique(String labelCode);

    /**
     *  校验标签名字是否唯一
     * @param labelName
     * @return
     */
    String checkTLabelNameUnique(String labelName);

    /**
     * 查询标签名称及其id
     * @return
     */
    List<TLabel> selectNameId();
}
