layui.config({
    base: '../js/'
});
layui.define(['jquery', 'element', 'layer', 'laydate', 'treeTable','form'], function (exports) {
    var $ = layui.jquery,
    element = layui.element,
    layer = layui.layer,
    laydate = layui.laydate;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'createddatestart', 'createddateend', 'YYYY-MM-DD', false);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    numRenderTo = function(param){
        parent.tab.tabAdd({
            href: 'company/statisticsAccount.html'+param,
            icon: 'fa fa-calendar-plus-o',
            title: '账户开变销统计列表'
        });
    }

    numRender = function(value,rows,accountStatus) {
        if (value > 0) {
            var createddatestart = $('#createddatestart').val();
            var createddateend = $('#createddateend').val();
            var acctType = $('#acctType').val();
            var dataUrl = '?createddatestart=' + createddatestart + '&createddateend=' + createddateend + '&accountStatus=' + accountStatus + '&acctType=' + acctType + '&orgCode=' + rows.orgCode ;
            return '<a href="javascript:numRenderTo(\'' + dataUrl + '\')" style="color: blue;" class="numRenderClass blue">' + value + '</ a>';
        } else {
            return value;
        }
    }

    var treeTable = layui.treeTable({
        elem: '#tree_list',
        url: '../../businessStatistics/list',
        fieldId: 'id',
        fieldPid: 'pid',
        fieldName: 'name',
        fieldRows: 'rows',
        fieldHasRows: 'hasRows',
        cols: [
            {field: 'name', title: '机构', width: 150}
            ,{field: 'num1', title: '新开户', width: 80,render: function(value, rows){
                    return numRender(value,rows,"ACCT_OPEN");
                }}
            ,{field: 'num2', title: '变更', width: 80,render: function(value, rows){
                    return numRender(value,rows,"ACCT_CHANGE");
                }}
            ,{field: 'num3', title: '销户', width: 80,render: function(value, rows){
                    return numRender(value,rows,"ACCT_REVOKE");
                }}
            ,{field: 'num4', title: '久悬', width: 80,render: function(value, rows){
                    return numRender(value,rows,"ACCT_SUSPEND");
                }}
        ]
    });

    $('#btn_query').on('click', function () {
        var createddatestart = $('#createddatestart').val();
        var createddateend = $('#createddateend').val();
        var acctType = $('#acctType').val();
        if(createddatestart == '' || createddateend == '') {
            layer.alert('请输入开始日期和结束日期');
            return;
        }
        var url = '../../businessStatistics/list?createddatestart=' + createddatestart + '&createddateend=' + createddateend + '&acctType=' + acctType;
        treeTable.refresh({url:url})
    });

    $('#btn_export').on('click', function () {
        var createddatestart = $('#createddatestart').val();
        var createddateend = $('#createddateend').val();
        var acctType = $('#acctType').val();
        var url = '../../businessStatistics/export?createddatestart=' + createddatestart + '&createddateend=' + createddateend + '&acctType=' + acctType;
        $("#downloadFrame").prop('src', url);
    });

});