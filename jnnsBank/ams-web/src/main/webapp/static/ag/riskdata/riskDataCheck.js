var riskDataCheck = {
    baseUrl: "../../riskData",
    tableId: "riskDetailDataTable",
    toolbarId: "riskDetailDataTable",
    unique: "id",
    order: "asc",
    currentItem: {},
    modelId:'',
    tableNameOptions: ''
};
/**
 * 默认时间
 */
var laydate = layui.laydate;

riskDataCheck.queryParams = function (params) {
    if (!params)
        return {
            modelName : $("#modelName").val(),
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelName : $("#modelName").val(),
    };

    return temp;
};
function getColumns() {
    // 加载动态表格
    var myColumns = new Array();
    var modelId =riskDataCheck.modelId;
    $.ajax({
        url : "../../riskData/getRiskDataColumns?modelId="+modelId,
        type : 'GET',
        dataType : "json",
        async : false,
        success : function(returnValue) {
            // 异步获取要动态生成的列
            if(returnValue!=null){
                var arr = eval(returnValue.data);
                $.each(arr, function(i, item) {
                    myColumns.push({
                        "field" : item.fieldsEn,
                        "title" :item.fieldsZh
                    });
                });
            }
        }
    });
    return myColumns;
}
riskDataCheck.init = function (modelId,riskPoint,riskDate,status) {
    riskDataCheck.modelId = modelId;
    var columns =  new getColumns();
    var riskPointVal = encodeURIComponent(riskPoint);
    riskDataCheck.table = $('#' + riskDataCheck.tableId).bootstrapTable({
        //请求后台的URL（*）
        url: riskDataCheck.baseUrl + '/queryModelListDetails?modelId='+modelId+'&riskPoint='+riskPointVal+"&status="+status+"&riskDate="+riskDate,
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
        columns: columns,
        responseHandler: function (res) {
            return {total:res.totalRecord, rows: res.list};
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    //初始化页面上面的按钮事件
    $('#btn_query_report').on('click', function () {
        var queryParams = riskDataCheck.queryParams();
        queryParams.pageNumber=1;
        riskDataCheck.table.bootstrapTable('refresh', queryParams);
    });
    laydate.render({
        elem: '#startEndTimeTwo'
        ,type: 'date'
        ,range: '~'
        ,format: 'yyyy-MM-dd'
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
                async:false,
                move:false,
                success: function (layero, index) {
                    var columns = new getColumns();
                    div="";
                    var options=$("#modelName option:selected");

                    for (var i = 1; i < columns.length; i++) {
                       var field = columns[i].field;
                        div = "<div class=\"col-md-6\"><label class=\"layui-form-label\">"+columns[i].title+"</label>\n" +
                           "  <div class=\"layui-input-inline\">\n" +
                           "   <input type=\"text\" class=\"layui-input\" id=\"jgName\" readonly=\"true\" value='"+row[field]+"'>\n" +
                           "  </div></div>";
                        $('#details').append(div);
                    }
                }
            });
        });
}

