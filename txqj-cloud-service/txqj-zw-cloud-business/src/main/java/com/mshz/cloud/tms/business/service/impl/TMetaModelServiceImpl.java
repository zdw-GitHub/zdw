package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.util.SecurityUtils;
import com.mshz.cloud.tms.business.mapper.CommonMetaMapper;
import com.mshz.cloud.tms.business.entity.*;
import com.mshz.cloud.tms.business.vo.*;
import com.mshz.cloud.tms.business.mapper.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.service.TMetaModelCategoryService;
import com.mshz.cloud.tms.business.service.TMetaModelService;
import com.mshz.cloud.tms.business.util.DateUtils;
import com.mshz.cloud.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

/**
 * 元模型Service业务层处理
 *
 * @author zxx
 * @date 2022-09-23
 */
@Service
public class TMetaModelServiceImpl implements TMetaModelService {
    @Resource
    private TMetaModelMapper tMetaModelMapper;

    @Resource
    private TMetaModelReleaseMapper tMetaModelReleaseMapper;
    @Resource
    private TMetaModelReLabelMapper tMetaModelReLabelMapper;
    @Resource
    private TMetaModelTechMapper tMetaModelTechMapper;
    @Resource
    private TMetaModelRelationMapper tMetaModelRelationMapper;
    @Resource
    private TMetaModelSafeMapper tMetaModelSafeMapper;
    @Resource
    private TMetaModelShowMapper tMetaModelShowMapper;
    @Resource
    private CommonMetaMapper commonMetaMapper;
    @Resource
    private TMetaModelVersionMapper tMetaModelVersionMapper;
    @Resource
    private TQualityVerificationRulesMapper tQualityVerificationRulesMapper;
    @Resource
    private TQualityAssRuleVerificationMapper tQualityAssRuleVerificationMapper;

    @Resource
    private TMetaModelCategoryService categoryService;

    /**
     * 根据id查询业务元数据信息
     *
     * @param modelId
     * @param modelVersion
     * @return
     */
    @Override
    public MetadataBuzInfoVo selectTMetaModelBuzById(Long modelId, String modelVersion) {
        MetadataBuzInfoVo metadataBuzInfoVo = new MetadataBuzInfoVo();
        if ("edit".equals(modelVersion)) {
            metadataBuzInfoVo = tMetaModelMapper.selectTMetaModelBuzById(modelId);
        } else {
            metadataBuzInfoVo = tMetaModelReleaseMapper.selectTMetaModelRelease(modelId, modelVersion);
        }
        List<Long> labelNameList = tMetaModelReLabelMapper.selectModelLabelById(modelId, modelVersion);
        if (labelNameList.size() != 0) {
            metadataBuzInfoVo.setLabelId(labelNameList);
        }
        return metadataBuzInfoVo;
    }

    /**
     * 查询所有类目及包括的元模型树
     *
     * @return
     */
    @Override
    public List<MetadataModelVo> selectMetaModelTree() {

        List<TMetaModel> tMetaModelList = tMetaModelMapper.selectMetaModelCategory();
        List<TMetaModelCategory> categoryNameList = categoryService.selectTMetaModelCategoryList(null);
        TMetaModelCategory otherModelCategory = new TMetaModelCategory();
        otherModelCategory.setCategoryNameCn("其他");
        categoryNameList.add(otherModelCategory);
        List<MetadataModelVo> metadataModelVoList = new ArrayList<>();
        for (TMetaModelCategory category : categoryNameList) {
            MetadataModelVo metadataModelVo = new MetadataModelVo();
            metadataModelVo.setCategoryId(category.getId()+"");
            metadataModelVo.setCategoryName(category.getCategoryNameCn());
            List<TMetaModel> finallyTMetaModelList = new ArrayList<>();
            for (TMetaModel tMetaModel2 : tMetaModelList) {
                if (metadataModelVo.getCategoryName().equals(tMetaModel2.getRemark())) {
                    TMetaModel tMetaModel = new TMetaModel();
                    tMetaModel.setId(tMetaModel2.getId() != null ? tMetaModel2.getId() : null);//
                    tMetaModel.setTableName(tMetaModel2.getTableName() != null ? tMetaModel2.getTableName() : null);
                    tMetaModel.setTableNameCn(tMetaModel2.getTableNameCn() != null ? tMetaModel2.getTableNameCn() : null);
                    if (tMetaModel != null) {
                        finallyTMetaModelList.add(tMetaModel);
                    }
                    metadataModelVo.setTMetaModels(finallyTMetaModelList);
                }
            }
            metadataModelVoList.add(metadataModelVo);
        }
        return metadataModelVoList;
    }

    @Override
    public List<MetadataModelVo> selectMetaModelTreeByTableName(TMetaModel para) {

        List<TMetaModel> tMetaModelList = tMetaModelMapper.selectMetaModelCategoryByTableName(para);
        List<String> categoryNameList = tMetaModelMapper.selectCategoryNames();
        categoryNameList.add("其他");
        List<MetadataModelVo> metadataModelVoList = new ArrayList<>();
        for (String categoryName : categoryNameList) {
            MetadataModelVo metadataModelVo = new MetadataModelVo();
            metadataModelVo.setCategoryName(categoryName);
            List<TMetaModel> finallyTMetaModelList = new ArrayList<>();
            for (TMetaModel tMetaModel2 : tMetaModelList) {
                if (metadataModelVo.getCategoryName().equals(tMetaModel2.getRemark())) {
                    TMetaModel tMetaModel = new TMetaModel();
                    tMetaModel.setId(tMetaModel2.getId() != null ? tMetaModel2.getId() : null);//
                    tMetaModel.setTableName(tMetaModel2.getTableName() != null ? tMetaModel2.getTableName() : null);
                    tMetaModel.setTableNameCn(tMetaModel2.getTableNameCn() != null ? tMetaModel2.getTableNameCn() : null);
                    if (tMetaModel != null) {
                        finallyTMetaModelList.add(tMetaModel);
                    }
                    metadataModelVo.setTMetaModels(finallyTMetaModelList);
                }
            }
            metadataModelVoList.add(metadataModelVo);
        }
        return metadataModelVoList;
    }

    @Override
    public List<MetadataModelVo> jobInsertTree() {
        List<TMetaModel> tMetaModelList = tMetaModelMapper.selectMetaModelCategoryJob();
        List<String> categoryNameList = tMetaModelMapper.selectCategoryNames();
        categoryNameList.add("其他");
        List<MetadataModelVo> metadataModelVoList = new ArrayList<>();
        for (String categoryName : categoryNameList) {
            MetadataModelVo metadataModelVo = new MetadataModelVo();
            metadataModelVo.setCategoryName(categoryName);
            List<TMetaModel> finallyTMetaModelList = new ArrayList<>();
            for (TMetaModel tMetaModel2 : tMetaModelList) {
                if (metadataModelVo.getCategoryName().equals(tMetaModel2.getRemark())) {
                    TMetaModel tMetaModel = new TMetaModel();
                    tMetaModel.setId(tMetaModel2.getId() != null ? tMetaModel2.getId() : null);//
                    tMetaModel.setTableName(tMetaModel2.getTableName() != null ? tMetaModel2.getTableName() : null);
                    tMetaModel.setTableNameCn(tMetaModel2.getTableNameCn() != null ? tMetaModel2.getTableNameCn() : null);
                    if (tMetaModel != null) {
                        finallyTMetaModelList.add(tMetaModel);
                    }
                    metadataModelVo.setTMetaModels(finallyTMetaModelList);
                }
            }
            metadataModelVoList.add(metadataModelVo);
        }
        return metadataModelVoList;
    }

    /**
     * 查询所有类目及包括的元模型树带有元模型版本号
     *
     * @return
     */
    @Override
    public List<TreeVo> selectMetaModelTreeWithVersion(TMetaModel tMetaModel) {
        List<TreeVo> treeList = new ArrayList<>();
        List<TMetaModelCategory> categoryList = categoryService.selectTMetaModelCategoryList(null);
        List<TMetaModel> metaModelList = tMetaModelMapper.selectTMetaModelList(tMetaModel);
        List<TMetaModelVersion> versionList = tMetaModelVersionMapper.selectTMetaModelVersionList(null);
        Map<Long, TMetaModelCategory> categoryMap = new HashMap<>();

        for (int i = 0; i < categoryList.size(); i++) {
            TMetaModelCategory category = categoryList.get(i);
            if (categoryMap.get(category.getId()) == null) {
                categoryMap.put(category.getId(), category);
            }
        }
        // 找出没有匹配类目的元模型
        List<TMetaModel> metaModelListWithoutCategory = new ArrayList<>();
        for (int i = metaModelList.size() - 1; i >= 0; i--) {
            TMetaModel metaModel = metaModelList.get(i);
            Long categoryId = metaModel.getCategoryNameId();
            if (categoryId == null || categoryMap.get(categoryId) == null) {
                metaModelListWithoutCategory.add(metaModel);
                metaModelList.remove(i);
            }
        }
        // 制作类目树
        if (categoryList != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                TMetaModelCategory categoryVo = categoryList.get(i);
                TreeVo categoryTree = new TreeVo();
                categoryTree.setId(categoryVo.getId() + "");//
                categoryTree.setLabel(categoryVo.getCategoryNameCn());
                categoryTree.setType("category");
                treeList.add(categoryTree);
                // 制作元模型树
                pacakgeMetaModelTreeVo(categoryTree, metaModelList, versionList, false);
            }

            // 找不到类目的数据处理
            TreeVo otherCategoryTree = new TreeVo();
            otherCategoryTree.setId("");//
            otherCategoryTree.setLabel("其他");
            otherCategoryTree.setType("category");
            treeList.add(otherCategoryTree);
            // 制作元模型树
            pacakgeMetaModelTreeVo(otherCategoryTree, metaModelListWithoutCategory, versionList, true);

        }
        return treeList;
    }

    @Override
    public List<TreeVo> selectMetaModelTreeWithVersionMiddle(TMetaModel tMetaModel) {
        List<TreeVo> treeList = new ArrayList<>();
        List<TMetaModelCategory> categoryList = categoryService.selectTMetaModelCategoryList(null);
        List<TMetaModel> metaModelList = tMetaModelMapper.selectTMetaModelList(tMetaModel);
        List<TMetaModelVersion> versionList = tMetaModelVersionMapper.selectTMetaModelVersionList(null);
        Map<Long, TMetaModelCategory> categoryMap = new HashMap<>();

        for (int i = 0; i < categoryList.size(); i++) {
            TMetaModelCategory category = categoryList.get(i);
            if (categoryMap.get(category.getId()) == null) {
                categoryMap.put(category.getId(), category);
            }
        }
        // 找出没有匹配类目的元模型
        List<TMetaModel> metaModelListWithoutCategory = new ArrayList<>();
        for (int i = metaModelList.size() - 1; i >= 0; i--) {
            TMetaModel metaModel = metaModelList.get(i);
            Long categoryId = metaModel.getCategoryNameId();
            if (categoryId == null || categoryMap.get(categoryId) == null) {
                metaModelListWithoutCategory.add(metaModel);
                metaModelList.remove(i);
            }
        }
        // 制作类目树
        if (categoryList != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                TMetaModelCategory categoryVo = categoryList.get(i);
                TreeVo categoryTree = new TreeVo();
                categoryTree.setId(categoryVo.getId() + "");//
                categoryTree.setName("");
                categoryTree.setLabel(categoryVo.getCategoryNameCn());
                categoryTree.setType("category");
                treeList.add(categoryTree);
                // 制作元模型树
                pacakgeMetaModelTreeVoMiddle(categoryTree, metaModelList, versionList, false, false);
            }

            // 找不到类目的数据处理
            TreeVo otherCategoryTree = new TreeVo();
            otherCategoryTree.setId("");//
            otherCategoryTree.setLabel("其他");
            otherCategoryTree.setType("category");
            treeList.add(otherCategoryTree);
            // 制作元模型树
            pacakgeMetaModelTreeVoMiddle(otherCategoryTree, metaModelListWithoutCategory, versionList, true, false);

        }
        return treeList;
    }

    @Override
    public List<TreeVo> selectMetaModelTreeForDataShare(TMetaModel tMetaModel) {
        List<TreeVo> treeList = new ArrayList<>();
        List<TMetaModelCategory> categoryList = categoryService.selectTMetaModelCategoryList(null);
        List<TMetaModel> metaModelList = tMetaModelMapper.selectTMetaModelList(tMetaModel);
        List<TMetaModelVersion> versionList = tMetaModelVersionMapper.selectTMetaModelVersionList(null);
        Map<Long, TMetaModelCategory> categoryMap = new HashMap<>();

        for (int i = 0; i < categoryList.size(); i++) {
            TMetaModelCategory category = categoryList.get(i);
            if (categoryMap.get(category.getId()) == null) {
                categoryMap.put(category.getId(), category);
            }
        }
        // 找出没有匹配类目的元模型
        List<TMetaModel> metaModelListWithoutCategory = new ArrayList<>();
        for (int i = metaModelList.size() - 1; i >= 0; i--) {
            TMetaModel metaModel = metaModelList.get(i);
            Long categoryId = metaModel.getCategoryNameId();
            if (categoryId == null || categoryMap.get(categoryId) == null) {
                metaModelListWithoutCategory.add(metaModel);
                metaModelList.remove(i);
            }
        }
        // 制作类目树
        if (categoryList != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                TMetaModelCategory categoryVo = categoryList.get(i);
                TreeVo categoryTree = new TreeVo();
                categoryTree.setId(categoryVo.getId() + "");//
                categoryTree.setName("");
                categoryTree.setLabel(categoryVo.getCategoryNameCn());
                categoryTree.setType("category");
                treeList.add(categoryTree);
                // 制作元模型树
                pacakgeMetaModelTreeVoMiddle(categoryTree, metaModelList, versionList, false, true);
            }

            // 找不到类目的数据处理
            TreeVo otherCategoryTree = new TreeVo();
            otherCategoryTree.setId("");//
            otherCategoryTree.setLabel("其他");
            otherCategoryTree.setType("category");
            treeList.add(otherCategoryTree);
            // 制作元模型树
            pacakgeMetaModelTreeVoMiddle(otherCategoryTree, metaModelListWithoutCategory, versionList, true, true);

        }
        return treeList;
    }

    private void pacakgeMetaModelTreeVo(TreeVo parent, List<TMetaModel> metaModelList, List<TMetaModelVersion> versionList, boolean isOther) {
        List<TreeVo> metaModelTreeList = new ArrayList<>();
        parent.setChildren(metaModelTreeList);
        for (int i = metaModelList.size() - 1; i >= 0; i--) {
            TMetaModel metaModel = metaModelList.get(i);
            Long categoryId = metaModel.getCategoryNameId();
            if (isOther || (categoryId == null && parent.getId() == null) || (parent.getId() != null && parent.getId().equals(categoryId + ""))) {
                TreeVo metaTree = new TreeVo();
                metaTree.setId(metaModel.getId() + "");//
                metaTree.setLabel(metaModel.getTableNameCn());
                metaTree.setType("metadata");
                metaTree.setParentId(parent.getId());
                metaModelTreeList.add(metaTree);
                // 删除处理掉过的数据
                metaModelList.remove(i);

                // 制作版本树
                List<TreeVo> versionTreeList = new ArrayList<>();
                metaTree.setChildren(versionTreeList);
                for (int j = versionList.size() - 1; j >= 0; j--) {
                    TMetaModelVersion version = versionList.get(j);
                    Long modelId = version.getModelId();
                    if (modelId.equals(metaModel.getId())) {
                        TreeVo versionTree = new TreeVo();
                        versionTree.setId(version.getId() + "");//
                        versionTree.setLabel(version.getModelVersion());
                        versionTree.setType("versionT");
                        versionTree.setParentId(metaTree.getId());
                        versionTree.setRlModelId(metaModel.getRelationMainTableId());
                        versionTree.setAuditMode(metaModel.getAuditMode());
                        versionTreeList.add(versionTree);
                        versionList.remove(j);
                    }

                } // for j

            } // if

        } // for i
    }

    private void pacakgeMetaModelTreeVoMiddle(TreeVo parent, List<TMetaModel> metaModelList, List<TMetaModelVersion> versionList, boolean isOther, boolean forDataSharing) {
        List<TreeVo> metaModelTreeList = new ArrayList<>();
        parent.setChildren(metaModelTreeList);
        for (int i = metaModelList.size() - 1; i >= 0; i--) {
            TMetaModel metaModel = metaModelList.get(i);
            Long categoryId = metaModel.getCategoryNameId();
            if (isOther || (categoryId == null && parent.getId() == null) || (parent.getId() != null && parent.getId().equals(categoryId + ""))) {
                TreeVo metaTree = new TreeVo();
                metaTree.setId(metaModel.getId() + "");//
                metaTree.setLabel(metaModel.getTableNameCn());
                if (forDataSharing) {
                    metaTree.setName(metaModel.getTableName());
                }
                metaTree.setType("metadata");
                metaTree.setParentId(parent.getId());
                metaTree.setRlModelId(metaModel.getRelationMainTableId());
                metaModelTreeList.add(metaTree);
                // 删除处理掉过的数据
                metaModelList.remove(i);

            } // if

        } // for i
    }

    /**
     * 新增元模型
     *
     * @param metadataBuzInfoVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTMetaModel(MetadataBuzInfoVo metadataBuzInfoVo) {
        String time = DateUtil.now();
        Long modelId = IdUtil.getSnowflakeNextId();
        // 将元模型id，名称，中文名称，类目id新增到元模型表中
        TMetaModel tMetaModel = new TMetaModel();
        tMetaModel.setId(modelId);//
        tMetaModel.setTableName(metadataBuzInfoVo.getTableName());
        tMetaModel.setTableNameCn(metadataBuzInfoVo.getTableNameCn() != null ? metadataBuzInfoVo.getTableNameCn() : null);
        tMetaModel.setCategoryNameId(metadataBuzInfoVo.getCategoryNameId() != null ? metadataBuzInfoVo.getCategoryNameId() : null);
        tMetaModel.setModelVersion("edit");
        tMetaModel.setCreateDate(time);
        int a = tMetaModelMapper.insertTMetaModel(tMetaModel);

        // ids依次存入标签关联表实体
        // 标签操作
        int b = 0;
        if (metadataBuzInfoVo.getLabelId().size() > 0) {
            List<TMetaModelReLabel> tMetaModelReLabelList = new ArrayList<>();
            for (Long labelId : metadataBuzInfoVo.getLabelId()) {
                Long tMetaModelReLabelId = IdUtil.getSnowflakeNextId();
                TMetaModelReLabel tMetaModelReLabel = new TMetaModelReLabel();
                tMetaModelReLabel.setId(tMetaModelReLabelId);//
                tMetaModelReLabel.setLabelId(labelId);
                tMetaModelReLabel.setModelId(modelId);
                tMetaModelReLabel.setModelVersion("edit");
                tMetaModelReLabel.setCreateDate(time);
                tMetaModelReLabelList.add(tMetaModelReLabel);
            }
            // 标签关联表新增实体集合
            b = tMetaModelReLabelMapper.insertTMetaModelReLabelList(tMetaModelReLabelList);
        }
        // 向版本表插入数据
        Long tMetaModelVersionId = IdUtil.getSnowflakeNextId();
        TMetaModelVersion tMetaModelVersion = new TMetaModelVersion();
        tMetaModelVersion.setId(tMetaModelVersionId);//
        tMetaModelVersion.setModelId(modelId);
        tMetaModelVersion.setModelVersion("edit");
        tMetaModelVersion.setCreateDate(time);
        tMetaModelVersion.setModelVersionAlias(metadataBuzInfoVo.getTableName());
        int d = tMetaModelVersionMapper.insertTMetaModelVersion(tMetaModelVersion);

        // 在数据库中生成表
        //int c = commonMetaMapper.createTable(metadataBuzInfoVo.getTableName(), metadataBuzInfoVo.getTableNameCn());

        return a + b + d;
    }

    /**
     * 发布元模型
     *
     * @param modelId
     * @param modelVersion
     * @param updateLog
     * @param remark
     * @param tableName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTMetaModelRelease(Long modelId, String modelVersion, String updateLog, String remark, String tableName) {

        String time = DateUtil.now();
        // 查询元模型表拷贝到元模型发布表的数据
        TMetaModel tMetaModel = tMetaModelMapper.selectMetaModelCopy(modelId);
        // 拷贝到元模型发布表
        String username = SecurityUtils.getUsername();
        Long modelReleaseId = IdUtil.getSnowflakeNextId();
        TMetaModelRelease tMetaModelRelease = new TMetaModelRelease();
        tMetaModelRelease.setId(modelReleaseId);//
        tMetaModelRelease.setModelId(modelId);
        tMetaModelRelease.setModelVersion(modelVersion);
        tMetaModelRelease.setUpdateLog(updateLog);
        tMetaModelRelease.setRemark(remark != null ? remark : null);
        tMetaModelRelease.setModelStatus("1");
        tMetaModelRelease.setCategoryNameId(tMetaModel.getCategoryNameId());
        tMetaModelRelease.setDbId(tMetaModel.getDbId());
        tMetaModelRelease.setTableName(tMetaModel.getTableName());
        tMetaModelRelease.setTableNameCn(tMetaModel.getTableNameCn());
        tMetaModelRelease.setSafetyLevel(tMetaModel.getSafetyLevel());
        tMetaModelRelease.setCreateDate(time);
        tMetaModelRelease.setOperator(username);
        int a = tMetaModelReleaseMapper.insertTMetaModelRelease(tMetaModelRelease);

        // 将版本号写入元模型表关联标签，技术元模型属性表，校验规表，
        // 组合关系表，元模型操作日志，安全元数据表，展示配置表
        int b = tMetaModelMapper.setModelVersion(modelId, modelVersion);

        /**
         * 未做
         */
        // 查询技术元模型属性表拷贝到元模型属性发布表的数据
        // 拷贝到元模型属性发布表

        // 向版本管理添加版本，并生成别名
        TMetaModelVersion tMetaModelVersion = new TMetaModelVersion();
        Long tMetaModelVersionId = IdUtil.getSnowflakeNextId();
        tMetaModelVersion.setId(tMetaModelVersionId);//
        tMetaModelVersion.setModelVersion(modelVersion);
        tMetaModelVersion.setModelId(modelId);
        tMetaModelVersion.setCreateDate(time);
        // 生成别名
        String modelVersionAlias = tableName + "_" + DateUtil.now();
        tMetaModelVersion.setModelVersionAlias(modelVersionAlias);
        int d = tMetaModelVersionMapper.insertTMetaModelVersion(tMetaModelVersion);
        /**
         * 发布：在数据库中拷贝要发布的表(版本别名)
         */
        int c = commonMetaMapper.copyTable(tableName, modelVersionAlias);
        return a + b + c + d;
    }

    /**
     * 删除编辑元模型相关数据
     *
     * @param modelId
     * @param tableName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteMetaMode(Long modelId, String tableName) {
        // 删除编辑元模型相关数据
        int a = tMetaModelMapper.deleteMetaMode(modelId);
        // 根据id逻辑删除元模型表
        int b = tMetaModelMapper.setMetaModelDeleteById(modelId);

        // 删除元模型数据后，在元模型正式库中删除对应的表
        //int meta = commonMetaMapper.deleteTable(tableName);

        int middle = 1;
        // 判断该元模型在转换中间库是否存在
        String tableExist = commonMetaMapper.whetherTheTableExists(tableName);
        if (!StringUtils.isEmpty(tableExist)) {
            // 删除元模型转换中间库中对应的表
            //middle = commonMetaMapper.deleteMiddleDbTable(tableName);
        }
        return a + b + middle;
    }

    /**
     * 复制元模型
     *
     * @param modelId
     * @param modelVersion
     * @param tableName
     * @param tableNameCn
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int copyTMetaModelRelease(Long modelId, String modelVersion, String tableName, String tableNameCn) {
        // 删除编辑元模型相关数据
        int j = tMetaModelMapper.deleteMetaMode(modelId);
        // 根据id逻辑删除元模型表
        int k = tMetaModelMapper.setMetaModelDeleteById(modelId);

        // 设置存放生成属性id的集合
        List<Long> columnIdList = new ArrayList();
        Map<Long, Long> columnInFo = new HashMap<Long, Long>();
        String time = DateUtil.now();
        // 从元模型发布表复制到元模型表
        TMetaModelRelease tMetaModelRelease = tMetaModelReleaseMapper.selectTMetaModelReleaseCopy(modelId, modelVersion);
        TMetaModel tMetaModel = new TMetaModel();
        tMetaModel.setId(modelId);//
        tMetaModel.setTableName(tableName);
        tMetaModel.setTableNameCn(tableNameCn);
        tMetaModel.setModelVersion("edit");
        tMetaModel.setCreateDate(time);
        tMetaModel.setIsDelete(0);
        if (tMetaModelRelease != null) {
            tMetaModel.setCategoryNameId(tMetaModelRelease.getCategoryNameId() != null ? tMetaModelRelease.getCategoryNameId() : null);
            tMetaModel.setDbId(tMetaModelRelease.getDbId() != null ? tMetaModelRelease.getDbId() : null);
            tMetaModel.setSafetyLevel(tMetaModelRelease.getSafetyLevel() != null ? tMetaModelRelease.getSafetyLevel() : null);
        }
        int a = tMetaModelMapper.updateTMetaModel(tMetaModel);

        // 元模型关联标签复制表，根据modelId和版本号
        List<TMetaModelReLabel> tMetaModelReLabelList = tMetaModelReLabelMapper.selectTMetaModelReLabelListCopy(modelId, modelVersion);
        int b = 0;
        if (tMetaModelReLabelList.size() != 0) {
            tMetaModelReLabelList.stream().forEach(response -> {
                Long tMetaModelReLabelId = IdUtil.getSnowflakeNextId();
                response.setId(tMetaModelReLabelId);//
                response.setModelVersion("edit");
                response.setCreateDate(time);
            });
            b = tMetaModelReLabelMapper.insertTMetaModelReLabelList(tMetaModelReLabelList);
        }
        // 技术元模型属性表复制，根据modelId和版本号
        List<TMetaModelTech> tMetaModelTechList = tMetaModelTechMapper.selectMetaModelTechListCopy(modelId, modelVersion);
        int c = 0;
        if (tMetaModelTechList.size() != 0) {
            tMetaModelTechList.stream().forEach(response -> {
                Long tMetaModelTechId = IdUtil.getSnowflakeNextId();
                columnInFo.put(response.getId(), tMetaModelTechId);
                columnIdList.add(tMetaModelTechId);
                response.setId(tMetaModelTechId);//
                response.setModelVersion("edit");
                response.setCreateDate(time);

            });
            c = tMetaModelTechMapper.insertTMetaModelTechList(tMetaModelTechList);
        }

        // 校检规则属性表复制，根据modelId和版本号
        TQualityVerificationRules tQualityVerificationRules = new TQualityVerificationRules();
        tQualityVerificationRules.setMetaModeId(String.valueOf(modelId));
        tQualityVerificationRules.setModelVersion(modelVersion);
        List<TQualityVerificationRules> tQualityVerificationRulesList = tQualityVerificationRulesMapper.selectTQualityVerificationRulesList(tQualityVerificationRules);
        int d = 0;
        if (tQualityVerificationRulesList.size() != 0) {
            for (int i = 0; i < tQualityVerificationRulesList.size(); i++) {
                Long tQualityVerificationRulesId = IdUtil.getSnowflakeNextId();
                tQualityVerificationRulesList.get(i).setId(tQualityVerificationRulesId);//
                tQualityVerificationRulesList.get(i).setCreateTime(DateUtils.getNowDate());
                tQualityVerificationRulesList.get(i).setModelVersion("edit");
                tQualityVerificationRulesList.get(i).setMetaModelColumnId(String.valueOf(columnIdList.get(i)));
            }
            d = tQualityVerificationRulesMapper.insertTQualityVerificationRulesList(tQualityVerificationRulesList);
        }
        // 校检规则关联性规则表复制，根据modelId和版本号
        TQualityAssRuleVerification tQualityAssRuleVerificatio = new TQualityAssRuleVerification();
        tQualityAssRuleVerificatio.setMetaModeId(String.valueOf(modelId));
        tQualityAssRuleVerificatio.setModelVersion(modelVersion);
        List<TQualityAssRuleVerification> tQualityAssRuleVerificationList = tQualityAssRuleVerificationMapper.selectTQualityAssRuleVerificationList(tQualityAssRuleVerificatio);
        int h = 0;
        if (tQualityAssRuleVerificationList.size() != 0) {
            for (int i = 0; i < tQualityAssRuleVerificationList.size(); i++) {
                Long tQualityAssRuleVerificationId = IdUtil.getSnowflakeNextId();
                tQualityAssRuleVerificationList.get(i).setId(tQualityAssRuleVerificationId);//
                tQualityAssRuleVerificationList.get(i).setCreateTime(DateUtils.getNowDate());
                tQualityAssRuleVerificationList.get(i).setModelVersion("edit");
            }
            h = tQualityAssRuleVerificationMapper.insertTQualityAssRuleVerificationList(tQualityAssRuleVerificationList);

        }

        // 组合关系表复制，根据modelId和版本号
        List<TMetaModelRelation> tMetaModelRelationList = tMetaModelRelationMapper.selectMetaModelRelationListCopy(modelId, modelVersion);
        int e = 0;
        if (tMetaModelRelationList.size() != 0) {
            for (int i = 0; i < tMetaModelRelationList.size(); i++) {
                Long tMetaModelRelationId = IdUtil.getSnowflakeNextId();
                tMetaModelRelationList.get(i).setId(tMetaModelRelationId);//
                tMetaModelRelationList.get(i).setModelVersion("edit");
                tMetaModelRelationList.get(i).setCreateDate(time);
                Long columnId = tMetaModelRelationList.get(i).getColumnId();
                if (columnInFo.get(columnId) != null) {
                    tMetaModelRelationList.get(i).setColumnId(columnInFo.get(columnId));
                }
            }
            e = tMetaModelRelationMapper.insertTMetaModelRelationList(tMetaModelRelationList);
        }
        // 安全元数据表复制，根据modelId和版本号
        List<TMetaModelSafe> tMetaModelSafeList = tMetaModelSafeMapper.selectMetaModelSafeListCopy(modelId, modelVersion);
        int f = 0;
        if (tMetaModelSafeList.size() != 0) {
            for (int i = 0; i < tMetaModelSafeList.size(); i++) {
                Long tMetaModelSafeId = IdUtil.getSnowflakeNextId();
                tMetaModelSafeList.get(i).setId(tMetaModelSafeId);//
                tMetaModelSafeList.get(i).setModelVersion("edit");
                tMetaModelSafeList.get(i).setCreateDate(time);
                tMetaModelSafeList.get(i).setColumnId(columnIdList.get(i));
            }
            f = tMetaModelSafeMapper.insertTMetaModelSafeList(tMetaModelSafeList);
        }
        // 展示配置表复制，根据modelId和版本号
        List<TMetaModelShow> tMetaModelShowList = tMetaModelShowMapper.selectMetaModelShowListCopy(modelId, modelVersion);
        int g = 0;
        if (tMetaModelShowList.size() != 0) {
            for (int i = 0; i < tMetaModelShowList.size(); i++) {
                Long tMetaModelShowId = IdUtil.getSnowflakeNextId();
                tMetaModelShowList.get(i).setId(tMetaModelShowId);
                tMetaModelShowList.get(i).setModelVersion("edit");
                tMetaModelShowList.get(i).setCreateDate(time);
                tMetaModelShowList.get(i).setCreateDate(time);
                tMetaModelShowList.get(i).setColumnId(columnIdList.get(i));
            } ;
            g = tMetaModelShowMapper.insertTMetaModelShowList(tMetaModelShowList);
        }

        // 向数据库中复制表
        // 先逻辑删除创建的编辑表

        // 向数据库中复制表，首先根据modelId和modelVersion查询版本别名
        String modelVersionAlias = tMetaModelVersionMapper.selectModelVersionAlias(modelVersion, modelId);
        // 复制表
        int o = commonMetaMapper.deleteTable(tableName);
        int i = commonMetaMapper.copyTable(modelVersionAlias, tableName);
        return a + b + c + d + e + f + g + i + j + k + h;
    }

    /**
     * 查询该元模型的历史版本号
     *
     * @param modelId
     * @return
     */
    @Override
    public List<String> selectMetaModelVersionList(Long modelId) {
        return tMetaModelMapper.selectMetaModelVersionList(modelId);
    }

    /**
     * 查询元模型
     *
     * @param id 元模型主键
     * @return 元模型
     */
    @Override
    public TMetaModel selectTMetaModelById(Long id) {
        return tMetaModelMapper.selectTMetaModelById(id);
    }

    /**
     * 查询元模型列表
     *
     * @param tMetaModel 元模型
     * @return 元模型
     */
    @Override
    public List<TMetaModel> selectTMetaModelList(TMetaModel tMetaModel) {
        return tMetaModelMapper.selectTMetaModelList(tMetaModel);
    }

    /**
     * 新增元模型
     *
     * @param tMetaModel 元模型
     * @return 结果
     */
    @Override
    public int insertTMetaModel(TMetaModel tMetaModel) {

        return tMetaModelMapper.insertTMetaModel(tMetaModel);
    }

    /**
     * 修改元模型
     *
     * @param tMetaModel 元模型
     * @return 结果
     */
    @Override
    public int updateTMetaModel(TMetaModel tMetaModel) {

        return tMetaModelMapper.updateTMetaModel(tMetaModel);
    }

    /**
     * 批量删除元模型
     *
     * @param ids 需要删除的元模型主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelByIds(Long[] ids) {
        return tMetaModelMapper.deleteTMetaModelByIds(ids);
    }

    /**
     * 删除元模型信息
     *
     * @param id 元模型主键
     * @return 结果
     */
    @Override
    public int deleteTMetaModelById(Long id) {
        return tMetaModelMapper.deleteTMetaModelById(id);
    }

    /**
     * 根据id保存业务元数据信息
     *
     * @param modelId
     * @param metadataBuzInfoVo
     * @param oldTableName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int keepTMetaModelBuzById(Long modelId, MetadataBuzInfoVo metadataBuzInfoVo, String oldTableName) {
        int a = 0;
        int b = 0;
        String time = DateUtil.now();
        TMetaModel tMetaModel = new TMetaModel();

        // 标签操作
        if (metadataBuzInfoVo.getLabelId() != null) {
            // 清空以前标签关联表记录
            a = tMetaModelReLabelMapper.deleteReLabel(modelId);
            // ids依次存入标签关联表实体
            List<TMetaModelReLabel> tMetaModelReLabelList = new ArrayList<>();
            for (Long labelId : metadataBuzInfoVo.getLabelId()) {
                Long tMetaModelReLabelId = IdUtil.getSnowflakeNextId();
                TMetaModelReLabel tMetaModelReLabel = new TMetaModelReLabel();
                tMetaModelReLabel.setId(tMetaModelReLabelId);//
                tMetaModelReLabel.setLabelId(labelId);
                tMetaModelReLabel.setModelId(modelId);
                tMetaModelReLabel.setModelVersion("edit");
                tMetaModelReLabel.setCreateDate(time);
                tMetaModelReLabelList.add(tMetaModelReLabel);
            }
            // 标签关联表新增实体集合
            if (tMetaModelReLabelList.size() != 0) {
                b = tMetaModelReLabelMapper.insertTMetaModelReLabelList(tMetaModelReLabelList);
            }
        }

        // 业务元数据操作
        // 向元模型表中保存名称，中文名称，类目id，更新日记
        tMetaModel.setId(modelId);//
        tMetaModel.setTableName(metadataBuzInfoVo.getTableName() != null ? metadataBuzInfoVo.getTableName() : null);
        tMetaModel.setCategoryNameId(metadataBuzInfoVo.getCategoryNameId() != null ? metadataBuzInfoVo.getCategoryNameId() : null);
        tMetaModel.setTableNameCn(metadataBuzInfoVo.getTableNameCn() != null ? metadataBuzInfoVo.getTableNameCn() : null);
        tMetaModel.setUpdateLog(metadataBuzInfoVo.getUpdateLog() != null ? metadataBuzInfoVo.getUpdateLog() : null);
        tMetaModel.setUpdateDate(time);
        tMetaModel.setAuditMode(metadataBuzInfoVo.getAuditMode());
        tMetaModel.setAccessMode(metadataBuzInfoVo.getAccessMode());
        int c = tMetaModelMapper.updateTMetaModel(tMetaModel);

        // 修改数据库中表名
        int d = 0;
        if (!StringUtils.isBlank(oldTableName)) {
            //d = commonMetaMapper.updateTableName(oldTableName, metadataBuzInfoVo.getTableName());
        }

        // 修改注释
        int e = 0;
        if (metadataBuzInfoVo.getTableNameCn() != null) {
            //e = commonMetaMapper.updateTableNameCn(metadataBuzInfoVo.getTableName(), metadataBuzInfoVo.getTableNameCn());
        }
        return a + b + c + d + e;
    }

    @Override
    public MetaModelDetailVo selectTMetaModelDetail(Long id) {
        return tMetaModelMapper.selectTMetaModelDetail(id);
    }

    @Override
    public ModelVo getModelId(String tableName) {
        return tMetaModelMapper.getModelId(tableName);
    }

    @Override
    public String whetherTheTableExists(String tableName) {
        return commonMetaMapper.whetherTheTableExists(tableName);
    }

    /**
     * 查询元模型关联主表的数据库信息
     *
     * @param id 元模型id
     * @return
     */
    @Override
    public Map<String, Object> getMiddleDbInfo(Long id) {
        return tMetaModelMapper.getMiddleDbInfo(id);
    }

    /**
     * 查询元模型字段关联的源数据库信息
     *
     * @param id 元模型id
     * @return
     */
    @Override
    public List<Map<String, Object>> getRelationDbInfo(Long id) {
        return tMetaModelMapper.getRelationDbInfo(id);
    }


    @Override
    public List<TMetaModel> selectMetaModelWithoutIdRuleConfig(String category) {
        List<TMetaModel> list = tMetaModelMapper.selectMetaModelWithoutIdRuleConfig();
        List<TMetaModel> result = new ArrayList<>();
        for (TMetaModel tMetaModel : list) {
            if (tMetaModel.getRemark().equals(category)) {
                result.add(tMetaModel);
            }
        }
        return result;
    }
}
