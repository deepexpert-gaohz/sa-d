layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form', 'murl', 'account', 'common', 'laydate','upload'], function () {
    var form = layui.form, url = layui.murl,
        account = layui.account,
        common = layui.common,
        laydate = layui.laydate,
        upload = layui.upload;
    // //初始化日期控件
    var laydateAcctCreate =  beginEndDateInit(laydate, 'beginDateAcctCreate', 'endDateAcctCreate', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateAcctCreate.length; i++) {
            delete laydateAcctCreate[i].max;
            delete laydateAcctCreate[i].min;
        }
    });

    //白名单管理  1：白名单列表  0：正常账户列表
    var whiteList = common.getReqParam("whiteList");

    var tabId = common.decodeUrlChar(decodeURI(common.getReqParam("tabId")));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var billId = decodeURI(common.getReqParam("billId"));
    var acctType = common.getReqParam("acctType");//账户性质
    var accountStatus = common.getReqParam("accountStatus");//账户状态
    var orgCode = common.getReqParam("orgCode");//父类机构号
    var string003 = common.getReqParam("string003");//-1-全量，1-初始化存量，0-增量
    var string005 = common.getReqParam("string005"); //账户报表统计页面跳转标记

    /**
     * 账户报表统计页面跳出过来
     */
    if(string003) {
        $("#acctType").val(acctType);
        $("#accountStatus").val(accountStatus);
        $("#string003").val(string003);
    }

    var checked =true;
    var role =0;
    if(tabId) {
        //关闭流水tab选项框，关闭时，触发解锁业务流水
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }

    var apply = {
        baseUrl: "../../allAccountPublic",
        entity: "user",
        tableId: "userTable",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };
    apply.columns = function () {
        return [{
            checkbox: true
        }, {
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'refBillId',
            visible: false
        }, {
            field: 'acctNo',
            title: '账号'
        }, {
            field: 'acctName',
            title: '账户名称',
            'class': 'W200'
        }, {
            field: 'acctType',
            title: '账户性质',
            'class': 'W120',
            formatter: function (value, row, index) {
                return changeAcctType(value)
            }
        }, {
            field: 'accountStatus',
            title: '账户状态',
            formatter: function (value, row, index) {
                return formatAccountStatus(value)
            }
        }, {
            field: 'openAccountSiteType',
            title: '本地异地标识',
            formatter: function (value, row, index) {
                return formatOpenAccountSiteType(value)
            }
        }, {
            field: 'bankName',
            title: '开户行'
        }, {
            field: 'kernelOrgCode',
            title: '网点机构号'
        }, {
            field: 'acctCreateDate',
            title: '开户日期',
            //获取日期列的值进行转换
            formatter: function (value, row, index) {
                return changeDateStr(value)
            }
        },{
            field: 'cancelDate',
            title: '销户日期',
            formatter: function (value, row, index) {
                if(row.accountStatus=="revoke"){
                    return value;
                }
            }
        }
        // , {
        //     field: 'pbcSyncStatus',
        //     title: '账管上报状态',
        //     formatter: function (value, row, index) {
        //         return formatSyncStatus(value)
        //     }
        // }, {
        //     field: 'eccsSyncStatus',
        //     title: '信用代码状态',
        //     formatter: function (value, row, index) {
        //         return formatSyncStatus(value)
        //     }
        // }, {
        //     field: 'pbcSyncTime',
        //     title: '账管上报时间',
        //     //获取日期列的值进行转换
        //     formatter: function (value, row, index) {
        //         return changeDateStr(value)
        //     }
        // }, {
        //     field: 'eccsSyncTime',
        //     title: '信用代码上报时间',
        //     //获取日期列的值进行转换
        //     formatter: function (value, row, index) {
        //         return changeDateStr(value)
        //     }
        // }, {
        //     field: 'pbcCheckDate',
        //     title: '核准类人行审核时间',
        //     //获取日期列的值进行转换
        //     formatter: function (value, row, index) {
        //         return changeDateStr(value)
        //     }
        // }, {
        //     field: 'billType',
        //     title: '操作类型',
        //     formatter: function (value, row, index) {
        //         return changeBillType(value)
        //     }
        // }
        ];
    };

   //  $.get('../../config/isCheck', '', function (data) {
   //  	checked = data;
   //      $.get('../../security/crole', '', function (data) {
   //      	role = data.data.code;
	//     	   if(role == 0){//显示：待补录、待上报、上报成功、核准通过
	//     		   $("#zhsq").hide();
	//     		   $("#dsh").hide();
	//     	   }else if(role == 1){
	//     		   if(checked){//审核模式：待补录、上报成功、核准通过，
	//     			   $("#dsb").hide();
	//     			   $("#zhsq").hide();
	//     			   $("#dsh").hide();
	//     		   }else{//无审核模式 ： 待补录、待上报、上报成功、核准通过，
	//     			   $("#zhsq").hide();
	//     			   $("#dsh").hide();
	//     		   }
	//     	   }else if(role==2){
	//     		   if(checked){//待审核、待上报、核准通过
	//     			   $("#zhsq").hide();
	//     			   $("#dbl").hide();
	//     			   $("#cgsb").hide();
	//     		   }
	//     	   }
   //      });
   // });

    //检查批量久悬的按钮
    $.get(apply.baseUrl + '/suspend/process', function (res) {
        if (res.code === 'ACK') {
            var suspendBtn = $("#btn_down_suspend");
            if(res.data.process){
                suspendBtn.removeClass("dropdown-toggle");
                suspendBtn.removeAttr("data-toggle");
                suspendBtn.find(".caret").hide();
                suspendBtn.find(".layui-badge-dot").show();
                suspendBtn.addClass("suspend-process");
                suspendBtn.attr("batch-no",res.data.batchNo);
            }else{
                suspendBtn.addClass("dropdown-toggle");
                suspendBtn.attr("data-toggle", "dropdown");
                suspendBtn.find(".caret").show();
                suspendBtn.find(".layui-badge-dot").hide();
                suspendBtn.removeClass("suspend-process");
                suspendBtn.removeAttr("batch-no");
            }
        } else {
            layerTips.msg(res.message);
        }
    });


    apply.queryParams = function (params) {
        if (!params)
            return {
                acctName: $.trim($("#name").val()),
                acctNo: $.trim($("#acctNo").val()),
                acctType: $("#acctType").val(),
                accountStatus:$("#accountStatus").val(),
                orgCode: orgCode,//查询的机构号
                string003: $("#string003").val(),//查询的存量标识符
                string005: string005,
                openAccountSiteType:$("#openAccountSiteType").val()
            };
        var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            size: params.limit, //页面大小
            page: params.offset / params.limit, //页码
            acctName: $.trim($("#name").val()),
            acctNo: $.trim($("#acctNo").val()),
            acctType: $("#acctType").val(),
            accountStatus: $("#accountStatus").val(),//账户状态
            bankName: $("#bankName").val(),//开户行
            //bankCode: $("#bankCode").val(),//人行机构号
            kernelOrgCode: $("#kernelOrgCode").val(),//网点机构号
            beginDateAcctCreate: $("#beginDateAcctCreate").val(),//开户开始时间
            endDateAcctCreate: $("#endDateAcctCreate").val(),//开户结束时间
            orgCode: orgCode,//查询的机构号
            string003: $("#string003").val(),//查询的存量标识符
            string005: string005,
            openAccountSiteType:$("#openAccountSiteType").val()

            // status:$("#status").val(),
            // operator: $.trim($("#operator").val()),
            // phone: $.trim($("#phone").val())
        };
        return temp;
    };

    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/list?whiteList=' + whiteList, //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: apply.queryParams,//传递参数（*）
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
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            singleSelect : true, // 单选checkbox
            columns: apply.columns()
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

    apply.init1 = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: apply.baseUrl + '/list?whiteList=' + whiteList, //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: apply.queryParams,//传递参数（*）
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
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            singleSelect : false, // 单选checkbox
            columns: apply.columns()
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
    apply.select = function (layerTips) {
        var rows = apply.table.bootstrapTable('getSelections');
        if (rows.length == 1) {
            apply.currentItem = rows[0];
            return true;
        } else {
            layerTips.msg("请选中一行");
            return false;
        }
    };

    layui.use(['form', 'layedit', 'laydate'], function () {

        var timestamp = Date.parse(new Date());
        var isCheck;
        $.get('../../allBillsPublic/whiteListsCounts?whiteList='+whiteList+'&timestamp=' + timestamp, function (dat) {
            $('#zhsqCount').html(dat[0]);
            $('#bbcgCount').html(dat[1]);
            $('#dshCount').html(dat[2]);
            $('#hzcgCount').html(dat[3]);
            $('#dsbCount').html(dat[4]);
            $('#dblCount').html(dat[5]);
            $('#clzhCount').html(dat[6]);
        });

        $.get('../../config/isCheck', function (dat) {
            if (dat == false) {
                $('#zhsq').hide();
                $('#dsh').hide();
            }
        });

        $('#zhsq').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/zhsqAccount.html?whiteList='+whiteList,
                icon: 'fa fa-calendar-plus-o',
                title: '账户申请'
            });
        });

        $('#cgsb').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/cgsbAccount.html',
                icon: 'fa fa-check',
                title: '报备成功'
            });
        });

        $('#dsh').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/dshAccount.html?whiteList='+whiteList,
                icon: 'fa fa-calendar-check-o',
                title: '待审核'
            });
        });

        $('#dsb').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/dsbAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '待上报'
            });
        });

        $('#hztg').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/hztgAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '核准成功'
            });
        });

        $('#dbl').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/daibulu.html?whiteList='+whiteList,
                icon: 'fa fa-calendar-times-o',
                title: '待补录'
            });
        });

        $('#clzh').on('click', function () {
            parent.tab.tabAdd({
                href: 'company/clzhAccount.html',
                icon: 'fa fa-calendar-times-o',
                title: '存量账户'
            });
        });

        if(whiteList == 1){
            $('#hztg').hide();
            $('#dsb').hide();
            $('#btn_update_acctType').hide();
            $('#btn_down_suspend').hide();
            $('#cgsb').hide();
            $('#clzh').hide();
        }
        /*$('a[role="menuitem"]').on('click', function () {
            var type = $(this).attr("href");
            var href = ""
            if (type == "jiben" || type == "linshi" || type == "teshu") {
                href = "validate/detail.html?type=" + type;
            } else {
                href = "validate/detailPBOC.html?type=" + type;
            }
            parent.tab.tabAdd({
                    title: '校验管理',
                    href: href
                }
            );
            return false;
        });
*/
        // apply.init();

        //根据配置选择对公页面批量久悬方式（false：导入上传，true：批量选择）
        $.get('../../config/getDropdownSuspends', function (dat) {
            if (dat) {
                $(".downSuspend").hide();
                $("#btn_select_suspend").removeClass('dn');
                apply.init1();
            }else{
                apply.init();
            }
        });
        var editIndex;
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form(),
            layedit = layui.layedit,
            laydate = layui.laydate;
        var addBoxIndex = -1;

        if(string003){
            $(".paging_report").hide();
            $(".paging_report_hide").show();
            $(".paging_report_1").show();
            form.render("select");
        }
        //初始化页面上面的按钮事件
        $('#btn_query').on('click', function () {

            //开始结束时间校验
            var beginDateAcctCreate = $("#beginDateAcctCreate").val();//开户开始时间
            var endDateAcctCreate = $("#endDateAcctCreate").val();//开户结束时间
            if (beginDateAcctCreate!=null && endDateAcctCreate!=null){
                var startTime = new Date(Date.parse(beginDateAcctCreate.replace(/-/g,"/"))).getTime();//浏览器兼容性
                var endTime = new Date(Date.parse(endDateAcctCreate.replace(/-/g,"/"))).getTime();//浏览器兼容性
                if (startTime>endTime){
                    layer.msg("开始时间大于结束时间");
                    return;
                }
            }

            var queryParams = apply.queryParams();
            queryParams.pageNumber=1;
            apply.table.bootstrapTable('refresh', queryParams);
        });

        $('#btn_view').on('click', function () {
            if (apply.select(layer)) {
                var accountId = apply.currentItem.id;
                var refBillId = apply.currentItem.refBillId;
                var name = apply.currentItem.depositorName;
                var type = apply.currentItem.acctType;
                var billType = apply.currentItem.billType;
                var accountStatus = apply.currentItem.accountStatus;
                var acctNo = apply.currentItem.acctNo;
                var buttonType = 'selectForChangeBtn';  //按钮操作类型
                var typeCode = '';
                var urlStr="&refBillId="+refBillId+"&string005="+string005;
                var createdDate = apply.currentItem.createdDate == null ? "" : apply.currentItem.createdDate;
                var kernelOrgName = apply.currentItem.kernelOrgName == null ? "" : apply.currentItem.kernelOrgName;

                $('#accountId').val(apply.currentItem.id)
                //账户性质为大类时转小类
                if(type == 'specialAcct' || type == 'tempAcct' || type == 'unknow') {
                    if (type == 'specialAcct') {
                        //页面层
                        index = layer.open({
                            title: '选择小类',
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['300px', '200px'], //宽高
                            content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="yusuan">预算单位专用存款账户</option><option value="feiyusuan">非预算单位专用存款账户</option><option value="teshu">特殊单位专用存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
                        });

                    } else if (type == 'tempAcct') {
                        //页面层
                        index = layer.open({
                            title: '选择小类',
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['300px', '200px'], //宽高
                            content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="linshi">临时机构临时存款账户</option><option value="feilinshi">非临时机构临时存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
                        });


                    } else if (type == 'unknow') {
                        //页面层
                        index = layer.open({
                            title: '选择小类',
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['300px', '200px'], //宽高
                            content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option><option value="jiben">基本存款账户</option><option value="yiban">一般存款账户</option><option value="yusuan">预算单位专用存款账户</option><option value="feiyusuan">非预算单位专用存款账户</option><option value="linshi">临时机构临时存款账户</option><option value="feilinshi">非临时机构临时存款账户</option><option value="teshu">特殊单位专用存款账户</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
                        });
                    }

                    $('#acctTypeBtn').click(function () {
                        if (!$('#accountId').val()) {
                            layer.alert("该账户账户id为空,无法更新小类");
                            return;
                        } else if (!$('#acctTypeOption').val()) {
                            layer.alert("请选择账户性质的小类");
                            return;
                        } else {
                            $.post('../../allAccountPublic/updateAcctType', {
                                'accountId': $('#accountId').val(),
                                'acctType': $('#acctTypeOption').val(),
                                'refBillId': refBillId
                            }, function (data) {
                                layer.close(index);
                                tempToDetail($('#acctTypeOption').val(), accountId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName);
                            });
                        }
                    });

                    return;
                }

                //小类跳转
                tempToDetail(type, accountId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName);
            }
        });

        $('#btn_khjd').on('click', function () {
            if (apply.select(layer)) {
                var applyId = apply.currentItem.applyid;
                var name = apply.currentItem.name;
                parent.tab.tabAdd({
                        title: '客户尽调',
                        href: 'kyc/detail.html?name=' + name + '&applyId=' + applyId + '&history=false'
                    }
                );
            }
        });

        $('a[role="menuitem"]').on('click', function () {
            var type = $(this).attr("data-type");
            var href = ""
            if (type == "jiben" || type == "linshi" || type == "teshu") {
                href = "validate/detail.html?type=" + type;
            } else {
                href = "validate/detailPBOC.html?type=" + type;
            }
            parent.tab.tabAdd({
                    title: '校验管理',
                    href: href
                }
            );
            return false;
        });

        //批量久悬按钮
        $('.suspend-process').click(function(){
            var batchNo = $(this).attr("batch-no");
            $.get(apply.baseUrl + '/suspend/process/' + batchNo, function (res) {
                $.get('batchSuspendProcess.html', null, function (form) {
                    console.log(res.result);
                    var html = setFormValueReplace(form, res.result);
                    html = html.replaceAll("{width}",((res.result.processed/res.result.process)*100) +"%");
                    layer.open({
                        type: 1,
                        title: '批量久悬进度',
                        content: html,
                        shade: false,
                        offset: ['10px'],
                        area: ['600px', '230px'],
                        btn: ['结束本次任务'],
                        maxmin: true,
                        yes:function(index, layero){
                            $.ajax({
                                url: apply.baseUrl + "/suspend/process/" + batchNo,
                                type: 'put',
                                dataType: "json",
                                success: function () {
                                    layerTips.msg('本次任务已结束');
                                    layer.close(index);
                                    location.reload();
                                }

                            });
                        }
                    });
                });
            });
        });

        $('a[role="suspendMenuItem"]').on('click', function () {
            var type = $(this).attr("data-type");
            var href = ""
            if (type == "suspendDown") {
                $("#templateDownloadForm").submit();
            } else if(type == "suspendUp"){
                $('#file').click();
            } else if(type =='suspendDetail'){
                parent.tab.tabAdd({
                    title: '久悬处理记录',
                    href: 'batch/list.html'
                });
            }
            return false;
        });

        upload({
            url: apply.baseUrl + '/suspend/upload',
            before: function(input){
                //返回的参数item，即为当前的input DOM对象
                layerTips.msg("文件上传中");
            },
            success: function (res) {
                if (res.code === 'ACK') {
                    layerTips.msg("已成功上传"+res.data+"条批量久悬数据，后台开始处理");
                    location.reload();
                } else {
                    layerTips.msg(res.message);
                }
            }
        });


        $('#btn_update_acctType').on('click', function () {
            $.get(apply.baseUrl + '/updateAcctTypeFromPbc/', function (res) {
                if (res.code === 'ACK') {
                    layerTips.msg("已成功转换所有数据!");
                    location.reload();
                } else {
                    layerTips.msg(res.message);
                }
            });
        });

        //批量选择久悬上报
        $('#btn_select_suspend').on('click', function () {
            var rows = apply.table.bootstrapTable('getSelections');
            if (rows.length === 0) {
                layerTips.msg("请选中至少一行");
            } else {
                var idArr = [];
                for (var i = 0; i < rows.length; i++) {
                    idArr.push(rows[i].refBillId);
                }
                selecrSuspend(idArr);
            }
        });

        function selecrSuspend(idArr){
            $.ajax({
                type: "POST",
                url: "../../allAccountPublic/selectSuspend",
                dataType: 'json',
                data: {"ids": idArr},
                success: function (res) {
                    if (res.code=="ACK"){
                        layer.msg("处理成功");
                    }else {
                        layer.msg("处理失败");
                    }
                    customer.table.bootstrapTable('refresh');
                },
                error: function (res) {
                }
            });
        }


        $('#btn_down_account').on('click', function () {
            var dataUrl ='/companyAccountExport?whiteList='+ whiteList +'&acctName='+ $.trim($("#name").val()) +'&acctNo='+$.trim($("#acctNo").val())+'&acctType=' + $.trim($("#acctType").val()) + '&accountStatus=' + $.trim($("#accountStatus").val()) +'&orgCode='+orgCode+"&string003="+$.trim($("#string003").val())+"&string005=accountStatistics";
            location.href = apply.baseUrl + dataUrl;
        });
    })
})

function tempToDetail(type, accountId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName) {

    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if (result) {
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }

        //(改：billType == 'ACCT_REVOKE'---->accountStatus == 'revoke')
        //基本户允许销户
        if(accountStatus == 'revoke' && (type == 'yiban' || type == 'feiyusuan' || type == 'jiben')) {   //备案类销户类型
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + accountId + '&billType=' + billType + '&buttonType=revokeInfo' + urlStr + '&syncEccs=' + syncEccs + '&updateType=' + updateType + '&acctNo=' + acctNo
            });
            return;
        }

        parent.tab.tabAdd({
            title: '查看' + acctTypeMap[type] + '-' + name,
            href: 'accttype/' + type + 'Open.html?'
            + '&billId=' + accountId
            + '&acctNo=' + acctNo
            + '&billType=' + billType
            + '&accountStatus=' + accountStatus
            + '&buttonType=' + buttonType
            + urlStr
            + '&syncEccs=' + syncEccs
            + '&updateType=' + updateType
            + '&createdDate=' + encodeURI(createdDate)//创建时间
            + '&kernelOrgName=' + encodeURI(kernelOrgName)//创建机构
        });
    });
}