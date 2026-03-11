package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.util.ModifyDataBatchUtil;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.entity.MetaModelRule;
import com.mshz.cloud.tms.business.entity.MetaModelRuleConversionLog;
import com.mshz.cloud.tms.business.entity.MetaModelRuleParam;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.mapper.MetaModelRuleMapper;
import cn.hutool.core.date.DateUtil;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.vo.ModelRuleVo;
import com.mshz.cloud.tms.business.vo.ModelRuleConvertVo;
import com.mshz.cloud.tms.business.service.IMetaModelRuleConversionLogService;
import com.mshz.cloud.tms.business.service.IMetaModelRuleParamService;
import com.mshz.cloud.tms.business.service.IMetaModelRuleService;
import com.mshz.cloud.common.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 元模型字段规则Service业务层处理
 *
 * @author zdw
 * @date 2023-08-17
 */
@Service
public class MetaModelRuleServiceImpl implements IMetaModelRuleService {

    @Resource
    private ModifyDataBatchUtil batchUtil;

    @Resource
    private CommonMetaMapper commonMetaMapper;

    @Resource
    private MetaModelRuleMapper metaModelRuleMapper;

    @Resource
    private IMetaModelRuleParamService ruleParamService;

    @Resource
    private IMetaModelRuleConversionLogService ruleLogService;

    /**
     * 元模型字段规则参数配置判断正则表达式
     */
    private static final Pattern PATTERN_META_RULE_PARAM = Pattern.compile("\\$(.+?)\\$");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询元模型字段规则
     *
     * @param id 元模型字段规则主键
     * @return 元模型字段规则
     */
    @Override
    public MetaModelRule selectMetaModelRuleById(Long id)
    {
        return metaModelRuleMapper.selectMetaModelRuleById(id);
    }

    /**
     * 查询元模型字段规则列表
     *
     * @param metaModelRule 元模型字段规则
     * @return 元模型字段规则
     */
    @Override
    public List<MetaModelRule> selectMetaModelRuleList(MetaModelRule metaModelRule)
    {
        return metaModelRuleMapper.selectMetaModelRuleList(metaModelRule);
    }

    /**
     * 新增元模型字段规则
     *
     * @param metaModelRule 元模型字段规则
     * @return 结果
     */
    @Override
    public int insertMetaModelRule(MetaModelRule metaModelRule)
    {
        return metaModelRuleMapper.insertMetaModelRule(metaModelRule);
    }

    /**
     * 修改元模型字段规则
     *
     * @param metaModelRule 元模型字段规则
     * @return 结果
     */
    @Override
    public int updateMetaModelRule(MetaModelRule metaModelRule)
    {
        return metaModelRuleMapper.updateMetaModelRule(metaModelRule);
    }

    /**
     * 批量删除元模型字段规则
     *
     * @param ids 需要删除的元模型字段规则主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleByIds(Long[] ids)
    {
        return metaModelRuleMapper.deleteMetaModelRuleByIds(ids);
    }

    /**
     * 删除元模型字段规则信息
     *
     * @param id 元模型字段规则主键
     * @return 结果
     */
    @Override
    public int deleteMetaModelRuleById(Long id)
    {
        return metaModelRuleMapper.deleteMetaModelRuleById(id);
    }

    /**
     * 查询元模型字段规则列表
     *
     * @param param
     * @return 规则vo集合
     */
    @Override
    public List<ModelRuleVo> getModelRuleList(MetaModelRule param) {
        return metaModelRuleMapper.getModelRuleList(param);
    }

    /**
     * 转换元模型数据
     *
     * @param modelRuleConvertVo
     * @return
     */
    @Override
    public ModelRuleConvertVo convertMetaModelData(ModelRuleConvertVo modelRuleConvertVo) {
        logger.info(">>> 开始进行元模型字段规则数据转换...");
        // 获取选中的转换规则信息
        MetaModelRule rule = metaModelRuleMapper.selectMetaModelRuleById(modelRuleConvertVo.getRuleId());
        // 转换日志信息
        MetaModelRuleConversionLog ruleLog = ModelConvertUtil.modelConvert(rule ,MetaModelRuleConversionLog.class);
        ruleLog.setId(IdUtil.getSnowflakeNextId());//
        ruleLog.setModelRuleId(rule.getId());

        // 传入待转换字段的元数据数量
        int perConvertDataNum = CollectionUtils.isEmpty(modelRuleConvertVo.getMetaDataIdList()) ? 0 : modelRuleConvertVo.getMetaDataIdList().size();

        int convertDataNum = 0;
        try {
            // 参数做返回数据vo
            modelRuleConvertVo.setColumnId(rule.getColumnId());
            // 获取元模型主键字段名
            String modelKey = metaModelRuleMapper.getTableKeyName(rule.getTableId());
            // 拼接查询数据的id查询
            String querySql = ruleDataQuerySql(rule, modelRuleConvertVo, modelKey);
            if ("error".equals(querySql)) {
                modelRuleConvertVo.setConversionResult("0");
                modelRuleConvertVo.setResultInfo("转换失败，元模型字段规则，使用参数规则拼接规则数据查询sql失败。");
                logger.info(">>> 转换失败，元模型字段规则，使用参数规则拼接规则数据查询sql失败。");

                setRuleConvertLogInfo(ruleLog, "0", "转换失败，元模型字段规则，使用参数规则拼接规则数据查询sql失败。");
                return modelRuleConvertVo;
            }

            // 执行查询sql。获取规则转换后字段数据
            CommonVo sqlVo = new CommonVo();
            sqlVo.setSql(querySql.toString());
            List<Map> ruleDataMapList = commonMetaMapper.selectAll(sqlVo);
            if (CollectionUtils.isEmpty(ruleDataMapList)) {
                modelRuleConvertVo.setConversionResult("0");
                modelRuleConvertVo.setResultInfo("转换失败，传入元模型数据按规则查询待转换数据，结果集为空。");
                logger.info(">>> 转换失败，传入元模型数据按规则查询待转换数据，结果集为空。");

                setRuleConvertLogInfo(ruleLog, "0", "转换失败，传入元模型数据按规则查询待转换数据，结果集为空。");
                return modelRuleConvertVo;
            }

            // 以查询到转换后数据为基础，拼接元数据更新sql
            // List<CommonVo> updateCommonVoList = new ArrayList<CommonVo>();
            // for (Map ruleDataMap : ruleDataMapList) {
            //     CommonVo updateVo = new CommonVo();
            //     String updateSql = "update " + rule.getTableName()
            //             + " set " + rule.getColumnName() + " = '" + ruleDataMap.get("ruleData") + "'"
            //             + " where " + modelKey + " = '" + ruleDataMap.get("id") + "'";
            //     updateVo.setSql(updateSql);
            //     updateCommonVoList.add(updateVo);
            // }
            // convertDataNum = updateCommonVoList.isEmpty() ? 0
            //         : batchUtil.batchInsertOrUpdate(updateCommonVoList, CommonMetaMapper.class,
            //         (updateCommonVo, commonMetaMapper) -> commonMetaMapper.updateSql(updateCommonVo));


            StringBuffer updateSql = new StringBuffer();
            StringBuffer updateResourceSql = new StringBuffer();
            updateSql.append("UPDATE ");
            updateResourceSql.append("update tms.nr_res_resource_b master join ");
            if(rule.getTargetTable()!=null&&!rule.getTargetTable().equals("")){
                updateSql.append(rule.getTargetTable());
                updateResourceSql.append(rule.getTargetTable());
            }else {
                updateSql.append(rule.getTableName());
                updateResourceSql.append(rule.getTableName());
            }
            updateSql.append(" master JOIN ( ")
                    .append(querySql).append(" ) slave on slave.id =  master.").append(modelKey)
                    .append(" SET master.").append(rule.getColumnName()).append(" = slave.ruleData");
            CommonVo updateVo = new CommonVo();
            updateVo.setSql(updateSql.toString());
            convertDataNum = commonMetaMapper.updateSql(updateVo);
            //更新2.0资源总表
            updateVo.setSql(updateResourceSql.append(" son on master.id = son.id set master.").append(rule.getColumnName()).append("= son.").append(rule.getColumnName()).toString());
            commonMetaMapper.updateSql(updateVo);
            logger.info(">>> 元模型[{}]字段[{}]数据按规则转换完成，选中传入数据[{}]条，实际转换[{}]条",
                    rule.getTableName(), rule.getColumnName(),perConvertDataNum, convertDataNum);

            modelRuleConvertVo.setResultInfo("转换成功，共转换" + convertDataNum + "条数据");
            modelRuleConvertVo.setConversionResult("1");
            modelRuleConvertVo.setConversionTime(DateUtil.now());

            setRuleConvertLogInfo(ruleLog, "1",
                    "转换成功，元模型字段数据按配置规则转换完成，操作选中元数据" + perConvertDataNum + "条进行转换，本次共成功转换"+ convertDataNum +"条数据");
        } catch (Exception e) {
            e.printStackTrace();

            setRuleConvertLogInfo(ruleLog, "0", "转换失败，元模型数据规则转换发生异常：" + e.getMessage());
            modelRuleConvertVo.setConversionResult("0");
            modelRuleConvertVo.setResultInfo("转换失败，元模型数据规则转换发生异常");
            logger.info(">>> 转换失败，元模型数据规则转换发生异常：{}", e.getMessage());
        }

        return modelRuleConvertVo;
    }

    @Override
    public List<ModelRuleConvertVo> getColumRuleList() {
        return metaModelRuleMapper.getColumRuleList();
    }

    private void setRuleConvertLogInfo(MetaModelRuleConversionLog ruleLog, String result, String info) {
        if (ruleLog == null) {
            ruleLog = new MetaModelRuleConversionLog();
        }

        ruleLog.setIsBatch("0");
        ruleLog.setConversionResult(result);
        ruleLog.setConvertLog(info);
        ruleLog.setConversionTime(DateUtil.now());

        ruleLogService.insertMetaModelRuleConversionLog(ruleLog);
    }

    /**
     * 判断规则的使用规则方式并拼接返回规则数据查询sql
     * @param rule
     * @param modelRuleConvertVo
     * @return
     */
    private String ruleDataQuerySql(MetaModelRule rule, ModelRuleConvertVo modelRuleConvertVo, String modelKey) {
        // 判断当前规则的使用规则方式
        StringBuffer querySql = new StringBuffer();
        if ("0".equals(rule.getRuleUsed())) {
            // 0：参数规则
            logger.info(">>> 当前元模型[{}]字段[{}]使用转换规则为参数规则转换...", rule.getTableName(), rule.getColumnName());

            // 查询当前规则的参数，拼接执行转换的sql
            MetaModelRuleParam param = new MetaModelRuleParam();
            param.setModelRuleId(rule.getId());
            List<MetaModelRuleParam> ruleParamList = ruleParamService.selectMetaModelRuleParamList(param);
            // 获取参数规则设置
            String paramRuleSet = rule.getParamRuleSet();

            // 若参数或参数规则设置为空，代表设置有问题
            if (CollectionUtils.isEmpty(ruleParamList) || StringUtils.isEmpty(paramRuleSet)) {
                modelRuleConvertVo.setConversionResult("0");
                modelRuleConvertVo.setResultInfo("当前元模型属性没有查询到规则设置信息");
                logger.info(">>> 当前元模型属性没有查询到规则设置信息。");
                return "error";
            }
            logger.info(">>> 当前参数规则配置了[{}]个参数，规则配置为：{}。开始拼接查询sql...", ruleParamList.size(), paramRuleSet);

            // 根据规则以及参数拼接查询sql，将字段规则数据与对应的元数据id查询出来
            querySql.append("select ");

            querySql.append(rule.getTableName()).append(".").append(modelKey).append(" as id, ");

            // 根据 参数规则 和 参数列表，匹配规则需要字段，拼接规则查询字段
            StringBuffer queryRuleData = new StringBuffer();
            List<String> ruleParamStrList = new ArrayList<>();
            Matcher matcher = PATTERN_META_RULE_PARAM.matcher(paramRuleSet);
            int matcher_start = 0;
            while (matcher.find(matcher_start)){
                ruleParamStrList.add(matcher.group(1));
                matcher_start = matcher.end();
            }
            // 规则使用的参数排序，用于拼接查询字段
            List<MetaModelRuleParam> queryParamList = ruleParamList.stream().filter(ruleParam -> ruleParamStrList.stream().anyMatch(ruleParamStr ->
                            ruleParamStr.equals(ruleParam.getParamName())
                    )
            ).collect(Collectors.toList());

            // 拼接规则数据查询字段
            for (MetaModelRuleParam ruleParam : queryParamList) {
                paramRuleSet = paramRuleSet.replace(ruleParam.getParamName(), ruleParam.getParamTableName() + "." + ruleParam.getParamColumnName());
            }
            List<String> queryStrList = Arrays.asList(paramRuleSet.split("\\$"));
            queryRuleData.append("concat(");
            queryStrList.forEach(queryStr -> {
                if (queryParamList.stream().anyMatch(queryParam -> queryStr.equals(queryParam.getParamTableName() + "." + queryParam.getParamColumnName()))) {
                    queryRuleData.append(queryStr);
                } else {
                    queryRuleData.append("'");
                    queryRuleData.append(queryStr);
                    queryRuleData.append("'");
                }
                queryRuleData.append(",");
            });
            // 去除最后多余的逗号
            queryRuleData.deleteCharAt(queryRuleData.length() - 1);
            queryRuleData.append(") as ruleData ");
            querySql.append(queryRuleData);
            querySql.append(" from ").append(rule.getTableName());
            // 拼接条件查询
            queryParamList.forEach(queryParam -> {
                if (!StringUtils.isEmpty(queryParam.getParamCondition()) && queryParam.getParamCondition().contains(rule.getTableName())) {
                    querySql.append(" join ").append(queryParam.getParamTableName()).append(" on ").append(queryParam.getParamCondition());
                }
            });

            // 拼接查询条件（调用接口传入的需要转换的数据的id）
            if (!CollectionUtils.isEmpty(modelRuleConvertVo.getMetaDataIdList())) {
//                String idList = modelRuleConvertVo.getMetaDataIdList().stream().map(String::valueOf).collect(Collectors.joining(","));
                String idList = "'" + String.join("','", modelRuleConvertVo.getMetaDataIdList()) + "'";
                querySql.append(" where ").append(rule.getTableName()).append(".").append(modelKey).append(" in (").append(idList).append(")");
            }
        } else {
            // 1：sql规则
            String sql = rule.getConvertRuleSql();
            logger.info(">>> 当前元模型[{}]字段[{}]使用转换规则为sql转换规则转换...", rule.getTableName(), rule.getColumnName());
            // querySql.append(sql);
            // //转小写
            // sql = sql.toLowerCase();
            // // 规范sql，将可能存在的多个空格，替换为一个空格
            // sql = sql.replaceAll("\\s+", " ");
            // // 获取sql中查询表的表别名（默认为元模型表名）
            // List<String> sqlPart = Arrays.asList(sql.split("\\s"));
            // String alias = "";
            // int fromIndex = sqlPart.indexOf("from");
            // alias = sqlPart.size() > fromIndex + 2 ? sqlPart.get(fromIndex + 2) : rule.getTableName();
            // // 判断可能存在的没有别名直接关联的情况
            // if ("join".equalsIgnoreCase(alias)
            //         || "left".equalsIgnoreCase(alias)
            //         || "right".equalsIgnoreCase(alias)
            //         || "inner".equalsIgnoreCase(alias)
            // ) {
            //     alias = rule.getTableName();
            // }
            // // 拼接查询条件（调用接口传入的需要转换的数据的id）
            // if (!CollectionUtils.isEmpty(modelRuleConvertVo.getMetaDataIdList())) {
            //     String idList = "'" + String.join("','", modelRuleConvertVo.getMetaDataIdList()) + "'";
            //     if (sql.contains("where") || sql.contains("WHERE")) {
            //         querySql.append(" and ").append(alias).append(".").append(modelKey).append(" in (").append(idList).append(")");
            //     } else {
            //         querySql.append(" where ").append(alias).append(".").append(modelKey).append(" in (").append(idList).append(")");
            //     }
            // }


            querySql.append("SELECT id, ruleData FROM ( ")
                    .append(sql)
                    .append(" ) main ");
            if (!CollectionUtils.isEmpty(modelRuleConvertVo.getMetaDataIdList())) {
                String idList = "'" + String.join("','", modelRuleConvertVo.getMetaDataIdList()) + "'";
                querySql.append("WHERE main.id IN (").append(idList).append(")");
            }

        }

        logger.info(">>> 规则数据查询SQL：\n{}", querySql.toString());
        return querySql.toString();
    }


}
