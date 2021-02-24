layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../syncCoreCompare",
    entity: "syncCompareInfo",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '账户名称'
    }, {
        field: 'acctType',
        title: '账户性质',
        formatter: function (value) {
            return typeFormat(value)
        }
    }, {
        field: 'kaixhubz',
        title: '业务类型',
        formatter: function (value) {
            return bztypeFormat(value)
        }
    },{
        field: 'businessDate',
        title: '业务日期'
    }, {
        field: 'organCode',
        title: '开户机构号'
    },{
        field: 'pbcStarts',
        title: '人行上报状态',
        formatter: function (value, row, index) {
            var typeMap = {
                'tongBuChengGong':'上报成功',
                'tongBuShiBai':'上报失败',
                'buTongBu':'无需上报',
                'weiShangBao':'未上报',
                'tongBuZhong':'处理中',
                'tuiHui':'退回'
            }
            return typeMap[value] || '不同步';
        }
    }, {
        field: 'eccsStarts',
        title: '信用代码证上报状态',
        formatter: function (value, row, index) {
            var typeMap = {
                'tongBuChengGong':'上报成功',
                'tongBuShiBai':'上报失败',
                'buTongBu':'无需上报',
                'weiShangBao':'未上报',
                'tongBuZhong':'处理中',
                'tuiHui':'退回'
            }
            return typeMap[value] || '不同步';
        }
    }];
};

$('#list').on('click', '.viewinfo', function () {
    var acctNo = $(this).attr('data-acctNo');
    var depositorName = $(this).attr('data-depositorName');
    var href = '../../syncCoreCompare/details?acctNo='+acctNo;
    parent.tab.tabAdd({
        title: '账户详情-' + depositorName,
        href: href
    });
    return false;
});

function bztypeFormat(value) {
    var typeMap = {
        '01': '开户',
        '02': '销户',
        '03': '变更',
    }
    return typeMap[value] || '';
}
function typeFormat(value) {
    var typeMap = {
        'jiben': '基本存款账户',
        'yiban': '一般存款账户',
        // 'yusuan':'预算单位专用存款账户',
        // 'feiyusuan':'非预算单位专用存款账户',
        // 'teshu':'特殊单位专用存款账户',
        'zhuanhu':'专用存款账户',
        'linshi':'临时存款账户',
        // 'feilinshi':'非临时机构临时存款账户',
        // 'yanzi':'验证户临时存款账户',
        // 'zengzi':'增资户临时存款账户'
    }
    return typeMap[value] || '';
}

list.queryParams = function (params) {
    if (!params)
        return {
            acctNo: $.trim($("#acctNo").val()),
            businessbeginDate: $("#businessbeginDate").val(),
            businessendDate: $("#businessendDate").val(),
            businessDate: $("#businessDate").val(),
            organCode: $("#organCode").val(),
            depositorName: $("#depositorName").val(),
            acctType: $("#acctType").val(),
            kaixhubz: $("#kaixhubz").val(),
            eccsStarts: $("#eccsStarts").val(),
            pbcStarts: $("#pbcStarts").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        acctNo: $.trim($("#acctNo").val()),
        organCode: $("#organCode").val(),
        depositorName: $("#depositorName").val(),
        acctType: $("#acctType").val(),
        businessDate: $("#businessDate").val(),
        businessbeginDate: $("#businessbeginDate").val(),
        businessendDate: $("#businessendDate").val(),
        kaixhubz: $("#kaixhubz").val(),
        eccsStarts: $("#eccsStarts").val(),
        pbcStarts: $("#pbcStarts").val()
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
        columns: list.columns(),
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

layui.use(['form', 'layedit', 'laydate', 'upload', 'loading'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload,
        loading = layui.loading;
    var addBoxIndex = -1;

    $('.date-picker').change(function () {
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if (beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            list.table.bootstrapTable('refresh', list.queryParams());
        }
    });

    // $('#list').on('click', '.view', function () {
    //     var id = $(this).attr('data-id');
    //     var acctType = $(this).attr('data-acctType');
    //     var depositorName = $(this).attr('data-depositorName');
    //     var href = '';
    //     if (acctType == 'jiben' || acctType == 'linshi' || acctType == 'teshu') {
    //         href = '../ui/validate/detail.html?type=' + acctType;
    //     } else {
    //         href = '../ui/validate/detailPBOC.html?type=' + acctType
    //     }
    //     parent.tab.tabAdd({
    //         title: '开户报备-' + depositorName,
    //         href: href
    //     });
    //     return false;
    // });
    $('#btn_compare').on('click', function () {
        layer.confirm('确定同步数据吗？', null, function (index) {
            $.ajax({
                url: list.baseUrl + "/sync",
                type: "GET",
                success: function (data) {
                    if (data.code === 'ACK') {
                        layerTips.msg("同步成功！");
                        location.reload();
                    } else {
                        layerTips.msg("同步失败！" + data.message)
                        location.reload();
                    }
                }
            });
        });
    });
});

