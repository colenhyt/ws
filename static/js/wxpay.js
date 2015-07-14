// 部分来自网络，不保证全部有用
// 购买和收获地址为OK
function wxUnify(appId, timeStamp, nonceStr, packages, paySign, cb) {
 
}

function arrayToXml(array)
{
  var xmlData = "<xml>"
  for (var key in array){
   xmlData += "<"+key+">"+array[key]+"</"+key+">"
  }
  xmlData += "</xml"
}