layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form', 'layedit', 'laydate', 'layer', "common"], function () {
    var common = layui.common;
    var form = layui.form;
    var depositorType = decodeURI(common.getReqParam("depositorType"));//存款人类别
    var acctType = decodeURI(common.getReqParam("acctType"));//账户类型
    var beginDate = decodeURI(common.getReqParam("beginDate"));//父类机构号
    var endDate = decodeURI(common.getReqParam("endDate"));//父类机构号
    if (depositorType && depositorType !== 'count') {
        $("#depositorType").val(depositorType);
    }
    if (acctType) {
        $("#acctType").val(acctType);
    }
    form.render();
    var apply = {
        baseUrl: "../../businessStatistics",
        entity: "user",
        tableId: "dshTable",
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
            field: 'depositorType',
            title: '存款人类别',
            formatter: function (value, row, index) {
                return getDepositorTypeName(value);
            }
        }, {
            field: 'kernelOrgCode',
            title: '网点机构号'
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
            field: 'openAccountSiteType',
            title: '本地异地标识',
            formatter: function (value, row, index) {
                return formatOpenAccountSiteType(value)
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
            field: 'createdName',
            title: '申请人'
        }, {
            field: 'pbcSyncMethod',
            title: '上报操作方式',
            formatter: function (value, row, index) {
                return formatSyncOperateType(value)
            }
        }];
    };
    apply.queryParams = function (params) {
        var temp = {
            depositorType: $("#depositorType").val(),
            acctType: $("#acctType").val(),
            beginDate: beginDate,
            endDate: endDate,
            beginDateApply: $("#beginDateApply").val(),
            endDateApply: $("#endDateApply").val(),
            depositorName: $.trim($("#depositorName").val()),
            acctNo: $.trim($("#acctNo").val()),
            billType: $("#billType").val(),
            openAccountSiteType: $("#openAccountSiteType").val(),
            kernelOrgCode: $("#kernelOrgCode").val(),
            createdBy: $("#createdBy").val()
        };
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };

    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/open/detail/list', //请求后台的URL（*）
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

            //     tempToDeatil();
            // },
            onLoadError: function (status) {
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
    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;

    var laydateArr = beginEndDateInit2(laydate, 'beginDateApply', 'endDateApply', 'yyyy-MM-dd HH:mm:ss', true);

    var addBoxIndex = -1;
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber = 1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_view').on('click', function () {
        tempToDeatil();
    });

    function tempToDeatil() {
        if (apply.select(layer)) {
            var billId = apply.currentItem.id;
            var name = apply.currentItem.depositorName;
            var type = apply.currentItem.acctType;
            var billType = apply.currentItem.billType;
            var buttonType = 'select';  //按钮操作类型
            var syncEccs;
            var updateType;
            $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
                if (result) {
                    updateType = result.split(',')[0];
                    syncEccs = result.split(',')[1];
                }
                if (billType != 'ACCT_REVOKE') {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
                    });
                } else {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
                    });
                }
            });
        }
    }

});
