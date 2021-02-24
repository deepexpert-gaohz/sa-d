layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var syncType;
var apply = {
    baseUrl: "../../syncHistory",
    entity: "user",
    tableId: "syncTable",
    toolbarId: "toolbar",
    unique: "applyid",
    order: "desc",
    currentItem: {}
};
apply.columns = function () {
    return [/*{
        radio: true
    },*/ {
        field: 'id',
        title: 'ID',
        visible: false
    }, {
        field: 'organName',
        title: '机构'
    }, {
        field: 'organCode',
        title: '网点机构号'
    }, {
        field: 'acctNo',
        title: '报送账号'
    }, {
        field: 'acctType',
        title: '账户类型',
        'class': 'W120',
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    }, {
        field: 'billType',
        title: '报送业务类型',
        formatter: function (value, row, index) {
            return changeBillType(value)
        }
    }, {
        field: 'syncDateTime',
        title: '报送时间'

    }, {
        field: 'syncName',
        title: '报送人'
    }, {
        field: 'syncStatus',
        title: '报送结果',
        formatter: function (value, row, index) {
            return formatSyncStatus(value)
        }
    }];
};
apply.queryParams = function (params) {
    if (!params)
        return {
            organName: $.trim($("#organName").val()),
            organCode: $.trim($("#organCode").val()),
            acctNo: $("#acctNo").val(),
            billType: $("#billType").val(),
            //bankCode: $("#bankCode").val(),
            acctType: $("#acctType").val(),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
            syncName: $("#syncName").val(),
            syncStatus: $("#syncStatus").val(),
            syncType:syncType
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        organName: $.trim($("#organName").val()),
        organCode: $.trim($("#organCode").val()),
        acctNo: $("#acctNo").val(),
        billType: $("#billType").val(),
        //bankCode: $("#bankCode").val(),
        acctType: $("#acctType").val(),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        syncName: $("#syncName").val(),
        syncStatus: $("#syncStatus").val(),
        syncType:syncType
    };
    return temp;
};
apply.init = function () {

    apply.table = $('#' + apply.tableId).bootstrapTable({
        url: apply.baseUrl + '/search', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: apply.order, //排序方式
        queryParams: apply.queryParams,//传递参数（*）
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
        uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: apply.columns(),
        // onDblClickRow:function (items,$element) {
        //     var id = items.id;
        //     var depositorName = items.depositorName;
        //     parent.tab.tabAdd({
        //         title: '开户-'+depositorName,
        //         href: 'bank/view.html?id='+id+'&depositorName='+encodeURI(depositorName)
        //     });
        // },
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};
layui.use(['form', 'layedit', 'laydate','layer','common'], function () {
    var common = layui.common;
    syncType = decodeURI(common.getReqParam("syncType"));
    console.log("syncType="+syncType)
    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;


    var laydateArr = beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);
    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber=1;
        apply.table.bootstrapTable('refresh', queryParams);
    });
});