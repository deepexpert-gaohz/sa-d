var areaPbcAudit = {
    baseUrl: "../../pbc/address",
    entity: "pbcIPAddressPo",
    toolbarId: "ca_toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    selectItems: [],
    code: "id",
    parentCode: "parentId",
    isInit: true
};

var areaPbcAuditList = {
    entity: "pbcIPAddressPo",
    tableId: "ca_list",
    toolbarId: "ca_list_toolbar",
    unique: "id",
    order: "desc",
    currentItem: {},
    selectItems: []
};

areaPbcAuditList.queryParams = function (params) {
    var temp = {
        //看后端接口传分页参数
        size: params.limit, //页面大小
        page: (params.offset / params.limit), //页码
        ip: $.trim($("#ipAddress").val()), //机构名称
        provinceName: $.trim($("#provinceName").val()), //人行机构代码
    };
    return temp;
};

areaPbcAuditList.columns = function () {
    return [{
        checkbox: true
    }, {
        field: 'id',
        title: 'id',
        visible: false
    }, {
        field: 'ip',
        title: '人行IP地址'
    }, {
        field: 'provinceName',
        title: '省份'
    }, {
        field: 'isAnnualSubmit',
        title: '人行年检提交',
        formatter: function (value, row, index) {
            return value == true ? '启用' : '禁用';
        }
    }];
};

areaPbcAuditList.select = function (layerTips) {
    var rows = areaPbcAuditList.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        areaPbcAuditList.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

areaPbcAuditList.init = function () {

    areaPbcAuditList.table = $('#' + areaPbcAuditList.tableId).bootstrapTable({
        url: areaPbcAudit.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + areaPbcAuditList.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: areaPbcAuditList.order, //排序方式
        queryParams: areaPbcAuditList.queryParams,//传递参数（*）
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
        uniqueId: areaPbcAuditList.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: areaPbcAuditList.columns(),
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};

layui.use(['form', 'layedit', 'laydate', 'picker', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;

    var caBoxIndex = -1;
    var addBoxIndex = -1;

    $('#btn_area_pbc_audit').on('click', function () {
        $.get('areaPbcAudit.html', null, function (form) {
            caBoxIndex = layer.open({
                type: 1,
                title: '人行系统设置',
                content: form,
                shade: false,
                offset: 't',
                area: ['100%', '100%'],
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
                    caBoxIndex = -1;
                },
                success: function (layero, index) {
                    var form = layui.form;

                    layero.find('#btn_ca_query, #btn_refresh_pbc_audit').on('click', function () {
                        $('#ca_list').bootstrapTable('refresh');
                    });

                    layero.find('#btn_edit_pbc_audit').on('click', function () {
                        if (areaPbcAuditList.select(layer)) {
                            var id = areaPbcAuditList.currentItem.id;

                            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                            $.get('areaPbcEdit.html', null, function (form) {
                                addBoxIndex = layer.open({
                                    type: 1,
                                    title: '修改',
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

                                        $.get(areaPbcAudit.baseUrl + '/' + id, null, function (data) {
                                            var result = data.data;
                                            // setFromValues(layero, result);

                                            layero.find('#ip').val(result.ip);
                                            layero.find('#provinceName').val(result.provinceName);
                                            if(result.isAnnualSubmit == true) {
                                                $("input[name='isAnnualSubmit'][value=true]").attr("checked",true);
                                            } else {
                                                $("input[name='isAnnualSubmit'][value=false]").attr("checked",true);
                                            }

                                            form.render();
                                        });

                                        layero.find("input[name='ip']").attr("disabled", "");

                                        form.on('submit(edit)', function (data) {
                                            if(id) {
                                                data.field.id = id;

                                                $.ajax({
                                                    url: areaPbcAudit.baseUrl + "/save",
                                                    type: 'POST',
                                                    data: data.field,
                                                    dataType: "json",
                                                    success: function (res) {
                                                        if (res.code === 'ACK') {
                                                            layerTips.msg('更新成功');
                                                            layer.close(addBoxIndex);
                                                            $('#ca_list').bootstrapTable('refresh');
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

                    areaPbcAuditList.init();
                }
            });
        });

    });


});