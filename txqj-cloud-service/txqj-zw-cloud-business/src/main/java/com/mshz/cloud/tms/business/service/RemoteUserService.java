package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.constant.SecurityConstants;
import com.mshz.cloud.tms.business.constant.ServiceNameConstants;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.factory.RemoteUserFallbackFactory;
import com.mshz.cloud.tms.business.entity.SysUser;
import com.mshz.cloud.tms.business.domain.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 用户服务
 *
 * @author zdw
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE,
    fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username,
        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/user/infobyid/{userId}")
    public R<LoginUser> getInfoById(@PathVariable(value = "userId", required = false) Long userId,
        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取用户列表
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/user/info/list")
    public R<List<SysUser>> getUserList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser,
        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
