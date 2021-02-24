layui.config({
    base: '../js/'
});
layui.define(['jquery', 'element', 'layer', 'laydate', 'treeTable','form'], function (exports) {
    var $ = layui.jquery,
    element = layui.element,
    layer = layui.layer,
    laydate = layui.laydate;
    form = layui.form;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    // $('a.numRenderClass').on('click', function () {
    //     var dataCode = $(this).attr('data-code');
    //     alert(dataCode);
    // });

    numRenderTo = function(param){
        parent.tab.tabAdd({
            href: 'company/companyAccounts.html'+param,
            icon: 'fa fa-calendar-plus-o',
            title: '账户信息统计列表'
        });
    }

    numRender = function(value,rows,accountStatus){
        if(value >0){
            var acctType = $('#acctType').val();
            var string003 = $('#string003').val();
            var dataUrl ='?acctType=' + acctType + '&accountStatus=' + accountStatus+'&orgCode='+rows.orgCode+"&string003="+string003+"&string005=accountStatistics";
            return '<a href="javascript:numRenderTo(\''+dataUrl+'\')" style="color: blue;" class="numRenderClass blue">'+ value +'</ a>';
        }else{
            return value;
        }
    }

    var treeTable = layui.treeTable({
        elem: '#tree_list',
        url: '../../businessStatistics/info/list',
        fieldId: 'id',
        fieldPid: 'pid',
        fieldName: 'name',
        fieldRows: 'rows',
        fieldHasRows: 'hasRows',
        cols: [
            {field: 'name', title: '机构', width: 150}
            ,{field: 'num1', title: '正常', width: 80,render: function(value, rows){
                   return numRender(value,rows,"normal");
                }}
            ,{field: 'num2', title: '销户', width: 80,render: function(value, rows){
                    return numRender(value,rows,"revoke");
                }}
            ,{field: 'num3', title: '久悬', width: 80,render: function(value, rows){
                    return numRender(value,rows,"suspend");
                }}
        ]
    });

    $('#btn_query').on('click', function () {
        var acctType = $('#acctType').val();
        var string003 = $('#string003').val();
        var url = '../../businessStatistics/info/list?acctType=' + acctType + '&string003=' + string003;
        treeTable.refresh({url:url})
    });

    $('#btn_export').on('click', function () {
        var acctType = $('#acctType').val();
        var string003 = $('#string003').val();
        var url = '../../businessStatistics/info/export?acctType=' + acctType + '&string003=' + string003;
        $("#downloadFrame").prop('src', url);
        // treeTable.refresh({url:url});
    });

});