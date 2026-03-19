
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
    return 150
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
  const baseWidth = Math.max(width1, width2);
  const minWidth = 120; // 最小保障宽度
  const extraSpace = 100; // 额外余量
  return Math.max(baseWidth, minWidth) + extraSpace;
}
