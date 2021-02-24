var config = {
    baseUrl: "../../config",
    entity: "config"
};
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
config.init = function (form) {
    $.get(config.baseUrl+"/", null, function (data) {
        data.data.forEach(function (config) {
            switch (config.configKey) {
                case 'pbcEnabled':
                    $("input[name='pbcEnabled']").prop("checked", "");
                    $("input[name='pbcEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break
                case 'pbcLoginMockEnabled':
                    $("input[name='pbcLoginMockEnabled']").prop("checked", "");
                    $("input[name='pbcLoginMockEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'pbcSyncMockEnabled':
                    $("input[name='pbcSyncMockEnabled']").prop("checked", "");
                    $("input[name='pbcSyncMockEnabled']").prop("checked", "");
                    $("input[name='pbcSyncMockEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'idcardEnabled':
                    $("input[name='idcardEnabled']").prop("checked", "");
                    $("input[name='idcardEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'idcardLoginEnabled':
                    $("input[name='idcardLoginEnabled']").prop("checked", "");
                    $("input[name='idcardLoginEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'tpoAutoSyncEnabled':
                    $("input[name='tpoAutoSyncEnabled']").prop("checked", "");
                    $("input[name='tpoAutoSyncEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'annualAgainEnabled':
                    $("input[name='annualAgainEnabled']").prop("checked", "");
                    $("input[name='annualAgainEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'annualSaicAgainEnabled':
                    $("input[name='annualSaicAgainEnabled']").prop("checked", "");
                    $("input[name='annualSaicAgainEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'accountImageMockEnabled':
                    $("input[name='accountImageMockEnabled']").prop("checked", "");
                    $("input[name='accountImageMockEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    if(config.configValue=="true"){
                        $("#collectType").show();
                    }else{
                        $("#third").hide();
                        $("#a").hide();
                        $("#collectType").hide();
                    }
                    break;
                case 'imageCollect':
                    $("input[name='imageCollect']").prop("checked", "");
                    $("input[name='imageCollect'][value='"+config.configValue+"']").prop("checked", "checked");
                    if(config.configValue=="thirdImage"){
                        $("#third").show();
                        $("#a").hide();
                    }else if(config.configValue=="hardWareImage" || config.configValue=="accountImage"){
                        $("#third").hide();
                        $("#a").show();
                    }
                    break;
                /*case 'thirdImageMockEnabled':
                    $("input[name='thirdImageMockEnabled']").prop("checked", "");
                    $("input[name='thirdImageMockEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;*/
                case 'import':
                    $("input[name='importConfigEnabled']").prop("checked", "");
                    $("input[name='importConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'reservationValidationEnabled':
                    $("input[name='printConfreservationValidationEnabledigEnabled']").prop("checked", "");
                    $("input[name='reservationValidationEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'printf':
                    $("input[name='printConfigEnabled']").prop("checked", "");
                    $("input[name='printConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'billLockConfigEnabled':
                    $("input[name='billLockConfigEnabled']").prop("checked", "");
                    $("input[name='billLockConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'customerDivEnabled':
                    $("input[name='customerDivEnabled']").prop("checked", "");
                    $("input[name='customerDivEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'legalDivEnabled':
                    $("input[name='legalDivEnabled']").prop("checked", "");
                    $("input[name='legalDivEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'imageDivEnabled':
                    $("input[name='imageDivEnabled']").prop("checked", "");
                    $("input[name='imageDivEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'breakAppointCustomDivEnabled':
                    $("input[name='breakAppointCustomDivEnabled']").prop("checked", "");
                    $("input[name='breakAppointCustomDivEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'pbcStartTime':
                    var time = config.configValue;
                    var splited = time.split(":");
                    var hour = splited[0];
                    var minute = splited[1];
                    $("#pbcStartHour").val(hour);
                    $("#pbcStartMinute").val(minute);
                    break;
                case 'pbcEndTime':
                    var time = config.configValue;
                    var splited = time.split(":");
                    var hour = splited[0];
                    var minute = splited[1];
                    $("#pbcEndHour").val(hour);
                    $("#pbcEndMinute").val(minute);
                    break;
                case 'acctStatisticsRange':
                    $("#acctStatisticsRange").val(config.configValue);
                    break;
                case 'publicKey':
                    $("#publicKey").val(config.configValue);
                    break;
                case 'thirdUrl':
                    $("#thirdUrl").val(config.configValue);
                    break;
                case 'organid':
                    $("#organId").val(config.configValue);
                    break;
                case 'thirdParthPublicKey':
                    $("#thirdParthPublicKey").val(config.configValue);
                    break;
                case 'systemIP':
                    $("#systemIP").val(config.configValue);
                    break;
                case 'resvrProcessDay':
                    $("#resvrProcessDay").val(config.configValue);
                    break;
                case 'tempAcctNoticeDay':
                    $("#tempAcctNoticeDay").val(config.configValue);
                    break;
                case 'tempAcctOverNoticeDay':
                    $("#tempAcctOverNoticeDay").val(config.configValue);
                    break;
                case 'fileDueNoticeDay':
                    $("#fileDueNoticeDay").val(config.configValue);
                    break;
                case 'fileOverNoticeDay':
                    $("#fileOverNoticeDay").val(config.configValue);
                    break;
                case 'legalDueNoticeDay':
                    $("#legalDueNoticeDay").val(config.configValue);
                    break;
                case 'legalOverNoticeDay':
                    $("#legalOverNoticeDay").val(config.configValue);
                    break;
                case 'operatorDueNoticeDay':
                    $("#operatorDueNoticeDay").val(config.configValue);
                    break;
                case 'operatorOverNoticeDay':
                    $("#operatorOverNoticeDay").val(config.configValue);
                    break;
                //通知提醒到期日开关配置
                case 'resvrConfigEnabled':
                    $("input[name='resvrConfigEnabled']").prop("checked", "");
                    $("input[name='resvrConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'tempConfigEnabled':
                    $("input[name='tempConfigEnabled']").prop("checked", "");
                    $("input[name='tempConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'tempAcctOverConfigEnabled':
                    $("input[name='tempAcctOverConfigEnabled']").prop("checked", "");
                    $("input[name='tempAcctOverConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'fileDueConfigEnabled':
                    $("input[name='fileDueConfigEnabled']").prop("checked", "");
                    $("input[name='fileDueConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'fileOverConfigEnabled':
                    $("input[name='fileOverConfigEnabled']").prop("checked", "");
                    $("input[name='fileOverConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'legalDueConfigEnabled':
                    $("input[name='legalDueConfigEnabled']").prop("checked", "");
                    $("input[name='legalDueConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'legalOverConfigEnabled':
                    $("input[name='legalOverConfigEnabled']").prop("checked", "");
                    $("input[name='legalOverConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'operatorDueConfigEnabled':
                    $("input[name='operatorDueConfigEnabled']").prop("checked", "");
                    $("input[name='operatorDueConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'operatorOverConfigEnabled':
                    $("input[name='operatorOverConfigEnabled']").prop("checked", "");
                    $("input[name='operatorOverConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;

                case 'msgNoticeConfigEnabled':
                    $("input[name='msgNoticeConfigEnabled']").prop("checked", "");
                    $("input[name='msgNoticeConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'fileDueConfigEnabled':
                    $("input[name='fileDueConfigEnabled']").prop("checked", "");
                    $("input[name='fileDueConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'tempAcctMsgNoticeDay':
                    $("#tempAcctMsgNoticeDay").val(config.configValue);
                    break;
                case 'tempAcctMsgOverNoticeDay':
                    $("#tempAcctMsgOverNoticeDay").val(config.configValue);
                    break;
                case 'fileMsgDueNoticeDay':
                    $("#fileMsgDueNoticeDay").val(config.configValue);
                    break;
                case 'fileMsgOverNoticeDay':
                    $("#fileMsgOverNoticeDay").val(config.configValue);
                    break;
                case 'legalMsgDueNoticeDay':
                    $("#legalMsgDueNoticeDay").val(config.configValue);
                    break;
                case 'legalMsgOverNoticeDay':
                    $("#legalMsgOverNoticeDay").val(config.configValue);
                    break;
                case 'operatorMsgDueNoticeDay':
                    $("#operatorMsgDueNoticeDay").val(config.configValue);
                    break;
                case 'operatorMsgOverNoticeDay':
                    $("#operatorMsgOverNoticeDay").val(config.configValue);
                    break;
                case 'breakAppointCustomTime':
                    $("#breakAppointCustomTime").val(config.configValue);
                    break;
                case 'titleName':
                    $("#titleName").val(config.configValue);
                    break;
                case 'coreBatchFileDate':
                    $("#coreBatchFileDate").val(config.configValue);
                    break;
                case 'batchSuspendAutoSyncEnabled':
                    $("input[name='batchSuspendAutoSyncEnabled']").prop("checked", "");
                    $("input[name='batchSuspendAutoSyncEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'saicState':
                    $("input[name='saicStateConfigEnabled']").prop("checked", "");
                    $("input[name='saicStateConfigEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                // case 'syncStatus':
                //     $("#syncStatus").val(config.configValue);
                //     break;
                case 'openSyncStatus':
                    $("#openSyncStatus").val(config.configValue);
                    break;
                case 'changeSyncStatus':
                    $("#changeSyncStatus").val(config.configValue);
                    break;
                case 'suspendSyncStatus':
                    $("#suspendSyncStatus").val(config.configValue);
                    break;
                case 'revokeSyncStatus':
                    $("#revokeSyncStatus").val(config.configValue);
                    break;
                case 'stoppedCancelHezhun':
                    $("input[name='stoppedCancelHezhun']").prop("checked", "");
                    $("input[name='stoppedCancelHezhun'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'syncImageEnabled':
                    $("input[name='syncImageEnabled']").prop("checked", "");
                    $("input[name='syncImageEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'openPriority':
                    $("input[name='openPriority']").prop("checked", "");
                    $("input[name='openPriority'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'syncImageUse':
                    $("input[name='syncImageUse']").prop("checked", "");
                    $("input[name='syncImageUse'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
                case 'uniqueEnabled':
                    $("input[name='uniqueEnabled']").prop("checked", "");
                    $("input[name='uniqueEnabled'][value='"+config.configValue+"']").prop("checked", "checked");
                    break;
            }
        });
        form.render();
    });
};

layui.use(['form'], function () {
    var form = layui.form();

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    for (var i = 0; i < 24; i++) {
        $("#pbcStartHour").append('<option value="' + i + '">' + i + '</option>');
        $("#pbcEndHour").append('<option value="' + i + '">' + i + '</option>');
    }

    for (var i = 0; i < 60; i++) {
        $("#pbcStartMinute").append('<option value="' + i + '">' + i + '</option>');
        $("#pbcEndMinute").append('<option value="' + i + '">' + i + '</option>');
    }

    config.init(form);

    form.render();
    form.on('radio(accountImageMockEnabled)', function(data){
        //被点击的radio的value值collectType
        if(data.value=="true"){
            $("#collectType").show();
            $("#third").hide();
            $("#a").hide();
        }else{
            $("#collectType").hide();
            $("#third").hide();
            $("#a").hide();
        }

    });
    form.on('radio(imageCollect)', function(data){
        //被点击的radio的value值collectType
        if(data.value=="thirdImage"){
            $("#third").show();
            $("#a").hide();
        }else{
            $("#third").hide();
            $("#a").show();
        }
    });

    form.on('submit(savePbcConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "pbcEnabled", configValue: data.field.pbcEnabled});
        postData.push({configKey: "pbcLoginMockEnabled", configValue: data.field.pbcLoginMockEnabled});
        postData.push({configKey: "pbcSyncMockEnabled", configValue: data.field.pbcSyncMockEnabled});
        postData.push({configKey: "tpoAutoSyncEnabled", configValue: data.field.tpoAutoSyncEnabled});
        postData.push({configKey: "idcardEnabled", configValue: data.field.idcardEnabled});
        postData.push({configKey: "idcardLoginEnabled", configValue: data.field.idcardLoginEnabled});
        postData.push({configKey: "batchSuspendAutoSyncEnabled", configValue: data.field.batchSuspendAutoSyncEnabled});
        postData.push({configKey: "syncImageEnabled", configValue: data.field.syncImageEnabled});
        postData.push({configKey: "openPriority", configValue: data.field.openPriority});
        postData.push({configKey: "syncImageUse", configValue: data.field.syncImageUse});
        postData.push({
            configKey: "pbcStartTime",
            configValue: data.field.pbcStartHour + ":" + data.field.pbcStartMinute
        });
        postData.push({configKey: "pbcEndTime", configValue: data.field.pbcEndHour + ":" + data.field.pbcEndMinute});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });



    form.on('submit(saveIdpConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "organid", configValue: data.field.organId});
        postData.push({configKey: "thirdParthPublicKey", configValue: data.field.thirdParthPublicKey});
        postData.push({configKey: "reservationValidationEnabled", configValue: data.field.reservationValidationEnabled});
        postData.push({configKey: "customerDivEnabled", configValue: data.field.customerDivEnabled});
        postData.push({configKey: "legalDivEnabled", configValue: data.field.legalDivEnabled});
        postData.push({configKey: "imageDivEnabled", configValue: data.field.imageDivEnabled});
        postData.push({configKey: "breakAppointCustomDivEnabled", configValue: data.field.breakAppointCustomDivEnabled});
        postData.push({configKey: "breakAppointCustomTime", configValue: data.field.breakAppointCustomTime});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(saveAccount)', function (data) {
        var postData = [];
        //postData.push({configKey: "image", configValue: data.field.imageMockEnabled});
        postData.push({configKey: "acctStatisticsRange", configValue: data.field.acctStatisticsRange});
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    form.on('submit(saveAnnualConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "annualAgainEnabled", configValue: data.field.annualAgainEnabled});
        postData.push({configKey: "annualSaicAgainEnabled", configValue: data.field.annualSaicAgainEnabled});
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(saveAccountImageConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "accountImageMockEnabled", configValue: data.field.accountImageMockEnabled});
        if(data.field.accountImageMockEnabled=="false"){
            postData.push({configKey: "imageCollect", configValue: ''});
            postData.push({configKey: "thirdUrl", configValue: ''});
        }else{
            postData.push({configKey: "imageCollect", configValue: data.field.imageCollect});
            if(data.field.imageCollect=="thirdImage"){
                postData.push({configKey: "thirdUrl", configValue:  data.field.thirdUrl});
            }else{
                postData.push({configKey: "thirdUrl", configValue:  ""});
            }
        }
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    form.on('submit(saveNotice)', function (data) {
        var postData = [];
        postData.push({configKey: "resvrProcessDay", configValue: data.field.resvrProcessDay});
        postData.push({configKey: "tempAcctNoticeDay", configValue: data.field.tempAcctNoticeDay});
        postData.push({configKey: "tempAcctOverNoticeDay", configValue: data.field.tempAcctOverNoticeDay});
        postData.push({configKey: "fileDueNoticeDay", configValue: data.field.fileDueNoticeDay});
        postData.push({configKey: "fileOverNoticeDay", configValue: data.field.fileOverNoticeDay});
        postData.push({configKey: "legalDueNoticeDay", configValue: data.field.legalDueNoticeDay});
        postData.push({configKey: "legalOverNoticeDay", configValue: data.field.legalOverNoticeDay});
        postData.push({configKey: "operatorDueNoticeDay", configValue: data.field.operatorDueNoticeDay});
        postData.push({configKey: "operatorOverNoticeDay", configValue: data.field.operatorOverNoticeDay});
        //通知提醒开关控制
        postData.push({configKey: "resvrConfigEnabled", configValue: data.field.resvrConfigEnabled});
        postData.push({configKey: "tempConfigEnabled", configValue: data.field.tempConfigEnabled});
        postData.push({configKey: "tempAcctOverConfigEnabled", configValue: data.field.tempAcctOverConfigEnabled});
        postData.push({configKey: "fileDueConfigEnabled", configValue: data.field.fileDueConfigEnabled});
        postData.push({configKey: "fileOverConfigEnabled", configValue: data.field.fileOverConfigEnabled});
        postData.push({configKey: "legalDueConfigEnabled", configValue: data.field.legalDueConfigEnabled});
        postData.push({configKey: "legalOverConfigEnabled", configValue: data.field.legalOverConfigEnabled});
        postData.push({configKey: "operatorDueConfigEnabled", configValue: data.field.operatorDueConfigEnabled});
        postData.push({configKey: "operatorOverConfigEnabled", configValue: data.field.operatorOverConfigEnabled});

        postData.push({configKey: "msgNoticeConfigEnabled", configValue: data.field.msgNoticeConfigEnabled});
        postData.push({configKey: "fileDueConfigEnabled", configValue: data.field.fileDueConfigEnabled});
        postData.push({configKey: "tempAcctMsgNoticeDay", configValue: data.field.tempAcctMsgNoticeDay});
        postData.push({configKey: "tempAcctMsgOverNoticeDay", configValue: data.field.tempAcctMsgOverNoticeDay});
        postData.push({configKey: "fileMsgDueNoticeDay", configValue: data.field.fileMsgDueNoticeDay});
        postData.push({configKey: "fileMsgOverNoticeDay", configValue: data.field.fileMsgOverNoticeDay});
        postData.push({configKey: "legalMsgDueNoticeDay", configValue: data.field.legalMsgDueNoticeDay});
        postData.push({configKey: "legalMsgOverNoticeDay", configValue: data.field.legalMsgOverNoticeDay});
        postData.push({configKey: "operatorMsgDueNoticeDay", configValue: data.field.operatorMsgDueNoticeDay});
        postData.push({configKey: "operatorMsgOverNoticeDay", configValue: data.field.operatorMsgOverNoticeDay});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    form.on('submit(saveSystemIP)', function (data) {
        var postData = [];
        postData.push({configKey: "systemIP", configValue: data.field.systemIP});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //初始导入管理
    form.on('submit(saveImportConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "import", configValue: data.field.importConfigEnabled});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //打印管理
    form.on('submit(savePrintConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "printf", configValue: data.field.printConfigEnabled});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //工商网络状态监测管理
    form.on('submit(saicStateConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "saicState", configValue: data.field.saicStateConfigEnabled});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //流水管理
    form.on('submit(saveBillLockConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "billLockConfigEnabled", configValue: data.field.billLockConfigEnabled});
        postData.push({configKey: "uniqueEnabled", configValue: data.field.uniqueEnabled});
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    form.on('submit(saveTitleConfig)', function (data) {
        var postData = [];
        postData.push({configKey: "titleName", configValue: data.field.titleName});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //接口上报配置
    form.on('submit(saveInterfaceSync)', function (data) {
        var postData = [];
        postData.push({configKey: "openSyncStatus", configValue: data.field.openSyncStatus});
        postData.push({configKey: "changeSyncStatus", configValue: data.field.changeSyncStatus});
        postData.push({configKey: "suspendSyncStatus", configValue: data.field.suspendSyncStatus});
        postData.push({configKey: "revokeSyncStatus", configValue: data.field.revokeSyncStatus});

        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //批量文件处理日期
    form.on('submit(batchFileManage)', function (data) {
        var postData = [];
        postData.push({configKey: "coreBatchFileDate", configValue: data.field.coreBatchFileDate});
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //停用取消核准业务
    form.on('submit(savaStoppedCancelHeZhun)', function (data) {
        var postData = [];
        postData.push({configKey: "stoppedCancelHezhun", configValue: data.field.stoppedCancelHezhun});
        $.ajax({
            url: config.baseUrl + '/',
            type: 'post',
            data: JSON.stringify(postData),
            dataType: "json",
            contentType: "application/json",
            success: function () {
                layerTips.msg('保存成功');
            }

        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

});

function openImageTypeManger() {
    parent.tab.tabAdd({
        title: '影像业务配置',
        href: 'image/imageTypeManger.html'
    });
}


