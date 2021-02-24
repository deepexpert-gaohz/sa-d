var apply = {
    baseUrl: "../../allBillsPublic",
    entity: "user",
    tableId: "dsbTable",
    toolbarId: "toolbar",
    unique: "applyid",
    order: "desc",
    currentItem: {}
};
apply.columns = function () {
    return [{
        radio: true
    }, {
        field: 'id',
        title: 'ID',
        visible: false
    }, {
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '存款人名称',
        'class': 'W200'
    }, {
        field: 'acctType',
        title: '账户性质',
        'class': 'W120',
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    }, {
        field: 'billType',
        title: '操作类型',
        formatter: function (value, row, index) {
            return changeBillType(value)
        }
    }, {
        field: 'kernelOrgCode',
        title: '网点机构号'
    //}, {
    //    field: 'status',
    //    title: '审核状态',
    //    formatter: function (value, row, index) {
    //        return changeStatus(value)
    //    }
    }, {
        field: 'pbcSyncStatus',
        title: '账管上报状态',
        formatter: function (value, row, index) {
            return formatSyncStatus(value)
        }
    }, {
        field: 'eccsSyncStatus',
        title: '信用代码证上报状态',
        formatter: function (value, row, index) {
            return formatSyncStatus(value)
        }
    }, {
        field: 'createdDate',
        title: '申请日期',
        width: '150px',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateStr(value)
        }
    }, {
        field: 'createdBy',
        title: '申请人',
        width: '150px',
    }, {
        field: 'pbcSyncMethod',
        title: '上报操作方式',
        formatter: function (value, row, index) {
            return formatSyncOperateType(value)
        }
    }, {
        field: 'openAccountSiteType',
        title: '本地异地标识',
        formatter: function (value, row, index) {
            return formatOpenAccountSiteType(value)
        }
    }];
};
apply.queryParams = function (params) {
    if (!params)
        return {
            depositorName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
            billType: $("#billType").val(),
            //bankCode: $("#bankCode").val(),
            kernelOrgCode: $("#kernelOrgCode").val(),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
            createdBy: $("#createdBy").val(),
            openAccountSiteType: $("#openAccountSiteType").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        depositorName: $.trim($("#name").val()),
        acctNo: $.trim($("#acctNo").val()),
        acctType: $("#acctType").val(),
        billType: $("#billType").val(),
        //bankCode: $("#bankCode").val(),
        kernelOrgCode: $("#kernelOrgCode").val(),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        createdBy: $("#createdBy").val(),
        code : 'dsbCheck',
        openAccountSiteType: $("#openAccountSiteType").val()
    };
    return temp;
};

apply.init = function () {

    apply.table = $('#' + apply.tableId).bootstrapTable({
        url: apply.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: apply.order, //排序方式
        queryParams: apply.queryParams,//传递参数（*）
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
        uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: apply.columns(),
        // onDblClickRow:function (items,$element) {
        //     var id = items.id;
        //     var depositorName = items.depositorName;
        //     parent.tab.tabAdd({
        //         title: '开户-'+depositorName,
        //         href: 'bank/view.html?id='+id+'&depositorName='+encodeURI(depositorName)
        //     });
        // },
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};
apply.select = function (layerTips) {
    var rows = apply.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        apply.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','layer'], function () {
    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;


    var laydateArr = beginEndDateInit2(laydate, 'beginDate', 'endDate', 'yyyy-MM-dd HH:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            laydateArr[i].config.max = {
                date: 31,
                hours: 0,
                minutes: 0,
                month: 11,
                seconds: 0,
                year: 2099
            };
            laydateArr[i].config.min = {
                date: 1,
                hours: 0,
                minutes: 0,
                month: 0,
                seconds: 0,
                year: 1900
            };
        }
    });

    var addBoxIndex = -1;
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber=1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_view').on('click', function () {
        if (apply.select(layer)) {
            var billId = apply.currentItem.id;
            var name = apply.currentItem.depositorName;
            var type = apply.currentItem.acctType;
            var billType = apply.currentItem.billType;
            var acctNo = apply.currentItem.acctNo;
            var buttonType = 'sync';  //按钮操作类型

            var syncEccs;
            var updateType;
            $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
                if (result) {
                    updateType = result.split(',')[0];
                    syncEccs = result.split(',')[1];
                }

                if(billType != 'ACCT_REVOKE') {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType + "&pageCode=dsbAccount"
                    });
                } else {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=sync' + '&syncEccs=' + syncEccs + '&updateType=' + updateType + "&pageCode=dsbAccount" + '&acctNo=' + acctNo
                    });
                }
            });
        }
    });

    $('#btn_delete').on('click', function () {
        if (apply.select(layer)) {
            var billId = apply.currentItem.id;
            deleteBillsAndAccount(billId, function () {
                layerTips.msg('删除成功');
                location.reload();
            });
        }
    });

});