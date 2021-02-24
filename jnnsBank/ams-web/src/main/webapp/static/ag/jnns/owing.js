
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var user = {
    baseUrl: "../../jnns",
    entity: "user",
    tableId: "userTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};
user.columns = function () {
    return [{
        radio: true
    }, {
        field: 'cid',
        title: '证件号码'
    }, {
        field: 'balance',
        title: '欠税余额'
    }, {
        field: 'datatinme',
        title: '欠税发生时间'
    }, {
        field: 'name',
        title: '当事人姓名或名称'
    }, {
        field: 'organ',
        title: '主管税务机关'
    }, {
        field: 'taxtype',
        title: '所欠税种'
    }, {
        field: 'period',
        title: '欠税属期'
    }, {
        field: 'title',
        title: '标题'
    }, {
        field: 'remark',
        title: '备注'
    }];
};

user.queryParams = function (params) {
    if (!params)
        return {
            cid: $("#cid").val(),
            name : $("#name").val(),
            organ: $("#organ").val(),
            /* checkName: $("#checkName").val(),
             bankCode: $("#bankCode").val()*/
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: (params.offset / params.limit), //页码
        cid: $("#cid").val(),
        name : $("#name").val(),
        organ: $("#organ").val(),
        /* checkName: $("#checkName").val(),
         bankCode: $("#bankCode").val()*/
    };
    return temp;
};
user.init = function () {

    user.table = $('#' + user.tableId).bootstrapTable({
        url: user.baseUrl + '/queryOwingList', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + user.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: user.order, //排序方式
        queryParams: user.queryParams,//传递参数（*）
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
        uniqueId: user.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: user.columns(),
        responseHandler: function (res) {
            // console.log(res)
            return res;
            // if (res.code === 'ACK') {
            // return {total:res.data.totalRecord, rows: res.data.list};
            //         return {total:res.data.totalRecord, rows: res.data.list};
            // } else {
            //     layerTips.msg('查询失败');
            //     return false;
            // }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
user.select = function (layerTips) {
    var rows = user.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        user.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    user.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;





    $.get("../../organization/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++)
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';

            user.orgOptions = options;
        }
    });
    $.get("../../role/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';
                $('#roleId').append('<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>');
            }
            form.render();
            user.roleOptions = options;
        }
    });


    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = user.queryParams();
        queryParams.pageNumber=1;
        user.table.bootstrapTable('refresh', queryParams);
    });




















});