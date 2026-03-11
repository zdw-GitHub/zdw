package com.mshz.cloud.tms.business.factory;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.entity.SysRole;
import com.mshz.cloud.tms.business.service.RemoteRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 角色服务降级处理
 *
 * @author zdw
 */
@Component
public class RemoteRoleFallbackFactory implements FallbackFactory<RemoteRoleService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteRoleFallbackFactory.class);

    @Override
    public RemoteRoleService create(Throwable throwable) {
        log.error("角色服务调用失败:{}", throwable.getMessage());
        return new RemoteRoleService() {
            @Override
            public R<SysRole> getRoleInfo(Long roleId, String source) {
                return R.fail("获取角色信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysRole>> getRoleList(String source) {
                return R.fail("获取角色列表信息失败:" + throwable.getMessage());
            }

        };
    }
}
