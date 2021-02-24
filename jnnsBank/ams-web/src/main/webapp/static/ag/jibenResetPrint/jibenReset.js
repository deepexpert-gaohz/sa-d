layui.config({
    base: '../js/'
});
var loading2;
layui.use(['jquery', 'layer', 'element', 'form', 'laydate', 'common','loading'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        element = layui.element,
        form = layui.form,
        laydate = layui.laydate,
        loading = layui.loading,
        common = layui.common,
        laytpl = layui.laytpl;

    //打印loading2赋值
    loading2 = loading;
    //详情页面进来的需要反显
    var key = decodeURI(common.getReqParam('accountKey'));
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var validate = function () {
        this.config = {
            accountKey: '',
            selectPwd: '',
            rselectPwd: ''
        };
    };
    if(key!=null || key!="" || key!=undefined){
        $('#accountKey').val(key);
    }

    validate.prototype = $.extend({}, {
        //参数设置[options]
        set: function (options) {
            $.extend(true, this.config, options);
            return this;
        },
        //初始化
        init: function (options) {
            //参数设置
            this.set(options);
            //初始化验证
            this.initValid();
            //绑定事件
            this.bindEvent();
            //初始化数据
            this.initData();
        },
        //初始化验证
        initValid: function () {
            var that = this;

            var validLabel = ['accountKey', 'selectPwd','rselectPwd'];
            addValidateFlag(validLabel);

            var rules = {
                accountKey: {//基本存款账户标号
                    required: true,
                    checkJiBenParAccountKey:true
                },
                selectPwd: {//新密码
                    required: true
                },
                rselectPwd: {//确认新密码
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
            $.get('../../organization/orgTree', function (data) {
                $('#pbcCode').val(data.data[0].code);
                $('#name').val(data.data[0].name);
            });
        },
        //绑定事件
        bindEvent: function () {
            var that = this;

            //确定按钮
            $('#btnSubmit').click(function(){
                $('#editForm').submit();
            });
            $('#printPwd').click(function(){
                var depositorName=$('#depositorName').val();
                var selectPwd = $('#selectPwd').val();
                if(depositorName==null || depositorName==""){
                    layerTips.msg('打印异常');
                    return;
                }
                //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                $.get('../account/print.html', null, function (form) {

                    var url = '../../selectPwd/getPrintPreview?depositorName='+depositorName+"&selectPwd="+selectPwd;
                    parent.layer.open({
                        type: 1,
                        title: '打印预览',
                        content: form,
                        shade: false,
                        offset: ['20px', '20%'],
                        area: ['800px', '600px'],
                        maxmin: true,
                        scrollbar: false,
                        full: function (elem) {
                            var win = window.top === window.self ? window : parent.window;
                            $(win).on('resize', function () {
                                var $this = $(this);
                                elem.width($this.width()).height($this.height()).css({
                                    top: 0,
                                    left: 0
                                });
                                elem.children('div.layui-layer-content').height($this.height() - 95);
                            });
                        },
                        success: function (layero, index) {
                            /*loading2.show({
                                target:'#print',
                                message:'正在努力生成打印文件,请稍等...'
                            });*/
                            layero.find("iframe")[0].contentWindow.location.replace(url);
                            layero.find("iframe").on('load',function () {
                                // setTimeout(function hide() {
                                //     loading2.hide('#print');
                                // },1000);

                            });
                        },
                        cancel: function(index, layero){ 
                            layero.find("iframe").hide();
                        },
                        end: function (index) {
                            layer.close(index);
                        }
                    });
                });
            });
        },
        //提交表单
        submitForm: function (data, fn) {
            var acctNo = $('#acctNo').val();
            var pbcCode = $('#pbcCode').val();
            var name = $('#name').val();

            var postData = {
                acctNo: acctNo,
                pbcCode:pbcCode
            };
            $.ajax({
                url: '../../validate/getJibenDetails',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (data) {
                    if (data.code == '1') {
                        data = data.data;
                        console.log(data);
                        if(data){
                            name=data.depositorName;
                            data.customerTitle = '客户信息';
                            data.parentTitle = '上机机构信息';
                            loadBasicTemplate(data,'#tab_0','jibenResetCustomerPBOC.html');
                            $("#btn_next").show();
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
        }
    });
    new validate().init({});

    function loadBasicTemplate(data, element, url) {
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get(url, null, function (template) {
            if (template) {
                laytpl(template).render(data, function (html) {
                    $(element).html(html);
                });
            }
        });
    }

    $("#btn_next").on('click',function(data){
        console.log(data)
        var acctNo = $('#acctNo').val();
        var pbcCode = $('#pbcCode').val();

        var postData = {
            acctNo: acctNo,
            pbcCode:pbcCode
        };

        $.ajax({
            url: '../../validate/jiBenResetPrint',
            type: 'post',
            data: postData,
            dataType: "json",
            success: function (data) {
                if (data.code == '1') {
                    data = data.data;
                    parent.tab.tabAdd({
                        title: '基本存款账户开户信息',
                        href: 'jibenResetPrint/jibenResetPrint.html?accountKey=' + data.accountKey + '&openKey=' + data.openKey + '&acctNo=' + data.acctNo + '&depositorName=' + data.depositorName
                            + '&legalName=' + data.legalName + '&pbcCode=' + data.pbcCode + '&selectPwd=' + data.selectPwd
                    });
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
    });


});