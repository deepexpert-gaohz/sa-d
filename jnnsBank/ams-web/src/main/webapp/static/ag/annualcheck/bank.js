/**
 * Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

function accountStatusFormat(value) {
    if(value == 'normal') {
        return '正常';
    } else if(value == 'suspend') {
        return '久悬';
    } else if(value == 'revoke') {
        return '销户';
    } else if(value == 'unKnow') {
        return '未知';
    } else if(value == 'notExist') {
        return '不存在';
    } else if(value == 'notActive') {
        return '未激活';
    } else {
        return '';
    }
}

var bank={};
bank.tableOption = function (options) {
    return $.extend({
        "ordering": false,

        "columns": [
            { "data": "acctNo", "targets":0, "orderable": false},
            { "data": "depositorName", "targets":1,"orderable": false},
            { "data": "acctCreateDate", "targets":2,"orderable": false},
            { "data": null, "targets":3,"orderable": false,
                "render": function (data){
                    if(data.accountStatus === 'normal'){
                        return '正常';
                    }else if(data.accountStatus === 'suspend'){
                        return '久悬';
                    }else if(data.accountStatus === 'revoke'){
                        return '销户';
                    }else if(data.accountStatus === 'unKnow'){
                        return '未知';
                    }else if(data.accountStatus === 'notExist'){
                        return '不存在';
                    }else if(data.accountStatus === 'notActive'){
                        return '未激活';
                    }else{
                        return '';
                    }
                }
            },
            { "data": null, "targets":4,"orderable": false,
                "render": function (data){
                    if(data.collectState === 'init'){
                        return '未采集';
                    }else if(data.collectState === 'success'){
                        return '采集成功';
                    }else if(data.collectState === 'fail'){
                        return '采集失败';
                    }else if(data.collectState === 'noNeed'){
                        return '无需采集';
                    }else{
                        return '';
                    }
                }
            },
            { "data": "parErrorMsg", "targets":5,"orderable": false},
            { "data": null, "targets":6,
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
            bank.searchHistory(data,callback);
        }
    },options);
};

bank.init = function (options) {
    bank.table = $('#bankTable').dataTable(bank.tableOption(options))
};

bank.searchHistory = function (data,callback) {
    //封装请求参数
    var param = {};
    param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    param.offset = (data.start / data.length);//当前页码

    param.collectState = $.trim($("#collectStateId").val());
    param.acctNo = $.trim($("#acctNoId").val());
    param.depositorName = $.trim($("#depositorNameId").val());


    bank.param = param;

    bank.jqueryDataTableAjax('../../annualTask/pbcdata',param,callback,data,bank.layerTips);
};

layui.use(['common','loading','annual_collect','form'], function () {
    var $ = layui.jquery,
        common = layui.common,
        form = layui.form,
        annual = layui.annual_collect;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    bank.layerTips = layerTips;
    bank.jqueryDataTableAjax = common.jqueryDataTableAjaxByAck;
    bank.init(common.tableDefaulOptions());

    $('#backBtn').on('click',function () {
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
    });

    $('#bankTable').on('click','.data-detail', function () {
        annual.loadBankDataTemplate($(this).data('id'),'人行数据详情','templateForPbc.html');
    });

    //查询按钮
    $('#searchSaicBtn').on('click', function () {
        bank.table.api().ajax.reload();
    });

    //查询重置
   /* $('#resetSearchSaicBtn').on('click', function () {
        $("#collectStateId").val("");
    });
*/

    //失败记录重新采集
    $('#failReCollectBtn').on('click', function () {
        //提交年检比对
        layer.confirm('是否要将失败的人行数据重新采集？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $.ajax({
                url: '../../annualTask/pbc/start/reset',
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