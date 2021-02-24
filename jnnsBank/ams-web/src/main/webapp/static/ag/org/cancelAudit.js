var cancelAudit = {
    baseUrl: "../../organization",
    entity: "organization",
    treeId: 'ca_tree',
    toolbarId: "ca_toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    selectItems: [],
    code: "id",
    parentCode: "parentId",
    isQuerying: false,
    isInit: true
};

cancelAudit.columns = function () {
    return [
        {
            field: 'selectItem',
            radio: true
        }, {
            field: 'name',
            title: '组织名称'
        }, {
            field: 'code',
            title: '编码'
    }];
};
//得到查询的参数
cancelAudit.queryParams = function () {
    var temp = {};
    return temp;
};
cancelAudit.init = function () {

    if ($.jstree.reference($("#" + cancelAudit.treeId))) {
        $("#" + cancelAudit.treeId).jstree().delete_node($("#" + cancelAudit.treeId).jstree().get_json());
    }
    $("#" + cancelAudit.treeId).jstree('destroy');

    $("#" + cancelAudit.treeId).jstree({
        "core": {
            "themes": {
                responsive: false,
                dots: false,
                stripes: true
            },
            // so that create works
            'check_callback': function (operation, node, node_parent, node_position) {
                return true;
            },
            'data': {
                url: function () {
                    return cancelAudit.baseUrl + '/orgTree';
                },
                type: 'GET',
                data: function (node) {
                    if (cancelAudit.isQuerying) {
                        return {'name': $('#ac_name').val(), 'code': $('#ac_code').val()};
                    } else {
                        return {'parentId': node.id === '#' ? '' : node.id};
                    }
                },
                dataFilter: function (nodes) {
                    var nodesArray = JSON.parse(nodes).data;
                    var data = [];
                    for (var i = 0; i < nodesArray.length; i++) {
                        var node = {
                            //text: nodesArray[i].name + state,
                            id: nodesArray[i].id,
                            children: nodesArray[i].childs ? true : false,
                            data: nodesArray[i]
                        };
                        if(nodesArray[i].cancelHeZhun == true){
                            node.text = nodesArray[i].name + ' <span class="label label-warning">取消核准</span>';
                            if(nodesArray[i].childs) {
                                node.text = nodesArray[i].name
                                    + ' <span class="label label-success check-all" onclick="cancelAudit.checkAll(this, event);">全选</span>'
                                    + ' <span class="label label-info uncheck-all" onclick="cancelAudit.uncheckAll(this, event);">取消全选</span>'
                                    + ' <span class="label label-warning">取消核准</span>'
                            }
                            node.state = {
                                "selected": true,
                                "disabled": true
                            }
                        }else{
                            node.text = nodesArray[i].name;
                            if(nodesArray[i].childs) {
                                node.text = nodesArray[i].name
                                    + ' <span class="label label-success check-all" onclick="cancelAudit.checkAll(this, event);">全选</span>'
                                    + ' <span class="label label-info uncheck-all" onclick="cancelAudit.uncheckAll(this, event);">取消全选</span>'
                            }
                        }
                        if (!cancelAudit.isQuerying) {
                            if (!cancelAudit.isInit) {
                                node.parent = nodesArray[i].parentId;
                            } else {
                                node.parent = '#';
                            }
                        } else {
                            node.parent = '#';
                        }

                        data.push(node);
                    }

                    // 查询重置
                    if (cancelAudit.isQuerying) {
                        cancelAudit.isQuerying = false;
                    }

                    //初始化标志
                    if (cancelAudit.isInit) {
                        cancelAudit.isInit = false;
                    }

                    return JSON.stringify(data);
                }
            }
        },
        "types": {
            "default": {
                "icon": "fa fa-bank icon-state-info icon-lg"
            },
            "file": {
                "icon": "fa fa-building-o icon-state-info icon-lg"
            }
        },
        "state": {"key": "orgTree"},
        "plugins": ["state", "types", "wholerow", "checkbox"],
        "checkbox" : {
            "keep_selected_style" : false,
            "three_state":false,
            "tie_selection":true
        }
    }).on("loaded.jstree", function(event, data) {
        data.instance.clear_state(); // <<< 这句清除jstree保存的选中状态
    });
    

    $("#" + cancelAudit.treeId).on("select_node.jstree", function (e, data) {
        cancelAudit.currentItem = data.node.data;
    });
};
cancelAudit.select = function (layerTips) {
    cancelAudit.selectItems = $("#" + cancelAudit.treeId).jstree(true).get_selected(true); //获取所有选中的节点对象

    if (cancelAudit.selectItems.length < 1) {
        layerTips.msg("请选中一行");
        return false;
    }
    return true;
};

cancelAudit.getSelectIds = function (layerTips) {
    cancelAudit.selectItems = $("#" + cancelAudit.treeId).jstree(true).get_selected(true); //获取所有选中的节点对象

    var ids = [];
    for (i = 0; i < cancelAudit.selectItems.length; i++) {
        var item = cancelAudit.selectItems[i];
        ids.push(item.id);
    }
    return ids;
};

cancelAudit.checkAll = function (obj, event) {

    if (event.stopPropagation){
        event.stopPropagation();
    }
    else{
        event.cancelBubble = true;
    }

    if (event.preventDefault){
      event.preventDefault();
    }
    else{
       event.returnValue = false;
    }

    var elem = $(obj);
    var liElem = elem.parent().parent();
    var listElem = liElem.find('.jstree-checkbox');

    for (var i = 0; i < listElem.length; i++) {
        var item = listElem[i];
        if(!$(item).parent().hasClass('jstree-clicked')) {
            $(item).click();
        }
    }
    return false;
}

cancelAudit.uncheckAll = function (obj, event) {
    if (event.stopPropagation){
        event.stopPropagation();
    }
    else{
        event.cancelBubble = true;
    }

    if (event.preventDefault){
      event.preventDefault();
    }
    else{
       event.returnValue = false;
    }

    var elem = $(obj);
    var liElem = elem.parent().parent();
    var listElem = liElem.find('.jstree-checkbox');

    for (var i = 0; i < listElem.length; i++) {
        var item = listElem[i];
        if($(item).parent().hasClass('jstree-clicked')) {
            $(item).click();
        }
    }
    return false;
}

cancelAudit.refresh = function () {
    cancelAudit.isInit = true;
    $("#" + cancelAudit.treeId).jstree(true).refresh();
};

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});


layui.use(['form', 'layedit', 'laydate', 'picker', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;

    var caBoxIndex = -1;

    $('#btn_open_cancel_audit').on('click', function () {
        
        $.get('cancelAudit.html', null, function (form) {
            caBoxIndex = layer.open({
                type: 1,
                title: '取消核准设置',
                content: form,
                shade: false,
                offset: 't',
                area: ['100%','100%'],
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

                    cancelAudit.isQuerying = false;
                    cancelAudit.isInit = true;

                    $.get(cancelAudit.baseUrl + '/root', null, function (data) {
						if (data) {
                            cancelAudit.init();
                            layero.find('#btn_ca_query').on('click', function () {
                                cancelAudit.isQuerying = true;
                                cancelAudit.init();
                            });
						}
                    });

                    cancelAuditList.init();

                    layero.find('#btn_ca_list_query').on('click', function () {
                        
                        cancelAuditList.table.bootstrapTable('refresh', { pageNumber: 1 });
                    });
                    
                    layero.find('#btn_cancel_audit').on('click', function () {
                        if (cancelAudit.select(layerTips)) {
                            var ids = cancelAudit.getSelectIds();
                            layer.confirm('确定取消核准吗？', null, function (index) {
                                $.ajax({
                                    url: org.baseUrl + "/cancelHeZhun",
                                    type: "POST",
                                    data: {
                                        ids: ids
                                    },
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg(data.message);
                                            cancelAudit.refresh();
                                            cancelAuditList.refresh();
                                        } else {
                                            layerTips.msg("取消核准失败！");
                                            cancelAudit.refresh();
                                            cancelAuditList.refresh();
                                        }
                                    }
                                });
                                layer.close(index);
                            });
                        }
                    });


                    layero.find('#btn_recall_cancel_audit').on('click', function () {
                        if (cancelAuditList.select(layerTips)) {
                            var ids = cancelAuditList.getSelectIds();
                            layer.confirm('确定撤回核准业务吗？', null, function (index) {
                                $.ajax({
                                    url: cancelAudit.baseUrl + "/recallCancelHeZhun",
                                    type: "POST",
                                    data: { ids: ids },
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg("撤回核准业务成功！");
                                            cancelAudit.refresh();
                                            cancelAuditList.refresh();
                                        } else {
                                            layerTips.msg("撤回核准业务失败，请确认所选网点是否受该用户管辖！");
                                            cancelAudit.refresh();
                                            cancelAuditList.refresh();
                                        }
                                    }
                                });
                                layer.close(index);
                            });
                        }
                    });


                }
            });
        });

    });

});

var cancelAuditList = {
   entity: "organRegisterPo",
   tableId: "ca_list",
   toolbarId: "ca_list_toolbar",
   unique: "id",
   order: "asc",
   currentItem: {},
   selectItems: []
};
cancelAuditList.columns = function () {
    return [{
        checkbox: true
    }, {
       field: 'name',
       title: '机构名称'
   }, {
       field: 'pbcCode',
       title: '人行机构代码'
   }];
};

cancelAuditList.queryParams = function (params) {
   var temp = {
       //看后端接口传分页参数
       size: params.limit, //页面大小
       page: (params.offset / params.limit), //页码
       name: $.trim($("#organName").val()), //机构名称
       pbcCode: $.trim($("#bankCode").val()), //人行机构代码
   };
   return temp;
};

cancelAuditList.init = function () {

    cancelAuditList.table = $('#' + cancelAuditList.tableId).bootstrapTable({
       url: cancelAudit.baseUrl + '/organRegisterList', //请求后台的URL（*）
       method: 'get', //请求方式（*）
       toolbar: '#' + cancelAuditList.toolbarId, //工具按钮用哪个容器
       striped: true, //是否显示行间隔色
       cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
       pagination: true, //是否显示分页（*）
       sortable: false, //是否启用排序
       sortOrder: cancelAuditList.order, //排序方式
       queryParams: cancelAuditList.queryParams,//传递参数（*）
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
       uniqueId: cancelAuditList.unique, //每一行的唯一标识，一般为主键列
       showToggle: true, //是否显示详细视图和列表视图的切换按钮
       cardView: false, //是否显示详细视图
       detailView: false, //是否显示父子表
       columns: cancelAuditList.columns(),
       onLoadError: function(status){
           ajaxError(status);
       }
   });
};

cancelAuditList.select = function (layerTips) {
   var rows = cancelAuditList.table.bootstrapTable('getSelections');
   if (rows.length > 0) {
        cancelAuditList.selectItems = rows;
        return true;
   } else {
        layerTips.msg("请选中一行");
        return false;
   }
};

cancelAuditList.getSelectIds = function (layerTips) {
    var rows = cancelAuditList.table.bootstrapTable('getSelections');

    var ids = [];
    for (i = 0; i < rows.length; i++) {
        var item = rows[i];
        ids.push(item.id);
    }
    return ids;
 };

cancelAuditList.refresh = function () {
    cancelAuditList.table.bootstrapTable('refresh', { pageNumber: 1 });
}
