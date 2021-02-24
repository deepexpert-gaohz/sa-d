var ruleElement = {
    baseUrl: "../../ruleConfigure",
    entity: "modelRule",
    tableId: "elementTable",
    toolbarId: "elementToolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    fieldOption:"",
    pageNumber:''
};
ruleElement.columns = function () {
    return [{
        radio: true
    }, {
        field: 'fieldName',
        title: '字段名称'
    }, {
        field: 'field',
        title: '字段'
    }, {
        field: 'condition',
        title: '条件'
    }, {
        field: 'value',
        title: '值'
    }
    ];
};
ruleElement.queryParams = function (params) {
    if (!params)
        return {
            id:rule.currentItem.id
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        id:rule.currentItem.id
    };
    return temp;
};

ruleElement.init = function () {

    ruleElement.table = $('#' + ruleElement.tableId).bootstrapTable({
        url: ruleElement.baseUrl + '/getField', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + ruleElement.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: ruleElement.order, //排序方式
        queryParams: ruleElement.queryParams,//传递参数（*）
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
        uniqueId: ruleElement.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: ruleElement.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                ruleElement.pageNumber=res.data.offset
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
ruleElement.select = function (layerTips) {
    var rows = ruleElement.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        ruleElement.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

ruleElement.refresh = function(modelId){
    rule.select();
   // element.queryParams(modelId);
    ruleElement.table.bootstrapTable("refresh");
}

layui.use(['form', 'layedit', 'laydate'], function () {
    ruleElement.init();
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate;
    var addBoxIndex = -1;
    //初始化页面上面的按钮事件

    $('#btn_element_add').on('click', function () {
        if(rule.select(layerTips)) {
            if (addBoxIndex !== -1)
                return;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('../modelRule/ruleEdit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '添加按钮',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: 0.1,
                    offset: ['20px', '20%'],
                    area: ['800px', '600px'],
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
                        form.on('select(test)', function(data){
                            var value = data.value;
                            if (value!="between"){
                                $("#inValueS").hide();
                                $('#inValueS').attr("lay-verify", "");

                            }else{
                                $("#inValueS").show();
                                $('#inValueS').attr("lay-verify", "required");

                            }
                        });
                        editIndex = layedit.build('description_editor');
                        layero.find('#fieldNmae').append(ruleElement.fieldOption);
                        form.render('select');
                        form.render();
                        form.on('submit(edit)', function (data) {
                            $.ajax({
                                url: ruleElement.baseUrl+"/save",
                                type: 'post',
                                data: data.field,
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('保存成功');
                                    layer.close(addBoxIndex);
                                    ruleElement.refresh();
                                    // ruleElement.refresh();
                                    // var rows = rule.table.bootstrapTreeTable('getSelections');
                                    // rule.refresh();
                                    // rule.currentItem = rows[0];
                                    // rows.LAY_CHECKED=true

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


    $('#btn_element_edit').on('click', function () {
        if (ruleElement.select(layerTips)) {
            if (addBoxIndex !== -1)
                return;
            var id = ruleElement.currentItem.id;
            var field="";
            //设为同步
            $.ajaxSettings.async = false;
            $.get(ruleElement.baseUrl + '/findConfigerById/' + id, null, function (data) {
                var result = data.data;
                var id = result.ruleId;
                $.get(ruleElement.baseUrl + '/findEditField/' + id, null, function (data) {
                    if(data.code === 'ACK') {
                        //console.log(data);
                        var options = '';
                        for (var i = 0; i < data.data.length; i++) {
                            options += '<option value="' + data.data[i].id + '" >' + data.data[i].fieldName + '</option>';
                        }
                        ruleElement.fieldOption="";
                        ruleElement.fieldOption += options;
                       //console.log(ruleElement.fieldOption);
                        //console.log(element.fieldOption);
                    }
                })
                $.get('../modelRule/ruleEdit.html', null, function (form) {
                    addBoxIndex = layer.open({
                        type: 1,
                        title: '编辑按钮',
                        content: form,
                        btn: ['保存', '取消'],
                        shade: 0.1,
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
                            if(result.condition!="between"){
                                //alert(result.condition);
                                $("#inValueS").hide();
                                $('#inValueS').attr("lay-verify", "");
                            }
                            else {
                                $("#inValueS").show();
                                $('#inValueS').attr("lay-verify", "required");
                            }
                            form.on('select(test)', function(data){
                                //alert(123);
                                var value = data.value;
                                // console.log(data.value); //得到被选中的值
                                if (value!="between"){
                                    $("#inValueS").hide();
                                    $('#inValueS').attr("lay-verify", "");
                                }else{
                                    $("#inValueS").show();
                                    $('#inValueS').attr("lay-verify", "required");
                                }
                            });
                            setFromValues(layero, result);
                            editIndex = layedit.build('description_editor');
                           // console.log(result);
                            layero.find("select[name='condition']").val(result.condition);
                            layero.find('#fieldNmae').append(ruleElement.fieldOption);
                            layero.find("select[name='ruleId']").val(result.ruleId);
                            //alert(element.currentItem.field);
                            layero.find("#field").val(ruleElement.currentItem.field);
                            form.render("select");
                            form.render();
                            form.on('submit(edit)', function (data) {
                                $.ajax({
                                    url: ruleElement.baseUrl + "/" + result.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function () {
                                        layerTips.msg('更新成功');
                                        layer.close(addBoxIndex);
                                        ruleElement.refresh();
                                    }

                                });
                                //这里可以写ajax方法提交表单
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            });
                            ruleElement.fieldOption="";
                            layero.find('#fieldNmae').append(ruleElement.fieldOption);
                        } ,
                        end: function () {
                            addBoxIndex = -1;
                        }
                    });
                });
            });
        }
    });
    $('#btn_element_del').on('click', function () {
        if (ruleElement.select(layerTips)) {
            var id = ruleElement.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: ruleElement.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            layer.close(addBoxIndex);
                            ruleElement.refresh();
                        } else {
                            layerTips.msg("移除失败！");
                            window.location.reload();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
});


