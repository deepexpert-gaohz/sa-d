layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var validator;
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading', 'laydate'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        loading = layui.loading,
        laydate = layui.laydate;
    var laytpl = layui.laytpl;

    var name = decodeURI(common.getReqParam("name"));
    var accountKey = decodeURI(common.getReqParam("accountKey"));
    var regAreaCode = decodeURI(common.getReqParam("regAreaCode"));
    var type = decodeURI(common.getReqParam("type"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var billType = decodeURI(common.getReqParam("billType"));
    var operateType = decodeURI(common.getReqParam("operateType"));//select查看   change变更
    var createdDate = decodeURI(common.getReqParam("createdDate"));
    var organName = decodeURI(common.getReqParam("organName"));

    var compareResultSaicCheckId = decodeURI(common.getReqParam("compareResultSaicCheckId"));
    var compareTaskId = decodeURI(common.getReqParam("compareTaskId"));
    var saicInfoId = decodeURI(common.getReqParam("saicInfoId"));
    var compareResultId = decodeURI(common.getReqParam("compareResultId"));
    var depositorName = decodeURI(common.getReqParam("depositorName"));
    var abnormalTime = decodeURI(common.getReqParam("abnormalTime"));
    var tabType = decodeURI(common.getReqParam("tabType"));
    var id = decodeURI(common.getReqParam("id"));//客户主表id

    $("#createTimeSpan").html(createdDate);
    $("#createOrgSpan").html(organName);

    //进入页面默认切换到指定Tab页
    switch (tabType) {
        case 'abnormal':
            $('#abnormalTab').click();
            break;
        default:
    }


    if(buttonType === 'saicValidate') {
        $.get('../../organization/root', function (data) {
            $('#organName').val(data.data.name);
        });
        $.get('../../validate/open/getCustomerInfo?accountKey=' + accountKey + '&regAreaCode=' + regAreaCode + '&name=' + encodeURI(name) + '&type=' + type).done(function (response) {
            if (response.rel === false) {
                layer.alert(response.msg, {
                    title: "提示",
                    closeBtn: 0
                }, function () {
                    parent.tab.deleteTab(parent.tab.getCurrentTabId());
                });
            } else {
                if (response.msg) {
                    layer.alert(response.msg, {
                        title: "提示",
                        closeBtn: 0
                    });
                }

                initByExistData(response.result);
            }
        });
    }

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();

    // 企业名称
    $("#name").html(name);

    $.ajaxSettings.async = false;

    $.get('../account/appoint.html', function (form) {
        $("#appointDiv").html(form);
    });

    $.get('../customer/customer.html', function (form) {
        $("#saicDiv").html(form);
    });

    $.get('../account/legal.html', function (form) {
        $("#legalDiv").html(form);

    });
    $.get('../account/org.html', function (form) {
        $("#orgDiv").html(form);

    });
    $.get('../account/tax.html', function (form) {
        $("#taxDiv").html(form);
    });
    $.get('../account/contact.html', function (form) {
        $("#contactDiv").html(form);
        $('#isSameRegistAreaDiv').remove()

    });
    $.get('../account/other.html', function (form) {
        $("#otherDiv").html(form);
        $('#corpScaleDiv').html("");
        $('#corpScaleDiv').append("    <div class=\"form-group\">\n" +
            "        <label class=\"control-label col-md-4\">完整机构ID</label>\n" +
            "        <div class=\"col-md-8\">\n" +
            "            <input type=\"text\" class=\"layui-input\" placeholder=\"\" name=\"organFullId\" id=\"organFullId\">\n" +
            "        </div>\n" +
            "    </div>");

    });
    $.get('../account/superviser.html', function (form) {
        $("#superviserDiv").html(form);
    });

    $.get('../account/acct_hiddenfield.html', function (form) {
        $("#hiddenFieldDiv").html(form);
        $('#hiddenFieldDiv input[name="customerNo"]').remove();//删除隐藏域中的客户号（客户开立页面中已存在客户号input框）
        $('#hiddenFieldDiv input[name="organName"]').remove();
        $('#hiddenFieldDiv input[name="organFullId"]').remove();
    });

    $.ajaxSettings.async = true;
    if(type=="customerOpen"){
        var urlStr ="../../video/findByCondition?depositorName="+name;
        getVideoData(urlStr,true);
    }else if(type=="update"){
        var urlStr ="../../video/findByCondition?depositorName="+depositorName;
        getVideoData(urlStr,true);
    }else{
        var urlStr ="../../video/findByCondition?depositorName="+depositorName;
        getVideoData(urlStr);
    }

    laydate.render({
        elem: '#operatorIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#operatorIdcardDue"));
        }
    });

    laydate.render({
        elem: '#acctCreateDate',
        format: 'yyyy-MM-dd',
        max : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#acctCreateDate"));
        }
    });

    laydate.render({
        elem: '#fileSetupDate',
        format: 'yyyy-MM-dd',
        max : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#fileSetupDate"));
        }
    });

    laydate.render({
        elem: '#fileDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            if($("#fileType").val() === "01"){
                $("#businessLicenseDue").val(value);
            }
            validator.element($("#fileDue"));
        }
    });

    laydate.render({
        elem: '#businessLicenseDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#businessLicenseDue"));
        }
    });

    laydate.render({
        elem: '#legalIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#legalIdcardDue"));
        }
    });

    laydate.render({
        elem: '#orgCodeDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#orgCodeDue"));
        }
    });

    laydate.render({
        elem: '#stateTaxDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#stateTaxDue"));
        }
    });

    laydate.render({
        elem: '#taxDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#taxDue"));
        }
    });

    laydate.render({
        elem: '#parOrgEccsDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#parOrgEccsDue"));
        }
    });

    laydate.render({
        elem: '#parLegalIdcardDue',
        format: 'yyyy-MM-dd',
        min : 'nowDate',
        done: function(value, date, endDate){
            validator.element($("#parLegalIdcardDue"));
        }
    });

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    if(id) {
        $.get("../../customerPublic/find?customerId=" + id, null, function (data) {
            initByExistData(data);
            if (data.customerNo != null && data.customerNo !== '') {//若客户号不为空 则不允许修改客户号
                $('#customerNo').attr({readonly: 'true'});
            }
            //隐藏域赋值
            $('#id').val(data.id);
        });
    }

    var selectDisabled = false;
    if(operateType === 'select') {  //详情页
        $('#cancel').css('display', '');
        $('input,select,textarea').prop("disabled",true)
        selectDisabled = true;
    } else {
        $('#saveBtn').css('display', '');
        $('#cancel').css('display', '');
    }
    $('#organName').attr({ readonly: 'true' });
    $('#organFullId').attr({ readonly: 'true' });

    //提交
    $('#saveBtn').click(function(){
        $('#submitBtnType').val('save')
        $("#formId").submit();
    });

    $('#cancel').click(function(){
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });


    form.on('select', function(data){
        validator.element(data.elem);
    });


    removeAll();
    var validLabelCustomer = ["depositorName", 'fileType', 'fileNo'];//客户信息
    var validLabelLegal = ["legalType"];//法人代表信息
    var docCodeFlag = ["docCode"];//影像
    addValidateFlag(validLabelCustomer.concat(validLabelLegal,docCodeFlag));

    var rules = {
        depositorName : {//存款人名称
            required: true,
            stringMaxLength : 128
        },
        legalType : "required",//法人代表（负责）人
        fileType : "required",
        fileNo : "required",
        customerNo : {//客户号
            // required: true,
            stringMaxLength : 50
        },
        businessScope : {//经营范围（人行账管系统）
            maxlength : 489
        }
    };

    var messages = {
        legalName : {
            equalTo : "必须与存款人姓名相同"
        },
        noTaxProve : {
            manyChooseOne : "这些税务登记信息必须填写一个"
        },
        stateTaxRegNo : {
            manyChooseOne : "这些税务登记信息必须填写一个"
        },
        taxRegNo : {
            manyChooseOne : "这些税务登记信息必须填写一个"
        },
        fileType2 : {
            shouldAllRequiredOrNot : "非必填，但与证明文件2编号必须同时联动必填"
        },
        fileNo2 : {
            shouldAllRequiredOrNot : "非必填，但与证明文件2种类必须同时联动必填"
        },
        regNo:{
            lengthOrOther : "必须为18位或者15位"
        }
    };

    otherValidate = function(){

        //当为存款类别为"01"、"02"、"13"、"14"时，请录入国税登记证号或地税登记证号,或填写无需办理税务登记证的文件或税务机关出具的证明!
        /*$("#depositorType").attr("lay-filter","depositorType");
        form.on('select(depositorType)',function (data) {
           if(data.value == "14"){
               addValidate("legalName",{equalTo:"#depositorName"},true,validator);
           }else{
               removeValidate("legalName",false,validator,"equalTo");
           }*/
//           if(data.value == "01" || data.value == "02"|| data.value == "13"|| data.value == "14"){
//    		   addValidate("noTaxProve",{manyChooseOne : ["#stateTaxRegNo","#taxRegNo"]},true,validator);
//    		   addValidate("stateTaxRegNo",{manyChooseOne : ["#noTaxProve","#taxRegNo"]},true,validator);
//    		   addValidate("taxRegNo",{manyChooseOne : ["#stateTaxRegNo","#noTaxProve"]},true,validator);
//
//        	   if(data.value == "14"){
//        		   addValidate("legalName",{equalTo:"#depositorName"},true,validator);
//        	   }else{
//        		   removeValidate("legalName",false,validator,"equalTo");
//        	   }
//           }else{
//    		   removeValidate("noTaxProve",true,validator,"manyChooseOne");
//    		   removeValidate("stateTaxRegNo",true,validator,"manyChooseOne");
//    		   removeValidate("taxRegNo",true,validator,"manyChooseOne");
//    		   removeValidate("legalName",false,validator,"equalTo");
//           }
        // });

        //证明文件2种类 和 文件编号2 联动必填，
        $("#fileType2").attr("lay-filter","fileType2");
        form.on('select(fileType2)',function (data) {
            if($.trim(data.value) !=="" || $.trim($("#fileNo2").val()) !==""){
                addValidateFlag(["fileType2","fileNo2"]);
            }else{
                removeValidateFlag(["fileType2","fileNo2"]);
            }
        });
        $("#fileNo2").on("change",function(){
            if($.trim(this.value) !=="" || $.trim($("#fileType2").val()) !==""){
                addValidateFlag(["fileType2","fileNo2"]);
            }else{
                removeValidateFlag(["fileType2","fileNo2"]);
            }
        });

        //上级法定代表人或负责人,姓名、身份证明文件种类及其编号数据项联动必填
        parLegalTypeChange(form,validator);


        //如证明文件类型为营业执照，默认联动证明文件1编号
        //如证明文件类型为营业执照，默认联动证明文件1的到期日期
        fileTypeChange(form);


        //如注册资金未注明选择“是”，该字段为不可填，
        //币种根据工商返显的单位进行自动判断，自动赋值，
        isIdentificationChange(form,validator,true);

        //经营范围（人行账管系统）上限500，超过时最后一个字符是“等”，自动同步到经营范围（信用机构代码），最多200个字符，超过时最后一个字符为“等”
        businessScopeChange(true,validator);


        //如证件类型为身份证，证件号码则根据身份证规则进行输入校验
        legalIdcardTypeChange(form,validator);

        //上级组织机构代码不为空时,机构名称必录
        //上级组织机构代码不为空时,基本户开户许可核准号必录
        //上级组织机构代码不为空时,法人姓名必录，不能超过32个字符或16个汉字
        parOrgCodeChange(validator);


        //股东信息的关系人类型，当选1高管时：类型包括：实际控制人、董事长、总经理（主要负责人）、财务负责人、监事长、法定代表人
        //当选2股东：自然人、机构
        partnerTypeChange(form);

        //如“无需”输入框内有值，则将国地税输入框锁定，
        //如“无需”输入框为空，则国地税至少有一项必填，
        noTaxProveChange(validator);

        //上级机构信息，证件号码则根据身份证规则进行输入校验
//    	parLegalIdcardTypeChange(form,validator);

        //股东信息，证件号码则根据身份证规则进行输入校验
        idcardTypeChange(form,validator);

        //授权经办人信息，证件号码则根据身份证规则进行输入校验
        operatorIdcardTypeChange(form,validator);
    }

    otherInit = function(){
        //初始化：
        //上级组织机构代码不为空时,机构名称必录
        //上级组织机构代码不为空时,基本户开户许可核准号必录
        //上级组织机构代码不为空时,法人姓名必录
        parOrgCodeInit(validator);

        //初始化：股东信息，证件号码则根据身份证规则进行输入校验
        idcardTypeInit(form,validator);

        //初始化：授权经办人信息，证件号码则根据身份证规则进行输入校验
        operatorIdcardTypeInit(form,validator);

        //初始化：上级机构信息，证件号码则根据身份证规则进行输入校验
        parLegalIdcardTypeInit(form,validator);

        //初始化：如证件类型为身份证，证件号码则根据身份证规则进行输入校验
        legalIdcardTypeInit(form,validator);

        //存款人名称复制到存款人简称
        depositorNameChange(validator);

        //未标明注册资金默认为否，如返显时注册资金有值，则未标明注册资金为：否
        isIdentificationInit(validator,true,form);

        //如“无需”输入框内有值，则将国地税输入框锁定，
        //如“无需”输入框为空，则国地税至少有一项必填，
        noTaxProveInit(validator);

        //与注册地是否一致:默认为“是”
        isSameRegistAreaInit();

        //机构状态:默认值正常
        if($.trim($("#orgStatus").val()) === ""){
            $("#orgStatus").val("1");
        }

        //基本户状态:默认值正常
        if($.trim($("#basicAccountStatus").val()) === ""){
            $("#basicAccountStatus").val("1");
        }

    };

    $.validator.setDefaults({
        rules: rules,
        messages: messages
    });


    /**
     * 根据现有数据回填表单
     * @param data
     */
    function initByExistData(data) {
        if(data){
            setLabelValues(data);


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

            if(buttonType === 'update' && billType === 'ACCT_CHANGE' ){//补录&&变更
                initAddress('reg',regAddressPicker,regAddressCode,regAddressType,thirdObject,selectDisabled);
            }else{
                initAddress('reg',regAddressPicker,regAddressCode,regAddressType,thirdObjectValidate,selectDisabled);
            }


            var workAddressCode,workAddressType;
            if(data.workArea){
                workAddressCode = data.workArea;
                workAddressType = 3;
            } else if(data.workCity){
                workAddressCode = data.workCity;
                workAddressType = 3;
            } else if(data.workProvince){
                workAddressCode = data.workProvince;
                workAddressType = 3;
            } else{
                workAddressCode = regAddressCode;
                workAddressType = 3;
            }
            initAddress('work',workAddressPicker,workAddressCode,workAddressType,null,selectDisabled);

            //组织机构类别
            var selectedArr = [];
            if(data.orgType && data.orgTypeDetail){
                selectedArr.push(data.orgType);
                selectedArr.push(data.orgTypeDetail);
            }
            initLinkSelect("orgSelect",org.data(),selectedArr,function (item,dom) {
                if(item.value.length === 1){//说明是一级的值
                    $('#orgType').val(item.value);
                    $('#orgTypeDetail').val("");
                }else if(item.value.length === 2){//说明是二级的值
                    $('#orgTypeDetail').val(item.value);
                }else if(item.value.length ===0){
                    if(item.level ==="0"){
                        $('#orgType').val("");
                        $('#orgTypeDetail').val("");
                    }else if(item.level === "1"){
                        $('#orgTypeDetail').val("");
                    }
                }
                validator.element($("#linkSelectInput10001"));
            },selectDisabled);
            $('#orgType').val(data.orgType);
            $('#orgTypeDetail').val(data.orgTypeDetail);

            //经济行业分类
            var selectedIndustryArr = [];
            if(data.economyIndustryCode){
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,1));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,3));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,4));
                selectedIndustryArr.push(data.economyIndustryCode);
            }
            initLinkSelect("economyIndustrySelect",industry.data(),selectedIndustryArr,function (item,dom,valueArr) {
                //选中回调函数
                if(valueArr && valueArr.length > 0){
                    $('#economyIndustryName').val(valueArr[valueArr.length-1].name);
                    $('#economyIndustryCode').val(valueArr[valueArr.length-1].value);
                }else if(valueArr != undefined){
                    //设置最终的值
                    $('#economyIndustryCode').val("");
                    $('#economyIndustryName').val("");
                }

                validator.element($("#linkSelectInput10002"));
            },selectDisabled);
            //注册地址区域修改->保存内容到注册地地区代码
            form.on('select(regArea)',function (data) {
                $('#regAreaCode').val($("#regArea").find("option:selected").attr("otherValue"));
            });
        }

        if(!$('#acctCreateDate').val()){
            //开户默认当前时间
            $('#acctCreateDate').val(new Date().Format("yyyy-MM-dd"));
        }

        //disable 账户类型
        $('#acctType').attr("disabled",true);

        otherValidate();
        otherInit();

        form.render('select');

        //loading.hide();

    }


    // //渲染其他组件
    // setTimeout(function () {
    //
    // })

    function initLinkSelect(elementId,data,selectedArr,selectedCallback,disabled) {
        linkSelect.render({
            id:elementId,
            elem: '#'+elementId,
            data:data,
            lableName:'',			//自定义表单名称    默认：'级联选择'
            placeholderText:'请选择...',		//自定义holder名称    默认：'请选择'
            replaceId:"ids",				//替换字段id   默认id
            replaceName:"names",			//替换字段名称  默认name
            replaceChildren:"childrens",	//替换字段名称  默认 children
            disabled:disabled,					//初始禁用         默认false
            selectWidth:200,
            selectedArr: selectedArr,
            selected:function(item,dom,valueArr){
                if(selectedCallback) selectedCallback(item,dom,valueArr);
            }
        });
    }


    //保存、提交、上报按钮
    function submitForm(data, operateType){
        var msg;
        var url;

        if(data.id) {
            url = '../../customerPublic/edit';
        } else {
            url = '../../customerPublic/save';
        }

        if(operateType === 'save') {
            data.action='saveForm';
            msg = "提交"
        }

        data.acctType = $('#acctType').find("option:selected").val();
//        data.regCurrencyType = $('#regCurrencyType').find("option:selected").val();
        loopSelectedDisabled(data);

        //地址信息赋值
        data.regProvinceChname = $('#regProvince').find("option:selected").text();
        data.regCityChname = $('#regCity').find("option:selected").text();
        data.regAreaChname = $('#regArea').find("option:selected").text();
        data.workProvinceChname = $('#workProvince').find("option:selected").text();
        data.workCityChname = $('#workCity').find("option:selected").text();
        data.workAreaChname = $('#workArea').find("option:selected").text();

        if(operateType === 'save') {  //提交操作
            $.post(url, data, function (data) {
                layer.alert(msg + '成功', {
                    title: "提示",
                    closeBtn: 0
                }, function (index) {
                    layer.close(index);

                    parent.tab.tabAdd({
                        href: 'customer/list.html?tabId=' + encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId())),
                        icon: 'fa fa-venus',
                        title: '客户智能管理'
                    });


                });
            }).error(function (err) {
                layerTips.msg('处理submit失败['+err.responseJSON.message+']');
            });
        }


    }

    validator = $("#formId").validate({
        onkeyup : onKeyUpValidate,
        onfocusout : onFocusOutValidate,
        ignore: "",
        errorPlacement : errorPlacementCallback,
        highlight : highlight,
        unhighlight : unhighlight,
        showErrors: showErrors,
        submitHandler: function() {
            submitForm(toJson($("#formId").serializeArray()), $('#submitBtnType').val());

            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        }
    });

    var thirdObject = new Object();
    thirdObject.regCallback = function(){
        form.on('select(regArea)',function (data) {
            $('#regAreaCode').val($("#regArea").find("option:selected").attr("otherValue"));
            validator.element($("#regAreaCode"));
        });
    };
    var thirdObjectValidate = new Object();
    thirdObjectValidate.validateObject = validator;
    thirdObjectValidate.regCallback = thirdObject.regCallback;


    //关联账户
    var apply = {
        tableId: "accountTable",
        toolbarId: "toolbar",
        order: "desc",
        currentItem: {}
    };
    apply.columns = function () {
        return [
            {
                field: 'acctNo',
                title: '账号'
            }, {
                field: 'acctType',
                title: '账户性质',
                'class': 'W120',
                formatter: function (value, row, index) {
                    return changeAcctType(value)
                }
            }, {
                field: 'accountStatus',
                title: '账户状态',
                formatter: function (value, row, index) {
                    return formatAccountStatus(value)
                }
            }, {
                field: 'bankName',
                title: '开户行'
            }, {
                field: 'kernelOrgCode',
                title: '网点机构号'
            }, {
                field: 'acctCreateDate',
                title: '开户日期',
                //获取日期列的值进行转换
                formatter: function (value, row, index) {
                    return changeDateStr(value)
                }
            }, {
                field: 'id',
                title: '操作',
                formatter: function (value, row, index) {
                    return '<a class="view" style="color: blue" href="#" onclick="accountDetail(\'' + value + '\')">查看详情</a>';
                }
            }
        ];
    };
    apply.queryParams = function (params) {
        var temp = {};
        if (id) {
            temp.id = id;
        }
        if (params) {
            temp.size = params.limit; //页面大小
            temp.page = params.offset / params.limit; //页码
        }
        return temp;
    };
    apply.init = function () {

        apply.table = $('#' + apply.tableId).bootstrapTable({
            url: "../../allAccountPublic/listForFinished", //请求后台的URL（*）
            method: 'get', //请求方式（*）
            toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: true, //是否启用排序
            sortOrder: "desc", //排序方式
            sortName: 'lastUpdateDate',
            queryParams: apply.queryParams,//传递参数（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
            search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: false,
            showColumns: false, //是否显示所有的列
            showRefresh: true, //是否显示刷新按钮
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
            showToggle: true, //是否显示详细视图和列表视图的切换按钮
            cardView: false, //是否显示详细视图
            detailView: false, //是否显示父子表
            columns: apply.columns()
            ,onLoadError: function(status){
                ajaxError(status);
            }
        });
    };
    apply.init();

    //风险异动信息
    if (saicInfoId) {
        $.get('../../kyc/history/basic?saicInfoId=' + saicInfoId).done(function (response) {
            $('.defaultDivClass').html('<span>暂无异动信息</span>');
            var result = response.result;
            if (result) {
                //严重违法信息
                setYzwf(saicInfoId, function (innerHtml) {
                    if (innerHtml) {
                        $('.break').html(innerHtml);
                        $('#breakDiv').show();
                        $('#defaultDiv').hide();
                    }
                });
                //经营异常信息
                setJyyc(saicInfoId, function (innerHtml) {
                    if (innerHtml) {
                        $('.abnormal').html(innerHtml);
                        $('#abnormalDiv').show();
                        $('#defaultDiv').hide();
                    }
                });
                //营业到期信息
                setYydq(result, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.businessDate').html(innerHtml);
                        $('#businessDateDiv').show();
                        $('#defaultDiv').hide();
                    }
                });
                //工商状态异常
                setGszt(result, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.saicType').html(innerHtml);
                        $('#saicTypeDiv').show();
                        $('#defaultDiv').hide();
                    }
                });
                //登记信息异动
                setDjxx(result, compareResultId, organName, abnormalTime, function (innerHtml) {
                    if (innerHtml) {
                        $('.registerAbnormal').html(innerHtml);
                        $('#registerAbnormalDiv').show();
                        $('#defaultDiv').hide();
                    }
                });
            }
        });
    } else {
        $('.defaultDivClass').html('<span>暂无异动信息</span>');
    }

    //导出异动信息
    $('#exportAbnormalExcel').click(function () {
        exportAbnormalExcel(compareResultSaicCheckId, compareResultId, saicInfoId);
    });
});

//账户查看详情
function accountDetail(id) {
    var data = $('#accountTable').bootstrapTable('getData');
    var row = [];
    for (var i = 0; i < data.length; i++) {
        if (data[i].id + '' === id) {
            row = data[i];
            break;
        }
    }
    var billId = row.id;
    var refBillId = row.refBillId;
    var name = row.depositorName;
    var type = row.acctType;
    var billType = row.billType;
    var accountStatus = row.accountStatus;
    var acctNo = row.acctNo;
    var buttonType = 'selectForChangeBtn';  //按钮操作类型
    var typeCode = '';
    var urlStr="&refBillId="+refBillId;
    var createdDate = row.createdDate == null ? "" : row.createdDate;
    var kernelOrgName = row.kernelOrgName == null ? "" : row.kernelOrgName;

    $('#accountId').val(row.id);
    //账户性质为大类时转小类
    if(type === 'specialAcct' || type === 'tempAcct' || type === 'unknow') {
        var index;
        if (type === 'specialAcct') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input">' +
                '<option value="" selected>请选择</option><option value="yusuan">预算单位专用存款账户</option>' +
                '<option value="feiyusuan">非预算单位专用存款账户</option><option value="teshu">特殊单位专用存款账户</option>' +
                '</select>' +
                '<button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });

        } else if (type === 'tempAcct') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input">' +
                '<option value="" selected>请选择</option><option value="linshi">临时机构临时存款账户</option>' +
                '<option value="feilinshi">非临时机构临时存款账户</option>' +
                '</select>' +
                '<button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });


        } else if (type === 'unknow') {
            //页面层
            index = layer.open({
                title: '选择小类',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '200px'], //宽高
                content: '<select id="acctTypeOption" style="width:250px;margin: 10px; " class="layui-input">' +
                '<option value="" selected>请选择</option><option value="jiben">基本存款账户</option>' +
                '<option value="yiban">一般存款账户</option><option value="yusuan">预算单位专用存款账户</option>' +
                '<option value="feiyusuan">非预算单位专用存款账户</option><option value="linshi">临时机构临时存款账户</option>' +
                '<option value="feilinshi">非临时机构临时存款账户</option><option value="teshu">特殊单位专用存款账户</option>' +
                '</select>' +
                '<button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" id="acctTypeBtn"> 确定</button>'
            });
        }

        $('#acctTypeBtn').click(function () {
            if (!$('#accountId').val()) {
                layer.alert("该账户账户id为空,无法更新小类");
                return;
            } else if (!$('#acctTypeOption').val()) {
                layer.alert("请选择账户性质的小类");
                return;
            } else {
                $.post('../../allAccountPublic/updateAcctType', {
                    'accountId': $('#accountId').val(),
                    'acctType': $('#acctTypeOption').val(),
                    'refBillId': refBillId
                }, function (data) {
                    layer.close(index);
                    tempToDetail($('#acctTypeOption').val(), billId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName);
                });
            }
        });

        return;
    }

    //小类跳转
    tempToDetail(type, billId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName);
}

function tempToDetail(type, billId, billType, buttonType, accountStatus, urlStr, acctNo, createdDate, kernelOrgName) {

    var syncEccs;
    var updateType;
    $.get('../../allAccountPublic/checkSyncModel', '', function (result) {
        if (result) {
            updateType = result.split(',')[0];
            syncEccs = result.split(',')[1];
        }

        if(billType === 'ACCT_REVOKE' && (type === 'yiban' || type === 'feiyusuan')) {   //备案类销户类型
            parent.tab.tabAdd({
                title: '查看' + acctTypeMap[type] + '-' + name,
                href: 'account/accountRevoke.html?billId=' + billId + '&billType=' + billType + '&buttonType=revokeInfo' + urlStr + '&syncEccs=' + syncEccs + '&updateType=' + updateType+ "&string005=accountStatistics"
            });
            return;
        }

        parent.tab.tabAdd({
            title: '查看' + acctTypeMap[type] + '-' + name,
            href: 'accttype/' + type + 'Open.html?billId=' + billId
            + '&acctNo=' + acctNo
            + '&billType=' + billType
            + '&accountStatus=' + accountStatus
            + '&buttonType=' + buttonType
            + '&string005=accountStatistics'
            + urlStr
            + '&syncEccs=' + syncEccs
            + '&updateType=' + updateType
            + '&createdDate=' + encodeURI(createdDate)//创建时间
            + '&kernelOrgName=' + encodeURI(kernelOrgName)//创建机构
        });
    });
}