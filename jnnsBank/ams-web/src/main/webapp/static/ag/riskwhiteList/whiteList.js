var whiteList = {
    baseUrl: "../../whiteList",
    entity: "whiteList",
    tableId: "riskWhiteListTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},

};
whiteList.columns = function () {
    return [{
        radio: true
    }, {
        field: 'accountId',
        title: '账户号'
    }, {
        field: 'accountName',
        title: '账户名称'
    }, {
        field: 'socialUnifiedCode',
        title: '社会统一编码'
    }];
};

whiteList.queryParams = function (params) {
    if (!params)
        return {
            accountId: $("#accountId").val(),
            accountName : $("#accountName").val(),
            socialUnifiedCode : $("#socialUnifiedCode").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        accountId: $("#accountId").val(),
        accountName : $("#accountName").val(),
        socialUnifiedCode : $("#socialUnifiedCode").val()
    };
    return temp;
};
whiteList.init = function () {
    whiteList.table = $('#' + whiteList.tableId).bootstrapTable({
        url: whiteList.baseUrl + '/searchWhiteList', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + whiteList.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: whiteList.order, //排序方式
        queryParams: whiteList.queryParams,//传递参数（*）
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
        uniqueId: whiteList.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: whiteList.columns(),
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
whiteList.select = function (layerTips) {
    var rows = whiteList.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        whiteList.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    whiteList.init();
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
        var queryParams = whiteList.queryParams();
        queryParams.pageNumber=1;
        whiteList.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '添加风险白名单',
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
                    /*layero.find('#levelId').append(whiteList.levelIdOptions);
                    layero.find('#typeId').append(whiteList.typeIdOptions);
                    layero.find('#ruleId').append(whiteList.ruleIdOptions);*/
                    form.render();

                    form.on('submit(edit)', function (data) {
                        $.ajax({
                            url: whiteList.baseUrl+"/saveWhiteList",
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
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
        });
    });
    $('#btn_edit').on('click', function (){
        if (whiteList.select(layerTips)) {
            var id = whiteList.currentItem.id;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改风险风险白名单',
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

                        $.get(whiteList.baseUrl + '/' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            layero.find("select[name='accountId']").val(result.accountId);
                            layero.find("select[name='accountName']").val(result.accountName);
                            layero.find("select[name='socialUnifiedCode']").val(result.socialUnifiedCode);
                            form.render();
                        });

                        form.on('submit(edit)', function (data) {
                            if(whiteList.currentItem.id) {
                                $.ajax({
                                    url: whiteList.baseUrl + "/" + whiteList.currentItem.id,
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
    $('#btn_del').on('click', function () {
        if (whiteList.select(layerTips)) {
            var id = whiteList.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: whiteList.baseUrl + "/" + id,
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
    $('#btn_risk_upload').on('click', function () {
        layer.open({
            type : 1,
            title : '风险白名单导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '300px', '200px' ], //宽高
            content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">请选择文件</div> </div>'
        });
        var uploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            method: 'POST',
            chunked: true,
            chunkRetry: 10, // 如果某个分片由于网络问题出错，允许自动重传多少次？
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  whiteList.baseUrl + '/uploadRiskWhite', // 文件接收服务端
            pick: '#imgPicker', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx'
            }
        });
        uploader.on( 'uploadSuccess', function( file, res ) {
            layerTips.msg(res.message);
            location.reload();
        });
        uploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
    });
    //导出模板
    $("#btn_risk_downLoad").on("click", function () {
        $("#templateDownloadForm").submit();
    });
});