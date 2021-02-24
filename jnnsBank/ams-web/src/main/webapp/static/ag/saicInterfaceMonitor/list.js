layui.config({
  base: '../js/'
});
layui.define(['jquery', 'element', 'layer', 'laydate', 'treeTable','form'], function (exports) {
	var $ = layui.jquery,
	element = layui.element,
	layer = layui.layer,
	laydate = layui.laydate;
	form = layui.form;

	viewRecordList = function(id){
		parent.tab.tabAdd({
			href: 'saicInterfaceMonitor/recordList.html?organId='+id,
			icon: 'fa fa-calendar-plus-o',
			title: '工商调用记录'
		});
	}

	var treeTable = layui.treeTable({
		elem: '#tree_list',
		url: '../../saicMonitor/info/list',
		fieldId: 'id',
		fieldPid: 'pid',
		fieldName: 'name',
		fieldRows: 'rows',
		fieldHasRows: 'hasRows',
		cols: [
			{field: 'name', title: '机构', width: 150},
			{field: 'num1', title: '数量', width: 80},
			{field: 'id', title: '操作', width: 80, render: function(value, rows){
				return '<a href="javascript:viewRecordList(\''+value+'\')" style="color: blue;">查看</ a>'
			}}
		]
  	});

});