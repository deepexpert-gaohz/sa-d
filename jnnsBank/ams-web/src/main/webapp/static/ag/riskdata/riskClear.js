layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

var riskClear = {
    baseUrl: "../../riskData",
    tableId: "",
    toolbarId: "",
    unique: "id",
    order: "desc",
    currentItem: {}
};

var layerTips;
riskClear.init = function () {
    /**
     * 查询开户风险
     */
    $.ajax({
        url: '../../riskData/findAccountNearDate',
        type: 'get',
        dataType: "json",
        async: false,
        success: function (res) {
            if (res.code == "ACK") {
                $("#riskDate").val(res.data.riskDate);
            }
        }
    });
    /**
     * 查询交易风险
     */
    $.ajax({
        url: '../../riskData/findTradeNearDate',
        type: 'get',
        dataType: "json",
        async: false,
        success: function (res) {
            if (res.code == "ACK") {
                $("#tradeDate").val(res.data.cjrq);
            }
        }
    });
};

layui.use(['form', 'layedit', 'laydate','common'], function () {

    var layer = layui.layer, //获取当前窗口的layer对象
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    layerTips = parent.layer === undefined ? layui.layer : parent.layer; //获取父窗口的layer对象
    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'tradeDate', 'endDate', 'YYYY-MM-DD', false);

    var laydateArr =  beginEndDateInit(laydate, 'riskDate', 'endDate', 'YYYY-MM-DD', false);
    riskClear.init();
    /**
     * @yangcq 20191125
     *交易类型补录
     */
    $("#jyClear").on('click',function () {
        var tradeDate = $("#tradeDate").val();
        $.ajax({
            url: '../../riskData/tradeClear?date='+tradeDate,
            type: 'get',
            dataType: "json",
            async: false,
            success: function (res) {
                if (res.code == "ACK") {
                    layerTips.msg(res.message);
                }
            }
        });
    });
    /**
     *@yangcq 20191125
     *客户类型补录
     */
    $("#khClear").on('click',function () {
        var riskDate = $("#riskDate").val();
        $.ajax({
            url: '../../riskData/recendClear?date='+riskDate,
            type: 'get',
            dataType: "json",
            async: false,
            success: function (res) {
                if (res.code == "ACK") {
                    layerTips.msg(res.message);
                }
            }
        });
    });
});

