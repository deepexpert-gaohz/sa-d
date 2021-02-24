var businessAcceptTime = {
    baseUrl: "../../networkVerify/businessAcceptTime"
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

    laydate.render({
        elem: '#queDt',
        format: 'yyyy-MM-dd',
        done: function (value, date, endDate) {
            $("#queDt").blur();
        }
    });

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });


    var unique = function () {
		this.config = {
			name: '',
			type: '',
			accountKey: '',
			buttonType: '',
			recId: '',
			billType: '',
			syncEccs: '',
			updateType: ''
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
			var validLabel = ['sysInd', 'queDt'];
			addValidateFlag(validLabel);
			var rules = {
                sysInd: {//核查系统标识
                    required: true
				},
                queDt: {//查询日期
					required: true
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

            //input选择后去焦点，进行校验
            form.on('input', function (data) {
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

            var sysInd = $('#sysInd').val();
            var queDt = $('#queDt').val();

            var postData = {
                queDt: queDt,
                sysInd: sysInd
            };

            $.ajax({
                url: businessAcceptTime.baseUrl + '/verify',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {

                    if (res.code === 'ACK') {
                        var data = res.data;
                        if (data.orgnlQueDt != undefined && data.orgnlQueDt != null) {
                            $('#orgnlQueDt').text(data.orgnlQueDt);
                            $('#procSts').text(procStsSwitch(data.procSts));
                            if (data.procSts == "PR09") {
                                $('#procCd').text(data.procCd);
                                $('#rjctInf').text(data.rjctInf);

                                $('#svcInf1').hide();
                                $('#svcInf2').hide();
                                $('#procCdDiv').show();
                            } else if (data.procSts == "PR07") {
                                $('#sysInd1').text(sysIndSwitch(data.svcInf.sysInd));
                                $('#svcInd').text(svcIndSwitch(data.svcInf.svcInd));
                                $('#sysOpTm').text(data.svcInf.sysOpTm);
                                $('#sysClTm').text(data.svcInf.sysClTm);

                                $('#svcInf1').show();
                                $('#svcInf2').show();
                                $('#procCdDiv').hide();
                            }
                            $('#result').show();
                        } else if(data.msgCode!=null){
                            layerTips.msg('报文已提交，等待返回结果');
                            $('#result').hide();
                        } else {
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