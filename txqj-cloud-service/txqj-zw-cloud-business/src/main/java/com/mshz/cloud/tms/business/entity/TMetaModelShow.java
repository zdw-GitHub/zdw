package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;

import java.util.List;

/**
 * 展示配置对象 t_meta_model_show
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelShow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 技术元数据属性编号 */
    @Excel(name = "技术元数据属性编号")
    private Long columnId;

    /** 是否查询1是，0否 */
    @Excel(name = "是否查询1是，0否")
    private String isQuery;

    /** 是否显示1是，0否 */
    @Excel(name = "是否显示1是，0否")
    private String isVisible;

    /** 排序编号 */
    @Excel(name = "排序编号")
    private Integer sortNum;

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createDate;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private String updateDate;

    /** 是否支持高级查询索引 1是，0否 */
    @Excel(name = "是否支持高级查询索引 1是，0否")
    private String isAdvancedQueryIndex;

    /** 是否自定义查询1是，0否 */
    @Excel(name = "是否自定义查询1是，0否")
    private String isCustomQuery;

    /**
     * 高级查询顺序
     */
    @Excel(name = "高级查询顺序")
    private String advancedQueryOrder;

    /**
     * 是否高级查询1是，0否
     */
    @Excel(name = "是否高级查询1是，0否")
    private String isAdvancedQuery;

    /**
     * 是否排序  1是，0否
     */
    @Excel(name = "是否排序  1是，0否")
    private String sortable;

    /** 是否跳转链接  1是，0否 */
    @Excel(name = "是否跳转链接  1是，0否")
    private String isJumpLink;

    /** 菜单编号 */
    @Excel(name = "菜单编号")
    private Long menuId;

    /** 菜单路由 */
    @Excel(name = "菜单路由")
    private String menuPath;

    /** 元模型编号 */
    @Excel(name = "元模型编号")
    private Long linkReMetaModelId;

    /** 元模型名称 */
    @Excel(name = "元模型名称")
    private String linkReMetaModel;

    /** 元模型名称中文 */
    @Excel(name = "元模型名称中文")
    private String linkReMetaModelCn;

    /** 属性编号 */
    @Excel(name = "属性编号")
    private Long linkReColumnId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String linkReColumnName;

    /** 属性名称中文 */
    @Excel(name = "属性名称中文")
    private String linkReColumnNameCn;

    /** 是否合并行 1是，0否 */
    @Excel(name = "是否合并行 1是，0否")
    private String isMergeRows;

    /** 关联查询条件  and   or */
    @Excel(name = "关联查询条件  and   or")
    private String relateSearchCondition;

    /** 关联查询属性编号 */
    @Excel(name = "关联查询属性编号")
    private Long relateSearchAttributeId;

    /** 关联查询属性 */
    @Excel(name = "关联查询属性")
    private String relateSearchAttribute;

    /** 关联查询命名中文 */
    @Excel(name = "关联查询命名中文")
    private String relateSearchName;

    /** 图标 */
    @Excel(name = "图标")
    private String icon;

    /** 是否个性样式 1是，0否 */
    @Excel(name = "isStyle")
    private String isStyle;


    /** 个性样式列表 */
    @Excel(name = "styleList")
    private List<TMetaModelShowStyle> styleList;

    public String getIsStyle() {
        return isStyle;
    }

    public void setIsStyle(String isStyle) {
        this.isStyle = isStyle;
    }

    public List<TMetaModelShowStyle> getStyleList() {
        return styleList;
    }

    public void setStyleList(List<TMetaModelShowStyle> styleList) {
        this.styleList = styleList;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setColumnId(Long columnId)
    {
        this.columnId = columnId;
    }

    public Long getColumnId()
    {
        return columnId;
    }
    public void setIsQuery(String isQuery)
    {
        this.isQuery = isQuery;
    }

    public String getIsQuery()
    {
        return isQuery;
    }
    public void setIsVisible(String isVisible)
    {
        this.isVisible = isVisible;
    }

    public String getIsVisible()
    {
        return isVisible;
    }
    public void setSortNum(Integer sortNum)
    {
        this.sortNum = sortNum;
    }

    public Integer getSortNum()
    {
        return sortNum;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion()
    {
        return modelVersion;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public String getIsAdvancedQueryIndex() {
        return isAdvancedQueryIndex;
    }

    public void setIsAdvancedQueryIndex(String isAdvancedQueryIndex) {
        this.isAdvancedQueryIndex = isAdvancedQueryIndex;
    }

    public String getIsCustomQuery() {
        return isCustomQuery;
    }

    public void setIsCustomQuery(String isCustomQuery) {
        this.isCustomQuery = isCustomQuery;
    }

    public String getAdvancedQueryOrder() {
        return advancedQueryOrder;
    }

    public void setAdvancedQueryOrder(String advancedQueryOrder) {
        this.advancedQueryOrder = advancedQueryOrder;
    }

    public String getIsAdvancedQuery() {
        return isAdvancedQuery;
    }

    public void setIsAdvancedQuery(String isAdvancedQuery) {
        this.isAdvancedQuery = isAdvancedQuery;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
    }

    public String getSortable() {
        return sortable;
    }

    public String getIsJumpLink() {
        return isJumpLink;
    }

    public void setIsJumpLink(String isJumpLink) {
        this.isJumpLink = isJumpLink;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public Long getLinkReMetaModelId() {
        return linkReMetaModelId;
    }

    public void setLinkReMetaModelId(Long linkReMetaModelId) {
        this.linkReMetaModelId = linkReMetaModelId;
    }

    public String getLinkReMetaModel() {
        return linkReMetaModel;
    }

    public void setLinkReMetaModel(String linkReMetaModel) {
        this.linkReMetaModel = linkReMetaModel;
    }

    public String getLinkReMetaModelCn() {
        return linkReMetaModelCn;
    }

    public void setLinkReMetaModelCn(String linkReMetaModelCn) {
        this.linkReMetaModelCn = linkReMetaModelCn;
    }

    public Long getLinkReColumnId() {
        return linkReColumnId;
    }

    public void setLinkReColumnId(Long linkReColumnId) {
        this.linkReColumnId = linkReColumnId;
    }

    public String getLinkReColumnName() {
        return linkReColumnName;
    }

    public void setLinkReColumnName(String linkReColumnName) {
        this.linkReColumnName = linkReColumnName;
    }

    public String getLinkReColumnNameCn() {
        return linkReColumnNameCn;
    }

    public void setLinkReColumnNameCn(String linkReColumnNameCn) {
        this.linkReColumnNameCn = linkReColumnNameCn;
    }

    public void setIsMergeRows(String isMergeRows)
    {
        this.isMergeRows = isMergeRows;
    }

    public String getIsMergeRows()
    {
        return isMergeRows;
    }

    public String getRelateSearchCondition() {
        return relateSearchCondition;
    }

    public void setRelateSearchCondition(String relateSearchCondition) {
        this.relateSearchCondition = relateSearchCondition;
    }

    public Long getRelateSearchAttributeId() {
        return relateSearchAttributeId;
    }

    public void setRelateSearchAttributeId(Long relateSearchAttributeId) {
        this.relateSearchAttributeId = relateSearchAttributeId;
    }

    public String getRelateSearchAttribute() {
        return relateSearchAttribute;
    }

    public void setRelateSearchAttribute(String relateSearchAttribute) {
        this.relateSearchAttribute = relateSearchAttribute;
    }

    public String getRelateSearchName() {
        return relateSearchName;
    }

    public void setRelateSearchName(String relateSearchName) {
        this.relateSearchName = relateSearchName;
    }

    @Override
    public String toString() {
        return "TMetaModelShow{" +
                "id=" + id +
                ", modelId=" + modelId +
                ", columnId=" + columnId +
                ", isQuery='" + isQuery + '\'' +
                ", isVisible='" + isVisible + '\'' +
                ", sortNum=" + sortNum +
                ", modelVersion='" + modelVersion + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", isAdvancedQueryIndex='" + isAdvancedQueryIndex + '\'' +
                ", isCustomQuery='" + isCustomQuery + '\'' +
                ", advancedQueryOrder='" + advancedQueryOrder + '\'' +
                ", isAdvancedQuery='" + isAdvancedQuery + '\'' +
                ", sortable='" + sortable + '\'' +
                ", isJumpLink='" + isJumpLink + '\'' +
                ", menuId=" + menuId +
                ", menuPath='" + menuPath + '\'' +
                ", linkReMetaModelId=" + linkReMetaModelId +
                ", linkReMetaModel='" + linkReMetaModel + '\'' +
                ", linkReMetaModelCn='" + linkReMetaModelCn + '\'' +
                ", linkReColumnId=" + linkReColumnId +
                ", linkReColumnName='" + linkReColumnName + '\'' +
                ", linkReColumnNameCn='" + linkReColumnNameCn + '\'' +
                ", isMergeRows='" + isMergeRows + '\'' +
                ", relateSearchCondition='" + relateSearchCondition + '\'' +
                ", relateSearchAttributeId=" + relateSearchAttributeId +
                ", relateSearchAttribute='" + relateSearchAttribute + '\'' +
                ", relateSearchName='" + relateSearchName + '\'' +
                '}';
    }
}
