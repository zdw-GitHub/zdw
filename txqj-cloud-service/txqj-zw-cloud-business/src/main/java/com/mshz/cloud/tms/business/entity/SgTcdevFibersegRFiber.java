package com.mshz.cloud.tms.business.entity;
import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/***
 * 光缆与光缆段关系对象 正式库 sg_tcdev_fiberseg_r_fiber
 *
 * @author ruoyi
 * @date 2023-04-17
 */
public class SgTcdevFibersegRFiber extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(" 创建时间 ")
    @Excel(name = "创建时间")
    private String createDate;

    @ApiModelProperty(" 更新时间 ")
    @Excel(name = "更新时间")
    private String updateDate;

    @ApiModelProperty(" ID ")
    private String id;

    @ApiModelProperty(" 光缆编号 ")
    @Excel(name = "光缆编号")
    private String fiberId;

    @ApiModelProperty(" 光缆段编号 ")
    @Excel(name = "光缆段编号")
    private String fibersegId;

    @ApiModelProperty(" 拥有者 ")
    @Excel(name = "拥有者")
    private String owner;

    @ApiModelProperty(" 更新标志 ")
    @Excel(name = "更新标志")
    private String stamp;

    @ApiModelProperty(" 数据类型 （0无变动 1新增 2更新 3删除 4 正更新） ")
    @Excel(name = "数据类型 ", readConverterExp = "0=无变动,1=新增,2=更新,3=删除,4=,正=更新")
    private String dataCategory;

    @ApiModelProperty(" 删除标记 0删除 ")
    @Excel(name = "删除标记  0删除 ")
    private String deleteFlag;

    @ApiModelProperty("操作状态 0:无变动、1:新增、2:修改、3：删除")
    @Excel(name = "操作状态 0:无变动、1:新增、2:修改、3：删除")
    private String editstatus;

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

    public void setFiberId(String fiberId) {
        this.fiberId = fiberId;
    }

    public String getFiberId() {
        return fiberId;
    }

    public void setFibersegId(String fibersegId) {
        this.fibersegId = fibersegId;
    }

    public String getFibersegId() {
        return fibersegId;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("createDate", getCreateDate()).append("updateDate", getUpdateDate()).append("id", getId())
            .append("fiberId", getFiberId()).append("fibersegId", getFibersegId()).append("owner", getOwner()).append("stamp", getStamp()).append("dataCategory", getDataCategory())
            .append("deleteFlag", getDeleteFlag()).toString();
    }
}
