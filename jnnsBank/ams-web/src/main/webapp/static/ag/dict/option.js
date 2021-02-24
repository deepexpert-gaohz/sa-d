var option = {
    baseUrl: "../../dictionary",
    entity: "option",
    tableId: "optionTable",
    toolbarId: "optionToolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};

option.columns = function () {
    return [{
        radio: true
    }, {
        field: 'name',
        title: '字典项名称'
    }, {
        field: 'value',
        title: '字典项值'
    }];
};

option.init = function () {
    option.table = $('#' + option.tableId).bootstrapTable({
        method: 'get', //请求方式（*）
        toolbar: '#' + option.toolbarId, //工具按钮用哪个容器
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
        uniqueId: option.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        selectItemName: 'optionSelectItem',
        columns: option.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return res.data;
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
}

option.select = function (layerTips) {
    var rows = option.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        option.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

option.refresh = function () {
    dictionary.select();
    option.table.bootstrapTable("refresh", {
        silent: true,
        url: option.baseUrl + "/" + dictionary.currentItem.id + "/option"
    });
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    option.init();

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var editIndex;

    var addBoxIndex = -1;

    $('#btn_option_add').on('click', function () {
        if (dictionary.select(layerTips)) {//dictionary字典名称表选中
            if (addBoxIndex !== -1)
                return;
            $.get('option.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '添加字典项',
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
                        var form = layui.form();
                        form.render();
                        form.on('submit(edit)', function (data) {
                            $.ajax({
                                url: option.baseUrl + "/" + dictionary.currentItem.id +"/option/" ,
                                type: 'post',
                                data: data.field,
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('保存成功');
                                    layer.close(addBoxIndex);
                                    option.refresh();
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

    $('#btn_option_edit').on('click', function () {
        if (option.select(layerTips)) {
            var id = option.currentItem.id;
            $.get(option.baseUrl + "/" + dictionary.currentItem.id + '/option/' + id, null, function (data) {
                var result = data.data;
                $.get('option.html', null, function (form) {
                    addBoxIndex = layer.open({
                        type: 1,
                        title: '编辑字典项',
                        content: form,
                        btn: ['保存', '取消'],
                        shade: false,
                        offset: ['20px', '20%'],
                        area: ['600px', '400px'],
                        maxmin: true,
                        yes: function (index) {
                            //触发表单的提交事件
                            layedit.sync(editIndex);
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
                            var form = layui.form();
                            setFromValues(layero, result);
                            form.render();
                            form.on('submit(edit)', function (data) {
                                $.ajax({
                                    url: option.baseUrl + "/" + dictionary.currentItem.id + "/option/" + result.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function () {
                                        layerTips.msg('更新成功');
                                        layer.close(addBoxIndex);
                                        option.refresh();
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
            });
        }
    });

    $('#btn_option_del').on('click', function () {
        if (option.select(layerTips)) {
            var id = option.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: option.baseUrl + "/" + dictionary.currentItem.id + "/option/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            option.refresh();
                        } else {
                            layerTips.msg("移除失败！")
                            option.refresh();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
});