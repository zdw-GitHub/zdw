package com.mshz.cloud.tms.common.modules.system.feign;

import com.mshz.cloud.tms.common.constant.ServiceConstant;
import com.mshz.cloud.tms.common.modules.system.feign.fallback.SysDicFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wuyu
 * @date   2025/7/23
 * @msg 字典服务feign
 */
@FeignClient(name = ServiceConstant.MSHZ_TMS_SYSTEM, contextId = "sysDic", fallback = SysDicFeignClientFallback.class)
public interface SysDicFeignClient {


}