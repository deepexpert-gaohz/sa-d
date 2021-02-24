layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var result = {};
var isAnnualSubmitBtn;


function accountStatusFormat(value) {
    if (value === 'normal') {
        return '正常';
    } else if (value === 'suspend') {
        return '久悬';
    } else if (value === 'revoke') {
        return '销户';
    } else if (value === 'unKnow') {
        return '未知';
    } else if (value === 'notExist') {
        return '不存在';
    } else if (value === 'notActive') {
        return '未激活';
    } else {
        return '';
    }
}

layui.use(['common', 'loading', 'annual', 'murl'], function () {
    var $ = layui.jquery,
        common = layui.common,
        annual = layui.annual,
        url = layui.murl;

    var taskId = url.get("taskId");
    var hisAnnualTaskId = url.get("taskId");

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    result.tableOption = function (options) {
        $.ajax({
            url: '../../permission/element/code?code=annual:submit',
            type: 'get',
            dataType: "json",
            async: false,
            success: function (res) {
                if (res === true) {   //年检提交按钮权限显示
                    isAnnualSubmitBtn = true;
                } else {   ////年检提交按钮权限隐藏
                    isAnnualSubmitBtn = false;
                }
            }

        });

        return $.extend({
            "ordering": false,

            "columns": [
                {
                    "orderable": false,
                    "className": 'dt-right',
                    "targets": 0,
                    "data": "id",
                    "render": function (data, type, full, meta) {
                        return '<label class="mt-checkbox mt-checkbox-outline mt-checkbox-single"><input type="checkbox" class="checkboxes" value="' +
                            data + '" /><span></span></label>';
                    }
                },
                {"data": "acctNo", "targets": 1, "orderable": false},
                {"data": "depositorName", "targets": 2, "orderable": false},
                {"data": "organPbcCode", "targets": 3, "orderable": false},
                {"data": "organCode", "targets": 4, "orderable": false},
                {
                    "data": "unilateral", "targets": 5, "orderable": false,
                    "render": function (data) {
                        if (data === 'PBC') {
                            return "人行单边";
                        } else if (data === 'CORE') {
                            return "核心单边";
                        } else if (data === 'NONE') {
                            return "正常";
                        } else {
                            return "";
                        }
                    }
                },
                {
                    "data": "abnormal", "targets": 6, "orderable": false,
                    "render": function (data) {
                        if (data !== '' && data != null) {
                            return abnormalConvert(data);
                        } else {
                            return "";
                        }
                    }
                },
                {
                    "data": "saicStatus", "targets": 7, "orderable": false,
                    "render": function (data) {
                        if (data === 'REGISTERED') {
                            return "在册";
                        } else if (data === 'CANCEL') {
                            return "注销撤销";
                        } else if (data === 'REVOKE') {
                            return "吊销";
                        } else if (data === 'QUIT') {
                            return "迁出";
                        } else if (data === 'OTHER') {
                            return "其他";
                        } else if (data === 'NOTFOUND' || data == null) {
                            return "未找到";
                        } else {
                            return "";
                        }
                    }
                },
                {
                    "data": "match", "targets": 9, "orderable": false,
                    "render": function (data) {
                        var status = {
                            icon: 'fa-refresh fa-times',
                            cssClass: 'label-danger',
                            label: '不一致'
                        };
                        if (data) {
                            status.icon = 'fa-check-circle';
                            status.cssClass = 'label-success';
                            status.label = '一致';
                        }

                        return '<span class="label label-sm ' + status.cssClass + '" >' +
                            '    <i class="fa ' + status.icon + '"></i>' +
                            status.label +
                            '</span>';

                    }
                },
                {
                    "data": null, "targets": 10, "orderable": false,
                    "render": function (data) {
                        if (data.dataProcessStatus === 'PROCESSING') {
                            return '<a href="javascript:void(0)" data-id="' + data.id + '" data-name="' + data.dataProcessPerson + '" data-date="' + data.dataProcessDate + '" class="dispose-detail">正在处理</a>';
                        } else {
                            return "未处理";
                        }
                    }
                },
                {
                    "data": null, "targets": 11,
                    "orderable": false,
                    "render": function (data) {
                        return layui.laytpl($('#detailActions').html()).render({
                            core: data.coreData != null,
                            id: data.id,
                            saic: data.saicData != null,
                            bank: data.pbcData != null,
                            result: true,
                            submit: isAnnualSubmitBtn
                        });

                    }
                }
            ],

            "ajax": function (data, callback, settings) {
                result.searchHistory(data, callback, 'waitingProcess');
            }
        }, options);
    };

    result.systemSuccTableOption = function (options) {
        return $.extend({
            "ordering": false,

            "columns": [
                {"data": "acctNo", "targets": 1, "orderable": false},
                {"data": "depositorName", "targets": 2, "orderable": false},
                {"data": "organPbcCode", "targets": 3, "orderable": false},
                {"data": "organCode", "targets": 4, "orderable": false},
                {
                    "data": "result", "targets": 5, "orderable": false,
                    "render": function (data) {
                        if (data === 'PASS') {
                            return '年检通过';
                        } else if (data === 'FAIL') {
                            return '年检失败';
                        } else if (data === 'INIT') {
                            return '未年检';
                        }
                    }
                },
                {
                    "data": "match", "targets": 6, "orderable": false,
                    "render": function (data) {
                        var status = {
                            icon: 'fa-refresh fa-times',
                            cssClass: 'label-danger',
                            label: '不一致'
                        };
                        if (data === true) {
                            status.icon = 'fa-check-circle';
                            status.cssClass = 'label-success';
                            status.label = '一致';
                        }

                        return '<span class="label label-sm ' + status.cssClass + '" >' +
                            '    <i class="fa ' + status.icon + '"></i>' +
                            status.label +
                            '</span>';

                    }
                },
                {
                    "data": null, "targets": 7,
                    "orderable": false,
                    "render": function (data) {
                        return layui.laytpl($('#detailActions').html()).render({
                            core: data.coreData != null,
                            id: data.id,
                            saic: data.saicData != null,
                            bank: data.pbcData != null,
                            result: true
                        });
                    }
                }
            ],

            "ajax": function (data, callback, settings) {
                result.searchHistory(data, callback, 'systemSuccess');
            }
        }, options);
    };

    result.manualSuccTableOption = function (options) {
        return $.extend({
            "ordering": false,

            "columns": [
                {"data": "acctNo", "targets": 1, "orderable": false},
                {"data": "depositorName", "targets": 2, "orderable": false},
                {"data": "organPbcCode", "targets": 3, "orderable": false},
                {"data": "organCode", "targets": 4, "orderable": false},
                {
                    "data": "abnormal", "targets": 5, "orderable": false,
                    "render": function (data) {
                        if (data !== '' && data != null) {
                            return abnormalConvert(data);
                        } else {
                            return "";
                        }
                    }
                },
                {"data": "pbcSubmitter", "targets": 6, "orderable": false},
                {"data": "pbcSubmitDate", "targets": 7, "orderable": false},
                {
                    "data": null, "targets": 8,
                    "orderable": false,
                    "render": function (data) {
                        return layui.laytpl($('#detailActions').html()).render({
                            core: data.coreData != null,
                            id: data.id,
                            saic: data.saicData != null,
                            bank: data.pbcData != null,
                            result: true,
                            submit: isAnnualSubmitBtn
                        });

                    }
                }
            ],

            "ajax": function (data, callback, settings) {
                result.searchHistory(data, callback, 'manualSuccess');
            }
        }, options);
    };

    result.annualFailTableOption = function (options) {
        return $.extend({
            "ordering": false,

            "columns": [
                {
                    "orderable": false,
                    "className": 'dt-right',
                    "targets": 0,
                    "data": "id",
                    "render": function (data, type, full, meta) {
                        return '<label class="mt-checkbox mt-checkbox-outline mt-checkbox-single"><input type="checkbox" class="checkboxes" value="' +
                            data + '" /><span></span></label>';
                    }
                },
                {"data": "acctNo", "targets": 1, "orderable": false},
                {"data": "depositorName", "targets": 2, "orderable": false},
                {"data": "organPbcCode", "targets": 3, "orderable": false},
                {"data": "organCode", "targets": 4, "orderable": false},
                {
                    "data": "abnormal", "targets": 5, "orderable": false,
                    "render": function (data) {
                        if (data !== '' && data != null) {
                            return abnormalConvert(data);
                        } else {
                            return "";
                        }
                    }
                },
                {"data": "pbcSubmitErrorMsg", "targets": 6, "orderable": false},
                {
                    "data": null, "targets": 7,
                    "orderable": false,
                    "render": function (data) {
                        return layui.laytpl($('#detailActions').html()).render({
                            core: data.coreData != null,
                            id: data.id,
                            saic: data.saicData != null,
                            bank: data.pbcData != null,
                            result: true,
                            submit: isAnnualSubmitBtn
                        });
                    }
                }
            ],

            "ajax": function (data, callback, settings) {
                result.searchHistory(data, callback, 'fail');
            }
        }, options);
    };

    result.noCheckAnnualTableOption = function (options) {
        return $.extend({
            "ordering": false,

            "columns": [
                {
                    "orderable": false,
                    "className": 'dt-right',
                    "targets": 0,
                    "data": "id",
                    "render": function (data, type, full, meta) {
                        return '<label class="mt-checkbox mt-checkbox-outline mt-checkbox-single"><input type="checkbox" class="checkboxes" value="' +
                            data + '" /><span></span></label>';
                    }
                },
                {"data": "acctNo", "targets": 1, "orderable": false},
                {"data": "depositorName", "targets": 2, "orderable": false},
                {"data": "organPbcCode", "targets": 3, "orderable": false},
                {"data": "organCode", "targets": 4, "orderable": false},
                {
                    "data": "abnormal", "targets": 5, "orderable": false,
                    "render": function (data) {
                        if (data !== '' && data != null) {
                            return abnormalConvert(data);
                        } else {
                            return "";
                        }
                    }
                },
                {"data": "pbcSubmitErrorMsg", "targets": 6, "orderable": false},
                {
                    "data": null, "targets": 7,
                    "orderable": false,
                    "render": function (data) {
                        return layui.laytpl($('#detailActions').html()).render({
                            core: data.coreData != null,
                            id: data.id,
                            saic: data.saicData != null,
                            bank: data.pbcData != null,
                            result: true,
                            submit: isAnnualSubmitBtn
                        });
                    }
                }
            ],

            "ajax": function (data, callback, settings) {
                result.searchHistory(data, callback, 'noCheck');
            }
        }, options);
    };

    result.init = function (options) {
        result.table = $('#processTable').dataTable(result.tableOption(options));
        result.systemSuccTable = $('#systemSuccTable').dataTable(result.systemSuccTableOption(options));
        result.manualSuccTable = $('#manualSuccTable').dataTable(result.manualSuccTableOption(options));
        result.annualFailTable = $('#annualFailTable').dataTable(result.annualFailTableOption(options));
        result.noCheckAnnualTable = $('#noCheckAnnualTable').dataTable(result.noCheckAnnualTableOption(options));

    };

    result.searchHistory = function (data, callback, type) {
        //封装请求参数
        var param = {};
        param.size = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
        param.page = (data.start / data.length);//当前页码

        param.code = type;

        if (taskId !== undefined && taskId !== null) {
            param.taskId = taskId;
        }

        if (type === 'waitingProcess') {
            param.acctNo = $.trim($("#searchAccount").val());
            param.depositorName = $.trim($("#searchName").val());
            param.organPbcCode = $.trim($("#searchPbcCode").val());
            param.organCode = $.trim($("#searchCode").val());
            param.unilateral = $.trim($("#searchUnilateral").val());
            // param.result = $.trim($("#searchAnnualStatus").val());
            param.match = $.trim($("#searchIsSame").val());

            param.saicStatuses = $.trim($("#searchSaicStatus").val());
            // param.pbcSubmitStatuses = $.trim($("#searchSaicStatus").val());
            param.results = $.trim($("#searchAnnualStatus").val());

            //处理状态
            param.disposeStatus = $.trim($("#dataProcessStatus").val());
            //处理人
            param.disposePerson = $.trim($("#dataProcessPerson").val());


            //当前查询字段
            $.each($('#annualProcess .form-control'), function (key, value) {
                var name = $(value).attr('name');
                param[name] = $(value).val();
            });
        } else if (type === 'systemSuccess') {
            param.acctNo = $.trim($("#searchAccount2").val());
            param.depositorName = $.trim($("#searchName2").val());
            param.organPbcCode = $.trim($("#searchPbcCode2").val());
            param.organCode = $.trim($("#searchCode2").val());

            //当前查询字段
            $.each($('#annualProcess2 .form-control'), function (key, value) {
                var name = $(value).attr('name');
                param[name] = $(value).val();
            });
        } else if (type === 'manualSuccess') {
            param.acctNo = $.trim($("#searchAccount3").val());
            param.depositorName = $.trim($("#searchName3").val());
            param.organPbcCode = $.trim($("#searchPbcCode3").val());
            param.organCode = $.trim($("#searchCode3").val());
            //处理人
            param.dataProcessPerson = $.trim($("#dataProcessPerson3").val());
            //处理日期
            param.dataProcessDate = $.trim($("#dataProcessDate3").val());

            //当前查询字段
            $.each($('#annualProcess3 .form-control'), function (key, value) {
                var name = $(value).attr('name');
                param[name] = $(value).val();
            });
        } else if (type === 'fail') {
            param.acctNo = $.trim($("#searchAccount4").val());
            param.depositorName = $.trim($("#searchName4").val());
            param.organPbcCode = $.trim($("#searchPbcCode4").val());
            param.organCode = $.trim($("#searchCode4").val());
            //处理人
            param.abnormal = $.trim($("#abnormal4").val());
            //处理日期
            param.pbcSubmitErrorMsg = $.trim($("#pbcSubmitErrorMsg4").val());

            //当前查询字段
            $.each($('#annualProcess4 .form-control'), function (key, value) {
                var name = $(value).attr('name');
                param[name] = $(value).val();
            });
        } else if (type === 'noCheck') {
            param.acctNo = $.trim($("#searchAccount5").val());
            param.depositorName = $.trim($("#searchName5").val());
            param.organPbcCode = $.trim($("#searchPbcCode5").val());
            param.organCode = $.trim($("#searchCode5").val());
            //处理人
            param.abnormal = $.trim($("#abnormal5").val());
            //处理日期
            param.pbcSubmitErrorMsg = $.trim($("#pbcSubmitErrorMsg5").val());

            //当前查询字段
            $.each($('#annualProcess5 .form-control'), function (key, value) {
                var name = $(value).attr('name');
                param[name] = $(value).val();
            });
        }

        result.param = param;

        result.jqueryDataTableAjax('../../annualResult/annualResultByCode', param, callback, data, result.layerTips);
    };


    result.layerTips = layerTips;
    result.jqueryDataTableAjax = common.jqueryDataTableAjax;
    result.init(common.tableDefaulOptions());

    //绑定checkbox
    result.checkRows = {};
    common.bindTableCheckboxAction($('#processTable'), result.checkRows, result.table);

    //绑定checkbox
    result.checkRowsOnFail = {};
    common.bindTableCheckboxAction($('#annualFailTable'), result.checkRowsOnFail, result.annualFailTable);

    //批量提交
    $('.batchAnnualSubmit').on('click', function () {
        var selected = common.getSelectedRows(result.checkRows);

        if (selected.length === 0) {
            layerTips.msg('请选择要提交年检数据');
            return;
        }
        layerTips.confirm('确认提交选中项年检数据吗？', {
            btn: ['确认', '取消'] //按钮
        }, function (index, layero) {
            layerTips.close(index);
            //todo 批量接口
            $.ajax({
                url: '../../annualResult/submit',
                type: 'post',
                data: {ids: selected},
                dataType: "json",
                success: function (res) {
                    openAnnualAgainModel(res);
                    result.table.api().ajax.reload(null, false);
                    result.checkRows = {};
                    common.bindTableCheckboxAction($('#processTable'),result.checkRows,result.table);
                }
            });
        }, function () {
            //取消
        });
    });

    $('.batchAnnualDispose').on('click', function () {
        var selected = common.getSelectedRows(result.checkRows);

        if (selected.length === 0) {
            layerTips.msg('请选择要处理年检数据');
            return;
        }

        layerTips.confirm('确定处理选中项年检数据吗？', {
            btn: ['确认', '取消'] //按钮
        }, function (index, layero) {
            layerTips.close(index);
            $.ajax({
                url: '../../annualResult/dataProcessSubmit',
                type: 'post',
                data: {ids: selected},
                dataType: "json",
                success: function (res) {
                    if (res.result) {
                        layerTips.msg('选中的数据中有已处理的数据，请重新选择...');
                    }
                    result.table.api().ajax.reload(null, false);
                    result.checkRows = {};
                    common.bindTableCheckboxAction($('#processTable'),result.checkRows,result.table);
                }
            });
        }, function () {
            //取消
        });
    });

    //批量提交在失败页面
    $('.batchAnnualOnFailSubmit').on('click', function () {
        var selected = common.getSelectedRows(result.checkRowsOnFail);

        if (selected.length === 0) {
            layerTips.msg('请选择要提交年检数据');
            return;
        }

        layerTips.confirm('确认提交选中项年检数据吗？', {
            btn: ['确认', '取消'] //按钮
        }, function (index, layero) {
            layerTips.close(index);
            $.ajax({
                url: '../../annualResult/submit',
                type: 'post',
                data: {ids: selected},
                dataType: "json",
                success: function (res) {
                    openAnnualAgainModel(res);
                    // window.location.reload();
                    result.table.api().ajax.reload(null, false);
                }

            });
        }, function () {
            //取消
        });
    });

    //年检待处理历史查询按钮
    $('#searchAnnualBtn').on('click', function () {
        result.table.api().ajax.reload();
    });

    //历史重置
    $('#resetSearchAnualBtn').on('click', function () {
        resetAnnualSearchField();
    });

    function resetAnnualSearchField() {
        $.each($('#annualProcess .form-control'), function (key, value) {
            $(value).val('');
        });
    }

    //系统年检成功查询按钮
    $('#searchAnnualBtn2').on('click', function () {
        result.systemSuccTable.api().ajax.reload();
    });

    //历史重置
    $('#resetSearchAnualBtn2').on('click', function () {
        resetSystemSearchField();
    });

    function resetSystemSearchField() {
        $.each($('#annualProcess2 .form-control'), function (key, value) {
            $(value).val('');
        });
    }

    //手工处理成功查询按钮
    $('#searchAnnualBtn3').on('click', function () {
        result.manualSuccTable.api().ajax.reload();
    });

    //历史重置
    $('#resetSearchAnualBtn3').on('click', function () {
        resetManualSuccSearchField();
    });

    function resetManualSuccSearchField() {
        $.each($('#annualProcess3 .form-control'), function (key, value) {
            $(value).val('');
        });
        $('#dataProcessDate3').val('');
    }

    //手工处理成功查询按钮
    $('#searchAnnualBtn4').on('click', function () {
        result.annualFailTable.api().ajax.reload();
    });

    //历史重置
    $('#resetSearchAnualBtn4').on('click', function () {
        resetAnnualFailSearchField();
    });

    function resetAnnualFailSearchField() {
        $.each($('#annualProcess4 .form-control'), function (key, value) {
            $(value).val('');
        });
    }

    //表格操作
    $('.dataTable').on('click', '.dispose-detail', function () {
        var id = $(this).data('id');
        var name = $(this).data('name');
        var date = $(this).data('date');

        var self;
        var info = '<div style="padding: 20px 50px;">处理操作人：' + name + ' <br>处理操作时间：' + date + '</div>';

        $.ajax({
            url: '../../annualResult/checkSelf',
            type: 'post',
            data: {id: id},
            dataType: "json",
            sync: false,
            success: function (result) {
                self = result.result;
                if (self) {
                    layer.open({
                        type: 1
                        , content: info
                        , btn: ['撤回处理', '返回']
                        , yes: function () {
                            //撤回处理
                            $.ajax({
                                url: '../../annualResult/recall',
                                type: 'post',
                                data: {id: id},
                                dataType: "json",
                                sync: false,
                                success: function (result) {
                                    window.location.reload();
                                }
                            });
                        }
                    });
                } else {
                    layer.open({
                        type: 1
                        , content: info
                        , btn: '返回'
                    });
                }
            }
        });
    });

    $('.dataTable').on('click', '.core-detail', function () {
        annual.loadAnnualDataTemplate($(this).data('id'), '数据详情', 'view_template.html');
    });
    $('.dataTable').on('click', '.result-detail', function () {
        annual.loadCompareResult($(this).data('id'), '年检比对详情', 'view_compare.html');
    });

    //刷新操作
    $('#processTableId').on('click', function () {
        result.table.api().ajax.reload(null, false);
    });

    //刷新操作
    $('#systemSuccTableId').on('click', function () {
        result.systemSuccTable.api().ajax.reload(null, false);
    });
    //刷新操作
    $('#manualSuccTableId').on('click', function () {
        result.manualSuccTable.api().ajax.reload(null, false);
    });

    //刷新操作
    $('#annualFailTableId').on('click', function () {
        result.annualFailTable.api().ajax.reload(null, false);
    });

    //年检统计导出
    $('#annualExport').on('click', function () {
        // $.get('../../annualTask/getAnnualTaskId', function (taskId) {
            $("#downloadFrame").prop('src', '../../annualTask/statistics/export/' + hisAnnualTaskId);
            // window.location.href = '../../annualTask/statistics/export/' + taskId;
            return false;
        // });

        // return false;
    });


    //年检结果导出
    $('#annualExportWord').on('click', function () {
        window.location.href = "../../annualResult/downloadHis?hisTaskId=" + hisAnnualTaskId;
        return false;
    });

});
/**
 * 从layui内挪出来
 * 判断返回的年检结果数据是否全部没有通过年检
 * @param data
 */
function annualAgainIsFailure(data) {
    for (var i = 0; i < data.length; i++) {
        if (data[i].forceStatus === 'AGAIN_SUCCESS' || data[i].forceStatus === 'SUCCESS') {
            return false;
        }
    }
    return true;
}

/**
 * 从layui内挪出来
 * 判断返回的年检结果数据是否全部通过年检
 * @param data
 */
function annualAgainIsSuccess(data) {
    for (var i = 0; i < data.length; i++) {
        if (data[i].forceStatus !== 'AGAIN_SUCCESS'&& data[i].forceStatus !== 'SUCCESS') {
            return false;
        }
    }
    return true;
}

/**
 * 从layui内挪出来
 * 重新年检结果弹出层
 * @param res
 */
function openAnnualAgainModel(res){

    if (res.result.length === 0){
        layer.open({
            type: 1,
            title: '年检结果',
            area: ['300px', '150px'], //宽高
            content: '<div style="text-align:center;margin-top:20px">无需提交</div>',
            btn: ['确定']
        });
    }else if (res.result.length === 1) {//手工批量提交-重新年检（年检成功的提交） 单条 & 手工提交
        if (annualAgainIsSuccess(res.result)) {
            layer.open({
                type: 1,
                title: '年检结果',
                area: ['300px', '150px'], //宽高
                content: '<div style="text-align:center;margin-top:20px">手动年检通过</div>',
                btn: ['确定']
            });
        } else {
            layer.open({
                type: 1,
                title: '年检结果',
                area: ['300px', '150px'], //宽高
                content: '<div style="text-align:center;margin-top:20px">手动年检失败，请处理后重新提交！</div>',
                btn: ['确定']
            });
        }
    } else {//手工批量提交-重新年检（年检成功的提交） 多条
        //初始化重新年检结果表格
        if (annualAgainIsSuccess(res.result)) {
            layer.open({
                type: 1,
                title: '年检结果',
                area: ['300px', '150px'], //宽高
                content: '<div style="text-align:center;margin-top:20px">手动年检全部通过</div>',
                btn: ['确定']
            });
        } else {
            var titleHtml = '<div style="text-align:center;margin-top:20px">手动年检部分通过，请处理后重新提交！</div>';
            if (annualAgainIsFailure(res.result)) {
                titleHtml = '<div style="text-align:center;margin-top:20px">手动年检全部失败，请处理后重新提交！</div>';
            }
            layer.open({
                type: 1,
                title: '年检结果',
                area: ['300px', '150px'], //宽高
                content: titleHtml,
                btn: ['确定', '查看详情'],
                yes: function () {
                    layer.closeAll();
                },
                btn2: function (index) {
                    parent.layui.annualAgainResultData = res.result;
                    parent.tab.tabAdd({
                        title: '年检结果详情',
                        href: 'annualcheck/againResult.html'
                    });
                }
            });
        }
    }
}

function batchAnnualSubmit(annualResultId) {
    var selected = new Array(1);
    selected[0] = annualResultId;

    result.layerTips.confirm('确认提交选中项年检数据吗？', {
        btn: ['确认', '取消'] //按钮
    }, function (index, layero) {
        result.layerTips.close(index);
        //todo 批量接口
        $.ajax({
            url: '../../annualResult/submit',
            type: 'post',
            data: {ids: selected},
            dataType: "json",
            success: function (res) {
                openAnnualAgainModel(res);
                result.table.api().ajax.reload(null, false);
                result.manualSuccTable.api().ajax.reload(null, false);
            }
        });
    }, function () {
        //取消
    });
}

function batchAnnualDispose(annualResultId) {
    var selected = new Array(1);
    selected[0] = annualResultId;

    result.layerTips.confirm('确定处理选中项年检数据吗？', {
        btn: ['确认', '取消'] //按钮
    }, function (index, layero) {
        result.layerTips.close(index);
        $.ajax({
            url: '../../annualResult/dataProcessSubmit',
            type: 'post',
            data: {ids: selected},
            dataType: "json",
            success: function (res) {
                if (res.result) {
                    result.layerTips.msg('选中的数据中有已处理的数据，请重新选择...');
                } else {
                    result.layerTips.msg('已处理');
                }
                result.table.api().ajax.reload(null, false);
            }
        });
    }, function () {
        //取消
    });
}