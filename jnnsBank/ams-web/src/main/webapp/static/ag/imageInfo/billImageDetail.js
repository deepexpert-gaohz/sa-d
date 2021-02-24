layui.config({
    base: '../js/'
});

layui.use(['form', 'laydate', 'common', 'imageInfo'], function () {
    var form = layui.form,
        common = layui.common,
        laydate = layui.laydate;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    var id = decodeURI(common.getReqParam("id"));
    var billType = decodeURI(common.getReqParam("billType"));

    $.get('../../allBillsPublic/getFormDetails?id=' + id + "&billType=" + billType, null, function (data) {
        $('#name').text(data.result.acctName);
        var billType = getBillTypeName(data.result.billType);
        $('#billInfo').html('<p>流水类型：' + billType + '</p><p>流水时间：' + data.result.createdDate + '</p><p>流水号：' + data.result.billNo + '</p>');
        for (var key in data.result) {
            if (data.result[key] != null) {
                $('#' + key).val(data.result[key]);
            }
        }
        $('#acctType').val(getAcctTypeName(data.result.acctType));//账户性质
        $('#acctFileType').val(getAcctFileTypeName(data.result.acctFileType));//开户证明文件种类1
        $('#acctFileType2').val(getAcctFileType2Name(data.result.acctFileType2));
        //获取双录视频信息
        var urlStr ="../../video/findByBillId?billId="+id;
        getVideoData(urlStr);
        //获取影像信息
        $.ajax({
            type: "POST",
            url: "../../imageAll/getImageForBill",
            data: {
                billId: id
            },
            dataType: "json",
            async: false,
            success: function (res) {
                if (res.code === "ACK") {
                    if (res.data.length === 0) {
                        // 没有影像信息
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

});
