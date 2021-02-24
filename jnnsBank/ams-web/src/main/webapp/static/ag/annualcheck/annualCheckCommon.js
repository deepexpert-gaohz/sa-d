function abnormalConvert(data) {
    if(data.indexOf("INIT") != -1) {
        data = data.replace("INIT", "未采集");
    }
    if(data.indexOf("NOT_FOUND") != -1) {
        data = data.replace("NOT_FOUND", "未找到");
    }
    if(data.indexOf("CANCEL") != -1) {
        data = data.replace("CANCEL", "注销撤销");
    }
    if(data.indexOf("REVOKE") != -1) {
        data = data.replace("REVOKE", "吊销");
    }
    if(data.indexOf("ABNORMAL_OPERATION") != -1) {
        data = data.replace("ABNORMAL_OPERATION", "经营异常");
    }
    if(data.indexOf("NO_ANNUAL_REPORT") != -1) {
        data = data.replace("NO_ANNUAL_REPORT", "无年报");
    }
    if(data.indexOf("BUSINESS_LICENSE_EXPIRED") != -1) {
        data = data.replace("BUSINESS_LICENSE_EXPIRED", "营业执照到期");
    }
    if(data.indexOf("BLACK") != -1) {
        data = data.replace("BLACK", "黑名单");
    }
    if(data.indexOf("ILLEGAL") != -1) {
        data = data.replace("ILLEGAL", "严重违法");
    }

    return data;
}

var saicDataFormat = function(data){
    if (!data.unitycreditcode) {
        data.unitycreditcode = data.registNo;
    } else {
        data.orgcode = data.unitycreditcode.substring(8, 17);
    }
    return data;
}