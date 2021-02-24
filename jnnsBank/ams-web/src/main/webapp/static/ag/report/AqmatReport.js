var report = {
    baseUrl: "../../report",
    entity: "model",
    tableId: "reportTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions:''
};
report.columns = function () {
    return [

        [
            {
                "title": "企业银行账户数量监测分析表",
                align:"center",
                colspan: 31
            }
        ],[
            {
                "title":"填制单位:",
                colspan: 29
            },
            {
                "title":"单位/户",
                colspan: 2
            }
        ],[
            {
                field:'depositorType',
                title:"存款人类别",
                rowspan: 3,
                align:"center"

            },
            {
                "title":"基本存款账户",
                "align":"center",
                colspan:6,
                width: "18%"
            },

            {
                "title":"一般存款账户",
                "align":"center",
                colspan:6,
                width: "18%"
            },

            {
                "title":"专用存款账户",
                "align":"center",
                colspan:6,
                width: "18%"
            },
            {
                "title":"临时存款账户",
                "align":"center",
                colspan:6,
                width: "18%"
            },
            {
                "title":"合计",
                "align":"center",
                colspan:6,
                width: "18%"
            }
        ],
        [
            {
                title: "本月账户开立情况",
                align:"center",
                colspan: 3
            },
            {
                title: "存量账户情况",
                align:"center",
                colspan: 3
            },
            {
                title: "本月账户开立情况",
                align:"center",
                colspan: 3
            },
            {
                title: "存量账户情况",
                align:"center",
                colspan: 3
            },
            {
                title: "本月账户开立情况",
                align:"center",
                colspan: 3
            },
            {
                title: "存量账户情况",
                align:"center",
                colspan: 3
            },
            {
                title: "本月账户开立情况",
                align:"center",
                colspan: 3
            },
            {
                title: "存量账户情况",
                align:"center",
                colspan: 3
            },
            {
                title: "本月账户开立情况",
                align:"center",
                colspan: 3
            },
            {
                title: "存量账户情况",
                align:"center",
                colspan: 3
            }
        ],
        [
            {
                field:'baseAcct',
                title: "开户数",
                align:"center"
            },
            {
                field:'baseYToY',
            title: "同比变化率",
            align:"center"
           },
            {
                field:'baseMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'baseStorageCount',
                title: "存量数",
                align:"center"
            },
            {
                field:'baseStorageYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'baseStorageMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'generalAcct',
                title: "开户数",
                align:"center"
            },
            {
                field:'generalYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'generalMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'generalStorageCount',
                title: "存量数",
                align:"center"
            },
            {
                field:'generalStorageYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'generalStorageMToM',
                title: "环比变化率",
                align:"center"
            },{
            field:'specialAcct',
            title: "开户数",
            align:"center"
        },
            {
                field:'specialYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'specialMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'specialStorageCount',
                title: "存量数",
                align:"center"
            },
            {
                field:'specialStorageYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'specialStorageMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'provisionalAcct',
                title: "开户数",
                align:"center"
            },
            {
                field:'provisionalYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'provisionalMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'provisionalStorageCount',
                title: "存量数",
                align:"center"
            },
            {
                field:'provisionalStorageYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'provisionalStorageMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'totalAcct',
                title: "开户数",
                align:"center"
            },
            {
                field:'totalYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'totalMToM',
                title: "环比变化率",
                align:"center"
            },
            {
                field:'totalStorageCount',
                title: "存量数",
                align:"center"
            },
            {
                field:'totalStorageYToY',
                title: "同比变化率",
                align:"center"
            },
            {
                field:'totalStorageMToM',
                title: "环比变化率",
                align:"center"
            },
        ]
    ];
};

report.queryParams = function (params) {
    if (!params)
        return {
            currentDateStr: $('#currentDate').val()
            // modelId: $("#modelId").val(),
            // name : $("#name").val(),
            // typeId: $("#typeId").val(),
            // ruleId: $("#ruleId").val(),
            // levelId: $("#levelId").val(),
            // deptIdl: $("#deptId").val(),
            // status: $("#status").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        currentDateStr: $('#currentDate').val()
        // modelId: $("#modelId").val(),
        // name : $("#name").val(),
        // typeId: $("#typeId").val(),
        // ruleId: $("#ruleId").val(),
        // levelId: $("#levelId").val(),
        // deptId: $("#deptId").val(),
        // status: $("#status").val()
    };
    return temp;
};
report.init = function () {
    report.table = $('#' + report.tableId).bootstrapTable({
        url:report.baseUrl+"/createReportData", //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + report.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: report.order, //排序方式
        queryParams: report.queryParams,//传递参数（*）
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
        uniqueId: report.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        fixedColumns: false,  //ie下不支持冻结列的属性
        columns: report.columns(),
        responseHandler: function (res) {
            console.log(res);
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
report.select = function (layerTips) {
    var rows = report.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        report.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};



layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    report.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    laydate.render({
        elem: '#currentDate',
        type: 'month'
    });

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = report.queryParams();
        queryParams.pageNumber=1;
        report.table.bootstrapTable('refresh', queryParams);
    });

    /*导出报表*/
    $("#exportBtn").click(function () {
        $("#downloadFrame").prop('src', report.baseUrl + "/exportXLS?currentDateStr=" + $('#currentDate').val());
    })

});