package com.mshz.cloud.tms.business.service;

import com.mshz.cloud.tms.business.vo.DataMapParaVo;
import com.mshz.cloud.tms.business.vo.DataMapReturnVo;

public interface IDataMapService {

    public DataMapReturnVo getDataMap(DataMapParaVo para);
}
