package com.mshz.cloud.tms.business.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.mshz.cloud.common.util.BeanUtils;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegB;
import com.mshz.cloud.tms.business.entity.SgTcdevFibersegRFiber;
import com.mshz.cloud.tms.business.mapper.ISgTcdevFibersegBMapper;
import com.mshz.cloud.tms.business.service.ISgTcdevFibersegBVoService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.util.PageUtils;
import com.mshz.cloud.tms.business.util.SnowflakeIdGenerator;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegBPo;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegBVo;
import com.mshz.cloud.tms.business.entity.SgTcdevFiberlineB;
import com.mshz.cloud.tms.business.vo.SgTcdevFibersegRFiberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 光缆段基本信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-07
 */
@Service
public class SgTcdevFibersegBVoServiceImpl implements ISgTcdevFibersegBVoService {
    @Resource
    private ISgTcdevFibersegBMapper sgTcdevFibersegBMapper;

    @Autowired
    private SnowflakeIdGenerator idGenerator;

    /**
     * 已绑定-光缆段基本信息列表-待处理（全部）
     *
     * @param sgTcdevFibersegBVo 光缆段基本信息
     * @return 光缆段基本信息
     */
    @Override
    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListSlave(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        // 1、获取光缆在正式库和临时库所有绑定的有效的光缆段id
        // 2、根据光缆段id获取正式库和临时库光缆段列表集合
        // 3、翻译字段

        List<SgTcdevFibersegBVo> resList = new ArrayList<>();

        // 1
        // 调用关联关系公共方法实现
        String fiberId = sgTcdevFibersegBVo.getFiberId();
        if (!StringUtil.isEmpty(fiberId)) {
            String idstr = getSegIdStrRFiber(fiberId);

            // 2
            // 调用光缆段列表公共方法
            resList = this.getSegListByIds(idstr, null, sgTcdevFibersegBVo);
        }

        return resList;
    }

    /***
     * 未绑定-光缆段基本信息列表-待处理（全部）
     *
     * @param sgTcdevFibersegBVo
     * @return
     */
    @Override
    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListSlaveNotIn(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        // 1、获取光缆在正式库和临时库所有绑定的有效的光缆段id
        // 2、根据光缆段id获取正式库和临时库未绑定光缆段列表集合
        // 3、翻译字段

        List<SgTcdevFibersegBVo> resList = new ArrayList<>();

        // 1
        // 调用关联关系公共方法实现
        String fiberId = sgTcdevFibersegBVo.getFiberId();
        if (!StringUtil.isEmpty(fiberId)) {
            String notinidstr = getSegIdStrRFiber(fiberId);

            // 2
            // 调用光缆段列表公共方法
            resList = this.getSegListByIds(null, notinidstr, sgTcdevFibersegBVo);
        }

        return resList;
    }

    /***
     * 已绑定-光缆段基本信息列表-正式库
     *
     * @param sgTcdevFibersegBVo
     * @return
     */
    @Override
    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListMeta(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        // 1、获取光缆在正式库和临时库所有绑定的有效的光缆段id
        // 2、根据光缆段id获取正式库已绑定光缆段
        // 3、翻译字段

        List<SgTcdevFibersegBVo> resList = new ArrayList<>();
        // 1
        // 调用关联关系公共方法实现
        String fiberId = sgTcdevFibersegBVo.getFiberId();
        if (!StringUtil.isEmpty(fiberId)) {
            String idstr = getSegIdStrRFiber(fiberId);
            // 2
            // 调用光缆段列表公共方法
            resList = this.getSegListByIdsFromMeta(idstr, null, sgTcdevFibersegBVo);
        }

        return resList;
    }

    /***
     * 未绑定-光缆段基本信息列表-正式库
     *
     * @param sgTcdevFibersegBVo
     * @return
     */
    @Override
    public List<SgTcdevFibersegBVo> selectSgTcdevFibersegBListMetaNotIn(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        // 1、获取光缆在正式库和临时库所有绑定的有效的光缆段id
        // 2、根据光缆段id获取正式库未绑定光缆段
        // 3、翻译字段

        List<SgTcdevFibersegBVo> resList = new ArrayList<>();
        // 1
        // 调用关联关系公共方法实现
        String fiberId = sgTcdevFibersegBVo.getFiberId();
        if (fiberId != null) {
            String notinidstr = getSegIdStrRFiber(fiberId);
            // 2
            // 调用光缆段列表公共方法
            resList = this.getSegListByIdsFromMeta(null, notinidstr, sgTcdevFibersegBVo);
        }

        return resList;
    }

    /***
     * 查询正式库-所有光缆段
     *
     * @param sgTcdevFibersegBVo
     * @return
     */
    @Override
    public List<SgTcdevFibersegBVo> selectAllFibersegListMeta(SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        List<SgTcdevFibersegBVo> resList = this.getSegListByIdsFromMeta(null, null, sgTcdevFibersegBVo);
        return resList;
    }

    /**
     * 新增光缆段基本信息
     *
     * @param objVo 光缆段基本信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSgTcdevFibersegB(SgTcdevFibersegBVo objVo) {
        int result = 0;
        result = insertSgTcdevFibersegBIntoMasterDB(objVo);
        return result;
    }

    private int insertSgTcdevFibersegBIntoMasterDB(SgTcdevFibersegBVo objVo) {
        // 1、新增资源数据主库
        // 2、生成光缆段与光缆关联关系
        // 3、生成光缆段纤芯
        int result = 0;
        try {
            // 1.添加光缆段
            String nowtime = DateUtils.getTime();
            String segId = idGenerator.nextId();
            objVo.setId(segId);//根据ID生成规则生成ID
            objVo.setCreateDate(nowtime);
            objVo.setUpdateDate(nowtime);
            objVo.setEditstatus("0");
            SgTcdevFibersegB sTcdevFibersegB = new SgTcdevFibersegB();
            BeanUtils.copyBeanProp(sTcdevFibersegB, objVo);
            result = sgTcdevFibersegBMapper.insertSgTcdevFibersegB(sTcdevFibersegB);
            // 2.添加光缆与光缆段关系
            SgTcdevFibersegRFiber sgTcdevFibersegRFiber = new SgTcdevFibersegRFiber();
            sgTcdevFibersegRFiber.setFiberId(objVo.getFiberId());
            sgTcdevFibersegRFiber.setFibersegId(segId);
            sgTcdevFibersegRFiber.setId(idGenerator.nextId());//根据ID生成规则生成ID
            sgTcdevFibersegRFiber.setCreateDate(nowtime);
            sgTcdevFibersegRFiber.setUpdateDate(nowtime);
            sgTcdevFibersegRFiber.setEditstatus("0");
            sgTcdevFibersegBMapper.insertSgTcdevFibersegRFiber(sgTcdevFibersegRFiber);
            // 3.生成光缆段纤芯
            Long linenum = objVo.getLineNumber();
            if (objVo != null && objVo.getLineNumber() != null) {
                for (int i = 1; i <= linenum; i++) {
                    SgTcdevFiberlineB line = new SgTcdevFiberlineB();
                    line.setParFiberSeg(segId);
                    line.setSn(Long.valueOf(i));
                    line.setParFiber(objVo.getFiberId());
                    String linename = i + "";
                    if (i <= 9) {
                        linename = "F00" + i;
                    } else if (i > 9 && i <= 99) {
                        linename = "F0" + i;
                    } else if (i > 99 && i < 999) {
                        linename = "F" + i;
                    }
                    line.setName(linename);
                    line.setId(idGenerator.nextId());//根据ID生成规则生成ID
                    line.setCreateDate(nowtime);
                    line.setUpdateDate(nowtime);
                    line.setDataCategory("0");
                    line.setEditstatus("0");
                    sgTcdevFibersegBMapper.insertSgTcdevFiberlineB(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 修改光缆段基本信息
     *
     * @param objVo 光缆段基本信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSgTcdevFibersegB(SgTcdevFibersegBVo objVo) {
        return updateSgTcdevFibersegBInMasterDB(objVo);
    }

    private int updateSgTcdevFibersegBInMasterDB(SgTcdevFibersegBVo objVo) {
        // 1、更新光缆段数据
        // 2、判断是否纤芯扩容
        // 3、生成新的纤芯
        int result = 0;
        //通过id获取库内光缆段数据
        String segId = objVo.getId();
        SgTcdevFibersegB orSeg = sgTcdevFibersegBMapper.selectSgTcdevFibersegBById(segId);
        Long orLinenum = orSeg.getLineNumber();
        // 1、更新光缆段数据
        SgTcdevFibersegB paraSeg = new SgTcdevFibersegB();
        BeanUtils.copyBeanProp(paraSeg, objVo);
        String nowtime = DateUtils.getTime();
        paraSeg.setUpdateDate(nowtime);
        paraSeg.setDataCategory(objVo.getEditstatus());
        result = sgTcdevFibersegBMapper.updateSgTcdevFibersegB(paraSeg);
        // 2、判断是否纤芯扩容
        Long linenum = objVo.getLineNumber();
        if(orLinenum!=null&&linenum!=null&&(linenum!=orLinenum)){
            // 3、生成新的纤芯
            if(linenum>orLinenum){
                SgTcdevFiberlineB sgTcdevFiberlineB = new SgTcdevFiberlineB();
                sgTcdevFiberlineB.setParFiberSeg(segId);
                long firstLineNumber = orLinenum + 1;
                for (long i = firstLineNumber; i <= linenum; i++) {
                    SgTcdevFiberlineB line = new SgTcdevFiberlineB();
                    line.setParFiberSeg(segId);
                    line.setParFiber(objVo.getFiberId());
                    line.setSn(Long.valueOf(i));
                    String linename = i + "";
                    if (i <= 9) {
                        linename = "F00" + i;
                    } else if (i > 9 && i <= 99) {
                        linename = "F0" + i;
                    } else if (i > 99 && i < 999) {
                        linename = "F" + i;
                    }
                    line.setName(linename);
                    line.setId(idGenerator.nextId());//根据ID生成规则生成ID
                    line.setCreateDate(nowtime);
                    line.setUpdateDate(nowtime);
                    line.setDataCategory("0");
                    line.setEditstatus("0");
                    sgTcdevFibersegBMapper.insertSgTcdevFiberlineB(line);
                }
            }else {//删除多的纤芯

                long firstLineNumber = linenum + 1;
                for (long i = firstLineNumber; i <= orLinenum; i++) {
                    SgTcdevFiberlineB sgTcdevFiberlineB = new SgTcdevFiberlineB();
                    sgTcdevFiberlineB.setParFiberSeg(segId);
                    sgTcdevFiberlineB.setSn(i);
                    sgTcdevFibersegBMapper.deleteSgTcdevFiberlineBByParFiberSegOrParFiber(sgTcdevFiberlineB);
                }
            }

        }
        return result;
    }

    /**
     * 删除光缆段基本信息信息
     *
     * @param objVo 光缆段基本信息主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSgTcdevFibersegB(SgTcdevFibersegBVo objVo) {
        return deleteSgTcdevFibersegBInMasterDB(objVo);
    }


    private int  deleteSgTcdevFibersegBInMasterDB(SgTcdevFibersegBVo objVo) {
        // 1、删除光缆段数据
        // 2、删除光缆与光缆段关系
        // 3、删除光缆段纤芯

        // 1、删除光缆段数据
        int result = sgTcdevFibersegBMapper.deleteSgTcdevFibersegBById(objVo.getId());
        // 2、删除光缆与光缆段关系
        SgTcdevFibersegRFiber segRFFiberPara = new SgTcdevFibersegRFiber();
        segRFFiberPara.setFibersegId(objVo.getId());
        sgTcdevFibersegBMapper.deleteSgTcdevFibersegRFiberByFiberIdOrFibersegId(segRFFiberPara);
        // 3、删除光缆段纤芯
        SgTcdevFiberlineB linePara = new SgTcdevFiberlineB();
        linePara.setParFiberSeg(objVo.getId());
        sgTcdevFibersegBMapper.deleteSgTcdevFiberlineBByParFiberSegOrParFiber(linePara);
        return result;
    }

    /**
     * 批量删除光缆段基本信息
     *
     * @param ids 需要删除的光缆段基本信息主键
     * @return 结果
     */
    @Override
    public int deleteSgTcdevFibersegBByIds(String[] ids) {
        int result = 0;
        for (String id:ids) {
            SgTcdevFibersegBVo segBVo = new SgTcdevFibersegBVo();
            segBVo.setId(id);//
            int res = deleteSgTcdevFibersegB(segBVo);
            result = result + res;
        }
        return result;
    }

    // ###################################以下为公共方法 方便调用#########################################################

    /***
     * 根据光缆段id串，和光缆段过滤条件获取正式库和临时库光缆段列表集合
     *
     * @param idstr
     * @param sgTcdevFibersegBVo
     * @return
     */
    @Override
    public List<SgTcdevFibersegBVo> getSegListByIds(String idstr, String notinidstr, SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        List<SgTcdevFibersegBVo> resList = new ArrayList<>();
        if (idstr != null || notinidstr != null) {
            SgTcdevFibersegBPo sgTcdevFibersegBPo = new SgTcdevFibersegBPo();
            if (sgTcdevFibersegBVo != null) {
                BeanUtils.copyBeanProp(sgTcdevFibersegBPo, sgTcdevFibersegBVo);
            }
            sgTcdevFibersegBPo.setIdstr(idstr);
            sgTcdevFibersegBPo.setNotinidstr(notinidstr);
            List<SgTcdevFibersegB> list1;
            list1 = sgTcdevFibersegBMapper.selectSegListBYIds(sgTcdevFibersegBPo);
            for (SgTcdevFibersegB obj : list1) {
                SgTcdevFibersegBVo voObj = new SgTcdevFibersegBVo();
                BeanUtils.copyBeanProp(voObj, obj);
                resList.add(voObj);
            }
        }

        return resList;
    }

    /***
     * 根据光缆段id串和光缆段过滤条件获取正式库光缆段列表
     *
     * @param idstr
     * @param sgTcdevFibersegBVo
     * @return
     */
    public List<SgTcdevFibersegBVo> getSegListByIdsFromMeta(String idstr, String notinidstr, SgTcdevFibersegBVo sgTcdevFibersegBVo) {
        List<SgTcdevFibersegBVo> resList = new ArrayList<>();
        if (idstr != null || notinidstr != null) {
            SgTcdevFibersegBPo sgTcdevFibersegBPo = new SgTcdevFibersegBPo();
            if (sgTcdevFibersegBVo != null) {
                BeanUtils.copyBeanProp(sgTcdevFibersegBPo, sgTcdevFibersegBVo);
            }
            sgTcdevFibersegBPo.setIdstr(idstr);
            sgTcdevFibersegBPo.setNotinidstr(notinidstr);
            PageUtils.startPage();
            List<SgTcdevFibersegB> list1 = sgTcdevFibersegBMapper.selectSegListBYIds(sgTcdevFibersegBPo);
            for (SgTcdevFibersegB obj : list1) {
                SgTcdevFibersegBVo voObj = new SgTcdevFibersegBVo();
                BeanUtils.copyBeanProp(voObj, obj);
                resList.add(voObj);
            }
            resList = PageUtils.listToPage(resList, new PageInfo<>(list1).getTotal());
        }
        return resList;
    }

    /***
     * 正式库-更新正式库数据操作状态公共方法 方便调用
     *
     * @return
     */
    public int updateSegEditstatus(String id, String editstatus) {
        // 1、更新正式库数据-光缆段
        int res = 1;
        if (id != null && !"".equals(id)) {
            SgTcdevFibersegB obj = new SgTcdevFibersegB();
            obj.setId(id);//
            obj.setEditstatus(editstatus);
            res = sgTcdevFibersegBMapper.updateSgTcdevFibersegB(obj);
        }

        return res;
    }

    @Override
    public SgTcdevFibersegBVo selectSgTcdevFibersegBById(String id) {
        SgTcdevFibersegB sgTcdevFibersegB = sgTcdevFibersegBMapper.selectSgTcdevFibersegBById(id);
        SgTcdevFibersegBVo sgTcdevFibersegBVo = new SgTcdevFibersegBVo();
        BeanUtils.copyBeanProp(sgTcdevFibersegBVo, sgTcdevFibersegB);
        return sgTcdevFibersegBVo;
    }


    public String getSegIdStrRFiber(String fiberId) {
        // 1、获取光缆在正式库和临时库所有已绑定的有效的光缆段id
        // 2、转字符串
        SgTcdevFibersegRFiberVo ObjVo = new SgTcdevFibersegRFiberVo();
        ObjVo.setFiberId(fiberId);
        String idstr = "''";

        if (!StringUtil.isEmpty(fiberId)) {
            List<SgTcdevFibersegRFiberVo> SgTcdevFibersegRFiberVoList = this.getSegRFiber(ObjVo);

            if (SgTcdevFibersegRFiberVoList != null) {
                for (int i = 0; i < SgTcdevFibersegRFiberVoList.size(); i++) {
                    SgTcdevFibersegRFiberVo obj = SgTcdevFibersegRFiberVoList.get(i);
                    if (i == 0) {
                        idstr = "'" + obj.getFibersegId() + "'";
                    } else {
                        idstr = idstr + ",'" + obj.getFibersegId() + "'";
                    }
                }
            }
        }

        return idstr;
    }


    public List<SgTcdevFibersegRFiberVo> getSegRFiber(SgTcdevFibersegRFiberVo ObjVo) {
        // 1、获取光缆在正式库和临时库所有有效的光缆段id
        // 1.1、正式库光缆段关联关系
        // 1.2、临时库光缆段关联关系
        List<SgTcdevFibersegRFiberVo> resList = new ArrayList<>();
        List<SgTcdevFibersegRFiber> objList = sgTcdevFibersegBMapper.selectSgTcdevFibersegRFiberList(ObjVo);
        for (SgTcdevFibersegRFiber obj : objList) {
            SgTcdevFibersegRFiberVo voObj = new SgTcdevFibersegRFiberVo();
            BeanUtils.copyBeanProp(voObj, obj);
            resList.add(voObj);
        }
        return resList;
    }

}
