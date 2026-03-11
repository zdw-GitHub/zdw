package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import com.mshz.cloud.tms.business.vo.MetaDataSyncVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 展示配置Mapper接口
 *
 * @author zdw
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelShowMapper {

    /**
     * 获取数据同步基础模型数据
     *
     * @param metaModelShowVo
     * @return
     */
    public List<MetaDataSyncVo> selectMetaDataSyncVoList(MetaModelShowVo metaModelShowVo);

    public List<MetaDataSyncVo> selectMetaDataSyncVoListFiber(MetaModelShowVo metaModelShowVo);

    /**
     * 查询展示配置
     *
     * @param id 展示配置主键
     * @return 展示配置
     */
    public MetaModelShowVo selectTMetaModelShowById(Long id);

    /**
     * 查询展示配置列表带有属性信息
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo);


    /**
     * 查询展示配置列表带有属性信息新
     *
     * @param metaModelShowVo 展示配置
     * @return 展示配置集合带有属性信息
     */
    public List<MetaModelShowVo> selectMetaModelShowVoListNew(MetaModelShowVo metaModelShowVo);

    /**
     * 查询展示配置列表
     *
     * @param tMetaModelShow 展示配置
     * @return 展示配置集合
     */
    public List<TMetaModelShow> selectTMetaModelShowList(TMetaModelShow tMetaModelShow);

    /**
     * 新增展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    public int insertTMetaModelShow(TMetaModelShow tMetaModelShow);

    /**
     * 修改展示配置
     *
     * @param tMetaModelShow 展示配置
     * @return 结果
     */
    public int updateTMetaModelShow(TMetaModelShow tMetaModelShow);

    /**
     * 删除展示配置
     *
     * @param id 展示配置主键
     * @return 结果
     */
    public int deleteTMetaModelShowById(Long id);

    public int deleteTMetaModelShowByModelId(@Param("modelId") Long modelId, @Param("columnId") Long columnId);

    /**
     * 批量删除展示配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelShowByIds(Long[] ids);

    /**
     * 展示配置表复制，根据modelId和版本号
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<TMetaModelShow> selectMetaModelShowListCopy(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     * 展示配置表复制-新增展示配置
     *
     * @param tMetaModelShowList
     * @return
     */
    int insertTMetaModelShowList(List<TMetaModelShow> tMetaModelShowList);


    Map<String, String> selectBasicInformation(String modelId);
}
