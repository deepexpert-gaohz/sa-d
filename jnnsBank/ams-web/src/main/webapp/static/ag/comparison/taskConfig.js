layui.config({
  	base: '../js/' //假设这是你存放拓展模块的根目录
});

var list3 = {
	baseUrl: "../../compareTask",
	baseUrl2:"../ag/comparison/json/",
	entity: "list3",
	tableId: "list3",
	toolbarId: "toolbar3",
	unique: "id",
	order: "asc",
	currentItem: {},
    ruleOptions: '',
    laytpl: null,
    element: null
};
list3.columns = function () {
	return [{
        checkbox: true
    }, {
		field: 'name',
		title: '任务名称'
	}, {
		field: 'taskType',
		title: '任务类型',
		formatter: function (value, row, index) {
			var typeMap = {
				'NOW': '即时任务',
				'TIMING': '定时任务'
			}
			return typeMap[value] || '';
		}
	}, {
		field: 'compareRuleName',
		title: '任务规则'
	}, {
		field: 'createName',
		title: '创建人'
	}, {
		field: 'time',
		title: '创建时间'
	}, {
		field: 'state',
		title: '任务状态',
		formatter: function (value, row, index) {
			var typeMap = {
				'INIT': '待导入',
				'COLLECTING': '采集中',
                'COLLECTSUCCESS': '采集完成',
				'PAUSE': '暂停',
                'COMPARING': '比对中',
                'SUCCESS': '完成',
                'FAIL': '失败'
			}
			return typeMap[value] || '';
		}
	}, {
		field: 'id',
		title: '操作',
		formatter: function (value, row, index) {
			if(row.state == "SUCCESS") {
				return '<a class="viewResult" style="color: blue" href="#" data-id="'+value+'" data-name="'+row.name+'">结果查看</a>';
			} else {
			    return '<a class="viewInfo" style="color: blue" href="#" data-id="'+value+'" data-name="'+row.name+'">任务管理</a>';
            }
		}
	}];
};

list3.queryParams = function (params) {
	if (!params)
		return {
			name: $.trim($("#taskName").val()),
            beginDateStr: $("#taskBeginDate").val(),
            endDateStr: $("#taskEndDate").val(),
			state: $("#taskState").val(),
            taskType: $("#taskType").val(),
            createName: $("#taskCreator").val()
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
		size: params.limit, //页面大小
		page: params.offset / params.limit, //页码
		name: $.trim($("#taskName").val()),
        beginDateStr: $("#taskBeginDate").val(),
        endDateStr: $("#taskEndDate").val(),
		state: $("#taskState").val(),
        taskType: $("#taskType").val(),
        createName: $("#taskCreator").val()
	};
	return temp;
	};

	list3.init = function () {

	list3.table = $('#' + list3.tableId).bootstrapTable({
		url: list3.baseUrl + '/list', //请求后台的URL（*）
		method: 'get', //请求方式（*）
        toolbar: '#' + list3.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list3.order, //排序方式
        queryParams: list3.queryParams,//传递参数（*）
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
        uniqueId: list3.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list3.columns(),
		onLoadError: function(status){
			ajaxError(status);
		},
        responseHandler: function (res) {
            return {total:res.data.totalRecord, rows: res.data.list};//根据后端接口json数据格式修改
        }
	});
};

list3.select = function (layer) {
	var rows = list3.table.bootstrapTable('getSelections');
	if (rows.length == 1) {
		list3.currentItem = rows[0];
		return true;
	} else {
		layer.msg("请选中一行");
		return false;
	}
};

list3.refresh = function () {
    var queryParams = list3.queryParams();
    queryParams.pageNumber=1;
    list3.table.bootstrapTable('refresh', queryParams);
}

layui.use(['element', 'form', 'layedit', 'laydate', 'laytpl', 'loading'], function () {
	list3.init();
    setInterval(function(){
        var queryParams = list3.queryParams();
        list3.table.bootstrapTable('refresh', queryParams);
    },60000);

	var editIndex;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
		layer = layui.layer, //获取当前窗口的layer对象
		element = layui.element(),
		form = layui.form(),
		layedit = layui.layedit,
		laydate = layui.laydate,
		laytpl = layui.laytpl,
		loading = layui.loading;

    //初始化日期控件
    var laydateTask =  beginEndDateInit(laydate, 'taskBeginDate', 'taskEndDate', 'YYYY-MM-DD', false);

    var addBoxIndex = -1;
    list3.laytpl = laytpl;
    list3.element = element;

	$('.date-picker').change(function(){
		var value = $(this).val();
		$(this).val(dateFormat(value));
    });

    $('#tab3').on('click', function () {
        list3.refresh();
        list3.ruleOptions = '';
        layer.closeAll();
    });

	$('#btn_search3').on('click', function () {
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		if(beginDate && endDate && beginDate > endDate) {
			layer.msg("时间筛选开始时间不能大于结束时间");
		} else {
			var queryParams = list3.queryParams();
			queryParams.pageNumber=1;
			list3.table.bootstrapTable('refresh', queryParams);
		}
	});

	$('#btn_add3').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('taskEdit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '新建比对任务',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                area: ['600px', '380px'],
                maxmin: true,
                yes: function (index) {
                    layedit.sync(editIndex);
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
                success: function (layero, index) {
                    var form = layui.form();

                    getRuleOptions(function (ruleOptions) {
                        layero.find('#compareRuleId').append(ruleOptions);
                        form.render();
                    });

					form.render();

					$('.timing').hide();

                    $('#taskType').attr('lay-filter','taskType');
                    form.on('select(taskType)',function (data) {
                    	console.log("data.value="+data.value)
                        if (data.value == "TIMING") {
                            $('.timing').show();
                            $("#rate").attr("lay-verify","required");
                            $("#startTime").attr("lay-verify","required");
                        } else {
                            $('.timing').hide();
                            $("#rate").attr("lay-verify","");
                            $("#startTime").attr("lay-verify","");
                        }
                    });

                    form.on('submit(edit)', function (data) {
                        $.ajax({
                            url: list3.baseUrl+"/",
                            type: 'post',
                            data: data.field,
                            dataType: "json",
                            success: function (res) {
                                if (res.code === 'ACK') {
                                    layer.msg('保存成功');
                                    layer.close(index);
                                    list3.refresh();
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

	$('#btn_edit3').on('click', function () {
		if (addBoxIndex !== -1)
            return;
        if (list3.select(layer)) {
            var id = list3.currentItem.id;
            var type = list3.currentItem.state;
            if(type != 'INIT'){
                layer.msg('该任务状态不允许进行修改！');
                return;
            }
            $.get(list3.baseUrl+"/checkCompareTaskUser?taskId=" + id, null, function (result) {
                if (result.code == 'ACK') {
                    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                    $.get('taskEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '修改比对任务',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            area: ['600px', '400px'],
                            maxmin: true,
                            yes: function (index) {
                                layedit.sync(editIndex);
                                //触发表单的提交事件
                                $('form.layui-form').find('button[lay-filter=edit]').click();
                            },
                            success: function (layero, index) {
                                var form = layui.form();
                                getRuleOptions(function (ruleOptions) {
                                    layero.find('#compareRuleId').append(ruleOptions);
                                    form.render();
                                });
                                form.render();

                                $('.timing').hide();

                                $('#taskType').attr('lay-filter','taskType');
                                form.on('select(taskType)',function (data) {
                                    if (data.value == "TIMING") {
                                        $('.timing').show();
                                    } else {
                                        $('.timing').hide();
                                    }
                                });

                                $.get(list3.baseUrl + '/' + id, null, function (data) {
                                    var result = data.data;
                                    console.log("result.taskType="+result.taskType)
                                    if(result.taskType=="TIMING"){
                                        $('.timing').show();
                                    }else{
                                        $('.timing').hide();
                                    }
                                    setFromValues(layero, result);
                                    form.render();
                                });

                                form.on('submit(edit)', function (data) {
                                    if(list3.currentItem.id) {
                                        $.ajax({
                                            url: list3.baseUrl + "/" + list3.currentItem.id,
                                            type: 'put',
                                            data: data.field,
                                            dataType: "json",
                                            success: function (res) {
                                                if (res.code === 'ACK') {
                                                    layer.msg('更新成功');
                                                    layer.close(index);
                                                    list3.refresh();
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
                }else{
                    layer.msg(result.message);
                }
            })

        }
	});

	$('#btn_del3').on('click', function () {
        if (list3.select(layer)) {
            var id = list3.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: list3.baseUrl + "/" + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layer.msg("删除成功！");
                            list3.refresh();
                        } else {
                            layer.msg("删除失败！" + data.message)
                            list3.refresh();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });

	$('#list3').on('click','.viewInfo', function () {
        var id = $(this).attr('data-id');
        var name = $(this).attr('data-name');
        var timers;
        var taskInfoTimes;
        $.get(list3.baseUrl+"/checkCompareTaskUser?taskId=" + id, null, function (result) {
            if(result.code == 'ACK'){
                if (addBoxIndex !== -1) return;
                $.get('taskInfo.html', null, function (form) {
                    addBoxIndex = layer.open({
                        type: 1,
                        title: name + '任务进程信息',
                        content: form,
                        btn: '关闭',
                        shade: false,
                        area: ['600px', '620px'],
                        maxmin: true,
                        success: function (layero, index) {
                            element = layui.element();
                            element.init();
                            $('#id').val(id);
                            viewTaskInfo(id);
                            bindTaskInfo(id);
                            taskInfoTimes = setInterval('viewTaskInfo("' + id + '")',5000);
                            // viewTaskDetail(id);

                            viewDataDetail(id);
                            timers = setInterval('viewDataDetail("' + id + '")',10000);
                            bindDataDetail(id);

                            viewRuleDetail(id);
                        },
                        end: function () {
                            clearInterval(timers);
                            clearInterval(taskInfoTimes);
                            addBoxIndex = -1;
                        }
                    });
                });
            }else{
                layer.msg(result.message);
            }
        });
        return false;
    });

	$('#list3').on('click','.viewResult', function () {
		var id = $(this).attr('data-id');
        var name = $(this).attr('data-name');
        layer.closeAll();
        parent.tab.tabAdd({
            title: '结果管理-' + name,
            href: 'comparison/resultDetail.html?taskId=' + id
        });
        return false;
	});

    //
    // function viewTaskInfo1(id){
    //     $.get(list3.baseUrl + '/getCompareTaskDetails1?taskId=' + id, null, function (result) {
    //         $('#compareTaskState').text(result.compareStateCN);
    //         $('#dataTotal').text(result.count);
    //         // $('#processTime').text(result.processTime);
    //         $('#process').text(result.processed);
    //         $('.layui-progress-bar').attr('lay-percent', result.percent);
    //         element.init();
    //
    //         var getTpl = $('#taskTpl').html();
    //         laytpl(getTpl).render(result, function(html){
    //             $('#tab_001').html(html);
    //         });
    //     });
    // }

	function bindTaskInfo(id){
		$('#btn_start').off().click(function () {
            if ($(this).attr("class").indexOf("layui-btn-disabled") !== -1) {
                return null;
            } else {
                $.get(list3.baseUrl + '/doCompareBefore?taskId=' + id, null, function (result) {
                    if(result.code == 'ACK'){
                        $('#btn_start').addClass('layui-btn-disabled');
                        $('#btn_reset').addClass('layui-btn-disabled');
                        $('#btn_stop').removeClass('layui-btn-disabled');
                        $.get(list3.baseUrl + '/doCompare?taskId=' + id, null, function (result) {
                            layer.msg(result);
                        })
                    }else{
                        layer.msg(result.message);
                    }
                })
            }
		});

		$('#btn_reset').off().click(function () {
            if ($(this).attr("class").indexOf("layui-btn-disabled") !== -1) {
                return null;
            } else {
                $.get(list3.baseUrl + '/comapreReset?taskId=' + id, null, function (result) {
                    if (result.code == 'ACK') {
                        layer.msg(result.message);
                        $('#btn_start').removeClass('layui-btn-disabled');
                        $('#btn_reset').removeClass('layui-btn-disabled');
                        $('#btn_stop').addClass('layui-btn-disabled');
                    } else {
                        layer.msg(result.message);
                    }
                });
            }
		});

        $('#btn_stop').off().click(function () {
            if ($(this).attr("class").indexOf("layui-btn-disabled") !== -1) {
                return null;
            } else {
                $.get(list3.baseUrl + '/compareShutDown?taskId=' + id, null, function (result) {
                    if (result.code == 'ACK') {
                        $('#btn_start').removeClass('layui-btn-disabled');
                        $('#btn_reset').removeClass('layui-btn-disabled');
                    } else {
                        layer.msg(result.message);
                    }
                });
            }
        });
	}

	function viewTaskDetail(id){
	}

	function bindDataDetail(id){
		$('#tab_001').on('click','.viewtaskData', function () {
			var itemId = $(this).attr('data-id');
            var taskId = $('#id').val();
            layer.closeAll();
            parent.tab.tabAdd({
                title: '导入详情',
                href: 'comparison/dataDetail.html?taskId=' + taskId + '&dataSourceId=' + itemId
            });
		});

		$('#tab_001').on('click','.collectData', function () {
			var itemId = $(this).attr('data-id');
            var taskId = $('#id').val();
            $.ajax({
                url: list3.baseUrl + '/onlineCollect?dataSourceId='+itemId+'&taskId='+taskId,
                type: 'post',
                dataType: "json",
                success: function (res) {
                    if (res.code === 'ACK') {
                        layer.msg('在线采集启动成功');
                    } else {
                        layer.msg(res.message);
                    }
                }
            });
		});

        $('#tab_001').on('click','.resetCollectData', function () {
            var itemId = $(this).attr('data-id');
            var taskId = $('#id').val();
            $.ajax({
                url: list3.baseUrl + '/onlineResetCollect?dataSourceId='+itemId+'&taskId='+taskId,
                type: 'post',
                dataType: "json",
                success: function (res) {
                    if (res.code === 'ACK') {
                        layer.msg('再次在线采集启动成功');
                    } else {
                        layer.msg(res.message);
                    }
                }
            });
        });

        $('#tab_001').on('click','.exportData', function () {
            // var itemId = $(this).attr('data-id');
            // $("#saicImportData").show();
            var taskId = $('#id').val();
            $.ajax({
                url: list3.baseUrl + '/checkDataSourceImporter?taskId='+taskId,
                type: 'get',
                dataType: "json",
                success: function (res) {
                    if (res.code === 'ACK') {
                        $("#downloadFrame").prop('src', list3.baseUrl + '/export?taskId=' + taskId);
                    } else {
                        layer.msg(res.message);
                    }
                }
            });
        });


        $('#tab_001').on('click','.importCheck', function () {
            var itemId = $(this).attr('data-id');
            var taskId = $('#id').val();
            layer.closeAll();
            parent.tab.tabAdd({
                title: '导入结果',
                href: 'comparison/importDetail.html?taskId=' + taskId + '&dataSourceId=' + itemId
            });
        });

		$('#tab_001').on('click','.importData', function () {
			var itemId = $(this).attr('data-id');
			var taskId = $('#id').val();
            layer.open({
                type : 1,
                title : '比对数据导入',
                skin : 'layui-layer-rim', //加上边框
                area : [ '300px', '200px' ], //宽高
                content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">请选择文件</div> </div>'
            });
            var uploader = WebUploader.create({
                auto: true, // 选完文件后，是否自动上传
                swf: '../js/webuploader/Uploader.swf', // swf文件路径
                server:  list3.baseUrl + '/upload?dataSourceId='+itemId+'&taskId='+taskId, // 文件接收服务端
                pick: '#imgPicker', // 选择文件的按钮。可选
                timeout: 25 * 60 * 1000,// 25分钟
                /*fileNumLimit: 5,//一次最多上传五张*/
                // 只允许选择图片文件。
                /*accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/!*'
                }*/
            });
            uploader.on( 'uploadSuccess', function( file, res ) {
                layer.msg(res.message);
                $('#btn_start').removeClass('layui-btn-disabled');
                $.get(list3.baseUrl + '/getCompareTaskCount?taskId='+taskId, function (res) {
                    $('#dataTotal').text(res);
                });
                list3.refresh();
            });
            uploader.on( 'uploadError', function( file, reason ) {
                console.log('file' + file);
                console.log('reason' + reason);
                layer.msg('上传失败');
            });
		});

		$('#tab_001').on('click','.exportTemplate', function () {
            $("#templateDownloadForm").submit();
		});
	}

	function viewRuleDetail(id){
		$.get(list3.baseUrl + '/getCompareRuleDetails?taskId=' + id, null, function (result) {
			var getTpl = $('#ruleTpl').html();
			laytpl(getTpl).render(result, function(html){
				$('#tab_002 .ruleDiv').html(html);
			});
		});
    }

    function getRuleOptions(fn) {
        if (list3.ruleOptions) {
            fn && fn(list3.ruleOptions);
        } else {
            $.get('../../compareRule/getByOrganUpWard', function (res) {
                if(res.code === 'ACK') {
                    var options = '';
                    for (var i = 0; i < res.data.length; i++){
                        options += '<option value="' + res.data[i].id + '" >' + res.data[i].name + '</option>';
                    }
                    list3.ruleOptions = options;

                    fn && fn(list3.ruleOptions);
                }
            });
        }
    }
});

function viewDataDetail(id){
    $.get(list3.baseUrl + '/getCompareDataSourceDetails?taskId=' + id, null, function (result) {
        var getTpl = $('#dataTpl').html();
        list3.laytpl(getTpl).render(result, function(html){
            $('#tab_001').html(html);
        });
    });
}

function viewTaskInfo(id){
    $.get(list3.baseUrl + '/getCompareTaskDetails?taskId=' + id, null, function (result) {

        if(result.taskType == "TIMING") {
            $('#btn_start').hide();
            $('#btn_reset').hide();
            $('#btn_stop').hide();
        }
        if(result.state == "COMPARING"){
            $('#btn_start').addClass('layui-btn-disabled');
            $('#btn_reset').addClass('layui-btn-disabled');
        }
        $('#compareTaskState').text(result.compareStateCN);
        $('#dataTotal').text(result.count);
        // $('#processTime').text(result.processTime);
        $('#process').text(result.processed);
        $('.layui-progress-bar').attr('lay-percent', (result.processed / result.count) * 100 + "%");
        list3.element.init();

        var getTpl = $('#taskTpl').html();
        list3.laytpl(getTpl).render(result, function(html){
            $('#tab_002 .taskDiv').html(html);
        });
    });
}