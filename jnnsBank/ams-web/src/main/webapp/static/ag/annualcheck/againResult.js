layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['common', 'loading', 'annual', 'murl'], function () {
    var $ = layui.jquery,
        common = layui.common,
        annual = layui.annual,
        url = layui.murl;
    var data = parent.layui.annualAgainResultData;
    var html = '';
    for (var i = 0; i < data.length; i++) {
        html += '<tr>' +
            '<td>' + data[i].acctNo + '</td>' +
            '<td>' + data[i].depositorName + '</td>' +
            '<td>' + ((data[i].forceStatus === 'AGAIN_SUCCESS'||data[i].forceStatus === 'SUCCESS') ? '成功' : '失败') + '</td>' +
            '<td>' + (data[i].pbcSubmitErrorMsg || "") + '</td>' +
            '<td><a href="#" data-id="' + data[i].id + '">对比结果</a></td>' +
            '</tr>'
    }
    $('#annualAgainResultTable tbody').html(html);
    $('#annualAgainResultTable a').click(function () {
        var id = $(this).attr("data-id");
        annual.loadCompareResult(id, '年检比对详情', 'view_compare.html');
    });
});