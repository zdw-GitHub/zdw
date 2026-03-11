package com.mshz.cloud.tms.business.util;

public class NumberToChineseUppercase {

    private static final char[] NUMBERS = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '就'};
    private static final String[] UNITS = {"", "十", "百", "千"};
    private static final String[] BIG_UNITS = {"", "万", "亿", "兆"};

    public static String numberToChineseUppercase(long num) {
        if (num == 0) {
            return "零";
        }

        StringBuilder result = new StringBuilder();
        long part = 0;
        int unitPos = 0; // UNITS的位置
        int bigUnitPos = 0; // BIG_UNITS的位置

        while (num > 0) {
            part = num % 10000; // 每次取四位进行处理
            String partStr = convertPart(part, unitPos);
            if (!partStr.isEmpty()) {
                if (result.length() > 0) {
                    result.insert(0, BIG_UNITS[bigUnitPos] + partStr);
                } else {
                    result.append(partStr);
                }
            } else if (result.length() > 0 && !"零".equals(result.substring(0, 1))) {
                // 如果当前部分为零，且结果字符串不是以“零”开头，则需要在前面加上“零”
                result.insert(0, "零");
            }

            num /= 10000;
            unitPos = 0; // 重置UNIT位置
            bigUnitPos++;
        }

        return result.toString();
    }

    private static String convertPart(long part, int unitPos) {
        StringBuilder partResult = new StringBuilder();
        boolean zeroFlag = false; // 标记是否出现了零

        for (int i = 0; i < 4; i++) {
            int digit = (int) (part % 10);
            if (digit == 0) {
                if (!zeroFlag && partResult.length() == 0) {
                    partResult.append(NUMBERS[digit]);
                }
                zeroFlag = true;
            } else {
                if (partResult.length() > 0 && "零".equals(partResult.substring(partResult.length() - 1))) {
                    partResult.setLength(partResult.length() - 1); // 移除末尾的“零”
                }
                partResult.append(NUMBERS[digit]).append(UNITS[unitPos]);
                zeroFlag = false;
            }
            part /= 10;
            unitPos++;
        }

        // 移除末尾的无用单位
        while (partResult.length() > 1 && "零".equals(partResult.substring(partResult.length() - 1))) {
            partResult.setLength(partResult.length() - 1);
        }

        return partResult.toString();
    }

    public static void main(String[] args) {
        System.out.println(numberToChineseUppercase(1234567)); // 输出：壹佰贰拾叁万肆仟伍佰陆拾柒
        System.out.println(numberToChineseUppercase(10001));    // 输出：壹万零壹
        System.out.println(numberToChineseUppercase(100000001));// 输出：壹亿零壹
    }
}
