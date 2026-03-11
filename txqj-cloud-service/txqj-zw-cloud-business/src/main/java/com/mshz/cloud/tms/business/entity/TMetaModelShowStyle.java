package com.mshz.cloud.tms.business.entity;

import com.mshz.cloud.common.annotation.Excel;
import com.mshz.cloud.common.web.domain.BaseEntity;

/**
 * 展示配置对象 t_meta_model_show_style
 *
 * @author zxx
 * @date 2022-09-23
 */
public class TMetaModelShowStyle extends BaseEntity
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

    /** 版本号 */
    @Excel(name = "版本号")
    private String modelVersion;

    /** 值 */
    private String value;

    /** 样式 */
    private String style;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    @Override
    public String toString() {
        return "TMetaModelShowStyle{" +
                "id=" + id +
                ", modelId=" + modelId +
                ", columnId=" + columnId +
                ", modelVersion='" + modelVersion + '\'' +
                ", value='" + value + '\'' +
                ", style=" + style +
                '}';
    }
}
