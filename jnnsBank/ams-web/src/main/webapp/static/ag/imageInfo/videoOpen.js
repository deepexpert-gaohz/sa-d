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
        isShow=false;
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
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

            var validLabel = ['acctNo', 'acctType','depositorName','legalName','regNo'];
            addValidateFlag(validLabel);

            var rules = {
                acctNo: {
                    required: true
                },
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
            //发起双录
            $('#btnVideoRecord').on('click', function () {
                if (!$("#editForm").valid()) {
                    return;
                }
                $.get('../account/videoRecord.html', null, function (html) {
                    layer.open({
                        id: 1,
                        type: 1,
                        title: '视频双录',
                        content: html,
                        shade: false,
                        area: ['900px', '444px'],
                        maxmin: true,
                        success: function (layero, index) {

                            /*list = [
                                { name:'1、您是否为XX公司代表人XXX？' },
                                { name:'2、现XXXXXX公司将在本行开立基本存款账户，您是否知晓。' },
                                { name:'3、您是否XX公司法定代表人XXX？' },
                                { name:'4、现XXXX公司在本行开立基本存款户，您是否为XX公司法定代表人XXX？' },
                                { name:'5、您是否为XX公司代表人XXX？' },
                                { name:'6、现XXXXXX公司将在本行开立基本存款账户，您是否知晓。' }
                            ]*/
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

            });

            //确定按钮
            $('#btnVideoUpload').click(function(){
                //$('#editForm').submit();
                if (!$("#editForm").valid()) {
                    return;
                }
                $.get("../../video/getVideoSize", null, function (data) {
                    var acctType= $("#acctType").val();
                    if(acctType==undefined){
                        acctType="";
                    }
                    var acctNo= $("#acctNo").val();
                    if(acctNo==undefined){
                        acctNo="";
                    }
                    var depositorName = $("#depositorName").val();
                    if(depositorName==undefined || depositorName==""){
                        layer.msg("企业名称不能为空");
                        return false;
                    }
                    var legalName = $("#legalName").val();
                    var regNo = $("#regNo").val();
                    var parms = "acctType="+acctType+"&acctNo="+acctNo+"&depositorName="+encodeURI(depositorName)+"&legalName="+encodeURI(legalName)+"&regNo="+regNo;
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
                        server: '../../video/upload?'+parms, // 文件接收服务端
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
                    /*uploader.on('uploadSuccess', function (file, res) {
                        if(res.code=='ACK') {
                            layer.msg("上传成功");
                            createView = "../../video/findByCondition?depositorName="+depositorName;
                            videoInfo.init(createView);
                        }else{
                            layer.msg('上传失败：'+res.message);
                        }
                    });
                    uploader.on('uploadError', function (file, reason) {
                        layer.msg('上传失败');
                    });*/
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
            });
            //上传影像平台
            $('#btnVideoSend').on('click', function () {
                var depositorName = $("#depositorName").val();
                $.get('../../video/syncToImageByDepositorName?depositorName='+depositorName, null, function (data) {
                    if(data.code=="ACK"){
                        if(data.data==0){
                            layer.msg('同步成功');
                        }else{
                            layer.msg('同步失败'+data.data+"段视频");
                        }
                    }else{
                        layer.msg('同步失败');
                    }
                });
                return false;
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
                            supplied: "webmv,ogv,m4v",
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
            var id = elem.data('id');
            var downloadZipUrl="../../video/downloadZip?id="+id;
            location.href=downloadZipUrl;
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
                    '				<li><a href="javascript:;" data-id="' + item.id + '" class="btn-video-download">下载</a></li>' +
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