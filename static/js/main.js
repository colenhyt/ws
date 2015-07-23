var catGoods = {};
var g_user = null;

function wxpayrequest(){

}

function renderGroupCats(catItems){
var tag = document.getElementById("catDiv");
var content = "";
for (var i=0;i<catItems.length;i++){
var cat = catItems[i];
content += "<li class=\"nav-parent\">"
content += "<a href='#' onclick='queryGroupGoods("+cat.catId+")'>"+cat.catName;
content += "<i class=\"icon-chevron-right nav-parent-fold-icon\"></i>"
content += "</a>";
content += "<ul class=\"nav\" id='cat"+cat.catId+"'>";
content += "</ul>";
content += "</li>";
}
tag.innerHTML = content;
}

function findGoodsNo(goodsId)
{
	var goodsno = 0;
	for (var catid in catGoods)
	{
		var items = catGoods[catid];
		for (var i=0;i<items.length;i++){
		 if (items[i].goodsId==goodsId){
		  goodsno = items[i].goodsNumber;
		  break;
		 }
		}
	}
	return goodsno;
}

function queryGroupCats(){
	//alert(dataParam);
	var dataobj = $.ajax({type:"post",url:"/ec/goods_cat.do",async:false});
	 var obj = cfeval(dataobj.responseText);
	 
	 renderGroupCats(obj);
}

function queryGroupGoods(catId){
 
 	var items = catGoods[catId];
 	if (items==null){
		var item = {catId:catId};
		var dataParam = obj2ParamStr("ecsGoods",item);
		//alert(dataParam);
		var dataobj = $.ajax({type:"post",url:"/ec/goods_goods.do",data:dataParam,async:false});
	    items = cfeval(dataobj.responseText);
	    catGoods[catId] = items;
 	}else {
 	
 	}
 	
    var tag = document.getElementById("cat"+catId);
    var content = "";
    for (var i=0;i<items.length;i++){
    var item = items[i];
    var imgSrc = "";
    if (item.originalImg.length>0)
     imgSrc = item.originalImg;
    else if (item.goodsImg.length>0)
     imgSrc = item.goodsImg;
    else if (item.goodsThumb.length>0)
     imgSrc = item.goodsThumb;
    else
     imgSrc = "/ec/images/upload/Image/"+item.goodsSn+".jpg";
    var goodsno = item.goodsNumber;
	content += "<table class='goods'>";
    content += "    <tr><td class='goods img'><img src='"+imgSrc+"' class='goodsImg'></td><td class='goods title'>"+item.goodsName+"</td></tr>"
    content += "    <tr><td class='goods desc' colspan=2>"+item.goodsDesc+"</td></tr>"
    content += "    <tr><td class='goods ps'> ￥"+item.shopPrice+"</td>"
    content += "<td class='goods no'>"
    if (goodsno>0){
     content += "剩余: "+goodsno
    }
    content += "</td></tr>"
    content += "    <tr><td class='goods' colspan=2>"
    if (goodsno>0){
     var func = "g_cart.count("+item.goodsId+",'"+item.goodsName+"',"+item.shopPrice;   
     content += "<li><img class='wsgoods_count' onclick=\""+func+",-1)\"><input type=\"text\" id=\"goodsCount"+item.goodsId+"\" value=\"0\" class='wsgoods_value'><img class='wsgoods_count add' onclick=\""+func+",1)\">"
    }
    else
     content += "已售空"
    content += "</td></tr>"
	content += "</table>";
	content += "<div class=\"line\"></div>"
    }
 	content += "<br>"
 	
    tag.innerHTML = content;
    
}

function queryGroup()
{
	var dataobj = $.ajax({type:"post",url:"/ec/goods_group.do",async:false});
	var group = cfeval(dataobj.responseText);
	var content = "";
	content += "<div class='header'>"
	content += group.actName;
	content += "</div>"
	content += "<div class='groupDesc'>"
	var desc = group.actDesc;
	content += desc;
	content += "</div>"
	
	var tag = document.getElementById("groupDiv");
	tag.innerHTML = content;
	
	var tag2 = document.getElementById("userinfo");
	g_user = cfeval(tag2.value);
	if (g_user==null){
	 g_user = {userId:0};
	}else {
	 if (g_user.city.length>0)
	  g_user.city += "市"
	}	
	
	//var tag3 = document.getElementById("wxconfig");
	//g_wxconfig = cfeval(tag3.value);
	//if (g_wxconfig!=null){
	//  g_wx.init();
	//}
		
	content = ""
	content += "<select id='region_province' class='ws_province' onchange=\"selectRegion(this[selectedIndex].text)\">"
	 content += "<option value='-1'>-省份/直辖市-</option>";
	for (var i=0;i<data_region.length;i++){
	 var pro = data_region[i];
	 content += "<option value='"+pro[0]+"'";
	 if (g_user.province==pro[1])
	  content += " selected";
	 content += ">"
	 content += pro[1]+"</option>"
	}
	content += "</select>"	
	
	content += "&nbsp&nbsp&nbsp"
	
	var cities = data_subregion[data_region[0][1]];
	if (data_subregion[g_user.province]){
	 cities = data_subregion[g_user.province];
	}
	content += "<select id='region_city' class='ws_province'>"
	 content += "<option value='-1'>-市-</option>";
	for (var i=0;i<cities.length;i++){
	var city = cities[i];
	 content += "<option value='"+city[0]+"'";
	 if (g_user.city==city[1])
	  content += " selected";
	 content += ">"
	 content += city[1]+"</option>"
	}
	content += "</select>"	
	var tagRegion = document.getElementById("regionDIV");
	tagRegion.innerHTML = content;
	
	//contact:
	if (g_user.nickname!=null){
	 var tag = document.getElementById("wxhao");
	 tag.innerHTML = g_user.nickname;
	}
	if (g_user.contact!=null){
	 var tag = document.getElementById("contact");
	 tag.value = g_user.contact;
	}
	if (g_user.mobile!=null){
	 var tag = document.getElementById("phone");
	 tag.value = g_user.mobile;
	}		
	if (g_user.address!=null){
	 var tag = document.getElementById("address");
	 tag.value = g_user.address;
	}
}

function selectRegion(province){
	var tagCity = document.getElementById("region_city"); 
	 while (tagCity.options.length>0)
    {
     tagCity.options.remove(tagCity.options.length-1);
    }
    var cities = data_subregion[province];
	 tagCity.options.add(new Option("-市-","-1"));
    for (var i=0;i<cities.length;i++){
	 var city =cities[i];
	 var op = new Option(city[1],city[0]);
     tagCity.options.add(op);    
    }
	tagCity.options[1].selected = true;
    
}

function choosePay(index){
	var tags = document.getElementsByName("paytype");
	if (index==0){
	 tags[0].checked = true;
	 tags[1].checked = false;
	}else {
	 tags[0].checked = false;
	 tags[1].checked = true;
	}
}

function testajax(){
    $.ajaxSetup({
        type: "POST",
        dataType: 'JSONP'
    });

    var appBase = "/xdnphb";
    var urlBase = appBase + "/";
    var appDomain = 'newrank.cn';

    var getDataSync = function(url, options) {
        $.ajax({
            url: "http://www.newrank.cn"+url + ".json",
            async : true,
            data: options,
            success: function(data) {
			alert('daa1');
            },
            error: function(data) {
			alert('daa2'+JSON.stringify(data));
               /* if ($.isFunction(networkFail)) {
                    networkFail(data);
                }*/
            }
        });
    };
    
    var  url = "/xdnphb/data/getAccountArticle";
    var option = {uid:"174",size:10};
    getDataSync(url,option);
}
//var aa ={'success':true,'value':{'lastestArticle':[{'id':'41076515','uid':'174','messageId':'222206411','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.864','summary':'冷兔槽，谁想出来的，管哪吒叫藕霸………','site':'微信','author':'冷兔','publicTime':'2015-07-21 20:04:46','clicksCount':'489148','likeCount':'6771','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222206408&idx=1&sn=f4359ec858aa38297b2cc0bfb8a4a2d1&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2fiaKn1jL5Xg9InHOreQTU2lb535M6Cp0bg3EJlRRSicsH0y2NE3mec9A/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，谁想出来的，管哪吒叫藕霸………懒居然可以战胜饥饿....#无法反驳。。。。#哈哈哈哈司令说得对一个女性朋友和直男聊天，她给发过去一句：“今晚的月色真好，本少女都忍不住想要变身了呢！”对方过了一会回复说：“没想到...你竟然是个狼人。”#并没有什么不对啊！#最近总有人夸我漂亮……当有人讨厌你的时候，不如反思一下自己，是不是性感可爱的你又完美得让人嫉妒了？#吾日三省吾身#车主在山东济南街头看到拍下来的。狗狗站在三轮车顶上，像风一样的男子。看电视的时候不停在嗑瓜子，女朋友一脸嫌弃地说：“你这么喜欢吃瓜子，直接买瓜子仁得了。”我摇摇头说：“那可不行，我享受的是嗑的过程。”她听完一脸兴奋地坐起来说：“等的就是你这句话，那你把瓜子仁嗑出来给我，你享受嗑的过程就好了！”#智商压制！#和朋友踢点球大战时手柄的必备道具时间让我明白了，除了公交车、快递和大姨妈值得让我去等，其他啥都等不到！#谢谢时间！#哈哈哈老板做指甲吗？问表嫂怎么决定嫁给表哥的，她说你表哥别的不行却有一手出神入化的剥壳技术啊！螃蟹虾姑小龙虾都能又快又清楚地去壳，能出来的肉全出来了包括蟹脚尖里的，丢我碗里前还不忘在佐料盘里蘸一下，并且只是稍微蘸，这样我就既能吃到加味的肉又能尝到鲜甜的原味…#好吃不过嫂子！#国外新出的doge面具，售价25美元一个，想象一下以后满大街尽是doge脸。。。。才知道章鱼是喜欢吃龙虾的，这么便宜的食材吃这么贵的食材，表脸！！！#知道真相的我眼泪掉下来！#当我出门去踢球的时候……我认识一个壕，优雅而奢华，哪怕动手术都要求麻醉师注射82年的吗啡。#动完手术还醉着呢！#都进来考验智商了，求这题的答案，注意图！ 答出来的都是学霸。。。今天的冷兔槽就到这里啦！觉得自己突然化身学霸的童鞋赞咯~','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:19','type':'幽默'},{'id':'41076526','uid':'174','messageId':'222206411','commentId':null,'orderNum':'2','oriAuthor':null,'originalFlag':'0','title':'哈喽，你们找谁啊，为什么在我家门口~','summary':'萌cry了！！！！！','site':'微信','author':'冷兔','publicTime':'2015-07-21 20:04:46','clicksCount':'251418','likeCount':'3763','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222206408&idx=3&sn=15d9f5c24aac01742c14b8e3a0f33b33&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic20PZxz64G3gqZibcSbFfqn0bWchP0EePgDmOT5OdpsKG1iaicrUOSAM8mQ/0?wx_fmt=jpeg','downloadStatus':'1','content':'萌cry了！！！！！','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:18','type':'幽默'},{'id':'41076530','uid':'174','messageId':'222206411','commentId':null,'orderNum':'3','oriAuthor':null,'originalFlag':'0','title':'何弃疗！女人们怎么都得了这种怪病？','summary':'女人们都爱抱怨，长期做家务，洗碗洗衣服神马的，手都变粗了！最近网络上就流传着一则小夫妻“不洗碗砸宝马”的视频','site':'微信','author':'冷兔','publicTime':'2015-07-21 20:04:46','clicksCount':'227632','likeCount':'1067','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222206408&idx=4&sn=6a73b2bb6289ca9cc1acf01887d16917&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2AEtPMleo8CIW6SKhu1UHtKsQZHAkOYiblUJ4Qhubmzxsq17apExsFVg/0?wx_fmt=jpeg','downloadStatus':'31','content':'女人们都爱抱怨，长期做家务，洗碗洗衣服神马的，手都变粗了！最近网络上就流传着一则小夫妻“不洗碗砸宝马”的视频：老婆爱惜美手，据说还开了一家美甲店，属于“靠手吃饭”的女人。她因怕伤手拒绝承担家务，所有活儿都由老公承包。于是老公积怨成疾，得了“一洗碗就会抓狂”的病。一天又因洗碗和老婆开撕，一怒之下，将家里的碗从高空砸下，把自家宝马车都砸坏了！ 你造吗？女人们除了会因“怕伤手”拒绝洗碗之外，还会拿哪些丧心病狂的理由做借口呢！Top 1：一洗碗就叫妈 嫁给一个妈宝，别再幻想妻子会主动做家务。从小就过着衣来伸手饭来张口的日子，要想妻子分担家务活比登天还难，只因她得了一种“一洗碗就会叫妈”的病。Top 2：一洗碗就会手滑 家有一枚软妹纸，一做家务就伐开心，提到洗碗就说没力气！你一定是得了“一洗碗就会手滑”的病！洗一次碗，摔破五个，病得这么严重，你这样家里人知道吗？Top 3：一洗碗就想离家出走吓死宝宝了！老婆不喜欢做家务，有一次洗碗的时候，发现人不见了，结果老公在隔壁王叔叔家找到了她。她说她得了一种“一洗碗就想离家出走”的病。病情这么重，老公能受得了吗？Top 4：一洗碗就会变丑伦家可是小公举，怎么能做洗碗这种粗活？厨房里装了三面大镜子，时刻都要美美哒，总觉得洗碗洗着洗着就会变丑了！你造嘛，这绝对是得了一种“一洗碗就会变丑”的病！更有胜者，一对小夫妻不想洗碗怒砸宝马！（碗此刻的内心是崩溃的。。。。。）俗话说自己吃饭的碗，哭着也要把洗完！！那么问题来了，洗洁精哪家强？用立白，你懂得！宝马再也不担心你不洗碗啦！','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:18','type':'幽默'},{'id':'41076521','uid':'174','messageId':'222206411','commentId':null,'orderNum':'1','oriAuthor':null,'originalFlag':'0','title':'看看变身成功的潜力股吧 一胖毁所有啊毁所有。','summary':'请珍惜身边的胖子吧，因为你不知道谁瘦下来就变帅变美了！','site':'微信','author':'冷兔','publicTime':'2015-07-21 20:04:46','clicksCount':'301827','likeCount':'3573','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222206408&idx=2&sn=f2f1e221ebe11582d0b082bfa68144f2&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic27X8rhkwhF0wXzCCibw6hCSpNSIqbcEOzgdc5JW8cuk4Vpg7WlyLDs2A/0?wx_fmt=jpeg','downloadStatus':'1','content':'请珍惜身边的胖子吧，因为你不知道谁瘦下来就变帅变美了！','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:17','type':'幽默'},{'id':'41076539','uid':'174','messageId':'222195265','commentId':null,'orderNum':'2','oriAuthor':null,'originalFlag':'0','title':'熊孩子，见到朕还不跪下请安！！','summary':'快替朕试试草甜不甜！','site':'微信','author':'冷兔','publicTime':'2015-07-21 13:48:15','clicksCount':'306516','likeCount':'3642','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222195262&idx=3&sn=429832b8ef34243c2deccb2041ed3c62&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2EaJaHUicVRY7O6lzibjwqJIY0BTvGxhvTztepsSsnicMBXesAAT1YPWwA/0?wx_fmt=jpeg','downloadStatus':'1','content':'快替朕试试草甜不甜！','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:16','type':'幽默'},{'id':'41076533','uid':'174','messageId':'222195265','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔趣闻】世界上有个国家，叫纽埃，有点萌。。。','summary':'付款的时候说：去吧，皮卡丘，决定就是你了！内容来自@英国报姐','site':'微信','author':'冷兔','publicTime':'2015-07-21 13:48:15','clicksCount':'526594','likeCount':'7152','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222195262&idx=1&sn=32fe701f9aab0acf45a10010f726e32e&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2tnTguxCM56krlVeB1mdYkqCo5ybQ1GElWclaxQvCxYuLmerDsrNjYw/0?wx_fmt=jpeg','downloadStatus':'1','content':'付款的时候说：去吧，皮卡丘，决定就是你了！内容来自@英国报姐','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:19','type':'幽默'},{'id':'41076536','uid':'174','messageId':'222195265','commentId':null,'orderNum':'1','oriAuthor':null,'originalFlag':'0','title':'隔着屏幕能感觉到的演技！这一次我服辣！！','summary':'模仿了几次，发现自己办不到！！！','site':'微信','author':'冷兔','publicTime':'2015-07-21 13:48:15','clicksCount':'353606','likeCount':'4048','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222195262&idx=2&sn=9718380085f45e596d54bf12a5f45a94&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2oqCsz3McNW3oUXDEMwM8LCoEwLzQkEwciacrrQVJ8mOslxrGZblvicng/0?wx_fmt=jpeg','downloadStatus':'1','content':'模仿了几次，发现自己办不到！！！','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:16','type':'幽默'},{'id':'40846379','uid':'174','messageId':'222180516','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.863','summary':'冷兔槽，成功就是1%的灵感加99%的远离手机。','site':'微信','author':'冷兔','publicTime':'2015-07-20 19:48:03','clicksCount':'493726','likeCount':'10354','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222180513&idx=1&sn=f86e8ee61f4f405f8ab4ea4dce625853&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd2GaejFKic7WaUZMEBDVROjt5BVgvV4xTic9dibNfqfibsYAQj7hVtcHElH28zeicKByZAD1u9sIfK6P6A/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，成功就是1%的灵感加99%的远离手机。洗澡已经很累了，出了浴室一看，还有这么多霜要擦。#洗头已经很累了吗，居然还要吹！！#老版西游记最厉害的地方，真的是这样！！果然和人聊天不能太敷衍……刚刚室友讲到女朋友爱说梦话，我随口接了一句我知道，现在气氛变得十分微妙。#有杀气！#这他妈居然是个西瓜“年轻时特别想做一个能拯救世界的人。”“现在呢？”“现在只想每个月能多赚点钱。”#能不能淡淡的涨个工资？#哈哈哈哪是喷泉，流口水吧学生党的夏天才叫夏天，漫长得让人失去耐性的暑假，蝉声里追偶像剧，空调房里吃西瓜，跟喜欢的男孩子约去图书馆里自修，然后趴在桌子上睡着。从泳池里爬起，一身漂白粉的味道地回家去，猜今晚大概是吃盐水毛豆。时光拖得跟树荫一样深远。而大人的七八月，只能叫“天很热的那些日子”。#老板我想去上学！#周日，一颗值连城的小行星飞过地球附近，距离比最近的行星要近30倍。这颗小行星编号2011 UW-158，直径不到一公里，但它可能储存有约1亿吨的白金，据估算价值超过5.4万亿美元。小行星采矿公司正在研究，是否在不远的将来可以前往采矿。夜袭儿童医院，检查得知没大碍后整个人都轻松了，和媳妇有说有笑地就从诊室里走了出来。身后传来医生冷酷无比的声音：“把孩子带走。”#此刻孩子的内心是崩溃的！#有打算养小乌龟当宠物的同学，这个新技能一定要get刚才东京台的节目讲了一个很鸡汤的故事：一个日本人去爱迪生那里应聘助手，爱迪生要考验他，就让他一个人去他书房，书房的桌子上放了一堆金银首饰。那个日本人看了这堆值钱货后忍了又忍，终于没克制住，还是下了手，把这些首饰按种类和大小排放得整整齐齐，最后被爱迪生录用了。#强迫症的用处。。。#感觉给奥特曼手办穿上衣服后整个都变的美美哒了，有种让人把持不住的冲动！你可以反驳我的观点，但你不能否定这个观点客观存在的事实，例如，我就是比你长得好看。#别否定！#大家一起来找茬，据说能找到6处以上不同的人是天才。“你什么时候最文静？” “开学遇老师，操场遇男神，家中遇亲戚，路遇陌生人，其他时候都像脱缰了的哈士奇。”#是我！！#你是什么。。。。今天的冷兔槽就到这里啦！很准时！快赞我！','insertTime':'2015-07-21 01:00:30','updateTime':'2015-07-21 12:00:20','type':'幽默'},{'id':'40846380','uid':'174','messageId':'222180516','commentId':null,'orderNum':'1','oriAuthor':null,'originalFlag':'0','title':'除了学霸之外，学渣的世界也同样无人能懂……','summary':'这都不是一般的学渣，是高级学渣！','site':'微信','author':'冷兔','publicTime':'2015-07-20 19:48:03','clicksCount':'290923','likeCount':'4114','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222180513&idx=2&sn=62f6b45683fdd0169602899f3c66d77e&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd2GaejFKic7WaUZMEBDVROjt1gaahhlh5p8dkgjvNwIELGk9SGHVocj6nRUDU9mmBGvHQr2YgosAVA/0?wx_fmt=jpeg','downloadStatus':'1','content':'这都不是一般的学渣，是高级学渣！','insertTime':'2015-07-21 01:00:30','updateTime':'2015-07-21 12:00:20','type':'幽默'},{'id':'40846381','uid':'174','messageId':'222180516','commentId':null,'orderNum':'2','oriAuthor':null,'originalFlag':'0','title':'网友等红绿灯时，看到一个狗狗正坐在摩托车回家。。。。','summary':'狗狗：卧槽要掉下去了！卧槽！！卧槽！！尼玛我要掉了！停车！卧槽！停车！卧槽卧槽！','site':'微信','author':'冷兔','publicTime':'2015-07-20 19:48:03','clicksCount':'248043','likeCount':'4086','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222180513&idx=3&sn=fed38c6f6b05e708ca7197d1fb34244e&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd2GaejFKic7WaUZMEBDVROjtb2EVrhamK2w4cZMAO3DCFiciblEysrABstZEicswWJ73v0statX2jqEQA/0?wx_fmt=jpeg','downloadStatus':'1','content':'狗狗：卧槽要掉下去了！卧槽！！卧槽！！尼玛我要掉了！停车！卧槽！停车！卧槽卧槽！','insertTime':'2015-07-21 01:00:30','updateTime':'2015-07-21 12:00:20','type':'幽默'}],'topArticle':[{'id':'40846379','uid':'174','messageId':'222180516','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.863','summary':'冷兔槽，成功就是1%的灵感加99%的远离手机。','site':'微信','author':'冷兔','publicTime':'2015-07-20 19:48:03','clicksCount':'493726','likeCount':'10354','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222180513&idx=1&sn=f86e8ee61f4f405f8ab4ea4dce625853&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd2GaejFKic7WaUZMEBDVROjt5BVgvV4xTic9dibNfqfibsYAQj7hVtcHElH28zeicKByZAD1u9sIfK6P6A/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，成功就是1%的灵感加99%的远离手机。洗澡已经很累了，出了浴室一看，还有这么多霜要擦。#洗头已经很累了吗，居然还要吹！！#老版西游记最厉害的地方，真的是这样！！果然和人聊天不能太敷衍……刚刚室友讲到女朋友爱说梦话，我随口接了一句我知道，现在气氛变得十分微妙。#有杀气！#这他妈居然是个西瓜“年轻时特别想做一个能拯救世界的人。”“现在呢？”“现在只想每个月能多赚点钱。”#能不能淡淡的涨个工资？#哈哈哈哪是喷泉，流口水吧学生党的夏天才叫夏天，漫长得让人失去耐性的暑假，蝉声里追偶像剧，空调房里吃西瓜，跟喜欢的男孩子约去图书馆里自修，然后趴在桌子上睡着。从泳池里爬起，一身漂白粉的味道地回家去，猜今晚大概是吃盐水毛豆。时光拖得跟树荫一样深远。而大人的七八月，只能叫“天很热的那些日子”。#老板我想去上学！#周日，一颗值连城的小行星飞过地球附近，距离比最近的行星要近30倍。这颗小行星编号2011 UW-158，直径不到一公里，但它可能储存有约1亿吨的白金，据估算价值超过5.4万亿美元。小行星采矿公司正在研究，是否在不远的将来可以前往采矿。夜袭儿童医院，检查得知没大碍后整个人都轻松了，和媳妇有说有笑地就从诊室里走了出来。身后传来医生冷酷无比的声音：“把孩子带走。”#此刻孩子的内心是崩溃的！#有打算养小乌龟当宠物的同学，这个新技能一定要get刚才东京台的节目讲了一个很鸡汤的故事：一个日本人去爱迪生那里应聘助手，爱迪生要考验他，就让他一个人去他书房，书房的桌子上放了一堆金银首饰。那个日本人看了这堆值钱货后忍了又忍，终于没克制住，还是下了手，把这些首饰按种类和大小排放得整整齐齐，最后被爱迪生录用了。#强迫症的用处。。。#感觉给奥特曼手办穿上衣服后整个都变的美美哒了，有种让人把持不住的冲动！你可以反驳我的观点，但你不能否定这个观点客观存在的事实，例如，我就是比你长得好看。#别否定！#大家一起来找茬，据说能找到6处以上不同的人是天才。“你什么时候最文静？” “开学遇老师，操场遇男神，家中遇亲戚，路遇陌生人，其他时候都像脱缰了的哈士奇。”#是我！！#你是什么。。。。今天的冷兔槽就到这里啦！很准时！快赞我！','insertTime':'2015-07-21 01:00:30','updateTime':'2015-07-21 12:00:20','type':'幽默'},{'id':'40670178','uid':'174','messageId':'222160630','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.862','summary':'冷兔槽，真的好想知道到底是谁想出来的，管哪吒叫藕霸……我这人挺害羞的，不信你亲我一下，我害羞给你看。＃骗吻新','site':'微信','author':'冷兔','publicTime':'2015-07-19 23:13:45','clicksCount':'681178','likeCount':'8055','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222160625&idx=1&sn=41345e042cffff1e44ba7ac261d09f06&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd3byDAVJMUr1cOcYvU4Qj7wic7yUiaqmGM4w4tMmSibwm4kibvaMerayHakibKvGyqXEJ4CrVCOwWuUBzQ/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，真的好想知道到底是谁想出来的，管哪吒叫藕霸……我这人挺害羞的，不信你亲我一下，我害羞给你看。＃骗吻新技能＃刚刚看到的一张图。。哈哈哈你们城里人真会玩儿我这人不太会说话，要是说话得罪了你，说明你这人真是小心眼。＃从未见过如此厚颜无耻之人＃饲主Akuro家的金毛一胎生下11只！现在已经两周大了，主人把它们按照颜色深浅排成一排。。萌哭早上在酒店醒来，身边躺着一个不认识的女人。我数了数钱包里的钱，掏出四百放在床头，因为我只收六百多一分不要。＃并没有猜到这结局＃要被这样的玩法美哭了不知道大家对在电梯里放屁的人是什么态度。反正我是绝对不能忍！我该放就放！＃好的！那我们一定该揍就揍＃说话这么犀利一定是因为单身吧。。果然和人聊天不能太敷衍……刚刚室友讲到女朋友爱说梦话，我随口接了一句我知道，现在气氛变得十分微妙。＃敷衍：怪我喽？＃据说这才是桃子的标准剥法“你今年命里吃喝不愁，就算不用工作也会过的很舒适，不过不能出门旅游，只能宅在家里，可以晒太阳，睡觉什么的，但是在年底会有一次大的血光之灾！”“然。。。然后呢？”猪担心的问。＃然后你就回去人类的肠胃来一次说走就走的旅行＃每个和汪星人相亲相爱的视频或帖子下面，可能都有猫奴们的辛酸泪一天二货好友兴奋的对我说：“我会识别假币了。”我不屑一顾：“就你那智商，你怎么识别的？”他说：“你要是给我一张3元的，我就知道肯定是假币。”＃世界上还有一种币，叫二币＃twi主@masa0410money 拍到的彩虹桥奇迹的拼接，分享好运气！“啊～五环，你比四环多一环，啊～五环，你比六环少一环～”不好意思啦小伙伴们，今天兔子我被堵在了北京的五环外，比以往来的有点晚，那今天咱就来说说你堵车时你最想做的事儿是什么？','insertTime':'2015-07-20 01:00:30','updateTime':'2015-07-22 13:02:26','type':'幽默'},{'id':'40031082','uid':'174','messageId':'222071301','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.859','summary':'冷兔槽，传说中的七仙女：腐女，宅女，腹黑女，路痴女，剩女，败金女，花痴女！','site':'微信','author':'冷兔','publicTime':'2015-07-16 19:41:52','clicksCount':'680639','likeCount':'7734','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222071297&idx=1&sn=40b1f9682822581c21b816339efbd902&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1cPQO2eOjsKkFGe9XcxlureW0LuyiaPghic8VENfDzxlSZSUWmvNGELW4EOou8Vibaj5DsgCydvYYVw/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，传说中的七仙女：腐女，宅女，腹黑女，路痴女，剩女，败金女，花痴女！如果你跟一个妹子约好了，到了日子妹子又突然爽约，并表示起码要一周后再约，不用想太多，她可能只是刚刚剪了刘海。#涨姿势了！#战斗民族的锻炼日常，看的我心惊胆颤第一次坐飞机，心情特别的激动和紧张，着实费了好大功夫才把安全带系好，边上的大姐似乎看出了我的尴尬，热心地说：“小伙子，你要玩就赶紧投币，我家宝宝还等着上呢！”#对不起，我没猜到结局！#论美照是怎么拍成的。。。“男票是近视眼，一直没配眼镜，说他戴眼镜不好看，于是我给他配了一副隐形眼镜。。。他整个世界顿时明亮了起来，然后看了我一眼淡定的说：“分手吧””“然后他又照了下镜子，哭着喊着求你不要走”#很快就认清自己！#从没见过如此强大的松鼠今天我去一饭店吃饭，点了一道红烧肉，结果发现怎么咬都咬不动，我就把服务员叫来说你们这是坑我的吧，这肉怎么咬都咬不动，把你们经理叫来。结果服务员居然告诉我，叫我们经理干啥啊，你都咬不动，他能咬得动啊！ #好像有道理诶#技术贴，不是一家人不进一家门。产房外杰伦焦急等待。过了好久听见婴儿一声啼哭，护士高兴的喊「小公主！小公主！」杰伦大喜过望「我在！我在！是男孩还是女孩？」#杰伦小公举！#一只蜥蜴吊在笼子顶上，女朋友在它怀里睡着了，于是他就一直这样，抱着女朋友，一动不动地挂着……看看别人的男朋友……在医院挂水，在没有WiFi的情况下我发现了一个小秘密，一瓶250毫升的水，竟然要滴3566滴...#惊天大秘密！#会场发现一只超棒的黑长直少女，然而是个逗比今天终于出了口气 ，正在卫生间洗衣服的时候媳妇进来要帮忙 ，我大声吼道：“这是你该来的地方吗 ？给我滚回客厅看电视，我去给你拿薯片。”媳妇灰溜溜的去了客厅。#这霸气！没谁了。。。#这要是下五子棋的话就赢了某人酷爱钓鱼，可总是一条也钓不到，这天，他还是没钓到，他收了鱼竿，郁闷的回家了。路过附近菜市场时，他对卖鱼的老板说：“给我来两条鲶鱼，要个头大点的。”老板一看是他，便道：“你还是买鲫鱼吧。”他疑惑的问：“为什么?”老板：“你妻子早上过来说，今天让你买鲫鱼，她想炖汤喝……”#她知道的太多了！# 六种常见课桌桌面类型……你是哪一种？今天的冷兔槽就到这里啦！今天评论里请用一句话假装你是生意人。','insertTime':'2015-07-17 01:00:45','updateTime':'2015-07-19 13:00:58','type':'幽默'},{'id':'40502748','uid':'174','messageId':'222114520','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.861','summary':'冷兔槽，看完一部古装片，狗说：全程无尿点！一根电线杆子都没有！','site':'微信','author':'冷兔','publicTime':'2015-07-18 21:13:23','clicksCount':'648770','likeCount':'7429','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222114517&idx=1&sn=0e2796b32adb2831b2352b3f95c7e5fa&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd0n0ncfrfSz7uV3HxbAtxHOdFUejNknorgfOlQzqEuNegMtYtiaEmHeTP58Sl56JKl0mHaxgWcSn9g/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，看完一部古装片，狗说：全程无尿点！一根电线杆子都没有！“我选汪峰老师！” “哈哈哈告诉大家为什么要选我？” “因为我觉得自己配不上杰伦！”#汪峰：“你滚！”#群里有个妹子在漫展买了个抱枕，他想找店长想聊聊人生，这已经不是商业欺诈这么简单了早上约了几个朋友打牌，女朋友不让去。我也没多说什么，在她脸上轻轻地摸了一下，便走了出去。直到现在才回到家，不等她开口，我先说：“宝宝，你皮肤弹性也太好了吧！早上摸了一下就被弹出去好远，差点迷路回不来了都。”#感觉get到了一个了不得的技能！#不是我想熬夜，而是……有人嘲笑平胸妹子的胸不能藏东西，其实也是可以的。只是人家有胸的藏在沟里，我们没胸的藏在杯里。穿件大几码的内衣，指不定谁藏的多呢。#竟然无言以对！#得亏她没点夫妻肺片虎皮青椒龙抄手跟朋友聊天回想起WOW时候的趣事，给我印象最深的就是打JJC，3v3的时候对面ID可折磨死我们了，战士叫打那个小德，小德叫打那个萨满，萨满叫打那个战士；5v5的时候对面更混乱，遇到5个法师，ID分别叫：这个法师，那个法师，哪个法师，就那个法师，到底哪个法师。当时我们语音都快喊爆炸了。。。。。。#不会玩WOW都看笑了哈哈哈哈！#哈士奇它484傻很久没运动了，明显感觉体力下降。这不，在厕所蹲半个小时就有点脚麻了，这半个小时工资真难混！#这年头不好混啊！#‘喝酒认怂书’哈哈哈哈有人说我脾气差，真是笑死了。我长这么好看脾气好还得了？#我脾气就是不好怎么滴？#我永远忘不了中国首富马云跟我说的第一句话……今天的冷兔槽就到这里啦！来评论里推荐一首自己最喜欢的一首歌，明天放出来给大家一起听！','insertTime':'2015-07-19 01:00:44','updateTime':'2015-07-21 12:52:45','type':'幽默'},{'id':'41076533','uid':'174','messageId':'222195265','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔趣闻】世界上有个国家，叫纽埃，有点萌。。。','summary':'付款的时候说：去吧，皮卡丘，决定就是你了！内容来自@英国报姐','site':'微信','author':'冷兔','publicTime':'2015-07-21 13:48:15','clicksCount':'526594','likeCount':'7152','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222195262&idx=1&sn=32fe701f9aab0acf45a10010f726e32e&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2tnTguxCM56krlVeB1mdYkqCo5ybQ1GElWclaxQvCxYuLmerDsrNjYw/0?wx_fmt=jpeg','downloadStatus':'1','content':'付款的时候说：去吧，皮卡丘，决定就是你了！内容来自@英国报姐','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:19','type':'幽默'},{'id':'40502750','uid':'174','messageId':'222114520','commentId':null,'orderNum':'2','oriAuthor':null,'originalFlag':'0','title':'小短腿儿的反击。。。','summary':'好心疼。。。','site':'微信','author':'冷兔','publicTime':'2015-07-18 21:13:23','clicksCount':'404418','likeCount':'6882','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222114517&idx=3&sn=280736f0684677a3df91584e6e3ae1a8&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd0n0ncfrfSz7uV3HxbAtxHOgicdIjHO6KNY1fXS9I7eeMSHtNY7o6icDr9smGQIaxibcEEARcbW6AiazQ/0?wx_fmt=jpeg','downloadStatus':'1','content':'好心疼。。。','insertTime':'2015-07-19 01:00:44','updateTime':'2015-07-21 12:52:45','type':'幽默'},{'id':'41076515','uid':'174','messageId':'222206411','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔•槽】每日一冷NO.864','summary':'冷兔槽，谁想出来的，管哪吒叫藕霸………','site':'微信','author':'冷兔','publicTime':'2015-07-21 20:04:46','clicksCount':'489148','likeCount':'6771','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222206408&idx=1&sn=f4359ec858aa38297b2cc0bfb8a4a2d1&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd1BdtQ0RXnbEg57O0HT0oic2fiaKn1jL5Xg9InHOreQTU2lb535M6Cp0bg3EJlRRSicsH0y2NE3mec9A/0?wx_fmt=jpeg','downloadStatus':'1','content':'冷兔槽，谁想出来的，管哪吒叫藕霸………懒居然可以战胜饥饿....#无法反驳。。。。#哈哈哈哈司令说得对一个女性朋友和直男聊天，她给发过去一句：“今晚的月色真好，本少女都忍不住想要变身了呢！”对方过了一会回复说：“没想到...你竟然是个狼人。”#并没有什么不对啊！#最近总有人夸我漂亮……当有人讨厌你的时候，不如反思一下自己，是不是性感可爱的你又完美得让人嫉妒了？#吾日三省吾身#车主在山东济南街头看到拍下来的。狗狗站在三轮车顶上，像风一样的男子。看电视的时候不停在嗑瓜子，女朋友一脸嫌弃地说：“你这么喜欢吃瓜子，直接买瓜子仁得了。”我摇摇头说：“那可不行，我享受的是嗑的过程。”她听完一脸兴奋地坐起来说：“等的就是你这句话，那你把瓜子仁嗑出来给我，你享受嗑的过程就好了！”#智商压制！#和朋友踢点球大战时手柄的必备道具时间让我明白了，除了公交车、快递和大姨妈值得让我去等，其他啥都等不到！#谢谢时间！#哈哈哈老板做指甲吗？问表嫂怎么决定嫁给表哥的，她说你表哥别的不行却有一手出神入化的剥壳技术啊！螃蟹虾姑小龙虾都能又快又清楚地去壳，能出来的肉全出来了包括蟹脚尖里的，丢我碗里前还不忘在佐料盘里蘸一下，并且只是稍微蘸，这样我就既能吃到加味的肉又能尝到鲜甜的原味…#好吃不过嫂子！#国外新出的doge面具，售价25美元一个，想象一下以后满大街尽是doge脸。。。。才知道章鱼是喜欢吃龙虾的，这么便宜的食材吃这么贵的食材，表脸！！！#知道真相的我眼泪掉下来！#当我出门去踢球的时候……我认识一个壕，优雅而奢华，哪怕动手术都要求麻醉师注射82年的吗啡。#动完手术还醉着呢！#都进来考验智商了，求这题的答案，注意图！ 答出来的都是学霸。。。今天的冷兔槽就到这里啦！觉得自己突然化身学霸的童鞋赞咯~','insertTime':'2015-07-22 01:00:38','updateTime':'2015-07-22 12:00:19','type':'幽默'},{'id':'40846382','uid':'174','messageId':'222172641','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔趣闻】最近大家又发现了另一个专业抢镜的电灯泡！','summary':'有一对兄弟从小感情很好，有一天，哥哥找了女朋友，然后就又变成了“三人行，必有灯泡”系列。','site':'微信','author':'冷兔','publicTime':'2015-07-20 13:22:31','clicksCount':'451244','likeCount':'6497','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222172638&idx=1&sn=5e99fee329ccef2aa328558645f6fd38&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd2GaejFKic7WaUZMEBDVROjt5Fd8SwTibXjQ5B9AvjhHvG6xKzvymJ65Nibiaz6b693LkzibrZ2slo8kpQ/0?wx_fmt=jpeg','downloadStatus':'1','content':'有一对兄弟从小感情很好，有一天，哥哥找了女朋友，然后就又变成了“三人行，必有灯泡”系列。已被灯泡亮瞎双眼。。。。','insertTime':'2015-07-21 01:00:30','updateTime':'2015-07-21 12:00:20','type':'幽默'},{'id':'40670179','uid':'174','messageId':'222160630','commentId':null,'orderNum':'1','oriAuthor':null,'originalFlag':'0','title':'什么叫输在了起跑线上，你们来感受一下～','summary':'Ins上两个小正太，他们的爸妈特别喜欢打扮他们，并且经常会让他们cos一些明星和模特们的穿着，现在这俩个小家','site':'微信','author':'冷兔','publicTime':'2015-07-19 23:13:45','clicksCount':'465112','likeCount':'6477','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222160625&idx=2&sn=8ccd60918b5b748fb6000627457fa11a&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd3byDAVJMUr1cOcYvU4Qj7whjkG07ftJulNd0JdZGN0vcA9FG5gGJCe7aabh7Wdwz6esFXjC7Cmmw/0?wx_fmt=jpeg','downloadStatus':'1','content':'Ins上两个小正太，他们的爸妈特别喜欢打扮他们，并且经常会让他们cos一些明星和模特们的穿着，现在这俩个小家伙还建了一个网站，已经开始教大家如何搭配衣服了他们幸福并且潮范儿十足的一家输在了起跑线上！','insertTime':'2015-07-20 01:00:30','updateTime':'2015-07-22 13:02:26','type':'幽默'},{'id':'40264486','uid':'174','messageId':'222083528','commentId':null,'orderNum':'0','oriAuthor':null,'originalFlag':'0','title':'【冷兔趣闻】抢劫犯被逮捕后说自己“专挑动漫宅下手，因为看起来很弱”然后。。。。','summary':'然后推特的二次元分分po上自拍：有种来抢我啊。你们感受下有种淡淡的反差萌。。。。','site':'微信','author':'冷兔','publicTime':'2015-07-17 12:58:31','clicksCount':'455735','likeCount':'5840','commentsCount':'0','url':'http://mp.weixin.qq.com/s?__biz=MTgwNTE3Mjg2MA==&mid=222083525&idx=1&sn=115026ea5bc4705f64cc8ceb08f2a95c&scene=4','imageUrl':'http://mmbiz.qpic.cn/mmbiz/GsuL1FRLKd05eE2gSQJAry3Q9IwwiafXWQoQMdnXev3pcDO7pfY9PCjyYglQeTFk737YW9A7obg8LEfNba4pP6Q/0?wx_fmt=jpeg','downloadStatus':'1','content':'然后推特的二次元分分po上自拍：有种来抢我啊。你们感受下有种淡淡的反差萌。。。。','insertTime':'2015-07-18 01:00:32','updateTime':'2015-07-18 12:01:23','type':'幽默'}]}}

//alert(aa['value']['lastestArticle'].length);
// 加载入口模块
//seajs.use("./static/js/detail_new")

//testajax();

queryGroup();
//queryGroupCats();
//queryGroupGoods(3);
//addOrder();