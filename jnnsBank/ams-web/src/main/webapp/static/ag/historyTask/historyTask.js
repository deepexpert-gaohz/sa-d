var task = {
    baseUrl: "../../task",
    entity: "taskDto",
    tableId: "taskTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: '',
    comment:[]
};
task.columns = function () {
    return [ {
        field: "",
        title: '操作',
        formatter: function operateFormatter(e, value, row){
            return "<a style='color: #4CAF50' id='viewTask'>查看任务</a>" ;
        },
        events: {
            'click #viewTask': function (e, value, row) {
                task.proVal = row.processInstanceId;
                queryHisTaskData(row);
            },
        }
    },{
        field: 'name',
        title: '案例名称'
    }, {
        field: 'flowName',
        title: '流程名称'
    }, {
        field: 'startTime',
        title: '开始时间'
    }, {
        field: 'endTime',
        title: '结束时间'
    },{
        field: 'status',
        title: '状态',
        formatter : function (value) {
            if(value=='finish'){
                return "结束";
            }
        }
    }];
};

task.queryParams = function (params) {
    if (!params)
        return {
            name: $("#title").val(),
            startEndTime : $("#startEndTime").val(),

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        name: $("#title").val(),
        startEndTime : $("#startEndTime").val(),
    };
    return temp;
};
task.init = function () {

    task.table = $('#' + task.tableId).bootstrapTable({
        url: task.baseUrl + '/queryHistoryTask', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + task.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: task.order, //排序方式
        queryParams: task.queryParams,//传递参数（*）
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
        uniqueId: task.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: task.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
task.select = function (layerTips) {
    var rows = task.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        user.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    task.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = task.queryParams();
        queryParams.pageNumber=1;
        task.table.bootstrapTable('refresh', queryParams);
    });
});
var procInsId;
function queryHisTaskData(row){
    $.get('edit.html', null, function (form) {
        addBoxIndex = layer.open ({
            type: 1,
            title: '查看已办任务',
            content: form,
            shade: 0.1,
            area: ['100%', '100%'],
            async: 0.1,
            success: function () {
                procInsId= row.processInstanceId;
                historyTaskData.init(row.countId,row.id,row.processInstanceId,row.processDefinitionId);
                queryComment(procInsId);
            }
        });
    });
}
