package com.mshz.cloud.tms.system.controller;

import com.mshz.cloud.common.entity.vo.ResultFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzt 2025年7月22日
 * @msg 系统-字典
 */
@RestController
@RequestMapping("/sysDic")
@Slf4j
public class SysDicController {
    public ResultFrom getSome(){
        ResultFrom result = new ResultFrom();
        result.getResult();
        return result;
    }
}
