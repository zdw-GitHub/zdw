package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 光缆段纤芯基本信息对象 正式库 sg_tcdev_fiberline_b
 *
 * @author ruoyi
 * @date 2023-04-17
 */
public class SgTcdevFiberlineB extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private String updateDate;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 描述 */
    @Excel(name = "描述")
    private String des;

    /** 所属光缆段 */
    @Excel(name = "所属光缆段")
    private String parFiberSeg;

    /** 所属光缆 */
    @Excel(name = "所属光缆")
    private String parFiber;

    /** 纤芯类型 */
    @Excel(name = "纤芯类型")
    private String fiberlineType;

    /** 序号 */
    @Excel(name = "序号")
    private Long sn;

    /** 使用状态 */
    @Excel(name = "使用状态")
    private String serviceState;

    /** 对应光路 */
    @Excel(name = "对应光路")
    private String opticalroadId;

    /** 对应纤芯通道 */
    @Excel(name = "对应纤芯通道")
    private String channelId;

    /** 损耗（db） */
    @Excel(name = "损耗", readConverterExp = "d=b")
    private Long loss;

    /** 维护单位 */
    @Excel(name = "维护单位")
    private String manageDeptId;

    /** 调管单位 */
    @Excel(name = "调管单位")
    private String authorityUnit;

    /** 拥有者 */
    @Excel(name = "拥有者")
    private String owner;

    /** 更新标志 */
    @Excel(name = "更新标志")
    private String stamp;

    /** 数据类型（0无变动 1新增 2更新 3删除 4 正更新） */
    @Excel(name = "数据类型", readConverterExp = "0=无变动,1=新增,2=更新,3=删除,4=,正=更新")
    private String dataCategory;

    /** 删除标记 */
    @Excel(name = "删除标记")
    private String deleteFlag;

    @ApiModelProperty("操作状态 0:无变动、1:新增、2:修改、3：删除")
    @Excel(name = "操作状态 0:无变动、1:新增、2:修改、3：删除")
    private String editstatus;

    @ApiModelProperty("纤芯类型")
    @Excel(name = "纤芯类型")
    private String fiberlineTypeName;

    @ApiModelProperty("状态")
    @Excel(name = "状态")
    private String serviceStateName;

    @ApiModelProperty("光缆段名称")
    @Excel(name = "光缆段名称")
    private String fiberSegName;

    @ApiModelProperty("光缆名称")
    @Excel(name = "光缆名称")
    private String fiberName;

    @ApiModelProperty("光路名称")
    @Excel(name = "光路名称")
    private String roadName;

    @ApiModelProperty("通道名称")
    @Excel(name = "通道名称")
    private String channelName;

    public String getFiberlineTypeName() {
        return fiberlineTypeName;
    }

    public void setFiberlineTypeName(String fiberlineTypeName) {
        this.fiberlineTypeName = fiberlineTypeName;
    }

    public String getServiceStateName() {
        return serviceStateName;
    }

    public void setServiceStateName(String serviceStateName) {
        this.serviceStateName = serviceStateName;
    }

    public String getFiberSegName() {
        return fiberSegName;
    }

    public void setFiberSegName(String fiberSegName) {
        this.fiberSegName = fiberSegName;
    }

    public String getFiberName() {
        return fiberName;
    }

    public void setFiberName(String fiberName) {
        this.fiberName = fiberName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public void setParFiberSeg(String parFiberSeg) {
        this.parFiberSeg = parFiberSeg;
    }

    public String getParFiberSeg() {
        return parFiberSeg;
    }

    public void setParFiber(String parFiber) {
        this.parFiber = parFiber;
    }

    public String getParFiber() {
        return parFiber;
    }

    public void setFiberlineType(String fiberlineType) {
        this.fiberlineType = fiberlineType;
    }

    public String getFiberlineType() {
        return fiberlineType;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public Long getSn() {
        return sn;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setOpticalroadId(String opticalroadId) {
        this.opticalroadId = opticalroadId;
    }

    public String getOpticalroadId() {
        return opticalroadId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setLoss(Long loss) {
        this.loss = loss;
    }

    public Long getLoss() {
        return loss;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate()).append("id", getId()).append("name", getName())
            .append("des", getDes()).append("parFiberSeg", getParFiberSeg()).append("parFiber", getParFiber())
            .append("fiberlineType", getFiberlineType()).append("sn", getSn()).append("serviceState", getServiceState())
            .append("opticalroadId", getOpticalroadId()).append("channelId", getChannelId()).append("loss", getLoss())
            .append("manageDeptId", getManageDeptId()).append("authorityUnit", getAuthorityUnit())
            .append("owner", getOwner()).append("stamp", getStamp()).append("dataCategory", getDataCategory())
            .append("deleteFlag", getDeleteFlag()).toString();
    }
}
