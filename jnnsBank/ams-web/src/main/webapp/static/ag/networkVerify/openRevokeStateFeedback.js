var openRevokeState = {
    baseUrl: "../../networkVerify"
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

    laydate.render({
        elem: '#chngDt',
        format: 'yyyy-MM-dd',
        done: function (value, date, endDate) {
            $("#chngDt").blur();
        }
    });

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

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
			var validLabel = ['uniSocCdtCd', 'acctSts', 'chngDt'];
			addValidateFlag(validLabel);
			var rules = {
                entNm: {//企业名称
                    maxlength: 100
				},
                traNm: {//字号名称
                    maxlength: 256
				},
                uniSocCdtCd: {//统一社会信用代码
                    required: true,
                    maxlength: 18
				},
                acctSts: {//账户状态标识
                    required: true
                },
                chngDt: {//变更日期
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


            var entNm = $('#entNm').val();
            var traNm = $('#traNm').val();
            var uniSocCdtCd = $('#uniSocCdtCd').val();
            var acctSts = $('#acctSts').val();
            var chngDt = $('#chngDt').val();

            var postData = {
                entNm: entNm,
                traNm: traNm,
                uniSocCdtCd: uniSocCdtCd,
                acctSts: acctSts,
                chngDt: chngDt
            };

            $.ajax({
                url: openRevokeState.baseUrl+'/openRevokeState/feedback',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {

                    if (res.code === 'ACK') {
                        var data = res.data;
                        if (data.msgCode!=undefined && data.msgCode=="000001") {
                            layerTips.msg('报文已提交，稍后查询返回结果');
                        }else if (data.prcSts!=undefined && data.prcSts!=null) {
                        	$('#prcSts').text(procStsSwitch((data.prcSts || '')));
                            $('#prcCd').text((data.prcCd || ''));
                            $('#ptyId').text((data.ptyId || ''));
                            $('#ptyPrcCd').text((data.ptyPrcCd || ''));
                            $('#rjctInf').text((data.rjctInf || ''));
                            $('#prcDt').text((data.prcDt || ''));
                            $('#netgRnd').text((data.netgRnd || ''));


                            $('#success').show();
                            $('#result').show();
						}else {
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
                    layerTips.alert("提交失败", {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layerTips.close(index);
                    });

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
