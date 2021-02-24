var modelField = {
    baseUrl: "../../modelField",
    entity: "modelField",
    tableId: "modelFieldTable",
    toolbarId: "toolbarModelField",
    unique: "id",
    order: "asc",
    currentItem: {},
    fieldName:'',
    modelFieldOptions: '',
    modelIdval:''
};

modelField.columns = function () {
    return [
        {
            field: 'id',
            title: '字段',
            visible: false
        }, {
            field: 'fieldsEn',
            title: '字段'
        }, {
            field: 'fieldsZh',
            title: '字段名称',
            editable: {
                type: 'text',
                title: '字段名称',
                validate: function (v) {
                    if (!v) return '字段名称不能为空';
                }
            }
        }, {
            field: 'modelId',
            title: '字段表名'
        }, {
            field: 'showFlag',
            title: '是否显示',
            formatter: function (showFlag) {
                return showFlag;
            } ,
            editable: {
                type: 'select',
                title: '是否显示',
                source:[{value:"1",text:"否"},{value:"0",text:"是"}]
            }
        }, {
            field: 'exportFlag',
            title: '是否导出',
            formatter: function (exportFlag) {
                return exportFlag;
            },
            editable: {
                type: 'select',
                title: '是否导出',
                source:[{value:"1",text:"否"},{value:"0",text:"是"}]
            }
        }, {
            field: 'orderFlag',
            title: '字段排序',
            editable: {
                type: 'text',
                title: '字段排序',
                validate: function (v) {
                    if ((!(/(^[1-9]\d*$)/.test(v)))) return '字段排序必须是正整数';
                }
            }
        },{
            field: "riskdataStaticTable",
            title: '操作',
            formatter: function operateFormatter(){
                return "<a style='color: #4CAF50' class='deleteField'>删除</a>"
            },
            events: {
                'click .deleteField': function (e, value, row) {
                    deleteField(row)
                },
            }
        }];
};


modelField.init = function (modelId,modelName,typeId) {
    modelField.modelIdval = modelId;
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    modelField.table = $('#' + modelField.tableId).bootstrapTable({
        url: modelField.baseUrl + '/queryModelField/'+modelId.toUpperCase(), //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + modelField.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, //是否显示分页（*）
        sortable: true, //是否启用排序
        sortOrder: modelField.order, //排序方式
        queryParams: modelField.queryParams,//传递参数（*）
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
        uniqueId: modelField.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: modelField.columns(modelId),
        onEditableSave: function (field, row, oldValue, $el) {
            $.ajax({
                type: "get",
                url: modelField.baseUrl + "/saveModelField",
                data: row,
                dataType: 'JSON',
                success: function (data) {
                    if (data.code == 'ACK') {
                        layerTips.msg("更新成功");
                    }},
                error: function(data) {
                    if (data.code != 'ACK') {
                        layerTips.msg('更新失败');
                    }
                }
            });
        },
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return {total:res.data.totalRecord, rows: res.data};
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
    $.get(modelField.baseUrl +"/getFileds",null,function (data) {
        var options = '';
        for (var i = 0; i < data.data.length; i++){
            options += '<option value="' + data.data[i].field + '" >' + data.data[i].fieldName + '</option>';
        }
        form.render();
        model.modelFieldOptions = options;
    });

    $('#btnField_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('fieldEdit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: modelName,
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
                    $("#riskId").val(modelId);
                    layero.find('#fieldName').append(model.modelFieldOptions);
                    form.render();
                    form.on('submit(edit)', function (data) {
                        var field= $("#fieldName").val();
                        $.get(modelField.baseUrl +"/findByField/"+field, null, function (data) {
                            modelField.fieldName = data.data.fieldName;
                            var fieldNameVal = encodeURIComponent(modelField.fieldName);
                            $.ajaxSettings.async = false;
                            $.ajax({
                                url: modelField.baseUrl + "/saveConfigerField/"+modelId+"/"+field+"/"+fieldNameVal,
                                type: 'get',
                                dataType: "json",
                                success: function (res) {
                                    if (res.code === 'ACK') {
                                        layer.msg('保存成功');
                                        layer.close(index);
                                        layer.close(addBoxIndex);
                                        $('#modelFieldTable').bootstrapTable('refresh');
                                    } else {
                                        layer.msg(res.message);
                                    }
                                }
                            });
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

    //模型字段初始化
    $('#btnField_init').on('click', function () {
        alert(11);
        if (addBoxIndex !== -1)
            return;
        var modelId = modelField.modelIdval;
        $.ajax({
            url: modelField.baseUrl + "/initModelFields/" + modelId,
            type: "get",
            success: function (data) {
                if (data.code === 'ACK') {
                    layerTips.msg("初始化成功！");
                    $('#modelFieldTable').bootstrapTable('refresh');
                } else {
                    layerTips.msg("初始化失败！")
                    $('#modelFieldTable').bootstrapTable('refresh');
                }
            }
        });

    });
};
layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form();
    form.on('select(fieldsName)', function(data){
        var field = data.value;
        $("#field").val(field);
    });

});
function deleteField(row){

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var id = row.id;
    layer.confirm('确定删除数据吗？', null, function (index) {
        $.ajax({
            url: modelField.baseUrl + "/deleteField/" + id,
            type: "get",
            success: function (data) {
                if (data.code === 'ACK') {
                    layerTips.msg("删除成功！");
                    layer.close(index);
                    // layer.close(addBoxIndex);
                    $('#modelFieldTable').bootstrapTable('refresh');
                } else {
                    layerTips.msg("删除失败！")
                    $('#modelFieldTable').bootstrapTable('refresh');
                }
            }
        });
        layer.close(index);
    });
}
