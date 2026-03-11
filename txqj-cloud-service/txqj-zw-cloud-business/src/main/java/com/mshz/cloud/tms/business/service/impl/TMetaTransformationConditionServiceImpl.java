package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.tms.business.vo.TestSQLParaVo;
import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.entity.TMetaTransformationCondition;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.mapper.TMetaTransformationConditionMapper;
import com.mshz.cloud.tms.business.vo.TestSQLResultVo;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionGroupService;
import com.mshz.cloud.tms.business.service.ITMetaTransformationConditionService;
import com.mshz.cloud.tms.business.service.MetaDataSearchService;
import com.mshz.cloud.tms.business.service.TMetaModelService;
import com.mshz.cloud.common.util.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 转换条件管理Service业务层处理
 *
 * @author zdw
 * @date 2023-06-20
 */
@Service
public class TMetaTransformationConditionServiceImpl implements ITMetaTransformationConditionService {
    @Resource
    private TMetaTransformationConditionMapper tMetaTransformationConditionMapper;
    @Resource
    private TMetaModelService tMetaModelService;
    @Resource
    private ITMetaTransformationConditionGroupService transformationConditionGroupService;
    @Resource
    private MetaDataSearchService metaDataSearchService;
    @Resource
    private CommonMetaMapper commonMetaMapper;


    /**
     * 查询转换条件管理
     *
     * @param id 转换条件管理主键
     * @return 转换条件管理
     */
    @Override
    public TMetaTransformationCondition selectTMetaTransformationConditionById(Long id) {
        return tMetaTransformationConditionMapper.selectTMetaTransformationConditionById(id);
    }

    /**
     * 查询转换条件管理列表
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 转换条件管理
     */
    @Override
    public List<TMetaTransformationCondition> selectTMetaTransformationConditionList(TMetaTransformationCondition tMetaTransformationCondition) {
        return tMetaTransformationConditionMapper.selectTMetaTransformationConditionList(tMetaTransformationCondition);
    }

    /**
     * 新增转换条件管理
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 结果
     */
    @Override
    public long insertTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition) {
        tMetaTransformationCondition.setCreateTime(DateUtils.getNowDate());
        tMetaTransformationConditionMapper.insertTMetaTransformationCondition(tMetaTransformationCondition);
        return tMetaTransformationCondition.getId();
    }

    /**
     * 修改转换条件管理
     *
     * @param tMetaTransformationCondition 转换条件管理
     * @return 结果
     */
    @Override
    public int updateTMetaTransformationCondition(TMetaTransformationCondition tMetaTransformationCondition) {
        tMetaTransformationCondition.setUpdateTime(DateUtils.getNowDate());
        return tMetaTransformationConditionMapper.updateTMetaTransformationCondition(tMetaTransformationCondition);
    }

    /**
     * 批量删除转换条件管理
     *
     * @param ids 需要删除的转换条件管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionByIds(Long[] ids) {
        return tMetaTransformationConditionMapper.deleteTMetaTransformationConditionByIds(ids);
    }

    /**
     * 删除转换条件管理信息
     *
     * @param id 转换条件管理主键
     * @return 结果
     */
    @Override
    public int deleteTMetaTransformationConditionById(Long id) {
        return tMetaTransformationConditionMapper.deleteTMetaTransformationConditionById(id);
    }

    /**
     * SQL测试方法
     *
     * @param para SQL测试入参
     * @return TestSQLResultVo  SQL测试的结果
     */
    @Override
    public TestSQLResultVo testSQL(TestSQLParaVo para) {
        TestSQLResultVo testSQLResultVo = new TestSQLResultVo();
        try {String conditionType = para.getConditionType();
            String sql = para.getSql();
            if (StringUtils.isEmpty(sql)) {
                StringBuilder sb = new StringBuilder();
                /** 条件类型 conditon_type_meta_model 元模型同步条件，conditon_type_attribute_transformation 属性转换条件，conditon_type_sql_transformation sql转换条件， */
                switch (conditionType) {
                    case "conditon_type_meta_model":
                        Long modelId = Long.parseLong(para.getMetaModelId());
                        TMetaModel model = tMetaModelService.selectTMetaModelById(modelId);
                        sb.append("select * from ").append(model.getTableName().toLowerCase());
                        break;
                    case "condition_type_attribute_transformation":
                        TestSQLResultVo calcSQlVo = metaDataSearchService.calcSQl(Long.parseLong(para.getMetaModelId()), para.getMetaModelVersion(), 1);
                        if (calcSQlVo.getCode().equals("fail")) {
                            return calcSQlVo;
                        } else {
                            sb.append(calcSQlVo.getSql());
                        }
                        break;
                    case "condition_type_sql_transformation":
                        TestSQLResultVo calcSQlVo2 = metaDataSearchService.calcSQl(Long.parseLong(para.getMetaModelId()), para.getMetaModelVersion(), 2);
                        if (calcSQlVo2.getCode().equals("fail")) {
                            return calcSQlVo2;
                        } else {
                            sb.append(calcSQlVo2.getSql());
                        }
                        break;
                    default:
                        break;
                }
                //不能代码where,需要运维人员自己判断是否添加where 对于自定义sql来说 无法确定sql中是否含有where
                sb.append(" ").append(para.getConditionContent());
                sql = sb.toString();
            }
            testSQLResultVo.setSql(sql);
            CommonVo vo = new CommonVo();
            int offset = (para.getPageIndex() - 1) * para.getPageSize();
            sql = sql + " limit "+offset+","+para.getPageSize();
            vo.setSql(sql);

            List result = commonMetaMapper.selectAll(vo);
            testSQLResultVo.setData(result);
            testSQLResultVo.setCode("success");
        } catch (Exception e) {
            e.printStackTrace();
            testSQLResultVo.setCode("fail");
            testSQLResultVo.setError(e.getMessage());
        }
        return testSQLResultVo;
    }


}
