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

	var printAccountData;

    var revokeValidate = function () {
		this.config = {
            acctType: '',
            billId: '',
            buttonType: '',
            recId: '',
            type:'',
            acctNo:''
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
           
			var acctType = decodeURI(common.getReqParam('acctType'));
			var billId = decodeURI(common.getReqParam('billId'));
			var buttonType = decodeURI(common.getReqParam('buttonType'));
			var recId = decodeURI(common.getReqParam('recId'));
			var name = decodeURI(common.getReqParam('name'));
            var type = decodeURI(common.getReqParam('type'));
            var depositorType = decodeURI(common.getReqParam('depositorType'));
            var acctNo = decodeURI(common.getReqParam('acctNo'));
            var operateType = decodeURI(common.getReqParam('operateType'));
            //初始化销户原因下拉选项
            if (depositorType && (depositorType === '01'//企业法人
                || depositorType === '02'//非企业法人
                || depositorType === '13'//有字号的个体工商户
                || depositorType === '14'//无字号的个体工商户
            )) {
                $('#acctCancelReason').append('<option value="1">1-转户</option>');
            }
            $('#acctCancelReason').append('<option value="2">2-撤并</option>');
            $('#acctCancelReason').append('<option value="3">3-解散</option>');
            $('#acctCancelReason').append('<option value="4">4-宣告破产</option>');
            $('#acctCancelReason').append('<option value="5">5-关闭</option>');
            $('#acctCancelReason').append('<option value="6">6-被吊销营业执照或执业许可证</option>');
            $('#acctCancelReason').append('<option value="7">7-其它</option>');

            this.set({
                acctType: acctType,
                billId: billId,
				buttonType: buttonType,
				recId: recId,
				name: name,
                type:type,
				acctNo:acctNo,
                operateType:operateType
			});

            if(type == "menu"){
                $("#btn_pbcIgnore").hide();
                this.config.acctType = 'jiben';
            }
		},
		//初始化验证
		initValid: function () {
			var that = this;

			var validLabel = ['depositorName', 'bankCode','accountKey','selectPwd','acctCancelReason'];
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
				},
                acctCancelReason: {//销户原因
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
			var that = this;

            $.ajaxSettings.async = false;
            $.get('../../organization/orgTree', function (data) {
                $('#bankCode').val(data.data[0].code);
                $('#bankName').val(data.data[0].name);
            });
            $.ajaxSettings.async = true;

            //$.get('../ag/json/detail.json', null, function (data) {

				var data = {
					result: {
                        depositorName: that.config.depositorName
					}
				}

				if (data && data.result) {
					var item = data.result;
					form.render();
				}

                $('#btnNext').click(function () {
                    that.jumpOpenHtml();
                });

                $('#btn_pbcIgnore').click(function () {
                    that.jumpOpenHtml();
                });
			//});
			
		},
		//绑定事件
		bindEvent: function () {
			var that = this;

			//确定按钮
			$('#btnSubmit').click(function(){
				$('#editForm').submit();
			});

            //确定按钮
            $('#accountPrint').click(function(){
               	printAccount();
            });
		},
		//提交表单
		submitForm: function (data, fn) {
			
			var depositorName = $('#depositorName').val();
			var bankCode = $('#bankCode').val();
            var accountKey = $('#accountKey').val();
            var selectPwd = $('#selectPwd').val();
            var acctCancelReason = $('#acctCancelReason').val();
            var postData = {
				depositorName: depositorName,
				bankCode: bankCode,
                accountKey: accountKey,
                selectPwd: selectPwd,
                acctType: this.config.acctType
            };
            // if (acctCancelReason === '1') {//销户原因为转户时，跳过验证
            //     layerTips.msg('基本户转户销户，跳过校验');
            //     $('#btnNext').click();
            // } else {
                $.ajax({
                    url: '../../validate/revokeValidate',
                    type: 'post',
                    data: postData,
                    dataType: "json",
                    success: function (data) {
                        if (data.code == '1') {
                            printAccountData = data.data;
                            $('#btnNext').css('display', '');
                            if(acctCancelReason === '1'){
                                $('#accountPrint').css('display', '');
                                layer.confirm('基本户销户校验通过，是否打印账户清单？', {
                                    title: "提示",
                                    closeBtn: 0,
                                    btn: ['是', '否'] //按钮
                                }, function (index) {
                                    printAccount();
                                    layer.close(index);
                                }, function (index) {
                                });
                            }else{
                                layerTips.msg('基本户销户校验通过');
							}
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
            // }
		},
        jumpOpenHtml: function() {
            parent.tab.tabAdd({
                title: '销户-' + this.config.name,
                href: 'account/accountRevoke.html?billId=' + this.config.billId
				+ '&buttonType=selectForRevoke'
				+ '&recId=' + this.config.recId
				+ '&acctCancelReason=' + $('#acctCancelReason').val() + '&acctNo=' + this.config.acctNo+'&operateType='+ this.config.operateType
            });
        }
	});

	new revokeValidate().init({});

	function printAccount() {
        var params = {
            allAccountData: JSON.stringify(printAccountData)
        };
        postExcelFile(params, '../../validate/cancelAccount/pdf');
    }
});