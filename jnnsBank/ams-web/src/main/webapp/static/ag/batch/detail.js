var list = {
    baseUrl: "../../batchSuspend",
    entity: "batch",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'batchNo',
        title: '批次号'
    }, {
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '存款人名称'
    }, {
        field: 'organName',
        title: '机构'
    }, {
        field: 'acctType',
        title: '账户性质',
        formatter: function (value, row, index) {
            return acctTypeFormat(value)
        }
    }, {
        field: 'syncStatus',
        title: '是否上报',
        formatter: function (value, row, index) {
            return value == 'Yes' ? '是' : '否';
        }
    }, {
        field: 'processed',
        title: '是否处理完成',
        formatter: function (value, row, index) {
            return value == 'Yes' ? '是' : '否'
        }
    }, {
        field: 'type',
        title: '批量久悬类型',
        formatter: function (value, row, index) {
            return typeFormat(value)
        }
    }, {
        field: 'errorMessage',
        title: '上报失败原因'
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            batchNo: $("#batchNo").val(),
            depositorName: $.trim($("#depositorName").val()),
            acctType: $.trim($("#acctType").val()),
            syncStatus: $("#syncStatus").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        batchNo: $("#batchNo").val(),
        depositorName: $.trim($("#depositorName").val()),
        acctType: $.trim($("#acctType").val()),
        syncStatus: $("#syncStatus").val()
    };
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
        responseHandler: function (res) {
            return {total: res.total, rows: errorMessageFormat(res.rows)};
            /*if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }*/
        }
        , onLoadError: function (status) {
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

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

layui.use(['form', 'layedit', 'laydate', 'upload', 'common'], function () {

    var common = layui.common;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var batchNo = decodeURI(common.getReqParam("batchNo"));
    $("#batchNo").val(batchNo);

    list.init();

    $('#btn_query').on('click', function () {
        var queryParams = list.queryParams();
        queryParams.pageNumber=1;
        list.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_batch_suspend_submit').on('click', function () {
        var rows = list.table.bootstrapTable('getSelections');
        var billIds = new Array();
        var ids = new Array();
        for (var i = 0; i < rows.length; i++) {
            billIds[i] = rows[i].billId;
            ids[i] = rows[i].id;
        }
        //提交久悬
        $.get(list.baseUrl + '/submitSync/', {billIds: billIds, ids: ids}, function (res) {
            if (res.code === 'ACK') {
                layerTips.msg("久悬提交人行完成!");
                location.reload();
            } else {
                layerTips.msg(res.message);
            }
        });
    });

});

function acctTypeFormat(value) {
    var acctTypeMap = {
        'jiben': '基本存款账户',
        'yiban': '一般存款账户',
        'yusuan': '预算单位专用存款账户',
        'feiyusuan': '非预算单位专用存款账户',
        'teshu': '特殊单位专用存款账户',
        'linshi': '临时机构临时存款账户',
        'feilinshi': '非临时机构临时存款账户'
    }
    return acctTypeMap[value] || '';
}

function typeFormat(value) {
    var typeMap = {
        'HTML_UPLOAD': '页面上传',
        'BATCH_FILE': '批次文件'
    }
    return typeMap[value] || '';
}

var errorMessageFormat = function(data){
    for( i in data){
        if(data[i].syncStatus == 'Yes'){
            data[i].errorMessage = '';
        }
    }
    return data;
}