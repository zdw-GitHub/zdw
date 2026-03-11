package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.entity.TMetaModelIdConversionLog;
import com.mshz.cloud.tms.business.entity.TMetaModelIdRule;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.vo.IDConversionReturnVo;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelIdConversionLogMapper;
import com.mshz.cloud.tms.business.mapper.TMetaModelIdRuleMapper;
import com.mshz.cloud.tms.business.service.ITMetaModelIdRuleService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模型ID生成规则配置Service业务层处理
 *
 * @author zdw
 * @date 2023-08-09
 */
@Service
public class TMetaModelIdRuleServiceImpl implements ITMetaModelIdRuleService {
    @Resource
    private TMetaModelIdRuleMapper tMetaModelIdRuleMapper;

    @Resource
    private CommonMetaMapper commonMetaMapper;

    @Resource
    private TMetaModelIdConversionLogMapper logMapper;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 查询模型ID生成规则配置
     *
     * @param id 模型ID生成规则配置主键
     * @return 模型ID生成规则配置
     */
    @Override
    public TMetaModelIdRule selectTMetaModelIdRuleById(Long id) {
        return tMetaModelIdRuleMapper.selectTMetaModelIdRuleById(id);
    }

    /**
     * 查询模型ID生成规则配置列表
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 模型ID生成规则配置
     */
    @Override
    public List<TMetaModelIdRule> selectTMetaModelIdRuleList(TMetaModelIdRule tMetaModelIdRule) {
        return tMetaModelIdRuleMapper.selectTMetaModelIdRuleList(tMetaModelIdRule);
    }

    /**
     * 查询模型ID生成规则配置列表 没有默认配置信息
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 模型ID生成规则配置集合
     */
    @Override
    public List<TMetaModelIdRule> selectTMetaModelIdRuleListWithoutDefault(TMetaModelIdRule tMetaModelIdRule) {
        return tMetaModelIdRuleMapper.selectTMetaModelIdRuleListWithoutDefault(tMetaModelIdRule);
    }

    /**
     * 新增模型ID生成规则配置
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 结果
     */
    @Override
    public int insertTMetaModelIdRule(TMetaModelIdRule tMetaModelIdRule) {
        tMetaModelIdRule.setCreateDate(DateUtils.getTime());
        tMetaModelIdRule.setUpdateDate(tMetaModelIdRule.getCreateDate());
        return tMetaModelIdRuleMapper.insertTMetaModelIdRule(tMetaModelIdRule);
    }

    /**
     * 修改模型ID生成规则配置
     *
     * @param tMetaModelIdRule 模型ID生成规则配置
     * @return 结果
     */
    @Override
    public int updateTMetaModelIdRule(TMetaModelIdRule tMetaModelIdRule) {
        tMetaModelIdRule.setUpdateDate(DateUtils.getTime());
        return tMetaModelIdRuleMapper.updateTMetaModelIdRule(tMetaModelIdRule);
    }

    /**
     * 批量删除模型ID生成规则配置
     *
     * @param ids 需要删除的模型ID生成规则配置主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelIdRuleByIds(Long[] ids) {
        return tMetaModelIdRuleMapper.deleteTMetaModelIdRuleByIds(ids);
    }

    /**
     * 删除模型ID生成规则配置信息
     *
     * @param id 模型ID生成规则配置主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelIdRuleById(Long id) {
        return tMetaModelIdRuleMapper.deleteTMetaModelIdRuleById(id);
    }

    @Override
    public IDConversionReturnVo getIDByTableName(String modelName) {
        IDConversionReturnVo returnVo = new IDConversionReturnVo();
        TMetaModelIdRule rule = getIDRule(modelName);
        if (rule != null) {
            String key = "$tableName$";
            String interfaceSql = rule.getIdInterfaceSql();
            if (interfaceSql.contains(key)) {
                interfaceSql = interfaceSql.replace("$tableName$", modelName);
            }
            try {
                CommonVo vo = new CommonVo();
                vo.setSql(interfaceSql);
                List<Map> l = commonMetaMapper.selectAll(vo);
                returnVo.setCode("1");
                String id = l.get(0).get("max_value") + "";
                returnVo.setId(id);//
            } catch (Exception e) {
                returnVo.setCode("0");
                returnVo.setMsg("获取ID失败:" + e.getCause().getMessage());
                return returnVo;
            }
        } else {
            returnVo.setCode("0");
            returnVo.setMsg("未找到模型ID生成规则配置");
        }
        return returnVo;
    }

    @Override
    public List<IDConversionReturnVo> batchConversionId(String[] modelNames) {
        List<IDConversionReturnVo> returnVos = new ArrayList<>();
        for (String modelName : modelNames) {
            IDConversionReturnVo returnVo = conversionId(modelName);
            returnVos.add(returnVo);
        }
        return returnVos;
    }

    private IDConversionReturnVo conversionId(String modelName){
        IDConversionReturnVo returnVo = new IDConversionReturnVo();
        TMetaModelIdRule rule = getIDRule(modelName);
        if (rule != null) {
            returnVo.setModelName(modelName);
            returnVo.setModelId(rule.getModelId());
            returnVo.setModelNameCn(rule.getModelNameCn());
            String key = "$tableName$";
            String batchUpdateSql = rule.getBatchUpdateSql();
            if (batchUpdateSql.contains(key)) {
                batchUpdateSql = batchUpdateSql.replace("$tableName$", modelName);
            }
            try {

                StringBuffer failSqls = new StringBuffer();
                String[] split = batchUpdateSql.split(";");
                List<String> sqls = Arrays.asList(split);
                sqls = (List)sqls.stream().filter((s) -> {
                    return StringUtils.isNotBlank(s);
                }).collect(Collectors.toList());

                int i = 0;
                boolean failFlag = true;
                // failSqls.append("【id:" + jobInfo.getId() + "/name:" + jobInfo.getJobName() + " 执行第");
                Iterator var10 = sqls.iterator();

                while (var10.hasNext()) {
                    String sql = (String)var10.next();
                    ++i;
                    long start = (new Date()).getTime();
                    CommonVo sqlVo = new CommonVo();
                    sqlVo.setSql(sql);
                    try {
                        if (sql.trim().toUpperCase().contains("INSERT")) {
                            this.commonMetaMapper.insertSql(sqlVo);
                        } else if (sql.trim().toUpperCase().contains("UPDATE")) {
                            this.commonMetaMapper.updateSql(sqlVo);
                        } else if (sql.trim().toUpperCase().contains("DELETE")) {
                            this.commonMetaMapper.deleteSql(sqlVo);
                        } else {
                            // ddl
                            this.commonMetaMapper.selectAll(sqlVo);
                        }

                        long end = (new Date()).getTime();
                        double times = (double)(end - start) / 1000.0;
                        logger.info("【模型:" + modelName + " 更新ID时"  + " 执行第" + i
                                + " 条Sql语句耗时】：执行成功==>" + times + "s");
                    } catch (Exception var18) {
                        var18.printStackTrace();
                        failFlag = false;
                        failSqls.append("【【模型:" + modelName + " 更新ID时"   + " 执行第" + i
                                + " 条Sql语句】：执行失败" + var18.getMessage()).append(";");
                        logger.info("【模型:" + modelName + " 更新ID时"   + " 执行第" + i
                                + " 条Sql语句】：执行失败" + var18.getMessage());
                    }
                }
                if (!failFlag) {
                    returnVo.setCode("0");
                    returnVo.setMsg("批量更新ID失败:" + failSqls.toString());
                }else{
                    returnVo.setCode("1");
                    returnVo.setMsg("批量更新ID成功");
                }

            } catch (Exception e) {
                returnVo.setCode("0");
                returnVo.setMsg("批量更新ID失败:" + e.getCause().getMessage());
                return returnVo;
            }
        } else {
            returnVo.setCode("0");
            returnVo.setMsg("未找到模型ID生成规则配置");
        }

        //存储日志
        TMetaModelIdConversionLog log = new TMetaModelIdConversionLog();
        log.setModelName(rule.getModelName());
        log.setModelId(rule.getModelId());
        log.setModelNameCn(rule.getModelNameCn());
        log.setConversionTime(DateUtils.getTime());
        log.setConversionResult(returnVo.getCode());
        String msg = returnVo.getMsg();
        if(msg!=null&&msg.length()>3000){
            msg = msg.substring(0,3000);
        }
        log.setErrorLog(msg);
        log.setCreateDate(log.getConversionTime());
        log.setUpdateDate(log.getConversionTime());
        logMapper.insertTMetaModelIdConversionLog(log);

        //更新id规则最后一次信息
        if(rule.getId()!=null){
            TMetaModelIdRule record =  new TMetaModelIdRule();
            record.setId(rule.getId());//
            record.setLastConversionTime(log.getConversionTime());
            record.setLastConversionResult(log.getConversionResult());
            tMetaModelIdRuleMapper.updateTMetaModelIdRule(record);
        }

        return returnVo;
    }


    private TMetaModelIdRule getIDRule(String modelName) {
        TMetaModelIdRule idRule = getTMetaModelIdRuleByModelName(modelName);
        TMetaModelIdRule defaultIdRule = getTMetaModelIdRuleByModelName("default");
        if (idRule != null) {
            if (StringUtils.isEmpty(idRule.getPrefixEncodingDefault())) {
                idRule.setPrefixEncodingDefault(defaultIdRule.getPrefixEncodingDefault());
            }
            if (StringUtils.isEmpty(idRule.getDataManagementOrgCodeDigit())) {
                idRule.setDataManagementOrgCodeDigit(defaultIdRule.getDataManagementOrgCodeDigit());
            }
            if (StringUtils.isEmpty(idRule.getDataManagementOrgCodeDefault())) {
                idRule.setDataManagementOrgCodeDefault(defaultIdRule.getDataManagementOrgCodeDefault());
            }

            if (StringUtils.isEmpty(idRule.getSerialNumberDigit())) {
                idRule.setSerialNumberDigit(defaultIdRule.getSerialNumberDigit());
            }
            if (StringUtils.isEmpty(idRule.getIdInterfaceSql())) {
                idRule.setIdInterfaceSql(defaultIdRule.getIdInterfaceSql());//
            }
            if (StringUtils.isEmpty(idRule.getBatchUpdateSql())) {
                idRule.setBatchUpdateSql(defaultIdRule.getBatchUpdateSql());
            }
        } else {
            return defaultIdRule;
        }

        return idRule;
    }

    @Override
    public TMetaModelIdRule getTMetaModelIdRuleByModelName(String modelName) {
        TMetaModelIdRule tMetaModelIdRule = new TMetaModelIdRule();
        tMetaModelIdRule.setModelName(modelName);
        List<TMetaModelIdRule> list = tMetaModelIdRuleMapper.selectTMetaModelIdRuleList(tMetaModelIdRule);
        if (list != null && list.size() > 0) {
            // 返回第一个
            return list.get(0);
        } else {
            return null;
        }

    }
}
