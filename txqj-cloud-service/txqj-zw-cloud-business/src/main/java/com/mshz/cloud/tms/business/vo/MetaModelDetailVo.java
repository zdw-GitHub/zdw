package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.tms.business.entity.TMetaModel;
import lombok.Data;

/**
 * @author wh
 * @date 2022/11/18
 */
@Data
public class MetaModelDetailVo extends TMetaModel {

    private String relationMainDbUserName;

    private String relationMainDbName;

    private String relationMainDbNameCn;

    private String relationMainTableName;

    private String relationMainTableNameCn;
}
