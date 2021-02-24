var riskdataTrade = {
    baseUrl: "../../highRisk",
    tableId: "riskdataTradeTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    modelIdOptions: '',
    roleOptions: '',
    pageNo: ''
};
/**
 * 获取页面上的前一天时间
 * @type {string|*}
 */
var laydate = layui.laydate;
var curDate = new Date();

var preDate = new Date(curDate.getTime() - 0).Format("YYYY-MM-dd"); //前一天
var nextDate = new Date(curDate.getTime() - 0).Format("YYYY-MM-dd"); //后一天
var startEndDate = preDate + " ~ " + nextDate
$("#startEndTime").val(startEndDate)
riskdataTrade.columns = function () {
    return [
        {
            field: 'ordNum',
            title: '序号',
            visible: true
        }, {
            field: 'depositorName',
            title: '企业名称',
        }, {
            field: 'depositorcardType',
            title: '企业证件类型'
        }, {
            field: 'depositorcardNo',
            title: '企业证件号'
        }, {
            field: 'legalName',
            title: '法人姓名',
        }, {
            field: 'legalIdcardNo',
            title: '法人证件号',
            height:'50'
        }, {
            field: 'riskDesc',
            title: '高风险原因',
            
        }, {
            field: 'riskDate',
            width: '100',
            title: '触发日期',
            formatter: function (value, row, index) {
                if (value == "" || value == null) {
                    return "";
                } else {
                    return new Date(value).Format("yyyy-MM-dd");
                }
            }

        }, {
            field: 'riskdataTradeTable',
            title: '账户详情',
            width: '50',
            formatter: function operateFormatter(e, value, row) {
                var butVal = "";
                butVal = "&nbsp<a style='color: blue' class='queryRiskData'>查看</a>";
                return butVal;
            },
            events: window.operatEvents = {
                'click .queryRiskData': function (e, value, row, index) {
                    queryRiskData(row)
                }
            }
        }, {
            field: "riskTable",
            title: '操作',
            width: '50',
            formatter: function operateFormatter(e, value, row) {
                var butVal1 = "";
                if(value.status == 0){
                    butVal1 = "&nbsp<a style='color: blue' class='handleRisk'>处理</a>";
                }else if(value.status == 2 || value.status == 3){
                    butVal1 = "&nbsp<a style='color: blue' class='restoreRisk'>恢复</a>";
                } else if(value.status == 4 || value.status == 5 || value.status == 1){
                    butVal1 = "&nbsp<a style='color: #ffaeb1' class=''>处理</a>";
                }
                // if (value.status == '0') {
                //     butVal = "&nbsp<a style='color: blue' class='handleRisk'>处理</a>";
                // } else {
                //     butVal = "&nbsp<a style='color:#ffaeb1 ' class=''>处理</a>";
                // }
                return butVal1;
            },
            events: {
                'click .handleRisk': function (e, value, row) {
                    handleRiskInfo(row);
                },
                'click .restoreRisk': function (e, value, row) {
                    restoreRiskInfo(row);
                }
            }
        }, {
            field: 'status',
            title: '处理状态',
            formatter: function (status) {
                var str = '';
                if(status == 0){
                    str ='待处理';
                }else if (status == 2 ||status == 3){
                    str ='已处理';
                }else {
                    str ='已恢复';
                }
                return str;
            }
        }];
};

/**
 * 查询账户详细信息
 * @param row
 */
function queryRiskData(row) {
    $.get('riskEdit.html', null, function (form) {
        addBoxIndex = layer.open({
            type: 1,
            title: row.name,
            content: form,
            shade: false,
            area: ['100%', '100%'],
            async: false,
            success: function () {
                riskDataCheck.init(row.customerNo);
                var form = layui.form;
                form.render();
            },
            end:function () {
                // $('#riskdataTradeTable').bootstrapTable('refresh', riskdataTrade.pageNo);
                location.reload();
            }
        });
    });
}

riskdataTrade.queryParams = function (params) {
    if (!params)
        return {
            status: $("#status").val(),
            depositorName: $("#depositorName").val(),
            depositorcardNo: $("#depositorcardNo").val(),
            legalName: $("#legalName").val(),
            legalIdcardNo: $("#legalIdcardNo").val(),
            riskDesc: $("#riskDesc").val(),

            startEndTime: $("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit) + 1, //页码
        status: $("#status").val(),
        depositorName: $("#depositorName").val(),
        depositorcardNo: $("#depositorcardNo").val(),
        legalName: $("#legalName").val(),
        legalIdcardNo: $("#legalIdcardNo").val(),
        riskDesc: $("#riskDesc").val(),
        startEndTime: $("#startEndTime").val(),
    };
    return temp;
};

function handleRiskInfo(row) {
    var accountNo = row.accountNo;
    var id = row.id;
    //标记1为处理企业下所有账号,0为单个账号
    var choo = '1';
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
                $('form.layui-form').find('button[lay-filter=edit]').click();
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

                                $('#riskdataTradeTable').bootstrapTable('refresh', riskdataTrade.pageNo);
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
function restoreRiskInfo(row) {
    var accountNo = row.accountNo;
    var id = row.id;
    //标记1为处理企业下所有账号,0为单个账号
    var choo = '1';
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
                $('form.layui-form').find('button[lay-filter=edit]').click();
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

                                $('#riskdataTradeTable').bootstrapTable('refresh', riskdataTrade.pageNo);
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
riskdataTrade.init = function () {

    riskdataTrade.table = $('#' + riskdataTrade.tableId).bootstrapTable({
        url: riskdataTrade.baseUrl + '/highRiskDataInit', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + riskdataTrade.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: riskdataTrade.order, //排序方式
        queryParams: riskdataTrade.queryParams,//传递参数（*）
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
        uniqueId: riskdataTrade.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: riskdataTrade.columns(),

        responseHandler: function (res) {
            if (res.code === 'ACK') {
                riskdataTrade.pageNo = res.data.offset
                return {total: res.data.totalRecord, rows: res.data.list};
            } else {
                return false;
            }
        }
        , onLoadError: function (status) {
            ajaxError(status);
        }
    });
};
laydate.render({
    elem: '#startEndTime'
    , type: 'date'
    , range: '~'
    , format: 'yyyy-MM-dd'
});

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var form = layui.form;
    riskdataTrade.init();

    //根据条件查询
    $('#btn_query').on('click', function () {
        var queryParams = riskdataTrade.queryParams();
        queryParams.pageNumber = 1;
        riskdataTrade.table.bootstrapTable('refresh', queryParams);
    });


});