layui.config({
    base: '../js/'
});

layui.use(['form', 'laydate', 'common', 'laytpl', 'imageInfo'], function () {
    var form = layui.form,
        common = layui.common,
        laydate = layui.laydate,
        laytpl = layui.laytpl;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    var id = decodeURI(common.getReqParam("id"));

    //获取账户信息
    $.get('../../allAccountPublic/details?id=' + id, null, function (data) {
        $('#name').text(data.result.acctName);

        var getTpl = $('#' + data.result.acctType + 'Tpl').html();
        laytpl(getTpl).render(data.result, function(html){
            $('#accountInfo').html(html);
        });
        //获取双录视频信息
        var urlStr ="../../video/findByCondition?acctNo="+data.result.acctNo;
        getVideoData(urlStr);
    });

    //获取影像信息
    $.ajax({
        type: "POST",
        url: "../../imageAll/getImageForAccount",
        data: {
            accountId: id
        },
        dataType: "json",
        success: function (res) {
            if (res.code === "ACK") {
                if (res.data.length === 0) {//补录
                    var tabId = parent.tab.getCurrentTabId();//当前选项卡id
                    layer.confirm('该账户未查到相关的影像信息，是否现在补录？', null, function (index) {
                        //获取当前账户的第一个账户的第一笔流水id
                        $.get('../../allBillsPublic/getFirstBillByAccountId?accountId=' + id, null, function (data) {
                            parent.tab.tabAdd({
                                title: '业务影像补录',
                                href: 'imageInfo/billImageEdit.html?id=' + data.result.id + "&billType=" + data.result.billType
                            });
                            layer.close(index);
                            parent.tab.deleteTab(tabId);//关闭当前选项卡
                        });
                    });
                } else {//仅展示
                    layui.imageInfo({
                        elem: '#imageList',
                        data: res.data,
                        downloadUrl: '../../imageAll/downloadImage',
                        downloadZipUrl: '../../imageAll/downloadZip2',
                        editImageUrl: '../../imageAll/editImage',
                        readOnly: true,
                        downloadType: 2
                    });
                }
            }
        },
        error: function () {
            layer.msg("error");
        }
    });
});

function formatAcctType(value) {
    var typeMap = {
        jiben: '基本存款账户',
        yiban: '一般存款账户',
        yusuan: '预算单位专用存款账户',
        feiyusuan: '非预算单位专用存款账户',
        teshu: '特殊单位专用存款账户',
        linshi: '临时机构临时存款账户',
        feilinshi: '非临时机构临时存款账户',
        yanzi: '验资户临时存款账户',
        zengzi: '增资户临时存款账户'
    }
    return typeMap[value] || '-';
}

function formatFileType(value) {
    var typeMap = {
        '01': '01-工商注册号',
        '02': '02-批文',
        '03': '03-登记证书',
        '04': '04-开户证明',
        '06': '06-借款合同',
        '07': '07-其他结算需要的证明',
        '08': '08-财政部门批复书',
        '09': '09-主管部门批文',
        '10': '10-相关部门证明',
        '11': '11-政府部门文件',
        '12': '12-证券从业资格证书',
        '13': '13-国家外汇管理局的批文',
        '14': '14-建筑主管部门核发的许可证',
        '15': '15-施工及安装合同',
        '16': '16-工商行政管理部门的证明',
        '17': '17-其他'
    }
    return typeMap[value] || '-';
}

function formatIdCardType(value) {
    var typeMap = {
        '0': '其他',
        '1': '1-身份证',
        '2': '2-军官证',
        '3': '3-文职干部证',
        '4': '4-警官证',
        '5': '5-士兵证',
        '6': '6-护照',
        '7': '7-港、澳、台居民通行证',
        '8': '8-户口簿',
        '9': '9-其它合法身份证件'
    }
    return typeMap[value] || '-';
}

function formatAccountNameFrom(value) {
    var typeMap = {
        '0': '与存款人名称一致',
        '1': '存款人名称加内设部门',
        '2': '存款人名称加资金性质'
    }
    return typeMap[value] || '-';
}

function formatCapitalProperty(value) {
    var typeMap = {
        '01': '01-基本建设资金',
        '02': '02-更新改造资金',
        '03': '03-财政预算外资金',
        '04': '04-粮、棉、油收购资金',
        '05': '05-证券交易结算资金',
        '06': '06-期货交易保证金',
        '07': '07-金融机构存放同业资金',
        '08': '08-政策性房地产开发资金',
        '09': '09-单位银行卡备用金',
        '10': '10-住房基金',
        '11': '11-社会保障基金',
        '12': '12-收入汇缴资金',
        '13': '13-业务支出资金',
        '14': '14-单位内部的党、团、工会经费',
        '16': '16-其他需要专项管理和使用的资金'
    }
    return typeMap[value] || '-';
}

function formatAcctCreateReason(value) {
    var typeMap = {
        '1': '1-建筑施工及安装',
        '2': '2-从事临时经营活动'
    }
    return typeMap[value] || '-';
}