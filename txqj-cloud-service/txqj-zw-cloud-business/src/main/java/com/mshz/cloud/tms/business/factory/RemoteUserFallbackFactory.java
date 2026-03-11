package com.mshz.cloud.tms.business.factory;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.entity.SysUser;
import com.mshz.cloud.tms.business.service.RemoteUserService;
import com.mshz.cloud.tms.business.domain.LoginUser;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户服务降级处理
 *
 * @author zdw
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<LoginUser> getInfoById(Long userId, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserList(String source) {
                return R.fail("获取用户列表:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}
