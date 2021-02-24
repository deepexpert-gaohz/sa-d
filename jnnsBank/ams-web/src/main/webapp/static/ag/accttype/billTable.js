layui.use(['form', 'murl', 'saic', 'account', 'picker', 'linkSelect', 'org', 'industry', 'common', 'loading', 'laydate', 'imageInfo'], function () {
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        loading = layui.loading,
        laydate = layui.laydate;

    var acctNo = decodeURI(common.getReqParam("acctNo"));
    //获取影像配置信息
    var imageConfig = "";//系统配置的影像补录标识
    $.get("../../accountImage/getConfig", function (res) {
        if (res.data.length > 0) {
            if (res.code === 'ACK') {
                for (var i = 0; i < res.data.length; i++) {
                    if (res.data[i].configKey == "imageCollect") {
                        //accountImage
                        imageConfig = res.data[i].configValue;
                    }
                }
            }
        }
    });
    //关联流水
    var apply = {
        tableId: "billsTable",
        unique: "id",
        order: "desc",
        currentItem: {}
    };
    apply.columns = function () {
        return [
            {
                field: 'lastUpdateDate',
                title: '操作时间',
                //获取日期列的值进行转换
                formatter: function (value, row, index) {
                    return changeDateStr(value)
                }
            }, {
                //     field: 'acctNo',
                //     title: '账号'
                // }, {
                //     field: 'acctType',
                //     title: '账户性质',
                //     class: 'W120',
                //     formatter: function (value, row, index) {
                //         return changeAcctType(value)
                //     }
                // }, {
                field: 'billTypeStr',
                title: '操作类型'
            }, {
                field: 'kernelOrgCode',
                title: '网点机构号'
            }, {
                field: 'createdName',
                title: '申请人'
            }, {
                field: 'preOpenAcctId',
                title: '预约记录',
                formatter: function (value, row, index) {
                    if (value !== null && value !== "") {
                        return '<a class="view" style="color: blue" href="#" onclick="applyDetail(\'' + row.id + '\')">预约详情</a>';
                    } else {
                        return "无";
                    }
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function (value, row, index) {
                    var html = '&nbsp;  <a class="view btn btn-primary btn-link btn-xs" href="javascript:void(0)" onclick="billDetail(\'' + value + '\')"><i class="fa fa-eye"></i> 查看详情</a>';
                    if (imageConfig != null && imageConfig !== "") {
                        html += '&nbsp;  <a class="view btn btn-primary btn-link btn-xs" href="javascript:void(0)" onclick="addedImage(\'' + value + '\',\'' + imageConfig + '\')"><i class="fa fa-pencil-square-o"></i> 影像补录</a>';
                    }
                    if (row.changeRecordJsonStr != null && JSON.parse(row.changeRecordJsonStr).length > 0) {
                        html += '&nbsp;  <a class="view btn btn-primary btn-link btn-xs" href="javascript:void(0)" onclick="changeRecord(\'' + value + '\')"><i class="fa fa-pencil-square-o"></i> 变更记录</a>';
                        // html += "&nbsp;  <a class='view btn btn-primary btn-link btn-xs' href='javascript:void(0)' onclick='changeRecord(" + JSON.stringify(row) + ")'><i class='fa fa-pencil-square-o'></i> 变更记录</a>";
                    }
                   html += '&nbsp;  <a class="view btn btn-primary btn-link btn-xs" href="javascript:void(0)" onclick="showOperateLog(\'' + value + '\')"><i class="fa fa-history"></i> 操作记录</a>';
                    return html;
                }
            }
        ];
    };
    apply.queryParams = function (params) {
        var temp = {};
        if (acctNo !== undefined && acctNo !== "" && acctNo !== null) {
            temp.acctNo = acctNo;
        }
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };
    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: "../../allBillsPublic/listForAccount", //请求后台的URL（*）
            method: 'get', //请求方式（*）
            // toolbar: false, //工具按钮用哪个容器
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
            columns: apply.columns()
            , onLoadError: function (status) {
                ajaxError(status);
            }
        });
    };
    if (acctNo !== undefined && acctNo !== "" && acctNo !== null) {
        apply.init();
    }

});

function showOperateLog(id) {
    layer.open({
        type: 2,
        title: '操作记录',
        content: '../account/operateLog.html?billId='+id,
        shade: false,
        offset: ['20px', '20%'],
        area: ['1000px', '500px'],
        maxmin: true,
        success: function (layero, index) {
        },
        end: function () {
        }
    });
}
/**
 * 变更记录弹出框
 * @param row
 */
function changeRecord(id) {
    var tableData = $("#billsTable").bootstrapTable('getData');
    var row = [];
    for (var i = 0; i < tableData.length; i++) {
        if (tableData[i].id + '' === id) {
            row = tableData[i];
            break;
        }
    }
    var data = JSON.parse(row.changeRecordJsonStr);
    var html = '<div style="margin: 10px;">' +
        '   <div class="layui-form-item">' +
        '       <label class="layui-form-label">账号</label>' +
        '       <div class="layui-input-block">' +
        '          <label class="layui-form-label">' + (row.acctNo === undefined ? '' : row.acctNo) + '</label>' +
        '       </div>' +
        '   </div>' +
        '   <div class="layui-form-item">' +
        '       <label class="layui-form-label">账户名称</label>' +
        '       <div class="layui-input-block">' +
        '          <label class="layui-form-label">' + (row.acctName === undefined ? '' : row.acctName) + '</label>' +
        '       </div>' +
        '   </div>' +
        '</div>';
    html += '<table class="layui-table">\n' +
        '  <colgroup>\n' +
        '    <col width="150">\n' +
        '    <col width="200">\n' +
        '    <col>\n' +
        '  </colgroup>\n' +
        '  <thead>\n' +
        '    <tr>\n' +
        '      <th>字段名称</th>\n' +
        '      <th>变更前</th>\n' +
        '      <th>变更后</th>\n' +
        '    </tr> \n' +
        '  </thead>\n' +
        '  <tbody>\n';
    for (var i = 0; i < data.length; i++) {
        var name = data[i].name;
        var oldValue = (data[i].oldValue === undefined ? '' : data[i].oldValue);
        var newValue = (data[i].newValue === undefined ? '' : data[i].newValue);
        if (name == 'regCity' || name == 'regArea') {
            getAreaNameByCode(oldValue, function (areaNames) {
                oldValue = areaNames;
            });
            getAreaNameByCode(newValue, function (areaNames) {
                newValue = areaNames;
            });
        } else {
            try {
                //拼接数据格式化方法名
                name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length);
                var functionName = 'get' + name + 'Name';
                console.log(functionName + "-----" + data[i].cname + "-------" + oldValue + "-------" + newValue);
                if (typeof eval(functionName) === "function") {//判断是否存在该方法
                    //调用方法并赋值
                    oldValue = window[functionName](oldValue);
                    newValue = window[functionName](newValue);
                }
            } catch (e) {
            }
        }

        html += '    <tr>\n' +
            '      <td>' + (data[i].cname === undefined ? '' : data[i].cname) + '</td>\n' +
            '      <td>' + (oldValue === undefined ? '' : oldValue) + '</td>\n' +
            '      <td>' + (newValue === undefined ? '' : newValue) + '</td>\n' +
            '    </tr>\n';
    }
    html += '  </tbody>\n' +
        '</table>';
    var height = 350 + data.length * 20;
    height = height > 550 ? 550 : height;
    layer.open({
        type: 1,
        title: '变更记录',
        content: html,
        shade: false,
        offset: ['20px', '20%'],
        area: ['600px', height + 'px'],
        maxmin: true,
        success: function (layero, index) {
        },
        end: function () {
        }
    });
}

/**
 * 根据code获取地区中文地址
 * @param areaCode
 */
function getAreaNameByCode(areaCode, callBack) {
    if (areaCode === undefined || areaCode === null || areaCode === "") {
        callBack && callBack("");
    }
    $.ajax({
        type: "GET",
        url: "../../area/registLists",
        data: {areaCode: areaCode},
        dataType: "json",
        async: false,
        success: function (result) {
            if (result.rel) {
                var areaNames = "";
                for (var i = 0; i < result.result.length; i++) {
                    if (result.result[i] != null && result.result[i].areaName != null) {
                        if (i === 0) {
                            areaNames += result.result[i].areaName;
                        } else {
                            areaNames += "-" + result.result[i].areaName;
                        }
                    }
                }
                callBack && callBack(areaNames);
            }
        },
        error: function () {
            layer.msg("error");
        }
    });
}

/**
 * 预约详情
 * @param id
 */
function applyDetail(id) {
    var data = $("#billsTable").bootstrapTable('getData');
    var rowData = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id + '' === id) {
            rowData = data[i];
            break;
        }
    }
    parent.tab.tabAdd({
        title: '查看-' + rowData.depositorName,
        href: 'bank/view.html?applyId=' + rowData.preOpenAcctId + '&name=' + encodeURI(rowData.depositorName)
    });
}

/**
 * 查看详情
 * @param id
 */
function billDetail(id) {
    var data = $("#billsTable").bootstrapTable('getData');
    var rowData = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id + '' === id) {
            rowData = data[i];
            break;
        }
    }
    var billId = rowData.id;
    var name = rowData.depositorName;
    var type = rowData.acctType;
    var billType = rowData.billType;
    var buttonType = 'select';  //按钮操作类型
    var typeCode = '';

    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if (result) {
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }

        if (billType !== 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType=' + billType + '&buttonType=' + buttonType + '&updateType=' + updateType + '&syncEccs=' + syncEccs
            });
        } else {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
            });
        }
    });
}

/**
 * 影像补录
 * @param id
 * @param imageConfig 系统配置的影像补录标识
 */
function addedImage(id, imageConfig) {

    var data = $("#billsTable").bootstrapTable('getData');
    var rowData = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id + '' === id) {
            rowData = data[i];
            break;
        }
    }
    var billId = rowData.id;
    var name = rowData.depositorName;
    var type = rowData.acctType;
    var billType = rowData.billType;
    var buttonType = 'select';  //按钮操作类型
    var typeCode = '';

    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if (result) {
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }

        if (billType !== 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'accttype/' + type + 'Open.html?billId=' + billId
                + '&billType=' + billType
                + '&buttonType=' + buttonType
                + '&imageFlag=' + 1//是否只显示账户影像信息
                + '&syncEccs=' + syncEccs
                + '&updateType=' + updateType
            });
        } else {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
            });
        }
    });
}