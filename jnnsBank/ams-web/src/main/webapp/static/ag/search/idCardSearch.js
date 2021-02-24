layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['loading', 'laytpl', 'form', 'laydate'], function () {
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer,
        layer = layui.layer,
        form = layui.form,
        loading = layui.loading,
        laytpl = layui.laytpl,
        laydate = layui.laydate;

    laydate.render({
        elem: '#idCardBirthday',
        max : 'nowDate',
    });

    laydate.render({
        elem: '#idCardValidityBeginData',
        max : 'nowDate',
        format: 'yyyy.MM.dd'
    });

    laydate.render({
        elem: '#idCardValidityEndData',
        min : 'nowDate',
        format: 'yyyy.MM.dd'
    });


    var typeData = [
            { id: 1, name: '法定代表人' },
            { id: 2, name: '财务联系人' },
            { id: 3, name: '授权开户人' },
            { id: 4, name: '个人开户' },
            { id: 5, name: '其他' },
    ];
    $.get('../../dictionary/32119222946719/option',function (res) {
        if(res.code=="ACK"){
            typeData = res.data;
        }
        var getTpl = $('#typeTpl').html();
        laytpl(getTpl).render(typeData, function(html){
            $('#type').html(html);
        });
    });


    // 读卡认证
    $('#btnReadCard').on('click', function () {
        setData();
        var fullName = $('#idCardName').val();
        var idCardNo = $('#idCardNo').val();
        if (fullName == '') {
            layerTips.msg("请输入姓名");
            return false;
        }

        if (idCardNo == '') {
            layerTips.msg("请输入身份证号");
            return false;
        }
        //loading.show();
        submitData();

        return false;
    });
    var submitData = function(){
        var fullName = $('#idCardName').val();
        var idCardNo = $('#idCardNo').val();
        if (fullName == '') {
            return false;
        }

        if (idCardNo == '') {
            return false;
        }
        $.post('../../idCard/sumit', searchData(), function(data){
            loading.hide();
            $("#sust").text("")
            $("#errort").text("")
            var result = "";
            if(data) {
                result = data.checkResult;
                if(data.checkStatus=="1"){
                    $("#sus").show();
                    $("#sust").text(result)
                    $("#error").hide();
                }else{
                    $("#sus").hide();
                    $("#error").show();
                    $("#errort").text(result)
                }

            } else {
                $('#lwResult').html('<div class="result-text">暂无数据</div>');
            }
            $.post('../../customerSearch/queryIdCardLog',{name:fullName,cardNo:idCardNo,result:result});
            //$.post('../../customerSearch/queryIdCardLog?name=' + fullName + '&cardNo=' + idCardNo + '&result=' + result);

        }).error(function (e) {
            loading.hide();
            layerTips.msg(e.responseJSON.message);
            //$.post('../../customerSearch/queryIdCardLog?name=' + fullName + '&cardNo=' + idCardNo + '&result=' + e.responseJSON.message);
            $.post('../../customerSearch/queryIdCardLog',{name:fullName,cardNo:idCardNo,result:e.responseJSON.message});
        });
    }
    // 批量核查
    $('#btnBatchInspect').on('click', function () {
        var html = '<div style="margin:20px;">批量核查前请先下载模版<br>如已下载请上传核查表格</div>'
        layer.open({
            type: 1,
            area: '300px',
            title: '批量核查提示',
            content: html,
            btn: ['下载模板', '导入'],
            yes: function(index, layero){
                // 下载模板
                //window.location.href="../attach/idCardModel.xlsx";
                $("#templateDownloadForm").submit();
            }
            ,btn2: function(index, layero){
                layer.open({
                    type : 1,
                    title : '文件导入',
                    skin : 'layui-layer-rim', //加上边框
                    area : [ '300px', '200px' ], //宽高
                    content : '<div id="uploadimg"> <div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择文件</div> </div>'
                });
                var uploader = WebUploader.create({
                    auto: true, // 选完文件后，是否自动上传
                    swf: '../js/webuploader/Uploader.swf', // swf文件路径
                    server:  '../../idCard/import', // 文件接收服务端
                    pick: '#imgPicker', // 选择文件的按钮。可选
                    /*fileNumLimit: 5,//一次最多上传五张
                    // 只允许选择图片文件。
                    accept: {
                        title: 'Images',
                        extensions: 'gif,jpg,jpeg,bmp,png',
                        mimeTypes: 'image/!*'
                    }*/
                });
                uploader.on("uploadAccept", function (file, res) {
                    if (res.code === 'ACK') {
                        return true;
                    }
                    return false;
                });
                uploader.on( 'uploadSuccess', function( file, res ) {
                    layerTips.msg("上传成功,开始执行批量核查");
                    $.get('../../idCard/start',function (result) {
                        layerTips.msg(result.message);
                        $.get('../../customerSearch/queryIdCardLogBatch',function (data2) {
                        });
                    });
                });
                uploader.on( 'uploadError', function( file, reason ) {
                    layerTips.msg('上传失败');
                });
            }
        });
    });

    // 打印身份证
    $('#btnPrintIdCard').on('click', function () {
        var idCardLocalImageByte  = $("#idCardLocalImageByte").val();
        if(idCardLocalImageByte=="" || idCardLocalImageByte==undefined){
            layerTips.msg('二代身份证可打印');
            return false;
        }
        var  idCardValidityBeginData =$("#idCardValidityBeginData").val();
        var idCardValidityEndData = $("#idCardValidityEndData").val();
        var fullName = $('#idCardName').val();
        var idCardNo = $('#idCardNo').val();
        var idCardNation = $('#idCardNation').val();
        var idCardBirthday = $('#idCardBirthday').val();
        var idCardValidity = idCardValidityBeginData+"-"+idCardValidityEndData;
        var idCardOrgan = $('#idCardOrgan').val();
        var idCardAddress = $('#idCardAddress').val();
        var idCardLocalImageByte = $('#idCardLocalImageByte').val();
        var idCardSex = $("#idCardSex").val();
        var year="";
        var month = "";
        var day="";
        if(idCardBirthday!=null || idCardBirthday!=""){
            year = idCardBirthday.split("-")[0];
            month = idCardBirthday.split("-")[1];
            day = idCardBirthday.split("-")[2];
        }
        var printData = {
            realname: fullName,
            gender: idCardSex,
            nation: idCardNation,
            year: year,
            month: month,
            day: day,
            address: idCardAddress,
            idCard: idCardNo,
            issuingAuthority: idCardOrgan,
            effectiveDate:idCardValidity,
            imgUrl: 'data:image/jpeg;base64,'+idCardLocalImageByte
        }

        var printTpl = $('#printIdCardTpl').html();

        laytpl(printTpl).render(printData, function(html){
            layer.open({
                type: 1,
                area: '400px',
                title: '打印身份证',
                content: html,
                btn: ['打印', '取消'],
                yes: function(index, layero){
                    $("#printIdCardArea").printArea();
                }
                ,btn2: function(index, layero){
                    layer.close(index);
                }
            });
        });
    });

    // 打印核查单
    $('#btnPrintInspect').on('click', function () {
        var fullName = $('#idCardName').val();
        var idCardNo = $('#idCardNo').val();
        var idCardOrgan = $('#idCardOrgan').val();
        var sust = $('#sust').text();
        var errort = $('#errort').text();
        var idCardLocalImageByte = $('#idCardLocalImageByte').val();
        if(errort=="" && sust==""){
            layerTips.msg("请先核查");
            return ;
        }
        var printData = {
            realname: fullName,
            idCard: idCardNo,
            checkResult:sust+errort,
            issuingAuthority: idCardOrgan,
            imgUrl: 'data:image/jpeg;base64,'+idCardLocalImageByte
        }

        var printTpl = $('#printInspectTpl').html();

        laytpl(printTpl).render(printData, function(html){
            layer.open({
                type: 1,
                area: '600px',
                title: '打印核准单',
                content: html,
                btn: ['打印', '取消'],
                yes: function(index, layero){
                    $("#printInspectArea").printArea();
                }
                ,btn2: function(index, layero){
                    layer.close(index);
                }
            });
        });
    });
   var  interval;
    // 自动读卡
    $('#checkbox').on('click', function () {
        var ischecked = $("input[type='checkbox']").is(':checked');
        clearInterval(interval);
        if(ischecked){
            //重启一个新的定时器
            interval = setInterval(function () {
                setData();
                submitData();
            },2000);
        }
    });

    //联网身份核查日志查询
    $('#btnLog').on('click', function () {
        parent.tab.tabAdd({
            title: "联网身份核查日志查询",
            href: 'search/idCardSearchLog.html'
        });
    });
    var searchData = function () {
        var fullName = $('#idCardName').val();
        var idCardNo = $('#idCardNo').val();
        var idCardNation = $('#idCardNation').val();
        var idCardBirthday = $('#idCardBirthday').val();
        //var idCardValidity = $('#idCardValidity').val();
        var  idCardValidityBeginData =$("#idCardValidityBeginData").val();
       var idCardValidityEndData = $("#idCardValidityEndData").val();
        var idCardOrgan = $('#idCardOrgan').val();
        var idCardAddress = $('#idCardAddress').val();
        var idCardLocalImageByte = $('#idCardLocalImageByte').val();
        var idCardSex = $("#idCardSex").val();
        var idCardedType = $("input[name='idCardedType']:checked").val();
        var data = {"idCardNo":idCardNo, "idCardName":fullName,"idCardNation":idCardNation,"idCardBirthday":idCardBirthday,"idCardValidity":idCardValidityBeginData+"-"+idCardValidityEndData
        ,"idCardOrgan":idCardOrgan,"idCardAddress":idCardAddress,"idCardedType":idCardedType,"idCardLocalImageByte":idCardLocalImageByte,"idCardSex":idCardSex};
        return data;
    }
});
