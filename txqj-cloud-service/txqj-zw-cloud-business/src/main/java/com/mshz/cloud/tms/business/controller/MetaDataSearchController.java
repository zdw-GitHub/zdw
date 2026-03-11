package com.mshz.cloud.tms.business.controller;
import cn.hutool.http.HttpStatus;
import com.mshz.cloud.tms.business.vo.GridInitDataVo;
import com.mshz.cloud.tms.business.vo.MetaModelShowVo;
import com.mshz.cloud.tms.business.service.MetaDataSearchService;
import com.mshz.cloud.tms.business.vo.MetaDataParaVo;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author wh
 * @date 2022/10/18
 */

@RestController
@RequestMapping("/metadatasearch")
@Api(value = "metadatasearch", tags = {"metadatasearch"}, description = "元数据查询")
public class MetaDataSearchController {

    @Resource
    private MetaDataSearchService metaDataSearchService;
    private final static Logger logger = LoggerFactory.getLogger(MetaDataSearchController.class);

    /**
     * 初始化展示列表 metadataShowVo 传modeId 和 版本modelVersion
     */
   //@RequiresPermissions("mm:metadatasearch:init")
    @PostMapping("/initgriddata")
    public AjaxResult initGridData(@RequestBody MetaModelShowVo metaModelShowVo) {
        GridInitDataVo data = null;
        try {
            data = metaDataSearchService.initGridData(metaModelShowVo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询异常：", e);
            return AjaxResult.error("查询异常");
        }
        return AjaxResult.success(data);
    }

    /**
     * 获取表格数据
     */
   //@RequiresPermissions("mm:metadatasearch:list")
    @PostMapping("/list")
    public TableDataInfo queryList(@RequestBody MetaDataParaVo para) {
        List list = null;
        long total = 0;
        try {
            list = metaDataSearchService.queryList(para);
            total = metaDataSearchService.queryCount(para);

        } catch (Exception e) {
            logger.error("分页查询异常：", e);
            e.printStackTrace();
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setRows(list);
        rspData.setMsg("查询成功");
        rspData.setTotal(total);
        return rspData;
    }

}
