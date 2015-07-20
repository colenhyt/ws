
var WXAPI_GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

Cart = function(){
}

Cart.prototype.init = function(){

  /*
   * 注意：
   * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
   * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
   * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
   *
   * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
   * 邮箱地址：weixin-open@qq.com
   * 邮件主题：【微信JS-SDK反馈】具体问题
   * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
   */
  wx.config({
      debug: true,
      appId: 'wxf8b4f85f3a794e77',
      timestamp: 1437360914,
      nonceStr: 'Jz24bYGgwU5G7Jz0',
      signature: '7c8d3f4f7f5d4f811805713ccccd5901efd5748c',
      jsApiList: [
        'checkJsApi',
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
        'hideMenuItems',
        'showMenuItems',
        'hideAllNonBaseMenuItem',
        'showAllNonBaseMenuItem',
        'translateVoice',
        'startRecord',
        'stopRecord',
        'onRecordEnd',
        'playVoice',
        'pauseVoice',
        'stopVoice',
        'uploadVoice',
        'downloadVoice',
        'chooseImage',
        'previewImage',
        'uploadImage',
        'downloadImage',
        'getNetworkType',
        'openLocation',
        'getLocation',
        'hideOptionMenu',
        'showOptionMenu',
        'closeWindow',
        'scanQRCode',
        'chooseWXPay',
        'openProductSpecificView',
        'addCard',
        'chooseCard',
        'openCard'
      ]
  });

/*
 * 注意：
 * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
 * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
 * 3. 完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
 *
 * 如有问题请通过以下渠道反馈：
 * 邮箱地址：weixin-open@qq.com
 * 邮件主题：【微信JS-SDK反馈】具体问题
 * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
 */
wx.ready(function () {
}
}

// 部分来自网络，不保证全部有用
// 购买和收获地址为OK
function wxBuy(appId, timeStamp, nonceStr, packages, paySign, cb) {
    if (typeof WeixinJSBridge == "undefined") {
        alert("请先通过微信访问！");
    } else {
        WeixinJSBridge.invoke('getBrandWCPayRequest',{
            "appId" : appId, //公众号名称，由商户传入
            "timeStamp" : timeStamp, //时间戳 这里随意使用了一个值
            "nonceStr" : nonceStr, //随机串
            "package" : packages, //扩展字段，由商户传入
            "signType" : "SHA1", //微信签名方式:sha1
            "paySign" : paySign //微信签名
        }, function(res){
            WeixinJSBridge.log(res.err_msg);
            cb(res);
            // 返回 res.err_msg,取值
            // get_brand_wcpay_request:cancel 用户取消
            // get_brand_wcpay_request:fail 发送失败
            // get_brand_wcpay_request:ok 发送成功
            //alert(res.err_code + res.err_desc);
        });
    }
};
// 微信收获地址
function wxGetAddress (appId, cb){
    if (typeof WeixinJSBridge == "undefined") {
        alert("请先通过微信访问！");
    } else {
        WeixinJSBridge.invoke('getRecentlyUsedAddress',{
            //公众号名称，由商户传入
            "appId" : appId
        },function(res){
            cb(res);
            //  返回 res.err_msg,取值
            // get_recently_used_address:fail  获取失败
            // get_recently_used_address:ok  获取成功
            // WeixinJSBridge.log(res.err_msg);
            // 收获地址格式为下列数据共同组成，其中参数列表如下：
            // userName:收货人姓名
            // telNumber:收货人电话号码
            // addressPostalCode:邮政编码
            // proviceFirstStageName:收货地址第⼀一级省、直辖市、自治区、特别行政区名称
            // addressCitySecondStageName:收货地址第二级市名称
            // addressCountiesThirdStageName:收货地址第三级区县名称
            // addressDetailInfo:收货地址详细信息
            // alert(res.userName+res.telNumber+res.addressPostalCode+res.proviceFirstStageName+res.addressCitySecondStageName+res.addressCountiesThirdStageName+res.addressDetailInfo);
        });
    }
};
// 修改微信地址
function wxEditAddress(appId, cb) {
    if (typeof WeixinJSBridge == "undefined") {
        alert("请先通过微信访问！");
    } else {
        WeixinJSBridge.invoke('editTransactionAddress',{
            //公众号名称，由商户传入
            "appId" : appId
        },function(res){
            cb(res);
            // 返回 res.err_msg,取值
            // edit_address:fail  编辑被取消
            // edit_address:ok  编辑成功
            // WeixinJSBridge.log(res.err_msg);
            // 收获地址格式为下列数据共同组成，其中参数列表如下：
            // userName:收货人姓名
            // telNumber:收货人电话号码
            // addressPostalCode:邮政编码
            // proviceFirstStageName:收货地址第⼀一级省、直辖市、自治区、特别行政区名称
            // addressCitySecondStageName:收货地址第二级市名称
            // addressCountiesThirdStageName:收货地址第三级区县名称
            // addressDetailInfo:收货地址详细信息
            // alert(res.userName+res.telNumber+res.addressPostalCode+res.proviceFirstStageName+res.addressCitySecondStageName+res.addressCountiesThirdStageName+res.addressDetailInfo);
        });
    }
}
