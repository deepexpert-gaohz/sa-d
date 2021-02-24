var whiteList;
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var apply = {
    baseUrl: "../../allBillsPublic",
    entity: "user",
    tableId: "daibuluTable",
    toolbarId: "toolbar",
    unique: "applyid",
    order: "desc",
    currentItem: {},
    columnsData: []
};
apply.columns = function () {
    return apply.columnsData;
};
apply.queryParams = function (params) {
    if (!params)
        return {
            depositorName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
            //bankCode: $("#bankCode").val(),
            kernelOrgCode: $("#kernelOrgCode").val(),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
            createdBy: $("#createdBy").val(),
            pbcSyncMethod: $("#pbcSyncMethod").val(),
            billType: $("#billType").val(),
            openAccountSiteType: $("#openAccountSiteType").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        depositorName: $.trim($("#name").val()),
        acctNo: $.trim($("#acctNo").val()),
        acctType: $("#acctType").val(),
        //bankCode: $("#bankCode").val(),
        kernelOrgCode: $("#kernelOrgCode").val(),
        billType: $("#billType").val(),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        createdBy: $("#createdBy").val(),
        pbcSyncMethod: $("#pbcSyncMethod").val(),
        code: 'dbllb',
        action: 'keepForm',
        openAccountSiteType: $("#openAccountSiteType").val()
    };
    return temp;
};

apply.init = function () {

    apply.table = $('#' + apply.tableId).bootstrapTable({
        url: apply.baseUrl + '/list?whiteList=' + whiteList, //请求后台的URL（*）
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
        // onDblClickRow:function (items,$element) {
        //     convertAcctBigType()
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

// var autoColumn = [{
//     checkbox: true
// }, {
//     field: 'id',
//     title: 'ID',
//     visible: false
// }, {
//     field: 'accountId',
//     title: '',
//     visible: false
// }, {
//     field: 'acctNo',
//     title: '账号'
// }, {
//     field: 'depositorName',
//     title: '存款人名称',
//     'class': 'W200'
// }, {
//     field: 'acctType',
//     title: '账户性质',
//     'class': 'W160',
//     formatter: function (value, row, index) {
//         return changeAcctType(value, row)
//     }
// }, {
//     field: 'billType',
//     title: '操作类型',
//     formatter: function (value, row, index) {
//         return changeBillType(value)
//     }
// }, {
//     field: 'kernelOrgCode',
//     title: '网点机构号',
//     'class': 'W120'
//     //}, {
//     //    field: 'status',
//     //    title: '审核状态',
//     //    formatter: function (value, row, index) {
//     //        return changeStatus(value)
//     //    }
// }, {
//     field: 'pbcSyncStatus',
//     title: '账管上报状态',
//     formatter: function (value, row, index) {
//         return formatSyncStatus(value)
//     }
// }, {
//     field: 'eccsSyncStatus',
//     title: '信用代码证上报状态',
//     formatter: function (value, row, index) {
//         return formatSyncStatus(value)
//     }
// }, {
//     field: 'pbcCheckDate',
//     title: '核准类人行审核时间',
//     //获取日期列的值进行转换
//     formatter: function (value, row, index) {
//         return changeDateStr(value)
//     }
// }, {
//     field: 'createdDate',
//     title: '申请日期',
//     'class': 'W160',
//     //获取日期列的值进行转换
//     formatter: function (value, row, index) {
//         return changeDateStr(value)
//     }
// }, {
//     field: 'createdBy',
//     title: '申请人'
// }, {
//     field: 'pbcSyncMethod',
//     title: '上报操作方式',
//     formatter: function (value, row, index) {
//         return formatSyncOperateType(value)
//     }
// }, {
//     field: 'openAccountSiteType',
//     title: '本地异地标识',
//     formatter: function (value, row, index) {
//         return formatOpenAccountSiteType(value)
//     }
// }];

var checkSync = [{
    checkbox: true
}, {
    field: 'id',
    title: 'ID',
    visible: false
}, {
    field: 'accountId',
    title: '',
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
    //}, {
    //    field: 'status',
    //    title: '审核状态',
    //    formatter: function (value, row, index) {
    //        return changeStatus(value)
    //    }
}, {
    field: 'status',
    title: '审核状态',
    formatter: function (value, row, index) {
        return changeStatus(value)
    }
},{
    field: 'approveDesc',
    title: '审核失败原因',
    'class': 'W200'
},{
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
    field: 'pbcCheckDate',
    title: '核准类人行审核时间',
    //获取日期列的值进行转换
    formatter: function (value, row, index) {
        return changeDateStr(value)
    }
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

layui.use(['form', 'layedit', 'common', 'laydate', 'layer'], function () {

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        common = layui.common,
        laydate = layui.laydate;

    whiteList = common.getReqParam("whiteList");
    // apply.init();


    // $.get(apply.baseUrl + '/checkSyncStatus', null, function (data) {
    //     if (data == '' || data == 'autoSync') {
    //         // apply.columnsData = autoColumn;
    //         apply.columnsData = checkSync;
    //     }else{
    //         apply.columnsData = checkSync;
    //         $("#btn_bbcg").hide();
    //         $("#btn_delete").hide();
    //     }
    //     apply.init();
    // });
    //待补录页面按钮不根据接口模式来判断隐藏
    apply.columnsData = checkSync;
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

    var addBoxIndex = -1;
    $.get('../../config/isCheck', '', function (data) {
        if (!data) {
            apply.table.bootstrapTable("hideColumn", "status");
        }
    });

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber = 1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_bl').on('click', function () {
        convertAcctBigType()
    });

    //报备成功按钮
    $('#btn_bbcg').on('click', function () {
        if (apply.select(layer)) {
            var billId = apply.currentItem.id;
            //验证是否已经有人正在操作此单据
            getBillIsBusy(billId, function (res) {
                if (res.rel) {
                    layer.msg(res.result.organName + "机构" + res.result.userName + "用户正在进行该笔业务");
                } else {
                    $.get('../accttype/syncSystem.html', null, function (form) {
                        layer.open({
                            type: 1,
                            title: '报备成功',
                            content: form,
                            btn: ['确定', '取消'],
                            shade: false,
                            offset: ['20px', '20%'],
                            area: ['500px', '170px'],
                            maxmin: true,
                            yes: function (index) {
                                //触发表单的提交事件
                                // layedit.sync(editIndex);
                                $('form.layui-form').find('button[lay-filter=edit]').click();
                            },
                            btn2: function(){
                                $("#btn_bbcg").show();
                            },
                            cancel: function () {
                                $("#btn_bbcg").show();
                            },
                            full: function (elem) {
                                var win = window.top === window.self ? window : parent.window;
                                $(win).on('resize', function () {
                                    var $this = $(this);
                                    elem.width($this.width()).height($this.height()).css({
                                        top: 0,
                                        left: 0
                                    });
                                    elem.children('div.layui-layer-content').height($this.height() - 95);
                                });
                            },
                            success: function (layero, index) {
                                if (apply.currentItem.pbcSyncStatus === 'tongBuChengGong' || apply.currentItem.pbcSyncStatus === 'buTongBu') {
                                    $('#isSyncAmsDiv').hide();
                                }
                                if ( apply.currentItem.eccsSyncStatus === 'tongBuChengGong' || apply.currentItem.eccsSyncStatus === 'buTongBu') {
                                    $('#isSyncEccsDiv').hide();
                                }
                                form = layui.form;
                                form.render();
                                form.on('submit(edit)', function (data) {
                                    var pbcSync = $("#isSyncAms").prop("checked");
                                    var eccsSync = $("#isSyncEccs").prop("checked");
                                    if(apply.currentItem.status!='WAITING_SUPPLEMENT'){
                                        if(pbcSync === false && eccsSync === false) {
                                            layer.alert("请勾选报备成功系统");
                                            return false;
                                        }
                                    }

                                    $.ajax({
                                        url: apply.baseUrl + "/finishSync",
                                        data: {
                                            billId:billId,
                                            pbcSync:pbcSync,
                                            eccsSync:eccsSync
                                        },
                                        type: 'post',
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
                                    layer.close(index);
                                    return false;
                                });
                            }
                        });
                    });
                    $("#btn_bbcg").hide();
                }
            });
        }
    });

    //删除按钮
    $('#btn_delete').on('click', function () {
        debugger;
        if (apply.select(layer)) {
            var billId = apply.currentItem.id;
            //验证是否已经有人正在操作此单据
            getBillIsBusy(billId, function (res) {
                if (res.rel) {
                    layer.msg(res.result.organName + "机构" + res.result.userName + "用户正在进行该笔业务");
                } else {
                    deleteBillsAndAccount(billId, function () {
                        layerTips.msg('删除成功');
                        location.reload();
                    });
                }
            });
        }
    });

});

//大类转小类
function convertAcctBigType() {
    if (apply.select(layer)) {
        var billId = apply.currentItem.id;
        var name = apply.currentItem.depositorName;
        var type = apply.currentItem.acctType;
        var billType = apply.currentItem.billType;
        var accountId = apply.currentItem.accountId;
        var buttonType = 'update';  //按钮操作类型
        var typeCode = '';
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

            if(!accountId) {
                layer.alert("该账户账户id为空,无法更新小类");
                return;
            } else if(!acctType) {
                layer.alert("请选择账户性质的小类");
                return;
            } else {
                $.post('../../allAccountPublic/updateAcctType', {'accountId' : accountId, 'acctType' : acctType}, function (data) {
                    layer.close(index)
                    toDblDetail(acctType, billId, billType, buttonType)
                });
            }
        });
    }
}

function toDblDetail(type, billId, billType, buttonType) {
    var typeCode;

    var acctNo = apply.currentItem.acctNo;
    /*if (billType == 'ACCT_REVOKE' && (type == 'linshi'  || type == 'yusuan' || type == 'teshu')) {
        layer.alert("核准类账户销户将有系统自动进行处理！无需点击补录！", {
            title: "提示",
            closeBtn: 0
        });
        return;
    }*/

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
                        href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&updateType=' + updateType + '&syncEccs=' + syncEccs + "&pageCode=dblAccount"
                    });
                }
            });
        } else {
            parent.tab.tabAdd({
                title: '补录' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=update' + '&updateType=' + updateType + '&syncEccs=' + syncEccs + "&pageCode=dblAccount" + '&acctNo=' + acctNo
            });
        }
    });

}