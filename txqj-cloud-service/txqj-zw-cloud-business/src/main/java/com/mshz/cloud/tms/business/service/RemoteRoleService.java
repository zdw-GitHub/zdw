package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.constant.SecurityConstants;
import com.mshz.cloud.tms.business.constant.ServiceNameConstants;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.factory.RemoteRoleFallbackFactory;
import com.mshz.cloud.tms.business.entity.SysRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * 角色服务
 *
 * @author zdw
 */
@FeignClient(contextId = "remoteRoleService", value = ServiceNameConstants.SYSTEM_SERVICE,
    fallbackFactory = RemoteRoleFallbackFactory.class)
public interface RemoteRoleService {
    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色编号
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/role/info/{roleId}")
    public R<SysRole> getRoleInfo(@PathVariable("roleId") Long roleId,
        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取角色列表
     *
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/role/info/list")
    public R<List<SysRole>> getRoleList(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
