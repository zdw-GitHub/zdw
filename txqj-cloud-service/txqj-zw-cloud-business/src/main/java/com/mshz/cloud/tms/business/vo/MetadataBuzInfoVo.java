package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/23
 */
@Data
public class MetadataBuzInfoVo {
    /*元模型表*/
    /**名称*/
    @ApiModelProperty("名称")
    private  String tableName;
    /**中文名称*/
    @ApiModelProperty("中文名称")
    private  String tableNameCn;
    /**版本号*/
    @ApiModelProperty("版本号")
    private  String modelVersion;
    /**状态*/
    @ApiModelProperty("状态")
    private  String modelStatus;
    /**更新日记*/
    @ApiModelProperty("更新日记")
    private  String updateLog;

    /*标签关联表*/
    /**标签名多个*/
    @ApiModelProperty("标签名多个")
    private List<String> labelNameList;
    @ApiModelProperty("标签id多个")
    private List<Long> labelId;
    /*类目表*/
    /**类目名称*/
    @ApiModelProperty("类目名称")
    private  String categoryName;
    /**类目名称*/
    @ApiModelProperty("类目id")
    private  Long categoryNameId;

    /**审计模式*/
    @ApiModelProperty("审计模式")
    private  String auditMode;

    /**接入模式*/
    @ApiModelProperty("接入模式")
    private  String accessMode;

    /**同步方式*/
    @ApiModelProperty("同步方式")
    private String dataStorageMode;

    /**关联方式*/
    @ApiModelProperty("关联方式")
    private String dataRelationType;


}
