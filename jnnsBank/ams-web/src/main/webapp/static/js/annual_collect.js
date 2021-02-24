/** annual.js  */
layui.define(['laytpl'], function(exports) {
    "use strict";

    var $ = layui.jquery,
        laytpl = layui.laytpl;

    var annual = {

        /**
         * 获取核心基本信息模板
         * @param id
         * @param element
         * @param title
         */
        loadCoreDataTemplate: function(id,title,templateHtml) {
            var _that = this;
            if(id){
                $.get('../../annualTask/core/'+id).done(function (response) {
                    var data = response.result;
                    if (data) {
                        if(data.accountStatus == 'normal') {
                            data.accountStatus = '正常'
                        }
                        _that.loadBasicTemplate(data,title,templateHtml);
                    }
                });
            }
        },
        /**
         * 获取人行基本信息模板
         * @param id
         * @param element
         * @param title
         */
        loadBankDataTemplate: function(id,title,templateHtml) {
            var _that = this;
            if(id){
                $.get('../../annualTask/pbc/'+id).done(function (response) {
                    var data = response.result;
                    if (data) {
                        _that.loadBasicTemplate(data,title,templateHtml);
                    }
                });
            }
        },
        /**
         * 获取工商基本信息模板
         * @param id
         * @param element
         * @param title
         */
        loadSaicDataTemplate: function(id,title,templateHtml) {
            var _that = this;
            if(id){
                $.get('../../annualTask/saic/'+id).done(function (response) {
                    var data = response.result;
                    if (data) {
                        _that.loadBasicTemplate(saicDataFormat(data),title,templateHtml);
                    }
                });
            }
        },
        loadBasicTemplate: function (data,title,templateHtml) {
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get(templateHtml, null, function (template) {
                if(template){
                    laytpl(template).render(data,function (html) {
                        //$(element).html(html);
                        layer.open({
                            area: ['800px', '500px'],
                            type: 1,
                            title: title || '信息',
                            content: html //这里content是一个普通的String
                        });
                    });
                }
            });
        },
        /**
         * 获取年检比对信息模板
         * @param id
         * @param element
         * @param title
         */
        loadCompareResult: function(id,title,templateHtml) {
            var _that = this;
            if(id){
                $.get('../../annualResult/compareResult/'+id).done(function (response) {
                    var data = response.result;
                    if (data) {
                        _that.loadCompareResultTemplate(data,title,templateHtml);
                    }
                });
            }
        },
        loadCompareResultTemplate: function (data,title,templateHtml) {
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get(templateHtml, null, function (template) {
                if(template){
                    laytpl(template).render(data,function (html) {
                        layer.open({
                            area: ['800px', '600px'],
                            type: 1,
                            title: title || '年检比对信息',
                            content: html //这里content是一个普通的String
                        });
                    });
                }
            });
        }
    };

    exports('annual_collect', annual);
});