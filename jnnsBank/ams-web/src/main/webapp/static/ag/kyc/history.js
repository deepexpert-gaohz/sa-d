/**
 * Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var history1={};
var batchNo;

history1.tableOption = function (options) {
    return $.extend({
        "order": [
        ],

        "lengthMenu": [
            [5, 10, 15, 20, 100],
            [5, 10, 15, 20, 100] // change per page values here
        ],
        // set the initial value
        "pageLength": 10,

        "columns": [
            { "data": "querydate", "targets":0,
                "orderable": false},
            { "data": "entname", "targets":1,
                "orderable": false},
            { "data": "orgfullname", "targets":2,
                "orderable": false},
            { "data": "cname", "targets":3,
                "orderable": false},
            { "data": "saicinfoId", "targets":4,
                "searchable": false,
                "orderable": false,
                "render": function (data,type, full, meta){
                    return '<a href="javascript:void(0)" saicinfoId="'+data+'" querydate="'+full.querydate+'" class="khjd-detail btn btn-outline' +
                        ' btn-xs blue"> ' +
                        '<i class="fa fa-file-text-o"></i> 客户尽调详情 </a>';
                }
            }
        ],

        "processing":true,
        "serverSide": true,  //启用服务器端分页

        "ajax":function (data,callback,settings) {
            history1.searchHistory(data,callback);
        },
        "dom": "<'row'<'col-md-6 col-sm-12'><'col-md-6 col-sm-12'>r><'table-scrollable't><'row'<'col-md-5 col-sm-12'il><'col-md-7 col-sm-12'p>>"
    },options);
};

history1.init = function (options) {
    history1.table = $('#historyTable').dataTable(history1.tableOption(options))
};

history1.searchHistory = function (data,callback) {
    //封装请求参数
    var param = {};
    param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    // param.offset1 = data.start;//开始的记录序号
    param.offset = (data.start / data.length);//当前页码

    var beginDate = $('#beginDate').val();
    if(beginDate){
        param.beginDate = beginDate;
    }
    var endDate = $('#endDate').val();
    if(endDate){
        param.endDate = endDate;
    }
    var key = $('#key').val();
    if(key){
        param.key = key;
    }
    var flag = $('#flag option:selected').val();
    if(flag){
        param.flag = flag;
    }

    if(batchNo) {
        param.batchNo = batchNo;
        $('#uploadCustomerBtn').hide();
        $('#downloadTemplateBtn').hide();
        $('#searchPageDiv').hide();
    }

    if(data.order && data.order.length === 1){
        if( data.order[0].column === 1){
            param.column = 'entname';
        } else {
            param.column = 'querydate';
        }
        param.orderStr = data.order[0].dir;
    }

    $.ajax({
        type: 'GET',
        url: '../../kyc/history/list',
        cache: false,  //禁用缓存
        data: param,  //传入组装的参数
        dataType: "json",
        success: function (res) {
            //封装返回数据
            var returnData = {};
            if (res.code === 'ACK') {
                returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                returnData.recordsTotal = res.data.totalRecord;//返回数据全部记录
                returnData.recordsFiltered = res.data.totalRecord;//后台不实现过滤功能，每次查询均视作全部结果
                returnData.data = res.data.list;//返回的数据列表
            } else {
                layerTips.msg('查询失败');
                return false;
            }
            //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
            //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
            callback(returnData);
        },
        complete: function() {
        }
    });
}

layui.use(['common', 'laydate'], function () {
    var $ = layui.jquery,
        common = layui.common,
        laydate = layui.laydate;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);

    batchNo = decodeURI(common.getReqParam("batchNo"));

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    history1.layerTips = layerTips;
    history1.init(common.tableDefaulOptions());

    placeholder($('.placeholder'));
   
    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    //绑定按钮事件
    $('#historyTable').on('click','.khjd-detail', function () {
        var keyword = $(this).attr('saicinfoId');
        var querydate = $(this).attr('querydate');
        if(keyword && keyword != 'null'){
            parent.tab.tabAdd({
                    title: '客户尽调',
                    href: 'kyc/detail.html?saicInfoId='+encodeURI(keyword)+'&history=true&querydate='+querydate
                }
            );
        }else{
            layerTips.msg('无该客户的尽调详情，请重新查询');
        }
    });

    $('#searchHistoryBtn').on('click', function () {
        history1.table.api().ajax.reload();
    });

    $('#refreshBtn').on('click', function () {
        history1.table.api().ajax.reload();
    });

    //初始化页面上面的按钮事件
    $('#searchBtn').on('click', function () {
        var keyword = $('#keyword').val();
        if(keyword){
            parent.tab.tabAdd({
                    title: '客户尽调',
                    href: 'kyc/detail.html?name='+encodeURI(keyword)+'&searchType=KHJD'
                }
            );
        }
    });

    $('#resetSearchHistoryBtn').on('click', function () {
        $('#beginDate').val('');
        $('#endDate').val('');
        $('#key').val('');
        $('#flag').val('1');
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    $('#downloadTemplateBtn').on('click', function () {
        $("#downloadFrame").prop('src', "../attach/batchCustomers_tuneIn.xlsx");
        return false;
    });

    $('#btn_export').on('click', function () {
        $("#downloadFrame").prop('src', "../../kyc/history/historyExport");
        return false;
    });

    $('#uploadCustomerBtn').on('click', function () {
        var result;
        var batchNo;

        var index = layer.open({
            type : 1,
            title : '文件导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '300px', '200px' ], //宽高
            content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> <div style="color: red; display: none" id="importText">导入中...</div> </div>'
        });

        var uploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../kyc/batch/upload', // 文件接收服务端
            timeout: 0,
            pick: '#imgPicker', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });

        uploader.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });

        uploader.on( 'uploadSuccess', function( file, response ) {
            $.ajaxSettings.async = false;
            layer.open({
                title: '上传结果'
                ,content: response.message
            });

            layer.close(index);
            if(response.code != 'ACK') {  //上传不成功
                return;
            }

            batchNo = response.data;
            history1.table.api().ajax.reload();

            $.ajaxSettings.async = true;
        });

        // 文件上传结束后执行
        uploader.on( 'uploadFinished', function( file ) {
            if(batchNo) {
                $.get('../../batch/updateBatchCount?batchNo=' + batchNo, function (data) {

                });
            }
        });

        uploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        uploader.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });

    });

});