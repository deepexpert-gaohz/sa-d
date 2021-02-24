var list = {
    baseUrl: "../../illegalQuery",
    entity: "user",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'organCode',//companyName
        title: '机构号'
    }, {
        field: 'companyName',//companyName
        title: '企业名称'
    }, {
        field: 'regNo',//regNo
        title: '工商注册号'
    },{
        field: 'fileEndDate',//
        title: '营业执照到期日'
    }, {
        field: 'fileDueExpired',//fileDue
        title: '营业执照到期状态',
        formatter: function (value, row, index) {
            return fileDue(value)
        }
    },{
        field: 'saicStatus',//
        title: '工商状态'
    }, {
        field: 'changemess',//
        title: '是否经营异常',
        formatter: function (value, row, index) {
            return illegalStatusName(value)
        }
    }, {
        field: 'illegalStatus',//illegalStatus
        title: '严重违法状态',
        formatter: function (value, row, index) {
        return illegalStatusName(value)
    }
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" href="#" data-id="'+value+'" data-companyName="'+row.companyName+'">再次查看</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            organCode: $.trim($("#organCode").val()),
            companyName: $.trim($("#name").val()),
            regNo: $.trim($("#regNo").val()),
            fileEndDate: $.trim($("#fileEndDate").val()),
            saicStatus: $.trim($("#saicStatus").val()),
            changemess: $.trim($("#changemess").val()),
            fileDueExpired: $.trim($("#fileDueExpired").val()),
            illegalStatus: $("#status").val(),
            illegalQueryBatchId: $("#batchId").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        organCode: $.trim($("#organCode").val()),
        companyName: $.trim($("#name").val()),
        regNo: $.trim($("#regNo").val()),
        saicStatus: $.trim($("#saicStatus").val()),
        changemess: $.trim($("#changemess").val()),
        fileDueExpired: $.trim($("#fileDueExpired").val()),
        illegalStatus: $("#status").val(),
        illegalQueryBatchId: $("#batchId").val()
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/queryList', //请求后台的URL（*）
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
        ,onLoadError: function(status){
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

layui.use(['form', 'layedit', 'laydate', 'upload','common'], function () {
    var common = layui.common;

    var batchId = decodeURI(common.getReqParam("illegalQueryBatchId"));
    $("#batchId").val(batchId);

    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $('#btn_query').on('click', function () {
        var queryParams = list.queryParams();
        queryParams.pageNumber=1;
        list.table.bootstrapTable('refresh', queryParams);
    });

    $("#btn_download").on("click", function () {
        $.get(list.baseUrl + '/checkIllegalStatus?batchId=' + batchId,function(data) {
            if(data.rel == false) {
                layer.confirm('正在进行批量企业违法信息查询，确认下载吗？', null, function (index) {
                    layer.close(index);
                    $("#downloadFrame").prop('src', list.baseUrl + '/download?illegalQueryBatchId=' + batchId);

                });
            } else {
                $("#downloadFrame").prop('src', list.baseUrl + '/download?illegalQueryBatchId=' + batchId);
            }

        });

        return false;
    });

    $('#list').on('click','.view', function () {
        var id = $(this).attr('data-id');
        var companyName = $(this).attr('data-companyName');

        $.get(list.baseUrl + '/illegalQueryCheck?id=' + id + "&keyword=" + encodeURI(companyName), function (data) {
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh', queryParams);
            if(data.rel == true) {
                if(data.result.illegalStatus != null) {
                    if(data.result.changemess == true){
                        layer.alert('该企业严重违法校验结果为' + illegalStatusName(data.result.illegalStatus) + '并存在经营异常...');
                    }else{
                        layer.alert('该企业严重违法校验结果为' + illegalStatusName(data.result.illegalStatus));
                    }
                }
            } else {
                layer.alert('获取该企业严重违法校验结果失败');
            }

        });

        return false;
    });

});