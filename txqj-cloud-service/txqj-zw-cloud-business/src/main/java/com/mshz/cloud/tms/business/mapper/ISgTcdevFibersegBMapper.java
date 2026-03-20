package com.mshz.cloud.tms.business.mapper;


import com.mshz.cloud.tms.business.entity.SgTcdevFiberlineB;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegB;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegRFiber;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegBPo;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegRFiberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 光缆与光缆段关系Service接口
 *
 * @author ruoyi
 * @date 2023-04-17
 */

@Mapper
public interface ISgTcdevFibersegBMapper {
    /**
     * 新增光缆与光缆段关系
     *
     * @return 结果
     */
    public int insertSgTcdevFibersegRFiber(SgTcdevFibersegRFiber sgTcdevFibersegRFiber);

    /**
     * 删除光缆与光缆段关系信息
     *
     * @param objVo 光缆与光缆段关系
     * @return 结果
     */
    public int deleteSgTcdevFibersegRFiberById(SgTcdevFibersegRFiberVo objVo);

    List<SgTcdevFibersegRFiberVo> getSegRFiber(SgTcdevFibersegRFiberVo ObjVo);

    String getSegIdStrRFiber(String fiberId);

    int insertSgTcdevFibersegRFiberAll(List<SgTcdevFibersegRFiberVo> list);

    int insertSgTcdevFibersegB(SgTcdevFibersegB sTcdevFibersegB);

    void insertSgTcdevFiberlineB(SgTcdevFiberlineB line);

    SgTcdevFibersegB selectSgTcdevFibersegBById(String segId);

    int updateSgTcdevFibersegB(SgTcdevFibersegB paraSeg);

    void deleteSgTcdevFiberlineBByParFiberSegOrParFiber(SgTcdevFiberlineB sgTcdevFiberlineB);

    int deleteSgTcdevFibersegBById(String id);

    void deleteSgTcdevFibersegRFiberByFiberIdOrFibersegId(SgTcdevFibersegRFiber segRFFiberPara);

    List<SgTcdevFibersegB> selectSegListBYIds(SgTcdevFibersegBPo sgTcdevFibersegBPo);

    List<SgTcdevFibersegRFiber> selectSgTcdevFibersegRFiberList(SgTcdevFibersegRFiberVo objVo);
}
