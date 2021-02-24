layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var csrMessage = {
    baseUrl: "../../customerAbnormal/message",
    tableId: "csrMessageTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "desc",
    currentItem: {}
};
csrMessage.columns = function () {
    return [{
        field: 'id',
        title: 'ID',
        visible: false
    }, {
        field: 'depositorName',
        title: '客户名称'
    },{
        field: 'phone',
        title: '手机号'
    }, {
        field: 'createdDate',
        title: '发送时间',
        formatter: function (value, row, index) {
            return formatDate(value,"yyyy-MM-dd HH:mm:ss");
        }
    }, {
        field: 'checkPass',
        title: '是否成功',
        formatter: function (value, row, index) {
            return formatCheckPass(value);
        }
    },{
        field: 'errorMessage',
        title: '失败原因',
        'class': 'W200'
    },{
        field: 'operate',
        title: '操作',
        'class': 'W60',
        formatter: function (value, row, index) {
            return "<a href=\"javascript:void(0);\" style='color: blue' onclick='viewMessage(\"" + row.message + "\")'>查看短信内容</a>";
        }
    }];
};
csrMessage.queryParams = function (params) {
    if (!params)
        return {

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码

    };
    return temp;
};

csrMessage.init = function () {

    csrMessage.table = $('#' + csrMessage.tableId).bootstrapTable({
        url: csrMessage.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + csrMessage.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: csrMessage.order, //排序方式
        singleSelect: true,
        queryParams: csrMessage.queryParams,//传递参数（*）
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
        uniqueId: csrMessage.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: csrMessage.columns(),
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};

csrMessage.select = function (layerTips) {
    var rows = csrMessage.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        csrMessage.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','layer','common'], function () {
    csrMessage.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    var addBoxIndex = -1;

    var tabId = decodeURI(common.getReqParam("tabId"));
    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = csrMessage.queryParams();
        queryParams.pageNumber=1;
        csrMessage.table.bootstrapTable('refresh', queryParams);
    });

});

function viewMessage(message) {
    layer.open({
        type: 1,
        title: '短信内容',
        area: ['400px', '200px'],
        content: message
    });
}