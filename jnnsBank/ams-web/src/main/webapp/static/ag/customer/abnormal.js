layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var customer = {
    baseUrl: "../../customerAbnormal",
    tableId: "abnormalTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "desc",
    currentItem: {}
};

customer.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'depositorName',
        title: '客户名称'
    }, {
        field: 'organName',
        title: '银行机构名称'
    }, {
        field: 'code',
        title: '银行机构代码'
    }, {
        field: 'abnormalTime',
        title: '系统异动时间'
    }, {
        field: 'illegal',
        title: '严重违法',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='illegalView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'changeMess',
        title: '经营异常',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='changeMessView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'businessExpires',
        title: '经营到期',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='businessExpiresView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    },{
        field: 'abnormalState',
        title: '工商状态异常',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='abnormalStateView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    },{
        field: 'changed',
        title: '登记信息异动',
        formatter: function (value, row, index) {
            if (value == null) {
                return '-';
            } else if (value) {
                return "<a href=\"javascript:void(0);\" style='color: blue' onclick='changedView(" +"\"" + row.id + "\"" +")'>是</a>";
            } else {
                return '否';
            }
        }
    }, {
        field: 'processState',
        title: '处理状态',
        formatter: function (value, row, index) {
            if (value == null) {
                return '待处理';
            } else if (value === 'underway') {
                return '处理中';
            } else if (value === 'finish') {
                return '已处理';
            } else {
                return value;
            }
        }
    }, {
        field: 'processTime',
        title: '处理时间'
    }, {
        field: 'processer',
        title: '处理人'
    }, {
        field: 'message',
        title: '短信发送状态',
        formatter: function (value, row, index) {
            if (value=="1") {
                return '发送成功';
            } else if (value=="2") {
                return '发送失败';
            }else {
                return '未发送';
            }
        }
    }, {
        field: 'id',
        title: '操作',
        visible: false,
        formatter: function (value, row, index) {
            return "<a href=\"javascript:void(0);\" style='color: blue' class='abnormalNote' data-id='" + value + "'>短信通知</a>";
        }
    }];
};

customer.queryParams = function (params) {
    var temp = {
        depositorName: $.trim($("#depositorName").val()),
        organName: $.trim($("#organName").val()),
        code: $.trim($("#code").val()),
        beginDate: $.trim($("#beginDate").val()),
        endDate: $.trim($("#endDate").val()),
        illegal: $.trim($("#illegal").val()),
        changeMess: $.trim($("#changeMess").val()),
        businessExpires: $.trim($("#businessExpires").val()),
        abnormalState: $.trim($("#abnormalState").val()),
        changed: $.trim($("#changed").val()),
        processState: $.trim($("#processState").val()),
        processTimeBeginDate: $.trim($("#processTimeBeginDate").val()),
        processTimeEndDate: $.trim($("#processTimeEndDate").val()),
        processer: $.trim($("#processer").val())
    };
    if (params) {
        temp.size = params.limit; //页面大小
        temp.page = params.offset / params.limit; //页码
    }
    return temp;
};

customer.init = function () {

    customer.table = $('#' + customer.tableId).bootstrapTable({
        url: customer.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + customer.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: true, //是否启用排序
        sortOrder: "desc", //排序方式
        sortName: 'lastUpdateDate',
        queryParams: customer.queryParams,//传递参数（*）
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
        uniqueId: customer.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: customer.columns()
        ,onLoadError: function(status){
            ajaxError(status);
        }
        ,onLoadSuccess: function(data){
            //根据权限决定是否显示短信通知操作列
            $.ajax({
                url: '../../permission/element/code?code=customerAbnormal_manager:note',
                type: 'get',
                dataType: "json",
                async: false,
                success: function (res) {
                    if (res === true) {
                        customer.table.bootstrapTable('showColumn','id');
                    }
                }
            });
        }
    });
};

customer.select = function (layerTips) {
    var rows = customer.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        customer.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

var layerTips;

layui.use(['form', 'layedit', 'laydate','common'], function () {

    var layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    layerTips = parent.layer === undefined ? layui.layer : parent.layer; //获取父窗口的layer对象

    document.getElementById("paging_container").width = document.body.offsetWidth
        - 40 + 'px';

    var illegal = common.getReqParam("illegal");
    var changeMess = common.getReqParam("changeMess");
    var businessExpires = common.getReqParam("businessExpires");
    var abnormalState = common.getReqParam("abnormalState");
    var changed = common.getReqParam("changed");
    //设置查询条件初始值
    if (illegal) {
        $('#illegal').val(illegal === '1' ? 'true' : 'false');
    }
    if (changeMess) {
        $('#changeMess').val(changeMess === '1' ? 'true' : 'false');
    }
    if (businessExpires) {
        $('#businessExpires').val(businessExpires === '1' ? 'true' : 'false');
    }
    if (abnormalState) {
        $('#abnormalState').val(abnormalState === '1' ? 'true' : 'false');
    }
    if (changed) {
        $('#changed').val(changed === '1' ? 'true' : 'false');
    }
    form.render();

    customer.init();

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = customer.queryParams();
        queryParams.pageNumber=1;
        customer.table.bootstrapTable('refresh', queryParams);
    });


    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);
    var laydateProcessTimeArr =  beginEndDateInit(laydate, 'processTimeBeginDate', 'processTimeEndDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
        for (var i = 0; i < laydateProcessTimeArr.length; i++) {
            delete laydateProcessTimeArr[i].max;
            delete laydateProcessTimeArr[i].min;
        }
    });

    //根据搜索结果导出
    $('#btn_abnormal_export').on('click',function () {
        var params = '';
        var data = customer.queryParams();
        for (var key in data) {
            params += '&'+ key + '=' + data[key]
        }
        params = params.substr(1);
       location.href = customer.baseUrl+'/export?'+params;
    });

    //异动处理
    $('#btn_abnormal_process').on('click', function () {
        abnormalProcess("underway");
    });
    //异动处理完成
    $('#btn_abnormal_process_finish').on('click', function () {
        abnormalProcess("finish");
    });

    /**
     * 异动处理
     * @param processState 处理后的状态
     */
    function abnormalProcess(processState) {
        var rows = customer.table.bootstrapTable('getSelections');
        if (rows.length === 0) {
            layerTips.msg("请选中至少一行");
        } else {
            var idArr = [];
            for (var i = 0; i < rows.length; i++) {
                idArr.push(rows[i].id);
            }
            $.ajax({
                type: "POST",
                url: "../../customerAbnormal/abnormalProcess",
                dataType: 'json',
                data: {"ids": idArr, "processState": processState},
                success: function (data) {
                    if (data.data) {
                        layer.msg("异动处理成功");
                    } else {
                        layer.msg("异动处理失败");
                    }
                    customer.table.bootstrapTable('refresh');
                },
                error: function (res) {
                }
            });
        }
    }

    $('#btn_sendHistory').on('click', function () {
        parent.tab.tabAdd({
                title: '短信发送历史',
                href: 'customer/csrMessage.html'
            }
        );

    });

    $('#btn_managerHistory').on('click', function () {
        if(customer.select(layer)) {
            var depositorName = customer.currentItem.depositorName;

            parent.tab.tabAdd({
                    title: '异动管理历史',
                    href: 'customer/abnormalHistory.html?depositorName=' + encodeURI(depositorName)
                }
            );
        }

    });

    //短信通知
    $('#' + customer.tableId).on('click', '.abnormalNote', function () {
        var id = $(this).attr('data-id');
        var idArr = [];
        idArr.push(parseInt(id));
        abnormalNote(idArr);
    });

    //批量短信通知
    $('#btn_abnormal_note').on('click', function () {
        var rows = customer.table.bootstrapTable('getSelections');
        if (rows.length === 0) {
            layerTips.msg("请选中至少一行");
        } else {
            var idArr = [];
            for (var i = 0; i < rows.length; i++) {
                idArr.push(rows[i].id);
            }
            abnormalNote(idArr);
        }
    });

    //短信通知
    function abnormalNote(idArr) {
        $.ajax({
            type: "POST",
            url: "../../customerAbnormal/sendMessage",
            dataType: 'json',
            data: {"ids": idArr},
            success: function (res) {
                if (res.code=="ACK"){
                    layer.msg("发送成功");
                }else {
                    layer.msg("发送失败");
                }
                customer.table.bootstrapTable('refresh');
            },
            error: function (res) {
            }
        });
        console.log(idArr);
    }
});

//查看详情，且进入页默认显示“严重违法”Tab页
function illegalView(id) {
    openView(id, 'illegal');
}
//查看详情，且进入页默认显示“经营异常”Tab页
function changeMessView(id) {
    openView(id, 'changeMess');
}
//查看详情，且进入页默认显示“经营到期”Tab页
function businessExpiresView(id) {
    openView(id, 'businessExpires');
}
//查看详情，且进入页默认显示“工商状态异常”Tab页
function abnormalStateView(id) {
    openView(id, 'abnormalState');
}
//查看详情，且进入页默认显示“登记信息异动”Tab页
function changedView(id) {
    openView(id, 'changed');
}

/**
 * 查看详情
 * @param id 当前行的uniqueId
 * @param tabType 默认显示的Tab页标记
 */
function openView(id, tabType) {
    var rowDate = $('#abnormalTable').bootstrapTable('getRowByUniqueId', id);
    // $.get('../../customerPublic/findByName?name=浙江省易得融信软件有限公司', function (data) {
    $.get('../../customerPublic/findByName?name=' + encodeURI(rowDate.depositorName), function (data) {
        if (data) {
            parent.tab.tabAdd({
                title: '异动详情-' + nullToEntity(rowDate.depositorName),
                href: 'customer/abnormalOpen.html?compareResultSaicCheckId=' + nullToEntity(rowDate.id)
                + '&compareTaskId=' + nullToEntity(rowDate.compareTaskId)
                + '&saicInfoId=' + nullToEntity(rowDate.saicInfoId)
                + '&compareResultId=' + nullToEntity(rowDate.compareResultId)
                + '&depositorName=' + encodeURI(data.depositorName) //客户名称
                + '&abnormalTime=' + encodeURI(rowDate.abnormalTime) //异动时间
                + '&createdDate=' + encodeURI(format(data.createdDate)) //创建日期
                + '&organName=' + encodeURI(data.organName) //创建机构
                + '&tabType=' + encodeURI(tabType)
            });
        } else {
            layerTips.msg('没有查询到该客户信息');
        }
    });
}

function nullToEntity(str) {
    return str == null ? "" : str;
}