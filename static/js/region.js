var data_region=[
[2,'北京市'],[3,'安徽省'],[4,'福建省'],[5,'甘肃省'],[6,'广东省'],[7,'广西省'],[8,'贵州省'],[9,'海南省'],[10,'河北省'],[11,'河南省'],[12,'黑龙江省'],[13,'湖北省'],[14,'湖南省'],[15,'吉林省'],[16,'江苏省'],[17,'江西省'],[18,'辽宁省'],[19,'内蒙古省'],[20,'宁夏省'],[21,'青海省'],[22,'山东省'],[23,'山西省'],[24,'陕西省'],[25,'上海市'],[26,'四川省'],[27,'天津市'],[28,'西藏省'],[29,'新疆省'],[30,'云南省'],[31,'浙江省'],[32,'重庆市'],];

var data_subregion={
'广东省':[[76,'广州市'],[77,'深圳市'],[78,'潮州市'],[79,'东莞市'],[80,'佛山市'],[81,'河源市'],[82,'惠州市'],[83,'江门市'],[84,'揭阳市'],[85,'茂名市'],[86,'梅州市'],[87,'清远市'],[88,'汕头市'],[89,'汕尾市'],[90,'韶关市'],[91,'阳江市'],[92,'云浮市'],[93,'湛江市'],[94,'肇庆市'],[95,'中山市'],[96,'珠海市'],],
'西藏省':[[344,'拉萨市'],[345,'阿里市'],[346,'昌都市'],[347,'林芝市'],[348,'那曲市'],[349,'日喀则市'],[350,'山南市'],],
'陕西省':[[311,'西安市'],[312,'安康市'],[313,'宝鸡市'],[314,'汉中市'],[315,'商洛市'],[316,'铜川市'],[317,'渭南市'],[318,'咸阳市'],[319,'延安市'],[320,'榆林市'],],
'河南省':[[149,'郑州市'],[150,'洛阳市'],[151,'开封市'],[152,'安阳市'],[153,'鹤壁市'],[154,'济源市'],[155,'焦作市'],[156,'南阳市'],[157,'平顶山市'],[158,'三门峡市'],[159,'商丘市'],[160,'新乡市'],[161,'信阳市'],[162,'许昌市'],[163,'周口市'],[164,'驻马店市'],[165,'漯河市'],[166,'濮阳市'],],
'江西省':[[233,'南昌市'],[234,'抚州市'],[235,'赣州市'],[236,'吉安市'],[237,'景德镇市'],[238,'九江市'],[239,'萍乡市'],[240,'上饶市'],[241,'新余市'],[242,'宜春市'],[243,'鹰潭市'],],
'海南省':[[120,'海口市'],[121,'三亚市'],[122,'白沙市'],[123,'保亭市'],[124,'昌江市'],[125,'澄迈县市'],[126,'定安县市'],[127,'东方市'],[128,'乐东市'],[129,'临高县市'],[130,'陵水市'],[131,'琼海市'],[132,'琼中市'],[133,'屯昌县市'],[134,'万宁市'],[135,'文昌市'],[136,'五指山市'],[137,'儋州市'],],
'上海市':[[321,'上海市'],],
'吉林省':[[211,'长春市'],[212,'吉林市'],[213,'白城市'],[214,'白山市'],[215,'辽源市'],[216,'四平市'],[217,'松原市'],[218,'通化市'],[219,'延边市'],],
'安徽省':[[36,'安庆市'],[37,'蚌埠市'],[38,'巢湖市'],[39,'池州市'],[40,'滁州市'],[41,'阜阳市'],[42,'淮北市'],[43,'淮南市'],[44,'黄山市'],[45,'六安市'],[46,'马鞍山市'],[47,'宿州市'],[48,'铜陵市'],[49,'芜湖市'],[50,'宣城市'],[51,'亳州市'],[3401,'合肥市'],],
'天津市':[[343,'天津市'],],
'河北省':[[138,'石家庄市'],[139,'保定市'],[140,'沧州市'],[141,'承德市'],[142,'邯郸市'],[143,'衡水市'],[144,'廊坊市'],[145,'秦皇岛市'],[146,'唐山市'],[147,'邢台市'],[148,'张家口市'],],
'重庆市':[[394,'重庆市'],],
'甘肃省':[[62,'兰州市'],[63,'白银市'],[64,'定西市'],[65,'甘南市'],[66,'嘉峪关市'],[67,'金昌市'],[68,'酒泉市'],[69,'临夏市'],[70,'陇南市'],[71,'平凉市'],[72,'庆阳市'],[73,'天水市'],[74,'武威市'],[75,'张掖市'],],
'山西省':[[300,'太原市'],[301,'长治市'],[302,'大同市'],[303,'晋城市'],[304,'晋中市'],[305,'临汾市'],[306,'吕梁市'],[307,'朔州市'],[308,'忻州市'],[309,'阳泉市'],[310,'运城市'],],
'江苏省':[[220,'南京市'],[221,'苏州市'],[222,'无锡市'],[223,'常州市'],[224,'淮安市'],[225,'连云港市'],[226,'南通市'],[227,'宿迁市'],[228,'泰州市'],[229,'徐州市'],[230,'盐城市'],[231,'扬州市'],[232,'镇江市'],],
'新疆省':[[351,'乌鲁木齐市'],[352,'阿克苏市'],[353,'阿拉尔市'],[354,'巴音郭楞市'],[355,'博尔塔拉市'],[356,'昌吉市'],[357,'哈密市'],[358,'和田市'],[359,'喀什市'],[360,'克拉玛依市'],[361,'克孜勒苏市'],[362,'石河子市'],[363,'图木舒克市'],[364,'吐鲁番市'],[365,'五家渠市'],[366,'伊犁市'],],
'四川省':[[322,'成都市'],[323,'绵阳市'],[324,'阿坝市'],[325,'巴中市'],[326,'达州市'],[327,'德阳市'],[328,'甘孜市'],[329,'广安市'],[330,'广元市'],[331,'乐山市'],[332,'凉山市'],[333,'眉山市'],[334,'南充市'],[335,'内江市'],[336,'攀枝花市'],[337,'遂宁市'],[338,'雅安市'],[339,'宜宾市'],[340,'资阳市'],[341,'自贡市'],[342,'泸州市'],],
'福建省':[[53,'福州市'],[54,'龙岩市'],[55,'南平市'],[56,'宁德市'],[57,'莆田市'],[58,'泉州市'],[59,'三明市'],[60,'厦门市'],[61,'漳州市'],],
'广西省':[[97,'南宁市'],[98,'桂林市'],[99,'百色市'],[100,'北海市'],[101,'崇左市'],[102,'防城港市'],[103,'贵港市'],[104,'河池市'],[105,'贺州市'],[106,'来宾市'],[107,'柳州市'],[108,'钦州市'],[109,'梧州市'],[110,'玉林市'],],
'湖南省':[[197,'长沙市'],[198,'张家界市'],[199,'常德市'],[200,'郴州市'],[201,'衡阳市'],[202,'怀化市'],[203,'娄底市'],[204,'邵阳市'],[205,'湘潭市'],[206,'湘西市'],[207,'益阳市'],[208,'永州市'],[209,'岳阳市'],[210,'株洲市'],],
'浙江省':[[383,'杭州市'],[384,'湖州市'],[385,'嘉兴市'],[386,'金华市'],[387,'丽水市'],[388,'宁波市'],[389,'绍兴市'],[390,'台州市'],[391,'温州市'],[392,'舟山市'],[393,'衢州市'],],
'贵州省':[[111,'贵阳市'],[112,'安顺市'],[113,'毕节市'],[114,'六盘水市'],[115,'黔东南市'],[116,'黔南市'],[117,'黔西南市'],[118,'铜仁市'],[119,'遵义市'],],
'辽宁省':[[244,'沈阳市'],[245,'大连市'],[246,'鞍山市'],[247,'本溪市'],[248,'朝阳市'],[249,'丹东市'],[250,'抚顺市'],[251,'阜新市'],[252,'葫芦岛市'],[253,'锦州市'],[254,'辽阳市'],[255,'盘锦市'],[256,'铁岭市'],[257,'营口市'],],
'内蒙古省':[[258,'呼和浩特市'],[259,'阿拉善盟市'],[260,'巴彦淖尔盟市'],[261,'包头市'],[262,'赤峰市'],[263,'鄂尔多斯市'],[264,'呼伦贝尔市'],[265,'通辽市'],[266,'乌海市'],[267,'乌兰察布市市'],[268,'锡林郭勒盟市'],[269,'兴安盟市'],],
'宁夏省':[[270,'银川市'],[271,'固原市'],[272,'石嘴山市'],[273,'吴忠市'],[274,'中卫市'],],
'山东省':[[283,'济南市'],[284,'青岛市'],[285,'滨州市'],[286,'德州市'],[287,'东营市'],[288,'菏泽市'],[289,'济宁市'],[290,'莱芜市'],[291,'聊城市'],[292,'临沂市'],[293,'日照市'],[294,'泰安市'],[295,'威海市'],[296,'潍坊市'],[297,'烟台市'],[298,'枣庄市'],[299,'淄博市'],],
'北京市':[[52,'北京市'],],
'湖北省':[[180,'武汉市'],[181,'仙桃市'],[182,'鄂州市'],[183,'黄冈市'],[184,'黄石市'],[185,'荆门市'],[186,'荆州市'],[187,'潜江市'],[188,'神农架林区市'],[189,'十堰市'],[190,'随州市'],[191,'天门市'],[192,'咸宁市'],[193,'襄樊市'],[194,'孝感市'],[195,'宜昌市'],[196,'恩施市'],],
'黑龙江省':[[167,'哈尔滨市'],[168,'大庆市'],[169,'大兴安岭市'],[170,'鹤岗市'],[171,'黑河市'],[172,'鸡西市'],[173,'佳木斯市'],[174,'牡丹江市'],[175,'七台河市'],[176,'齐齐哈尔市'],[177,'双鸭山市'],[178,'绥化市'],[179,'伊春市'],],
'云南省':[[367,'昆明市'],[368,'怒江市'],[369,'普洱市'],[370,'丽江市'],[371,'保山市'],[372,'楚雄市'],[373,'大理市'],[374,'德宏市'],[375,'迪庆市'],[376,'红河市'],[377,'临沧市'],[378,'曲靖市'],[379,'文山市'],[380,'西双版纳市'],[381,'玉溪市'],[382,'昭通市'],],
'青海省':[[275,'西宁市'],[276,'果洛市'],[277,'海北市'],[278,'海东市'],[279,'海南市'],[280,'海西市'],[281,'黄南市'],[282,'玉树市'],],
};

