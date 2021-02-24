layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

//委托更新功能公共模块
var kyc = {
    baseUrl: "../../kyc",
};

var layerTips;
layui.use(['common', 'loading', 'laydate'], function () {
    var $ = layui.jquery,
        common = layui.common, loading = layui.loading, laydate = layui.laydate;

    layerTips = parent.layer === undefined ? layui.layer : parent.layer;
});

function getEntrustUpdateResult(data) {
    if(data.lastUpdateDays <= 3) {
        $('#dayBetween').html('3天内更新');
    } else if(data.lastUpdateDays > 3 && data.lastUpdateDays <= 7) {
        $('#dayBetween').html('7天内更新');
    } else if(data.lastUpdateDays > 7 && data.lastUpdateDays <= 15) {
        $('#dayBetween').html('15天内更新');
    } else if(data.lastUpdateDays > 15 && data.lastUpdateDays <= 30) {
        $('#dayBetween').html('30天内更新');
    } else if(data.lastUpdateDays > 30) {
        $('#dayBetween').html('30天前更新');
    }
    $.get(kyc.baseUrl + '/getEntrustUpdateResult?keyword=' + encodeURI(data.name), function (data) {
        var result = data.data;
        if(result) {
            var state = result.state;
            if(state == '1') {
                $('#entrustUpdateState').html('委托更新成功');
            } else if(state == '2') { //委托状态未完成
                $('#entrustUpdateState').html('已委托，正在更新中');
                $('#btn_entrustUpdate').attr('disabled', true);
                $('#btn_entrustUpdate').attr('class', 'layui-btn layui-btn-disabled');
            }
        }
    });
}

function entrustUpdate(companyName) {
    if(isEmpty(companyName)) {
        layerTips.msg('企业名称不能为空');
        return;
    }

    $('#btn_entrustUpdate').attr('disabled', true);
    $.get(kyc.baseUrl + '/entrustUpdate?keyword=' + encodeURI(companyName), function (data) {
        if(data) {
            var result = data.data;
            if(result.state) {
                layerTips.msg(result.details);
                if(result.state == '1') {
                    $('#entrustUpdateState').html('');
                    $('#btn_entrustUpdate').html('发起委托更新');
                    $('#btn_entrustUpdate').attr('disabled', true);
                    $('#btn_entrustUpdate').attr('class', 'layui-btn layui-btn-disabled');
                    return;
                }
            } else {
                layerTips.msg("委托更新异常");
            }
        }

        $('#btn_entrustUpdate').attr('disabled', false);
    });
}