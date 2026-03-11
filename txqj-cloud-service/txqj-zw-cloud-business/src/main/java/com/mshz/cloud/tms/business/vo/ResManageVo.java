package com.mshz.cloud.tms.business.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 公共树形组件-资源维护元模型树组件设置关系vo
 * @author Liuyue
 */
@Data
public class ResManageVo {

    @ApiModelProperty(value = "元模型树形组件配置对象", name = "metaModelTreeSetList")
    List<QualityPublicTreeModelSet> metaModelTreeSetList;

    @ApiModelProperty(value = "公共资源树信息vo", name = "treeInfoVoList")
    private List<TreeInfoVo> treeInfoVoList;

}
