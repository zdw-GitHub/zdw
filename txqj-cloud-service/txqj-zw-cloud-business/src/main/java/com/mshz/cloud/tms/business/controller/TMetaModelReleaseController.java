package com.mshz.cloud.tms.business.controller;

import com.mshz.cloud.tms.business.entity.TMetaModelRelease;
import com.mshz.cloud.tms.business.service.TMetaModelReleaseService;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 元模型发布Controller
 *
 * @author zxx
 * @date 2022-10-10
 */
@RestController
@RequestMapping("/release")
@Api(value = "tMetaModelRelease", tags = {"MetaModelRelease"}, description = "发布元模型")
public class TMetaModelReleaseController extends BaseController
{
    @Autowired
    private TMetaModelReleaseService tMetaModelReleaseService;

    /**
     * 历史版本管理
     */
   //@RequiresPermissions("mm:release:historicalVersionManagement")
    @GetMapping("/historicalVersionManagement")
    @ApiOperation(value = "历史版本管理")
    public TableDataInfo historicalVersionManagement(TMetaModelRelease tMetaModelRelease, String startTime,
                                                     String endTime)
    {
        startPage();
        List<TMetaModelRelease> list = tMetaModelReleaseService.selectHistoricalVersionManagement(tMetaModelRelease, startTime,endTime);
        return getDataTable(list);
    }
}
