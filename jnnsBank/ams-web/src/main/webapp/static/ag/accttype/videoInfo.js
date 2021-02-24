layui.config({
	base: '../js/' //假设这是你存放拓展模块的根目录
});
var accountImageBillsId;
var isShow;
var options = '';
var applyid;
layui.use(['form', 'common'], function () {
	var form = layui.form,
		common = layui.common;
    var refBillId = decodeURI(common.getReqParam("refBillId"));
    var billId = decodeURI(common.getReqParam("billId"));
    var recId = decodeURI(common.getReqParam("recId"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    applyid = decodeURI(common.getReqParam("applyid"));
	//var acctNo = decodeURI(common.getReqParam("acctNo"));
    accountImageBillsId = refBillId;

    if (accountImageBillsId == undefined || accountImageBillsId == "") {
        accountImageBillsId = billId;
    }
    if (accountImageBillsId == undefined || accountImageBillsId == "") {
        accountImageBillsId = recId;
    }
    if(recId!=null && recId!="" && recId!=undefined){
		//	recId有值证明是新发起流水
        accountImageBillsId=recId;
	}
    //isShow = buttonType != 'selectForChangeBtn';
    //待补录和新建流水
    /*if(buttonType=='update' || buttonType=='saicValidate' || buttonType==""){
        isShow=true;
	}else{
        isShow=false;
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
	}*/
    if(buttonType == 'selectForChangeBtn' ){
        isShow=false;
        $("#btnMessageSend").hide();
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
        $("#imageVideoDiv").hide();
    }else if(buttonType == 'select'){
        isShow=false;
        $("#btnMessageSend").hide();
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
    }else{
        isShow=true;
    }
    /*if(isShow==false){
        $("#btnVideoRecord").hide();
        $("#btnVideoUpload").hide();
        $("#btnVideoSend").hide();
	}*/
    $.get("../../config/findByKey?configKey=stoppedVideoEnabled", null, function (data) {
        if (data == true) {
            $("#videoInfo").css("display","");
            videoInfo.init();
        }
    })
    $.get('../../dictionary/findOptionsByDictionaryName?name=imageVideoErrorType', null, function (res) {
        for (var i = 0; i < res.data.length; i++) {
            options += '<option value="' + res.data[i].value + '" >' + res.data[i].name + '</option>';
        }
    })
	//查询
	$('#btnVideoQuery').on('click', function () {
		var videoNo = $.trim($('#videoNo').val());
        $.get('../../video/getFromIdp?billId='+accountImageBillsId+"&applyid="+videoNo, null, function (data) {
            if(data.code!='ACK'){
                layer.msg( data.message);
            }
        });
		videoInfo.init();
	});

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
                    maxmin: false,
                    resize: false,
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


    //发起双录
    $('#btnMessageSend').on('click', function () {

        $.get('../account/messageSend.html', null, function (html) {
            layer.open({
                id: 1,
                type: 1,
                title: '远程双录信息确认',
                content: html,
                shade: false,
                area: ['400px', '360px'],
                btn: ['确定', '取消'],
                maxmin: false,
                yes: function (index) {
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
                    // console.log("yes");
                    var data = {
                        depositorName:$("#msgDepositorName").val(),
                        legalTelephone:$("#msgLegalTelephone").val(),
                        legalName:$("#msgLegalName").val(),
                        legalIdcardNo:$("#msgLegalIdcardNo").val(),
                        acctType:$("#acctType").val(),
                        randomFlag: $('#randomFlag').val()
                    };
                    $.post('../../video/message', data, function(result){
                        layerTips.msg("发送成功");
                    }).error(function (e) {
                        layerTips.msg("发送失败");
                    });

                    layer.closeAll();
                },
                success: function (layero, index) {
                    //企业名称
                    var depositorName = $("#depositorName").val();
                    if (depositorName!=""){
                        $("#msgDepositorName").val(depositorName);
                    }

                    //法人姓名
                    var legalName = $("#legalName").val();
                    if (legalName!=""){
                        $("#msgLegalName").val(legalName);
                    }

                    //证件号码
                    var legalIdcardNo = $("#legalIdcardNo").val();
                    if (legalIdcardNo!=""){
                        $("#msgLegalIdcardNo").val(legalIdcardNo);
                    }

                    //手机号码
                    var legalTelephone = $("#legalTelephone").val();
                    if (legalTelephone!=""){
                        $("#msgLegalTelephone").val(legalTelephone);
                    }
                }
            });
        });

    });


	//本地上传
	$('#btnVideoUpload').on('click', function () {
        $.get("../../video/getVideoSize", null, function (data) {
            var acctType= $("#acctType").val();
            if(acctType==undefined){
                acctType="";
            }
            var acctNo= $("#acctNo").val();
            var depositorName = $("#depositorName").val();
            var legalName = $("#legalName").val();
            var regNo = $("#regNo").val();
            if (applyid == null || applyid == ""){
                var source = 'account';//来源：账户
            }else {
                var source = 'apply';//来源：预约
            }
            var parms = "acctType="+acctType+"&acctNo="+acctNo+"&depositorName="+encodeURI(depositorName)+"&legalName="+encodeURI(legalName)+"&regNo="+regNo+"&billsId="+accountImageBillsId+"&source="+source+"&applyid="+applyid;
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
                layer.msg("上传成功");
                videoInfo.init();
            });
            uploader.on('uploadError', function (file, reason) {
                layer.msg(reason.message);
            });*/
            uploader.on("error", function (err) {
                //Q_TYPE_DENIED
                if (err == "Q_TYPE_DENIED") {
                    layer.msg('不支持该格式，请上传mp4, avi, rmvb格式的视频！');
                } else if(err == 'Q_EXCEED_NUM_LIMIT'){
                    layer.msg('单次视频上传只能1个');
                }else if(err=="F_EXCEED_SIZE"){
                    layer.msg('视频超过最大限制,最大'+data+'M,请压缩后上传，压缩格式为zip');
                } else {
                    layer.msg('上传失败:' + err);
                }
            });
        });
	});

	//上传影像平台
	$('#btnVideoSend').on('click', function () {
        $.get('../../video/syncToImage?billId='+accountImageBillsId, null, function (data) {
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
		if (accountImageBillsId != null && accountImageBillsId != ''){
            $.get('../../video/findByBillId?billId='+accountImageBillsId, null, function (data) {
                if(data.code=='ACK'){
                    videoInfo.loadList(data.data);
                }else{
                    layer.msg( data.message);
                }
            });
        } else if(applyid != null && applyid != '') {
            $.get('../../video/findByApplyId?applyId='+applyid, null, function (data) {
                if(data.code=='ACK'){
                    videoInfo.loadList(data.data);
                }else{
                    layer.msg( data.message);
                }
            });
        }



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
                            videoInfo.init();
                        } else {
                            layer.msg(data.message);
                        }
                    }

                });
            });
		});
		//编辑
        $('.btn-video-edit').off().on('click', function () {
            var elem = $(this);
            var id = elem.data('id');

                $.get('../account/imageVideoEdit.html', null, function (form) {
                    layer.open({
                        type: 1,
                        title: "审核",
                        content: form,
                        btn: ['保存', '取消'],
                        shade: false,
                        area: '500px',
                        yes: function (index) {
                            //触发表单的提交事件

							$('form.layui-form').find('button[lay-filter=edit]').click();

                        },
                        success: function (layero, index) {
                            var form = layui.form;
                            layero.find('#vErrorCode').append(options);
                            form.render('select');
                            $.get('../../video/findOneInfo?id='+id, null, function (res2) {
                                var result = res2.data;
                                layero.find('#vErrorCode').val(result.verrorCode);
                                layero.find('#remarks').val(result.remarks);
                                form.render();
							});
                            form.on('submit(edit)', function (data) {
                                layer.closeAll();
                                $.ajax({
                                    url: "../../video/edit?id="+id,
                                    type: 'POST',
                                    data: data.field,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data.code === 'ACK') {
                                            layer.msg('编辑成功');
                                            videoInfo.init();
                                        } else {
                                            layer.msg(data.message);
                                        }
                                    }

                                });
                                return false;
                            });
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
                    '				<li><a href="javascript:;" data-id="' + item.id + '" class="btn-video-edit" data-code="account:btn_video_edit">编辑</a></li>' +
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
                    '			<ul>' +
                    '				<li><a href="javascript:;" data-id="' + item.id + '" class="btn-video-download">下载</a></li>' +
                    '			</ul>' +
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


