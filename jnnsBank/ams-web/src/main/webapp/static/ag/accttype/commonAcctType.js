var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

var billId;
var depositorName;
var acctType;

function acctChangeClick(){
    billId = $('#id').val();
    depositorName = $('#depositorName').val();
    acctType = $('#acctType').val();
    checkSaicDetail(acctType,depositorName,billId);
    // jumpToOpenHtml('变更', 'ACCT_CHANGE');
}

function acctSuspendClick(){
    jumpToOpenHtml('久悬', 'ACCT_SUSPEND');
}

function acctRevokeClick(){
    jumpToOpenHtml('销户', 'ACCT_REVOKE');
}

function acctExtensionClick(){
    jumpToOpenHtml('展期', 'ACCT_EXTENSION');
}
function acctCloseSuspendClick(){
    jumpToOpenHtml('撤销久悬', 'ACCT_CLOSESUSPEND');
}
function resetSelectPwd() {
    var name = $('#depositorName').val();
    var accountKey = $('#accountKey').val();
    parent.tab.tabAdd({
        title: name+"-查询密码重置",
        href: 'selectpwd/reset.html?accountKey='+accountKey
    });
}
function toNomarlClick(){
    var name = $('#depositorName').val();
    $.get ("../../whitelist/findByName?name=" + name, null, function (data) {
        if(!data){
            //白名单移除后删除对应的流水信息
            var billId = $('#id').val();
            $.get ("../../allAccountPublic/deleteAccountsAll?id=" + billId, null, function (data) {
                if(data){
                    $("#acctWhiteListGoNormalBtn").hide();
                    jumpToOpenHtml('转正常', 'ACCT_OPEN');
                }else{
                    layerTips.msg("企业：" + name + "转正常失败。");
                }
            });
        }else{
            layerTips.msg(name + "是白名单企业，无法转正常操作。如需转正常，请先删除白名单中该企业信息!!!");
        }
    });
}

function jumpToChangeOpen(billId,depositorName,acctType,accountKey,regAreaCode){
    jumpToOpenHtml('变更', 'ACCT_CHANGE',billId,depositorName,acctType,accountKey,regAreaCode);
}

function checkSaicDetail(type,depositorName,billId){
    var href = ""
    var res;
    $.get('../../config/findByKey?configKey=heZhunAccountCheckSuspend', function (data) {
        res = data;
        //基本，临时，特殊通过系统配置是否增加变更久悬校验机制（沧州银行）
        if (type == "jiben" || type == "linshi" || type == "teshu") {
            if(res){
                href = "validate/detailPBOC.html?type=" + type + '&buttonType=ACCT_CHANGE&depositorName=' + depositorName + "&billId=" + billId + "&acctType=" + acctType;
            }else{
                href = "validate/detail.html?type=" + type + '&buttonType=ACCT_CHANGE&depositorName=' + depositorName + "&billId=" + billId + "&acctType=" + acctType;
            }
        } else {
            // href = "validate/detail.html?type=" + type + '&buttonType=ACCT_CHANGE&depositorName=' + depositorName + "&billId=" + billId + "&acctType=" + acctType;
            //基于基本户变更的账户在变更时需要进行校验基本户动作
            href = "validate/detailPBOC.html?type=" + type + '&buttonType=ACCT_CHANGE&depositorName=' + depositorName + "&billId=" + billId + "&acctType=" + acctType;
        }
        parent.tab.tabAdd({
                title: '校验管理',
                href: href
            }
        );
    });
    return false;
}


function jumpToOpenHtml(operateName, operateType,billId,name,type,accountKey,regAreaCode){
    var billId = billId;
    var name = name;
    var type = type;
    var accountKey = accountKey;
    var regAreaCode = regAreaCode;
    var acctNo = $('#acctNo').val();
    if(billId == undefined){
        billId = $('#id').val();
    }
    if(name == undefined){
        name = $('#depositorName').val();
    }
    if(type == undefined){
        type = $('#acctType').val();
    }
    var buttonType = 'editAcct';  //按钮操作类型
    var typeCode  = '';
    var tRecId ='';
    $.ajaxSettings.async = false;
    $.get('../../allAccountPublic/getBillId', function (data) {
        tRecId = data;
    });
    $.ajaxSettings.async = true;

    if(type === 'jiben'){
      typeCode = '基本存款账户';
        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/jibenOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId + "&acctType=" + type
            });
        }else{
            var depositorType = $("#depositorType").val() === undefined ? "" : $("#depositorType").val();
            parent.tab.tabAdd({
                title: '基本存款账户销户校验-' + name,
                href: 'validate/revoke_validate.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId+'&acctType='+type+'&name='+name+'&depositorType='+depositorType + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
        }

    } else if (type === 'yiban'){
        typeCode  = '一般存款帐户';

        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/yibanOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId+'&accountKey='+accountKey+'&regAreaCode='+regAreaCode
            });
        } else {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
            /*parent.tab.tabAdd({
                title: '一般存款帐户销户校验-' + name,
                href: 'validate/revoke_validate.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId+'&acctType='+type+'&name='+name
            });*/
        }

    } else if (type === 'yusuan'){
        typeCode  = '预算单位专用存款账户';

        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/yusuanOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
            });
        }else{
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
        }
    }  else if (type === 'feiyusuan'){
        typeCode  = '非预算单位专用存款账户';

        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName + typeCode + '-' + name,
                href: 'accttype/feiyusuanOpen.html?billId=' + billId + '&buttonType=' + buttonType + '&operateType=' + operateType+'&recId='+tRecId
            });
        } else {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
           /* parent.tab.tabAdd({
                title: '非预算单位专用存款账户销户校验-' + name,
                href: 'validate/revoke_validate.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId+'&acctType='+type+'&name='+name
            });*/
        }
    }  else if (type === 'linshi'){
        typeCode  = '临时机构临时存款账户';

        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/linshiOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
            });
        }else{
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
        }
    }  else if (type === 'feilinshi'){
        typeCode  = '非临时机构临时存款账户';
        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/feilinshiOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
            });
        }else {
            parent.tab.tabAdd({
                title: '非临时机构临时存款账户销户校验-' + name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
        }

    }  else if (type === 'teshu'){
        typeCode  = '特殊单位专用存款账户';

        if(operateType != 'ACCT_REVOKE') {
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'accttype/teshuOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
            });
        }else{
            parent.tab.tabAdd({
                title: operateName+typeCode+'-'+name,
                href: 'account/accountRevoke.html?billId='+billId+'&buttonType=selectForRevoke&recId='+tRecId + '&acctNo=' + acctNo+'&operateType=' + operateType
            });
        }
    }  else if (type === 'yanzi'){
        typeCode  = '验资户临时存款账户';

        parent.tab.tabAdd({
            title: operateName+typeCode+'-'+name,
            href: 'accttype/teshuOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
        });

    }  else if (type === 'zengzi'){
        typeCode  = '增资户临时存款账户';

        parent.tab.tabAdd({
            title: operateName+typeCode+'-'+name,
            href: 'accttype/teshuOpen.html?billId='+billId+'&buttonType='+buttonType+'&operateType=' + operateType+'&recId='+tRecId
        });
    }

}

function changeBillType(value) {
    if(value == 'ACCT_OPEN') {
        return '开户';
    } else if(value == 'ACCT_CHANGE') {
        return '变更';
    } else if(value == 'ACCT_SUSPEND') {
        return '久悬';
    } else if(value == 'ACCT_REVOKE') {
        return '销户';
    } else if(value == 'ACCT_INIT') {
        return '存量';
    }else if(value == 'ACCT_EXTENSION') {
        return '展期';
    }else if(value == 'ACCT_CLOSESUSPEND') {
        return '撤销久悬';
    }
}

function changeStatus(value) {
    if(value == 'WAITING_SUPPLEMENT') {
        return '待补录';
    } else if(value == 'NEW') {
        return '新建';
    } else if(value == 'APPROVING') {
        return '审核中';
    } else if(value == 'APPROVED') {
        return '审核通过';
    } else if(value == 'REJECT') {
        return '驳回';
    } else if(value == 'WAITING_CORE') {
        return '待核心开户';
    }
}

function changeAccountFieldStatus(validator) {
    removeValidate("basicAcctRegArea",true,validator,"required");
    removeValidate("bankName",true,validator,"required");
    removeValidate("bankCode",true,validator,"required");
    removeValidate("acctCreateDate",true,validator,"required");
    removeValidate("acctFileType",true,validator,"required");
    removeValidate("accountNameFrom",true,validator,"required");
    removeValidate("capitalProperty",true,validator,"required");
    removeValidate("effectiveDate",true,validator,"required");
    removeValidate("acctCreateReason",true,validator,"required");
    removeValidate("acctFileNo",true,validator,"required");
    removeValidate("fileDue",true,validator,"required");

}

function changeLegalFieldStatus(validator) {
    removeValidate("legalType",true,validator,"required");
    removeValidate("legalName",true,validator,"required");
    removeValidate("legalIdcardType",true,validator,"required");
    removeValidate("legalIdcardNo",true,validator,"required");
    removeValidate("legalIdcardDue",true,validator,"required");
}

function changeContactFieldStatus(validator) {
    removeValidate("telephone",true,validator,"required");
    removeValidate("zipcode",true,validator,"required");
    removeValidate("financeIdcardNo",true,validator,"required");
    removeValidate("financeTelephone",true,validator,"required");

}

function changeSaicFieldStatus(validator) {   //临时、特殊户部分字段隐藏
    removeValidate("depositorType",true,validator,"required");
    removeValidate("regAddress",true,validator,"required");
    removeValidate("regType",true,validator,"required");
    removeValidate("regNo",true,validator,"required");
    removeValidate("fileDue",true,validator,"required");

    $('#businessScopeEccsRow').remove();
}

//待补录详情页变更
function changeAllSaicFieldStatus(acctType, validator) {
    removeValidate("depositorType",true,validator,"required");
    removeValidate("regAddress",true,validator,"required");
    removeValidate("regFullAddress",true,validator,"required");
    removeValidate("regAreaCode",true,validator,"required");
    removeValidate("industryCode",true,validator,"required");
    removeValidate("regOffice",true,validator,"required");
    removeValidate("regType",true,validator,"required");
    removeValidate("regNo",true,validator,"required");
    removeValidate("fileType",true,validator,"required");
    removeValidate("fileNo",true,validator,"required");
    removeValidate("fileSetupDate",true,validator,"required");
    removeValidate("fileDue",true,validator,"required");
    removeValidate("isIdentification",true,validator,"required");
    removeValidate("regCurrencyType",true,validator,"required");

    if(acctType != 'teshu') {
        removeValidate("depositorName", true, validator, "required");
    }
    if(acctType != 'jiben') {
        $('#businessScopeEccsRow').remove();
    } else {
        removeValidate("businessScopeEccs",true,validator,"required");
    }

}

function changeOrgFieldStatus(validator) {
    removeValidate({label : "orgSelect",select :"linkSelectInput10001" },true,validator);
}

function changeOtherFeiJiBenFieldStatus(validator) {
    removeValidate({label : "economyIndustrySelect",select : "linkSelectInput10002" },true,validator,"required");

}

function changeOtherDivFieldStatus(validator) {
    removeValidate({label : "economyIndustrySelect",select : "linkSelectInput10002" },true,validator,"required");
    removeValidate("corpScale",true,validator,"required");

}

//变更、久悬、销户清空流水
function Blank() {

    $('#billNo').val('');
    $('#billDate').val('');
    // $('billType').val('');
    $('#status').val('');
    $('#approver').val('');
    $('#approveDate').val('');
    $('#approveDesc').val('');
    $('#pbcCheckStatus').val('');
    $('#pbcCheckDate').val('');
    //$('#customerNo').val('');
    $('#id').val('');
    $('#description').val('');
    $('#pbcSyncStatus').val('');
    $('#pbcSyncError').val('');
    $('#pbcOperator').val('');
    $('#pbcSyncTime').val('');
    $('#pbcSyncCheck').val('');
    $('#pbcSyncMethod').val('');
    $('#eccsSyncStatus').val('');
    $('#eccsSyncError').val('');
    $('#eccsOperator').val('');
    $('#eccsSyncTime').val('');
    $('#eccsSyncCheck').val('');
    $('#acctIsFromCore').val('');
    $('#coreDataCompleted').val('');
    $('#finalStatus').val('');
    $('#handingMark').val('');
    $('#initFullStatus').val('');
    $('#initRemark').val('');
    $('#fromSource').val('');
}

function formatAccountStatus(value) {
    if(value == 'normal') {
        return '正常';
    } else if(value == 'suspend') {
        return '久悬';
    } else if(value == 'revoke') {
        return '销户';
    } else if(value == 'unKnow') {
        return '未知';
    } else if(value == 'notExist') {
        return '不存在';
    } else if(value == 'notActive') {
        return '未激活';
    }

}

function formatString004(value) {
    if(value == '1') {
        return '已补录';
    } else {
        return '未补录';
    }
}
function formatYes(value) {
    if(value == 'Yes') {
        return '是';
    }else if(value=="No"){
        return '否';
    } else {
        return '未知';
    }
}
function formatProofType(value) {
    if(value == 'PBC') {
        return '人行校验';
    }else if(value=="SAIC"){
        return '工商校验';
    } else if(value=="PHONE"){
        return '手机号实名校验';
    }else if(value=="KYC"){
        return '客户尽调';
    }else if(value=="JUDICIAL_INFORMATION"){
        return '司法查询';
    }else {
        return '未知';
    }
}
function formatSyncStatus(value) {
    if(value == 'buTongBu') {
        return '无需上报';
    } else if(value == 'weiTongBu') {
        return '未同步';
    } else if(value == 'tongBuChengGong') {
        return '上报成功';
    } else if(value == 'tongBuZhong') {
        return '处理中';
    } else if(value == 'tuiHui') {
        return '退回';
    } else if(value == 'tongBuShiBai') {
        return '上报失败';
    }else {
        return '未同步';
    }

}
function formatOperateType(value) {
    if(value == 'keepForm') {
        return '保存';
    } else if(value == 'saveForm') {
        return '申请';
    } else if(value == 'verifyForm') {
        return '上报';
    } else if(value == 'addInfoForm') {
        return '补录';
    } else if(value == 'rejectForm') {
        return '驳回';
    } else if(value == 'verifyPass') {
        return '审核通过';
    }else if(value == 'verifyNotPass'){
        return '审核退回';
    }else if(value == 'syncForm') {
        return '审核并上报';
    }else {
        return value;
    }

}
/**
 * 本地异地标识
 */
function formatOpenAccountSiteType(value) {
    if(value === 'LOCAL') {
        return '本地';
    } else if(value === 'ALLOPATRIC') {
        return '异地';
    } else {
        return '未知';
    }

}

function formatAmsCheckStatus(value) {
    if(value == 'WaitCheck') {
        return '待审核';
    } else if(value == 'CheckPass') {
        return '审核通过';
    } else if(value == 'NoCheck') {
        return '无需审核';
    }

}

function formatSyncOperateType(value) {
    if(value == 'personSyncType') {
        return '手工上报';
    } else if(value == 'autoSyncType') {
        return '自动上报';
    } else if(value == 'halfSyncType') {
        return '手工补录';
    }

}

function cop(){
    $('#acctShortName').val($('#depositorName').val()).blur();
}

function changeBtnStatus (acctType, accountStatus) {
    if(accountStatus == 'normal') {
        // if(acctType == 'yiban' || acctType == 'feiyusuan') {  //去除备案类的判断  核准类久悬销户线下人行上报，系统内部销户流程开启
            $('#acctRevokeBtn').css('display', 'inline')
        // }
        $('#acctChangeBtn').css('display', 'inline')  //按钮显示
        $('#acctSuspendBtn').css('display', 'inline')
        if(acctType == 'yanzi' || acctType == 'zengzi'){
            /*$('#acctGoNormalBtn').css('display', 'inline');*/
            $('#acctChangeBtn').css('display', 'none');
            $('#acctSuspendBtn').css('display', 'none');
            $('#acctRevokeBtn').css('display', 'none');
        }
    } else if(accountStatus == 'suspend' ) {//&& (acctType == 'yiban' || acctType == 'feiyusuan') 去除备案类的判断  核准类久悬销户线下人行上报，系统内部销户流程开启
        $('#acctRevokeBtn').css('display', 'inline')
    }
    $('input,select,textarea').prop("disabled",true)
    $('#keepBtn').hide();
    $('#saveBtn').hide()
}

function getDepositorTypeName (depositorType) {
    var depositorTypeName;
    if(depositorType === '01') {
        depositorTypeName = '企业法人';
    } else if(depositorType === '02') {
        depositorTypeName = '非法人企业';
    } else if(depositorType === '03') {
        depositorTypeName = '机关';
    } else if(depositorType === '04') {
        depositorTypeName = '实行预算管理的事业单位';
    } else if(depositorType === '05') {
        depositorTypeName = '非预算管理的事业单位';
    } else if(depositorType === '06') {
        depositorTypeName = '团级(含)以上军队及分散执勤的支(分)队';
    } else if(depositorType === '07') {
        depositorTypeName = '团级(含)以上武警部队及分散执勤的支(分)队';
    } else if(depositorType === '08') {
        depositorTypeName = '社会团体';
    } else if(depositorType === '09') {
        depositorTypeName = '宗教组织';
    } else if(depositorType === '10') {
        depositorTypeName = '民办非企业组织';
    } else if(depositorType === '11') {
        depositorTypeName = '外地常设机构';
    } else if(depositorType === '12') {
        depositorTypeName = '外国驻华机构';
    } else if(depositorType === '13') {
        depositorTypeName = '有字号的个体工商户';
    } else if(depositorType === '14') {
        depositorTypeName = '无字号的个体工商户';
    } else if(depositorType === '15') {
        depositorTypeName = '居民委员会、村民委员会、社区委员会';
    } else if(depositorType === '16') {
        depositorTypeName = '单位设立的独立核算的附属机构';
    } else if(depositorType === '17') {
        depositorTypeName = '其他组织';
    } else if(depositorType === '20') {
        depositorTypeName = '境外机构';
    } else if(depositorType === '50') {
        depositorTypeName = 'QFII';
    } else if(depositorType === '51') {
        depositorTypeName = '境外贸易机构';
    } else if(depositorType === '52') {
        depositorTypeName = '跨境清算';
    }

    return depositorTypeName;
}
function getDepositorTypeValue (depositorType) {
    var depositorTypeName;
    if(depositorType === '企业法人') {
        depositorTypeName = '01';
    } else if(depositorType === '非法人企业') {
        depositorTypeName = '02';
    } else if(depositorType === '机关') {
        depositorTypeName = '03';
    } else if(depositorType === '实行预算管理的事业单位') {
        depositorTypeName = '04';
    } else if(depositorType === '非预算管理的事业单位') {
        depositorTypeName = '05';
    } else if(depositorType === '团级(含)以上军队及分散执勤的支(分)队') {
        depositorTypeName = '06';
    } else if(depositorType === '团级(含)以上武警部队及分散执勤的支(分)队') {
        depositorTypeName = '07';
    } else if(depositorType === '社会团体') {
        depositorTypeName = '08';
    } else if(depositorType === '宗教组织') {
        depositorTypeName = '09';
    } else if(depositorType === '民办非企业组织') {
        depositorTypeName = '10';
    } else if(depositorType === '外地常设机构') {
        depositorTypeName = '11';
    } else if(depositorType === '外国驻华机构') {
        depositorTypeName = '12';
    } else if(depositorType === '有字号的个体工商户') {
        depositorTypeName = '13';
    } else if(depositorType === '无字号的个体工商户') {
        depositorTypeName = '14';
    } else if(depositorType === '居民委员会、村民委员会、社区委员会') {
        depositorTypeName = '15';
    } else if(depositorType === '单位设立的独立核算的附属机构') {
        depositorTypeName = '16';
    } else if(depositorType === '其他组织') {
        depositorTypeName = '17';
    } else if(depositorType === '境外机构') {
        depositorTypeName = '20';
    } else if(depositorType === 'QFII') {
        depositorTypeName = '50';
    } else if(depositorType === '境外贸易机构') {
        depositorTypeName = '51';
    } else if(depositorType === '跨境清算') {
        depositorTypeName = '52';
    }

    return depositorTypeName;
}
function getFileTypeName (fileType) {
    var fileTypeName;
    if (fileType === '00') {
        fileTypeName = '社会统一信用代码';
    } else if (fileType === '01') {
        fileTypeName = '工商营业执照';
    } else if (fileType === '02') {
        fileTypeName = '批文';
    } else if (fileType === '03') {
        fileTypeName = '登记证书';
    } else if (fileType === '04') {
        fileTypeName = '开户证明';
    } else if (fileType === '05') {
        fileTypeName = '其他';
    } else if (fileType === '06') {
        fileTypeName = '借款合同';
    } else if (fileType === '07') {
        fileTypeName = '其他结算需要的证明';
    } else if (fileType === '09') {
        fileTypeName = '主管部门批文';
    } else if (fileType === '12') {
        fileTypeName = '证券从业资格证书';
    } else if (fileType === '17') {
        fileTypeName = '其他';
    }

    return fileTypeName;
}

function getFileType2Name (fileType2) {
    var fileType2Name;
    if (fileType2 === '02') {
        fileType2Name = '批文';
    } else if (fileType2 === '03') {
        fileType2Name = '登记证书';
    } else if (fileType2 === '04') {
        fileType2Name = '开户证明';
    } else if (fileType2 === '08') {
        fileType2Name = '财政部门批复书';
    } else if (fileType2 === '13') {
        fileType2Name = '国家外汇管理局的批文';
    } else if (fileType2 === '17') {
        fileType2Name = '其他';
    }

    return fileType2Name;
}

function getLegalTypeName (legalType) {
    var legalTypeName;
    if (legalType == '1') {
        legalTypeName = '法定代表人';
    } else if (legalType == '2') {
        legalTypeName = '单位负责人';
    }

    return legalTypeName;
}

function getParLegalTypeName(parLegalType) {
    var parLegalTypeName;
    if (parLegalType === '1') {
        parLegalTypeName = '法定代表人';
    } else if (parLegalType === '2') {
        parLegalTypeName = '单位负责人';
    }
    return parLegalTypeName;
}

function getIdCardTypeName(key) {
    var value;
    if (key === '1') {
        value = '身份证';
    } else if (key === '2') {
        value = '军官证';
    } else if (key === '3') {
        value = '文职干部证';
    } else if (key === '4') {
        value = '警官证';
    } else if (key === '5') {
        value = '士兵证';
    } else if (key === '6') {
        value = '护照';
    } else if (key === '7') {
        value = '港、澳、台居民通行证';
    } else if (key === '8') {
        value = '其它合法身份证件';
    } else if (key === '9') {
        value = '户口簿';
    }
    return value;
}

function getLegalIdcardTypeName (legalIdcardType) {
    return getIdCardTypeName(legalIdcardType);
}
function getParLegalIdcardTypeName (parLegalIdcardType) {
    return getIdCardTypeName(parLegalIdcardType);
}

function getIsIdentificationName (isIdentification) {
    var isIdentificationName;
    if (isIdentification == '0') {
        isIdentificationName = '否';
    } else if (isIdentification == '1') {
        isIdentificationName = '是';
    }

    return isIdentificationName;
}

function getRegCurrencyTypeName (regCurrencyType) {
    var regCurrencyTypeName;

    if (regCurrencyType == 'CNY') {
        regCurrencyTypeName = '人民币';
    } else if (regCurrencyType == 'USD') {
        regCurrencyTypeName = '美元';
    } else if (regCurrencyType == 'HKD') {
        regCurrencyTypeName = '港元';
    } else if (regCurrencyType == 'EUR') {
        regCurrencyTypeName = '欧元';
    } else if (regCurrencyType == 'KRW') {
        regCurrencyTypeName = '韩元';
    } else if (regCurrencyType == 'JPY') {
        regCurrencyTypeName = '日元';
    } else if (regCurrencyType == 'GBP') {
        regCurrencyTypeName = '英镑';
    } else if (regCurrencyType == 'SGD') {
        regCurrencyTypeName = '新加坡元';
    } else if (regCurrencyType == 'AUD') {
        regCurrencyTypeName = '澳大利亚元';
    } else if (regCurrencyType == 'CAD') {
        regCurrencyTypeName = '加拿大元';
    } else if (regCurrencyType == 'XEU') {
        regCurrencyTypeName = '其它货币折美元';
    }

    return regCurrencyTypeName;
}

function getIsSameRegistAreaName (isSameRegistArea) {
    var isSameRegistAreaName;
    if (isSameRegistArea === '0') {
        isSameRegistAreaName = '否';
    } else if (isSameRegistArea === '1') {
        isSameRegistAreaName = '是';
    }
    return isSameRegistAreaName;
}

function getCorpScaleName (corpScale) {
    var corpScaleName;
    if (corpScale === '2') {
        corpScaleName = '大型企业';
    } else if (corpScale === '3') {
        corpScaleName = '中型企业';
    } else if (corpScale === '4') {
        corpScaleName = '小型企业';
    } else if (corpScale === '5') {
        corpScaleName = '微型企业';
    } else if (corpScale === '9') {
        corpScaleName = '其他';
    }
    return corpScaleName;
}

function getRegOfficeName (regOffice) {
    if ("G" === regOffice) {
        return "工商部门";
    } else if ("R" === regOffice) {
        return "人民银行";
    } else if ("M" === regOffice) {
        return "民政部门";
    } else if ("B" === regOffice) {
        return "机构编制部门";
    } else if ("S" === regOffice) {
        return "司法行政部门";
    } else if ("W" === regOffice) {
        return "外交部门";
    } else if ("Z" === regOffice) {
        return "宗教部门";
    } else if ("Q" === regOffice) {
        return "其他";
    }
}

function getParRegTypeName (parRegType) {
    var parRegTypeName;
    if (parRegType === '01') {
        parRegTypeName = '工商注册号';
    } else if (parRegType === '02') {
        parRegTypeName = '机关和事业单位登记号';
    } else if (parRegType === '03') {
        parRegTypeName = '社会团体登记号';
    } else if (parRegType === '04') {
        parRegTypeName = '民办非企业单位登记号';
    } else if (parRegType === '05') {
        parRegTypeName = '基金会登记号';
    } else if (parRegType === '06') {
        parRegTypeName = '宗教活动场所登记号';
    } else if (parRegType === '07') {
        parRegTypeName = '统一社会信用代码';
    } else if (parRegType === '08') {
        parRegTypeName = '商事与非商事登记证号';
    } else if (parRegType === '99') {
        parRegTypeName = '其他';
    }
    return parRegTypeName;
}

/**
 * 工商注册类型
 */
function getRegTypeName (regType) {
    var regTypeName;
    if (regType === '01') {
        regTypeName = '工商注册号';
    } else if (regType === '02') {
        regTypeName = '机关和事业单位登记号';
    } else if (regType === '03') {
        regTypeName = '社会团体登记号';
    } else if (regType === '04') {
        regTypeName = '民办非企业单位登记号';
    } else if (regType === '05') {
        regTypeName = '基金会登记号';
    } else if (regType === '06') {
        regTypeName = '宗教活动场所登记号';
    } else if (regType === '07') {
        regTypeName = '统一社会信用代码';
    } else if (regType === '08') {
        regTypeName = '商事与非商事登记证号';
    } else if (regType === '99') {
        regTypeName = '其他';
    }
    return regTypeName;
}

/**
 * 经济类型格式化
 */
function getEconomyTypeName (economyType) {
    var economyTypeName;
    if (economyType === '10') {
        economyTypeName = '内资';
    } else if (economyType === '11') {
        economyTypeName = '国有全资';
    } else if (economyType === '12') {
        economyTypeName = '集体全资';
    } else if (economyType === '13') {
        economyTypeName = '股份合作';
    } else if (economyType === '14') {
        economyTypeName = '联营';
    } else if (economyType === '15') {
        economyTypeName = '有限责任公司';
    } else if (economyType === '16') {
        economyTypeName = '股份有限公司';
    } else if (economyType === '17') {
        economyTypeName = '私有';
    } else if (economyType === '19') {
        economyTypeName = '其它内资';
    } else if (economyType === '20') {
        economyTypeName = '港澳台投资';
    } else if (economyType === '21') {
        economyTypeName = '内地和港澳台投资';
    } else if (economyType === '22') {
        economyTypeName = '内地和港澳台合作';
    } else if (economyType === '23') {
        economyTypeName = '港澳台独资';
    } else if (economyType === '24') {
        economyTypeName = '港澳台投资股份有限公司';
    } else if (economyType === '29') {
        economyTypeName = '其他港澳台投资';
    } else if (economyType === '30') {
        economyTypeName = '国外投资';
    } else if (economyType === '31') {
        economyTypeName = '中外合资';
    } else if (economyType === '32') {
        economyTypeName = '中外合作';
    } else if (economyType === '33') {
        economyTypeName = '外资';
    } else if (economyType === '34') {
        economyTypeName = '国外投资股份有限公司';
    } else if (economyType === '39') {
        economyTypeName = '其他国外投资';
    } else if (economyType === '99') {
        economyTypeName = '其他';
    }
    return economyTypeName;
}

/**
 * 开户证明文件种类1
 */
function getAcctFileTypeName(acctFileType) {
    var acctFileTypeName;
    if (acctFileType === "06") {
        acctFileTypeName = "借款合同";
    } else if (acctFileType === "07") {
        acctFileTypeName = "其他结算需要的证明";
    } else if (acctFileType === "09") {
        acctFileTypeName = "主管部门批文";
    } else if (acctFileType === "10") {
        acctFileTypeName = "相关部门证明";
    } else if (acctFileType === "11") {
        acctFileTypeName = "政府部门文件";
    } else if (acctFileType === "14") {
        acctFileTypeName = "建筑主管部门核发的许可证";
    } else if (acctFileType === "15") {
        acctFileTypeName = "施工及安装合同";
    } else if (acctFileType === "16") {
        acctFileTypeName = "工商行政管理部门的证明";
    }
    return acctFileTypeName;
}

/**
 * 开户证明文件种类2
 */
function getAcctFileType2Name(acctFileType) {
    var acctFileTypeName;
    if (acctFileType === "08") {
        acctFileTypeName = "08-财政部门批复书";
    }
    return acctFileTypeName;
}

/**
 * 账户名称构成方式
 */
function getAccountNameFromName(accountNameFrom) {
    var accountNameFromName;
    if (accountNameFrom === "0") {
        accountNameFromName = "与存款人名称一致";
    } else if (accountNameFrom === "1") {
        accountNameFromName = "存款人名称加内设部门";
    } else if (accountNameFrom === "2") {
        accountNameFromName = "存款人名称加资金性质";
    }
    return accountNameFromName;
}

/**
 * 资金管理人身份证种类
 */
function getFundManagerIdcardTypeName(key) {
    return getIdCardTypeName(key);
}

/**
 * 资金性质
 */
function getCapitalPropertyName(key) {
    var value;
    if (key === "01") {
        value = "01-基本建设资金";
    } else if (key === "02") {
        value = "02-更新改造资金";
    } else if (key === "03") {
        value = "03-财政预算外资金";
    } else if (key === "04") {
        value = "04-粮、棉、油收购资金";
    } else if (key === "05") {
        value = "05-证券交易结算资金";
    } else if (key === "06") {
        value = "06-期货交易保证金";
    } else if (key === "07") {
        value = "07-金融机构存放同业资金";
    } else if (key === "08") {
        value = "08-政策性房地产开发资金";
    } else if (key === "09") {
        value = "09-单位银行卡备用金";
    } else if (key === "10") {
        value = "10-住房基金";
    } else if (key === "11") {
        value = "11-社会保障基金";
    } else if (key === "12") {
        value = "12-收入汇缴资金";
    } else if (key === "13") {
        value = "13-业务支出资金";
    } else if (key === "14") {
        value = "14-单位内部的党、团、工会经费";
    } else if (key === "16") {
        value = "16-其他需要专项管理和使用的资金";
    }
    return value;
}

/**
 * 组织机构类别
 */
function getOrgTypeName(key) {
    var value = "";
    if (key === "1") {
        value = "企业";
    } else if (key === "2") {
        value = "事业单位";
    } else if (key === "3") {
        value = "机关";
    } else if (key === "4") {
        value = "社会团体";
    } else if (key === "7") {
        value = "个体工商户";
    } else if (key === "9") {
        value = "其他";
    }
    return value;
}

/**
 * 组织机构类别
 */
function getAcctTypeName(key) {
    var value;
    if (key === "jiben") {
        value = "基本存款账户";
    } else if (key === "yiban") {
        value = "一般存款账户";
    } else if (key === "yusuan") {
        value = "预算单位专用存款账户";
    } else if (key === "feiyusuan") {
        value = "非预算单位专用存款账户";
    } else if (key === "teshu") {
        value = "特殊单位专用存款账户";
    } else if (key === "linshi") {
        value = "临时机构临时存款账户";
    } else if (key === "feilinshi") {
        value = "非临时机构临时存款账户";
    }
    return value;
}

/**
 * 单据类型
 */
function getBillTypeName(key) {
    var value;
    if (key === "ALL") {
        value = "所有";
    } else if (key === "ACCT_INIT") {
        value = "存量";
    } else if (key === "ACCT_OPEN") {
        value = "新开户";
    } else if (key === "ACCT_CHANGE") {
        value = "变更";
    } else if (key === "ACCT_SUSPEND") {
        value = "久悬";
    } else if (key === "ACCT_SEARCH") {
        value = "查询";
    } else if (key === "ACCT_REVOKE") {
        value = "销户";
    } else if (key === "ACCT_SAIC") {
        value = "工商信息";
    } else if (key === "APPT_UNCOMPLETE") {
        value = "接洽打印";
    } else if (key === "APPT_AFTERCOMPLETE") {
        value = "已受理详情";
    }
    return value;
}

/**
 * 组织机构类别细分
 */
function getOrgTypeDetailName(key) {
    var value = "";
    if (key === "10") {
        value = "企业法人";
    } else if (key === "11") {
        value = "其他企业";
    } else if (key === "12") {
        value = "农民专业合作社";
    } else if (key === "13") {
        value = "个人独资、合伙企业";
    } else if (key === "14") {
        value = "企业的分支机构";
    } else if (key === "20") {
        value = "事业法人";
    } else if (key === "21") {
        value = "未登记的事业单位";
    } else if (key === "24") {
        value = "事业单位的分支机构";
    } else if (key === "30") {
        value = "机关法人";
    } else if (key === "31") {
        value = "机关的内设机构";
    } else if (key === "32") {
        value = "机关的下设机构";
    } else if (key === "40") {
        value = "社会团体法人";
    } else if (key === "41") {
        value = "社会团体分支机构";
    } else if (key === "70") {
        value = "个体工商户";
    } else if (key === "51") {
        value = "民办非企业";
    } else if (key === "52") {
        value = "基金会";
    } else if (key === "53") {
        value = "居委会";
    } else if (key === "54") {
        value = "村委会";
    } else if (key === "60") {
        value = "律师事务所、司法鉴定所";
    } else if (key === "61") {
        value = "宗教活动场所";
    } else if (key === "62") {
        value = "境外在境内成立的组织机构";
    } else if (key === "99") {
        value = "其他"
    }
    return value;
}

/**
 * 内设部门负责人身份种类
 */
function getInsideLeadIdcardTypeName(key) {
    return getIdCardTypeName(key);
}

/**
 * 开户原因
 */
function getAcctCreateReasonName(key) {
    var value;
    if (key === "1") {
        value = "建筑施工及安装";
    } else if (key === "2") {
        value = "从事临时经营活动";
    }
    return value;
}

/*function ValidateBusinessScope(data){
    if(data.value.trim().length >= 500){
        layer.alert("上报人行账管系统的经营范围长度不能超过500个字节！", {
            title: "提示",
            closeBtn: 0
        });

        return;
    }

}

function ValidateBusinessScopeEccs(data){
    if(data.value.trim().length >= 200){
        layer.alert("上报信用代码证系统的经营范围长度不能超过200个字节！", {
            title: "提示",
            closeBtn: 0
        });
        return false;
    }
}*/

// function layerTips(element,msg){
// 	layer.tips(msg, '#'+element+'ChangedMsg',{tips: [3, '#0FA6D8'],area: ['auto', 'auto'],time:2000});
// }

function addChangedMessage(element,msg){

    var elem = $('#' + element);
    if(elem.length<1) {
        elem = $('[id="' + element + '"]');
    }
    elem.parent().addClass('highlight');
    showChangeTab();

	elem.parent().after("<div id=\""+element+"ChangedMsg\" class=\"col-md-1\" " +
            //"onmouseover=\"layerTips('"+element+"','"+msg+"')\">" +
            "data-msg=\"" + msg + "\">" +
            "<i class=\"fa fa-question-circle-o\" aria-hidden=\"true\" style=\"color:red;\"></i></div>");
         
    var tips;

    var elemChangedMsg = $('#' + element + 'ChangedMsg');
    if(elemChangedMsg.length < 1) {
        elemChangedMsg = $('[id="' + element + 'ChangedMsg"]');
    }
    elemChangedMsg.hover(function(){
        var elem = $(this);
        var msg = elem.attr('data-msg')

        if($('#' + element + 'ChangedMsg').length < 1) {
            tips = layer.tips(msg, '[id="' + element + 'ChangedMsg"]',{tips: [3, '#0FA6D8']});
        } else {
            tips = layer.tips(msg, '#'+element+'ChangedMsg',{tips: [3, '#0FA6D8']});
        }
    },function(){
        layer.close(tips);
    });
}

function removeChangedMessage(element){
    var elem = $('#' + element);
    if(elem.length<1) {
        elem = $('[id="' + element + '"]');
    }
    elem.parent().removeClass('highlight');
    showChangeTab();
    elem.parent().next().remove();
}

function addChangedMessageForAddress(element,msg){
    $("#"+element).parent().addClass('highlight');
    showChangeTab();
	$("#"+element).parent().after("<div id=\""+element+"ChangedMsg\" class=\"addressTips layui-input-inline layui-form\" " +
            //"onmouseover=\"layerTips('"+element+"','"+msg+"')\">" +
            "data-msg=\"" + msg + "\">" +
            "<i class=\"fa fa-question-circle-o\" aria-hidden=\"true\" style=\"color:red;\"></i></div>");

    var tips;
    $('#' + element + 'ChangedMsg').hover(function(){
        var elem = $(this);
        var msg = elem.attr('data-msg')
        tips = layer.tips(msg, '#'+element+'ChangedMsg',{tips: [3, '#0FA6D8']});
    },function(){
        layer.close(tips);
    });
}

function removeChangedMessageForAddress(element){
	if($("#"+element).parent().next().html() != undefined && $("#"+element).parent().next().attr("class").indexOf("addressTips") >=0){
        $("#"+element).parent().removeClass('highlight');
        showChangeTab();
        $("#"+element).parent().next().remove();
	}
}

function showChangeTab() {
    $('.tab-pane').each(function(index,element){
		if($(this).find('.highlight').length>0){
			$('a[href="#'+element.id+'"]').addClass('change-tab');
		}else{
			$('a[href="#'+element.id+'"]').removeClass('change-tab');
		}
	});
}

function showChangedMessage(element,msg){
    hideChangedMessage(element);

    var elem = $('#' + element);
    if(elem.length<1) {
        elem = $('[id="' + element + '"]');
    }

	var className = elem.parent().attr("class");
	if(className != undefined){
		if(className.indexOf("col-md-8") >=0){
			elem.parent().removeClass(className);
			elem.parent().addClass("col-md-7");
		}else if(className.indexOf("col-md-10") >=0){
			elem.parent().removeClass(className);
			elem.parent().addClass("col-md-9");
        }
        addChangedMessage(element,msg);
    }
}

function hideChangedMessage(element){
    var elem = $('#' + element);
    if(elem.length<1) {
        elem = $('[id="' + element + '"]');
    }
	var className = elem.parent().attr("class");
	if(className != undefined){
		if(className.indexOf("col-md-7") >=0){
			elem.parent().removeClass(className);
			elem.parent().addClass("col-md-8");
		}else if(className.indexOf("col-md-9") >=0){
			elem.parent().removeClass(className);
			elem.parent().addClass("col-md-10");
        }
        removeChangedMessage(element);
    }
}

function ignoreInput(key){
	if(key =="regCountry" ||key =="regProvinceChname" ||key =="regCityChname" ||key =="regAreaChname"||key =="workProvinceChname" ||key =="workCityChname" ||key =="workAreaChname"
		||key =="regProvince" ||key =="regCity" ||key =="regArea"||key =="workProvince" ||key =="workCity" ||key =="workArea"){
		return true;
	}else{
		return false;
	}
}

// 从后台变更记录表中获取记录并展现
function setChangedIteam(data,map){
	for (var key in data) {
		if(key != 'acctType' && key != 'id'){
			var value = data[key];
			if(value =="" || value =="null"|| value ==undefined){
				value ="空";
			}

            var elem = $('#' + key);
            if(elem.length<1) {
                elem = $('[id="' + key + '"]');
            }

			if(elem.length >0 && !ignoreInput(key)){
				if(elem[0].tagName=="INPUT" || elem[0].tagName=="TEXTAREA"){
					showChangedMessage(key,"原始值: "+value);
				}else if(elem[0].tagName=="SELECT"){
				    if($('#' + key).length < 1){
                        showChangedMessage(key,"原始值: "+(value=="空" ? value : $('[id="' + key + '"] option[value="'+value+'"]').text()));
                    }else{
                        showChangedMessage(key,"原始值: "+(value=="空" ? value : $("#"+key+" option[value='"+value+"']").text()));
                    }
                    // showChangedMessage(key,"原始值: "+value);
				}
			}
			//删除此处  是因为当第一次变更的字段为空时  会从变更记录表中获取value  导致data[key] 一直是“” 在下次提交相同变更字段变更的时候原始值一直为空
			// if(map.containsKey(key)){
			// 	map.remove(key);
			// 	map.put(key,data[key]);
			// }
		}
    }
}

function setLabelValues(data,map) {

    for (var key in data) {
        if(key != 'acctType' && key != 'id') {
        	if(map != undefined){
        		map.put(key, data[key]);
        		if(key == 'companyPartners'){
        		    var com = data[key];
        		    if(com != null && com.length > 0){
                        for (i = 0 ; i < data[key].length; i ++){
                            var item = data[key][i];
                            map.put('companyPartnerInfoSet['+i+'].name',item.name);
                            map.put('companyPartnerInfoSet['+i+'].idcardNo',item.idcardNo);
                            map.put('companyPartnerInfoSet['+i+'].idcardType',item.idcardType);
                            map.put('companyPartnerInfoSet['+i+'].partnerTelephone',item.partnerTelephone);
                            map.put('companyPartnerInfoSet['+i+'].partnerType',item.partnerType);
                            map.put('companyPartnerInfoSet['+i+'].roleType',item.roleType);
                        }
                    }
                }

        	}
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
    }
    console.log(map)
    if (map) {
        var child=$("#customerNo");
        if(child.length>0 && child.val() =="" ){
            $(child).remove();
        }
    }

    // 企业名称
    $("#name").html(data.depositorName);
    $("#statusSpan").html("对公" + changeBillType($('#billType').val()));
    if(data.createdDate) {
        $('#billDateDiv').css('display', 'block')

        var regu =/^\+?[1-9][0-9]*$/;
        var re = new RegExp(regu);
        if(re.test(data.createdDate) == true) {
            $("#billDateSpan").html(format(data.createdDate));
        } else {
            $("#billDateSpan").html(data.createdDate.replace(".0", ""));
        }
    }
    if(data.billNo) {
        $('#billNoDiv').css('display', 'block')
        $("#billNoSpan").html(data.billNo);
    }
    var operateType = GetUrlParam("operateType");
    if(operateType!=""){
        $("#statusSpan").html("对公" + changeBillType(operateType));
        $("#billNoDiv").hide();
        $("#billDateSpan").html(new Date().Format("yyyy-MM-dd hh:mm:ss"));
    }
    if ($('#billType').val()=="ACCT_CHANGE" && operateType != 'ACCT_REVOKE'){
        if(operateType!="ACCT_SUSPEND"){//过滤发起久悬时(已完成一次变更操作的账户)，变更记录按钮的显示。
            $("#statusDiv").append('<button class="layui-btn layui-btn-small" style="margin-left:30px;" onclick="showChangeItems('+$("#refBillId").val()+')">变更记录</button>');
        }
    }
}

//待补录上报失败原因显示
function showSyncErrorMsg(billId) {
    $.get('../../allBillsPublic/getSyncErrorMsg?refBillId=' + billId, function(data) {
        if(data.pbcFailMsg) {
            $('#syncPbcErrorDiv').css('display', '');
            $('#syncPbcErrorSpan').html(data.pbcFailMsg);
        }
        if(data.eccsFailMsg) {
            $('#syncEccsErrorDiv').css('display', '');
            $('#syncEccsErrorSpan').html(data.eccsFailMsg);
        }
        if(data.IMSFailMsg) {
            $('#syncImageErrorDiv').css('display', '');
            $('#syncImageErrorSpan').html(data.IMSFailMsg);
        }
    });

}


//获取变更内容
function showChangeItems(billId) {
    if (billId==null){
        layerTips("billId为空，无法查询变更记录");
    }
    $.ajax({
        type: "GET",
        url: "../../allAccountPublic/getChangeItems",
        data: "billId="+billId,
        success: function (res) {
            var data = res;
            var html = '<div style="margin: 10px;">' +
                '   <div class="layui-form-item">' +
                '       <label class="layui-form-label">账号</label>' +
                '       <div class="layui-input-block">' +
                '          <label class="layui-form-label">' + $("#acctNo").val() + '</label>' +
                '       </div>' +
                '   </div>' +
                '   <div class="layui-form-item">' +
                '       <label class="layui-form-label">账户名称</label>' +
                '       <div class="layui-input-block">' +
                '          <label class="layui-form-label">' + $("#acctName").val() + '</label>' +
                '       </div>' +
                '   </div>' +
                '</div>';

            html += '<table class="layui-table">\n' +
                '  <colgroup>\n' +
                '    <col width="150">\n' +
                '    <col width="200">\n' +
                '    <col>\n' +
                '  </colgroup>\n' +
                '  <thead>\n' +
                '    <tr>\n' +
                '      <th>字段名称</th>\n' +
                '      <th>变更前</th>\n' +
                '      <th>变更后</th>\n' +
                '    </tr> \n' +
                '  </thead>\n' +
                '  <tbody>\n';

            for (var i = 0; i < data.length; i++) {
                var name = data[i].name;
                var oldValue = (data[i].oldValue === undefined ? '' : data[i].oldValue);
                var newValue = (data[i].newValue === undefined ? '' : data[i].newValue);
                if (name == 'regCity' || name == 'regArea') {
                    getAreaNameByCode(oldValue, function (areaNames) {
                        oldValue = areaNames;
                    });
                    getAreaNameByCode(newValue, function (areaNames) {
                        newValue = areaNames;
                    });
                } else {
                    try {
                        //拼接数据格式化方法名
                        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length);
                        var functionName = 'get' + name + 'Name';
                        if (typeof eval(functionName) === "function") {//判断是否存在该方法
                            //调用方法并赋值
                            oldValue = window[functionName](oldValue);
                            newValue = window[functionName](newValue);
                        }
                    } catch (e) {
                    }
                }

                html += '    <tr>\n' +
                    '      <td>' + (data[i].cname === undefined ? '' : data[i].cname) + '</td>\n' +
                    '      <td>' + (oldValue === undefined ? '' : oldValue) + '</td>\n' +
                    '      <td>' + (newValue === undefined ? '' : newValue) + '</td>\n' +
                    '    </tr>\n';
            }
            if (data.length==0){
                html += "<tr><td colspan='3' align='center'>无变更记录</td></tr>"
            }
            html += '  </tbody>\n' +
                '</table>';

            layer.open({
                type: 1,
                title: '变更记录',
                content: html,
                shade: false,
                //offset: ['20px', '20%'],
                area: ['600px', '400px'],
                maxmin: true,
                success: function (layero, index) {
                },
                end: function () {
                }
            });
        },
        error: function () {
            layerTips.msg("请求error");
        }
    });
}
//获取URL中的参数
function GetUrlParam(paraName) {
    var url = document.location.toString();
    var arrObj = url.split("?");

    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;

        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");

            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}

//动态增加校验规则
function addValidate(elementId,obj,labelFlag,validator){
	var element = "";
	var elementLabel = "";
	if(typeof elementId ==="string"){
		element = elementId;
		elementLabel = elementId;
	}else if(typeof elementId === "object"){
		element = elementId.select;
		elementLabel = elementId.label;
	}else{
		return ;
    }

    if($('#' + element).length == 0) {
        return;
    }

	 $("#"+element).rules("add",obj);
	 if(labelFlag){
		 addValidateFlag([elementLabel]);
	 }
	 if(validator !=undefined){
		 validator.element("#"+element);
	 }
}


//动态删除校验规则
function removeValidate(elementId,labelFlag,validator,subRules){
	var element = "";
	var elementLabel = "";
	if(typeof elementId ==="string"){
		element = elementId;
		elementLabel = elementId;
	}else if(typeof elementId === "object"){
		element = elementId.select;
		elementLabel = elementId.label;
	}else{
		return;
	}

    if($('#' + element).length == 0) {
        return;
    }

	 if(subRules != undefined){
		 $("#"+element).rules("remove",subRules);
	 }else{
		 $("#"+element).rules("remove");
	 }
	 if(labelFlag){
		 removeValidateFlag([elementLabel]);
	 }
	 if(validator !=undefined){
		 validator.element("#"+element);
	 }
}

//SELECT 设置值并且刷新
function selectedSetValue(form,element,value){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		$("#" + element).val(value);
	    form.render('select',parentFilter);
	}
}


//SELECT disabled option框
function selectedDisabledOption(form,element,optionValue){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivOptionFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		var optionValues = optionValue.split(",");
		for(j in optionValues) {
			if($("#" + element).val() == optionValues[j]){
				$("#" + element).val("");
			}
			$("#" + element+" option[value='"+optionValues[j]+"']").attr("disabled","disabled");
		}
	    form.render('select',parentFilter);
	}
}


//SELECT enable option框
function selectedEnabledOption(form,element,optionValue){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivOptionFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		var optionValues = optionValue.split(",");
		for(j in optionValues) {
			$("#" + element+" option[value='"+optionValues[j]+"']").removeAttr("disabled");
		}
	    form.render('select',parentFilter);
	}
};

//SELECT enable All option框
function selectedEnabledAllOption(form,element){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivOptionFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		$("#" + element+" option").removeAttr("disabled");
	    form.render('select',parentFilter);
	}
};
//增加隐藏字段
function removeElement(elementId){
    $('#' + elementId).parent().parent().parent().remove();
}

//联系信息中‘与注册地是否’为'1-是'时同步注册地址
function isSameRegistAreaChange(form,operateType,billType,buttonType,workAddressPicker,thirdObjectJustValidate) {
    $('#isSameRegistArea').attr('lay-filter','isSameRegistArea');
    form.on('select(isSameRegistArea)',function (data) {
        $('#regAddress').change();
        var regArea = $('#regArea').val();
        var isSameRegistArea = data.value;
        if(isSameRegistArea == '1') {
            if(thirdObjectJustValidate && (operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN'  ||buttonType == 'saicValidate')){
                initAddress('work',workAddressPicker,regArea,3,thirdObjectJustValidate);
            }else{
                initAddress('work',workAddressPicker,regArea,3);
            }
        }
    });

    $('#regAddress').on('change',function(){
        var isSameRegistArea = $('#isSameRegistArea').val();
        if(isSameRegistArea == '1') $('#workAddress').val($.trim($(this).val())).blur();
    });
}

//客户信息中‘存款人类别’为'16-单位设立的独立核算的附属机构'时，
//上级机构信息中的“机构名称”“基本户开户许可核准号”“组织机构代码”“法人类型”“法人姓名”“法人证件类型”“法人证件号码”为必填
function depositorTypeChange(value) {
    if (value === "16") {
        addValidate("parCorpName", {required: true}, true, validator);
        addValidate("parAccountKey", {required: true}, true, validator);
        addValidate("parOrgCode", {required: true}, true, validator);
        addValidate("parLegalType", {required: true}, true, validator);
        addValidate("parLegalName", {required: true}, true, validator);
        addValidate("parLegalIdcardType", {required: true}, true, validator);
        addValidate("parLegalIdcardNo", {required: true}, true, validator);
    } else {
        removeValidate("parCorpName", true, validator, "required");
        removeValidate("parAccountKey", true, validator, "required");
        removeValidate("parOrgCode", true, validator, "required");
        removeValidate("parLegalType", true, validator, "required");
        removeValidate("parLegalName", true, validator, "required");
        removeValidate("parLegalIdcardType", true, validator, "required");
        removeValidate("parLegalIdcardNo", true, validator, "required");
    }
}

function regAreaChange(value,operateType,billType,buttonType,workAddressPicker,thirdObjectJustValidate) {
    var isSameRegistArea = $('#isSameRegistArea').val();
    if(isSameRegistArea == '1') {
        var regArea = value;
        if(operateType == 'ACCT_OPEN' || billType == 'ACCT_OPEN'  ||buttonType == 'saicValidate'){
            initAddress('work',workAddressPicker,regArea,3,thirdObjectJustValidate);
        }else{
            initAddress('work',workAddressPicker,regArea,3);
        }
    }
}

//上级组织机构代码不为空时,机构名称必录
//上级组织机构代码不为空时,基本户开户许可核准号必录
//上级组织机构代码不为空时,法人姓名必录
function parOrgCodeChange(validator){
    if(validator != undefined){
        $("#parOrgCode").attr("lay-filter","parOrgCode");

        $("#parOrgCode").on("change",function(){
            if($.trim($(this).val()) != ""){

                addValidate("parCorpName",{required : true},true,validator);
                addValidate("parAccountKey",{required : true},true,validator);
                addValidate("parLegalName",{required : true},true,validator);
            }else{
                removeValidate("parCorpName",true,validator,"required");
                removeValidate("parAccountKey",true,validator,"required");
                removeValidate("parLegalName",true,validator,"required");
            }
        });
    }
}
function  parAccountKeyChange(validator){
    if(validator != undefined){
        $(".parGgreg").on("change",function(){
            if($.trim($('.parGgreg').val()) != ""){

                addValidate("parCorpName",{required : true},true,validator);
                addValidate("parAccountKey",{required : true},true,validator);
                addValidate("parLegalName",{required : true},true,validator);
            }else{
                removeValidate("parCorpName",true,validator,"required");
                removeValidate("parAccountKey",true,validator,"required");
                removeValidate("parLegalName",true,validator,"required");
            }
        });
    }
}

//上级开户许可证不为空时机构名称和法人姓名联动必填
function  parAccountKeyValidChange(validator){
    if(validator != undefined){
        $("#parAccountKey").on("change",function(){
            if($.trim($('#parAccountKey').val()) != ""){
                addValidate("parCorpName",{required : true},true,validator);
                addValidate("parLegalName",{required : true},true,validator);
            }else{
                removeValidate("parCorpName",true,validator,"required");
                removeValidate("parLegalName",true,validator,"required");
            }
        });
    }
}

//初始化：
//上级组织机构代码不为空时,机构名称必录
//上级组织机构代码不为空时,基本户开户许可核准号必录
//上级组织机构代码不为空时,法人姓名必录
function parOrgCodeInit(validator){
    if(validator != undefined){
        if($.trim($("#parOrgCode").val()) != ""){
            addValidate("parCorpName",{required : true},true,validator);
            addValidate("parAccountKey",{required : true},true,validator);
            addValidate("parLegalName",{required : true},true,validator);
        }else{
            removeValidate("parCorpName",true,validator,"required");
            removeValidate("parAccountKey",true,validator,"required");
            removeValidate("parLegalName",true,validator,"required");
        }
    }
}

//工商注册类型时，经营范围不能为空
//function parsueeg(form,validator){
//	
//}



//上级法定代表人或负责人,姓名、身份证明文件种类及其编号数据项联动必填
function parLegalTypeChange(form,validator){
	 
	$("#parLegalType").attr("lay-filter","parLegalType");
	$("#parLegalIdcardType").attr("lay-filter","parLegalIdcardType");
	form.on('select(parLegalType)',function (data) {
		if($.trim(data.value) !="" || $.trim($("#parLegalName").val()) !=""|| $.trim($("#parLegalIdcardType").val()) !=""|| $.trim($("#parLegalIdcardNo").val()) !=""){
     	   addValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
		}else{
			removeValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
		}
	});
	form.on('select(parLegalIdcardType)',function (data) {
		
		if($.trim(data.value) !="" || $.trim($("#parLegalName").val()) !=""|| $.trim($("#parLegalType").val()) !=""|| $.trim($("#parLegalIdcardNo").val()) !=""){
     	   addValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
     	  
		}else{
			removeValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
				
		}
        if(validator != undefined){
            if(data.value=="1"){
                addValidate("parLegalIdcardNo",{isIdCardNo : "true"},false,validator);

            }else{
                removeValidate("parLegalIdcardNo",false,validator,"isIdCardNo");
            }
        }
	});
	$("#parLegalName").on("change",function(){
		if($.trim(this.value) !="" || $.trim($("#parLegalIdcardType").val()) !=""|| $.trim($("#parLegalType").val()) !=""|| $.trim($("#parLegalIdcardNo").val()) !=""){
      	   addValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
 		}else{
			removeValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
 		}
	});
	$("#parLegalIdcardNo").on("change",function(){
		if($.trim(this.value) !="" || $.trim($("#parLegalIdcardType").val()) !=""|| $.trim($("#parLegalType").val()) !=""|| $.trim($("#parLegalName").val()) !=""){
      	   addValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
 		}else{
			removeValidateFlag(["parLegalType","parLegalName","parLegalIdcardType","parLegalIdcardNo"]);
 		}
	});
}

//股东信息的关系人类型，当选1高管时：类型包括：实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人
//当选2股东：自然人、机构
function partnerTypeChange(form){
	$("#partnerType").attr("lay-filter","partnerType");
	form.on('select(partnerType)',function (data) {
		if(data.value=="1"){
			selectedEnabledAllOption(form,"roleType");
			selectedDisabledOption(form,"roleType","7,8");
		}else if(data.value =="2"){
			selectedEnabledAllOption(form,"roleType");
			selectedDisabledOption(form,"roleType","1,2,3,4,5,6");
		}else{
			selectedEnabledAllOption(form,"roleType");
		}
	});
}


//动态改变
//如“无需”输入框内有值，则将国地税输入框锁定，
//如“无需”输入框为空，则国地税至少有一项必填，
function noTaxProveChange(validator){
	$("#noTaxProve").on("change",function(){
		if($.trim($(this).val()) == ""){
            if(validator != undefined){
                addValidate("stateTaxRegNo",{manyChooseOne : ["#taxRegNo"]},true,validator);
                addValidate("taxRegNo",{manyChooseOne : ["#stateTaxRegNo"]},true,validator);
            }
		   $("#stateTaxRegNo").removeAttr("readOnly");
		   $("#taxRegNo").removeAttr("readOnly");
		}else{
            if(validator != undefined){
                removeValidate("stateTaxRegNo",true,validator,"manyChooseOne");
                removeValidate("taxRegNo",true,validator,"manyChooseOne");
            }
		   $("#stateTaxRegNo").attr("readOnly","readOnly");
		   $("#taxRegNo").attr("readOnly","readOnly");
		}
	});
}

//初始化
//如“无需”输入框内有值，则将国地税输入框锁定，
//如“无需”输入框为空，则国地税至少有一项必填，
function noTaxProveInit(validator){
	if($.trim($("#noTaxProve").val()) == ""){
	        if(validator != undefined){
                addValidate("stateTaxRegNo",{manyChooseOne : ["#taxRegNo"]},true,validator);
                addValidate("taxRegNo",{manyChooseOne : ["#stateTaxRegNo"]},true,validator);
            }
		   $("#stateTaxRegNo").removeAttr("readOnly");
		   $("#taxRegNo").removeAttr("readOnly");
 	}else{
            if(validator != undefined){
                removeValidate("stateTaxRegNo",true,validator,"manyChooseOne");
                removeValidate("taxRegNo",true,validator,"manyChooseOne");
            }
		   $("#stateTaxRegNo").attr("readOnly","readOnly");
		   $("#taxRegNo").attr("readOnly","readOnly");
 	}
}


//如证明文件类型为营业执照，默认联动证明文件1编号
//如证明文件类型为营业执照，默认联动证明文件1的到期日期
function fileTypeChange(form,validator,fn){
	$("#fileType").attr("lay-filter","fileType");
	form.on('select(fileType)',function (data) {
		if(data.value=="01"){
			$("#businessLicenseNo").val($("#fileNo").val());
			$("#businessLicenseDue").val($("#fileDue").val());
			
        }
        fn && fn(data);
	});
	$("#fileNo").on("change",function(){
		if($("#fileType").val() == "01"){
			$("#businessLicenseNo").val(this.value);
			
		}
	});
}

//证明文件2种类 和 文件编号2 联动必填
function fileType2Change(form,validator){
	if(validator != undefined) {
        $("#fileType2").attr("lay-filter","fileType2");
        form.on('select(fileType2)',function (data) {
            if($.trim(data.value) !="" || $.trim($("#fileNo2").val()) !=""){
                addValidateFlag(["fileType2","fileNo2"]);
            }else{
                removeValidateFlag(["fileType2","fileNo2"]);
            }
        });
        $("#fileNo2").on("change",function(){
            if($.trim(this.value) !="" || $.trim($("#fileType2").val()) !=""){
                addValidateFlag(["fileType2","fileNo2"]);
            }else{
                removeValidateFlag(["fileType2","fileNo2"]);
            }
        });
    }
}

//如证明文件类型为营业执照，默认联动证明文件1编号
//如证明文件类型为营业执照，默认联动证明文件1的到期日期(临时账户限制经营信息联动)
function lsFleTypeChange(form,validator){
	$("#fileType").attr("lay-filter","fileType");
	form.on('select(fileType)',function (data) {
		if(data.value=="01"){
			$("#businessLicenseNo").val($("#fileNo").val());
			$("#businessLicenseDue").val($("#fileDue").val());
			if(validator != undefined){
                addValidate("businessScope",{required: true},true,validator);
            }
		}else{
            if(validator != undefined){
                removeValidate("businessScope",true,validator,"required");
            }
		}
	});
	$("#fileNo").on("change",function(){
		if($("#fileType").val() == "01"){
			$("#businessLicenseNo").val(this.value);
			
		}
	});
}





//如注册资金未注明选择“是”，该字段为不可填，
//币种根据工商返显的单位进行自动判断，自动赋值，
function isIdentificationChange(form,validator,validateFlag){
	$("#isIdentification").attr("lay-filter","isIdentification");
	$("#regCurrencyType").attr("lay-filter","regCurrencyType");
	form.on('select(isIdentification)',function (data) {
		if(data.value=="1"){
//			if($.trim($("#regCurrencyType").val()) !=""){
			selectedSetValue(form,"regCurrencyType","");
			selectedDisabled(form,"regCurrencyType");
//			if($.trim($("#registeredCapital").val()) != ""){
        	$("#registeredCapital").val("");
        	$("#registeredCapital").attr("disabled","disabled");
			if(validateFlag && validator!=undefined){
	        	removeValidate("regCurrencyType",true,validator,"required");
	        	removeValidate("registeredCapital",true,validator,"required");
			}
//			$("#registeredCapital").attr("readOnly","readOnly");
//			}
		}else{
			selectedEnabled(form,"regCurrencyType");
	        
	        $("#registeredCapital").removeAttr("disabled");
			if(validateFlag && validator!=undefined){
				addValidate("regCurrencyType",{required: true},true,validator);
				addValidate("registeredCapital",{required: true},true,validator);
			}
		}
	});
}


//未标明注册资金默认为否，如返显时注册资金有值，则未标明注册资金为：否
function isIdentificationInit(validator,validateFlag,form){
	if($.trim($("#isIdentification").val())==""){
	    if($.trim($("#registeredCapital").val()) !=""){
            $("#isIdentification").val("0");
        }
	}
	if($("#isIdentification").val() =="1"){
		selectedSetValue(form,"regCurrencyType","");
		selectedDisabled(form,"regCurrencyType");
    	$("#registeredCapital").val("");
    	$("#registeredCapital").attr("disabled","disabled");
    	if(validateFlag && validator!=undefined){
        	removeValidate("regCurrencyType",true,validator,"required");
        	removeValidate("registeredCapital",true,validator,"required");
    	}
	}
}

//经营范围（人行账管系统）上限500，超过时最后一个字符是“等”，自动同步到经营范围（信用机构代码），最多200个字符，超过时最后一个字符为“等”
function businessScopeChange(eccsFlag,validator){
	$("#businessScope").on("change",function(){
		var scope = $(this).val();
		if(scope.length >500){
			$(this).val(scope.substr(0,499)+"等");
		}
		if(eccsFlag){
			if(scope.length <200){
				$("#businessScopeEccs").val(scope);
			}else{
				$("#businessScopeEccs").val(scope.substr(0,199)+"等");
			}
			if(validator != undefined){
				validator.element("#businessScopeEccs");
			}
		}
	});
	if(eccsFlag){
		$("#businessScopeEccs").on("change",function(){
			var scope = $(this).val();
			if(scope.length >200){
				$(this).val(scope.substr(0,199)+"等");
			}
		});
	}
}

//证明文件1种类为工商注册号时经营范围不能为空
//当存款人类别是 企业法人、非企业法人、有字号的个体工商户、无字号的个体工商户时，经营范围不能为空
function businessScopeValidChange(form,validator,strFileType,strDepositorType,eccsSyncEnabled){
    if(validator != undefined){
        if(!strFileType) strFileType = $("#fileType").val();
        if(!strDepositorType) strDepositorType = $("#depositorType").val();
        if(strFileType === '01' || strDepositorType === '01' || strDepositorType === '02' || strDepositorType === '13' ||strDepositorType === '14') {
            addValidate("businessScope",{required : true},true,validator);
            if(eccsSyncEnabled) {
                addValidate("businessScopeEccs", {required: true}, true, validator);
            }
        }else {
            removeValidate("businessScope",true,validator,"required");
            removeValidate("businessScopeEccs",true,validator,"required");
        }
    }
}

//如证件类型为身份证，证件号码则根据身份证规则进行输入校验
function legalIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#legalIdcardType").attr("lay-filter","legalIdcardType");
        form.on('select(legalIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("legalIdcardNo",{isIdCardNo : "true"},true,validator);
                addValidate("legalIdcardNo",{maxlength : 18},true,validator);
            }else{
                removeValidate("legalIdcardNo",false,validator,"isIdCardNo");
                addValidate("legalIdcardNo",{maxlength : 20},true,validator);
            }
        });
    }
}

//初始化：如证件类型为身份证，证件号码则根据身份证规则进行输入校验
function legalIdcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#legalIdcardType").val() == "1"){
            addValidate("legalIdcardNo",{isIdCardNo : "true"},true,validator);
            addValidate("legalIdcardNo",{maxlength : 18},true,validator);
        }
    }
}


//账户构成方式:
//1. 选择“存款人名称一致”时，下方显示“资金管理信息”区块，
//2. 选择“存款人名称加内设部门”时，下方显示内设部门信息
//3. 选择“存款人名称加资金性质”时，下方前缀、后缀、资金管理人信息
function accountNameFromChange(form,validator,syncEccs){
	$("#accountNameFrom").attr("lay-filter","accountNameFrom");
	form.on('select(accountNameFrom)',function (data) {
		showInnerTabArray("tab_1,tab_2",form);
		$("#saccprefix").parent().parent().hide();
		$("#saccprefix").attr("disabled","disabled");
		$("#saccpostfix").parent().parent().hide();
		$("#saccpostfix").attr("disabled","disabled");
		if(validator != undefined){
            isYuSuanPbcSyncChangeNotValid(validator);
        }
		if(data.value == ""){
            hideInnerTabArray("tab_1,tab_2",true,form);
            syncEccs == 'true' && $('#customerInfo').hide();
		}else if(data.value == "0"){
            hideInnerTabArray("tab_1",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
		}else if(data.value == "1"){
            hideInnerTabArray("tab_2",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_1"]').click();
            if(validator != undefined){
                isYuSuanPbcSyncChangeValid(validator);
            }
		}else if(data.value == "2"){
            hideInnerTabArray("tab_1",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
	    	$("#saccprefix").parent().parent().show();
	    	$("#saccprefix").removeAttr("disabled");
	    	$("#saccpostfix").parent().parent().show();
	    	$("#saccpostfix").removeAttr("disabled");
		}
	});
}

//账户构成方式:
//1. 选择“存款人名称一致”时，下方显示“资金管理信息”区块，
//2. 选择“存款人名称加内设部门”时，下方显示内设部门信息
//3. 选择“存款人名称加资金性质”时，下方前缀、后缀、资金管理人信息
function accountNameFromChangeForFYS(form,validator,syncEccs){
    $("#accountNameFrom").attr("lay-filter","accountNameFrom");
    form.on('select(accountNameFrom)',function (data) {
        showInnerTabArray("tab_1,tab_2",form);
        $("#saccprefix").parent().parent().hide();
        $("#saccprefix").attr("disabled","disabled");
        $("#saccpostfix").parent().parent().hide();
        $("#saccpostfix").attr("disabled","disabled");
        if(validator != undefined){
            isYuSuanPbcSyncChangeNotValid(validator);
        }
        if(data.value == ""){
            hideInnerTabArray("tab_1,tab_2",true,form);
            syncEccs == 'true' && $('#customerInfo').hide();
        }else if(data.value == "0"){
            hideInnerTabArray("tab_1",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
        }else if(data.value == "1"){
            hideInnerTabArray("tab_2",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_1"]').click();
            addValidate("insideDeptName",{required : true},true,validator);
            addValidate("insideLeadName",{required : true},true,validator);
            addValidate("insideLeadIdcardType",{required : true},true,validator);
            addValidate("insideLeadIdcardNo",{required : true},true,validator);
            addValidate("insideTelephone",{required : true},true,validator);
            addValidate("insideZipcode",{required : true},true,validator);
            addValidate("insideAddress",{required : true},true,validator);
        }else if(data.value == "2"){
            hideInnerTabArray("tab_1",true,form);
            syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
            $("#saccprefix").parent().parent().show();
            $("#saccprefix").removeAttr("disabled");
            $("#saccpostfix").parent().parent().show();
            $("#saccpostfix").removeAttr("disabled");
        }
    });
}


//资金性质选择04/10/11/13/14时：默认打钩，
//“资金性质”选择任何其他时：默认不打勾。
function capitalPropertyChange(form){
	$("#capitalProperty").attr("lay-filter","capitalProperty");
    form.on('select(capitalProperty)',function (data) {
        if(data.value =="04" || data.value=="10" || data.value=="11" || data.value=="13" || data.value=="14"){
            selectedSetValue(form,"enchashmentType",1);
        }else if(data.value != ""){
            selectedSetValue(form,"enchashmentType",0);
        }
    });
}

//资金性质选择04/10/11/13/14时：默认打钩，
//“资金性质”选择任何其他时：默认不打勾。
function feiyusuanCapitalPropertyChange(form){
    $("#capitalProperty").attr("lay-filter","capitalProperty");
    form.on('select(capitalProperty)',function (data) {
        if(data.value =="04" || data.value=="10" || data.value=="11" || data.value=="13" || data.value=="14"){
            selectedSetValue(form,"enchashmentType",1);
            if(data.value == "14"){
                $("#saccprefix").removeAttr("disabled");
            }else{
                $("#saccprefix").attr("disabled","disabled");
            }
        }else if(data.value != ""){
            $("#saccprefix").attr("disabled","disabled");
            selectedSetValue(form,"enchashmentType",0);
        }
    });
}


//账户构成方式:
//1. 选择“存款人名称一致”时，下方显示“资金管理信息”区块，
//2. 选择“存款人名称加内设部门”时，下方显示内设部门信息
//3. 选择“存款人名称加资金性质”时，下方前缀、后缀、资金管理人信息
function accountNameFromInit(form,validator,syncEccs){
	var accountNameFromValue = $("#accountNameFrom").val();
	$("#saccprefix").parent().parent().hide();
	$("#saccprefix").attr("disabled","disabled");
	$("#saccpostfix").parent().parent().hide();
	$("#saccpostfix").attr("disabled","disabled");
	if(accountNameFromValue == ""){
        hideInnerTabArray("tab_1,tab_2",true,form);
        syncEccs == 'true' && $('#customerInfo').show();
	}else if(accountNameFromValue == "0"){
        hideInnerTabArray("tab_1",true,form);
        syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
	}else if(accountNameFromValue == "1"){
        hideInnerTabArray("tab_2",true,form);
        syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_1"]').click();
        if(validator != undefined){
            isYuSuanPbcSyncChangeValid(validator);
        }
	}else if(accountNameFromValue == "2"){
        hideInnerTabArray("tab_1",true,form);
        syncEccs == 'true' && $('#customerInfo').show() && $('a[href="#tab_2"]').click();
		$("#saccprefix").parent().parent().show();
		$("#saccprefix").removeAttr("disabled");
		$("#saccpostfix").parent().parent().show();
		$("#saccpostfix").removeAttr("disabled");
	}
}


//资金性质选择04/10/11/13/14时：默认打钩，
//“资金性质”选择任何其他时：默认不打勾。
function capitalPropertyInit(){
	var capitalPropertyValue = $("#capitalProperty").val();
	if(capitalPropertyValue =="04" || capitalPropertyValue=="10" ||capitalPropertyValue=="11" || capitalPropertyValue=="13" || capitalPropertyValue=="14"){
		if($("#enchashmentType").val() ==""){
			$("#enchashmentType").val("1");
		}
	}else if(capitalPropertyValue!=""){
		if($("#enchashmentType").val() ==""){
			$("#enchashmentType").val("0");
		}
	}
}


//与注册地是否一致:默认为“是”
function isSameRegistAreaInit(){
	if($.trim($("#isSameRegistArea").val()) ==""){
		$("#isSameRegistArea").val("1");
	}
}


//绑定:开户证明文件种类1不为10时，必填属性与编号1同时有同时无
function acctFileTypeChange(form,validator,acctFileTypeFlag,valAttr){
	$("#acctFileType").attr("lay-filter","acctFileType");
	form.on('select(acctFileType)',function (data) {
		var flag = false;
		for( j in valAttr){
			if($("#acctFileType").val() == valAttr[j]){
				flag = true;
				break;
			}
		}
		if(validator != undefined){
            if(flag){
                addValidate("acctFileType",{shouldAllRequiredOrNot : ["#acctFileNo"]},true,validator);
                addValidate("acctFileNo",{shouldAllRequiredOrNot : ["#acctFileType"]},true,validator);
            }else{
                removeValidate("acctFileType",acctFileTypeFlag,validator,"shouldAllRequiredOrNot");
                removeValidate("acctFileNo",true,validator,"shouldAllRequiredOrNot");
            }
        }
	});
}

//初始化：开户证明文件种类1不为10时，必填属性与编号1同时有同时无
function acctFileTypeInit(validator,acctFileTypeFlag,valAttr){
	var flag = false;
	for( j in valAttr){
		if($("#acctFileType").val() == valAttr[j]){
			flag = true;
			break;
		}
	}
	if(validator != undefined){
        if(flag){
            addValidate("acctFileType",{shouldAllRequiredOrNot : ["#acctFileNo"]},true);
            addValidate("acctFileNo",{shouldAllRequiredOrNot : ["#acctFileType"]},true);
        }else{
            removeValidate("acctFileType",acctFileTypeFlag,undefined,"shouldAllRequiredOrNot");
            removeValidate("acctFileNo",true,undefined,"shouldAllRequiredOrNot");
        }
    }
}


//申请开户原因动态改变:
//1-建筑施工及安装，显示建筑施工及安装单位信息表单
//2-从事临时经营活动，隐藏建筑施工及安装单位信息表单
function acctCreateReasonChange(form,syncEccs){
	$("#acctCreateReason").attr("lay-filter","acctCreateReason");
	form.on('select(acctCreateReason)',function (data) {
		selectedEnabledAllOption(form,"acctFileType");
		if(data.value == "2"){
            hideInnerTabArray("tab_1",true,form);
            syncEccs == 'true';
            // $('#customerInfo').hide();
			selectedDisabledOption(form,"acctFileType","14,15");
		}else{
            showInnerTabArray("tab_1",form);
            syncEccs == 'true' &&  $('#customerInfo').show() && $('a[href="#tab_1"]').click();
			if(data.value == "1"){
				selectedDisabledOption(form,"acctFileType","16");
			}
		}
	});
}


//申请开户原因初始化:
//1-建筑施工及安装，显示建筑施工及安装单位信息表单
//2-从事临时经营活动，隐藏建筑施工及安装单位信息表单
function acctCreateReasonInit(form,syncEccs){
	var acctCreateReason = $("#acctCreateReason").val();
	if(acctCreateReason == "2"){
        hideInnerTabArray("tab_1",true,form);
        syncEccs == 'true';
        // $('#customerInfo').hide();
		selectedDisabledOption(form,"acctFileType","14,15");
	}else{
        showInnerTabArray("tab_1",form);
        syncEccs == 'true' &&  $('#customerInfo').show() && $('a[href="#tab_1"]').click();
		if(acctCreateReason == "1"){
			selectedDisabledOption(form,"acctFileType","16");
		}
	}
}


//证明文件1种类--临时和非临时
//字典选项，根据账户类型不同，临时户选项为：01，09
//能返显工商信息时，默认下拉值为01-工商营业执照，
function fileTypeOfLinShi(){
	$("#fileType").empty();
	$("#fileType").append("<option value=''></option>");
	$("#fileType").append("<option value='01'>01-工商营业执照</option>");
	$("#fileType").append("<option value='09'>09-主管部门批文</option>");
}


//证明文件1种类-特殊
//字典选项，根据账户类型不同，临时户选项为：01，09
//能返显工商信息时，默认下拉值为01-工商营业执照，
function fileTypeOfTeShu(){
	$("#fileType").empty();
	$("#fileType").append("<option value=''></option>");
	$("#fileType").append("<option value='12'>12-证券从业资格证书</option>");
	$("#fileType").append("<option value='17'>17-其他</option>");
}


//建筑施工及安装单位信息，证件号码则根据身份证规则进行输入校验
function nontmpLegalIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#nontmpLegalIdcardType").attr("lay-filter","nontmpLegalIdcardType");
        form.on('select(nontmpLegalIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("nontmpLegalIdcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("nontmpLegalIdcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}


//上级机构信息，证件号码则根据身份证规则进行输入校验
function parLegalIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#parLegalIdcardType").attr("lay-filter","parLegalIdcardType");
        form.on('select(parLegalIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("parLegalIdcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("parLegalIdcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}



//初始化：上级机构信息，证件号码则根据身份证规则进行输入校验
function parLegalIdcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#parLegalIdcardType").val() =="1"){
            addValidate("parLegalIdcardNo",{isIdCardNo : "true"},false,validator);
        }
    }
}


//股东信息，证件号码则根据身份证规则进行输入校验
function idcardTypeChange(form,validator){
    if(validator != undefined){
        $("#idcardType").attr("lay-filter","idcardType");
        form.on('select(idcardType)',function (data) {
            if(data.value=="1"){
                addValidate("idcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("idcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}

//初始化：股东信息，证件号码则根据身份证规则进行输入校验
function idcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#idcardType").val() == "1"){
            addValidate("idcardNo",{isIdCardNo : "true"},false,validator);
        }
    }
}



//内设部门信息-负责人身份证件种类，证件号码则根据身份证规则进行输入校验
function insideLeadIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#insideLeadIdcardType").attr("lay-filter","insideLeadIdcardType");
        form.on('select(insideLeadIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("insideLeadIdcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("insideLeadIdcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}

//初始化：内设部门信息-负责人身份证件种类，证件号码则根据身份证规则进行输入校验
function insideLeadIdcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#insideLeadIdcardType").val() == "1"){
            addValidate("insideLeadIdcardNo",{isIdCardNo : "true"},false,validator);
        }
    }
}


//资金管理人信息-资金管理人身份证种类，证件号码则根据身份证规则进行输入校验
function fundManagerIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#fundManagerIdcardType").attr("lay-filter","fundManagerIdcardType");
        form.on('select(fundManagerIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("fundManagerIdcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("fundManagerIdcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}

//初始化：资金管理人信息-资金管理人身份证种类，证件号码则根据身份证规则进行输入校验
function fundManagerIdcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#fundManagerIdcardType").val() == "1"){
            addValidate("fundManagerIdcardNo",{isIdCardNo : "true"},false,validator);
        }
    }
}

//授权经办人信息，证件号码则根据身份证规则进行输入校验
function operatorIdcardTypeChange(form,validator){
    if(validator != undefined){
        $("#operatorIdcardType").attr("lay-filter","operatorIdcardType");
        form.on('select(operatorIdcardType)',function (data) {
            if(data.value=="1"){
                addValidate("operatorIdcardNo",{isIdCardNo : "true"},false,validator);
            }else{
                removeValidate("operatorIdcardNo",false,validator,"isIdCardNo");
            }
        });
    }
}


//初始化：授权经办人信息，证件号码则根据身份证规则进行输入校验
function operatorIdcardTypeInit(form,validator){
    if(validator != undefined){
        if($("#operatorIdcardType").val() == "1"){
            addValidate("operatorIdcardNo",{isIdCardNo : "true"},false,validator);
        }
    }
}

//新开户页面的账号为非必填 
function newCreateCustomer(buttonType){
    if(buttonType == 'saicValidate'){
        removeValidate("acctNo",true,undefined,"required");
    }
}


//存款人名称复制到存款人简称
function depositorNameChange(validator){
	$("#depositorName").removeAttr("onpropertychange");
	$("#depositorName").removeAttr("oninput");
	$("#depositorName").on("change",function(){
		$('#acctShortName').val($(this).val()).blur();
	});
}

//当存款人类别为'无字号个体工商户'时，添加存款人名称前缀"个体户-"，不能仅为‘个体户-’
function depositorNameValidChange(form,validator,strDepositorType) {
    if(validator != undefined){
        if (!strDepositorType) strDepositorType = $('#depositorType').val();
        var elemDepositorName = $('#depositorName');
        var strDepositorName = elemDepositorName.val();
        if(strDepositorType == "14") {
            if(strDepositorName.substr(0,3)!=='个体户') {
                elemDepositorName.val('个体户' + strDepositorName).change().blur();
            }
            addValidate("depositorName",{hasPrefix:"个体户"},true,validator);

        }else{
            if(strDepositorName.substr(0,3) ==='个体户') {
                elemDepositorName.val(strDepositorName.substr(3)).change().blur();
            }
            removeValidate("depositorName",false,validator,"hasPrefix");
        }
    }
}

//当存款人类别为'无字号个体工商户'时，添加存款人名称前缀"个体户-"，不能仅为‘个体户-’
function feilinshiDepositorTypeValidChange(form,validator) {
    form.on('select(depositorType)',function (data) {
        if (validator != undefined) {
            var strDepositorType = data.value;
            if (strDepositorType == "01" || strDepositorType == "02" || strDepositorType == "13" || strDepositorType == "14") {
                removeValidateFlag(["acctCreateReason"]);
                removeValidate("acctCreateReason", true, validator, "required");
            } else {
                addValidateFlag(["acctCreateReason"]);
                addValidate("acctCreateReason", true, validator, "required");
            }
        }
    });
}

//基本户-信用机构上传
function isSyncEccsChange(form,validator){
    if(validator != undefined){
        addValidate("workAddress",{required : true},true,validator);
        addValidate("orgStatus",{required : true},true,validator);
        addValidate("basicAccountStatus",{required : true},true,validator);
    }
}


//一般户-人行上报
function isYiBanPbcSyncChange(form,validator){
    if(validator != undefined){
        addValidate("basicAcctRegArea",{required : true},true,validator);
        //addValidate("acctFileType",{required : true},true,validator);
        addValidate("depositorName",{required : true},true,validator);
    }
}

//预算/非预算-人行上报
function isYuSuanPbcSyncChangeValid(validator){
    if(validator != undefined){
        addValidate("insideDeptName",{required : true},true,validator);
        addValidate("insideLeadName",{required : true},true,validator);
        addValidate("insideLeadIdcardType",{required : true},true,validator);
        addValidate("insideLeadIdcardNo",{required : true},true,validator);
        addValidate("insideTelephone",{required : true},true,validator);
        addValidate("insideZipcode",{required : true},true,validator);
        addValidate("insideAddress",{required : true},true,validator);
    }
}


function isYuSuanPbcSyncChangeNotValid(validator){
    if(validator != undefined){
        removeValidate("insideDeptName",true,validator,"required");
        removeValidate("insideLeadName",true,validator,"required");
        removeValidate("insideLeadIdcardType",true,validator,"required");
        removeValidate("insideLeadIdcardNo",true,validator,"required");
        removeValidate("insideTelephone",true,validator,"required");
        removeValidate("insideZipcode",true,validator,"required");
        removeValidate("insideAddress",true,validator,"required");
    }
}

//预算/非预算-人行上报
function isYuSuanPbcSyncChange(form,validator){
    if(validator != undefined){
        //addValidate("acctFileType",{required : true},true,validator);
        //addValidate("acctFileNo",{required : true},true,validator);
        addValidate("capitalProperty",{required : true},true,validator);
        //addValidate("regAreaCode",{required : true},true,validator);
        addValidate("accountNameFrom",{required : true},true,validator);
    }
}

//非临时-人行上报
function isFeiLinshiPbcSyncChange(form,validator){
    if(validator != undefined){
        //addValidate("regAreaCode",{required : true},true,validator);
    }
}


//特殊-人行上报
function isTeshuPbcSyncChange(form,validator){
    if(validator != undefined){
        addValidate("depositorType",{required : true},true,undefined);
    }
}

function mapCheck(forId,forVal,map,changeFieldsMap){
    var mapVal = map.get(forId);
    if(map.containsKey(forId) && checkNull(forVal,mapVal)){
        changeFieldsMap.remove(forId);
        hideChangedMessage(forId,mapVal);
    }else{
        var changeVal = mapVal;
        if(mapVal =="" || mapVal =="null"|| mapVal ==undefined){
            mapVal ="空";
            changeVal = "";
        }
        changeFieldsMap.put(forId, changeVal);
        showChangedMessage(forId,"原始值: "+mapVal);
    }
	
}

function listenerDate(elem,value,map,changeFieldsMap){
	if(map.containsKey(elem)){
    	var mapVal = map.get(elem);
		if(checkNull(value,mapVal)){
            changeFieldsMap.remove(elem);
			removeChangedMessage(elem);
		}else{
		    var changeVal = mapVal;
			if(mapVal =="" || mapVal =="null"|| mapVal ==undefined){
				mapVal ="空";
                changeVal = "";
			}
			removeChangedMessage(elem);
            changeFieldsMap.put(elem, changeVal);
			addChangedMessage(elem,"原始值: "+mapVal);
		}
	}
}

function listenerAllInput(map,changeFieldsMap){
    $(":input").on("change",function(){
    	var forId = $(this).attr("id");
    	var forVal = $(this).val();
    	mapCheck(forId,forVal,map,changeFieldsMap);
    });
}


function listenerAllSelect(form,map,changeFieldsMap){
	form.on('select',function (data) {
		var forId = data.elem.id;
		var forVal = data.value;
		if(forId !="regProvince" && forId !="regCity" && forId !="regArea" && forId !="workProvince" && forId !="workCity"  &&forId !="workArea"){
            console.log(forVal);
		    
            var mapVal = map.get(forId);

            if(map.containsKey(forId) && checkNull(forVal,mapVal)){
                changeFieldsMap.remove(forId);
                hideChangedMessage(forId,mapVal);
            }else{
                var changeVal = mapVal;
                if(mapVal =="" || mapVal =="null"|| mapVal ==undefined){
                    mapVal ="空";
                    changeVal = "";
                }else{
                    if($("#"+forId).length<1) {
                        mapVal = $('[id="'+forId+'"] option[value="'+mapVal+'"]').text();
                    }else{
                        mapVal = $("#"+forId+" option[value='"+mapVal+"']").text();
                    }
                }
                changeFieldsMap.put(forId, changeVal);
                showChangedMessage(forId,"原始值: "+mapVal);
            }
			
		}else{
            var mapVal = map.get(forId);
            if(map.containsKey(forId) && checkNull(forVal,mapVal)){
                changeFieldsMap.remove(forId);
                removeChangedMessageForAddress(forId);
            }else{
                var changeVal = mapVal;
                if(mapVal =="" || mapVal =="null"|| mapVal ==undefined){
                    mapVal ="空";
                    changeVal = "";
                }else{
                    mapVal = $("#"+forId+" option[value='"+mapVal+"']").text();
                }
                removeChangedMessageForAddress(forId);
                changeFieldsMap.put(forId, changeVal);
                addChangedMessageForAddress(forId,"原始值: "+mapVal);
            }
		}
	});
}

function checkNull(forVal,mapVal){
	if(mapVal =="" || mapVal =="null"|| mapVal ==undefined){
		mapVal ="";
	}
	if(forVal =="" || forVal =="null"|| forVal ==undefined){
		forVal ="";
	}
	return forVal==mapVal;
}


function getChangedItem(map, billId){
    $.get ('../../allAccountPublic/changedItem?billId=' + billId, null, function (data) {
        setChangedIteam(data,map);
    });
}

function convertDepositorType(depositorType) {
    if(depositorType == '' || depositorType==undefined) {
        return '';
    }

    if(depositorType == '01') {
        depositorType = 'DEPOSITOR_TYPE_01'
    } else if(depositorType == '02') {
        depositorType = 'DEPOSITOR_TYPE_02'
    } else if(depositorType == '03') {
        depositorType = 'DEPOSITOR_TYPE_03'
    } else if(depositorType == '04') {
        depositorType = 'DEPOSITOR_TYPE_04'
    } else if(depositorType == '05') {
        depositorType = 'DEPOSITOR_TYPE_05'
    } else if(depositorType == '06') {
        depositorType = 'DEPOSITOR_TYPE_06'
    } else if(depositorType == '07') {
        depositorType = 'DEPOSITOR_TYPE_07'
    } else if(depositorType == '08') {
        depositorType = 'DEPOSITOR_TYPE_08'
    } else if(depositorType == '09') {
        depositorType = 'DEPOSITOR_TYPE_09'
    } else if(depositorType == '10') {
        depositorType = 'DEPOSITOR_TYPE_10'
    } else if(depositorType == '11') {
        depositorType = 'DEPOSITOR_TYPE_11'
    } else if(depositorType == '12') {
        depositorType = 'DEPOSITOR_TYPE_12'
    } else if(depositorType == '13') {
        depositorType = 'DEPOSITOR_TYPE_13'
    } else if(depositorType == '14') {
        depositorType = 'DEPOSITOR_TYPE_14'
    } else if(depositorType == '15') {
        depositorType = 'DEPOSITOR_TYPE_15'
    } else if(depositorType == '16') {
        depositorType = 'DEPOSITOR_TYPE_16'
    } else if(depositorType == '17') {
        depositorType = 'DEPOSITOR_TYPE_17'
    } else if(depositorType == '20') {
        depositorType = 'DEPOSITOR_TYPE_20'
    } else if(depositorType == '50') {
        depositorType = 'DEPOSITOR_TYPE_50'
    } else if(depositorType == '51') {
        depositorType = 'DEPOSITOR_TYPE_51'
    } else if(depositorType == '52') {
        depositorType = 'DEPOSITOR_TYPE_52'
    }else if(depositorType == '53') {
        depositorType = 'DEPOSITOR_TYPE_53'
    }

    return depositorType;
}

function carrierBtnClick() {
    var telephone = document.getElementsByName('legalTelephone')[0].value;
    var name = document.getElementsByName('legalName')[0].value;
    var idcardNo = document.getElementsByName('legalIdcardNo')[0].value;

    $.get("../../carrier/getCarrierOperatorResult?name=" + encodeURI(name) + "&mobile=" + telephone + "&cardno=" + idcardNo, null, function (result) {
        if (result.result) {
            document.getElementById("carrierResult").innerHTML = result.result;
        }

    });
}

function printfClick(common, billType) {
    var depositorType = $('#depositorType').val();
    depositorType = convertDepositorType(depositorType);
    var acctType = $('#acctType').val();
    if(isEmpty(billType)) {
        billType = $('#billType').val();
    }
    if(acctType==undefined){
        acctType ="";
    }
    $.get('../../template/getTemplateNameList?billType=' + billType + '&depositorType=' + depositorType+'&acctType='+acctType, function (data) {
        if(data == '' || data == null) {
            layer.alert("对应打印模版未配置");
        } else {

            //页面层
            index = layer.open({
                title: '请选择打印模版',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="templateOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" onclick="templateClick()" id="templateBtn"> 确定</button>'
            });

            for (var i = 0; i < data.length; i++) {
                $('#templateOption').append("<option value=" + data[i] + ">" + data[i] + "</option>")
            }
        }
    })
}

function format(timestamp)
{
    var time = new Date(timestamp);
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
}

/**
 * 账户影像显示配置
 * @param accoungImageShow
 */
function accountImageConfig(accoungImageShow){
    $.get("../../accountImage/getConfig",function (res){
        if(res.data.length>0){
            if(res.code === 'ACK') {
                for (var i = 0; i < res.data.length; i++){
                    if(res.data[i].configKey=="imageCollect"){
                        if(res.data[i].configValue=="accountImage"){
                            $("#accountImageInfo").css("display","");
                            accoungImageShow = true;
                        }else if(res.data[i].configValue=="hardWareImage"){
                            $("#accountImageInfo").css("display","");
                            accoungImageShow = true;
                        }else if(res.data[i].configValue=="thirdImage"){
                            $("#thirdImage").css("display","");
                            accoungImageShow = true;
                        }
                    }
                }
            }else{
                $("#accountImageInfo").css("display","none");
                $("#thirdImage").css("display","none");
                accoungImageShow = false;
            }
        }else{
            $("#accountImageInfo").css("display","none");
            $("#thirdImage").css("display","none");
            accoungImageShow = false;
        }
    });
}

/**
 * 账户影像运行
 * @param acctBillsId
 * @param operateType
 */
function runImageData(acctBillsId,operateType,readOnly){
    var imageInfo;
    //扫描影像
    $('#btnScanImage').click(function(){
        getImageData(function(list){
            
            if(list && list.data && list.data.length > 0) {
                var imageTypeOptions = '';
                for(i=0; i<list.data.length; i++) {
                    var item = list.data[i];
                    imageTypeOptions += '<option value="' + item.id + '">' + item.type + '</option>';
                }
            
                $.get('../account/scan.html', null, function (html) {
                    layer.open({
                        id: 1,
                        type: 1,
                        title: '影像拍摄',
                        content: html,
                        btn: ['确定', '左转', '右转'],
                        shade: false,
                        area: ['600px', '600px'],
                        maxmin: true,
                        yes: function (index, layero) {

                            var imageType = $('#imageType').val();
                            if (!imageType) {
                                layer.msg('请选择影像类型');
                                return false;
                            }

                            $.ajax({
                                url: '../../imageAll/uploadByHardWare',
                                type: 'post',
                                data: { imageTypeId: imageType,base64:getImageBase64(),acctBillsId:acctBillsId},
                                dataType: "json",
                                success: function (res) {
                                    if (res.code === 'ACK') {
                                        layer.msg('已保存');
                                        layer.close(index);
                                        //location.reload();

                                        getImageData(function(list){
                                            imageInfo.refresh({data: list.data,acctId:list.acctId});
                                        },imageInfo,acctBillsId,operateType);
                                    } else {
                                        layer.msg(res.message);
                                    }
                                }

                            });
                        },
                        btn2: function(index, layero){
                            rotateLeft();
                            return false
                        },
                        btn3: function(index, layero){
                            rotateRight();
                            return false
                        },
                        success: function (layero, index) {
                            layero.find('#imageType').append(imageTypeOptions);
                        }
                    });
                });
            }

        },undefined,acctBillsId,operateType);

    });

    //传送影像平台
    $('#btnSendImage').click(function(){
        layer.confirm('确定传送影像平台吗？', {
            btn: ['确定','取消']
        }, function(){
            $.ajax({
                url: '../../imageAll/uploadToImage',
                type: 'POST',
                dataType: "json",
                data: {acctBillsId: acctBillsId},
                success: function (data) {
                    if (data.code === 'ACK') {
                        layer.msg("传送成功");
                    } else {
                        layer.msg(data.message);
                    }
                }
            });
        });
    });
    $('#btn_syncImage').click(function(){
        layer.confirm('确定是否上报？', {
            btn: ['确定','取消']
        }, function(){
            $.ajax({
                url: "../../imageAll/sync",
                type: 'GET',
                dataType: "json",
                data: {acctBillsId: acctBillsId},
                success: function (data) {
                    if (data.code === 'ACK') {
                        layer.msg("传送成功");
                    } else {
                        layer.msg(data.message);
                    }
                }
            });
        });
    });
    getImageData(function(data){
        imageInfo = layui.imageInfo({
            elem: '#imageList',
            data: data.data,
            uploadUrl: '../../imageAll/uploadImage',
            downloadUrl: '../../imageAll/downloadImage',
            downloadZipUrl: '../../imageAll/downloadZip2',
            editImageUrl: '../../imageAll/editImage',
            deleteImageUrl: '../../imageAll/deleteImage',
            sendImageUrl: '../../imageAll/uploadToImage',
            batchDeleteImageUrl:'../../imageAll/delete',
            batchMoveImageUrl:'../../imageAll/beatchEdit',
            acctBillsId: data.acctBillsId,
            acctId: data.acctId,
            readOnly: readOnly,
            downloadType: 2,
            isSend: true,
            handleUpload: function(id) {
                //上传
                console.log(id);

                layer.msg('已上传');
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            },
            handleEdit: function(data) {
                //编辑
                console.log(data);

                layer.msg('已保存');
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            },
            handleDelete: function(id) {
                //删除
                console.log(id);

                layer.msg('已删除');
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            },
            handleSend: function(id) {
                //传送影像平台
                console.log(id);

                layer.msg('已传送影像平台');
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            },
            handleBatchDelete:function (id) {
                console.log("批量删除："+id);
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            },
            handleBatchMove:function (id) {
                console.log("批量移动："+id);
                getImageData(function(list){
                    imageInfo.refresh({data: list.data,acctId:list.acctId});
                },imageInfo,acctBillsId,operateType);
            }
        });

    },undefined,acctBillsId,operateType);
}
function selecImageConfig(depositorTypeCode,acctType,billType){

        $.get("../../imageType/getImageType?acctType="+acctType+"&operateType="+billType+"&depositorTypeCode="+depositorTypeCode,function (res) {
            if(res.code === 'ACK') {
                $('#docCode').empty();
                $('#docCodeLi').empty();
            }
        });
}
/**
 * 账户影像获得详情
 * @param callback
 * @param imageInfo
 * @param acctBillsId
 * @param operateType
 */
function getImageData(callback,imageInfo,acctBillsId,operateType) {
    if(imageInfo == undefined){
        tempId = "";
    }else{
        tempId = imageInfo.config.tempId;
    }
    $.ajax({
        type: "POST",
        url: "../../imageAll/queryByBillsId",
        data: {
            acctBillsId: acctBillsId,
            acctType: $("#acctType").val(),
            operateType: operateType,
            depositorTypeCode: $("#depositorType").val(),
            tempId: tempId,
        },
        dataType: "json",
        async: false,
        success: function (res) {
            if (res.code === "ACK") {
                callback && callback(res.data);
            }
        },
        error: function () {
            layer.msg("error");
        }
    });
}


/**
 * 删除流水和账号
 * @param billId
 * @param resultCall
 */
function deleteBillsAndAccount(billId,resultCall){
    layer.confirm('确定删除数据吗？', null, function (index) {
        $.ajax({
            type: "POST",
            url: "../../allBillsPublic/delete/"+billId,
            dataType: "json",
            success: function (data) {
                if(data.rel){//删除成功
                    resultCall();
                }else{//删除失败
                    layer.alert("删除失败,请确认状态");
                }
            },
            error: function (err) {
                layer.msg('处理失败['+err.responseJSON.message+']');
            }
        });
        layer.close(index);
    });
}

function add0(m){return m<10?'0'+m:m }

/**
 * 根据imageFlag，是否只显示影像tab页
 * @param imageFlag
 */
function showImageAccountTabOnly(imageFlag,acctBillsId,operateType,readOnly,acctType) {
    if (imageFlag === "1") {//隐藏其他tab，只显示影像补录tab

        $('a[data-toggle = "tab"]').parent().each(function () {
            $(this).removeClass();//隐藏默认显示的tab
            var liId = $(this).attr("id");
            if (liId === undefined || (liId != "accountImageInfo" && liId != "imageInfo" && liId != "thirdImage") ) {
                $(this).hide();
            }
        });
        $(".tab-pane").hide();
        //显示影像tab
        $.get("../../accountImage/getConfig",function (res){
            if (res.data.length > 0) {
                if (res.code === 'ACK') {
                    for (var i = 0; i < res.data.length; i++) {
                        if(res.data[i].configKey=="imageCollect"){
                            if(res.data[i].configValue=="accountImage" || res.data[i].configValue=="hardWareImage"){
                                //$("#accountImageInfo").css("display","");
                                if(acctType=='feilinshi' || acctType=='feiyusuan' || acctType=='linshi' || acctType=='yanzi' || acctType=='yusuan'|| acctType=='zengzi'){
                                    $("#tab_12").show();
                                }else{
                                    $("#tab_10").show();
                                }
                            }else if(res.data[i].configValue=="thirdImage"){
                                $("#tab_thirdImage").show();
                                // $("#thirdImage").css("display","");
                            }
                        }
                    }
                }
            }
        });
        runImageData(acctBillsId, operateType ,readOnly);
    }
}


/**
 * 变更时，本地客户，工商，人行数据进行比较，并创建弹出层
 */
function addChangeCompareBox(laytpl, response, callback) {
    $.get("changeCompare.html", null, function (template) {
        if (template) {
            laytpl(template).render(response, function (html) {
                layer.open({
                    type: 1,
                    title: '字段校验',
                    content: html,
                    btn: (function () {
                        return ['继续变更', '关闭'];
                    })(),
                    shade: [0.8, '#393D49'],
                    offset: ['20px', '20%'],
                    area: ['800px', '600px'],
                    yes: function (index) {
                        layer.close(index);
                        callback && callback(response.result.final);
                    },
                    btn2: function (index) {
                        layer.close(index);
                        parent.tab.deleteTab(parent.tab.getCurrentTabId());
                    }
                });
            });
        }
    });
}

/**
 * 开户时，本地客户，工商，人行数据进行比较，并创建弹出层
 */
function addCompareBox(laytpl, response, callback) {
    $.get("compare.html", null, function (template) {
        if (template) {
            laytpl(template).render(response, function (html) {
                layer.open({
                    type: 1,
                    title: '开户校验',
                    content: html,
                    btn: (function () {
                        if (response.code === "1") {
                            return ['关闭', '继续开户'];
                        } else {
                            return ['关闭'];
                        }
                    })(),
                    shade: [0.8, '#393D49'],
                    offset: ['20px', '20%'],
                    area: ['800px', '600px'],
                    yes: function (index) {
                        layer.close(index);
                        parent.tab.deleteTab(parent.tab.getCurrentTabId());
                    },
                    btn2: function (index) {
                        layer.close(index);
                        response.result.final.organFullId = null;
                        callback && callback(response.result.final);
                    },
                    cancel: function (index) {//点击右上角的X
                        layer.close(index);
                        parent.tab.deleteTab(parent.tab.getCurrentTabId());
                    }
                });
            });
        }
    });
}

/**
 * 获取流水是否需要单人操作上锁配置
 * @return true:需要单人操作时上锁；false：允许多人同时操作一条流水信息
 */
function getBillLockConfig(callback) {
    $.get("../../config/findByKey?configKey=billLockConfigEnabled", null, function (data) {
        callback(data);
    });
}

/**
 * 对业务流水进行操作前，判断该流水是否已经有人员在操作了
 * @param billId 流水id
 * @param callback 回调函数
 */
function getBillIsBusy(billId, callback) {
    
    getBillLockConfig(function (flag) {
        if (flag) {
            $.ajax({
                url: '../../billOperateLock/getBillIsBusy',
                type: 'post',
                data: {billId: billId},
                dataType: "json",
                async: false,
                success: function (res) {
                    callback(res);
                }
            });
        } else {
            callback({rel: false});
        }
    });
}

/**
 * 每隔一段时间，对指定业务流水重新上锁
 * @param buttonType    按钮类型
 * @param billId    流水id
 * @param ms 毫秒数
 */
function circulationBillLock(buttonType, billId, ms) {
    //当操作类型为审核或者补录时，业务流水上锁，避免其他人员同时对同一个业务流水进行操作
    if (buttonType === 'sync' || buttonType === 'update') {
        billLock(billId);
        setInterval(function () {
            billLock(billId);
        }, ms);
    }
}

/**
 * 业务流水上锁
 * @param billId    流水id
 */
function billLock(billId) {
    $.ajax({
        url: '../../billOperateLock/billLock',
        type: 'post',
        data: {billId: billId},
        dataType: "json",
        success: function (res) {
            if (!res.rel) {
                layer.msg(res.msg);
                console.log('上锁失败,关闭页面！');
                parent.tab.deleteTab(parent.tab.getCurrentTabId());
            }
        }
    });
}

/**
 * 当关闭页面时，对指定业务流水解锁
 * @param buttonType    按钮类型
 * @param billId    流水id
 */
function billUnLock(buttonType, billId){
    //当操作类型为审核或者补录时，业务流水解锁
    if (buttonType === 'sync' || buttonType === 'update') {
        // $.get( '../../billOperateLock/billUnLock?billId=' + billId);
        $.ajax({
            url: '../../billOperateLock/billUnLock',
            type: 'post',
            data: {billId: billId},
            dataType: "json",
            async: false,
            success: function (res) {
                if (!res.rel) {
                    layer.msg(res.msg);
                    console.log('解锁失败,关闭页面！');
                    parent.tab.deleteTab(parent.tab.getCurrentTabId());
                }
            }
        });
    }
}

/**
 * 保存、提交、上报按钮执行结束后，打开对公报备管理页面，在新tab页中关闭当前页
 */
function addCompanyAccountsTab(common, buttonType, billId, whiteList) {
    var tabId = encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId()));//当前tab页id
    var url;
    if(whiteList == '1'){
        url = 'company/companyAccounts.html?whiteList=1&tabId=' + tabId;
    }else{
        url = 'company/companyAccounts.html?tabId=' + tabId;
    }
    //buttonType与billId 这2个参数是为了解锁该业务流水用
    if (buttonType) {
        url += '&buttonType=' + buttonType;
    }
    if (billId) {
        url += '&billId=' + billId;
    }

    parent.tab.tabAdd({
        href: url,
        icon: 'fa fa-calendar-plus-o',
        title: '对公报备管理'
    });
}

//取消开户许可证核发显示
function cancelLicenseCheck() {
    var operateType = $("#billType").val() == undefined ? "" : $("#billType").val();
    var depositorType = $("#depositorType").val() == undefined ? "" : $("#depositorType").val();
    var acctType = $("#acctType").val() == undefined ? "" : $("#acctType").val();
    if(depositorType == "01" || depositorType == "02" || depositorType == "13" || depositorType == "14"){
        $("#fileType").empty();
        $("#fileType").append("<option value='01'>01-工商营业执照</option>");
    }else{
        $("#fileType").empty();
        $("#fileType").append("<option value=''></option>");
        $("#fileType").append("<option value='02'>02-批文</option>");
        $("#fileType").append("<option value='03'>03-登记证书</option>");
        $("#fileType").append("<option value='04'>04-开户证明</option>");
    }
    $.get("../../cancelLicense/check" + '?operateType=' + operateType + "&acctType=" + acctType + "&depositorType=" + depositorType, null, function (data) {
        if (data.data == true || data.data == 'true') {
            $("#qxkhxkz").show();
            $("#openKeyDiv").show();//开户许可证号（新）
        } else {
            $("#qxkhxkz").hide();
            $("#openKeyDiv").hide();//开户许可证号（新）
        }
    });
}

function showCancelHeZhun(data){
    if(data.cancelHeZhun == true){
        $("#qxkhxkz").show();
    }else{
        $("#qxkhxkz").hide();
    }
}
/**
 * 客户信息表单不可编辑
 */
function hideCustomer() {
    $('#depositorName').prop("disabled",true);
    $("#depositorName").addClass("disableElement");
    $('#acctShortName').prop("disabled",true);
    $("#acctShortName").addClass("disableElement");
    $('#depositorType').prop("disabled",true);
    $("#depositorType").addClass("disableElement");
    $('#orgEnName').prop("disabled",true);
    $("#orgEnName").addClass("disableElement");
    $('#regAddress').prop("disabled",true);
    $("#regAddress").addClass("disableElement");
    $('#regFullAddress').prop("disabled",true);
    $("#regFullAddress").addClass("disableElement");
    $('#regAreaCode').prop("disabled",true);
    $("#regAreaCode").addClass("disableElement");
    $('#industryCode').prop("disabled",true);
    $("#industryCode").addClass("disableElement");
    $('#regOffice').prop("disabled",true);
    $("#regOffice").addClass("disableElement");
    $('#regType').prop("disabled",true);
    $("#regType").addClass("disableElement");
    $('#regNo').prop("disabled",true);
    $("#regNo").addClass("disableElement");
    $('#fileType').prop("disabled",true);
    $("#fileType").addClass("disableElement");
    $('#fileNo').prop("disabled",true);
    $("#fileNo").addClass("disableElement");
    $('#fileType2').prop("disabled",true);
    $("#fileType2").addClass("disableElement");
    $('#fileNo2').prop("disabled",true);
    $("#fileNo2").addClass("disableElement");
    $('#fileSetupDate').prop("disabled",true);
    $("#fileSetupDate").addClass("disableElement");
    $('#fileDue').prop("disabled",true);
    $("#fileDue").addClass("disableElement");
    $('#businessLicenseNo').prop("disabled",true);
    $("#businessLicenseNo").addClass("disableElement");
    $('#businessLicenseDue').prop("disabled",true);
    $("#businessLicenseDue").addClass("disableElement");
    $('#isIdentification').prop("disabled",true);
    $("#isIdentification").addClass("disableElement");
    $('#regCurrencyType').prop("disabled",true);
    $("#regCurrencyType").addClass("disableElement");
    $('#registeredCapital').prop("disabled",true);
    $("#registeredCapital").addClass("disableElement");
    $('#businessScope').prop("disabled",true);
    $("#businessScope").addClass("disableElement");
}
function sysnImage(acctBillsId,common, buttonType, billId, whiteList) {
    $.ajax({
        url: "../../imageAll/sync",
        type: 'GET',
        dataType: "json",
        data: {acctBillsId: acctBillsId},
        success: function (data) {
            if (data.code === 'ACK') {
                layer.alert("上报成功", {
                    title: "提示",
                    closeBtn: 0
                }, function (index) {
                    layer.close(index);
                    addCompanyAccountsTab(common, buttonType, billId, whiteList);
                });
            } else {
                layer.alert(data.message, {
                    title: "提示",
                    closeBtn: 0
                }, function (index) {
                    layer.close(index);
                    addCompanyAccountsTab(common, buttonType, billId, whiteList);
                });
            }
        }
    });
}