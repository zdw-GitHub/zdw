package com.mshz.cloud.tms.business.vo;


import com.mshz.cloud.tms.business.entity.TMetaModelShow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/30
 */
@Data
public class MetaModelShowVo extends TMetaModelShow {
    @ApiModelProperty("属性名称")
    private String columnName;

    @ApiModelProperty("属性名称中文")
    private String columnNameCn;

    @ApiModelProperty("属性类型")
    private String columnType;

    @ApiModelProperty("属性长度")
    private String columnLength;

    @ApiModelProperty("是否必填")
    private String isNotNull;

    private String customizedSql;

    private String relMetaMode;

    private String relIdMetaModelColumn;

    private String relNameMetaModelColumn;

    private String sid;

    private String isPrimaryKey;

    private int isPage = 0;

    @ApiModelProperty("排序属性名称")
    private String orderByColumnName;

    @ApiModelProperty("排序类别")
    private String orderByType;

    @ApiModelProperty("是否更新")
    private String updateOrNot;

    @ApiModelProperty("条件组编号")
    private long conditionGroupId;

    public String getOrderByColumnName() {
        if(orderByColumnName!=null){
            return "s."+orderByColumnName;
        }else {
            return orderByColumnName;
        }

    }

    public void setOrderByColumnName(String orderByColumnName) {
        this.orderByColumnName = orderByColumnName;
    }
}
