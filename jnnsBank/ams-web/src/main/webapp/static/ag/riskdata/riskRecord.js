var apply = {
    baseUrl: "../../riskRecord",
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
        field: 'acctName',
        title: '企业名称'
    }, {
        field: 'bankCode',
        title: '机构号'
    }, {
        field: 'acctType',
        title: '账户性质',
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    },  {
        field: 'billType',
        title: '操作类型',
        formatter: function (value, row, index) {
            return billTypeMap[value]
        }
    }, {
        field: 'createdDate',
        title: '申请时间',
        //获取日期列的值进行转换
        formatter: function (value, row, index) {
            return changeDateFormat(value)
        }
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
        name: $.trim($("#name").val()),
        status:$("#status").val(),
        operator: $.trim($("#operator").val()),
        phone: $.trim($("#phone").val()),
        type: $.trim($("#type").val()),
        branch: $.trim($("#branch").val()),
        applyid: $.trim($("#applyid").val()),
        beginDateApply: $.trim($("#beginDateApply").val()),
        endDateApply: $.trim($("#endDateApply").val()),
        billType: $("#billType").val(),
        channel:$("#channelType").val()
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
        sortable: true, //是否启用排序
        sortOrder: "desc", //排序方式
        singleSelect: true,
        sortName : 'lastUpdateDate',
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

layui.use(['form', 'layedit', 'laydate'], function () {

    var timestamp = Date.parse(new Date());



    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate;

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
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber=1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_view').on('click', function () {
        if (apply.select(layer)) {
            var applyId = apply.currentItem.id;
            var name = apply.currentItem.name;
            $.get('riskRecordDetail.html', null, function (form) {
                $.get("../../riskRecord/queryDetail?id="+applyId, null, function (data) {
                    formLoadforCustomer(data);
                    // form.render();
                });
                layer.open({
                    type: 1,
                    title: '详情',
                    content: form,
                    btn: ['确定'],
                    shade: false,
                    offset: ['100px', '30%'],
                    area: ['800px', '600px'],
                    maxmin: true,
                    yes: function (index) {
                        layer.closeAll();
                    },
                    success: function (layero, index) {
                        console.log("1");
                    }
                });
            })
            // parent.tab.tabAdd({
            //     title: '查看-'+name,
            //     href: 'bank/view.html?applyId='+applyId+'&name='+encodeURI(name),
            // });
        }
    });

});


function formLoadforCustomer(obj, i, str) {
    if ((str != "" && str != null && str != undefined) && (i != "" && i != null && i != undefined)) {
        for (k in obj) {
            if (obj[k] != "" && obj[k] != null && obj[k] != undefined && $.isPlainObject(obj[k]) == false) {
                if ((k.toLowerCase()).indexOf(str) >= 0) {
                    $("input[name=" + k.toLowerCase() + i + "]").val(obj[k]);
                    $("textarea[name=" + k.toLowerCase() + i + "]").val(obj[k]);
                    $("select[name=" + k.toLowerCase() + i + "]").val(obj[k]);
                } else {
                    $("input[name=" + str + k.toLowerCase() + i + "]").val(obj[k]);
                    $("textarea[name=" + str + k.toLowerCase() + i + "]").val(obj[k]);
                    $("select[name=" + str + k.toLowerCase() + i + "]").val(obj[k]);
                }
            }
        }
    } else {
        for (k in obj) {
            if(k == "billType"){
                if(obj[k] == "ACCT_OPEN"){
                    obj[k] = "开户";
                }else if(obj[k] == "ACCT_CHANGE"){
                    obj[k] = "变更";
                }else if(obj[k] == "ACCT_SUSPEND"){
                    obj[k] = "久悬";
                }else if(obj[k] == "ACCT_REVOKE"){
                    obj[k] = "销户";
                }
            }
            if(k == "acctType"){
                if(obj[k] == "jiben"){
                    obj[k] = "基本存款账户";
                }else if(obj[k] == "yiban"){
                    obj[k] = "一般存款账户";
                }else if(obj[k] == "yusuan"){
                    obj[k] = "预算单位专用存款账户";
                }else if(obj[k] == "feiyusuan"){
                    obj[k] = "非预算单位专用存款账户";
                }else if(obj[k] == "linshi"){
                    obj[k] = "临时机构临时存款账户";
                }else if(obj[k] == "feilinshi"){
                    obj[k] = "非临时机构临时存款账户";
                }else if(obj[k] == "teshu"){
                    obj[k] = "特殊单位专用存款账户";
                }else if(obj[k] == "yanzi"){
                    obj[k] = "验资户临时存款账户";
                }else if(obj[k] == "zengzi"){
                    obj[k] = "增资户临时存款账户";
                }else if(obj[k] == "specialAcct"){
                    obj[k] = "专用存款账户";
                }else if(obj[k] == "tempAcct"){
                    obj[k] = "临时存款账户";
                }else if(obj[k] == "unknow"){
                    obj[k] = "未知账户性质";
                }
            }

            if(obj[k] == "true"){
                obj[k] = "是";
            }else if(obj[k] == "false"){
                obj[k] = "否";
            }
            if (obj[k] != "" && obj[k] != null && obj[k] != undefined && $.isPlainObject(obj[k]) == false) {
                $("input[name=" + k + "]").val(obj[k]);
                $("textarea[name=" + k + "]").val(obj[k]);
                $("select[name=" + k + "]").val(obj[k]);
                // $("radio[name=" + k + "][value="+obj[k]+"]").attr("checked", "checked");//根据值设置radio选中
                $("radio[name=" + k + "][value="+obj[k]+"]").click();//根据值设置radio选中
            }
        }
    }

}
