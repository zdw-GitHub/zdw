package com.mshz.cloud.tms.business.service;
import com.mshz.cloud.tms.business.vo.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 通信站基本信息Service接口
 *
 * @author zdw
 * @date 2022-11-02
 */
public interface ResourceManagementService {
    List<GuideVo> selectGuideVoListByModel(String modelId, String modelVersion);

    public List<MetaModelShowVo> selectMetaModelShowVoList(MetaModelShowVo metaModelShowVo);

    GridInitDataVo initGridData(MetaModelShowVo metadataShowVo);

    List<Map> selectData(DataMaintainParaVo para);

    List<Map> QueryData(DataMaintainParaVo para);

    Map<String, Object> packageOne(DataMaintainParaVo para);

    Map<String, Object> packageOneByType(DataMaintainParaVo para);

    public String exportExcel(DataMaintainParaVo para) throws IOException;

    public String QueryexportExcel(DataMaintainParaVo para) throws IOException;

    public void importExcel(DataMaintainParaVo vo);

    public long queryCount(DataMaintainParaVo para);

    public long advancedQueryCount(DataMaintainParaVo para);

    public void delete(DataMaintainParaVo para);



    public void update(DataMaintainParaVo para);


    public String create(DataMaintainParaVo para);

    Map getNewTabInfo(String id, String tableName, String attribute);

    Map<String, String> selectTableMessage(String modeId);

    /**
     * 下载导入模板（复用导出的列配置，无数据仅返回表头）
     * @param para 模板下载参数（modelId）
     * @return 模板文件存储路径
     * @throws IOException
     */
    String downloadImportTemplate(ImportTemplateParaVo para) throws IOException;

    /**
     * 解析Excel并批量导入数据
     * @param file Excel文件
     * @param modelId 模型ID
     * @return 导入结果
     * @throws Exception
     */
    ImportResultVo importDataFromExcel(MultipartFile file, String modelId) throws Exception;
}
