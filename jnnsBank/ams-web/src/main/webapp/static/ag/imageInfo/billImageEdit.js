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
    var imgaeSyncStatus = decodeURI(common.getReqParam("imgaeSyncStatus"));
    var syncImageList = decodeURI(common.getReqParam("syncImageList"));
    var acctType;
    var depositorTypeCode;
    if (syncImageList==="syncImageList" && imgaeSyncStatus != 'tongBuChengGong'){
        $.get("../../config/findByKey?configKey=syncImageUse", null, function (data) {
            if (data == true) {
                $('#syncDiv').show();
            }
        });
    }

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
        $('#acctTypeVal').val(data.result.acctType);//账户性质
        $('#acctFileType').val(getAcctFileTypeName(data.result.acctFileType));//开户证明文件种类1
        $('#acctFileType2').val(getAcctFileType2Name(data.result.acctFileType2));
        acctType = data.result.acctType;
        depositorTypeCode = data.result.depositorType;
        if(data.result.billType=='ACCT_REVOKE'){
            depositorTypeCode=getDepositorTypeValue(data.result.depositorType);
        }
        //获取双录视频信息
        var urlStr ="../../video/findByBillId?billId="+id;
        getVideoData(urlStr,true);
        runImageData(id);
    });


    /**
     * 账户影像运行
     * @param acctBillsId
     */
    function runImageData(acctBillsId) {

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

        getImageData(function (data) {
            var imageInfo = layui.imageInfo({
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
                downloadType: 2,
                isSend: true,
                handleUpload: function (id) {
                    //上传
                    console.log(id);

                    layer.msg('已上传');
                    getImageData(function (list) {
                        imageInfo.refresh({data: list.data, acctId: list.acctId});
                    }, imageInfo, acctBillsId);
                },
                handleEdit: function (data) {
                    //编辑
                    console.log(data);

                    layer.msg('已保存');
                    getImageData(function (list) {
                        imageInfo.refresh({data: list.data, acctId: list.acctId});
                    }, imageInfo, acctBillsId);
                },
                handleDelete: function (id) {
                    //删除
                    console.log(id);

                    layer.msg('已删除');
                    getImageData(function (list) {
                        imageInfo.refresh({data: list.data, acctId: list.acctId});
                    }, imageInfo, acctBillsId);
                },
                handleSend: function(id) {
                    //传送影像平台
                    console.log(id);

                    layer.msg('已传送影像平台');
                    getImageData(function(list){
                        imageInfo.refresh({data: list.data,acctId:list.acctId});
                    },imageInfo,acctBillsId);
                },
                handleBatchDelete:function (id) {
                    console.log("批量删除："+id);
                    getImageData(function(list){
                        imageInfo.refresh({data: list.data,acctId:list.acctId});
                    },imageInfo,acctBillsId);
                },
                handleBatchMove:function (id) {
                    console.log("批量移动："+id);
                    getImageData(function(list){
                        imageInfo.refresh({data: list.data,acctId:list.acctId});
                    },imageInfo,acctBillsId);
                }
            });

        }, undefined, acctBillsId);
    }

    /**
     * 账户影像获得详情
     * @param callback
     * @param imageInfo
     * @param acctBillsId
     */
    function getImageData(callback, imageInfo, acctBillsId) {
        var tempId;
        if (imageInfo === undefined) {
            tempId = "";
        } else {
            tempId = imageInfo.config.tempId;
        }
        $.ajax({
            type: "POST",
            url: "../../imageAll/queryByBillsId",
            data: {
                acctBillsId: acctBillsId,
                acctType: acctType,
                operateType: billType,
                depositorTypeCode: depositorTypeCode,
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
    //影像上报
    $('#btn_syncImage').on('click', function () {
        if(imgaeSyncStatus==='tongBuChengGong'){
            layer.msg("已上报，无需再次上报");
            return false;
        }
        $.ajax({
            type: "GET",
            url: "../../imageAll/sync",
            data: {
                acctBillsId: id,
            },
            dataType: "json",
            async: false,
            success: function (res) {
                if (res.code === "ACK") {
                    layer.msg("上报成功");
                }else{
                    layer.msg(res.message);
                }
            },
            error: function () {
                layer.msg("error");
            }
        });
    });
});
