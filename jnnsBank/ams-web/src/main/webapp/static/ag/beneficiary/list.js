
layui.config({
     base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../batch",
    entity: "batch",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'batchNo',
        title: '批次号'
    }, {
        field: 'processTime',
        title: '处理时间'
    }, {
        field: 'fileName',
        title: '文件名'
    }, {
        field: 'fileSize',
        title: '文件大小(Byte)'
    }, {
        field: 'txCount',
        title: '数据量'
    }, {
        field: 'process',
        title: '是否已完成',
        formatter: function (value, row, index) {
            return value ? '是' : '否'
        }
    }, {
        field: 'type',
        title: '批量类型',
        formatter: function (value, row, index) {
            return typeFormat(value)
        }
    }, {
        field: 'batchNo',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="'+value+'" data-type="'+row['type']+'">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            type: "BENEFICIARY,BENEFICIARYNAME,BENEFICIARYCOLLECT,STOCKHOLDER,TELECOM_3EL, BATCH_CUSTOMERTUNE",
            batchNo: $.trim($("#batchNo").val()),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        type: "BENEFICIARY,BENEFICIARYNAME,BENEFICIARYCOLLECT,STOCKHOLDER,TELECOM_3EL, BATCH_CUSTOMERTUNE",
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        batchNo: $.trim($("#batchNo").val()),
        processTimeStart: $("#beginDate").val(),
        processTimeEnd: $("#endDate").val()
    };
    if ($("#type").val() !== "") {
        temp.type = $("#type").val();
    }
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/query', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + list.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list.order, //排序方式
        queryParams: list.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: list.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list.columns(),
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};

list.select = function (layerTips) {
    var rows = list.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        list.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'loading','common'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        common = layui.common,
        laydate = layui.laydate,
        loading = layui.loading;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh',queryParams);
        }
    });

    $('#list').on('click','.view', function () {
        var batchNo = $(this).attr('data-id');
        var batchType = $(this).attr('data-type');

        if(batchType == 'BATCH_CUSTOMERTUNE') {
            parent.tab.tabAdd({
                title: '客户尽调',
                href: '../ui/kyc/list.html?batchNo=' + batchNo
            });
        } else if (batchType === 'BENEFICIARYCOLLECT'){
            parent.tab.tabAdd({
                title: '采集详情',
                href: '../ui/beneficiary/' + typeDetail(batchType) + '?batchNo=' + batchNo
            });
        } else {
            parent.tab.tabAdd({
                title: '比对详情',
                href: '../ui/beneficiary/' + typeDetail(batchType) + '?batchNo=' + batchNo
            });
        }

        return false;
    });


    $("#btn_beneficiary_download").on("click", function () {
        $("#downloadFrame").prop('src', "../attach/beneficiary_template.xls");
        return false;
    });


    $("#btn_stockholder_download").on("click", function () {
        $("#downloadFrame").prop('src', "../attach/stockholder_template.xls");
        return false;
    });

    $('#btn_upload').on('click', function () {
        var result;
        var index = layer.open({
            type : 1,
            title : '文件导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '320px', '300px' ], //宽高
            content : (function () {
                var html = '<div id="uploadimg">\n    ' +
                    '<div id="fileList" class="uploader-list"></div>\n    ';
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div id="beneficiaryUpload">上传受益人列表</div>\n    ';
                }
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div id="beneficiaryUpload-name">上传受益人列表(只比对受益人)</div>\n    ';
                }
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div id="beneficiaryUploadCollect">上传受益人列表(采集工商受益人信息)</div>\n    ';
                }
                if (parent.tab.config.authBtns["verify:upload_stockHolder"]) {
                    html += '<div id="stockHolderUpload">上传控股股东列表</div>\n    ';
                }
                if (parent.tab.config.authBtns["verify:upload_telecom"]) {
                    html += '<div id="telecom3ELUpload">上传电信运营商校验列表</div>\n    ';
                }
                if (parent.tab.config.authBtns["kyc:uploadCustomers"]) {
                    html += '<div id="customerUpload">上传尽调列表</div>\n    ';
                }

                html += '<div style="color: red; display: none" id="importText">导入中...</div>\n' +
                    '</div>\n';
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a href="javascript:void(0);" onclick="downloadFile(\'../attach/beneficiary_template.xls\')" style="margin-left: 10px;cursor:pointer;color: blue">下载受益人信息模板文件</a>\n' +
                        '</div>\n';
                }
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a href="javascript:void(0);" onclick="downloadFile(\'../attach/beneficiary_name_template.xls\')" style="margin-left: 10px;cursor:pointer;color: blue">下载受益人信息模板文件(只比对受益人)</a>\n' +
                        '</div>\n';
                }
                if (parent.tab.config.authBtns["verify:upload_beneficiary"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a target="_blank" href="../attach/beneficiary_collect_template.xls" style="margin-left: 10px;cursor:pointer;color: blue">下载受益人信息模板文件(采集工商受益人信息)</a>\n' +
                        '</div>\n';
                }
                if (parent.tab.config.authBtns["verify:upload_stockHolder"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a href="javascript:void(0);" onclick="downloadFile(\'../attach/stockholder_template.xls\')" style="margin-left: 10px;cursor:pointer;color: blue">下载控股股东信息模板文件</a>\n' +
                        '</div>\n';
                }
                if (parent.tab.config.authBtns["verify:upload_telecom"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a href="javascript:void(0);" onclick="downloadFile(\'../attach/telecom_template.xls\')" style="margin-left: 10px;cursor:pointer;color: blue">下载电信运营商校验模板文件</a>\n' +
                        '</div>\n';
                }
                if (parent.tab.config.authBtns["kyc:downloadTemplate"]) {
                    html += '<div class="row" style="margin-left: 10px;margin-top: 10px;">\n    ' +
                        '<a href="javascript:void(0);" onclick="downloadFile(\'../attach/batchCustomers_tuneIn.xls\')" style="margin-left: 10px;cursor:pointer;color: blue">下载批量尽调模板文件</a>\n' +
                        '</div>\n';
                }

                return html;
            })()
        });

        var beneficiaryUploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../beneficiary/beneficiary/upload', // 文件接收服务端
            timeout: 0,
            pick: '#beneficiaryUpload', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });



        beneficiaryUploader.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });

        beneficiaryUploader.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            if(res.code == "ACK"){
                layer.close(index);
                var queryParams = list.queryParams();
                queryParams.pageNumber=1;
                list.table.bootstrapTable('refresh',queryParams);
            }
        });
        beneficiaryUploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        beneficiaryUploader.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });

        var beneficiaryUploader1 = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../beneficiary/beneficiary/uploadName', // 文件接收服务端
            timeout: 0,
            pick: '#beneficiaryUpload-name', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });



        beneficiaryUploader1.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });

        beneficiaryUploader1.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            if(res.code == "ACK"){
                layer.close(index);
                var queryParams = list.queryParams();
                queryParams.pageNumber=1;
                list.table.bootstrapTable('refresh',queryParams);
            }
        });
        beneficiaryUploader1.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        beneficiaryUploader1.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });


        var beneficiaryUploader2 = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../beneficiary/beneficiary/uploadCollect', // 文件接收服务端
            timeout: 0,
            pick: '#beneficiaryUploadCollect', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });



        beneficiaryUploader2.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });

        beneficiaryUploader2.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            if(res.code == "ACK"){
                layer.close(index);
                var queryParams = list.queryParams();
                queryParams.pageNumber=1;
                list.table.bootstrapTable('refresh',queryParams);
            }
        });
        beneficiaryUploader2.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        beneficiaryUploader2.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });

        var telecomUploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../beneficiary/telecom/upload', // 文件接收服务端
            timeout: 0,
            pick: '#telecom3ELUpload', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });

        telecomUploader.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });

        telecomUploader.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            if(res.code == "ACK"){
                layer.close(index);
                var queryParams = list.queryParams();
                queryParams.pageNumber=1;
                list.table.bootstrapTable('refresh', queryParams);
            }
        });
        telecomUploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        telecomUploader.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });

        var stockHolderUploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../beneficiary/stockholder/upload', // 文件接收服务端
            timeout: 0,
            pick: '#stockHolderUpload', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });

        stockHolderUploader.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });
        stockHolderUploader.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            if(res.code == "ACK"){
                layer.close(index);
                var queryParams = list.queryParams();
                queryParams.pageNumber=1;
                list.table.bootstrapTable('refresh',queryParams);
            }
        });
        stockHolderUploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        stockHolderUploader.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });
    });
});

function typeFormat(value) {
    var typeMap = {
        'TPO': 'T+1',
        'BATCH_SUSPEND': '批量久悬',
        'ILLEGAL_INQUIRY': '违法查询',
        'BENEFICIARY': '受益人比对',
        'BENEFICIARYNAME': '受益人名称比对',
        'BENEFICIARYCOLLECT': '受益人采集',
        'STOCKHOLDER': '控股股东比对',
        'TELECOM_3EL': '电信三要素校验',
        'BATCH_CUSTOMERTUNE': '批量客户尽调'
    }
    return typeMap[value] || '';
}

function typeDetail(value) {
    var typeMap = {
        'BENEFICIARY': 'beneficiary_detail.html',
        'BENEFICIARYNAME': 'beneficiary_name_detail.html',
        'BENEFICIARYCOLLECT': 'beneficiary_collect_detail.html',
        'STOCKHOLDER': 'stock_holder_detail.html',
        'TELECOM_3EL': 'telecom_detail.html',
    }
    return typeMap[value] || '';
}

function downloadFile(url) {
    $('#downloadFrame').attr('src', url);
    return false;
}