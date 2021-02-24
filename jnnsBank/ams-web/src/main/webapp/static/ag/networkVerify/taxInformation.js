var taxInformation = {
    baseUrl: "../../networkVerify/taxInformation"
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
			var validLabel = ['coNm','opNm'];
			addValidateFlag(validLabel);//增加必填标识
			var rules = {
                coNm: {//单位名称
                    required: true,
                    maxlength:300
				},
                uniSocCdtCd: {//统一社会信用代码
                    onlyInputOne : ["#txpyrIdNb"],
                    maxlength:18
                },
                txpyrIdNb: {//纳税人识别号
                    onlyInputOne : ["#uniSocCdtCd"],
                    maxlength:20
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

            var coNm = $('#coNm').val();
            var uniSocCdtCd = $('#uniSocCdtCd').val();
            var txpyrIdNb = $('#txpyrIdNb').val();
            var opNm = $('#opNm').val();

            var postData = {
                coNm: coNm,
                uniSocCdtCd: uniSocCdtCd,
                txpyrIdNb: txpyrIdNb,
                opNm: opNm
            };


            $.ajax({
                url:taxInformation.baseUrl+'/verify',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {


                    if (res.code === 'ACK') {

                        var data = res.data;
                        if (data.rslt!=undefined && data.rslt!=null && data.rslt!=''){
                            var html = '<tr>\n' +
                                '<td class="td-label">纳税信息核查结果：</td>\n' +
                                '<td class="td-label-right" colspan="2" id="rslt">'+taxRsltSwitch(data.rslt)+'</td>\n' +
                                '<td class="td-label">数据源日期：</td>\n' +
                                '<td class="td-label-right" colspan="2" id="dataResrcDt">'+data.dataResrcDt+'</td>\n' +
                                '</tr>';
                            if (data.rslt=='MCHD'){
                                for (var i = 1;i<=data.txpmtInf.length;i++){
                                	html += '<tr>\n' +
                                        '<td class="td-label">税务机关代码['+i+']：</td>\n' +
                                        '<td class="td-label-right">'+data.txpmtInf[i-1].txAuthCd+'</td>\n' +
                                        '<td class="td-label">税务机关名称['+i+']：</td>\n' +
                                        '<td class="td-label-right">'+data.txpmtInf[i-1].txAuthNm+'</td>\n' +
                                        '<td class="td-label">纳税人状态['+i+']：</td>\n' +
                                        '<td class="td-label-right">'+taxTxpyrStsSwitch(data.txpmtInf[i-1].txpyrSts)+'</td>\n' +
                                        '</tr>\n' ;
								}
                            }

                            $('#txpmtInf').html(html);
                            $('#success').show();
                            $('#fail').hide();
                            $('#result').show();
						} else if (data.procSts!=undefined && data.procSts!=null && data.procSts!='') {
                            $('#procSts').text(procStsSwitch(data.procSts));
                            $('#procCd').text(data.procCd);
                            $('#rjctInf').text(data.rjctInf);

                            $('#fail').show();
                            $('#success').hide();
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
                        layerTips.alert(data.message, {
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