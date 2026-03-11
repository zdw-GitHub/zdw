import request from '@/utils/request'



// 获取光缆列表
export function getFiberListApi() {
    return request({
        url: '/analysis/PathAlgorithms/getFiberList',
        method: 'get'
    })
}


// 根据类型获取起始终止资源列表
export function getResListByTypeApi(params) {
    return request({
        url: '/analysis/PathAlgorithms/getResListByType?type='+params,
        method: 'get'
    })
}


// 获取传输平面列表
export function getTcplanLitsApi() {
    return request({
        url: '/analysis/PathAlgorithms/getTcplanList',
        method: 'get'
    })
}

// 获取站点类型列表
export function getSiteTypeListApi() {
    return request({
        url: '/analysis/PathAlgorithms/getSiteTypeList',
        method: 'get'
    })
}

// 获取光缆类型列表
export function getFiberTypeListApi() {
    return request({
        url: '/analysis/PathAlgorithms/getFiberTypeList',
        method: 'get'
    })
}

// 获取电压等级列表
export function getVoltageClassListApi() {
    return request({
        url: '/analysis/PathAlgorithms/getVoltageClassList',
        method: 'get'
    })
}


// 路径分析
export function pathAlgorithmsApi(params) {
    return request({
        url: '/analysis/PathAlgorithms/pathAlgorithms',
        method: 'get',
        params:params
    })
}


export function _debounce(fn, delay = 300) {
    var timer = null;
    return function () {
      var _this = this;
      var args = arguments;
      if (timer) clearTimeout(timer);
      timer = setTimeout(function () {
        fn.apply(_this, args);
      }, delay);
    };
  }

//影响情况
 export function impactSituationApi(params){
    return request({
        url: '/analysis/PathAlgorithms/impactSituation',
        method: 'get',
        params:params
    })
 }

 export function getOpticalCarryBuzListApi(params){
    return request({
        url: '/analysis/PathAlgorithms/getOpticalCarryBuzList?id='+params,
        method: 'get'
    })
 }

export function querySiteInformation(data) {
  return request({
    url: '/analysis/PathAlgorithms/querySiteInformation',
    method: 'post',
    data: data
  })
}

// 不可靠路由分析
export function querySiteInformationByEquipment(data) {
  return request({
    url: '/analysis/PathAlgorithms/querySiteInformationByEquipment',
    method: 'post',
    data: data
  })
}
export function getAnalysisResInfo(params) {
  return request({
    url: '/analysis/PathAlgorithms/getAnalysisResInfo',
    method: 'get',
    params:params
  })
}
export function saveEmergencyRoute(data) {
  return request({
    url: '/analysis/PathAlgorithms/saveEmergencyRoute',
    method: 'post',
    data: data
  })
}
export function saveBuzEmergencyRoute(params) {
  return request({
    url: '/analysis/PathAlgorithms/saveBuzEmergencyRoute',
    method: 'post',
    params:params
  })
}


