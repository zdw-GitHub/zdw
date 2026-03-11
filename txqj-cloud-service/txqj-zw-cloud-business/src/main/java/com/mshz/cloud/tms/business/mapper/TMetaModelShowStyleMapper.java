package com.mshz.cloud.tms.business.mapper;

import com.mshz.cloud.tms.business.entity.TMetaModelShowStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 展示配置Mapper接口
 *
 * @author zdw
 * @date 2022-09-23
 */
@Mapper
public interface TMetaModelShowStyleMapper {

    int insertTMetaModelShowStyleList(List<TMetaModelShowStyle> tMetaModelShowList);
    int deleteTMetaModelShowStyleByModelId(@Param("modelId") Long modelId, @Param("columnId") Long columnId);
}
