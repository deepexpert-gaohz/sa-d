var apply = {
    baseUrl: "../../apply",
};
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var loading2;

var linkSelect;

layui.use(['form','murl','layer','element','laydate','laytpl','saic', 'picker', 'common', 'linkSelect', 'org', 'industry', 'loading', 'account'], function () {
    var laydate = layui.laydate,element = layui.element,form = layui.form,
        murl=layui.murl,applyId = murl.get("applyId"), url=layui.murl, name = decodeURI(murl.get("name")),viewType = murl.get("type"),laytpl = layui.laytpl,
        saic = layui.saic,picker = layui.picker, common=layui.common;
        org = layui.org,
        industry = layui.industry,
        loading = layui.loading;
        account = layui.account;
    linkSelect = layui.linkSelect;

    loading2 = loading;

    var urlType=false;
    if(viewType !="" && viewType =="finish" && applyId !=""){
    	urlType= true;
    }
    
    laydate.render({
        elem: '#times' //指定元素
        ,type: 'time'
        ,min: '09:00:00'
        ,max: '17:30:00'
        ,format: 'HH:mm'
    });

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();

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

    $.ajaxSettings.async = false;
    $.get('../../config/findByKey?configKey=legalDivEnabled', function (data) {
        if(data == true) {
            $('#legalRowDiv').css('display', '');
            $.get('../account/legal.html', null, function (form) {
                $("#legalDiv").html(form);
            });
        }
    });

    $.get('../../config/findByKey?configKey=customerDivEnabled', function (data) {
        if(data == true) {
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

    $("#name").html(name);

    initAddress('reg',regAddressPicker,null,3);
    initAddress('work',workAddressPicker,null,3);
    form.render('select');

    // $('#depositorName').attr('lay-verify', '')
    // $('#depositorName').attr('required', false)
    // $('#depositorNameSpan').hide()
    $('#acctShortNameDiv').remove()
    $('input,select,textarea').prop("disabled",true)
    $('#carrierBtn').hide();

    if(viewType =="finish") {  //已受理界面
        $.get("../../config/findByKey?configKey=printf", null, function (data) {
            if (data == true) {  //配置打印
                $('#printBtn').css('display', '');
            }
        })
    }

    removeAll()

    //如有预约编号
    if(applyId){
        $.get (apply.baseUrl+'/mina/edit?applyId=' + applyId, null, function (data) {
            if(data.rel){
                handlePreOpenAccountRecord(data);
            }
        });
    } else  if(id){
        //若有预约记录ID
        $.get (apply.baseUrl+'/bank/details?id=' + id, null, function (data) {
            if(data.rel){
                handlePreOpenAccountRecord(data);
            }
        });
    }

    if(urlType){//已受理/受理失败/受理成功
        initBySaic(saic,name,"../../apply/bank/view?applyId="+applyId);
    }else{
        initBySaic(saic,name,"../../kyc/saic/basic?keyword="+encodeURI(name));
    }

    //打印
    $('#printBtn').click(function(){
        printfClick(common, 'APPT_AFTERCOMPLETE');
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    /**
     * 根据工商数据回填
     * @param name
     */
    function initBySaic(saic, name,saicUrl) {
    	getAjaxUrl(saicUrl,function (data) {

    		if(urlType){
    			for (var key in data) {
    	            $('#'+key).val(data[key]);
    			}

    	        form.render('select');
    		}else{
                initSaicTemplate(data);
    		}

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

            if($("#regProvince").length>0){
            	$("#regProvince").attr("disabled",true);
            }
            if($("#regCity").length>0){
            	$("#regCity").attr("disabled",true);
            }
            if($("#regArea").length>0){
            	$("#regArea").attr("disabled",true);
            }
            form.render('select');
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

        form.render('select');
    }

    function handlePreOpenAccountRecord(data) {
        var result = data.result;
        if(result){
            setLabelValues( result);
            if(result.banktime) {
                $("#banktime").html(result.banktime + " " + result.times);
            }
            if(result.applytime) {
                $("#applytime").html(result.applytime);
            }
            if(result.hasocr != 0){
                $(".showNumber").show();
                $(".showNumberButton").show();
                getOcrs(result.id);
            }else{
                $(".showNumber").hide();
                $(".showNumberButton").hide();
            }

            // if(result)
            // queryOcr(result.applyid);
        }

    }


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

    function queryOcr(applyId){
        var queryStr="applyId="+applyId;

        $.get (apply.baseUrl+'/mina/ocr?' + queryStr, null, function (data) {
            if(data.rel){
                saic.loadImageView(data.result,'.ocrImages');
            }
        });
    }

    function setLabelValues(data) {
        var typeCode;

        for (var p in data) {
            $("#" + p + "").html(data[p]);
        }

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

        $('#type').html(typeCode);


        if(data.type === 'jiben' || data.type === 'linshi' || data.type === 'teshu'){
            $('#pbcInfo').remove();
        } else {
            $('#pbcInfo').show();
        }

        if(data.createdDate) {
            $("#applytime").html(changeDateFormat(data.createdDate));
        }

        if(data.id){
            $("#idHiddenId").val(data.id);
        }

        if(data.hasocr){
            $("#hasocrHiddenId").val(data.hasocr);
        }

    }


    $('#cancel').click(function(data){
       parent.tab.deleteTab(parent.tab.getCurrentTabId());
       return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

});

function templateClick() {
    if($('#templateOption').val() == '') {
        layer.alert("请选择对应模版类型");
        return;
    }

    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
    $.get('../account/print.html', null, function (form) {

        var url = '../../apply/getPrintPreview?applyId=' + $('#applyid').html() + '&templateName=' + encodeURI($('#templateOption').val()) + "&billType=APPT_AFTERCOMPLETE&depositorType=" + convertDepositorType($('#depositorType').val());
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

