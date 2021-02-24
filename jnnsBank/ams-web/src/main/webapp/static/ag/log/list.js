var log = {
    baseUrl: "../../adminLog",
    entity: "log",
    tableId: "userTable",
    toolbarId: "logToolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
log.columns = function () {
    return [{
        field: 'syslogContent',
        title: '日志名称'
    },{
        field: 'syslogContent',
        title: '下载',
        formatter: function (value, row, index) {
            return "<a class='downloadLog' href='javascript:downloadSyslogFile(\""+value+"\");' target='_self'>下载</a>";
        }
    }];
};

log.init = function () {
    log.table = $('#' + log.tableId).bootstrapTable({
        url: log.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + log.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false, //是否显示分页（*）
        sortable: false, //是否启用排序
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: log.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: log.columns(),
        onCheck: log.clickRow,
        responseHandler: function (res) {
            if (res.code === 'ACK') {
                return res.data;
            } else {
                layerTips.msg('查询失败');
                return false;
            }
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });

};

log.clickRow = function () {
    option.refresh();
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    log.init();
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'createddatestart', 'createddateend', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var editIndex;

    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    $('#btn_packageDownload').on('click', function () {
        var beginDate = $("#createddatestart").val();
        var endDate = $("#createddateend").val();
        if(beginDate && endDate){
            var downloadUrl = log.baseUrl+'/syslog/batchDown?beginDate='+beginDate+"&endDate="+endDate;
            $('<iframe>', { id:'idown', src:downloadUrl }).hide().appendTo('body');
        }else{
        	layer.msg("请输入开始和结束时间");
        }
    });

});

function downloadSyslogFile(filename) {
	document.getElementById("downloadFrame").src = log.baseUrl+"/syslog/exportLog?filename=" + filename;
}