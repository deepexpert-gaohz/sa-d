
var menu = {
    baseUrl: "../../businessStatistics",
    entity: "menu",
    tableId: "menuTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    code: "id",
    parentCode: "parentId",
    explandColumn: 0
};
menu.columns = function () {
    return [
        {
            field: 'name',
            title: '机构',
            width: '40%'
        }, {
            field: 'acctOpen',
            title: '新开户',
            width: '20%'
        }, {
            field: 'acctChange',
            title: '变更',
            width: '20%'
        }, {
            field: 'acctRevoke',
            title: '撤销',
            width: '20%'
        }];
};

//初始化页面上面的按钮事件
$('#btn_query').on('click', function () {
    if($("#createddatestart").val() == "" || $("#createddateend").val() == "") {
        layer.alert("请输入开始日期和结束日期");
        return;
    }
    menu.table.bootstrapTreeTable('refresh', menu.queryParams());
});

//得到查询的参数
menu.queryParams = function (params) {
    if (!params)
        return {
            createddatestart: $("#createddatestart").val(),
            createddateend: $("#createddateend").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        createddatestart: $("#createddatestart").val(),
        createddateend: $("#createddateend").val()
    };
    return temp;

};
menu.init = function (parentId) {
    menu.table = $('#' + menu.tableId).bootstrapTreeTable({
        id: menu.unique,// 选取记录返回的值
        code: menu.code,// 用于设置父子关系
        parentCode: menu.parentCode,// 用于设置父子关系
        rootCodeValue: parentId,
        url: menu.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + menu.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        ajaxParams: menu.queryParams,//传递参数（*）
        expandColumn: menu.explandColumn,//在哪一列上面显示展开按钮,从0开始
        queryParams: menu.queryParams,//传递参数（*）
        expandAll: true,
        columns: menu.columns(),
        clickRow: menu.clickRow,
        responseHandler: function (res) {

            return res.data;

        }
    });
};
menu.select = function (layerTips) {
    var rows = menu.table.bootstrapTreeTable('getSelections');
    if (rows.length == 1) {
        menu.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};
menu.clickRow = function () {
    element.refresh();
};
menu.refresh = function () {
    menu.table.bootstrapTreeTable("refresh");
};


layui.use(['form', 'layedit', 'laydate'], function () {
    $.get('../../organization/root', function(dat) {
        menu.init(dat.data.parentId);
    });
    $('#' + menu.tableId + '>.treegrid-tbody>tr').click(function () {
        var rows = menu.table.bootstrapTreeTable('getSelections');
        menu.currentItem = rows[0];
        alert();
    });

    var editIndex;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });
})
;
