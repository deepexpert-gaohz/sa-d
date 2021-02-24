
layui.config({
     base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../networkVerify/phoneNumber",
    entity: "mobilePhoneNumberLog",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'selectItem',
        radio: true
    },{
        field: 'id',
        title: 'ID编号'
    }, {
        field: 'mobNb',
        title: '手机号码',
        formatter:function (value, row, index) {
            return value.toString().substr(2);//隐藏86
        }
    }, {
        field: 'nm',
        title: '姓名'
    }, {
        field: 'orgName',
        title: '机构名称'
    }, {
        field: 'createdUserName',
        title: '操作员'
    }, {
        field: 'createTime',
        title: '操作时间'
    },{
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            mobNb: $.trim($("#mobNb").val()),
            nm: $.trim($("#nm").val()),
            beginDate: $.trim($("#beginDate").val()),
            endDate: $.trim($("#endDate").val()),
            orgName: $.trim($("#orgName").val()),
            createdUserName: $.trim($("#createdUserName").val())
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        mobNb: $.trim($("#mobNb").val()),
        nm: $.trim($("#nm").val()),
        beginDate: $.trim($("#beginDate").val()),
        endDate: $.trim($("#endDate").val()),
        orgName: $.trim($("#orgName").val()),
        createdUserName: $.trim($("#createdUserName").val())
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/list', //请求后台的URL（*）
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
        responseHandler: function (res) {
            return {total:res.total, rows: res.rows};
        }
        ,onLoadError: function(status){
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

layui.use(['form', 'layedit', 'laydate', 'loading', 'laytpl'], function () {
    list.init();

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        loading = layui.loading;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    //查询
    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            list.table.bootstrapTable('refresh', list.queryParams());
        }
    });

    $('#btn_add').on('click', function () {
        parent.tab.tabAdd({
            title: "手机号码联网核查申请",
            href: 'networkVerify/phoneNumber.html'
        });
    });

    $('#btn_feedback').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;
            parent.tab.tabAdd({
                title: "手机号码疑义反馈申请",
                href: 'networkVerify/feedback.html?id='+id+'&type=phoneNumber'
            });
        }
    });

    $('#btn_feedback_log').on('click', function () {
        parent.tab.tabAdd({
            title: "手机号码疑义反馈记录",
            href: 'networkVerify/feedbackLog.html?type=phoneNumber'
        });
    });

    //查看详情点击事件
    $('#list').on('click','.view', function () {
        var Id = $(this).attr('data-id');
        if (Id && Id !== '') {
            showTableHTML(Id);
        } else {
            layer.alert('该条记录没有手机号码联网核查数据', {
                title: "提示信息",
                closeBtn: 0
            });
        }
        return false;
    });


    //展示详情table页
    function showTableHTML(id) {
        $.get(list.baseUrl+'/' + id, function (res) {
            if(res.code=="NACK"){
                layer.alert('手机号码联网核查详情获取异常', {
                    title: "提示信息",
                    closeBtn: 0
                });
                return;
            }
            var data = res.data;

            html = '<div style="margin: 10px"><table class="table table-bordered word-break"><tbody>';

            //1、请求信息
            html += '<tr><td class="td-label" colspan="4" align="center">请求信息</td></tr>'+
                '<tr>\n' +
                '<td class="td-label">姓名：</td>\n' +
                '<td class="td-label-right">'+(data.nm||'')+'</td>\n' +
                '<td class="td-label">手机号码：</td>\n' +
                '<td class="td-label-right">'+(data.mobNb||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">证件类型：</td>\n' +
                '<td class="td-label-right">'+(idTpSwitch(data.idTp)||'')+'</td>\n' +
                '<td class="td-label">证件号码：</td>\n' +
                '<td class="td-label-right">'+(data.identification||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">统一社会信用代码：</td>\n' +
                '<td class="td-label-right">'+(data.uniSocCdtCd||'')+'</td>\n' +
                '<td class="td-label">工商注册号：</td>\n' +
                '<td class="td-label-right">'+(data.bizRegNb||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">操作员姓名：</td>\n' +
                '<td class="td-label-right">'+(data.opNm||'')+'</td>\n' +
                '</tr>\n' ;

            //2、应答信息
            if (data.rslt!=null) {
                html += '<tr><td class="td-label" align="center" colspan="4">应答信息</td></tr>' +
                    '<tr>\n' +
                    '<td class="td-label">手机号码：</td>\n' +
                    '<td class="td-label-right">'+(data.mobNb.toString().substr(2)||'')+'</td>\n' +
                    '<td class="td-label">手机号码核查结果：</td>\n' +
                    '<td class="td-label-right" id="rslt">'+(rsltSwitch(data.rslt)||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">手机运营商：</td>\n' +
                    '<td class="td-label-right" id="mobCrr">'+(mobCrrSwitch(data.mobCrr)||'')+'</td>\n' +
                    '<td class="td-label">手机号归属地代码：</td>\n' +
                    '<td class="td-label-right" id="locMobNb">'+(data.locMobNb||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">手机号归属地名称：</td>\n' +
                    '<td class="td-label-right" id="locNmMobNb">'+(data.locNmMobNb||'')+'</td>\n' +
                    '<td class="td-label">客户类型：</td>\n' +
                    '<td class="td-label-right" id="cdTp">'+(cdTpSwitch(data.cdTp)||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">手机号码状态：</td>\n' +
                    '<td class="td-label-right" id="sts">'+(stsSwitch(data.sts)||'')+'</td>\n' +
                    '</tr>\n' ;
            }else if (data.procSts!=null){
                html += '<tr><td class="td-label" align="center" colspan="4">应答信息</td></tr>'+
                    '<tr>\n' +
                    '<td class="td-label">申请报文拒绝状态：</td>\n' +
                    '<td class="td-label-right" id="procSts">'+(procStsSwitch(data.procSts)||'')+'</td>\n' +
                    '<td class="td-label">申请报文拒绝码：</td>\n' +
                    '<td class="td-label-right" id="procCd">'+(data.procCd||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">申请报文拒绝信息：</td>\n' +
                    '<td class="td-label-right" colspan="3" id="rjctInf">'+(data.rjctInf||'')+'</td>\n' +
                    '</tr>\n';
            }else {
                html += '<tr><td class="td-label" colspan="4" align="center">应答信息</td></tr>';
                html += '<tr><td class="td-label-right" colspan="4" align="center">无结果</td></tr>';
            }

            html += '</tbody></table></div>';

            layer.open({
                type: 1,
                area: '800px',
                title: '手机联网核查详情',
                content: html
            });
        });
    }
});
