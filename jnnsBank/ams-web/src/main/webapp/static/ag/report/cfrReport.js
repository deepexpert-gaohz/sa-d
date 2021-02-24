var model = {
    baseUrl: "../../report",
    entity: "model",
    tableId: "reportTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions:'',
    year:'',
    quarter:''
};
model.columns = function () {
    return [
        [
            {
                "title": "账户资金流动情况统计表(按企业类型)",
                align:"center",
                colspan: 5
            }
        ],[
            {
                "title":"填制单位:",
                colspan:2
            },{
                "title":"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+model.year+"&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+model.quarter+"&nbsp;&nbsp;季度"
            },{
                "title":"单位:万元"
            }
        ],[
            {
                "title":"企业类型",
                field: 'depositorType',
                align:"center"
            },
            {
                "title":"期初余额",
                field: 'beginPeriodData',
                align:"center"
            },
            {
                "title":"流入",
                field: 'inflowData',
                align:"center"
            },
            {
                "title":"流出",
                field: 'outflowData',
                align:"center"
            },
            {
                "title":"期末余额",
                field: 'endPeriodData',
                align:"center"
            }
        ]
    ];
};

model.queryParams = function (params) {
    if (!params)
        return {
            modelId: $("#modelId").val()

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelId: $("#modelId").val()

    };
    return temp;
};
model.init = function () {
    //获取当前年
    var now = new Date(); //当前日期
    var nowYear = now.getYear();//当前年
    nowYear += (nowYear < 2000) ? 1900 : 0;
    var nowMonth = now.getMonth();//获取当前月
    //判断获取当前季度
    var quarter = 0;
    if(nowMonth>=1 && nowMonth<=3){
        quarter = 1;
    }
    if(4<=nowMonth && nowMonth<=6){
        quarter = 2;
    }
    if(7<=nowMonth && nowMonth<=9){
        quarter = 3;
    }
    if(10<=nowMonth && nowMonth<=12){
        quarter = 4;
    }
    model.year= nowYear;
    model.quarter = quarter;
model.table = $('#' + model.tableId).bootstrapTable({
        url: model.baseUrl+"/financialLiquReport", //请求后台的URL（*）
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
                console.log(res);
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


layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    model.init();
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
        var queryParams = model.queryParams();
        queryParams.pageNumber=1;
        model.table.bootstrapTable('refresh', queryParams);
    });
    /*导出报表*/
    $("#exportBtn").click(function () {
        var year = model.year;
        var season = model.quarter;
        $("#downloadFrame").prop('src', model.baseUrl + "/exportfinancialLiquXLS?season="+ season+"&year="+year);
    })

});