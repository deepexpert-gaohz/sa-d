layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form', 'murl', 'saic', 'account', 'picker', 'linkSelect', 'org', 'industry', 'common', 'loading', 'laydate', 'upload'], function () {
    var form = layui.form, url = layui.murl,
        layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        loading = layui.loading,
        upload = layui.upload,
        laydate = layui.laydate;

    var tabId = decodeURI(common.getReqParam("tabId"));
    var checked = true;
    var role = 0;
    if (tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

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
            radio: true
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
            field: 'string004',
            title: '影像补录状态',
            formatter: function (value, row, index) {
                return formatString004(value)
            }
        }, {
            field: 'bankName',
            title: '开户行'
        }, {
            field: 'bankCode',
            title: '人行机构号'
        }, {
            field: 'acctCreateDate',
            title: '开户日期',
            //获取日期列的值进行转换
            formatter: function (value, row, index) {
                return changeDateStr(value)
            }
        }, {
            field: 'cancelDate',
            title: '销户日期'
        }];
    };
    apply.queryParams = function (params) {
        if (!params)
            return {
                acctName: $.trim($("#name").val()),
                acctNo: $.trim($("#acctNo").val()),
                acctType: $("#acctType").val(),
                accountStatus: $("#accountStatus").val(),
                string003: "1",
                openAccountSiteType:$("#openAccountSiteType").val()
            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            size: params.limit, //页面大小
            page: params.offset / params.limit, //页码
            acctName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $.trim($("#acctType").val()),
            accountStatus: $.trim($("#accountStatus").val()),
            string004: $.trim($("#string004").val()),
            string003: "1",
            openAccountSiteType:$("#openAccountSiteType").val()
        };
        return temp;
    };

    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/stockList', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            singleSelect: true,
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

    layui.use(['form', 'layedit', 'laydate', 'layer'], function () {
        apply.init();

        var editIndex;
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form,
            layedit = layui.layedit,
            laydate = layui.laydate;

        var addBoxIndex = -1;
        $.get('../../config/isCheck', '', function (data) {
            if (!data) {
                apply.table.bootstrapTable("hideColumn", "status");
            }
        });

        //初始化页面上面的按钮事件
        $('#btn_query').on('click', function () {
            var queryParams = apply.queryParams();
            queryParams.pageNumber=1;
            apply.table.bootstrapTable('refresh', queryParams);
        });

        $('#btn_view').on('click', function () {
            if (apply.select(layer)) {
                var billId = apply.currentItem.id;
                var refBillId = apply.currentItem.refBillId;
                var name = apply.currentItem.depositorName;
                var type = apply.currentItem.acctType;
                var billType = apply.currentItem.billType;
                var accountStatus = apply.currentItem.accountStatus;
                var buttonType = 'selectForChangeBtn';  //按钮操作类型
                var typeCode = '';
                var urlStr = "&refBillId=" + refBillId;

                $('#accountId').val(apply.currentItem.id)
                //账户性质为大类时转小类
                if (type == 'specialAcct' || type == 'tempAcct' || type == 'unknow') {
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
                        if (!$('#accountId').val()) {
                            layer.alert("该账户账户id为空,无法更新小类");
                            return;
                        } else if (!$('#acctTypeOption').val()) {
                            layer.alert("请选择账户性质的小类");
                            return;
                        } else {
                            $.post('../../allAccountPublic/updateAcctType', {
                                'accountId': $('#accountId').val(),
                                'acctType': $('#acctTypeOption').val(),
                                'refBillId': refBillId
                            }, function (data) {
                                layer.close(index);
                                tempToDetail($('#acctTypeOption').val(), billId, billType, buttonType, accountStatus, urlStr);
                            });
                        }
                    });

                    return;
                }

                //小类跳转
                tempToDetail(type, billId, billType, buttonType, accountStatus, urlStr);
            }
        });

    });

    function tempToDetail(type, billId, billType, buttonType, accountStatus, urlStr) {

        var syncEccs;
        var updateType;
        $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
            if (result) {
                updateType = result.split(',')[0];
                syncEccs = result.split(',')[1];
            }

            if (billType == 'ACCT_REVOKE' && (type == 'yiban' || type == 'feiyusuan')) {   //备案类销户类型
                parent.tab.tabAdd({
                    title: '查看' + acctTypeMap[type] + '-' + name,
                    href: 'account/accountRevoke.html?billId=' + billId + '&billType=' + billType + '&buttonType=revokeInfo' + urlStr + '&syncEccs=' + syncEccs + '&updateType=' + updateType
                });
                return;
            }

            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&accountStatus=' + accountStatus + '&buttonType=' + buttonType + urlStr + '&syncEccs=' + syncEccs + '&updateType=' + updateType
            });
        });
    }

    $('#btn_yxbl').on('click', function () {
        if (apply.select(layer)) {
            if(apply.currentItem.string004==="1"){
                layerTips.msg('该数据已补录');
                return;
            }
            var id = apply.currentItem.acctNo;
            $.ajax({
                url: apply.baseUrl + "/changeImageStatus/" + id,
                type: 'put',
                dataType: "json",
                success: function (result) {
                    if (result.rel) {
                        layerTips.msg('更新成功');
                        location.reload();
                    } else {
                        layerTips.msg('更新失败');
                    }
                }

            });
        }
    });

});