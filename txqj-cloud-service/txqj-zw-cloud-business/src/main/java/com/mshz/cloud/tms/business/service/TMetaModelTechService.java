package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.vo.MetadataTechInfoVo;

import java.util.List;


/**
 * 技术元模型属性Service接口
 *
 * @author zxx
 * @date 2022-09-23
 */
public interface TMetaModelTechService
{
    /**
     * 根据id查询技术元模型信息
     * @param modelId
     * @param modelVersion
     * @return
     */
    MetadataTechInfoVo selectTMetaModelTechById(Long modelId, String modelVersion);


    /**
     * 查询技术元模型属性列表
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    public List<TMetaModelTech> selectTMetaModelTechList(Long modelId, String columnName, String modelVersion);
    /**
     * 查询技术元模型属性列表-校验
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    public List<TMetaModelTech> selectTMetaModelTechOne(Long modelId, String columnName, String modelVersion);

    /**
     *  新增技术元模型属性
     * @param tMetaModelTech
     * @param modelId
     * @param tableName
     * @return
     */
    public int insertTMetaModelTech(TMetaModelTech tMetaModelTech, Long modelId, String tableName);

    /**
     * 修改技术元模型属性
     *
     * @param tMetaModelTech 技术元模型属性
     * @param tableName
     * @param oldColumnName
     * @return 结果
     */
    public int updateTMetaModelTech(TMetaModelTech tMetaModelTech, String tableName, String oldColumnName) ;

    public int updateTMetaModelTechToData(TMetaModelTech tMetaModelTech) ;

    /**
     * 批量删除技术元模型属性
     *
     * @param ids 需要删除的技术元模型属性主键集合
     * @return 结果
     */
    public int deleteTMetaModelTechByIds(Long[] ids);

    /**
     * 删除技术元模型属性信息
     *
     * @param id 技术元模型属性主键
     * @param tableName
     * @param columnName
     * @return 结果
     */
    public int deleteTMetaModelTechById(Long id,String modelId, String tableName, String columnName);

    /**
     * 根据id保存技术元模型信息
     * @param modelId
     * @param metadataTechInfoVo
     * @return
     */
    int keepTMetaModelBuzById(Long modelId, MetadataTechInfoVo metadataTechInfoVo);

    /**
     * 查询技术属性类型int，char
     * @return
     */
    List<String> selectTMetaModelTechType();

}
