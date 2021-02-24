var org = {
    baseUrl: "../../organization",
    entity: "organization",
    treeId: 'orgTree',
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    code: "id",
    parentCode: "parentId",
    rootValue: -1,
    explandColumn: 1,
    isQuerying: false,
    isInit: true
};

org.columns = function () {
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
org.queryParams = function () {
    var temp = {};
    return temp;
};
org.init = function () {

    if ($.jstree.reference($("#" + org.treeId))) {
        $("#" + org.treeId).jstree().delete_node($("#" + org.treeId).jstree().get_json());
    }
    $("#" + org.treeId).jstree('destroy');

    $("#" + org.treeId).jstree({
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
                    return org.baseUrl + '/orgTree';
                },
                type: 'GET',
                data: function (node) {
                    if (org.isQuerying) {
                        return {'name': $('#title').val(), 'code': $('#code').val()};
                    } else {
                        return {'parentId': node.id === '#' ? '' : node.id};
                    }
                },
                dataFilter: function (nodes) {
                    var nodesArray = JSON.parse(nodes).data;
                    var data = [];
                    for (var i = 0; i < nodesArray.length; i++) {
                        var state = ' <span class="label label-warning">取消核准</span>';

                        var node = {
                            //text: nodesArray[i].name + state,
                            id: nodesArray[i].id,
                            children: nodesArray[i].childs ? true : false,
                            data: nodesArray[i]
                        };
                        if(nodesArray[i].cancelHeZhun == true){
                            node.text = nodesArray[i].name + state;
                        }else{
                            node.text = nodesArray[i].name;
                        }
                        if (!org.isQuerying) {
                            if (!org.isInit) {
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
                    if (org.isQuerying) {
                        org.isQuerying = false;
                    }

                    //初始化标志
                    if (org.isInit) {
                        org.isInit = false;
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
        "plugins": ["state", "types", "wholerow"]
    }).on("loaded.jstree", function(event, data) {
        data.instance.clear_state(); // <<< 这句清除jstree保存的选中状态
    });

    $("#" + org.treeId).on("select_node.jstree", function (e, data) {
        org.currentItem = data.node.data;
        user.refresh();
    });
};
org.select = function (layerTips) {
    if (!org.currentItem) {
        layerTips.msg("请选中一行");
        return false;
    }
    return true;
};

org.refresh = function () {
    org.isInit = true;
    $("#" + org.treeId).jstree(true).refresh();
};

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

layui.use(['form', 'layedit', 'laydate', 'picker', 'upload'], function () {

    $.get(org.baseUrl + '/root', null, function (data) {
        if (data) {
            org.init();
        }

        var editIndex;

        var orgSyncFlag;

        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form,
            layedit = layui.layedit,
            picker = layui.picker,
            upload = layui.upload;


        $.ajaxSettings.async = false;

        $.ajax({
            url: org.baseUrl + "/orgSyncFlag",
            type: 'get',
            dataType: "json",
            success: function (data) {
                console.log(data)
                orgSyncFlag=data.data;
            }
        });

        $.ajaxSettings.async = true;

        if(!orgSyncFlag){
            $("#btn_orgSync").hide();
        }
        //初始化页面上面的按钮事件
        $('#btn_refresh').on('click', function () {
            $('#title').val('');
            $('#code').val('');
            org.isInit = true;
            org.init();
        });
        $('#btn_query').on('click', function () {
            // if ($.trim($('#title').val()) === '') {
            //     layerTips.msg("请输入查询组织");
            //     return;
            // } else {
                //查询组织
                org.isQuerying = true;
                org.init();
            // }

        });
        var addBoxIndex = -1;

        orgSyncHide = function(){
            if(orgSyncFlag){//需要显示全部
                $(".orgSyncClass").show();
            }else{
                $(".orgSyncClass").hide();
            }
        }

        $('#btn_add').on('click', function () {
            if (addBoxIndex !== -1)
                return;

            if (!org.currentItem.id) {
                layerTips.msg("请选中一行");
                return false;
            }

            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '添加机构',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['800px', '450px'],
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
                        orgSyncHide();
                        //初始化省
                        var p2 = new picker();
                        p2.set({
                            elem: '#addressDist',
                            provinceElementName: 'province', //省份element的"name"
                            cityElementName: 'city', //城市element的"name"
                            areaElementName: 'area',  //区域element的"name"
                            codeConfig: {
                                type: 3
                            }
                        }).render();

                        form.verify({
                            pbccode: [
                                /^\d{12}$/
                                ,'请输入正确的人行机构代码'
                            ],
                            notRequiredPhone: function (value, item) {
                                if (value === undefined || value === '') {
                                    return;
                                }
                                var mobiles = value.split(",");
                                for(var i=0; i<mobiles.length;i++){
                                    var mobile = mobiles[i];
                                    if (!/^1\d{10}$/.test(mobile)) {
                                        return "请输入正确的手机号";
                                    }
                                }
                            }
                        });
                        form.render();
                        layero.find("#parentId").val(org.currentItem.id);

                        form.on('submit(edit)', function (data) {
                            $.ajax({
                                url: org.baseUrl + "/",
                                type: 'post',
                                data: data.field,
                                dataType: "json",
                                success: function (data) {
                                    console.log(data)
                                    if (data.code === 'ACK') {
                                        layerTips.msg('保存成功');
                                        layer.close(addBoxIndex);
                                        org.refresh();
                                    } else {
                                        layerTips.msg(data.message);
                                    }
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
        });

        $('#btn_edit').on('click', function () {
            if (addBoxIndex !== -1)
                return;
            if (!org.currentItem.id) {
                layerTips.msg("请选中一行");
                return false;
            }

            $.get(org.baseUrl + '/' + org.currentItem.id, null, function (data) {
                var result = data.data;
                $.get('edit.html', null, function (form) {
                    addBoxIndex = layer.open({
                        type: 1,
                        title: '修改机构',
                        content: form,
                        btn: ['保存', '取消'],
                        shade: false,
                        offset: ['20px', '20%'],
                        area: ['800px', '450px'],
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
                            orgSyncHide();
                            setFromValues(layero, result);
                            //初始化省
                            var p2 = new picker();
                            p2.set({
                                elem: '#addressDist',
                                // elem: '#regAddressDiv',
                                provinceElementName: 'province', //省份element的"name"
                                cityElementName: 'city', //城市element的"name"
                                areaElementName: 'area',  //区域element的"name"
                                codeConfig: {
                                    code: result.area,
                                    type: 3
                                }
                            }).render();
                            // layero.find(":input[name='code']").attr("disabled", "");
                            form.verify({
                                pbccode: [
                                    /^\d{12}$/
                                    ,'请输入正确的人行机构代码'
                                ],
                                notRequiredPhone: function (value, item) {
                                    if (value === undefined || value === '') {
                                        return;
                                    }
                                    var mobiles = value.split(",");
                                    for(var i=0; i<mobiles.length;i++){
                                        var mobile = mobiles[i];
                                        if (!/^1\d{10}$/.test(mobile)) {
                                            return "请输入正确的手机号";
                                        }
                                    }
                                }
                            });
                            form.render();

                            form.on('submit(edit)', function (data) {
                                $.ajax({
                                    url: org.baseUrl + '/' + result.id,
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg('更新成功');
                                            layer.close(index);
                                            org.refresh();
                                        } else {
                                            layerTips.msg(data.message);
                                        }
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
            });

        });
        $('#btn_del').on('click', function () {
            if (org.select(layerTips)) {
                var id = org.currentItem.id;
                layer.confirm('确定删除数据吗？', null, function (index) {
                    $.ajax({
                        url: org.baseUrl + "/" + id,
                        type: "DELETE",
                        success: function (data) {
                            console.log(data);
                            if (data.code === 'ACK') {
                                layerTips.msg("移除成功！");
                                location.reload();
                            } else {
                                if (data.message != undefined) {
                                    layerTips.msg(data.message);
                                } else {
                                    layerTips.msg("移除失败！");
                                }
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
            url: org.baseUrl+'/upload',
            success: function (res) {
                if (res.code === 'ACK') {
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
                server:org.baseUrl+'/upload', // 文件接收服务端
                pick: '#imgPicker', // 选择文件的按钮。可选
                /*fileNumLimit: 5,//一次最多上传五张
                // 只允许选择图片文件。
                accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/!*'
                }*/
            });
            uploader.on( 'uploadSuccess', function( file, res ) {
                layerTips.msg(res.message);
                location.reload();
            });
            uploader.on( 'uploadError', function( file, reason ) {
                layerTips.msg('上传失败');
            });
        });

        $("#btn_download").on("click", function () {
            $("#templateDownloadForm").submit();
        });

        $('#btn_upload_account').on('click', function () {
            layer.open({
                type : 1,
                title : '文件导入',
                skin : 'layui-layer-rim', //加上边框
                area : [ '300px', '200px' ], //宽高
                content : '<div id="uploadAccount"> <div id="accountList" class="uploader-list"></div> <div id="accountPicker">选择文件</div> </div>'
            });
            var uploader = WebUploader.create({
                auto: true, // 选完文件后，是否自动上传
                swf: '../js/webuploader/Uploader.swf', // swf文件路径
                server:org.baseUrl+'/uploadAccount', // 文件接收服务端
                pick: '#accountPicker' // 选择文件的按钮。可选
            });
            uploader.on( 'uploadSuccess', function( file, res ) {
                layerTips.alert(res.message);
                location.reload();
            });
            uploader.on( 'uploadError', function( file, reason ) {
                layerTips.msg('上传失败');
            });
        });

        $("#btn_download_account").on("click", function () {
            $("#templateDownloadAccountForm").submit();
        });

        $('#btn_move').on('click', function () {
            if (addBoxIndex !== -1)
                return;
            if (!org.currentItem.id) {
                layerTips.msg("请选中一行");
                return false;
            }
            //
            // if (org.currentItem.isRoot) {
            //     layerTips.msg('不允许修改本机构信息！');
            //     return false;
            // }

            $.get(org.baseUrl + '/' + org.currentItem.id, null, function (data) {
                var result = data.data;
                $.get('move.html', null, function (form) {
                    addBoxIndex = layer.open({
                        type: 1,
                        title: '机构迁并管理',
                        content: form,
                        btn: ['保存', '取消'],
                        shade: false,
                        offset: ['20px', '20%'],
                        area: ['800px', '420px'],
                        maxmin: true,
                        yes: function (index) {
                            //layedit.sync(editIndex);
                            //触发表单的提交事件
                            $('form.layui-form').find('button[lay-filter=move]').click();
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

                            $.get("../../organization/all", function (res) {
                                if(res.code === 'ACK') {
                                    var options = '';
                                    for (var i = 0; i < res.data.length; i++) 
                                        options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';
                                    
                                    layero.find('#fromOrgId').html(options);
                                    layero.find('#toOrgId').html(options);
                                    layero.find('#fromOrgId').val(org.currentItem.id);
                                    
                                    form.render('select');
                                }
                            });

                            
                            // $('#moveType').attr('lay-filter','moveType');
                            // form.on('select(moveType)',function (data) {
                            //     if (data.value == '0') {
                            //         $('#excelTemplate').hide();
                            //         $('#toOrg').show();
                            //         $('#dataAll').show();
                            //         $('#toMoveData').hide();
                            //         $('#toMoveTemplate').hide();
                            //         $('#toMoveData').val('0').prop('disabled',true);
                            //         $('#containOrgan').val('0').prop('disabled',true);
                            //     } else if (data.value == '1') {
                            //         $('#excelTemplate').hide();
                            //         $('#toOrg').show();
                            //         $('#dataAll').show();
                            //         $('#toMoveData').show();
                            //         $('#toMoveTemplate').show();
                            //         $('#moveTemplate').val('0').prop('disabled',true);
                            //         //$('#moveData').val('0').prop('disabled',true);
                            //     }
                            //     form.render('select');
                            // });

                            //迁移类型
                            $('#moveData').attr('lay-filter','moveData');
                            form.on('select(moveData)',function (data) {
                                if (data.value == '0') {
                                    $('#moveTemplate').val('0').prop('disabled',true);
                                }else{
                                    $('#toMoveTemplate').show();
                                    $('#moveTemplate').val('0').prop('disabled',false);
                                }
                                form.render('select');
                            });

                            //迁移类型
                            $('#moveTemplate').attr('lay-filter','moveTemplate');
                            form.on('select(moveTemplate)',function (data) {
                                if (data.value == '0') {
                                    $('#toOrg').show();
                                    $('#excelTemplate').hide();
                                }else{
                                    $('#excelTemplate').show();
                                    $('#toOrg').hide();
                                    $('#dataAll').hide();
                                }
                                form.render('select');
                            });

                            $('#excelDownload').on('click', function () {
                                var moveTemplate = $('#moveTemplate').val();
                                //window.location.href = '../attach/org_template.xls';
                                // window.location.href = org.baseUrl + '/downLoadDataExcel?moveTemplate='+moveTemplate+'&fromOrgId='+$('#fromOrgId').val();
                                $("#downloadFrame").prop('src', org.baseUrl + '/downLoadDataExcel?moveTemplate='+moveTemplate+'&fromOrgId='+$('#fromOrgId').val());
                                return false;
                            });
                            
                            $('#excelUpload').on('click', function () {
                                var moveTemplate = $('#moveTemplate').val();
                                layer.open({
                                    type : 1,
                                    title : '迁移上传',
                                    skin : 'layui-layer-rim', //加上边框
                                    area : [ '300px', '200px' ], //宽高
                                    content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="excelPicker">选择文件</div> </div>'
                                });
                                var uploader = WebUploader.create({
                                    auto: true, // 选完文件后，是否自动上传
                                    swf: '../js/webuploader/Uploader.swf', // swf文件路径
                                    server:org.baseUrl+'/moveOrganUpload?moveTemplate='+moveTemplate, // 文件接收服务端
                                    pick: '#excelPicker', // 选择文件的按钮。可选
                                });
                                uploader.on( 'uploadSuccess', function( file, res ) {
                                    layerTips.alert(res.message);
                                    location.reload();
                                });
                                uploader.on( 'uploadError', function( file, reason ) {
                                    layerTips.msg('上传失败');
                                });
                                return false;
                            });

                            form.on('submit(move)', function (data) {
                                $.ajax({
                                    url: org.baseUrl + '/move/',
                                    type: 'put',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layerTips.msg('迁移成功');
                                            layer.close(addBoxIndex);
                                            org.refresh();
                                        } else {
                                            layerTips.msg(data.message);
                                        }
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
            });
        });

        $('#btn_orgSync').on('click', function () {
            $.get('orgSyncRecord.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '机构同步记录',
                    content: form,
                    btn: ['关闭'],
                    shade: false,
                    offset: ['10px', '10%'],
                    area: ['860px', '450px'],
                    maxmin: true,
                    success: function (layero, index) {
                        orgSync.init();
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
            });
        });

    });

})
;
