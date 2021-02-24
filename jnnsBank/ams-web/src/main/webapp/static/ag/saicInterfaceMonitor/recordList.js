layui.config({
    base: '../js/'
});
var organId;
layui.use(['form', 'layedit', 'common','laydate', 'laytpl'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer,
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    	laytpl = layui.laytpl;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    organId = decodeURI(common.getReqParam("organId"));

    list.init();
    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh', queryParams);
        }
    });

    $("#btn_download").on("click", function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var checkType = $("#checkType").val();
        var companyName = $("#companyName").val();
        var regNo =$("#regNo").val();
        $("#downloadFrame").prop('src', list.baseUrl + '/download?beginDate=' + beginDate + '&endDate=' + endDate + '&checkType=' + checkType + '&companyName=' + companyName + '&regNo=' + regNo + "&organId=" + organId);
        return false;
    });

    $('#list').on('click','.view', function () {
        var id = $(this).attr('data-id');

        return false;
    });

    //绑定按钮事件
    $('#list').on('click','.khjd-detail', function () {
        var keyword = $(this).attr('saicInfoId');
        if(keyword && keyword != 'null'){
            parent.tab.tabAdd({
                    title: '客户尽调',
                    href: 'kyc/detail.html?saicInfoId='+encodeURI(keyword)+'&history=true'
                }
            );
        }else{
            layerTips.msg('无该客户的尽调详情，请重新查询');
        }
    });

});

var list = {
	baseUrl: "../../saicMonitor",
	entity: "saicMonitorPo",
	tableId: "list",
	toolbarId: "toolbar",
	unique: "id",
	order: "asc",
	currentItem: {}
};
list.columns = function () {
	return [{
		field: 'createTime',
		title: '调用时间'
	}, {
		field: 'organName',
		title: '机构'
	}, {
		field: 'userName',
		title: '调用用户'
	}, {
		field: 'companyName',
		title: '调用企业名称'
	}, {
        field: 'regNo',
        title: '工商注册号'
    }, {
		field: 'checkType',
		title: '调用类型',
		formatter: function (value, row, index) {
			return operateType(value);
		}
	}, {
		field: 'saicInfoId',
		title: '操作',
		formatter: function (value, row, index) {
			// return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看详情</a>';

            return '<a href="javascript:void(0)" saicinfoId="'+value+'"  class="khjd-detail btn btn-outline' +
                ' btn-xs blue"> ' +
                '<i class="fa fa-file-text-o"></i> 客户尽调详情 </a>';
		}
	}];
};

list.queryParams = function (params) {
	if (!params)
		return {
			beginDate: $("#beginDate").val(),
			endDate: $("#endDate").val(),
			companyName: $("#companyName").val(),
            regNo: $("#regNo").val(),
            checkType: $("#checkType").val(),
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		limit: params.limit, //页面大小
		offset: (params.offset / params.limit)+1, //页码
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        companyName: $("#companyName").val(),
        regNo: $("#regNo").val(),
        checkType: $("#checkType").val(),
	};
	if (params) {
        temp.size = params.limit; //页面大小
        temp.page = params.offset / params.limit; //页码
    }
	return temp;
};

list.init = function () {

	list.table = $('#' + list.tableId).bootstrapTable({
		url: list.baseUrl + '/page?organId=' + organId, //请求后台的URL（*）
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

function operateType(value) {
	var map = {
		'SAIC': '工商调用',
		'KYC': '客户尽调',
		'ANNUAL': '年检尽调',
		'ILLEGAL' : '批量违法',
	}
	return map[value] || '';
}
