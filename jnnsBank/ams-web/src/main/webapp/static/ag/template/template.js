var template = {
    baseUrl: "../../template",
    entity: "template",
    tableId: "templateTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
template.columns = function () {
    return [{
        radio: true
    }, {
        field: 'billType',
        title: '单据类型',
        formatter: function (value) {
            return template.billTypeMapping[value];
        }
    }, {
        field: 'depositorType',
        title: '存款人类别',
        formatter: function (value) {
            return template.depositorTypeMapping[value];
        }
    }, {
        field: 'acctType',
        title: '账户性质',
        formatter: function (value) {
            return template.accTypeMapping[value];
        }
    }, {
        field: 'templateName',
        title: '打印模版名称'
    }, {
        field: 'id',
        formatter: function (value) {
            return "<a href='javascript:downloadFile(\""+value+"\");' target='_self'>下载</a>";
        }
    }];
};

template.billTypeMapping = {
    "ACCT_INIT":"存量",
    "ACCT_OPEN":"新开户",
    "ACCT_CHANGE":"变更",
    "ACCT_SUSPEND":"久悬",
    "ACCT_SEARCH":"查询",
    "ACCT_REVOKE":"销户",
    "ACCT_SAIC":"工商信息",
    "APPT_UNCOMPLETE":"接洽打印",
    "APPT_AFTERCOMPLETE":"已受理详情"

};
template.accTypeMapping = {
    "jiben":"基本存款账户",
    "yiban":"一般存款账户",
    "yusuan":"预算单位专用存款账户",
    "feiyusuan":"非预算单位专用存款账户",
    "teshu":"特殊单位专用存款账户",
    "linshi":"临时机构临时存款账户",
    "feilinshi":"非临时机构临时存款账户",
    "yanzi":"验资户临时存款账户",
    "zengzi":"增资户临时存款账户"

};
template.depositorTypeMapping = {
    "ALL":"所有",
    "DEPOSITOR_TYPE_01":"企业法人",
    "DEPOSITOR_TYPE_02":"非法人企业",
    "DEPOSITOR_TYPE_03":"机关",
    "DEPOSITOR_TYPE_04":"实行预算管理的事业单位",
    "DEPOSITOR_TYPE_05":"非预算管理的事业单位",
    "DEPOSITOR_TYPE_06":"团级(含)以上军队及分散执勤的支(分)队",
    "DEPOSITOR_TYPE_07":"团级(含)以上武警部队及分散执勤的支(分)队",
    "DEPOSITOR_TYPE_08":"社会团体",
    "DEPOSITOR_TYPE_09":"宗教组织",
    "DEPOSITOR_TYPE_10":"民办非企业组织",
    "DEPOSITOR_TYPE_11":"外地常设机构",
    "DEPOSITOR_TYPE_12":"外国驻华机构",
    "DEPOSITOR_TYPE_13":"有字号的个体工商户",
    "DEPOSITOR_TYPE_14":"无字号的个体工商户",
    "DEPOSITOR_TYPE_15":"居民委员会、村民委员会、社区委员会",
    "DEPOSITOR_TYPE_16":"单位设立的独立核算的附属机构",
    "DEPOSITOR_TYPE_17":"其他组织",
    "DEPOSITOR_TYPE_20":"境外机构",
    "DEPOSITOR_TYPE_50":"QFII",
    "DEPOSITOR_TYPE_51":"境外贸易机构",
    "DEPOSITOR_TYPE_52":"跨境清算",
    "DEPOSITOR_TYPE_53":"工商信息"
};

template.queryParams = function (params) {
    if (!params)
        return {
            billType: $("#billType").val(),
            depositorType: $("#depositorType").val(),
            templateName: $("#templateName").val(),
            acctType:$("#acctType").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        billType: $("#billType").val(),
        depositorType: $("#depositorType").val(),
        templateName: $("#templateName").val(),
        acctType:$("#acctType").val()
    };
    return temp;
};
template.init = function () {

    template.table = $('#' + template.tableId).bootstrapTable({
        url: template.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + template.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: template.order, //排序方式
        queryParams: template.queryParams,//传递参数（*）
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
        uniqueId: template.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: template.columns(),
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
template.select = function (layerTips) {
    var rows = template.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        template.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    template.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = template.queryParams();
        queryParams.pageNumber=1;
        template.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加打印模版',
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

                    upload({
                        url: template.baseUrl + '/upload',
                        success: function (res) {
                            if (res.code === 'ACK') {
                                layerTips.msg("上传成功！");
                                layero.find("#fileName").val(res.data.fileName);
                                layero.find("#displayName").text(res.data.displayName);
                            } else {
                                layerTips.msg(res.message);
                            }
                        }
                    });

                    form.on('submit(edit)', function (data) {
                        if (data.field.billType==""){
                            layerTips.msg("请输入单据类型");
                            return false;
                        } else if(data.field.depositorType==""){
                            layerTips.msg("请输入存款人类别");
                            return false;
                        }else if(data.field.templateName == ""){
                            layerTips.msg("请输入模版名称");
                            return false;
                        }else if(data.field.fileName==""){
                            layerTips.msg("请上传打印模板");
                            return false;
                        }
                        $.ajax({
                            url: template.baseUrl+"/",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function () {
                                layerTips.msg('保存成功');
                                layerTips.close(index);
                                location.reload();
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
        if (template.select(layerTips)) {
            var id = template.currentItem.id;
            $.get(template.baseUrl + '/' + id, null, function (data) {
                var result = data.data;
                $.get('edit.html', null, function (form) {
                    layer.open({
                        type: 1,
                        title: '编辑打印模版',
                        content: form,
                        btn: ['保存', '取消'],
                        shade: false,
                        offset: ['20px', '20%'],
                        area: ['600px', '500px'],
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

                            upload({
                                url: template.baseUrl + '/upload',
                                success: function (res) {
                                    if (res.code === 'ACK') {
                                        layerTips.msg("上传成功！");
                                        layero.find("#fileName").val(res.data.fileName);
                                        layero.find("#displayName").text(res.data.displayName);
                                    } else {
                                        layerTips.msg(res.message);
                                    }
                                }
                            });

                            form.on('submit(edit)', function (data) {
                                $.ajax({
                                    url: template.baseUrl + "/" + result.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (data) {
                                        if(data.code == 'ACK') {
                                            layerTips.msg('更新成功');
                                        } else {
                                            layerTips.msg(data.message);
                                        }
                                        layerTips.close(index);
                                        location.reload();
                                    }

                                });
                                //这里可以写ajax方法提交表单
                                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                            });
                        }
                    });
                });
            });
        }
    });
    $('#btn_del').on('click', function () {
        if (template.select(layerTips)) {
            var id = template.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: template.baseUrl + "/" + id,
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
    $('#btn_upload').on('click', function () {
        $('#file').click();
    });
    upload({
        url: template.baseUrl + '/upload',
        success: function(res) {
            if(res.code === 'ACK') {
                layerTips.msg("上传成功！");
                location.reload();
            } else {
                layerTips.msg(res.message);
            }
        }
    });

    $("#btn_download").on("click", function () {
        $("#templateDownloadForm").submit();
    });

});


function downloadFile(id) {
    document.getElementById("downloadFrame").src = template.baseUrl+'/download?id='+id;
}