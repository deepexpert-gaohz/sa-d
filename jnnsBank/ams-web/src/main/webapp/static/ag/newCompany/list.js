
layui.config({
     base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../freshCompany",
    entity: "newCompany",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'name',
        title: '公司名称'
    }, {
        field: 'legalPerson',
        title: '法人名称'
    }, {
        field: 'openDate',
        title: '成立日期'
    }, {
        field: 'state',
        title: '企业状态'
    }, {
        field: 'provinceName',
        title: '省'
    }, {
        field: 'cityName',
        title: '市'
    }, {
        field: 'areaName',
        title: '区'
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            name: $.trim($("#name").val()),
            address: $.trim($("#address").val()),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
            unityCreditCode: $.trim($("#unityCreditCode").val()),
            provinceName: $.trim($("#provinceName").val()),
            cityName: $.trim($("#cityName").val()),
            areaName: $.trim($("#areaName").val())
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        name: $.trim($("#name").val()),
        address: $.trim($("#address").val()),
        beginDate: $("#beginDate").val(),
        endDate: $("#endDate").val(),
        unityCreditCode: $.trim($("#unityCreditCode").val()),
        provinceName: $.trim($("#provinceName").val()),
        cityName: $.trim($("#cityName").val()),
        areaName: $.trim($("#areaName").val())
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/query', //请求后台的URL（*）
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
        // responseHandler: function (res) {
        //     if (res.code === 'ACK') {
        //         return {total:res.data.totalRecord, rows: res.data.list};
        //     } else {
        //         layerTips.msg('查询失败');
        //         return false;
        //     }
        // }
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

layui.use(['form', 'layedit', 'laydate', 'loading', 'common'], function () {
    list.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        loading = layui.loading,
        common = layui.common;

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

    $("#btn_download").on("click", function () {
        $("#downloadFrame").prop('src', list.baseUrl + '/download');
        return false;
    });

    $("#btn_collect").on("click", function () {
        dataConfig();
    });

    $('#list').on('click','.view', function () {
        if (addBoxIndex !== -1)
            return;
        var id = $(this).attr('data-id');
        $.get('detail.html', null, function (form) {
            addBoxIndex = layer.open({
                type: 1,
                title: '详情',
                content: form,
                btn: ['关闭'],
                shade: false,
                offset: ['20px', '20%'],
                area: '700px',
                maxmin: true,
                yes: function (index) {
                    layer.close(index);
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

                    $.get(list.baseUrl + '/detail?id=' + id, null, function (data) {
                        if(data.code == 'ACK') {
                            var result = data.data;
                            for (var p in result) {
                                var element = layero.find("#" + p);
                                if (result[p]) {
                                    element.text(result[p]);
                                } else {
                                    element.text('-');
                                }
                            }
                        }
                    });
                },
                end: function () {
                    addBoxIndex = -1;
                }
            });
        });
        return false;
    });

    function dataConfig() {
        $.get(list.baseUrl + '/getConfig', null, function (data) {
            var result = data.result;
            $.get('dataSettings.html', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '采集配置',
                    content: form,
                    btn: ['保存', '取消'],
                    offset: ['20px', '20%'],
                    area: ['700px', '500px'],
                    maxmin: true,
                    yes: function (index) {
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
                        //setFromValues(layero, result);

                        common.handleBootstrapDatetimepicker();
                        //初始化值
                        if(!isEmpty(result)){
                            $('#switchBtn').prop('checked',result.unlimited);

                            $('#provinceCode').val(result.provinceCode);
                            // $('#excuteCycle').val(result.excuteCycle);
                            $('#dayRange').val(result.dayRange);
                            $('#selectRange').val(result.selectRange);

                            //初始值
                            // if(!isEmpty(result.times)) {
                            //     $('#times').datetimepicker('setDate', new Date("2000-01-01" + ' ' + result.times));
                            // }

                            if(result.selectRange == 'months') {
                                $('#dayRangeDiv').hide();
                                $('#collectBeginDate').val(result.beginDate);
                                $('#collectEndDate').val(result.endDate);
                            } else if(result.selectRange == 'days') {
                                $('#collectBeginDateDiv').hide();
                                $('#collectEndDateDiv').hide();
                                $('#dayRange').val(result.dayRange);
                            }
                        } else {
                            toggleBankConfig(false);
                        }


                        //格式化开始日期和结束日期
                        var nowDate = new Date();//当前日期

                        var startDate = dateBefore(nowDate, 60);

                        //监听指定开关
                        form.on('switch(switchBtn)', function(data){
                            toggleBankConfig(this.checked);
                        });

                        //form.render();
                        var form = layui.form();

                        form.on('select(selectRange)', function(data){
                            if(data.value == 'months') {
                                $('#collectBeginDateDiv').show();
                                $('#collectEndDateDiv').show();
                                $('#dayRangeDiv').hide();
                            } else if(data.value == 'days') {
                                $('#collectBeginDateDiv').hide();
                                $('#collectEndDateDiv').hide();
                                $('#dayRangeDiv').show();
                            }

                        })

                        form.on('submit(edit)', function (data) {
                            var unlimited = $('#switchBtn').prop('checked');
                            var startTime;
                            // var times;
                            var endTime;
                            var provinceCode;
                            // var excuteCycle;
                            var selectRange;
                            var dayRange;

                            if(unlimited){
                                //配置采集
                                provinceCode = $('#provinceCode').val();
                                // excuteCycle = $('#excuteCycle').val();
                                selectRange = $('#selectRange').val();
                                dayRange = $('#dayRange').val();

                                // times = $('#times> .form-control').val();
                                startTime = $('#collectBeginDate').val();
                                endTime = $('#collectEndDate').val();

                                if(isEmpty(provinceCode)){
                                    layerTips.msg('省份编号不能为空');
                                    return false;
                                }

                                // if($('#excuteCycle').val() == "") {
                                //     layerTips.msg('任务执行周期不能为空');
                                //     return false;
                                // }

                                if($('#selectRange').val() == "") {
                                    layerTips.msg('获取范围不能为空');
                                    return false;
                                }

                                if($('#selectRange').val() == 'months') {
                                    if(isEmpty(startTime)){
                                        layerTips.msg('请输入新注册企业采集开始时间');
                                        return false;
                                    }
                                    // if(isEmpty(endTime)) {
                                    //     layerTips.msg('请输入新注册企业采集结束时间');
                                    //     return false;
                                    // }
                                    if(startTime > formatDate(new Date(), 'yyyy-MM-dd')) {
                                        layerTips.msg("开始日期不能大于当前日期");
                                        return false;
                                    }

                                    if(!isEmpty(endTime) && startTime > endTime) {
                                        layerTips.msg("时间筛选开始时间不能大于结束时间");
                                        return false;
                                    }
                                } else if($('#selectRange').val() == 'days') {
                                    if(!isEmpty(dayRange) && (dayRange > 60 || dayRange < 1)) {
                                        layerTips.msg("天数范围在1-60之间");
                                        return false;
                                    }
                                }

                            }

                            var param = {
                                // times:times,
                                unlimited:unlimited,
                                provinceCode: provinceCode,
                                beginDate:startTime,
                                endDate:endTime,
                                // excuteCycle: excuteCycle,
                                selectRange: selectRange,
                                dayRange: dayRange
                            };
                            $.ajax({
                                url: list.baseUrl + '/saveConfig',
                                type: 'GET',
                                data: param,
                                dataType: "json",
                                success: function (result) {
                                    if(result.rel){
                                        layerTips.msg('更新成功');
                                        layer.close(addBoxIndex);
                                    } else {
                                        layerTips.msg(result.msg);
                                    }

                                },
                                error: function (err) {
                                    layerTips.msg('更新异常');
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });

                        form.render();

                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });

            });
        });
    }

    function toggleBankConfig(state) {
        if(state){
            //无限制
            $(".form_datetime").datetimepicker({
                startView: 0,
                minView: 0,
                autoclose: true,
                language: 'zh-CN'
            });
            $(".form_datetime> .form-control").prop('disabled', false);

            $(".form_date").datetimepicker({
                startView: 0,
                minView: 0,
                autoclose: true,
                language: 'zh-CN'
            });
            $(".form_date> .form-control").prop('disabled', false);

            $("#provinceCode").prop('disabled', false);
            // $("#excuteCycle").prop('disabled', false);
            $("#selectRange").prop('disabled', false);
            $("#dayRange").prop('disabled', false);
        } else {
            //限制
            $(".form_datetime> .form-control").val('');
            $(".form_datetime> .form-control").prop('disabled', true);

            $(".form_date> .form-control").val('');
            $(".form_date> .form-control").prop('disabled', true);

            $("#provinceCode").val('');
            $("#provinceCode").prop('disabled', true);
            // $("#excuteCycle").val('');
            // $("#excuteCycle").prop('disabled', true);
            $("#selectRange").val('');
            $("#selectRange").prop('disabled', true);
            $("#dayRange").val('');
            $("#dayRange").prop('disabled', true);
        }
        form.render();
    }
});

