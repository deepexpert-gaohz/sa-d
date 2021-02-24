layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../illegalQuery",
    entity: "illegalQuery",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'illegalbatchNo',
        title: '批次号'
    }, {
        field: 'batchDate',
        title: '处理时间'
    }, {
        field: 'fileName',
        title: '文件名'
    }, {
        field: 'fileSize',
        title: '文件大小(Byte)'
    }, {
        field: 'batchNum',//batchNum
        title: '数据量'
    }, {
        field: 'process',//batchNum
        title: '是否已完成',
        formatter: function (value, row, index) {
            return value ? '是' : '否'
        }
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            if(row.process){
                return '<a class="view" href="#" data-id="'+value+'">查看</a> ' +
                    '<a class="download" href="#" data-id="'+value+'" target="_self">下载</a>';
            }
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            illegalbatchNo: $.trim($("#illegalbatchNo").val()),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        illegalbatchNo: $.trim($("#illegalbatchNo").val()),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val()
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/queryBatchList', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + list.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list.order, //排序方式
        queryParams: list.queryParams,//传递参数（*）
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
        uniqueId: list.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list.columns()
        // onLoadError: function(status){
        //     ajaxError(status);
        // }
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

layui.use(['form', 'layedit', 'laydate', 'upload', 'loading'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload,
        loading = layui.loading;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh', queryParams);
        }
    });

    $('#btn_check').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;

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

                        form.render('select');

                        $.get(list.baseUrl + '/' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            layero.find('#password').val('******');
                            layero.find("select[name='orgId']").val(result.orgId);
                            layero.find("select[name='roleId']").val(result.roleId);
                            form.render('select');
                        });

                        layero.find(":input[name='username']").attr("disabled", "");
                        form.on('submit(edit)', function (data) {

                            if (data.field.password == '******') data.field.password ='';

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

    $('#btn_upload').on('click', function () {
        var result;

        var index = layer.open({
            type : 1,
            title : '文件导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '300px', '200px' ], //宽高
            content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> <div style="color: red; display: none" id="importText">导入中...</div> </div>'
        });

        var uploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  list.baseUrl + '/upload', // 文件接收服务端
            timeout: 0,
            pick: '#imgPicker', // 选择文件的按钮。可选
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                // mimeTypes: 'application/vnd.ms-excel'
            }
        });

        uploader.on("uploadProgress", function (file, percentage) {
            $('#importText').show();
        });
        uploader.on( 'uploadSuccess', function( file, response ) {
            $.ajaxSettings.async = false;

            result = response;
            if(result.code == 'NACK') {
                return;
            }

            layer.open({
                title: '上传结果'
                ,content: response.message +"&nbsp;&nbsp; 批量校验企业违法信息-----开始"
            });
            // layerTips.msg(response.message);
            layer.close(index);
            var queryParams = list.queryParams();
            queryParams.pageNumber=1;
            list.table.bootstrapTable('refresh', queryParams);

            $.ajaxSettings.async = true;
        });

        // 文件上传结束后执行
        uploader.on( 'uploadFinished', function( file ) {
            if(!isEmpty(result)) {
                if (result.code == "NACK") {
                    $('#importText').hide();
                    layer.open({
                        title: '上传结果'
                        ,content: result.message
                    });
                } else {
                    if (result.code) {
                        $.ajax({
                            url: list.baseUrl + '/illegaListCheck?batchId=' + result.code,
                            type: 'GET',
                            success: function (data) {
                                $.ajax({
                                    url: list.baseUrl + '/checkIllegalExpired?batchId=' + result.code,
                                    type: 'GET',
                                    success: function (data) {
                                        if(data){
                                            console.log("批量校验企业违法信息结束,并且该批次存在营业期限到期企业！可通过营业执照到期状态进行查询。")
                                            layer.alert("批量校验企业违法信息结束,并且该批次存在营业期限到期企业！可通过营业执照到期状态进行查询。");
                                        }else{
                                            console.log("批量校验企业违法信息------结束")
                                            layer.alert("批量校验企业违法信息------结束");
                                        }

                                    }

                                });
                                // if(data){
                                //     console.log("批量校验企业违法信息结束,并且该批次存在营业期限到期企业！可通过营业执照到期状态进行查询。")
                                //     layer.alert("批量校验企业违法信息结束,并且该批次存在营业期限到期企业！可通过营业执照到期状态进行查询。");
                                // }else{
                                //     console.log("批量校验企业违法信息------结束")
                                //     layer.alert("批量校验企业违法信息------结束");
                                // }

                            }

                        });
                    }
                }
            }
        });

        uploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        uploader.on( 'error', function( handler ) {
            if(handler == 'Q_TYPE_DENIED' ){
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            }else{
                layerTips.msg(handler);
            }
        });
    });

    $("#btn_download").on("click", function () {
        $("#downloadFrame").prop('src', "../attach/illegalQuery_template.xls");
        return false;
    });

    $('#list').on('click','.view', function () {
        var id = $(this).attr('data-id');

        parent.tab.tabAdd({
            title: '查看' + id,
            href: '../ui/illegal/detail.html?illegalQueryBatchId=' + id
        });
        return false;
    });

    $('#list').on('click','.download', function () {
        var id = $(this).attr('data-id');

        $.get(list.baseUrl + '/checkIllegalStatus?batchId=' + id,function(data) {
            if(data.rel == false) {
                layer.confirm('正在进行批量企业违法信息查询，确认下载吗？', null, function (index) {
                    layer.close(index);
                    $("#downloadFrame").prop('src', list.baseUrl + '/download?illegalQueryBatchId=' + id);

                });
            } else {
                $("#downloadFrame").prop('src', list.baseUrl + '/download?illegalQueryBatchId=' + id);
            }

        });

        return false;
    });
});