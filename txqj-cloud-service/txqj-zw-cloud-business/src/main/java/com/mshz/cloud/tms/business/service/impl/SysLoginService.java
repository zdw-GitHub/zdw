package com.mshz.cloud.tms.business.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mshz.cloud.common.exception.ServiceException;
import com.mshz.cloud.common.service.RedisService;
import com.mshz.cloud.tms.business.config.Auth2Config;
import com.mshz.cloud.tms.business.config.MetadataDBConfig;
import com.mshz.cloud.tms.business.constant.CacheConstants;
import com.mshz.cloud.tms.business.constant.Constants;
import com.mshz.cloud.tms.business.constant.SecurityConstants;
import com.mshz.cloud.tms.business.constant.UserConstants;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.entity.SysUser;
import com.mshz.cloud.tms.business.enums.UserStatus;
import com.mshz.cloud.tms.business.service.RemoteUserService;
import com.mshz.cloud.tms.business.util.SecurityUtils;
import com.mshz.cloud.common.util.StringUtils;
import com.mshz.cloud.tms.business.domain.LoginUser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author zdw
 */
@Component
public class SysLoginService
{
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    @Resource
    private Auth2Config auth2;


    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + username + " 不存在");
        }

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        //临时用户不校验密码
        if(user.getDeptId() != 130){
            passwordService.validate(user, password);
        }
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }



    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, "注册成功");
    }





    public LoginUser authLogin(String accessToken) {
        LoginUser loginUser = null;
        System.out.println("accessToken:"+accessToken);
        if (accessToken!=null&&!accessToken.equals("")) {
            //根据access_token获取用户信息
            try {
                //从统一权限同步用户
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(auth2.getUserMessage()+"?access_token="+accessToken);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                System.out.println("entity:"+entity.toString());
                String jsonString = EntityUtils.toString(entity);
                System.out.println("jsonString:"+jsonString);
                ObjectMapper mapper = new ObjectMapper();
                Map data = mapper.readValue(jsonString, Map.class);
                System.out.println("data:" +data.toString());

                String username = data.get("iscAdCode").toString();
                //String username = "ceshi";
                System.out.println("username:" +username);
                // 查询用户信息
                R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
                //本地数据库中不存在该用户
                if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
                    //开启注册用户
                    redisService.setCacheObject(CacheConstants.SYS_CONFIG_KEY+"sys.account.registerUser","true");
                    String nickName = data.get("name").toString();
                    //String nickName = "测试";
                    System.out.println("nickName:" +nickName);
                    SysUser user = new SysUser();
                    user.setUserName(username);
                    user.setNickName(nickName);
                    user.setDeptId(130L);
                    user.setStatus("0");
                    //设置角色
                    Long[] roles = {9L};
                    user.setRoleIds(roles);
                    //注册用户
                    R<Boolean> r = remoteUserService.registerUserInfo(user, SecurityConstants.INNER);
                    if (r.getData()){
                        System.out.println("用户注册成功");
                        userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
                    }else {
                        System.out.println("用户注册失败："+r.getMsg());
                    }
                }
                loginUser = userResult.getData();
                System.out.println("loginUser：" + loginUser);
                //登录
                recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
                response.close();
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return loginUser;
    }


}
