var SiteUrl = "http://112.74.108.46:8080/cf"

var Share_Img = SiteUrl+"/static/img/app_icon_share.png"

var Share_Url = SiteUrl+"/dl.html"

var Dl_Url_ios = "http://itunes.apple.com/cn/app/id549421060"

var Dl_Url_android = "http://dd.myapp.com/16891/32B5098A37D290206587504942EBB30D.apk?fsname=com.pingan.lifeinsurance_2.5.6.2_52.apk&asr=8eff"

var g_game;

var LoginMode = 0;	//1为注册模式，0为登陆模式

var UpdateDuration = 200;	//刷帧频率(ms)

var MsgDuration = 100;	//消息刷帧频率(ms)

var StockDuration = 1000;	//股票刷帧频率(ms)

var NetReqWait = 10000;	//网络请求等待时间(ms)

var QUOTETIME = 300;		//行情跳动时间(秒)

var FirstEventTriggerTime = 60*5;	//注册后首次意外事件触发事件,

var EventTriggerTime = 60*5;	//随机事件跳动;*UpdateDuration/1000 秒

var Panel_ClickColor = "#123123";

var Is_InBrowser = true;

var Share_Prize = 500

var Share_PageText = "又开始了新的一天！是否分享您今日的心情给您的好朋友们?"

var Share_PageText2 = "成功分享将获得<span style='color:red'>"+Share_Prize+"元</span>游戏币奖励"

var Share_Title = "我正在使用平安人寿APP玩<<财富人生>>游戏，快跟我一起来吧，更多惊喜和活动等着您:"+Share_Url

var Share_Text = "财富人生"

var Share_PageText_Prize = "获得<img class='cficon_money' src='static/img/money.png'/><span style='color:red'>"+Share_Prize+"</span>"

//var Share_Url = "http://elife.pingan.com/"

var Login_InputDft = "输入昵称";

var login_imgs = [
	{name:"map",src:"static/img/login_bg.png",x:0,y:0,zindex:0},
	{name:"inputnick",src:"static/img/nick_input.png",x:130,y:110,zindex:0},
	{name:"choseboy",src:"static/img/player_head_bg.png",x:150,y:380,zindex:0},
	{name:"chosegirl",src:"static/img/player_head_bg.png",x:400,y:380,zindex:0},
	{name:"choseboy",src:"static/img/icon_boy.png",x:150,y:380,zindex:0},
	{name:"chosegirl",src:"static/img/icon_girl.png",x:400,y:380,zindex:0},
	{name:"wchoseboy",src:"static/img/bt_boy.png",x:130,y:510,zindex:0},
	{name:"wchosegirl",src:"static/img/bt_girl.png",x:390,y:510,zindex:0},
	{name:"btstart",src:"static/img/bt_start.png",x:205,y:590,zindex:0},
]

var game_imgs = [
	{name:"map",src:"static/img/map.png",x:0,y:0,zindex:0},
	{name:"quest",src:"static/img/icon_quest.png",x:555,y:7,hasDiv:true},
	{name:"playerinfo",src:"static/img/icon_head.png",x:0,y:0,hasDiv:true},
	{name:"toplist",src:"static/img/icon_toplist.png",x:555,y:120,hasDiv:true,zindex:100},
	{name:"help",src:"static/img/icon_help.png",x:170,y:280,hasDiv:true},
	{name:"stock",src:"static/img/icon_stock.png",x:385,y:207,hasDiv:true,zindex:100,},
	{name:"bank",src:"static/img/icon_bank.png",x:70,y:448,hasDiv:true,zindex:100,},
	{name:"insure",src:"static/img/icon_insure.png",x:342,y:622,hasDiv:true,zindex:100,},
	{name:"saving",src:"static/img/icon_saving.png",x:150,y:15,hasDiv:true},
	{name:"weektop",src:"static/img/icon_weektop.png",x:365,y:15,hasDiv:true},
	{name:"level",src:"static/img/icon_level.png",x:-5,y:103,hasDiv:true},
];

var head_imgs = [
	{name:"man",src:"static/img/icon_boy.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
	{name:"women",src:"static/img/icon_girl.png",x:0,y:0,abs:true,hasDiv:true,divX:160,divY:10,zindex:10},
]

var feeling_imgs = [
    {id:1,src:"static/img/feeling_1.png"},
    {id:2,src:"static/img/feeling_2.png"},
    {id:3,src:"static/img/feeling_3.png"},
    {id:4,src:"static/img/feeling_4.png"},
    {id:5,src:"static/img/feeling_5.png"},
    {id:6,src:"static/img/feeling_6.png"},
    {id:7,src:"static/img/feeling_7.png"},
    {id:8,src:"static/img/feeling_8.png"},
]

var QUEST_TYPE = {
	BUY_INSURE:0,BUY_FINAN:1,BUY_STOCK:2,SELL_STOCK:3,SAVING:4
}

var QUEST_STATUS = {
	ACTIVE:0,DONE:1,FINISH:2
}

var ITEM_TYPE = {
	CASH : 0,EXP:1
};

var ITEM_NAME = {
	CASH : "金钱",EXP:"经验"
};

var EVENT_TYPE = [
];

var MSG_OK = 0;
var MSG_SQLExecuteError = 1;
var MSG_MoneyNotEnough = 2;
var MSG_NoThisStock = 3;
var MSG_NoSavingData = 4;
var MSG_PlayerNameIsExist = 5;
var MSG_WrongPlayerNameOrPwd = 6;
var MSG_StockIsClosed = 7;

var ERR_MSG ={};
ERR_MSG[MSG_SQLExecuteError] = "sql出错";
ERR_MSG[MSG_MoneyNotEnough] = "您的钱不够";
ERR_MSG[MSG_NoThisStock] = "这个股票不存在";
ERR_MSG[MSG_NoSavingData] = "没有该存款";
ERR_MSG[MSG_PlayerNameIsExist] = "这个昵称已经被使用";
ERR_MSG[MSG_WrongPlayerNameOrPwd] = "用户名或密码不正确";
ERR_MSG[MSG_StockIsClosed] = "股市已关闭,不能买卖";

function loadStyle(url){
    var link = document.createElement('link');
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = url;
    var head = document.getElementsByTagName("head")[0];
    head.appendChild(link);
}

var Page_Top = 80;
var PageDetail_Top = 100;

var Screen_Status_Height = 40;
var Screen_Nav_Height = 88;

var Scene_Height = 1236 - Screen_Status_Height - Screen_Nav_Height;

var PageSizes = {
	"640":{SceneWidth:640,SceneHeight:1008,PieWidth:350,
	PieHeight:400,PageWidth:"580px",PieFontSize:30,PieFontSize2:23,
	PageHeight:"637px",PageTop:80,
	DetailPageTop:100,MsgTop:120,
	StockView:[540,420,480,280,22]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","20px","210px"]
	,QuestMoney:["120px","390px"]
	},
	
	"480":{SceneWidth:640,SceneHeight:1008,PieWidth:260,PieFontSize:24,PieFontSize2:18,
	PieHeight:280,PageWidth:"432px",PageHeight:"477px",PageTop:60,
	DetailPageTop:80,MsgTop:100,
	StockView:[410,350,400,230,18]
	,EventMoney:["70px","320px"]
	,SigninMoney:["60px","50px","160px"]
	,QuestMoney:["120px","390px"]
	},
	
	"360":{SceneWidth:640,SceneHeight:1008,PieWidth:200,PieFontSize:20,PieFontSize2:16,
	PieHeight:220,PageWidth:"328px",PageHeight:"477px",PageTop:40,
	DetailPageTop:50,MsgTop:50,
	StockView:[300,300,280,190,15]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","50px","210px"]
	,QuestMoney:["120px","390px"]
	},

	"320":{SceneWidth:640,SceneHeight:1008,PieWidth:180,PieFontSize:16,PieFontSize2:15,
	PieHeight:190,PageWidth:"290px",PageHeight:"477px",PageTop:50,
	DetailPageTop:60,MsgTop:40,
	StockView:[270,220,260,120,15]
	,EventMoney:["70px","410px"]
	,SigninMoney:["90px","50px","210px"]
	,QuestMoney:["120px","390px"]
	},
}

var SCREENKEY = 640;
var SIZEPER = 1;

 var browser={
    versions:function(){ 
           var u = navigator.userAgent, app = navigator.appVersion; 
           //alert(u);
           return {//移动终端浏览器版本信息 
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
                iPhone5: u.indexOf('iPhone OS 6_1') > -1 , //os版本
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            };
         }(),
         language:(navigator.browserLanguage || navigator.language).toLowerCase()
} 

function initScreen(){
//alert(window.screen.width)
	var versions = browser.versions
 	 var width = window.screen.width;
	if (versions.iPhone||versions.iPad){
		SCREENKEY =640;	 
	}else {
	  if (width>=640)
		SCREENKEY = 640;
	  else if (width<640&&width>=480)
		SCREENKEY = 480;
	  else if (width<480&&width>=360)
		SCREENKEY = 360;
	  else
	    SCREENKEY = 320;	
	}
	 //  SCREENKEY = 320;	  
//
//alert(browser.versions.iPhone)
//alert(width);
//alert(SCREENKEY);
//SCREENKEY = 640
	//SCREENKEY = 480;
	SIZEPER = SCREENKEY/640;
	var cssFile = "static/css/cf"+SCREENKEY+".css";
	loadStyle(cssFile);		
}

initScreen();

getSizes = function(){	
	return PageSizes[SCREENKEY];
}

function sc(){
var s = "<div style='color:red'>"; 
s += " 网页可见区域宽："+ document.body.clientWidth+"<br />"; 
s += " 网页可见区域高："+ document.body.clientHeight+"<br />"; 
s += " 网页可见区域宽："+ document.body.offsetWidth + " (包括边线和滚动条的宽)"+"<br />"; 
s += " 网页可见区域高："+ document.body.offsetHeight + " (包括边线的宽)"+"<br />"; 
s += " 网页正文全文宽："+ document.body.scrollWidth+"<br />"; 
s += " 网页正文全文高："+ document.body.scrollHeight+"<br />"; 
s += " 网页被卷去的高(ff)："+ document.body.scrollTop+"<br />"; 
s += " 网页被卷去的高(ie)："+ document.documentElement.scrollTop+"<br />"; 
s += " 网页被卷去的左："+ document.body.scrollLeft+"<br />"; 
s += " 网页正文部分上："+ window.screenTop+"<br />"; 
s += " 网页正文部分左："+ window.screenLeft+"<br />"; 
s += " 屏幕分辨率的高："+ window.screen.height+"<br />"; 
s += " 屏幕分辨率的宽："+ window.screen.width+"<br />"; 
s += " 屏幕可用工作区高度："+ window.screen.availHeight+"<br />"; 
s += " 屏幕可用工作区宽度："+ window.screen.availWidth+"<br />"; 
s += " 您的屏幕设置是 "+ window.screen.colorDepth +" 位彩色"+"<br />"; 
s += " 您的屏幕设置 "+ window.screen.deviceXDPI +" 像素/英寸"+"<br />"; 
 s += "</div>"
document.writeln(s);
}
function aa(){
document.writeln("语言版本: "+browser.language);
document.writeln(" 是否为移动终端: "+browser.versions.mobile);
document.writeln(" ios终端: "+browser.versions.ios);
document.writeln(" android终端: "+browser.versions.android);
document.writeln(" 是否为iPhone: "+browser.versions.iPhone);
document.writeln(" 是否为iPhone5: "+browser.versions.iPhone5);
document.writeln(" 是否iPad: "+browser.versions.iPad);
document.writeln(navigator.userAgent);
}

//sc();
//aa();
