var user = {
    baseUrl: "../../user",
    groupUrl: "/group",
    orgUrl: "../../organization",
    entity: "user",
    tableId: "userTable",
    toolbarId: "userToolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
user.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'username',
        title: '用户名'
    }, {
        field: 'cname',
        title: '姓名'
    }
    ];
};
user.queryParams = function (params) {
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

user.init = function () {

    user.table = $('#' + user.tableId).bootstrapTable({
        url: user.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + user.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: user.order, //排序方式
        queryParams: user.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 0, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: user.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: user.columns(),
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
user.select = function (layerTips) {
    var rows = user.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        user.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

user.refresh = function(){
    org.select();
    user.table.bootstrapTable("refresh");
}
layui.use(['form', 'layedit', 'laydate'], function () {
    user.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;
    var addBoxIndex = -1;
    //初始化页面上面的按钮事件
    $('#btn_user_add').on("click", function () {
        if (org.select(layerTips)) {
            var id = org.currentItem.id;
            $.get('user.html', null, function (form) {
                var index = layer.open({
                    type: 1,
                    title: '添加用户',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '400px'],
                    maxmin: true,
                    yes: function (index) {
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
                            debugger;
                            var $this = $(this);
                            elem.width($this.width()).height($this.height()).css({
                                top: 0,
                                left: 0
                            });
                            elem.children('div.layui-layer-content').height($this.height() - 95);
                        });
                    },
                    success: function (layero, index) {
                        var form = layui.form;
                        // 获取人员
                        $.get(org.baseUrl + '/' + id + "/user", null, function (data) {
                            if (data.code !== 'ACK') {
                                layerTips.msg('获取数据异常！');
                                return;
                            }
                            var members = data.data;
                            var memOpts = "";
                            for (var i = 0; i < members.length; i++) {
                                memOpts += '<option  value="' + members[i].id + '" selected>' + members[i].cname + '</option>';
                            }
                            // 加载人员
                            layero.find("#orgMember").append(memOpts).trigger('change');
                        });

                        form.on('submit(edit)', function (data) {
                            var vals = {};
                            var mems = layero.find("#orgMember").val();
                            if (mems)
                                vals.members = mems.join();
                            $.ajax({
                                url: org.baseUrl + '/' + id + "/user",
                                type: 'put',
                                data: vals,
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('更新成功');
                                    layer.close(index);
                                    location.reload();
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                    }
                });
            });
        }
    });
    $('#btn_user_del').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定从机构中移除该用户吗？', null, function (index) {
                $.ajax({
                    url: user.orgUrl + "/" + org.currentItem.id + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            location.reload();
                        } else {
                            layerTips.msg("移除失败！")
                            location.reload();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
});