var kyc = {
    baseUrl: "../../kyc/history",
};

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

function p(s) {
    return s < 10 ? '0' + s: s;
}

var myDate = new Date();
var year = myDate.getFullYear();
var month = myDate.getMonth() + 1;
var day = myDate.getDate();
var time = year + '-' + p(month) + '-' + p(day) + '  ' + p(myDate.getHours()) + ':' + p(myDate.getMinutes()) + ':' + p(myDate.getSeconds());
// $(document).ready(function () {
//     var myDate = new Date();
//     var time = myDate.getFullYear() + '/' + (+myDate.getMonth() + 1) + '/' + myDate.getDate() + '  ' + myDate.getHours() + ':' + myDate.getMinutes() + ':' + myDate.getSeconds();
//     $('#printtime').html(time);
// })

var saicId;
layui.use(['form','common'], function () {
    var $ = layui.jquery,
        common = layui.common,form = layui.form;
    var saicInfoId = decodeURI(common.getReqParam("saicId"));
    var jiben = decodeURI(common.getReqParam("jiben"));
    var guquan = decodeURI(common.getReqParam("guquan"));
    var shouyi = decodeURI(common.getReqParam("shouyi"));
    // var kongzhiren = decodeURI(common.getReqParam("kongzhiren"));
    var djg = decodeURI(common.getReqParam("djg"));
    var gudong = decodeURI(common.getReqParam("gudong"));
    var biangeng = decodeURI(common.getReqParam("biangeng"));
    // var jibenlvli = decodeURI(common.getReqParam("jibenlvli"));
    var yichang = decodeURI(common.getReqParam("yichang"));
    var weifa = decodeURI(common.getReqParam("weifa"));
    var report = decodeURI(common.getReqParam("report"));
    var querydate = decodeURI(common.getReqParam("querydate"));
    saicId = saicInfoId;
    //基本信息
    if(jiben == true || jiben == 'true'){
        $.get(kyc.baseUrl + '/basic?saicInfoId=' + saicInfoId).done(function (response) {
            var data = response.result;
            if(querydate == ''){
                $('#printDate').html(time);
            }else{
                $('#printDate').html(querydate);
            }

            //企业名称
            $('#depositorName').html(data.name);
            //社会代码证
            if(data.unitycreditcode){
                $('#regNo').html(data.unitycreditcode);
            }else {
                $('#regNo').html(data.registno);
            }
            //企业名称
            $('#name').html(data.name);
            $('#type').html(data.type);
            $('#opendate').html(data.opendate);
            $('#legalperson').html(data.legalperson);
            $('#registorgan').html(data.registorgan);
            $('#state').html(data.state);
            $('#address').html(data.address);
            $('#scope').html(data.scope);
            $('#licensedate').html(data.licensedate);
            $('#registfund').html(data.registfund);
            $('#enddate').html(data.enddate);
        });
    }else {
        $('#kyc-jiben').hide();
    }
    //变更
    if(biangeng == true || biangeng == 'true'){
        var biangengHtml = '';
        $.get(kyc.baseUrl + '/changes?saicInfoId=' + saicInfoId).done(function (data) {
            if (data.code === 'ACK') {
                var result = data.data;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        biangengHtml += '<tr >' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + tranlate(result[i].type) + '</td>' +
                            '<td>' + tranlate(result[i].beforecontent) + '</td>' +
                            '<td>' + tranlate(result[i].aftercontent) + '</td>' +
                            '<td>' + tranlate(result[i].changedate) + '</td>' +
                            '</tr>';
                    }
                }else{
                    biangengHtml = '<span>暂无相关信息</span>';
                }
            }else{
                biangengHtml = '<span>暂无相关信息</span>';
            }
            $('.biangeng').html(biangengHtml);
        });
    }else {
        $('#kyc-biangeng').hide();
    }
    //受益人
    if(shouyi == true || shouyi == 'true'){
        var shouyiHtml = '';
        $.get(kyc.baseUrl + '/beneficiary?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result && data.result.length > 0) {
                var result = data.result;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        shouyiHtml += '<tr >' +
                            '<td>' + tranlate(result[i].name) + '</td>' +
                            '<td>' + tranlate(result[i].type) + '</td>' +
                            '<td>' + tranlate(calculatePercent(result[i].capitalpercent)) + '</td>' +
                            '<td>' + tranlate(result[i].identifytype) + '</td>' +
                            '<td>' + tranlate(result[i].identifyno) + '</td>' +
                            '</tr>';
                    }
                }else{
                    shouyiHtml = '<span>暂无相关信息</span>';
                }
            }else{
                shouyiHtml = '<span>暂无相关信息</span>';
            }
            $('.shouyi').html(shouyiHtml);
        });
    }else {
        $('#kyc-shouyi').hide();
    }

    //董监高
    if(djg == true || djg == 'true'){
        var djgHtml = '';
        $.get(kyc.baseUrl + '/managers?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result.directorList;
                var managementList = data.result.managementList;
                var superviseList = data.result.superviseList;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        djgHtml += '<tr >' +
                            '<td>' + tranlate(result[i].name) + '</td>' +
                            '<td>' + tranlate(result[i].sex) + '</td>' +
                            '<td>' + tranlate(result[i].position) + '</td>' +
                            '</tr>';
                    }
                }

                if(managementList.length > 0){
                    for(var i = 0 ; i < managementList.length; i ++){
                        djgHtml += '<tr >' +
                            '<td>' + tranlate(managementList[i].name) + '</td>' +
                            '<td>' + tranlate(managementList[i].sex) + '</td>' +
                            '<td>' + tranlate(managementList[i].position) + '</td>' +
                            '</tr>';
                    }
                }

                if(superviseList.length > 0){
                    for(var i = 0 ; i < superviseList.length; i ++){
                        djgHtml += '<tr >' +
                            '<td>' + tranlate(superviseList[i].name) + '</td>' +
                            '<td>' + tranlate(superviseList[i].sex) + '</td>' +
                            '<td>' + tranlate(superviseList[i].position) + '</td>' +
                            '</tr>';
                    }
                }
            }else{
                djgHtml = '<span>暂无相关信息</span>';
            }
            $('.djg').html(djgHtml);
        });
    }else {
        $('#kyc-djg').hide();
    }

    //股东出资信息
    if(gudong == true || gudong == 'true'){
        var gudongHtml = '';
        $.get(kyc.baseUrl + '/stockholders?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        gudongHtml += '<tr >' +
                            '<td>' + tranlate(result[i].name) + '</td>' +
                            '<td>' + tranlate(result[i].strtype) + '</td>' +
                            '<td>' + tranlate(result[i].subconam) + '</td>' +
                            '<td>' + tranlate(result[i].condate) + '</td>' +
                            '<td>' + tranlate(result[i].fundedratio) + '</td>' +
                            '</tr>';
                    }
                }else{
                    gudongHtml = '<span>暂无相关信息</span>';
                }
            }else{
                gudongHtml = '<span>暂无相关信息</span>';
            }
            $('.gudong').html(gudongHtml);
        });
    }else {
        $('#kyc-gudong').hide();
    }

    //工商年报
    if(report == true || report == 'true'){
        var reportHtml = '';
        $.get(kyc.baseUrl + '/report?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        reportHtml += '<tr >' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + tranlate(result[i].annualreport) + '</td>' +
                            '<td>' + tranlate(result[i].releasedate) + '</td>' +
                            '</tr>';
                    }
                }else{
                    reportHtml = '<span>暂无相关信息</span>';
                }
            }else{
                reportHtml = '<span>暂无相关信息</span>';
            }
            $('.report').html(reportHtml);
        });
    }else {
        $('#kyc-report').hide();
    }

    if(yichang == 'true' || yichang == true){
        var yichangHtml = '';
        $.get(kyc.baseUrl + '/changemess?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        yichangHtml += '<tr >' +
                            '<td>' + tranlate(result[i].inreason) + '</td>' +
                            '<td>' + tranlate(result[i].indate) + '</td>' +
                            '<td>' + tranlate(result[i].outreason) + '</td>' +
                            '<td>' + tranlate(result[i].outdate) + '</td>' +
                            '<td>' + tranlate(result[i].outorgan) + '</td>' +
                            '</tr>';
                    }
                }else{
                    yichangHtml = '<span>暂无相关信息</span>';
                }
            }else{
                yichangHtml = '<span>暂无相关信息</span>';
            }
            $('.yichang').html(yichangHtml);
        });
    }else {
        $('#kyc-yichang').hide();
    }
    //违法信息
    if(weifa == 'true' || weifa == true){
        var weifaHtml = '';
        $.get(kyc.baseUrl + '/illegals?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        weifaHtml += '<tr >' +
                            '<td>' + tranlate(result[i].type) + '</td>' +
                            '<td>' + tranlate(result[i].reason) + '</td>' +
                            '<td>' + tranlate(result[i].date) + '</td>' +
                            '<td>' + tranlate(result[i].organ) + '</td>' +
                            '<td>' + tranlate(result[i].reasonout) + '</td>' +
                            '<td>' + tranlate(result[i].dateout) + '</td>' +
                            '<td>' + tranlate(result[i].organout) + '</td>' +
                            '</tr>';
                    }
                }else{
                    weifaHtml = '<span>暂无相关信息</span>';
                }
            }else{
                weifaHtml = '<span>暂无相关信息</span>';
            }
            $('.weifa').html(weifaHtml);
        });
    }else {
        $('#kyc-weifa').hide();
    }
    if(guquan == 'true' || guquan == true){
        var guquanHtml = '';
        $.get(kyc.baseUrl + '/equityshare?saicInfoId=' + saicInfoId).done(function (data) {
            if (data && data.result) {
                var result = data.result.children;
                if(result.length > 0){
                    for(var i = 0 ; i < result.length; i ++){
                        var text = result[i].text;
                        text = text.split("--");
                        var bili = text[0];
                        bili = bili.replace("股权比例-[","");
                        bili = bili.replace("]","");
                        guquanHtml += '<tr >' +
                            '<td>' + tranlate(text[1]) + '</td>' +
                            '<td>' + tranlate(bili) + '</td>' +
                            '</tr>';
                    }
                }else{
                    guquanHtml = '<span>暂无相关信息</span>';
                }
            }else{
                guquanHtml = '<span>暂无相关信息</span>';
            }
            $('.guquan').html(guquanHtml);
        });
    }else {
        $('#kyc-guquan').hide();
    }

});

function tranlate(value) {
    if (value) {
        return value;
    } else {
        return '';
    }
}

function calculatePercent(capitalPercent) {
    if(capitalPercent.indexOf('E')>0 || capitalPercent === '0.0'){
        return "占比太小";
    } else {
        return accMul(capitalPercent,100)+'%';
    }
}

function accMul(arg1,arg2) {
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
