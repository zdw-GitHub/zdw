package com.mshz.cloud.tms.business.mapper;


import com.mshz.cloud.tms.business.entity.TMetaModelCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 元数据类目Mapper接口
 *
 * @author zdw
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelCategoryMapper
{
    /**
     * 查询元数据类目
     *
     * @param id 元数据类目主键
     * @return 元数据类目
     */
    public TMetaModelCategory selectTMetaModelCategoryById(Long id);

    /**
     * 查询元数据类目列表
     *
     * @param tMetaModelCategory 元数据类目
     * @return 元数据类目集合
     */
    public List<TMetaModelCategory> selectTMetaModelCategoryList(TMetaModelCategory tMetaModelCategory);

    /**
     * 新增元数据类目
     *
     * @param tMetaModelCategory 元数据类目
     * @return 结果
     */
    public int insertTMetaModelCategory(TMetaModelCategory tMetaModelCategory);

    /**
     * 修改元数据类目
     *
     * @param tMetaModelCategory 元数据类目
     * @return 结果
     */
    public int updateTMetaModelCategory(TMetaModelCategory tMetaModelCategory);

    /**
     * 删除元数据类目
     *
     * @param id 元数据类目主键
     * @return 结果
     */
    public int deleteTMetaModelCategoryById(Long id);

    /**
     * 批量删除元数据类目
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelCategoryByIds(Long[] ids);


}
