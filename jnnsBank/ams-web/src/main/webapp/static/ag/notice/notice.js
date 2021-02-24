layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'laydate','upload'], function () {
    var editIndex;
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        layedit = layui.layedit,
        common = layui.common,
        loading = layui.loading,
        upload = layui.upload,
        laydate = layui.laydate;

    var tabId = decodeURI(common.getReqParam("tabId"));
    var noticeType = decodeURI(common.getReqParam("noticeType"));
    var checked =true;
    var role =0;
    var fileOverConfigEnabled;
    var tempAcctOverConfigEnabled;
    var legalOverConfigEnabled;
    var operatorOverConfigEnabled;

    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    laydate.render({
        elem: '#effectiveDate',
        format: 'yyyy-MM-dd'
    });
    laydate.render({
        elem: '#fileDue',
        format: 'yyyy-MM-dd'
    });
    laydate.render({
        elem: '#fileDue2',
        format: 'yyyy-MM-dd'
    });
    laydate.render({
        elem: '#legalIdcardDue',
        format: 'yyyy-MM-dd'
    });
    laydate.render({
        elem: '#operatorIdcardDue',
        format: 'yyyy-MM-dd'
    });

   $.ajax({
        type: "get",
        url: '../../notice/overNoticeConfig',
        async: false,
        success: function (res) {
            data = res.data;
            fileOverConfigEnabled = data.fileOverConfigEnabled;
            tempAcctOverConfigEnabled = data.tempAcctOverConfigEnabled;
            legalOverConfigEnabled = data.legalOverConfigEnabled;
            operatorOverConfigEnabled = data.operatorOverConfigEnabled;
        },
        error: function () {
            layer.msg("error");
        }
    });

    var notice = {
        baseUrl: "../../notice",
        jnnsUrl: "../../testJnns",
        tableId: "noticeList",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };
    if(noticeType === 'tempAcct'){

        $('#tempAcctContionDiv').css('display', '');
        $('#fileDue2Div').css('display', '');

        //临时户到期提醒列表
        notice.columns = function () {
            return [{
                radio: true
            },{
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'acctNo',
                title: '账号',
                'class': 'W200'

            }, {
                field: 'acctName',
                title: '账户名称',
                'class': 'W200'
            }, {
                field: 'bankCode',
                title: '人行机构号',
                'class': 'W120'
            }, {
                field: 'bankName',
                title: '开户行',
                'class': 'W120'
            }, {
                field: 'acctCreateDate',
                title: '开户日期',
                'class': 'W120'
            }, {
                field: 'effectiveDate',
                title: '到期日期',
                'class': 'W120'
            },{
                field: 'isEffectiveDateOver',
                title: '是否超期',
                formatter: function (value, row, index) {
                    if(value == true) {
                        return "是";
                    } else if(value == false) {
                        return "否";
                    }else {
                        return "";
                    }
                }
            }/*, {
                field: 'ckeckStatus',
                title: '核实状态',
                'class': 'W120'
            },*/];
        };
    } else if(noticeType === 'operatorDueNotice') {
        $('#ckeckStatus').css('display', 'none');
        notice.columns = function () {
            return [{
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'acctNo',
                title: '账号',
                'class': 'W200'

            }, {
                field: 'acctName',
                title: '账户名称',
                'class': 'W200'
            }, {
                field: 'bankCode',
                title: '人行机构号',
                'class': 'W120'
            }, {
                field: 'bankName',
                title: '开户行',
                'class': 'W120'
            }, {
                field: 'operatorIdcardType',
                title: '证件类型',
                'class': 'W120'
            }, {
                field: 'operatorIdcardNo',
                title: '证件编号',
                'class': 'W120'
            }, {
                field: 'acctCreateDate',
                title: '开户日期',
                'class': 'W120'
            }, {
                field: 'operatorIdcardDue',
                title: '到期日期',
                'class': 'W120'
            },{
                field: 'refBillId',
                title: '关联最新流水',
                visible: false
            },{
                field: 'acctType',
                title: '账户性质',
                visible: false
            },{
                field: 'string001',
                title: '操作类型',
                visible: false
            },{
                field: 'string002',
                title: '企业名称',
                visible: false
            }, {
                field: 'operate',
                title: '操作',
                formatter: function (value, row, index) {
                    var html = "<a href=\"javascript:void(0);\" style='color: blue' onclick='tempToAcctDeatil(" +
                        "\"" + (row.string002 == null ? "" : row.string002) + "\"," +
                        "\"" + (row.refBillId == null ? "" : row.refBillId) + "\"," +
                        "\"" + (row.string001 == null ? "" : row.string001) + "\"," +
                        "\"" + (row.acctType == null ? "" : row.acctType) + "\"" +
                        ")'>查看</a>";

                    return html;
                }
            },{
                field: 'isOperatorIdcardDue',
                title: '是否超期',
                formatter: function (value, row, index) {
                    if(value == true) {
                        return "是";
                    } else if(value == false) {
                        return "否";
                    } else {
                        return "";
                    }
                }
            }];
        };
    } else if(noticeType == 'fileDueNotice') {
        $('#fileDueDiv').css('display', '')
        $('#fileDue2Div').css('display', '')
        $('#isFileDueOverDiv').css('display', '')

        notice.columns = function () {
            return [
                {
                    radio: true
                },{
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'depositorName',
                title: '企业名称',
                'class': 'W200'

            },{
                field: 'fileType',
                title: '证明文件名称',
                formatter: function (value, row, index) {
                        if(value == 00) {
                            return "社会统一信用代码";
                        } else if(value == 01) {
                            return "工商营业执照";
                        } else if(value == 02) {
                            return "批文";
                        } else if(value == 03) {
                            return "登记证书";
                        } else if(value == 04) {
                            return "开户证明";
                        } else if(value == 05) {
                            return "其他";
                        } else  {
                            return "";
                        }
                    }
            }, {
                field: 'fileNo',
                title: '证明文件编号',
                'class': 'W100'
            }, {
                field: 'fileSetupDate',
                title: '证明文件设立日期',
                'class': 'W100'
            }, {
                field: 'fileDue',
                title: '证明文件到期日',
                'class': 'W100'
            }, {
                field: 'createdDate',
                title: '创建日期',
                visible: false
            }, {
                field: 'organName',
                title: 'ID',
                visible: false
            }, {
                field: 'customerNo',
                title: 'ID',
                visible: false
            }, {
                field: 'operate',
                title: '操作',
                formatter: function (value, row, index) {
                    var html = "<a href=\"javascript:void(0);\" style='color: blue' onclick='btnCustomerView(" +
                        "\"" + row.id + "\"," +
                        "\"" + (row.depositorName == null ? "" : row.depositorName) + "\"," +
                        "\"" + (row.createdDate == null ? "" : row.createdDate) + "\"," +
                        "\"" + (row.organName == null ? "" : row.organName) + "\"," +
                        "\"" + row.customerNo + "\"" +
                        ")'>查看</a>";

                    return html;
                }
            },{
                field: 'isFileDueOver',
                title: '是否超期',
                formatter: function (value, row, index) {
                    if(value == true) {
                        return "是";
                    } else if(value == false) {
                        return "否";
                    } else {
                        return "";
                    }
                }
            }/*, {
                field: 'checkStatus',
                title: '核实状态',
                'class': 'W120'
            }*/];
        }
    } else if(noticeType == 'legalDueNotice') {
        $('#legalNameDiv').css('display', '')
        $('#legalIdcardDueDiv').css('display', '')
        $('#isLegalIdcardDueOverDiv').css('display', '')

        notice.columns = function () {

            return [ {
                radio: true
            },{
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'depositorName',
                title: '企业名称',
                'class': 'W200'

            },  {
                field: 'legalName',
                title: '法人（负责人）姓名',
                'class': 'W200'
            }, {
                field: 'legalIdcardType',
                title: '证件类型',
                'class': 'W200'
            }, {
                field: 'legalIdcardNo',
                title: '证件号码',
                'class': 'W200'
            }, {
                field: 'legalIdcardDue',
                title: '法人（负责人）证件到期日',
                'class': 'W200'
            }, {
                field: 'createdDate',
                title: '创建日期',
                visible: false
            }, {
                field: 'organName',
                title: 'ID',
                visible: false
            }, {
                field: 'customerNo',
                title: 'ID',
                visible: false
            }, {
                field: 'operate',
                title: '操作',
                formatter: function (value, row, index) {
                    var html = "<a href=\"javascript:void(0);\" style='color: blue' onclick='btnCustomerView(" +
                        "\"" + row.id + "\"," +
                        "\"" + (row.depositorName == null ? "" : row.depositorName) + "\"," +
                        "\"" + (row.createdDate == null ? "" : row.createdDate) + "\"," +
                        "\"" + (row.organName == null ? "" : row.organName) + "\"," +
                        "\"" + row.customerNo + "\"" +
                        ")'>查看</a>";

                    return html;
                }
            },{
                field: 'isLegalIdcardDueOver',
                title: '是否超期',
                formatter: function (value, row, index) {
                    if(value == true) {
                        return "是";
                    } else if(value == false) {
                        return "否";
                    } else {
                        return "";
                    }
                }
            }/*,{
                field: 'checkStatus',
                title: '核实状态',
                'class': 'W120'
            }*/];
        }
    } else if (noticeType == 'resvrUnprocess') {
        $('#resvrUnprocessDiv').show();
        //预约未及时处理列表
        notice.columns = function () {
            return [{
                field: 'id',
                title: 'ID',
                visible: false
            }, {
                field: 'applyid',
                title: '预约号'
            }, {
                field: 'name',
                title: '客户名称',
                'class': 'W200'
            }, {
                field: 'type',
                title: '预约类型',
                'class': 'W120',
                formatter: function (data) {
                    switch (data) {
                        case "jiben":
                            return "基本户";
                        case "yiban":
                            return "一般户";
                        case "yusuan":
                            return "预算单位专用户";
                        case "feiyusuan":
                            return "非预算单位专用户";
                        case "teshu":
                            return "特殊单位专用户";
                        case "linshi":
                            return "临时机构临时户";
                        case "feilinshi":
                            return "非临时机构临时户";
                    }
                    return "";
                }
            }, {
                field: 'createdDate',
                title: '预约时间',
                'class': 'W120',
                formatter: function (value) {
                    return changeDateFormat(value);
                }
            }, {
                field: 'branch',
                title: '预约网点',
                'class': 'W120'
            }];
        };
    }


    notice.queryParams = function (params) {
        if (!params)
            return {
                noticeType: noticeType,
                depositorName: $("#depositorName").val(),
                legalName: $("#legalName").val(),
                legalIdcardDue: $("#legalIdcardDue").val(),
                fileDue: $("#fileDue").val(),
                fileDue2: $("#fileDue2").val(),
                operatorIdcardDue: $("#operatorIdcardDue").val(),
                isOperatorIdcardDue: $("#isOperatorIdcardDue").val(),
                // isFileDueOver: $("#isFileDueOver").val(),
                isFileDueOver: $("#isFileDueOver").val(),
                isLegalIdcardDueOver: $("#isLegalIdcardDueOver").val(),
                effectiveDate: $("#effectiveDate").val(),
                isEffectiveDateOver: $("#isEffectiveDateOver").val(),
                acctNo: $("#acctNo").val(),
                acctName: $("#acctName").val(),
                bankName: $("#bankName").val(),
                applyid:$("#applyid").val(),
                name:$("#name").val(),
                type:$("#type").val(),
                branch:$("#branch").val(),
                createdDateEnd:$("#createdDateEnd").val(),
                createdDateStart:$("#createdDateStart").val(),
                fileNo:$("#fileNo").val(),
                openBank:$("#openBank").val(),
                ckeckStatus:$("#ckeckStatus").val(),
                checkStatus:$("#checkStatus").val()

            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit, //页面大小
            offset: params.offset / params.limit, //页码
            noticeType: noticeType,
            depositorName: $("#depositorName").val(),
            legalName: $("#legalName").val(),
            legalIdcardDue: $("#legalIdcardDue").val(),
            fileDue: $("#fileDue").val(),
            fileDue2: $("#fileDue2").val(),
            operatorIdcardDue: $("#operatorIdcardDue").val(),
            isOperatorIdcardDue: operatorOverConfigEnabled == false ? false : $("#isOperatorIdcardDue").val(),
            isLegalIdcardDueOver: legalOverConfigEnabled == false ? false : $("#isLegalIdcardDueOver").val(),
            isFileDueOver: fileOverConfigEnabled == false ? false : $("#isFileDueOver").val(),
            effectiveDate: $("#effectiveDate").val(),
            isEffectiveDateOver: tempAcctOverConfigEnabled == false ? false : $("#isEffectiveDateOver").val(),
            acctNo: $("#acctNo").val(),
            applyid:$("#applyid").val(),
            acctName: $("#acctName").val(),
            bankName: $("#bankName").val(),
            name:$("#name").val(),
            type:$("#type").val(),
            branch:$("#branch").val(),
            createdDateEnd:$("#createdDateEnd").val(),
            createdDateStart:$("#createdDateStart").val(),
            fileNo:$("#fileNo").val(),
            openBank:$("#openBank").val(),
            ckeckStatus:$("#ckeckStatus").val(),
            checkStatus:$("#checkStatus").val()

        };
        return temp;
    };

    notice.select = function (layerTips) {
      var rows = notice.table.bootstrapTable('getSelections');
        if (rows.length === 1) {
            notice.currentItem = rows[0];
            return true;
        } else {
            layerTips.msg("请选中一行");
            return false;
        }
    };

  $(document).ready(function () {
      $("#btn_download").click(function () {
         var selectRow = notice.table.bootstrapTable('getSelections');
            var id = '';
            if (selectRow.length > 0) {
               selectRow.forEach(function (selectRow) {
                   id = selectRow.id;
                });
                //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                 $.get('temporaryEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '修改日期',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            offset: ['20px', '20%'],
                            area: ['500px', '200px'],
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
                                var form = layui.form;
                                form.render();
                                form.render('select');

                                $.get(notice.baseUrl + '/temAcctId/' + id, null, function (data) {
                                    var result = data.data;
                                    setFromValues(layero, result);
                                    form.render();
                                });
                                form.on('submit(edit)', function (data) {

                                    if (data.field.password == '******') data.field.password = '';
                                    if (id) {
                                        $.ajax({
                                            url: notice.baseUrl + "/temAcctId/" + id,
                                            type: 'post',
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
        }else{
          alert("请选择一条数据")}})


        $("#btn_download1").click(function () {

            var selectRow = notice.table.bootstrapTable('getSelections');
            var id = '';
            if (selectRow.length > 0) {
               // var depositorName = notice.currentItem.id;
                selectRow.forEach(function (selectRow) {
                    id = selectRow.id;
                });
                //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                if(noticeType == 'legalDueNotice') {
                    $.get('legalEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '修改日期',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            offset: ['20px', '20%'],
                            area: ['500px', '200px'],
                            maxmin: false,
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
                                var form = layui.form;
                                form.render();

                                layero.find('#pwd').hide();//隐藏密码
                                layero.find('#password').attr('lay-verify', 'pass');
                                form.render('select');

                                $.get(notice.baseUrl + '/' + id, null, function (data) {
                                    var result = data.data;
                                    setFromValues(layero, result);
                                    layero.find('#password').val('******');
                                    form.render();
                                });
                                layero.find(":input[name='username']").attr("disabled", "");
                                form.on('submit(edit)', function (data) {

                                    if (data.field.password == '******') data.field.password = '';
                                    debugger
                                    if (id) {
                                        $.ajax({
                                            url: notice.baseUrl + "/" + id,
                                            type: 'post',
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
                }else {
                    $.get('proveEdit.html', null, function (form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '修改日期',
                            content: form,
                            btn: ['保存', '取消'],
                            shade: false,
                            offset: ['20px', '20%'],
                            area: ['500px', '200px'],
                            maxmin: false,
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
                                var form = layui.form;
                                form.render();

                                layero.find('#pwd').hide();//隐藏密码
                                layero.find('#password').attr('lay-verify', 'pass');
                                form.render('select');

                                $.get(notice.baseUrl + '/' + id, null, function (data) {
                                    var result = data.data;
                                    setFromValues(layero, result);
                                    layero.find('#password').val('******');
                                    form.render();
                                });

                                layero.find(":input[name='username']").attr("disabled", "");
                                form.on('submit(edit)', function (data) {

                                    if (data.field.password == '******') data.field.password = '';

                                    if (id) {
                                        $.ajax({
                                            url: notice.baseUrl + "/" + id,
                                            type: 'post',
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

            }else{
                alert("请选择一条数据")
            }
        })


        $("#btn_download2").click(function () {
            var selectRow = notice.table.bootstrapTable('getSelections');
            var ids = '';
            if(selectRow.length > 0){
                layer.confirm('确定核实数据吗？', null, function (index) {
                    selectRow.forEach(function (row) {
                        ids += row.id + ",";
                    });
                    document.getElementById("downloadFrame").src = notice.jnnsUrl + "/check2?id=" + ids.substring(0, ids.length - 1);
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                });
            }else{
                alert("请选择一条数据")
            }
        })


    });



    notice.init = function () {
        notice.table = $('#' + notice.tableId).bootstrapTable({
            url: notice.baseUrl + '/list', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + notice.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: false, //是否启用排序
            sortOrder: notice.order, //排序方式
            queryParams: notice.queryParams,//传递参数（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
            search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            singleSelect: false, //多选框是否单选
            showColumns: false, //是否显示所有的列
            showRefresh: true, //是否显示刷新按钮
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            uniqueId: notice.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: notice.columns(),
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

        if(!fileOverConfigEnabled) {  //证明文件到期
            $('#isFileDueOver').attr("disabled", true);
            $('#isFileDueOver').val('false');
            form.render('select');
        }
        if(!legalOverConfigEnabled) { //法人证件到期
            $('#isLegalIdcardDueOver').attr("disabled", true);
            $('#isLegalIdcardDueOver').val('false');
            form.render('select');
        }
        if(!tempAcctOverConfigEnabled) {
            $('#isEffectiveDateOver').val('false');
            $('#isEffectiveDateOver').attr("disabled", true);
            form.render('select');
        }
        if(!operatorOverConfigEnabled) {
            $('#isOperatorIdcardDue').attr("disabled", true);
            $('#isOperatorIdcardDue').val('false');
            form.render('select');
        }

    };

    notice.mappingNoticeType = function (noticeType) {
        if (noticeType === 'resvrUnprocess') {
            return '预约未及时处理列表';
        }
        if (noticeType === 'tempAcct') {
            return '临时户到期提醒列表';
        }
        return '';
    };

    $('#btn_query').on('click', function () {
        var queryParams = notice.queryParams();
        queryParams.pageNumber = 1;
        notice.table.bootstrapTable('refresh',queryParams);
    });


    if (noticeType == "resvrUnprocess"){

        //预约未及时处理搜索
        $('#btn_query_resvr').on('click', function () {
            var queryParams = notice.queryParams();
            queryParams.pageNumber = 1;
            notice.table.bootstrapTable('refresh',queryParams);
        });

        //初始化日期控件
        var laydateArr =  beginEndDateInit2(laydate, 'createdDateStart', 'createdDateEnd', 'yyyy-MM-dd', true);

        //重置按钮
        $('button[type="reset"]').click(function () {
            for (var i = 0; i < laydateArr.length; i++) {
                delete laydateArr[i].max;
                delete laydateArr[i].min;
            }
        });
    }

    $('#btn_export').on('click',function () {
        var urlParams = '';
        if(noticeType == 'fileDueNotice') {
            urlParams = '&isFileDueOver=' + $('#isFileDueOver').val();
        } else if(noticeType == 'legalDueNotice') {
            urlParams = '&isLegalIdcardDueOver=' + $('#isLegalIdcardDueOver').val()
        } else if(noticeType == 'operatorDueNotice') {
            urlParams = '&isOperatorIdcardDue=' + $('#isOperatorIdcardDue').val()
        }
        $("#downloadFrame").prop('src', notice.baseUrl+"/export?noticeType="+noticeType + urlParams);
        return false;
        // window.location.href= notice.baseUrl+"/export?noticeType="+noticeType + urlParams;
    });

    layui.use(['form', 'layedit', 'laydate'], function () {
        // $("#noticeType").text(notice.mappingNoticeType(noticeType));
        notice.init();
    })

})

function btnCustomerView(id, depositorName,createdDate,organName,customerNo) {
    parent.tab.tabAdd({
        title: '查看-' + depositorName,
        href: 'customer/customerOpen.html?id=' + parseFloat(id)
            + '&depositorName=' + encodeURI(depositorName)
            + '&createdDate=' + encodeURI(createdDate)
            + '&organName=' + encodeURI(organName)
            // + '&customerNo=' + encodeURI(customerNo)
            + '&operateType=select'
    });

}

function tempToAcctDeatil(depositorName, refBillId, billType, acctType) {
    var billId = refBillId;
    var name = depositorName;
    var type = acctType;
    var buttonType = 'select';  //按钮操作类型
    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if(result){
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }
        if(billType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-'+name,
                href: 'accttype/' + type + 'Open.html?billId=' + billId + '&billType='+ billType + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
            });
        } else {
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-'+name,
                href: 'account/accountRevoke.html?billId=' + billId + '&buttonType=' + buttonType + '&syncEccs=' + syncEccs + '&updateType=' + updateType
            });
        }
    });
}

