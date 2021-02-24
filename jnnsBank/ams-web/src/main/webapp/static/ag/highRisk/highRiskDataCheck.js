var riskDataCheck = {
    baseUrl: "../../highRisk",
    tableId: "riskDetailDataTable",
    toolbarId: "riskDetailDataTable",
    unique: "id",
    order: "asc",
    currentItem: {},
    modelId: '',
    tableNameOptions: '',
    pageNo: ''
};
/**
 * 获取页面上的前一天时间
 * @type {string|*}
 */
var laydate = layui.laydate;
riskdataTrade.columns = function () {
    return [
        {
            field: 'ordNum',
            title: '序号'
        }, {
            field: 'accountNo',
            title: '账号',
        }, {
            field: 'depositorName',
            title: '企业名称',
            visible: false
        }, {
            field: 'accountType',
            title: '账号类型',
            formatter: function (value, row, index) {
                return changeAcctType(value)
            }
        }, {
            field: 'depositorNo',
            title: '企业证件号',
            visible: false
        }, {
            field: 'bankName',
            title: '开户行名称'
        }, {
            field: 'acctCreateDate',
            width: '100',
            title: '开户日期',
            formatter: function (value, row, index) {
                if (value == "" || value == null) {
                    return "";
                } else {
                    return new Date(value).Format("yyyy-MM-dd");
                }
            }

        }, {
            field: "riskTable",
            title: '操作',
            width: '50',
            formatter: function operateFormatter(e, value, row) {
                var butVal1 = "";
                if (value.status == 0) {
                    butVal1 = "&nbsp<a style='color: blue' class='handleRisk'>处理</a>";
                } else if (value.status == 2 || value.status == 3) {
                    butVal1 = "&nbsp<a style='color: blue' class='restoreRisk'>恢复</a>";
                } else if (value.status == 4 || value.status == 5 || value.status == 1) {
                    butVal1 = "&nbsp<a style='color: #ffaeb1' class=''>处理</a>";
                }
                return butVal1;
            },
            events: {
                'click .handleRisk': function (e, value, row) {
                    handleRiskInfo1(row);
                },
                'click .restoreRisk': function (e, value, row) {
                    restoreRiskInfo1(row);
                }
            }
        }, {
            field: 'status',
            title: '处理状态',
            formatter: function (status) {
                var str = '';
                if (status == 0) {
                    str = '待处理';
                } else if (status == 2 || status == 3) {
                    str = '已处理';
                } else {
                    str = '已恢复';
                }
                return str;
            }
        }];


};

//风险处理
function handleRiskInfo1(row) {
    var handle = row.status;
    var accountNo = row.accountNo;
    if (accountNo == '') {
        accountNo = null;
    }
    var id = row.id;
    //标记1为处理企业下所有账号,0为单个账号
    var choo = '0';
    var customerNo = row.customerNo;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get('../highRisk/disHighRisk.html', null, function (form) {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        addBoxIndex = layer.open({
            type: 1,
            title: '风险处理',
            content: form,
            btn: ['提交', '取消'],
            shade: false,
            offset: ['20px', '20%'],
            area: ['50%', '50%'],
            maxmin: true,
            yes: function (index) {
                sub(id,addBoxIndex, index,accountNo,customerNo,choo,layerTips,layer);
                // $('form.layui-form').find('button[lay-filter=edit]').click();

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
                var form = layui.form;
                form.render();
                form.on('submit(edit)', function () {
                    var handleMode = $("#handleMode").val();
                    var userId = $("#kyh").val();
                    var reason = $("#disReason").val();
                    $.ajaxSettings.async = false;
                    $.ajax({
                        url: riskDataCheck.baseUrl + '/handleHighRiskInfo/' + id + '/' + handleMode + '/' + accountNo + '/' + choo + '/' + customerNo + '/' + userId + '/' + reason,
                        type: 'get',
                        dataType: "json",
                        success: function (res) {
                            if (res.code === 'ACK') {
                                layerTips.msg(res.message);
                                layerTips.close(index);
                                layer.close(addBoxIndex);
                                $('#riskDetailDataTable').bootstrapTable('refresh', riskDataCheck.pageNo);
                                // location.reload();
                            } else {
                                layerTips.msg(res.message);
                            }
                        }
                    });
                    //这里可以写ajax方法提交表单
                    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                });
            },
            end: function () {
                addBoxIndex = -1;
            }
        });

    });
}
function sub(id,addBoxIndex, index,accountNo,customerNo,choo,layerTips,layer) {
    var handleMode = $("#handleMode").val();
    var userId = $("#kyh").val();
    var reason = $("#disReason").val();
    if(handleMode == null || handleMode == ""){
        layerTips.msg("处理方式不能为空!");
        return ;
    }
    if(userId == null || userId == ""){
        layerTips.msg("柜员号不能为空!");
        return ;
    }
    if(reason == null || reason == ""){
        layerTips.msg("原因不能为空!");
        return ;
    }
    $.ajax({
        url: riskDataCheck.baseUrl + '/handleHighRiskInfo/' + id + '/' + handleMode + '/' + accountNo + '/' + choo + '/' + customerNo + '/' + userId + '/' + reason,
        type: 'get',
        dataType: "json",
        success: function (res) {
            if (res.code === 'ACK') {
                layerTips.msg(res.message);
                layerTips.close(index);
                layer.close(addBoxIndex);
                $('#riskDetailDataTable').bootstrapTable('refresh', riskDataCheck.pageNo);
                // location.reload();
            } else {
                layerTips.msg(res.message);
            }
        }
    });
    //这里可以写ajax方法提交表单
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}

//恢复
function restoreRiskInfo1(row) {
    var accountNo = row.accountNo;
    var id = row.id;
    //标记1为处理企业下所有账号,0为单个账号
    var choo = '0';
    var customerNo = row.customerNo;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get('../highRisk/nonHandle.html', null, function (form) {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        addBoxIndex = layer.open({
            type: 1,
            title: '风险处理',
            content: form,
            btn: ['提交', '取消'],
            shade: false,
            offset: ['20px', '20%'],
            area: ['50%', '50%'],
            maxmin: true,
            yes: function (index) {
                sub(id,addBoxIndex, index,accountNo,customerNo,choo,layerTips,layer)
                // $('form.layui-form').find('button[lay-filter=edit]').click();
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
                var form = layui.form;
                form.render();
                form.on('submit(edit)', function () {
                    var handleMode = $("#handleMode").val();
                    var userId = $("#kyh").val();
                    var reason = $("#disReason").val();
                    $.ajaxSettings.async = false;
                    $.ajax({
                        url: riskdataTrade.baseUrl + '/handleHighRiskInfo/' + id + '/' + handleMode + '/' + accountNo + '/' + choo + '/' + customerNo + '/' + userId + '/' + reason,
                        type: 'get',
                        dataType: "json",
                        success: function (res) {
                            if (res.code === 'ACK') {
                                layerTips.msg(res.message);
                                layerTips.close(index);
                                layer.close(addBoxIndex);
                                $('#riskDetailDataTable').bootstrapTable('refresh', riskdataTrade.pageNo);
                            } else {
                                layerTips.msg(res.message);
                            }
                        }
                    });
                    //这里可以写ajax方法提交表单
                    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                });
            },
            end: function () {
                addBoxIndex = -1;
            }
        });

    });
}

riskDataCheck.queryParams = function (params) {
    if (!params)
        return {
            status: $("#statusList").val(),
            accountNo: $("#accountNoList").val(),
            accountType: $("#acctTypeList").val(),
            bankName: $("#bankNameList").val(),
            startEndTime: $("#startEndTimeList").val(),
            modelName: $("#modelName").val(),
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit) + 1, //页码
        status: $("#statusList").val(),
        accountNo: $("#accountNoList").val(),
        accountType: $("#acctTypeList").val(),
        bankName: $("#bankNameList").val(),
        startEndTime: $("#startEndTimeList").val(),
        modelName: $("#modelName").val(),
    };
    return temp;
};

riskDataCheck.init = function (customerNo) {
    riskDataCheck.table = $('#' + riskDataCheck.tableId).bootstrapTable({
        //请求后台的URL（*）
        url: riskDataCheck.baseUrl + '/highRiskListInit?customerNo=' + customerNo,
        method: 'get', //请求方式（*）
        //  toolbar: '#' + riskDataCheck.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        showColumns: true,
        sortable: true, //是否启用排序
        sortOrder: riskDataCheck.order, //排序方式
        queryParams: riskDataCheck.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: false, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: riskDataCheck.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: riskdataTrade.columns(),
        responseHandler: function (res) {
            //     return {total:res.totalRecord, rows: res.list};
            return {total: res.data.totalRecord, rows: res.data.list};
        }
        , onLoadError: function (status) {
            ajaxError(status);
        }
    });
};
laydate.render({
    elem: '#startEndTimeList'
    , type: 'date'
    , range: '~'
    , format: 'yyyy-MM-dd'
});
layui.use(['form', 'layedit', 'laydate', 'upload'], function () {

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get("../../highRisk/findAcctType", function (res) {
        if (res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i] + '" >' + changeAcctType(res.data[i]) + '</option>';
                $('#acctTypeList').append('<option value="' + res.data[i] + '" >' + changeAcctType(res.data[i]) + '</option>');
            }
        }
        form.render();
    });
    //根据条件查询
    $('#btn_queryList').on('click', function () {
        var queryParams = riskDataCheck.queryParams();
        queryParams.pageNumber = 1;
        riskDataCheck.table.bootstrapTable('refresh', queryParams);
    });
    laydate.render({
        elem: '#startEndTimeList'
        , type: 'date'
        , range: '~'
        , format: 'yyyy-MM-dd'
    });
});

function selectRiskData(row) {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get('detail.html', null, function (form) {
        addBoxIndex = layer.open({
            type: 1,
            title: '详情',
            content: form,
            shade: 0.1,
            area: ['70%', '70%'],
            async: false,
            move: false,
            success: function (layero, index) {
                var columns = new getColumns();
                div = "";
                var options = $("#modelName option:selected");

                for (var i = 1; i < columns.length; i++) {
                    var field = columns[i].field;
                    div = "<div class=\"col-md-6\"><label class=\"layui-form-label\">" + columns[i].title + "</label>\n" +
                        "  <div class=\"layui-input-inline\">\n" +
                        "   <input type=\"text\" class=\"layui-input\" id=\"jgName\" readonly=\"true\" value='" + row[field] + "'>\n" +
                        "  </div></div>";
                    $('#details').append(div);
                }
            }
        });
    });
}

