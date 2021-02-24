var page = null;

$(function () { page = new Page(); });

var Page = function (_options) {
    this.options = {
		flg: false, //监听是否静音
		address: '',//服务器地址
		isHold: 'hold', //挂断和保持
		isAnswer: false, //监听是否接听
		isOpenRiskWarning: true, //监听是否打开风险提示
		isOpenShareDesktop: true, //监听是否打开桌面共享
		isVideoAudio: true, //监听视频通话还是语音通话 true视频  false音频
		isStop: false, //监听是否暂停服务 true暂停 false开始
		_shareType: '', //屏幕共享的类型
		isRecord: true, //监听是否录制
		isCalling: false,//监听是否通话
		serverNumber: 0,//服务人数
		ShareDesktopClick: true,//屏幕共享本身点击状态
		SwitchAudioClick: true,//
		SharedWindowClick: true,//局部共享本身点击状态
		isLogin: false,
		username:'',//用户名
		auth:false,//是否有登录双录系统权限
    };
    this.init(_options);
}

Page.prototype = {
    init: function (_options) {
        //参数设置
        this.set(_options);
        //获取参数
        this.getParam();
        //初始化元素
        this.initElem();
        //显示数据
        this.viewData();
        //绑定事件
        this.bindEvent();
        return this;
    },
    //参数设置
    set: function (_options) {
        $.extend(true, this.options, _options);
        return this;
    },
    //获取参数
    getParam: function () {

    },
    //初始化元素
    initElem: function () {

		window.onunload = function () {
			try {
				console.log("Destroy start");
				cSharpActiveX.Destroy();
				console.log("Destroy end");
			} catch (err) {
				console.log(err);
			}
		}

		var that = this;
		that.setBoxHeight();
        $(window).resize(function () {
            that.setBoxHeight();
        });

		console.log("创建object对象 start");
		var csax = document.createElement('object');
		csax.setAttribute("classid", "clsid:E2AAFE18-F8B4-4523-9F94-175B64347F6A");
		csax.setAttribute("id", "cSharpActiveX");
		csax.setAttribute("name", "cSharpActiveX");
		csax.setAttribute("style", "display: none");
		document.body.appendChild(csax);
		console.log("创建object对象 over");

		console.log("自动登录 start");
		$.ajax({
			async: false,//false 同步执行完再往下执行
			url: "../../security/dualRecord",
			type: "GET",
			success: function (res) {
				if (res.code === 'ACK' && res.data) {
					console.log(res.data.username);console.log(res.data.auth);
					that.options.username = res.data.username;that.options.auth = res.data.auth;
					$("#username").val(that.options.username);
					$.ajax({
						async: false,
						type: 'GET',
						url: "../../config/findByKeyLike?configKey=dualRecordServerUrl",
						dataType: "json",
						success: function (res) {
							if (res.code=="ACK"){
								if (res.data.length>0){
									//免密登录，密码随机
									that.options.address = res.data[0].configValue;
									$("#address").val(res.data[0].configValue);
									console.log(that.options.address);
									if (that.options.auth){
										try {
											cSharpActiveX.CheckInTest(that.options.username, '12345678',that.options.address);// 192.168.111.10:8000
										} catch (err) {
											console.log(err);
											if (err.toString().indexOf("is not a function")!=-1){
												$("#ieTips").removeClass("hidden");
											}
										}
									}
								}
							}
						},
						error:function (res) {
							console.log("自动登录异常"+res);
						}
					});
				}
			},
			error:function (res) {
				console.log("获取用户名异常"+res);
			}
		});
		console.log("自动登录 over");


		console.log("获取话术提示 start");
		$.ajax({
			type: 'GET',
			url: "../../config/findByKeyLike?configKey=dualRecordRemind",
			dataType: "json",
			success: function (res) {
				if (res.code=="ACK"){
					$("#speechTips").html("");
					for (var i = 0; i < res.data.length; i++) {
						$("#speechTips").append('<p>'+res.data[i].configValue+'</p>');
					}
				}
			},
			error:function (res) {
				$("#speechTips").append('<p>话术提示获取失败</p>');
			}
		});
		console.log("获取话术提示 over");

		console.log('获取风险提示 start');
		$.ajax({
			type: 'GET',
			url: "../../config/findByKeyLike?configKey=riskWarning",
			dataType: "json",
			success: function (res) {
				if (res.code=="ACK"){
					for (var i = 0; i < res.data.length; i++) {
						var value = new Array();
						value = res.data[i].configValue.split("||");
						$("#files").append('<div class="files-item"><i class="fa fa-file-text-o"></i><span>'+value[0]+'</span></div>');
						var html = '<div class="risk-warning-item hidden" style="white-space:pre-line;">';
						html += '<h2>'+value[0]+'</h2>';
						html += '<p>'+value[1]+'</p>';
						html += '</div>';
						$("#risk_warning").append(html);
					}
				}
			},
			error:function (res) {
				$('#files').html('<div class="file-back"><span id="files_back">&lt; 返回</span></div><p>风险提示获取失败</p>');
			}
		});
		console.log('获取风险提示 over');

    },
    //显示数据
    viewData: function () {
        var that = this;

    },
    //绑定事件
    bindEvent: function () {
        var that = this;

		$('#tools_list li').on('click', function () {
			var type = $(this).data('type');
			switch (type) {
				case 'SnapShot': //实时拍照
					cSharpActiveX.SnapShot();
					break;
				case 'GuestSnapShot': //前端拍照
					cSharpActiveX.GuestSnapshot();
					break;
				case 'ShowMessage': //文本消息
					cSharpActiveX.ShowMessage();
					break;
				case 'TransferCall': //通话转接
					cSharpActiveX.Transfer('G2');
					break;
				case 'SwitchCall': //通话切换
					cSharpActiveX.ToVideo();
					break;
				case 'ShareDesktop': //屏幕共享
					cSharpActiveX.EnableDesktopShare(that.options.isOpenShareDesktop);
					$('#ShareDesktop').text(that.options.ShareDesktopClick ? '关闭屏幕共享' : '屏幕共享');
					that.options.ShareDesktopClick = !that.options.ShareDesktopClick;
					that.options.isOpenShareDesktop = !that.options.isOpenShareDesktop;
					break;
				case 'SharedWindow': //局部共享 有问题
					cSharpActiveX.ShareIEWindow(that.options.isOpenShareDesktop);
					$('#ShareDesktop').text(that.options.SharedWindowClick ? '关闭局部共享' : '局部共享');
					that.options.SharedWindowClick = !that.options.SharedWindowClick;
					break;
				case 'noFonc':
					alert('此功能未开放，请联系客服');
					break;
				case 'settingWindow':
					cSharpActiveX.Set();
					break;
				default:
					break;
			}
		});



		//风险提示
        $('#folders').on('dblclick', function () {
			$('#folders').addClass('hidden');
			$('#files').removeClass('hidden');
		});
		$('#files').on('dblclick','.files-item',function () {
			var index = $(this).index();
			console.log(index)
			$('#files').addClass('hidden');
			$('#risk_warning').removeClass('hidden');
			$('#risk_warning .risk-warning-item').addClass('hidden');
			$('#risk_warning .risk-warning-item').eq(index-1).removeClass('hidden');
		});
		$('#risk_warning_back').on('click', function () {
			$('#risk_warning .risk-warning-item').addClass('hidden');
			$('#risk_warning').addClass('hidden');
			$('#files').removeClass('hidden');
		});
		$('#files_back').on('click', function () {
			$('#files').addClass('hidden');
			$('#folders').removeClass('hidden');
		});


		//登录
		$('#checkIn').on('click',function () {
			that.options.username = ($("#username").val()||Math.ceil(Math.random()*10000));
			if (that.options.auth){
				try {
					console.log("登录");
					cSharpActiveX.CheckInTest(that.options.username, '12345678',that.options.address);// 192.168.111.10:8000
					console.log("end");
				} catch (err) {
					console.log(err);
					if (err.toString().indexOf("is not a function")!=-1){
						$("#ieTips").removeClass("hidden");
					}
				}
			}
		});

		//开启暂停服务
		$('#applyPause').on('click',function () {
			console.log("开启暂停服务 start isStop:"+that.options.isStop);
			that.options.isStop = !that.options.isStop;
			if (that.options.isStop) {
				$("#applyPause").addClass("btn-primary").removeClass('btn-danger').text('开始服务');
			} else {
				$("#applyPause").addClass('btn-danger').removeClass('btn-primary').text('暂停服务');
			}
			cSharpActiveX.ApplyPause(that.options.isStop);
			console.log("开启暂停服务 over isStop:"+that.options.isStop);
		});

		//通话挂起
		$('#holdGetBack').on('click',function () {
			console.log("通话挂起 start isHold:"+that.options.isHold);
			cSharpActiveX.HoldGetBack(that.options.isHold);//status:hold会话保持 get:保持取回
			$('#holdGetBack').text(that.options.isHold === 'hold' ? '保持取回' : '通话挂起');
			that.options.isHold = that.options.isHold === 'hold' ? 'get' : 'hold';
			console.log("通话挂起 over isHold:"+that.options.isHold);
		});

		//办理失败
		$('#handlingResultFail').on('click',function () {
			console.log("办理结果失败");
			cSharpActiveX.SetVerifyResult(false);
			alert("办理失败");
		});

		//办理成功
		$('#handlingResultSuccess').on('click',function () {
			console.log("办理结果成功");
			cSharpActiveX.SetVerifyResult(true);
			that.saveResult();
			alert("办理成功,稍后请在双录视频管理页查询");
		});
	},
	//设置box高度
    setBoxHeight: function () {
		var screenHeight = $(window).height();
		console.log(screenHeight);
		$('.c1-body').css('height', (screenHeight-80)+ 'px');
		$('.c2-r1-body').css('height', (screenHeight-222)+ 'px');
		$('.c3-body').css('height', (screenHeight-80)+ 'px');
		$('.c4-r1-body').css('height', (screenHeight-145)/2 + 'px');
		$('.c4-r2-body').css('height', (screenHeight-145)/2 + 'px');
		$('.info-txt').css('height', (screenHeight-272)+ 'px');
    },

	//保存双录结果
	saveResult:function(){
		var data = {
			// filePath : "http://127.0.0.1/123456.m4v",
			// fileName : "12314566456",
			// acctNo : "12312311231231",
			remarks: $("#remarks").val(),
			source: "image",
			callId:this.GetSerialNumber(),
			clientName:this.GetGuestUserId(),
			recordType:"REMOTERECORD"
		};

		$.ajax({
			type: 'POST',
			url: "../../video/imageVideo",
			data: data,
			dataType: "json",
			success: function (res) {
				console.log(res);
			},
			error:function (res) {
				console.log(res);
			}
		});
	},

	//在通话中获取访客的 id
	GetGuestUserId : function(){
		return cSharpActiveX.GuestUserId();
	},

	// 用于获取自己的标识，
	GetUserId:function (){
		return cSharpActiveX.UserId();
	},

	//用于获取通话的唯一标识（用于查询视频文件）
	GetSerialNumber:function(){
		return cSharpActiveX.GetSerialNumber();
	},
};
