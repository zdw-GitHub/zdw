package com.mshz.cloud.tms.business.service.impl;
import com.mshz.cloud.tms.business.config.MetadataDBConfig;
import com.mshz.cloud.tms.business.service.OptionsService;
import com.mshz.cloud.tms.business.util.ChineseCharacterUtil;
import com.mshz.cloud.tms.business.vo.DictionariesVo;
import com.mshz.cloud.tms.business.vo.DictionaryInfoVo;
import com.mshz.cloud.tms.business.vo.MapMessage;
import com.mshz.cloud.tms.business.mapper.OptionsMapper;
import com.mshz.cloud.tms.business.vo.ModelVo;
import com.mshz.cloud.tms.business.vo.OptionsVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Creat By  cuiHy on 2022/11/3.
 */

@Service
public class OptionsServiceImpl implements OptionsService {

    @Resource
    private OptionsMapper mapper;


    @Resource
    private MetadataDBConfig metadataDBConfig;


    @Override
    public List<OptionsVo> getGridOptions(String name) {
        return mapper.getGridOptions(name);
    }

    @Override
    public List<OptionsVo> devTypeOptions(String name) {
        return mapper.devTypeOptions(name);
    }

    @Override
    public List<OptionsVo> companyOptions(String name) {
        return mapper.companyOptions(name);
    }

    @Override
    public ModelVo getModelId(String tableName) {
        return mapper.getModelId(tableName);
    }

    @Override
    public List<OptionsVo> getVoltageOptions(String name) {
        return mapper.getVoltageOptions(name);
    }

    @Override
    public BigDecimal getColunmId(BigDecimal modelId, String columnName) {
        return mapper.getColunmId(modelId,columnName);
    }

    @Override
    public List<OptionsVo> getSiteStatusOptions(String name) {
        return mapper.getSiteStatusOptions(name);
    }

    @Override
    public List<OptionsVo> getSiteLevelOptions(String name) {
        return mapper.getSiteLevelOptions(name);
    }

    @Override
    public List<OptionsVo> getSiteTypeOptions(String name) {
        return mapper.getSiteTypeOptions(name);
    }

    @Override
    public List<MapMessage> getModelConditionDisplay(String modelId, String modelVersion) {
        return mapper.getModelConditionDisplay(modelId, modelVersion);
    }

    @Override
    public List<Map<String, String>> selectDictionaries(String table, String id, String name) {
        return mapper.selectDictionaries(table, id, name);
    }

    @Override
    public List<Map<String, Object>> useDefinedVerSql(String sql) {
        return mapper.useDefinedVerSql(sql);
    }

    @Override
    public DictionaryInfoVo getDictionaryInformation(String modelId, String modelVersion, String columnId) {
        return mapper.getDictionaryInformation(modelId, modelVersion, columnId);
    }

    @Override
    public List<DictionariesVo> getMapData(DictionaryInfoVo dictionaryInfoVo) {
        List<DictionariesVo> dictionariesVos = new ArrayList<>();
        List<DictionariesVo> resultList = new ArrayList<>();
        if(dictionaryInfoVo.getType()!=null&&dictionaryInfoVo.getType().equals("1")||dictionaryInfoVo.getType().equals("0")){
            if(dictionaryInfoVo.getCuzSql()==null&&!dictionaryInfoVo.getCuzSql().equals("")){
                return dictionariesVos;
            }
            List<Map<String,Object>> mapList  =   mapper.useDefinedVerSql(dictionaryInfoVo.getCuzSql());
            for (Map<String,Object> map:mapList) {
                DictionariesVo dictionariesVo = new DictionariesVo();
                dictionariesVo.setCode(String.valueOf(map.get("code")));
                dictionariesVo.setName((String) map.get("name"));
                if((String) map.get("name")!=null){
                    dictionariesVos.add(dictionariesVo);
                }
            }
        }else if(dictionaryInfoVo.getType()!=null&&dictionaryInfoVo.getType().equals("2")){
            if(dictionaryInfoVo.getTableName()==null&&!dictionaryInfoVo.getTableName().equals("")){
                return dictionariesVos;
            }
            List<Map<String,String>> mapList =   mapper.selectDictionaries(metadataDBConfig.getUserName().concat(".").concat(dictionaryInfoVo.getTableName().toLowerCase()),dictionaryInfoVo.getId(),dictionaryInfoVo.getName());
            for (Map<String,String> map:mapList) {
                DictionariesVo dictionariesVo = new DictionariesVo();
                dictionariesVo.setCode(String.valueOf(map.get("code")));
                dictionariesVo.setName(map.get("name"));
                if((String) map.get("name")!=null){
                    dictionariesVos.add(dictionariesVo);
                }
            }
        }
        if(dictionaryInfoVo.getInitial()!=null&&!dictionaryInfoVo.getInitial().equals("")){
            //resultList = dictionariesVos.stream().filter(dic -> ChineseCharacterUtil.getSpells(dic.getName()).substring(0,1).equals(dictionaryInfoVo.getInitial())).collect(Collectors.toList());
            for (DictionariesVo dic : dictionariesVos) {
                if (ChineseCharacterUtil.getSpells(dic.getName()).substring(0, 1).equals(dictionaryInfoVo.getInitial())) {
                    resultList.add(dic);
                }
            }
            return resultList;
        }
        return dictionariesVos;
    }
}
