var riskdataStatic = {
    baseUrl: "../../riskData",
    tableId: "riskdataStaticTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    modelIdOptions: '',
    roleOptions: ''
};
/**
 * 获取页面上的前一天时间
 * @type {string|*}
 */
var laydate = layui.laydate;
var curDate = new Date();
var preDate = new Date(curDate.getTime() - 0).Format("YYYY-MM-dd"); //前一天
var nextDate = new Date(curDate.getTime() - 0).Format("YYYY-MM-dd"); //后一天
var startEndDate = preDate + " ~ " + nextDate
$("#startEndTime").val(startEndDate)
riskdataStatic.columns = function () {
    return [
        {
            field: 'ordNum',
            title: '序号'
        }, {
            field: 'accountNo',
            title: '账号'
        }  ,{
            field: 'organCode',
            title: '机构号'
        },{
            field: 'acctName',
            title: '账户名称'
        }, {
            field: 'riskDate',
            width: '100',
            title: '数据日期',
            formatter: function (value, row, index) {
                if (value == "" || value == null) {
                    return "";
                } else {
                    return new Date(value).Format("yyyy-MM-dd");
                }
            }

        }, {
            field: 'modelId',
            title: '模型编号',
            visible: false
        }, {
            field: 'riskType',
            title: '风险类型',
            visible: false
        }, {
            field: 'name',
            title: '模型名称'
        }, {
            field: 'riskDesc',
            title: '风险描述',
            visible: false
        }, {
            field: 'riskPoint',
            title: '风险点'
        },{
            field: "riskdataStaticTable",
            title: '操作',
            width: '100',
            formatter: function operateFormatter(e, value, row) {
                var butVal = "";
                butVal = "<a style='color: #4CAF50' href='#' class='queryRiskData'>查看</a>";
                return butVal;
            },
            events: {
                'click .handleRisk': function (e, value, row) {
                    handleRiskInfo(row);
                }, 'click .queryRiskData': function (e, value, row) {
                    queryRiskData(row)
                }
            }
        }];
};


/**
 * 查询风险详细信息
 * @param row
 */
function queryRiskData(row) {
    $.get('edit.html', null, function (form) {
        addBoxIndex = layer.open({
            type: 1,
            title: row.name,
            content: form,
            shade: false,
            area: ['100%', '100%'],
            async: false,
            success: function () {
                riskDataCheck.init(row.modelId, row.riskPoint, row.status);
                var form = layui.form;
                form.render();
            }
        });
    });
}

riskdataStatic.queryParams = function (params) {
    if (!params)
        return {
            status: $("#status").val(),
            modelName: $("#modelName").val(),
            riskType: $("#riskType").val(),
            startEndTime: $("#startEndTime").val(),
            accountNo: $("#accountNo").val(),
            organCode: $("#organCode").val(),
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit) + 1, //页码
        status: $("#status").val(),
        modelName: $("#modelName").val(),
        riskType: $("#riskType").val(),
        startEndTime: $("#startEndTime").val(),
        accountNo: $("#accountNo").val(),
        organCode: $("#organCode").val(),
    };

    return temp;
};

riskdataStatic.init = function () {

    riskdataStatic.table = $('#' + riskdataStatic.tableId).bootstrapTable({
        url: riskdataStatic.baseUrl + '/queryRiskStaticData', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + riskdataStatic.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: riskdataStatic.order, //排序方式
        queryParams: riskdataStatic.queryParams,//传递参数（*）
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
        uniqueId: riskdataStatic.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: riskdataStatic.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total: res.data.totalRecord, rows: res.data.list};
            } else {
                return false;
            }
        }
        , onLoadError: function (status) {
            ajaxError(status);
        }
    });
};
laydate.render({
    elem: '#startEndTime'
    , type: 'date'
    , range: '~'
    , format: 'yyyy-MM-dd'
});

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var form = layui.form;
    riskdataStatic.init();
    $.get("../../model/findTypeNameAsChange", function (res) {
        if (res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.list.length; i++) {
                options += '<option value="' + res.data.list[i].modelId + '" >' + res.data.list[i].name + '</option>';
                $('#modelName').append('<option value="' + res.data.list[i].modelId + '" >' + res.data.list[i].name + '</option>');
            }
        }
        form.render();
    });
    $.get("../../modelKind/findTypeAll", function (res) {
        if (res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].typeName + '</option>';
                $('#riskType').append('<option value="' + res.data[i].id + '" >' + res.data[i].typeName + '</option>');
            }
            form.render();
        }
    });

    $('#btn_query').on('click', function () {
        var queryParams = riskdataStatic.queryParams();
        queryParams.pageNumber = 1;
        riskdataStatic.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_export').on('click', function () {

        var dateStr = $("#startEndTime").val();
        layer.confirm('确定导出数据吗？', null, function (index) {
            window.location.href = riskdataStatic.baseUrl + "/expRiskDataOpenAccount";
            layer.close(index);
        });
    });
});