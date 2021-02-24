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
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'imageInfo','laytpl'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        loading = layui.loading,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    loading2 = loading;

    var map = new Map();     var changeFieldsMap = new Map();
    var nowDate = new Date();
    var name = decodeURI(common.getReqParam("name"));
    var accountKey = decodeURI(common.getReqParam("accountKey"));
    var regAreaCode = decodeURI(common.getReqParam("regAreaCode"));
    var type = decodeURI(common.getReqParam("type"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var updateType = decodeURI(common.getReqParam("updateType"));
    var billType = decodeURI(common.getReqParam("billType"));
    var recId = decodeURI(common.getReqParam("recId"));
    var createdDate = decodeURI(common.getReqParam("createdDate"));
    var kernelOrgName = decodeURI(common.getReqParam("kernelOrgName"));

    var pageCode = decodeURI(common.getReqParam("pageCode"));

    var operateType = url.get("operateType");

    var billId = url.get("billId");
    var applyid = url.get("applyid");
    var refBillId = url.get("refBillId");
    var accountStatus = url.get("accountStatus");
    var imageFlag = url.get("imageFlag");//是否只显示账户影像信息
    var string005 = decodeURI(common.getReqParam("string005")); //账户报表统计页面跳转标记
    var whiteList;
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

    //---账户影像的逻辑判断---

    var accountImageBillsId = refBillId;
    if(accountImageBillsId == undefined || accountImageBillsId ==""){
        accountImageBillsId = billId;
    }
    if(accountImageBillsId == undefined || accountImageBillsId ==""){
        accountImageBillsId = recId;
    }
    if(operateType){
        accountImageBillsId='';
    }

    var accoungImageShow = buttonType != 'selectForChangeBtn';
    //---账户影像的逻辑判断---

    var validatorParam = undefined;
    var isCheck = true;   //上报模式：默认为审核模式
    var isPrint=false;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();

    // 企业名称
    var name = decodeURI(url.get("name"));
    $("#name").html(name);

    // 预约标号
    if(applyid){
        $('#preOpenAcctId').val(applyid);
    }

    $.get("../../apply/getVideoCallType", function (data) {
        if(data == 'local'){
            $("#applyVideoCall").hide();
        }else if(data == 'saas'){
            $("#applyVideoCall").show();
        }
    });
    $.ajaxSettings.async = false;
    $.get("../../config/findByKey?configKey=printf", null, function (data) {
        if (data == true) {
            isPrint =data;
            if (billType == 'ACCT_CHANGE' || billType == 'ACCT_OPEN') {
                $('#printBtn').css('display', '');
            }
        }
    });
    if(accoungImageShow){
        accountImageConfig(accoungImageShow);
    }

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

    $.get('../account/saic.html', function (form) {
        $("#saicDiv").html(form);

        $('#fileType').empty();
        $('#fileType').append("<option value=\"\">请选择</option>\n" +
            "<option value=\"12\">12-证券从业资格证书</option>\n" +
            "<option value=\"17\">17-其他</option>");
        
        $('#fileType2').empty();
        $('#fileType2').append("<option value=\"\">请选择</option>\n" +
            "<option value=\"13\">13-国家外汇管理局的批文</option>\n" +
            "<option value=\"17\">17-其他</option>");
    });
    $('#depositorType').empty();
    $('#depositorType').append("<option value=\"\">请选择</option>\n" +
        "<option value=\"50\">50-QFII</option>\n" +
        "<option value=\"51\">51-境外贸易机构</option>\n" +
        " <option value=\"52\">52-跨境清算</option>");

    form.render('select');

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

    if(updateType == 'pbc') {
        hideInnerTabArray("tab_7",true,form);
        hideInnerTabArray("tab_8",true,form);
        hideInnerTabArray("tab_9",true,form);
    } else {
        $.get('../account/relateCompany.html', function (form) {
            $("#relateCompanyDiv").html(form);
        });
        $.get('../account/stockHoder.html', function (form) {
            $("#stockHoderDiv").html(form);
        });
        $.get('../account/agent.html', function (form) {
            $("#agentDiv").html(form);
        });
    }

    $.get('../account/acct_hiddenfield.html', function (form) {
        $("#hiddenFieldDiv").html(form);
    });

    $.get('../../config/isCheck', function(data) {
        if(data == false) {
            isCheck = false;
        }
    });

    var dblToCheck;  //待补录页面根据系统设置上报方式进行按钮的配置
    var nowRoleName; //角色名称

    if(billType == 'ACCT_OPEN'){
        $.get('../../config/findConfigValue?configKey=openSyncStatus', null, function (data) {
            dblToCheck = data;
        });
    }
    if(billType == 'ACCT_CHANGE'){
        $.get('../../config/findConfigValue?configKey=changeSyncStatus', null, function (data) {
            dblToCheck = data;
        });
    }
    if(billType == 'ACCT_SUSPEND'){
        $.get('../../config/findConfigValue?configKey=suspendSyncStatus', null, function (data) {
            dblToCheck = data;
        });
    }
    if(billType == 'ACCT_REVOKE'){
        $.get('../../config/findConfigValue?configKey=revokeSyncStatus', null, function (data) {
            dblToCheck = data;
        });
    }
    if(billType == 'ACCT_CLOSESUSPEND'){
        $.get('../../config/findConfigValue?configKey=closeSuspendSyncStatus', null, function (data) {
            dblToCheck = data;
        });
    }
    $.get('../../role/getRoleName', null, function (data) {
        nowRoleName = data;
    });

    if(updateType == 'pbc') {
        //账户信息
        // removeElement('bankName'); //开户银行名称
        //客户信息
        removeElement('orgEnName'); //机构英文名称
        removeElement('regCountry'); //注册地址（省市区）
        removeElement('regAddress'); //注册地址（详细地址）
        // removeElement('regOffice'); //登记部门
        removeElement('regType'); //工商注册类型
        removeElement('regNo'); //工商注册编号
        removeElement('fileType2'); //证明文件2种类
        removeElement('fileNo2'); //证明文件2编号
        removeElement('businessLicenseNo'); //营业执照编号
        removeElement('businessLicenseDue'); //营业执照到期日
        //法人代表信息
        removeElement('legalIdcardDue'); //证件到期日
        removeElement('legalTelephone'); //联系电话
        //组织机构代码信息
        removeElement('orgSelect'); //组织机构代码类别
        removeElement('orgCodeDue'); //组织机构证到期日
        //税务登记信息
        removeElement('stateTaxDue'); //纳税人识别号（地税）到期日
        removeElement('taxDue'); //纳税人识别号（国税）到期日
        //联系信息
        removeElement('isSameRegistArea'); //与注册地是否一致
        removeElement('workProvinceChname'); //办公地址(省市区)
        removeElement('workAddress'); //办公地址(详细地址)
        removeElement('financeName'); //财务主管姓名
        removeElement('financeIdcardNo'); //财务主管身份证号
        removeElement('financeTelephone'); //财务主管电话
        //其他信息
        removeElement('economyType'); //经济类型
        removeElement('economyIndustrySelect'); //经济行业分类
        removeElement('corpScale'); //企业规模
        //上级机构信息
        removeElement('parOrgEccsNo'); //机构信用代码
        removeElement('parOrgEccsDue'); //上级机构信用证件到期日
        removeElement('parRegType'); //登记注册号类型
        removeElement('parRegNo'); //登记注册号码
        removeElement('parLegalIdcardDue'); //上级法人证件到期日
        removeElement('parLegalTelephone'); //法人手机号码
    }

    if(billType == 'ACCT_OPEN') {
        $("input[name='currency1']").attr("disabled","disabled");
        form.render('radio');
    }
    //更多
    function addRowsTpl(selector, ids, id, fnAdd) {
        var container = $(selector + 'Div');
        var getTpl = $(selector + 'Tpl').html();
        if (!id) id = Date.now();
        ids.push(id);
        laytpl(getTpl).render({ "id": id, "operateType": operateType }, function (html) {
            container.append(html);
            if(operateType == 'ACCT_CHANGE' || billType == 'ACCT_CHANGE'){
                listenerAllSelect(form,map,changeFieldsMap);
                listenerAllInput(map,changeFieldsMap);

            }
            form.render('select');
            fnAdd && fnAdd(id);
            //设置最新的移除的id
            $(selector + 'DelBtn').attr('data-id',id);
        });
    }

    $("#stockHoderDelBtn").click(function () {
        //移除最后一个
        var id = $(this).attr('data-id');
        $('#stockHoder_' + id).remove();
        //设置下次移除的
        $(this).attr('data-id',id-1);
        if(partnerIdsMaxId>1){
            partnerIdsMaxId--;
        }
    });

    //股东/高管信息-更多
    var partnerIds = [], partnerIdsMaxId =1;
    $('#stockHoderMoreBtn').click(function () {
        if (partnerIdsMaxId < 10) {
            addRowsTpl('#stockHoder', partnerIds, partnerIdsMaxId, function (id) {
                //校验
                partnerIdsMaxId++;
            });
        } else {
            layer.msg('股东/高管信息不能超过10个');
        }
        return false;
    });
    if(accountStatus==='suspend'){
        //当账户状态为久悬的时候才显示撤销久悬按钮
        $('#acctCloseSuspendBtn').show();
    }
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
        value : new Date(),
        done: function(value, date, endDate){
            validator.element($("#acctCreateDate"));
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
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可
    });

    //保存
    $('#keepBtn').click(function(){
        submitForm(toJson($("#formId").serializeArray()), 'keep');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可
    });

    //上报
    // $('#verifyFormBtn').click(function(){
    //     $('input,select,textarea').removeAttr('disabled');
    //     $('#submitBtnType').val('verifyForm');
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

    //删除
    $('#deleteBtn').click(function(){
        deleteBillsAndAccount(billId,function(){
            parent.tab.deleteTab(parent.tab.getCurrentTabId());
        })
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可
    });
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
                        $.post("../../allAccountPublic/form/reject", {"id": billId ,"formId": billId,"denyReason" : data.field.denyReason ,"action":"verifyNotPass", 'preOpenAcctId': $('#preOpenAcctId').val(), 'billType': billType}, function (data) {
                            layer.alert('审核退回成功', {
                                title: "提示",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(index);
                                addCompanyAccountsTab(common, buttonType, billId,whiteList);

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

    var syncSystem = true;
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
                },
                cancel: function () {
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
                        //针对无需上报人行账户增加
                        syncSystem = false;
                    }
                    form = layui.form;
                    form.render();
                    //上报
                    form.on('submit(edit)', function (data) {
                        var pbcSync = $("#isSyncAms").prop("checked");
                        var eccsSync = $("#isSyncEccs").prop("checked");
                        if (pbcSync == false && eccsSync == false) {
                            //1 是白名单账户，不需要上报人行  跳过该验证
                            if(whiteList != '1' && syncSystem){
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
                        layer.close(index);
                        return false;
                    });
                }
            });
        });
        $("#verifyFormBtn").hide();
    });

    if(operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN' || buttonType == 'saicValidate' || buttonType == 'applyValidate') {
        var validLabelBase = ["acctNo","acctType","bankName","bankCode","acctCreateDate","capitalProperty"];//账户信息
        var validLabelCustomer = ["depositorName","regAddress","regFullAddress","regAreaCode","acctShortName",
            "industryCode","regOffice","fileType","fileNo","isIdentification",
            "regCurrencyType","registeredCapital","businessScope"];//客户信息
        var validLabelLegal = ["legalType","legalName","legalIdcardType","legalIdcardNo","legalIdcardDue", "nationality"];//法人代表信息
        var validLabelContact = ["workProvinceChname","workAddress","telephone","zipcode"];//联系信息
        var docCodeFlag = ["docCode"];//影像
        addValidateFlag(validLabelBase.concat(validLabelCustomer,validLabelLegal,validLabelContact,docCodeFlag));

        var rules = {
            acctNo : "required",//账号
            acctType : "required",//账户性质
            bankName : "required",//开户银行名称
            bankCode : {    //开户银行代码
                required : true,
                fixedLength : 12
            },
            acctCreateDate : {//开户日期
                required : true,
                maxDate: new Date().Format("yyyy-MM-dd")
            },
            capitalProperty : {//资金性质
                required : true
            },
            depositorName : {//存款人名称
                required: true,
                stringMaxLength : 128
            },
            acctShortName : {//存款人简称
                required: true,
                stringMaxLength : 60
            },
            depositorType : "required",//存款人类别
            regAddress : {//注册地址（详细地址）
                required: true,
                stringMaxLength : 128
            },
            regFullAddress : "required",//工商注册地址
            regAreaCode :{
                required: true,
                fixedLength: 6
            },
            industryCode : "required",//行业归属
            regOffice : "required",//登记部门
            regNo :{ //工商注册编号
                lengthOrOther : [18,15]
            },
            fileType : "required",//证明文件1种类
            fileNo : "required",//证明文件1编号
            fileSetupDate : {//成立日期
                maxDate: new Date().Format("yyyy-MM-dd")
            },
            fileDue : {//到期日期
                minDate: new Date().Format("yyyy-MM-dd")
            },
            businessLicenseDue:{//营业执照到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            isIdentification : "required",//未标明注册资金
            regCurrencyType : "required",//注册资金币种
            registeredCapital:{
                isNumber: true
            },//注册资金（元）
            businessScope : {//经营范围（人行账管系统）
                maxlength : 489
            },
            legalType : "required",//法人代表（负责）人
            legalName : "required",//姓名
            nationality : "required",//国籍
            legalIdcardType : "required",//证件类型
            legalIdcardNo :{//证件号码
                required: true,
                maxlength: 20
            },
            legalIdcardDue:{//证件到期日
                required: true,
                minDate: new Date().Format("yyyy-MM-dd")
            },
            orgCodeDue : {//组织机构证到期日
                minDate: new Date().Format("yyyy-MM-dd")
            },
            noTaxProve : {//无需办理税务登记证的文件或税务机关出具的证明
                stringMaxLength : 32
            },
            fileType2 : {
                shouldAllRequiredOrNot : ["#fileNo2"]
            },
            fileNo2 : {
                shouldAllRequiredOrNot : ["#fileType2"]
            },
            orgCode :{
                minlength: 9,
                maxlength: 9
            },
            workAddress : {//联系信息-办公地址(详细地址)
                required : true,
                stringMaxLength : 128
            },
            telephone : {//联系电话
                required : true,
                isTelphoneOrMobile : true
            },
            zipcode:{//邮政编码
                required : true,
                isZipCode : true
            },
            parOrgCode : {
                maxlength: 9
            },
            accountKey:{
//  	        checkJiBenParAccountKey:true
                fixedLength : 14
            },
            parAccountKey:{
//  	        checkJiBenParAccountKey:true
                fixedLength : 14
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
            legalTelephone :{//手机号码
                isTelphoneOrMobile : true
            },
            legalTelephone :{//手机号码
                isTelphoneOrMobile : true
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
            legalName : {
                equalTo : "必须与存款人姓名相同"
            },
            noTaxProve : {
                manyChooseOne : "这些税务登记信息必须填写一个"
            },
            // stateTaxRegNo : {
            //     manyChooseOne : "这些税务登记信息必须填写一个"
            // },
            // taxRegNo : {
            //     manyChooseOne : "这些税务登记信息必须填写一个"
            // },
            fileType2 : {
                shouldAllRequiredOrNot : "非必填，但与证明文件2编号必须同时联动必填"
            },
            fileNo2 : {
                shouldAllRequiredOrNot : "非必填，但与证明文件2种类必须同时联动必填"
            },
            regNo:{
                lengthOrOther : "必须为18位或者15位"
            },
            orgCode :{
                minlength: "必须为9位",
                maxlength: "必须为9位"
            }
        };
    }
    otherValidate = function(){

    	//如证明文件类型为营业执照，默认联动证明文件1编号
    	//如证明文件类型为营业执照，默认联动证明文件1的到期日期
        fileTypeChange(form);
        
        //证明文件2种类 和 文件编号2 联动必填
        fileType2Change(form,validatorParam);

    	//如注册资金未注明选择“是”，该字段为不可填，
    	//币种根据工商返显的单位进行自动判断，自动赋值，
    	isIdentificationChange(form,validatorParam,true);

    	//经营范围（人行账管系统）上限500，超过时最后一个字符是“等”，自动同步到经营范围（信用机构代码），最多200个字符，超过时最后一个字符为“等”
    	businessScopeChange(false,validatorParam);

    	//上级法定代表人或负责人,姓名、身份证明文件种类及其编号数据项联动必填
    	parLegalTypeChange(form,validatorParam);

    	//如证件类型为身份证，证件号码则根据身份证规则进行输入校验
    	legalIdcardTypeChange(form,validatorParam);

    	//上级组织机构代码不为空时,机构名称必录
    	//上级组织机构代码不为空时,基本户开户许可核准号必录
    	//上级组织机构代码不为空时,法人姓名必录，不能超过32个字符或16个汉字
    	parOrgCodeChange(validatorParam);


    	//股东信息的关系人类型，当选1高管时：类型包括：实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人
    	//当选2股东：自然人、机构
    	partnerTypeChange(form);

    	//如“无需”输入框内有值，则将国地税输入框锁定，
    	//如“无需”输入框为空，则国地税至少有一项必填，
    	// noTaxProveChange(validatorParam);

    	//上级机构信息，证件号码则根据身份证规则进行输入校验
//    	parLegalIdcardTypeChange(form,validator);

    	//股东信息，证件号码则根据身份证规则进行输入校验
    	idcardTypeChange(form,validatorParam);

    	//授权经办人信息，证件号码则根据身份证规则进行输入校验
    	operatorIdcardTypeChange(form,validatorParam);

        //特殊-人行上报
        isTeshuPbcSyncChange(form,validatorParam);

        //上级开户许可证不为空时机构名称和法人姓名联动必填
        parAccountKeyValidChange(validatorParam);

        //联系信息中‘与注册地是否’为'1-是'时同步注册地址
        isSameRegistAreaChange(form,operateType,billType,buttonType,workAddressPicker);

        //币种类型
        currencyTypeChange(form, validatorParam);
        //账户影像变动
        form.on('select(depositorType)',function (data) {
            selecImageConfig(data.value,type,billType);
            if(accoungImageShow){
                if(operateType){
                    var temprecid =refBillId;
                    if(temprecid == undefined || temprecid ==""){
                        temprecid = recId;
                    }
                    if(temprecid == undefined || temprecid ==""){
                        temprecid = billId;
                    }
                    runImageData(temprecid,billType == "" ? operateType : billType,buttonType == 'select');
                }else{
                    runImageData(accountImageBillsId,billType == "" ? operateType : billType,buttonType == 'select');
                }
            }
        });
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
    	isIdentificationInit(validatorParam,true,form);

    	//如“无需”输入框内有值，则将国地税输入框锁定，
    	//如“无需”输入框为空，则国地税至少有一项必填，
    	// noTaxProveInit(validatorParam);

    	//与注册地是否一致:默认为“是”
    	isSameRegistAreaInit();
        //币种类型
        currencyTypeInit(validatorParam);

    	//机构状态:默认值正常
    	if($.trim($("#orgStatus").val()) == ""){
    		$("#orgStatus").val("1");
    	}

    	//基本户状态:默认值正常
    	if($.trim($("#basicAccountStatus").val()) == ""){
    		$("#basicAccountStatus").val("1");
    	}

    	//新开户页面，账号为非必填
    	newCreateCustomer(buttonType);

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
            registeredCapital:{
                isNumber: true
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

    var buttonType = url.get("buttonType");
    var selectDisabled = false;
    if(buttonType == 'select') {
        $('input,select,textarea').prop("disabled",true)
        selectDisabled = true;
        $('#saveBtn').hide()
        $('#videoNo').prop("disabled",false);
        $('#videoNo').attr('lay-verify', '')
    } else if(buttonType == 'sync') {  //上报页面
        //待审核 页面设置成不可改写(帐号可补录) begin
        $('input,select,textarea').prop("disabled",true);
        selectDisabled = true;
        $('input[type=checkbox]').prop("disabled",false);
        $('input[type=checkbox][name="currency0"]').prop("disabled", true);
        //待审核 页面设置成不可改写(帐号可补录) end
        $('#keepBtn').hide();
        $('#saveBtn').hide();
        $('#videoNo').prop("disabled",false);
        $('#videoNo').attr('lay-verify', '')
    }

    if(pageCode == 'dsbAccount' || pageCode == 'dblAccount'){
        $('#verifyPass').hide();
        $('#verifyNotPass').hide();
    }


    if(isCheck == true) {  //审核模式
        if(buttonType != 'sync' && buttonType != 'update') {  //待审核页面和补录页面展示
            $('#syncDiv').hide();  //上报区块隐藏
        }
    } else {  //无审核模式
        if(buttonType != 'sync' && buttonType != 'update' && buttonType != 'saicValidate' && buttonType != 'editAcct') {  //待上报、补录、开户、变更久悬界面
            $('#syncDiv').hide();  //上报区块隐藏
        } else if(buttonType == 'saicValidate' || buttonType == 'editAcct') {
            $('#verifyBtnName').html('上报');
        }
    }

    if (buttonType == 'sync' && nowRoleName == '分行审核员') {
        $('#syncDiv').hide();  //上报区块隐藏
    }

    if (buttonType == 'sync' && nowRoleName == '总行审核员') {
        $('#rejectBtn').show();  //上报区块隐藏
    }


    if(buttonType != 'update') {
        $('#addInfoFormBtn').hide();  //隐藏补录按钮
    } else {  //待补录隐藏提交按钮
        $('#acctChangeBtn').hide();
        $('#acctSuspendBtn').hide();
        $('#acctRevokeBtn').hide();

        //接口模式上报人行需要进行审核的情况下，上报系统、审核并上报隐藏，提交按钮展现。
        if (dblToCheck == '' || dblToCheck == 'autoSync') {
            $('#saveBtn').hide();
        } else {
            $('#sync1').hide();
            $('#isSyncAmsDiv').hide();
            $('#verifyFormBtn').hide();
        }
    }

    $('#rejectBtn').on('click', function () {
        $.get('reject.html', null, function (form) {
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
                },btn2: function(){
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
                        $.post("../../allAccountPublic/form/reject", {"id": billId ,"formId": billId,"denyReason" : data.field.denyReason ,"action":"rejectForm"}, function (data) {
                            layer.alert('驳回成功', {
                                title: "提示",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(index);
                                addCompanyAccountsTab(common, buttonType, billId,whiteList);

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

    var uri;
    if(buttonType === 'selectForChangeBtn') {
        hideInnerTabArray("tab_21", true, form);
        changeBtnStatus($('#acctType').val(), accountStatus);  //详情页进行变更操作页面展示
        selectDisabled = true;
        uri = "../../allAccountPublic/details";
        if (isPrint == true) {  //配置打印
            $('#printBtn').css('display', '');
        }

    } else if(buttonType === 'yanziToNormal') {
        uri = "../../allAccountPublic/details";
    } else if(buttonType === 'zengziToNormal') {
        uri = "../../allAccountPublic/details";
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

            whiteList = data.whiteList;
            if (whiteList == '1' && data.accountStatus == 'normal') {
                $('#acctWhiteListGoNormalBtn').css('display', 'inline')
            }
            //变更详情时需要比对字段信息弹出框
            if (operateType == 'ACCT_CHANGE') {
                $.get('../../validate/changeOpen/customerInfo?accountKey=' + data.accountKey
                    + '&regAreaCode=' + data.regAreaCode
                    + '&name=' + encodeURI(data.depositorName)
                    + '&type=' + data.acctType
                    + '&acctNo=' + data.acctNo
                    + '&regAddress=' + encodeURI(data.regAddress)
                    + '&legalName=' + encodeURI(data.legalName)
                    + '&fileNo=' + encodeURI(data.fileNo)
                    + '&regFullAddress=' + encodeURI(data.regFullAddress)
                ).done(function (response) {
                    addChangeCompareBox(laytpl, response, function (data) {
                    });
                });
            }
            initByExistData(data);
            //关联企业信息填充
            initRelateCompany(data);
            //股东信息渲染
            initStockholder(data);
            showImageAccountTabOnly(imageFlag,accountImageBillsId,billType == "" ? operateType : billType,false,data.acctType);

            //隐藏域赋值
            $('#id').val(data.id);
            $('#recId').val(recId);
            $('#customerNo').val(data.customerNo);
            if(data.imageBatchNo != null && data.imageBatchNo != ''){
                $('#imageBatchNo1').html('-->影像批次号：' + data.imageBatchNo);
                $('#imageBatchNo').val(data.imageBatchNo);
            }

            if(operateType == 'ACCT_CHANGE' || operateType == 'ACCT_SUSPEND' || operateType == 'ACCT_CLOSESUSPEND') {   //进入变更页面
                Blank();
                $('#billType').val(operateType)
                $('#saveBtn').show()
                if(isCheck == true) {
                    $('#syncDiv').hide()  //上报区块隐藏
                }            }

            if(buttonType == 'update') {
                $('#formId').valid();
            }

            /*if(buttonType == 'update' && billType == 'ACCT_CHANGE') {  //变更补录详情页
                changeAllSaicFieldStatus($('#acctType').val(), validator);
                changeAccountFieldStatus(validator)
                changeLegalFieldStatus(validator)
                changeContactFieldStatus(validator)
                removeValidate("parOrgCode",true,validator,"required");

            }*/

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

                //特殊变更-账户信息，账号和开户日期字段不可编辑
                $("#acctNo").attr("disabled",true);
                $("#acctNo").addClass("disableElement");
                $("#acctCreateDate").attr("disabled",true);
                $("#acctCreateDate").addClass("disableElement");

                //特殊变更-授权经办人，姓名、证件类型、证件号码和到期日字段不可编辑
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

            // 针对取消核准的数据在待补录页面显示  审核并上报
            if((data.cancelHeZhun == null || data.cancelHeZhun == false) && dblToCheck == 'cancelHeZhunSync' && pageCode == 'dblAccount'){
                $('#saveBtn').show();
                $('#verifyFormBtn').show();
            }

            if(pageCode == 'dblAccount') {  //待补录页面上报失败具体原因显示
                showSyncErrorMsg(billId);
            }

            //针对取消核准机构审核的接口模式判断
            if(dblToCheck == 'cancelHeZhunOrgan'){
                var cancelOrgan = false;
                $.get('../../organization/checkCancelOrganForPbcCode?pbcCode=' + data.bankCode, null, function (result) {
                    cancelOrgan = result;
                    if(!cancelOrgan) {
                        $('#saveBtn').hide();
                        $('#sync1').show();
                        $('#isSyncAmsDiv').show();
                        $('#isSyncEccsDiv').show();
                        $('#verifyFormBtn').show();
                    }
                });
            }
        });

    }

    //查看关联企业信息
    var initRelateCompany = function(data){
        var i;
        if (data.relateCompanys != undefined && data.relateCompanys != null) {

            for (i = 0; i < data.relateCompanys.length; i++) {

                var item = data.relateCompanys[i];

                $('[id="relateCompanyInfoSet[' + i + '].relateCompanyName"]').val(item.relateCompanyName);
                $('[id="relateCompanyInfoSet[' + i + '].companyCertificateType"]').val(item.companyCertificateType);
                $('[id="relateCompanyInfoSet[' + i + '].companyCertificateNo"]').val(item.companyCertificateNo);
                $('[id="relateCompanyInfoSet[' + i + '].companyCertificateName"]').val(item.companyCertificateName);
                $('[id="relateCompanyInfoSet[' + i + '].companyOrgCode"]').val(item.companyOrgCode);
                $('[id="relateCompanyInfoSet[' + i + '].companyLegalName"]').val(item.companyLegalName);
                $('[id="relateCompanyInfoSet[' + i + '].companyOrgEccsNo"]').val(item.companyOrgEccsNo);

                if (i <= data.relateCompanys.length) {
                    addRowsTpl('#relateCompany', partnerIds, partnerIdsMaxId, function (id) {
                        partnerIdsMaxId++;
                    });
                }

            }

        }

        $('#relateCompany_' + i).remove();
        //查看页面
        if (data.accountId != null) {
            //关联企业的显示更多删除按钮
            $("#btn_more_delete_relateCompany").hide();
            //禁止修改
            // $('select[name*=companyPartnerInfoSet]').prop("readonly",true);
            // $('input[type=text][name*=companyPartnerInfoSet]').prop("readonly",true);
            form.render();
        }
    }

    /**
     * 查看-股东信息回填
     */
    function initStockholder(data) {
        /**
         * 股东信息渲染
         */
        var i ;
        if(data.companyPartners != undefined && data.companyPartners!=null){

            for (i = 0;i < data.companyPartners.length;i++){

                var item = data.companyPartners[i];

                $('[id="companyPartnerInfoSet['+i+'].partnerType"]').val(item.partnerType);
                $('[id="companyPartnerInfoSet['+i+'].roleType"]').val(item.roleType);
                $('[id="companyPartnerInfoSet['+i+'].idcardType"]').val(item.idcardType);
                $('[id="companyPartnerInfoSet['+i+'].name"]').val(item.name);
                $('[id="companyPartnerInfoSet['+i+'].idcardNo"]').val(item.idcardNo);
                $('[id="companyPartnerInfoSet['+i+'].partnerTelephone"]').val(item.partnerTelephone);

                if (i <= data.companyPartners.length){
                    addRowsTpl('#stockHoder', partnerIds, partnerIdsMaxId, function (id) {
                        partnerIdsMaxId++;
                    });
                }

            }

        }

        $('#stockHoder_'+i).remove();
        //查看页面
        if (data.accountId!=null){
            //显示更多删除按钮
            $("#btn_more_delete").hide();
            //禁止修改
            // $('select[name*=companyPartnerInfoSet]').prop("readonly",true);
            // $('input[type=text][name*=companyPartnerInfoSet]').prop("readonly",true);
            form.render();
        }
    }


    /**
     * 根据现有数据回填表单
     * @param data
     */
    function initByExistData(data) {
        //显示打印按钮
        // $('#printBtn').removeClass('dn');

        if(data){
        	//fileTypeOfTeShu();
            setLabelValues(data,map);

            //如果账号为空设置可修改。
            if($("#acctNo").val()==""){
                $('#acctNo').prop("disabled",false);
            }

            //删除按钮
            if(billType == 'ACCT_OPEN' && (buttonType == "update" ||buttonType == "sync"|| (buttonType == "select" && data.status == "APPROVED")) && data.pbcCheckStatus && data.pbcCheckStatus !="CheckPass"  ){
                if($('#deleteBtn').length>0){
                    $('#deleteBtn').show();
                }
            }

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

            initAddress('reg',regAddressPicker,regAddressCode,regAddressType,thirdObject, selectDisabled);

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
                regAreaChange(data.value,operateType,billType,buttonType,workAddressPicker);
            });
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

        if(accoungImageShow){
            if(operateType){
                var temprecid =refBillId;
                if(temprecid == undefined || temprecid ==""){
                    temprecid = recId;
                }
                if(temprecid == undefined || temprecid ==""){
                    temprecid = billId;
                }
                runImageData(temprecid,billType == "" ? operateType : billType,buttonType == 'select');
            }else{
                runImageData(accountImageBillsId,billType == "" ? operateType : billType,buttonType == 'select');
            }
        }
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
        var url2 = '../../allAccountPublic/form/reject';
        var url3 = '../../allAccountPublic/form/verifyPass';//审核通过
        // if(data.openAccountLogId){
        //     url += '/'+data.openAccountLogId;
        // }
        //
        // if(!data.depositorName){
        //     layerTips.msg('请填写存款人名称');
        //     return;
        // }

        // data.acctType = 'teshu';
        // data.billType = 'ACCT_OPEN';
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
        } else if (submitOperateType == 'reject') {
            data.action = 'rejectForm'
            msg = "驳回"
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
                if(buttonType == 'update' || (isCheck == false && buttonType == 'saicValidate')) { //补录页面上报或者无审核的开户页面
                    if ($('#isSyncAms').prop("checked") == false) {
                        if(whiteList != "1" && syncSystem){
                            layer.alert("请勾选上报系统");
                            return;
                        }
                    }
                }
                // $('#verifyFormBtn').attr('disabled', 'disabled');
                if(post_flag) return; 
                post_flag = true;
                //开启遮罩
                var layerLoading = layer.msg('正在处理，请等待......', {icon: 16, shade: 0.3, time:0});
                $.post(url, data, function (data) {
                    layer.close(layerLoading);
                    if (data.submitResult == 'success') {
                        layer.alert(msg + '成功', {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            //addCompanyAccountsTab(common, buttonType, billId, whiteList);
                            $.get("../../config/findByKey?configKey=syncImageUse", null, function (res) {
                                if (res == true) {
                                    layer.confirm('影像是否上报人行？', {
                                        btn: ['是', '否'],
                                        cancel: function (index, layero) {  //取消操作，点击右上角的X
                                            addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                        }
                                    }, function () {
                                        sysnImage($('#id').val(),common, buttonType, billId, whiteList);
                                    }, function () {
                                        addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                    });
                                }else {
                                    addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                }
                            });

                        });
                    } else if (data.submitResult == 'fail') {
                        layer.alert(data.submitMsg, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId,whiteList);

                        });
                    } else {  //没勾选上报系统
                        layer.alert("提交成功", {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(index);
                            addCompanyAccountsTab(common, buttonType, billId,whiteList);

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
            } else if(submitOperateType == 'print') {   //打印按钮
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

                                    //待补录页面提交不展现是否打印提示
                                    if(pageCode != 'dblAccount'){
                                        layer.confirm('当前页面是否需要打印？', {
                                            btn: ['是', '否'],
                                            cancel: function (index, layero) {  //取消操作，点击右上角的X
                                                addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                            }
                                        }, function () {
                                            printfClick(common)

                                        }, function () {
                                            addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                        });
                                    }else{
                                        addCompanyAccountsTab(common, buttonType, billId, whiteList);
                                    }
                                } else {
                                    addCompanyAccountsTab(common, buttonType, billId,whiteList);
                                }
                            } else {
                                addCompanyAccountsTab(common, buttonType, billId,whiteList);
                            }

                        });

                    });
                }).error(function (err) {
                    $('#saveBtn').show();
                    layerTips.msg('处理submit失败['+err.responseJSON.message+']');
                });
            } else if (submitOperateType == 'reject') {
                url = url2;
                $('#rejectBtn').attr('disabled', 'disabled');
                $.post(url, data, function (data) {
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        addCompanyAccountsTab(common, buttonType, billId,whiteList);

                    });
                }).error(function (err) {
                    layerTips.msg('处理submit失败[' + err.responseJSON.message + ']');
                });

            }  else if (submitOperateType == 'verifyPass') {  //审核通过
                url = url3;
                $('#rejectBtn').attr('disabled', 'disabled');

                $.post(url, {"id": billId ,"formId": billId}, function (data) {
                    layer.alert(msg + '成功', {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        addCompanyAccountsTab(common, buttonType, billId,whiteList);

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
                        addCompanyAccountsTab(common, buttonType, billId,whiteList);

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
            //防止修改（删除）股东信息时，提交表单
            if ($('#submitBtnType').val()==""||$('#submitBtnType').val()==null) {
                return false;
            }
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
            regAreaChange(data.value,operateType,billType,buttonType,workAddressPicker);
        });
    }
    var thirdObjectValidate = new Object();
    thirdObjectValidate.validateObject = validator;
    thirdObjectValidate.regCallback = thirdObject.regCallback;
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

