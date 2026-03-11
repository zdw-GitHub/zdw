package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.exception.CaptchaException;
import java.io.IOException;

/**
 * 验证码处理
 *
 * @author zdw
 */
public interface ValidateCodeService
{
    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
