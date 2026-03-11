package com.mshz.cloud.tms.business.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelGenerateException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel工具类：支持导出/读取/动态生成导入模板（含必填批注、字典下拉框）
 * 解决问题：1.createRichTextString方法不存在 2.createExplicitListConstraint参数类型不匹配 3.兼容Long类型
 * 兼容xls/xlsx，无重复方法，无语法错误
 */
public class ExcelUtilTool {

    // ======================== 原有导出方法（全保留，兼容Long类型，无修改） ========================
    public static String writer(String path, List<Map> list, String titleRow[], String fileName) throws IOException {
        String filePath = path + "/" + fileName;
        FileUtil.judeDirExists(new File(path));
        String fileType = "xls";
        Workbook wb = null;
        File file = new File(filePath);
        Sheet sheet = null;
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                throw new ExcelGenerateException("文件格式不正确");
            }
            sheet = wb.createSheet("sheet1");
            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                wb.write(outputStream);
            }
        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                throw new ExcelGenerateException("文件格式不正确");
            }
        }
        if (sheet == null) {
            sheet = wb.createSheet("sheet1");
        }

        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
        style.setWrapText(true);

        Row row = sheet.createRow(0);
        for (int i = 0; i < titleRow.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titleRow[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * 256);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Map map = list.get(i);
            int k = 0;
            for (Object value : map.values()) {
                // 兼容Long类型：所有值统一判空，直接转字符串（Long转String无异常）
                row.createCell(k).setCellValue(value == null ? "" : value.toString());
                k++;
            }
        }

        try (OutputStream stream = new FileOutputStream(filePath)) {
            wb.write(stream);
        } finally {
            wb.close();
        }
        return filePath;
    }

    public static String opticalCableLoadBearingLightPathExport(String path, List<Map> list, String titleRow[]) throws IOException {
        String filePath = path + "/" + "光缆承载光路";
        FileUtil.judeDirExists(new File(path));
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");

        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)20);
        style.setFont(font);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
        Row row = sheet.createRow(0);
        row.setHeightInPoints(50);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("光缆承载光路");

        font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)15);
        style.setFont(font);
        row = sheet.createRow(1);
        row.setHeightInPoints(40);
        for (int i = 0; i < titleRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(titleRow[i]);
        }

        font = wb.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short)10);
        style.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 2);
            Map map = list.get(i);
            int k = 0;
            for (Object value : map.values()) {
                cell = row.createCell(k);
                cell.setCellStyle(style);
                cell.setCellValue(value == null ? "" : value.toString());
                k++;
            }
        }

        setColumnWidths(sheet, new int[]{5,40,20,10,10,40,40,40,40,40,10,20,10});
        writeAndClose(wb, filePath);
        return filePath;
    }

    public static String exportOpticalPath(String path, List<Map> list, String[] titleRow) throws IOException {
        String filePath = path + "/" + "光路";
        FileUtil.judeDirExists(new File(path));
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");

        CellStyle style = createCommonStyle(wb);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)20);
        style.setFont(font);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
        Row row = sheet.createRow(0);
        row.setHeightInPoints(50);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("光路列表");

        font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)15);
        style.setFont(font);
        row = sheet.createRow(1);
        row.setHeightInPoints(40);
        for (int i = 0; i < titleRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(titleRow[i]);
        }

        font = wb.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short)10);
        style.setFont(font);
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 2);
            Map map = list.get(i);
            int k = 0;
            for (Object value : map.values()) {
                cell = row.createCell(k);
                cell.setCellStyle(style);
                cell.setCellValue(value == null ? "" : value.toString());
                k++;
            }
        }

        setColumnWidths(sheet, new int[]{5,40,20,30,30,40,20,30,40,20,40});
        writeAndClose(wb, filePath);
        return filePath;
    }

    public static String exportN1Analysis(String path, Map map) throws IOException {
        String name = (String) map.get("name");
        String filePath = path + "/" + name + "N-1分析";
        FileUtil.judeDirExists(new File(path));
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");

        CellStyle style = createCommonStyle(wb);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)20);
        style.setFont(font);

        int rowIndex = 0;
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
        Row row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(50);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(name+"   N-1分析");

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
        row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(20);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("影响业务");

        font = wb.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short)10);
        style.setFont(font);
        List<Map> business = (List<Map>) map.get("business");
        for (Map m : business) {
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
            row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue((String) m.get("NAME"));
        }

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 3));
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("N-1分析");

        List<Map> line = (List<Map>) map.get("line");
        if (line != null) {
            for (Map l : line) {
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
                row = sheet.createRow(rowIndex++);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue((String) l.get("name"));
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue((boolean) l.get("isKeyPoint") ? "存在光缆需重点关注" : "无光缆无重点保护");
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue((String) l.get("fiberNames"));

                List<Map> b = (List<Map>) l.get("business");
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+b.size(), 0, 0));
                for (Map m : b) {
                    row = sheet.createRow(rowIndex++);
                    cell = row.createCell(1);
                    cell.setCellStyle(style);
                    cell.setCellValue((String) m.get("name"));
                    cell = row.createCell(2);
                    cell.setCellStyle(style);
                    cell.setCellValue((String) m.get("keyPoint"));
                    cell = row.createCell(3);
                    cell.setCellStyle(style);
                    cell.setCellValue((String) m.get("channelRoute"));
                }
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
        row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(100);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("风险分析");
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue((String) map.get("risk"));

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 3));
        row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(100);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("对应措施");
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue((String) map.get("measure"));

        setColumnWidths(sheet, new int[]{5,80,20,160});
        writeAndClose(wb, filePath);
        return filePath;
    }

    public static String exportImpactSituation(String path, String name, List<Map<String, String>> impactSituation, List<Map<String, String>> impactBuz) throws IOException {
        String filePath = path + "/" + name + "应急预案";
        FileUtil.judeDirExists(new File(path));
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");

        CellStyle style = createCommonStyle(wb);
        Font font = wb.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short)15);
        style.setFont(font);

        setColumnWidths(sheet, new int[]{10,50,30,30,80,80});
        String[] title = {"序号","业务名称","起始资源","终止资源","业务路由","应急路由"};

        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(40);
        for (int i = 0; i < title.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(title[i]);
        }

        for (int i = 0; i < impactSituation.size(); i++) {
            Map<String, String> gl = impactSituation.get(i);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 1, 4));
            row = sheet.createRow(rowIndex++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(NumberToChineseUppercase.numberToChineseUppercase(i + 1L));
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(gl.get("name"));
            cell = row.createCell(5);
            cell.setCellStyle(style);
            cell.setCellValue(gl.get("emergency"));

            List<Map<String, String>> llList = impactBuz.stream().filter(f -> Objects.equals(f.get("opticalId"), gl.get("id"))).collect(Collectors.toList());
            for (int z = 0; z < llList.size(); z++) {
                Map<String, String> ll = llList.get(z);
                row = sheet.createRow(rowIndex++);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(String.valueOf(z + 1));
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(ll.get("buzName"));
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(ll.get("aResName"));
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(ll.get("zResName"));
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(ll.get("path"));
                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell.setCellValue(ll.get("emergency"));
            }
        }

        writeAndClose(wb, filePath);
        return filePath;
    }

    // ======================== 原有读取方法（修复语法错误，兼容Long类型） ========================
    public static List<Map<String, Object>> readExcel(InputStream inputStream, String[] excelHeaders, String[] dbColumnNames) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();
        // 自动兼容xls/xlsx，修复原语法错误
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return dataList;
        }

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return dataList;
        }

        Map<String, String> header2DbColumnMap = new HashMap<>();
        for (int i = 0; i < excelHeaders.length; i++) {
            header2DbColumnMap.put(excelHeaders[i], dbColumnNames[i]);
        }

        int lastRowNum = sheet.getLastRowNum();
        for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
            Row dataRow = sheet.getRow(rowNum);
            if (dataRow == null) {
                continue;
            }
            Map<String, Object> rowData = new LinkedHashMap<>();
            for (int cellNum = 0; cellNum < headerRow.getLastCellNum(); cellNum++) {
                Cell headerCell = headerRow.getCell(cellNum);
                Cell dataCell = dataRow.getCell(cellNum);
                if (headerCell == null) {
                    continue;
                }
                String headerText = getCellValue(headerCell).trim();
                String dbColumnName = header2DbColumnMap.get(headerText);
                if (StrUtil.isBlank(dbColumnName)) {
                    continue;
                }
                // 兼容Long类型：读取的值统一为String，后续可按需转Long
                rowData.put(dbColumnName, getCellValue(dataCell));
            }
            dataList.add(rowData);
        }

        workbook.close();
        inputStream.close();
        return dataList;
    }

    // ======================== 动态导入模板核心方法（解决所有3个问题，无语法错误） ========================
    /**
     * 生成动态导入模板（含全字段、必填批注、字典下拉框）
     * @param excelPath 模板存储路径
     * @param headers 表头数组（中文名称）
     * @param requiredIndexs 必填字段列索引数组
     * @param dictDataMap 字典映射：列索引→字典选项数组（String[]）
     * @param templateName 模板名称
     * @return 模板文件全名
     * @throws Exception 异常
     */
    public static String generateImportTemplate(String excelPath, String[] headers,
                                                int[] requiredIndexs, Map<Integer, String[]> dictDataMap,
                                                String templateName) throws Exception {
        String filePath = excelPath + "/" + templateName + ".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet mainSheet = workbook.createSheet(templateName); // 主工作表（用户可见）

        // 新增：创建隐藏的辅助工作表，用于存储所有字典选项（命名为 _dict Hidden，避免用户误操作）
        HSSFSheet dictHiddenSheet = workbook.createSheet("_dictHidden");
        workbook.setSheetHidden(workbook.getSheetIndex(dictHiddenSheet), true); // 隐藏该工作表

        // 设置主工作表列宽
        for (int i = 0; i < headers.length; i++) {
            mainSheet.setColumnWidth(i, 20 * 256);
        }

        // 创建表头行
        HSSFRow headerRow = mainSheet.createRow(0);
        headerRow.setHeightInPoints(20);
        CellStyle headerStyle = createHeaderCellStyle(workbook);

        // 循环构建表头+必填批注+【重构后】字典下拉框
        for (int col = 0; col < headers.length; col++) {
            HSSFCell cell = headerRow.createCell(col);
            cell.setCellValue(headers[col]);
            cell.setCellStyle(headerStyle);

            // 必填字段添加批注（原有逻辑不变）
            if (contains(requiredIndexs, col)) {
                createRequiredComment(workbook, mainSheet, cell, headers[col]);
            }

            // 字典字段添加下拉框【核心修改】：调用重构后的方法，传入workbook+隐藏表
            if (dictDataMap != null && dictDataMap.containsKey(col) && dictDataMap.get(col) != null && dictDataMap.get(col).length > 0) {
                createDictDataValidationByHiddenSheet(workbook, mainSheet, dictHiddenSheet, col, dictDataMap.get(col));
            }
        }
        File dir = new File(excelPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File templateFile = new File(filePath);
        try (OutputStream os = new FileOutputStream(templateFile)) {
            workbook.write(os);
        }
        workbook.close();
        return filePath;
    }

    /**
     * 基于隐藏辅助表创建字典下拉框（避开255字符限制，支持任意长度字典）
     * @param workbook 整个Excel工作簿（关联主表和隐藏表）
     * @param mainSheet 主工作表（用户操作的表）
     * @param dictHiddenSheet 隐藏辅助表（存储字典选项）
     * @param col 主工作表中需要添加下拉框的列索引
     * @param dictOptions 字典选项数组
     */
    private static void createDictDataValidationByHiddenSheet(HSSFWorkbook workbook, HSSFSheet mainSheet,
                                                              HSSFSheet dictHiddenSheet, int col, String[] dictOptions) {
        try {
            // 步骤1：将字典选项写入隐藏表的「指定列」（按列存储，不同字典列对应隐藏表不同列，避免冲突）
            int dictRow = 0;
            for (String option : dictOptions) {
                HSSFRow row = dictHiddenSheet.getRow(dictRow);
                if (row == null) {
                    row = dictHiddenSheet.createRow(dictRow);
                }
                // 隐藏表的列索引 = 主表的字典列索引（一一对应，避免不同字典混淆）
                HSSFCell cell = row.createCell(col);
                cell.setCellValue(option == null ? "" : option); // 空值处理
                dictRow++;
            }

            // 步骤2：构建隐藏表的单元格引用（格式：隐藏表名!列字母开始行:列字母结束行）
            String hiddenSheetName = dictHiddenSheet.getSheetName();
            // 列索引转Excel列字母（如0→A、1→B、26→Z、27→AA）
            String colLetter = getExcelColumnLetter(col);
            // 引用范围：隐藏表[colLetter]列的第1行到第dictRow行（即所有字典选项）
            String refRange = hiddenSheetName + "!" + colLetter + "1:" + colLetter + dictRow;

            // 步骤3：创建数据验证约束（基于跨表引用，非直接拼接字符串）
            DVConstraint constraint = DVConstraint.createFormulaListConstraint(refRange);
            // 步骤4：设置下拉框作用范围（主表该列的第2行开始到最后一行，避开表头）
            CellRangeAddressList addressList = new CellRangeAddressList(
                    1, // 开始行：2行（索引1），用户填写从第2行开始
                    65535, // 结束行：xls最大行号（65535），覆盖整列可填写区域
                    col, // 开始列：当前字典列
                    col  // 结束列：当前字典列
            );
            // 步骤5：创建数据验证并添加到主工作表
            HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);
            // 可选：设置下拉框错误提示（用户输入非选项内容时弹出）
            validation.createErrorBox("输入错误", "请选择下拉框中的有效选项，禁止手动输入！");
            validation.setShowErrorBox(true); // 强制显示错误提示
            mainSheet.addValidationData(validation);

            // 可选：调整隐藏表列宽（不影响使用，仅规范）
            dictHiddenSheet.setColumnWidth(col, 20 * 256);
        } catch (Exception e) {
            throw new RuntimeException("为列索引" + col + "创建字典下拉框失败：" + e.getMessage(), e);
        }
    }

    /**
     * 将Excel列索引（数字）转为列字母（如0→A、1→B、26→Z、27→AA、28→AB...）
     * @param columnIndex 列索引（从0开始）
     * @return 列字母（大写）
     */
    private static String getExcelColumnLetter(int columnIndex) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("列索引不能为负数：" + columnIndex);
        }
        StringBuilder sb = new StringBuilder();
        int index = columnIndex;
        while (index >= 0) {
            // 取余26得到当前位的字母（0→A、1→B...25→Z）
            int remainder = index % 26;
            sb.append((char) ('A' + remainder));
            // 整除26后减1，处理进位（如26→Z，27→AA：27/26=1，1-1=0）
            index = index / 26 - 1;
        }
        // 反转字符串（因为计算时是从后往前拼接的，如27→A+A→反转后AA）
        return sb.reverse().toString();
    }


    // ======================== 模板辅助方法（解决3个核心问题，重点修改） ========================
    /**
     * 创建表头样式（居中、加粗、边框）
     */
    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * 彻底解决问题1：createRichTextString方法不存在
     * 改用HSSF专属API，直接创建HSSFComment+HSSFRichTextString，无跨版本兼容问题
     */
    private static void createRequiredComment(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCell cell, String fieldName) {
        // 1. 创建绘图对象（HSSF专属）
        HSSFPatriarch drawing = sheet.createDrawingPatriarch();
        // 2. 定义批注位置：单元格右侧，占3列3行
        HSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                (short) (cell.getColumnIndex() + 1), cell.getRowIndex(),
                (short) (cell.getColumnIndex() + 4), cell.getRowIndex() + 3);
        // 3. 创建批注（HSSF专属，无需Workbook.createRichTextString）
        HSSFComment comment = drawing.createCellComment(anchor);
        // 4. 直接创建HSSFRichTextString，彻底解决方法不存在问题
        HSSFRichTextString text = new HSSFRichTextString(fieldName + "【该字段为必填项，不能为空】");
        // 5. 设置批注内容和作者
        comment.setString(text);
        comment.setAuthor("系统自动生成");
        // 6. 绑定批注到单元格
        cell.setCellComment(comment);
    }

    /**
     * 彻底解决问题2：DataValidationConstraint参数类型不匹配
     * 入参直接为String[]，匹配createExplicitListConstraint的必填类型，无类型转换
     */
    private static void createDictDataValidation(HSSFSheet sheet, int col, String[] dictOptions) {
        // 1. 定义下拉框生效范围：第1行到65535行（整列数据行，跳过表头）
        CellRangeAddressList rangeList = new CellRangeAddressList(1, 65535, col, col);
        // 2. 创建验证助手（HSSF专属）
        HSSFDataValidationHelper helper = new HSSFDataValidationHelper(sheet);
        // 3. 直接传入String[]，彻底解决参数类型不匹配问题
        DataValidationConstraint constraint = helper.createExplicitListConstraint(dictOptions);
        // 4. 创建数据验证规则
        DataValidation validation = helper.createValidation(constraint, rangeList);
        // 5. 配置验证规则：禁止手动输入，输入错误弹出提示
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("输入错误", "请选择下拉框中的有效选项，不允许手动输入");
        // 6. 添加入工作表
        sheet.addValidationData(validation);
    }

    // ======================== 通用辅助方法（兼容Long类型，无修改） ========================
    /**
     * 获取单元格值（兼容所有类型，转为字符串，Long类型转String无异常）
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                } else {
                    String numStr = String.valueOf(cell.getNumericCellValue());
                    // 数字去尾零，兼容Long类型（如123.0→123）
                    return numStr.endsWith(".0") ? numStr.replace(".0", "") : numStr;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return getCellValue(cell.getCachedFormulaResultType(), cell);
            default:
                return "";
        }
    }

    /**
     * 处理公式单元格值
     */
    private static String getCellValue(CellType cellType, Cell cell) {
        if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else {
            return "";
        }
    }

    /**
     * 数组包含判断
     */
    private static boolean contains(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        for (int num : arr) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建通用单元格样式（居中、边框、自动换行）
     */
    private static CellStyle createCommonStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    /**
     * 批量设置列宽
     */
    private static void setColumnWidths(Sheet sheet, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i] * 256);
        }
    }

    /**
     * 写入文件并关闭流（通用方法，避免重复代码）
     */
    private static void writeAndClose(Workbook wb, String filePath) throws IOException {
        try (OutputStream stream = new FileOutputStream(filePath)) {
            wb.write(stream);
        } finally {
            wb.close();
        }
    }


}
