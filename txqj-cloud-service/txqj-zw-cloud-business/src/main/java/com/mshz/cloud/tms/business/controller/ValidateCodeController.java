package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.exception.CaptchaException;
import com.mshz.cloud.tms.business.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * MVC版验证码接口Controller（替代WebFlux的RouterFunction）
 * 调用已有的ValidateCodeServiceImpl，适配你的业务服务架构
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeService validateCodeService;

    /**
     * 验证码生成接口（路径：/txqj-zw-cloud-business/code）
     * 前端请求这个接口，直接调用Service的createCaptcha()方法
     */
    @GetMapping("/code")
    public AjaxResult getCode() {
        try {
            // 调用你现有的Service生成验证码
            return validateCodeService.createCaptcha();
        } catch (IOException | CaptchaException e) {
            // 异常处理，返回错误信息
            return AjaxResult.error("验证码生成失败：" + e.getMessage());
        }
    }

    /**
     * 可选：验证码校验接口
     */
    @GetMapping("/code/check")
    public AjaxResult checkCode(String code, String uuid) {
        try {
            validateCodeService.checkCaptcha(code, uuid);
            return AjaxResult.success("验证码校验成功");
        } catch (CaptchaException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
