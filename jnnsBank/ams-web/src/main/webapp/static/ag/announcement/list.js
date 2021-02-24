
layui.config({
	base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
	baseUrl: "../ag/announcement/json",
	entity: "announcement",
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
		field: 'title',
		title: '标题'
	}, {
        field: 'username',
        title: '发布人'
    }, {
		field: 'id',
		title: '操作',
		formatter: function (value, row, index) {
			return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看</a>';
		}
	}];
};

list.queryParams = function (params) {
	if (!params)
		return {
			title: $.trim($("#title").val())
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		size: params.limit, //页面大小
		page: params.offset / params.limit, //页码
		title: $.trim($("#title").val())
	};
	return temp;
};

list.init = function () {

	list.table = $('#' + list.tableId).bootstrapTable({
		//url: list.baseUrl + '/list.json', //请求后台的URL（*）
        url: '../../announcement/page', //请求后台的URL（*）
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
		columns: list.columns(),
		responseHandler: function (res) {
			return {total:res.total, rows: res.rows};
			/*if (res.code === 'ACK') {

			} else {
				alert('查询失败');
				return false;
			}*/
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

layui.use(['form', 'layedit', 'laydate', 'loading'], function () {
	list.init();

	var editIndex;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
		layer = layui.layer, //获取当前窗口的layer对象
		form = layui.form(),
		layedit = layui.layedit,
		laydate = layui.laydate,
		loading = layui.loading;
    var addBoxIndex = -1;
    
    form.verify({
        title: function(value, item){ 
            if(value.length > 20){
                return '公告标题不能超过20个字';
            }
        },
        content: function(value, item){ 
            if(value.length > 150){
                return '公告内容不能超过150个字';
            }
        }
    });

	$('.date-picker').change(function(){
		var value = $(this).val();
		$(this).val(dateFormat(value));
	});

	$('#btn_query').on('click', function () {
		var queryParams = list.queryParams();
		queryParams.pageNumber=1;
		list.table.bootstrapTable('refresh', queryParams);
	});

	//新建
	$('#btn_add').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('edit.html', null, function (form) {
			console.log(form);
            addBoxIndex = layer.open({
                type: 1,
                title: '添加公告通知',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                offset: ['20px', '20%'],
                area: ['600px', '450px'],
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

                    bindUpload();

                    form.on('submit(edit)', function (data) {
                        
                        $.ajax({
                            // url: list.baseUrl+"/",
                            url: '../../announcement/save',
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

	//修改
    $('#btn_edit').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;

            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('edit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改公告通知',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['600px', '450px'],
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

                        bindUpload();

                        // $.get('../../ui/ag/announcement/json/detail.json?id=' + id, null, function (data) {
                        $.get('../../announcement/getOne?id=' + id, null, function (data) {
                            var result = data.data;
                            setFromValues(layero, result);
                            form.render('select');
                        });

                        form.on('submit(edit)', function (data) {

                            if(list.currentItem.id) {
                                $.ajax({
                                    // url: list.baseUrl + "/" + list.currentItem.id,
                                    url: '../../announcement/update?id=' + list.currentItem.id,
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

    //删除
    $('#btn_del').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    // url: list.baseUrl + "/" + id,
                    url: '../../announcement/delete?id=' + list.currentItem.id,
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

    //查看详情
	$('#list').on('click','.view', function () {
		var id = $(this).attr('data-id');
		
        parent.tab.tabAdd({
            title: '公告通知详情',
            href: 'announcement/detail.html?id=' + id
        });
        
		return false;
    });
    
    function bindUpload() {
        $('#btn_upload').on('click', function () {
            var index = layer.open({
                type : 1,
                title : '文件导入',
                skin : 'layui-layer-rim', //加上边框
                area : [ '300px', '200px' ], //宽高
                content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> </div>'
            });
            var uploader = WebUploader.create({
                auto: true, // 选完文件后，是否自动上传
                swf: '../js/webuploader/Uploader.swf', // swf文件路径
                // server:  list.baseUrl + '/upload', // 文件接收服务端
                server:  '../../announcement/upload', // 文件接收服务端
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
                    $("#attachmentId").val(res.data);
                    layer.close(index);
                    return true;
                }
                return false;
            });
            uploader.on( 'uploadSuccess', function( file, res ) {
                layerTips.msg("上传成功");
                //location.reload();
            });
            uploader.on( 'uploadError', function( file, reason ) {
                layerTips.msg('上传失败');
            });

            return false;
        });
    }
});
