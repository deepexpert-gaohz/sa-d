layui.config({
    base: '../js/'
});

var taskId_global;

var list = {
    // baseUrl: "../ag/comparison/json",
    baseUrl: "../../compareResult",
    entity: "result",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    columnsData: [{
        field: 'match',
        title: '是否匹配',
        formatter: function (value, row, index) {
            if (value == true) {
                return '<a class="view" style="color: blue" href="#" data-id="'+row.id+'">匹配</a>';
            } else {
                return '<a class="view" style="color: red" href="#" data-id="'+row.id+'">不匹配</a>';
            }
        }
    },
    //     {
    //     field: 'account',
    //     title: '账号'
    // }
    {
        field: 'id',
        title: 'ID',
        visible: false
    }]
};
list.columns = function () {
    // $.ajax({
    //     type: "get",
    //     url: list.baseUrl + "/struct/" + taskId_global,
    //     async: false,
    //     success: function (res) {
    //         for(var key in res){
    //             list.columnsData.push({field: key, title: res[key]});
    //         }
    //
    //     }
    // });

    var res = {'account': '账号', 'depositorName': '存款人名称'};
    for(var key in res){
        list.columnsData.push({field: key, title: res[key]});
    }

    return list.columnsData;
};

list.queryParams = function (params) {
    if (!params)
        return {
            organId: $("#organId").val(),
            compareTaskId: taskId_global
        };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            size: params.limit, //页面大小
            page: params.offset / params.limit, //页码
            organId: $("#organId").val(),
            compareTaskId: taskId_global
        };
      return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        // url: list.baseUrl + '/resultDetailList.json', //请求后台的URL（*）
        url: list.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + list.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list.order, //排序方式
        queryParams: list.queryParams,//传递参数（*）
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
        uniqueId: list.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list.columns(),
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};

list.select = function (layerTips) {
    var rows = list.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        list.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.define(['jquery', 'element', 'layer', 'laydate', 'laytpl', 'treeTable','common'], function (exports) {
    var $ = layui.jquery,
    element = layui.element,
    layer = layui.layer,
	laydate = layui.laydate,
	laytpl = layui.laytpl,
    common=layui.common;

    var taskId = decodeURI(common.getReqParam("taskId"));
    taskId_global = taskId;

    list.init();

    var treeTable = layui.treeTable({
        elem: '#tree_list',
        url: '../../compareStatistics/list?taskId=' + taskId_global,
        fieldId: 'id',
        fieldPid: 'pid',
        fieldName: 'name',
        fieldRows: 'rows',
        fieldHasRows: 'hasRows',
        cols: [
            {field: 'name', title: '机构', width: 150}
            ,{field: 'totalCount', title: '账户总数', width: 50}
            ,{field: 'passCount', title: '匹配总数', width: 50}
            ,{field: 'passRate', title: '通过率', width: 50}
        ],
        handleSelected: function (id) {
            $('#organId').val(id);
            list.table.bootstrapTable('refresh', list.queryParams());

        }
    });

    $('a[role="menuitem"]').on('click', function () {
        var type = $(this).attr("href");
        // console.log('导出EXCEL: ' + type);
        $("#downloadFrame").prop('src', list.baseUrl + "/exportXLS/" + taskId_global + "?matchType=" + type);
        return false;
    });
    
    $('a[role="menuitemtxt"]').on('click', function () {
        var type = $(this).attr("href");
        // console.log('导出TXT: ' + type);
        $("#downloadFrame").prop('src', list.baseUrl + "/exportTxt/" + taskId_global + "?matchType=" + type);
        return false;
    });
    
    $('#list').on('click','.view', function () {
		var id = $(this).attr('data-id');

		$.get(list.baseUrl + '/detail?id=' + id, null, function (result) {
        // $.get('../ag/comparison/json/resultDetailList2.json?id=' + id, null, function (result) {
            var data = result;
            
			if (data) {
				var getTpl = $('#listTpl').html();
				laytpl(getTpl).render(data, function(html){
					
					layer.open({
						type: 1, 
						area: ['700px', '400px'],
						btn: '关闭',
						content: html
					});
				});
			}
		});
        
        return false;
    });
});