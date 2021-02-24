layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var buttonTypePub;
var billIdPub;
var billLockConfigPub;

//当前tab选项卡关闭时，解锁该流水
function onDeleteTab() {
    if (billLockConfigPub) {
        billUnLock(buttonTypePub, billIdPub);
    }
}

var loading2;
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'imageInfo'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        loading = layui.loading,
        laydate = layui.laydate;

    loading2 = loading;

    var operateType = url.get("operateType");

    var billId = url.get("billId");
    var applyid = url.get("applyid");
    var imageFlag = url.get("imageFlag");//是否只显示账户影像信息

    var validatorParam = undefined;

    var map = new Map();
    var changeFieldsMap = new Map();
    var nowDate = new Date();
    var name = decodeURI(common.getReqParam("name"));
    var accountKey = decodeURI(common.getReqParam("accountKey"));
    var regAreaCode = decodeURI(common.getReqParam("regAreaCode"));
    var type = decodeURI(common.getReqParam("type"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var billType = decodeURI(common.getReqParam("billType"));
    var recId = decodeURI(common.getReqParam("recId"));
    var createdDate = decodeURI(common.getReqParam("createdDate"));
    var kernelOrgName = decodeURI(common.getReqParam("kernelOrgName"));
    var accountStatus = url.get("accountStatus");

    var pageCode = decodeURI(common.getReqParam("pageCode"));
    var string005 = decodeURI(common.getReqParam("string005")); //账户报表统计页面跳转标记
    getBillLockConfig(function (flag) {
        billLockConfigPub = flag;
        buttonTypePub = buttonType;
        billIdPub = billId;
        if (flag) {
            //业务流水上锁，避免其他人员同时对同一个业务流水进行操作
            circulationBillLock(buttonType, billId, 1000 * 60 * 9);

            window.onbeforeunload = function (e){
                billUnLock(buttonType, billId);
            };
        }
    });

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();
    var isPrint=false;
    // 企业名称
    var name = decodeURI(url.get("name"));
    $("#name").html(name);

    // 预约编号
    if(applyid){
        $('#preOpenAcctId').val(applyid);
    }

    hideInnerTabArray("tab_10",true,form);

    $.ajaxSettings.async = false;
    $.get("../../config/findByKey?configKey=printf", null, function (data) {
        if (data == true) {
            isPrint =data;
            if (billType == 'ACCT_CHANGE' || billType == 'ACCT_OPEN') {
                $('#printBtn').css('display', '');
            }
        }
    });
    if (buttonType === 'selectForChangeBtn' || buttonType === 'yanziToNormal' || buttonType === 'zengziToNormal') {//页面展示为账户信息页面
        $.get('../account/headDiv_account.html', function (form) {
            $("#headDiv").html(form);
            $("#createTimeSpan").html(createdDate);
            $("#createOrgSpan").html(kernelOrgName);
            $("#accountTypeSpan").html(formatAccountStatus(accountStatus));
        });
    } else {//页面展示为流水信息页面
        $.get('../account/headDiv_bill.html', function (form) {
            $("#headDiv").html(form);
        });
    }

    $.get('../account/appoint.html', function (form) {
        $("#appointDiv").html(form);
    });
    if(buttonType == 'update' && billType == 'ACCT_CHANGE') {  //变更补录详情页
        $.get('../account/saic.html', function (form) {
            $("#saicDiv").html(form);

            $('#fileType').empty();
            $('#fileType').append("<option value=\"\">请选择</option>\n" +
                "<option value=\"01\">01-工商注册号</option>\n" +
                "<option value=\"09\">09-主管部门批文</option>");

            $('#fileType2').parent().parent().parent().parent().remove();
        });

    } else {
        $.get('../account/saicrequired.html', function (form) {
            $("#saicDiv").html(form);

            $('#fileType').empty();
            $('#fileType').append("<option value=\"\">请选择</option>\n" +
                "<option value=\"01\">01-工商注册号</option>\n" +
                "<option value=\"09\">09-主管部门批文</option>");

            $('#fileType2').parent().parent().parent().parent().remove();
        });
    }
    $("#businessScopeEccsRow").empty();
    $('#depositorType').empty();
    $('#depositorType').append("<option value=\"\">请选择</option>\n" +
        "<option value=\"50\">50-QFII</option>\n" +
        "<option value=\"51\">51-境外贸易机构</option>\n" +
        " <option value=\"52\">52-跨境清算</option>");

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
        $("#otherDiv").html(form + "<div class=\"row\">\n" +
            "    <div class=\"col-md-12\">\n" +
            "        <div class=\"form-group\">\n" +
            "            <label class=\"control-label col-md-2\">账户许可核准号</label>\n" +
            "            <div class=\"col-md-8\">\n" +
            "                <input type=\"text\" class=\"layui-input\" placeholder=\"\" name=\"accountLicenseNo\" id=\"accountLicenseNo\">\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>");
    });
    $.get('../account/superviser.html', function (form) {
        $("#superviserDiv").html(form);
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
    $.get('../account/acct_hiddenfield.html', function (form) {
        $("#hiddenFieldDiv").html(form);
    });
    $.ajaxSettings.async = true;

    if(buttonType == 'saicValidate') {
        $.get('../../organization/orgTree', function (data) {
            $('#bankCode').val(data.data[0].pbcCode)
            $('#bankName').val(data.data[0].name)
        })
        $.get('../../validate/open/customerInfo?accountKey=' + accountKey + '&regAreaCode=' + regAreaCode + '&name=' + encodeURI(name) + '&type=' + type).done(function (response) {
            if (response.rel == false) {
                if(response.code === "1"){
                    layer.confirm(response.msg, {
                        btn: ['是','否'],
                        cancel: function(index, layero){  //取消操作，点击右上角的X
                            parent.tab.deleteTab(parent.tab.getCurrentTabId());
                        }
                    }, function(index){
                        layer.close(index);
                        response.result.final.organFullId = null;
                        initByExistData(response.result.final);
                    }, function(){
                        parent.tab.deleteTab(parent.tab.getCurrentTabId());
                    });
                }else{
                    layer.alert(response.msg, {
                        title: "提示",
                        closeBtn: 0
                    }, function () {
                        parent.tab.deleteTab(parent.tab.getCurrentTabId());
                    });
                }
            } else {
                if (response.msg) {
                    layer.alert(response.msg, {
                        title: "提示",
                        closeBtn: 0
                    });
                } else {
                    $('#regOffice').val('G');
                }

                response.result.final.organFullId = null;
                initByExistData(response.result.final);
            }
        });

        //进入开户页面提醒生成流水id，方便影像调用
        $('#recId').val(recId);
    }

    if(buttonType == 'applyValidate') {
        $.get('../../organization/orgTree', function (data) {
            $('#bankCode').val(data.data[0].pbcCode);
            $('#bankName').val(data.data[0].name);
        })
        $.get('../../apply/bank/view?applyId=' + applyid).done(function (response) {
            if (response.rel == false) {
                layer.alert(response.msg, {
                    title: "提示",
                    closeBtn: 0
                }, function () {
                    parent.tab.deleteTab(parent.tab.getCurrentTabId());
                });
            } else {
                if (response.msg) {
                    layer.alert(response.msg, {
                        title: "提示",
                        closeBtn: 0
                    });
                }

                initByExistData(response.result);
            }
        });
    }

    laydate.render({
        elem: '#operatorIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#operatorIdcardDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("operatorIdcardDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#acctCreateDate',
        format: 'yyyy-MM-dd',
        max : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#acctCreateDate"));
            validator.element($("#effectiveDate"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("acctCreateDate",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#fileSetupDate',
        format: 'yyyy-MM-dd',
        max : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#fileSetupDate"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("fileSetupDate",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#fileDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
        	if($("#fileType").val() == "01"){
    			$("#businessLicenseDue").val(value);
    		}
            validator.element($("#fileDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("fileDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#businessLicenseDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#businessLicenseDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("businessLicenseDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#legalIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#legalIdcardDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("legalIdcardDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#orgCodeDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#orgCodeDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("orgCodeDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#stateTaxDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#stateTaxDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("stateTaxDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#taxDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#taxDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("taxDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#parOrgEccsDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#parOrgEccsDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("parOrgEccsDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#parLegalIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#parLegalIdcardDue"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("parLegalIdcardDue",value,map,changeFieldsMap);
            }
        }
    });

    laydate.render({
        elem: '#effectiveDate',
        format: 'yyyy-MM-dd',
        done: function(value, date, endDate){
            validator.element($("#effectiveDate"));
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerDate("effectiveDate",value,map,changeFieldsMap);
            }
        }
    });

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#cancel').click(function(){
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //提交
    $('#saveBtn').click(function(){
        $('#submitBtnType').val('save')
        $("#formId").submit();
    });

    //打印
    $('#printBtn').click(function(){
        submitForm(toJson($("#formId").serializeArray()), 'print');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //保存
    $('#keepBtn').click(function(){
        submitForm(toJson($("#formId").serializeArray()), 'keep');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可
    });

    //上报
    // $('#verifyFormBtn').click(function(){
    //     $('#submitBtnType').val('verifyForm')
    //     $("#formId").submit();
    // });

    //补录
    $('#addInfoFormBtn').click(function(){
        $('#submitBtnType').val('addInfoForm')
        $("#formId").submit();

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })


    $('#verifyPass').click(function () {
        $('input,select,textarea').removeAttr('disabled');
        $('#submitBtnType').val('verifyPass');
        $("#formId").submit();
    });

    //转正常按钮
    $('#acctGoNormalBtn').click(function(){
        var name = $('#depositorName').val();
        var title = '开户-' + name;
        var params = '?billId=' + billId + '&buttonType=yanziToNormal'
        layer.open({
            title: false
            ,content: '账户类型'
            ,area: '700px'
            ,skin: 'acctype-layer-open'
            ,btn: ['基本存款账户', '一般存款账户', '预算单位专用存款账户', '非预算单位专用存款账户', '临时机构临时存款账户', '非临时机构临时存款账户', '特殊单位专用存款账户']
            ,yes: function(index, layero){
                layer.close(index);
                //基本存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/jibenOpen.html' + params + '&type=jiben'
                });
            }
            ,btn2: function(index, layero){
                layer.close(index);
                //一般存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/yibanOpen.html' + params + '&type=yiban'
                });
            }
            ,btn3: function(index, layero){
                layer.close(index);
                //预算单位专用存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/yusuanOpen.html' + params + '&type=yusuan'
                });
            }
            ,btn4: function(index, layero){
                layer.close(index);
                //非预算单位专用存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/feiyusuanOpen.html' + params + '&type=feiyusuan'
                });
            }
            ,btn5: function(index, layero){
                layer.close(index);
                //临时机构临时存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/linshiOpen.html' + params + '&type=linshi'
                });
            }
            ,btn6: function(index, layero){
                layer.close(index);
                //非临时机构临时存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/feilinshiOpen.html' + params + '&type=feilinshi'
                });
            }
            ,btn7: function(index, layero){
                layer.close(index);
                //特殊单位专用存款账户
                parent.tab.tabAdd({
                    title: title,
                    href: 'accttype/teshuOpen.html' + params + '&type=teshu'
                });
            }
        });

    })
    

    form.on('select', function(data){
        $(data.elem).blur();
    });

    form.render('select');

    removeAll();

    $('#verifyNotPass').on('click', function () {
        $.get('reject.html', null, function (form) {
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
                },
                btn2: function(){
                    $("#verifyNotPass").show();
                },cancel: function () {
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
                        // $('#rejectBtn').attr('disabled', 'disabled');
                        $.post("../../allAccountPublic/form/reject", {"id": billId ,"formId": billId,"denyReason" : data.field.denyReason ,"action":"verifyNotPass"}, function (data) {
                            layer.alert('审核退回成功', {
                                title: "提示",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(index);
                                addCompanyAccountsTab(common, buttonType, billId);

                            });
                            $('#rejectBtn').show();
                        }).error(function (err) {
                            layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                            $('#rejectBtn').show();
                        });
                        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                    });
                }
            });
        });
        $("#verifyNotPass").hide();
    });

    $('#verifyFormBtn').on('click', function () {
        $.get('syncSystem.html', null, function (form) {
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
                },cancel: function () {
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
                    form = layui.form;
                    form.render();
                    //上报
                    form.on('submit(edit)', function (data) {
                        var pbcSync = $("#isSyncAms").prop("checked");
                        var eccsSync = $("#isSyncEccs").prop("checked");
                        if (pbcSync == false && eccsSync == false) {
                            layer.alert("请勾选上报系统");
                            return false;
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
                        layer.close(index);
                        return false;
                    });
                }
            });
        });
        $("#verifyFormBtn").hide();
    });

    if(operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN' ||buttonType == 'saicValidate' || buttonType == 'applyValidate'){
        var validLabelBase = ["acctNo","acctType","bankName","bankCode","acctCreateDate","effectiveDate"];
        var validLabelCustomer = ["depositorName","regFullAddress","regAreaCode","regProvinceChname","regAddress",
            "industryCode","fileType","fileNo","isIdentification"];//客户信息
        var validLabelLegal = ["legalType","legalName","legalIdcardType","legalIdcardNo"];//法人代表信息
        var validLabelOrg = ["orgSelect"];//组织结构代码证信息
        var validLabelContact = ["telephone","zipcode"];//联系信息
        var docCodeFlag = ["docCode"];//影像
        addValidateFlag(validLabelBase.concat(validLabelCustomer,validLabelLegal,validLabelOrg,validLabelContact,docCodeFlag));
        
        var rules = {
            acctNo : {
                required : true,
                maxlength : 32
            },
            acctType : "required",
            bankName : "required",
            bankCode : {    //开户银行代码
                required : true,
                fixedLength : 12
            },
            acctCreateDate : {//开户日期
                required : true,
                maxDate: new Date().Format("yyyy-MM-dd")
            },
            effectiveDate : {//有效期
                required : true,
                minDate: new Date().Format("yyyy-MM-dd"),
                minDateByElement : "#acctCreateDate",
                notEqualTo : "#acctCreateDate",
                notExceedYearsByElement: ["#acctCreateDate",2]
            },
            depositorName : {//存款人名称
                required: true,
                stringMaxLength : 128
            },
            acctShortName : {//存款人简称
                stringMaxLength : 60
            },
            regAddress : {//注册地址（详细地址）
                required: true,
                stringMaxLength : 128
            },
            regFullAddress : "required",//工商注册地址
            regAreaCode :{//注册地地区代码
                required: true,
                fixedLength: 6
            },
            industryCode : "required",//行业归属
            regNo :{ //工商注册编号
                lengthOrOther : [18,15]
            },
            fileType : "required",//证明文件1种类
            fileNo : "required",//证明文件1编号
            isIdentification : "required",//未标明注册资金
            acctFileType2 : {
                shouldAllRequiredOrNot : ["#acctFileNo2"]
            },
            acctFileNo2 : {
                shouldAllRequiredOrNot : ["#acctFileType2"]
            },
            fileType2 : {
                shouldAllRequiredOrNot : ["#fileNo2"]
            },
            fileNo2 : {
                shouldAllRequiredOrNot : ["#fileType2"]
            },
            fileSetupDate : {//成立日期
                maxDate: new Date().Format("yyyy-MM-dd")
            },
            fileDue : {//到期日期
                minDate: new Date().Format("yyyy-MM-dd")
            },
            businessLicenseDue:{//营业执照到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            businessScope : {//经营范围（人行账管系统）
//  			required: true,
                maxlength : 489
            },
            businessScopeEccs : {//经营范围（人行账管系统）
                maxlength : 200
            },
            legalType : "required",//法人代表（负责）人
            legalName : "required",//姓名
            legalIdcardType : "required",//证件类型
            legalIdcardNo :{//证件号码
                required: true,
                maxlength: 20
            },
            legalIdcardDue:{//证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            legalTelephone:{//法人代表联系电话
                isTelphoneOrMobile : true
            },
            linkSelectInput10001 : {//组织机构代码类别
                required: true,
                rightAngleCharNumber : 1
            },
            orgCode :{
                minlength: 9,
                maxlength: 9
            },
            workAddress : {//办公地址(详细地址)
                stringMaxLength : 128
            },
            parOrgCode : {
                maxlength: 9
            },
            accountKey:{
                checkLinshiParAccountKey:true
            },
            parAccountKey:{
                checkLinshiParAccountKey:true
            },
            parLegalType:{
                shouldAllRequiredOrNot :["#parLegalName","#parLegalIdcardType","#parLegalIdcardNo"]
            },
            parLegalName:{
                stringMaxLength : 32,
                shouldAllRequiredOrNot :["#parLegalType","#parLegalIdcardType","#parLegalIdcardNo"]
            },
            parLegalIdcardType:{
                shouldAllRequiredOrNot :["#parLegalType","#parLegalName","#parLegalIdcardNo"]
            },
            parLegalIdcardNo:{
                maxlength: 18,
                shouldAllRequiredOrNot :["#parLegalType","#parLegalName","#parLegalIdcardType"]
            },
            telephone : {//联系电话
                required: true,
                isTelphoneOrMobile : true
            },
            zipcode:{//邮政编码
                required: true,
                isZipCode : true
            },
            parLegalTelephone:{
                isMobile : true
            },
            partnerTelephone :{
                isTelphoneOrMobile : true
            },
            operatorTelephone :{
                isMobile : true
            },
            financeTelephone :{
                isTelphoneOrMobile : true
            },
            noTaxProve:{
//			required: true,
                stringMaxLength : 32
            },
            stateTaxDue : {//国税证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            taxDue : {//地税证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            parOrgEccsDue : {//机构信用证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            parLegalIdcardDue : {//上级法人证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            operatorIdcardDue : {//证件到期日
                minDate: new Date().Format("yyyy-MM-dd")
            }
        };

        var messages = {
            linkSelectInput10001 : {
                rightAngleCharNumber : "组织机构类别及其细分不能为空"
            },
            effectiveDate:{
                minDateByElement : "有效期不能小于开户日期",
                notEqualTo : "开户日期不能是同一天",
                notExceedYearsByElement : "有效期不能超过开户日期2年"
            },
            acctFileType : {
                shouldAllRequiredOrNot : "非必填，但与开户证明文件种类编号1必须同时联动必填"
            },
            acctFileNo : {
                shouldAllRequiredOrNot : "非必填，但与开户证明文件种类1必须同时联动必填"
            },
            acctFileType2 : {
                shouldAllRequiredOrNot : "非必填，但与开户证明文件种类编号2必须同时联动必填"
            },
            acctFileNo2 : {
                shouldAllRequiredOrNot : "非必填，但与开户证明文件种类2必须同时联动必填"
            },
            fileType2 : {
                shouldAllRequiredOrNot : "非必填，但与证明文件2编号必须同时联动必填"
            },
            fileNo2 : {
                shouldAllRequiredOrNot : "非必填，但与证明文件2种类必须同时联动必填"
            },
            regNo:{
                lengthOrOther : "必须为18位或者15位"
            },
            noTaxProve : {
                manyChooseOne : "这些税务登记信息必须填写一个"
            },
            stateTaxRegNo : {
                manyChooseOne : "这些税务登记信息必须填写一个"
            },
            taxRegNo : {
                manyChooseOne : "这些税务登记信息必须填写一个"
            },
            orgCode :{
                minlength: "必须为9位",
                maxlength: "必须为9位"
            }

        };
    }


    if(operateType == 'ACCT_SUSPEND' || billType == 'ACCT_SUSPEND' || billType == 'ACCT_REVOKE' || operateType == 'ACCT_REVOKE'){
        addValidateFlag(["acctNo","acctType"]);

        var rules = {
            acctNo : {
                required : true,
                maxlength : 32
            },//账号
            acctType : "required",//账户性质
        };

        var messages = {
        };
    }

    if(billType == 'ACCT_CHANGE' || operateType == 'ACCT_CHANGE'){
        addValidateFlag(["acctNo","acctType","bankCode"]);

        var rules = {
            acctNo : {
                required : true,
                maxlength : 32
            },//账号
            acctType : "required",//账户性质
            bankCode : {    //开户银行代码
                required : true,
                fixedLength : 12
            },
            orgCode :{
                minlength: 9,
                maxlength: 9
            },
            financeTelephone: {//财务主管电话
                isTelphoneOrMobile: true
            },
            parLegalTelephone: {
                isMobile: true
            },
            partnerTelephone: {
                isTelphoneOrMobile: true
            },
            operatorTelephone: {
                isMobile: true
            },
            fundManagerTelephone: {
                isTelphoneOrMobile: true
            },
            legalTelephone: {
                isTelphoneOrMobile: true
            },
            telephone: {//联系电话
                isTelphoneOrMobile: true
            }
        };

        var messages = {
            orgCode :{
                minlength: "必须为9位",
                maxlength: "必须为9位"
            }
        };
    }
    
    $.validator.setDefaults({
    	rules: rules,
        messages: messages
    });

    otherValidate = function(){
    	//证明文件2种类 和 文件编号2 联动必填
        fileType2Change(form,validatorParam);
    
    	//如证明文件类型为营业执照，默认联动证明文件1编号
    	//如证明文件类型为营业执照，默认联动证明文件1的到期日期
    	//同时限时下面的经营信息
    	
        // 	fileTypeChange(form,validator);
        lsFleTypeChange(form,validatorParam);
 	
    	//如注册资金未注明选择“是”，该字段为不可填，
    	//币种根据工商返显的单位进行自动判断，自动赋值，
    	isIdentificationChange(form,validatorParam,false);

    	//经营范围（人行账管系统）上限500，超过时最后一个字符是“等”，自动同步到经营范围（信用机构代码），最多200个字符，超过时最后一个字符为“等”
    	businessScopeChange(true,validatorParam);

    	//如证件类型为身份证，证件号码则根据身份证规则进行输入校验
    	legalIdcardTypeChange(form,validatorParam);

    	//如“无需”输入框内有值，则将国地税输入框锁定，
    	//如“无需”输入框为空，则国地税至少有一项必填，
    	noTaxProveChange(validatorParam);
    	

    	//上级组织机构代码不为空时,机构名称必录
    	//上级组织机构代码不为空时,基本户开户许可核准号必录
    	//上级组织机构代码不为空时,法人姓名必录，不能超过32个字符或16个汉字
//  	parOrgCodeChange(validator);
    

        //上级组织机构信用代码不为空时，机构名称和法人姓名联动必填，核准号以L开头
        parAccountKeyChange(validatorParam);

    	//股东信息的关系人类型，当选1高管时：类型包括：实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人
    	//当选2股东：自然人、机构
    	partnerTypeChange(form);

    	//上级机构信息，证件号码则根据身份证规则进行输入校验
//    	parLegalIdcardTypeChange(form,validator);
    	
    	//股东信息，证件号码则根据身份证规则进行输入校验
    	idcardTypeChange(form,validatorParam);
    	
    	//授权经办人信息，证件号码则根据身份证规则进行输入校验
        operatorIdcardTypeChange(form,validatorParam);
        
        //联系信息中‘与注册地是否’为'1-是'时同步注册地址
        isSameRegistAreaChange(form,operateType,billType,buttonType,workAddressPicker,thirdObjectJustValidate);
        
    }

    otherInit = function(){
    	//初始化：
    	//上级组织机构代码不为空时,机构名称必录
    	//上级组织机构代码不为空时,基本户开户许可核准号必录
    	//上级组织机构代码不为空时,法人姓名必录
    	parOrgCodeInit(validatorParam);
    	
    	//初始化：股东信息，证件号码则根据身份证规则进行输入校验
    	idcardTypeInit(form,validatorParam);
    	
    	//初始化：授权经办人信息，证件号码则根据身份证规则进行输入校验
    	operatorIdcardTypeInit(form,validatorParam);
    	
    	//初始化：上级机构信息，证件号码则根据身份证规则进行输入校验
    	parLegalIdcardTypeInit(form,validatorParam);
    	
    	//初始化：如证件类型为身份证，证件号码则根据身份证规则进行输入校验
    	legalIdcardTypeInit(form,validatorParam);
    	
    	//存款人名称复制到存款人简称
    	depositorNameChange(validatorParam);

    	//未标明注册资金默认为否，如返显时注册资金有值，则未标明注册资金为：否
    	isIdentificationInit(validatorParam,false,form);

    	//如“无需”输入框内有值，则将国地税输入框锁定，
    	//如“无需”输入框为空，则国地税至少有一项必填，
    	noTaxProveInit(validatorParam);

    	//与注册地是否一致:默认为“是”
    	isSameRegistAreaInit();
    	
    	//默认下拉值为01-工商营业执照
        if(validatorParam != undefined){
            if($("#fileType").val() =="" || $("#fileType").val() ==null){
                $("#fileType").val("01");
                addValidate("businessScope",{required: true},true,validator)
            }
        }

    	//新开户页面，账号为非必填
    	newCreateCustomer(buttonType);
    }
    
    
    var buttonType = url.get("buttonType");
    var selectDisabled = false;
    if(buttonType == 'select') {
        $('input,select,textarea').prop("disabled",true)
        selectDisabled = true;
        $('#saveBtn').hide()
        $('#videoNo').prop("disabled",false);
        $('#videoNo').attr('lay-verify', '')
    } else if(buttonType == 'sync') {  //上报页面
        $('#keepBtn').hide();
        $('#saveBtn').hide();
        $('#videoNo').prop("disabled",false);
        $('#videoNo').attr('lay-verify', '')
    }

    if(buttonType != 'sync' && buttonType != 'update') {
        $('#syncDiv').hide()  //上报区块隐藏
    }

    if(buttonType != 'update') {
        $('#addInfoFormBtn').hide();  //隐藏补录按钮
    } else {  //待补录隐藏提交按钮
        $('#saveBtn').hide();
        $('#acctChangeBtn').hide();
        $('#acctSuspendBtn').hide();
        $('#acctRevokeBtn').hide();
    }

    if(pageCode == 'dsbAccount' || pageCode == 'dblAccount'){
        $('#verifyPass').hide();
        $('#verifyNotPass').hide();
    }


    var uri;
    if(buttonType === 'selectForChangeBtn') {
        hideInnerTabArray("tab_21", true, form);
        changeBtnStatus($('#acctType').val(), accountStatus);  //详情页进行变更操作页面展示
        selectDisabled = true;
        uri = "../../allAccountPublic/details";
        if (isPrint) {  //配置打印
            $('#printBtn').css('display', '');
        }

    } else {
        uri = "../../allBillsPublic/getFormDetails";
        //删除“关联业务流水”tab
        hideInnerTabArray("tab_20",true,form);
    }
    //统计进来的所有按钮隐藏
    if(string005==="accountStatistics"){
        $('#acctChangeBtn').hide();
        $('#acctSuspendBtn').hide();
        $('#acctRevokeBtn').hide();
        $('#acctExtensionBtn').hide();
        $('#acctGoNormalBtn').hide();
        $('#acctWhiteListGoNormalBtn').hide();
        $('#resetSelectPwd').hide();
    }
    if(billId) {
        $.get (uri + '?id=' + billId, null, function (data) {
            data = data.result;
            initByExistData(data);

            showImageAccountTabOnly(imageFlag,billId,billType == "" ? operateType : billType,false,data.acctType);

            //隐藏域赋值
            $('#id').val(data.id);
            $('#recId').val(recId);
            $('#customerNo').val(data.customerNo);
            if(operateType == 'ACCT_CHANGE' || operateType == 'ACCT_SUSPEND' ) {   //进入变更页面
                Blank();
                $('#billType').val(operateType)
                $('#saveBtn').show()
                $('#syncDiv').hide()  //上报区块隐藏
            }

            // changeSaicFieldStatus(validator);
            // changeOtherFeiJiBenFieldStatus(validator)

            /*if(buttonType == 'update' && billType == 'ACCT_CHANGE') {  //变更补录详情页
                changeAccountFieldStatus(validator)
                changeAllSaicFieldStatus($('#acctType').val(), validator)
                changeLegalFieldStatus(validator)
                changeOrgFieldStatus(validator)
                changeContactFieldStatus(validator)
                removeValidate("depositorName",true,validator,"required");
            }*/

            if(buttonType == 'update') {
                $('#formId').valid();
            }

            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE') {
                $.get ('../../allAccountPublic/changedItemShow', null, function (res) {
                    if (res == 'false') {
                        if(data.finalStatus == 'No'){
                            getChangedItem(map, billId);
                        }
                    }else{
                        getChangedItem(map, billId);
                    }
                });
            }

            if(buttonType == 'sync') {  //上报页面
                removeValidateFlag(["acctNo"]);
                removeValidate("acctNo", true, validator, "required");
            }

            if(operateType == 'ACCT_CHANGE') {

                //临时变更-账户信息，账号和开户日期字段不可编辑
                $("#acctNo").attr("disabled",true);
                $("#acctNo").addClass("disableElement");
                $("#acctCreateDate").attr("disabled",true);
                $("#acctCreateDate").addClass("disableElement");

                //临时变更-授权经办人，姓名、证件类型、证件号码和到期日字段不可编辑
                $("#operatorName").attr("disabled",true);
                $("#operatorName").addClass("disableElement");
                $("#operatorIdcardType").attr("disabled",true);
                $("#operatorIdcardType").addClass("disableElement");
                $("#operatorIdcardNo").attr("disabled",true);
                $("#operatorIdcardNo").addClass("disableElement");
                $("#operatorIdcardDue").attr("disabled",true);
                $("#operatorIdcardDue").addClass("disableElement");

                form.render('select');
            }
        });


    }

    /**
     * 根据现有数据回填表单
     * @param data
     */
    function initByExistData(data) {

        if(data){
        	//fileTypeOfLinShi();
            setLabelValues(data,map);

            //省市区组件
            var regAddressCode,regAddressType;
            if(data.regArea){
                regAddressCode = data.regArea;
                regAddressType = 3;
            } else if(data.regCity){
                regAddressCode = data.regCity;
                regAddressType = 3;
            } else if(data.regProvince){
                regAddressCode = data.regProvince;
                regAddressType = 3;
            }

            if(buttonType == 'update' && billType == 'ACCT_CHANGE' ){//补录&&变更
            	initAddress('reg',regAddressPicker,regAddressCode,regAddressType,thirdObject, selectDisabled);
            }else{
                if(operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN' || buttonType == 'saicValidate' || buttonType == 'applyValidate') {
                    initAddress('reg',regAddressPicker,regAddressCode,regAddressType,thirdObjectValidate, selectDisabled);
                }else{
                    initAddress('reg',regAddressPicker,regAddressCode,regAddressType,null, selectDisabled);
                }
            }

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

            //注册地址区域修改->保存内容到注册地地区代码
            form.on('select(regArea)',function (data) {
                $('#regAreaCode').val($("#regArea").find("option:selected").attr("otherValue"));
                $('#regAreaCode').blur();

                //联系信息中‘与注册地是否’为'1-是'时同步注册地址
                regAreaChange(data.value,operateType,billType,buttonType,workAddressPicker,thirdObjectJustValidate);
            });
        }

        if(!$('#acctCreateDate').val()){
            //开户默认当前时间
            $('#acctCreateDate').val(new Date().Format("yyyy-MM-dd"));
        }

        //disable 账户类型
        $('#acctType').attr("disabled",true);

        if(operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN' || buttonType == 'saicValidate' || buttonType == 'applyValidate'){
            validatorParam = validator;
        }

        otherValidate();

        otherInit();

        if (buttonType == 'sync' || buttonType == 'update') {
            if(data.pbcSyncStatus == 'tongBuChengGong' || data.pbcSyncStatus == 'buTongBu') {
                $('#isSyncAmsDiv').hide();
            }
        }

        form.render('select');

        //loading.hide();
    }


    // //渲染其他组件
    // setTimeout(function () {
    //
    // })

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
            selected: function (item, dom, valueArr) {
                if (selectedCallback) selectedCallback(item, dom, valueArr);
            }
        });
    }


    if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
        listenerAllSelect(form,map,changeFieldsMap);
        listenerAllInput(map,changeFieldsMap);
    }
    
    var post_flag = false; 
    function submitForm(data, submitOperateType){
        var msg;
        var url = '../../allAccountPublic/form/submit';
        var url3 = '../../allAccountPublic/form/verifyPass';//审核通过

        if(submitOperateType == 'keep') {
            data.action='keepForm';
            msg = "保存"
        } else if(submitOperateType == 'save') {
            data.changeFields = JSON.stringify(changeFieldsMap.iterations());
            if (buttonType !== 'applyValidate' && (operateType === 'ACCT_CHANGE' || operateType === 'ACCT_SUSPEND')) {
                data.preOpenAcctId = "";//当不是从预约模块生成的流水，执行变更久悬时，设置预约编号为空
            }
            data.action='saveForm';
            msg = "提交"
        } else if(submitOperateType == 'verifyForm') {
            if($('#isSyncAms').prop("checked") == true) {
                data.isSyncAms = true;
            } else {
                data.isSyncAms = false;
            }
            data.changeFields = JSON.stringify(changeFieldsMap.iterations());
            data.action='verifyForm'
            msg = "上报"
        } else if(submitOperateType == 'addInfoForm') {
            data.changeFields = JSON.stringify(changeFieldsMap.iterations());
            data.action='addInfoForm'
            msg = "补录"
        } else if (submitOperateType == 'verifyPass') {
            data.action = 'verifyPass'
            msg = "审核通过"
        }

        data.acctType = $('#acctType').find("option:selected").val();
        loopSelectedDisabled(data);
        loopInputDisabled(data);
        if(data.acctType){
            //地址信息赋值
            data.regProvinceChname = $('#regProvince').find("option:selected").text();
            data.regCityChname = $('#regCity').find("option:selected").text();
            data.regAreaChname = $('#regArea').find("option:selected").text();
            data.workProvinceChname = $('#workProvince').find("option:selected").text();
            data.workCityChname = $('#workCity').find("option:selected").text();
            data.workAreaChname = $('#workArea').find("option:selected").text();

            if(submitOperateType == 'verifyForm') {
                // $('#verifyFormBtn').attr('disabled', 'disabled');
                if(post_flag) return; 
                post_flag = true;
                var layerLoading = layer.msg('正在处理，请等待......', {icon: 16, shade: 0.3, time:0});
                $.post(url, data, function (data) {
                    layer.close(layerLoading);
                    if (data.submitResult == 'success') {
                        layer.alert(msg + '成功', {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId);

                        });
                    } else if (data.submitResult == 'fail') {
                        layer.alert(data.submitMsg, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId);

                        });
                    } else {  //没勾选上报系统
                        layer.alert("提交成功", {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId);

                        });
                    }
                    // $('#verifyFormBtn').removeAttr('disabled');
                    $('#verifyFormBtn').show();
                    post_flag =false;
                }).error(function (err) {
                    layer.close(layerLoading);
                    layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                    $('#verifyFormBtn').show();
                    // $('#verifyFormBtn').removeAttr('disabled');
                    post_flag =false;
                });
            }  else if(submitOperateType == 'print') {   //打印按钮
                printfClick(common)

            } else if(submitOperateType == 'save') {  //提交操作
                $('#saveBtn').hide();
                $.post(url, data, function (data) {
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        $('#id').val(data.refBillId);

                        $.get("../../config/findByKey?configKey=printf", null, function (data) {
                            if(data == true) {  //配置打印
                                if($('#billType').val() == 'ACCT_OPEN' || $('#billType').val() == 'ACCT_CHANGE' || $('#billType').val() == 'ACCT_REVOKE') { //开变销打印按钮展示
                                    // $('#printBtn').css('display', '');

                                    layer.confirm('当前页面是否需要打印？', {
                                        btn: ['是','否'],
                                        cancel: function(index, layero){  //取消操作，点击右上角的X
                                            addCompanyAccountsTab(common, buttonType, billId);
                                        }
                                    }, function(){
                                        printfClick(common)

                                    }, function(){
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
            } else if (submitOperateType == 'verifyPass') {  //审核通过
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
            } else {
                $.post(url, data, function (data) {
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        addCompanyAccountsTab(common, buttonType, billId);

                    });
                }).error(function (err) {
                    layerTips.msg('处理submit失败['+err.responseJSON.message+']');
                });
            }
        } else {
            layerTips.msg('请到对公业务管理设置账户性质');
        }
    }


    var validator = $("#formId").validate({
        onkeyup : onKeyUpValidate,
        onfocusout : onFocusOutValidate,
        ignore: "",
        errorPlacement : errorPlacementCallback,
        highlight : highlight,
        unhighlight : unhighlight,
        showErrors: showErrors,
        submitHandler: function() {
            submitForm(toJson($("#formId").serializeArray()), $('#submitBtnType').val());

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });

    var thirdObject = new Object();
    thirdObject.regCallback = function(){
        form.on('select(regArea)',function (data) {
            $('#regAreaCode').val($("#regArea").find("option:selected").attr("otherValue"));
            $('#regAreaCode').blur();

            //联系信息中‘与注册地是否’为'1-是'时同步注册地址
            regAreaChange(data.value,operateType,billType,buttonType,workAddressPicker,thirdObjectJustValidate);
        });
    }
    var thirdObjectValidate = new Object();
    thirdObjectValidate.validateObject = validator;
    thirdObjectValidate.regCallback = thirdObject.regCallback;


    var thirdObjectJustValidate = new Object();
    thirdObjectJustValidate.validateObject = validator;
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

