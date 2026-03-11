package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author wh
 * @date 2022/10/18
 */
public interface MetaDataSearchService {
    /**
     * 初始化展示列表
     *
     * @param metadataShowVo 传modeId 和 版本modelVersion
     * @return 列表初始化信息
     */

    GridInitDataVo initGridData(MetaModelShowVo metadataShowVo);

    /**
     * 获取表格数据
     */
    List<Map> queryList(MetaDataParaVo para);

    /**
     * 获取表格数据总数
     */
    long queryCount(MetaDataParaVo para);


    String getRelationType(MetaModelShowVo para);

    /**
     * 计算字段关联查询sql
     * @param modelId
     * @return
     */
    TestSQLResultVo calcSQl(long modelId, String modelVersion, int relationType);

}
