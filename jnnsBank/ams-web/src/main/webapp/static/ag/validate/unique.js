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

    var unitycreditcode = decodeURI(common.getReqParam('unitycreditcode'));
    var regNo = decodeURI(common.getReqParam('regNo'));
    var fileNo = decodeURI(common.getReqParam('fileNo'));
    var fileDue = decodeURI(common.getReqParam('fileDue'));
    var type = decodeURI(common.getReqParam('type'));
    var regAreaCode = decodeURI(common.getReqParam('regAreaCode'));
    var depositorName = decodeURI(common.getReqParam('depositorName'));

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
			$.extend(true, this.config, options);
			return this;
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
			var name = decodeURI(common.getReqParam('name'));
			var type = decodeURI(common.getReqParam('type'));
			var accountKey = decodeURI(common.getReqParam('accountKey'));
			var buttonType = decodeURI(common.getReqParam('buttonType'));
			var recId = decodeURI(common.getReqParam('recId'));
			var billType = decodeURI(common.getReqParam('billType'));
			var syncEccs = decodeURI(common.getReqParam('syncEccs'));
			var updateType = decodeURI(common.getReqParam('updateType'));

            this.set({
				name: name,
				type: type,
				accountKey: accountKey,
				buttonType: buttonType,
				recId: recId,
				billType: billType,
				syncEccs: syncEccs,
				updateType: updateType
			});
		},
		//初始化元素
		initElem: function (fn) {
			laydate.render({
				elem: '#acctCreateDate',
				max : 'nowDate',
				done: function(value, date, endDate){
					$('#acctCreateDate').blur();
				}
			});

			$('.legal').hide();
		},
		//初始化验证
		initValid: function () {
			var that = this;

			var validLabel = ['depositorName', 'bankCode', 'regAreaCode','fileNo'];
			addValidateFlag(validLabel);
			
			var rules = {
				depositorName: {//存款人名称
                    required: true
					// manyChooseOne : ["#fileNo"]
					// addValidate("stateTaxRegNo",{manyChooseOne : ["#taxRegNo"]},true,validator);
				},
				fileNo: {//注册号
					required: true
					// manyChooseOne : ["#depositorName"]
					// addValidate("stateTaxRegNo",{manyChooseOne : ["#taxRegNo"]},true,validator);
				},
				bankCode: {//银行机构代码
                    required: true
				},
                depositorType: {//银行机构代码
                    required: true
                },
                acctNo: {//账号
                    //required: true,
                    maxlength: 32
                },
                acctCreateDate: {//开户日期
                    maxDate: new Date().Format("yyyy-MM-dd")
                },
                regAreaCode: {//地区代码
                    required: true,
                    fixedLength : 6
                }

            };

			var messages = {
				depositorName:{
					manyChooseOne : "存款人名称与工商营业执照注册号不能同时为空"
				},
				fileNo:{
					manyChooseOne : "存款人名称与工商营业执照注册号不能同时为空"
				}
			};

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

            var orgCode = '';
			if(unitycreditcode) {  //第9—17位数字：代表主体识别码，也就是组织机构代码。stringvar.substr(start [, length ])
                orgCode = unitycreditcode.substr(8, 9);
			}

            $.ajaxSettings.async = false;
            $.get('../../organization/orgTree', function (data) {
                $('#bankCode').val(data.data[0].pbcCode);
                $('#bankName').val(data.data[0].name);
            });
            $.ajaxSettings.async = true;

            //$.get('../ag/json/detail.json', null, function (data) {

            if(unitycreditcode != '' && unitycreditcode != undefined && unitycreditcode != 'null'){
                $('#fileNo').val(unitycreditcode);
            }else{
                $('#fileNo').val(regNo);
            }

				var data = {
					result: {
                        orgCode: orgCode,
                        regAreaCode: regAreaCode,
                        depositorName: depositorName
					}
				}

				if (data && data.result) {
					var item = data.result;
					that.viewData(item);
					form.render();
				}

                $('#btnNext').click(function () {
                    that.jumpOpenHtml();
                });

                $('#btn_pbcIgnore').click(function () {
                    that.jumpOpenHtml();
                });

                if(type == 'menu') {  //菜单链接进入
                    $('#btn_pbcIgnore').css('display', 'none');
                }
			//});
			
		},
		//绑定事件
		bindEvent: function () {
			var that = this;

			//确定按钮
			$('#btnSubmit').click(function(){
				$('#editForm').submit();
			});
		},
		//元素改变联动
		elemChange: function (fn) {
			var that = this;

			form.on('select', function (data) {
				$(data.elem).blur();
			});

			$("#depositorType").attr("lay-filter","depositorType");
			form.on('select(depositorType)',function (data) {
				if(data.value == "14"){
					$('.legal').show();
					addValidate("lgalIdcardNo",{required : true},true,that.config.validator);
					addValidate("legalIdcardTypeAms",{required : true},true,that.config.validator);

					var elemDepositorName = $('#depositorName');
					var strDepositorName = elemDepositorName.val();
					if (strDepositorName.substr(0, 3) !== '个体户') {
						elemDepositorName.val('个体户' + strDepositorName).change().blur();
					}
				}else{
					//$('.legal').hide();
					removeValidate("lgalIdcardNo",true,that.config.validator,"required");
					removeValidate("legalIdcardTypeAms",true,that.config.validator,"required");
				}
				$("#depositorType").blur();
			});
		},
		//提交表单
		submitForm: function (data, fn) {
			if($('#depositorType').val() == '14') {
                $('#legalIdcardTypeAms').val();
                $('#lgalIdcardNo').val();
			}

			depositorName = $('#depositorName').val();
            var depositorType = $('#depositorType').val();
            var legalIdcardTypeAms = $('#legalIdcardTypeAms').val();
            var lgalIdcardNo = $('#lgalIdcardNo').val();
            var orgCode = $('#orgCode').val();
            var acctNo = $('#acctNo').val();
            var acctCreateDate = $('#acctCreateDate').val();
			var fileNo = $('#fileNo').val();
            regAreaCode = $('#regAreaCode').val();
            var oldAccountKey = $('#oldAccountKey').val();
            var postData = {
				depositorName: depositorName,
				depositorType: depositorType,
				legalIdcardTypeAms: legalIdcardTypeAms,
				lgalIdcardNo: lgalIdcardNo,
                acctNo: acctNo,
                acctCreateDate: acctCreateDate,
                regNo: regNo,
				fileDue: fileDue,
				orgCode: orgCode,
                regAreaCode: regAreaCode,
				fileNo: fileNo,
                oldAccountKey:oldAccountKey
            };

            // console.log(postData)

            $.ajax({
                url: '../../validate/jibenUniqueValidate',
                type: 'get',
                data: postData,
                dataType: "json",
                success: function (data) {
                    // var data = {
                    //     rel: true,
                    //     msg: '通过'
                    // }

                    var logData = postData;
                    if (data.code === 'ACK') {
                        layerTips.msg('基本户唯一性校验通过');
                        if(type != 'menu') {  //非菜单链接进入
                            $('#btnNext').css('display', '');
                        }
                        logData.result = "基本户唯一性校验通过";
                    } else {
                        layerTips.alert(data.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layerTips.close(index);
                        });
                        logData.result = data.message;
                    }

                    //记录日志
                    $.post('../../customerSearch/jibenUniqueValidateLog', logData);
                }

            });


		},
		viewData: function (data) {
			if (data) {
				for (var key in data) {
					if(data[key] != null){
						$('#'+key).val(data[key]);
					}
				}
			}
		},
        jumpOpenHtml: function() {
            var suffix ="?name="+encodeURI(this.config.name)+"&type="+this.config.type+"&accountKey="+this.config.accountKey+

                "&buttonType=saicValidate"+"&recId=" + this.config.recId +"&billType=ACCT_OPEN";

            if(this.config.syncEccs == '' && this.config.updateType == '') {
                parent.tab.tabAdd({
                    title: '开户-' + name,
                    href: 'accttype/' + this.config.type + 'Open.html' + suffix
                });
            } else {
                parent.tab.tabAdd({
                    title: '开户-' + name,
                    href: 'accttype/' + this.config.type + 'Open.html' + suffix + '&syncEccs=' + this.config.syncEccs + '&updateType=' + this.config.updateType
                });
            }
        }

	});

	new unique().init({});

	//基本户唯一性校验日志查询
    $('#btn_log').on('click', function () {
        parent.tab.tabAdd({
            title: "基本户唯一性校验日志查询",
            href: 'validate/uniqueLog.html'
        });
        return false;
    });
});