var model = {
    baseUrl: "../../model",
    entity: "model",
    tableId: "modelTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions: '',
    relyTableOptions: '',
    pageNumber: ''
};
model.columns = function () {
    return [{
        radio: true
    }, {
        field: 'modelId',
        title: '模型编号'
    }, {
        field: 'name',
        title: '模型名称'
    }, {
        field: 'mdesc',
        title: '模型描述'
    }, {
        field: 'typeName',
        title: '风险类型'
    }, {
        field: 'ruleName',
        title: '风险规则',
    }, {
        field: 'levelName',
        title: '风险等级',
    }, {
        field: 'typeId',
        visible: false,
        title: '风险类型ID',
    }, {
        field: 'status',
        title: '当前状态',
        formatter: function (status) {
            return status == 1 ? "<a style='color: #4CAF50'>启用中</a>" : "<a style='color: red'>停用中</a>";
        }
    }];
};

model.queryParams = function (params) {
    if (!params)
        return {
            modelId: $("#modelId").val(),
            name: $("#name").val(),
            typeId: $("#typeId").val(),
            ruleId: $("#ruleId").val(),
            levelId: $("#levelId").val(),
            deptId: $("#deptId").val(),
            status: $("#status").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit) + 1, //页码
        modelId: $("#modelId").val(),
        name: $("#name").val(),
        typeId: $("#typeId").val(),
        ruleId: $("#ruleId").val(),
        levelId: $("#levelId").val(),
        deptId: $("#deptId").val(),
        status: $("#status").val()
    };
    return temp;
};
model.init = function () {
    model.table = $('#' + model.tableId).bootstrapTable({
        url: model.baseUrl + '/getModels', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + model.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: model.order, //排序方式
        queryParams: model.queryParams,//传递参数（*）
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
        uniqueId: model.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: model.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                model.pageNumber = res.data.offset
                return {total: res.data.totalRecord, rows: res.data.list};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        , onLoadError: function (status) {
            ajaxError(status);
        }
    });
};
model.select = function (layerTips) {
    var rows = model.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        model.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    model.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    $.get("../../modelKind/findLevelAll", function (res) {
        if (res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].levelName + '</option>';
                $('#levelId').append('<option value="' + res.data[i].id + '" >' + res.data[i].levelName + '</option>');
            }

            form.render();
            model.levelIdOptions = options;
        }
    });

    $.get("../../modelKind/findTypeAll", function (res) {
        if (res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].typeName + '</option>';
                $('#typeId').append('<option value="' + res.data[i].id + '" >' + res.data[i].typeName + '</option>');
            }
            form.render();
            model.typeIdOptions = options;
        }
    });

    $.get('../../tableInfo/searchTableInfo', null, function (data) {
        var list = data.data.list
        var options = '';
        for (var i = 0; i < list.length; i++) {
            options += '<option value="' + list[i].ename + '" >' + list[i].ename + '</option>';
        }
        model.relyTableOptions = options;
    });

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = model.queryParams();
        queryParams.pageNumber = 1;
        model.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加模型',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['20px', '20%'],
                area: ['60%', '80%'],
                maxmin: true,
                yes: function (index) {
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
                    layero.find('#levelId').append(model.levelIdOptions);
                    layero.find('#typeId').append(model.typeIdOptions);
                    layero.find('#ruleId').append(model.ruleIdOptions);
                    form.render();

                    form.on('submit(edit)', function (data) {
                        $.ajax({
                            url: model.baseUrl + "/saveModel",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (res) {
                                if (res.code === 'ACK') {
                                    layerTips.msg('保存成功');
                                    layerTips.close(index);
                                    //取消网页全部刷新，仅刷新表格
                                    layer.close(addBoxIndex);
                                    model.table.bootstrapTable('refresh', model.pageNumber);
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
    });
    $('#btn_edit').on('click', function () {
        if (model.select(layerTips)) {
            var id = model.currentItem.id;
            //alert(id);
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改模型',
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
                        var form = layui.form();

                        layero.find('#levelId').append(model.levelIdOptions);
                        layero.find('#typeId').append(model.typeIdOptions);
                        layero.find('#ruleId').append(model.ruleIdOptions);
                        form.render();

                        $.get(model.baseUrl + '/update/' + id, null, function (data) {

                            var result = data.data;
                            setFromValues(layero, result);
                            layero.find("select[name='modelId']").val(result.modelId);
                            layero.find("select[name='name']").val(result.name);
                            layero.find("select[name='typeId']").val(result.typeId);
                            layero.find("select[name='ruleId']").val(result.ruleId);
                            layero.find("select[name='levelId']").val(result.levelId);
                            layero.find("select[name='status']").val(result.status);
                            layero.find("select[name='mdesc']").val(result.mdesc);
                            layero.find("select[name='tableName']").val(result.tableName);
                            form.render();
                        });

                        form.on('submit(edit)', function (data) {
                            if (model.currentItem.id) {
                                $.ajax({
                                    url: model.baseUrl + "/" + model.currentItem.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (res) {
                                        if (res.code === 'ACK') {
                                            layerTips.msg('更新成功');
                                            layerTips.close(index);
                                            layer.close(addBoxIndex);
                                            model.table.bootstrapTable('refresh', model.pageNumber);
                                        } else {
                                            layerTips.msg(res.message);
                                        }
                                    }
                                });
                            }
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
    $('#btn_del').on('click', function () {
        if (model.select(layerTips)) {
            var id = model.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: model.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            layerTips.close(index);
                            layer.close(addBoxIndex);
                            model.table.bootstrapTable('refresh', model.pageNumber);
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

    $("#btn_rely").on('click', null, function () {
        if (model.select(layerTips)) {
            var modelId = model.currentItem.modelId;
            $.get('modelRely.html', null, function (form) {
                //console.log(form);
                addBoxIndex = layer.open({
                    type: 1,
                    title: '模型依赖列表',
                    content: form,
                    shade: false,
                    area: ['100%', '100%'],
                    async: false,
                    success: function (layero, index) {
                        modelRely.init(modelId);
                        var form = layui.form();
                        layero.find('#mTable').val(modelId);
                        form.render();
                        var myChart = echarts.init(document.getElementById('main'));
                        myChart.showLoading();
                        $.get('../../modelRely/echars/' + modelId, function (data) {

                            myChart.hideLoading();
                            echarts.util.each(data.list, function (datum, index) {
                                index % 2 === 0 && (datum.collapsed = true);
                            });

                            myChart.setOption(option = {
                                tooltip: {
                                    trigger: 'item',
                                    triggerOn: 'mousemove'
                                },
                                series: [
                                    {
                                        type: 'tree',
                                        data: [data.data],
                                        top: '20%',
                                        left: '20%',
                                        bottom: '1%',
                                        right: '20%',
                                        symbolSize: 10,
                                        label: {
                                            normal: {
                                                position: 'left',
                                                verticalAlign: 'middle',
                                                align: 'right',
                                                fontSize: 18
                                            }
                                        },
                                        leaves: {
                                            label: {
                                                normal: {
                                                    position: 'right',
                                                    verticalAlign: 'middle',
                                                    align: 'left'
                                                }
                                            }
                                        },
                                        expandAndCollapse: true,
                                        animationDuration: 550,
                                        animationDurationUpdate: 750
                                    }
                                ]
                            });
                        });
                    }
                });
            });

        }

    })

    $("#btn_field").on('click', null, function () {
        if (model.select(layerTips)) {
            var modelId = model.currentItem.modelId;
            var modelName = model.currentItem.name;
            var typeId = model.currentItem.typeId;
            $.get('modelField.html', null, function (form) {
                //console.log(form);
                addBoxIndex = layer.open({
                    type: 1,
                    title: modelName,
                    content: form,
                    shade: false,
                    area: ['100%', '100%'],
                    async: false,
                    success: function (layero, index) {
                        modelField.init(modelId, modelName, typeId);
                        var form = layui.form(modelId);
                        layero.find('#modelFieldTable').val(modelId);
                        //删除render方法中的modelId解决报错问题
                        form.render();
                    }
                });
            });
        }
    });
    $("#btn_init").on('click', null, function () {
        layer.confirm('确定初始化模型数据吗？', null, function (index) {
            $.ajax({
                url: "../../model/modelInit",
                type: 'get',
                dataType: "json",
                success: function (res) {
                    layerTips.msg(res.message);
                    layerTips.close(index);
                    location.reload();
                },
                error: function () {
                    layerTips.msg("初始化失败");
                    location.reload();
                }
            });
            layer.close(index);
        });

    });
    $("#btn_initField").on('click', null, function () {
        layer.confirm('确定初始化模型字段数据吗？', null, function (index) {
            $.ajax({
                url: "../../modelField/modelFieldInit",
                type: 'get',
                dataType: "json",
                success: function (res) {
                    layerTips.msg(res.message);
                    layerTips.close(index);
                    location.reload();
                },
                error: function () {
                    layerTips.msg("初始化失败");
                    location.reload();
                }
            });
            layer.close(index);
        });

    });
});