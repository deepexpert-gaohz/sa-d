layui.use(['form', 'laydate', 'common', 'laytpl', 'imageInfo'], function () {
    var form = layui.form,
        common = layui.common,
        laydate = layui.laydate,
        laytpl = layui.laytpl;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var id = decodeURI(common.getReqParam("id"));
    var billType = decodeURI(common.getReqParam("billType"));

    $.get('../../allBillsPublic/getFormDetails?id=' + id + "&billType=" + billType, null, function (data) {
        $('#name').text(data.result.acctName);
        var billType = getBillTypeName(data.result.billType);
        $('#billInfo').html('<p>流水类型：' + billType + '</p><p>流水时间：' + data.result.createdDate + '</p><p>流水号：' + data.result.billNo + '</p>');
        console.log(data);
        for (var key in data.result) {
            if (data.result[key] != null) {
                $('#' + key).val(data.result[key]);
            }
        }
        $('#acctType').val(getAcctTypeName(data.result.acctType));//账户性质
        $('#acctFileType').val(getAcctFileTypeName(data.result.acctFileType));//开户证明文件种类1
        $('#acctFileType2').val(getAcctFileType2Name(data.result.acctFileType2));

        deleteImageByBillId(id);
        //获取影像信息
        $.ajax({
            type: "POST",
            url: "../../jnnsMovie/getImageByCondition",
            data: {
                id: id,
                acctBillsId: id,
            },
            dataType: "json",
            success: function (res) {
                if (res.code === "ACK") {
                    layui.imageInfo({//展现
                        elem: '#imageList',
                        data: res.data,
                        acctBillsId: id,
                        uploadUrl: '../../jnnsMovie/uploadImage', //上传地址
                        deletUrl: '../../jnnsMovie/deleteImage', //删除地址
                        ploadUrl: '../../imageAll/uploadImage',
                        deleteImageUrl: '../../imageAll/deleteImage',
                        sendImageUrl: '../../imageAll/uploadToImage',
                        downloadUrl: '',
                        downloadZipUrl: '../../imageAll/downloadZip2',
                        editImageUrl: '../../imageAll/editImage',
                        readOnly: true,
                        downloadType: 2

                    });
                }
            },
            error: function () {
                layer.msg("error");
            }
        });
    });
    /**
     * 删除影像
     * @param acctBillsId
     */
    function deleteImageByBillId(acctBillsId) {
        $('#btnSendImage').click(function(){
            layer.confirm('确定删除吗？', {
                btn: ['确定','取消']
            }, function(){
                $.ajax({
                    url: '../../jnnsMovie/deleteImage',
                    type: 'POST',
                    dataType: "json",
                    data: {acctBillsId: acctBillsId},
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layer.msg("删除成功");
                            //2秒后触发
                            setTimeout(function(){
                                window.location.reload();
                            },2000);
                        } else {
                            layer.msg(data.message);
                        }
                    }
                });
            });
        });
    }
});