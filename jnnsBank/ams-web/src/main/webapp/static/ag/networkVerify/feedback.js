var feedback = {
    baseUrl: "../../networkVerify"
};

layui.config({
    base: '../js/'
});

var id;
var type;

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
            id = decodeURI(common.getReqParam("id"));
            type = decodeURI(common.getReqParam("type"));
		},
		//初始化元素
		initElem: function (fn) {

		},
		//初始化验证
		initValid: function () {
			var that = this;
			var validLabel = ['msgLogId', 'cntt', 'contactNm','contactNb'];
			addValidateFlag(validLabel);
			var rules = {
                msgLogId: {//ID编号
                    required: true
                },
                cntt: {//疑义反馈内容
                    required: true,
                    maxlength: 256
                },
                contactNm: {//联系人姓名
                    required: true,
                    maxlength: 140
                },
                contactNb: {//联系人电话
                    required: true,
                    maxlength: 30,
                    digits:true
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
            if (id != null){
                $("#id").val(id);
            }
            if (type!=null){
                $("#typeName").text(typeNameSwitch(type));
            }
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

            //重置按钮(id编号不重置)
            $('button[type="reset"]').click(function () {
                var myInput = document.getElementById("id");
                myInput.defaultValue = myInput.value;
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

            var id = $('#id').val();
            var cntt = $('#cntt').val();
            var contactNm = $('#contactNm').val();
            var contactNb = $('#contactNb').val();

            var postData = {
                msgLogId: id,
                cntt: cntt,
                contactNm: contactNm,
                contactNb: contactNb,
				msgType: type
            };

            $.ajax({
                url: feedback.baseUrl+'/'+type+'/feedback',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {
					if(res.code=="NACK"){
                        layerTips.alert(res.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layerTips.close(index);
                        });
					}else {
                        if (res.data.msgCode!=undefined && res.data.msgCode=="000001") {
                            layerTips.msg('报文已提交，稍后查询返回结果');
                        }else {
                            //TODO 实时返回结果的展示
                        }
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
