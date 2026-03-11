import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

const ExpiresInKey = 'Admin-Expires-In'

const userTypeKey = 'userType'

const iscUserId = 'iscUserId'

export function getToken() {
  let geturl = window.location.href
  let getqyinfo = geturl.split('?')[1]   //qycode=1001&qyname=%E4%BC%81%E4%B8%9A%E5%BF%99   截取到参数部分
  let getqys = new URLSearchParams('?'+getqyinfo)  //将参数放在URLSearchParams函数中
  let passToken = getqys.get('token')   //1001
  if(passToken) {
    return passToken
  }
  else {return Cookies.get(TokenKey)}
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getExpiresIn() {
  return Cookies.get(ExpiresInKey) || -1
}

export function setExpiresIn(time) {
  return Cookies.set(ExpiresInKey, time)
}

export function removeExpiresIn() {
  return Cookies.remove(ExpiresInKey)
}

export function getUserType() {
  return Cookies.get(userTypeKey) || '当前'
}

export function setUserType(time) {
  return Cookies.set(userTypeKey, time)
}


export function getIscUserId() {
  return Cookies.get(iscUserId)
}
export function removeIscUserId() {
  return Cookies.remove(iscUserId)
}
