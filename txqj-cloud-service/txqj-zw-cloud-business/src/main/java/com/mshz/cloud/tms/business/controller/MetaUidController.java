package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.entity.TMetaModelUid;
import com.mshz.cloud.tms.business.entity.TMetaSourceUid;
import com.mshz.cloud.tms.business.vo.MetaUidColumnVo;
import com.mshz.cloud.tms.business.vo.MetaUidTableVo;
import com.mshz.cloud.tms.business.vo.MetaUidVo;
import com.mshz.cloud.tms.business.service.ITMetaModelUidService;
import com.mshz.cloud.tms.business.service.ITMetaSourceUidService;
import com.mshz.cloud.tms.business.util.ModelConvertUtil;
import cn.hutool.core.util.IdUtil;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.common.controller.BaseController;
import com.mshz.cloud.common.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 元模型唯一标识Controller
 *
 * @author zdw
 * @date 2023-03-02
 */
@RestController
@RequestMapping("/uid")
@Api(value = "uid", tags = {"uid"}, description = "元模型唯一标识")
public class MetaUidController extends BaseController {

    @Resource
    private ITMetaModelUidService tMetaModelUidService;

    @Resource
    private ITMetaSourceUidService tMetaSourceUidService;

    /**
     * 查询元模型唯一标识列表
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping("/metaUidList")
    public TableDataInfo list(TMetaModelUid tMetaModelUid) {
        startPage();
        List<TMetaModelUid> list = tMetaModelUidService.selectTMetaModelUidList(tMetaModelUid);
        return getDataTable(list);
    }

    /**
     * 查询元模型对应采集源数据唯一标识 列表
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping("/sourceUidList")
    public TableDataInfo list(TMetaSourceUid tMetaSourceUid) {
        startPage();
        List<TMetaSourceUid> list = tMetaSourceUidService.selectTMetaSourceUidList(tMetaSourceUid);
        return getDataTable(list);
    }

    /**
     * 获取元模型唯一标识详细信息
     */
   //@RequiresPermissions("metadata:uid:query")
    @GetMapping(value = "/metaUid/{id}")
    public AjaxResult getMetaUidInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tMetaModelUidService.selectTMetaModelUidById(id));
    }

    /**
     * 获取元模型对应采集源数据唯一标识 详细信息
     */
   //@RequiresPermissions("metadata:uid:query")
    @GetMapping(value = "/sourceUid/{id}")
    public AjaxResult getSourceUidInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tMetaSourceUidService.selectTMetaSourceUidById(id));
    }

    /**
     * 新增元模型唯一标识
     */
   //@RequiresPermissions("metadata:uid:add")
    //@Log(title = "元模型唯一标识", businessType = BusinessType.INSERT)
    @PostMapping("/metaUid")
    public AjaxResult addMetaUid(@RequestBody TMetaModelUid tMetaModelUid) {
        return toAjax(tMetaModelUidService.insertTMetaModelUid(tMetaModelUid));
    }

    /**
     * 新增元模型对应采集源数据唯一标识
     */
   //@RequiresPermissions("metadata:uid:add")
    //@Log(title = "元模型对应采集源数据唯一标识 ", businessType = BusinessType.INSERT)
    @PostMapping("/sourceUid")
    public AjaxResult addSourceUid(@RequestBody TMetaSourceUid tMetaSourceUid) {
        tMetaSourceUid.setId(IdUtil.getSnowflakeNextId());//
        return toAjax(tMetaSourceUidService.insertTMetaSourceUid(tMetaSourceUid));
    }

    /**
     * 修改元模型唯一标识
     */
   //@RequiresPermissions("metadata:uid:edit")
    //@Log(title = "元模型唯一标识", businessType = BusinessType.UPDATE)
    @PutMapping("/metaUid")
    public AjaxResult editMetaUid(@RequestBody TMetaModelUid tMetaModelUid) {
        return toAjax(tMetaModelUidService.updateTMetaModelUid(tMetaModelUid));
    }

    /**
     * 修改元模型对应采集源数据唯一标识
     */
   //@RequiresPermissions("metadata:uid:edit")
    //@Log(title = "元模型对应采集源数据唯一标识 ", businessType = BusinessType.UPDATE)
    @PutMapping("/sourceUid")
    public AjaxResult editSourceUid(@RequestBody TMetaSourceUid tMetaSourceUid) {
        return toAjax(tMetaSourceUidService.updateTMetaSourceUid(tMetaSourceUid));
    }

    /**
     * 删除元模型唯一标识
     */
   //@RequiresPermissions("metadata:uid:remove")
    //@Log(title = "元模型唯一标识", businessType = BusinessType.DELETE)
    @DeleteMapping("/metaUid/{ids}")
    public AjaxResult removeMetaUid(@PathVariable Long[] ids) {
        return toAjax(tMetaModelUidService.deleteTMetaModelUidByIds(ids));
    }

    /**
     * 删除元模型对应采集源数据唯一标识
     */
   //@RequiresPermissions("metadata:uid:remove")
    //@Log(title = "元模型对应采集源数据唯一标识 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/sourceUid/{ids}")
    public AjaxResult removeSourceUid(@PathVariable Long[] ids) {
        return toAjax(tMetaSourceUidService.deleteTMetaSourceUidByIds(ids));
    }

    /**
     * 查询元数据唯一标识配置信息
     *
     * @param modelId 元模型id
     * @return
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping("/disposeMetaDataUid")
    @ApiOperation(value = "根据元模型id查询元数据唯一标识配置组成信息")
    public AjaxResult disposeMetaDataUid(Long modelId) {
        MetaUidVo metaUidVo = new MetaUidVo();
        // 元模型及元模型映射源数据模型的组成唯一标识的表信息
        List<MetaUidTableVo> metaUidTableVoList = new ArrayList<>();

        MetaUidTableVo metaModelUidVo = new MetaUidTableVo();
        // 查询并组装元模型表信息
        Map<String, Object> modelInfo = tMetaModelUidService.selectModelInfoByModelId(modelId);
        if(Objects.nonNull(modelInfo)){
            metaModelUidVo.setDbId(Long.valueOf(String.valueOf(modelInfo.get("categoryId"))));
            metaModelUidVo.setDbName(String.valueOf(modelInfo.get("categoryName")));
            metaModelUidVo.setTableName(String.valueOf(modelInfo.get("tableName")));
            metaModelUidVo.setTableNameCn(String.valueOf(modelInfo.get("tableNameCn")));
        }
        metaModelUidVo.setModelId(modelId);
        metaModelUidVo.setModelType("meta");

        /*
         * 根据元模型id查询该元模型的组成唯一表示的所有字段
         */
        List<TMetaModelUid> metaModelUidList = tMetaModelUidService.selectTMetaModelUidListByMetaId(modelId);
        if (!CollectionUtils.isEmpty(metaModelUidList)) {
            // 组装元模型唯一标识字段信息
            List<MetaUidColumnVo> metaModelUidColumnList = new ArrayList<>();
            for (TMetaModelUid tMetaModelUid : metaModelUidList) {
                MetaUidColumnVo columnVo = new MetaUidColumnVo();
                columnVo = ModelConvertUtil.modelConvert(tMetaModelUid, MetaUidColumnVo.class);
                columnVo.setModelType("meta");
                metaModelUidColumnList.add(columnVo);
            }

            metaModelUidVo.setUidColumnList(metaModelUidColumnList);
        }
        metaUidTableVoList.add(metaModelUidVo);

        /*
         * 根据元模型id查询该元模型转换映射的所有采集源模型的 唯一表示字段
         */
        // 查询映射的源数据模型表信息
        List<Map<String, Object>> sourceTableInfoList = tMetaSourceUidService.selectSourceTableInfoByRelation(modelId);
        if (!CollectionUtils.isEmpty(sourceTableInfoList)) {
            for (Map<String, Object> sourceTableInfo : sourceTableInfoList) {
                MetaUidTableVo metaSourceUidVo = new MetaUidTableVo();
                metaSourceUidVo.setModelId(modelId);
                metaSourceUidVo.setModelType("source");
                metaSourceUidVo.setDbId(Long.valueOf(String.valueOf(sourceTableInfo.get("dbId"))));
                metaSourceUidVo.setDbName(String.valueOf(sourceTableInfo.get("dbName")));
                metaSourceUidVo.setTableId(Long.valueOf(String.valueOf(sourceTableInfo.get("tableId"))));
                metaSourceUidVo.setTableName(String.valueOf(sourceTableInfo.get("tableName")));
                metaSourceUidVo.setTableNameCn(String.valueOf(sourceTableInfo.get("tableNameCn")));
                metaUidTableVoList.add(metaSourceUidVo);
            }
        }
        // 查询当前已有的元模型映射源数据模型表唯一标识组成信息
        List<TMetaSourceUid> metaSourceUidList = tMetaSourceUidService.selectTMetaSourceUidListByMetaId(modelId);
        if (!CollectionUtils.isEmpty(metaSourceUidList)) {
            // 采集源数据的字段标识区分与元数据不同，数据转换时元模型数据可能来自多个不同采集源数据库的不同表，所以采集源数据唯一标识区分需要按 数据库id、表id 两级分类
            Map<Long, Map<Long, List<TMetaSourceUid>>> sourceUidGroupMap =
                metaSourceUidList.stream().collect(Collectors.groupingBy(TMetaSourceUid::getDbId, Collectors.groupingBy(TMetaSourceUid::getTableId)));
            // 组装源数据模型唯一标识字段信息
            sourceUidGroupMap.forEach((dbId, tableMap) -> {
                tableMap.forEach((tableId, columnList) -> {
                    List<MetaUidColumnVo> metaSourceUidColumnList = new ArrayList<>();

                    for (TMetaSourceUid tMetaSourceUid : columnList) {
                        MetaUidColumnVo columnVo = new MetaUidColumnVo();
                        columnVo = ModelConvertUtil.modelConvert(tMetaSourceUid, MetaUidColumnVo.class);
                        columnVo.setModelId(tMetaSourceUid.getMetaModelId());
                        columnVo.setModelType("source");
                        metaSourceUidColumnList.add(columnVo);
                    }

                    // 查找源模型表信息中，可以匹配到数据库id、表id的唯一标识组成信息，存放字段。
                    Optional<MetaUidTableVo> metaSourceUidVoInfo = metaUidTableVoList.stream()
                        .filter(uidTableVo -> "source".equals(uidTableVo.getModelType()) && uidTableVo.getDbId().equals(dbId) && uidTableVo.getTableId().equals(tableId))
                        .findFirst();
                    metaSourceUidVoInfo.ifPresent(metaSourceUidVo -> metaSourceUidVo.setUidColumnList(metaSourceUidColumnList));

                    // 根据源数据表id查询该源数据表的组成唯一表示的所有字段
                    // MetaUidTableVo metaSourceUidVo = new MetaUidTableVo();
                    // Map<String, String> sourceTableInfo = tMetaSourceUidService.selectSourceTableInfo(tableId);
                    // metaSourceUidVo.setModelId(modelId);
                    // metaSourceUidVo.setModelType("source");
                    // metaSourceUidVo.setDbId(dbId);
                    // metaSourceUidVo.setDbName(sourceTableInfo.get("dbNameCn"));
                    // metaSourceUidVo.setTableId(tableId);
                    // metaSourceUidVo.setTableName(sourceTableInfo.get("tableName"));
                    // metaSourceUidVo.setTableNameCn(sourceTableInfo.get("tableNameCn"));
                    // metaSourceUidVo.setUidColumnList(metaSourceUidColumnList);
                    // metaUidTableVoList.add(metaSourceUidVo);

                });
            });
        }
        metaUidVo.setMetaUidTableVoList(metaUidTableVoList);

        return AjaxResult.success(metaUidVo);
    }

    /**
     * 根据元模型id查询该元模型的唯一标识字段信息
     *
     * @param modelId 元模型id
     * @return
     */
    ////@RequiresPermissions("metadata:uid:list")
    @GetMapping("/getMetaDataUidByModelId")
    @ApiOperation(value = "根据元模型id查询该元模型的唯一标识字段信息")
    public AjaxResult getMetaDataUidByModelId(Long modelId) {
        return AjaxResult.success(tMetaModelUidService.selectMetaModelUidListByMetaId(modelId));
    }

    /**
     * 根据元模型id查询该元模型映射的采集元模型的唯一标识字段信息
     *
     * @param modelId 元模型id
     * @return
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping("/getMetaSourceUidByModelId")
    @ApiOperation(value = "根据元模型id查询该元模型映射的采集元模型的唯一标识字段信息")
    public AjaxResult getMetaSourceUidByModelId(Long modelId) {
        return AjaxResult.success(tMetaSourceUidService.selectMetaSourceUidListByMetaId(modelId));
    }

    /**
     * 根据元模型id获取元模型字段详细信息
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping(value = "/metaUid/column")
    @ApiOperation(value = "根据元模型id查询该元数据表字段信息")
    public AjaxResult getMetaModelColumnList(Long modelId) {
        return AjaxResult.success(tMetaModelUidService.selectModelColumnInfoByModelId(modelId));
    }

    /**
     * 根据表id获取元模型转换映射的源数据表字段详细信息
     */
   //@RequiresPermissions("metadata:uid:list")
    @GetMapping(value = "/sourceUid/column")
    @ApiOperation(value = "根据采集源模型id查询该采集源数据表字段信息")
    public AjaxResult getSourceModelColumnList(Long tableId) {
        return AjaxResult.success(tMetaSourceUidService.selectSourceColumnInfoByTableId(tableId));
    }

    /**
     * 配置元模型以及元模型映射源数据模型唯一标识信息
     */
   //@RequiresPermissions("metadata:uid:edit")
    //@Log(title = "配置元模型以及元模型映射源数据模型唯一标识信息 ", businessType = BusinessType.INSERT)
    @PostMapping("/modify")
    @ApiOperation(value = "配置保存元模型以及元模型映射源数据模型唯一标识信息")
    public AjaxResult modifyUidInfo(@RequestBody MetaUidVo metaUidVo) {
        int num = 0;
        String modelType = "";
        for (MetaUidTableVo metaUidTableVo : metaUidVo.getMetaUidTableVoList()) {
            if (metaUidVo.getMetaUidTableVoList().indexOf(metaUidTableVo) == 0) {
                // 唯一标识vo中，元模型唯一标识信息集合里第一个元素固定为元模型的表及字段信息
                List<MetaUidColumnVo> columnVoList = metaUidTableVo.getUidColumnList();
                modelType = "meta";
                if (!CollectionUtils.isEmpty(columnVoList)) {
                    num += metaUidDataWarehousing(columnVoList, modelType, metaUidVo.getModelId(), null);
                } else {
                    // 未传入字段信息
                    num += 1;
                }
            } else {
                modelType = "source";
                List<MetaUidColumnVo> columnVoList = metaUidTableVo.getUidColumnList();
                if (!CollectionUtils.isEmpty(columnVoList)) {
                    num += metaUidDataWarehousing(columnVoList, modelType, metaUidVo.getModelId(), metaUidTableVo.getTableId());
                } else {
                    // 未传入字段信息
                    num += 1;
                }
            }

        }

        return toAjax(num);
    }

    /**
     * 元模型、源数据模型表配置唯一标识字段方法 编辑元模型唯一标识字段信息 或 编辑元模型映射源数据表唯一标识字段信息
     *
     * @param metaUidTableVo 组成唯一标识表vo
     * @return 入库数据数量
     */
   //@RequiresPermissions("metadata:uid:edit")
    //@Log(title = "元模型、源数据模型表配置唯一标识字段方法", businessType = BusinessType.INSERT)
    @PostMapping("/modifyMetaUid")
    @ApiOperation(value = "配置保存元模型 或 源数据模型的唯一标识信息")
    public AjaxResult modifyMetaUid(@RequestBody MetaUidTableVo metaUidTableVo) {
        return toAjax(metaUidDataWarehousing(metaUidTableVo.getUidColumnList(), metaUidTableVo.getModelType(), metaUidTableVo.getModelId(), metaUidTableVo.getTableId()));
    }

    /**
     * 元模型、源数据模型表配置唯一标识字段方法 编辑元模型唯一标识字段信息 或 编辑元模型映射源数据表唯一标识字段信息
     *
     * @param columnVoList 组成唯一标识的字段集合
     * @param modelType 模型类型，元模型（meta）或 源数据模型（source）
     * @param modelId 元模型id
     * @param tableId 源数据模型id（当传入数据为源数据模型时）
     * @return 入库数据数量
     */
    private int metaUidDataWarehousing(List<MetaUidColumnVo> columnVoList, String modelType, Long modelId, Long tableId) {
        int num = 0;
        if (!CollectionUtils.isEmpty(columnVoList)) {
            if ("meta".equals(modelType)) {
                List<TMetaModelUid> metaModelUidList = new ArrayList<>();
                for (int i = columnVoList.size() - 1; i >= 0; i--) {
                    MetaUidColumnVo columnVo = columnVoList.get(i);
                    TMetaModelUid metaModelUid = new TMetaModelUid();
                    metaModelUid = ModelConvertUtil.modelConvert(columnVo, TMetaModelUid.class);
                    metaModelUid.setColumnOrder(i + 1 + "");
                    metaModelUid.setId(IdUtil.getSnowflakeNextId());//
                    metaModelUidList.add(metaModelUid);
                }
                num = tMetaModelUidService.saveOrUpdateDataBatch(modelId, metaModelUidList);
            } else {
                List<TMetaSourceUid> metaSourceUidList = new ArrayList<>();
                for (int i = columnVoList.size() - 1; i >= 0; i--) {
                    MetaUidColumnVo columnVo = columnVoList.get(i);
                    TMetaSourceUid metaSourceUid = new TMetaSourceUid();
                    metaSourceUid = ModelConvertUtil.modelConvert(columnVo, TMetaSourceUid.class);
                    metaSourceUid.setMetaModelId(modelId);
                    metaSourceUid.setColumnOrder(i + 1 + "");
                    metaSourceUid.setId(IdUtil.getSnowflakeNextId());//
                    metaSourceUidList.add(metaSourceUid);
                }
                num = tMetaSourceUidService.saveOrUpdateDataBatch(modelId, tableId, metaSourceUidList);
            }
        } else {
            // 没有传入字段，意味着当前没有选择字段，或字段全部取消勾选
            num = "meta".equals(modelType) ? tMetaModelUidService.saveOrUpdateDataBatch(modelId, new ArrayList<>())
                : tMetaSourceUidService.saveOrUpdateDataBatch(modelId, tableId, new ArrayList<>());
        }
        return num;
    }



    /**
     * @author: David
     * @description 返回元模型ID下一个序列号
     * @date: 2023/8/10 9:32
     */
    @GetMapping(value = "/getMaxIdSerial")
    public String getMaxIdSerial(String tableName) {

        return tMetaModelUidService.getMaxIdSerial(tableName);
    }

}
