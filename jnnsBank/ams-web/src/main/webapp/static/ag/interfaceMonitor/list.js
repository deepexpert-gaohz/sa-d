var list = {
    baseUrl: "../../statistics",
    entity: "messageLog",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'tranType',
        title: '接口调用类型',
        formatter: function (value, row, index) {
            return operateType(value);
        }
    }, {
        field: 'acctNo',
        title: '账号'
    },{
        field: 'processResult',
        title: '接口校验结果',
        formatter: function (value, row, index) {
            return stateFormat(value);
        }
    }, {
        field: 'tranTime',
        title: '接口调用时间'
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
            processResult: $("#processResult").val(),
            acctNo: $("#acctNo").val(),
            tranType:$("#tranType").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: (params.offset / params.limit), //页码
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        processResult: $("#processResult").val(),
        acctNo: $("#acctNo").val(),
        tranType:$("#tranType").val()
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/page', //请求后台的URL（*）
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
        columns: list.columns(),
        // responseHandler: function (res) {
        //     if (res.code === 'ACK') {
        //         console.log(res.data.size);
        //         console.log(res.data);
        //         return {total:res.data.content.size, rows: res.data.content};
        //     } else {
        //         layer.msg('查询失败');
        //         return false;
        //     }
        // }
        onLoadError: function(status){
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

layui.use(['form', 'layedit', 'laydate', 'laytpl'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer,
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    staticInit('','','','','');

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_search').on('click', function () {
        var beginDate = $('#beginDate').val();
        var endDate = $('#endDate').val();
        var processResult = $("#processResult").val();
        var acctNo = $("#acctNo").val();
        var tranType =$("#tranType").val();
        staticInit(beginDate,endDate,processResult,tranType,acctNo);
        var queryParams = list.queryParams();
        queryParams.pageNumber=1;
        list.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var processResult = $("#processResult").val();
        var acctNo = $("#acctNo").val();
        var tranType =$("#tranType").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            staticInit(beginDate,endDate,processResult,tranType,acctNo);
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh', queryParams);
        }
    });

    $("#btn_download").on("click", function () {

        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var processResult = $("#processResult").val();
        var acctNo = $("#acctNo").val();
        var tranType =$("#tranType").val();
        $("#downloadFrame").prop('src', list.baseUrl + '/download?beginDate=' + beginDate + '&endDate=' + endDate + '&processResult=' + processResult + '&acctNo=' + acctNo + '&tranType=' + tranType);
        return false;
    });

    $('#list').on('click','.view', function () {
        var id = $(this).attr('data-id');
        if(id){
            $.get('../../statistics/view/' +id ).done(function (response) {
                var data = response.result;
                if (data) {
                    $.get('detail.html', null, function (template) {
                        if(template){
                            laytpl(template).render(data,function (html) {

                                layer.open({
                                    area: '800px',
                                    type: 1,
                                    title: '接口监控记录信息',
                                    content: html
                                });
                            });
                        }
                    });
                }
            });
        }
        return false;
    });

});

function staticInit(beginDate,endDate,processResult,tranType,acctNo) {
    var successCount;
    var failCount;
    var chartData;
    $.get('../../statistics/findByDate?beginDate=' + beginDate + '&endDate=' + endDate + '&processResult=' + processResult + '&tranType=' + tranType + '&acctNo=' + acctNo).done(function (response) {
        var res = response.result;
        successCount = res.successCount;
        failCount = res.failCount;
        chartData = [
            {value:failCount, name:'失败数'},
            {value:successCount, name:'成功数'}
        ];
        LoadChart('接口报送统计', chartData);
    });
}

function LoadChart(title, data) {
    var dom = document.getElementById("echarts");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title : {
            text: title,
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['成功数', '失败数']
        },
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:data,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

function acctTypeFormat(value) {
    var map = {
        'jiben': '基本存款账户',
        'yiban': '一般存款账户',
        'yusuan': '预算单位专用存款账户',
        'feiyusuan': '非预算单位专用存款账户',
        'teshu': '特殊单位专用存款账户',
        'linshi': '临时机构临时存款账户',
        'feilinshi': '非临时机构临时存款账户',
        'yanzi': '验资户临时存款账户',
        'zengzi': '增资户临时存款账户'
    }
    return map[value] || '';
}

function operateType(value) {
    var map = {
        'AMSCheck': '人行查询',
        'AMSSync': '人行报送',
        'ECCS': '机构代码证系统',
        'SAIC': '工商校验接口',
    }
    return map[value] || '';
}

function billType(value) {
    var map = {
        'ACCT_OPEN': '开户',
        'ACCT_CHANGE': '变更',
        'ACCT_REVOKE': '销户',
        'ACCT_SUSPEND': '久悬',
    }
    return map[value] || '';
}

function stateFormat(value) {
    var map = {
        'SUCCESS': '成功',
        'FAIL': '失败'
    }
    return map[value] || '';
}