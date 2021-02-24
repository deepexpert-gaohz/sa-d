layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var apply = {
    baseUrl: "../../apply/bank",
    entity: "user",
    tableId: "userTable",
    toolbarId: "toolbar",
    unique: "applyid",
    order: "desc",
    currentItem: {}
};
apply.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'name',
        title: '企业名称'
    }, {
        field: 'branch',
        title: '预约开户支行'
    }, {
        field: 'type',
        title: '预约开户性质',
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    }, {
        field: 'applyid',
        title: '预约编号'
    }, {
        field: 'billType',
        title: '预约操作类型',
        formatter: function (value, row, index) {
            return getBillTypeName(value)
        }
    }, {
        field: 'statusName',
        title: '预约状态'
    }, {
        field: 'createdDate',
        title: '申请时间',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateFormat(value)
        }
    }, {
        field: 'operator',
        title: '预约人员'
    }];
};
apply.queryParams = function (params) {
    if (!params)
        return {
            name: $("#name").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        status : 'UnComplete',
        name: $.trim($("#name").val()),
        branch: $.trim($("#branch").val()),
        type: $.trim($("#type").val()),
        applyid: $.trim($("#applyid").val()),
        beginDateApply: $.trim($("#beginDateApply").val()),
        endDateApply: $.trim($("#endDateApply").val()),
        operator: $.trim($("#operator").val()),
        billType: $("#billType").val()
    };
    return temp;
};

apply.init = function () {

    apply.table = $('#' + apply.tableId).bootstrapTable({
        url: apply.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: apply.order, //排序方式
        singleSelect: true,
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
        // ,
        // onDblClickRow:function (items,$element) {
        //     var applyId = items.applyid;
        //     var name = items.name;
        //     parent.tab.tabAdd({
        //         title: '开户-'+name,
        //         href: 'bank/view.html?applyId='+applyId+'&name='+encodeURI(name)
        //     });
        // }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
apply.select = function (layerTips) {
    var rows = apply.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        apply.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','layer','common'], function () {
    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDateApply', 'endDateApply', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    var tabId = decodeURI(common.getReqParam("tabId"));
    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber=1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
     if (apply.select(layer)) {
        var applyId = apply.currentItem.applyid;
        var name = apply.currentItem.name;
        var tabId = parent.tab.getCurrentTabId();

        parent.tab.tabAdd({
               title: '接洽-'+name,
               href: 'bank/edit.html?applyId='+applyId+'&name='+encodeURI(name)+'&tabId=' + tabId
         });

        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
      }
    });
      $('#btn_khjd').on('click', function () {
             if (apply.select(layer)) {
                  var applyId = apply.currentItem.applyid;
                  var name = apply.currentItem.name;
                    parent.tab.tabAdd({
                            title: '客户尽调',
                            href: 'kyc/detail.html?name='+encodeURI(name)+'&applyId='+applyId+'&history=false'
                        }
                    );
              }
       });


    $('#btn_sync').on('click', function () {
        $.post(apply.baseUrl+'/all/sync', function(result){
            if(result.rel){
                layerTips.msg("全数据同步成功");
                location.reload();
            }else{
                layerTips.msg("全数据同步失败："+result.msg);
            }
        }).error(function (e) {
            layerTips.msg("全数据同步异常，请稍后再试");
        });
    });

});