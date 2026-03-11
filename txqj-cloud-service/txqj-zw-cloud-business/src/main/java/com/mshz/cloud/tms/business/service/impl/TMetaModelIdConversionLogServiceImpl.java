package com.mshz.cloud.tms.business.service.impl;

import com.mshz.cloud.tms.business.entity.TMetaModelIdConversionLog;
import com.mshz.cloud.tms.business.vo.TMetaModelIdConversionLogParaVo;
import com.mshz.cloud.tms.business.mapper.TMetaModelIdConversionLogMapper;
import com.mshz.cloud.tms.business.service.ITMetaModelIdConversionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * id中转换日志Service业务层处理
 *
 * @author zdw
 * @date 2023-08-09
 */
@Service
public class TMetaModelIdConversionLogServiceImpl implements ITMetaModelIdConversionLogService
{
    @Autowired
    private TMetaModelIdConversionLogMapper tMetaModelIdConversionLogMapper;

    /**
     * 查询id中转换日志
     *
     * @param id id中转换日志主键
     * @return id中转换日志
     */
    @Override
    public TMetaModelIdConversionLog selectTMetaModelIdConversionLogById(Long id)
    {
        return tMetaModelIdConversionLogMapper.selectTMetaModelIdConversionLogById(id);
    }

    /**
     * 查询id中转换日志列表
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return id中转换日志
     */
    @Override
    public List<TMetaModelIdConversionLog> selectTMetaModelIdConversionLogList(TMetaModelIdConversionLogParaVo tMetaModelIdConversionLog)
    {
        return tMetaModelIdConversionLogMapper.selectTMetaModelIdConversionLogList(tMetaModelIdConversionLog);
    }

    /**
     * 新增id中转换日志
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return 结果
     */
    @Override
    public int insertTMetaModelIdConversionLog(TMetaModelIdConversionLog tMetaModelIdConversionLog)
    {
        return tMetaModelIdConversionLogMapper.insertTMetaModelIdConversionLog(tMetaModelIdConversionLog);
    }

    /**
     * 修改id中转换日志
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return 结果
     */
    @Override
    public int updateTMetaModelIdConversionLog(TMetaModelIdConversionLog tMetaModelIdConversionLog)
    {
        return tMetaModelIdConversionLogMapper.updateTMetaModelIdConversionLog(tMetaModelIdConversionLog);
    }

    /**
     * 批量删除id中转换日志
     *
     * @param ids 需要删除的id中转换日志主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelIdConversionLogByIds(Long[] ids)
    {
        return tMetaModelIdConversionLogMapper.deleteTMetaModelIdConversionLogByIds(ids);
    }

    /**
     * 删除id中转换日志信息
     *
     * @param id id中转换日志主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelIdConversionLogById(Long id)
    {
        return tMetaModelIdConversionLogMapper.deleteTMetaModelIdConversionLogById(id);
    }
}
