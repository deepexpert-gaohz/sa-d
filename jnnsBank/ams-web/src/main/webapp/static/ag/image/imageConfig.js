layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'laydate'], function () {
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        laydate = layui.laydate;

    var imageType = {
        baseUrl: "../../imageType",
        entity: "ImageType",
        tableId: "imageConfigTable",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };
    imageType.columns = function () {
        return [{
            radio: true
        },{
            field: 'id',
            title: 'ID',
            visible: false
        },{
            field: 'operateType',
            title: '操作类型',
            formatter: function (value, row, index) {
                return changeBillType(value);
            }
        },{
            field: 'acctType',
            title: '账户性质',
            'class': 'W120',
            formatter: function (value, row, index) {
                return changeAcctType(value)
            }
        },{
            field: 'depositorType',
            title: '存款人性质'
        },{
            field: 'imageName',
            title: '证件种类'
        },{
            field: 'choose',
            title: '是否必选',
            formatter:function (value, row, index){
                return isNotNull(value);
            }
        }];
    }
    imageType.queryParams = function (params) {
        if (!params)
            return {
                imageName: $("#imageName").val()
            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit, //页面大小
            offset: (params.offset / params.limit)+1, //页码
            imageName: $("#imageName").val()
        };
        return temp;
    };
    imageType.init = function () {

        imageType.table = $('#' + imageType.tableId).bootstrapTable({
            url: imageType.baseUrl+"/query", //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + imageType.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: imageType.queryParams,//传递参数（*）
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
            uniqueId: imageType.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: imageType.columns()
            // ,
            // onDblClickRow:function (items,$element) {
            //     var applyId = items.applyid;
            //     var name = items.name;
            //     parent.tab.tabAdd({
            //         title: '开户-'+name,
            //         href: 'bank/view.html?applyId='+applyId+'&name='+encodeURI(name)
            //     });
            // }
            ,onLoadError: function(status){
                ajaxError(status);
            }
        });
    };
    imageType.select = function (layerTips) {
        var rows = imageType.table.bootstrapTable('getSelections');
        if (rows.length == 1) {
            imageType.currentItem = rows[0];
            return true;
        } else {
            layerTips.msg("请选中一行");
            return false;
        }
    };
    layui.use(['form', 'layedit', 'laydate'], function () {
        imageType.init();
        var editIndex;
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form(),
            layedit = layui.layedit,
            laydate = layui.laydate;
        var addBoxIndex = -1;
        //初始化页面上面的按钮事件
        $('#btn_query').on('click', function () {
            var queryParams = imageType.queryParams();
            queryParams.pageNumber=1;
            imageType.table.bootstrapTable('refresh', queryParams);
        });
        $('#btn_del').on('click', function () {
            if (imageType.select(layerTips)) {
                var id = imageType.currentItem.id;
                layer.confirm('确定删除数据吗？', null, function (index) {
                    $.ajax({
                        url: imageType.baseUrl + "/delete/" + id,
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
        $('#btn_add').on('click', function () {
            if (addBoxIndex !== -1)
                return;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('imageTypeEdit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '添加影像名称',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['700px', '400px'],
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
                        $.get("../../imageType/getImageOption?name=imageOption", function (res) {
                            if(res.code === 'ACK') {
                                for (var i = 0; i < res.data.length; i++)
                                    layero.find('#value').append('<option value="' + res.data[i].value + '" >' + res.data[i].name + '</option>');
                                form.render('select');
                            }
                        });
                        $.get("../../imageType/getDepositorTypeCode", function (res) {
                            if(res.code === 'ACK') {
                                for (var i = 0; i < res.data.length; i++)
                                    layero.find('#depositorTypeCode').append('<option value="' + res.data[i].value + '" >' + res.data[i].name + '</option>');
                                form.render('select');
                            }
                        });
                        form.on('submit(edit)', function (data) {

                            if (data.field.operateType==-1){
                                layerTips.msg("请输入操作类型！");
                                return false;
                            }else if(data.field.acctType==-1){
                                layerTips.msg("请输入账户性质！");
                                return false;
                            }

                            //基本户开户：存款人必填
                            if (data.field.operateType=="ACCT_OPEN" && data.field.acctType=="jiben"){
                                //layerTips.msg("存款人性质必填！");
                                if (data.field.depositorTypeCode==-1){
                                    layerTips.msg("请输入存款人性质！");
                                    return false;
                                }
                            }

                            //特殊户开户：存款人必填
                            if (data.field.operateType=="ACCT_OPEN" && data.field.acctType=="teshu") {
                                if (data.field.depositorTypeCode==-1){
                                    layerTips.msg("请输入存款人性质！");
                                    return false;
                                }
                            }


                            if(data.field.value==-1){
                                layerTips.msg("请输入证件种类！");
                                return false;
                            }else {
                                $.ajax({
                                    url: imageType.baseUrl+"/save",
                                    type: 'post',
                                    data: data.field,
                                    dataType: "json",
                                    success: function () {
                                        layerTips.msg('保存成功');
                                        layerTips.close(index);
                                        location.reload();
                                    },
                                    error:function(data2){
                                        layerTips.msg(data2.responseJSON.message);
                                    }

                                });
                            }

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
            if (imageType.select(layerTips)) {
                var id = imageType.currentItem.id;
                $.get(imageType.baseUrl + '/' + id, null, function (data) {
                    var result = data.data;
                    $.get('imageTypeEdit.html', null, function (form) {
                        layer.open({
                            type: 1,
                            title: '编辑影像名称',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            offset: ['20px', '20%'],
                            area: ['700px', '400px'],
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
                                //setFromValues(layero, result);
                                $("#operateType").val(result.operateType);
                                $("#acctType").val(result.acctType);
                                $(":radio[name='choose'][value='" +  result.choose + "']").prop("checked", "checked");
                                form.render();


                                $.get("../../imageType/getImageOption?name=imageOption", function (res) {
                                    if(res.code === 'ACK') {
                                        for (var i = 0; i < res.data.length; i++)
                                            layero.find('#value').append('<option value="' + res.data[i].value + '" >' + res.data[i].name + '</option>');
                                        $("#value").val(result.value);
                                        form.render('select');
                                    }
                                });
                                $.get("../../imageType/getDepositorTypeCode", function (res) {
                                    if(res.code === 'ACK') {
                                        for (var i = 0; i < res.data.length; i++)
                                            layero.find('#depositorTypeCode').append('<option value="' + res.data[i].value + '" >' + res.data[i].name + '</option>');
                                        $("#depositorTypeCode").val(result.depositorTypeCode);
                                        form.render('select');
                                    }
                                });
                                //layero.find(":input[name='username']").attr("disabled", "");
                                form.on('submit(edit)', function (data) {
                                    $.ajax({
                                        url: imageType.baseUrl + "/" + result.id,
                                        type: 'put',
                                        data: data.field,
                                        dataType: "json",
                                        success: function () {
                                            layerTips.msg('更新成功');
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
    });
});