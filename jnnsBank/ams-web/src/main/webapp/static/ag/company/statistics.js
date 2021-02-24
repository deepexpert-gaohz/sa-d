layui.config({
    base: '../js/'
});
layui.define(['jquery', 'element', 'layer', 'laydate', 'treeTable','murl'], function (exports) {
    var $ = layui.jquery,
        element = layui.element,
        layer = layui.layer,
        laydate = layui.laydate,
        url = layui.murl;

    var taskId_url = url.get("taskId");

    loadAnnualTask();

    function loadAnnualTask() {
        if (taskId_url !== undefined && taskId_url != null) {
            setTreeTable(taskId_url);
        } else {
            $.get('../../annualTask/getAnnualTaskId', function (taskId) {
                setTreeTable(taskId);
            });
        }
    }

    function setTreeTable(taskId){
        if(taskId != null && taskId !== '') {
            var treeTable = layui.treeTable({
                elem: '#tree_list',
                url: '../../annualTask/statistics/' + taskId,
                fieldId: 'id',
                fieldPid: 'pid',
                fieldName: 'name',
                fieldRows: 'rows',
                fieldHasRows: 'hasRows',
                cols: [
                    {
                        field: 'name',
                        title: '机构',
                        width: 250
                    }, {
                        field: 'annualTotalCount',
                        title: '账号总数',
                        width: 120
                    }, {
                        field: 'annualPassCount',
                        title: '通过总数',
                        width: 120
                    }, {
                        field: 'annualPassRate',
                        title: '通过率',
                        width: 120
                    }
                ]
            });
        }

    }

    // $('#btn_query').on('click', function () {
    //     var url = '../../annualTask/statisticss';
    //     treeTable.refresh({url:url})
    // });

    //刷新操作
    $('#treeListId').on('click', function () {
        $("#tree_list").empty();
        loadAnnualTask();
    });
});