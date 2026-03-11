package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.util.JwtUtils;
import com.mshz.cloud.common.util.StringUtils;
import com.mshz.cloud.tms.business.common.service.TokenService;
import com.mshz.cloud.tms.business.domain.LoginUser;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.service.impl.SysLoginService;
import com.mshz.cloud.tms.business.util.AuthUtil;
import com.mshz.cloud.tms.business.util.SecurityUtils;
import com.mshz.cloud.tms.business.vo.LoginBody;
import com.mshz.cloud.tms.business.vo.RegisterBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author zdw
 */
@RestController
@RequestMapping("/auth")
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @PostMapping("accessToken")
    public R<?> accessToken(@RequestBody LoginBody form)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody)
    {
        // 用户注册
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }


    //接收ticket，用ticket获取access_token，换取用户信息
    @GetMapping("authLogin")
    public R<?> authLogin(String accessToken ){

        // 用户登录
        LoginUser userInfo = sysLoginService.authLogin(accessToken);
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

}
