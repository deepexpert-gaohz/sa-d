var pbc = {
    baseUrl: "../../pbc/account",
    entity: "pbcAccount",
    tableId: 'pbcUserTable',
    toolbarId: "pbc_toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    code: "id"
};
pbc.select =  function (layerTips) {
    var rows = pbc.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        pbc.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

pbc.columns = function () {
 return [{
     radio: true
 }, {field: 'accountType', title: "系统", formatter: function (value) {
         return pbc.accountTypeMap[value];
     }} , {field: 'ip', title: "IP"}, {
     field: 'account',
     title: '用户名'
 }, {field: 'password', title: '密码', formatter: function (value) {
         return "******";
     }},
     {
         field: 'enabled', title: '账号状态', formatter: function (value) {
             return value? "启用":"禁用";
         }
     },
     {
         field: 'accountStatus', title: '登录状态', formatter: function (value, row) {
             if(value === 'INVALID'){

                 return pbc.accountStatusMap[value] + (row.errorReason?":"+row.errorReason:"");
             } else {
                 return pbc.accountStatusMap[value];
             }
         }
     }];
}

pbc.accountTypeMap = {
    AMS:"人行账管系统",
    PICP:"身份证联网核查",
    ECCS:"机构信用代码证"
};

pbc.accountStatusMap = {
    NEW: "未校验",
    VALID: "成功",
    INVALID: "失败"
};

pbc.resetForm = function (layero) {
    layero.find("input[name='ip']").val("");
    layero.find("input[name='account']").val("");
    layero.find("input[name='password']").val("");
    layero.find("input[name='id']").val("");
    layero.find("input[name='enabled']").val("true");
    layero.find("#btn_updateECCS").hide();
    layero.find("#accountStatus").val("NEW");
    layero.find("#loginStatus").text("未校验");
    layero.find("#btn_update").hide();
    layero.find("#btn_create").show();
};


layui.use(['form', 'layedit', 'laydate', 'picker', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;

    var pbcBoxIndex = -1;

    $('#btn_pbc').on('click', function () {
        if (pbcBoxIndex !== -1)
            return;
        if (!org.currentItem.id) {
            layerTips.msg("请选中一行");
            return false;
        }

        $.get('pbc.html', null, function (form) {
            pbcBoxIndex = layer.open({
                type: 1,
                title: '配置人行账号',
                content: form,
                shade: false,
                // offset: ['100px'],
                // area: ['1100px', '550px'],
                offset: 't',
                area: ['100%','100%'],
                // maxmin: true,
                yes: function (index) {
                    layer.close(index);
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
                end: function () {
                    pbcBoxIndex = -1;
                },
                success: function (layero, index) {
                    var form = layui.form;

                    layero.find("#orgId").val(org.currentItem.id);
                    layero.find("#name").val(org.currentItem.name);
                    form.render();

                    form.on('submit(create)', function (data) {
                        /*form.verify({
                            ip: [
                                /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/,
                                "请输入正确的IP地址"
                            ]
                        });*/
                        if(layero.find("#accountStatus").val() === 'NEW'){
                            layerTips.msg('保存前请先校验！');
                            return false;
                        }
                        $.ajax({
                            url: pbc.baseUrl + '/',
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (data) {
                                if (data.code === 'ACK') {
                                    layerTips.msg('保存成功');
                                    pbc.resetForm(layero);
                                    layero.find("#formArea").hide();
                                    $('#pbcUserTable').bootstrapTable('refresh');
                                } else {
                                    layerTips.msg(data.message);
                                }
                            }

                        });
                        //这里可以写ajax方法提交表单
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });

                    form.on('submit(update)', function (data) {
                        /*form.verify({
                            ip: [
                                /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
                                "请输入正确的IP地址"
                            ]
                        });*/
                        if(layero.find("#accountStatus").val() === 'NEW'){
                            layerTips.msg('保存前请先校验！');
                            return false;
                        }
                        $.ajax({
                            url: pbc.baseUrl + '/' + pbc.currentItem.id,
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (data) {
                                if (data.code === 'ACK') {
                                    layerTips.msg('保存成功');
                                    pbc.resetForm(layero);
                                    layero.find("#formArea").hide();
                                    $('#pbcUserTable').bootstrapTable('refresh');
                                } else {
                                    layerTips.msg(data.message);
                                }
                            }

                        });
                        //这里可以写ajax方法提交表单
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });

                    form.on('submit(validate)', function (data) {
                        $.ajax({
                            url: pbc.baseUrl + '/validate',
                            type: 'PUT',
                            data: data.field,
                            dataType: "json",
                            success: function (data) {
                                if (data.code === 'ACK') {
                                    layerTips.msg('登录成功');
                                    layero.find("#accountStatus").val("VALID");
                                    layero.find("#loginStatus").text("成功");
                                    layero.find("#errorReason").val("");
                                } else {
                                    layero.find("#accountStatus").val("INVALID");
                                    layero.find("#loginStatus").text(data.message);
                                    layero.find("#errorReason").val(data.message);
                                    layerTips.msg(data.message);
                                }
                            }

                        });
                        //这里可以写ajax方法提交表单
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });

                    layero.find("#btn_add").on("click", function () {
                        pbc.resetForm(layero);
                        layero.find("#formArea").show();
                        return false;
                    });
                    layero.find("#btn_reset").on("click", function () {
                        if (layero.find("input[name='id']").val() !== "") {

                            layero.find("input[name='ip']").val(pbc.currentItem.ip);
                            layero.find("input[name='account']").val(pbc.currentItem.account);
                            layero.find("#accountType").val(pbc.currentItem.accountType);
                            layero.find("input[name='password']").val(pbc.currentItem.password);
                            layero.find("input[name='id']").val(pbc.currentItem.id);
                            layero.find("input[name='enabled']").val(pbc.currentItem.enabled);
                            layero.find("input[name='accountStatus']").val(pbc.currentItem.accountStatus);
                            if (pbc.currentItem.accountType === 'ECCS') {
                                layero.find("#btn_updateECCS").show();
                            } else {
                                layero.find("#btn_updateECCS").hide();
                            }
                            layero.find("#btn_update").show();
                            layero.find("#btn_create").hide();
                            form.render();
                        } else {
                            pbc.resetForm(layero);
                        }
                        return false;
                    });
                    layero.find("#btn_edit").on("click", function () {
                        if (pbc.select(layerTips)) {

                            layero.find("input[name='ip']").val(pbc.currentItem.ip);
                            layero.find("input[name='account']").val(pbc.currentItem.account);
                            layero.find("#accountType").val(pbc.currentItem.accountType);
                            layero.find("input[name='password']").val(pbc.currentItem.password);
                            layero.find("input[name='id']").val(pbc.currentItem.id);
                            layero.find("input[name='enabled']").val(pbc.currentItem.enabled);
                            layero.find("#loginStatus").text(pbc.accountStatusMap[pbc.currentItem.accountStatus]);
                            if (pbc.currentItem.accountType === 'ECCS') {
                                layero.find("#btn_updateECCS").show();
                            } else {
                                layero.find("#btn_updateECCS").hide();
                            }

                            layero.find("#btn_update").show();
                            layero.find("#btn_create").hide();
                            form.render();
                            layero.find("#formArea").show();
                        }
                        return false;
                    });
                    layero.find("#btn_del").on("click", function () {
                        if (pbc.select(layerTips)) {
                            var id = pbc.currentItem.id;
                            layer.confirm('确定删除数据吗？', null, function (index) {
                                $.ajax({
                                    url: pbc.baseUrl + "/" + id,
                                    type: "DELETE",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg("移除成功！");
                                            pbc.resetForm(layero);

                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        } else {
                                            layerTips.msg("移除失败！")
                                            pbc.resetForm(layero);

                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        }
                                    }
                                });
                                layer.close(index);
                            });
                        }
                        return false;
                    });

                    layero.find("#btn_enable").on("click", function () {
                        if (pbc.select(layerTips)) {
                            var id = pbc.currentItem.id;
                            layer.confirm('确定启用吗？', null, function (index) {
                                $.ajax({
                                    url: pbc.baseUrl + "/" + id+"/enable",
                                    type: "PUT",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg("启用成功！");

                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        } else {
                                            layerTips.msg("启用失败！")
                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        }
                                        pbc.resetForm(layero);
                                    }
                                });
                                layer.close(index);
                            });
                        }
                        return false;
                    });

                    layero.find("#btn_updateECCS").on("click", function () {
                        var password = layero.find("input[name='password']").val();
                        var item = $.extend({}, pbc.currentItem, {password:password});
                        layer.confirm('确定修改吗？', null, function (index) {
                            $.ajax({
                                url: pbc.baseUrl + "/change",
                                type: "PUT",
                                data: item,
                                dataType: "json",
                                success: function (data) {
                                    if (data.code === 'ACK') {
                                        layerTips.msg("修改成功！");
                                        $('#pbcUserTable').bootstrapTable('refresh');
                                    } else {
                                        layerTips.msg("修改失败！")
                                        $('#pbcUserTable').bootstrapTable('refresh');
                                    }
                                    pbc.resetForm(layero);
                                }
                            });
                            layer.close(index);
                        });
                        return false;
                    });

                    layero.find("#btn_disable").on("click", function () {
                        if (pbc.select(layerTips)) {
                            var id = pbc.currentItem.id;
                            layer.confirm('确定禁用吗？', null, function (index) {
                                $.ajax({
                                    url: pbc.baseUrl + "/" + id + "/disable",
                                    type: "PUT",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg("禁用成功！");
                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        } else {
                                            layerTips.msg("禁用失败！")
                                            $('#pbcUserTable').bootstrapTable('refresh');
                                        }
                                        pbc.resetForm(layero);
                                    }
                                });
                                layer.close(index);
                            });
                        }
                        return false;
                    });

                    pbc.table = $('#' + pbc.tableId).bootstrapTable({
                        method: 'get', //请求方式（*）
                        striped: true, //是否显示行间隔色
                        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                        pagination: false, //是否显示分页（*）
                        sortable: false, //是否启用排序
                        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                        strictSearch: false,
                        showColumns: false, //是否显示所有的列
                        showRefresh: true, //是否显示刷新按钮
                        minimumCountColumns: 2, //最少允许的列数
                        clickToSelect: true, //是否启用点击选中行
                        showToggle: true, //是否显示详细视图和列表视图的切换按钮
                        cardView: false, //是否显示详细视图
                        detailView: false, //是否显示父子表
                        toolbar: '#' + pbc.toolbarId,
                        url: pbc.baseUrl + '/',
                        uniqueId: pbc.unique,
                        responseHandler: function (res) {
                            if (res.code === 'ACK') {
                                return res.data;
                            } else {
                                layerTips.msg('查询失败');
                                return false;
                            }
                        },
                        queryParams: function () {
                            return {orgId: org.currentItem.id};
                        },
                        columns: pbc.columns()
                        ,onLoadError: function(status){
                            ajaxError(status);
                        }
                    });
                }
            });
        });

    });
});