/**
 * Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var core={};
core.tableOption = function (options) {
    return $.extend({
        "ordering": false,
        "columns": [
            { "data": "acctNo", "targets":0, "orderable": false},
            { "data": "depositorName", "targets":1,"orderable": false},
            { "data": "organCode", "targets":2,"orderable": false},
            { "data": "legalName", "targets":3,"orderable": false},
            { "data": null, "targets":4,"orderable": false,
                "render": function (data){
                    if(data.collectState === 'init'){
                        return '未处理';
                    }else if(data.collectState === 'success'){
                        return '成功';
                    }else if(data.collectState === 'fail'){
                        return '失败';
                    }else{
                        return '';
                    }
                }
            },
            { "data": "failReason", "targets":5,"orderable": false},
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
            core.searchHistory(data,callback);
        }
    },options);
};

core.init = function (options) {
    core.table = $('#coreTable').dataTable(core.tableOption(options))
};

core.searchHistory = function (data,callback) {
    //     //封装请求参数
    var param = {};
    param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    param.offset = (data.start / data.length);//当前页码

    param.collectState = $.trim($("#collectStateId").val());
    param.acctNo = $.trim($("#acctNoId").val());
    param.depositorName = $.trim($("#depositorNameId").val());
    param.organCode = $.trim($("#organCodeId").val());
    param.legalName = $.trim($("#legalNameId").val());

    core.param = param;

    core.jqueryDataTableAjax('../../annualTask/coredata',param,callback,data,core.layerTips);
};

layui.use(['common','loading','annual_collect','form'], function () {
    var $ = layui.jquery,
        common = layui.common,
        form = layui.form,
        annual = layui.annual_collect;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    core.layerTips = layerTips;
    core.jqueryDataTableAjax = common.jqueryDataTableAjaxByAck;
    core.init(common.tableDefaulOptions());

    $('#backBtn').on('click',function () {
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
    });

    $('#coreTable').on('click','.data-detail', function () {
        annual.loadCoreDataTemplate($(this).data('id'),'核心数据详情','templateForCore.html');
    });

    //查询按钮
    $('#searchSaicBtn').on('click', function () {
        core.table.api().ajax.reload();
    });

    /*form.render("select");*/
    //失败记录重新采集
    $('#failReCollectBtn').on('click', function () {
        //提交年检比对
        layer.confirm('是否要将失败的核心数据重新采集？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            $.ajax({
                url: '../../annualTask/core/start/reset',
                type: 'GET',
                dataType: "json",
                success: function (result) {
                    if(result.rel){
                        layer.alert("核心数据重新采集中...");
                    }else{
                        layer.alert(result.msg);
                    }
                }

            });
        }, function(){
            //取消
        });
    });


    //重新导入数据
    $('#reImportBtn').on('click', function () {

        //核心数据导入
        var index = layer.open({
            title: '选择类型',
            type: 1,
            // skin: 'layui-layer-rim', //加上边框
            area: ['300px', '150px'], //宽高
            //content: '<div id="uploadimg" class="row" style="margin-left: 10px;margin-top: 20px;"><div id="fileList" class="uploader-list"></div><div id="coreFileReUpload" class="col-md-5"></div></div>'
            content: '<div id="uploadimg" class="row" style="margin-left: 10px;margin-top: 20px;"><div id="fileList" class="uploader-list"></div><div id="coreFileReUpload" class="col-md-5">导入</div></div>'
        });


        // 导入文件
        var reUploaderCore = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server: '../../annualTask/upload', // 文件接收服务端
            pick: '#coreFileReUpload', // 选择文件的按钮。可选
            formData: { "reUpload": 'true'},
            /*fileNumLimit: 1,//一次最多上传五张
            // 只允许选择图片文件。*/
            accept: {
                title: 'Excel',
                extensions: 'xls',
                mimeTypes: 'application/vnd.ms-excel'

            }
        });
        reUploaderCore.on('uploadSuccess', function (file, res) {
            layerTips.msg(res.message);
            layer.close(index);
        });
        reUploaderCore.on('uploadError', function (file, reason) {
            layerTips.msg('上传失败');
        });
        reUploaderCore.on('error', function (handler) {
            if (handler == 'Q_TYPE_DENIED') {
                layerTips.msg('文件类型不对，请上传xls后缀名的文件');
            } else {
                layerTips.msg(handler);
            }
        });

    });

});