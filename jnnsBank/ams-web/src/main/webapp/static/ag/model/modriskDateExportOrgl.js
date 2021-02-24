var model = {
    baseUrl: "../../ModelCountFileController",
    entity: "ModelsExtendDto",
    tableId: "dateTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions:''

};
model.columns = function () {
    return [{
        checkbox:true
    }, {
        field: 'modelId',
        title: '模型编号'
    }, {
        field: 'riskType',
        title: '风险类型'
    }, {
        field: 'name',
        title: '模型名称'
    }, {
        field: 'tjjg',
        title: '统计结果'
    }];
};

model.queryParams = function (params) {
    if (!params)
        return {
            orgName: $("#orgName").val(),
            riskType: $("#riskType").val(),
            modelName: $("#modelName").val(),
            dataDate:$("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        orgName: $("#orgName").val(),
        riskType: $("#riskType").val(),
        modelName: $("#modelName").val(),
        dataDate:$("#startEndTime").val()
    };
    return temp;
};
model.init = function () {
    model.table = $('#' + model.tableId).bootstrapTable({
        url: model.baseUrl + '/expRiskDataDiyView', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + model.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: model.order, //排序方式
        queryParams: model.queryParams,//传递参数（*）
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
        uniqueId: model.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: model.columns(),
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
model.select = function (layerTips) {
    var rows = model.table.bootstrapTable('getSelections');
    if (rows.length !=0) {
        model.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    model.init();
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
        var queryParams = model.queryParams();
        queryParams.pageNumber=1;
        model.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_export_byModel').on('click', function () {
        if (model.select(layerTips)) {

            layer.confirm('确定导出数据吗？', null, function (index) {
                var row = $.map($('#dateTable').bootstrapTable('getSelections'),function (row) {
                    return row.modelId;
                });
                var b=row.toString();

                b = b.replace("\"").replace("[").replace("]")

                //alert($("#orgName").val())
                var orgName = $("#orgName").val();
                var dateStr = $("#startEndTime").val();
               // alert(dateStr);
                //window.location.href=model.baseUrl + "/expRiskDataDiy?ids="+b;

                window.location.href=model.baseUrl + "/expRiskDataDiy?ids="+b+"&dateStr="+dateStr;

                layer.close(index);
            });
        }
    });

    $('#btn_export_byOrg').on('click', function () {

        if (model.select(layerTips)) {

            layer.confirm('确定导出数据吗？', null, function (index) {
                var row = $.map($('#dateTable').bootstrapTable('getSelections'),function (row) {
                    return row.modelId;
                });
                var b=row.toString();

                b = b.replace("\"").replace("[").replace("]")
                var dateStr = $("#startEndTime").val();
                //window.location.href=model.baseUrl + "/expRiskDataByOrg?ids="+b ;

                window.location.href="../../ModelCountFileController/expRiskDataByOrg?ids="+b+"&dateStr="+dateStr;

                layer.close(index);
            });
        }
    });


});



