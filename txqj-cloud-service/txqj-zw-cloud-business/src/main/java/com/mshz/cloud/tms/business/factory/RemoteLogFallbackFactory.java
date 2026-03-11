package com.mshz.cloud.tms.business.factory;

import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.entity.SysOperLog;
import com.mshz.cloud.tms.business.service.RemoteLogService;
import com.mshz.cloud.tms.business.entity.SysLogininfor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author zdw
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable)
    {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source)
            {
                return null;
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source)
            {
                return null;
            }
        };

    }
}
