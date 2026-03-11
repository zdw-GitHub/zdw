package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签Mapper接口
 *
 * @author zxx
 * @date 2022-09-22
 */
@Mapper
public interface TLabelMetaMapper
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
     * 删除标签
     *
     * @param id 标签主键
     * @return 结果
     */
    public int deleteTLabelById(Long id);

    /**
     * 批量删除标签
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTLabelByIds(Long[] ids);

    /**
     * 校验标签编码是否唯一
     * @param labelCode
     * @return
     */
    public  String checkTLabelCodeUnique(@Param("labelCode") String labelCode);

    /**
     * 校验标签名字是否唯一
     * @param labelName
     * @return
     */
    public String checkTLabelNameUnique(@Param("labelName")String labelName);

    /**
     * 查询标签名称及其id
     * @return
     */
    List<TLabel> selectNameId();
}
