layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var customerHistory = {
    baseUrl: "../../customerAbnormal",
    tableId: "abnormalHistoryTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "desc",
    currentItem: {},
    depositorName: ""
};

customerHistory.init = function () {

    customerHistory.table = $('#' + customerHistory.tableId).bootstrapTable({
        url: customerHistory.baseUrl + '/pageHistory', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + customerHistory.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: true, //是否启用排序
        sortOrder: "desc", //排序方式
        sortName: 'lastUpdateDate',
        queryParams: customerHistory.queryParams,//传递参数（*）
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
        uniqueId: customerHistory.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: customerHistory.columns()
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};

customerHistory.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'depositorName',
        title: '客户名称'
    }, {
        field: 'organName',
        title: '银行机构名称'
    }, {
        field: 'code',
        title: '银行机构代码'
    }, {
        field: 'abnormalTime',
        title: '系统异动时间'
    }, {
        field: 'illegal',
        title: '严重违法',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='illegalView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'changeMess',
        title: '经营异常',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='changeMessView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'businessExpires',
        title: '经营到期',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='businessExpiresView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    },{
        field: 'abnormalState',
        title: '工商状态异常',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='abnormalStateView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    },{
        field: 'changed',
        title: '登记信息异动',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='changedView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'processState',
        title: '处理状态',
        formatter: function (value, row, index) {
            if (value == null) {
                return '待处理';
            } else if (value === 'underway') {
                return '处理中';
            } else if (value === 'finish') {
                return '已处理';
            } else {
                return value;
            }
        }
    }, {
        field: 'processTime',
        title: '处理时间'
    }, {
        field: 'processer',
        title: '处理人'
    }, {
        field: 'message',
        title: '短信发送状态',
        formatter: function (value, row, index) {
            if (value=="1") {
                return '发送成功';
            } else if (value=="2") {
                return '发送失败';
            }else {
                return '未发送';
            }
        }
    }];
};

customerHistory.queryParams = function (params) {
    var temp = {
        depositorName: customerHistory.depositorName

    };
    if (params) {
        temp.size = params.limit; //页面大小
        temp.page = params.offset / params.limit; //页码
    }
    return temp;
};

layui.use(['form', 'layedit', 'laydate','common'], function () {

    var layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    layerTips = parent.layer === undefined ? layui.layer : parent.layer; //获取父窗口的layer对象

    document.getElementById("paging_container").width = document.body.offsetWidth
        - 40 + 'px';

    customerHistory.depositorName = decodeURI(common.getReqParam("depositorName"));

    customerHistory.init();

});


