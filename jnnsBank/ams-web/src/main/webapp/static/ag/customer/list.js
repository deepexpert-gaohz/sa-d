layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var changeButtRole = false;

$.ajax({
    type: "get",
    url: "../../permission/element/code",
    data: {
        code: 'customerManager:change'
    },
    dataType: "json",
    async: false,
    success: function (res) {
        changeButtRole = res;
    },
    error: function () {
        layer.msg("error");
    }
});

    var customer = {
        baseUrl: "../../customerPublic",
        entity: "customerAllResponse",
        tableId: "customerTable",
        toolbarId: "toolbar",
        unique: "id",
        order: "desc",
        currentItem: {}
    };
    customer.columns = function () {
        return [{
            field: 'id',
            title: 'ID',
            visible: false
        }, {
            field: 'customerNo',
            title: '客户号'
        }, {
            field: 'depositorName',
            title: '客户名称',
            'class': 'W200'
        }, {
            field: 'economyType',
            title: '经济类型',
            'class': 'W120'
        }, {
            field: 'credentialType',
            title: '证件类型',
            'class': 'W120'
            // formatter: function (value, row, index) {
            //     return getFileTypeName(value)
            // }
        }, {
            field: 'fileNo',
            title: '证件号',
            'class': 'W120'
        // }, {
        //     field: 'legalType',
        //     title: '法人类型',
        //     formatter: function (value, row, index) {
        //         return getLegalTypeName(value)
        //     }
        }, {
            field: 'legalName',
            title: '法定代表人(负责人)'
        }, {
            field: 'organName',
            title: '客户开立机构'
        }, {
            field: 'abnormal',
            title: '风险异动',
            formatter: function (value, row, index) {
                // return "<a href=\"javascript:void(0);\" style='color: blue' onclick='btnCustomerAbnormalView(" +"\"" + row.id + "\"" +")'>是</a>";
                if (value == null) {
                    return '-';
                } else if (value) {
                    return "<a href=\"javascript:void(0);\" style='color: blue' onclick='btnCustomerAbnormalView(" +"\"" + row.id + "\"" +")'>是</a>";
                } else {
                    return '否';
                }
            }
        }, {
            field: 'abnormalTime',
            title: '系统异动日期'
        }, {
            field: 'operate',
            title: '操作',
            formatter: function (value, row, index) {
                var html = "<a href=\"javascript:void(0);\" style='color: blue' onclick='btnCustomerView(" +"\"" + row.id + "\"" +")'>查看</a>";
                if (changeButtRole) {
                    html += "   <a href=\"javascript:void(0);\" id='customerChangeBtn' style='color: blue;' onclick='btnCustomerChange(\"" + row.id + "\",\"" + row.depositorName + "\")'>变更</a>";
                }
                return html;
            }
        }];
    };

customer.queryParams = function (params) {
    var abnormal = null;
    if ($("#abnormal").val() === '1') {
        abnormal = true;
    } else if ($("#abnormal").val() === '0') {
        abnormal = false;
    }
    var temp = {
        customerNo: $.trim($("#customerNo").val()),
        depositorName: $.trim($("#depositorName").val()),
        economyType: $.trim($("#economyType").val()),
        credentialType: $.trim($("#credentialType").val()),
        legalName: $.trim($("#legalName").val()),
        organName: $.trim($("#organName").val()),
        abnormal: abnormal
    };
    if (params) {
        temp.size = params.limit; //页面大小
        temp.page = params.offset / params.limit; //页码
    }
    return temp;
};

customer.init = function () {

    customer.table = $('#' + customer.tableId).bootstrapTable({
        url: customer.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + customer.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: true, //是否启用排序
        sortOrder: "desc", //排序方式
        sortName: 'lastUpdateDate',
        queryParams: customer.queryParams,//传递参数（*）
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50, 100], //可供选择的每页的行数（*）
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: false,
        showColumns: false, //是否显示所有的列
        showRefresh: true, //是否显示刷新按钮
        minimumCountColumns: 2, //最少允许的列数
        clickToSelect: true, //是否启用点击选中行
        uniqueId: customer.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: customer.columns()
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};
customer.select = function (layerTips) {
    var rows = customer.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        customer.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','common'], function () {

    document.getElementById("paging_container").width = document.body.offsetWidth
        - 40 + 'px';
    customer.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        common = layui.common;
    var addBoxIndex = -1;

    var tabId = decodeURI(common.getReqParam("tabId"));
    var checked =true;
    var role =0;
    if(tabId) {
        parent.tab.deleteTab(common.decodeUrlChar(tabId));
    }
    //获取页面中“经济类型”下拉数据
    $.get('../../dictionary/findOptionsByDictionaryName?name=economyTypeValue2Item', function (res) {
        if (res.code === "ACK") {
            for (var i = 0; i < res.data.length; i++) {
                $('#economyType').append("<option value='" + res.data[i].name + "'>" + res.data[i].value + "</option>");
            }
            form.render('select');
        }
    });
    //获取页面中“证件类型”下拉数据
    $.get('../../dictionary/findOptionsByDictionaryNameStartWith?name=证明文件1', function (res) {
        if (res.code === "ACK") {
            for (var i = 0; i < res.data.length; i++) {
                $('#credentialType').append("<option value='" + res.data[i].value + "'>" + res.data[i].name + "</option>");
            }
            form.render('select');
        }
    });

    //初始化页面上面的按钮事件
    $('#btn_query').on('click', function () {
        var queryParams = customer.queryParams();
        queryParams.pageNumber=1;
        customer.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_customer_open').click(function(){
        var href = "validate/detail.html?type=customerOpen";

        parent.tab.tabAdd({
                title: '校验管理',
                href: href
            }
        );
        return false;
    });

});


function btnCustomerChange(id, depositorName) {
    $.get('../../allAccountPublic/getChangeAuthority?depositorName=' + depositorName, function(data) {
        if(data == false) {
            layer.alert("该客户的相关账户创建行才有权限修改");
            return;
        } else {
            parent.tab.tabAdd({
                title: '变更-'+depositorName,
                href: 'customer/customerOpen.html?id='+parseFloat(id)+'&depositorName='+encodeURI(depositorName) + '&operateType=change&type=update'
            });
        }
    });

}

//查看详情，且进入页默认显示“风险异动信息”Tab页
function btnCustomerAbnormalView(id) {
    btnCustomerView(id, 'abnormal');
}

/**
 * 查看详情
 * @param id 当前行的uniqueId
 * @param tabType 默认显示的Tab页标记
 */
function btnCustomerView(id, tabType) {
    var rowDate = $('#customerTable').bootstrapTable('getRowByUniqueId', id);
    parent.tab.tabAdd({
        title: '查看-' + nullToEntity(rowDate.depositorName),
        href: 'customer/customerOpen.html?id=' + parseFloat(id)
        + '&compareResultSaicCheckId=' + encodeURI(nullToEntity(rowDate.compareResultSaicCheckId))
        + '&depositorName=' + encodeURI(nullToEntity(rowDate.depositorName))
        + '&createdDate=' + encodeURI(nullToEntity(rowDate.createdDate))
        + '&organName=' + encodeURI(nullToEntity(rowDate.organName))
        + '&compareTaskId=' + encodeURI(nullToEntity(rowDate.compareTaskId))
        + '&saicInfoId=' + encodeURI(nullToEntity(rowDate.saicInfoId))
        + '&compareResultId=' + encodeURI(nullToEntity(rowDate.compareResultId))
        + '&abnormalTime=' + encodeURI(nullToEntity(rowDate.abnormalTime))
        + '&tabType=' + encodeURI(tabType)
        // + '&customerNo=' + encodeURI(nullToEntity(rowDate.customerNo))
        + '&operateType=select'
        +'&type=view'
    });
}

function nullToEntity(str) {
    return str == null ? "" : str;
}

