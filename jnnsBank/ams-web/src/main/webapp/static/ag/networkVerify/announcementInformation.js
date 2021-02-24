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
        field: 'id',
        title: 'ID',
        visible: false
    }, {
        field: 'msgId',
        title: '报文标识号'
    }, {
        field: 'creDtTm',
        title: '报文发送时间'
    }, {
        field: 'instgDrctPty',
        title: '发起直接参与机构',
        visible: false
    }, {
        field: 'instgPty',
        title: '发起参与机构',
        visible: false
    },{
        field: 'rplyFlag',
        title: '回复标识',
        formatter :function (value, row, index) {
            return replyFlagCodeSwitch(value);
        }
    },{
        field: 'msgCntt',
        title: '信息内容',
        formatter :function (value, row, index) {
            if (value && value.length>20){
                return value.substr(0,20)+"...";
            }
            return value;
        }
    }, {
        field: 'msgCntt',
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
            beginDate: $.trim($("#beginDate").val()),
            endDate: $.trim($("#endDate").val()),
            msgId:$.trim($("#msgId").val()),
            rplyFlag:$.trim($("#rplyFlag").val()),
            msgCntt:$.trim($("#msgCntt").val())
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        beginDate: $.trim($("#beginDate").val()),
        endDate: $.trim($("#endDate").val()),
        msgId:$.trim($("#msgId").val()),
        rplyFlag:$.trim($("#rplyFlag").val()),
        msgCntt:$.trim($("#msgCntt").val())
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

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
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

    //公告确认
    $('#btn_confirm').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;
            var rplyFlag = list.currentItem.rplyFlag;
            if (rplyFlag==="NRPL"){
                layerTips.msg("无需回复");
                return;
            }
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('../networkVerify/announcementInformationEdit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '公告信息确认',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '400px'],
                    maxmin: true,
                    yes: function (index) {
                        layedit.sync(editIndex);
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {

                    },
                    success: function (layero, index) {
                        var form = layui.form();

                        $("#msgId1").val(list.currentItem.msgId);
                        $("#instgDrctPty1").val(list.currentItem.instgDrctPty);
                        $("#instgPty1").val(list.currentItem.instgPty);
                        $("#myId").val(list.currentItem.id);

                        form.render();
                        form.on('submit(edit)', function (data) {

                            //部分字段校验
                            if ($("#msgId1").val()==null || $("#msgId1").val()===""){
                                layer.msg('获取报文标识号异常');
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            }else if ($("#instgDrctPty1").val()==null || $("#instgDrctPty1").val()==="") {
                                layer.msg('获取发起直接参与机构异常');
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            }else if ($("#instgPty1").val()==null || $("#instgPty1").val()==="") {
                                layer.msg('获取发起参与机构异常');
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            }else if ($("#myId").val()==null || $("#myId").val()===""){
                                layer.msg('获取公告信息ID异常');
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            }

                            $.ajax({
                                url: list.baseUrl + "/confirm",
                                type: 'post',
                                data: data.field,
                                dataType: "json",
                                success: function (res) {
                                    if (res.code === 'ACK') {
                                        if (res.data.msgCode && res.data.msgCode==="000002") {
                                            layer.msg('发送成功');
                                            layer.close(index);
                                        }else if (res.data.msgCode && res.data.msgCode==="000003") {
                                            layer.msg('发送失败');
                                        }else {
                                            layer.msg('返回结果异常：'+res);
                                        }
                                    } else {
                                        layer.msg(res.message);
                                    }
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
            });
        }
    });

    //公告信息确认
    $('#btn_confirm_log').on('click', function () {
        parent.tab.tabAdd({
            title: "公告信息确认记录",
            href: 'networkVerify/announcementInformationConfirmLog.html'
        });
    });

    //列表查看详情点击事件
    $('#list').on('click','.view', function () {
        var value = $(this).attr('data-id');
        if (value && value !== '') {
            layer.alert(value, {
                title: "信息内容详情",
                closeBtn: 0
            });
        }
        return false;
    });
});