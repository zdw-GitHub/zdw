package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 技术元模型属性对象 t_meta_model_tech
 *
 * @author jerry
 * @date 2022-10-28
 */
public class TMetaModelTech extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 元模型编号 */
    @Excel(name = "元模型编号")
    private Long modelId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String columnName;

    /**
     * 属性名称中文
     */
    @Excel(name = "属性名称中文")
    private String columnNameCn;

    /** 属性类型 */
    @Excel(name = "属性类型")
    private String columnType;

    /** 属性长度 */
    @Excel(name = "属性长度")
    private String columnLength;

    /** 是否必填1是，0否 */
    @Excel(name = "是否必填1是，0否")
    private String isNotNull;

    /** 是否主键1是，0否 */
    @Excel(name = "是否主键1是，0否")
    private String isPrimaryKey;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    /** 产生形式 */
    @Excel(name = "产生形式")
    private String productionForm;

    /** 数据来源 */
    @Excel(name = "数据来源")
    private String dataSources;

    private String updateOrNot;

    public String getUpdateOrNot() {
        return updateOrNot;
    }

    public void setUpdateOrNot(String updateOrNot) {
        this.updateOrNot = updateOrNot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnNameCn(String columnNameCn) {
        this.columnNameCn = columnNameCn;
    }

    public String getColumnNameCn() {
        return columnNameCn;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setIsNotNull(String isNotNull) {
        this.isNotNull = isNotNull;
    }

    public String getIsNotNull() {
        return isNotNull;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setProductionForm(String productionForm) {
        this.productionForm = productionForm;
    }

    public String getProductionForm() {
        return productionForm;
    }

    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }

    public String getDataSources() {
        return dataSources;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
            .append("modelId", getModelId()).append("columnName", getColumnName())
            .append("columnNameCn", getColumnNameCn()).append("columnType", getColumnType())
            .append("columnLength", getColumnLength()).append("isNotNull", getIsNotNull())
            .append("isPrimaryKey", getIsPrimaryKey()).append("modelVersion", getModelVersion())
            .append("createDate", getCreateDate()).append("updateDate", getUpdateDate())
            .append("productionForm", getProductionForm()).append("remark", getRemark())
            .append("dataSources", getDataSources()).toString();
    }
}
