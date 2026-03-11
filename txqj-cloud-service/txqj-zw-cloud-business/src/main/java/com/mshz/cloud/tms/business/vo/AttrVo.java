package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.common.web.domain.BaseEntity;
import lombok.Data;

/**
 * Creat By  cuiHy on 2022/11/8.
 */
@Data
public class AttrVo extends BaseEntity {

    Long modelId;
    String columnName;
    String modelVersion;
}
