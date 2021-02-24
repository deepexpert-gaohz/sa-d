var modelmap = {
    baseUrl: "../../model",
    entity: "model",
    tableId: "modelmapTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    roleOptions: ''
};
modelmap.columns = function () {
    return [{
        radio: true
    }, {
        field: 'modelId',
        title: '模型编号'
    }, {
        field: 'name',
        title: '模型名称'
    }, {
        field: 'tableName',
        title: '数据库表名',
    },{
        field: 'ruleId',
        title: '风险规则',
    },{
        field: 'levelId',
        title: '风险等级',
    },{
        field: 'typeId',
        title: '风险类型',
    },{
        field: "",
        title: '操作',
        formatter: function operateFormatter(){
                return "<a href='../checkmap/list.html?id='+modelmap.currentItem.id style='color: #4CAF50' name='modelmaplj'>逻辑关系图</a>     <a style='color: #4CAF50' name='modelmapxy'>血缘关系图</a>"
        }
    }];
};

modelmap.queryParams = function (params) {
    if (!params)
        return {
            modelId: $("#modelId").val(),
            name : $("#name").val(),
            tableName: $("#tableName").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelId: $("#modelId").val(),
        name : $("#name").val(),
        tableName: $("#tableName").val()
    };
    return temp;
};
modelmap.init = function () {

    modelmap.table = $('#' + modelmap.tableId).bootstrapTable({
        url: modelmap.baseUrl + '/getModels', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + modelmap.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: modelmap.order, //排序方式
        queryParams: modelmap.queryParams,//传递参数（*）
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
        uniqueId: modelmap.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: modelmap.columns(),
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
modelmap.select = function (layerTips) {
    var rows = modelmap.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        modelmap.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    modelmap.init();

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
        var queryParams = modelmap.queryParams();
        queryParams.pageNumber=1;
        modelmap.table.bootstrapTable('refresh', queryParams);
    });

    /*$("#modelmapxy").on('click',function () {
        if(modelmap.select(layerTips)){
            var id = modelmap.currentItem.id;
            console.log(id);
            parent.tab.tabAdd({
                href: 'checkmap/list.html?id='+id,
                icon: 'fa fa-calendar-times-o',
                title: '血缘关系图',
                id:id
            });
        }

    });*/
    $('#modelmapxy').on('click', function (){
        var id = modelmap.currentItem.id;
        if (modelmap.select(layerTips)) {
            var id = modelmap.currentItem.id;
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('../checkmap/list.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '血缘关系图',
                    content: form,
                    btn: ['返回'],
                    shade: false,
                    area: ['100%', '100%'],
                    maxmin: true,

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
                        var myChart = echarts.init(document.getElementById('main'));
                        myChart.showLoading();
                        $.get(modelmap.baseUrl + '/map/' + id, function (data) {

                            myChart.hideLoading();
                            echarts.util.each(data.list, function (datum, index) {
                                index % 2 === 0 && (datum.collapsed = true);
                            });

                            myChart.setOption(option = {
                                tooltip: {
                                    trigger: 'item',
                                    triggerOn: 'mousemove'
                                },
                                series: [
                                    {
                                        type: 'tree',

                                        data: [data],

                                        top: '50%',
                                        left: '20%',
                                        bottom: '1%',
                                        right: '20%',

                                        symbolSize: 7,

                                        label: {
                                            normal: {
                                                position: 'left',
                                                verticalAlign: 'middle',
                                                align: 'right',
                                                fontSize: 9
                                            }
                                        },

                                        leaves: {
                                            label: {
                                                normal: {
                                                    position: 'right',
                                                    verticalAlign: 'middle',
                                                    align: 'left'
                                                }
                                            }
                                        },

                                        expandAndCollapse: true,
                                        animationDuration: 550,
                                        animationDurationUpdate: 750
                                    }
                                ]
                            });
                        });
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
            });
        }
    });


});