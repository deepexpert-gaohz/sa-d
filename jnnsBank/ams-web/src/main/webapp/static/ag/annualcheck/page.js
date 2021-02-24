/**
 *  Created by alven on 11/02/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var annual={};

var taskId;

layui.use(['common','loading','validator', 'uploadV2','form', 'laydate','laytpl'], function () {
    var $ = layui.jquery,
        common = layui.common,
        loading = layui.loading,
        validator = layui.validator,
        upload = layui.uploadV2,
        laydate = layui.laydate;
        laytpl = layui.laytpl;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var isResetBtn;

    //TODO 需要加载任务状态 判断数据采集情况
    loading.show();
    getStatus();
    setInterval(getStatus,10000);
    function getStatus(){
        //当前页为年检任务页才去后台查询
        if (parent.tab.getCurrentTabId().indexOf('/annualcheck/page.html') == -1){
            return;
        }
        $.get('../../annualTask/datastatus', function (data) {
            var result = data.result;
            taskId = result.taskId;
            //核心数据
            var core = result.core;
            if(core){
                var elementTotalNumber = $('#core-data .totalNumber');
                var elementSuccessedNumber = $('#core-data .successedNumber');
                var elementFailedNumber = $('#core-data .failedNumber');
                $('#core-data').data('currentStatus',core.currentStatus);
                $(elementTotalNumber).html("");
                $(elementSuccessedNumber).html("");
                $(elementFailedNumber).html("");
                if(core.currentStatus === '0'){
                    //正在导入
                    $('#core-data .desc').html('正在导入');
                    $(elementTotalNumber).html("采集总数:"+core.dataTotal);
                    $(elementSuccessedNumber).html("成功数量:"+core.dataComplete);
                    $(elementFailedNumber).html("失败数量:"+core.dataFail);
                    $('#core-data').removeClass('no-active');
                } else if(core.currentStatus === '1'){
                    //导入完成
                    $('#core-data .desc').html('导入完成');
                    $(elementTotalNumber).html("采集总数:"+core.dataTotal);
                    $(elementSuccessedNumber).html("成功数量:"+core.dataComplete);
                    $(elementFailedNumber).html("失败数量:"+core.dataFail);
                    $('#core-data').data('isComplete',true);
                    $('#core-data').removeClass('no-active');
                    $("#resetCoreBtn").removeClass("dn");
                }else if(core.currentStatus === '-2'){
                    $('#core-data .desc').html('未导入');
                    $('#core-data').addClass('no-active');
                }else if(core.currentStatus === '-1'){
                    $('#core-data .desc').html('初始化采集');
                    $('#core-data').addClass('no-active');
                }else if(core.currentStatus === '5'){
                    $('#core-data .desc').html('采集失败');
                    $('#core-data').addClass('no-active');
                    $(elementTotalNumber).html(core.error);
                }
            }

            //人行数据
            handlerStatusConfigPbc(result.bank,'bank-data');

            //工商数据
            handlerStatusConfigSaic(result.saic,'saic-data');

            $.ajax({
                url: '../../permission/element/code?code=annual:reset',
                type: 'get',
                dataType: "json",
                async: false,
                success: function (res) {
                    if (res == true) {   //年检提交按钮权限显示
                        isResetBtn = true;
                    } else {   ////年检提交按钮权限隐藏
                        isResetBtn = false;
                    }
                }
            });
            //处理其他
            var taskStatus = result.status;
            if(taskStatus === 2 || taskStatus === 3 || taskStatus === 4 || taskStatus === 5) {
                $('#task-action').addClass('dn');
                $('#task-status').removeClass('dn');
                if(isResetBtn === true) {  //重置按钮根据权限配置
                    $('.resetBtn').removeClass('dn');
                }
                $('#task-data-total').html(result.dataTotal);
                $('#task-data-complete').html(result.dataComplete);

                if (taskStatus === 2) {
                    $('#task-status-display').html('正在比对');
                } else if (taskStatus === 3 || taskStatus === 5) {
                    if (taskStatus === 3) {
                        $('#task-status-display').html('比对完成');
                    } else if (taskStatus === 5) {
                        $('#task-status-display').html('再次年检比对完成');
                    }
                    $('#resultBtn').removeClass('dn');
                    $('#reCompareBtn').removeClass('dn');
                    if(isResetBtn === true) {  //重置按钮根据权限配置
                        $('.resetBtn').removeClass('dn');
                    }
                } else if (taskStatus === 4) {
                    $('#task-status-display').html('正在再次年检比对');
                }
            }

            loading.hide();
        });
    }

    //人行数据
    function handlerStatusConfigPbc(data,elementName){
        if(data){
            var elementDesc = $('#'+elementName+' .desc');
            var elementProcess = $('#'+elementName+' .process');
            var element = $('#'+elementName);
            var elementTotalNumber = $('#'+elementName+' .totalNumber');
            var elementSuccessedNumber = $('#'+elementName+' .successedNumber');
            var elementNoNeedNumber = $('#'+elementName+' .noNeedNumber');
            var elementFailedNumber = $('#'+elementName+' .failedNumber');
            var elementErrorPbc = $('#'+elementName+' .errorPbc');
            element.data('currentStatus',data.currentStatus);
            //处理配置
            if(data.isNeedConfig){
                element.data('isNeedConfig',true);
            }
            if(data.currentStatus === '0'){
                //正在采集
                elementDesc.html('正在采集');
                // elementProcess.html('('+data.dataComplete + '/' + data.dataTotal+')');
                element.removeClass('no-active');

                elementTotalNumber.html("采集总数:"+data.dataTotal);
                elementSuccessedNumber.html("成功数量:"+data.dataComplete);
                elementNoNeedNumber.html("无需采集:"+data.dataNoNeed);
                elementFailedNumber.html("失败数量:"+data.dataFail);

                if(data.error){
                    elementErrorPbc.html(data.error);
                }
                element.find('.action>.pause').removeClass('dn');
            } else if(data.currentStatus === '1'){
                //采集完成
                elementDesc.html('采集成功');
                elementTotalNumber.html("采集总数:"+data.dataTotal);
                elementSuccessedNumber.html("成功数量:"+data.dataComplete);
                elementNoNeedNumber.html("无需采集:"+data.dataNoNeed);
                elementFailedNumber.html("失败数量:"+data.dataFail);
                /*if(data.error){
                    elementErrorPbc.html(data.error);
                }*/
                element.data('isComplete',true);
                element.removeClass('no-active');
                $("#resetPbcBtn").removeClass("dn");
            } else if(data.currentStatus === '2'){
                //采集暂停
                elementDesc.html('采集暂停');
                element.removeClass('no-active');
                element.find('.action>.play').removeClass('dn');
            } else if(data.currentStatus === "-1"){
                elementDesc.html('未采集');
                element.addClass('no-active');
            } else if(data.currentStatus === "5"){
                elementDesc.html('采集失败');
                element.addClass('no-active');
            } else if(data.currentStatus === "6"){
                elementDesc.html('人行服务已经关闭，等待开启中');
                element.addClass('no-active');
                //采集完成
                elementTotalNumber.html("采集总数:"+data.dataTotal);
                elementSuccessedNumber.html("成功数量:"+data.dataComplete);
                elementNoNeedNumber.html("无需采集:"+data.dataNoNeed);
                elementFailedNumber.html("失败数量:"+data.dataFail);
            }
            if(data.error){
                elementErrorPbc.html("<span style='cursor: pointer' id='checkErrorPbc'>"+data.error+",&nbsp;点击查看详情</span>");
                $("#checkErrorPbc").click(function(event){
                    parent.tab.tabAdd({
                            title: '年检-人行数据',
                            href: 'annualcheck/errorMessage.html'
                        }
                    );
                    event.stopPropagation();
                });
            }
        }
    }

    //工商数据
    function handlerStatusConfigSaic(data,elementName){
        if(data){
            var elementDesc = $('#'+elementName+' .desc');
            var elementProcess = $('#'+elementName+' .process');
            var element = $('#'+elementName);
            var elementTotalNumber = $('#'+elementName+' .totalNumber');
            var elementSuccessedNumber = $('#'+elementName+' .successedNumber');
            var elementFailedNumber = $('#'+elementName+' .failedNumber');
            element.data('currentStatus',data.currentStatus);
            //处理配置
            if(data.isNeedConfig){
                element.data('isNeedConfig',true);
            }
            if(data.currentStatus === '0'){
                //正在采集
                elementDesc.html('正在采集');
                // elementProcess.html('('+data.dataComplete + '/' + data.dataTotal+')');
                element.removeClass('no-active');

                elementTotalNumber.html("采集总数:"+data.dataTotal);
                elementSuccessedNumber.html("成功数量:"+data.dataComplete);
                elementFailedNumber.html("失败数量:"+data.dataFail);
                element.find('.action>.pause').removeClass('dn');
            } else if(data.currentStatus === '1'){
                //采集完成
                elementDesc.html('采集成功');
                elementTotalNumber.html("采集总数:"+data.dataTotal);
                elementSuccessedNumber.html("成功数量:"+data.dataComplete);
                elementFailedNumber.html("失败数量:"+data.dataFail);
                element.data('isComplete',true);
                element.removeClass('no-active');
                $("#resetSaicBtn").removeClass("dn");
            } else if(data.currentStatus === '2'){
                //采集暂停
                elementDesc.html('采集暂停');
                element.removeClass('no-active');
                element.find('.action>.play').removeClass('dn');
            } else if(data.currentStatus === "-2"){
                elementDesc.html('未采集');
                element.addClass('no-active');
            }  else if(data.currentStatus === "-1"){
                elementDesc.html('初始化采集');
                element.addClass('no-active');
            } else if(data.currentStatus === "3"){
                elementDesc.html('可以导入采集文件');
                element.addClass('no-active');
                // $("#resetSaicBtn").removeClass("dn");
            } else if(data.currentStatus === "7"){
                elementDesc.html('正在执行定时采集操作');
                element.addClass('no-active');
            } else if(data.currentStatus === "8"){
                elementDesc.html('定时采集任务正在等待执行');
                element.addClass('no-active');
            } else if(data.currentStatus === "4"){
                elementDesc.html('可以启动在线采集任务');
                element.addClass('no-active');
            }
        }
    }

    //核心数据导入
    $('#core-data').on('click',function () {
        // TODO 校验是否已经导入过
        var index;

        //判断当前状态
        var currentStatus = $(this).data('currentStatus');
        if(currentStatus === '-2' || currentStatus==='5') {
            //页面层
            index = layer.open({
                title: '选择类型',
                type: 1,
                // skin: 'layui-layer-rim', //加上边框
                area: ['300px', '150px'], //宽高
                content: '<div id="uploadimg" class="row" style="margin-left: 10px;margin-top: 20px;"><div id="fileList" class="uploader-list"></div><div id="coreFileUpload" class="col-md-5">导入采集</div><div class="col-md-4"><button class="btn" style="background-color: #00b7ee;color: white;height: 39px;" id="coreLocalCollection">本地在线采集</button></div></div>' +
                    '<div class="row" style="margin-left: 10px;margin-top: 10px;"><a id="core_download" href="javascript:;" style="margin-left: 10px;cursor:pointer;color: blue">下载年检核心模板文件</a></div>'
            });


            // 导入文件
            var uploaderCore = WebUploader.create({
                auto: true, // 选完文件后，是否自动上传
                swf: '../js/webuploader/Uploader.swf', // swf文件路径
                server: '../../annualTask/upload', // 文件接收服务端
                pick: '#coreFileUpload', // 选择文件的按钮。可选
                /*fileNumLimit: 1,//一次最多上传五张
                // 只允许选择图片文件。*/
                accept: {
                    title: 'Excel',
                    extensions: 'xls,xlsx',
                    mimeTypes: 'application/vnd.ms-excel'

                }
            });
            uploaderCore.on( 'uploadSuccess', function( file, res ) {
                layerTips.msg(res.message);
                layer.close(index);
            });
            uploaderCore.on( 'uploadError', function( file, reason ) {
                layerTips.msg('上传失败');
            });
            uploaderCore.on( 'error', function( handler ) {
                if(handler == 'Q_TYPE_DENIED' ){
                    layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
                }else{
                    layerTips.msg(handler);
                }
            });

            $("#coreLocalCollection").click(function(){
                layer.confirm('确定本地在线采集吗？', null, function (indexInner) {
                    startDataCollection('../../annualTask/core/start', 'core-data');
                    layer.close(indexInner);
                    layer.close(index);
                });
            });

            $('#core_download').on('click', function () {
                $('#downloadFrame').prop('src', '../../annualTask/core/download');
            });

        } else if(currentStatus === '0' || currentStatus === '1'){
            parent.tab.tabAdd({
                    title: '年检-核心数据',
                    href: 'annualcheck/core.html'
                }
            );
        }

        return false;
    });

    //人行数据
    $('#bank-data').on('click',function () {
        // if($('#core-data').data('isComplete') === false){
        //     layerTips.msg('请先导入核心数据');
        //     return;
        // }

        //判断当前状态
        var currentStatus = $(this).data('currentStatus');
        if(currentStatus === '-1' || currentStatus==='5'){
            //未启动
            //判断是否配置
            if($(this).data('isNeedConfig')){
                //未配置情况 出现人行采集配置界面
                dataConfig(function () {
                    //启动人行采集
                    startBankDataCollection();
                });
            } else {
                //启动人行采集
                startBankDataCollection();
            }
        } else{
            parent.tab.tabAdd({
                    title: '年检-人行数据',
                    href: 'annualcheck/bank.html'
                }
            );
        }
    });

    // //人行数据采集操作
    // $('#bank-data').on('click','.action>i',function () {
    //     dataProcessHandler($(this),'bank');
    //     return false;
    // });
    //
    // function dataProcessHandler(element,type) {
    //     if(element.hasClass('pause')){
    //         //点击暂停
    //         // todo 调用后台
    //         element.addClass('dn');
    //         element.next().removeClass('dn');
    //
    //         //修改描述
    //         element.parents('.dashboard-stat').find('.desc').html('采集暂停');
    //         element.parents('.dashboard-stat').find('.process').html('');
    //     } else if(element.hasClass('play')){
    //         //点击采集
    //         element.addClass('dn');
    //         element.prev().removeClass('dn');
    //         //修改描述
    //         element.parents('.dashboard-stat').find('.desc').html('正在采集');
    //         // element.parents('.dashboard-stat').find('.process').html('111');
    //     }
    // }

    /**
     * 开始采集人行数据
     */
    function startBankDataCollection(){
        var elementTotalNumber = $('#bank-data .totalNumber');
        var elementSuccessedNumber = $('#bank-data .successedNumber');
        var elementNoNeedNumber = $('#bank-data .noNeedNumber');
        var elementFailedNumber = $('#bank-data .failedNumber');
        var elementErrorPbc = $('#bank-data .errorPbc');
        $(elementTotalNumber).html("");
        $(elementSuccessedNumber).html("");
        $(elementNoNeedNumber).html("");
        $(elementFailedNumber).html("");
        $(elementErrorPbc).html("");
        //查询配置文件是否需要采集人行前的人行账号校验动作
        $.get('../../config/findByKey?configKey=annualAmsAccountCheck', function (data) {
            //是否需要校验人行账号
            if(data){
                layerTips.msg('正在校验年检人行账号登录状态');
                $.get('../../annualTask/annualCheckAmsPassword', function (data) {
                    var res = data.data;
                    if(res.length > 0){
                        layer.confirm('机构【' + res + '】2级人行账号登录异常，请再次确认或继续采集！', {
                            btn: ['继续采集','取消'] //按钮
                        }, function(index){
                            layerTips.msg('数据正在采集');
                            startDataCollection('../../annualTask/pbc/start', 'bank-data');
                            layer.close(index);
                        }, function(){
                            //取消
                        });
                    }else{
                        layerTips.msg('数据正在采集');
                        startDataCollection('../../annualTask/pbc/start', 'bank-data');
                    }
                });
            }else{
                layerTips.msg('数据正在采集');
                startDataCollection('../../annualTask/pbc/start', 'bank-data');
            }
        });
    }

    /**
     * 查询工商的导入文件数量
     */
    function getSaicFileCounts(){
        $.get('../../annualTask/saic/fileCounts', function (data) {
            $("#saicFileCountsId").html(data.result);
        });
    }

    //工商数据
    $('#saic-data').on('click',function () {
        // TODO 校验是否已经采集或正在采集
        // if($('#bank-data').data('isComplete') === false){
        //     layerTips.msg('请先完成人行数据采集');
        //     return;
        // }

        //判断当前状态
        var currentStatus = $(this).data('currentStatus');
        if(currentStatus === '-2'){
            //启动人行采集
            startSaicDataCollection();
        }else if(currentStatus === '3'){//文件导入
            //页面层
            index = layer.open({
                title: '选择类型',
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['300px', '230px'], //宽高
                content: '<div style="margin-bottom:20px;"><a target="_blank" href="../../annualTask/saic/download" style="margin-left: 10px;cursor:pointer;color: blue">下载年检企业列表文件</a></div><div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">请选择文件</div><div><span>已存在的文件：</span><span id="saicFileCountsId">0</span></div></div>' +
                    '<div class="row" style="margin-left: 1px;margin-top: 20px;"><button id="saicStartId" class="btn" style="background-color: #00b7ee;color: white">开始采集</button><button id="clearSaicFilesId"  class="btn" style="margin-left: 5px;">清空文件</button></div>'
            });

            getSaicFileCounts();


            $("#saicStartId").click(function(){
                $.get('../../annualTask/saic/startFile', function (data) {
                    layerTips.msg(data.msg);
                    if(data.rel){
                        getStatus();
                        layer.close(index);
                    }
                });
            });

            $("#clearSaicFilesId").click(function(){
                $.get('../../annualTask/saic/clearFiles', function (data) {
                    layerTips.msg(data.msg);
                    getSaicFileCounts();
                });
            });

            var uploader = WebUploader.create({
                auto: true, // 选完文件后，是否自动上传
                swf: '../js/webuploader/Uploader.swf', // swf文件路径
                server: '../../annualTask/saic/upload', // 文件接收服务端
                pick: '#imgPicker', // 选择文件的按钮。可选
                /*fileNumLimit: 5,//一次最多上传五张
                // 只允许选择图片文件。
                accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/!*'
                }*/
                accept: {
                    title: 'Json',
                    extensions: 'json'
                }
            });
            uploader.on( 'uploadSuccess', function( file, res ) {
                getSaicFileCounts();
                layerTips.msg(res.message);
            });
            uploader.on( 'uploadError', function( file, reason ) {
                layerTips.msg('上传失败');
            });

            uploader.on( 'error', function( handler ) {
                if(handler == 'Q_TYPE_DENIED' ){
                    layerTips.msg('文件类型不对，请上传json后缀名的文件');
                }else{
                    layerTips.msg(handler);
                }
            });
        }else if(currentStatus ==='4'){//在线下载
            startSaicDataCollection();
        } else {
            parent.tab.tabAdd({
                    title: '年检-工商数据',
                    href: 'annualcheck/saic.html'
                }
            );
        }
    });

    // //工商数据采集操作
    // $('#saic-data').on('click','.action>i',function () {
    //     dataProcessHandler($(this),'saic');
    //     return false;
    // });

    /**
     * 开始采集工商数据
     */
    function startSaicDataCollection(){
        startDataCollection('../../annualTask/saic/start','saic-data');
    }

    function startDataCollection(url, elementId) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: "json",
            success: function (result) {
                if (result.rel) {
                    var element = $('#' + elementId);
                    layerTips.msg('数据采集启动成功');
                    element.find('.desc').html('初始化采集');
                } else {
                    var element = $('#' + elementId);
                    element.find('.desc').html('未采集');
                    layerTips.msg(result.msg);
                }

            },
            error: function (err) {
                layerTips.msg('数据采集启动失败');
            },
            beforeSend: function (data) {
                var element = $('#' + elementId);
                element.find('.desc').html('正在采集');
            }

        });
    }

    //采集配置按钮
    $('#dataConfigBtn').on('click',function () {
        dataConfig();
    });

    var addBoxIndex = -1;
    function dataConfig(callback) {
        if (addBoxIndex !== -1)
            return;
        $.get('../../annualTask/dataconfig', null, function (data) {
            var result = data.result;
            $.get('datasettings.html?v=1', null, function (form) {
                addBoxIndex = layer.open({
                    type: 1,
                    title: '采集配置',
                    content: form,
                    btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['800px', '600px'],
                    maxmin: true,
                    yes: function (index) {
                        //触发表单的提交事件
                        $('form.layui-form').find('button[lay-filter=edit]').click();
                    },
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
                        var form = layui.form();
                        //初始化日期控件
                        $('.date-picker').change(function(){
                            var value = $(this).val();
                            $(this).val(dateFormat(value));
                        });
                        common.handleBootstrapDatetimepicker();
                        $('#bankStartDatetime').timepicker({
                            showMeridian:false,
                            defaultTime:false
                        });
                        $('#bankEndDatetime').timepicker({
                            showMeridian:false,
                            defaultTime:false
                        });
                        //初始化人行采集配置默认值
                        if(result.pbcUnlimited){
                            $('#bankSwitchBtn').prop('checked',true);

                            if (result.pbcStartTime!=null){
                                $('#bankStartDatetime').timepicker('setTime',result.pbcStartTime);
                            }

                            if (result.pbcEndTime!=null){
                                $('#bankEndDatetime').timepicker('setTime',result.pbcEndTime);
                            }

                            if (result.pbcEndDate===''||result.pbcEndDate==null){
                                $('#bankEndDate').datetimepicker('setDate',new Date(myDate.getFullYear()+"-01-01"));
                            } else {
                                var str = result.pbcEndDate;
                                $('#bankEndDate').datetimepicker('setDate',new Date(Date.parse(str.replace(/-/g,"/"))));
                            }
                        }

                        //人行采集配置开关
                        form.on('switch(bankSwitchBtn)', function () {
                            if (this.checked) { //限制
                                $('#bankConfigDiv').show();
                            } else { //无限制
                                $('#bankConfigDiv').hide();
                            }
                        });
                        //工商采集方式单选按钮
                        form.on('radio(saicSwitchBtn)', function (data) {
                            if (data.value === "TIMING") {//定时采集
                                $("#saicStartTime").show();
                            } else {//文件导入、即时采集
                                $("#saicStartTime").hide();
                            }
                        });

                        //初始化工商采集配置默认值
                        if (result.saicUnlimited) {
                            $('input[name="saicSwitchBtn"][value="FILE"]').click();//文件上传采集
                        } else {
                            if (result.saicStartDate != null && result.saicStartDate !== "") {
                                $('#saicStartTime').show();
                                $('input[name="saicSwitchBtn"][value="TIMING"]').click();//定时采集
                                $('#saicStartDate').val(result.saicStartDate);
                            } else {
                                $('input[name="saicSwitchBtn"][value="IMMEDIATELY"]').click();//即时采集
                            }
                        }

                        //添加修改配置
                        form.on('submit(edit)', function (data) {
                            var param = {
                                pbcUnlimited: $('#bankSwitchBtn').prop('checked'),//人行采集配置是否有限制
                                saicUnlimited: (data.field.saicSwitchBtn === "FILE")//工商采集方式
                            };
                            if (param.pbcUnlimited) {
                                var pbcEndDate = $('#bankEndDate> .form-control').val();
                                var pbcStartTime = $('#bankStartDatetime').val();
                                var pbcEndTime = $('#bankEndDatetime').val();

                                //小时补0
                                var startTime = pbcStartTime.split(":");
                                var startHour = startTime[0].length === 1 ? '0' + startTime[0]  : startTime[0];
                                pbcStartTime = startHour + ':' + startTime[1];

                                var endTime = pbcEndTime.split(":");
                                var endHour = endTime[0].length === 1 ? '0' + endTime[0]  : endTime[0];
                                pbcEndTime = endHour + ':' + endTime[1];

                                //配置人行采集
                                if (pbcEndDate === '') {
                                    layerTips.msg('请输入人行数据截止日期');
                                    return false;
                                }
                                if (pbcStartTime === '') {
                                    layerTips.msg('请输入人行数据采集开始时间');
                                    return false;
                                }
                                if (pbcEndTime === '') {
                                    layerTips.msg('请输入行数据采集结束时间');
                                    return false;
                                }
                                param.pbcEndDate = pbcEndDate;
                                param.pbcStartTime = pbcStartTime;
                                param.pbcEndTime = pbcEndTime;
                            }

                            if (data.field.saicSwitchBtn === "TIMING") {
                                //配置工商采集
                                var saicStartDate = $('#saicStartDate').val();
                                if (saicStartDate === '') {
                                    layerTips.msg('请输入工商采集开始日期');
                                    return false;
                                } else {
                                    param.saicStartDate = saicStartDate;
                                }
                            }

                            $.ajax({
                                url: '../../annualTask/save/dataconfig',
                                type: 'POST',
                                data: param,
                                dataType: "json",
                                success: function (result) {
                                    if (result.rel) {
                                        layerTips.msg('更新成功');
                                        layer.close(addBoxIndex);
                                        if (callback) callback();
                                    } else {
                                        layerTips.msg(result.msg || '更新异常');
                                    }
                                },
                                error: function (err) {
                                    layerTips.msg('更新异常');
                                }

                            });
                            //这里可以写ajax方法提交表单
                            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
                        });

                        form.render();
                    },
                    end: function () {
                        addBoxIndex = -1;
                    }
                });
            });
        });
    }

    //比较字段配置按钮
    $('#compareFieldsConfigBtn').on('click',function () {
        dataConfig1();
    });

    var addBoxIndex1 = -1;

    function dataConfig1(callback) {
        if (addBoxIndex1 !== -1)
            return;
        // $.get('../../annualTask/comparefieldsconfig', null, function (data) {
        //     var result = data.result;
            $.get('compareFields.html', null, function (form) {

                addBoxIndex1 = layer.open({
                    type: 1,
                    title: '对比配置',
                    content: form,
                    //btn: ['保存', '取消'],
                    shade: false,
                    offset: ['20px', '20%'],
                    area: ['800px', '600px'],
                    maxmin: true,
                    end: function () {
                        addBoxIndex1 = -1;
                    }
                });
            });
        // });
    }

    function startAnnual(taskId){
        $.ajax({
            url: '../../annualTask/start/'+taskId,
            type: 'get',
            dataType: "json",
            success: function (result) {
                if(result.rel){
                    layerTips.msg('年检比对启动成功');
                    $('#task-action').addClass('dn');
                    $('#task-status').removeClass('dn');
                    $.ajax({
                        url: '../../annualTask/statistsics/' + taskId,
                        type: 'get',
                        dataType: "json",
                        success:function(res){
                        },
                        error:function(err){
                        }
                    });
                } else {
                    layerTips.msg('年检比对启动失败:'+result.msg);
                }

            },
            error: function (err) {
                if(err.responseJSON.message){
                    layerTips.msg(err.responseJSON.message);
                }else{
                    layerTips.msg('年检比对启动失败');
                }
            }

        });
    }

    //开始年检比对
    $('#startAnnualCheck').on('click',function () {
        if($('#core-data').data('isComplete') === false){
            layerTips.msg('请先导入核心数据');
            return false;
        }
        if($('#bank-data').data('isComplete') === false){
            layerTips.msg('请先完成人行数据采集');
            return false;
        }
        if($('#saic-data').data('isComplete') === false){
            layerTips.msg('请先完成工商数据采集');
            return false;
        }


        $.ajax({
            url: '../../annualTask/start/'+taskId+"/check",
            type: 'get',
            dataType: "json",
            success: function (result) {
                if(result.rel){
                   if(result.msg){//说明人行或者工商有失败的信息
                       layer.confirm(result.msg, {
                           btn: ['确认','取消'] //按钮
                       }, function(index){
                           startAnnual(taskId);
                           layer.close(index);
                       });
                   }else{
                       startAnnual(taskId);
                   }
                } else {
                    layerTips.msg(result.msg);
                }

            },
            error: function (err) {
                if(err.responseJSON.message){
                    layerTips.msg(err.responseJSON.message);
                }else{
                    layerTips.msg('年检比对启动失败');
                }
            }

        });
    });

    //重置按钮
    $(".resetBtn").on('click',function () {
        layer.confirm('重置会删除结果,是否确认重置？', {
            btn: ['同时清空数据','只清空结果数据','取消'] //按钮
        }, function(index){
            layer.msg('重置中', {
                icon: 16,
                shade: 0.01,
                time: 99000
            });
            $.ajax({
                url: '../../annualTask/collect/reset/'+taskId,
                type: 'get',
                dataType: "json",
                success: function (result) {
                    layer.closeAll('loading');
                    if(result.rel){
                        layerTips.msg('重置成功');
                        layer.close(index);
                        location.reload();
                    } else {
                        layerTips.msg('重置失败');
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layerTips.msg('重置失败');
                }
            });
            layer.close(index);
        }, function(index){
            layer.msg('重置中', {
                icon: 16,
                shade: 0.01,
                time: 99000
            });
            $.ajax({
                url: '../../annualTask/reset/'+taskId,
                type: 'get',
                dataType: "json",
                success: function (result) {
                    layer.closeAll('loading');
                    if(result.rel){
                        layerTips.msg('年检重置成功');
                        location.reload();
                    } else {
                        layerTips.msg('年检重置失败');
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layerTips.msg('年检重置失败');
                }
            });
            layer.close(index);
        }, function(){
            //取消
        });
    });

    //重置工商数据
    $(".resetSaicBtn").on('click',function () {
        layer.confirm('确定重置工商数据吗？', {
            btn: ['清空数据','取消'] //按钮
        }, function(index){
            layer.msg('重置中', {
                icon: 16,
                shade: 0.01,
                time: 99000
            });
            $.ajax({
                url: '../../annualTask/collect/resetSaic/'+taskId,
                type: 'get',
                dataType: "json",
                success: function (result) {
                    layer.closeAll('loading');
                    if(result.rel){
                        layerTips.msg('重置成功');
                        layer.close(index);
                        location.reload();
                    } else {
                        layerTips.msg('重置失败');
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layerTips.msg('重置失败');
                }
            });
            layer.close(index);
        }, function(){
            //取消
        });
    });

    //重置核心数据
    $(".resetCoreBtn").on('click',function () {
        layer.confirm('确定重置核心数据吗？', {
            btn: ['清空数据','取消'] //按钮
        }, function(index){
            layer.msg('重置中', {
                icon: 16,
                shade: 0.01,
                time: 99000
            });
            $.ajax({
                url: '../../annualTask/collect/resetCore/'+taskId,
                type: 'get',
                dataType: "json",
                success: function (result) {
                    layer.closeAll('loading');
                    if(result.rel){
                        layerTips.msg('重置成功');
                        layer.close(index);
                        location.reload();
                    } else {
                        layerTips.msg('重置失败');
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layerTips.msg('重置失败');
                }
            });
            layer.close(index);
        }, function(){
            //取消
        });
    });

    //重置人行数据
    $(".resetPbcBtn").on('click',function () {
        layer.confirm('确定重置人行数据吗？', {
            btn: ['清空数据','取消'] //按钮
        }, function(index){
            layer.msg('重置中', {
                icon: 16,
                shade: 0.01,
                time: 99000
            });
            $.ajax({
                url: '../../annualTask/collect/resetPbc/'+taskId,
                type: 'get',
                dataType: "json",
                success: function (result) {
                    layer.closeAll('loading');
                    if(result.rel){
                        layerTips.msg('重置成功');
                        layer.close(index);
                        location.reload();
                    } else {
                        layerTips.msg('重置失败');
                    }
                },
                error: function (err) {
                    layer.closeAll('loading');
                    layerTips.msg('重置失败');
                }
            });
            layer.close(index);
        }, function(){
            //取消
        });
    });

    //比对结果操作

    //resultBtn
    $('#resultBtn').on('click', function () {
        parent.tab.tabAdd({
                title: '年检结果',
                href: 'annualcheck/result.html?taskId=' + taskId
            }
        );
    });

    //重新比对
    $('#reCompareBtn').on('click', function () {
        //触发开始比对按钮
        $("#startAnnualCheck").click();
    });
});