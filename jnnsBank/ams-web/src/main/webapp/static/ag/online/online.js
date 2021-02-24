var online = {
    baseUrl: "../../online",
    entity: "online",
    tableId: "onlineTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};
online.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'username',
        title: '用户名'
    }, {
        field: 'cname',
        title: '姓名'
    }, {
        field: 'roleName',
        title: '角色'
    }, {
        field: 'orgName',
        title: '所属机构'
    }];
};

online.queryParams = function (params) {
    if (!params)
        return {
            name: $("#name").val(),
            username: $("#username").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        name: $("#name").val(),
        username: $("#username").val()
    };
    return temp;
};
online.init = function () {

    online.table = $('#' + online.tableId).bootstrapTable({
        url: online.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + online.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: online.order, //排序方式
        queryParams: online.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: false, //是否启用点击选中行
        uniqueId: online.id, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: online.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
// online.select = function (layerTips) {
//     var rows = online.table.bootstrapTable('getSelections');
//     if (rows.length == 1) {
//         online.currentItem = rows[0];
//         return true;
//     } else {
//         layerTips.msg("请选中一行");
//         return false;
//     }
// };

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    online.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;


    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = online.queryParams();
        queryParams.pageNumber=1;
        online.table.bootstrapTable('refresh', queryParams);
    });

});