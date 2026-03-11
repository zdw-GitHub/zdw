import request from '@/utils/request'

/**
 * 1. 解析两个Excel，返回「2.2025年×月快报数据分析模板（新）.docx」所需数据
 * @param {FormData} formData - 含draftFile（底稿）、transferFile（划入）
 * @returns {Object} 解析结果：{ income: {}, balance: {}, cashFlow: {}, other: {} }
 */
export function parseExcelForTemplate(formData) {
  return request({
    url: '/finance/parseExcelForTemplate',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 2. 无模板生成「2.2025年×月快报数据分析.docx」
 * @param {FormData} formData - 含draftFile、transferFile、targetReportName（目标文件名）
 * @returns {Blob} Word文件流
 */
export function generateReportForTarget(formData) {
  return request({
    url: '/finance/generateReportForTarget',
    method: 'post',
    data: formData,
    responseType: 'blob' // 接收二进制流
  })
}