layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['loading', 'laytpl'], function () {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer,
        layer = layui.layer,
        loading = layui.loading,
        laytpl = layui.laytpl;
    
    placeholder($('.placeholder'));

    // 账号查人行信息
    $('#btnZh').on('click', function () {
        var accountId = $('#accountId').val();
        
        if (accountId === '') {
            layerTips.msg("请输入账号");
            return;
        }

        loading.show();
        $.get('../../customerSearch/getPbcInfoByAcctNo/?acctNo=' + accountId, function (response) {
            // var data = {
            //     acctNo:1122334455
            // }

            loading.hide();
            var msg = response.msg;
            var depositorName = '';
            if (response.rel) {
                var data = response.result;
                viewInfo(data, '#zhResult', 'pbcResult.html');
                if (data.depositorName != null) {
                    depositorName = data.depositorName;
                }
            } else {
                $('#zhResult').html('<div class="result-text">暂无数据</div>');
                layer.alert(msg,{
                    title:"人行提示",
                    closeBtn:0
                });
            }

            //记录日志
            $.post('../../customerSearch/queryPbcResultByAcctNoLog?acctNo=' +  encodeURI(accountId) + '&depositorName=' + encodeURI(depositorName) + '&result=' + encodeURI(msg));
        }).error(function (data) {
            loading.hide();
            layer.alert(data.responseJSON.message,{
                title:"人行提示",
                closeBtn:0
            });
            //记录日志
            $.post('../../customerSearch/queryPbcResultByAcctNoLog?acctNo=' +  encodeURI(accountId) + '&result=' + encodeURI(data.responseJSON.message));
        });

        return false;
    });

    // 开户许可证查人行信息
    $('#btnKh').on('click', function () {
        var accountKey = $('#accountKey').val();
        var regAreaCode = $('#regAreaCode').val();
        
        if (accountKey === '') {
            layerTips.msg("请输入基本户许可证号");
            return;
        }

        if (regAreaCode === '') {
            layerTips.msg("请输入基本户注册地地区代码");
            return;
        }

        loading.show();
        $.get('../../validate/pbc/?accountKey=' + accountKey + '&regAreaCode=' + regAreaCode, function (response) {
            // var data = {
            //
            // }
            loading.hide();

            var rel = response.rel;
            var msg = response.msg;
            var data = response.result;
            var result = response.msg;
            var acctNo = '';
            var depositorName = '';
            if(rel){
                if(data) {
                    viewInfo(data, '#khResult', 'pbcResult.html');
                    acctNo = data.acctNo;
                    depositorName = data.depositorName;
                    result = "操作成功";
                } else {
                    $('#khResult').html('<div class="result-text">暂无数据</div>');
                }
            } else{
                layer.alert(msg,{
                    title:"人行提示",
                    closeBtn:0
                });
            }

            //记录日志
            $.post('../../customerSearch/queryPbcResultByAccountKeyLog?acctNo=' + encodeURI(acctNo) + '&depositorName=' + encodeURI(depositorName)
                + '&result=' + encodeURI(result));
        });

        return false;
    });

    // 查询代码证系统信息
    $('#btnCx').on('click', function () {
        var postData = {
            orgEccsNo : $('#orgEccsNo').val(), // 机构信用代码
            accountKey : $('#accountKey2').val(), // 基本户开户许可证
            orgCode : $('#orgCode').val(), // 组织机构代码
            stateTaxRegNo : $('#stateTaxRegNo').val(), // 纳税人识别号（国税）
            regType : $('#regType').val(), // 工商注册类型
            regNo : $('#regNo').val(), // 工商注册号
            taxRegNo : $('#taxRegNo').val() // 纳税人识别号（地税）
        };

        var orgEccsNo = $('#orgEccsNo').val(); // 机构信用代码

        if (orgEccsNo === '') {
            layerTips.msg("请输入机构信用代码");
            return;
        }

        loading.show();
        $.post('../../customerSearch/getEccsAccountInfoByCondition', postData, function (response) {
            loading.hide();

            var rel = response.rel;
            var msg = response.msg;
            var data = response.result;
            var orgCode = "";// 组织机构代码
            var accountKey = "";// 开户许可证核准号
            var result = "";

            if(rel) {
                if (data) {
                    viewInfo(data, '#cxResult', 'cxResult.html');
                    orgCode = data.orgCode;
                    accountKey = data.accountKey;
                    result = '操作成功';
                } else {
                    $('#cxResult').html('<div class="result-text">暂无数据</div>');
                    result = '暂无数据';
                }
            } else {
                layer.alert(msg,{
                    title:"提示信息",
                    closeBtn:0
                });
                result = msg;
            }
            //记录日志
            // $.post('../../customerSearch/queryEccsResultLog?orgCode=' + encodeURI(orgCode) + '&accountKey=' + encodeURI(accountKey) + '&result=' + encodeURI(result));
            $.post('../../customerSearch/queryEccsResultLog?orgCode=' + encodeURI($('#orgCode').val()) + '&accountKey=' + encodeURI(accountKey) + '&result=' + encodeURI(result));
        }).error(function (data) {
            loading.hide();
            layer.alert(data.responseJSON.message,{
                title:"提示",
                closeBtn:0
            });
            //记录日志
            $.post('../../customerSearch/queryEccsResultLog?orgCode=' + encodeURI($('#orgCode').val()) + '&result=' + encodeURI(data.responseJSON.message));
        });

        return false;
    });

    // 工商基本信息查询
    $('#btnGs').on('click', function () {
        var companyName = $('#companyName').val(); // 企业名称
        var isBlack = $('#isBlack').prop('checked');
        
        if (companyName === '') {
            layerTips.msg("请输入企业名称");
            return;
        }

        loading.show();
        $.get('../../kyc/saic/basic?keyword=' + encodeURI(companyName), function (response) {

            // var data = {
            //     companyName: 'XX有限工商',
            //     blackMsg: '该企业属于正常状态，通过黑名单校验'
            // }
            loading.hide();

            var rel = response.rel;
            var msg = response.msg;
            var data = response.result;
            var refId = '';

            var ajaxData = {};
            if(rel) {
                if (data) {
                    data.registfundcurrency = getRegCurrencyTypeName(data.registfundcurrency);
                    viewInfo(data, '#gsResult', 'gsResult.html');

                    //弹窗展示异常信息
                    loadAnomalyInfo(data.id, isBlack, companyName);
                    // if (isBlack) layerTips.alert(data.blackMsg);
                    ajaxData.refId = data.id;
                    refId = data.id;

                    //委托更新业务处理
                    $('#entrustUpdateDiv').css('display', '');
                    getEntrustUpdateResult(data);

                } else {
                    $('#gsResult').html('<div class="result-text">暂无数据</div>');
                }
            } else{
                layer.alert(msg,{
                    title:"提示信息",
                    closeBtn:0
                });
            }
            ajaxData.customerName = companyName;
            ajaxData.result = msg;
            //记录日志
            $.post('../../customerSearch/querySaicInfoByNameLog?customerName=' + encodeURI(companyName) + '&result=' + encodeURI(msg === null ? "" : msg) + '&refId=' + refId);
            // $.ajax({
            //     type: "POST",
            //     url: '../../customerSearch/querySaicInfoByNameLog',
            //     data: ajaxData,
            //     dataType: "json"
            // });
        });

        return false;
    });

    // 运营商校验
    // 运营商校验 - 二级身份认证
    $('#btnYy1').on('click', function () {
        var realname = $('#yy1_realname').val(); // 企业名称
        var cellphone = $('#yy1_cellphone').val(); // 手机号

        if (realname === '') {
            layerTips.msg("请输入企业名称");
            return;
        }

        if (cellphone === '') {
            layerTips.msg("请输入手机号");
            return;
        }

        loading.show();
        $.get('../../carrier/getCarrierOperatorResult?name=' + encodeURI(realname) + '&mobile=' + cellphone, function (data) {
            // var data = {
            //     isblack: true
            // }

            loading.hide();

            if(data) {
                if(data.status === 'success') {
                    $('#yy1Result').html('<div class="result-text">查询结果：<i class="fa fa-street-view"></i> ' + data.result +'</div>');
                } else {
                    $('#yy1Result').html('<div class="result-text">查询结果：<i class="fa fa-user-times red"></i>' + data.reason + '</div>');
                }
            } else {
                $('#yy1Result').html('<div class="result-text">查询无结果</div>');
            }

        });

        return false;
    });

    // 运营商校验 - 三级身份认证
    $('#btnYy2').on('click', function () {
        var realname = $('#yy2_realname').val(); // 姓名
        var cellphone = $('#yy2_cellphone').val(); // 手机号
        var idCardNo = $('#yy2_idCardNo').val(); // 身份认证

        if (realname === '') {
            layerTips.msg("请输入姓名");
            return;
        }

        if (cellphone === '') {
            layerTips.msg("请输入手机号");
            return;
        }

        if (idCardNo === '') {
            layerTips.msg("请输入身份认证");
            return;
        }

        loading.show();
        $.get('../../carrier/getCarrierOperatorResult?name=' + encodeURI(realname) + '&mobile=' + cellphone + '&cardno=' + idCardNo, function (data) {

            // var data = {
            //     isblack: false
            // }

            loading.hide();
            var result = "";
            var idCardedType = $("#typeCarrierOperator input[name='idCardedType']:checked").val();
            idCardedType = idCardedType === undefined ? '' : idCardedType;
            if(data) {
                result = data.result;
                if(data.status == 'success') {
                    $('#yy2Result').html('<div class="result-text">查询结果：<i class="fa fa-street-view"></i>' + data.result + '</div>');
                } else {
                    $('#yy2Result').html('<div class="result-text">查询结果：<i class="fa fa-user-times red"></i>' + data.reason + '</div>');
                }
            } else {
                result = "查询无结果";
                $('#yy2Result').html('<div class="result-text">查询无结果</div>');
            }
            //记录日志
            $.post('../../customerSearch/queryCarrierOperatorResultLog' +
                '?name=' + encodeURI(realname) + '&mobile=' + encodeURI(cellphone) + '&cardNo=' + encodeURI(idCardNo)
                + '&result=' + encodeURI(result) + '&idCardedType=' + encodeURI(idCardedType));
        });

        return false;
    });
    //获取运营商校验页面中“核查人员类型”枚举
    if($('#typeTplCarrierOperator').length > 0) {
        $.get('../../dictionary/32119222946719/option', function (res) {
            var typeData = '';
            if (res.code === "ACK") {
                typeData = res.data;
            }
            var getTpl = $('#typeTplCarrierOperator').html();
            laytpl(getTpl).render(typeData, function (html) {
                $('#typeCarrierOperator').html(html);
            });
        });
    }

    // 联网身份核查
    $('#btnLw').on('click', function () {
        var fullName = $('#lw-realname').val();
        var idCardNo = $('#lw-idCardNo').val();

        if (fullName === '') {
            layerTips.msg("请输入姓名");
            return;
        }
        
        if (idCardNo === '') {
            layerTips.msg("请输入身份证号");
            return;
        }

        loading.show();

        $.post('../../idCard/sumit', {"idCardNo":idCardNo, "idCardName":fullName}, function(data){
            loading.hide();

            // var data = {
            //     idCardName:'aa', idCardNo:'11',checkStatus:'1',checkResult:'ok'
            // }

            var result = "";
            if(data) {
                result = data.checkResult;
                viewInfo(data, '#lwResult', 'lwResult.html')
            } else {
                $('#lwResult').html('<div class="result-text">暂无数据</div>');
            }
            $.get('../../customerSearch/queryIdCardLog?name=' + encodeURI(fullName) + '&cardNo=' + encodeURI(idCardNo) + '&result=' + encodeURI(result));

        }).error(function (e) {
            loading.hide();
            layerTips.msg(e.responseJSON.message);
            $.get('../../customerSearch/queryIdCardLog?name=' + encodeURI(fullName) + '&cardNo=' + encodeURI(idCardNo) + '&result=' + encodeURI(e.responseJSON.message));
        });
        return false;
    });

    // 本地黑名单查询
    $('#btnBd').on('click', function () {
        var companyName = $('#bd_companyName').val(); // 企业名称
        
        if (companyName === '') {
            layerTips.msg("请输入企业名称");
            return;
        }

        loading.show();
        // $.get('../ag/?accountId=' + accountId, function (data) {

            var data = {
                isblack: true
            };

            loading.hide();
            if(data) {
                if(data.isblack) {
                    $('#bdResult').html('<div class="result-text">是否黑名单：<i class="fa fa-user-times red"></i> 是</div>');
                } else {
                    $('#bdResult').html('<div class="result-text">是否黑名单：<i class="fa fa-street-view"></i> 否</div>');
                }
            } else {
                $('#bdResult').html('<div class="result-text">暂无数据</div>');
            }

        // });

        return false;
    });


    var loadAnomalyInfo = function (id, isBlack, companyName) {
        // $.get('../../validate/saic/local?keyword=' + encodeURI(name)).done(function (data) {
        $.get('../../validate/saic/local?saicInfoId=' + encodeURI(id)).done(function (data) {
            var msg = data.msg;
            var rel = data.rel;
            var result = data.result;
            if (!rel) {
                layer.alert(msg, {
                    title: "工商提示",
                    closeBtn: 0
                }, function (index) {
                    layer.close(index);

                    isBlank(isBlack, companyName)
                });

            } else {
                isBlank(isBlack, companyName)
            }
        }).fail(function (msg) {
            layer.alert("工商提示出错,请稍候重试", {
                title: "工商提示",
                closeBtn: 0
            });
        });
    };

    function isBlank(isBlack, companyName) {
        if (isBlack) {
            $.get('../../validate/black?keyword=' + encodeURI(companyName)).done(function (data) {
                var msg = data.msg;
                var rel = data.rel;
                if (rel) {
                    layer.alert(msg, {
                        title: "黑名单校验提示",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(index);
                    });
                } else {
                    layer.alert(msg, {
                        title: "黑名单校验提示",
                        closeBtn: 0
                    });
                }
            }).fail(function () {
                layer.alert("黑名单校验出错,请稍候重试", {
                    title: "黑名单校验提示",
                    closeBtn: 0
                });
            });
        }
    }
    
    function viewInfo(data, selector, templateHtml) {
        $.get(templateHtml, null, function (template) {
            if(template){
                laytpl(template).render(data,function (html) {
                    $(selector).html(html)
                });
            }
        });
    }

    //账号查人行信息日志查询
    $('#btn_log_0').on('click', function () {
        parent.tab.tabAdd({
            title: "账号查人行信息日志查询",
            href: 'search/pbcResultByAcctNoLog.html'
        });
    });

    //开户许可证查人行日志查询
    $('#btn_log_1').on('click', function () {
        parent.tab.tabAdd({
            title: "开户许可证查人行日志查询",
            href: 'search/pbcResultByAccountKeyLog.html'
        });
    });

    //查询代码证日志查询
    $('#btn_log_2').on('click', function () {
        parent.tab.tabAdd({
            title: "查询代码证日志查询",
            href: 'search/eccsResultSearchLog.html'
        });
    });

    //工商基本信息日志查询
    $('#btn_log_3').on('click', function () {
        parent.tab.tabAdd({
            title: "工商基本信息日志查询",
            href: 'search/saicInfoByNameLog.html'
        });
    });

    //运营商校验日志查询
    $('#btn_log_4').on('click', function () {
        parent.tab.tabAdd({
            title: "运营商校验日志查询",
            href: 'search/carrierOperatorSearchLog.html'
        });
    });

    //联网身份核查日志查询
    $('#btn_log_5').on('click', function () {
        parent.tab.tabAdd({
            title: "联网身份核查日志查询",
            href: 'search/idCardSearchLog.html'
        });
    });

    $('#btn_entrustUpdate').on('click', function(){
        var companyName = $('#companyName').val()
        entrustUpdate(companyName);
    });

});
