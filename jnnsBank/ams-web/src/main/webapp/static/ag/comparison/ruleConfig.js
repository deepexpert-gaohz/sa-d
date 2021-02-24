layui.config({
  	base: '../js/' //假设这是你存放拓展模块的根目录
});

var list2 = {
	baseUrl: "../../compareRule",
	entity: "list2",
	tableId: "list2",
	toolbarId: "toolbar2",
	unique: "id",
	order: "asc",
    currentItem: {},
    dataList: null,
    fieldList: null
};
list2.columns = function () {
	return [{
        radio: true
    }, {
		field: 'name',
		title: '规则名称'
	}, {
		field: 'creater',
		title: '创建人'
	}, {
		field: 'createTime',
		title: '创建时间'
	}, {
		field: 'count',
		title: '规则状态',
		formatter: function (value, row, index) {
			return '使用过' + value + '次';
		}
	}];
};

list2.queryParams = function (params) {
	if (!params)
		return {
			name: $.trim($("#ruleName").val()),
            beginDate: $("#ruleBeginDate").val(),
			endDate: $("#ruleEndDate").val(),
			type: $("#ruleType").val(),
            creater: $("#ruleCreator").val()
		};
	var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        name: $.trim($("#ruleName").val()),
        beginDate: $("#ruleBeginDate").val(),
        endDate: $("#ruleEndDate").val(),
        type: $("#ruleType").val(),
        creater: $("#ruleCreator").val()
	};
	return temp;
};

list2.init = function () {

	list2.table = $('#' + list2.tableId).bootstrapTable({
		url: list2.baseUrl+'/list', //请求后台的URL（*）
		method: 'get', //请求方式（*）
        toolbar: '#' + list2.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list2.order, //排序方式
        queryParams: list2.queryParams,//传递参数（*）
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
        uniqueId: list2.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list2.columns(),
		onLoadError: function(status){
			ajaxError(status);
		},
        responseHandler: function (res) {
            return {total:res.data.totalRecord, rows: res.data.list};//根据后端接口json数据格式修改
        }
	});
};

list2.select = function (layer) {
	var rows = list2.table.bootstrapTable('getSelections');
	if (rows.length == 1) {
		list2.currentItem = rows[0];
		return true;
	} else {
		layer.msg("请选中一行");
		return false;
	}
};

list2.refresh = function () {
    var queryParams = list2.queryParams();
    queryParams.pageNumber=1;
    list2.table.bootstrapTable('refresh', queryParams);
}

layui.use(['element', 'form', 'layedit', 'laydate', 'loading'], function () {
	list2.init();

	var editIndex;
	var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        element = layui.element(),
		form = layui.form(),
		layedit = layui.layedit,
		laydate = layui.laydate,
		loading = layui.loading;

    //初始化日期控件
    var laydateRule =  beginEndDateInit(laydate, 'ruleBeginDate', 'ruleEndDate', 'YYYY-MM-DD', false);

	var addBoxIndex = -1;

	$('.date-picker').change(function(){
		var value = $(this).val();
		$(this).val(dateFormat(value));
    });
    
    $('#tab2').on('click', function () {
        list2.dataList = null;
        list2.fieldList = null;
        list2.refresh();
        layer.closeAll();
    });

	$('#btn_search2').on('click', function () {
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		if(beginDate && endDate && beginDate > endDate) {
			layer.msg("时间筛选开始时间不能大于结束时间");
		} else {
			var queryParams = list2.queryParams();
			queryParams.pageNumber=1;
			list2.table.bootstrapTable('refresh', queryParams);
		}
	});

    //规则配置-新增
	$('#btn_add2').on('click', function () {
        if (addBoxIndex !== -1)
            return;
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('ruleEdit.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '新建规则配置',
                content: form,
                btn: ['保存', '取消'],
                shade: false,
                area: ['600px', '600px'],
                maxmin: true,
                yes: function (index) {
                    layedit.sync(editIndex);
                    //触发表单的提交事件
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
                success: function (layero, index) {
                    var form = layui.form();

                    initEdit(form);

					form.render();

                    form.on('submit(edit)', function (data) {
                        if ($("#name").prop('value')==""){
                            layer.msg("请输入规则名称");
                            return true;
                        }
                        $.ajax({
                            url: '../../compareRule/save',
                            type: 'post',
                            data: $("#ruleEdit").serialize(),
                            dataType: "json",
                            success: function (res) {
                                if (res.code === 'ACK') {
                                    layer.msg('保存成功');
                                    layer.close(index);
                                    list2.refresh();
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

	//规则配置-修改
	$('#btn_edit2').on('click', function () {
		if (addBoxIndex !== -1)
            return;
        if (list2.select(layer)) {
            var id = list2.currentItem.id;

            //判断该规则是否已经被比对任务使用，若在使用，则不能进行修改。
            $.get("../../compareRule/checkRuleIsEdit?id=" + id, null, function (data) {
                if (data.code === 'ACK') {
                    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                    $.get('ruleEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '修改规则配置',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            area: ['600px', '600px'],
                            maxmin: true,
                            yes: function (index) {
                                layedit.sync(editIndex);
                                //触发表单的提交事件
                                $('form.layui-form').find('button[lay-filter=edit]').click();
                            },
                            success: function (layero, index) {
                                var form = layui.form();

                                $.get('../../compareRule/getOne?id=' + id, null, function (data) {
                                    //$.get(list2.baseUrl + '/ruleConfigDetail.json?id=' + id, null, function (data) {
                                    var result = data.data;
                                    setFromValues(layero, result);
                                    $('#personBlackList').prop('value', true);//默认为true，选中后点击保存，序列化时只提交选中的
                                    $('#bussBlackList').prop('value', true);
                                    $('#personBlackList').prop('checked', result.personBlackList);//根据返回结果进行渲染
                                    $('#bussBlackList').prop('checked', result.bussBlackList);
                                    initEdit(form, result, false);//渲染选中的
                                    form.render();
                                });

                                form.on('submit(edit)', function (data) {
                                    if(list2.currentItem.id) {
                                        if ($("#name").prop('value')==""){
                                            layer.msg("请输入规则名称");
                                            return;
                                        }
                                        $.ajax({
                                            url: '../../compareRule/save',
                                            type: 'put',
                                            data: $("#ruleEdit").serialize(),
                                            dataType: "json",
                                            success: function (res) {
                                                if (res.code === 'ACK') {
                                                    layer.msg('更新成功');
                                                    layer.close(index);
                                                    list2.refresh();
                                                } else {
                                                    layer.msg("此规则已经被任务" + res.message + "使用，不能进行修改");
                                                    layer.close(index);
                                                    list2.refresh();
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
                } else {
                    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                    $.get('ruleEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '查看规则配置',
                            content: form,
                            // btn: ['保存', '取消'],
                            shade: false,
                            area: ['600px', '600px'],
                            maxmin: true,
                            yes: function (index) {
                                layedit.sync(editIndex);
                                //触发表单的提交事件
                                $('form.layui-form').find('button[lay-filter=edit]').click();
                            },
                            success: function (layero, index) {
                                var form = layui.form();

                                $.get('../../compareRule/getOne?id=' + id, null, function (data) {
                                    //$.get(list2.baseUrl + '/ruleConfigDetail.json?id=' + id, null, function (data) {
                                    var result = data.data;
                                    setFromValues(layero, result);
                                    $('#personBlackList').prop('value', true);//默认为true，选中后点击保存，序列化时只提交选中的
                                    $('#bussBlackList').prop('value', true);
                                    $('#personBlackList').prop('checked', result.personBlackList);//根据返回结果进行渲染
                                    $('#bussBlackList').prop('checked', result.bussBlackList);
                                    initEdit(form, result,true);//渲染选中的
                                    form.render();
                                    layer.msg("此规则已经被使用，不能进行修改");
                                });

                            },
                            end: function () {
                                addBoxIndex = -1;
                            }
                        });
                    });
                }
            });

        }
	});

    //规则配置-删除
	$('#btn_del2').on('click', function () {
        if (list2.select(layer)) {
            var id = list2.currentItem.id;
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: '../../compareRule/delete?id=' + id,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layer.msg("删除成功！");
                            list2.refresh();
                        } else {
                            layer.msg("删除失败！" + data.message)
                            list2.refresh();
                        }
                    }
                });
                layer.close(index);
            });
        }
    });

    function initEdit(from, data,isDisabled) {

        getDataList(function (dataList) {
            if (data) {
                bindDataList(from, dataList, data.checkedDataList,isDisabled)
            } else {
                bindDataList(from, dataList, null,isDisabled)
            }

            from.render();
        });

        getFieldList(function (fieldList) {
            if (data) {
                bindFieldList(from, fieldList, data.checkedDataList, data.checkedFieldList,isDisabled);
            } else {
                bindFieldList(from, fieldList, null, null,isDisabled);
            }
            from.render();
        });
    }

    function bindDataList(from, dataList, checkedDataList,isDisabled) {

        //比对数据源选择
        var content = "";
        for(j=0; j< dataList.length; j++) {
            var item = dataList[j];
            elemId= 'data' + item.id;
            content += '<input type="checkbox" id="'+elemId+'" name="dataIds" lay-filter="'+elemId+'" value="' + item.id + '" data-name="' + item.name + '" class="cbData" lay-skin="primary" title="'+item.name+'" data-mytype="'+item.collectType+'">';
        }
        $('#compareData').html(content);

        //设置监听事件
        for(i=0; i< dataList.length; i++) {
            var dataItem = dataList[i]
            form.on('checkbox(data' + dataItem.id + ')', function(data){
                var elem = $(data.elem);
                var name = elem.attr('data-name');
                var type = elem.attr('data-mytype');//采集方式
                var id = data.value;

                //勾选的数据源
                var checkedDataList = getCheckedDataList();

                //数据源比对字段勾选表生成
                getFieldList(function (fieldList) {
                    bindFieldList(from, fieldList, checkedDataList, null);
                    from.render();
                });

                //被勾选点击时
                if(data.elem.checked) {
                    //在线采集需要勾选先决数据源
                    if (type=="ONLINE" && id !=1001) {//导入方式和本地账管不需要先决数据源
                        var bodyHtml = '';
                        bodyHtml += '<tr id="row'+id+'">';
                        bodyHtml += '<td>' + name +'</td>';
                        bodyHtml += '<td id="preData' + id + '"></td>';
                        bodyHtml += '</tr>';
                        $('#tbody').append(bodyHtml);
                    }

                    //每行数据添加先决数据源选项
                    for(j=0; j< checkedDataList.length; j++) {
                        var preDataItem = checkedDataList[j];
                        $('#preData' + preDataItem.id).append('<input type="checkbox" class="preDataItem' + id + '" name="parentIds" title="' + name + '" value="' + preDataItem.id + '-' + id + '" lay-skin="primary">');
                    }

                    //自身添加先决数据源选项
                    var preDataHtml = getPreDataHtml(checkedDataList);
                    var html = preDataHtml.replace(/preDataId/g, id);
                    $('#preData' + id).html(html);
                    $('#preData' + id).find('.preDataItem' + id).remove();

                    //先决数据源移除自身（每行数据）
                    $('#row'+id).find('.preDataItem' + id).next('.layui-form-checkbox').remove();
                    $('#row'+id).find('.preDataItem' + id).remove();

                } else {//不勾选时

                    //取消勾选移除数据源每行数据
                    $('#row'+id).remove();
                    //移除其他数据中显示的先决数据源选项
                    $('.preDataItem' + id).next('.layui-form-checkbox').remove();
                    $('.preDataItem' + id).remove();
                }

                from.render();
            });
        }

        if (checkedDataList) {
            var preDataHtml = getPreDataHtml(checkedDataList);

            for(i=0; i< checkedDataList.length; i++) {
                var dataItem = checkedDataList[i];

                //生成表中每行数据，在线采集数据
                if (dataItem.collectType=="ONLINE" && dataItem.id !=1001) {//导入方式和本地账管不需要先决数据源
                    var bodyHtml = '';
                    bodyHtml += '<tr id="row'+dataItem.id+'">';
                    bodyHtml += '<td>' + dataItem.name +'</td>';
                    bodyHtml += '<td id="preData' + dataItem.id + '"></td>';
                    bodyHtml += '</tr>';
                    $('#tbody').append(bodyHtml);
                }

                $('#data' + dataItem.id).prop('checked', true);
                var html = preDataHtml.replace(/preDataId/g, dataItem.id); 
                $('#preData' + dataItem.id).html(html);
                $('#preData' + dataItem.id).find('.preDataItem' + dataItem.id).remove();
                // var parentIds = dataItem.parentIds;

                var list = [];
                if(dataItem.parentIds){
                    list = dataItem.parentIds.split(",");
                }

                for(j=0; j< list.length; j++) {
                    $('#preData' + dataItem.id).find('.preDataItem' + list[j]).prop('checked', true);
                }

            }
            from.render();
        }
        if(isDisabled){
            $("#ruleEdit").find('input,select,textarea').prop("disabled", true);
        }
        from.render();
    }

    function bindFieldList(from, fieldList, checkedDataList, checkedFieldList,isDisabled) {
        var headHtml = '', bodyHtml = '';

        headHtml += '<thead><tr><th rowspan="2">比对字段</th><th rowspan="2">选用</th>'
        if (checkedDataList && checkedDataList.length > 0) {
            for(j=0; j< checkedDataList.length; j++) {
                headHtml += '<th colspan="2">' + checkedDataList[j].name + '</th>'
            }
        }
        headHtml += '</tr><tr>'

        if (checkedDataList && checkedDataList.length > 0) {
            for(j=0; j< checkedDataList.length; j++) {
                headHtml += '<th>使用</th><th>空算过</th>';
            }
        }
        headHtml += '</thead>'

        bodyHtml += '<tbody>';
        for(i=0; i< fieldList.length; i++) {
            var fieldItem = fieldList[i];
            if(fieldItem.name == '机构代码'){
                bodyHtml += '<tr><td>' + fieldItem.name + '</td><td><input checked="true" type="checkbox" id="field' + fieldItem.id + '" lay-filter ="field'+fieldItem.id +'" name="fields" value="'+fieldItem.id +'" lay-skin="primary"></td>'
            }else{
                bodyHtml += '<tr><td>' + fieldItem.name + '</td><td><input type="checkbox" id="field' + fieldItem.id + '" lay-filter ="field'+fieldItem.id +'" name="fields" value="'+fieldItem.id +'" lay-skin="primary"></td>'
            }

            if (checkedDataList && checkedDataList.length > 0) {
                for(j=0; j< checkedDataList.length; j++) {
                    var dataItem = checkedDataList[j];
                    var elemId1 = 'field' + fieldItem.id + '_data' + dataItem.id + '_use';
                    var elemId2 = 'field' + fieldItem.id + '_data' + dataItem.id + '_empty';

                    bodyHtml += '<th><input type="checkbox" id="'+elemId1+'" name="'+elemId1+'" lay-filter="'+elemId1+'" lay-skin="primary"></th>';
                    bodyHtml += '<th><input type="checkbox" id="'+elemId2+'" name="'+elemId2+'" lay-filter="'+elemId2+'" lay-skin="primary"></th>';
                }
            }
            bodyHtml += '</tr>'
        }
        bodyHtml += '</tbody>';
        $('#fieldList').html(headHtml + bodyHtml);


        for(i=0; i< fieldList.length; i++) {
            var fieldItem = fieldList[i];
            if (checkedDataList && checkedDataList.length > 0) {
                for(j=0; j< checkedDataList.length; j++) {
                    var dataItem = checkedDataList[j];
                    var elemId1 = 'field' + fieldItem.id + '_data' + dataItem.id + '_use';
                    var elemId2 = 'field' + fieldItem.id + '_data' + dataItem.id + '_empty';

                    form.on('checkbox(' + elemId2 + ')', function(data){//空值过，选中时：使用、选用的联动
                        var prevElem = data.othis.parent().prev().find('input[type="checkbox"]');
                        if (data.elem.checked) {
                            //1、使用的联动
                            prevElem.prop('checked', true);
                            //2、选用的联动
                            var prevElem = data.othis.parent().prev().find('input[type="checkbox"]');
                            while (prevElem.length>0){
                                //console.log("checked:"+prevElem.prop("checked"));
                                //console.log("prevElem.length离开while："+prevElem.length);
                                if(prevElem.parent().prev().find('input[type="checkbox"]').length===0){
                                    prevElem.prop('checked', true);
                                }
                                prevElem = prevElem.parent().prev().find('input[type="checkbox"]');
                            }
                            from.render();
                        }
                    });

                    form.on('checkbox(' + elemId1 + ')', function(data){//使用，不选中时：空值过、选用的联动
                        var nextElem = data.othis.parent().next().find('input[type="checkbox"]');
                        if (!data.elem.checked) {//使用，不选中时：空值过的联动
                            nextElem.prop('checked', false);
                            from.render();
                        }
                        if (data.elem.checked){//使用，选中时：选用的联动
                            var prevElem = data.othis.parent().prev().find('input[type="checkbox"]');
                            while (prevElem.length>0){
                                //console.log("checked:"+prevElem.prop("checked"));
                                //console.log("prevElem.length离开while："+prevElem.length);
                                if(prevElem.parent().prev().find('input[type="checkbox"]').length===0){
                                    prevElem.prop('checked', true);
                                }
                                prevElem = prevElem.parent().prev().find('input[type="checkbox"]');
                            }
                            from.render();
                        }

                    });

                    form.on('checkbox(field' + fieldItem.id + ')', function(data){//选用不选中时：整行的联动
                        if (!data.elem.checked) {
                            var nextElem = data.othis.parent().next().find('input[type="checkbox"]');
                            while (nextElem.length>0){
                                nextElem.prop('checked', false);
                                nextElem = nextElem.parent().next().find('input[type="checkbox"]');
                            }
                            from.render();
                        }
                    });
                }
            }
        }

        if (checkedFieldList) {
            for(i=0; i< checkedFieldList.length; i++) {
                var fieldItem = checkedFieldList[i];
                var dataList = fieldItem.dataList;

                $('#field' + fieldItem.id).prop('checked', true);

                for(j=0; j< dataList.length; j++) {
                    var dataItem = dataList[j];
                    if (dataItem.use) {
                        var elemId1 = 'field' + fieldItem.id + '_data' + dataItem.id + '_use';
                        $('#' + elemId1).prop('checked', true);
                    }
                    if (dataItem.empty) {
                        var elemId2 = 'field' + fieldItem.id + '_data' + dataItem.id + '_empty';
                        $('#' + elemId2).prop('checked', true);
                    }
                }
            }
            from.render();
        }
        if(isDisabled){
            $("#ruleEdit").find('input,select,textarea').prop("disabled", true);
        }
        from.render();
    }

    function getCheckedDataList() {
        var list = [];

        var elems = $('.cbData');
        for(i=0; i<elems.length; i++) {
            var item = elems[i];
            if (item.checked) {
                var id = item.value
                var name = $(item).attr('data-name');
                var type = $(item).attr('data-mytype');
                list.push({id: id, name: name ,type:type});
            }
        }
        return list;
    }

    function getPreDataHtml(list) {
        var html = '';
        for(i=0; i<list.length; i++) {
            var item = list[i];
            html += '<input type="checkbox" class="preDataItem' + item.id + '" name="parentIds" title="' + item.name + '" value="preDataId-' + item.id + '" lay-skin="primary">'
        }
        return html;
    }

    //获取compareDataSource列表
    function getDataList(fn) {
        if (list2.dataList) {
            fn && fn(list2.dataList);
        } else {
            $.get('../../compareDataSource/list?size=100', null, function (data) {
                list2.dataList = data.rows;
                fn && fn(list2.dataList);
            });
        }
    }

    //获取比较字段列表
    function getFieldList(fn) {
        if (list2.fieldList) {
            fn && fn(list2.fieldList);
        } else {
            $.get('../../compareField/', null, function (data) {
                list2.fieldList = data.data;
                fn && fn(list2.fieldList);
            });
        }
    }

});
