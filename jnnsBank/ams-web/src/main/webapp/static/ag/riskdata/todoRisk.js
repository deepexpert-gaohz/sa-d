var todoRisk = {
    baseUrl: "../../riskHandleInfo",
    tableId: "todoRiskTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: '',
    cjrqVal:"",
    pageNo:""
};
/**
 * 获取页面上的前一天时间
 * @type {string|*}
 */
var laydate = layui.laydate;
var curDate = new Date();
var now = new Date();
now.setMonth(now.getMonth()-1);//当前时间减一个月
var preDate = new Date(now.getTime()).Format("YYYY-MM-dd"); //前一天
var nextDate = new Date(curDate.getTime()).Format("YYYY-MM-dd"); //后一天
var startEndDate = preDate+" ~ "+nextDate
$("#startEndTime").val(startEndDate)
todoRisk.columns = function () {
    return [ {
        field: 'riskDate',
        title: '风险日期'
    } ,{
        field: 'accountNo',
        title: '账号'
    },{
        field: 'accountName',
        title: '账户名称'
    },{
        field: 'accountStatus',
        title: '账户性质',
        formatter: function operateFormatter(accountStatus){
            return changeAcctType(accountStatus);
        }
    },{
        field: 'name',
        title: '模型名称'
    },{
        field: 'riskDesc',
        title: '风险描述'
    },{
        field: 'bankCode',
        title: '开户机构'
    },{
        field: 'accCreateDate',
        title: '开户日期'
    },{
        field: 'riskId',
        title: '风险编号',
        visible: false
    }, {
        field: 'riskPoint',
        title: '风险点',
        visible: false
    },{
        field: 'status',
        title: '处理状态',
        formatter: function (status) {
            return status==0 ? "待处理" : "已处理";
        }
    },{
        field: "riskTable",
        title: '操作',
        width:'50',
        formatter: function operateFormatter(e, value, row){
            if (value.status == '0') {
                 butVal = "&nbsp<a style='color: blue' class='handleRisk'>处理</a>";
             } else {
                butVal = "&nbsp<a style='color:#ffaeb1 ' class=''>处理</a>";
             }
            return butVal;
        },
        events: {
            'click .handleRisk': function (e, value, row) {
                handleRiskInfo(row);
            }
        }
    }];
};
todoRisk.init = function () {

    todoRisk.table = $('#' + todoRisk.tableId).bootstrapTable({
        url: todoRisk.baseUrl + '/querytodoRiskInfo', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + todoRisk.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: todoRisk.order, //排序方式
        queryParams: todoRisk.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 30, //每页的记录行数（*）
        pageList: [10, 30, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: todoRisk.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: todoRisk.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                todoRisk.pageNo = res.data.offset
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        },onLoadSuccess: function (data) {
            mergeCells(data.rows, "riskDate", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "name", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "accountNo", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "accountName", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "accountStatus", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "riskDesc", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "bankCode", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "accCreateDate", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "riskId", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "riskPoint", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "status", 1, todoRisk.tableId);//行合并
            mergeCells(data.rows, "riskTable", 1, todoRisk.tableId);//行合并
        }
    });
};
/**
 * 合并单元格
 * @param data  原始数据（在服务端完成排序）
 * @param fieldName 合并属性名称
 * @param fieldName2 指定行方向可能的合并字段名称
 * @param target    目标表格对象
 * 以下方法可实现动态行与列的同时合并
 */
function mergeCells(data,fieldName,fieldName2,target){
    //声明一个map计算相同属性值在data对象   列方向上出现的次数和
    var colName = fieldName;
    var colVal = '',colValRiskDate = '',colValaccountName = '',colValAccountNo = '',colValaccountStatus='';
    var rowStart = 0;
    var rowCount = 0;
    for (var j = 0; j < data.length; j++) {
        var row = data[j];
        if (colVal == '') {
            colVal = row[colName];
            colValRiskDate = row['riskDate'];
            colValaccountName= row['accountName'];
            colValAccountNo = row['accountNo'];
            colValaccountStatus = row['accountStatus'];
            rowCount++;
        } else {
            if (colVal == row[colName] && colValRiskDate == row['riskDate'] && colValaccountName ==row['accountName'] && colValAccountNo == row['accountNo'] && colValaccountStatus == row['accountStatus']) {// 行的值相同
                // 计数加1
                rowCount++;
                // 最后一行
                if (j == data.length - 1) {
                    $('#'+target).bootstrapTable('mergeCells', {
                        index: rowStart,
                        field: colName,
                        rowspan: rowCount
                    });
                }
            } else {// 行值不同，将前面相同行值的所有行合并
                $('#'+target).bootstrapTable('mergeCells', {
                    index: rowStart,
                    field: colName,
                    rowspan: rowCount
                });
                colVal = row[colName];
                colValRiskDate = row['riskDate'];
                colValaccountName = row['accountName'];
                colValAccountNo = row['accountNo'];
                colValaccountStatus = row['accountStatus'];
                rowCount = 1;
                rowStart = j;
            }
        }
    }
};
todoRisk.queryParams = function (params) {
    if (!params)
        return {
            modelId : $("#modelId").val(),//模型名称
            accountName : $("#accountName").val(),//账户名称
            accountNo : $("#accountNo").val(),//账户号
            status : $("#status").val(),//账户号
            startEndTime : $("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelId : $("#modelId").val(),//模型名称
        accountName : $("#accountName").val(),//账户名称
        accountNo : $("#accountNo").val(),//账户号
        status : $("#status").val(),//账户号
        startEndTime : $("#startEndTime").val()
    };
    return temp;
};


laydate.render({
    elem: '#startEndTime'
    ,type: 'date'
    ,range: '~'
    ,format: 'yyyy-MM-dd'
});

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var form=layui.form;

    todoRisk.init();
    $.get("../../model/getModels", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.list.length; i++){
                options += '<option value="' + res.data.list[i].modelId + '" >' +res.data.list[i].name + '</option>';
                $('#modelId').append('<option value="' + res.data.list[i].modelId  + '" >' + res.data.list[i].name + '</option>');
            }
        }
        form.render();
    });
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = todoRisk.queryParams();
        queryParams.pageNumber=1;
        todoRisk.table.bootstrapTable('refresh', queryParams);
    });
});
/**
 * 处理风险模型
 * add by yangcq
 * 20190622
 */
function handleRiskInfo(row){
    var accountNo = row.accountNo;
    var id  = row.id;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get('handle.html', null, function (form) {
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
                    var dubiousReason = encodeURIComponent($("#dubiousReason").val());
                    var counterEmp = $("#counterEmp").val();
                    $.ajaxSettings.async = false;
                    $.ajax({
                        url: todoRisk.baseUrl + '/handleRiskInfo/'+id+'/'+handleMode+'/'+accountNo+"/"+dubiousReason+"/"+counterEmp,
                        type: 'get',
                        dataType: "json",
                        success: function (res) {
                            console.log(res.code)
                            if (res.code === 'ACK') {
                                layerTips.msg(res.message);
                                layerTips.close(index);
                                layer.close(addBoxIndex);
                                $.get(todoRisk.baseUrl+'/countHandle', function(data) {
                                    if(data!=null&&data.data!=null){
                                        $('#acctRiskCount').html(data.data.totalRecord);
                                    }
                                });
                                //location.reload();
                                $('#todoRiskTable').bootstrapTable('refresh',todoRisk.pageNo);
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
//统计登记簿数量
$.get(todoRisk.baseUrl+'/countHandle', function(data) {
    if(data!=null&&data.data!=null){
        $('#acctRiskCount').html(data.data.totalRecord);
    }
});

$('#todoRisk').on('click', function () {
    parent.tab.tabAdd({
        href: '../ui/riskdata/handleList.html',
        icon: 'fa fa-calendar-check-o',
        title: '账户风险处理登记簿'
    });
});