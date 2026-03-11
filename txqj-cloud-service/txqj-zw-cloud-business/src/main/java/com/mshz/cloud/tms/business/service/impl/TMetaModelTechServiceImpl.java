package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.config.MetadataDBConfig;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.entity.TMetaModel;
import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import com.mshz.cloud.tms.business.entity.TMetaModelTech;
import com.mshz.cloud.tms.business.vo.MetadataTechInfoVo;
import com.mshz.cloud.tms.business.mapper.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.service.TMetaModelTechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 技术元模型属性Service业务层处理
 *
 * @author zdw
 * @date 2022-09-23
 */
@Service
public class TMetaModelTechServiceImpl implements TMetaModelTechService
{
    @Autowired
    private TMetaModelTechMapper tMetaModelTechMapper;

    @Autowired
    private TMetaModelMapper tMetaModelMapper;

    @Autowired
    private TQualityVerificationRulesMapper tQualityVerificationRulesMapper;

    @Autowired
    private TMetaModelRelationMapper tMetaModelRelationMapper;

    @Autowired
    private TMetaModelShowMapper tMetaModelShowMapper;

    @Autowired
    private CommonMetaMapper commonMetaMapper;

    @Autowired
    private MetadataDBConfig metadataDBConfig;





    /**
     * 根据id查询技术元模型信息
     * @param modelId
     * @param modelVersion
     * @return
     */
    @Override
    public MetadataTechInfoVo selectTMetaModelTechById(Long modelId, String modelVersion) {

        MetadataTechInfoVo metadataTechInfoVo = tMetaModelTechMapper.selectTMetaModelTechById(modelId);
        if(metadataTechInfoVo!=null){
        metadataTechInfoVo.setDbName(metadataDBConfig.getDatabaseName());
        }
        return metadataTechInfoVo;
    }

    /**
     * 查询技术元模型属性列表
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    @Override
    public List<TMetaModelTech> selectTMetaModelTechList(Long modelId, String columnName, String modelVersion)
    {
        return tMetaModelTechMapper.selectTMetaModelTechList(modelId,columnName,modelVersion);
    }
    /**
     * 查询技术元模型属性列表-校验
     * @param modelId
     * @param columnName
     * @param modelVersion
     * @return
     */
    @Override
    public List<TMetaModelTech> selectTMetaModelTechOne(Long modelId, String columnName, String modelVersion)
    {
        return tMetaModelTechMapper.selectTMetaModelTechOne(modelId,columnName,modelVersion);
    }

    /**
     * 新增技术元模型属性
     * @param tMetaModelTech
     * @param modelId
     * @param tableName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTMetaModelTech(TMetaModelTech tMetaModelTech, Long modelId, String tableName)
    {
        String time=DateUtil.now();
        //技术元模型表新增
        Long columnId = IdUtil.getSnowflakeNextId();
        tMetaModelTech.setId(columnId);//
        tMetaModelTech.setModelId(modelId);
        tMetaModelTech.setModelVersion("edit");
        tMetaModelTech.setCreateDate(time);


        int a= tMetaModelTechMapper.insertTMetaModelTech(tMetaModelTech);
//        //数据校验数据表新增
//        Long tQualityVerificationRulesId = IdUtil.getSnowflakeNextId();
//        TQualityVerificationRules tQualityVerificationRules=new TQualityVerificationRules();
//        tQualityVerificationRules.setId(tQualityVerificationRulesId);
//        tQualityVerificationRules.setMetaModeId(String.valueOf(modelId));
//        tQualityVerificationRules.setMetaModelColumnId(String.valueOf(columnId));
//        tQualityVerificationRules.setModelVersion("edit");
//        tQualityVerificationRules.setCreateTime(new Date());
//        int b= tQualityVerificationRulesMapper.insertTQualityVerificationRules(tQualityVerificationRules);
        //组合关联表新增
//        Long tMetaModelRelationId= IdUtil.getSnowflakeNextId();
//        TMetaModelRelation tMetaModelRelation=new TMetaModelRelation();
//        tMetaModelRelation.setId(tMetaModelRelationId);
//        tMetaModelRelation.setModelId(modelId);
//        tMetaModelRelation.setColumnId(columnId);
//        tMetaModelRelation.setModelVersion("edit");
//        tMetaModelRelation.setRelationType("1");
//        tMetaModelRelation.setCreateDate(time);
//        int c= tMetaModelRelationMapper.insertTMetaModelRelation(tMetaModelRelation);
//        //安全元数据表新增
//        Long tMetaModelSafeId = IdUtil.getSnowflakeNextId();
//        TMetaModelSafe tMetaModelSafe=new TMetaModelSafe();
//        tMetaModelSafe.setId(tMetaModelSafeId);
//        tMetaModelSafe.setModelId(modelId);
//        tMetaModelSafe.setModelVersion("edit");
//        tMetaModelSafe.setColumnId(columnId);
//        tMetaModelSafe.setCreateDate(time);
//        int d= tMetaModelSafeMapper.insertTMetaModelSafe(tMetaModelSafe);
//        //展示配置表新增
        Long tMetaModelShowId = IdUtil.getSnowflakeNextId();
        TMetaModelShow tMetaModelShow=new TMetaModelShow();
        tMetaModelShow.setId(tMetaModelShowId);//
        tMetaModelShow.setModelId(modelId);
        tMetaModelShow.setColumnId(columnId);
        tMetaModelShow.setModelVersion("edit");
        tMetaModelShow.setIsVisible("1");
        tMetaModelShow.setCreateDate(time);
        int e = tMetaModelShowMapper.insertTMetaModelShow(tMetaModelShow);

        /*向数据库表中增添字段    中间库中对应表的字段并未联动
        int f = commonMetaMapper.alterTable(tableName,
                                                   tMetaModelTech.getColumnName(),
                                                   tMetaModelTech.getColumnType(),
                                                   tMetaModelTech.getColumnLength(),
                                                   tMetaModelTech.getColumnNameCn(),
                                                   tMetaModelTech.getIsNotNull());
        String is="1";
        String notIs="0";
        int g=0;
        if(is.equals(tMetaModelTech.getIsPrimaryKey())){
            //设置主键
            g= commonMetaMapper.setPrimaryKey(tableName, tMetaModelTech.getColumnName());
        }else  if (notIs.equals(tMetaModelTech.getIsPrimaryKey())){

        }
         */

        //向数据库表中增添字段    中间库中对应表的字段联动
        int f = 0;
        int g=0;
        String[] dbNames = {"","ry-middledb","ry-transmiddledb"};
        dbNames = new String[]{};
        for (String dbName : dbNames) {
            String dbNameAndTableName = "`"+dbName+"`."+tableName;
            //判断表是否存在
            int i = commonMetaMapper.isTableExist(dbName,tableName);
            if(i != 0){
                //判断字段是否存在
                int j = commonMetaMapper.doesTheColumnNameExist(dbName,tableName,tMetaModelTech.getColumnName());
                if(j == 0){
                    f = commonMetaMapper.alterTable(dbNameAndTableName,
                            tMetaModelTech.getColumnName(),
                            tMetaModelTech.getColumnType(),
                            tMetaModelTech.getColumnLength(),
                            tMetaModelTech.getColumnNameCn(),
                            tMetaModelTech.getIsNotNull());
                }
                String is="1";
                String notIs="0";
                if(is.equals(tMetaModelTech.getIsPrimaryKey())){
                    //设置主键
                    g= commonMetaMapper.setPrimaryKey(dbNameAndTableName, tMetaModelTech.getColumnName());
                }else  if (notIs.equals(tMetaModelTech.getIsPrimaryKey())){

                }
            }

        }
        return a+f+g;
    }

    /**
     * 修改技术元模型属性
     *
     * @param tMetaModelTech 技术元模型属性
     * @param tableName
     * @param oldColumnName
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTMetaModelTech (TMetaModelTech tMetaModelTech, String tableName, String oldColumnName)
    {
        String is="1";
        String notIs="0";
        int c=0;
        if ("datetime".equals(tMetaModelTech.getColumnType())){
            tMetaModelTech.setColumnLength("0");
        }
        tMetaModelTech.setUpdateDate(DateUtil.now());

        /* 中间库中对应表的字段并未联动
        if(is.equals(tMetaModelTech.getIsPrimaryKey())){
            //设置主键
            c= commonMetaMapper.setPrimaryKey(tableName, tMetaModelTech.getColumnName());
        }

        if(notIs.equals(tMetaModelTech.getIsPrimaryKey())){
            //先确定表中是否有主键
            String column = commonMetaMapper.selectPrimaryKey(tableName);
            if(column!=null) {
                if (column.equals(tMetaModelTech.getColumnName())) {
                    //取消主键
                    c = commonMetaMapper.setCancelPrimaryKey(tableName);
                }
            }
        }

        //更改数据库表字段属性
        int b= commonMetaMapper.updateTable(tableName,
                                        tMetaModelTech.getColumnName(),
                                        tMetaModelTech.getColumnType(),
                                        tMetaModelTech.getColumnLength(),
                                        tMetaModelTech.getColumnNameCn(),
                                        tMetaModelTech.getIsNotNull(),
                                        oldColumnName);
         */


        int b=0;
        //更改中间库表字段属性
        String[] dbNames = {"","ry-middledb","ry-transmiddledb"};
        dbNames = new String[]{};
        for (String dbName : dbNames) {
            String dbNameAndTableName = "`"+dbName+"`."+tableName;
            //判断表是否存在
            int i = commonMetaMapper.isTableExist(dbName,tableName);
            if(i != 0){

                if(notIs.equals(tMetaModelTech.getIsPrimaryKey())){
                    //先确定表中是否有主键
                    String column = commonMetaMapper.selectPrimaryKey(dbName,tableName);
                    if(column!=null) {
                        if (column.equals(tMetaModelTech.getColumnName())) {
                            //取消主键
                            c = commonMetaMapper.setCancelPrimaryKey(dbNameAndTableName);
                        }
                    }
                }

                //判断字段是否存在，存在修改，不存在新增
                int j = commonMetaMapper.doesTheColumnNameExist(dbName,tableName,oldColumnName);
                if (j == 0){
                    b = commonMetaMapper.alterTable(dbNameAndTableName,
                            tMetaModelTech.getColumnName(),
                            tMetaModelTech.getColumnType(),
                            tMetaModelTech.getColumnLength(),
                            tMetaModelTech.getColumnNameCn(),
                            tMetaModelTech.getIsNotNull());
                }else {
                    b = commonMetaMapper.updateTable(dbNameAndTableName,
                            tMetaModelTech.getColumnName(),
                            tMetaModelTech.getColumnType(),
                            tMetaModelTech.getColumnLength(),
                            tMetaModelTech.getColumnNameCn(),
                            tMetaModelTech.getIsNotNull(),
                            oldColumnName);
                }

                if(is.equals(tMetaModelTech.getIsPrimaryKey())){
                    //先确定表中是否有主键
                    String column = commonMetaMapper.selectPrimaryKey(dbName,tableName);
                    if(column!=null) {
                        if (!column.equals(tMetaModelTech.getColumnName())) {
                           throw new RuntimeException("存在主键");
                        }
                    }else {
                        //设置主键
                        c = commonMetaMapper.setPrimaryKey(dbNameAndTableName, tMetaModelTech.getColumnName());
                    }
                }

            }
        }
        //修改技术元数据中数据
        int a= tMetaModelTechMapper.updateTMetaModelTech(tMetaModelTech);


        return a+b+c;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTMetaModelTechToData(TMetaModelTech tMetaModelTech) {
        int a= tMetaModelTechMapper.updateTMetaModelTech(tMetaModelTech);
        return a;
    }

    /**
     * 批量删除技术元模型属性
     *
     * @param ids 需要删除的技术元模型属性主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelTechByIds(Long[] ids)
    {
        return tMetaModelTechMapper.deleteTMetaModelTechByIds(ids);
    }

    /**
     * 删除技术元模型属性信息
     *
     * @param id 技术元模型属性主键
     * @param tableName
     * @param columnName
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTMetaModelTechById(Long id,String modelId, String tableName, String columnName) {
        //删除基础校验规则
        int c = tQualityVerificationRulesMapper.deleteTQualityVerificationRulesByColumnId(String.valueOf(id));
        // 删除元模型的字段信息
        int a = tMetaModelTechMapper.deleteTMetaModelTechById(id);
        //删除错误数据
        int d = tQualityVerificationRulesMapper.deleteTQualityResultByColumnId(String.valueOf(id),modelId);

        int b = 1;

        String[] dbNames = {"","ry-middledb","ry-transmiddledb"};
        dbNames = new String[]{};
        for (String dbName : dbNames) {
            String dbNameAndTableName = "`" + dbName + "`." + tableName;
            //判断表是否存在
            int i = commonMetaMapper.isTableExist(dbName, tableName);
            if (i != 0) {
                //删除数据库中表中字段
                b += commonMetaMapper.deleteTableColumn(dbNameAndTableName,columnName);
            }
        }
        //关联删除
        tMetaModelRelationMapper.deleteTMetaModelRelationByType(Long.valueOf(modelId),id);

        //显示删除
        tMetaModelShowMapper.deleteTMetaModelShowByModelId(Long.valueOf(modelId),id);
        //
        return a+b;
    }

    /**
     * 根据id保存技术元模型信息
     * @param modelId
     * @param metadataTechInfoVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int keepTMetaModelBuzById(Long modelId, MetadataTechInfoVo metadataTechInfoVo) {
         String time=DateUtil.now();

         //将数据库配置表id保存到技术元模型属性表
          TMetaModel tMetaModel = new TMetaModel();
          tMetaModel.setId(modelId);//
          tMetaModel.setDbId(metadataTechInfoVo.getDbId()!=null?metadataTechInfoVo.getDbId():null);
          tMetaModel.setTableName(metadataTechInfoVo.getTableName()!=null?metadataTechInfoVo.getTableName():null);
          tMetaModel.setUpdateDate(time);
        return  tMetaModelMapper.updateTMetaModel(tMetaModel);
    }

    /**
     * 查询技术属性类型int，char
     * @return
     */
    @Override
    public List<String> selectTMetaModelTechType() {
        return tMetaModelTechMapper.selectTMetaModelTechType();
    }
}
