package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.common.web.page.TableDataInfo;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.tms.business.service.TMetaModelOperatorLogService;
import com.mshz.cloud.tms.business.entity.TMetaModelOperatorLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型操作日志Controller
 *
 * @author zxx
 * @date 2022-09-27
 */
@RestController
@RequestMapping("/log")
@Api(value = "tMetaModelOperatorLog", tags = {"tMetaModelOperatorLog"}, description = "模型操作日志")
public class TMetaModelOperatorLogController extends BaseController
{
    @Autowired
    private TMetaModelOperatorLogService tMetaModelOperatorLogService;

    /**
     * 查询当前编辑元模型的操作日志列表
     */
   //@RequiresPermissions("mm:log:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询当前编辑元模型的操作日志列表")
    public TableDataInfo list(TMetaModelOperatorLog tMetaModelOperatorLog,
                              String startTime,
                              String endTime)
    {
        startPage();
        List<TMetaModelOperatorLog> list = tMetaModelOperatorLogService.selectTMetaModelOperatorLogList(tMetaModelOperatorLog,startTime,endTime);
        return getDataTable(list);
    }


}
