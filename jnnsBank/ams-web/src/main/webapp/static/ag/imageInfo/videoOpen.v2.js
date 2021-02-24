layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var isShow;
var createView;
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'laydate'], function () {
    var form = layui.form, url = layui.murl,
        picker = layui.picker,
        common = layui.common;
    var laytpl = layui.laytpl;
    var type = decodeURI(common.getReqParam("type"));
    var id = decodeURI(common.getReqParam("id"));
    if(type=="view"){
        isShow=true;
        $("#btnMessageSend").hide();
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
        $("#btnVideoRecordNew").hide();
        $("#btnVideoUploadNew").hide();
        $.get('../../video/findOne?id=' + id, null, function (result) {
            var data = result.data;
            for (var key in data) {
                if (data[key] != null) {
                    $('#' + key).val(data[key]);
                    if(key=="acctType"){
                        form.render('select');
                    }
                }
            }
        });
        var viewUrl = "../../video/findByOne?id="+id;
        videoInfo.init(viewUrl);
    }else if(type=="apply"){//预约模块跳转
        var applyid = decodeURI(common.getReqParam("applyId"));
        $('#applyid').val(applyid);
        var depositorName = decodeURI(common.getReqParam("depositorName"));
        $('#depositorName').val(depositorName);
        var acctType = decodeURI(common.getReqParam("acctType"));
        $('#acctType').val(acctType);
        var source = decodeURI(common.getReqParam("source"));
        $('#source').val(source);
        form.render('select');
    }else{
        isShow=true;
    }

    var validate = function () {
        this.config = {
            acctNo: '',
            acctType: '',
            depositorName: '',
            legalName: '',
            regNo: ''
        };
    };
    validate.prototype = $.extend({}, {
        //参数设置[options]
        set: function (options) {
            $.extend(true, this.config, options);
            return this;
        },
        //初始化
        init: function (options) {
            //参数设置
            this.set(options);
            //初始化验证
            this.initValid();
            //绑定事件
            this.bindEvent();
        },
        //初始化验证
        initValid: function () {
            var that = this;

            var validLabel = [ 'acctType','depositorName','legalName','regNo'];
            addValidateFlag(validLabel);

            var rules = {
                // acctNo: {
                //     required: true
                // },
                acctType: {
                    required: true
                },
                depositorName: {
                    required: true
                },
                legalName: {
                    required: true
                },
                regNo: {
                    required: true
                }
            };

            var messages = {};

            this.config.validator = $('#editForm').validate({
                rules: rules,
                messages: messages,
                onkeyup : onKeyUpValidate,
                onfocusout : onFocusOutValidate,
                ignore: "",
                errorPlacement : errorPlacementCallback,
                highlight : highlight,
                unhighlight : unhighlight,
                showErrors: showErrors,
                submitHandler: function() {
                    that.submitForm(toJson($('#editForm').serializeArray()));
                    return false;
                }
            });
        },
        //绑定事件
        bindEvent: function () {
            var that = this;

            //发送远程双录短信
            $('#btnMessageSend').on('click', function () {

                $.get('../account/messageSend.html', null, function (html) {
                    layer.open({
                        id: 1,
                        type: 1,
                        title: '远程双录信息确认',
                        content: html,
                        shade: false,
                        area: ['400px', '410px'],
                        btn: ['确定', '取消'],
                        maxmin: false,
                        yes: function (index) {

                            if ($("#businessType").val()=="corporate"){//对公
                                if ($("#msgDepositorName").val()==""){
                                    layerTips.msg("请输入企业名称");
                                    return;
                                }
                                if ($("#msgLegalName").val()==""){
                                    layerTips.msg("请输入法人姓名");
                                    return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");
                                    return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }else {//个人
                                if ($("#msgCustomerName").val()==""){
                                    layerTips.msg("请输入客户姓名");return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }


                            var data = {
                                depositorName:$("#msgDepositorName").val(),
                                legalTelephone:$("#msgLegalTelephone").val(),
                                legalName:$("#msgLegalName").val(),
                                legalIdcardNo:$("#msgLegalIdcardNo").val(),
                                acctType:$("#acctType").val(),
                                applyid:$("#applyid").val(),
                                source:$('#source').val(),
                                randomFlag: $('#randomFlag').val(),
                                customerName:$("#msgCustomerName").val(),
                                businessType:$("#businessType").val()
                            };

                            $.post('../../video/message', data, function(result){
                                if (result.code == "ACK") {
                                    // layerTips.msg("发送成功");

                                    //TODO 测试时使用
                                    layer.open({
                                        id: 2,
                                        type: 1,
                                        title: '访客测试地址',
                                        content: '<div id="qrcode"></div>',
                                        shade: false,
                                        area: ['200px', '250px'],
                                        maxmin: false,
                                        yes: function (index) {

                                        },
                                        success: function (layero, index) {

                                            //使用table生成
                                            jQuery('#qrcode').qrcode({
                                                width : 190,
                                                height : 190,
                                                render: !!document.createElement('canvas').getContext ? 'canvas' : 'table', //为了支持ie8及以下
                                                text: result.data.url
                                            });
                                        },
                                        error:function (index) {
                                            layerTips.msg("二维码生成失败");
                                        }
                                    });

                                }else {
                                    layerTips.msg("发送失败");
                                }

                            }).error(function (e) {
                                layerTips.msg("发送失败");
                            });

                            layer.closeAll();
                        },
                        success: function (layero, index) {
                            // var form = layui.form();

                            $("#businessType").on("change",function (e) {
                                if ($("#businessType").val()=="corporate"){//对公
                                    console.log("1111");
                                    $('#msgDepositorNameDiv').show();
                                    $('#msgLegalNameDiv').show();
                                    $('#msgCustomerNameDiv').hide();
                                }else {//个人
                                    console.log("2222");
                                    $('#msgDepositorNameDiv').hide();
                                    $('#msgLegalNameDiv').hide();
                                    $('#msgCustomerNameDiv').show();
                                }
                            });
                        }
                    });
                })

            });

            //为兼容原有代码，又满足现有需求，所以新增临柜双录发起方式。
            //发起临柜双录
            $('#btnVideoRecordNew').on('click', function () {

                $.get('../account/messageSend.html', null, function (html) {
                    layer.open({
                        id: 1,
                        type: 1,
                        title: '临柜双录信息确认',
                        content: html,
                        shade: false,
                        area: ['400px', '360px'],
                        btn: ['确定', '取消'],
                        maxmin: false,
                        yes: function (index) {

                            if ($("#businessType").val()=="corporate"){//对公
                                if ($("#msgDepositorName").val()==""){
                                    layerTips.msg("请输入企业名称");
                                    return;
                                }
                                if ($("#msgLegalName").val()==""){
                                    layerTips.msg("请输入法人姓名");
                                    return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");
                                    return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }else {//个人
                                if ($("#msgCustomerName").val()==""){
                                    layerTips.msg("请输入客户姓名");return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }

                            $.get('../account/videoRecord.html', null, function (html) {
                                layer.open({
                                    id: 2,
                                    type: 1,
                                    title: '视频双录',
                                    content: html,
                                    shade: false,
                                    area: ['900px', '444px'],
                                    maxmin: true,
                                    success: function (layero, index) {
                                        $.get('../../config/getImageVideoRemind?configKey=imageVideoRemind', null, function (data) {
                                            if(data.code=='ACK'){
                                                var list=data.data;
                                                var listHtml = '';
                                                for (var i = 0; i < list.length; i++) {
                                                    var item = list[i];
                                                    listHtml += '<li>' +(i+1)+"、"+ item.configValue + '</li>'
                                                }
                                                $('#videoRemindList').html(listHtml);
                                            }
                                        });
                                    }
                                });
                            });
                            layer.close(index);
                        },
                        success: function (layero, index) {

                            $("#randomFlagDiv").hide();

                            $("#businessType").on("change",function (e) {
                                if ($("#businessType").val()=="corporate"){//对公
                                    $('#msgDepositorNameDiv').show();
                                    $('#msgLegalNameDiv').show();
                                    $('#msgCustomerNameDiv').hide();
                                }else {//个人
                                    $('#msgDepositorNameDiv').hide();
                                    $('#msgLegalNameDiv').hide();
                                    $('#msgCustomerNameDiv').show();
                                }
                            });
                        }
                    });
                })

            });

            //为兼容原有代码，又满足现有需求，所以新增本地上传发起方式。
            //发起本地上传
            $('#btnVideoUploadNew').on('click', function () {

                $.get('../account/messageSend.html', null, function (html) {
                    layer.open({
                        id: 1,
                        type: 1,
                        title: '本地上传信息确认',
                        content: html,
                        shade: false,
                        area: ['400px', '360px'],
                        btn: ['确定', '取消'],
                        maxmin: false,
                        yes: function (index) {

                            if ($("#businessType").val()=="corporate"){//对公
                                if ($("#msgDepositorName").val()==""){
                                    layerTips.msg("请输入企业名称");
                                    return;
                                }
                                if ($("#msgLegalName").val()==""){
                                    layerTips.msg("请输入法人姓名");
                                    return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");
                                    return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }else {//个人
                                if ($("#msgCustomerName").val()==""){
                                    layerTips.msg("请输入客户姓名");return;
                                }
                                if ($("#msgLegalIdcardNo").val()==""){
                                    layerTips.msg("请输入证件号码");return;
                                }
                                if ($("#msgLegalTelephone").val()==""){
                                    layerTips.msg("请输入手机号码");
                                    return;
                                }
                            }



                            $.get("../../video/getVideoSize", null, function (data) {

                                var myData = {
                                    depositorName:$("#msgDepositorName").val(),
                                    legalTelephone:$("#msgLegalTelephone").val(),
                                    legalName:$("#msgLegalName").val(),
                                    legalIdcardNo:$("#msgLegalIdcardNo").val(),
                                    acctType:$("#acctType").val(),
                                    applyid:$("#applyid").val(),
                                    source:$('#source').val(),
                                    randomFlag: $('#randomFlag').val(),
                                    customerName:$("#msgCustomerName").val(),
                                    businessType:$("#businessType").val(),
                                    recordType:"LOCALUPLOAD"
                                };

                                layer.open({
                                    type: 1,
                                    title: '视频上传',
                                    skin: 'layui-layer-rim',
                                    area: '300px',
                                    content: '<div style="padding: 15px;" id="uploadimg"><div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择视频</div> </div>'
                                });
                                var uploader = WebUploader.create({
                                    auto: true, // 选完文件后，是否自动上传
                                    swf: '../js/webuploader/Uploader.swf', // swf文件路径
                                    server: '../../video/upload/v2?'+ JsonToUrl(myData), // 文件接收服务端
                                    pick: '#imgPicker', // 选择文件的按钮。可选
                                    fileNumLimit: 10,//一次最多上传十张
                                    fileSingleSizeLimit:data*1024*1024,
                                    // 只允许选择图片文件。
                                    accept: {
                                        title: 'Videos',
                                        extensions: 'mp4,avi,rmvb,zip',
                                        mimeTypes: 'video/*'
                                    }
                                });
                                uploader.on("uploadAccept", function (file, res) {
                                    if (res.code === 'ACK') {
                                        layer.msg("上传成功");
                                        videoInfo.init();
                                    }else{
                                        layer.msg(res.message);
                                    }
                                });
                                uploader.on("error", function (err) {
                                    //Q_TYPE_DENIED
                                    if (err == "Q_TYPE_DENIED") {
                                        layer.msg('不支持该格式，请上传mp4, avi, rmvb格式的视频！');
                                    } else if(err == 'Q_EXCEED_NUM_LIMIT'){
                                        layer.msg('单次视频上传只能1个');
                                    } else if(err=="F_EXCEED_SIZE"){
                                        layer.msg('视频超过最大限制,最大'+data+'M,请压缩后上传，压缩格式为zip');
                                    } else {
                                        layer.msg('上传失败:' + err);
                                    }
                                });
                            });

                            layer.closeAll();
                        },
                        success: function (layero, index) {

                            $("#randomFlagDiv").hide();

                            $("#businessType").on("change",function (e) {
                                if ($("#businessType").val()=="corporate"){//对公
                                    $('#msgDepositorNameDiv').show();
                                    $('#msgLegalNameDiv').show();
                                    $('#msgCustomerNameDiv').hide();
                                }else {//个人
                                    $('#msgDepositorNameDiv').hide();
                                    $('#msgLegalNameDiv').hide();
                                    $('#msgCustomerNameDiv').show();
                                }
                            });
                        }
                    });
                })

            });

        }
    });
    new validate().init({});
});

var videoInfo = {
    init: function (url) {
        videoInfo.viewData(url);
    },
    viewData: function (url) {
        $.get(url, null, function (data) {
            if(data.code=='ACK'){
                videoInfo.loadList(data.data);
            }else{
                layer.msg( data.message);
            }
        });


    },
    loadList: function (list) {
        var listHtml = videoInfo.getListHtml(list);
        $('#video_list').html(listHtml);

        $('.video-play').on('click', function () {
            var elem = $(this);
            var id = elem.data('id');
            var title = elem.data('title');
            var videoUrl = elem.data('video');
            var imgUrl = elem.data('img');
            suffix = videoUrl.substring(videoUrl.lastIndexOf('.') + 1);

            $.get('../account/videoPlay.html', null, function (html) {
                layer.open({
                    id: 1,
                    type: 1,
                    title: '视频播放',
                    content: html,
                    shade: false,
                    area: ['642px', '505px'],
                    maxmin: true,
                    success: function (layero, index) {
                        var data = {
                            title: title,
                            poster: imgUrl
                        }
                        data[suffix] = videoUrl;

                        $("#jquery_jplayer_1").jPlayer({
                            ready: function () {
                                $(this).jPlayer("setMedia", data);
                            },
                            swfPath: "../plugins/jPlayer/jplayer",
                            supplied: "webmv,ogv,m4v,mp4",
                            size: {
                                width: "640px",
                                height: "360px",
                                cssClass: "jp-video-360p"
                            },
                            useStateClassSkin: true,
                            autoBlur: false,
                            smoothPlayBar: true,
                            keyEnabled: true,
                            remainingDuration: true,
                            toggleDuration: true
                        });

                    }
                });
            });
        });

        //下载
        $('.btn-video-download').off().on('click', function () {
            var elem = $(this);
            var videoUrl = elem.data('video');
            if (videoUrl!=null && videoUrl.indexOf("mp4") != -1) {
                $("#downloadFrame").prop('src', "../../video/downloadVideo?videoUrl="+videoUrl);
            }else {
                var id = elem.data('id');
                var downloadZipUrl="../../video/downloadZip?id="+id;
                $("#downloadFrame").prop('src', downloadZipUrl);
            }
        });

        //删除
        $('.btn-video-delete').off().on('click', function () {
            var elem = $(this);
            var id = elem.data('id');
            layer.confirm('确定删除视频？', {
                btn: ['确定', '取消']
            }, function () {
                $.ajax({
                    url:"../../video/delete?id="+id,
                    type: 'GET',
                    dataType: "json",
                    success: function (data) {
                        if (data.code === 'ACK') {
                            layer.msg("删除成功");
                            videoInfo.init(createView);
                        } else {
                            layer.msg(data.message);
                        }
                    }

                });
            });
        });

    },
    getListHtml: function (list) {
        var listHtml = '';
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if(isShow==true){
                listHtml += '<div class="video-item">' +
                    '	<div class="video-item-header">' +
                    '		<h4 class="video-item-title">' + item.title + '</h4>' +
                    '		<div class="video-item-operate">' +
                    '			<i class="layui-icon"></i>' +
                    '			<ul>' +
                    '				<li><a href="javascript:;" data-id="' + item.id + '" data-video="' + item.videoUrl + '" class="btn-video-download">下载</a></li>' +
                    '				<li><a href="javascript:;" data-id="' + item.id + '" class="btn-video-delete">删除</a></li>' +
                    '			</ul>' +
                    '		</div>' +
                    '	</div>' +
                    '	<div class="video-item-body">' +
                    '		<img width="240" height="135"  src="' + item.imgUrl + '">' +
                    '		<i class="fa fa-play-circle-o video-play" data-id="' + item.id + '" data-title="' + item.title + '" data-video="' + item.videoUrl +'" data-img="' + item.imgUrl + '"></i>' +
                    '	</div>' +
                    '</div>';
            }else{
                listHtml += '<div class="video-item">' +
                    '	<div class="video-item-header">' +
                    '		<h4 class="video-item-title">' + item.title + '</h4>' +
                    '		<div class="video-item-operate">' +
                    '			<i class="layui-icon"></i>' +
                    '		</div>' +
                    '	</div>' +
                    '	<div class="video-item-body">' +
                    '		<img width="240" height="135"  src="' + item.imgUrl + '">' +
                    '		<i class="fa fa-play-circle-o video-play" data-id="' + item.id + '" data-title="' + item.title + '" data-video="' + item.videoUrl +'" data-img="' + item.imgUrl + '"></i>' +
                    '	</div>' +
                    '</div>';
            }

        }
        return listHtml;
    }
}