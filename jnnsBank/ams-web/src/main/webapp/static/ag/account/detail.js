layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form','murl','saic','account', 'picker', 'linkSelect', 'org', 'industry','common', 'loading'], function () {
    var form = layui.form,url=layui.murl,
        saic = layui.saic, account = layui.account,picker = layui.picker,
        linkSelect = layui.linkSelect,
        org = layui.org,
        industry = layui.industry,
        common=layui.common,
        loading = layui.loading;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    //注册地址选择器
    var regAddressPicker = new picker();

    //联系地址选择器
    var workAddressPicker = new picker();

    // 企业名称
    var name = decodeURI(url.get("name"));
    $("#name").html(name);

    // 预约标号
    var preOpenAcctId = decodeURI(url.get("preOpenAcctId"));
    if(preOpenAcctId){
        $('#preOpenAcctId').val(preOpenAcctId);
    } else {
        $('#preOpenAcctId').remove();
    }

    //账户信息
    account.loadAccountTemplate({},'#accountDiv');

    //客户信息
    account.loadSaicTemplate({},'#saicDiv');

    //法人代表信息
    account.loadLegalTemplate({},'#legalDiv');

    //组织结构代码证信息
    account.loadOrgTemplate({},'#orgDiv');

    //税务登记信息
    account.loadTaxTemplate({},'#taxDiv');

    //联系信息
    account.loadContactTemplate({},'#contactDiv');

    //其他信息
    account.loadOtherTemplate({},'#otherDiv');

    //上级机构信息
    account.loadSuperviserTemplate({},'#superviserDiv');

    form.on('submit(save)', function(data){
        $("input[name='status']").val("SUCCESS");
        data.field.status="SUCCESS";
        submitForm(data);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    var addBoxIndex = -1;
    form.on('submit(print)', function(data){
        //判断是否保存过数据
        if(!$('#openAccountLogId').val()){
            layerTips.msg('请先保存填写的信息,再点击打印');
            return;
        }

        if (addBoxIndex !== -1)
            return;

        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get('/admin/account/print', null, function (form) {

            var url = '/apply/openAccount/customerInfo/preview/?id=' + $('#openAccountLogId').val();
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
                    // loading.show({
                    //     target:'#print',
                    //     message:'正在努力生成打印文件,请稍等...'
                    // });
                    layero.find("iframe")[0].contentWindow.location.replace(url);
                    layero.find("iframe").on('load',function () {
                        // setTimeout(function hide() {
                        //     loading.hide('#print');
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
    });

    form.on('submit(cancel)', function(){
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //注册地是否一致
    form.on('select(isSameRegistArea)',function (data) {
        if(data.value=== '1'){
            //联系地址和注册地一致

            workAddressPicker.set({
                codeConfig: {
                    code: $('#regAreaCode').val(),
                    type: 3
                }
            }).render();

            //同步地址
            $('#workAddress').val($('#regAddress').val());
        }
    });

    saic.loadBasicData(name,function (data) {
        //下拉框组件初始化
        initAddress('reg',regAddressPicker,"",3);
        initAddress('work',workAddressPicker,"",3);
    });

    /**
     * 根据工商数据回填
     * @param name
     */
    function initBySaic(name) {
        saic.loadBasicData(name,function (data) {

            //时间组件
            if (jQuery().datepicker) {
                $('.date-picker').datepicker({
                    orientation: "top left",
                    autoclose: true,
                    language: 'zh-CN'
                });
            }

            initSaicTemplate(data);

            //开户默认当前时间
            $(".acctCreateDate.date-picker").datepicker("setDate", new Date());

            //注册地址区域修改->保存内容到注册地地区代码
            form.on('select(regArea)',function (data) {
                $('#regAreaCode').val(data.value);
            });

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

            // loading.hide();
        },function (data) {
            //初始化存款人名称
            $('#depositorName').val(name);
        });
    }

    /**
     * 根据现有数据回填表单
     * @param data
     */
    function initByEsixtData(data) {
        //显示打印按钮
        $('#printBtn').removeClass('dn');

        //时间组件
        if (jQuery().datepicker) {
            $('.date-picker').datepicker({
                orientation: "top left",
                autoclose: true,
                language: 'zh-CN'
            });
        }

        if(data){
            for (var key in data) {
                $('#'+key).val(data[key]);
            }

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
            }
            initAddress('work',workAddressPicker,workAddressCode,workAddressType);

            //组织机构类别
            var selectedArr = [];
            if(data.orgType && data.orgTypeDetail){
                selectedArr.push(data.orgType);
                selectedArr.push(data.orgTypeDetail);
            }
            initLinkSelect("orgSelect",org.data(),selectedArr,function (item) {
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
            var selectedIndustryArr = [];
            if(data.economyIndustryCode){
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,1));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,3));
                selectedIndustryArr.push(data.economyIndustryCode.substring(0,4));
                selectedIndustryArr.push(data.economyIndustryCode);
            }
            initLinkSelect("economyIndustrySelect",industry.data(),selectedIndustryArr,function (item) {
                //选中回调函数
                if(item.value.length === 5){
                    //设置最终的值
                    $('#economyIndustryCode').val(item.value);
                    $('#economyIndustryName').val(item.name);
                }
            });

            //注册地址区域修改->保存内容到注册地地区代码
            form.on('select(regArea)',function (data) {
                $('#regAreaCode').val(data.value);
            });
        }

        if(!$('#acctCreateDate').val()){
            //开户默认当前时间
            $('.acctCreateDate.date-picker').datepicker("setDate", new Date());
        }

        //disable 账户类型
        $('#acctType').attr("disabled",true);

        form.render('select');
    }


    // //渲染其他组件
    // setTimeout(function () {
    //
    // })
    
    function initLinkSelect(elementId,data,selectedArr,selectedCallback) {
        linkSelect.render({
            id:elementId,
            elem: '#'+elementId,
            data:data,
            lableName:'',			//自定义表单名称    默认：'级联选择'
            placeholderText:'请选择...',		//自定义holder名称    默认：'请选择'
            replaceId:"ids",				//替换字段id   默认id
            replaceName:"names",			//替换字段名称  默认name
            replaceChildren:"childrens",	//替换字段名称  默认 children
            disabled:false,					//初始禁用         默认false
            selectWidth:200,
            selectedArr: selectedArr,
            selected:function(item,dom){
                if(selectedCallback) selectedCallback(item);
            }
        });
    }

    function submitForm(data){
        var url = '/apply/openAccount/customerInfo';
        if(data.field.openAccountLogId){
            url += '/'+data.field.openAccountLogId;
        }

        if(!data.field.depositorName){
            layerTips.msg('请填写存款人名称');
            return;
        }

        if(data.field.acctType){
            //地址信息赋值
            data.field.regProvinceChname = $('#regProvince').find("option:selected").text();
            data.field.regCityChname = $('#regCity').find("option:selected").text();
            data.field.regAreaChname = $('#regArea').find("option:selected").text();
            data.field.workProvinceChname = $('#workProvince').find("option:selected").text();
            data.field.workCityChname = $('#workCity').find("option:selected").text();
            data.field.workAreaChname = $('#workArea').find("option:selected").text();


            $.ajax({
                url: url,
                type: 'post',
                data: data.field,
                dataType: "json"
            }).done(function (data) {
                if(data.rel){
                    var result = data.result;
                    if(result) {
                        //保存id和账户性质
                        $('#openAccountLogId').val(result.id);

                        //显示打印
                        $('#printBtn').removeClass('dn');

                        //disable账户性质
                        $('#acctType').attr("disabled",true);
                        form.render('select','acctType');
                    }
                    layerTips.msg('保存成功');
                } else {
                    layerTips.msg(data.msg || '保存失败');
                }
            }).fail(function (err) {
                layerTips.msg('保存失败['+err+']');
            });
        } else {
            layerTips.msg('请选择账户性质');
        }
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
        $('.fileSetupDate.date-picker').datepicker("setDate",startDate);//成立日期


        var endDate = '2099-12-31'; //默认到期时间
        if(result.enddate){
            endDate = common.cnDateTranslator(result.enddate);
        }

        //对公页面重构新增字段
        $('.fileDue.date-picker').datepicker("setDate",endDate);//成立日期

        $('#businessLicenseNo').val(result.registno);//营业执照编号

        $('.businessLicenseDue.date-picker').datepicker("setDate",endDate);//营业执照到期日

        if(result.registfund){
            $("#registeredCapital").val(common.currencyTranslator(result.registfund)); //注册资本(元)
            $('#regCurrencyType').val(common.currencyTypeTranslator(result.registfund)); //注册资本币种

            $('#isIdentification').val('1');//设置标明注册资金
        } else {
            $('#isIdentification').val('0');//设置未标明注册资金
        }

        $('#businessScope').val(result.scope); //经营范围
        $('#businessScopeEccs').val(result.scope); //经营范围
        $('#legalName').val(result.legalperson);// 法人

        form.render('select');
    }
});



