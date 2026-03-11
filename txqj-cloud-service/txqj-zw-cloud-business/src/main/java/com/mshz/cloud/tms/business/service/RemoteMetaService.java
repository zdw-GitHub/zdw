package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.constant.SecurityConstants;
import com.mshz.cloud.tms.business.constant.ServiceNameConstants;
import com.mshz.cloud.tms.business.domain.R;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.factory.RemoteMetaFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

/**
 * Create by hzy 2023/1/11
 **/
@FeignClient(contextId = "remoteMetaService", value = ServiceNameConstants.META_SERVICE, fallbackFactory = RemoteMetaFallbackFactory.class)
public interface RemoteMetaService {

    @PostMapping("/metadatasearch/enter")
    public R<AjaxResult> enterIntoOfficialDatabase(@RequestParam("modelId") String modelId, @RequestParam("version") String version, @RequestParam("primaryKey") String primaryKey,
        @RequestParam("ids") List<String> ids, @RequestBody List<Map<String, String>> uidList, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/metadatasearch/enterFiber")
    public R<AjaxResult> enterIntoOfficialDatabaseFiber(@RequestParam("modelId") String modelId, @RequestParam("version") String version,
        @RequestParam("primaryKey") String primaryKey, @RequestParam("ids") List<String> ids, @RequestParam("dbName") String dbName,
        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
