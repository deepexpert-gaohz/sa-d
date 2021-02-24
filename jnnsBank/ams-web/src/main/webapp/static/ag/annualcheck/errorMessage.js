/**
 * Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var errorMessage={};
errorMessage.tableOption = function (options) {
    return $.extend({
        "ordering": false,
        "columns": [
            { "data": "bankName", "targets":0, "orderable": false},
            { "data": "organizationId", "targets":1,"orderable": false},
            { "data": "pbcCode", "targets":2,"orderable": false},
            { "data": "error", "targets":3,"orderable": false},
        ],

        "ajax":function (data,callback,settings) {
            errorMessage.searchHistory(data,callback);
        }
    },options);
};

errorMessage.init = function (options) {
    errorMessage.table = $('#errorMessageTable').dataTable(errorMessage.tableOption(options))
};

errorMessage.searchHistory = function (data,callback) {
    //     //封装请求参数
    var param = {};
    param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    param.offset = (data.start / data.length);//当前页码

    param.bankName = $.trim($("#bankNameId").val());
    // param.organizationId = $.trim($("#organizationIdId").val());
    param.pbcCode = $.trim($("#pbcCodeId").val());

    errorMessage.param = param;

    errorMessage.jqueryDataTableAjax('../../annualTask/pbc/errorMessage',param,callback,data,errorMessage.layerTips);
};

layui.use(['common','loading','annual_collect'], function () {
    var $ = layui.jquery,
        common = layui.common,
        annual = layui.annual_collect;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    errorMessage.layerTips = layerTips;
    errorMessage.jqueryDataTableAjax = common.jqueryDataTableAjaxByAck;
    errorMessage.init(common.tableDefaulOptions());

    $('#backBtn').on('click',function () {
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
    });

    $('#errorMessageTable').on('click','.data-detail', function () {
        annual.loaderrorMessageDataTemplate($(this).data('id'),'核心数据详情','template.html');
    });

    //查询按钮
    $('#searchSaicBtn').on('click', function () {
        errorMessage.table.api().ajax.reload();
    });

    /*//查询重置
    $('#resetSearchSaicBtn').on('click', function () {
        $("#collectStateId").val("");
    });*/


    //失败记录重新采集
    $('#failReCollectBtn').on('click', function () {
        //提交年检比对
        layer.confirm('是否要将失败的人行数据重新采集？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $.ajax({
                url: '../../annualTask/pbc/start/excel/reset',
                type: 'GET',
                dataType: "json",
                success: function (result) {
                    if(result.rel){
                        layer.alert("人行数据重新采集中...");
                    }else{
                        layer.alert(result.msg);
                    }
                }

            });
        }, function(){
            //取消
        });
    });
});