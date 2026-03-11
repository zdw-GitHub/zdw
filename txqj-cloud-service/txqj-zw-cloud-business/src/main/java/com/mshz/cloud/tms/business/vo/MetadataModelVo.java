package com.mshz.cloud.tms.business.vo;

import com.mshz.cloud.tms.business.entity.TMetaModel;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author zxx
 * @since 2022/9/26
 */
@Data
public class MetadataModelVo {
    private String categoryId;
    private String categoryName;
    private List<TMetaModel>  tMetaModels;

}
