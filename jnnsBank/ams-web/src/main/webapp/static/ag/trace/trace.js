var trace = {
    baseUrl: "../../trace",
    entity: "userTrace",
    tableId: "traceTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};

trace.columns = function () {
    return [{
        field: 'username',
        title: '用户名'
    }, {
        field: 'operateModule',
        title: '操作模块',
        formatter: function (value, row, index) {
            return operateModuleFormat(value)
        }
    }, {
        field: 'operateType',
        title: '操作类型',
        formatter: function (value, row, index) {
            return operateTypeFormat(value)
        }
    }, {
        field: 'operateContent',
        title: '操作内容'
    }, {
        field: 'operateDate',
        title: '操作时间',
        formatter: function (value, row, index) {
            return changeDateFormat(value)
        }
    }, {
        field: 'operateResult',
        title: '操作结果',
        formatter: function (value) {
            return value ? "成功" : "失败";
        }
    }];
};

trace.queryParams = function (params) {
    if (!params)
        return {
            username : $("#username").val(),
            operateModule: $("#operateModule").val(),
            operateType: $("#operateType").val(),
            operateResult: $("#operateResult").val(),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        username : $("#username").val(),
        operateModule: $("#operateModule").val(),
        operateType: $("#operateType").val(),
        operateResult: $("#operateResult").val(),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val()
    };
    return temp;
};
trace.init = function () {
    trace.table = $('#' + trace.tableId).bootstrapTable({
        url: trace.baseUrl + '/query', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + trace.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: trace.order, //排序方式
        queryParams: trace.queryParams,//传递参数（*）
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
        uniqueId: trace.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: trace.columns(),
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};
trace.select = function (layerTips) {
    var rows = trace.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        trace.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    trace.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    //初始化日期控件
    var laydateTask =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = trace.queryParams();
        queryParams.page = 1;
        trace.table.bootstrapTable('refresh', queryParams);
    });

});

function operateModuleFormat(value) {
    if(value=="ROLE"){
        return "角色管理";
    }else if (value=="ANNUAL"){
        return "年检管理";
    } else if (value=="USER"){
        return "用户管理";
    } else if (value=="PERMISSION"){
        return "菜单管理";
    } else if (value=="ORGANIZATION"){
        return "组织机构管理";
    } else if (value=="ANNOUNCEMENT"){
        return "公告管理";
    } else if (value=="ADMINLOG"){
        return "日志管理";
    } else if (value=="DICTIONARY"){
        return "数据字典管理";
    } else if (value=="CONFIG"){
        return "系统配置管理";
    } else if (value=="NOTICE"){
        return "证件到期提醒";
    } else {
        return "未知";
    }
}

function operateTypeFormat(value) {
    if(value=="INSERT"){
        return "新建";
    }else if (value=="UPDATE"){
        return "修改";
    }else if (value=="DELETE"){
        return "删除";
    }else if (value=="SELECT"){
        return "搜索";
    }else if (value=="IMPORT"){
        return "导入";
    }else if (value=="EXPORT"){
        return "导出";
    }else if (value=="DISABLE"){
        return "禁用";
    }else if (value=="ENABLE"){
        return "启用";
    }else if (value=="START"){
        return "启动";
    }else if (value=="PAUSE"){
        return "暂停";
    }else if (value=="END"){
        return "结束";
    }else if (value=="OTHER"){
        return "其他";
    }else {
        return "未知";
    }
}