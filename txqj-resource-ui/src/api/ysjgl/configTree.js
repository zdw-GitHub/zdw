import request from '@/utils/request'


export function getTableDataApi(query) {
  return request({
    url: '/tree/treeList',
    method: 'get',
    params: query
  })
}

export function treeCommitApi(type, query) {
  return request({
    url: '/tree',
    method: type,
    data: query
  })
}

export function getTreeDataByIdApi(id) {
  return request({
    url: '/tree/' + id,
    method: 'get',
  })
}

export function delTreeCommitApi(id) {
  return request({
    url: '/tree/removeById/' + id,
    method: 'delete',
  })
}

export function getTableTreeOptionsApi(query) {
  return request({
    url: '/tech/list',
    method: 'get',
    params: query
  })
}

export function nodeFormCommitApi(query) {
  return request({
    url: '/treenode/addOrUpdateTreeNode',
    method: 'post',
    data: query
  })
}

export function getNodeByIdApi(id) {
  return request({
    url: '/treenode/' + id,
    method: 'get',
  })
}

export function delNodeCommitApi(id) {
  return request({
    url: '/treenode/' + id,
    method: 'delete',
  })
}