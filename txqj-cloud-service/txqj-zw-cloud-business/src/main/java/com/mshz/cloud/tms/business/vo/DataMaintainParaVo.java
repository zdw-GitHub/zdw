package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.common.web.domain.BaseEntity;
import com.mshz.cloud.tms.business.util.AtrrNameUtils;

import com.mshz.cloud.tms.common.modules.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "数据维护入参")
public class DataMaintainParaVo extends BaseEntity {

    @ApiModelProperty(value = "模型编号", name = "modelId", required = false, example = "")
    private String modelId;

    private String tableName;

    @ApiModelProperty(value = "查询条件", name = "whereAttributes", required = false, example = "")
    private List<DataMaintainAttributeVo> whereAttributes;

    @ApiModelProperty(value = "赋值", name = "valueAttributes", required = false, example = "")
    private List<DataMaintainAttributeVo> valueAttributes;

    @ApiModelProperty(value = "排序", name = "orderAttributes", required = false, example = "")
    private List<DataMaintainAttributeVo> orderAttributes;

    @ApiModelProperty(value = "页码", name = "pageIndex", required = false, example = "")
    private int pageIndex;

    @ApiModelProperty(value = "分页大小", name = "pageSize", required = false, example = "")
    private int pageSize;

    @ApiModelProperty(value = "是否分页  1 是 0 否 ", name = "isPage", required = false, example = "")
    private int isPage = 1;

    @ApiModelProperty(value = "文件路径 ", name = "filePath", required = false, example = "")
    private String filePath;

    @ApiModelProperty(value = "流程任务id", name = "taskNo", required = false, example = "")
    private String taskNo;

    @ApiModelProperty(value = "访问类型", name = "type", required = false, example = "")
    private String type;

    @ApiModelProperty(value = "数据库名称", name = "dbName", required = false, example = "")
    private String dbName;

    private String packageWhereConditionLike() {
        StringBuilder sb = new StringBuilder();
        if (whereAttributes != null && whereAttributes.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < whereAttributes.size(); i++) {
                DataMaintainAttributeVo record = whereAttributes.get(i);
                String attrName = AtrrNameUtils.underline(record.getAttributeName()).toUpperCase();
                if("1".equals(record.getIsPrimaryKey())){
                    sb.append(record.getAttributeName()).append(" = ").append("'" + record.getAttributeValue() + "'").append(" and ");
                }else {
                    sb.append(record.getAttributeName()).append(" like ").append("'%" + record.getAttributeValue() + "%'").append(" and ");
                }

            } // for whereAttributes
        }
        String result = sb.toString();
        if (!result.equals("")){
            result = result.substring(0, result.length() - 4);
        }
        return result;
    }

    private String packageQueryWhereConditionLike(Map<String, MetaModelShowVo> modelShowVoMap, String companyQuery) {
        StringBuilder sb = new StringBuilder();
        if ( (whereAttributes != null && whereAttributes.size() > 0)) {
            sb.append(" where ");
            for (int i = 0; i < whereAttributes.size(); i++) {
                DataMaintainAttributeVo record = whereAttributes.get(i);
                // 排除空值条件""
                if (StringUtils.isNotEmpty(record.getAttributeValue() + "")) {
                    // record.getAttributeName() 若为翻译后字段，则查询条件不起作用
                    String attrName = AtrrNameUtils.underline(record.getAttributeName()).toUpperCase();
                    // 判断查询类型

                    // 高级查询
                    if (record.getAttributeValue().contains(",")) {
                        // 判断是否是第一个属性 是则 不加and
                        if (i == 0) {
                            sb.append(" ");
                        } else {
                            sb.append(" and ");
                        }
                        String[] values = record.getAttributeValue().split(",");
                        sb.append(record.getAttributeName()).append("  in (");
                        for (String value : values) {
                            sb.append("'" + value + "',");
                        }
                        sb = sb.deleteCharAt(sb.length() - 1);
                        sb.append(")");
                        // 自定义查询
                    } else if (record.getLogicalRelationSymbol() != null) {

                        // 判断是否是第一个属性 是则 不加 逻辑关系
                        if (i == 0) {
                            sb.append(record.getAttributeName());
                        } else {
                            sb.append(" " + record.getLogicalRelationSymbol() + " ").append(record.getAttributeName());
                        }
                        // 判断是否为 字典属性
                        MetaModelShowVo metaModelShowVo = modelShowVoMap.get(record.getAttributeName());
                        if (metaModelShowVo != null && metaModelShowVo.getRelMetaMode() != null) {
                            // 处理电压等级去除±
                            String column = " in  (select " + metaModelShowVo.getRelIdMetaModelColumn() + " from " + "``." + metaModelShowVo.getRelMetaMode()
                                + " where REPLACE(" + metaModelShowVo.getRelNameMetaModelColumn() + ",'±','') ";
                            sb.append(column);
                        }

                        if (record.getOperator().equals("包含")) {
                            sb.append(" like ").append("'%" + record.getAttributeValue() + "%'");
                        } else if (record.getOperator().equals("不为空")) {
                            sb.append(" is not null ");
                        } else if (record.getOperator().equals("大于")) {
                            sb.append(" > ").append("'" + record.getAttributeValue() + "'");
                        } else if (record.getOperator().equals("小于")) {
                            sb.append(" < ").append("'" + record.getAttributeValue() + "'");
                        } else if (record.getOperator().equals("等于")) {
                            sb.append(" = ").append("'" + record.getAttributeValue() + "'");
                        } else if (record.getOperator().equals("大于等于")) {
                            sb.append(" >= ").append("'" + record.getAttributeValue() + "'");
                        } else if (record.getOperator().equals("小于等于")) {
                            sb.append(" <= ").append("'" + record.getAttributeValue() + "'");
                        }
                        if (metaModelShowVo != null && metaModelShowVo.getRelMetaMode() != null) {
                            sb.append(" )");
                        }
                        // 高级查询或者普通查询
                    } else {
                        // 判断是否是第一个属性 是则 不加and
                        if (i == 0) {
                            sb.append(" ");
                        } else {
                            sb.append(" and ");
                        }
                        //判断字段是否有关联查询字段
                        if(record.getRelateSearchAttribute()!=null){
                            sb.append("(");
                            sb.append(record.getAttributeName()).append(" like ").append("'%" + record.getAttributeValue() + "%'");
                            sb.append(" ").append(record.getRelateSearchCondition()).append(" ");
                            sb.append(record.getRelateSearchAttribute()).append(" like ").append("'%" + record.getAttributeValue() + "%'");
                            sb.append(")");
                        }else{
                            String attributeValue = record.getAttributeValue();
                            attributeValue = attributeValue != null ? attributeValue.replace(" ", "%") : null;
                            sb.append(record.getAttributeName()).append(" like ").append("'%" + attributeValue + "%'");
                        }
                    }
                }
            } // for whereAttributes
        }
        if (StringUtils.isNotEmpty(companyQuery)) {
            if (StringUtils.isNotEmpty(sb.toString())) {
                sb.append(companyQuery);
            } else {
                sb.append(" where 1=1 ").append(companyQuery);
            }
        }
        return sb.toString();
    }

    private String packageWhereCondition(boolean isUsePrimaryKey) {
        StringBuilder sb = new StringBuilder();
        if (whereAttributes != null && whereAttributes.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < whereAttributes.size(); i++) {
                DataMaintainAttributeVo record = whereAttributes.get(i);
                sb.append(record.getAttributeName()).append(" = ").append("'" + record.getAttributeValue() + "'").append(",");
            } // for whereAttributes
        } else {// 如果没有查询条件，查找主键作为查询条件
                // 是否采用主键作为查询条件
            if (isUsePrimaryKey) {
                if (valueAttributes != null && valueAttributes.size() > 0) {
                    valueAttributesFor:
                    for (int i = 0; i < valueAttributes.size(); i++) {
                        DataMaintainAttributeVo record = valueAttributes.get(i);
                        String isPrimaryKey = record.getIsPrimaryKey();
                        if (isPrimaryKey != null && isPrimaryKey.equals("1")) {
                            sb.append(" where ");
                            sb.append(record.getAttributeName()).append(" = ").append("'" + record.getAttributeValue() + "'").append(",");
                            break valueAttributesFor;
                        }
                    } // for valueAttributes
                } // if valueAttributes
            } // if isUsePrimaryKey
        } // if else
        String result = sb.toString();
        if (!result.equals("")){
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String packageUpdateSet() {
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            valueAttributesFor:
            for (int i = 0; i < valueAttributes.size(); i++) {
                DataMaintainAttributeVo record = valueAttributes.get(i);
                String isPrimaryKey = record.getIsPrimaryKey();
                if (isPrimaryKey == null || (isPrimaryKey != null && !isPrimaryKey.equals("1"))) {
                    String attributeValue = record.getAttributeValue();
                    if(attributeValue != null){
                        attributeValue = "'" + attributeValue + "'";
                    }
                    sb.append(record.getAttributeName()).append(" = ").append(attributeValue).append(",");
                }
            } // for valueAttributes
        } // if valueAttributes
        String result = sb.toString();
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    private String packageUpdateValues() {
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            for (int i = 0; i < valueAttributes.size(); i++) {
                DataMaintainAttributeVo record = valueAttributes.get(i);
                String isPrimaryKey = record.getIsPrimaryKey();
                if (isPrimaryKey == null || (isPrimaryKey != null && !isPrimaryKey.equals("1"))) {
                    sb.append(record.getAttributeName()).append(" = VALUES(").append(record.getAttributeName()).append("),");
                }
            } // for valueAttributes
        } // if valueAttributes
        String result = sb.toString();
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String packageInsertAttribute() {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            sb.append(" ( ");
            for (int i = 0; i < valueAttributes.size(); i++) {
                DataMaintainAttributeVo record = valueAttributes.get(i);
                sb.append("`").append(record.getAttributeName()).append("`").append(",");
            } // for valueAttributes
            result = sb.toString();
            if (!result.equals(" ( ")) {
                result = result.substring(0, result.length() - 1);
            }
            result = result + " ) ";
        } // if valueAttributes
        return result;
    }

    private String packageInsertValues() {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            sb.append(" ( ");
            for (int i = 0; i < valueAttributes.size(); i++) {
                DataMaintainAttributeVo record = valueAttributes.get(i);
                String attributeValue = record.getAttributeValue();
                if(attributeValue != null){
                    attributeValue = "'" + attributeValue + "'";
                }
                sb.append(attributeValue).append(",");
            } // for valueAttributes
            result = sb.toString();
            if (!result.equals(" ( ")){
                result = result.substring(0, result.length() - 1);
            }
            result = result + " ) ";
        } // if valueAttributes
        return result;
    }

    public String packageDeleteSql(String tableName) {
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(tableName);
        sb.append(packageWhereCondition(true));
        return sb.toString();
    }

    public String packageUpdateSql(String tableName) {
        StringBuilder sb = new StringBuilder("update ");
        sb.append(tableName);
        sb.append(" set ");
        sb.append(packageUpdateSet());
        sb.append(packageWhereCondition(true));
        return sb.toString();
    }

    public String packageInsertSql(String tableName) {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(tableName);
        sb.append(packageInsertAttribute());
        sb.append(" values ");
        sb.append(packageInsertValues());
        return sb.toString();
    }

    public String packageUpdateOrInsertSql(String tableName) {
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(tableName);
        sb.append(packageInsertAttribute());
        sb.append(" values ");
        sb.append(packageInsertValues());
        sb.append(" ON DUPLICATE KEY UPDATE ");
        sb.append(packageUpdateValues());
        return sb.toString();
    }

    public String pacakgePageCondition(int isPage) {
        if (isPage == 1) {
            int offset = (pageIndex - 1) * pageSize;
            StringBuilder sb = new StringBuilder(" limit ");
            sb.append(offset);
            sb.append(",");
            sb.append(pageSize);
            return sb.toString();
        }
        return "";
    }

    private String packageOrderConditon() {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (orderAttributes != null && orderAttributes.size() > 0) {
            sb.append(" order by ");
            for (int i = 0; i < orderAttributes.size(); i++) {
                DataMaintainAttributeVo record = orderAttributes.get(i);
                sb.append(record.getAttributeName());
                sb.append(" ").append(record.getAttributeValue()).append(",");
            } // for orderAttributes
            result = sb.toString();
            result = result.substring(0, result.length() - 1);
        }else {
            //电压等级排序规则
            String voltageClass = "CASE {} WHEN '1001' THEN 1 WHEN '2002' THEN 2 WHEN '1003' THEN 3 WHEN '1005' THEN 4 WHEN '2009' THEN 5 WHEN '1006' THEN 6 WHEN '1008' THEN 7 WHEN '1010' THEN 8 WHEN '2100' THEN 9 ELSE 10 END asc";
            //通信站默认排序
            if("SG_TCCON_TCSITE_B".equals(tableName)){
                sb.append(" order by ");
                sb.append(StringUtils.format(voltageClass,"VOLTAGE_CLASS")).append(",");
                sb.append("PAR_GRID asc");
                result = sb.toString();
            }
            //光缆默认排序
            if("SG_TCDEV_FIBER_B".equals(tableName)){
                sb.append(" order by ");
                sb.append(StringUtils.format(voltageClass,"LINE_VOLTAGE_CLASS")).append(",");
                sb.append("A_RESOBJID asc");
                result = sb.toString();
            }
            //业务默认排序
            if("SG_TCCON_TCBUZ_B".equals(tableName)){
                sb.append(" order by ");
                sb.append(StringUtils.format(voltageClass,"VOLTAGE_CLASS")).append(",");
                sb.append("LINE_NAME asc,");
                sb.append("PROTECT_NUM asc,");
                sb.append("PROTECT_PORT asc");
                result = sb.toString();
            }
            //SDH默认排序
            if("SG_TCDEV_TCNESDH_B".equals(tableName)){
                sb.append(" order by ");
                sb.append("CASE IFNULL(TCPLANE,'') WHEN '601633000000000177' THEN 1 WHEN '601633000000000176' THEN 2 WHEN '601633000000000101' THEN 3 WHEN '601633000000000015' THEN 4 WHEN '601633000000000106' THEN 5 WHEN '601633000000000079' THEN 6 WHEN '601633000000000040' THEN 7 WHEN '601633000000000235' THEN 9 WHEN '' THEN 'ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ' ELSE TCPLANE END asc");
                result = sb.toString();
            }
        }
        return result;
    }

    public String packageListSqlLike(String tableName, int isPage) {
        StringBuilder sb = new StringBuilder("select * from ");
        sb.append(tableName);
        sb.append(packageWhereConditionLike());
        sb.append(packageOrderConditon());
        sb.append(pacakgePageCondition(isPage));
        return sb.toString();
    }

    public String packageListSql(String tableName, int isPage) {
        StringBuilder sb = new StringBuilder("select * from ");
        sb.append(tableName);
        sb.append(packageWhereCondition(false));
        sb.append(packageOrderConditon());
        sb.append(pacakgePageCondition(isPage));
        return sb.toString();
    }

    public String packageCountSql(String tableName, int isPage) {
        StringBuilder sb = new StringBuilder("select count(1) as COUNTNUM from ");
        sb.append(tableName);
        sb.append(packageWhereCondition(false));
        return sb.toString();
    }

    public String packageQueryCountSql(String tableName, int isPage, List<MetaModelShowVo> list, String companyQuerySql) {
        Map<String, MetaModelShowVo> metaModelShowVoMap = new HashMap<>();
        for (MetaModelShowVo metaModelShowVo : list) {
            metaModelShowVoMap.put(metaModelShowVo.getColumnName(), metaModelShowVo);
        }
        StringBuilder sb = new StringBuilder("select count(1) as COUNTNUM from ");
        sb.append(tableName);
        sb.append(packageQueryWhereConditionLike(metaModelShowVoMap, companyQuerySql));
        return sb.toString();
    }

    public String packageExportSql(String tableName, List<MetaModelShowVo> showAttributes) {
        StringBuilder sb = new StringBuilder("select ");
        if (showAttributes != null && showAttributes.size() > 0) {
            StringBuilder recordSB = new StringBuilder();
            for (int i = 0; i < showAttributes.size(); i++) {
                MetaModelShowVo record = showAttributes.get(i);
                String column = "";
                if (record.getRelNameMetaModelColumn() != null && !record.getRelNameMetaModelColumn().equals("")) {
                    column = "(select " + record.getRelNameMetaModelColumn() + " from " + record.getRelMetaMode() + " where " + record.getRelIdMetaModelColumn() + "=t."
                        + record.getColumnName() + ")" + record.getColumnName();
                } else {
                    column = "t." + record.getColumnName();
                }
                recordSB.append(column).append(",");
            }
            String result = recordSB.toString();
            result = result.substring(0, result.length() - 1);
            sb.append(result);
        } else {
            sb.append(" * ");
        }
        sb.append(" from ");
        sb.append(tableName.concat(" t"));
        sb.append(packageWhereConditionLike());
        sb.append(packageOrderConditon());
        sb.append(pacakgePageCondition(isPage));
        return sb.toString();
    }

    public String packageQuerySql(String tableName, String companyQuery, List<MetaModelShowVo> showAttributes) {
        // 封装属性信息 包括字典属性信息
        Map<String, MetaModelShowVo> metaModelShowVoMap = new HashMap<>();
        for (MetaModelShowVo metaModelShowVo : showAttributes) {
            metaModelShowVoMap.put(metaModelShowVo.getColumnName(), metaModelShowVo);
        }
        StringBuilder sb = new StringBuilder("select ");
        if (showAttributes != null && showAttributes.size() > 0) {
            StringBuilder recordSB = new StringBuilder();
            for (int i = 0; i < showAttributes.size(); i++) {
                MetaModelShowVo record = showAttributes.get(i);
                String column = "";
                if (record.getRelNameMetaModelColumn() != null && !record.getRelNameMetaModelColumn().equals("")) {
                    if (record.getCustomizedSql() != null && !record.getCustomizedSql().equals("")) {
                        column = "(select " + record.getRelNameMetaModelColumn() + " from (" + record.getCustomizedSql().toLowerCase() + ") taleAlies where code=t." + record.getColumnName()
                            + ")" + record.getColumnName();
                    } else {
                        column = "(select " + record.getRelNameMetaModelColumn() + " from " + record.getRelMetaMode().toLowerCase() + " where " + record.getRelIdMetaModelColumn() + "=t."
                                + record.getColumnName() + ")" + record.getColumnName();
                    }
                } else {
                    column = "t." + record.getColumnName();
                }
                recordSB.append(column).append(",");
            }
            String result = recordSB.toString();
            result = result.substring(0, result.length() - 1);
            sb.append(result);
        } else {
            sb.append(" * ");
        }
        sb.append(" from ");
        sb.append(tableName.concat(" t"));
        sb.append(packageQueryWhereConditionLike(metaModelShowVoMap, companyQuery));
        sb.append(packageOrderConditon());
        sb.append(pacakgePageCondition(isPage));
        return sb.toString();
    }

    public String packageOne(String tableName, List<MetaModelShowVo> showAttributes) {
        StringBuilder sb = new StringBuilder("select ");
        if (showAttributes != null && showAttributes.size() > 0) {
            StringBuilder recordSB = new StringBuilder();
            for (int i = 0; i < showAttributes.size(); i++) {
                MetaModelShowVo record = showAttributes.get(i);
                recordSB.append(record.getColumnName()).append(",");
            }
            String result = recordSB.toString();
            result = result.substring(0, result.length() - 1);
            sb.append(result);
        } else {
            sb.append(" * ");
        }
        sb.append(" from ");
        sb.append(tableName);
        sb.append(packageWhereConditionLike());
        return sb.toString();
    }

}
