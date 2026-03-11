package com.mshz.cloud.tms.business.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by hzy 2023/1/11
 **/
@Data
public class UpdatesVo {
   private List<String> ids = new ArrayList<>();
   private  String modelId;
}
