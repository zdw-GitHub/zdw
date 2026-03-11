package com.mshz.cloud.tms.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "数据维护入参")
public class MetaDataParaVo {

    @ApiModelProperty(value = "模型编号", name = "modelId", required = false, example = "")
    private Long modelId;

    @ApiModelProperty(value = "版本编号", name = "modelVersion", required = false, example = "")
    private String modelVersion;

    @ApiModelProperty(value = "查询条件", name = "whereAttributes", required = false, example = "")
    private List<MetaDataAttributeVo> whereAttributes;

    @ApiModelProperty(value = "赋值", name = "valueAttributes", required = false, example = "")
    private List<MetaDataAttributeVo> valueAttributes;

    @ApiModelProperty(value = "排序", name = "orderAttributes", required = false, example = "")
    private List<MetaDataAttributeVo> orderAttributes;

    @ApiModelProperty(value = "页码", name = "pageIndex", required = false, example = "")
    private int pageIndex;

    @ApiModelProperty(value = "分页大小", name = "pageSize", required = false, example = "")
    private int pageSize;

    @ApiModelProperty(value = "是否分页  1 是 0 否 ", name = "isPage", required = false, example = "")
    private int isPage = 1;

    @ApiModelProperty(value = "是否分页  1 是 0 否 ", name = "filePath", required = false, example = "")
    private String filePath;

    private String packageWhereConditionLike() {
        StringBuilder sb = new StringBuilder();
        if (whereAttributes != null && whereAttributes.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < whereAttributes.size(); i++) {
                MetaDataAttributeVo record = whereAttributes.get(i);
                if (record.getAttributeValue() != null) {
                    sb.append(record.getAttributeName()).append(" like ")
                        .append("'%" + record.getAttributeValue() + "%'").append(" and ");
                }
            } // for whereAttributes
        }
        String result = sb.toString();
        if (!result.equals(""))
            result = result.substring(0, result.length() - 4);
        return result;
    }

    private String packageWhereCondition(boolean isUsePrimaryKey) {
        StringBuilder sb = new StringBuilder();
        if (whereAttributes != null && whereAttributes.size() > 0) {
            sb.append(" where ");
            for (int i = 0; i < whereAttributes.size(); i++) {
                MetaDataAttributeVo record = whereAttributes.get(i);
                sb.append(record.getAttributeName()).append(" = ").append("'" + record.getAttributeValue() + "'")
                    .append("and");
            } // for whereAttributes
        } else {// 如果没有查询条件，查找主键作为查询条件
                // 是否采用主键作为查询条件
            if (isUsePrimaryKey) {
                if (valueAttributes != null && valueAttributes.size() > 0) {
                    valueAttributesFor:
                    for (int i = 0; i < valueAttributes.size(); i++) {
                        MetaDataAttributeVo record = valueAttributes.get(i);
                        String isPrimaryKey = record.getIsPrimaryKey();
                        if (isPrimaryKey != null && isPrimaryKey.equals("1")) {
                            sb.append(" where ");
                            sb.append(record.getAttributeName()).append(" = ")
                                .append("'" + record.getAttributeValue() + "'").append("and");
                            break valueAttributesFor;
                        }
                    } // for valueAttributes
                } // if valueAttributes
            } // if isUsePrimaryKey
        } // if else
        String result = sb.toString();
        if (!result.equals(""))
            result = result.substring(0, result.length() - 3);
        return result;
    }

    private String packageUpdateSet() {
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            valueAttributesFor:
            for (int i = 0; i < valueAttributes.size(); i++) {
                MetaDataAttributeVo record = valueAttributes.get(i);
                String isPrimaryKey = record.getIsPrimaryKey();
                if (isPrimaryKey == null || (isPrimaryKey != null && !isPrimaryKey.equals("1"))) {
                    sb.append(record.getAttributeName()).append(" = ").append("'" + record.getAttributeValue() + "'")
                        .append(",");
                }
            } // for valueAttributes
        } // if valueAttributes
        String result = sb.toString();
        if (!result.equals(""))
            result = result.substring(0, result.length() - 1);
        return result;
    }

    private String packageInsertAttribute() {
        String result = "";
        StringBuilder sb = new StringBuilder();
        if (valueAttributes != null && valueAttributes.size() > 0) {
            sb.append(" ( ");
            for (int i = 0; i < valueAttributes.size(); i++) {
                MetaDataAttributeVo record = valueAttributes.get(i);
                sb.append("`").append(record.getAttributeName()).append("`").append(",");
            } // for valueAttributes
            result = sb.toString();
            if (!result.equals(" ( "))
                result = result.substring(0, result.length() - 1);
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
                MetaDataAttributeVo record = valueAttributes.get(i);
                String isPrimary = record.getIsPrimaryKey();
                sb.append("'").append(record.getAttributeValue()).append("'").append(",");

            } // for valueAttributes
            result = sb.toString();
            if (!result.equals(" ( "))
                result = result.substring(0, result.length() - 1);
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
                MetaDataAttributeVo record = orderAttributes.get(i);
                sb.append(record.getAttributeName());
                sb.append(" ").append(record.getAttributeValue()).append(",");
            } // for orderAttributes
            result = sb.toString();
            result = result.substring(0, result.length() - 1);
        } // if orderAttributes
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

    public String packageCountSqlLike(String tableName, int isPage) {
        StringBuilder sb = new StringBuilder("select count(1) as COUNTNUM from ");
        sb.append(tableName);
        sb.append(packageWhereConditionLike());
        return sb.toString();
    }

    public String packageExportSql(String tableName, List<MetaModelShowVo> showAttributes) {
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
        sb.append(packageOrderConditon());
        return sb.toString();
    }

}
