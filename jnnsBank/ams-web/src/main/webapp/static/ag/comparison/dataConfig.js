layui.config({
  	base: '../js/' //假设这是你存放拓展模块的根目录
});

var list1 = {
	baseUrl: "../../compareDataSource",
	entity: "list1",
	tableId: "list1",
	toolbarId: "toolbar1",
	unique: "id",
	order: "asc",
	currentItem: {},
	collectTypeOptions: ''
};
list1.columns = function () {
	return [{
        radio: true
    }, {
		field: 'name',
		title: '数源名称'
	}, {
		field: 'createdBy',
		title: '创建人'
	}, {
		field: 'createdDateStr',
		title: '创建时间'
	}, {
		field: 'collectType',
		title: '来源方式',
		formatter: function (value) {
		    if(value=="IMPORT"){
                return "手动导入";
            }
            if(value=="ONLINE"){
                return "在线采集";
            }

		}
	}];
};

list1.queryParams = function (params) {
	if (!params)
		return {
			name: $.trim($("#dataName").val()),
			beginDateStr: $("#dataBeginDate").val(),
			endDateStr: $("#dataEndDate").val(),
            collectType: $("#_collectType").val(),
            createdBy: $("#createdBy").val()
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		size: params.limit, //页面大小
		page: params.offset / params.limit, //页码
		name: $.trim($("#dataName").val()),
		beginDateStr: $("#dataBeginDate").val(),
		endDateStr: $("#dataEndDate").val(),
        collectType: $("#_collectType").val(),
        createdBy: $("#createdBy").val()
	};
	return temp;
};

list1.init = function () {

	list1.table = $('#' + list1.tableId).bootstrapTable({
		url: list1.baseUrl + '/list', //请求后台的URL（*）
		method: 'get', //请求方式（*）
        toolbar: '#' + list1.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list1.order, //排序方式
        queryParams: list1.queryParams,//传递参数（*）
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
        uniqueId: list1.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list1.columns(),
		onLoadError: function(status){
			ajaxError(status);
		}
	});
};

list1.select = function (layer) {
	var rows = list1.table.bootstrapTable('getSelections');
	if (rows.length == 1) {
		list1.currentItem = rows[0];
		return true;
	} else {
		layer.msg("请选中一行");
		return false;
	}
};

list1.refresh = function () {
    var queryParams = list1.queryParams();
    queryParams.pageNumber=1;
    list1.table.bootstrapTable('refresh', queryParams);
}

layui.use(['common', 'element', 'form', 'layedit', 'laydate', 'loading'], function () {
	list1.init();

	var editIndex;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        common = layui.common,
        layer = layui.layer, //获取当前窗口的layer对象
        element = layui.element(),
		form = layui.form(),
		layedit = layui.layedit,
		laydate = layui.laydate,
		loading = layui.loading;

    //初始化日期控件
    var laydateData =  beginEndDateInit(laydate, 'dataBeginDate', 'dataEndDate', 'YYYY-MM-DD', false);

	var addBoxIndex = -1;

	$('.date-picker').change(function(){
		var value = $(this).val();
		$(this).val(dateFormat(value));
	});

	$.get("../../compareField/", function (res) {
        /*if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++)
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';

			list1.collectTypeOptions = options;
        }*/
    });

    $('#tab1').on('click', function () {
        list1.refresh();
        layer.closeAll();
    });

	$('#btn_search1').on('click', function () {
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		if(beginDate && endDate && beginDate > endDate) {
			layer.msg("时间筛选开始时间不能大于结束时间");
		} else {
			var queryParams = list1.queryParams();
			queryParams.pageNumber=1;
			list1.table.bootstrapTable('refresh', queryParams);
		}
	});

	$('#btn_add1').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('dataEdit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '新建数据源',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                area: ['450px', '480px'],
                maxmin: true,
                yes: function (index) {
                    layedit.sync(editIndex);
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
                success: function (layero, index) {
                    var form = layui.form();
					form.render();

                    $('.dataNo').hide();
                    $('.timingForEveryday').hide();

                    boxInit(form);//初始化日期控件及新增修改弹出框公共js

                    form.on('submit(edit)', function (data) {
                        var submitData = submitDataFormat(data.field);
                        if (submitData.collectType === "IMPORT") {
                            submitData.dataType = "OTHER";
                        }
                        $.ajax({
                            url: list1.baseUrl+"/",
                            type: 'post',
                            data: submitData,
                            dataType: "json",
                            success: function (res) {
                                if (res.code === 'ACK') {
                                    layer.msg('保存成功');
                                    layer.close(index);
                                    list1.refresh();
                                } else {
                                    layer.msg(res.message);
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

	$('#btn_edit1').on('click', function () {
		if (addBoxIndex !== -1)
            return;
        if (list1.select(layer)) {
            var id = list1.currentItem.id;

            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('dataEdit.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '修改数据源',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    area: ['450px', '480px'],
                    maxmin: true,
                    yes: function (index) {
                        layedit.sync(editIndex);
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
                    success: function (layero, index) {
						var form = layui.form();
						form.render();

                        $('.dataNo').hide();
                        $('.timingForEveryday').hide();

                        boxInit(form);//初始化日期控件及新增修改弹出框公共js
                        $.get(list1.baseUrl + '/'+ id, null, function (data) {
							var result = data.data;
							//修改数据前，默认赋值
							setFromValues(layero, result);
                            $('#bankStartDatetime').timepicker('setTime',result.pbcStartTime);
                            $('#bankEndDatetime').timepicker('setTime',result.pbcEndTime);

							if(result.code == 'ams' || result.code == 'saic' || result.code == 'pbc' || result.code == 'core'){
                                $('#name').attr("readonly","readonly");
                                $('#code').attr("readonly","readonly");
                                $('#dataType').attr("disabled","disabled");
                            } else {
                                if (result.collectType === 'ONLINE' && result.dataType === 'OTHER') {
                                    $('.dataNo').show();
                                }
                            }
                            if (result.collectType === 'IMPORT') {
                                $('.collect').hide();
                            }
                            if (result.collectType === 'ONLINE' && result.dataType === 'PBC') {
                                $('.timingForEveryday').show();
                            }
                            form.render();
                        });

                        form.on('submit(edit)', function (data) {
                            if(list1.currentItem.id) {
                                $.ajax({
                                    url: list1.baseUrl + "/" + list1.currentItem.id,
                                    type: 'put',
                                    data: submitDataFormat(data.field),
                                    dataType: "json",
                                    success: function (res) {
                                        if (res.code === 'ACK') {
                                            layer.msg('修改成功，请确认比对规则配置是否需要修改先决数源。');
                                            layer.close(index);
                                            list1.refresh();
                                        } else {
                                            layer.msg(res.message);
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

    /**
     * 新增修改弹出框数据初始化
     */
	function boxInit(form){
        //初始化日期控件
        common.handleBootstrapDatetimepicker();
        $('#bankStartDatetime').timepicker({
            showMeridian:false,
            defaultTime:false
        });
        $('#bankEndDatetime').timepicker({
            showMeridian:false,
            defaultTime:false
        });

        form.on('select(dataType)',function (data) {
            if (data.value === "OTHER") {
                $('.dataNo').show();
            } else {
                $('.dataNo').hide();
            }
            if($('#dataType').val() === "PBC" && $('#collectType').val() === 'ONLINE'){
                $('.timingForEveryday').show();
            }else{
                $('.timingForEveryday').hide();
            }
        });
        form.on('select(collectType)',function (data) {
            if (data.value === "IMPORT") {
                // if ($('#dataType').val() === "") {
                //     $('#dataType').val("OTHER")
                // }
                $('.collect').hide();
                $('.dataNo').hide();
            } else {
                $('.collect').show();
                if ($('#dataType').val() === "OTHER") {
                    $('.dataNo').show();
                } else {
                    $('.dataNo').hide();
                }
            }
            if($('#dataType').val() === "PBC" && $('#collectType').val() === 'ONLINE'){
                $('.timingForEveryday').show();
            }else{
                $('.timingForEveryday').hide();
            }
            form.render('select');
        });
    }

    function submitDataFormat(data) {
        var pbcStartTime = $('#bankStartDatetime').val();
        var pbcEndTime = $('#bankEndDatetime').val();
        data.pbcStartTime = pbcStartTime;
        data.pbcEndTime = pbcEndTime;
        if (data.code === '') {
            delete data["code"];
        }
        return data;
    }

	$('#btn_del1').on('click', function () {
        if (list1.select(layer)) {
            var id = list1.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: list1.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layer.msg("删除成功！");
                            list1.refresh();
                        } else {
                            layer.msg("删除失败！" + data.message)
                            list1.refresh();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });

});