package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegBVo;
import java.util.List;

/**
 * 光缆段基本信息Service接口
 *
 * @author ruoyi
 * @date 2023-03-07
 */
public interface ISgTcdevFibersegBVoService {

    /**
     * 查询光缆段基本信息列表
     *
     * @param sgTcdevFibersegBVo 光缆段基本信息
     * @return 光缆段基本信息集合
     */
    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListSlave(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListSlaveNotIn(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    /**
     * 新增光缆段基本信息
     *
     * @param sgTcdevFibersegBVo 光缆段基本信息
     * @return 结果
     */
    public int insertSgTcdevFibersegB(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    /**
     * 修改光缆段基本信息
     *
     * @param sgTcdevFibersegBVo 光缆段基本信息
     * @return 结果
     */
    public int updateSgTcdevFibersegB(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    /**
     * 批量删除光缆段基本信息
     *
     * @param ids 需要删除的光缆段基本信息主键集合
     * @return 结果
     */
    public int deleteSgTcdevFibersegBByIds(String[] ids);

    /**
     * 删除光缆段基本信息信息
     *
     * @param sgTcdevFibersegBVo 光缆段基本信息主键
     * @return 结果
     */
    public int deleteSgTcdevFibersegB(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListMeta(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListMetaNotIn(SgTcdevFibersegBVo sgTcdevFibersegBVo);

    List<SgTcdevFibersegBVo> getSegListByIds(String idstr, String notinidstr, SgTcdevFibersegBVo sgTcdevFibersegBVo);

    List<SgTcdevFibersegBVo> selectAllFibersegListMeta(SgTcdevFibersegBVo sgTcdevFibersegBVo);
    SgTcdevFibersegBVo selectSgTcdevFibersegBById(String id);

}
