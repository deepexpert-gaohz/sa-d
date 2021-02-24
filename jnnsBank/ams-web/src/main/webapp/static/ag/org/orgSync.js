
var orgSync = {
    baseUrl: "../../organization/orgSyncRecord",
    // groupUrl: "/group",
    // orgUrl: "../../organization",
    // entity: "orgSync",
    tableId: "orgSyncRecordTable",
    // toolbarId: "orgSyncToolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
orgSync.columns = function () {
    return [{
        field: 'name',
        title: '机构名称'
    }, {
        field: 'code',
        title: '机构代码'
    }, {
        field: 'pbcCode',
        title: '人行机构代码'
    }, {
        field: 'createdDate',
        title: '操作时间',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateFormat(value)
        }
    }, {
        field: 'syncType',
        title: '操作类型',
        formatter: function (value, row, index) {
            if(value == 'INSERT'){
                return "新建";
            }else if(value == 'UPDATE'){
                return "修改";
            }else if(value == 'DELETE'){
                return "删除";
            }else{
                return "";
            }
        }
    }, {
        field: 'syncFinishStatus',
        title: '同步完成',
        formatter: function (value, row, index) {
            if(value){
                return "完成";
            }else if(!value){
                return "未完成";
            }else{
                return "";
            }
        }
    }, {
        field: 'syncSuccessStatus',
        title: '同步成功',
        formatter: function (value, row, index) {
            if(value){
                return "成功";
            }else if(!value && row.syncFinishStatus){
                return "失败";
            }else{
                return "";
            }
        }
    }, {
        field: 'errorMsg',
        title: '同步异常详情',
        formatter: function (value, row, index) {
            if(row.syncFinishStatus && !row.syncSuccessStatus){
                return value;
            }else{
                return "";
            }
        }
    }, {
        field: 'operation',
        title: '操作',
        formatter: function (value, row, index) {
            if(row.syncFinishStatus && !row.syncSuccessStatus){
                return '<button class="layui-btn layui-btn-small" id="btn_add" onclick="recall('+row.id+')">重发</button>';
            }else{
                return "";
            }
        }
    }
    ];
};

recall = function(id){
    layer.confirm('确定重发该同步记录吗？', null, function (index) {
        $.ajax({
            url: orgSync.baseUrl + "/recall/" + id,
            type: 'GET',
            success: function (data) {
                if (data.code === 'ACK') {
                    layer.msg("同步记录成功！");
                } else {
                    layer.msg("同步记录失败！"+data.message)
                }
            }
        });
        layer.close(index);
    });
}

orgSync.queryParams = function (params) {
    if (!params)
        return {
            orgId:org.currentItem.id
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        orgId:org.currentItem.id
    };
    return temp;
};

orgSync.init = function () {

    orgSync.table = $('#' + orgSync.tableId).bootstrapTable({
        url: orgSync.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        // toolbar: '#' + orgSync.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: orgSync.order, //排序方式
        queryParams: orgSync.queryParams,//传递参数（*）
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
        uniqueId: orgSync.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: orgSync.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};