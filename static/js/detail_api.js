
define(function (require) {

    //var $ = require('jquery');
    //require('cookie');

    $.ajaxSetup({
        type: "POST",
        dataType: 'JSONP'
    });

    var appBase = "/xdnphb";
    var urlBase = appBase + "/";
    var appDomain = 'newrank.cn';

    var getDataSync = function(url, options, async, success, fail) {
        $.ajax({
            url: "http://www.newrank.cn"+url + ".json",
            async : async,
            data: options,
            success: function(data) {
                if (data.success) {
                    success(data.value);
                } else {
                    commonFail(data.value);
                }
            },
            error: function(data) {
                if ($.isFunction(fail)) {
                    fail(data);
                }
               /* if ($.isFunction(networkFail)) {
                    networkFail(data);
                }*/
            }
        });
    };

    var getLoginData = function(url, options, success, fail) {
        $.ajax({
            url: url + ".json",
            data: options,
            success: function(data) {
                if (data.success) {
                    if (data.value == -999) {
                        if (window.location.href.indexOf('/account/') > 0) {
                            window.location.href = '../../public/login/login.html';
                        } else {
                            success(data.value);
                        }
                    } else {
                        success(data.value);
                    }
                }
            },
            error: function(data) {
                if ($.isFunction(fail)) {
                    fail(data);
                }
                if ($.isFunction(networkFail)) {
//                networkFail(data);
                }
            }
        });
    };

    var getWeixinData = function(url,options,success,fail) {
        $.ajax({
            url: url + ".json",
            data: options,
            success: function(data) {
                if (data.success) {
                    if (data.value == -999) {
                        showTip('该账号已被锁定!')
                    } else if (data.value == -1000) {
                        showTip('无权限');
                    } else {
                        success(data.value);
                    }
                }
            },
            error: function(data) {
                if ($.isFunction(fail)) {
                    fail(data);
                }
                if ($.isFunction(networkFail)) {
//                networkFail(data);
                }
            }
        });
    };


    var getData = function(url, options, success, fail){
        getDataSync(url, options, true, success, fail);
    };

    var getJsonpSync = function(url, options, async, success, fail) {
        $.ajax({
            url: url + ".json",
            async : async,
            dataType:"jsonp",
            jsonp:"callback",
            async : async,
            data: options,
            success: function(data) {
                if (data.success) {
                    success(data.value);
                }
            }
        });
    };

    var getJsonp = function(url, options, success, fail) {
        getJsonpSync(url, options, true, success, fail);
    };

    var remote = (function(){
        var baseUrl = urlBase;

        var account = (function(){
            var typeUrl = baseUrl + "account/type/"
            return {
                type:{
                    isLocked:function(id, success){
                        getData(typeUrl + "isLocked",{"weixin_user_id":id}, success);
                    }
                }
            }
        }());

        var user = (function(){
            var user_url = baseUrl + "sys/account/"
            var box_url = baseUrl +"sys/account/box/";

            return {
                getUser: function(success){
                    getData(user_url+ "get",{},success);
                },
                info: {
                    getUser: function(success){
                        getLoginData(user_url+ "getFull",{},success);
                    }
                },
                box: {
                    searchBox:function(success){
                        getLoginData(box_url + "search", {}, success);
                    },
                    postIds:function(ids, success){
                        getLoginData(box_url + "postIds", {'ids': ids}, success);
                    },
                    delBox:function(ids, success){
                        getLoginData(box_url + "delete", {"ids": ids}, success);
                    }
                }
            };
        }());


        var data = (function(){
            var dataUrl = baseUrl + "data/";
            return {
                mail: function(name, email, title, content,sendToMe, success) {
                    getData(dataUrl + "mail", {name: name, email: email, "title": title, "content": content, "sendToMe": sendToMe}, success);
                },

                searchUser: function(value, success) {
                    getData(dataUrl + "search", {"name": value}, success);
                },
                getTopUser: function(date, success) {
                    getData(dataUrl + "top", {"date": date}, success);
                },
                getUser: function(uid, success) {
                    getData(dataUrl + "get", {"uid": uid}, success);
                },
                getUserDetail: function(uid, date, success) {
                    getData(dataUrl + "getDetail", {"uid": uid, "date": date}, success);
                },
                getTrend: function(uid, success) {
                    getData(dataUrl + "trend", {"uid": uid}, success);
                },
                getTrendByAccount: function(account, success) {
                    getData(dataUrl + "trendByAccount", {"account": account}, success);
                },
                getTopArticles: function(uid, success) {
                    getData(dataUrl + "topArticles", {"uid": uid}, success);
                },
                getTimeArticles: function(uid, success) {
                    getData(dataUrl + "timeArticles", {"uid": uid}, success);
                },
                getTypeRankArticles: function(type, date, success) {
                    getData(dataUrl + "typeRankArticles", {"type": type, "date": date}, success);
                },
                addReport: function(report, success) {
                    getData(dataUrl + "addReport", {"report": JSON.stringify(report)}, success);
                },
                getAllReports: function(page,  success) {
                    getData(dataUrl + "getReports", {"page": page, 'status': 0}, success);
                },
                getPublishReports: function(page,  success) {
                    getData(dataUrl + "getReports", {"page": page, 'status': 1}, success);
                },
                publicReport: function(id, success) {
                    getData(dataUrl + "publicReport", {"id": id}, success);
                },
                removeReport: function(id, success) {
                    getData(dataUrl + "removeReport", {"id": id}, success);
                },
                getUserByAccountAndInitPage : function(account, success) {
                    getData(dataUrl + "getUserByAccountAndInitPage", {"account": account}, success)
                },
                getUserDetailByAccount: function(account, date, success) {
                    getData(dataUrl + "getDetailByAccount", {"account": account, "date": date}, success);
                },
                getTrendByColumnName: function(uid, columnName, success) {
                    getData(dataUrl + "getTrendByColumnName", {"uid": uid, "columnName" : columnName}, success);
                },
                getAccountLastestArticle : function(uid,size, success) {
                    getData(dataUrl + "getAccountLastestArticle", {"uid": uid, "size" : size}, success);
                },
                getAccountTopArticle : function(uid,size, success) {
                    getData(dataUrl + "getAccountTopArticle", {"uid": uid, "size" : size}, success);
                },
                getAccountKeyword : function(uid,keyword,size, success) {
                    getData(dataUrl + "getAccountKeyword", {"uid": uid, "keyword" : keyword, "size" : size}, success);
                },
                getAccountArticle : function(uid,size, success) {
                    getData(dataUrl + "getAccountArticle", {"uid": uid, "size" : size}, success);
                },
                isAdGroupUserOrBindAccount : function(uuid, success) {
                    getData(dataUrl + "isAdGroupUserOrBindAccount", {account_id: uuid}, success);
                },
                isAdGroupUserAndIsNeedBindAccount : function(uuid, success) {
                    getData(dataUrl + "isAdGroupUserAndIsNeedBindAccount", {account_id: uuid}, success);
                },
                isLogin : function(success) {
                    getDataSync(dataUrl + "isLogin", {}, false, success);
                },
                getAdValueData : function(account, success) {
                    getData(dataUrl + "getAdValueData", {account: account}, success);
                }

            };
        }());

        return {
            account : account,
            user: user,
            data: data
        };
    }());

    return {
        remote: remote
    }
});
