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

    var revokeValidate = function () {
		this.config = {
            acctType: 'jiben',
            billId: '',
            buttonType: '',
            recId: '',
            type:''
		};
	};

	revokeValidate.prototype = $.extend({}, {
		//参数设置[options]
		set: function (options) {
			$.extend(true, this.config, options);
			return this;
		},
		//初始化
		init: function (options) {
			//参数设置
			this.set(options);
			//获取参数
			this.getParam();
			//初始化验证
			this.initValid();
			//初始化数据
			this.initData();
			//绑定事件
			this.bindEvent();
		},
		//获取参数
		getParam: function () {
		},
		//初始化验证
		initValid: function () {
			var that = this;

			var validLabel = ['depositorName', 'bankCode','accountKey','selectPwd'];
			addValidateFlag(validLabel);
			
			var rules = {
				depositorName: {//存款人名称
                    required: true
				},
				bankCode: {//银行机构代码
                    required: true
				},
				accountKey: {//基本存款账户开户许可证核准号
                    required: true
				},
				selectPwd: {//存款人查询密码
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
		//初始化数据
		initData: function (fn) {
            $.ajaxSettings.async = false;
            $.get('../../organization/orgTree', function (data) {
                $('#bankCode').val(data.data[0].code);
            });
            $.ajaxSettings.async = true;
            $('#accountKey').val('');
            $('#selectPwd').val('');
		},
		//绑定事件
		bindEvent: function () {
			//确定按钮
			$('#btnSubmit').click(function(){
				$('#editForm').submit();
			});
		},
		//提交表单
		submitForm: function (data, fn) {
            var postData = {
                depositorName: $('#depositorName').val(),
                bankCode: $('#bankCode').val(),
                accountKey: $('#accountKey').val(),
                selectPwd: $('#selectPwd').val(),
                acctType: this.config.acctType
            };
            $.ajax({
                url: '../../validate/revokeValidate',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (data) {
                    if (data.code === '1') {
                        layerTips.msg('基本户销户校验通过');
                    } else {
                        layerTips.alert(data.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layerTips.close(index);
                        });
                    }
                }
            });
        }
	});

	new revokeValidate().init({});

});