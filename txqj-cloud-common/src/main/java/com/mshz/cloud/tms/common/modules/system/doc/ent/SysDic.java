package com.mshz.cloud.tms.common.modules.system.doc.ent;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "系统字典表")
@EqualsAndHashCode
@TableName("sys_dic")
public class SysDic {

    private String id;

    private String pid;

    private String code;

    private String name;

    private Integer sort;

    private String isDel;

}
