var user = {
    baseUrl: "../../user",
    entity: "user",
    tableId: "userTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};

$.get("../../config/findByKey?configKey=pwdExpireCheck", null, function (data) {
    if (data == true) {  //开启密码有效期控制
        user.columns = function () {
            return [{
                radio: true
            }, {
                field: 'username',
                title: '用户名'
            }, {
                field: 'cname',
                title: '姓名'
            }, {
                field: 'roleName',
                title: '角色'
            }, {
                field: 'orgName',
                title: '所属机构'
            }, {
                field: 'enabled',
                title: '是否启用',
                formatter: function (value) {
                    return value ? "启用" : "禁用";
                }
            }, {
                field: 'pwdExpireDay',
                title: '密码有效期剩余天数'
            }];
        };
    } else {
        user.columns = function () {
            return [{
                radio: true
            }, {
                field: 'username',
                title: '用户名'
            }, {
                field: 'cname',
                title: '姓名'
            }, {
                field: 'roleName',
                title: '角色'
            }, {
                field: 'orgName',
                title: '所属机构'
            }, {
                field: 'enabled',
                title: '是否启用',
                formatter: function (value) {
                    return value ? "启用" : "禁用";
                }
            }];
        };
    }
})

user.queryParams = function (params) {
    if (!params)
        return {
            name: $("#name").val(),
            username : $("#username").val(),
            roleId: $("#roleId").val(),
            orgName: $("#orgName").val(),
            enabled: $("#enabled").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        name: $("#name").val(),
        username : $("#username").val(),
        roleId: $("#roleId").val(),
        orgName: $("#orgName").val(),
        enabled: $("#enabled").val()
    };
    return temp;
};
user.init = function () {

    user.table = $('#' + user.tableId).bootstrapTable({
        url: user.baseUrl + '/page', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + user.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: user.order, //排序方式
        queryParams: user.queryParams,//传递参数（*）
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
        uniqueId: user.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: user.columns(),
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
user.select = function (layerTips) {
    var rows = user.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        user.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    user.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;
    var addBoxIndex = -1;


    /*form.verify({
        pass: [
          /^[\S]{6}$/
          ,'密码必须6位，且不能出现空格'
        ] 
    }); */


    $.get("../../organization/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++)
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';
                            
            user.orgOptions = options;
        }
    });
    $.get("../../role/all", function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';
                $('#roleId').append('<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>');
            }
            form.render();
            user.roleOptions = options;
        }
    });


    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = user.queryParams();
        queryParams.pageNumber=1;
        user.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加用户',
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
                    
                    layero.find('#roleId').append(user.roleOptions);
                    layero.find('#orgId').append(user.orgOptions);
                    form.render('select');

                    form.on('submit(edit)', function (data) {
                        
                        $.ajax({
                            url: user.baseUrl+"/",
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
        if (user.select(layerTips)) {
            var id = user.currentItem.id;

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

                        layero.find('#pwd').hide();//隐藏密码
                        layero.find('#password').attr('lay-verify', 'pass');
                        layero.find('#roleId').append(user.roleOptions);
                        layero.find('#orgId').append(user.orgOptions);
                        form.render('select');

                        $.get(user.baseUrl + '/' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            layero.find('#password').val('******');
                            layero.find("select[name='orgId']").val(result.orgId);
                            layero.find("select[name='roleId']").val(result.roleId);
                            form.render();
                        });

                        layero.find(":input[name='username']").attr("disabled", "");
                        form.on('submit(edit)', function (data) {

                            if (data.field.password == '******') data.field.password ='';

                            if(user.currentItem.id) {
                                $.ajax({
                                    url: user.baseUrl + "/" + user.currentItem.id,
                                    type: 'post',
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


    // $('#btn_edit').on('click', function () {
    //     if (user.select(layerTips)) {
    //         var id = user.currentItem.id;
    //         $.get(user.baseUrl + '/' + id, null, function (data) {
    //             var result = data.data;
    //             $.get('edit.html', null, function (form) {
    //                 layer.open({
    //                     type: 1,
    //                     title: '编辑用户',
    //                     content: form,
    //                     btn: ['保存', '取消'],
    //                     shade: false,
    //                     offset: ['20px', '20%'],
    //                     area: ['600px', '500px'],
    //                     maxmin: true,
    //                     yes: function (index) {
    //                         //触发表单的提交事件
    //                         layedit.sync(editIndex);
    //                         $('form.layui-form').find('button[lay-filter=edit]').click();
    //                     },
    //                     full: function (elem) {
    //                         var win = window.top === window.self ? window : parent.window;
    //                         $(win).on('resize', function () {
    //                             var $this = $(this);
    //                             elem.width($this.width()).height($this.height()).css({
    //                                 top: 0,
    //                                 left: 0
    //                             });
    //                             elem.children('div.layui-layer-content').height($this.height() - 95);
    //                         });
    //                     },
    //                     success: function (layero, index) {
    //                         var form = layui.form();
    //                         setFromValues(layero, result);
    //                         form.render();

    //                         $.get("../../organization/all", function (res) {
    //                             if(res.code === 'ACK') {
    //                                 for (var i = 0; i < res.data.length; i++)
    //                                     layero.find('#orgId').append('<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>');
    //                                 layero.find("select[name='orgId']").val(result.orgId);
    //                                 form.render('select');
    //                             }
    //                         });
    //                         $.get("../../role/all", function (res) {
    //                             if(res.code === 'ACK') {
    //                                 for (var i = 0; i < res.data.length; i++)
    //                                     layero.find('#roleId').append('<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>');
    //                                 layero.find("select[name='roleId']").val(result.roleId);
    //                                 form.render('select');
    //                             }
    //                         });


    //                         layero.find(":input[name='username']").attr("disabled", "");
    //                         form.on('submit(edit)', function (data) {
    //                             $.ajax({
    //                                 url: user.baseUrl + "/" + result.id,
    //                                 type: 'put',
    //                                 data: data.field,
    //                                 dataType: "json",
    //                                 success: function (res) {
    //                                     if (res.code === 'ACK') {
    //                                         layerTips.msg('更新成功');
    //                                         layerTips.close(index);
    //                                         location.reload();
    //                                     } else {
    //                                         layerTips.msg(res.message);
    //                                     }
    //                                 }

    //                             });
    //                             //这里可以写ajax方法提交表单
    //                             return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    //                         });
    //                     }
    //                 });
    //             });
    //         });
    //     }
    // });

    $('#btn_del').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: user.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("移除成功！");
                            var queryParams = user.queryParams();
                            queryParams.pageNumber=1;
                            user.table.bootstrapTable('refresh', queryParams);
                        } else {
                            layerTips.msg("移除失败！"+data.message)
                            var queryParams = user.queryParams();
                            queryParams.pageNumber=1;
                            user.table.bootstrapTable('refresh', queryParams);
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    $('#btn_reset').on('click', function () {
        if (user.select(layerTips)) {
            var id = user.currentItem.id;
            layer.confirm('确定重置密码吗？', null, function (index) {
                $.ajax({
                    url: user.baseUrl + "/" + id+ "/reset",
                    type: "PUT",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layerTips.msg("重置密码成功！");
                            location.reload();
                        } else {
                            layerTips.msg("重置密码失败！"+data.message)
                        }
                    }
                });
                layer.close(index);
            });
        }
    });
    /*$('#btn_upload').on('click', function () {
        $('#file').click();
    });
    upload({
        url: user.baseUrl + '/upload',
        success: function(res) {
            if(res.code === 'ACK') {
                layerTips.msg("上传成功！");
                location.reload();
            } else {
                layerTips.msg(res.message);
            }
        }
    });*/
    $('#btn_upload').on('click', function () {
        layer.open({
            type : 1,
            title : '文件导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '300px', '200px' ], //宽高
            content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> </div>'
        });
        var uploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  user.baseUrl + '/upload', // 文件接收服务端
            pick: '#imgPicker', // 选择文件的按钮。可选
            /*fileNumLimit: 5,//一次最多上传五张
            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/!*'
            }*/
        });
        uploader.on("uploadAccept", function (file, res) {
            if (res.code === 'ACK') {
                return true;
            }
            return false;
        });
        uploader.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            location.reload();
        });
        uploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg(reason.message);
        });
    });

    $("#btn_download").on("click", function () {
        $("#templateDownloadForm").submit();
    });

    $("#btn_export").on("click", function () {
        //询问框
        layer.confirm('确定要导出该机构下的用户信息吗？', {
            btn: ['确定','取消'] //按钮
        }, function(index){
            window.location.href= user.baseUrl+"/export";
            layer.close(index);
        }, function(){
            //取消
        });

    });


    $("#btn_online").on("click", function () {
        parent.tab.tabAdd({
            href: 'online/list.html',
            icon: 'fa fa-calendar-times-o',
            title: '在线用户列表'
        });
    });

});