var menu = {
  baseUrl: "../../annualTask",
  entity: "menu",
  tableId: "menuTable",
  toolbarId: "toolbar",
  unique: "id",
  order: "asc",
  currentItem: {},
  code: "id",
  parentCode: "parentId",
  rootValue: -1,
  explandColumn: 0
};
menu.columns = function () {
  return [
      {
          field: 'name',
          title: '机构',
          width: '40%'
      }, {
          field: 'annualTotalCount',
          title: '账号总数',
          width: '20%'
      }, {
          field: 'annualPassCount',
          title: '通过总数',
          width: '20%'
      }, {
          field: 'annualPassRate',
          title: '通过率',
          width: '20%'
      }];
};

menu.init = function (taskId) {
    if(taskId != null && taskId != '') {
        menu.table = $('#' + menu.tableId).bootstrapTreeTable({
            id: menu.unique,// 选取记录返回的值
            code: menu.code,// 用于设置父子关系
            parentCode: menu.parentCode,// 用于设置父子关系
            rootCodeValue: menu.rootValue,
            url: menu.baseUrl + '/statisticss/' + taskId, //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + menu.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）

            expandColumn: menu.explandColumn,//在哪一列上面显示展开按钮,从0开始

            expandAll: true,
            columns: menu.columns(),
            clickRow: menu.clickRow,
            responseHandler: function (res) {

                return res.data;

            }
        });
    }
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
// menu.clickRow = function () {
//     element.refresh();
// };
menu.refresh = function () {
    menu.table.bootstrapTreeTable("refresh");
};


layui.use(['form', 'layedit', 'laydate'], function () {
    $.get('../../annualTask/getAnnualTaskId', function (data) {
        menu.init(data);
    })
    $('#' + menu.tableId + '>.treegrid-tbody>tr').click(function () {
        var rows = menu.table.bootstrapTreeTable('getSelections');
        menu.currentItem = rows[0];
        alert();
    });
    var allMenu = null;
    var editIndex;
    // $.get(menu.baseUrl + '/statistics', null, function
    // (data) {
    //     allMenu = data.data;
    // });
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit;

})
;
