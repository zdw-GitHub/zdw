package com.mshz.cloud.tms.business.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Create by hzy 2022/12/6
 **/
public class ChineseCharacterUtil {


    /**
     * 提取汉字字符串的首字母
     * @param str 汉字字符串
     * @return
     */
    public static String getSpells(String str) {
        if (isNull(str)) {
            return "";
        }
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            }
            else {
                convert += word;
            }
        }

        convert = string2AllTrim(convert);
        return convert.toUpperCase();
    }

    /**
     * 去掉字符串包含的所有空格
     *
     * @param value
     * @return
     */
    public static String string2AllTrim(String value) {
        if (isNull(value)) {
            return "";
        }
        return value.trim().replace(" ", "");
    }

    /*
     * 判断字符串是否为空
     */

    public static boolean isNull(Object strData) {
        if (strData == null || String.valueOf(strData).trim().equals("")) {
            return true;
        }
        return false;
    }

//    public static void main(String[] args) {
//
//        dictionariesVos1 = dictionariesVos.stream().filter(s->getSpells(s.getName()).substring(0,1).equals("G")).collect(Collectors.toList());
//
//    }

}
