package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TLabel;
import com.mshz.cloud.tms.business.entity.TMetaModelReLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 元模型关联标签Mapper接口
 *
 * @author zdw
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelReLabelMapper
{
    /**
     * 查询元模型关联标签
     *
     * @param id 元模型关联标签主键
     * @return 元模型关联标签
     */
    public TMetaModelReLabel selectTMetaModelReLabelById(Long id);

    /**
     * 查询元模型关联标签列表
     *
     * @param tMetaModelReLabel 元模型关联标签
     * @return 元模型关联标签集合
     */
    public List<TMetaModelReLabel> selectTMetaModelReLabelList(TMetaModelReLabel tMetaModelReLabel);

    /**
     * 新增元模型关联标签
     *
     * @param tMetaModelReLabel 元模型关联标签
     * @return 结果
     */
    public int insertTMetaModelReLabel(TMetaModelReLabel tMetaModelReLabel);

    /**
     * 修改元模型关联标签
     *
     * @param tMetaModelReLabel 元模型关联标签
     * @return 结果
     */
    public int updateTMetaModelReLabel(TMetaModelReLabel tMetaModelReLabel);

    /**
     * 删除元模型关联标签
     *
     * @param id 元模型关联标签主键
     * @return 结果
     */
    public int deleteTMetaModelReLabelById(Long id);

    /**
     * 批量删除元模型关联标签
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelReLabelByIds(Long[] ids);

    /**
     * 元模型关联标签表复制，根据modelId和版本号
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<TMetaModelReLabel> selectTMetaModelReLabelListCopy(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     * 元模型关联标签表复制-新增元模型关联标签表集合
     * @param tMetaModelReLabelList
     * @return
     */
    int insertTMetaModelReLabelList(@Param("list")List<TMetaModelReLabel> tMetaModelReLabelList);

    /**
     *  根据便签id填入标签名称到标签关联表
     * @param id
     * @param labelName
     * @return
     */
    int updateLabelNameByLabelId(@Param("labelId")Long id, @Param("labelName")String labelName);

    /**
     *  查询已经删除的标签
     * @return
     */
    List<TLabel> selectIsDeleteLabelList();
    /**
     *  根据id查询业务元数据标签列表
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<Long>  selectModelLabelById(@Param("modelId")Long modelId, @Param("modelVersion")String modelVersion);

    /**
     * 清空以前标签关联表记录
     * @param modelId
     * @return
     */
    int deleteReLabel(@Param("modelId")Long modelId);
}
