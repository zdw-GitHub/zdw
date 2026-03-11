package com.mshz.cloud.tms.business.util;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * bean对象转换
 * @author Liuyue
 */
public class ModelConvertUtil {

    public static <T> T modelConvert(Object vo, Class<T> type) {
        T poData = null;
        try {
            Map<String, String> voParam = new HashMap<>();

//            dateStrTypeConverter();
            voParam = BeanUtils.describe(vo);
            voParam.remove("params");
            voParam.remove("createTime");
            voParam.remove("updateTime");
            poData = type.newInstance();
            dateTypeConverter();
            BeanUtils.populate(poData, voParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poData;

    }

    public static <T> T mapConvert(Map<String, String> voParam, Class<T> type) {
        T poData = null;
        try {
//            dateStrTypeConverter();
            voParam.remove("params");
            voParam.remove("createTime");
            voParam.remove("updateTime");
            poData = type.newInstance();
            dateTypeConverter();
            BeanUtils.populate(poData, voParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poData;

    }

    /**
     * 注册类型转换器，时间类型转字符串
     */
    public static void dateStrTypeConverter() {
        ConvertUtilsBean convertUtils = BeanUtilsBean.getInstance().getConvertUtils();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern(DateUtils.YYYY_MM_DD_HH_MM_SS);
        convertUtils.register(dateConverter, String.class);
    }

    /**
     *  自定义beanUtil date类型注册类型转换器
     */
    public static void dateTypeConverter() {
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class clazz, Object value) {
                if(value instanceof Date) {
                    return value;
                }
                if (value == null || "".equals(value.toString().trim()) || clazz != Date.class) {
                    return null;
                }
                Date time = DateUtils.parseDate(value.toString());
                return time != null ? time : DateUtils.getNowDate();
            }
        }, Date.class);

    }


}
