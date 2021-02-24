layui.config({
	base: '../js/'
});

var list = {
	baseUrl: "../../batch",
	entity: "batch",
	tableId: "list",
	toolbarId: "toolbar",
	unique: "id",
	order: "asc",
	currentItem: {}
};
list.columns = function () {
	return [{
		field: 'batchNo',
		title: '批次号'
	}, {
		field: 'processTime',
		title: '处理时间'
	}, {
		field: 'fileName',
		title: '文件名'
	}, {
		field: 'fileSize',
		title: '文件大小(Byte)'
	}, {
		field: 'txCount',
		title: '数据量'
	}, {
		field: 'process',
		title: '是否已完成',
		formatter: function (value, row, index) {
			return value ? '是' : '否'
		}
	}, {
		field: 'batchNo',
		title: '操作',
		formatter: function (value, row, index) {
			return '<a class="view" style="color: blue" href="#" data-id="' + value + '">查看详情</a>';
		}
	}];
};

list.queryParams = function (params) {
	if (!params)
		return {
			type: "STOCKHOLDER",
			batchNo: $.trim($("#batchNo").val()),
			processTimeStart: $("#beginDate").val(),
			processTimeEnd: $("#endDate").val()
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		size: params.limit, //页面大小
		page: params.offset / params.limit, //页码
		type: "STOCKHOLDER",
		batchNo: $.trim($("#batchNo").val()),
		processTimeStart: $("#beginDate").val(),
		processTimeEnd: $("#endDate").val()
	};
	return temp;
};

list.init = function () {

	list.table = $('#' + list.tableId).bootstrapTable({
		url: list.baseUrl + '/query', //请求后台的URL（*）
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
		onLoadError: function (status) {
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

layui.use(['form', 'layedit', 'laydate', 'loading', 'common'], function () {
	list.init();

	var editIndex;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
		layer = layui.layer, //获取当前窗口的layer对象
		form = layui.form(),
		layedit = layui.layedit,
		common = layui.common,
		laydate = layui.laydate,
		loading = layui.loading;
	var addBoxIndex = -1;

	$('.date-picker').change(function () {
		var value = $(this).val();
		$(this).val(dateFormat(value));
	});

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

	$('#btn_query').on('click', function () {
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		if (beginDate && endDate && beginDate > endDate) {
			layerTips.msg("时间筛选开始时间不能大于结束时间");
		} else {
			var queryParams = list.queryParams();
			queryParams.pageNumber = 1;
			list.table.bootstrapTable('refresh', queryParams);
		}
	});

	$("#btn_download").on("click", function () {
		$("#downloadFrame").prop('src', "../attach/stockholder_template.xls");
		return false;
	});

	$('#btn_upload').on('click', function () {

		var index = layer.open({
			type: 1,
			title: '文件导入',
			skin: 'layui-layer-rim', //加上边框
			area: ['300px', '200px'], //宽高
			content: '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> <div style="color: red; display: none" id="importText">导入中...</div> </div>'
		});

		var uploader = WebUploader.create({
			auto: true, // 选完文件后，是否自动上传
			swf: '../js/webuploader/Uploader.swf', // swf文件路径
			server: '../../beneficiary/stockholder/upload', // 文件接收服务端
			timeout: 0,
			pick: '#imgPicker', // 选择文件的按钮。可选
			accept: {
				title: 'Excel',
				extensions: 'xls,xlsx'
			}
		});

		uploader.on("uploadProgress", function (file, percentage) {
			$('#importText').show();
		});
		uploader.on('uploadSuccess', function (file, res) {
			layerTips.msg(res.message);
			if (res.code == "ACK") {
				layer.close(index);
				var queryParams = list.queryParams();
				queryParams.pageNumber = 1;
				list.table.bootstrapTable('refresh', queryParams);
			}
		});
		uploader.on('uploadError', function (file, reason) {
			layerTips.msg('上传失败');
		});
		uploader.on('error', function (handler) {
			if (handler == 'Q_TYPE_DENIED') {
				layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
			} else {
				layerTips.msg(handler);
			}
		});
	});

	$('#list').on('click', '.view', function () {
		var batchNo = $(this).attr('data-id');

		parent.tab.tabAdd({
			title: '控股股东比对详情',
			href: '../ui/beneficiary/stock_holder_detail.html?batchNo=' + batchNo
		});
		return false;
	});

});