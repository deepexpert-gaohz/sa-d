/*案例查询
* 修改人：杨春秋
* 时间：2019-05-25
* 用于风险管理员发起案例流程
*
*
*/

var modelCount = {
    baseUrl: "../../modelCount",
    baseWorkUrl: "../../workFlow",
    entity: "modelCount",
    tableId: "modelCountTable",
    toolbarId: "toolbar",
    code: "id",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions:'',
    relyTableOptions:'',
    ids:'',
    keys:''
};

modelCount.columns = function () {
    return [{
        checkbox:true
    }, {
        field: 'Button',
        title: '操作',
        formatter: function operateFormatter(e, value, row) {
            var butVal ="";
            if (value.status == '0') {
                butVal = "<button id='btn_start' onclick='start(value);'  style='background:#cccccc' disabled=\"disabled\"  type='button' class='layui-btn layui-btn-small'>发起流程</button>";
            } else {
                 butVal = "<button id='btn_start' onclick='start(value);' type='button' class='layui-btn layui-btn-small'>发起流程</button>";
            }
            return butVal;
        },
        events: {
            'click #btn_start': function (e, value, row) {
                start(row)
            },
        }
    },{
        field: 'cjrq',
        title: '数据日期',
    }, {
        field: 'flowName',
        title: '流程名称',
    },{
        field: 'modelName',
        title: '模型名称',
    },{
        field: 'riskId',
        title: '风险点编号',
    },{
        field: 'officeName',
        title: '机构名称',
    },{
        field: 'khId',
        title: '账户号',
    },{
        field: 'khName',
        title: '账户名称',
    },{
        field: 'riskAmt',
        title: '风险金额',
    },{
        field: 'key',
        title: 'key',
        visible: false
    },{
        field: 'status',
        title: '状态',
        visible: true,
        formatter: function (status) {
            var statusVal = "";
            if(status=='0'){
                statusVal="已发起";
            }
            return status=="1" ? "<font style='color: red'>待发起</font>" :statusVal;
        }
    }];
};


modelCount.queryParams = function (params) {
    if (!params)
        return {
            modelName: $("#modelName").val(),
            officeName: $("#officeName").val(),
            riskId: $("#riskId").val(),
            khId: $("#khId").val(),
            khName: $("#khName").val(),
            dataDate:$("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelName: $("#modelName").val(),
        officeName: $("#officeName").val(),
        riskId: $("#riskId").val(),
        khId: $("#khId").val(),
        khName: $("#khName").val(),
        dataDate:$("#startEndTime").val()
    };
    return temp;
};

modelCount.select = function (layerTips) {
    var rows = modelCount.table.bootstrapTable('getSelections');

    if (rows.length != 0) {
        modelCount.currentItem = rows[0];
        for (var i=0;i<rows.length;i++){
            if(rows[i].status == 1){
                if(i == rows.length-1){
                    modelCount.ids += rows[i].id
                    modelCount.keys += rows[i].key
                }else {
                    modelCount.ids += rows[i].id+"-";
                    modelCount.keys += rows[i].key+"-";
                }
            }
        }
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};
function start(row){
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    layer.confirm('确定发起流程吗？', null, function (index) {
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer; //获取父窗口的layer对象
        //alert(row.id);
        var id= row.id;
        var key= row.key;
        $.ajax({
            url: "../../workFlow/startFlow/"+id+"/"+key,
            type: "POST",
            success: function (data) {

                if (data.code === 'ACK') {
                    layerTips.msg(data.message);
                    location.reload();
                } else {
                    layerTips.msg(data.message)
                    location.reload();
                }
            }
        });
        layer.close(index);
    })
}

modelCount.init = function () {
    modelCount.table = $('#' + modelCount.tableId).bootstrapTable({
        url: modelCount.baseUrl + '/getModelCount', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + modelCount.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: modelCount.order, //排序方式
        queryParams: modelCount.queryParams,//传递参数（*）
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
        uniqueId: modelCount.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: modelCount.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    modelCount.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = modelCount.queryParams();
        queryParams.pageNumber=1;
        modelCount.table.bootstrapTable('refresh', queryParams);
    });
    /**
     * 批量发起流程
     * @杨春秋
     * @2019-05-27
     */
    $("#batchStartFlow").on('click',function () {
        if (modelCount.select(layerTips)) {
            var ids = modelCount.ids;
            var keys = modelCount.keys
            layer.confirm('确定批量发起流程吗？', null, function (index) {
                if(ids != null && ids != '' && keys != null && keys != ''){
                    $.ajax({
                        url: modelCount.baseWorkUrl+"/startBatchFlow/"+ids+"/"+keys,
                        type: "POST",
                        success: function (data) {
                            if (data.code === 'ACK') {
                                layerTips.msg(data.message)
                                location.reload();
                            } else {
                                layerTips.msg(data.message)
                                location.reload();
                            }
                        }
                    });
                    layer.close(index);
                }else {
                    layerTips.msg("该流程已发起");
                    layer.close(index);
                }
            });
        }
    });
});