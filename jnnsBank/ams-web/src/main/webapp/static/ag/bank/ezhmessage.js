layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var ezhmessage = {
    baseUrl: "../../ezhMessage",
    entity: "ezhmessage",
    tableId: "ezhmessageTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "desc",
    currentItem: {}
};
ezhmessage.columns = function () {
    return [{
        field: 'id',
        title: 'ID',
        visible: false
    }, {
        field: 'phone',
        title: '手机号'
    }, {
        field: 'applyId',
        title: '预约编号',
        'class': 'W120'
    }, {
        field: 'message',
        title: '短信内容',
        'class': 'W300',
        visible: false
    }, {
        field: 'checkPass',
        title: '是否成功',
        formatter: function (value, row, index) {
            return formatCheckPass(value);
        }
    }, {
        field: 'errorMessage',
        title: '失败原因',
        'class': 'W200'
    }, {
        field: 'type',
        title: '通知提醒类别',
        'class': 'W120',
        formatter: function (value, row, index) {
            return formatType(value);
        }
    }, {
        field: 'operate',
        title: '操作',
        'class': 'W60',
        formatter: function (value, row, index) {
            if(row.checkPass == true) {  //短信发送成功
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='viewMessage(\"" + row.message + "\")'>查看短信内容</a>";
            } else {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='reSendMessage(\"" + row.id + "\")'>重新发送</a>" + " " +
                    "<a href=\"javascript:void(0);\" style='color: blue' onclick='viewMessage(\"" + row.message + "\")'>查看短信内容</a>";
            }
        }
    }];
};
ezhmessage.queryParams = function (params) {
    if (!params)
        return {
            phone: $("#phone").val(),
            checkPass: $("#checkPass").val(),
            type: $("#type").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        phone: $("#phone").val(),
        checkPass: $("#checkPass").val(),
        type: $("#type").val()

    };
    return temp;
};

ezhmessage.init = function () {

    ezhmessage.table = $('#' + ezhmessage.tableId).bootstrapTable({
        url: ezhmessage.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + ezhmessage.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: ezhmessage.order, //排序方式
        singleSelect: true,
        queryParams: ezhmessage.queryParams,//传递参数（*）
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
        uniqueId: ezhmessage.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: ezhmessage.columns()
        // ,
        // onDblClickRow:function (items,$element) {
        //     var applyId = items.applyid;
        //     var name = items.name;
        //     parent.tab.tabAdd({
        //         title: '开户-'+name,
        //         href: 'bank/view.html?applyId='+applyId+'&name='+encodeURI(name)
        //     });
        // }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
ezhmessage.select = function (layerTips) {
    var rows = ezhmessage.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        ezhmessage.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','layer','common'], function () {
    ezhmessage.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    var addBoxIndex = -1;

    var tabId = decodeURI(common.getReqParam("tabId"));
    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = ezhmessage.queryParams();
        queryParams.pageNumber=1;
        ezhmessage.table.bootstrapTable('refresh', queryParams);
    });

});

function reSendMessage(id) {
    layer.confirm('确认重发短信吗？', {
        btn: ['确认','取消'] //按钮
    }, function(){
        $.get(ezhmessage.baseUrl + '/sendMessage?id=' + id, function (data) {
            var queryParams = ezhmessage.queryParams();
            queryParams.pageNumber=1;
            ezhmessage.table.bootstrapTable('refresh', queryParams);
            layer.alert(data.msg);

        })

    }, function(){
        //取消
    });
}

function viewMessage(message) {
    layer.open({
        type: 1,
        title: '短信内容',
        area: ['400px', '200px'],
        content: message
    });
}