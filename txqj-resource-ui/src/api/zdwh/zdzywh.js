import request from '@/utils/request'
/*获取modelId*/
export function getModelId(query) {
  return request({
    url:'/resourceManage/getModelId',
    method: 'get',
    params:query
  })
}
//动态获取查询条件与属性
export function initgriddatasite(data) {
  return request({
    url:'/resourceManage/initgriddatasite',
    method: 'post',
    data
  })
}
// 删除站点
export function deleteStation(data) {
  return request({
    url: '/resourceManage/delete',
    method: 'post',
    data
  })
}
// 删除站点同时删除规则
export function deleteError(id) {
  return request({
    url: '/result/removeResult?siteId='+ id,
    method: 'delete',
  })
}
// 获取表格下拉框数据
export function tableDropDownBox(data) {
  return request({
    url: '/verrules/tableDropDownBoxForOne',
    // url: '/verrules/tableDropDownBox',
    method: 'get',
    params:data
  })
}
// 获取表格下拉框数据 待处理数据
export function awaitTableDropDownBox(data) {
  return request({
    url: '/verrules/tableDropDownBoxByBD',
    method: 'get',
    params:data
  })
}
/*获取资源树*/
export function tree(data) {
  return request({
    url:'/show/listwithcolumninfoQuery',
    method: 'get',
    params:data
  })
}
/*获取展示配置*/
export function tabData(data) {
  return request({
    url:'/show/listwithcolumninfonew',
    method: 'get',
    params:data
  })
}
/*获取展示基本信息*/
export function getInfo(id) {
  return request({
    url:'/model/' + id,
    method: 'get',
  })
}
/*获取展示基本信息*/
export function getInfo1(id) {
  return request({
    url:'/category/' + id,
    method: 'get',
  })
}
/*获取展示配置*/
export function editCommit(data) {
  return request({
    url:'/show',
    method: 'put',
    data:data
  })
}
//获取新增全部规则
export function selectAllRules() {
  return request({
    url: '/verrules/selectByModeIdVersionforCreate',
    method: 'get'
  })
}
/*获取属性id检验传字段*/
export function getColunmId(query) {
  return request({
    url:'/resourceManage/getColunmId',
    method: 'get',
    params: query
  })
}
/*检验传填入值*/
export function getVerrules(query) {
  return request({
    url:'/verrules/singleAttributeVerification',
    method: 'get',
    params: query
  })
}
// 提交新增
export function submit(data) {
  return request({
    url: '/resourceManage/add',
    method: 'post',
    data
  })
}
/*错误站点保存传值*/
export function errorStation(data) {
  return request({
    url:'/result/createToUpdateRule',
    method: 'post',
    data: data
  })
}
/*导出*/
export function exportExcel(data) {
  return request({
    url:'/resourceManage/export',
    method: 'post',
    data: data
  })
}

export function downloadImportTemplate(data) {
  return request({
    url:'/resourceManage/import/template',
    method: 'post',
    data: data
  })
}

// 导入Excel数据
export function importDataByExcel(file, modelId) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('modelId', modelId)
  return request({
    url: '/resourceManage/import/submit',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

//获取单个站点对象
export function getForm(data) {
  return request({
    url: '/resourceManage/getInfo',
    method: 'post',
    data:data
  })
}
//文件下载
export function fileDownload(query) {
  return request({
    url: '/business/common/download',
    method: 'get',
    params: query,
    responseType: 'arraybuffer'
  })
}

//文件下载
export function fileDownload2(query) {
  return request({
    url: '/analysis/common/download',
    method: 'get',
    params: query,
    responseType: 'arraybuffer'
  })
}

// 定义格式化封装函数
function formaData(timer) {
  const year = timer.getFullYear()
  const month = timer.getMonth() + 1 // 由于月份从0开始，因此需加1
  const day = timer.getDate()
  const hour = timer.getHours()
  const minute = timer.getMinutes()
  const second = timer.getSeconds()
  return `${pad(year, 4)}_${pad(month)}_${pad(day)}_${pad(hour)}_${pad(minute)}_${pad(second)}`
}
// 定义具体处理标准
// timeEl 传递过来具体的数值：年月日时分秒
// total 字符串总长度 默认值为2
// str 补充元素 默认值为"0"
function pad(timeEl, total = 2, str = '0') {
  return timeEl.toString().padStart(total, str)
}
export function export_excel_file(export_data, list_name) {
    let link = document.createElement("a");
    let blogw = new Blob([export_data], {
    type: "application/vnd.ms-excel;charset=utf-8"
    })
    let objectUrl = window.URL.createObjectURL(blogw);
    link.href = objectUrl;
    const date = formaData(new Date()) // 保留原有日期格式化逻辑
    
    // 核心改造：正则判断是否包含xls/xlsx后缀，有则不拼接，无则拼接.xlsx
    let file_name;
    const suffixReg = /\.(xls|xlsx)$/i; // 匹配xls/xlsx后缀（忽略大小写）
    if (suffixReg.test(list_name)) {
        // 带后缀：直接拼接列表+日期，不额外加后缀
        file_name = `${list_name.replace(suffixReg, '')}列表${date}${list_name.match(suffixReg)[0]}`;
    } else {
        // 不带后缀：按原有逻辑拼接.xlsx后缀
        file_name = `${list_name}列表${date}.xlsx`;
    }

    link.download = file_name;
    link.click();
    window.URL.revokeObjectURL(objectUrl);
  }
/*查询表格*/
export function getData(query) {
  return request({
    url: '/resourceManage/list',
    method: 'post',
    data: query
  })
}
// 提交修改
export function submitEdit(data) {
  return request({
    url: '/resourceManage/update',
    method: 'post',
    data: data
  })
}
//获取条件查询列表
export function mapMessage(data) {
  return request({
    url: '/business/common/MapMessage',
    method: 'get',
    params:data
  })
}
//条件查询列表首字母索引
export function conditionalData(data) {
  return request({
    url: '/business/common/conditionalData',
    method: 'post',
    data:data
  })
}
//获取主键
export function getPrimaryKeyName(data) {
  return request({
    url: '/resourceManage/getPrimaryKeyName',
    method: 'post',
    params: data
  })
}

// 获取表单树配置
export function getTreeConfigApi(data) {
  return request({
    url: '/tree/getMetaTechTreeNodeInfo',
    method: 'post',
    data: data
  })
}

// 获取表单树下拉框
export async function getTreeOptions(data) {
  let arr
  await request({
    url: '/treenode/queryResDataTree',
    method: 'post',
    data: data
  }).then(res => {
    for (let i = 0; i < res.data.length; i++) {
      res.data[i].showId = res.data[i].nodeLevel + res.data[i].code
    }
    arr = {data: res.data}
  })
  return arr
}

function setShowId (arr) {
  for (let i = 0; i < arr.length; i++) {
    arr[i].showId = arr[i].nodeLevel + arr[i].code
    if (arr[i].children) {
      setShowId(arr[i].children)
    }
  }
}

//表单树下拉框多级搜索
export async function queryResDataTreeAll(data) {
  let arr
  await request({
    url: '/treenode/queryResDataTreeAll',
    method: 'post',
    data: data
  }).then(res => {
    setShowId(res.data)
    arr = {data: res.data}
  })
  return arr
}

// 表单下拉框树回显
export function getTreeSelDataApi(data) {
  return request({
    url: '/treenode/resDataTreeEcho',
    method: 'post',
    data: data
  })
}


// 获取批量转换表格内容
export function getTransitionTableDataApi (params) {
  return request({
    url: '/rule/getModelRuleList',
    method: 'get',
    params: params
  })
}

//按规则转换元模型数据
export function commitTransitionApi (params) {
  return request({
    url: '/rule/convertMetaModelDataByRule',
    method: 'post',
    data: params
  })
}

//根据筛选条件获取数据ID
export function obtainIDBasedOnFilteringCriteria (params) {
  return request({
    url: '/resCommon/obtainIDBasedOnFilteringCriteria',
    method: 'post',
    data: params
  })
}
export function uploadFile (params) {
  return request({
    url: '/file/upload',
    method: 'post',
    data: params
  })
}
export function getNewIdByOriginalId (params) {
  return request({
      url: '/DataDetailInformation/getNewIdByOriginalId',
    method: 'get',
    params: params
  })
}
export function getNewTabInfo (params) {
  return request({
      url: '/resourceManage/getNewTabInfo',
    method: 'get',
    params: params
  })
}

