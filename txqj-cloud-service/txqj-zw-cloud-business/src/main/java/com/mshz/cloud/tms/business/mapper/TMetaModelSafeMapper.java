package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.entity.TMetaModelSafe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 安全元数据Mapper接口
 *
 * @author zxx
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelSafeMapper
{
    /**
     * 查询安全元数据
     *
     * @param id 安全元数据主键
     * @return 安全元数据
     */
    public TMetaModelSafe selectTMetaModelSafeById(Long id);

    /**
     * 查询安全元数据列表
     *
     * @param tMetaModelSafe 安全元数据
     * @return 安全元数据集合关心
     */
    public List<TMetaModelSafe> selectTMetaModelSafeList(TMetaModelSafe tMetaModelSafe);

    /**
     * 新增安全元数据
     *
     * @param tMetaModelSafe 安全元数据
     * @return 结果
     */
    public int insertTMetaModelSafe(TMetaModelSafe tMetaModelSafe);

    /**
     * 修改安全元数据
     *
     * @param tMetaModelSafe 安全元数据
     * @return 结果
     */
    public int updateTMetaModelSafe(TMetaModelSafe tMetaModelSafe);

    /**
     * 删除安全元数据
     *
     * @param id 安全元数据主键
     * @return 结果
     */
    public int deleteTMetaModelSafeById(Long id);

    /**
     * 批量删除安全元数据
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMetaModelSafeByIds(Long[] ids);

    /**
     *  安全元数据表复制，根据modelId和版本号
     * @param modelId
     * @param modelVersion
     * @return
     */
    List<TMetaModelSafe> selectMetaModelSafeListCopy(@Param("modelId") Long modelId, @Param("modelVersion") String modelVersion);

    /**
     *  安全元数据表复制-新增安全元数据集合
     * @param tMetaModelSafeList
     * @return
     */
    int insertTMetaModelSafeList(@Param("list") List<TMetaModelSafe> tMetaModelSafeList);
}
