var g_wxconfig = null;

WxCaller = function(){
}

WxCaller.prototype.init = function(){
return;
 var cfg = g_wxconfig;
 //alert(cfg.noncestr);
//	var dataobj = $.ajax({type:"post",url:"/ec/login_wxjsinit.do",async:false});
//	var cfg = cfeval(dataobj.responseText);
	if (cfg.appid.length>0){
	 //alert('wx.config init 完成');
	}
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
      debug: false,
      appId: cfg.appid,
      timestamp: cfg.timestamp,
      nonceStr: cfg.noncestr,
      signature: cfg.sign,
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
   //alert('ready回调');
  });
  
  wx.error(function(res){
   alert('失败回调'+res);
  });
}

WxCaller.prototype.reqWxpay = function(jsonReq){
//alert('bbb');
//wx.chooseWXPay({
//    timeStamp: jsonReq.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
//    nonceStr: jsonReq.nonceStr, // 支付签名随机串，不长于 32 位
//    package: jsonReq.prepay_id, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
//    signType: jsonReq.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
//    paySign: jsonReq.sign, // 支付签名
//    success: function (res) {
//        // 支付成功后的回调函数
//          alert('成功aaa');
//    },
//    cancel: function (res) {
//       alert('支付被取消');
//    },  
//    fail: function (res) {
//       alert('微信支付失败:'+JSON.stringify(res));
//    },      
//});
  
   WeixinJSBridge.invoke(
       'getBrandWCPayRequest', {
           "appId" : jsonReq.appId,     //公众号名称，由商户传入     
           "nonceStr" : jsonReq.nonceStr, //随机串     
           timeStamp: jsonReq.timeStamp,
           "package" : jsonReq.prepay_id,     
           "signType" : jsonReq.signType,         //微信签名方式:     
           "paySign" : jsonReq.sign //微信签名 
       },
       function(res){
         var payOk = false;     
       // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            payOk = true;
           }   
            g_cart.wxpayCallback(payOk);
       }
   );  
}
var g_wx = new WxCaller();
