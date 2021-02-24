layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'laydate','upload'], function () {
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        loading = layui.loading,
        upload = layui.upload,
        laydate = layui.laydate;

    var tabId = decodeURI(common.getReqParam("tabId"));
    var code = decodeURI(common.getReqParam("code"));

    var checked =true;
    var role =0;
    var baseUrl;

    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    var remind = {
        baseUrl: "../../allBillsPublic",
        tableId: "accountTable",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };

    remind.columns = function () {
        return [{
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'refBillId',
            visible: false
        }, {
            field: 'acctNo',
            title: '账号'
        }, {
            field: 'depositorName',
            title: '存款人名称',
            'class': 'W200'
        }, {
            field: 'acctType',
            title: '账户性质',
            'class': 'W120',
            formatter: function (value, row, index) {
                return changeAcctType(value)
            }
        }, {
            field: 'accountStatus',
            title: '账户状态',
            formatter: function (value, row, index) {
                return formatAccountStatus(value)
            }
        }, {
            field: 'bankName',
            title: '开户行'
        }, {
            field: 'kernelOrgCode',
            title: '网点机构号'
        }, {
            field: 'acctCreateDate',
            title: '开户日期',
            //获取日期列的值进行转换
            formatter: function (value, row, index) {
                return changeDateStr(value)
            }
        },{
            field: 'cancelDate',
            title: '销户日期'
        }];
    };


    remind.queryParams = function (params) {
        if (!params)
            return {
                "code": code,
                acctNo: $.trim($("#acctNo").val()),
                acctType: $("#acctType").val(),
                depositorName: $.trim($("#name").val())

            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            size: params.limit, //页面大小
            page: params.offset / params.limit, //页码

            "code": code,
            depositorName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
        };
        return temp;
    };

    remind.init = function () {

        remind.table = $('#' + remind.tableId).bootstrapTable({
            url: remind.baseUrl + '/listForBills', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + remind.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: remind.queryParams,//传递参数（*）
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
            uniqueId: remind.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: remind.columns(),
            onLoadError: function(status){
                ajaxError(status);
            }
        });
    };

    $('#btn_query').on('click', function () {
        remind.table.bootstrapTable('refresh', remind.queryParams());
    });

    layui.use(['form', 'layedit', 'laydate'], function () {
        remind.init();
    })

})

