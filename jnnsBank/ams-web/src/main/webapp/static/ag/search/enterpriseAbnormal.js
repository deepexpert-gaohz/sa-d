layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../customerSearch/enterpriseAbnormalNotice",
    entity: "enterpriseAbnormalNotice",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'msgId',
        title: '报文标识号'
    }, {
        field: 'creDtTm',
        title: '报文发送时间'
    // }, {
    //     field: 'instgDrctPty',
    //     title: '发起直接参与机构'
    // }, {
    //     field: 'instgPty',
    //     title: '发起参与机构'
    // },{
    //     field: 'instdDrctPty',
    //     title: '接收直接参与机构'
    // },{
    //     field: 'instdPty',
    //     title: '接收参与机构'
    },{
        field: 'coNm',
        title: '单位名称'
    },{
        field: 'uniSocCdtCd',
        title: '统一社会信用代码'
    },{
        field: 'phNb',
        title: '手机号码'
    },{
        field: 'nm',
        title: '姓名'
    },{
        field: 'abnmlType',
        title: '异常核查类型',
        formatter: function (value, row, index) {
            return abnmlTypeSwitch(value);
        }
    },{
        field: 'desc',
        title: '异常内容说明'
    }
    // , {
    //     field: 'id',
    //     title: '操作',
    //     formatter: function (value, row, index) {
    //         return '<a class="view" href="#" data-id="'+value+'">查看</a> ';
    //     }
    // }
    ];
};

list.queryParams = function (params) {
    if (!params)
        return {

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码

    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/list', //请求后台的URL（*）
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
        columns: list.columns()

    });
};



layui.use(['form', 'layedit', 'laydate', 'upload', 'loading'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload,
        loading = layui.loading;

    var addBoxIndex = -1;



});