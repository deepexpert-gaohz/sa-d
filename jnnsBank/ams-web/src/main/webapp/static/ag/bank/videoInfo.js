layui.config({
	base: '../js/' //假设这是你存放拓展模块的根目录
});
var applyId;
layui.use(['form', 'common'], function () {
	var form = layui.form,
		common = layui.common;
    applyId = decodeURI(common.getReqParam("applyId"));
	//var acctNo = decodeURI(common.getReqParam("acctNo"));
    $.get("../../config/findByKey?configKey=stoppedVideoEnabled", null, function (data) {
        if (data == true) {
            $("#videoInfo").css("display","");
            videoInfo.init();
        }
    })
});

var videoInfo = {
	init: function () {
		videoInfo.viewData();
	},
	viewData: function () {
		
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
        $.get('../../video/findByApplyId?applyId='+applyId, null, function (data) {
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
								supplied: "webmv, ogv, m4v",
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
                            videoInfo.init();
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
		return listHtml;
	}
}


