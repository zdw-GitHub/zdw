package com.mshz.cloud.tms.business.factory;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.service.RemoteMetaService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * Create by hzy 2023/1/11
 **/
@Component
public class RemoteMetaFallbackFactory implements FallbackFactory<RemoteMetaService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteMetaService create(Throwable cause) {
        log.error("元数据模块调用失败:{}", cause.getMessage());
        return new RemoteMetaService() {
            @Override
            public R<AjaxResult> enterIntoOfficialDatabase(String modelId, String version, String primaryKey, List<String> ids, List<Map<String, String>> uidList, String source) {
                return R.fail("获取审批入库失败:" + cause.getMessage());
            }

            @Override
            public R<AjaxResult> enterIntoOfficialDatabaseFiber(String modelId, String version, String primaryKey, List<String> ids, String dbName, String source) {
                return R.fail("获取光缆资源审批入库失败:" + cause.getMessage());
            }
        };
    }
}
