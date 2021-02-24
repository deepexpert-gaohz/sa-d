layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['common'], function () {
    var common=layui.common;
    var laytpl = layui.laytpl;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    var compareResultSaicCheckId = decodeURI(common.getReqParam("compareResultSaicCheckId"));
    var compareTaskId = decodeURI(common.getReqParam("compareTaskId"));
    var saicInfoId = decodeURI(common.getReqParam("saicInfoId"));
    var compareResultId = decodeURI(common.getReqParam("compareResultId"));
    var depositorName = decodeURI(common.getReqParam("depositorName"));
    var abnormalTime = decodeURI(common.getReqParam("abnormalTime"));
    var createdDate = decodeURI(common.getReqParam("createdDate"));
    var organName = decodeURI(common.getReqParam("organName"));
    var tabType = decodeURI(common.getReqParam("tabType"));

    $("#name").html(depositorName);
    $("#createTimeSpan").html(createdDate);
    $("#createOrgSpan").html(organName);

    //进入页面默认切换到指定Tab页
    switch (tabType) {
        case 'illegal':
            $('#illegalTab').click();
            break;
        case 'changeMess':
            $('#changeMessTab').click();
            break;
        case 'businessExpires':
            $('#businessExpiresTab').click();
            break;
        case 'abnormalState':
            $('#abnormalStateTab').click();
            break;
        case 'changed':
            $('#changedTab').click();
            break;
        default:
    }
    //风险异动信息
    if (saicInfoId) {
        $.get('../../kyc/history/basic?saicInfoId=' + saicInfoId).done(function (response) {
            var result = response.result;
            if (result) {
                //严重违法信息
                setYzwf(saicInfoId, function (innerHtml) {
                    if (innerHtml) {
                        $('.break').html(innerHtml);
                    } else {
                        $('.break').html('<span>暂无相关信息</span>');
                    }
                });
                //经营异常信息
                setJyyc(saicInfoId, function (innerHtml) {
                    if (innerHtml) {
                        $('.abnormal').html(innerHtml);
                    } else {
                        $('.abnormal').html('<span>暂无相关信息</span>');
                    }
                });
                //营业到期信息
                setYydq(result, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.businessDate').html(innerHtml);
                    } else {
                        $('.businessDate').html('<span>暂无相关信息</span>');
                    }
                });
                //工商状态异常
                setGszt(result, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.saicType').html(innerHtml);
                    } else {
                        $('.saicType').html('<span>暂无相关信息</span>');
                    }
                });
                //登记信息异动
                setDjxx(result, compareResultId, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.registerAbnormal').html(innerHtml);
                    } else {
                        $('.registerAbnormal').html('<span>暂无相关信息</span>');
                    }
                });
            }
        });
    }

    //导出异动信息
    $('#exportAbnormalExcel').click(function () {
        exportAbnormalExcel(compareResultSaicCheckId, compareResultId, saicInfoId);
    });

    //信息变更
    $('#dataChange').click(function () {
        $.get('../../allAccountPublic/findAccountByDepositorName?depositorName=' + depositorName, function (response) {
            if (response.rel) {
                var account = response.result;
                if (account == null) {
                    layerTips.alert("未查询到该客户基本户信息");
                } else {//进入该客户基本户变更页面
                    $.get('../../allAccountPublic/details?id=' + account.id, function (data) {//获取该账户最新流水信息
                        parent.tab.tabAdd({
                                title: '校验管理',
                                href: "validate/detail.html?type=" + data.result.acctType + '&buttonType=ACCT_CHANGE&depositorName=' + data.result.depositorName + "&billId=" + data.result.id + "&acctType=" + data.result.acctType
                            }
                        );
                    });
                }
            } else {
                layerTips.alert(response.msg);
            }
        });
    });

});