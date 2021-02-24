layui.config({
    base: '../js/'
});

layui.use(['form', 'laydate', 'common', 'imageInfo'], function () {
    var form = layui.form,
        common = layui.common,
        laydate = layui.laydate;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    var id = decodeURI(common.getReqParam("id"));
    var customerNo = decodeURI(common.getReqParam("customerNo"));

    //获取客户信息
    $.get('../../customerPublic/find?customerId=' + id, null, function (data) {
        $('#name').text(data.depositorName);
        for (var key in data) {
            if (data[key] != null) {
                $('#' + key).val(data[key]);
            }
        }
        $('#fileType').val(getFileTypeName(data.fileType));
        var orgType = getOrgTypeName(data.orgType);
        var orgTypeDetail = getOrgTypeDetailName(data.orgTypeDetail);
        var orgTypeStr = "";
        if (orgType !== "" && orgTypeDetail !== "") {
            orgTypeStr = orgType + " > " + orgTypeDetail;
        } else {
            orgTypeStr = orgType + orgTypeDetail;
        }
        $('#orgType').val(orgTypeStr);
        //获取双录视频信息
        var urlStr ="../../video/findByCondition?depositorName="+data.depositorName;
        getVideoData(urlStr);
        form.render('select');
    });

    //获取影像信息
    if (id==null) {
        layerTips.msg('客户ID为空，不能获取影像信息！');
    } else {
        $.ajax({
            type: "POST",
            url: "../../imageAll/getImageForCustomerId",
            data: {
                customerId: id
            },
            dataType: "json",
            async: false,
            success: function (res) {
                if (res.code === "ACK") {
                    if (res.data.length === 0) {//补录
                        layer.confirm('该账户未查到营业执照和法人身份证的影像信息，是否现在补录？', null, function (index) {
                            var tabId = parent.tab.getCurrentTabId();//当前选项卡id
                            //获取当前客户的第一个账户的第一笔流水id
                            $.get('../../allAccountPublic/getFirstBillByCustomerId?customerId=' + id, null, function (data) {
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
    }

});
