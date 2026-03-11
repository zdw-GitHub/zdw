package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.tms.business.entity.TMetaModelShowStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "列表属性")
public class GridColumnVo {

    @ApiModelProperty(value = "编号", name = "id", required = false, example = "")
    private Long id;

    @ApiModelProperty(value = "驼峰属性名称", name = "attributeName", required = false, example = "")
    private String attributeName;

    @ApiModelProperty(value = "下划线属性名称", name = "attributeName", required = false, example = "")
    private String attributeNameUnderline;

    @ApiModelProperty(value = "中文属性名称", name = "attributeNameCn", required = false, example = "")
    private String attributeNameCn;

    @ApiModelProperty(value = "所属元模型编号", name = "modelId", required = false, example = "")
    private Long modelId;

    @ApiModelProperty(value = "版本编号", name = "version", required = false, example = "")
    private String version;

    @ApiModelProperty(value = "是否必填", name = "isNotNull", required = false, example = "")
    private String isNotNull;

    @ApiModelProperty(value = "属性类型", name = "attType", required = false, example = "")
    private String attType;

    @ApiModelProperty(value = "是否排序 1 排序 0 不排序", name = "sortable", required = false, example = "")
    private String sortable;

    /** 是否跳转链接  1是，0否 */
    @ApiModelProperty(value = "是否跳转链接  1是，0否", name = "isJumpLink", required = false, example = "")
    private String isJumpLink;

    /** 菜单编号 */
    @ApiModelProperty(value = "菜单编号", name = "menuId", required = false, example = "")
    private Long menuId;

    /** 菜单路由 */
    @ApiModelProperty(value = "菜单路由", name = "menuPath", required = false, example = "")
    private String menuPath;

    /** 元模型编号 */
    @ApiModelProperty(value = "元模型编号", name = "linkReMetaModelId", required = false, example = "")
    private Long linkReMetaModelId;

    /** 元模型名称 */
    @ApiModelProperty(value = "元模型名称", name = "linkReMetaModel", required = false, example = "")
    private String linkReMetaModel;

    /** 元模型名称中文 */
    @ApiModelProperty(value = "元模型名称中文", name = "linkReMetaModelCn", required = false, example = "")
    private String linkReMetaModelCn;

    /** 属性编号 */
    @ApiModelProperty(value = "属性编号", name = "linkReColumnId", required = false, example = "")
    private Long linkReColumnId;

    /** 属性名称 */
    @ApiModelProperty(value = "属性名称", name = "linkReColumnName", required = false, example = "")
    private String linkReColumnName;

    /** 属性名称中文 */
    @ApiModelProperty(value = "属性名称中文", name = "linkReColumnNameCn", required = false, example = "")
    private String linkReColumnNameCn;

    @ApiModelProperty(value = "排序编号", name = "sortNum", required = false, example = "")
    private Integer sortNum;

    /** 是否合并行 1是，0否 */
    @ApiModelProperty(value = "是否合并行 1是，0否", name = "isMergeRows", required = false, example = "")
    private String isMergeRows;

    /** 关联查询条件  and   or */
    @ApiModelProperty(value = "关联查询条件  and   or", name = "relateSearchCondition", required = false, example = "")
    private String relateSearchCondition;

    /** 关联查询属性编号 */
    @ApiModelProperty(value = "关联查询属性编号", name = "relateSearchAttributeId", required = false, example = "")
    private Long relateSearchAttributeId;

    /** 关联查询属性 */
    @ApiModelProperty(value = "关联查询属性", name = "relateSearchAttribute", required = false, example = "")
    private String relateSearchAttribute;

    /** 关联查询命名中文 */
    @ApiModelProperty(value = "关联查询命名中文", name = "relateSearchName", required = false, example = "")
    private String relateSearchName;

    /** 图标 */
    @ApiModelProperty(value = "图标", name = "icon", required = false, example = "")
    private String icon;

    /** 是否个性样式 1是，0否 */
    @ApiModelProperty(value = "是否个性样式", name = "isStyle", required = false, example = "")
    private String isStyle;

    /** 个性样式列表 */
    @ApiModelProperty(value = "个性样式列表", name = "styleList", required = false, example = "")
    private List<TMetaModelShowStyle> styleList;
}
