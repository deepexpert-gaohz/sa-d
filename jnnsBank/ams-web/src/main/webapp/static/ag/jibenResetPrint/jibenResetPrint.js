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
    var accountKey = decodeURI(common.getReqParam('accountKey'));
    var openKey = decodeURI(common.getReqParam('openKey'));
    var acctNo = decodeURI(common.getReqParam('acctNo'));
    var depositorName = decodeURI(common.getReqParam('depositorName'));
    var pbcCode = decodeURI(common.getReqParam('pbcCode'));
    var legalName = decodeURI(common.getReqParam('legalName'));
    var selectPwd = decodeURI(common.getReqParam('selectPwd'));

    //详情页面进来的需要反显
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var validate = function () {
        this.config = {
            accountKey: '',
            selectPwd: '',
            rselectPwd: ''
        };
    };
    validate.prototype = $.extend({}, {
        //参数设置[options]
        set: function (options) {
            $.extend(true, this.config, options);
            return this;
        },
        //初始化
        init: function (options) {
            //初始化验证
            this.initValid();
        },
        //初始化验证
        initValid: function () {
            var that = this;
            $("#accountKey").val(accountKey);
            $("#openKey").val(openKey);
            $("#acctNo").val(acctNo);
            $("#depositorName").val(depositorName);
            $("#pbcCode").val(pbcCode);
            $("#selectPwd").val(selectPwd);
            $("#legalName").val(legalName);
        },
    });
    new validate().init({});


    $("#printBtn").on('click',function(){

        $.get('../../template/getTemplateNameList?billType=ACCT_OPEN&depositorType=', function (data) {
            if(data == '' || data == null) {
                layer.alert("对应打印模版未配置");
            } else {

                //页面层
                index = layer.open({
                    title: '请选择打印模版',
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['300px', '200px'], //宽高
                    content: '<select id="templateOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" onclick="templateClick()" id="templateBtn"> 确定</button>'
                });

                for (var i = 0; i < data.length; i++) {
                    $('#templateOption').append("<option value=" + data[i] + ">" + data[i] + "</option>")
                }
            }
        })
    });
});

function templateClick() {
    if ($('#templateOption').val() == '') {
        layer.alert("请选择对应模版类型");
        return;
    }

    var depositorName = $('#depositorName').val();
    var selectPwd = $('#selectPwd').val();
    var accountKey = $("#accountKey").val();
    var openKey = $("#openKey").val();
    var acctNo = $("#acctNo").val();
    var pbcCode = $("#pbcCode").val();
    var legalName = $("#legalName").val();

    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
    $.get('../account/print.html', null, function (form) {

        var url = '../../selectPwd/getResetPrintPreview?depositorName='+ depositorName + '&selectPwd=' + selectPwd + '&accountKey=' + accountKey
        + '&openKey=' + openKey + '&acctNo=' + acctNo + '&pbcCode=' + pbcCode + '&legalName=' + legalName +'&templateName=' + encodeURI($('#templateOption').val());
        addBoxIndex = parent.layer.open({
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
                // loading2.show({
                //     target: '#print',
                //     message: '正在努力生成打印文件,请稍等...'
                // });
                layero.find("iframe")[0].contentWindow.location.replace(url);
                layero.find("iframe").on('load', function () {
                    // setTimeout(function hide() {
                    //     loading2.hide('#print');
                    // }, 1000);

                });
            },
            cancel: function(index, layero){ 
                layero.find("iframe").hide();
            },
            end: function () {
                addBoxIndex = -1;
            }
        });
    });
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}
