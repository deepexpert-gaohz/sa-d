var apply = {
    baseUrl: "../../apply",
};

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var loading2;

var myType;//开户性质（校验时基本临时特殊隐藏判断标识）

var linkSelect;

layui.use(['form','murl','layer','element','laydate','laytpl','saic', 'picker', 'common', 'linkSelect', 'org', 'industry', 'loading'], function () {
    var laydate = layui.laydate,element = layui.element,form = layui.form,
        murl=layui.murl,applyId = murl.get("applyId"), url=layui.murl, name = decodeURI(murl.get("name")),laytpl = layui.laytpl,
        saic = layui.saic,picker = layui.picker, common=layui.common,
        org = layui.org,
        industry = layui.industry,
        loading = layui.loading;
    linkSelect = layui.linkSelect;

        loading2 = loading;

        var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

        var tabId = common.getReqParam("tabId");

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();


    $.ajaxSettings.async = false;
    $.get('../../config/findConfigValue?configKey=legalDivEnabled', function (data) {
        if(isEmpty(data) || data == 'true') {  //数据第一次初始化默认显示
            $('#legalRowDiv').css('display', '');
            $.get('../account/legal.html', null, function (form) {
                $("#legalDiv").html(form);
            });
        }
    });

    $.get('../../config/findConfigValue?configKey=customerDivEnabled', function (data) {
        if(isEmpty(data) || data == 'true') {  //数据第一次初始化默认显示
            $('#saicRowDiv').css('display', '');
            $.get('../account/saic.html', null, function (form) {
                $("#saicDiv").html(form);
            });
        }
    });

    $.get('../../config/findByKey?configKey=imageDivEnabled', function (data) {
        if(data == true) {
            $('#imageRowDiv').css('display', '');
        }
    });
    $.ajaxSettings.async = true;

    initAddress('reg',regAddressPicker,null,3);

    laydate.render({
        elem: '#fileSetupDate',
        format: 'yyyy-MM-dd'
    });

    laydate.render({
        elem: '#fileDue',
        format: 'yyyy-MM-dd'
    });

    laydate.render({
        elem: '#businessLicenseDue',
        format: 'yyyy-MM-dd'
    });

    laydate.render({
        elem: '#legalIdcardDue',
        format: 'yyyy-MM-dd'
    });
    
    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $("#name").html(name);
  //  saic.loadBasic(name,'#tab_1','工商信息');

    $("input[name='applyid']").val(applyId);

    $.get (apply.baseUrl+'/mina/edit?applyId=' + applyId, null, function (data) {
        if(data.rel){
            var result = data.result;
            setLabelValues(result);
            // queryOcr(saic,applyId);
            if(result.hasocr != 0){
                $(".showNumber").show();
                $(".showNumberButton").show();
                getOcrs(result.id);
            }else{
                $(".showNumber").hide();
                $(".showNumberButton").hide();
            }
        }
    });

    $('#depositorName').attr('lay-verify', '')
    $('#depositorName').attr('required', false)
    $('#depositorNameSpan').hide()
    $('#acctShortNameDiv').remove()
    $('input,select,textarea').each(function(){
    	$(this).prop("disabled",true);
        $(this).addClass("disableElement");
    })
    $('#applynote').prop("disabled",false)
    $('#banktime').prop("disabled",false)
    $('#times').prop("disabled",false)
    $('#carrierBtn').hide();

    $('#accountKey').removeAttr("disabled");
    $('#regAreaCode').removeAttr("disabled");

    $.get("../../config/findByKey?configKey=printf", null, function (data) {
        if (data == true) {  //配置打印
            $('#printBtn').css('display', '');
        }
    })

    form.render('select');

    removeAll()
    // changeLegalFieldStatus();

    $.get(apply.baseUrl+'/mina/getCustomerDivConfig', function (data) {
        data = data.result;
        if(data.customerDivEnabled != 'false' || data.legalDivEnabled != 'false') {  //客户工商信息区块和法人信息区块同时隐藏时不调用工商
            initBySaic(saic,name);
        }
    })

    var saveBoxIndex = -1;
    $('#save').click(function () {
        if (saveBoxIndex !== -1)
            return;

        var html = '<div style="margin:20px;">\n'
        + '<div class="form-group">\n'
        + '<label class="control-label col-sm-4">受理日期</label>\n'
        + '<div class="col-sm-8">\n'
        + '<input type="text" class="form-control datetimepicker" id="banktime" placeholder="请选择日期" autocomplete="off">\n'
        + '</div>\n' 
        + '</div>\n'
        + '</div>\n';

        saveBoxIndex = layer.open({
            type: 1,
            title: '受理',
            content: html,
            btn: ['提交', '取消'],
            shade: false,
            offset: ['20px', '20%'],
            area: ['500px', '300px'],
            maxmin: true,
            yes: function (index) {

                var dateTime = $('#banktime').val();
                var date = '';
                var time = '';
                if (dateTime.length == 19) {
                    date = dateTime.substr(0, 10);
                    time = dateTime.substr(11);
                }
                $("#formId").append('<input type="hidden" name="banktime" value="' + date + '"/>');
                $("#formId").append('<input type="hidden" name="times" value="' + time + '"/>');
                $('#submitBtnType').val('save');
                $("#formId").submit();
                // videoOpen();

            },
            full: function (elem) {
            },
            success: function (layero, index) {
                
                $('.datetimepicker').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd hh:ii:ss',
                    startDate: new Date()
                });
            },
            end: function () {
                saveBoxIndex = -1;
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    var cancelBoxIndex = -1;
    $('#cancel').click(function(){

        if (cancelBoxIndex !== -1)
            return;
        cancelBoxIndex = layer.open({
            type: 1,
            title: '受理',
            content: '\n' +
            '    <div class="row">\n' +
            '        <div class="col-md-12">\n' +
            '            <div class="portlet light bordered">\n' +
            '                <div class="portlet-body">\n' +
            '                    <div class="row">\n' +
            '                        <div class="col-md-12">\n' +
            '                            <div class="form-group">\n' +
            '                                <label class="control-label col-xs-3 col-md-2">受理回执</label>\n' +
            '                                <div class="col-xs-9 col-md-10">\n' +
            // '                                    <input type="text" placeholder="请输入回执" id="applynote" class="layui-input"/>\n' +
            '                                    <select class="layui-input" id="applynote" title="">\n' +
            '                                        <option value="">请选择</option>\n' +
            '                                    </select>'+
            '                                </div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>',
            btn: ['提交', '取消'],
            shade: false,
            offset: ['20px', '20%'],
            area: ['600px', '400px'],
            maxmin: true,
            yes: function (index) {
                $("#formId").append('<input type="hidden" name="applynote" value="' + $('#applynote').find("option:selected").text() + '"/>');
                $('#submitBtnType').val('cancel');
                $("#formId").submit();
            },
            full: function (elem) {
            },
            success: function (layero, index) {
                //获取页面中“受理回执”下拉数据
                $.get('../../dictionary/findOptionsByDictionaryName?name=applyNoteValue2Item', function (res) {
                    if (res.code === "ACK") {
                        for (var i = 0; i < res.data.length; i++) {
                            $('#applynote').append("<option value='" + res.data[i].value + "'>" + res.data[i].name + "</option>");
                        }
                        form.render('select');
                    }
                });
            },
            end: function () {
                cancelBoxIndex = -1;
            }
        });
    });

    $('#back').click(function(data){
        parent.tab.deleteTab(tabId);

        parent.tab.tabAdd({
            href: 'bank/pre.html?tabId=' + encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId())),
            icon: 'fa fa-calendar-plus-o',
            title: '待受理'
        });

        // parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //打印
    $('#printBtn').click(function(){
        printfClick(common, 'APPT_UNCOMPLETE');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    function getOcrs(id){
        $.post(apply.baseUrl+"/bank/images/"+id,function(result){
            if(result.rel){
                var resultObj = result.result;
                var fileNames = resultObj.fileNames;
                $("#totalNumId").html(resultObj.totalNum);
                for(var j=1;j<=resultObj.syncNum;j++){
                    $("#curNumId").html(j);
                    if(j > fileNames.length){
                        imageAdd(id,j);
                    }else{
                        imageAdd(id,j,fileNames[j-1]);
                    }
                }
                if(resultObj.syncNum < resultObj.totalNum){//未同步完全
                    getOtherOcrs(id,resultObj.syncNum+1);
                }else{
                    $("#loadingId").hide();
                }
                // $("#loadingId").hide();
            }else{
                $(".showNumber").hide();
                layerTips.msg("预约影像同步失败："+result.msg);
            }
        });
    }

    function getOtherOcrs(id,reqPageNum){
        $.post(apply.baseUrl+"/bank/images/"+id,{reqPageNum:reqPageNum},function(result){
            if(result.rel){
                var resultObj = result.result;
                var fileNames = resultObj.fileNames;
                if(reqPageNum > fileNames.length){
                    imageAdd(id,reqPageNum);
                }else{
                    imageAdd(id,reqPageNum,fileNames[reqPageNum-1]);
                }
                $("#curNumId").html(result.result.curNum);
                if(result.result.curNum < resultObj.totalNum){
                    getOtherOcrs(id,result.result.curNum+1);
                }else{
                    $("#loadingId").hide();
                }
            }else{
                layerTips.msg("预约影像同步失败："+result.msg);
            }
        });
    }

    function imageAdd(id,curNum,filename){
        var elem = $("#imageList");

        var url = "../../apply/bank/images/downLoadImage/"+id+"?reqPageNum="+curNum;
        var imgHtml;
        if(filename){
            imgHtml = '\n<a data-magnify="gallery" data-caption="'+filename+'" href="'+url+'">'
                +     '<img width="200" src="'+url+'" alt="">'
                + '</a>\n';
        }else{
            imgHtml = '\n<a data-magnify="gallery" data-caption="其他" href="'+url+'">'
                +     '<img width="200" src="'+url+'" alt="">'
                + '</a>\n';
        }
        elem.append(imgHtml);

        $('[data-magnify]').magnify({
            headToolbar: [
                'close'
            ],
            initMaximized: true
        });

        return false;
    }

    $("#resetSync").click(function(){
        $("#imageList").empty();
        var id = $("#idHiddenId").val();
        var hasOcr = $("#hasocrHiddenId").val();
        if(hasOcr != 0){
            $(".showNumber").show();
            $(".showNumberButton").show();
            getOcrs(id);
        }
    });

   /* $('#imageAdd').click(function(data){
        var elem = $(this);

        var imgHtml = '\n<a data-magnify="gallery" data-caption="Paraglider flying over Aurlandfjord, Norway by framedbythomas" href="http://www.jq22.com/demo/magnify201712261506/examples/img/a3.png">'
                    +     '<img width="200" src="http://www.jq22.com/demo/magnify201712261506/examples/img/a3.png" alt="">'
                    + '</a>\n';
        elem.before(imgHtml);

        $('[data-magnify]').magnify({
            headToolbar: [
              'close'
            ],
            initMaximized: true
        });
        
        return false;
    });*/

function submitForm(data){
    var btnTyle = $('#submitBtnType').val();
    if(btnTyle == 'save') {
        $('#save').addClass('layui-btn-disabled');
        if ($('#banktime').val() == '') {
            layerTips.msg('受理日期不能为空');
            $('#save').removeClass('layui-btn-disabled');
            return false;
        } else if ($('#times').val() == '') {
            layerTips.msg('受理时间不能为空');
            $('#save').removeClass('layui-btn-disabled');
            return false;
        }
    } else if (btnTyle == 'cancel') {
        $('#cancel').addClass('layui-btn-disabled');
        if ($('#applynote').val() == '') {
            layerTips.msg('受理回执不能为空');
            $('#cancel').removeClass('layui-btn-disabled');
            return false;
        }
    }
    
    $.ajax({
        url: apply.baseUrl+'/bank/edit',
        type: 'post',
        data: data,
        dataType: "json",
        success: function () {
            var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

            if(btnTyle == 'save') layerTips.msg('受理成功');
            else if(btnTyle == 'cancel') layerTips.msg('退回成功');
            else layerTips.msg('保存成功');
        },
        complete:function () {
            videoOpen();
        }
    });

}

function queryOcr(saic,applyId){
    var queryStr="applyId="+applyId;

    $.get (apply.baseUrl+'/mina/ocr?' + queryStr, null, function (data) {
        if(data.rel){

            saic.loadImageView(data.result,'.ocrImages');

            $('[data-magnify]').magnify({
                headToolbar: [
                  'close'
                ],
                initMaximized: true
            });
        }

    });

}

function setLabelValues(data) {
    var typeCode;

    for (var p in data) {
        
        if($("input#" + p).length > 0) {
            $("input#" + p).val(data[p]);
        } else {
            $("#" + p).html(data[p]);
        }
    }
    myType = data.type;

    if(data.type == 'jiben'){
        typeCode = '基本存款账户';
    } else if(data.type == 'yiban') {
        typeCode  = '一般存款帐户';
    } else if(data.type == 'yusuan') {
        typeCode  = '预算单位专用存款账户';
    } else if(data.type == 'feiyusuan') {
        typeCode  = '非预算单位专用存款账户';
    } else if(data.type == 'linshi') {
        typeCode  = '临时机构临时存款账户';
    } else if(data.type == 'feilinshi') {
        typeCode  = '非临时机构临时存款账户';
    } else if(data.type == 'teshu') {
        typeCode  = '特殊单位专用存款账户';
    }

    if(data.type === 'jiben' || data.type === 'linshi' || data.type === 'teshu'){
        $('#pbcValidationFields').remove();
    } else {
        $('#pbcValidationFields').show();
    }

    $('#type').html(typeCode);

    if(data.applytime) {
        $("#applytime").html(data.applytime);
    }

    if(data.accountKey){
        $("#accountKey").val(data.accountKey);
    }

    if(data.id){
        $("#idHiddenId").val(data.id);
    }

    if(data.hasocr){
        $("#hasocrHiddenId").val(data.hasocr);
    }

}

/**
 * 根据工商数据回填
 * @param name
 */
function initBySaic(saic, name) {
    saic.loadBasicData(name,function (data) {
        initSaicTemplate(data);

        //开户默认当前时间
        $(".acctCreateDate").val(new Date());

        //注册地址区域修改->保存内容到注册地地区代码
        form.on('select(regArea)',function (data) {
            $('#regAreaCode').val(data.value);
        });

        //省市区组件
        var regAddressCode,regAddressType;
        if(data.regArea){
            regAddressCode = data.regArea;
            regAddressType = 3;
        } else if(data.regCity){
            regAddressCode = data.regCity;
            regAddressType = 3;
        } else if(data.regProvince){
            regAddressCode = data.regProvince;
            regAddressType = 3;
        }

        initAddress('reg',regAddressPicker,regAddressCode,regAddressType);

        //组织机构类别
        initLinkSelect("orgSelect",org.data(),[],function (item) {
            //选中回调函数
            if(item.value >= 10){
                //说明是二级的值
                $('#orgTypeDetail').val(item.value);
            } else {
                //说明是一级的值
                $('#orgType').val(item.value);
            }
        });

        //经济行业分类
        initLinkSelect("economyIndustrySelect",industry.data(),[],function (item) {
            //选中回调函数
            if(item.value.length === 5){
                //设置最终的值
                $('#economyIndustryCode').val(item.value);
                $('#economyIndustryName').val(item.name);
            }
        });


        // 开户类型
        var type = url.get("type");

        if(type){
            //disable 账户性质
            $('#acctType').val(type);
            $('#acctType').attr("disabled",true);

            form.render('select','acctType');
        }

        if($("#regProvince").length>0 && $("#regProvince").val() !=""){
        	$("#regProvince").attr("disabled",true);
        	$("#regProvince").addClass("disableElement");
        }else{
        	$("#regProvince").addClass("modify-input");
        }
        if($("#regCity").length>0 && $("#regCity").val() !=""){
        	$("#regCity").attr("disabled",true);
        	$("#regCity").addClass("disableElement");
        }
        if($("#regArea").length>0 && $("#regArea").val() !=""){
        	$("#regArea").attr("disabled",true);
        	$("#regArea").addClass("disableElement");
        }

        form.render('select');
        $('select').each(function(){
        	if($(this).val() ==""){
        		$(this).parent().find(".layui-select-title").addClass("modify-input");
        	}
        })
        // loading.hide();
    },function (data) {
        //初始化存款人名称
        $('#depositorName').val(name);
    });
}

/**
 * 给工商字段赋值
 * @param form
 * @param picker
 * @param common
 * @param result
 */
function initSaicTemplate(result) {

    //省份

    $('#depositorName').val(result.name);// 企业名称
    $('#orgEnName').val('');// 机构英文名

    $('#regAddress').val(result.address);// 注册详细地址
    $('#regFullAddress').val(result.address);// 注册详细地址

    $('#isSameRegistArea').val('1');
    $('#workAddress').val(result.address);//联系信息详细地址

    var unityCreditCode = result.unitycreditcode;
    if(unityCreditCode){
        $('#regNo').val(unityCreditCode);// 工商注册号
        $('#regType').val('07');// 工商注册类型

        $('#fileNo').val(unityCreditCode);//证明文件1编号
        $('#stateTaxRegNo').val(unityCreditCode);//纳税人识别号（国税）
        $('#taxRegNo').val(unityCreditCode);//纳税人识别号（地税）

        //省市区
        var areaCode;
        if(unityCreditCode.length === 15) {
            areaCode = unityCreditCode.substring(0, 6);
        } else if(unityCreditCode.length === 18) {
            areaCode = unityCreditCode.substring(2, 8);
        }

        //注册地地区代码
        $('#regAreaCode').val(areaCode);

        //省市区组件
        initAddress('reg',regAddressPicker,areaCode,3);
        initAddress('work',workAddressPicker,areaCode,3);

    }else{
        var registNo = result.registno;
        $('#regNo').val(registNo);// 工商注册号
        $('#regType').val('01');// 工商注册类型

        $('#fileNo').val(registNo);//证明文件1编号
        $('#stateTaxRegNo').val(registNo);//纳税人识别号（国税）
        $('#taxRegNo').val(registNo);//纳税人识别号（地税）
    }

    $('#regOffice').val('G');// 登记部门
    $('#fileType').val('01');//证明文件1种类

    $('#fileType2').val('');// 证明文件2种类
    $('#fileNo2').val('');// 证明文件2编号

    var startDate = common.cnDateTranslator(result.startdate);
    //时间初始化
    $("#fileSetupDate").val(startDate);  //成立日期


    var endDate = '2099-12-31'; //默认到期时间
    if(result.enddate){
        endDate = common.cnDateTranslator(result.enddate);
    }

    //对公页面重构新增字段
    $("#fileDue").val(endDate);
    // $('.fileDue.date-picker').datepicker("setDate",endDate);//到期日期

    $('#businessLicenseNo').val(result.registno);//营业执照编号

    // $('.businessLicenseDue.date-picker').datepicker("setDate",endDate);//营业执照到期日
    $("#businessLicenseDue").val(endDate);

    if(result.registfund){
        $("#registeredCapital").val(common.currencyTranslator(result.registfund)); //注册资本(元)
        $('#regCurrencyType').val(common.currencyTypeTranslator(result.registfund)); //注册资本币种

        $('#isIdentification').val('1');//设置标明注册资金
    } else {
        $('#isIdentification').val('0');//设置未标明注册资金
    }

    $('#businessScope').val(result.scope); //经营范围
    $('#businessScopeEccs').val(result.scope); //经营范围
    $('#legalName').val(result.legalperson);// 法人姓名
    if($('#legalName').val() != '') {
        $("#legalName").attr("readonly", true);
    }

    $('#legalType').val(result.legalpersontype);// 法人类型

    $('input,select,textarea').each(function(){
    	if($(this).val() =="" || $(this).attr('id') === 'regAreaCode'|| $(this).attr('id') === 'accountKey'){
    		$(this).removeAttr("disabled");
    		$(this).addClass("modify-input");
    	}
    })
    
    form.render('select');
}

    $('#carrierBtn').on('click', function () {
        var telephone = document.getElementsByName('legalTelephone')[0].value;
        var name = document.getElementsByName('legalName')[0].value;
        var idcardNo = document.getElementsByName('legalIdcardNo')[0].value;

        $.get("../../carrier/getCarrierOperatorResult?name=" + encodeURI(name) + "&mobile=" + telephone + "&cardno=" + idcardNo, null, function (result) {
            if (result.result) {
                document.getElementById("carrierResult").innerHTML = result.result;
            }

        });
    });

    $('#idcardBtn').on('click', function () {
        var name = document.getElementsByName('legalName')[0].value;
        var idcardNo = document.getElementsByName('legalIdcardNo')[0].value;

        if(name == "" || idcardNo == "") {
            document.getElementById("idcardResult").innerHTML = "法人姓名或身份证号码不能为空";
            return;
        }

        $.post('../../idCard/sumit', {"idCardNo":idcardNo, "idCardName":name}, function(result){
            document.getElementById("idcardResult").innerHTML = result.checkResult;
        }).error(function (e) {
            document.getElementById("idcardResult").innerHTML = e.responseJSON.message;
        });

    });

    $("#validate").on('click', function () {
        var type = $("#type").val();
        // if(type === 'jiben' || type === 'linshi' || type === 'teshu'){
        // } else {
        //     if ($('#accountKey').val() == '') {
        //         layerTips.msg('基本户核准号不能为空');
        //         return false;
        //     }
        //     if ($('#regAreaCode').val() == '') {
        //         layerTips.msg('基本户地区代码不能为空');
        //         return false;
        //     }
        // }

        var keyword = $('#name').text();
        $.get('../../validate/composed?keyword='+encodeURI(keyword)+'&accountKey='+($('#accountKey').val()?$('#accountKey').val():"")
            +'&regAreaCode='+($('#regAreaCode').val()?$('#regAreaCode').val():""), function (data) {
            laytpl($('#validationRusultTemplate').html()).render(data.data, function (html) {
                $('#validationResultBody').html(html);
                $('#valiationResultModal').modal();
                if (myType==="jiben" || myType==="linshi" || myType==="teshu"){
                    $("#pbcMessage").hide();
                    $("#pbcDiv").hide();
                }
            });
        });
    });

    var validator = $("#formId").validate({
        ignore: "",
        errorPlacement : errorPlacementCallback,
        highlight : highlight,
        unhighlight : unhighlight,
        showErrors: showErrors,
        submitHandler: function() {
            var data = toJson($("#formId").serializeArray())

            if($('#submitBtnType').val() == 'save') {
                $("input[name='status']").val("SUCCESS");
                data.status="SUCCESS";
            }
            if($('#submitBtnType').val() == 'cancel') {
                $("input[name='status']").val("FAIL");
                data.status="FAIL";
            }

            data.operator = $('#operator').html()
            data.type = $('#type').html()
            data.phone = $('#phone').html()
            data.applytime = $('#applytime').html()
            data.branch = $('#branch').html()
            data.applyid = $('#applyid').html()

            loopSelectedDisabled(data);
            loopInputDisabled(data);
            loopTextAreaDisabled(data);
            data.regAreaCode = $('#regAreaCode').val();
            submitForm(data);

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });

    /**
     * 弹出是否进行双录提示
     */
    function videoOpen() {
        layer.open({
            type: 1,
            title: '提示',
            content: '是否进行双录？',
            btn: ['新建双录', '取消'],
            shade: false,
            offset: ['80px', '30%'],
            area: ['240px', '150px'],
            maxmin: false,
            yes: function (index) {
                var myTabId = parent.tab.getCurrentTabId();//当前页的tabID
                var depositorName = $('#name').text();
                var acctType = myType;
                var type = 'apply';
                var applyId = $('#applyid').text();
                var params = 'applyId='+applyId+'&type='+type+'&acctType='+acctType+'&depositorName='+depositorName;

                parent.tab.tabAdd({
                    href: 'imageInfo/videoOpen.html?'+params,
                    icon: 'fa fa-calendar-times-o',
                    title: '新建双录信息'
                });

                parent.tab.deleteTab(myTabId);
            },
            end: function () {
                saveBoxIndex = -1;
                parent.tab.deleteTab(parent.tab.getCurrentTabId(),true);
            }
        });

    }

});

function templateClick() {
    if($('#templateOption').val() == '') {
        layer.alert("请选择对应模版类型");
        return;
    }

    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
    $.get('../account/print.html', null, function (form) {

        var url = '../../apply/getPrintPreview?applyId=' + $('#applyid').html() + '&templateName=' + encodeURI($('#templateOption').val()) + "&billType=APPT_UNCOMPLETE&depositorType=" + convertDepositorType($('#depositorType').val());
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
                //     target:'#print',
                //     message:'正在努力生成打印文件,请稍等...'
                // });
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
            end: function () {
                addBoxIndex = -1;
            }
        });
    });
    return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
}