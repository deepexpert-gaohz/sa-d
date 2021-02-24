layui.config({
	base: '../js/' //假设这是你存放拓展模块的根目录
});
layui.use(['form', 'common'], function () {
		var form = layui.form
		common = layui.common;

		var id = decodeURI(common.getReqParam('id'));

	// $.get('../../ui/ag/announcement/json/detail.json?id=' + id, null, function (res) {
    $.get('../../announcement/getOne?id=' + id, null, function (res) {
		if (res.code === 'ACK') {
			var item = res.data;
			$('#headContent').text('各位同仁：');
			$('#title').text(item.title);
            $('#content').text(item.content);
            $('#organName').text(item.organName);
            $('#noticeDate').text(item.noticeDate);
		}
	});

    $.get('../../announcement/getAttachment?id=' + id, null, function (res) {
        if (res.code === 'ACK') {
            $('#download').text(res.data.filename);
            $('#download').attr("href","../../announcement/download?id="+id);
            $('#download').show();
        }
    });
});
