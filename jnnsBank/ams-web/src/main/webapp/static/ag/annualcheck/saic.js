/**
 * Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var saic={};
saic.tableOption = function (options) {
    return $.extend({
        "ordering": false,

        "columns": [
            { "data": "customerName", "targets":0,"orderable": false},
            { "data": "state", "targets":1,"orderable": false},
            { "data": null, "targets":2,"orderable": false,
                "render": function (data){
                    if(data.collectState === 'init'){
                        return '未采集';
                    }else if(data.collectState === 'success'){
                        return '采集成功';
                    }else if(data.collectState === 'fail'){
                        return '采集失败';
                    }else{
                        return '';
                    }
                }
            },
            { "data": "failReason", "targets":3,"orderable": false},
            { "data": null, "targets":4,
                "orderable": false,
                "render": function (data){
                    if(data.collectState === 'success'){
                        return '<a href="javascript:void(0)" data-id="'+data.id+'" class="data-detail btn' +
                            ' btn-outline' +
                            ' btn-xs blue"> ' +
                            '<i class="fa fa-file-text-o"></i> 数据详情 </a>';
                    }
                    return '';
                }
            }
        ],

        "ajax":function (data,callback,settings) {
            saic.searchHistory(data,callback);
        }
    },options);
};

saic.init = function (options) {
    saic.table = $('#saicTable').dataTable(saic.tableOption(options))
};

saic.searchHistory = function (data,callback) {
    //封装请求参数
    var param = {};
    param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    param.offset = (data.start / data.length);//当前页码

    param.collectState = $.trim($("#collectStateId").val());
    param.customerName = $.trim($("#customerNameId").val());
    param.state = $.trim($("#stateId").val());

    saic.param = param;

    saic.jqueryDataTableAjax('../../annualTask/saicdata',param,callback,data,saic.layerTips);
};

layui.use(['common','loading','annual_collect','form'], function () {
    var $ = layui.jquery,
        common = layui.common,
        form = layui.form;
        annual = layui.annual_collect;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    saic.layerTips = layerTips;
    saic.jqueryDataTableAjax = common.jqueryDataTableAjaxByAck;
    saic.init(common.tableDefaulOptions());

    $('#backBtn').on('click',function () {
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
    });

    $('#saicTable').on('click','.data-detail', function () {
        annual.loadSaicDataTemplate($(this).data('id'),'工商数据详情','templateForSaic.html');
    });

    //查询按钮
    $('#searchSaicBtn').on('click', function () {
        saic.table.api().ajax.reload();
    });

    //查询重置
    /*$('#resetSearchSaicBtn').on('click', function () {
        $("#collectStateId").val("");
    });*/

    //失败记录重新采集
    $('#failReCollectBtn').on('click', function () {
        //提交年检比对
        layer.confirm('是否要将失败的工商信息重新采集？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $.ajax({
                url: '../../annualTask/saic/start/reset',
                type: 'GET',
                dataType: "json",
                success: function (result) {
                    if(result.rel){
                        layer.alert("工商数据重新采集中...");
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