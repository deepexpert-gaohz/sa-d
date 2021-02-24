var list = {
    baseUrl: "../../holiday",
    entity: "holiday",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'dateStr',
        title: '日期'
        //获取日期列的值进行转换
        // formatter: function (value, row, index) {
        //     return changeDateStr(value)
        // }
    }, {
        field: 'holidayType',
        title: '节假日类型',
        formatter: function (value, row, index) {
            if(value == 'WORKDAY_REST') {
                return '放假调休';
            } else if(value == 'HOLIDAY_WORK') {
                return '上班';
            }
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            dateStr: $("#holiday-date").val(),
            holidayType: $("#holiday-type").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        dateStr: $("#holiday-date").val(),
        holidayType: $("#holiday-type").val()
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + list.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list.order, //排序方式
        queryParams: list.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 365, //每页的记录行数（*）
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
        columns: list.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                console.log(res.data.size);
                console.log(res.data);
                return {total:res.data.content.size, rows: res.data.content};
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

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var queryParams = list.queryParams();
        queryParams.pageNumber=1;
        list.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加特殊假日',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['20px', '20%'],
                area: ['500px', '300px'],
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
                            url: list.baseUrl+"/",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (res) {
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

    $('#btn_edit').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;

            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改特殊假日',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['500px', '300px'],
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

                        $.get(list.baseUrl + '/' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            form.render('select');
                        });

                        form.on('submit(edit)', function (data) {

                            if(list.currentItem.id) {
                                $.ajax({
                                    url: list.baseUrl + "/" + list.currentItem.id,
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

    $('#btn_del').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: list.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            location.reload();
                        } else {
                            layerTips.msg("移除失败！"+data.message)
                            location.reload();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    
    $('#btn_sync').on('click', function () {
        layer.confirm('确定同步本年度节假日吗？', null, function (index) {
            $.ajax({
                url: list.baseUrl + "/sync",
                type: "get",
                success: function (data) {
                    if (data.code === 'ACK') {
                        layerTips.msg("同步成功！");
                        location.reload();
                    } else {
                        layerTips.msg("同步失败！"+data.message)
                        location.reload();
                    }
                }
            });
            layer.close(index);
        });
    });

});