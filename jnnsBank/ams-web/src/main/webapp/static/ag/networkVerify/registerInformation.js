var registerInformation = {
    baseUrl: "../../networkVerify/registerInformation"
};

layui.config({
    base: '../js/'
});

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

        },
        //初始化元素
        initElem: function (fn) {

        },
        //初始化验证
        initValid: function () {
            var that = this;
            var validLabel = ['entNm','uniSocCdtCd1','nmOfLglPrsn','idOfLglPrsn','nm', 'managerId','uniSocCdtCd2','opNm'];
            addValidateFlag(validLabel);
            var rules = {
                entNm: {//企业名称
                    required: true,
                    maxlength: 100
                },
                uniSocCdtCd1: {//统一社会信用代码1
                    required: true,
                    maxlength: 18
                },
                nmOfLglPrsn: {//法定代表人或单位负责人姓名
                    required: true,
                    maxlength: 200
                },
                idOfLglPrsn: {//法定代表人或单位负责人身份证件号
                    required: true,
                    maxlength: 35
                },
                opNm: {//操作员姓名
                    required: true,
                    maxlength: 140
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

        },
        //绑定事件
        bindEvent: function () {
            var that = this;
            //确定按钮
            $('#btnSubmit').click(function(){
                $('#editForm').submit();
            });

            var entRules = {
                entNm: {//企业名称
                    required: true,
                    maxlength: 100
                },
                uniSocCdtCd1: {//统一社会信用代码1
                    required: true,
                    maxlength: 18
                },
                nmOfLglPrsn: {//法定代表人或单位负责人姓名
                    required: true,
                    maxlength: 200
                },
                idOfLglPrsn: {//法定代表人或单位负责人身份证件号
                    required: true,
                    maxlength: 35
                }
            };

            var slfEplydPplRules = {
                uniSocCdtCd2: {//统一社会信用代码2
                    required: true,
                    maxlength: 18
                },
                nm: {//经营者姓名
                    required: true,
                    maxlength: 200
                },
                managerId: {//经营者证件号
                    required: true,
                    maxlength: 35
                }
            };

            //市场主体类型选择
            form.on('select(myType)',function (data) {
                if ($("#myType").val()==0){
                    $("#ent").show();
                    $("#slfEplydPpl").hide();
                    removeRules(slfEplydPplRules);//清除验证
                    addRules(entRules);//增加验证
                }else if ($("#myType").val()==1) {
                    $("#ent").hide();
                    $("#slfEplydPpl").show();
                    removeRules(entRules);//清除验证
                    addRules(slfEplydPplRules);//增加验证
                }
            });

            //重置按钮
            $('button[type="reset"]').click(function () {
                $("#ent").show();
                $("#slfEplydPpl").hide();
                removeRules(slfEplydPplRules);//清除验证
                addRules(entRules);//增加验证
            });

            //select选择后去焦点，进行校验
            form.on('select', function (data) {
                $(data.elem).blur();
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

            var entNm = $('#entNm').val();
            var uniSocCdtCd1 = $('#uniSocCdtCd1').val();
            var nmOfLglPrsn = $('#nmOfLglPrsn').val();
            var idOfLglPrsn = $('#idOfLglPrsn').val();
            var traNm = $('#traNm').val();
            var uniSocCdtCd2 = $('#uniSocCdtCd2').val();
            var nm = $('#nm').val();
            var managerId = $('#managerId').val();
            var agtNm = $('#agtNm').val();
            var agtId = $('#agtId').val();
            var opNm = $('#opNm').val();
            var postData;

            if ($("#myType").val()==0) {
                postData = {
                    entNm: entNm,
                    uniSocCdtCd: uniSocCdtCd1,
                    nmOfLglPrsn: nmOfLglPrsn,
                    idOfLglPrsn: idOfLglPrsn,
                    agtNm: agtNm,
                    agtId: agtId,
                    opNm:opNm
                };
            }else if ($("#myType").val()==1) {
                postData = {
                    nm: nm,
                    traNm: traNm,
                    uniSocCdtCd: uniSocCdtCd2,
                    managerId: managerId,
                    agtNm: agtNm,
                    agtId: agtId,
                    opNm:opNm
                };
            }

            $.ajax({
                url: registerInformation.baseUrl+'/verify',
                type: 'post',
                data: postData,
                dataType: "json",
                success: function (res) {

                    if (res.code === 'ACK') {
                        var data = res.data;
                        if (data.rslt!=null) {
                            splicingHTML(data);//拼接查询结果html
                            $('#success').show();
                            $('#fail').hide();
                            $('#result').show();
                        }else if (data.procSts!=null){//申请报文被拒绝
                            $('#procSts').text(procStsSwitch(data.procSts));
                            $('#procCd').text(data.procCd);
                            $('#rjctInf').text(data.rjctInf);

                            $('#fail').show();
                            $('#success').hide();
                            $('#result').show();
                        }else if(data.msgCode!=null){
                            layerTips.msg('报文已提交，等待返回结果');
                            $('#result').hide();
                        } else {
                            layerTips.msg('异常：MIVS无法解析请求报文');
                            $('#result').hide();
                        }
                    } else {
                        $('#result').hide();
                        layerTips.alert(res.message, {
                            title: "提示",
                            closeBtn: 0
                        }, function (index) {
                            layerTips.close(index);
                        });

                    }

                    //button释放
                    layer.close(index);
                    $('#btnSubmit').removeClass("layui-btn-disabled");
                    $('#btnSubmit').attr("disabled",false);

                },
                error: function () {
                    //button释放
                    layer.close(index);
                    $('#btnSubmit').removeClass("layui-btn-disabled");
                    $('#btnSubmit').attr("disabled",false);
                }

            });

        }

    });

    new unique().init({});

    //增加规则
    function addRules(rulesObj){
        for (var item in rulesObj){
            $('#'+item).rules('add',rulesObj[item]);
        }
    }

    //去除规则
    function removeRules(rulesObj){
        for (var item in rulesObj){
            $('#'+item).rules('remove');
        }
    }

    //拼接查询结果html
    function splicingHTML(data) {
        var html = '<tr>\n' +
            '<td class="td-label">登记信息核查结果：</td>\n' +
            '<td class="td-label-right" id="rslt">'+registerRsltSwitch(data.rslt)+'</td>\n' +
            '<td class="td-label">数据源日期：</td>\n' +
            '<td class="td-label-right"id="dataResrcDt">'+data.dataResrcDt+'</td>\n' +
            '</tr>';
        if (data.rslt=="MCHD"){

            if (data.basicInformationOfEnterpriseDto!=null){//企业照面
                var dto = data.basicInformationOfEnterpriseDto;
                html += '<tr><td class="td-label" align="center" colspan="4">企业照面信息部分</td></tr>';
                html += '<tr>\n' +
                    '<td class="td-label">企业名称：</td>\n' +
                    '<td class="td-label-right">'+(dto.entNm || '')+'</td>\n' +
                    '<td class="td-label">统一社会信用代码：</td>\n' +
                    '<td class="td-label-right">'+(dto.uniSocCdtCd || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">市场主体类型：</td>\n' +
                    '<td class="td-label-right">'+(dto.coTp || '')+'</td>\n' +
                    '<td class="td-label">住所：</td>\n' +
                    '<td class="td-label-right">'+(dto.dom || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">注册资本(金)：</td>\n' +
                    '<td class="td-label-right">'+(dto.regCptl || '')+'</td>\n' +
                    '<td class="td-label">成立日期：</td>\n' +
                    '<td class="td-label-right">'+(dto.dtEst || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">登记状态：</td>\n' +
                    '<td class="td-label-right">'+(dto.regSts||'')+'</td>\n' +
                    '<td class="td-label">法定代表人或单位负责人姓名：</td>\n' +
                    '<td class="td-label-right">'+(dto.nmOfLglPrsn || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">登记机关：</td>\n' +
                    '<td class="td-label-right">'+(dto.regAuth || '')+'</td>\n' +
                    '<td class="td-label">经营范围：</td>\n' +
                    '<td class="td-label-right">'+(dto.bizScp || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">核准日期：</td>\n' +
                    '<td class="td-label-right">'+(dto.dtAppr || '')+'</td>\n' +
                    '</tr>\n';


            } else if (data.basicInformationOfSelfEmployedPeopleDto!=null){//个体户照面
                var dto = data.basicInformationOfSelfEmployedPeopleDto;
                html += '<tr><td class="td-label" align="center" colspan="4">个体户照面信息部分</td></tr>';
                html += '<tr>\n' +
                    '<td class="td-label">字号名称：</td>\n' +
                    '<td class="td-label-right">'+(dto.traNm || '')+'</td>\n' +
                    '<td class="td-label">统一社会信用代码：</td>\n' +
                    '<td class="td-label-right">'+(dto.uniSocCdtCd || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">市场主体类型：</td>\n' +
                    '<td class="td-label-right">'+(dto.coTp || '')+'</td>\n' +
                    '<td class="td-label">经营场所：</td>\n' +
                    '<td class="td-label-right">'+(dto.opLoc || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">资金数额：</td>\n' +
                    '<td class="td-label-right">'+(dto.fdAmt || '')+'</td>\n' +
                    '<td class="td-label">成立日期：</td>\n' +
                    '<td class="td-label-right">'+(dto.dtReg || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">登记状态：</td>\n' +
                    '<td class="td-label-right">'+(dto.regSts||'')+'</td>\n' +
                    '<td class="td-label">经营者姓名：</td>\n' +
                    '<td class="td-label-right">'+(dto.nm || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">登记机关：</td>\n' +
                    '<td class="td-label-right">'+(dto.regAuth || '')+'</td>\n' +
                    '<td class="td-label">经营范围：</td>\n' +
                    '<td class="td-label-right">'+(dto.bizScp || '')+'</td>\n' +
                    '</tr>\n';
                html += '<tr>\n' +
                    '<td class="td-label">核准日期：</td>\n' +
                    '<td class="td-label-right">'+(dto.dtAppr || '')+'</td>\n' +
                    '</tr>\n';
            }

            if (data.companyShareholdersAndFundingInformationDtoList!=null){//企业股东及出资信息部分，属于企业登记信息核查内容。
                var dtoList = data.companyShareholdersAndFundingInformationDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">企业股东及出资信息部分</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">自然人标识:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].natlPrsnFlag || '')+'</td>\n' +
                        '<td class="td-label">投资人名称:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].invtrNm || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">投资人证件号码或证件编号:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].invtrId || '')+'</td>\n' +
                        '<td class="td-label">认缴出资额['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConAmt || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">实缴出资额:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].actlCptlConAmt || '')+'</td>\n' +
                        '<td class="td-label">认缴出资方式['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConFm || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">认缴出资日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConDt || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }

            if (data.directorSupervisorSeniorManagerInformationDtoList!=null){//董事监事及高管信息，属于企业登记信息核查内容。
                var dtoList = data.directorSupervisorSeniorManagerInformationDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">董事监事及高管信息部分</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">姓名:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].nm || '')+'</td>\n' +
                        '<td class="td-label">职务:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].posn || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }

            if (data.changeInformationDtoList!=null){//历史变更信息，属于企业/个体户登记信息核查内容。
                var dtoList = data.changeInformationDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">历史变更信息</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">变更事项:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].chngItm || '')+'</td>\n' +
                        '<td class="td-label">变更日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].dtOfChng || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">变更前内容:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].bfChng || '')+'</td>\n' +
                        '<td class="td-label">变更后内容['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].aftChng || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }

            if (data.abnormalBusinessInformationDtoList!=null){//异常经营信息，属于企业登记信息核查内容。
                var dtoList = data.abnormalBusinessInformationDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">异常经营信息</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">列入经营异常名录原因类型:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].abnmlCause || '')+'</td>\n' +
                        '<td class="td-label">列入日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].abnmlDate || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">列入决定机关:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].abnmlCauseDcsnAuth || '')+'</td>\n' +
                        '<td class="td-label">移出经营异常名录原因['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvCause || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">移出日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvDate || '')+'</td>\n' +
                        '<td class="td-label">移出决定机关['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvCauseDcsnAuth || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }

            if (data.illegalAndDiscreditInformationDtoList!=null){//严重违法失信信息，属于企业登记信息核查内容。
                var dtoList = data.illegalAndDiscreditInformationDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">严重违法失信信息</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">列入事由或情形:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].illDscrtCause || '')+'</td>\n' +
                        '<td class="td-label">列入日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].abnmlDate || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">列入决定机关:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].abnmlCauseDcsnAuth || '')+'</td>\n' +
                        '<td class="td-label">移出事由['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvCause || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">移出日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvDate || '')+'</td>\n' +
                        '<td class="td-label">移出决定机关['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rmvCauseDcsnAuth || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }

            if (data.licenseNullifyDtoList!=null){//营业执照作废声明，属于企业登记信息核查内容。
                var dtoList = data.licenseNullifyDtoList;
                html += '<tr><td class="td-label" align="center" colspan="4">营业执照作废声明</td></tr>';
                for (var i = 1;i<=dtoList.length;i++){
                    html += '<tr>\n' +
                        '<td class="td-label">正副本标识:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(registerOrgnlOrCpSwitch(dtoList[i-1].orgnlOrCp) || '')+'</td>\n' +
                        '<td class="td-label">声明内容:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].licNullStmCntt || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">声明日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].licNullStmDt || '')+'</td>\n' +
                        '<td class="td-label">补领标识['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(registerRplStsSwitch(dtoList[i-1].rplSts) || '')+'</td>\n' +
                        '</tr>\n' ;
                    html += '<tr>\n' +
                        '<td class="td-label">补领日期:['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].rplDt || '')+'</td>\n' +
                        '<td class="td-label">营业执照副本编号['+i+']：</td>\n' +
                        '<td class="td-label-right">'+(dtoList[i-1].licCpNb || '')+'</td>\n' +
                        '</tr>\n' ;
                }
            }
        }

        $('#vrfctnInf').html(html);
    }
});
