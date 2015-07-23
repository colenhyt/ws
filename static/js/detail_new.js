/**
 * Created by xc on 15-5-22.
 */

define(function (require) {

    var api = require('./detail_api');
    
           var searchArticle = function() {
                api.remote.data.getAccountArticle("174", 10, function(datas) {

                    var topArticleArr = [], //最热
                        lastestArticleArr = []; //最新

                    $.each(datas.topArticle, function() {
                        topArticleArr.push(articleSearchHtml(this));
                        bindToarticleUl();
                    });

                    $.each(datas.lastestArticle, function() {
                        lastestArticleArr.push(articleSearchHtml(this));
                        bindToarticleUl();
                    });

                    $topArticle.append(topArticleArr.join(""));
                    $lastestArticle.append(lastestArticleArr.join(""));
                    bindToarticleUl();
                });
            };

            //初始化时加载——“最新”分类数据
            searchArticle();
});