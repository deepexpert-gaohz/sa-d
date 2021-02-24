var phoneNumber = {
    baseUrl: "../../networkVerify/phoneNumber"
};

layui.config({
    base: '../js/'
});

layui.use(['jquery', 'layer', 'element', 'form', 'laydate', 'common'], function () {
	var $ = layui.jquery,
		layer = layui.layer,
		element = layui.element,
		form = layui.form,
		laydate = layui.laydate,
		common = layui.common;

	var layerTips = parent.layer === undefined ? layui.layer : parent.layer;


    var unique = function () {
		this.config = {

		};
	};

	unique.prototype = $.extend({}, {
		//参数设置[options]
		set: function (options) {
			// $.extend(true, this.config, options);
			// return this;
		},
		//初始化
		init: function (options) {
			//参数设置
			this.set(options);
			//获取参数
			this.getParam();
			//初始化元素
			this.initElem();
			//初始化验证
			this.initValid();
			//初始化数据
			this.initData();
			//绑定事件
			this.bindEvent();
			//元素改变联动
			this.elemChange();
		},
		//获取参数
		getParam: function () {

		},
		//初始化元素
		initElem: function (fn) {

		},
		//初始化验证
		initValid: function () {
			var that = this;
			var validLabel = ['nm', 'mobNb', 'idTp','identification','opNm'];
			addValidateFlag(validLabel);
			var rules = {
                nm: {//姓名
                    required: true,
                    maxlength: 140
				},
                mobNb: {//手机号码
					required: true,
                    isMobile : true
				},
                idTp: {//证件类型
                    required: true
				},
                identification: {//证件号码
                    required: true,
                    maxlength: 35
                },
                uniSocCdtCd: {//统一社会信用代码
                    onlyInputOne :["#bizRegNb"],
                    maxlength:18
                },
                bizRegNb: {//工商注册号
                    onlyInputOne :["#uniSocCdtCd"],
                    maxlength:15
                },
                opNm: {//操作员姓名
                    required: true,
                    maxlength:140
				}

            };

			this.config.validator = $('#editForm').validate({
				rules: rules,
				//messages: messages,
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
		//初始化数据
		initData: function (fn) {

		},
		//绑定事件
		bindEvent: function () {
			var that = this;
			//确定按钮
			$('#btnSubmit').click(function(){

				$('#editForm').submit();

			});

            //select选择后去焦点，进行校验
            form.on('select', function (data) {
                $(data.elem).blur();
            });

		},
		//元素改变联动
		elemChange: function (fn) {

		},
		//提交表单
		submitForm: function (data, fn) {

		    //按钮置灰
            $('#btnSubmit').addClass("layui-btn-disabled");
            $('#btnSubmit').attr("disabled",true);
            var index = layer.load(1, {
                shade: [0.1,'#fff'] //0.1透明度的白色背景
            });




            var nm = $('#nm').val();
            var mobNb = '86'+$('#mobNb').val();//默认+86
            var idTp = $('#idTp').val();
            var identification = $('#identification').val();
            var uniSocCdtCd = $('#uniSocCdtCd').val();
            var bizRegNb = $('#bizRegNb').val();
            var opNm = $('#opNm').val();


            var postData = {
                nm: nm,
                mobNb: mobNb,
                idTp: idTp,
                identification: identification,
                uniSocCdtCd: uniSocCdtCd,
                bizRegNb: bizRegNb,
                opNm:opNm
            };

            $.ajax({
                url: phoneNumber.baseUrl+'/verify',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {

                    if (res.code === 'ACK') {
                        var data = res.data;
                        if (data.mobNb!=null) {
                        	$('#mobNb1').text(data.mobNb||'');
                            $('#rslt').text(rsltSwitch(data.rslt)||'');
                            if (data.rslt=="MCHD"){
                                $('#mobCrr').text(mobCrrSwitch(data.mobCrr)||'');
                                $('#locMobNb').text(data.locMobNb||'');
                                $('#locNmMobNb').text(data.locNmMobNb||'');
                                $('#cdTp').text(cdTpSwitch(data.cdTp)||'');
                                $('#sts').text(stsSwitch(data.sts)||'');
							}

                            $('#success').show();
                            $('#fail').hide();
                            $('#result').show();
						}else if (data.procSts!=null){
                            $('#procSts').text(procStsSwitch(data.procSts)||'');
                            $('#procCd').text(data.procCd||'');
                            $('#rjctInf').text(data.rjctInf||'');

                            $('#fail').show();
                            $('#success').hide();
                            $('#result').show();
						}else if(data.msgCode!=null){
                            layerTips.msg('报文已提交，等待返回结果');
                            $('#result').hide();
                        } else{
                            layerTips.msg('异常：MIVS无法解析请求报文');
                            $('#result').hide();
						}
                    } else {
                        $('#result').hide();
                        layerTips.alert(res.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layerTips.close(index);
                        });

                    }
                    //button释放
                    layer.close(index);
                    $('#btnSubmit').removeClass("layui-btn-disabled");
                    $('#btnSubmit').attr("disabled",false);
                },
                error: function () {
                    //button释放
                    layer.close(index);
                    $('#btnSubmit').removeClass("layui-btn-disabled");
                    $('#btnSubmit').attr("disabled",false);
                }

            });

		}

	});

	new unique().init({});

});
