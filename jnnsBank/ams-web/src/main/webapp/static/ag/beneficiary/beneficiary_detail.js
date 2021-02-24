var list = {
    baseUrl: "../../beneficiary",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'batchNo',
        title: '批次号',
        visible: false
    }, {
        field: 'customerNo',
        title: '客户号'
    }, {
        field: 'customerName',
        title: '客户名称'
    }, {
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'organCode',
        title: '核心机构号'
    }, {
        field: 'isSame',
        title: '是否一致',
        formatter: function (value) {
            return value ? '一致' : '不一致'
        }
    }, {
        field: 'beneficiaryName1',
        title: '受益人名称1'
    }, {
        field: 'beneficiaryRatio1',
        title: '受益人比例1',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName2',
        title: '受益人名称2'
    }, {
        field: 'beneficiaryRatio2',
        title: '受益人比例2',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName3',
        title: '受益人名称3'
    }, {
        field: 'beneficiaryRatio3',
        title: '受益人比例3',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName4',
        title: '受益人名称4'
    }, {
        field: 'beneficiaryRatio4',
        title: '受益人比例4',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'legalName',
        title: '法定代表人',
        formatter: function (data, row, index) {
            // if (row["beneficiaryName1"] || row["beneficiaryName2"] || row["beneficiaryName3"] || row["beneficiaryName4"]) {
            //     return "-";
            // }
            return data;
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            batchNo: $.trim($("#batchNo").val()),
            customerNo: $.trim($("#customerNo").val()),
            customerName: $.trim($("#customerName").val()),
            acctNo: $.trim($("#acctNo").val()),
            organCode: $.trim($("#organCode").val()),
            isSame: $("#isSame").val(),
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: params.offset / params.limit, //页码
        batchNo: $.trim($("#batchNo").val()),
        customerNo: $.trim($("#customerNo").val()),
        customerName: $.trim($("#customerName").val()),
        acctNo: $.trim($("#acctNo").val()),
        organCode: $.trim($("#organCode").val()),
        isSame: $("#isSame").val(),
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/beneficiary', //请求后台的URL（*）
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
        detailView: true, //是否显示父子表
        columns: list.columns(),
        onExpandRow: function (index, row, $detail) {
            expandTable($detail, row);
        },
        responseHandler: function (res) {
            // return {total: res.total, rows: errorMessageFormat(res.rows)};
            if (res.code === 'ACK') {
                return {total: res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        , onLoadError: function (status) {
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

layui.use(['form', 'layedit', 'laydate', 'upload', 'common'], function () {

    var common = layui.common;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var batchNo = decodeURI(common.getReqParam("batchNo"));
    $("#batchNo").val(batchNo);

    list.init();

    $('#btn_query').on('click', function () {
        var queryParams = list.queryParams();
        queryParams.pageNumber=1;
        list.table.bootstrapTable('refresh', queryParams);
    });

    //结果导出
    $('#btn_export').on('click', function () {

        var params = '';
        var item = list.queryParams();
        for (var key in item) {
            if(item[key] != null){
                params += '&' + key + '=' + item[key];
            }
        }
        params = params.substr(1);
        $("#downloadFrame").prop('src', list.baseUrl + '/beneficiary/exportExcel?' + params);
        return false;
    });

});


function toPercent(point) {
    var str = Number(point * 100).toFixed(1);
    str += "%";
    return str;
}

function expandTable($detail, row) {
    var subCols = [{
        field: 'source',
        title: ''
    }, {
        field: 'customerNo',
        title: '客户号'
    }, {
        field: 'customerName',
        title: '客户名称'
    }, {
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'beneficiaryName1',
        title: '受益人名称1'
    }, {
        field: 'beneficiaryRatio1',
        title: '受益人比例1',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName2',
        title: '受益人名称2'
    }, {
        field: 'beneficiaryRatio2',
        title: '受益人比例2',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName3',
        title: '受益人名称3'
    }, {
        field: 'beneficiaryRatio3',
        title: '受益人比例3',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'beneficiaryName4',
        title: '受益人名称4'
    }, {
        field: 'beneficiaryRatio4',
        title: '受益人比例4',
        formatter: function (data) {
            if (data) {
                return toPercent(data);
            }
            return "-";
        }
    }, {
        field: 'legalName',
        title: '法定代表人',
    },];
    data = [{
        source: "工商信息",
        customerNo: row["customerNo"],
        customerName: row["customerName"],
        acctNo: row["acctNo"],
        beneficiaryName1: row["beneficiaryName1"],
        beneficiaryRatio1: row["beneficiaryRatio1"],
        beneficiaryName2: row["beneficiaryName2"],
        beneficiaryRatio2: row["beneficiaryRatio2"],
        beneficiaryName3: row["beneficiaryName3"],
        beneficiaryRatio3: row["beneficiaryRatio3"],
        beneficiaryName4: row["beneficiaryName4"],
        beneficiaryRatio4: row["beneficiaryRatio4"],
        legalName: row["legalName"],
    }, {
        source: "核心信息",
        customerNo: row["customerNo"],
        customerName: row["customerName"],
        acctNo: row["acctNo"],
        beneficiaryName1: row["coreBeneficiaryName1"],
        beneficiaryRatio1: row["coreBeneficiaryRatio1"],
        beneficiaryName2: row["coreBeneficiaryName2"],
        beneficiaryRatio2: row["coreBeneficiaryRatio2"],
        beneficiaryName3: row["coreBeneficiaryName3"],
        beneficiaryRatio3: row["coreBeneficiaryRatio3"],
        beneficiaryName4: row["coreBeneficiaryName4"],
        beneficiaryRatio4: row["coreBeneficiaryRatio4"],
        legalName: row["coreLegalName"],
    }];
    $detail.html('<table></table>').find('table').bootstrapTable({
        columns: subCols,
        data: data,
        detailView: false
    });
}

