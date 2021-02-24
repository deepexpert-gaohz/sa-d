/** common.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['layer'], function(exports) {
	"use strict";

	var $ = layui.jquery,
		layer = layui.layer;

	var common = {
		/**
		 * 抛出一个异常错误信息
		 * @param {String} msg
		 */
		throwError: function(msg) {
			throw new Error(msg);
			return;
		},
		/**
		 * 弹出一个错误提示
		 * @param {String} msg
		 */
		msgError: function(msg) {
			layer.msg(msg, {
				icon: 5
			});
			return;
		},

        /**
		 * 表格默认配置
         * @returns
         */
		tableDefaulOptions:function () {
            return {
                // Internationalisation. For more info refer to http://datatables.net/manual/i18n
                "language": {
                "sProcessing":   "处理中...",
                    "sLengthMenu":   "显示 _MENU_ 项结果",
                    "sZeroRecords":  "没有匹配结果",
                    "sInfo":         "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    "sInfoEmpty":    "显示第 0 至 0 项结果，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix":  "",
                    "sSearch":       "搜索:",
                    "sUrl":          "",
                    "sEmptyTable":     "表中数据为空",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands":  ",",
                    "oPaginate": {
                    "sFirst":    "首页",
                        "sPrevious": "上页",
                        "sNext":     "下页",
                        "sLast":     "末页"
                },
                "oAria": {
                    "sSortAscending":  ": 以升序排列此列",
                        "sSortDescending": ": 以降序排列此列"
                }
            },
                // setup responsive extension: http://datatables.net/extensions/responsive/
                responsive: true,
                autoWidth:false,
                "pagingType": "bootstrap_full_number",
                "lengthMenu": [
                    [5, 10, 15, 20, 100],
                    [5, 10, 15, 20, 100] // change per page values here
                ],
                // set the initial value
                "pageLength": 10,
                "dom": "<'row'<'col-md-6 col-sm-12'><'col-md-6 col-sm-12'>r><'table-scrollable't><'row'<'col-md-5" +
                " col-sm-12'il><'col-md-7 col-sm-12'p>>",
                "processing":true,
                "serverSide": true  //启用服务器端分页
            }
        },

        /**
         * jquery Datatable ajax 公共方法
         * @param url
         * @param param
         * @param callback
         * @param tableData
         * @param tips
         */
        jqueryDataTableAjax: function(url,param,callback,tableData,tips){
            //封装返回数据
            var returnData = {
                draw: 0,
                recordsTotal:0,
                recordsFiltered: 0,
                data: []
            };
            $.ajax({
                type: 'GET',
                url: url,
                cache: false,  //禁用缓存
                data: param,  //传入组装的参数
                dataType: "json",
                success: function (response) {
                    if(response.rel){
                        var result = response.result;
                        returnData.draw = tableData.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.total;//返回数据全部记录
                        returnData.recordsFiltered = result.total;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.rows;//返回的数据列表
                    } else {
                        tips.msg(response.msg)
                    }
                    //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                    //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                    callback(returnData);
                },
                error: function(){
                    tips.msg('系统异常');
                    callback(returnData);
                },
                complete: function() {
                }
            });
        },


        /**
         * jquery Datatable ajax 公共方法
         * @param url
         * @param param
         * @param callback
         * @param tableData
         * @param tips
         */
        jqueryDataTableAjaxByAck: function(url,param,callback,tableData,tips){
            //封装返回数据
            var returnData = {
                draw: 0,
                recordsTotal:0,
                recordsFiltered: 0,
                data: []
            };
            $.ajax({
                type: 'GET',
                url: url,
                cache: false,  //禁用缓存
                data: param,  //传入组装的参数
                dataType: "json",
                success: function (response) {
                    if (response.code === 'ACK') {
                        // returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = response.data.totalRecord;//返回数据全部记录
                        returnData.recordsFiltered = response.data.totalRecord;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = response.data.list;//返回的数据列表
                    } else {
                        layerTips.msg('查询失败');
                        return false;
                    }
                    //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                    //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                    callback(returnData);
                },
                error: function(){
                    tips.msg('系统异常');
                    callback(returnData);
                },
                complete: function() {
                }
            });
        },
        /**
         * 从请求中获取参数
         * @param str
         * @returns {*}
         */
        getReqParam:function (str) {
            var reg = new RegExp("(^|&)" + str + "=([^&]*)(&|$)","i");
            var r = window.location.search.substr(1).match(reg);
            if (r!=null) {
                var reg=/%3C|%3E|<|>/g;
                return r[2].replace(reg, '');
            }
            return "";
        },
        /**
         * 转义&为%26,?为%3F
         * @param str
         * @returns {*}
         */
        encodeUrlChar:function(str){
            if(str!= null){
                var mid = str.replace(/&/g, "%26");
                return mid.replace(/\?/g, "%3F");
            }else{
                return "";
            }
        },

        /**
         * 转义%26为&,%3F为?
         * @param str
         * @returns {*}
         */
        decodeUrlChar:function(str){
            if(str!= null){
                var mid = str.replace(/%26/g, "&");
                return mid.replace(/%3F/g, "?");
            }else{
                return "";
            }
        },
       /**
         * 根据法人证件类型编号返回证件类型中文名
         * @param type
         * @returns {*}
         */
        checkLegalIdcardType: function (type) {
            if(type == "1"){
                return "身份证";
            }
            if(type == "2"){
                return "军官证";
            }
            if(type == "3"){
                return "文职干部证";
            }
            if(type == "4"){
                return "警官证";
            }
            if(type == "5"){
                return "士兵证";
            }
            if(type == "6"){
                return "护照";
            }
            if(type == "7"){
                return "港、澳、台通行证";
            }
            if(type == "9"){
                return "其它合法身份证件";
            }
            if(type == "8"){
                return "户口簿";
            }
            if(type == "0"){
                return "其他";
            }
            return type;
        },
        /**
         * 根据机构证件类型编号返回证件类型中文名
         * @param type
         * @returns {*}
         */
        checkOrgType: function (type) {

            var orgTypeMap = {
                '01':'社会统一信用代码',
                '02':'工商营业执照',
                '03':'批文',
                '04':'登记证书',
                '05':'开户证明',
                '06':'其他'
            }
            var orgTypeName = orgTypeMap[type];
            return orgTypeName ? orgTypeName : '';
        },
        /**
         * 年月日时间转成 xxxx-xx-xx格式
         * @param cnDate
         * @returns {*}
         */
        cnDateTranslator: function (cnDate) {
        	if(cnDate !=null){
                if(cnDate.indexOf('年')>0){
                    var date = cnDate.replace(new RegExp("年|月|日","gm"),'-');
                    return date.substring(0,date.length-1);
                } else {
                    return cnDate;
                }
        	}else{
        		return "";
        	}

        },
        /**
         * 中文金额转成数字
         * @param cnCurrency
         * @returns {*}
         */
        currencyTranslator: function (cnCurrency) {
            if(cnCurrency){
                var funds = cnCurrency.split("万",-1);
                return funds[0]*10000;
            }
            return "";
        },
        /**
         * 中文币种转换
         * @param cnCurrency
         * @returns {*}
         */
        currencyTypeTranslator: function (cnCurrency) {
            if(cnCurrency){
                if(cnCurrency.indexOf("人")>0){
                    return "CNY";
                } else if(cnCurrency.indexOf("美")){
                    return "USD";
                } else if(cnCurrency.indexOf("港")){
                    return "HKD";
                } else if(cnCurrency.indexOf("欧")){
                    return "EUR";
                } else if(cnCurrency.indexOf("韩")){
                    return "KRW";
                } else if(cnCurrency.indexOf("日")){
                    return "JPY";
                } else if(cnCurrency.indexOf("英")){
                    return "GBP";
                } else if(cnCurrency.indexOf("新")){
                    return "SGD";
                } else if(cnCurrency.indexOf("澳")){
                    return "AUD";
                } else if(cnCurrency.indexOf("加")){
                    return "CAD";
                }else{
                    return "";
                }
            }
            return "";
        },
        isIE: function () {
            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");
            // If Internet Explorer, return version number
            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
                return true;
            }
            return false;
        },

        // Handles Bootstrap switches
        handleBootstrapSwitch: function() {
            if (!$().bootstrapSwitch) {
                return;
            }
            $('.make-switch').bootstrapSwitch();
        },

        handleBootstrapDatetimepicker: function () {
            if (!jQuery().datetimepicker) {
                return;
            }

            $(".form_datetime").datetimepicker({
                startView: 0,
                minView: 0,
                autoclose: true,
                language: 'zh-CN'
            });

            $(".form_date").datetimepicker({
                autoclose: true,
                language: 'zh-CN',
                minView: 2
            });
        },
        handleBootstrapDatepicker: function () {
            if (!jQuery().datepicker) {
                return;
            }

            $(".form_date").datepicker({
                autoclose: true,
                language: 'zh-CN'
            });
        },
        /**
         * table 操作
         */
        getSelectedRows: function (checkedRows) {
            var selectedRows = [];
            for (var key in checkedRows){
                if (checkedRows.hasOwnProperty(key) && checkedRows[key]){
                    selectedRows.push(key);
                }
            }
            return selectedRows;
        },
        bindTableCheckboxAction: function (element,dataStore,resultTable) {
            element.find('.group-checkable').change(function () {
                var allCheckboxes = resultTable.find('tbody tr .checkboxes');
                var checked = $(this).is(":checked");
                allCheckboxes.each(function () {
                    if (checked) {
                        $(this).prop("checked", true);
                        dataStore[$(this).prop('value')] = true;
                    } else {
                        $(this).prop("checked", false);
                        dataStore[$(this).prop('value')] = false;
                    }
                });
            });
            element.on('change', 'tbody tr .checkboxes', function () {
                var checked = $(this).is(":checked");
                if (!checked){
                    $('.group-checkable', $(this).parents('table')).prop("checked", false);
                } else {
                    if (isAllChecked($(this).parents('table').find('tbody tr .checkboxes'))){
                        $('.group-checkable', $(this).parents('table')).prop("checked", true);
                    }
                }
                $(this).prop("checked", checked);
                dataStore[$(this).prop('value')] = checked;
            });

            function isAllChecked(allCheckboxes) {
                var isAllCheckedFlag = true;
                allCheckboxes.each(function () {
                    var checked = $(this).is(":checked");
                    if (!checked){
                        isAllCheckedFlag = false;
                    }
                });
                return isAllCheckedFlag;
            }
        }
        // /**
        //  * 模板加载基础工具方法
        //  * @param type
        //  * @returns {*}
        //  */
        // loadTemplateBasic: function (url, data, element) {
        //     //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        //     $.get(url, null, function (template) {
        //         if(template){
        //             laytpl(template).render(data,function (html) {
        //                 $(element).html(html);
        //             });
        //         }
        //     });
        // }
	};

	exports('common', common);
});