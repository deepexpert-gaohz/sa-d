layui.config({
	base: '../js/'
});

var list = {
    baseUrl: "../../compareTask",
	entity: "result",
	tableId: "list",
	toolbarId: "toolbar",
	unique: "id",
	order: "asc",
	currentItem: {},
	columnsData: [
		// {
		// 	field: 'accountNo',
		// 	title: '账号'
		// }
	]
};
list.columns = function () {
	return list.columnsData;
};

list.queryParams = function (params) {
	if (!params)
		return {
			dataId: $("#dataId").val(),
			accountNo: $.trim($("#accountNo").val())
		};
		var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			size: params.limit, //页面大小
			page: params.offset / params.limit, //页码
			dataId: $("#dataId").val(),
			accountNo: $.trim($("#accountNo").val())
	};
	return temp;
};

list.init = function () {

	list.table = $('#' + list.tableId).bootstrapTable({
		url: list.baseUrl + '/dataDetailList?taskId='+taskId+'&dataSourceId='+dataSourceId, //请求后台的URL（*）
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
		columns: list.columns()
		// onLoadError: function(status){
		// 	ajaxError(status);
		// }
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

var dataSourceId;
var taskId;
layui.use(['form', 'layedit', 'laydate', 'loading', 'common'], function () {
	
	var common = layui.common;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
	var dataId = decodeURI(common.getReqParam('id'));
	taskId = decodeURI(common.getReqParam('taskId'));
	dataSourceId = decodeURI(common.getReqParam('dataSourceId'));
	$("#dataId").val(dataId);
	
	$.get(list.baseUrl + '/dataDetailColumns?taskId=' + taskId, null, function (data) {
		if(data && data.list) {
			for(i=0; i<data.list.length; i++) {
				var item = data.list[i];
				list.columnsData.push({field: item.fieldName, title: item.name});
			}
			list.init();
		}
	});

	$('.date-picker').change(function(){
		var value = $(this).val();
		$(this).val(dateFormat(value));
	});

	$('#btn_query').on('click', function () {
		var queryParams = list.queryParams();
		queryParams.pageNumber=1;
		list.table.bootstrapTable('refresh', queryParams);
	});

});
