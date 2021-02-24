var group = {
    baseUrl: "../../role",
    entity: "role",
    tableId: "groupTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    code: "id",
    parentCode: "parentId",
    rootValue: -1,
    explandColumn: 1,
    currentAuthorityMenu: {}
};
group.columns = function () {
    return [
        {
            field: 'selectItem',
            radio: true
        }, {
            field: 'name',
            title: '名称'
        }, {
            field: 'code',
            title: '编码'
        }, {
            field: 'level',
            title: '等级'
        }];
};
//得到查询的参数
group.queryParams = function () {
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        name: $("#name").val(),
    };
    return temp;
};
group.init = function () {
    group.table = $('#' + group.tableId).bootstrapTreeTable({
        id: group.unique,// 选取记录返回的值
        code: group.code,// 用于设置父子关系
        parentCode: group.parentCode,// 用于设置父子关系
        rootCodeValue: group.rootValue,
        url: group.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + group.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        ajaxParams: group.queryParams,//传递参数（*）
        expandColumn: group.explandColumn,//在哪一列上面显示展开按钮,从0开始
        expandAll: false,
        columns: group.columns(),
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return res.data;
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
    });
};
group.select = function (layerTips) {
    var rows = group.table.bootstrapTreeTable('getSelections');
    if (rows.length == 1) {
        group.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

group.refresh = function () {
    group.table.bootstrapTreeTable('refresh', group.queryParams());
};


layui.use(['form', 'layedit', 'laydate', 'element'], function () {
    group.init();
    var allItems = null;
    var editIndex;
    var allGroupItems = null;
    $.get(group.baseUrl + '/all', null, function (data) {
        allItems = data.data;
    });
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit;

    var element = layui.element();
    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        group.table.bootstrapTreeTable('refresh', group.queryParams());
    });
    var addBoxIndex = -1;
    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        var rows = group.table.bootstrapTreeTable('getSelections');
        var id = "-1";
        if (rows.length == 1) {
            id = rows[0].id;
        }
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加角色',
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
                    for (var i = 0; i < allItems.length; i++)
                        layero.find('#parentId').append('<option value="' + allItems[i].id + '" >' + allItems[i].name + '</option>');
                    form.on('submit(edit)', function (data) {
                        $.ajax({
                            url: group.baseUrl+"/",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (data) {
                                if(data.code == 'ACK'){
                                    layerTips.msg('保存成功');
                                    layerTips.close(index);
                                    location.reload();
                                }else{
                                    layerTips.msg(data.message);
                                }
                            }

                        })
                        //这里可以写ajax方法提交表单
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
        })
    });
    $('#btn_edit').on('click', function () {
        var rows = group.table.bootstrapTreeTable('getSelections');
        if (group.select(layerTips)) {
            var id = group.currentItem.id;
            $.get(group.baseUrl + '/' + id, null, function (data) {
                var result = data.data;
                $.get('edit.html', null, function (form) {
                    layer.open({
                        type: 1,
                        title: '编辑菜单',
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
                            setFromValues(layero, result);
                            // layero.find(":input[name='code']").attr("disabled", "");
                            form.render();

                            form.on('submit(edit)', function (data) {
                                $.ajax({
                                    url: group.baseUrl + '/' + result.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (data) {
                                        if(data.code == 'ACK'){
                                            layerTips.msg('保存成功');
                                            layerTips.close(index);
                                            location.reload();
                                        }else{
                                            layerTips.msg(data.message);
                                        }
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
        if (group.select(layerTips)) {
            var id = group.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: group.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            location.reload();
                        } else {
                            layerTips.msg("移除失败！ "+data.message);
                            location.reload();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    $('#btn_userManager').on("click", function () {
        if (group.select(layerTips)) {
            var id = group.currentItem.id;
            $.get('user.html', null, function (form) {
                var index = layer.open({
                    type: 1,
                    title: '添加用户',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '400px'],
                    maxmin: true,
                    yes: function (index) {
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    full: function (elem) {
                        var win = window.top === window.self ? window : parent.window;
                        $(win).on('resize', function () {
                            debugger;
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
                        // 获取人员
                        $.get(group.baseUrl + '/' + id + "/user", null, function (data) {
                            if (data.code !== 'ACK') {
                                layerTips.msg('获取数据异常！');
                                return;
                            }
                            var members = data.data;
                            var memOpts = "";
                            for (var i = 0; i < members.length; i++) {
                                // layero.find("#groupMember").append();
                                memOpts += '<option  value="' + members[i].id + '" selected>' + members[i].cname + '</option>';
                            }
                            // 加载人员
                            layero.find("#groupMember").append(memOpts).trigger('change');
                        });

                        form.on('submit(edit)', function (data) {
                            var vals = {};
                            var mems = layero.find("#groupMember").val();
                            if (mems)
                                vals.members = mems.join();
                            $.ajax({
                                url: group.baseUrl + '/' + id + "/user",
                                type: 'put',
                                data: vals,
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('更新成功');
                                    layer.close(index);
                                    // location.reload();
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                    }
                });
            });
        }
    });
    $('#btn_resourceManager').on("click", function () {
        if (group.select(layerTips)) {
            var id = group.currentItem.id;
            var nodeMap = {};
            $.get('authority.html', null, function (form) {
                var index = layer.open({
                    type: 1,
                    title: '分配权限',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '400px'],
                    maxmin: true,
                    yes: function (index) {
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
                        $.ajax({
                            type: "GET",
                            url: "../../permission/menu/authorityTree?roleId="+id,
                            success: function (defaultData) {
                                authorityElement.init();
                                var $checkableTree = $('#menuTreeview').treeview({
                                    data: defaultData.data,
                                    levels: 1,
                                    showIcon: false,
                                    showCheckbox: true,
                                    multiSelect: false,
                                    levels: 5,
                                    state: {
                                        checked: true,
                                        disabled: true,
                                        expanded: true,
                                        selected: true
                                    },
                                    onNodeUnchecked: function (event, data) {

                                        var selectNodes = treeViewHelper.getChildrenNodeIdArr(data);//获取所有子节点
                                        if (selectNodes) { //子节点不为空，则选中所有子节点
                                            $('#menuTreeview').treeview('uncheckNode', [selectNodes, {silent: true}]);
                                        }
                                    },
                                    onNodeChecked: function (event, data) {
                                        group.currentAuthorityMenu = data;
                                        var selectNodes = treeViewHelper.getChildrenNodeIdArr(data);//获取所有子节点
                                        if (selectNodes) {
                                            $('#menuTreeview').treeview('checkNode', [selectNodes, {silent: true}]);
                                        }
                                        var parNodes = treeViewHelper.getParentIdArr("menuTreeview", data);
                                        if (parNodes) {
                                            $('#menuTreeview').treeview('checkNode', [parNodes, {silent: true}]);

                                        }
                                    },
                                    onNodeSelected: function(event, data) {
                                        group.currentAuthorityMenu = data;
                                        authorityElement.refresh();
                                    } ,
                                    onNodeUnselected: function(event, data) {
                                        group.currentAuthorityMenu = {};
                                        authorityElement.refresh();
                                    }
//
                                });
                                var findCheckableNodess = function () {
                                    return $checkableTree.treeview('search', [
                                        $('#input-check-node').val(), {
                                            ignoreCase: false,
                                            exactMatch: false
                                        }]);
                                };
                                var checkableNodes = findCheckableNodess();

                                $('#input-check-node').on('keyup', function (e) {
                                    checkableNodes = findCheckableNodess();
                                    $('.check-node')
                                        .prop('disabled', !(checkableNodes.length >= 1));
                                });
                                // $.get(group.baseUrl + '/' + id + "/authority/menu", null, function (data) {
                                //     if (data.code === 'ACK') {
                                //         var nodes = $('#menuTreeview').treeview('getUnselected', 0);
                                //         var map = {};
                                //         for (var i = 0; i < nodes.length; i++) {
                                //             map[nodes[i].id] = nodes[i].nodeId;
                                //             nodeMap[nodes[i].nodeId] = nodes[i];
                                //         }
                                //         for (var i = 0; i < data.data.length; i++) {
                                //             var node = data.data[i];
                                //             //$('#menuTreeview').treeview('checkNode', [map[node.id], {silent: true}]);
                                //
                                //             // $('li[data-nodeid="'+map[node.id]+'"]').addClass("node-checked")
                                //             //     .find(".check-icon").removeClass("glyphicon-unchecked").addClass("glyphicon-check");
                                //
                                //             //$('#menuTreeview').treeview();
                                //         }
                                //     }
                                // });
                            }
                        });

                        form.on('submit(edit)', function (data) {
                            var menuList = [];
                            // layero.find('#menuTreeview li').each(function(){
                            //     if($(this).hasClass("list-group-item node-menuTreeview node-checked")){
                            //         var node = nodeMap[parseInt($(this).attr('data-nodeid'))];
                            //         if(node.state.checked){
                            //             menuList.push(node.id);
                            //         }
                            //     }
                            // });
                            var menuObjList = $('#menuTreeview').treeview('getChecked');
                            for (var i = 0; i < menuObjList.length; i++) {
                                menuList.push(menuObjList[i].id);
                            }
                            $.ajax({
                                url: group.baseUrl + '/' + id + "/authority/menu",
                                type: 'POST',
                                data: {"menuIds": menuList.join()},
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('更新成功');
                                    layer.close(index);
                                    // location.reload();
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });
                    },
                    end:function(){
                        group.currentAuthorityMenu = {};
                        element.currentItem = {};
                    }
                });
                layer.full(index);
            });
        }
    });
});
