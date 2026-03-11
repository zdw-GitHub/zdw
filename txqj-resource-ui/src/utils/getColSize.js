
export function getColSize (val, titVal) {
  let doms = document.getElementsByClassName('getSpanSize')
  let dom
  for (let i = 0; i < doms.length; i++) {
    if (doms[i] && doms[i].offsetParent != null) {
      dom = doms[i]
      break
    }
  }
  if (!dom) {
    return 80
  }
  let width1 = 0
  let width2 = 0
  if (val) {
    let span = document.createElement('span')
    span.innerHTML = val
    span.setAttribute('class','fontSize')
    span.style.visibility = 'hidden';
    dom.appendChild(span)
    width1 = span.offsetWidth
    dom.removeChild(span)
  }
  if (titVal) {
    let span = document.createElement('span')
    span.innerHTML = titVal
    span.setAttribute('class','fontSize')
    span.style.visibility = 'hidden';
    dom.appendChild(span)
    width2 = span.offsetWidth
    dom.removeChild(span)
  }
  if(width1 > 420){
    width1 = 420
  }
  return Math.max(width1, width2) + 80
}
