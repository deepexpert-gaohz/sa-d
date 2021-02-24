var user = {
    baseUrl: "../../user",
    groupUrl: "/group",
    orgUrl: "../../organization",
    entity: "user",
    tableId: "userTable",
    toolbarId: "userToolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};
user.columns = function () {
    return [{
        radio: true
    }, {
        field: 'username',
        title: '用户名'
    }, {
        field: 'cname',
        title: '姓名'
    }, {
        field: 'roleName',
        title: '角色'
    }, {
        field: 'enabled',
        title: '是否启用',
        formatter: function (data) {
            return data ? '启用' : '禁用';
        }
    }
    ];
};
user.queryParams = function (params) {
    if (!params)
        return {
            orgId: org.currentItem.id
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit) + 1, //页码
        orgId: org.currentItem.id
    };
    return temp;
};

user.onCheck = function (item) {
    if (item.enabled) {
        $('#btn_user_disable').removeClass('layui-btn-disabled');
        $('#btn_user_disable').attr('disabled', false);
        $('#btn_user_enable').addClass('layui-btn-disabled');
        $('#btn_user_enable').attr('disabled', true);
    } else {
        $('#btn_user_disable').addClass('layui-btn-disabled');
        $('#btn_user_disable').attr('disabled', true);
        $('#btn_user_enable').removeClass('layui-btn-disabled');
        $('#btn_user_enable').attr('disabled', false);
    }
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
        pageNumber: 1, //初始化加载第一页，默认第一页
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
        onCheck: user.onCheck,
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total: res.data.totalRecord, rows: res.data.list};
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

user.refresh = function () {
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

    // form.verify({
    //     pass: [
    //       /^[\S]{6}$/
    //       ,'密码必须6到，且不能出现空格'
    //     ]
    // });

    $.get("../../organization/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++)
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';

            user.orgOptions = options;
        }
    });
    $.get("../../role/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++)
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';

            user.roleOptions = options;
        }
    });

    //初始化页面上面的按钮事件
    $('#btn_user_add').on("click", function () {
        if (org.select(layerTips)) {
            var id = org.currentItem.id;

            if (!id) {
                layerTips.msg('选择一个机构');
                return;
            }

            if (addBoxIndex !== -1)
                return;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('../organizationUser/edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '添加用户',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '500px'],
                    maxmin: true,
                    yes: function (index) {
                        layedit.sync(editIndex);
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
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
                        form.render();

                        layero.find('#roleId').append(user.roleOptions);
                        layero.find('#orgId').append(user.orgOptions);
                        layero.find('#orgId').val(id);
                        form.render('select');

                        form.on('submit(edit)', function (data) {
                            $.ajax({
                                url: user.baseUrl + "/",
                                type: 'post',
                                data: data.field,
                                dataType: "json",
                                success: function (res) {
                                    if (res.code === 'ACK') {
                                        layerTips.msg('保存成功');
                                        layerTips.close(index);
                                        user.refresh();
                                        layer.close(addBoxIndex);
                                        // location.reload();
                                    } else {
                                        layerTips.msg(res.message);
                                    }
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                        //console.log(layero, index);
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
            });

        }
    });

    $('#btn_user_edit').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;

            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('../user/edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改用户',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '500px'],
                    maxmin: true,
                    yes: function (index) {
                        layedit.sync(editIndex);
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
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
                        form.render();

                        layero.find('#pwd').hide();//隐藏密码
                        layero.find('#password').attr('lay-verify', 'pass');
                        layero.find('#roleId').append(user.roleOptions);
                        layero.find('#orgId').append(user.orgOptions);
                        form.render('select');

                        $.get(user.baseUrl + '/' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            layero.find('#password').val('******');
                            layero.find("select[name='orgId']").val(result.orgId);
                            layero.find("select[name='roleId']").val(result.roleId);
                            form.render();
                        });

                        layero.find(":input[name='username']").attr("disabled", "");
                        form.on('submit(edit)', function (data) {

                            if (data.field.password == '******') data.field.password ='';

                            if(user.currentItem.id) {
                                $.ajax({
                                    url: user.baseUrl + "/" + user.currentItem.id,
                                    type: 'post',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (res) {
                                        if (res.code === 'ACK') {
                                            layerTips.msg('更新成功');
                                            layerTips.close(index);
                                            // location.reload();
                                            user.refresh();
                                            layer.close(addBoxIndex);
                                        } else {
                                            layerTips.msg(res.message);
                                        }
                                    }

                                });
                            }
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

    $('#btn_user_del').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定删除该用户吗？', null, function (index) {
                $.ajax({
                    url: user.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            user.refresh();
                        } else {
                            layerTips.msg("移除失败！"+data.message)
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    $('#btn_user_enable').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定启用该用户吗？', null, function (index) {
                $.ajax({
                    url: user.baseUrl + "/" + id + "/enable",
                    type: "PUT",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("启用成功！");
                            user.refresh();
                        } else {
                            layerTips.msg("启用失败！")
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    $('#btn_user_disable').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定禁用该用户吗？', null, function (index) {
                $.ajax({
                    url: user.baseUrl + "/" + id + "/disable",
                    type: "PUT",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("禁用成功！");
                            user.refresh();
                        } else {
                            layerTips.msg("禁用失败！")
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
});