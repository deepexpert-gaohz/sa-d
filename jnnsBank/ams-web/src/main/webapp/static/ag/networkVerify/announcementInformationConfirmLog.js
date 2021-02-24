layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../networkVerify/announcementInformation",
    entity: "announcementInformation",
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
        field: 'msgId',
        title: '报文标识号'
    }, {
        field: 'instgDrctPty',
        title: '原发起直接参与机构'
    }, {
        field: 'instgPty',
        title: '原发起参与机构'
    },{
        field: 'msgCntt',
        title: '附加信息',
        formatter :function (value, row, index) {
            if (value && value.length>20){
                return value.substr(0,20)+"...";
            }
            return value;
        }
    }, {
        field: 'flag',
        title: '发送状态',
        formatter :function (value, row, index) {
            if (value){
                return "正常发送";
            }else {
                return "发送失败";
            }
        }
    }, {
        field: 'orgName',
        title: '机构名称'
    }, {
        field: 'createdUserName',
        title: '操作员'
    }, {
        field: 'createTime',
        title: '操作时间'
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" href="#" data-id="'+value+'">查看详情</a> ';
        }
    }
    ];
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

list.queryParams = function (params) {
    if (!params)
        return {

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码

    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/confirm/list', //请求后台的URL（*）
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
        columns: list.columns()

    });
};



layui.use(['form', 'layedit', 'laydate', 'upload', 'loading'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload,
        loading = layui.loading;

    var addBoxIndex = -1;

    //列表查看详情点击事件
    $('#list').on('click','.view', function () {
        var Id = $(this).attr('data-id');
        showTableHTML(Id);
        return false;
    });

    //展示详情table页
    function showTableHTML(id) {
        $.get(list.baseUrl+'/confirm/' + id, function (res) {
            if(res.code=="NACK"){
                layer.alert('公告信息确认详情获取异常', {
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
                '<td class="td-label">原报文标识号：</td>\n' +
                '<td class="td-label-right" colspan="3">'+(data.msgId||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">原发起直接参与机构：</td>\n' +
                '<td class="td-label-right">'+(data.instgDrctPty||'')+'</td>\n' +
                '<td class="td-label">原发起参与机构：</td>\n' +
                '<td class="td-label-right">'+(data.instgPty||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">附加信息：</td>\n' +
                '<td class="td-label-right" colspan="3" >'+(data.msgCntt||'')+'</td>\n' +
                '</tr>\n' ;

            //2、应答信息
            if (data.procSts!=null) {
                html += '<tr><td class="td-label" align="center" colspan="4">应答信息</td></tr>' +
                    '<tr>\n' +
                    '<td class="td-label">申请报文处理状态：</td>\n' +
                    '<td class="td-label-right" colspan="3">'+(procStsSwitch(data.procSts)||'')+'</td>\n' +
                    '</tr>\n' ;
                if (data.procSts == "PR09"){
                    html += '<tr>\n' +
                        '<td class="td-label">申请报文拒绝码：</td>\n' +
                        '<td class="td-label-right">'+(data.procCd||'')+'</td>\n' +
                        '<td class="td-label">申请报文拒绝信息：</td>\n' +
                        '<td class="td-label-right">'+(data.rjctInf||'')+'</td>\n' +
                        '</tr>\n';
                }
            }else {
                html += '<tr><td class="td-label" colspan="4" align="center">应答信息</td></tr>';
                html += '<tr><td class="td-label-right" colspan="4" align="center">无结果</td></tr>';
            }

            html += '</tbody></table></div>';

            layer.open({
                type: 1,
                area: '800px',
                title: '公告信息确认详情',
                content: html
            });
        });
    }
});