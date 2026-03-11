package com.mshz.cloud.tms.business.controller;
import com.mshz.cloud.tms.business.domain.AjaxResult;
import com.mshz.cloud.tms.business.service.OptionsService;
import com.mshz.cloud.tms.business.vo.DictionariesVo;
import com.mshz.cloud.tms.business.vo.DictionaryInfoVo;
import com.mshz.cloud.tms.business.vo.MapMessage;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 业务通用接口Controller
 * 包含下载、字典查询、首字母检索等通用业务接口
 *
 * @author zdw
 * @date 2022-11-02
 */
@RestController
@RequestMapping("/business/common")  // 加业务前缀，避免和其他项目冲突
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private OptionsService optionsService;
    /**
     * 下载
     * @return
     */
    @ApiOperation(value = "下载", notes = "下载")
    @GetMapping("/download")
    @ResponseBody
    public AjaxResult download(HttpServletResponse response, String filename) throws UnsupportedEncodingException
    {
//        LocalDate end = LocalDate.now();
//        LocalDate start = end.minusDays(14);
//        String filename = "稿源抓取周报-" + end.format(DateTimeFormatter.ISO_DATE) + ".xlsx";
        //String filename = fileVo.getPath();
        String filepath = filename;
        // 如果文件名不为空，则进行下载
        if (filename != null) {
            File file = new File(filepath);
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/json;charset=utf-8");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    logger.info("下载成功!");
                    return AjaxResult.success();

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("下载失败!");
                    return AjaxResult.error("服务器内部错误");

                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return AjaxResult.error("请求参数有误");
    }

   //@RequiresPermissions({"common:pub:list"})
    @GetMapping("/MapMessage")
    @ApiOperation("高级查询数据")
    public AjaxResult MapMessage(String modelId,String modelVersion){
        //获取表 需要高级查询的 字段信息
        List<MapMessage> modelConditionDisplayS =  optionsService.getModelConditionDisplay(modelId, modelVersion);
        for (MapMessage mapMessage:modelConditionDisplayS) {
            //字段关联字典信息
            DictionaryInfoVo dictionaryInfoVo = optionsService.getDictionaryInformation(mapMessage.getModelId(),mapMessage.getModelVersion(),mapMessage.getId());
            //字典数据
            if(dictionaryInfoVo!=null){
                mapMessage.setList(optionsService.getMapData(dictionaryInfoVo));
            }
        }
        return  AjaxResult.success(modelConditionDisplayS);
    }

   //@RequiresPermissions({"common:pub:list"})
    @PostMapping({"/conditionalData"})
    @ApiOperation("首字母检索")
    public AjaxResult conditionalData(@RequestBody MapMessage mapMessage) {
        //字段关联字典信息+检索首字母
        DictionaryInfoVo dictionaryInfoVo = optionsService.getDictionaryInformation(mapMessage.getModelId(),mapMessage.getModelVersion(),mapMessage.getId());
        if(dictionaryInfoVo==null){
            List<DictionariesVo> list = new ArrayList<>();
            return AjaxResult.success(list);
        }
        dictionaryInfoVo.setInitial(mapMessage.getInitial()!=null?mapMessage.getInitial():null);
        //字典数据
        return AjaxResult.success(optionsService.getMapData(dictionaryInfoVo));
    }




}
