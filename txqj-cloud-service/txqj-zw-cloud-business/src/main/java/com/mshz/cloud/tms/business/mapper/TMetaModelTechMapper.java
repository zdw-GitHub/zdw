package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.vo.MetadataTechInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 技术元模型属性Mapper接口
 *
 * @author zxx
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelTechMapper
{
    /**
     * 根据id查询技术元模型信息
     * @param modelId

     * @return
     */
    MetadataTechInfoVo selectTMetaModelTechById(@Param("modelId")Long modelId);


    /**
     * 查询技术元模型属性列表
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    public List<TMetaModelTech> selectTMetaModelTechList(@Param("modelId") Long modelId, @Param("columnName") String columnName,@Param("modelVersion") String modelVersion);
    /**
     * 查询技术元模型属性列表-校验
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    public List<TMetaModelTech> selectTMetaModelTechOne(@Param("modelId") Long modelId, @Param("columnName") String columnName,@Param("modelVersion") String modelVersion);

    /**
     * tMetaModelTech
     * @param tMetaModelTech
     * @return
     */
    public int insertTMetaModelTech(@Param("t") TMetaModelTech tMetaModelTech);

    /**
     * 修改技术元模型属性
     *
     * @param tMetaModelTech 技术元模型属性
     * @return 结果
     */
    public int updateTMetaModelTech(TMetaModelTech tMetaModelTech);

    /**
     * 删除技术元模型属性
     *
     * @param id 技术元模型属性主键
     * @return 结果
     */
    public int deleteTMetaModelTechById(Long id);

    /**
     * 批量删除技术元模型属性
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelTechByIds(Long[] ids);

    /**
     * 查询技术属性类型int，char
     * @return
     */
    List<String> selectTMetaModelTechType();

    /**
     * 技术元模型属性表复制，根据modelId和版本号
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<TMetaModelTech> selectMetaModelTechListCopy(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     * 技术元模型属性表复制-新增技术元模型属性集合
     * @param tMetaModelTechList
     * @return
     */
    int insertTMetaModelTechList(@Param("list") List<TMetaModelTech> tMetaModelTechList);
}
