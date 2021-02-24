layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var validator;
var loading2;
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'laydate', 'imageInfo','laytpl'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        loading = layui.loading,
        laydate = layui.laydate;

    //打印loading2赋值
    loading2 = loading;
    var name = decodeURI(common.getReqParam("name"));
    var accountKey = decodeURI(common.getReqParam("accountKey"));
    var regAreaCode = decodeURI(common.getReqParam("regAreaCode"));
    var updateType = decodeURI(common.getReqParam("updateType"));
    var type = decodeURI(common.getReqParam("type"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var recId = decodeURI(common.getReqParam("recId"));
    var refBillId = url.get("refBillId");
    var billId = url.get("billId");
    var acctCancelReason = url.get("acctCancelReason");//销户原因
    var isCheck = true;   //上报模式：默认为审核模式
    var dblToCheck;  //待补录页面根据系统设置上报方式进行按钮的配置
    var nowRoleName;

    var revokeData;

    var map = new Map();

    var pageCode = decodeURI(common.getReqParam("pageCode"));

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var accoungImageShow = buttonType != 'selectForChangeBtn';
    //注册地址选择器
    var regAddressPicker = new picker();

    var eccsSyncEnabled = true;

    //联系地址选择器
    var workAddressPicker = new picker();
    var validatorParam = undefined;

    // 企业名称
    var name = decodeURI(url.get("name"));
    $("#name").html(name);

    laydate.render({
        elem: '#cancelDate',
        format: 'yyyy-MM-dd',
        done: function(value, date, endDate){
            validator.element($("#cancelDate"));
        }
    });
    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });
    var accountImageBillsId = refBillId;
    if(accountImageBillsId == undefined || accountImageBillsId ==""){
        accountImageBillsId = billId;
    }
    if(accountImageBillsId == undefined || accountImageBillsId ==""){
        accountImageBillsId = recId;
    }
    if(accoungImageShow){
        accountImageConfig(accoungImageShow);
    }
    $.ajaxSettings.async = false;

    $.get('../account/headDiv_bill.html', function (form) {
        $("#headDiv").html(form);
    });

    $.get('../account/saic.html', function (form) {
        $("#saicDiv").html(form);

    });
    $.get('../account/superviser.html', function (form) {
        $("#parOrganDiv").html(form);
    });
    $.get('../account/revokeHiddenField.html', function (form) {
        $("#hiddenFieldDiv").html(form);
    });
    $.get('../account/legal.html', function (form) {
        $("#legalDiv").html(form);
        //        if(buttonType == 'update') {    //补录详情页
        $('#carrierBtn').hide()
        //        }
    });
    $.get('../account/org.html', function (form) {
        $("#orgDiv").html(form);
    });
    $.get('../account/tax.html', function (form) {
        $("#taxDiv").html(form);
    });
    $.get('../account/contact.html', function (form) {
        $("#contactDiv").html(form);
    });
    $.get('../account/other_feijiben.html', function (form) {
        $("#otherDiv").html(form);
    });
    $.get('../account/relateCompany.html', function (form) {
        $("#relateCompanyDiv").html(form);
    });
    $.get('../account/stockHoder.html', function (form) {
        $("#stockHoderDiv").html(form);
    });
    $.get('../account/agent.html', function (form) {
        $("#agentDiv").html(form);
    });
    $.get('../account/buildDepartment.html', function (form) {
        $("#buildDiv").html(form);
    });
    laydate.render({
        elem: '#operatorIdcardDue',
        format: 'yyyy-MM-dd',
        done: function(value, date, endDate){
            validator.element($("#operatorIdcardDue"));
        }
    });
    $.get('../../config/isCheck', function(data) {
        if(data == false) {
            isCheck = false;
        }
    });

    //判断基本户是否上报信用机构
    $.get('../../config/isEccsSync', function (data) {
        if (data == false) {
            eccsSyncEnabled = false;
        }
    });

    $.get('../../config/findConfigValue?configKey=revokeSyncStatus', null, function (data) {
        dblToCheck = data;
    });

    $.get('../../role/getRoleName', null, function (data) {
        nowRoleName = data;
    });
    $.ajaxSettings.async = true;
    removeAll();
    addValidateFlag(["acctCancelReason", "cancelDate"]);
    $('#depositorNameSpan').hide();
    $('#depositorName').attr('lay-verify', '')
    $('#depositorName').attr('required', false)
   // $('#businessScopeSpan').append("<span style=\"color: red; padding-left: 5px\">*</span>")

    if(pageCode == 'dsbAccount' || pageCode == 'dblAccount'){
        $('#verifyPass').hide();
        $('#verifyNotPass').hide();
    }
    var selectDisabled = true;
    if(buttonType == 'selectForRevoke') {  //存量销户操作
        if(isCheck == true) {  //审核模式销户页面才隐藏上报按钮
            $('#syncDiv').hide();
        } else if(isCheck == false) {
            $('#verifyFormBtn').html('上报');
            $('#addInfoFormBtn').hide();
        }
        $('#saveBtn').css('display', '')
    } else if(buttonType == 'update') {  //销户状态补录
        $('#accountDiv').remove();
        $('#cancelDateSpan').hide();
        $('#cancelDate').attr('required', false)
        //接口模式上报人行需要进行审核的情况下，上报系统、审核并上报隐藏，提交按钮展现。
        if (dblToCheck == '' || dblToCheck == 'autoSync') {
            $('#saveBtn').hide();
        } else {
            $('#saveBtn').show();
            $('#sync1').hide();
            $('#isSyncAmsDiv').hide();
            $('#verifyFormBtn').hide();
        }

    } else if(buttonType != 'sync') {
        $('input,select,textarea').prop("disabled",true)
        // $('#bankName').attr("disabled",false)
        // $('#bankCode').attr("disabled",false)
        // $('#acctCancelReason').attr("disabled",false)
        // $('#businessScope').attr("disabled",false)
        // $('#isSyncAms').attr("disabled",false)

        if(buttonType == 'revokeDetail' || buttonType == 'revokeInfo' ||buttonType == 'select') {  //销户详情页
            $('#syncDiv').hide();
            $.get("../../config/findByKey?configKey=printf", null, function (data) {
                if (data == true) {  //配置打印
                    $('#printBtn').css('display', '');
                }
            })
        }
    }

    if (buttonType == 'sync' && nowRoleName == '分行审核员') {
        $('#syncDiv').hide();  //上报区块隐藏
    }

    if (buttonType == 'sync' && nowRoleName == '总行审核员') {
        $('#rejectBtn').show();  //上报区块隐藏
    }

    if(buttonType == 'sync') {  //待审核上报详情页
        //待审核 页面设置成不可改写(帐号可补录) begin
        $('input,select,textarea').prop("disabled",true);
        $('input[type=checkbox]').prop("disabled",false);
        //待审核 页面设置成不可改写(帐号可补录) end
        $('#addInfoFormBtn').hide()
        $.get("../../config/findByKey?configKey=printf", null, function (data) {
            if (data == true) {  //配置打印
                $('#printBtn').css('display', '');
            }
        })
    }

    $('#cancel').click(function(){
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    // //上报
    // $('#verifyFormBtn').click(function(){
    //     $('input,select,textarea').removeAttr('disabled');
    //     $('#submitBtnType').val('verifyForm')
    //     $("#formId").submit();
    // });

    //提交
    $('#saveBtn').click(function(){
        //提交
        $('#submitBtnType').val('save')
        $("#formId").submit();
    });

    $('#verifyPass').click(function () {
        $('input,select,textarea').removeAttr('disabled');
        $('#submitBtnType').val('verifyPass');
        $("#formId").submit();
    });

    //打印
    $('#printBtn').click(function(){
        submitForm(toJson($("#formId").serializeArray()), 'print');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    //联网核查
    /*$('#btn_check').click(function(){
        var legalName = $('legalName').val();
        var legalIdcardNo = $('legalIdcardNo').val();
        console.log("联网核查，姓名："+legalName+"身份证号码："+legalIdcardNo)
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });*/
    //提交
    form.on('submit(save)', function(data){
        submitForm(data, 'save');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //补录
    $('#addInfoFormBtn').click(function(){
        $('#submitBtnType').val('addInfoForm')
        $("#formId").submit();

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })

    $('#rejectBtn').on('click', function () {
        $.get('../accttype/reject.html', null, function (form) {
            layer.open({
                type: 1,
                title: '驳回原因',
                content: form,
                btn: ['驳回', '取消'],
                shade: false,
                offset: ['20px', '20%'],
                area: ['500px', '300px'],
                maxmin: true,
                yes: function (index) {
                    //触发表单的提交事件
                    // layedit.sync(editIndex);
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                }, btn2: function(){
                    $("#rejectBtn").show();
                },end: function () {
                    $("#rejectBtn").show();
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
                    form = layui.form;
                    form.render();
                    form.on('submit(edit)', function (data) {
                        // $('input,select,textarea').removeAttr('disabled');
                        $('#submitBtnType').val('reject');
                        // $('#denyReason').val(data.field.denyReason);
                        $('#rejectBtn').attr('disabled', 'disabled');
                        $.post("../../allAccountPublic/form/reject", {"id": billId ,"formId": billId,"denyReason" : data.field.denyReason  ,"action":"rejectForm"}, function (data) {
                            layer.alert('驳回成功', {
                                title: "提示",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(index);
                                addCompanyAccountsTab(common, buttonType, billId);

                            });
                        }).error(function (err) {
                            layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                }
            });
        });
        $("#rejectBtn").hide();
    });
    $('#verifyNotPass').on('click', function () {
        $.get('../accttype/reject.html', null, function (form) {
            layer.open({
                type: 1,
                title: '审核退回原因',
                content: form,
                btn: ['审核退回', '取消'],
                shade: false,
                offset: ['100px', '30%'],
                area: ['500px', '300px'],
                maxmin: true,
                yes: function (index) {
                    //触发表单的提交事件
                    // layedit.sync(editIndex);
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },btn2: function(){
                    $("#verifyNotPass").show();
                },end: function () {
                    $("#verifyNotPass").show();
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
                    form = layui.form;
                    form.render();
                    form.on('submit(edit)', function (data) {
                        // $('input,select,textarea').removeAttr('disabled');
                        $('#submitBtnType').val('reject');
                        // $('#denyReason').val(data.field.denyReason);
                        $('#rejectBtn').attr('disabled', 'disabled');
                        $.post("../../allAccountPublic/form/reject", {"id": billId ,"formId": billId,"denyReason" : data.field.denyReason ,"action":"verifyNotPass"}, function (data) {
                            layer.alert('审核退回成功', {
                                title: "提示",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(index);
                                addCompanyAccountsTab(common, buttonType, billId);

                            });
                        }).error(function (err) {
                            layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                }
            });
        });
        $("#verifyNotPass").hide();
    });

    var syncSystem =  true;
    $('#verifyFormBtn').on('click', function () {
        $.get('../accttype/syncSystem.html', null, function (form) {
            layer.open({
                type: 1,
                title: '上报系统',
                content: form,
                btn: ['确定', '取消'],
                shade: false,
                offset: ['100px', '30%'],
                area: ['500px', '170px'],
                maxmin: true,
                yes: function (index) {
                    //触发表单的提交事件
                    // layedit.sync(editIndex);
                    $('form.layui-form').find('button[lay-filter=edit]').click();
                },
                btn2: function(){
                    $("#verifyFormBtn").show();
                },
                end: function () {
                    $("#verifyFormBtn").show();
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
                    var pbcSync = $("#pbcSyncStatus").val();
                    var eccsSync = $("#eccsSyncStatus").val();
                    if (pbcSync == 'tongBuChengGong' || pbcSync == 'buTongBu') {
                        $('#isSyncAmsDiv').hide();
                    }
                    if ( eccsSync == 'tongBuChengGong' || eccsSync == 'buTongBu') {
                        $('#isSyncEccsDiv').hide();
                    }
                    if((pbcSync == 'tongBuChengGong' || pbcSync == 'buTongBu') && ( eccsSync == 'tongBuChengGong' || eccsSync == 'buTongBu')){
                        $('#syncMessage').show();
                        //无需上报
                        syncSystem = false;
                    }
                    form = layui.form;
                    form.render();
                    //上报
                    form.on('submit(edit)', function (data) {
                        var isSyncAms = $("#isSyncAms").prop("checked");
                        var isSyncEccs = $("#isSyncEccs").prop("checked");
                        if (isSyncAms == false && isSyncEccs == false) {
                            if(((revokeData.acctType == 'jiben' || revokeData.acctType == 'feilinshi') && revokeData.cancelHeZhun == true && syncSystem) ||
                                revokeData.acctType == 'yiban' || revokeData.acctType == 'feiyusuan'){
                                layer.alert("请勾选上报系统");
                                return false;
                            }
                        }
                        if(!$('#formId').valid()){
                            layer.alert('请完善信息');
                            $('#verifyFormBtn').show();
                            layer.close(index);
                            return false;
                        }
                        $('input,select,textarea').removeAttr('disabled');
                        $('#submitBtnType').val('verifyForm');
                        $("#formId").submit();
                        return false;
                    });
                }
            });
        });
        $("#verifyFormBtn").hide();
    });
    var rules = {
        operatorTelephone: {
            isMobile: true
        }
    }
    function otherValidate() {
        //legalIdcardTypeChange(form, validator);
        operatorIdcardTypeChange(form, validator);
    }
    //补录
    // form.on('submit(addInfoForm)', function(data){
    //     submitForm(data, 'addInfoForm');
    //     return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    // });

    form.on('select', function(data){
        validator.element(data.elem);
    });

    $.validator.setDefaults({
        ignore: "",
        errorPlacement : errorPlacementCallback,
        highlight : highlight,
        unhighlight : unhighlight,
        submitHandler: function() {
            submitForm(toJson($("#formId").serializeArray()), $('#submitBtnType').val());

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });


    var uri;
    if(buttonType == 'revokeInfo') {  //对公账户主页的销户详情页
        uri = '../../allAccountPublic/details?id=' + billId;
    } else {  //销户流水详情页
        uri = '../../allBillsPublic/getFormDetails?id=' + billId;
    }

    if(billId) {
        $.get (uri, null, function (data) {
            data = data.result;
            revokeData = data;
            initByExistData(data);
            if(data.acctType == 'feilinshi'){
                $("#cancelFeilinshi").show();
                $("#accountKey").val(data.accountKey);
                $("#cancelAccountKey").val(data.accountKey);
            }
            $.get("../../config/findByKey?configKey=revokeEnabled", null, function (data) {
                if (data != true) {  //配置销户是否可编辑
                    $('input,select,textarea').prop("disabled",true);
                    $("input,select,textarea").addClass("disableElement");
                    $('#cancelDate').prop("disabled",false);
                    $('#acctCancelReason').prop("disabled",false);
                    $('#cancelDate').attr('lay-verify', '')
                    $('#cancelDate').attr('required', true)

                    $('#acctCancelReason').attr('lay-verify', '')
                    $('#acctCancelReason').attr('required', true)

                }
            })
            $('#videoNo').prop("disabled",false);
            $('#videoNo').attr('lay-verify', '')

            if(buttonType == 'selectForRevoke') {
                Blank();
                $('#billType').val('ACCT_REVOKE')
            }
            $('#recId').val(recId)

            if(data.imageBatchNo != null && data.imageBatchNo != ''){
                $('#imageBatchNo1').html('-->影像批次号：' + data.imageBatchNo);
                $('#imageBatchNo').val(data.imageBatchNo);
            }
            //隐藏域赋值
            // $('#id').val(data.id);

            // $('input,select,textarea').attr("disabled",true);
            // $('#acctCancelReason').removeAttr("disabled");
            if(accoungImageShow){
                var temprecid =refBillId;
                if(temprecid == undefined || temprecid ==""){
                    temprecid = recId;
                }
                if(temprecid == undefined || temprecid ==""){
                    temprecid = billId;
                }
                runImageData(temprecid,'ACCT_REVOKE',buttonType == 'select');
            }
            form.render('select');
        });

        // $('#basicAcctRegArea').attr('lay-verify', '')

    }
    /**
     * 根据现有数据回填表单
     * @param data
     */
    function initByExistData(data) {
        // $('#acctType').val(data.acctType)
        // $('#acctCancelReason').val(data.acctCancelReason)

        if(data) {
            for (var key in data) {
                if(data[key] != null){
                    var elem = $('#' + key);
                    if(elem.length<1) {
                        elem = $('[id="' + key + '"]');
                    }
    
                    var value = data[key];
                    if(elem.length>0 && elem[0].tagName == 'SELECT') {
                        if(elem.find('option[value="' + value + '"]').length > 0) {
                            elem.val(value);
                        }
                    } else {
                        elem.val(value);
                    }
                }
            }

            setLabelValues(data, map);

            if(data.acctType!='feilinshi'){
                hideInnerTabArray("tab_15",true,form);
            }
            //省市区组件
            var regAddressCode, regAddressType;
            if (data.regArea) {
                regAddressCode = data.regArea;
                regAddressType = 3;
            } else if (data.regCity) {
                regAddressCode = data.regCity;
                regAddressType = 3;
            } else if (data.regProvince) {
                regAddressCode = data.regProvince;
                regAddressType = 3;
            }

            initAddress('reg', regAddressPicker, regAddressCode, regAddressType, null, selectDisabled);

            var workAddressCode,workAddressType;
            if(data.workArea){
                workAddressCode = data.workArea;
                workAddressType = 3;
            } else if(data.workCity){
                workAddressCode = data.workCity;
                workAddressType = 3;
            } else if(data.workProvince){
                workAddressCode = data.workProvince;
                workAddressType = 3;
            } else{
                workAddressCode = regAddressCode;
                workAddressType = 3;
            }
            initAddress('work',workAddressPicker,workAddressCode,workAddressType,null, selectDisabled);

            //组织机构类别
            var selectedArr = [];
            if(data.orgType && data.orgTypeDetail){
                selectedArr.push(data.orgType);
                selectedArr.push(data.orgTypeDetail);
            }
            initLinkSelect("orgSelect",org.data(),selectedArr,function (item,dom) {
                if(item.value.length === 1){//说明是一级的值
                    $('#orgType').val(item.value);
                    $('#orgTypeDetail').val("");
                }else if(item.value.length === 2){//说明是二级的值
                    $('#orgTypeDetail').val(item.value);
                }else if(item.value.length ===0){
                    if(item.level =="0"){
                        $('#orgType').val("");
                        $('#orgTypeDetail').val("");
                    }else if(item.level=="1"){
                        $('#orgTypeDetail').val("");
                    }
                }
                validator.element($("#linkSelectInput10001"));
            }, selectDisabled);
            $('#orgType').val(data.orgType);
            $('#orgTypeDetail').val(data.orgTypeDetail);

            //经济行业分类
            var selectedIndustryArr = [];
            if(data.economyIndustryCode){
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,1));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,3));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,4));
                selectedIndustryArr.push(data.economyIndustryCode);
            }
            initLinkSelect("economyIndustrySelect",industry.data(),selectedIndustryArr,function (item,dom,valueArr) {
                //选中回调函数
                if(valueArr && valueArr.length > 0){
                    $('#economyIndustryName').val(valueArr[valueArr.length-1].name);
                    $('#economyIndustryCode').val(valueArr[valueArr.length-1].value);
                }else if(valueArr != undefined){
                    //设置最终的值
                    $('#economyIndustryCode').val("");
                    $('#economyIndustryName').val("");
                }

                validator.element($("#linkSelectInput10002"));
            }, selectDisabled);

            //初始化销户原因下拉选项
            if ((data.acctType === 'jiben' || data.acctType === 'feilinshi')
                && (data.depositorType === '01'//企业法人
                    || data.depositorType === '02'//非企业法人
                    || data.depositorType === '13'//有字号的个体工商户
                    || data.depositorType === '14'//无字号的个体工商户
                )) {
                $('#acctCancelReason').append('<option value="1">1-转户</option>');
            }
            $('#acctCancelReason').append('<option value="2">2-撤并</option>');
            $('#acctCancelReason').append('<option value="3">3-解散</option>');
            $('#acctCancelReason').append('<option value="4">4-宣告破产</option>');
            $('#acctCancelReason').append('<option value="5">5-关闭</option>');
            $('#acctCancelReason').append('<option value="6">6-被吊销营业执照或执业许可证</option>');
            $('#acctCancelReason').append('<option value="7">7-其它</option>');
            //初始化销户原因值
            if (acctCancelReason !== undefined && acctCancelReason !== null && acctCancelReason !== '') {
                $('#acctCancelReason').val(acctCancelReason);
                $('#acctCancelReason').prop("disabled", true);
            } else if (data.acctCancelReason) {
                $('#acctCancelReason').val(data.acctCancelReason);
                // $('#acctCancelReason').prop("disabled", true);
            }

        }
        otherValidate();
        $('#acctType').attr("disabled",true);
        form.render("select");

    }
    $.validator.setDefaults({
        rules: rules
    });
    function initLinkSelect(elementId,data,selectedArr,selectedCallback,disabled) {
        linkSelect.render({
            id:elementId,
            elem: '#'+elementId,
            data:data,
            lableName:'',			//自定义表单名称    默认：'级联选择'
            placeholderText:'请选择...',		//自定义holder名称    默认：'请选择'
            replaceId:"ids",				//替换字段id   默认id
            replaceName:"names",			//替换字段名称  默认name
            replaceChildren:"childrens",	//替换字段名称  默认 children
            disabled:disabled,					//初始禁用         默认false
            selectWidth:200,
            selectedArr: selectedArr,
            selected:function(item,dom){
                if(selectedCallback) selectedCallback(item);
            }
        });
    }
    //保存、提交、上报按钮
    function submitForm(data, operateType){
        var msg;
        var url = '../../allAccountPublic/form/submit';
        var url3 = '../../allAccountPublic/form/verifyPass';//审核通过

        data.billType = 'ACCT_REVOKE'
        data.acctCancelReason = $('#acctCancelReason').find("option:selected").val();
        data.acctType = $('#acctType').find("option:selected").val();
        loopSelectedDisabled(data);
        loopInputDisabled(data);
        data.businessScope = $('#businessScope').val();
        if(data.accountKey != data.cancelAccountKey){
            data.accountKey = data.cancelAccountKey;
        }
        if(operateType == 'verifyForm') {
            if($('#isSyncAms').prop("checked") == true) {
                data.isSyncAms = true;
            }  else {
                data.isSyncAms = false;
            }
            data.action='verifyForm'
            msg = "上报"
        } else if(operateType == 'addInfoForm') {
            data.action='addInfoForm'
            msg = "补录"
        }  else if(operateType == 'save') {
            data.action='saveForm'
            msg = "提交"
        } else if (operateType == 'verifyPass') {
            data.action = 'verifyPass'
            msg = "审核通过"
        }


        if(data.acctType){

            if(operateType == 'verifyForm') {
                if(buttonType == 'update') { //补录状态
                    if ($('#isSyncAms').prop("checked") == false) {
                        if(((revokeData.acctType == 'jiben' || revokeData.acctType == 'feilinshi') && revokeData.cancelHeZhun == true && syncSystem) ||
                            revokeData.acctType == 'yiban' || revokeData.acctType == 'feiyusuan'){
                            layer.alert("请勾选上报系统");
                            return false;
                        }
                    }
                }

                var layerLoading = layer.msg('正在处理，请等待......', {icon: 16, shade: 0.3, time:0});

                $.post(url, data, function (resultData) {
                    //直接关闭遮罩
                    layer.close(layerLoading);
                    if (resultData.submitResult === 'success') {
                        layer.alert(msg + '成功', {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);

                            if (data.billType === 'ACCT_REVOKE'
                                && (data.acctType === 'jiben' || data.acctType === 'feilinshi')
                                && (data.depositorType === '01'//企业法人
                                    || data.depositorType === '02'//非企业法人
                                    || data.depositorType === '13'//有字号的个体工商户
                                    || data.depositorType === '14'//无字号的个体工商户
                                )
                            ) {
                                if (resultData.allAccountData === null) {
                                    layer.confirm('未查询到已开立其他银行账户', {
                                        title: "提示",
                                        closeBtn: 0,
                                        btn: ['确定'] //按钮
                                    }, function (index) {
                                        $.get("../../config/findByKey?configKey=syncImageUse", null, function (res) {
                                            if (res == true) {
                                                zhuanOpen(data, resultData, msg);
                                            } else {
                                                addCompanyAccountsTab(common, buttonType, billId);
                                            }

                                        });
                                    });
                                } else {
                                    layer.confirm('是否导出已开立其他银行账户清单', {
                                        title: "提示",
                                        closeBtn: 0,
                                        btn: ['是', '否'] //按钮
                                    }, function (index) {
                                        var params = {
                                            depositorName: data.depositorName,
                                            accountKey: data.accountKey,
                                            cancelDate: data.cancelDate,
                                            allAccountData: JSON.stringify(resultData.allAccountData)
                                        };
                                        postExcelFile(params, '../../allAccountPublic/otherBankAccount/export');
                                        layer.close(index);

                                    }, function (index) {
                                        $.get("../../config/findByKey?configKey=syncImageUse", null, function (res) {
                                            if (res == true) {
                                                zhuanOpen(data, resultData, msg);
                                            } else {
                                                addCompanyAccountsTab(common, buttonType, billId);
                                            }

                                        });
                                    });
                                }
                            } else {
                                $.get("../../config/findByKey?configKey=syncImageUse", null, function (res) {
                                    if (res == true) {
                                        zhuanOpen(data, resultData, msg);
                                    } else {
                                        addCompanyAccountsTab(common, buttonType, billId);
                                    }

                                });
                            }

                        });

                    } else if (resultData.submitResult === 'fail') {
                        submitResultHint(resultData.submitMsg);
                    } else {  //没勾选上报系统
                        submitResultHint('提交成功');
                    }
                }).error(function (err) {
                    //直接关闭遮罩
                    layer.close(layerLoading);
                    layerTips.msg('处理submit失败[' + err.submitResult + ']');
                });
            } else if(operateType == 'print') {   //打印按钮
                printfClick(common);
            } else if (operateType == 'verifyPass') {  //审核通过
                url = url3;
                $('#rejectBtn').attr('disabled', 'disabled');

                $.post(url, {"id": billId ,"formId": billId}, function (data) {
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        addCompanyAccountsTab(common, buttonType, billId);

                    });
                }).error(function (err) {
                    layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                });
            }  else {
                $('#saveBtn').hide();
                $.post(url, data, function (resultData) {
                    //submitResultHint(msg + '成功');
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        $('#id').val(resultData.refBillId);

                        $.get("../../config/findByKey?configKey=printf", null, function (data) {
                            if (data == true) {  //配置打印
                                if ($('#billType').val() == 'ACCT_OPEN' || $('#billType').val() == 'ACCT_CHANGE' || $('#billType').val() == 'ACCT_REVOKE') { //开变销打印按钮展示
                                    // $('#printBtn').css('display', '');

                                    layer.confirm('当前页面是否需要打印？', {
                                        btn: ['是', '否'],
                                        cancel: function (index, layero) {  //取消操作，点击右上角的X
                                            addCompanyAccountsTab(common, buttonType, billId);
                                        }
                                    }, function () {
                                        printfClick(common)

                                    }, function () {
                                        addCompanyAccountsTab(common, buttonType, billId);

                                    });
                                } else {
                                    addCompanyAccountsTab(common, buttonType, billId);
                                }
                            } else {
                                addCompanyAccountsTab(common, buttonType, billId);
                            }

                        });

                    });
                }).error(function (err) {
                    $('#saveBtn').show();
                    layerTips.msg('处理submit失败['+err.responseJSON.message+']');
                });
            }

        } else {
            layerTips.msg('请到对公业务管理设置账户性质');
        }
    }
    function zhuanOpen(data,resultData,msg) {
        layer.confirm('影像是否上报人行？', {
            btn: ['是', '否'],
            cancel: function (index, layero) {  //取消操作，点击右上角的X
                layer.close(index);
            }
        }, function () {
            $.ajax({
                url: "../../imageAll/sync",
                type: 'GET',
                dataType: "json",
                data: {acctBillsId: $('#id').val()},
                success: function (data2) {
                    if (data2.code === 'ACK') {
                        layer.alert('影像上报成功', {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId);
                        });
                    } else {
                        layer.alert(data2.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId);
                        });
                    }
                }
            });
        }, function () {
            addCompanyAccountsTab(common, buttonType, billId);
        });
    }
    function submitResultHint(mag) {
        layer.alert(mag, {
            title: "提示",
            closeBtn: 0
        }, function (index) {
            layer.close(index);

            parent.tab.tabAdd({
                href: 'company/companyAccounts.html?tabId=' + encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId())),
                icon: 'fa fa-calendar-plus-o',
                title: '对公报备管理'
            });
        });
    }
    //var validator = $("#formId").validate();
    validator =  $("#formId").validate({
        onkeyup: onKeyUpValidate,
        onfocusout: onFocusOutValidate,
        ignore: "",
        errorPlacement: errorPlacementCallback,
        highlight: highlight,
        unhighlight: unhighlight,
        showErrors: showErrors,
        submitHandler: function () {
            //防止修改（删除）股东信息时，提交表单
            if ($('#submitBtnType').val() == "" || $('#submitBtnType').val() == null) {
                return false;
            }
            submitForm(toJson($("#formId").serializeArray()), $('#submitBtnType').val());

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });
});


function templateClick() {
    if($('#templateOption').val() == '') {
        layer.alert("请选择对应模版类型");
        return;
    }
    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
    $.get('../account/print.html', null, function (form) {

        var url = '../../allBillsPublic/getPrintPreview?id=' + $('#id').val() + '&templateName=' + encodeURI($('#templateOption').val()) + "&billType=" + $('#billType').val() + "&depositorType=" + convertDepositorType($('#depositorType').val());
        addBoxIndex = parent.layer.open({
            type: 1,
            title: '打印预览',
            content: form,
            shade: false,
            offset: ['20px', '20%'],
            area: ['800px', '600px'],
            maxmin: true,
            scrollbar: false,
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
                // loading2.show({
                //     target:'#print',
                //     message:'正在努力生成打印文件,请稍等...'
                // });
                layero.find("iframe")[0].contentWindow.location.replace(url);
                layero.find("iframe").on('load',function () {
                    // setTimeout(function hide() {
                    //     loading2.hide('#print');
                    // },1000);

                });
            },
            cancel: function(index, layero){
                layero.find("iframe").hide();
            },
            end: function () {
                addBoxIndex = -1;
            }
        });
    });
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}
