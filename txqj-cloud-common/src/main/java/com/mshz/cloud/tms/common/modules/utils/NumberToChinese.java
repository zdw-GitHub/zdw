package com.mshz.cloud.tms.common.modules.utils;

/**
 * @msg 阿拉伯数字转中文数字
 */
public class NumberToChinese {
    private static final String[] CHINESE_NUMBERS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] CHINESE_UNITS = {"", "十", "百", "千", "万", "十", "百", "千", "亿"};
    public static String convertToChinese(int number) {
        if (number == 0) return CHINESE_NUMBERS[0];
        StringBuilder result = new StringBuilder();
        String numStr = String.valueOf(number);
        for (int i = 0; i < numStr.length(); i++) {
            int digit = numStr.charAt(i) - '0';
            if (digit != 0) {
                result.append(CHINESE_NUMBERS[digit])
                        .append(CHINESE_UNITS[numStr.length() - 1 - i]);
            } else {
                // 处理零的情况
                if (i < numStr.length() - 1 && numStr.charAt(i + 1) != '0') {
                    result.append(CHINESE_NUMBERS[0]);
                }
            }
        }
        return result.toString();
    }
//    public static void main(String[] args) {
//        System.out.println(convertToChinese(1)); // 输出: 一
//        System.out.println(convertToChinese(10)); // 输出: 一十
//        System.out.println(convertToChinese(100)); // 输出: 一百
//    }
}
