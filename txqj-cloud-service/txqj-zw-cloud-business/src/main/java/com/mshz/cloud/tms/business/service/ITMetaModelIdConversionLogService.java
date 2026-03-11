package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.entity.TMetaModelIdConversionLog;
import com.mshz.cloud.tms.business.vo.TMetaModelIdConversionLogParaVo;

import java.util.List;

/**
 * id中转换日志Service接口
 *
 * @author zdw
 * @date 2023-08-09
 */
public interface ITMetaModelIdConversionLogService
{
    /**
     * 查询id中转换日志
     *
     * @param id id中转换日志主键
     * @return id中转换日志
     */
    public TMetaModelIdConversionLog selectTMetaModelIdConversionLogById(Long id);

    /**
     * 查询id中转换日志列表
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return id中转换日志集合
     */
    public List<TMetaModelIdConversionLog> selectTMetaModelIdConversionLogList(TMetaModelIdConversionLogParaVo tMetaModelIdConversionLog);

    /**
     * 新增id中转换日志
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return 结果
     */
    public int insertTMetaModelIdConversionLog(TMetaModelIdConversionLog tMetaModelIdConversionLog);

    /**
     * 修改id中转换日志
     *
     * @param tMetaModelIdConversionLog id中转换日志
     * @return 结果
     */
    public int updateTMetaModelIdConversionLog(TMetaModelIdConversionLog tMetaModelIdConversionLog);

    /**
     * 批量删除id中转换日志
     *
     * @param ids 需要删除的id中转换日志主键集合
     * @return 结果
     */
    public int deleteTMetaModelIdConversionLogByIds(Long[] ids);

    /**
     * 删除id中转换日志信息
     *
     * @param id id中转换日志主键
     * @return 结果
     */
    public int deleteTMetaModelIdConversionLogById(Long id);
}
