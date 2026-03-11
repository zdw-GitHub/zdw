import request from '@/utils/request'

export function getFiberList(params) {
  return request({
    url: '/analysis/fiberBreak/getFiberList',
    method: 'get',
    params:params
  })
}
export function getTySiteList(params) {
  return request({
    url: '/analysis/fiberBreak/getTySiteList',
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


export function getRelateFiberDetail(id) {
  return request({
    url: '/analysis/fiberBreak/getRelateFiberDetail?id=' + id,
    method: 'get',
  })
}

export function getDefaultMsg(id) {
  return request({
    url: '/analysis/fiberBreak/getDefaultMsg?id=' + id,
    method: 'get',
  })
}

export function saveFiberBreak(params) {
  return request({
    url: '/analysis/fiberBreak/saveFiberBreak',
    method: 'post',
    params:params
  })
}


export function getTyFiberList(params) {
  return request({
    url: '/analysis/fiberBreak/getTyFiberList',
    method: 'get',
    params:params
  })
}


export function deleteFiberBreak(id) {
  return request({
    url: '/analysis/fiberBreak/deleteFiberBreak?id=' + id,
    method: 'get',
  })
}



