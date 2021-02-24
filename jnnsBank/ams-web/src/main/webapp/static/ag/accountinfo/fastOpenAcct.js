layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var validator;
//lcy
var loading2;
//lcy
var contact = 2;
var certificateType = ["100-第一代居民身份证","101-第二代居民身份证","102-临时身份证","103-中国护照","104-户口簿","105-居民委员证明","106-学生证","107-军官证",
    "108-离休干部荣誉证","109-军官退休证","110-文职干部证","111-军事学员证","112-武警证","113-士兵证","114-香港通行证","115-澳门通行证","116-台湾通行证或有效旅游证件",
    "117-外国人永久居住证","118-边民出入境通讯证","119-外国护照","120-其他对私证件类型","121-警官证","122-香港身份证","123-澳门身份证","124-台湾身份证",
    "125-港澳台居民来往内地通行证","126-港澳台居民证","127-台湾居民居住证","202-经营许可证","211-营业执照","212-事业法人证","213-工商核准号","214-证明文件",
    "218-税务登记证","219-企业法人营业执照","221-个人独资企业营业执照","222-合伙企业营业执照","223-合伙企业分支机构营业执照","229-境外机构证明文件","231-统一社会信用代码",
    "232-其他对公证件类型","235-人事部门或编制委员会的批文","236-事业法人登记证书","237-社会团体法人法人登记证书","238-社会团体分支机构等级证书","239-社会团体代表机构登记书",
    "240-工会社会团体法人资格证","241-社会团体登记证书","242-民办非企业单位登记证书","243-个体工商户营业执照","244-军队单位开户核准通知书","245-武警部队单位开户核准通知书",
    "246-驻X办事机构登记证","247-派出地政府部门的证明文件","248-政府外事部门的批文","249-外国（地区）企业常驻代表机构登记证","250-外商投资企业办事机构注册证","251-民办非企业单位登记证",
    "252-社会力量办学许可证","253-社会福利机构执业证照","254-当选证","255-主管部门或相关管理部门出示的证明","256-预先核准通知书","259-外管局特殊机构代码赋码通知发","260-同业金融j机构代码",
    "261-香港注册代码","262-澳门注册代码","263-台湾注册代码","264-SWIFI号码","265-外管编号"];
var contactType = ["01-联系电话","02-办公电话","03-家庭电话","04-传真号码","05-移动电话","06-业务验证电话","07-SWIFT","08-QQ","09-MSN","10-主Email","11-备用Email","12-电报","13-微信","14-备用Email","15-网址","16-信邮","17-其他"];
// var associateType = ["100-法人或负责人","101-控股股东","102-实际控制人","103-财务人员","104-经办人员","105-其他人员"];
var associateType = ["01-热线验证联系人"];
layui.use(['form', 'murl', 'saic', 'account', 'picker', 'linkSelect', 'org', 'industry', 'common', 'loading', 'laydate', 'imageInfo', 'laytpl'], function () {
    var form = layui.form, url = layui.murl,
        saic = layui.saic, account = layui.account, picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common = layui.common,
        loading = layui.loading,
        laydate = layui.laydate,
        laytpl = layui.laytpl;

    //lcy
    //打印loading2赋值
    loading2 = loading;
    //lcy

    // var operateType = url.get("operateType");
    var operateType = $("#billType").val() == undefined ? url.get("operateType") : $("#billType").val();
    var type = decodeURI(common.getReqParam("type"));
    var billType = decodeURI(common.getReqParam("billType"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var tabId = decodeURI(common.getReqParam("tabId"));
    var productType = $("#productType").val();
    if(tabId!=""){
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }
    for(var b=0;b<contactType.length;b++){
        $("#contactType1").append(new Option(contactType[b], contactType[b].split("-")[0]));
        $("#contactType2").append(new Option(contactType[b], contactType[b].split("-")[0]));
    }
    for(var c=0;c<certificateType.length;c++){
        $("#contactCertificateType1").append(new Option(certificateType[c], certificateType[c].split("-")[0]));
        $("#contactCertificateType2").append(new Option(certificateType[c], certificateType[c].split("-")[0]));
    }
    for(var c=0;c<associateType.length;c++){
        $("#associateType1").append(new Option(associateType[c], associateType[c].split("-")[0]));
        $("#associateType2").append(new Option(associateType[c], associateType[c].split("-")[0]));
    }

    $("#prodType").val("02020101");
    $("#ccy").val("CNY");
    $("#allDraInd").val("YN");
    $("#reasonCode").val("401");
    $("#isThreeSuspension").val("N");
    $("#isBearing").val("Y");
    $("#intType").val("Y");
    $("#HQI").val("HQI");
    // 日期控件
    laydate.render({
        elem: '#acctOpenDate',
        format: 'yyyy-MM-dd',
        max: 'nowDate',
        done: function (value, date, endDate) {
            validator.element($("#acctOpenDate"));
        }
    });
    laydate.render({
        elem: '#interestRaisingDate',
        format: 'yyyy-MM-dd',
        min: 'nowDate',
        done: function (value, date, endDate) {
            validator.element($("#interestRaisingDate"));
        }
    });
    laydate.render({
        elem: '#acctDueDate',
        format: 'yyyy-MM-dd',
        min: 'nowDate',
        done: function (value, date, endDate) {
            validator.element($("#acctDueDate"));
        }
    });

    $("#contactDel").click(function(){
        $("#contactTr"+contact).remove();
        contact -= 1;
        if(contact ==2){
            $("#contactDel").hide();
        }
    })

    $("#contactAdd").click(function(){
        if(contact == 2){
            $("#contactDel").show();
        }
        contact += 1;
        var str = "<tr id=\"contactTr"+contact+"\">\n" +
            "<td>\n" +
            "<select name=\"linkman_typ"+contact+"\" id=\"associateType"+contact+"\"  class=\"layui-input\" lay-filter=\"option\">\n" +
            "</select>\n" +
            "</td>\n" +
            "<td>\n" +
            "<input type=\"text\" class=\"layui-input\" placeholder=\"\" required name=\"linkman_name"+contact+"\" >\n" +
            "</td>\n" +
            "<td>\n" +
            "<select name=\"linkman_document_type"+contact+"\" required id=\"contactCertificateType"+contact+"\" class=\"layui-input\" lay-filter=\"\">\n" +
            "<option value=\"\"></option>\n" +
            "</select>\n" +
            "</td>\n" +
            "<td>\n" +
            "<input type=\"text\" class=\"layui-input\" placeholder=\"\" required name=\"linkman_document_id"+contact+"\" >\n" +
            "</td>\n" +
            "<td>\n" +
            "<input type=\"text\" class=\"layui-input\" placeholder=\"\" required name=\"linkman_phone_no1"+contact+"\">\n" +
            "</td>\n" +
            "<td>\n" +
            "<input type=\"text\" class=\"layui-input\" placeholder=\"\" name=\"linkman_phone_no2"+contact+"\">\n" +
            "</td>\n" +
            "</tr>";
        $("#contactTable").append(str);
        for(var c=0;c<certificateType.length;c++){
            $("#contactCertificateType"+contact).append(new Option(certificateType[c], certificateType[c].split("-")[0]));
        }
        for(var c=0;c<contactType.length;c++){
            $("#contactType"+contact).append(new Option(contactType[c], contactType[c].split("-")[0]));
        }
        for(var c=0;c<associateType.length;c++){
            $("#associateType"+contact).append(new Option(associateType[c], associateType[c].split("-")[0]));
        }
        form.render("select");
    })

    // 账户属性对应
    if(type == "customer"){
        $("#name").val(decodeURI(common.getReqParam("chGivenName")));
        $("#clientNo").val(decodeURI(common.getReqParam("customerNo")));
        $("#customerChineseName").val(decodeURI(common.getReqParam("chGivenName")));
        $("#acctName").val(decodeURI(common.getReqParam("chGivenName")));
    }else{
        contrastControlNormal(type);
    }
    // 提交
    $("#openBankAcct").click(function () {
        $("#customerInfo,#acctInfo,#selectAcct").show();
        $("#openBankAcct,#fastAccount").hide();
        $("#formId").submit();
    });
    $("#selectAcct").click(function () {
        $("#fastAccount,#openBankAcct").show();
        $("#selectAcct,#customerInfo,#acctInfo").hide();
    });
    //上报核心
    $('#verifyFormBtn').click(function () {
        $('form.layui-form').find('button[lay-filter=verifyForm]').click();
        form = layui.form;
        form.render();
        //监听提交
        form.on('submit(verifyForm)', function (data) {
            var obj = data.field;
            var contactInfo = [];
            for(var cc=1;cc<=contact;cc++){
                contactInfo[contactInfo.length]={
                    "LINKMAN_TYPE":eval("obj.linkman_typ"+cc),
                    "CONTACT_NAME":eval("obj.linkman_name"+cc),
                    "GLOBAL_TYPE":eval("obj.linkman_document_type"+cc),
                    "GLOBAL_ID":eval("obj.linkman_document_id"+cc),
                    "CON_TEL":eval("obj.linkman_phone_no1"+cc),
                    "CON_TEL1":eval("obj.linkman_phone_no2"+cc),
                }
            }
            obj.contactInfo = contactInfo;
            var jsonStr =JSON.stringify(obj);
            console.log(obj);
            console.log(jsonStr);
            $.post("../../ws/createAccountNo", jsonStr, function (data) {
                console.log(data);
                // var dataArray = JSON.parse(data);
                // var jsonArr = eval("(" + data + ")");
                // var jsonArr = (new Function("", "return " + data))();
                if (data.success == 'true') {
                    layer.alert(data.msg + "，请点击报送按钮，账号为：" + data.acctNo, {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        // 开户成功后续处理
                        $("#baseAcctNo").val(data.acctNo);
                        $("#verifyFormBtn1").show();
                    });
                } else {
                    // layerTips.msg('开户失败：' + dataArray.msg);
                    layer.confirm(data.msg, {
                        title: "提示",
                        closeBtn: 0,
                        btn: ['确定'] //按钮
                    });
                }
            }).error(function (err) {
                // var errArray = JSON.parse(data);
                layerTips.msg('系統异常：' + data.msg);
            });
            parent.layui.admin.events.closeThisTabs();
            // 阻止表单跳转
            return false;
        });
    });

    //上报账管
    $('#verifyFormBtn1').click(function () {
        $('form.layui-form').find('button[lay-filter=verifyForm]').click();
        form = layui.form;
        form.render();
        //监听提交
        form.on('submit(verifyForm)', function (data) {
            var obj = data.field;
            var contactInfo = [];
            for(var cc=1;cc<=contact;cc++){
                contactInfo[contactInfo.length]={
                    "LINKMAN_TYPE":eval("obj.linkman_typ"+cc),
                    "CONTACT_NAME":eval("obj.linkman_name"+cc),
                    "GLOBAL_TYPE":eval("obj.linkman_document_type"+cc),
                    "GLOBAL_ID":eval("obj.linkman_document_id"+cc),
                    "CON_TEL":eval("obj.linkman_phone_no1"+cc),
                    "CON_TEL1":eval("obj.linkman_phone_no2"+cc),
                }
            }
            obj.contactInfo = contactInfo;
            var jsonStr =JSON.stringify(obj);
            console.log(obj);
            $.post("../../ws/openSyncAms", jsonStr, function (data) {
                console.log(data);
                // var dataArray = JSON.parse(data);
                // var jsonArr = eval("(" + data + ")");
                // var jsonArr = (new Function("", "return " + data))();
                if (data.success == 'true') {
                    layer.alert(data.msg , {
                        title: "提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                        // 开户成功后续处理
                        addCompanyAccountsTab(common, buttonType, billId);
                        //lcy
                        // $("#printBtn").show();
                        //lcy
                    });
                } else {
                    // layerTips.msg('开户失败：' + dataArray.msg);
                    layer.confirm(data.msg, {
                        title: "提示",
                        closeBtn: 0,
                        btn: ['确定'] //按钮
                    });
                }
            }).error(function (err) {
                // var errArray = JSON.parse(data);
                layerTips.msg('系統异常：' + data.msg);
            });
            // 阻止表单跳转
            return false;
        });
    });

    // 获取当前登录用户的机构信息
    $.get('../../organization/orgTree', function (data) {
        $('#branch').val(data.data[0].code);
        if($("#branch").val() != ""&& $("#branch").val() != null && $("#branch").val() != undefined){
            $("#branch").attr("readonly","readonly");
        }
    });

    validator = $("#formId").validate({
        onkeyup: onKeyUpValidate,
        onfocusout: onFocusOutValidate,
        ignore: "",
        errorPlacement: errorPlacementCallback,
        highlight: highlight,
        unhighlight: unhighlight,
        showErrors: showErrors,
        submitHandler: function () {
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });

    //打印(lcy)
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
    //打印(lcy)

    form.render();
});

//lcy
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
        addBoxIndex = layer.open({
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
                loading2.show({
                    target: '#print',
                    message: '正在努力生成打印文件,请稍等...'
                });
                layero.find("iframe").attr('src', '../../selectPwd/getResetPrintPreview?depositorName='+ depositorName + '&selectPwd=' + selectPwd + '&accountKey=' + accountKey
                    + '&openKey=' + openKey + '&acctNo=' + acctNo + '&pbcCode=' + pbcCode + '&legalName=' + legalName +'&templateName=' + encodeURI($('#templateOption').val()));
                layero.find("iframe").on('load', function () {
                    setTimeout(function hide() {
                        loading2.hide('#print');
                    }, 1000);

                });
            },
            end: function () {
                addBoxIndex = -1;
            }
        });
    });
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}
//lcy

/**
 * 开账户页面，控制下拉框选项
 */
function selectAcctTypeManager(btnAcctType) {
    switch (btnAcctType) {
        case ("jiben"):
            contrastControlNormal("4");
            break;
        case ("yiban"):
            contrastControlNormal("3");
            break;
        case ("yusuan"):
            contrastControlSpecial();
            break;
        case ("feiyusuan"):
            contrastControlSpecial();
            break;
        case ("teshu"):
            contrastControlSpecial();
            break;
        case ("linshi"):
            contrastControlNormal("5");
            break;
        case ("feilinshi"):
            contrastControlNormal("5");
            break;
        // case ("yanzi"):
        //     break;
        // case ("zengzi"):
        //     break;
        default:
    }
}

/**
 * 产品类型变更监控
 */
function selectProductTypeManager() {
    // var productType = $(product).val();
    // 账户用户根据产品类型变化
    $("#productType").on("change", function () {
        selectAcctUseManager($("#productType").val());
    });
}

/**
 * 根据产品类型显示账户用途
 */
function selectAcctUseManager(productType) {
    var attr1 = ["307", "308", "4005", "401", "4300", "4301", "4302", "4500", "4501", "4502", "4700", "4800", "6000", "6001"];
    var attr2 = ["3400"];
    var attr3 = ["4701", "5000", "5001"];
    var attr4 = ["4702", "5001"];
    var attr5 = ["4703"];
    var attr6 = ["4704"];
    var attr7 = ["307", "308", "4005", "401", "4300", "4301", "4302", "4500", "4501", "4502", "4700", "4800", "5001", "6000", "6001"];

    // var productType = $("#productType").val();
    switch (productType) {
        case ("02020101"):
            contrastControlUnited("307", attr1);
            break;
        case ("02020105"):
            contrastControlUnited("3400", attr2);
            break;
        case ("02020110"):
            contrastControlUnited("4701", attr3);
            break;
        case ("02020111"):
            contrastControlUnited("4702", attr4);
            break;
        case ("02020112"):
            contrastControlUnited("4703", attr5);
            break;
        case ("02020113"):
            contrastControlUnited("4704", attr6);
            break;
        case ("02020115"):
            contrastControlUnited("307", attr7);
            break;
        default:
    }
}

/**
 * 一般情况
 * @param num
 */
function contrastControlNormal(num) {
    $("#acctNature option").each(function () {
        var $opt2 = $(this);
        var opt = $opt2.val();
        if (num == opt) {
            $opt2.attr("selected", "selected");
        } else {
            $opt2.remove();
        }
    });
}

/**
 * 特殊情况
 */
function contrastControlSpecial() {
    $("#acctAttr option").each(function () {
        var $opt2 = $(this);
        var opt = $opt2.val();
        if (opt != "6" && opt != "7" && opt != "8" && opt != "9") {
            $opt2.remove();
        } else {
            if (opt == "6") {
                $opt2.attr("selected", "selected");
            }
        }
    });
}

/**
 * 账户用途联级
 */
function contrastControlUnited(res, attr) {
    $("#acctUse option").each(function () {
        var $opt = $(this);
        var opt2 = $opt.val();
        if (opt2 == res) {
            $opt.attr("selected", "selected");
        }
        var resultEnd = $.inArray(opt2, attr);
        if (resultEnd == -1) {
            $opt.attr("disabled", "disabled");
        } else {
            $opt.removeAttr("disabled");
        }
    });
}

/**
 * 保存、提交、上报按钮执行结束后，打开对公报备管理页面，在新tab页中关闭当前页
 */
function addCompanyAccountsTab(common, buttonType, billId) {
    var tabId = encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId()));//当前tab页id
    var url = 'company/companyAccounts.html?tabId=' + tabId;
    //buttonType与billId 这2个参数是为了解锁该业务流水用
    if (buttonType) {
        url += '&buttonType=' + buttonType;
    }
    if (billId) {
        url += '&billId=' + billId;
    }
    parent.tab.tabAdd({
        href: url,
        icon: 'fa fa-calendar-plus-o',
        title: '对公报备管理'
    });
}
function dateStr(date){
    console.log((date.replace(/-/,"")).replace(/-/,""));
    return (date.replace(/-/,"")).replace(/-/,"");
}