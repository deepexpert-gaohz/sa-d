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
    var reportDate = decodeURI(common.getReqParam("reportDate"));

    var isAlert = decodeURI(common.getReqParam("isAlert"));
    if (isAlert === 'true') {
        layer.alert("存在未上报人行/信用代码系统数据，可通过上报状态进行查询");
    }

    var apply = {
        baseUrl: "../../allBillsPublic",
        tableId: "todayPbcTableDetail",
        toolbarId: "toolbar",
        unique: "id",
        currentItem: {},
        columnsData: []
    };
    apply.columns = function () {
        return apply.columnsData;
    };
    apply.queryParams = function (params) {
        var temp = {
            depositorName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
            kernelOrgCode: $("#kernelOrgCode").val(),
            billType: $("#billType").val(),
            createdBy: $("#createdBy").val(),
            pbcSyncMethod: $("#pbcSyncMethod").val(),
            openAccountSiteType: $("#openAccountSiteType").val(),
            reportDate: reportDate
        };
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };

    apply.init = function () {
        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/statisticsForDateDetailList', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
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
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '企业名称',
        'class': 'W200'
    }, {
        field: 'acctType',
        title: '账户性质',
        'class': 'W160',
        formatter: function (value, row, index) {
            return changeAcctType(value, row)
        }
    }, {
        field: 'billType',
        title: '操作类型',
        formatter: function (value, row, index) {
            return changeBillType(value)
        }
    }, {
        field: 'kernelOrgCode',
        title: '网点机构号',
        'class': 'W120'
    }, {
        field: 'createdDate',
        title: '申请日期',
        'class': 'W160',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateStr(value)
        }
    }, {
        field: 'createdBy',
        title: '申请人'
    }, {
        field: 'pbcSyncStatus',
        title: '人行上报状态',
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
        field: 'pbcSyncTime',
        title: '人行上报日期',
        'class': 'W160',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateStr(value)
        }
    }, {
        field: 'eccsSyncTime',
        title: '信用代码上报日期',
        'class': 'W160',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateStr(value)
        }
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="' + value + '">查看详情</a>';
        }
    }];
    apply.columnsData = autoColumn;
    apply.init();

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

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber = 1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_downLoad').on('click', function () {
        var dataUrl ='/statisticsForDateDetailsExport?reportDate=' + reportDate + '&depositorName=' + $.trim($("#name").val()) + '&acctNo=' + $.trim($("#acctNo").val()) + '&acctType=' + $("#acctType").val()
        + '&kernelOrgCode=' + $("#kernelOrgCode").val() + '&billType=' + $("#billType").val() + '&createdBy=' + $("#createdBy").val() + '&pbcSyncMethod=' + $("#pbcSyncMethod").val() + '&openAccountSiteType=' +
            $("#openAccountSiteType").val();
        location.href = apply.baseUrl + dataUrl;
    });

    $('#todayPbcTableDetail').on('click', '.view', function () {
        var id = $(this).attr('data-id');
        apply.currentItem = apply.table.bootstrapTable('getRowByUniqueId', id);
        // if (apply.currentItem.pbcSyncStatus === 'weiTongBu'
        //     || apply.currentItem.pbcSyncStatus === 'tongBuShiBai'
        //     || apply.currentItem.eccsSyncStatus === 'weiTongBu'
        //     || apply.currentItem.eccsSyncStatus === 'tongBuShiBai') {//进行补录
        //     convertAcctBigType('update');
        // } else {//进行查看
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

                if(billType != 'ACCT_REVOKE') {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
                    });
                } else {
                    parent.tab.tabAdd({
                        title: '查看' + acctTypeMap[type] + '-' + name,
                        href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=revokeDetail' + '&syncEccs=' + syncEccs + '&updateType=' + updateType
                    });
                }

            });
        // }
        return false;
    });

    /**
     * 大类转小类
     * @param buttonType 按钮操作类型
     */
    function convertAcctBigType(buttonType) {
        var billId = apply.currentItem.id;
        var type = apply.currentItem.acctType;
        var billType = apply.currentItem.billType;
        var accountId = apply.currentItem.accountId;
        var index;

        if (type != 'specialAcct' && type != 'tempAcct' && type != 'unknow') {
            toDblDetail(type, billId, billType, buttonType);
            return;
        }

        $('#accountId').val(accountId)
        if (type == 'specialAcct') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="yusuan">预算单位专用存款账户</option><option value="feiyusuan">非预算单位专用存款账户</option><option value="teshu">特殊单位专用存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });

        } else if (type == 'tempAcct') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="linshi">临时机构临时存款账户</option><option value="feilinshi">非临时机构临时存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });


        } else if (type == 'unknow') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="jiben">基本存款账户</option><option value="yiban">一般存款账户</option><option value="yusuan">预算单位专用存款账户</option><option value="feiyusuan">非预算单位专用存款账户</option><option value="linshi">临时机构临时存款账户</option><option value="feilinshi">非临时机构临时存款账户</option><option value="teshu">特殊单位专用存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });

        }

        $('#acctTypeBtn').click(function () {
            var accountId = $('#accountId').val();
            var acctType = $('#acctTypeOption').val();

            if (!accountId) {
                layer.alert("该账户账户id为空,无法更新小类");
                return;
            } else if (!acctType) {
                layer.alert("请选择账户性质的小类");
                return;
            } else {
                $.post('../../allAccountPublic/updateAcctType', {
                    'accountId': accountId,
                    'acctType': acctType
                }, function (data) {
                    layer.close(index)
                    toDblDetail(acctType, billId, billType, buttonType)
                });
            }
        });
    }

    function toDblDetail(type, billId, billType, buttonType) {
        if (billType == 'ACCT_REVOKE' && (type == 'linshi' || type == 'yusuan' || type == 'teshu')) {
            layer.alert("核准类账户销户将有系统自动进行处理！无需点击补录！", {
                title: "提示",
                closeBtn: 0
            });
            return;
        }

        var updateType;
        var syncEccs;

        $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
            if (result) {
                updateType = result.split(',')[0];
                syncEccs = result.split(',')[1];
            }
            if (billType !== 'ACCT_REVOKE') {
                //验证是否已经有人正在操作此单据
                getBillIsBusy(billId, function (res) {
                    if (res.rel) {
                        layer.msg(res.result.organName + "机构" + res.result.userName + "用户正在进行该笔业务");
                    } else {
                        parent.tab.tabAdd({
                            title: '补录' + acctTypeMap[type] + '-' + name,
                            href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&updateType=' + updateType + '&syncEccs=' + syncEccs
                        });
                    }
                });
            } else {
                parent.tab.tabAdd({
                    title: '补录' + acctTypeMap[type] + '-' + name,
                    href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=update' + '&updateType=' + updateType + '&syncEccs=' + syncEccs
                });
            }
        });

    }
});
