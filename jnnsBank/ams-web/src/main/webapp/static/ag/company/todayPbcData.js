layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

layui.use(['form', 'layedit', 'common', 'laydate', 'layer'], function () {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        common = layui.common,
        laydate = layui.laydate;

    var apply = {
        baseUrl: "../../allBillsPublic",
        entity: "user",
        tableId: "todayPbcTable",
        unique: "createdDate",
        order: "desc",
        currentItem: {},
        columnsData: []
    };

    laydate.render({
        elem: '#startDate',
        format: 'yyyy-MM-dd'
    });
    laydate.render({
        elem: '#endDate',
        format: 'yyyy-MM-dd'
    });

    apply.columns = function () {
        return apply.columnsData;
    };
    apply.queryParams = function (params) {
        var temp = {
            startDate: $("#startDate").val(),
            endDate: $("#endDate").val()
        };
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };

    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/statisticsForDateList', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: apply.queryParams,//传递参数（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
            search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            singleSelect: true,
            showColumns: false, //是否显示所有的列
            showRefresh: true, //是否显示刷新按钮
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: apply.columns(),
            onLoadError: function (status) {
                ajaxError(status);
            }
        });
    };

    var autoColumn = [{
        field: 'createdDate',
        title: '日期'
    }, {
        field: 'allNum',
        title: '当日增量业务数'
    }, {
        field: 'pbcNumAll',
        title: '需上报人行'
    }, {
        field: 'pbcNum',
        title: '实际上报人行数'
    }, {
        field: 'pbcNumAll',
        title: '全部上报人行',
        formatter: function (value, row, index) {
            if(row.pbcNumAll != 0) {
                return row.pbcNumAll === row.pbcNum ? '是' : '否';
            } else {
                return '是';
            }
        }
    }, {
        field: 'eccsNumAll',
        title: '需上报信用代码数'
    }, {
        field: 'eccsNum',
        title: '实际上报信用代码数'
    }, {
        field: 'eccsNumAll',
        title: '全部上报信用代码',
        formatter: function (value, row, index) {
            if(row.eccsNumAll != 0) {
                return row.eccsNumAll === row.eccsNum ? '是' : '否';
            } else {
                return '是';
            }

        }
    }, {
        field: 'createdDate',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="' + value + '">查看详情</a>';
        }
    }];
    apply.columnsData = autoColumn;
    apply.init();

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber = 1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_downLoad').on('click', function () {
        var startdate = $("#startDate").val();
        var enddate = $("#endate").val();
        if((startdate === undefined || startdate === '') && (enddate === undefined || enddate === '')){
            layerTips.msg("日期不能为空");
            return;
        }else{
            var dataUrl ='/statisticsForDateDetailsExport?startdate=' + $("#startDate").val() + '&enddate=' + $("#endDate").val();
            location.href = apply.baseUrl + dataUrl;
        }
    });

    $('#todayPbcTable').on('click', '.view', function () {
        var reportDate = $(this).attr('data-id');
        var row = apply.table.bootstrapTable('getRowByUniqueId', reportDate);
        if (reportDate === undefined || reportDate === '') {
            layerTips.msg("日期不能为空");
        } else {
            var isAlert;
            if (row.pbcNumAll === row.pbcNum && row.eccsNumAll === row.eccsNum) {
                isAlert = 'false';
            }else{
                isAlert = 'true';
            }
            parent.tab.tabAdd({
                title: reportDate + '上报明细列表',
                href: '../ui/company/todayPbcDataDetail.html?reportDate=' + encodeURI(reportDate) + '&isAlert=' + isAlert
            });
        }
        return false;
    });
});
