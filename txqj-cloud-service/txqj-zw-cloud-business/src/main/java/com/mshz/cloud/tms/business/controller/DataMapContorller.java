package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.vo.DataMapParaVo;
import com.mshz.cloud.tms.business.service.IDataMapService;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/dataMap")
@Api(value = "dataMap", tags = {"dataMap"}, description = "数据地图")
public class DataMapContorller {

    @Resource
    private IDataMapService dataMapService;


    /**
     * 获取数据地图数据
     *
     * @return
     */
   //@RequiresPermissions("mm:dataMap:getDataMap")
    @GetMapping("/getDataMap")
    public AjaxResult getDataMap(DataMapParaVo para) {
        return AjaxResult.success(dataMapService.getDataMap(para));
    }
}
