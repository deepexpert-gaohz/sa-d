var model = {
    baseUrl: "../../ModelCountFileController",
    entity: "ModelsExtendDto",
    tableId: "dateTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    levelIdOptions: '',
    ruleIdOptions: '',
    typeIdOptions:''

};
model.columns = function () {
    return [{
        checkbox:true
    }, {
        field: 'modelId',
        title: '模型编号'
    }, {
        field: 'riskType',
        title: '风险类型'
    }, {
        field: 'name',
        title: '模型名称'
    }, {
        field: 'tjjg',
        title: '统计结果'
    }];
};

model.queryParams = function (params) {
    if (!params)
        return {
            orgName: $("#orgName").val(),
            riskType: $("#riskType").val(),
            modelName: $("#modelName").val(),
            dataDate:$("#startEndTime").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        orgName: $("#orgName").val(),
        riskType: $("#riskType").val(),
        modelName: $("#modelName").val(),
        dataDate:$("#startEndTime").val()
    };
    return temp;
};
model.init = function () {
    model.table = $('#' + model.tableId).bootstrapTable({
        url: model.baseUrl + '/expRiskDataDiyView', //请求后台的URL（*）
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
model.select = function (layerTips) {
    var rows = model.table.bootstrapTable('getSelections');
    if (rows.length !=0) {
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
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;






    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = model.queryParams();
        queryParams.pageNumber=1;
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
                        console.log(data.field);
                        $.ajax({
                            url: model.baseUrl+"/saveModel",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (res) {
                                console.log(res)
                                if (res.code === 'ACK') {
                                    layerTips.msg('保存成功');
                                    layerTips.close(index);
                                    location.reload();
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
    $('#btn_edit').on('click', function (){
        if (model.select(layerTips)) {
            var id = model.currentItem.id;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
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
                        var form = layui.form();
                        form.render();
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
                            if(model.currentItem.id) {
                                $.ajax({
                                    url: model.baseUrl + "/" + model.currentItem.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (res) {
                                        if (res.code === 'ACK') {
                                            layerTips.msg('更新成功');
                                            layerTips.close(index);
                                            location.reload();
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



    $('#exportBtn').on('click', function () {

        if (model.select(layerTips)) {

            layer.confirm('确定导出数据吗？', null, function (index) {
                var row = $.map($('#dateTable').bootstrapTable('getSelections'),function (row) {
                    return row.modelId;
                });
                var b=row.toString();

                b = b.replace("\"").replace("[").replace("]")
                var dateStr = $("#startEndTime").val();
                //window.location.href=model.baseUrl + "/expRiskDataByOrg?ids="+b ;

                window.location.href=model.baseUrl + "/expRiskDataByOrg?ids="+b+"&dateStr="+dateStr;

                layer.close(index);
            });
        }
    });


});



