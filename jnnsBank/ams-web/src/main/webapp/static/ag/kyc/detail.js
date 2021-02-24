/**
 * Created by alven on 11/02/2018.
 */
var kyc = {
    baseUrl: "../../kyc",
};

layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var loading2;
var saicId;
layui.use(['form','common', 'saic', 'loading', 'laydate'], function () {
    var $ = layui.jquery,
        common = layui.common, saic = layui.saic, loading = layui.loading, laydate = layui.laydate,form = layui.form;
    loading2 = loading;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var laytpl = layui.laytpl;
    var name = decodeURI(common.getReqParam("name"));
    var isHistory = decodeURI(common.getReqParam("history"));
    var applyId = decodeURI(common.getReqParam("applyId"));
    var saicInfoId = decodeURI(common.getReqParam("saicInfoId"));
    var querydate = decodeURI(common.getReqParam("querydate"));
    //工商数据发起查询类型 KHJD("客户尽调"),REAL_TIME("实时数据"),EXACT("存量数据");
    var searchType = decodeURI(common.getReqParam("searchType"));

    saicId = saicInfoId;
    var historyUrl = kyc.baseUrl;
    //自然人类型
    var naturalType = ['自然人股东', '企业法人', '法人股东', '境内中国公民', '机关法人', '其他投资者', '自然人', '事业法人', '有限合伙人', '社团法人', '个人', '普通合伙人', '合伙人', '其他非自然人投资者', '其他股东', '非农民自然人','外国(地区)投资者','外籍自然人','港澳台投资者','社会团体法人','民办非企业法人','事业单位法人','外国公民','华侨','农民自然人','香港居民','内资企业法人','台湾居民'];
    if (isHistory && isHistory === 'true') {
        historyUrl = kyc.baseUrl + '/history';
    }

    $.get("../../config/findByKey?configKey=printf", null, function (data) {
        if (data == true) {  //配置打印
            $('#printBtn').css('display', '');
        }
    })

    $('#btn_open_account').on('click', function () {
        parent.tab.tabAdd({
            title: '新开户-' + name,
            href: 'account/detail?name=' + encodeURI(name)
        });
    });

    //导出
    $('#exportExcel').on('click', function () {
        var saicInfoId = $('#saicInfoId').val();
        if (saicInfoId) {
            var downloadUrl = kyc.baseUrl + '/download?saicInfoId=' + saicInfoId;
            var isExist = $('#idown').length === 1;
            if (isExist) {
                $('#idown').attr('src', downloadUrl);
            } else {
                $('<iframe>', {id: 'idown', src: downloadUrl}).hide().appendTo('body');
            }
        } else {
            layerTips.msg('暂无相关信息,请稍候重试');
        }
    });
    var saicUrl = '';
    if (name) {
        saicUrl = kyc.baseUrl + '/saic/basic?keyword=' + encodeURI(name) + '&searchType=' + searchType;
    } else if (saicInfoId) {
        saicUrl = kyc.baseUrl + '/history/basic?saicInfoId=' + saicInfoId;
    }
    if (saicUrl) {
        loading.show();
        $.get(saicUrl).done(function (response) {
            loading.hide();
            var data = response.result;
            if (data) {
                if (data.id == null) {
                    data.id = "";
                }
                initCompany(data);

                //股权结构
                loading.show({
                    target: '#blockui_equity_portlet_body'
                });

                createStructure(data.id);

                //受益所有人
                loading.show({
                    target: '#blockui_beneficiary_portlet_body'
                });


                intervalBenificiary(data.id);
                $("#tab_2a").click(function () {
                    intervalBenificiaryUnnatural(data.id);
                });


                //董事会人员
                loadManagers(data.id);

                //实际控制人
                loadUltimateOwner(data.id);

                //影像资料
                if (applyId) {
                    getApplyOCRImages(applyId);
                } else {
                    //隐藏
                    $('#ocrImages').addClass('dn');
                }

                //股东出资情况
                loadStockholder(data.id, 3);//自然人
                $("#tab_5a").click(function () {
                    loadStockholderUnnatural(data.id);
                });

                //基本户履历
                loading.show({
                    target: '#blockui_base_portlet_body'
                });
                intervalBaseAccounts(data.id);

                //经营异常信息
                loadAbnormal(data.id);

                //严重违法信息
                loadIllegals(data.id);

                //工商变更信息
                loadChanges(data.id);

                //弹窗展示异常信息
                loadAnomalyInfo(data.id);


                //公司年报信息
                loadReport(data.id);
            } else {
                // layerTips.msg('客户尽调查询失败');
                layer.confirm('客户尽调查询失败:未找到符合企业，是否手动触发委托更新', {
                    closeBtn: 0,
                    title: "工商提示",
                    btn: ['是', '否'] //可以无限个按钮
                }, function(index, layero){
                    $('#name').html(name);
                    layer.close(index);
                }, function(index){
                    parent.tab.deleteTab(parent.tab.getCurrentTabId());
                });
                // parent.tab.deleteTab(parent.tab.getCurrentTabId());
            }
        });
    }

    $('#person-idCardType').change(function () {
        if ($(this).val() == "1") {
            $("#carrierBtn").show();
            $("#idCheckBtn").show();
        } else {
            $("#carrierBtn").hide();
            $("#idCheckBtn").hide();
        }
    });

    $('#blackCheckBtn').on('click', function () {
        if ($('#person-dob').val() == undefined || $('#person-dob').val() == '') {
            layerTips.msg('出生日期不能为空');
            return false;
        }
        if ($('#person-nationality').val() == undefined || $('#person-nationality').val() == '') {
            layerTips.msg('国籍不能为空');
            return false;
        }

        $.get(kyc.baseUrl + "/validation/blacklist?name=" + $('input[name="person-name"]').val() + "&bod="
            + $('#person-dob').val() + "&nationality=" + $('#person-nationality').val(), function (data) {
            if (data.code == 'ACK') {
                layerTips.msg(data.message);
            } else {
                layerTips.msg(data.message);
            }
        })
    })

    /**
     * 最终控制人的黑名单校验
     */
    $('#blackCheckForFinalBtn').on('click', function () {
        if ($('#uo-name').val() == undefined || $('#uo-name').val() == '') {
            layerTips.msg('姓名不能为空');
            return false;
        }
        if ($('#uo-dob').val() == undefined || $('#uo-dob').val() == '') {
            layerTips.msg('出生日期不能为空');
            return false;
        }
        if ($('#uo-nationality').val() == undefined || $('#uo-nationality').val() == '') {
            layerTips.msg('国籍不能为空');
            return false;
        }

        $.get(kyc.baseUrl + "/validation/blacklist?name=" + $('input[name="uo-name"]').val() + "&bod="
            + $('#uo-dob').val() + "&nationality=" + $('#uo-nationality').val(), function (data) {
            if (data.code == 'ACK') {
                layerTips.msg(data.message);
            } else {
                layerTips.msg(data.message);
            }
        })
    })

    $('#carrierBtn').on('click', function () {
        if ($('#person-idCardType').val() != '1') {
            layerTips.msg('证件类型为身份证才能进行运营商校验');
            return false;
        }
        if ($('#person-identifyNo').val() == undefined || $('#person-identifyNo').val() == '') {
            layerTips.msg('证件号码不能为空');
            return false;
        }
        if ($('#person-idCardType').val() == '1' && !idCardNoUtil.checkIdCardNo($('#person-identifyNo').val())) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }
        if ($('#person-telephone').val() == undefined || $('#person-telephone').val() == '') {
            layerTips.msg('联系号码不能为空');
            return false;
        }
        $.get("../../carrier/getCarrierOperatorResult?name=" + $('input[name="person-name"]').val()
            + "&mobile=" + $('#person-telephone').val() + "&cardno=" + $('#person-identifyNo').val(), function (data) {
            if (data.status == 'success') {
                layerTips.msg(data.result);
            } else {
                layerTips.msg(data.reason);
            }
        });
    });

    $('#idCheckBtn').on('click', function () {
        if ($('#person-idCardType').val() != '1') {
            layerTips.msg('证件类型为身份证才能进行联网核查');
            return false;
        }

        if ($('#person-identifyNo').val() == undefined || $('#person-identifyNo').val() == '') {
            layerTips.msg('证件号码不能为空');
            return false;
        }
        if ($('#person-idCardType').val() == '1' && !idCardNoUtil.checkIdCardNo($('#person-identifyNo').val())) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }
    });

    $('#searchBtn').on('click', function () {
        var keyword = $('input[name="org-name"]').val();
        if (keyword) {
            parent.tab.tabAdd({
                    title: '客户尽调',
                    href: 'kyc/detail.html?name=' + encodeURI(keyword)
                }
            );
        }
    });

    function bindBulu(selector) {

        //补录按钮
        $(selector + ' a.addition-btn').on('click', function () {

            var dataId = $(this).attr('data-id');
            var dataType = $(this).attr('data-type');
            var dataSaicId = $(this).attr('data-saic-id');
            var dataType2 = $(this).attr('data-type2');

            // alert(dataType2);
            //console.log(dataType2);

            if (dataType === 'directorate'//董事
                || dataType === 'supervisor'//监事
                || dataType === 'senior'//高管
                || arrContains(naturalType, dataType2)) {

                initBuluModal(dataType);

                $.get(kyc.baseUrl + '/supplement?id=' + dataId + "&type=" + dataType + "&saicInfoId=" + dataSaicId).done(function (data) {
                    if (data && data.result) {
                        var result = data.result;
                        $('#person-saic-id').val(dataSaicId);
                        $('#person-type').val(dataType);
                        $('#person-id').val(dataId);
                        $('#person-name').html(result.name || '');
                        $('#person-beneficiaryType').val(result.type);//类型，对应数据库type
                        $('input[name="person-name"]').val(result.name || '');
                        $('#person-capitalPercent').val(result.capitalpercent || '');//收益比例
                        $('#person-idCardType').val(result.idcardtype || result.identifytype || '0');
                        $('#person-idCardType').change();
                        $('#person-identifyNo').val(result.idcardno || result.identifyno || '');
                        $('#person-idCardDue').val(result.idcarddue || '');
                        $('#person-address').val(result.address || '');
                        $('#person-telephone').val(result.telephone || '');
                        $('#person-idCardStart').val(result.idCardStart || '');
                        $('#person-dob').val(result.dob || '');
                        $('#person-nationality').val(result.nationality || 'CHN');
                        $('#person-sex').val(result.sex || '');
                        $('#person-condate').val(result.condate || '');//出资日期
                        $('#person-fundedratio').val(result.fundedratio || '');//出资比例
                        $('#person-regcapcur').val(result.regcapcur || '');//币种
                        $('#person-realamount').val(result.realamount || '');//实缴出资额
                        $('#person-realtype').val(result.realtype || '');//实缴出资方式
                        $('#person-realdate').val(result.realdate || '');//实缴出资日期
                        $('#person-subconam').val(result.subconam || '');//认缴出资额
                        $('#person-investtype').val(result.investtype || '');//认缴出资方式
                    }
                    $('#buluModal').modal('show');
                });

            } else {

                initBuluOrgModal(dataType);

                $.get(kyc.baseUrl + '/supplement?id=' + dataId + "&type=" + dataType + "&saicInfoId=" + dataSaicId).done(function (data) {
                    if (data && data.result) {
                        var result = data.result;
                        $('#org-saic-id').val(dataSaicId);
                        $('#org-type').val(dataType);
                        $('#org-beneficiaryType').val(result.type);
                        $('#org-id').val(dataId);
                        $('#org-name').html(result.name || '');//名称
                        $('input[name="org-name"]').val(result.name || '');
                        $('#org-capitalPercent').val(result.capitalpercent || '');//收益比例
                        $('#org-dob').val(result.dob || '');//成立日期
                        $('#org-idCardType').val(result.idcardtype || result.identifytype || '0');//证件类型
                        $('#org-idCardType').change();
                        $('#org-identifyNo').val(result.idcardno || result.identifyno || '');//证件号码
                        $('#org-idCardStart').val(result.idCardStart || '');//证件起始日期
                        $('#org-idCardDue').val(result.idcarddue || '');//证件到日期
                        $('#org-nationality').val(result.nationality || 'CHN');//国籍
                        $('#org-address').val(result.address || '');//联系地址
                        $('#org-telephone').val(result.telephone || '');//联系电话
                        $('#org-condate').val(result.condate || '');//出资日期
                        $('#org-fundedratio').val(result.fundedratio || '');//出资比例
                        $('#org-regcapcur').val(result.regcapcur || '');//币种
                        $('#org-realamount').val(result.realamount || '');//实缴出资额
                        $('#org-realtype').val(result.realtype || '');//实缴出资方式
                        $('#org-realdate').val(result.realdate || '');//实缴出资日期
                        $('#org-subconam').val(result.subconam || '');//认缴出资额
                        $('#org-investtype').val(result.investtype || '');//认缴出资方式

                    }
                    $('#buluOrgModal').modal('show');
                });
            }

        });

    }

    //按钮功能
    $('#saveButton').click(function () {
        var postData = {
            saicInfoId: $('#person-saic-id').val(),
            type: $('#person-type').val(),
            beneficiaryType: $('#person-beneficiaryType').val(),
            id: $('#person-id').val(),
            capitalpercent: $('#person-capitalPercent').val(),//收益比例
            identifyType: $('#person-idCardType').val(),
            idCardType: $('#person-idCardType').val(),
            idCardNo: $('#person-identifyNo').val(),
            identifyno: $('#person-identifyNo').val(),
            idCardDue: $('#person-idCardDue').val(),
            address: $('#person-address').val(),
            telephone: $('#person-telephone').val(),
            idCardStart: $('#person-idCardStart').val(),
            dob: $('#person-dob').val(),
            nationality: $('#person-nationality').val(),
            sex: $('#person-sex').val(),
            name: $('#person-name').text(),
            condate: $('#person-condate').val(),//出资日期
            fundedratio: $('#person-fundedratio').val(),//出资比例
            regcapcur: $('#person-regcapcur').val(),//币种
            realamount: $('#person-realamount').val(),//实缴出资额
            realtype: $('#person-realtype').val(),//实缴出资方式
            realdate: $('#person-realdate').val(),//实缴出资日期
            subconam: $('#person-subconam').val(),//认缴出资额
            investtype: $('#person-investtype').val()//认缴出资方式
        };

        // if (!postData.idCardType && postData.idCardType != '0') {
        //     layerTips.msg('证件类型不能为空');
        //     return false;
        // }
        // if (!postData.idCardNo) {
        //     layerTips.msg('证件号码不能为空');
        //     return false;
        // }
        if (postData.idCardType == '1' && !idCardNoUtil.checkIdCardNo(postData.idCardNo)) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }
        var telReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
        if (postData.telephone && !telReg.test(postData.telephone)) {
            layerTips.msg('联系电话格式不正确');
            return false;
        }

        //$('#buluModal').modal('hide');
        loading.show();
        $.post(kyc.baseUrl + '/supplement', postData, function (result) {
            if (result.rel) {
                layerTips.msg('保存成功');
                var type = $('#person-type').val();
                var id = $('#person-id').val();

                $('#buluModal').modal('hide');

                //修改受益人证件信息
                if (type === 'beneficiary') {
                    $('.benificial-identifyType-' + id).html(common.checkLegalIdcardType($('#person-idCardType').val()));
                    $('.benificial-identifyNo-' + id).html($('#person-identifyNo').val());
                    $('.benificial-capitalPercent-' + id).html(calculatePercent($('#person-capitalPercent').val()));
                } else if (type === 'stockholder') {
                    // 股东
                    $('.stockholder-subconam-' + id).html($('#person-subconam').val());
                    $('.stockholder-condate-' + id).html($('#person-condate').val());
                    $('.stockholder-fundedratio-' + id).html($('#person-fundedratio').val());
                } else if (type === 'directorate') {
                    // 董事
                    $('.directorate_' + id + '_sex').html($('#person-sex').val());
                } else if (type === 'supervisor') {
                    // 监事
                    $('.supervisor_' + id + '_sex').html($('#person-sex').val());
                } else if (type === 'senior') {
                    // 高管
                    $('.senior_' + id + '_sex').html($('#person-sex').val());
                }
            } else {
                layerTips.msg(result.msg);
            }

        }).always(function () {
            loading.hide();
        });
    });

    //新增受益人保存按钮
    $('#saveBtn').click(function () {
        var postData = {
            saicInfoId: $('#saicInfoId').val(),
            type: 'beneficiary',
            beneficiaryType: $('#type').val(),
            capitalpercent: $('#capitalPercent').val(),//收益比例
            identifyType: $('#idCardType').val(),
            idCardType: $('#idCardType').val(),
            idCardNo: $('#identifyNo').val(),
            identifyno: $('#identifyNo').val(),
            name: $('#shouyiName').val(),
            idCardDue:$("#idCardDue").val(),
            idCardStart:$("#idCardStart").val(),
            address:$("#address2").val(),
            telephone:$("#telephone").val(),
            dob:$("#person-dob1").val(),
            nationality:$("#nationality").val(),
            sex:$("#person-sex1").val()
        };

        //当受益人为非自然人时，出生日期为空，使用成立日期
        if (postData.dob==null||postData.dob==""){
            postData.dob=$("#org-dob1").val();
        }
        //console.log('saicId:'+$('#saicInfoId').val());


        if(postData.capitalpercent==""|| postData.capitalpercent==null){
            layerTips.msg('请输入受益比例');
            return false;
        }

        if (postData.idCardType == '1' && !idCardNoUtil.checkIdCardNo(postData.idCardNo)) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }

        loading.show();
        $.post(kyc.baseUrl + '/supplement', postData, function (result) {
            if (result.rel) {
                layerTips.msg('保存成功');
                var type = $('#type').val();
                var id = $('#id').val();

                $('#buluAddModal').modal('hide');

                //修改受益人证件信息
                if (type === 'beneficiary') {
                    $('.benificial-identifyType-' + id).html(common.checkLegalIdcardType($('#idCardType').val()));
                    $('.benificial-identifyNo-' + id).html($('#identifyNo').val());
                    $('.benificial-capitalPercent-' + id).html(calculatePercent($('#capitalPercent').val()));
                }
            } else {
                layerTips.msg(result.msg);
            }

        }).always(function () {
            loading.hide();
            intervalBenificiary($('#saicInfoId').val());
            initBuluAddModal();
        });
    });

    $('#saveOrgButton').click(function () {
        var postData = {
            saicInfoId: $('#org-saic-id').val(),
            type: $('#org-type').val(),
            beneficiaryType:$('#org-beneficiaryType').val(),
            id: $('#org-id').val(),
            capitalpercent: $('#org-capitalPercent').val(),//收益比例
            dob: $('#org-dob').val(),//成立日期
            idCardType: $('#org-idCardType').val(),//证件类型
            idCardNo: $('#org-identifyNo').val(),
            identifyno: $('#org-identifyNo').val(),//证件号码
            idCardStart: $('#org-idCardStart').val(),//证件起始日期
            idCardDue: $('#org-idCardDue').val(),//证件到日期
            nationality: $('#org-nationality').val(),//国籍
            address: $('#org-address').val(),//联系地址
            telephone: $('#org-telephone').val(),//联系电话
            name: $('#org-name').text(),
            condate: $('#org-condate').val(),//出资日期
            fundedratio: $('#org-fundedratio').val(),//出资比例
            regcapcur: $('#org-regcapcur').val(),//币种
            realamount: $('#org-realamount').val(),//实缴出资额
            realtype: $('#org-realtype').val(),//实缴出资方式
            realdate: $('#org-realdate').val(),//实缴出资日期
            subconam: $('#org-subconam').val(),//认缴出资额
            investtype: $('#org-investtype').val()//认缴出资方式
        };

        // if (!postData.idCardType && postData.idCardType != '0') {
        //     layerTips.msg('证件类型不能为空');
        //     return false;
        // }
        // if (!postData.idCardNo) {
        //     layerTips.msg('证件号码不能为空');
        //     return false;
        // }
        if (postData.idCardType == '1' && !idCardNoUtil.checkIdCardNo(postData.idCardNo)) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }
        var telReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
        if (postData.telephone && !telReg.test(postData.telephone)) {
            layerTips.msg('联系电话格式不正确');
            return false;
        }

        loading.show();
        $.post(kyc.baseUrl + '/supplement', postData, function (result) {
            if (result.rel) {
                layerTips.msg('保存成功');
                var type = $('#org-type').val();
                var id = $('#org-id').val();

                $('#buluOrgModal').modal('hide');

                //修改受益人证件信息
                if (type === 'beneficiary') {
                    $('.benificial-identifyType-' + id).html(common.checkOrgType($('#org-idCardType').val()));
                    $('.benificial-identifyNo-' + id).html($('#org-identifyNo').val());
                    $('.benificial-capitalPercent-' + id).html(calculatePercent($('#org-capitalPercent').val()));
                } else if (type === 'stockholder') {
                    // 股东
                    $('.stockholder-subconam-' + id).html($('#org-subconam').val());
                    $('.stockholder-condate-' + id).html($('#org-condate').val());
                    $('.stockholder-fundedratio-' + id).html($('#org-fundedratio').val());
                } else if (type === 'directorate') {
                    // 董事
                    $('.directorate_' + id + '_sex').html($('#org-sex').val());
                } else if (type === 'supervisor') {
                    // 监事
                    $('.supervisor_' + id + '_sex').html($('#org-sex').val());
                } else if (type === 'senior') {
                    // 高管
                    $('.senior_' + id + '_sex').html($('#org-sex').val());
                }
            } else {
                layerTips.msg(result.msg);
            }

        }).always(function () {
            loading.hide();
        });
    });

    $('#saveUO').click(function () {
        var postData = {
            saicInfoId: $('#uo-saic-id').val(),
            type: $('#uo-type').val(),
            name: $('#uo-name').val(),
            id: $('#uo-id').val(),
            identifyType: $('#uo-idCardType').val(),
            idCardType: $('#uo-idCardType').val(),
            idCardNo: $('#uo-identifyNo').val(),
            identifyno: $('#uo-identifyNo').val(),
            idCardDue: $('#uo-idCardDue').val(),
            address: $('#uo-address').val(),
            telephone: $('#uo-telephone').val(),
            idCardStart: $('#uo-idCardStart').val(),
            dob: $('#uo-dob').val(),
            nationality: $('#uo-nationality').val(),
            sex: $('#uo-sex').val()
        };

        if (!postData.name) {
            layerTips.msg('姓名不能为空');
            return false;
        }
        if (!postData.idCardType && postData.idCardType != '0') {
            layerTips.msg('证件类型不能为空');
            return false;
        }
        if (!postData.idCardNo) {
            layerTips.msg('证件号码不能为空');
            return false;
        }
        if (postData.idCardType == '1' && !idCardNoUtil.checkIdCardNo(postData.idCardNo)) {
            layerTips.msg('证件号码格式不正确');
            return false;
        }

        var telReg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
        if (postData.telephone && !telReg.test(postData.telephone)) {
            layerTips.msg('联系电话格式不正确');
            return false;
        }

        loading.show();
        $.post(kyc.baseUrl + '/supplement', postData, function (result) {
            if (result.rel) {
                layerTips.msg('保存成功');
                var type = $('#uo-type').val();
                $('#uo-id').val(result.result.id);
            } else {
                layerTips.msg(result.msg);
            }

        }).always(function () {
            loading.hide();
        });

    });

    $('.date-picker').change(function () {
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    function getApplyOCRImages(applyId) {
        $.get('../../apply/mina/ocr?applyId=' + applyId, null, function (data) {
            if (data.rel) {
                var imgHtml = "";
                var result = data.result;
                for (var i = 0; i < result.length; i++) {
                    var path = result[i].imgpath.replace(" ", "");
                    if (i === 0 || i % 3 === 0) {
                        imgHtml += '<div class="row">';
                    }
                    imgHtml += "<div class='col-md-4' ><img width='100%' src='" + path + "'/></div>";
                    if (i % 3 === 2 || i === result.length - 1) {
                        imgHtml += '</div>';
                    }
                }

                $('.ocrImages').html(imgHtml);
            }
        });
    }

    function initBuluModal(dataType) {
        $('#person-name').html('');
        $('#person-capitalPercent').val('');//收益比例
        $('#person-idCardType').val('其他');
        $('#person-identifyNo').val('');
        $('#person-idCardDue').val('');
        $('#person-address').val('');
        $('#person-telephone').val('');
        $('#person-sex').val('');
        $('#person-nationality').val('CHN');
        $('#person-idCardStart').val('');
        $('#person-dob').val('');
        $('#person-condate').val('');//出资日期
        $('#person-fundedratio').val('');//出资比例
        $('#person-regcapcur').val('');//币种
        $('#person-realamount').val('');//实缴出资额
        $('#person-realtype').val('');//实缴出资方式
        $('#person-realdate').val('');//实缴出资日期
        $('#person-subconam').val('');//认缴出资额
        $('#person-investtype').val('');//认缴出资方式

        $("#buluModal .form-group").hide();

        if (dataType === "beneficiary") {//受益所有人
            showFormGroups([
                "person-name"//姓名
                ,"person-capitalPercent"//受益比例
                ,"person-sex"//性别
                ,"person-dob"//出生日期
                ,"person-idCardType"//证件类型
                ,"person-identifyNo"//证件号码
                ,"person-idCardStart"//证件起始日期
                ,"person-idCardDue"//证件到期日
                ,"person-nationality"//国籍
                ,"person-address"//联系地址
                ,"person-telephone"//联系电话
            ]);
        } else if (dataType === "directorate"//董事
            || dataType === "supervisor"//监事
            || dataType === "senior"//高管
        ) {
            showFormGroups([
                "person-name"//姓名
                ,"person-sex"//性别
                ,"person-dob"//出生日期
                ,"person-idCardType"//证件类型
                ,"person-identifyNo"//证件号码
                ,"person-idCardStart"//证件起始日期
                ,"person-idCardDue"//证件到期日
                ,"person-nationality"//国籍
                ,"person-address"//联系地址
                ,"person-telephone"//联系电话
            ]);
        } else if (dataType === "stockholder") {//股东
            showFormGroups([
                "person-name"//姓名
                ,"person-sex"//性别
                ,"person-dob"//出生日期
                ,"person-idCardType"//证件类型
                ,"person-identifyNo"//证件号码
                ,"person-idCardStart"//证件起始日期
                ,"person-idCardDue"//证件到期日
                ,"person-nationality"//国籍
                ,"person-address"//联系地址
                ,"person-telephone"//联系电话
                ,"person-condate"//出资日期
                ,"person-fundedratio"//出资比例
                ,"person-regcapcur"//币种
                ,"person-realamount"//实缴出资额
                ,"person-realtype"//实缴出资方式
                ,"person-realdate"//实缴出资日期
                ,"person-subconam"//认缴出资额
                ,"person-investtype"//认缴出资方式
            ]);
        }
    }

    //显示补录弹出框中的form-group
    //arr 为form-group其中某个子项的id的集合
    function showFormGroups(arr) {
        for (var i = 0; i < arr.length; i++) {
            showFormGroup($("#" + arr[i]));
        }
    }

    //根据form-group子项的id获取form-group，并显示
    function showFormGroup(child) {
        if (child !== undefined) {
            if (child.attr("class") !== undefined && child.attr("class").indexOf("form-group") > -1) {
                child.show();
            } else {
                showFormGroup(child.parent());
            }
        }
    }

    //新增受益人初始化Modal
    function initBuluAddModal() {
        $('#shouyiName').val('');
        $('#capitalPercent').val('0');//收益比例
        $('#idCardType').val('0');
        $('#identifyno').val('');
        $('#type').val('自然人股东');
        $("#idCardDue").val("");
        $("#idCardStart").val("");
        $("#address2").val("");
        $("#telephone").val("");
        $("#person-dob1").val("");
        $("#nationality").val("");
        $("#person-sex1").val("");
        $("#org-dob1").val("");
        $("#type").change();
    }

    //['自然人股东', '自然人', '个人', '境内中国公民', '非农民自然人', '外籍自然人', '台湾居民', '外国公民', '华侨', '香港居民',
    // '合伙人', '农民自然人', '其他股东', '个人独资', '普通合伙人', '有限合伙人']
    $("#type").change(function (value) {
        if (!arrContains(naturalType, $("#type").val())){

            $("#orgDobDiv").attr("style","display:");
            $("#sexDiv").attr("style","display:none");
            $("#personDobDiv").attr("style","display:none");

            $("#idCardType").find("option").remove();
            $("#idCardType").append("<option value=''>请选择</option>");
            $("#idCardType").append("<option value='01'>01-社会统一信用代码</option>");
            $("#idCardType").append("<option value='02'>02-工商营业执照</option>");
            $("#idCardType").append("<option value='03'>03-批文</option>");
            $("#idCardType").append("<option value='04'>04-登记证书</option>");
            $("#idCardType").append("<option value='05'>05-开户证明</option>");
            $("#idCardType").append("<option value='06'>06-其他</option>");

        }else{
            $("#orgDobDiv").attr("style","display:none");
            $("#sexDiv").attr("style","display:");
            $("#personDobDiv").attr("style","display:");

            $("#idCardType").find("option").remove();
            $("#idCardType").append('<option value="0" selected>其他</option>');
            $("#idCardType").append('<option value="1">1-身份证</option>');
            $("#idCardType").append('<option value="2">2-军官证</option>');
            $("#idCardType").append('<option value="3">3-文职干部证</option>');
            $("#idCardType").append('<option value="4">4-警官证</option>');
            $("#idCardType").append('<option value="5">5-士兵证</option>');
            $("#idCardType").append('<option value="6">6-护照</option>');
            $("#idCardType").append('<option value="7">7-港、澳、台居民通行证</option>');
            $("#idCardType").append('<option value="8">8-户口簿</option>');
            $("#idCardType").append('<option value="9">9-其它合法身份证件</option>');
        }
    });

    function initBuluOrgModal(dataType) {
        $('#org-name').html('');//名称
        $('#org-capitalPercent').val('');//收益比例
        $('#org-dob').val('');//成立日期
        $('#org-idCardType').val('');//证件类型
        $('#org-identifyNo').val('');//证件号码
        $('#org-idCardStart').val('');//证件起始日期
        $('#org-idCardDue').val('');//证件到日期
        $('#org-nationality').val('CHN');//国籍
        $('#org-address').val('');//联系地址
        $('#org-telephone').val('');//联系电话
        $('#org-condate').val('');//出资日期
        $('#org-fundedratio').val('');//出资比例
        $('#org-regcapcur').val('');//币种
        $('#org-realamount').val('');//实缴出资额
        $('#org-realtype').val('');//实缴出资方式
        $('#org-realdate').val('');//实缴出资日期
        $('#org-subconam').val('');//认缴出资额
        $('#org-investtype').val('');//认缴出资方式

        $("#buluOrgModal .form-group").hide();

        if (dataType === "beneficiary") {//受益所有人
            showFormGroups([
                "org-name"//姓名
                ,"org-capitalPercent"//受益比例
                ,"org-dob"//成立日期
                ,"org-idCardType"//证件类型
                ,"org-identifyNo"//证件号码
                ,"org-idCardStart"//证件起始日期
                ,"org-idCardDue"//证件到期日
                ,"org-nationality"//国籍
                ,"org-address"//联系地址
                ,"org-telephone"//联系电话
            ]);
        } else if (dataType === "directorate"//董事
            || dataType === "supervisor"//监事
            || dataType === "senior"//高管
        ) {
            showFormGroups([
                "org-name"//姓名
                ,"org-dob"//成立日期
                ,"org-idCardType"//证件类型
                ,"org-identifyNo"//证件号码
                ,"org-idCardStart"//证件起始日期
                ,"org-idCardDue"//证件到期日
                ,"org-nationality"//国籍
                ,"org-address"//联系地址
                ,"org-telephone"//联系电话
            ]);
        } else if (dataType === "stockholder") {//股东
            showFormGroups([
                "org-name"//姓名
                ,"org-dob"//成立日期
                ,"org-idCardType"//证件类型
                ,"org-identifyNo"//证件号码
                ,"org-idCardStart"//证件起始日期
                ,"org-idCardDue"//证件到期日
                ,"org-nationality"//国籍
                ,"org-address"//联系地址
                ,"org-telephone"//联系电话
                ,"org-condate"//出资日期
                ,"org-fundedratio"//出资比例
                ,"org-regcapcur"//币种
                ,"org-realamount"//实缴出资额
                ,"org-realtype"//实缴出资方式
                ,"org-realdate"//实缴出资日期
                ,"org-subconam"//认缴出资额
                ,"org-investtype"//认缴出资方式
            ]);
        }
    }

    function initCompany(data) {
        //sumary
        $('#name').html(data.name);
        $('#state').html(data.state);
        if(!data.state) $('#state').parent().remove();
        $('#legalPersonType').html(data.legalpersontype || '法定代表人');
        $('#legalPerson').html(data.legalperson);
        $('#registFund').html(data.registfund);
        $('#openDate').html(data.opendate);
        $('#address').html(data.address);
        //获得委托更新状态
        getEntrustUpdateResult(data);

        $('#saicInfoId').val(data.id);

        //加载基本信息模板
        saic.loadBasicTemplate(data, '#tab_0', '基本信息');
    }

    var createStructureNum = 10;//当查询出现错误时，最多循环查询10次
    function createStructure(id) {
        $.get(historyUrl + '/equityshare?saicInfoId=' + id).done(function (data) {
            var result = data.result;
            if (result && !jQuery.isEmptyObject(result)) {
                //构建股权结构树
                initTree(result);
                loading.hide('#blockui_equity_portlet_body');
            } else {
                if (data.rel) {
                    if (createStructureNum-- > 0) {
                        window.setTimeout(function () {
                            createStructure(id);
                        }, 2000);
                    }
                } else {
                    layerTips.msg(data.msg);
                }

            }
        }).fail(function (result) {

            loading.hide('#blockui_equity_portlet_body');
        });
    }

    function initTree(data) {
        $("#equityShareTree").jstree({
            "core": {
                "themes": {
                    "responsive": false
                },
                // so that create works
                "check_callback": true,
                'data': [data]
            },
            "types": {
                "default": {
                    "icon": "fa fa-folder icon-state-warning icon-lg"
                },
                "file": {
                    "icon": "fa fa-file icon-state-warning icon-lg"
                }
            },
            "state": {"key": "equityShare"},
            "plugins": ["state", "types"]
        });
    }

    var intervalBenificiaryNum = 10;//当查询出现错误时，最多循环查询10次
    /**
     * 获取自然人的受益人信息
     * @param id
     */
    function intervalBenificiary(id) {
        $.get(historyUrl + '/beneficiary?saicInfoId=' + id +"&naturalPerson="+true).done(function (data) {
            if (data && data.result && data.result.length > 0) {
                var beneficiaryList = [];

                var result = data.result;
                for (var i = 0; i < result.length; i++) {
                    beneficiaryList.push({
                        id: result[i].id,
                        name: result[i].name,
                        type: result[i].type,
                        capitalPercent: result[i].capitalpercent,
                        capital: result[i].capital,
                        identifyType: result[i].identifytype,
                        identifyno: result[i].identifyno
                    });
                }
                createBeneficial(id, beneficiaryList, '.beneficiary');
            } else if (data && data.result && data.result.length  === 0){
                //只存在非自然人类型受益人的情况
            } else {
                if (data.rel) {
                    if (intervalBenificiaryNum-- > 0) {
                        window.setTimeout(function () {
                            intervalBenificiary(id);
                        }, 2000);
                    } else {
                        createBeneficial(id, [], '.beneficiary');
                    }
                } else {
                    layerTips.msg(data.msg);
                }
            }
            loading.hide('#blockui_beneficiary_portlet_body');
        }).fail(function (result) {

            loading.hide('#blockui_beneficiary_portlet_body');
        });
    }

    /**
     * 获取非自然人的受益人信息
     * @param id
     */
    function intervalBenificiaryUnnatural(id) {
        $.get(historyUrl + '/beneficiary?saicInfoId=' + id + "&naturalPerson=" + false).done(function (data) {
            if (data && data.result && data.result.length > 0) {
                $("#npDiv").show();
                layer.msg("部分无法穿透，请人工介入。");

                var beneficiaryList = [];
                var result = data.result;
                for (var i = 0; i < result.length; i++) {
                    beneficiaryList.push({
                        id: result[i].id,
                        name: result[i].name,
                        type: result[i].type,
                        capitalPercent: result[i].capitalpercent,
                        capital: result[i].capital,
                        identifyType: result[i].identifytype,
                        identifyno: result[i].identifyno
                    });
                }

                createBeneficial(id, beneficiaryList, '.beneficiary2');
            }
        }).fail(function (result) {
            //loading.hide('#blockui_beneficiary_portlet_body');
        });
    }

    function createBeneficial(id, beneficiaryList, cssName) {
        var innerHtml = '';
        if (beneficiaryList) {
            var length = beneficiaryList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th class="long-name">受益所有人</th><th>类型</th><th>受益比例</th><t' +
                    'h>证件类型</th><th>证件号码</th><th>最终受益人</th></tr> </thead> <tbody>';
                var normalCapitalPercentData = [];
                var tooSmallCapitalPercentData = [];
                for (var i = 0; i < length; i++) {
                    if (beneficiaryList[i].capitalPercent.indexOf('E') > 0
                        || beneficiaryList[i].capitalPercent === '0.0') {
                        tooSmallCapitalPercentData.push(beneficiaryList[i]);
                    } else {
                        normalCapitalPercentData.push(beneficiaryList[i]);
                    }
                }

                //处理正常百分比
                innerHtml += createBeneficiaryTableByDataList(id, normalCapitalPercentData);
                //处理太小的百分比
                innerHtml += createBeneficiaryTableByDataList(id, tooSmallCapitalPercentData);

                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }

            //展示导出按钮
            // todo 导出需要受益人结果信息
            showExportExcelBtn();
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $(cssName).html(innerHtml);
        bindBulu(cssName);
    }

    function showExportExcelBtn() {
        $('#exportExcel').removeClass('dn');
    }

    function createBeneficiaryTableByDataList(id, beneficiaryList) {
        var length = beneficiaryList.length;
        var innerHtml = '';
        for (var i = 0; i < length; i++) {
            var dataStr = 'data-saic-id="' + id + '" data-type="beneficiary" data-id="' + beneficiaryList[i].id + '" data-type2="' + beneficiaryList[i].type + '"';
            var highlightClass = '';
            if (beneficiaryList[i].capitalPercent && beneficiaryList[i].capitalPercent > 0.25) {
                highlightClass = 'final-benificial';
            }


            var identifyTypeName = '';
            var dataType2 = tranlate(beneficiaryList[i].type);
            if (arrContains(naturalType, dataType2)) {
                identifyTypeName = common.checkLegalIdcardType(tranlate(beneficiaryList[i].identifyType));
            } else {
                identifyTypeName = common.checkOrgType(tranlate(beneficiaryList[i].identifyType));
            }

            innerHtml += '<tr class="' + highlightClass + '"><td class="oneline-break" title="' + beneficiaryList[i].name + '">' + beneficiaryList[i].name + '</td>' +
                '<td>' + tranlate(beneficiaryList[i].type) + '</td>' +
                '<td class="benificial-capitalPercent-' + beneficiaryList[i].id +
                '">' + calculatePercent(beneficiaryList[i].capitalPercent) + '</td>' +
                '<td class="benificial-identifyType-' + beneficiaryList[i].id +
                '">' + identifyTypeName + '</td>' +
                '<td class="oneline-break benificial-identifyNo-' + beneficiaryList[i].id +
                '">' + tranlate(beneficiaryList[i].identifyno) + '</td>' +
                '<td><a href="javascript:void(0)" ' + dataStr + ' class="addition-btn btn btn-outline btn-xs blue"> ' +
                '<i class="fa fa-file-text-o"></i> 补录 </a></td></tr>';
        }

        return innerHtml;
    }

    function tranlate(value) {
        if (value) {
            return value;
        } else {
            return '';
        }
    }

    function calculatePercent(capitalPercent) {
        if (capitalPercent.indexOf('E') > 0 || capitalPercent === '0.0') {
            return "占比太小";
        } else {
            return accMul(capitalPercent, 100) + '%';
        }
    }

    function accMul(arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }

    function loadUltimateOwner(id) {
        $('#uo-saic-id').val(id);
        $.get(kyc.baseUrl + '/supplement?type=ultimateOwner&saicInfoId=' + id).done(function (data) {
            if (data && data.result) {
                var result = data.result;
                $('#uo-id').val(result.id);
                $('#uo-name').val(result.name || '');
                $('#uo-idCardType').val(result.idcardtype || result.identifytype || '0');
                $('#uo-identifyNo').val(result.idcardno || result.identifyno || '');
                $('#uo-idCardDue').val(result.idcarddue || '');
                $('#uo-address').val(result.address || '');
                $('#uo-telephone').val(result.telephone || '');
                $('#uo-idCardStart').val(result.idCardStart || '');
                $('#uo-dob').val(result.dob || '');
                $('#uo-nationality').val(result.nationality || 'CHN');
                $('#uo-sex').val(result.sex || '');
            }
        });
    }

    function loadManagers(id) {
        $.get(historyUrl + '/managers?saicInfoId=' + id).done(function (data) {
            var result = data.result;
            if (result && !jQuery.isEmptyObject(result)) {

                createPeopleTable(id, result.directorList, 'directorate');
                createPeopleTable(id, result.superviseList, 'supervisor');

                createPeopleTable(id, result.managementList, 'senior');

            }
            loading.hide('#blockui_equity_portlet_body');
        }).fail(function (result) {

            loading.hide('#blockui_equity_portlet_body');
        });
    }

    function createPeopleTable(id, dataList, cssElement) {
        var innerHtml = '';
        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>姓名</th><th>性别</th><th>职务</th><th>操作</th></tr> </thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    var dataStr = 'data-saic-id="' + id + '" data-type="' + cssElement + '" data-id="' + dataList[i].id + '" data-type2="' + dataList[i].type + '"';
                    innerHtml += '<tr ><td>' + dataList[i].name + '</td>' +
                        '<td class="' + cssElement + '_' + dataList[i].id + '_sex">' + tranlate(dataList[i].sex) + '</td>' +
                        '<td>' + tranlate(dataList[i].position) + '</td>' +
                        '<td><a href="javascript:void(0)" ' + dataStr + ' class="addition-btn btn btn-outline btn-xs' +
                        ' blue"> ' +
                        '<i class="fa fa-file-text-o"></i> 补录 </a></td></tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.' + cssElement).html(innerHtml);
        bindBulu('.' + cssElement);
    }

    function loadStockholder(id, num) {
        $.get(historyUrl + '/stockholders?saicInfoId=' + id + "&naturalPerson=" + true).done(function (data) {
            if (data && !jQuery.isEmptyObject(data)) {
                createStockholder(id, data.result, '.stockholder');
            } else {
                if (data.rel) {
                    if (num-- >= 0) {
                        window.setTimeout(function () {
                            loadStockholder(id, num);
                        }, 2000);
                    } else {
                        createStockholder(id, []);
                    }
                } else {
                    layerTips.msg(data.msg);
                }
            }
        }).fail(function (result) {
        });
    }

    function loadStockholderUnnatural(id, num) {
        $.get(historyUrl + '/stockholders?saicInfoId=' + id + "&naturalPerson=" + false).done(function (data) {
            if (data && data.result && data.result.length > 0) {
                $("#npDiv2").show();
                layer.msg("部分无法穿透，请人工介入。");
                createStockholder(id, data.result, '.stockholder2');
            } else {
                if (data.rel) {
                    if (num-- >= 0) {
                        window.setTimeout(function () {
                            loadStockholderUnnatural(id, num);
                        }, 2000);
                    } else {
                        createStockholder(id, [], '.stockholder2');
                    }
                } else {
                    layerTips.msg(data.msg);
                }
            }
        }).fail(function (result) {
            //loading.hide('#blockui_beneficiary_portlet_body');
        });
    }

    function createStockholder(id, dataList, cssName) {
        var innerHtml = '';

        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th class="long-name">股东名称</th><th>股东类型</th><th>认缴出资额（万元)</th><th>出资日期</th>' +
                    '<th>出资比例</th><th>操作</th></tr> </thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    var dataStr = 'data-saic-id="' + id + '" data-type="stockholder" data-id="' + dataList[i].id + '" data-type2="' + dataList[i].strtype + '"';
                    innerHtml += '<tr ><td class="oneline-break" title="' + dataList[i].name + '">' + dataList[i].name + '</td>' +
                        '<td>' + tranlate(dataList[i].strtype) + '</td>' +
                        '<td class="stockholder-subconam-' + dataList[i].id + '">' + tranlate(dataList[i].subconam) + '</td>' +
                        '<td class="stockholder-condate-' + dataList[i].id + '">' + dateTranslate(tranlate(dataList[i].condate)) + '</td>' +
                        '<td class="stockholder-fundedratio-' + dataList[i].id + '">' + tranlate(dataList[i].fundedratio) + '</td>' +
                        '<td><a href="javascript:void(0)" ' + dataStr + ' class="addition-btn btn btn-outline btn-xs' +
                        ' blue"> ' +
                        '<i class="fa fa-file-text-o"></i> 补录 </a></td></tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $(cssName).html(innerHtml);
        bindBulu(cssName);
    }


    function createReport(id, dataList) {
        var innerHtml = '';

        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th class="long-name">序号</th><th>报送年度</th><th>发布时间</th></thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    innerHtml += '<tr >' +
                        '<td>' + (i + 1) + '</td>' +
                        '<td>' + tranlate(dataList[i].annualreport) + '</td>' +
                        '<td>' + tranlate(dataList[i].releasedate) + '</td>' +
                        '</tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.report').html(innerHtml);
    }


    var intervalBaseAccountsNum = 10;//当查询出现错误时，最多循环查询10次
    function intervalBaseAccounts(id) {
        $.get(historyUrl + '/baseaccount?saicInfoId=' + id).done(function (data) {
            if (data && data.result) {
                var baseAccountList = [];

                var result = data.result;
                for (var i = 0; i < result.length; i++) {
                    baseAccountList.push({
                        name: result[i].name,
                        licenseKey: result[i].licensekey,
                        licenseOrg: result[i].licenseorg,
                        licenseDate: result[i].licensedate,
                        licenseType: result[i].licensetype
                    });
                }
                createBaseAccounts(baseAccountList);
                loading.hide('#blockui_base_portlet_body');
            } else {
                if (data.rel) {
                    if (intervalBaseAccountsNum-- > 0) {
                        window.setTimeout(function () {
                            intervalBaseAccounts(id);
                        }, 2000);
                    }
                } else {
                    layerTips.msg(data.msg);
                }

            }
        }).fail(function (result) {
            loading.hide('#blockui_base_portlet_body');
        });
    }

    function createBaseAccounts(dataList) {
        var innerHtml = '';
        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>单位名称</th><th>许可证号</th><th>审批机关</th><th>审批日期</th><th>许可类型</th>' +
                    '</tr> </thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    innerHtml += '<tr ><td>' + dataList[i].name + '</td>' +
                        '<td>' + tranlate(dataList[i].licenseKey) + '</td>' +
                        '<td>' + tranlate(dataList[i].licenseOrg) + '</td>' +
                        '<td>' + tranlate(dataList[i].licenseDate) + '</td>' +
                        '<td>' + tranlate(dataList[i].licenseType) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.baseAccount').html(innerHtml);
    }

    function loadAbnormal(id) {
        $.get(historyUrl + '/changemess?saicInfoId=' + id).done(function (data) {
            if (data && !jQuery.isEmptyObject(data)) {
                createAbnormal(data.result);
            }
        }).fail(function (result) {
        });
    }

    function createAbnormal(dataList) {
        var innerHtml = '';
        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>列入经营异常名录原因</th><th>列入日期</th><th>移出经营异常名录原因</th><th>移出日期</th><th>作出决定机关</th>' +
                    '</tr> </thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    innerHtml += '<tr ><td>' + dataList[i].inreason + '</td>' +
                        '<td>' + tranlate(dataList[i].indate) + '</td>' +
                        '<td>' + tranlate(dataList[i].outreason) + '</td>' +
                        '<td>' + tranlate(dataList[i].outdate) + '</td>' +
                        '<td>' + tranlate(dataList[i].belongorg) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.abnormal').html(innerHtml);
    }

    function loadIllegals(id) {
        /*var illegals;
        $.ajax({
            url: historyUrl + '/illegals?saicInfoId=' + id,
            async: false,
            type: "GET",
            success: function (data) {
                if (data && !jQuery.isEmptyObject(data)) {
                    createBreak(data.result);
                    illegals = data.result;
                }
            }
        });
        return illegals;*/
        $.get(historyUrl + '/illegals?saicInfoId=' + id).done(function (data) {
            if (data && !jQuery.isEmptyObject(data)) {
                createBreak(data.result);
                illegals = data.result;
            }
        }).fail(function (result) {
        });
    }

    function loadChanges(id) {
        $.get(historyUrl + '/changes?saicInfoId=' + id).done(function (data) {
            if (data.code === 'ACK') {
                laytpl($("#changesTemplate").html()).render({changes: data.data}, function (html) {
                    $("#changesholder").html(html);
                });
            } else {
                layerTips.msg('暂无相关信息,请稍候重试');
            }
        });
    }

    function loadReport(id) {
        $.get(historyUrl + '/report?saicInfoId=' + id).done(function (data) {
            if (data && !jQuery.isEmptyObject(data)) {
                createReport(id, data.result);
            }
        }).fail(function (result) {
        });
    }

    function createBreak(dataList) {
        var innerHtml = '';
        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th>类别</th><th>列入严重违法失信企业名单（黑名单）原因</th><th>列入日期</th><th>做出决定机关(列入)</th><th>移出严重违法失信企业名单(黑名单)原因</th>' +
                    '<th>移出日期</th><th>作出决定机关(移出)</th></tr> </thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    innerHtml += '<tr ><td>' + tranlate(dataList[i].type) + '</td>' +
                        '<td>' + tranlate(dataList[i].reason) + '</td>' +
                        '<td>' + tranlate(dataList[i].date) + '</td>' +
                        '<td>' + tranlate(dataList[i].organ) + '</td>' +
                        '<td>' + tranlate(dataList[i].reasonout) + '</td>' +
                        '<td>' + tranlate(dataList[i].dateout) + '</td>' +
                        '<td>' + tranlate(dataList[i].organout) + '</td></tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.break').html(innerHtml);
    }

    function createReport(id, dataList) {
        var innerHtml = '';

        if (dataList) {
            var length = dataList.length;
            if (length > 0) {
                innerHtml = '<table class="table table-bordered"> <thead>' +
                    '<tr><th class="long-name">序号</th><th>报送年度</th><th>发布时间</th></thead> <tbody>';
                for (var i = 0; i < length; i++) {
                    innerHtml += '<tr >' +
                        '<td>' + (i + 1) + '</td>' +
                        '<td>' + tranlate(dataList[i].annualreport) + '</td>' +
                        '<td>' + tranlate(dataList[i].releasedate) + '</td>' +
                        '</tr>';
                }
                innerHtml += '</tbody></table>';
            } else {
                innerHtml = '<span>暂无相关信息</span>';
            }
        } else {
            innerHtml = '<span>暂无相关信息</span>';
        }
        $('.report').html(innerHtml);
    }

    function dateTranslate(date) {
        if (date === '9999年12月31日') {
            return '-';
        } else {
            return date;
        }
    }

    var loadAnomalyInfo = function (id) {
        // $.get('../../validate/saic/local?keyword=' + encodeURI(name)).done(function (data) {
        $.get('../../validate/saic/local?saicInfoId=' + encodeURI(id)).done(function (data) {
            var msg = data.msg;
            var rel = data.rel;
            var result = data.result;
            if (!rel) {
                layer.confirm(msg, {
                    title: "工商提示",
                    btn: ['查看详情', '关闭'] //按钮
                }, function (index) {
                    showTabByHref(result);
                    fieldRed(msg);
                    layer.close(index);
                }, function (index) {
                    layer.close(index);
                });
            }
        }).fail(function (msg) {
            layer.alert("工商提示出错,请稍候重试", {
                title: "工商提示",
                closeBtn: 0
            });
        });
    }

    function showTabByHref(tabHref) {
        $('#kycTabs a[href="#' + tabHref + '"]').tab('show');
        $("html,body").animate({scrollTop: $('#kycTabs a[href="#' + tabHref + '"]').offset().top}, 100);
    }

    var fieldRed = function (msg) {
        if (msg.indexOf("工商状态") > -1) {
            $(".saicState").css("color", "red");
        } else if (msg.indexOf("营业期限") > -1) {
            $(".saicEnddate").css("color", "red");
        }
    }


    $('#printBtn').click(function(){
        var saiccId = $("#saicInfoId").val();
        var par;
        var html = '<div style="margin: 20px">'
            +'<form class="layui-form" action="">'
            + '<input type="checkbox" id="kyc-jiben" lay-skin="primary" title="基本信息" checked="true" disabled="true">'
            + '<input type="checkbox" id="kyc-guquan" lay-skin="primary" title="股权结构" checked="true">'
            + '<input type="checkbox" id="kyc-shouyi" lay-skin="primary" title="受益所有人" checked="true">'
            + '<input type="checkbox" id="kyc-djg" lay-skin="primary" title="董监高人员" checked="true">'
            + '<input type="checkbox" id="kyc-gudong" lay-skin="primary" title="股东出资信息" checked="true">'
            + '<input type="checkbox" id="kyc-biangeng" lay-skin="primary" title="工商信息变更" checked="true">'
            + '<input type="checkbox" id="kyc-yichang" lay-skin="primary" title="经营异常信息" checked="true">'
            + '<input type="checkbox" id="kyc-weifa" lay-skin="primary" title="严重违法信息" checked="true">'
            + '<input type="checkbox" id="kyc-report" lay-skin="primary" title="工商年报" checked="true">'
            + '</form>'
            + '</div>';

        layer.open({
            title: '请选择打印信息',
            type: 1,
            id: 'layerPrint',
            area: '500px',
            content: html,
            btn: '确定',
            yes: function (index) {
                var jiben = $('#kyc-jiben').prop('checked');
                var guquan = $('#kyc-guquan').prop('checked');
                var shouyi = $('#kyc-shouyi').prop('checked');
                var djg = $('#kyc-djg').prop('checked');
                var gudong = $('#kyc-gudong').prop('checked');
                var biangeng = $('#kyc-biangeng').prop('checked');
                var yichang = $('#kyc-yichang').prop('checked');
                var weifa = $('#kyc-weifa').prop('checked');
                var report = $('#kyc-report').prop('checked');
                par = '&jiben=' + jiben + '&guquan=' + guquan + '&shouyi=' + shouyi
                    + '&djg=' + djg  + '&gudong=' + gudong + '&biangeng=' + biangeng
                    + '&yichang=' + yichang + '&weifa=' + weifa + '&report=' + report + '&querydate=' + querydate
                parent.tab.tabAdd({
                        title: '客户尽调--打印',
                        href: 'kyc/kycPrint.html?saicId=' + saiccId + par
                    }
                );
                layer.close(index);
            },
            success: function (layero, index) {
                var form = layui.form();
                form.render();
            }
        });
    });

    $('#btn_entrustUpdate').on('click', function(){
        var name = $('#name').html();
        entrustUpdate(name);
    });

    $('#addBeneficiaryBtn').click(function () {
        initBuluAddModal();
        $('#buluAddModal').modal('show');
    });

    $('#nonNaturalPersonBtn').click(function(){
        $('#nonNaturalPerson').modal('show');
    });

    $('#nonNaturalPersonBtn2').click(function(){
        $('#nonNaturalPerson2').modal('show');
    });


    function printfClick(common) {
        $.get('../../template/getSaicTemplateNameList?billType=ACCT_SAIC&depositorType=DEPOSITOR_TYPE_53', function (data) {
            if(data == '' || data == null) {
                layer.alert("对应打印模版未配置", function(index){
                    parent.tab.tabAdd({
                        href: 'kyc/list.html',
                        icon: 'fa fa-calendar-plus-o',
                        title: '客户尽调'
                    });
                });
            } else {
                //页面层
                index = layer.open({
                    title: '请选择打印模版',
                    type: 1,
                    //skin: 'layui-layer-rim', //加上边框
                    area: ['300px', '200px'], //宽高
                    content: '<select id="templateOption" style="width:250px;margin: 10px; " class="layui-input"><option value="" selected>请选择</option></select> <button style="margin-top:10px; margin-left: 100px;" class="layui-btn layui-btn-small" onclick="siacClick()" id="templateBtn"> 确定</button>'
                });

                for (var i = 0; i < data.length; i++) {
                    $('#templateOption').append("<option value=" + data[i] + ">" + data[i] + "</option>")
                }
            }
        });
    }
});

function siacClick() {
    if($('#templateOption').val() == '') {
        layer.alert("请选择对应模版类型");
        return;
    }

    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
    $.get('../account/print.html', null, function (form) {

        var url = '../../kyc/getPrintPreview?id=' + saicId + '&templateName=' + encodeURI($('#templateOption').val());
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

//判断数组中是否包含指定值(兼容ie8)
function arrContains(arr, str) {
    if (arr === undefined || str === undefined) {
        return false;
    }
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] === str) {
            return true;
        }
    }
    return false;
}