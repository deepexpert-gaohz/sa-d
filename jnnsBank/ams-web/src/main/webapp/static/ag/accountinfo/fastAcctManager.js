layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form', 'murl', 'saic', 'account', 'picker', 'linkSelect', 'org', 'industry', 'common', 'loading', 'laydate', 'upload'], function () {
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        loading = layui.loading,
        upload = layui.upload,
        laydate = layui.laydate;

    // //初始化日期控件
    var laydateAcctCreate = beginEndDateInit(laydate, 'beginDateAcctCreate', 'endDateAcctCreate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateAcctCreate.length; i++) {
            delete laydateAcctCreate[i].max;
            delete laydateAcctCreate[i].min;
        }
    });

    var tabId = decodeURI(common.getReqParam("tabId"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var billId = decodeURI(common.getReqParam("billId"));
    var acctType = common.getReqParam("acctType");//账户性质
    var accountStatus = common.getReqParam("accountStatus");//账户状态
    var orgCode = common.getReqParam("orgCode");//父类机构号

    var checked = true;
    var role = 0;

    var apply = {
        baseUrl: "../../allAccountPublic",
        entity: "user",
        tableId: "userTable",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };
    apply.columns = function () {
        return [{
            checkbox: true
        }, {
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'refBillId',
            visible: false
        }, {
            field: 'acctNo',
            title: '账号'
        }, {
            field: 'acctName',
            title: '账户名称',
            'class': 'W200'
        }, {
            field: 'acctType',
            title: '账户性质',
            'class': 'W120',
            formatter: function (value, row, index) {
                return changeAcctType(value)
            }
        }, {
            field: 'accountStatus',
            title: '账户状态',
            formatter: function (value, row, index) {
                return formatAccountStatus(value)
            }
        }, {
            field: 'openAccountSiteType',
            title: '本地异地标识',
            formatter: function (value, row, index) {
                return formatOpenAccountSiteType(value)
            }
        }, {
            field: 'bankName',
            title: '开户行'
        }, {
            field: 'kernelOrgCode',
            title: '网点机构号'
        }, {
            field: 'acctCreateDate',
            title: '开户日期',
            //获取日期列的值进行转换
            formatter: function (value, row, index) {
                return changeDateStr(value)
            }
        }, {
            field: 'cancelDate',
            title: '销户日期',
            formatter: function (value, row, index) {
                if (row.accountStatus == "revoke") {
                    return value;
                }
            }
        }
        ];
    };

    //检查批量久悬的按钮
    $.get(apply.baseUrl + '/suspend/process', function (res) {
        if (res.code === 'ACK') {
            var suspendBtn = $("#btn_down_suspend");
            if (res.data.process) {
                suspendBtn.removeClass("dropdown-toggle");
                suspendBtn.removeAttr("data-toggle");
                suspendBtn.find(".caret").hide();
                suspendBtn.find(".layui-badge-dot").show();
                suspendBtn.addClass("suspend-process");
                suspendBtn.attr("batch-no", res.data.batchNo);
            } else {
                suspendBtn.addClass("dropdown-toggle");
                suspendBtn.attr("data-toggle", "dropdown");
                suspendBtn.find(".caret").show();
                suspendBtn.find(".layui-badge-dot").hide();
                suspendBtn.removeClass("suspend-process");
                suspendBtn.removeAttr("batch-no");
            }
        } else {
            layerTips.msg(res.message);
        }
    });

    apply.queryParams = function (params) {
        if (!params)
            return {
                acctName: $.trim($("#name").val()),
                acctNo: $.trim($("#acctNo").val()),
                acctType: $("#acctType").val(),
                accountStatus: $("#accountStatus").val(),
                orgCode: orgCode,//查询的机构号
                openAccountSiteType: $("#openAccountSiteType").val()
            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            size: params.limit, //页面大小
            page: params.offset / params.limit, //页码
            acctName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
            accountStatus: $("#accountStatus").val(),//账户状态
            bankName: $("#bankName").val(),//开户行
            //bankCode: $("#bankCode").val(),//人行机构号
            kernelOrgCode: $("#kernelOrgCode").val(),//网点机构号
            beginDateAcctCreate: $("#beginDateAcctCreate").val(),//开户开始时间
            endDateAcctCreate: $("#endDateAcctCreate").val(),//开户结束时间
            orgCode: orgCode,//查询的机构号
            openAccountSiteType: $("#openAccountSiteType").val()

            // status:$("#status").val(),
            // operator: $.trim($("#operator").val()),
            // phone: $.trim($("#phone").val())
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
            showColumns: false, //是否显示所有的列
            showRefresh: true, //是否显示刷新按钮
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            singleSelect: true, // 单选checkbox
            columns: apply.columns()
            , onLoadError: function (status) {
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

    layui.use(['form', 'layedit', 'laydate'], function () {

        var timestamp = Date.parse(new Date());
        var isCheck;

        $.get('../../config/isCheck', function (dat) {
            if (dat == false) {
                $('#zhsq').hide();
                $('#dsh').hide();
            }
        });

        $('#cgsb').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/cgsbAccount.html',
                icon: 'fa fa-check',
                title: '报备成功'
            });
        });

        $('#dsb').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/dsbAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '待上报'
            });
        });

        $('#hztg').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/hztgAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '核准成功'
            });
        });

        $('#clzh').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/clzhAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '存量账户'
            });
        });

        apply.init();

        var editIndex;
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form(),
            layedit = layui.layedit,
            laydate = layui.laydate;
        var addBoxIndex = -1;

        //初始化页面上面的按钮事件
        $('#btn_query').on('click', function () {

            //开始结束时间校验
            var beginDateAcctCreate = $("#beginDateAcctCreate").val();//开户开始时间
            var endDateAcctCreate = $("#endDateAcctCreate").val();//开户结束时间
            if (beginDateAcctCreate != null && endDateAcctCreate != null) {
                var startTime = new Date(Date.parse(beginDateAcctCreate.replace(/-/g, "/"))).getTime();//浏览器兼容性
                var endTime = new Date(Date.parse(endDateAcctCreate.replace(/-/g, "/"))).getTime();//浏览器兼容性
                if (startTime > endTime) {
                    layer.msg("开始时间大于结束时间");
                    return;
                }
            }

            var queryParams = apply.queryParams();
            queryParams.pageNumber = 1;
            apply.table.bootstrapTable('refresh', queryParams);
        });

        $('#btn_view').on('click', function () {
            console.log("btn_view");
            if (apply.select(layer)) {
                var accountId = apply.currentItem.id;
                var refBillId = apply.currentItem.refBillId;
                var name = apply.currentItem.depositorName;
                var type = apply.currentItem.acctType;
                var billType = apply.currentItem.billType;
                var accountStatus = apply.currentItem.accountStatus;
                var acctNo = apply.currentItem.acctNo;
                var buttonType = 'selectForChangeBtn';  //按钮操作类型
                var typeCode = '';
                var createdDate = apply.currentItem.createdDate == null ? "" : apply.currentItem.createdDate;
                var kernelOrgName = apply.currentItem.kernelOrgName == null ? "" : apply.currentItem.kernelOrgName;

                $('#accountId').val(apply.currentItem.id)
            }
        });

        $('#btn_khjd').on('click', function () {
            if (apply.select(layer)) {
                var applyId = apply.currentItem.applyid;
                var name = apply.currentItem.name;
                parent.tab.tabAdd({
                        title: '客户尽调',
                        href: 'kyc/detail.html?name=' + name + '&applyId=' + applyId + '&history=false'
                    }
                );
            }
        });

        $('a[role="menuitem"]').on('click', function () {
            var type = $(this).attr("href");
            var href = "accountinfo/fastAcctOpen.html?type=" + type;
            parent.tab.tabAdd({
                    title: '对公账户开立',
                    href: href
                }
            );
            return false;
        });
    })
});

function tempToDetail(type, accountId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName) {
    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if (result) {
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }

        //(改：billType == 'ACCT_REVOKE'---->accountStatus == 'revoke')
        //基本户允许销户
        if (accountStatus == 'revoke' && (type == 'yiban' || type == 'feiyusuan' || type == 'jiben')) {   //备案类销户类型
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + accountId + '&billType=' + billType + '&buttonType=revokeInfo' + urlStr + '&syncEccs=' + syncEccs + '&updateType=' + updateType + '&acctNo=' + acctNo
            });
            return;
        }

        parent.tab.tabAdd({
            title: '查看' + acctTypeMap[type] + '-' + name,
            href: 'accttype/' + type + 'Open.html?'
                + '&billId=' + accountId
                + '&acctNo=' + acctNo
                + '&billType=' + billType
                + '&accountStatus=' + accountStatus
                + '&buttonType=' + buttonType
                + urlStr
                + '&syncEccs=' + syncEccs
                + '&updateType=' + updateType
                + '&createdDate=' + encodeURI(createdDate)//创建时间
                + '&kernelOrgName=' + encodeURI(kernelOrgName)//创建机构
        });
    });
}