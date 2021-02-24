layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var highRisk = {
    baseUrl: "../../highRisk",
    tableId: "riskdataTradeTable",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {},
    orgOptions: '',
    modelIdOptions: '',
    roleOptions: '',

};

//穿梭框规则默认值
var value = [];

//穿梭框外部数据默认值
var wValue = [];
highRisk.init = function () {
    $.get("../../highRisk/highRiskInit", function (res) {
        var obj = JSON.parse(res);

        var ruleModel = obj.highRiskRule[0].ruleModel;
        // var externalData = obj.highRiskRule[0].externalData;
        //已选择模型规则
        value = ruleModel.split(",");
        // wValue = externalData.split(",");
        if (obj.highRisk.length != 0) {
            $("#hdate").html("已导入行内数据");
        }
        if (ruleModel != "" && ruleModel != " " && ruleModel != null) {
            $("#mdate").html("已选择规则模型");
        }
        // if (externalData != "" && externalData != " " && externalData != null) {
        //     $("#wdate").html("已选择外部数据");
        // }
    })
};
layui.use(['common', 'loading', 'validator', 'uploadV2', 'form', 'laydate', 'laytpl', 'transfer', 'util'], function () {

    var $ = layui.jquery,
        common = layui.common,
        loading = layui.loading,
        validator = layui.validator,
        upload = layui.uploadV2,
        form = layui.form,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        transfer = layui.transfer,
        util = layui.util;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    highRisk.init();

    //导入行内数据
    $('#core-data').on('click', function () {
        //页面层
        var index1 = layer.open({
            title: '导入',
            type: 1,
            // skin: 'layui-layer-rim', //加上边框
            area: ['500px', '250px'], //宽高
            content: '<div id="uploadimg" class="row" style="margin-left: 20%;margin-top: 15%;"><div id="fileList" class="uploader-list"></div><div id="coreFileUpload" class="col-md-5" >导入</div>' +
                '<div class="col-md-4"><button class="btn" style="background-color: #00b7ee;color: white;height: 39px;" id="core_download">下载导入模板</button></div></div>'
        });
        // 导入文件
        var uploaderCore = WebUploader.create({
            auto: false, // 选完文件后，是否自动上传
            swf: '../js/webuploader/Uploader.swf', // swf文件路径
            server: '../../highRisk/upload', // 文件接收服务端
            pick: '#coreFileUpload', // 选择文件的按钮。可选
            /*fileNumLimit: 1,//一次最多上传五张
            // 只允许选择图片文件。*/
            accept: {
                title: 'Excel',
                extensions: 'xls,xlsx',
                mimeTypes: 'application/vnd.ms-excel'
            },
        });
        uploaderCore.on('fileQueued', function (file) {
                layer.confirm('确认要上传' + file.name + '吗？', {
                        btn: ['确定', '取消']
                    }, function (index) {
                        uploaderCore.upload();
                        layer.close(index);
                    }, function (index) {
                        layer.close(index1);
                    }
                )
            }
        );
        uploaderCore.on('uploadSuccess', function (file, res) {
            layerTips.msg(res.message);
            layer.close(index1);
            $("#hdate").html("已导入")
        });
        uploaderCore.on('uploadError', function (file, reason) {
            layerTips.msg('上传失败');
        });
        uploaderCore.on('error', function (handler) {
            if (handler == 'Q_TYPE_DENIED') {
                layerTips.msg('文件类型不对，请上传xls或者xlsx后缀名的文件');
            } else {
                layerTips.msg(handler);
            }
        });
        //下载行内数据导入模板
        $('#core_download').on('click', function () {
            $('#downloadFrame').prop('src', '../../highRisk/download');
        });
        return false;
    });

    //规则模型
    $('#bank-data').on('click', function () {
        //初始化数据
        var data1 = [];
        $.get("../../highRisk/findmodel", function (res) {
            if (res.code === 'ACK') {
                var opt = '[';
                for (var i = 0; i < res.data.length; i++) {
                    data1.push({"value": res.data[i].modelId, "title": res.data[i].name});
                }
            }
            transfer.render({
                elem: '#modellist',
                data: data1,
                value: value,
                width: 400,
                title: ['规则模板', '已选择规则模板'],
                showSearch: true,
                processData: false,
                id: 'key123'
            })

        });
        //页面层
        index = layer.open({
            title: '规则模型配置',
            type: 1,
            area: ['900px', '500px'], //宽高
            content: '<div style="width: " id="modellist" class="demo-transfer"></div>',
            btn: ['确定', '取消'],
            success: function (index, layero) {

            },
            yes: function (index, layero) {
                value = [];
                var getData = transfer.getData('key123'); //获取右侧数据
                for (i in getData) {
                    value.push(getData[i].value);
                }
                $.get("../../highRisk/saveCon?value=" + value, function () {

                });
                if (value == "" || value == " " || value == null) {
                    $("#mdate").html("未选择模型规则");
                } else {
                    $("#mdate").html("已选择模型规则");
                }
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
        });

    });

    //外部数据
    $('#saic-data').on('click', function () {
        //初始化数据
        var externalData = [];

        $.get("../../highRisk/findDataApi", function (res) {
            if (res.code === 'ACK') {
                var opt = '[';
                for (var i = 0; i < res.data.length; i++) {
                    externalData.push({"value": res.data[i].apiNo, "title": res.data[i].apiName});
                }
            }
            transfer.render({
                elem: '#externalData',
                data: externalData,
                value: wValue,
                width: 400,
                title: ['外部数据接口', '已选择外部数据接口'],
                showSearch: true,
                processData: false,
                id: 'key2'
            })

        });

        //页面层
        index = layer.open({
            title: '外部数据采集',
            type: 1,
            // skin: 'layui-layer-rim', //加上边框
            area: ['900px', '500px'], //宽高
            content: '<div style="width: " id="externalData" class="demo-transfer"></div>',
            btn: ['确定', '取消'],
            success: function (index, layero) {

            },
            yes: function (index, layero) {
                wValue = [];
                var getData = transfer.getData('key2'); //获取右侧数据
                for (i in getData) {
                    wValue.push(getData[i].value);
                }
                $.get("../../highRisk/saveApi?externalData=" + wValue, function () {

                });
                if (wValue == "" || wValue == " " || wValue == null) {
                    $("#wdate").html("未选择外部数据接口");
                } else {
                    $("#wdate").html("已选择外部数据接口");
                }
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
            // yes: function (index, layero) {
            //     if ($("#apilist").val() != "defau") {
            //
            //         $("#wdate").html($("#apilist").find("option:selected").text());
            //     }
            //     layer.close(index); //如果设定了yes回调，需进行手工关闭
            // }
        });
    });

    //开始跑批
    $("#startAnnualCheck").on('click', function () {
        $.ajax({
            url: "../../highRisk/runData?value=1",
            type: 'get',
            dataType: "json",
            success: function (res) {
                alert(res.message);

            },
            error: function () {
                alert("跑批失败!")
            }
        });


    });

    //重置按钮
    $("#resetBtn").on('click', function () {
        layer.confirm('重置会清除所有数据,是否确认重置？', {
            btn: ['重置', '取消'] //按钮
        }, function (index) {
            $.ajax({
                url: "../../highRisk/resetData",
                type: 'get',
                dataType: "json",
                success: function (res) {
                    alert(res.message);
                    $("#hdate").html("未导入行内数据");
                    $("#mdate").html("未选择模型规则");
                    $("#wdate").html("未选择外部数据");
                    layer.close(index);
                    location.reload();
                },
                error: function () {
                    alert("重置失败!")
                }
            });
        }, function (index) {
        })
    });


});