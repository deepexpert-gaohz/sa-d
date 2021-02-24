/**
 * 客户信息管理详情 和 客户异动管理详情 公共js
 */

//严重违法信息
function setYzwf(saicInfoId, callBack) {
    $.get('../../kyc/illegals?saicInfoId=' + saicInfoId, function (data) {
        if (data && !jQuery.isEmptyObject(data)) {
            var dataList = data.result;
            if (dataList && dataList.length > 0) {
                var innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>类别</th><th>列入严重违法失信企业名单（黑名单）原因</th><th>列入日期</th><th>做出决定机关(列入)</th><th>移出严重违法失信企业名单(黑名单)原因</th>' +
                    '<th>移出日期</th><th>作出决定机关(移出)</th></tr> </thead> <tbody>';
                for (var i = 0; i < dataList.length; i++) {
                    innerHtml += '<tr ><td>' + tranlate(dataList[i].type) + '</td>' +
                        '<td>' + tranlate(dataList[i].reason) + '</td>' +
                        '<td>' + tranlate(dataList[i].date) + '</td>' +
                        '<td>' + tranlate(dataList[i].organ) + '</td>' +
                        '<td>' + tranlate(dataList[i].reasonout) + '</td>' +
                        '<td>' + tranlate(dataList[i].dateout) + '</td>' +
                        '<td>' + tranlate(dataList[i].organout) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
                callBack(innerHtml);
                // $('.break').html(innerHtml);
            } else {
                callBack();
                // if (flag) {
                //     $('.break').html('<span>暂无相关信息</span>');
                // } else {
                //     $("#illegal").hide();
                // }
            }
        }
    });
}

//经营异常信息
function setJyyc(saicInfoId, callBack) {
    $.get('../../kyc/changemess?saicInfoId=' + saicInfoId, function (data) {
        if (data && !jQuery.isEmptyObject(data)) {
            var dataList = data.result;
            if (dataList && dataList.length > 0) {
                var innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>列入经营异常名录原因</th><th>列入日期</th><th>移出经营异常名录原因</th><th>移出日期</th><th>作出决定机关</th>' +
                    '</tr> </thead> <tbody>';
                for (var i = 0; i < dataList.length; i++) {
                    innerHtml += '<tr ><td>' + dataList[i].inreason + '</td>' +
                        '<td>' + tranlate(dataList[i].indate) + '</td>' +
                        '<td>' + tranlate(dataList[i].outreason) + '</td>' +
                        '<td>' + tranlate(dataList[i].outdate) + '</td>' +
                        '<td>' + tranlate(dataList[i].belongorg) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
                callBack(innerHtml);
                // $('.abnormal').html(innerHtml);
            } else {
                callBack();
                // if (flag) {
                //     $('.abnormal').html('<span>暂无相关信息</span>');
                // } else {
                //     $("#changeMess").hide();
                // }
            }
        }
    });
}

//营业到期信息
function setYydq(result, organName, abnormalTime, callBack) {
    if (result.enddate.length === 11) {
        var endDate = new Date(parseInt(result.enddate.substring(0, 4)), parseInt(result.enddate.substring(5, 7)) - 1, parseInt(result.enddate.substring(8, 10)));
        // var endDate = new Date(2019, parseInt(result.enddate.substring(5, 7)) - 1, parseInt(result.enddate.substring(8, 10)));
        if (new Date() > endDate) {
            var innerHtml = '<table class="table table-bordered"> ' +
                '<thead> <tr><th>客户名称</th><th>所属银行名称</th><th>系统异动日期</th><th>营业期限</th></tr> </thead> ' +
                '<tbody>' +
                '<tr><td>' + result.name + '</td><td>' + organName + '</td><td>' + abnormalTime + '</td><td>' + result.startdate + ' - ' + result.enddate + '</td></tr>' +
                '</tbody>' +
                '</table>';
            callBack(innerHtml);
            // $('.businessDate').html(innerHtml);
        } else {
            callBack();
            // if (flag) {
            //     $('.businessDate').html('<span>暂无相关信息</span>');
            // } else {
            //     $("#businessExpires").hide();
            // }
        }
    }
}

//工商状态异常
function setGszt(result, organName, abnormalTime, callBack) {
    if (result.state) {
        $.get('../../kyc/getSaicStateForState?state=' + encodeURI(result.state), function (saicState) {
            if (saicState !== '续存' && saicState !== '在业') {
                var innerHtml = '<table class="table table-bordered"> ' +
                    '<thead> <tr><th>客户名称</th><th>所属银行名称</th><th>系统异动日期</th><th>工商状态</th></tr> </thead> ' +
                    '<tbody>' +
                    '<tr><td>' + result.name + '</td><td>' + organName + '</td><td>' + abnormalTime + '</td><td>' + saicState + '</td></tr>' +
                    '</tbody>' +
                    '</table>';
                callBack(innerHtml);
                // $('.saicType').html(innerHtml);
            } else {
                callBack();
                // if (flag) {
                //     $('.saicType').html('<span>暂无相关信息</span>');
                // } else {
                //     $("#abnormalState").hide();
                // }
            }
        });
    }
}

//登记信息异动
function setDjxx(result, compareResultId, organName, abnormalTime, callBack) {
    $.get('../../kyc/registerAbnormal?compareResultId=' + compareResultId, function (data) {
        if (data && !jQuery.isEmptyObject(data)) {
            var dataList = data.result;
            if (dataList && dataList.length > 0) {
                var innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>客户名称</th><th>所属银行名称</th><th>系统异动日期</th><th>异动内容</th><th>异动前内容</th><th>异动后内容</th>' +
                    '</tr> </thead> <tbody>';
                for (var i = 0; i < dataList.length; i++) {
                    innerHtml += '<tr ><td>' + result.name + '</td>' +
                        '<td>' + organName + '</td>' +
                        '<td>' + abnormalTime + '</td>' +
                        '<td>' + tranlate(dataList[i].name) + '</td>' +
                        '<td>' + tranlate(dataList[i].before) + '</td>' +
                        '<td>' + tranlate(dataList[i].after) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
                callBack(innerHtml);
                // $('.registerAbnormal').html(innerHtml);
            } else {
                callBack();
                // if (flag) {
                //     $('.registerAbnormal').html('<span>暂无相关信息</span>');
                // } else {
                //     $("#changed").hide();
                // }
            }
        }
    });
}

//导出异动信息
function exportAbnormalExcel(compareResultSaicCheckId, compareResultId, saicInfoId) {
    if (!compareResultSaicCheckId || !compareResultId) {
        alert('比对异动结果未生成，无法导出异动信息！');
        return;
    }
    var params = {
        compareResultSaicCheckId: compareResultSaicCheckId,
        compareResultId: compareResultId,
        saicInfoId: saicInfoId
    };
    postExcelFile(params, '../../customerAbnormal/exportAbnormalExcel');
}

function tranlate(value) {
    if (value) {
        return value;
    } else {
        return '';
    }
}