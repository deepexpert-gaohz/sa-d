var rule = {
    baseUrl: "../../ruleConfigure",
    entity: "model",
    tableId: "modelTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    code: "id",
    parentCode: "pId",
    rootValue: -1,
    explandColumn: 1
};
rule.columns = function () {
    return [{
        field: 'selectItem',
        radio: true
    },{
        field: 'name',
        title: '模型名称'
    } ,{
        field: 'modelId',
        title: '模型编号',
        width:'15%'
    }];
};
rule.queryParams = function (params) {
    if (!params)
        return {

        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码

    };
    return temp;
};
rule.init = function () {
    rule.table = $('#' + rule.tableId).bootstrapTreeTable({
        id: rule.unique,// 选取记录返回的值
        code: rule.code,// 用于设置父子关系
        parentCode: rule.parentCode,// 用于设置父子关系
        rootCodeValue: rule.rootValue,
        url: rule.baseUrl + '/getModels', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + rule.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        ajaxParams: rule.queryParams,//传递参数（*）
        expandColumn: rule.explandColumn,//在哪一列上面显示展开按钮,从0开始
        expandAll: true,
        columns: rule.columns(),
        clickRow: rule.clickRow,
        responseHandler: function (res) {
            //console.log(res);
            return res.list;
        }
    });
};
rule.select = function (layerTips) {
    var rows = rule.table.bootstrapTreeTable('getSelections');
    if (rows.length == 1) {
        rule.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};
rule.clickRow = function () {
    var rows = rule.table.bootstrapTreeTable('getSelections');
    //console.log(rows[0].id);
    $.get("../../ruleConfigure/findField/"+rows[0].id, function (res) {
        if(res.code === 'ACK') {
            var options = '';
            for (var i = 0; i < res.data.length; i++) {
                options += '<option value="' + res.data[i].id + '" >' + res.data[i].fieldName + '</option>';
            }
            ruleElement.fieldOption="";
            ruleElement.fieldOption = options;
        }
    });
    ruleElement.refresh();
};
rule.refresh = function () {
    rule.table.bootstrapTreeTable("refresh");
};


layui.use(['form', 'layedit', 'laydate'], function () {
    rule.init();
    $('#' + rule.tableId + '>.treegrid-tbody>tr').click(function () {
        var rows = rule.table.bootstrapTreeTable('getSelections');
        rule.currentItem = rows[0];

    });
    var allMenu = null;
    var editIndex;
    $.get(rule.baseUrl + '/getModels', null, function (data) {
        allMenu = data.data;
    });
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit;
//初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        rule.table.bootstrapTreeTable('refresh', rule.queryParams());
    });
    var addBoxIndex = -1;

    var form = layui.form();
    form.on('select(fieldName)', function(data){
        //console.log(data.elem);
        var  id = data.value;
        $.get(ruleElement.baseUrl + '/findFieldById/' + id, null, function (data) {
            var result = data.data;
            $("#field").val(result.field);
        })
    });
    $("#btn_init").on('click', null, function () {
        layer.confirm('确定初始化规则数据吗？', null, function (index) {
            $.ajax({
                url: "../../ruleConfigure/initRule",
                type: 'get',
                dataType: "json",
                success: function (res) {
                    layerTips.msg(res.message);
                    layerTips.close(index);
                    location.reload();
                },
                error: function () {
                    layerTips.msg("初始化失败");
                    location.reload();
                }
            });
            layer.close(index);
        });

    });
})
;

