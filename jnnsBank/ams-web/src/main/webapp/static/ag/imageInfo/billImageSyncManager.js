layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../allBillsPublic",
    entity: "result",
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
        field: 'billNo',
        title: '流水号'
    }, {
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '存款人名称'
    }, {
        field: 'acctType',
        title: '账户性质',
        'class': 'W120',
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    }, {
        field: 'billType',
        title: '操作类型',
        formatter: function (value, row, index) {
            return changeBillType(value)
        }
    }, {
        field: 'kernelOrgCode',
        title: '网点机构号'
    }, {
        field: 'bankName',
        title: '网点名称'
    }, {
        field: 'status',
        title: '审核状态',
        formatter: function (value, row, index) {
            return changeStatus(value)
        }
    },{
        field: 'pbcSyncStatus',
        title: '人行上报状态',
        formatter: function (value, row, index) {
            return formatSyncStatus(value)
        }
    }, {
        field: 'imgaeSyncStatus',
        title: '影像上报状态',
        formatter: function (value, row, index) {
            return formatSyncStatus(value)
        }
    },{
        field: 'createdDate',
        title: '申请日期',
        width: '150px',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateStr(value)
        }
    }, {
        field: 'createdBy',
        title: '申请人'
    // }, {
    //     field: 'id',
    //     title: '操作',
    //     formatter: function (value, row, index) {
    //         return '<a class="view" style="color: blue" href="#" data-id="' + value + '">查看</a>'
    //         + ' <a class="edit" style="color: blue" href="#" data-id="' + value + '">变更</a>';
    //     }
    }];
};

list.queryParams = function (params) {
    var temp = {
        billNo: $.trim($("#billNo").val()),
        billType: $.trim($("#billType").val()),
        beginDate: $.trim($("#beginDate").val()),
        endDate: $.trim($("#endDate").val()),
        acctNo: $.trim($("#acctNo").val()),
        depositorName: $.trim($("#depositorName").val()),
        kernelOrgCode: $.trim($("#kernelOrgCode").val()),
        kernelOrgName: $.trim($("#kernelOrgName").val()),
        createdBy: $.trim($("#createdBy").val()),
        acctType: $.trim($("#acctType").val()),
        status: $.trim($("#status").val()),
        pbcSyncStatus: $.trim($("#pbcSyncStatus").val()),
        imgaeSyncStatus: $.trim($("#imgaeSyncStatus").val())
    };
    if (params) {
        temp.size = params.limit; //页面大小
        temp.page = params.offset / params.limit; //页码
    }
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/list?syncImageList=syncImageList', //请求后台的URL（*）
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
        singleSelect: true, //多选框是否单选
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: list.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list.columns(),
        onLoadError: function (status) {
            ajaxError(status);
        }
    });
};

list.select = function (layerTips) {
    var rows = list.table.bootstrapTable('getSelections');
    if (rows.length === 1) {
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
        laydate = layui.laydate,
        common = layui.common,
        loading = layui.loading;
    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    $('.date-picker').change(function () {
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if (beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            var queryParams = list.queryParams();
            queryParams.pageNumber = 1;
            list.table.bootstrapTable('refresh', queryParams);
        }
    });

    /**
     * 查看
     */
    $('#btn_view').on('click', function () {
        if (list.select(layer)) {
            var id = list.currentItem.id;
            var billType = list.currentItem.billType;
            parent.tab.tabAdd({
                title: '业务影像详情',
                href: 'imageInfo/billImageDetail.html?id=' + id + "&billType=" + billType+"&syncImageList=syncImageList"
            });
        }
    });

    /**
     * 补录
     */
    $('#btn_edit').on('click', function () {

        if (list.select(layer)) {
            var id = list.currentItem.id;
            var billType = list.currentItem.billType;
            var imgaeSyncStatus = list.currentItem.imgaeSyncStatus;
            if(imgaeSyncStatus === 'tongBuChengGong'){
                layerTips.msg("上报成功后不能进行补录！");
                return false;
            }
            parent.tab.tabAdd({
                title: '业务影像补录',
                href: 'imageInfo/billImageEdit.html?id=' + id + "&billType=" + billType+"&syncImageList=syncImageList&imgaeSyncStatus="+imgaeSyncStatus
            });
        }
    });


});
