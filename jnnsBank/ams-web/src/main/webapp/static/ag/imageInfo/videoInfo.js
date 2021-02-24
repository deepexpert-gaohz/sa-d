layui.config({
	base: '../js/' //假设这是你存放拓展模块的根目录
});
var catchUrl;
var catchIsEdit;
layui.use(['form', 'common'], function () {
    var form = layui.form,
        common = layui.common;
    var id = decodeURI(common.getReqParam("id"));
    //发起双录
    $('#btnVideoRecord').on('click', function () {

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

    //本地上传
    $('#btnVideoUpload').on('click', function () {
        $.get("../../video/getVideoSize", null, function (data) {
            var acctType= $("#acctTypeVal").val();
            if(acctType==undefined){
                acctType="";
            }
            var acctNo= $("#acctNo").val();
            if(acctNo==undefined){
                acctNo="";
            }
            var depositorName = $("#depositorName").val();
            var legalName = $("#legalName").val();
            var regNo = $("#regNo").val();
            var parms = "acctType="+acctType+"&acctNo="+acctNo+"&depositorName="+encodeURI(depositorName)+"&legalName="+encodeURI(legalName)+"&regNo="+regNo+"&billsId="+id;
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
            uploader.on("uploadAccept", function (file, res) {
                if (res.code === 'ACK') {
                    layer.msg("上传成功");
                    videoInfo.init();
                }else{
                    layer.msg(res.message);
                }
            });
            /*uploader.on('uploadSuccess', function (file, res) {
                if(res.code=='ACK') {
                    layer.msg("上传成功");
                    videoInfo.init(catchUrl,catchIsEdit);
                }else{
                    layer.msg('上传失败：'+res.message);
                }

            });
            uploader.on('uploadError', function (file, reason) {
                layer.msg('上传失败');
            });*/
            uploader.on("error", function (err) {
                //Q_TYPE_DENIED
                if (err == "Q_TYPE_DENIED") {
                    layer.msg('不支持该格式，请上传mp4, avi, rmvb格式的视频！');
                } else if(err == 'Q_EXCEED_NUM_LIMIT'){
                    layer.msg('单次视频上传只能1个');
                }else if(err=="F_EXCEED_SIZE"){
                    layer.msg('视频超过最大限制,最大'+data+'M,请压缩后上传，压缩格式为zip');
                }  else {
                    layer.msg('上传失败:' + err);
                }
            });
        });
    });

    //上传影像平台
    $('#btnVideoSend').on('click', function () {
        $.get('../../video/syncToImage?billId='+id, null, function (data) {
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

});
function getVideoData(urlStr,isEdit) {
    catchUrl =urlStr;
    catchIsEdit = isEdit;
    if(isEdit==true){
        $("#btnMessageSend").show();
        $("#btnVideoRecord").show();
        $("#btnVideoUpload").show();
        $("#btnVideoSend").show();
        $("#imageVideoDiv").show();
	}else{
        $("#btnMessageSend").hide();
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
        $("#imageVideoDiv").hide();
    }
    $.get("../../config/findByKey?configKey=stoppedVideoEnabled", null, function (data) {
        if (data == true) {
            $("#videoInfo").css("display","");
            $("#videoInfo").show();
            videoInfo.init(urlStr,isEdit);
        }else {
            $("#videoInfo").css("display","none");
            $("#videoInfo").hide();
        }
    })
}
var videoInfo = {
	init: function (urlStr,isEdit) {
		videoInfo.viewData(urlStr,isEdit);
	},
	viewData: function (urlStr,isEdit) {
		
		var list = [
			{
				id: '000000',
				title: '000000000',
				videoUrl: "https://vd3.bdstatic.com/mda-jg2k3itix5tncgra/sc/mda-jg2k3itix5tncgra.mp4",
				imgUrl: "http://www.jplayer.org/video/poster/Big_Buck_Bunny_Trailer_480x270.png"
			},
			{
				id: '1111111',
				title: 'Big Buck Bunny Trailer',
				videoUrl: "http://www.jplayer.org/video/m4v/Big_Buck_Bunny_Trailer.m4v",
				imgUrl: "http://www.jplayer.org/video/poster/Big_Buck_Bunny_Trailer_480x270.png"
			},
			{
				id: '222222',
				title: "Incredibles Teaser",
				videoUrl: "http://www.jplayer.org/video/ogv/Incredibles_Teaser.ogv",
				imgUrl: "http://www.jplayer.org/video/poster/Incredibles_Teaser_640x272.png"
			},
			{
				id: '33333',
				title: "Finding Nemo Teaser",
				videoUrl: "http://www.jplayer.org/video/webm/Finding_Nemo_Teaser.webm",
				imgUrl: "http://www.jplayer.org/video/poster/Finding_Nemo_Teaser_640x352.png"
			}
		];
        $.get(urlStr, null, function (data) {
        	if(data.code=='ACK'){
                videoInfo.loadList(data.data,isEdit);
			}else{
                layer.msg( data.message);
			}
		});


	},
	loadList: function (list,isEdit) {
		var listHtml = videoInfo.getListHtml(list,isEdit);
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
                            videoInfo.init(catchUrl,catchIsEdit);
                        } else {
                            layer.msg(data.message);
                        }
                    }

                });
            });
		});

	},
	getListHtml: function (list,isEdit) {
		var listHtml = '';
		for (var i = 0; i < list.length; i++) {
			var item = list[i];
            if(isEdit==true){
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


