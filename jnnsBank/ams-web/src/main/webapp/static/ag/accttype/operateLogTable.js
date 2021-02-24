layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['common'], function () {
    var common = layui.common;
    var billId = decodeURI(common.getReqParam("billId"));
    var refBillId = decodeURI(common.getReqParam("refBillId"));
    var recId = decodeURI(common.getReqParam("recId"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var accountImageBillsId = refBillId;
    if (accountImageBillsId == undefined || accountImageBillsId == "") {
        accountImageBillsId = billId;
    }
    if (accountImageBillsId == undefined || accountImageBillsId == "") {
        accountImageBillsId = recId;
    }
    var apply = {
        tableId: "operateLogTable",
        unique: "id",
        order: "desc",
        currentItem: {}
    };
    apply.columns = function () {
        return [
            {
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'operateDate',
                title: '操作时间'
            }, {
                field: 'operateType',
                title: '操作类型',
                formatter: function (value, row, index) {
                    return formatOperateType(value)
                }
            }, {
                field: 'failMsg',
                title: '审核失败原因'
            }, {
                field: 'organCode',
                title: '网点机构号'
            }, {
                field: 'operateName',
                title: '操作人'
            }
        ];
    };
    apply.queryParams = function (params) {
        var temp = {};
        if (accountImageBillsId !== undefined && accountImageBillsId !== "" && accountImageBillsId !== null) {
            temp.refBillId = accountImageBillsId;
        }
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };
    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: "../../operatelog/findByRefBillId", //请求后台的URL（*）
            method: 'get', //请求方式（*）
            // toolbar: false, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: apply.queryParams,//传递参数（*）
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
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: apply.columns()
            , onLoadError: function (status) {
                ajaxError(status);
            }
        });
    };
    if (accountImageBillsId !== undefined && accountImageBillsId !== "" && accountImageBillsId !== null && buttonType!='selectForChangeBtn' && (recId==="" || recId==undefined || recId==null)) {
        apply.init();
    }
});