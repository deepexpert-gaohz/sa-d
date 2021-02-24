layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

layui.use(['common', 'loading', 'validator', 'uploadV2', 'form', 'laydate', 'laytpl'], function () {
    var $ = layui.jquery,
        common = layui.common,
        loading = layui.loading,
        validator = layui.validator,
        upload = layui.uploadV2,
        laydate = layui.laydate;
    laytpl = layui.laytpl;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;

    //获取历史年检数据
    $.ajax({
        url: '../../annualHistory/getAnnualHistory',
        type: 'POST',
        data: {},
        dataType: "json",
        success: function (result) {
            if (result.rel) {
                var html = "";
                for (var i = 0; i < result.result.length; i++) {
                    // var passingRate = result.result[i].passingRate;
                    // if (passingRate === undefined) {
                    //     passingRate = 0;
                    // } else if (passingRate !== 0) {
                    //     passingRate = passingRate.toFixed(2);
                    // }
                    html += '' +
                        '<tr>\n' +
                        '    <td>' + result.result[i].year + '</td>\n' +
                        '    <td>' + changeDateFormat(result.result[i].createdDate) + '</td>\n' +
                        '    <td>' + result.result[i].passedNum + '</td>\n' +
                        '    <td>' + result.result[i].sum + '</td>\n' +
                        // '    <td>' + passingRate + '%</td>\n' +
                        '    <td><a class="view" style="color: blue" href="#" onclick="queryResult(\'' + result.result[i].id + '\')">查看结果</a></td>\n' +
                        '</tr>';
                }
                $("#historyTable tbody").html(html);
            }
        }
    });

});

function queryResult(taskId) {
    parent.tab.tabAdd({
            title: '年检结果',
            href: 'annualcheck/view_result.html?taskId=' + taskId
        }
    );
}