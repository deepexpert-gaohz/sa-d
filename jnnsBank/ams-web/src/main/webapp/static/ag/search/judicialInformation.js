layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../kyc",
    entity: "JudicialInformationDto",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    isFirst: true
};
list.columns = function () {
    return [{
        field: 'pname',
        title: '企业名称'
    }, {
        field: 'sortTime',
        title: '立案时间',
    }, {
        field: 'caseNo',
        title: '案号'
    }, {
        field: 'caseNo',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="' + (value == null ? '' : value) + '">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            companyName: $.trim($("#companyName").val())
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        companyName: $.trim($("#companyName").val())
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/judicialInformation' , //请求后台的URL（*）
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
        pageSize: 50, //每页的记录行数（*）
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


layui.use(['loading', 'laytpl'], function () {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer,
        layer = layui.layer,
        loading = layui.loading,
        laytpl = layui.laytpl;
    
    placeholder($('.placeholder'));


    // 司法信息查询
    $('#btnJudicial').on('click', function () {
        var companyName = $('#companyName').val(); // 企业名称
        if (companyName === '') {
            layerTips.msg("请输入企业名称");
            return;
        }
        if (list.isFirst) {
            list.init();
            list.isFirst = false;
        } else {
            list.table.bootstrapTable('refresh',{pageNumber: 1});
        }
        $.post('../../customerSearch/queryJudicialInformationLog?companyName=' + encodeURI(companyName));
    });

    //工商基本信息日志查询
    $('#btn_log_3').on('click', function () {
        parent.tab.tabAdd({
            title: "司法信息日志查询",
            href: 'search/judicialInformationLog.html'
        });
    });

    $('#list').on('click','.view', function () {

        var caseNo = $(this).attr('data-id');
        if (caseNo && caseNo !== '') {
            $.get('../../kyc/judicialInformatioDetail?caseNo=' + caseNo, function (response) {
                $.get('judicialResult.html', null, function (template) {
                    laytpl(template).render(response.result, function (html) {
                        layer.open({
                            type: 1,
                            area: '800px',
                            title: '司法详情',
                            content: html
                        });
                    });
                });
            });
        } else {
            layer.alert('该条记录没有司法数据', {
                title: "提示信息",
                closeBtn: 0
            });
        }
        return false;
    });
});
