var historyTaskData = {
    baseUrl: "../../workFlow",
    tableId: "taskDataTable",
    toolbarId: "toolbarModelRely",
    unique: "id",
    order: "asc",
    currentItem: {},
    tableNameOptions: ''
};

var countId = task.coountIdVal;
historyTaskData.queryParams = function (params) {
    if (!params)
        return {
            modelName : $("#modelName").val(),
            jgName:$("#jgName").val(),
            startEndTimeTwo:$("#startEndTimeTwo").val()
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit, //页面大小
        offset: (params.offset / params.limit)+1, //页码
        modelName : $("#modelName").val(),
        jgName:$("#jgName").val(),
        startEndTimeTwo:$("#startEndTimeTwo").val()
    };

    return temp;
};


function getColumns(countId) {
    // 加载动态表格
    var myColumns = new Array();
    $.ajax({
        url : historyTaskData.baseUrl + '/countTitle/'+countId,
        type : 'GET',
        dataType : "json",
        async : false,
        success : function(returnValue) {
            // 异步获取要动态生成的列
            if(returnValue!=null){
                var arr = eval(returnValue.data);
                $.each(arr, function(i, item) {
                    myColumns.push({
                        "field" : item.fieldsEn,
                        "title" : item.fieldsZh,
                        "hide" : true,
                        "align" : 'left',
                        "width":150
                    });
                });
            }
        }
    });
    return myColumns;
}
var taskId;
historyTaskData.init = function (countId,id,processInstanceId,processDefinitionId) {
    taskId= id;
    //var columns =  new getColumns(countId);//加载模型字段
    processDefinitionIdVal = processDefinitionId;
    layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
        var editIndex;
        var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
            layer = layui.layer, //获取当前窗口的layer对象
            form = layui.form,
            layedit = layui.layedit,
            laydate = layui.laydate,
            upload = layui.upload;
        laytpl = layui.laytpl;
        var addBoxIndex = -1;
        //初始化页面上面的按钮事件
        //获取账户信息
        $.get(historyTaskData.baseUrl +"/getOpenAccountInfoForm/" +countId , null, function (data) {
            if (data !=null) {
                $('#name').text(data.result.acctName);
                var getTpl = $('#accountTpl').html();
                laytpl(getTpl).render(data.result, function (html) {
                    $('#accountInfo').html(html);
                });
            }
        });
    });
};

layui.use(['form', 'layedit', 'laydate', 'upload'], function () {
    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate,
        upload = layui.upload;historyTaskData
    var addBoxIndex = -1;
    //初始化页面上面的按钮事件
});

function  queryComment(procInsId){
    $.ajaxSettings.async = false;
    $.get("../../task/queryComment/"+procInsId,null,function (data) {
        var comments = data.data
        for (var i=0; i<comments.length;i++){
            var commentDiv = `<tr>
                                <td>${comments[i].userId}</td>
                                <td>${comments[i].time}</td>
                               
                                <td>${comments[i].message}       </td>
                              </tr>`
            $("#commentMessage").append(commentDiv);
        }

    })
}
/**
 *  加载跟踪图
 *  @add by yangcq 20190528
 * @type {{}}
 */
var DiagramGenerator = {};
var pb1;
$(document).ready(function(){
    var query_string = {};
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        query_string[pair[0]] = pair[1];
    }
    /*var processDefinitionId = query_string["processDefinitionId"];
    var processInstanceId = query_string["processInstanceId"];*/
    var processInstanceId = procInsId;
    var processDefinitionId = processDefinitionIdVal;
    console.log("Initialize progress bar");

    pb1 = new $.ProgressBar({
        boundingBox: '#pb1',
        label: 'Progressbar!',
        on: {
            complete: function() {
                console.log("Progress Bar COMPLETE");
                this.set('label', 'complete!');
                if (processInstanceId) {
                    ProcessDiagramGenerator.drawHighLights(processInstanceId);
                }
            },
            valueChange: function(e) {
                this.set('label', e.newVal + '%');
            }
        },
        value: 0
    });
    console.log("Progress bar inited");

    ProcessDiagramGenerator.options = {
        diagramBreadCrumbsId: "diagramBreadCrumbs",
        diagramHolderId: "diagramHolder",
        diagramInfoId: "diagramInfo",
        on: {
            click: function(canvas, element, contextObject){
                var mouseEvent = this;
                console.log("[CLICK] mouseEvent: %o, canvas: %o, clicked element: %o, contextObject: %o", mouseEvent, canvas, element, contextObject);

                if (contextObject.getProperty("type") == "callActivity") {
                    var processDefinitonKey = contextObject.getProperty("processDefinitonKey");
                    var processDefinitons = contextObject.getProperty("processDefinitons");
                    var processDefiniton = processDefinitons[0];
                    console.log("Load callActivity '" + processDefiniton.processDefinitionKey + "', contextObject: ", contextObject);

                    // Load processDefinition
                    ProcessDiagramGenerator.drawDiagram(processDefiniton.processDefinitionId);
                }
            },
            rightClick: function(canvas, element, contextObject){
                var mouseEvent = this;
                console.log("[RIGHTCLICK] mouseEvent: %o, canvas: %o, clicked element: %o, contextObject: %o", mouseEvent, canvas, element, contextObject);
            },
            over: function(canvas, element, contextObject){
                var mouseEvent = this;
                //console.log("[OVER] mouseEvent: %o, canvas: %o, clicked element: %o, contextObject: %o", mouseEvent, canvas, element, contextObject);

                // TODO: show tooltip-window with contextObject info
                ProcessDiagramGenerator.showActivityInfo(contextObject);
            },
            out: function(canvas, element, contextObject){
                var mouseEvent = this;
                ProcessDiagramGenerator.hideInfo();
            }
        }
    };

    var baseUrl = window.document.location.protocol + "//" + window.document.location.host + "/";
    var shortenedUrl = window.document.location.href.replace(baseUrl, "");
    baseUrl = baseUrl + shortenedUrl.substring(0, shortenedUrl.indexOf("/"));
    ActivitiRest.options = {
        processInstanceHighLightsUrl: baseUrl + "/act/service/process-instance/{processInstanceId}/highlights",
        processDefinitionUrl: baseUrl + "/act/service/process-definition/{processDefinitionId}/diagram-layout",
        processDefinitionByKeyUrl: baseUrl + "/act/service/process-definition/{processDefinitionKey}/diagram-layout",
        processInstanceUrl: baseUrl + "/act/service/process-instance/{processInstanceId}/diagram-layout"

    };
    if (processDefinitionId) {
        ProcessDiagramGenerator.drawDiagram(processDefinitionId);

    } else {
        alert("processDefinitionId parameter is required");
    }
});