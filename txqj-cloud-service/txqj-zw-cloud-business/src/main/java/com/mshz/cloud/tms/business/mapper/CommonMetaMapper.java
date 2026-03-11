package com.mshz.cloud.tms.business.mapper;
import com.mshz.cloud.tms.business.vo.ColumnNameInfoVo;
import com.mshz.cloud.tms.business.vo.CommonVo;
import com.mshz.cloud.tms.business.entity.TDbConfig;
import com.mshz.cloud.tms.business.entity.TDbTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMetaMapper {

    /**
     * 公共查询接口
     *
     * @param vo
     * @return
     */
    List<Map> selectAll(CommonVo vo);



    /**
     * 公共数据新增接口
     *
     * @param vo
     */
    int insertSql(CommonVo vo);

    /**
     * 公共数据更新接口
     *
     * @param vo
     */
    int updateSql(CommonVo vo);

    /**
     * 公共数据删除接口
     *
     * @param vo
     */
    int deleteSql(CommonVo vo);
    /**
     * 在数据库中生成表
     *
     * @param tableName
     * @param tableNameCn
     * @return
     */
    int createTable(@Param("tableName") String tableName, @Param("tableNameCn") String tableNameCn);


    /**
     * 向数据库表中增添字段
     *
     * @param tableName
     * @param columnName
     * @param columnType
     * @param columnLength
     * @param columnNameCn
     * @param isNotNull
     * @return
     */
    int alterTable(@Param("tableName") String tableName, @Param("columnName") String columnName,
        @Param("columnType") String columnType, @Param("columnLength") String columnLength,
        @Param("columnNameCn") String columnNameCn, @Param("isNotNull") String isNotNull);

    /**
     * 设置主键
     *
     * @param tableName
     * @param columnName
     * @return
     */
    int setPrimaryKey(@Param("tableName") String tableName, @Param("columnName") String columnName);

    /**
     * 取消主键
     * @param tableName
     * @return
     */
    int setCancelPrimaryKey(@Param("tableName") String tableName);

    /**
     * 更改数据库表字段属性
     *
     * @param tableName
     * @param columnName
     * @param columnType
     * @param columnLength
     * @param columnNameCn
     * @param isNotNull
     * @param oldColumnName
     * @return
     */
    int updateTable(@Param("tableName") String tableName, @Param("columnName") String columnName,
        @Param("columnType") String columnType, @Param("columnLength") String columnLength,
        @Param("columnNameCn") String columnNameCn, @Param("isNotNull") String isNotNull,
        @Param("oldColumnName") String oldColumnName);

    /**
     * 删除数据库中表中字段
     *
     * @param tableName
     * @param columnName
     * @return
     */
    int deleteTableColumn(@Param("tableName") String tableName, @Param("columnName") String columnName);

    /**
     * 数据库中拷贝要发布的表
     *
     * @param oldTable
     * @param newTable
     * @return
     */
    int copyTable(@Param("oldTable") String oldTable, @Param("newTable") String newTable);

    /**
     * 修改数据库中表名
     *
     * @param oldTableName
     * @param newTableName
     * @return
     */
    int updateTableName(@Param("oldTableName") String oldTableName, @Param("newTableName") String newTableName);

    /**
     * 修改数据空表注释
     *
     * @param tableName
     * @param tableNameCn
     * @return
     */
    int updateTableNameCn(@Param("tableName") String tableName, @Param("tableNameCn") String tableNameCn);

    /**
     * 确定表中是否有主键
     * @param tableName
     * @return
     */
    String selectPrimaryKey(@Param("dbName")String dbName, @Param("tableName")String tableName);

    int deleteTable(@Param("tableName")String tableName);

    int isTableExist(@Param("dbName")String dbName, @Param("tableName")String tableName);

    int doesTheColumnNameExist(@Param("dbName")String dbName, @Param("tableName")String tableName,
                               @Param("columnName")String columnName);

    List<ColumnNameInfoVo> queryAllFieldsInTheTable(@Param("dbName")String dbName, @Param("tableName")String tableName);



    /**
     * 查询数据源管理列表
     *
     * @param tDbConfig 数据源管理
     * @return 数据源管理集合
     */
    public List<TDbConfig> selectTDbConfigList(TDbConfig tDbConfig);

    /**
     * 查询数据库表清单列表
     *
     * @param tDbTable 数据库表清单
     * @return 数据库表清单集合
     */
    public List<TDbTable> selectTDbTableList(TDbTable tDbTable);

    String whetherTheTableExists(String tableName);
}
