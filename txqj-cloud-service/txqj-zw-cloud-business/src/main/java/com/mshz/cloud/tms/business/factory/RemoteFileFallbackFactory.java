package com.mshz.cloud.tms.business.factory;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.service.RemoteFileService;
import com.mshz.cloud.tms.business.entity.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务降级处理
 *
 * @author zdw
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService()
        {
            @Override
            public R<SysFile> upload(MultipartFile file)
            {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }
        };
    }
}
