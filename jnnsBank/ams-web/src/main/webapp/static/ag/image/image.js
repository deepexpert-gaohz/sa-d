layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var isHardWare ="false";
var billId;
var getImageType;
var isThirdImage;
layui.use(['form','murl','saic','account','upload', 'picker', 'linkSelect', 'org', 'industry','common', 'laydate'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        laydate = layui.laydate,
        upload = layui.upload;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var billType = $('#billType').val();
    var type = url.get("type");
    billId = url.get("refBillId");
    if(billId == ""){
        billId = url.get("billId");
    }
    var buttonType = url.get("buttonType");
    recId = url.get("recId");
    var depositorType = "";
    if (type==undefined || type=="undefined" || type=="") {
        type = $('#acctType').val();
    }
    $.ajaxSettings.async = false;
    $.get("../../imageType/getConfig",function (res){
        if(res.data.length>0){

            if(res.code === 'ACK') {
                for (var i = 0; i < res.data.length; i++){
                    if(res.data[i].configKey=="imageCollect"){
                        if(res.data[i].configValue=="hardWareImage" && buttonType != 'selectForChangeBtn'){
                            isHardWare = "true";
                            $("#tab_image").css("display","");
                            $("#imageInfo").show();
                        }else if(res.data[i].configValue=="thirdImage" && buttonType != 'selectForChangeBtn'){
                            isThirdImage = true;
                            $("#tab_thirdImage").css("display","");
                            $("#thirdImage").show();
                        }else{
                            $("#tab_image").css("display","none");
                            $("#imageInfo").hide();
                            $("#tab_thirdImage").css("display","none");
                            $("#thirdImage").hide();
                        }
                    }
                }

            }else{
                $("#tab_image").css("display","none");
                $("#imageInfo").hide();
                $("#tab_thirdImage").css("display","none");
                $("#thirdImage").hide();
            }
        }else{
            $("#tab_image").css("display","none");
            $("#imageInfo").hide();
            $("#tab_thirdImage").css("display","none");
            $("#thirdImage").hide();
        }
    });
    if(isThirdImage){
        $.get("../../imageType/getThirdUrl",function (res){
            if(res.data.length>0){
                if(res.code === 'ACK') {
                    for (var i = 0; i < res.data.length; i++){
                        if(res.data[i].configKey=="thirdUrl"){
                            $('#thirdUrl').attr('src',res.data[i].configValue);
                        }
                    }
                }else{
                    layerTips.msg('url读取异常');
                }
            }else{
                layerTips.msg('url读取异常');
            }
        });
    }
    $.get('../image/imageManger.html', function (form) {
        $("#imageDiv").html(form);
    });
    $.get('../image/edit.html', function (form) {
        $("#paperinfoDiv").html(form);
    });
    $.get('../image/add.html', function (form) {
        $("#imageinfoDiv").html(form);
    });
    form.render('select');

    laydate.render({
        elem: '#expireDateStr',
        format: 'yyyy-MM-dd'
    });
    
    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    if(billId){
        $.get("../../allBillsPublic/findOne/"+ billId, null,function (data) {

            billType = data.billType;
            type = data.acctType;
            depositorType = data.depositorType;
        });
    }else{
        billId = recId;
    }
    if(depositorType){
        if(isHardWare=="true"){
            $.get("../../imageType/getImageType?acctType="+type+"&operateType="+billType+"&depositorTypeCode="+depositorType,function (res) {
                if(res.code === 'ACK') {
                    $('#docCode').empty();
                    $('#docCodeLi').empty();
                    $('#docCode').append("<option value=\"\">请选择</option>");
                    $('#docCodeLi').append('<li role="presentation"><a role="menuitem2" tabindex="-1" href="javascript:void(0);" onclick ="image_Query()">所有影像</a></li>');
                    for (var i = 0; i < res.data.length; i++){
                        $('#docCode').append('<option value="' + res.data[i].value + '" >' + res.data[i].imageName + '</option>');
                        var temp = res.data[i].imageName;
                        var tempValue = res.data[i].value;
                        $('#docCodeLi').append('<li role="presentation"><a role="menuitem2" tabindex="-1" href="javascript:void(0);" onclick ="image_Query('+tempValue+')">'+temp+'</a></li>');
                        form.render('select');
                    }
                }
            });
        }
    }
    $.ajaxSettings.async = true;
    if(type!="jiben"){
        form.on('select(depositorType)', function(data){
            getImageType(data.value);
        });
    }
    /*form.on('select(depositorType)', function(data){
        if(type=="jiben"){
            if(data.value == "14"){
                addValidate("legalName",{equalTo:"#depositorName"},true,validator);
            }else{
                removeValidate("legalName",false,validator,"equalTo");
            }
        }
        getImageType(data.value);
    });*/

    /*$("#depositorType").bind("change",function(){
        alert("选择了");
    });*/

    getImageType = function(depositorTypeCode){
        if(isHardWare=="true"){
            billType=$('#billType').val();
            $.get("../../imageType/getImageType?acctType="+type+"&operateType="+billType+"&depositorTypeCode="+depositorTypeCode,function (res) {
                if(res.code === 'ACK') {
                    $('#docCode').empty();
                    $('#docCodeLi').empty();
                    $('#docCode').append("<option value=\"\">请选择</option>");
                    $('#docCodeLi').append('<li role="presentation"><a role="menuitem2" tabindex="-1" href="javascript:void(0);" onclick ="image_Query()">所有影像</a></li>');
                    for (var i = 0; i < res.data.length; i++){
                        $('#docCode').append('<option value="' + res.data[i].value + '" >' + res.data[i].imageName + '</option>');
                        var temp = res.data[i].imageName;
                        var tempValue = res.data[i].value;
                        $('#docCodeLi').append('<li role="presentation"><a role="menuitem2" tabindex="-1" href="javascript:void(0);" onclick ="image_Query('+tempValue+')">'+temp+'</a></li>');
                        form.render('select');
                    }
                }
            });
        }
    }

    //保存影像信息
    $('#btn_addImageInfo').on('click', function () {
        if($("#docCode").val()==""){
            layerTips.msg('请选择证件类型');
            return false;
        }
        var data = {docCode:$("#docCode").val(),fileNmeCN:$("#fileNmeCN").val(),number:$("#number").val(),expireDateStr:$("#expireDateStr").val(),operateType:billType};
        var imaid = $("#imageid").val();
        if(imaid==""){
            layerTips.msg('请选择影像');
        }else{
            if($("#docCode").val()==""){
                layerTips.msg('请选择证件类型');
            }else{
                $.ajax({
                    url: "../../image/setImageType/"+$("#imageid").val(),
                    type: 'post',
                    data: data,
                    dataType: "json",
                    success: function () {
                        layerTips.msg('更新成功');
                    }

                });
            }
        }
        return false;
    });
    //取消
    $("#btn_cancelImageInfo").on('click', function () {
        clearImageData();
        return false;
    });
    //补录
    $("#btn_reccordImage").on('click', function () {
        $('#docCode').attr("disabled", false);
        $("#docCode").removeClass("layui-disabled layui-unselect");
        $('#fileNmeCN').attr("disabled", false);
        $("#fileNmeCN").removeClass("layui-disabled layui-unselect");
        $('#number').attr("disabled", false);
        $("#number").removeClass("layui-disabled layui-unselect");
        $('#expireDateStr').attr("disabled", false);
        $("#expireDateStr").removeClass("layui-disabled layui-unselect");
        form.render();
        return false;
    });
    //删除
    $("#btn_imageDelete").on('click', function () {
        var imaid = $("#imageid").val();
        if(imaid==""){
            layerTips.msg('请选择影像');
        }else{
            layer.confirm('确定删除数据吗？', null, function (index) {
                $.ajax({
                    url: "../../image/delete/" + imaid,
                    type: "DELETE",
                    success: function (data) {
                        if (data.code == 'ACK') {
                            layerTips.msg("移除成功！");
                            $('.imgLiet  li.on').remove();
                            clearImageData();
                        } else {
                            layerTips.msg("移除失败！")
                        }
                    }
                });
                layer.close(index);
            });
        }
        return false;
    });
    //下载
    $("#btn_downLoadImage").on('click', function () {
        var imaid = $("#imageid").val();
        if(imaid==""){
            layerTips.msg('请选择影像');
        }else{
            window.location.href="../../image/downLoadImage/"+imaid;
        }
        return false;
    });
    //批量下载
    $("#btn_downLoadBetchImage").on('click', function () {
        if (!billId){
            billId = $('#recId').val();
        }
        window.location.href="../../image/downLoadBatchImage/"+billId;
        return false;

    });
    //上报道影像平台
    $("#btn_uploadToImage").on('click', function () {
        layer.confirm('确定上传到影像平台？', null, function (index) {
            $.ajax({
                url: "../../image/uploadToImage/" + billId,
                type: "get",
                success: function (data) {
                    if (data.code == 'ACK') {
                        if(data.data==0 || data.data=="0"){
                            layerTips.msg("上传成功！");
                        }else{
                            layerTips.msg("有"+data.data+"张图片上传失败，请再次上传！");
                        }
                    }
                },
                error:function(data2){
                    layerTips.msg(data2.responseJSON.message);
                }

            });
            layer.close(index);
        });
    });
    /*$('#btn_shaomiao').keyup(function () {
        return false;
    })*/
    //扫描
    $("#btn_shaomiao").on('click', function () {
        return false;
    });

    $('body').on('keydown',function(e){
        var k=e.keyCode, ctrl=e.ctrlKey, alt=e.altKey, shft=e.shiftKey;
        if(k==13){console.log('你按下了键盘E');return false}
    })
    $("#btn_shaomiao").on('click', function () {
        layer.open({
                type : 1,
                title : '影像拍摄',
                skin : 'layui-layer-rim', //加上边框
                area : [ '480px', '430px' ], //宽高
                content : '<div>\n' +
                '    <object id="show1" classid="clsid:E77E4CC8-E879-4A72-850A-B824742EC5B7" width="400" height="300"\n' +
                '            style="position: relative; left: 25px;top:10px; visibility: none;"></object>\n' +
                '</div>\n' +
                '<div>\n' +
                '    <input type="button" value="确定" id="btn_submitImage" onclick="submitImageFile();" style="position: relative; top: 20px;left: 50px;float: left;">\n' +
                '    <input type="button" value="左转" id="RotateLeft" onclick="rotateLeft();" style="position: relative; top: 20px;left: 80px;float: left;">\n' +
                '    <input type="button" value="右转" id="RotateRight" onclick="rotateRight();" style="position: relative; top: 20px;left: 110px;">\n' +
                '</div>',
                end: function () {
                    // 关闭拍摄窗口
                    closeScan();
                }
            });
        //打开拍摄窗口
        openScan();

    });

    $("#btn_uploadImage").on('click', function () {
       /* var i = $('.imgLiet  li').length;
        if (i != 0) {
            $('.imgLiet  li').remove();
        }*/
        if (!billId) {
            billId = $('#recId').val();
        }
        layer.open({
            type : 1,
            title : '图片导入',
            skin : 'layui-layer-rim', //加上边框
            area : [ '300px', '200px' ], //宽高
            content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择图片</div> </div>'
        });
        var uploader = WebUploader.create({
            auto: true, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server:  '../../image/upload/'+billId, // 文件接收服务端
            pick: '#imgPicker', // 选择文件的按钮。可选
            fileNumLimit: 5,//一次最多上传五张
            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            }
        });
        uploader.on( 'uploadSuccess', function( file, res ) {
            setImage("../.."+res.imgPath,res.id);
        });
        uploader.on( 'uploadError', function( file, reason ) {
            layerTips.msg('上传失败');
        });
        uploader.on("error",function(err){
            //Q_TYPE_DENIED
            if(err=="Q_TYPE_DENIED"){
                layer.msg('不支持该格式，请上传gif,jpg,jpeg,bmp,png格式的图片！');
            }else{
                layer.msg('上传失败:'+err);
            }
        });
        return false;
    });

    /*$("#btn_uploadImage").on('click', function () {
        var i = $('.imgLiet  li').length;
        if (i != 0) {
            $('.imgLiet  li').remove();
        }
        if (!billId){
            billId = $('#recId').val();
        }
        return false;
    });*/
    //多图片上传
   /* upload.render({
        elem: '#btn_uploadImage'
        ,url: '../../image/upload/'+billId
        ,multiple: true
        ,field : 'file'
        ,done: function(res){
            //alert(res.data[0].imgPath);
            if(res.code === 'ACK') {
                for (var i = 0; i < res.data.length; i++){
                    setImage("../.."+res.data[i].imgPath,res.data[i].id);
                }
            }
        }
    });*/
    var clearImageData = function (){
        var img = '<img src="" class="max-img">';
        $('#imgBox').html(img);
        $("#docCode").val("");
        $("#imageid").val("");
        $("#fileNmeCN").val("");
        $("#number").val("");
        $("#expireDateStr").val("");
        form.render('select');
    }
    var setImage = function (imagePath,imageid) {
        var li = '<li imgUrl="'+imagePath+'" isChoice="false" imageId="'+imageid+'" text=""><img src="'+imagePath+'" class="min-img"></li>'
        $('#ulLent').append(li);
        var i = $('.imgLiet  li').length;
        $('.imgLiet  li').eq(i - 1).addClass('on').siblings().removeClass('on');
        var img = '<img class="max-img" src="'+imagePath+'">';
        //$('#imgBox').html(img);
        $('.imgLiet  li').click(
            function() {
                $(this).addClass('on').siblings().removeClass('on');
                var ulr = $(this).attr('imgUrl');
                var img = '<img src="'+ulr+'" class="max-img">';
                $('#imgBox').html(img);
                var imageid=$(this).attr('imageId');
                $.get("../../image/getById/"+imageid,function(data){
                    $("#docCode").val(data.data.docCode);
                    $("#imageid").val(data.data.id);
                    $("#fileNmeCN").val(data.data.fileNmeCN);
                    $("#number").val(data.data.number);
                    $("#expireDateStr").val(data.data.expireDateStr);
                    form.render('select');
                });
            });
        $('.imgLiet  li').dblclick(
            function() {
                $(this).addClass('on').siblings().removeClass('on');
                var imageid=$(this).attr('imageId');
                window.open("../image/pictureView.html?bId="+imageid);
            });
    }
});
//查询
function image_Query(docCode){
    layui.use(['form','murl','saic','account','upload', 'picker', 'linkSelect', 'org', 'industry','common', 'laydate'], function () {
        var form = layui.form, url = layui.murl,
            saic = layui.saic, account = layui.account, picker = layui.picker,
            linkSelect = layui.linkSelect,
            org = layui.org,
            industry = layui.industry,
            common = layui.common,
            laydate = layui.laydate,
            upload = layui.upload;

            var i = $('.imgLiet  li').length;
            if (i != 0) {
             $('.imgLiet  li').remove();
            }

            var img = '<img src="" class="max-img">';
            $('#imgBox').html(img);
            $("#docCode").val("");
            $("#imageid").val("");
            $("#fileNmeCN").val("");
            $("#number").val("");
            $("#expireDateStr").val("");
            form.render('select');

        if (!billId){
            billId = $('#recId').val();
        }
        $.get("../../image/query",{refBillId:billId,docCode:docCode},function (res) {
            if(res.code === 'ACK') {
                for (var j = 0; j < res.data.length; j++){
                    var imagePath = "../.."+res.data[j].imgPath;
                    var imageid = res.data[j].id;
                    var li = '<li imgUrl="'+imagePath+'" isChoice="false" imageId="'+imageid+'" text=""><img src="'+imagePath+'" class="min-img"></li>'
                    $('#ulLent').append(li);
                    var i = $('.imgLiet  li').length;
                    $('.imgLiet  li').eq(i - 1).addClass('on').siblings().removeClass('on');
                    var img = '<img class="max-img" src="'+imagePath+'">';
                    //$('#imgBox').html(img);
                    $('.imgLiet  li').click(
                        function() {
                            $(this).addClass('on').siblings().removeClass('on');
                            var ulr = $(this).attr('imgUrl');
                            var img = '<img src="'+ulr+'" class="max-img">';
                            $('#imgBox').html(img);
                            var imageid=$(this).attr('imageId');
                            $.get("../../image/getById/"+imageid,function(data){
                                $("#docCode").val(data.data.docCode);
                                $("#imageid").val(data.data.id);
                                $("#fileNmeCN").val(data.data.fileNmeCN);
                                $("#number").val(data.data.number);
                                $("#expireDateStr").val(data.data.expireDateStr);
                                form.render('select');
                            });
                        });
                    $('.imgLiet  li').dblclick(
                        function() {
                            $(this).addClass('on').siblings().removeClass('on');
                            var imageid=$(this).attr('imageId');
                            window.open("../image/pictureView.html?bId="+imageid);
                        });
                }
            }else{
                layerTips.msg(res.message);
            }
        });
    });
    return false;
}

/**
 * 上传方法
 */
function submitImageFile() {
    //获取影像图片base64字符串
   var base64 =getBase64();
    layui.use(['form','murl','saic','account','upload', 'picker', 'linkSelect', 'org', 'industry','common', 'laydate'], function () {
        var form = layui.form, url = layui.murl,
            saic = layui.saic, account = layui.account, picker = layui.picker,
            linkSelect = layui.linkSelect,
            org = layui.org,
            industry = layui.industry,
            common = layui.common,
            laydate = layui.laydate,
            upload = layui.upload;
        var i = $('.imgLiet  li').length;
        if (i != 0) {
            $('.imgLiet  li').remove();
        }
        if (!billId){
            billId = $('#recId').val();
        }
        $.post("../../image/uploadImage/"+billId, {base64: base64}, function (res) {
            var imageid = res.data.id;
            var imagePath = res.data.imgPath;
            var li = '<li imgUrl="'+imagePath+'" isChoice="false" imageId="'+imageid+'" text=""><img src="'+imagePath+'" class="min-img"></li>'
            $('#ulLent').append(li);
            var i = $('.imgLiet  li').length;
            $('.imgLiet  li').eq(i - 1).addClass('on').siblings().removeClass('on');
            var img = '<img class="max-img" src="'+imagePath+'">';
            //$('#imgBox').html(img);
            $('.imgLiet  li').click(
                function() {
                    $(this).addClass('on').siblings().removeClass('on');
                    var ulr = $(this).attr('imgUrl');
                    var img = '<img src="'+ulr+'" class="max-img">';
                    $('#imgBox').html(img);
                    var imageid=$(this).attr('imageId');
                    $.get("../../image/getById/"+imageid,function(data){
                        $("#docCode").val(data.data.docCode);
                        $("#imageid").val(data.data.id);
                        $("#fileNmeCN").val(data.data.fileNmeCN);
                        $("#number").val(data.data.number);
                        $("#expireDateStr").val(data.data.expireDateStr);
                        form.render('select');
                    });
                });
            $('.imgLiet  li').dblclick(
                function() {
                    $(this).addClass('on').siblings().removeClass('on');
                    var imageid=$(this).attr('imageId');
                    window.open("../image/pictureView.html?bId="+imageid);
                });
        });
    });
    return false;
}
function banqhs() {
    var xtqhnum = 0;
    var popnum = 0;
    var pictime;
    var picnum = $('.imgLiet li').length

    $("#prev_btn1").click(function() {
        if (xtqhnum == 0) {
            xtqhnum = picnum
        }
        ;
        xtqhnum--;
        minshow(xtqhnum);
    })
    $("#next_btn1").click(function() {
        if (xtqhnum == picnum - 1) {
            xtqhnum = -1
        }
        ;
        xtqhnum++;
        minshow(xtqhnum)
    })
}

function minshow(xtqhnum) {

    var pnum = $('.imgLiet')
    var picminw = pnum.find('ul li').outerWidth(true);
    var picminnum = $('.imgLiet li').length
    var mingdjl_num = xtqhnum
    var mingdjl_w = -mingdjl_num * picminw;
    if (mingdjl_num < 0) {
        mingdjl_num = picminnum;
        return
    }

    pnum.find('ul li').css('float', 'left');
    if (picminnum > 11) {
        if (xtqhnum < 0) {
            mingdjl_w = 0;
        }
        if (xtqhnum == picminnum - 1) {
            mingdjl_w = -(mingdjl_num - 1) * picminw;
        }
        pnum.find('ul').stop().animate({
            'left' : mingdjl_w
        }, 1000);
    }

}
