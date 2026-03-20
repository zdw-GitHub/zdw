package com.mshz.cloud.tms.business.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 光缆段基本信息对象 正式库 sg_tcdev_fiberseg_b
 *
 * @author ruoyi
 * @date 2023-03-07
 */
public class SgTcdevFibersegB extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(" 创建时间")
    @Excel(name = "创建时间")
    private String createDate;

    @ApiModelProperty(" 更新时间")
    @Excel(name = "更新时间")
    private String updateDate;

    @ApiModelProperty(" ID")
    @Excel(name = "ID")
    private String id;

    @ApiModelProperty(" 名称")
    @Excel(name = "名称")
    private String name;

    @ApiModelProperty(" 描述")
    @Excel(name = "描述")
    private String des;

    @ApiModelProperty(" 起点接续装置")
    @Excel(name = "起点接续装置")
    private String aResobjid;

    @ApiModelProperty(" 终点接续装置")
    @Excel(name = "终点接续装置")
    private String zResobjid;

    @ApiModelProperty(" 芯数")
    @Excel(name = "芯数")
    private Long lineNumber;

    @ApiModelProperty(" 已用芯数")
    @Excel(name = "已用芯数")
    private Long lineNumberUsed;

    @ApiModelProperty(" 不可用芯数")
    @Excel(name = "不可用芯数")
    private Long lineNumberFree;

    @ApiModelProperty(" 序号")
    @Excel(name = "序号")
    private Long sn;

    @ApiModelProperty(" 光缆段类型")
    @Excel(name = "光缆段类型")
    private String fiberType;

    @ApiModelProperty(" 光缆段长度千米")
    @Excel(name = "光缆段长度千米")
    private BigDecimal fiberLength;

    @ApiModelProperty(" 是否为站内光缆")
    @Excel(name = "是否为站内光缆")
    private Long isInstation;

    @ApiModelProperty(" 敷设方式")
    @Excel(name = "敷设方式")
    private String layingType;

    @ApiModelProperty(" 运行状态")
    @Excel(name = "运行状态")
    private String serviceState;

    @ApiModelProperty(" 投运日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投运日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateDate;

    @ApiModelProperty(" 退运日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退运日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    @ApiModelProperty(" 资产单位")
    @Excel(name = "资产单位")
    private String assetsOwnershipComId;

    @ApiModelProperty(" 维护单位")
    @Excel(name = "维护单位")
    private String manageDeptId;

    @ApiModelProperty(" 调管单位")
    @Excel(name = "调管单位")
    private String authorityUnit;

    @ApiModelProperty(" 实物ID")
    @Excel(name = "实物ID")
    private String swid;

    @ApiModelProperty(" 资产ID")
    @Excel(name = "资产ID")
    private String erpId;

    @ApiModelProperty(" 拥有者")
    @Excel(name = "拥有者")
    private String owner;

    @ApiModelProperty(" 更新标志")
    @Excel(name = "更新标志")
    private String stamp;

    @ApiModelProperty(" 生产厂家")
    @Excel(name = "生产厂家")
    private String producerName;

    @Excel(name = "数据类型 0无变动  1新增 2更新 3删除 4 正更新")
    private String dataCategory;

    @ApiModelProperty(" 删除标记 0删除")
    @Excel(name = "删除标记  0删除 ")
    private String deleteFlag;

    @ApiModelProperty("操作状态 0:无变动、1:新增、2:修改、3：删除")
    @Excel(name = "操作状态 0:无变动、1:新增、2:修改、3：删除")
    private String editstatus;

    @ApiModelProperty("a段资源名称")
    @Excel(name = "a段资源名称")
    private String aResobjidName;

    @ApiModelProperty("z段资源名称")
    @Excel(name = "z段资源名称")
    private String zResobjidName;

    @ApiModelProperty("光缆段类型")
    @Excel(name = "光缆段类型")
    private String fiberTypeName;

    @ApiModelProperty("生产厂家")
    @Excel(name = "生产厂家")
    private String producerNameLabel;

    public String getProducerNameLabel() {
        return producerNameLabel;
    }

    public void setProducerNameLabel(String producerNameLabel) {
        this.producerNameLabel = producerNameLabel;
    }

    public String getaResobjidName() {
        return aResobjidName;
    }

    public void setaResobjidName(String aResobjidName) {
        this.aResobjidName = aResobjidName;
    }

    public String getzResobjidName() {
        return zResobjidName;
    }

    public void setzResobjidName(String zResobjidName) {
        this.zResobjidName = zResobjidName;
    }

    public String getFiberTypeName() {
        return fiberTypeName;
    }

    public void setFiberTypeName(String fiberTypeName) {
        this.fiberTypeName = fiberTypeName;
    }

    public String getEditstatus() {
        return editstatus;
    }

    public void setEditstatus(String editstatus) {
        this.editstatus = editstatus;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setaResobjid(String aResobjid) {
        this.aResobjid = aResobjid;
    }

    public String getaResobjid() {
        return aResobjid;
    }

    public void setzResobjid(String zResobjid) {
        this.zResobjid = zResobjid;
    }

    public String getzResobjid() {
        return zResobjid;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumberUsed(Long lineNumberUsed) {
        this.lineNumberUsed = lineNumberUsed;
    }

    public Long getLineNumberUsed() {
        return lineNumberUsed;
    }

    public void setLineNumberFree(Long lineNumberFree) {
        this.lineNumberFree = lineNumberFree;
    }

    public Long getLineNumberFree() {
        return lineNumberFree;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public Long getSn() {
        return sn;
    }

    public void setFiberType(String fiberType) {
        this.fiberType = fiberType;
    }

    public String getFiberType() {
        return fiberType;
    }

    public void setFiberLength(BigDecimal fiberLength) {
        this.fiberLength = fiberLength;
    }

    public BigDecimal getFiberLength() {
        return fiberLength;
    }

    public void setIsInstation(Long isInstation) {
        this.isInstation = isInstation;
    }

    public Long getIsInstation() {
        return isInstation;
    }

    public void setLayingType(String layingType) {
        this.layingType = layingType;
    }

    public String getLayingType() {
        return layingType;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setAssetsOwnershipComId(String assetsOwnershipComId) {
        this.assetsOwnershipComId = assetsOwnershipComId;
    }

    public String getAssetsOwnershipComId() {
        return assetsOwnershipComId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setAuthorityUnit(String authorityUnit) {
        this.authorityUnit = authorityUnit;
    }

    public String getAuthorityUnit() {
        return authorityUnit;
    }

    public void setSwid(String swid) {
        this.swid = swid;
    }

    public String getSwid() {
        return swid;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    public String getErpId() {
        return erpId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getStamp() {
        return stamp;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setDataCategory(String dataCategory) {
        this.dataCategory = dataCategory;
    }

    public String getDataCategory() {
        return dataCategory;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("createDate", getCreateDate()).append("updateDate", getUpdateDate()).append("id", getId())
            .append("name", getName()).append("des", getDes()).append("aResobjid", getaResobjid()).append("zResobjid", getzResobjid()).append("lineNumber", getLineNumber())
            .append("lineNumberUsed", getLineNumberUsed()).append("lineNumberFree", getLineNumberFree()).append("sn", getSn()).append("fiberType", getFiberType())
            .append("fiberLength", getFiberLength()).append("isInstation", getIsInstation()).append("layingType", getLayingType()).append("serviceState", getServiceState())
            .append("operateDate", getOperateDate()).append("expiryDate", getExpiryDate()).append("assetsOwnershipComId", getAssetsOwnershipComId())
            .append("manageDeptId", getManageDeptId()).append("authorityUnit", getAuthorityUnit()).append("swid", getSwid()).append("erpId", getErpId()).append("owner", getOwner())
            .append("stamp", getStamp()).append("producerName", getProducerName()).append("dataCategory", getDataCategory()).append("deleteFlag", getDeleteFlag()).toString();
    }
}
