var riskHandLeData = {
    baseUrl: "../../riskHandleInfo",
    tableId: "riskHandLeTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: '',
    cjrqVal:"",
    nonHandleRiskData:{}

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
var nextDate = new Date(curDate.getTime() ).Format("YYYY-MM-dd"); //后一天
var startEndDate = preDate+" ~ "+nextDate
$("#startEndTime").val(startEndDate)
riskHandLeData.columns = function () {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer; //获取父窗口的layer对象

    return [ {
        field: 'ordNum',
        title: '序号',
        visible: false
    }, {
        field: 'riskDate',
        title: '风险日期'
    },{
        field: 'name',
        title: '模型名称'
    }, {
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
        visible: false
    },{
        field: 'handler',
        title: '处理人'
    }, {
        field: 'handleDate',
        title: '处理时间'
        },{
        field: 'handleMode',
        title: '处理方式',
        formatter: function (handleMode) {
            var handleModelVal="";
            if(handleMode== '1'){
                handleModelVal="不处理";
            }else if(handleMode== '2'){
                handleModelVal="止付";
            }else if(handleMode== '3'){
                handleModelVal="暂停非柜面";
            }else if(handleMode== '4'){
                handleModelVal="解付";
            }
            else if(handleMode== '5'){
                handleModelVal="恢复非柜面";
            }
            return handleModelVal;
        }
    }, {
        field: 'dubiousReason',
        title: '处理原因',
        width: 100,
         formatter: function (dubiousReason) {
            return dubiousReason;
         }

     },{
        field: 'id',
        title: 'id',
        visible: false
    },{
        field: "riskTable",
        title: '操作',
        formatter: function operateFormatter(e, value, row){
            if(value.status=='4'||value.status=='5'||value.status=='1'){
                return "<a style='color: #ffaeb1' class='nonHandleRiskAt'>处理</a>"
            }else{
                riskHandLeData.onHandleRiskData = value
                return "<a style='color: blue' class='nonHandleRisk'>处理</a>"
            }
        },
        events: {
            'click .nonHandleRisk': function (e, value, row) {
                nonHandleRiskInfo(row);
            }, 'click .nonHandleRiskAt': function (e, value, row) {
                var trip = "";
                if(row.status=='5'){
                    trip ="恢复非柜面业务";
                }else if(row.status=='4'){
                    trip ="解付";
                }else if(row.status=='1'){
                    trip ="不处理";
                }
                layerTips.msg("已"+trip);
            }
        }
    }];
};

function queryHandleRiskData(row){
    $.get('edit.html', null, function (form) {
        addBoxIndex = layer.open ({
            type: 1,
            title: '查看处理结果',
            content: form,
            shade: false,
            area: ['100%', '100%'],
            async:false,
            success: function () {
                riskHandLeData.init(row);
                var form = layui.form;
                form.render();
            }
        });
    });
}
riskHandLeData.queryParams = function (params) {
    if (!params)
        return {
            modelId : $("#modelId").val(),//模型名称
            riskType : $("#riskType").val(),//风险类型
            accountNo : $("#accountNo").val(),//客户名称
            accountName : $("#accountName").val(),//客户号
            handleMode : $("#handleModel").val(),//处理方式
            startEndTime : $("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelId : $("#modelId").val(),//模型名称
        riskType : $("#riskType").val(),//风险类型
        accountNo : $("#accountNo").val(),//账号
        accountName : $("#accountName").val(),//账号名称
        handleMode : $("#handleModel").val(),//处理方式
        startEndTime : $("#startEndTime").val()
    };
    return temp;
};

riskHandLeData.init = function () {
    var dataRes = "";
    riskHandLeData.table = $('#' + riskHandLeData.tableId).bootstrapTable({
        url: riskHandLeData.baseUrl + '/queryHandleRiskInfo', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + riskHandLeData.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: riskHandLeData.order, //排序方式
        queryParams: riskHandLeData.queryParams,//传递参数（*）
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
        uniqueId: riskHandLeData.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: riskHandLeData.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord,
                        rows: res.data.list};
            } else {
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        },
        onLoadSuccess: function (data) {
           mergeCells(data.rows, "riskDate", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "name", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "accountNo", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "accountName", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "accountStatus", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "riskDesc", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "bankCode", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "accCreateDate", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "riskId", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "riskPoint", 1, riskHandLeData.tableId);//行合并
            mergeCells(data.rows, "riskTable", 1, riskHandLeData.tableId);//行合并
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
    var colVal = '',colValRiskDate = '',colValRiskId = '',colValAccountNo = '';
    var rowStart = 0;
    var rowCount = 0;
    for (var j = 0; j < data.length; j++) {
        var row = data[j];
        if (colVal == '') {
            colVal = row[colName];
            colValRiskDate = row['riskDate'];
            //colValRiskId = row['riskId'];
            colValAccountNo = row['accountNo'];
            rowCount++;
        } else {
            if (colVal == row[colName] && colValRiskDate == row['riskDate']  && colValAccountNo == row['accountNo']) {// 行的值相同
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
                //colValRiskId = row['riskId'];
                colValAccountNo = row['accountNo'];
                rowCount = 1;
                rowStart = j;
            }
        }
    }
};
laydate.render({
    elem: '#startEndTime'
    ,type: 'date'
    ,range: '~'
    ,format: 'yyyy-MM-dd'
});

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var form=layui.form;
    riskHandLeData.init();
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
        var queryParams = riskHandLeData.queryParams();
        queryParams.pageNumber=1;
        riskHandLeData.table.bootstrapTable('refresh', queryParams);
    });
});

/**
 * 解付/恢复非柜面业务
 * @yangcq
 * @date 20190626
 * @param row
 */
function nonHandleRiskInfo(row){
    var accountNo = row.accountNo;
    var id  = row.id;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    $.get('nonHandle.html', null, function (form) {
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
                var  handleModeVal = row.handleMode
                if(handleModeVal=='2'){
                    $('#nonHandleMode').append('<option value="4" >解付</option>');
                }else if(handleModeVal=='3'){
                    $('#nonHandleMode').append('<option value="5" >恢复非柜面</option>');
                }
                form.render();
                form.on('submit(edit)', function () {
                    var nonHandleMode = $("#nonHandleMode").val();
                    var nonDubiousReason = encodeURIComponent($("#nonDubiousReason").val());
                    var counterEmp = $("#counterEmp").val();
                    $.ajaxSettings.async = false;
                    $.ajax({
                        url: riskHandLeData.baseUrl + '/nonHandleRiskInfo/'+id+'/'+nonHandleMode+'/'+accountNo+"/"+nonDubiousReason+"/"+counterEmp,
                        type: 'get',
                        dataType: "json",
                        success: function (res) {
                            if (res.code === 'ACK') {
                                layerTips.msg(res.message);
                                layerTips.close(index);
                                layer.close(addBoxIndex);
                                $('#riskHandLeTable').bootstrapTable('refresh');
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
};
