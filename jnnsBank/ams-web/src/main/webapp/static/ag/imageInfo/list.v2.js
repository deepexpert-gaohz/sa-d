layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});
var apply = {
    baseUrl: "../../video",
    entity: "user",
    tableId: "videoTable",
    toolbarId: "toolbar",
    unique: "applyid",
    order: "desc",
    currentItem: {}
};
apply.columns = function () {
    return [{
        radio: true
    },{
        field: 'recordsNo',
        title: '双录编号'
    },{
        field: 'acctNo',
        title: '账号'
    }, {
        field: 'depositorName',
        title: '企业名称'
    }, {
        field: 'legalName',
        title: '法人名称'
    },  {
        field: 'customerName',
        title: '客户名称'
    },  {
        field: 'acctType',
        title: '性质',
        'class': 'W120',
        visible: false,
        formatter: function (value, row, index) {
            return changeAcctType(value)
        }
    }, {
        field: 'regNo',
        title: '工商注册编号',
        visible: false,
    }, {
        field:'source',
        title:'来源',
        visible: false,
        formatter: function (value, row, index) {
            return switchSource(value);
        }
    }, {
        field: 'businessType',
        title: '业务种类',
        formatter: function (value, row, index) {
            return switchBusinessType(value);
        }
    }, {
        field: 'dateTime',
        title: '双录时间'
    }, {
        field: 'faceResult',
        title: '核实结果',
        formatter: function (value, row, index) {
            return switchFaceResult(value);
        }
    }, {
        field: 'organFullId',
        title: '机构号'
    }, {
        field: 'username',
        title: '双录柜员'
    }, {
        field: 'recordType',
        title: '双录方式',
        formatter: function (value, row, index) {
            return switchRecordType(value);
        }
    }, {
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="' + value + '">查看</a>';
        }
    }];
};
apply.queryParams = function (params) {
    if (!params)
        return {
            acctNo: $("#acctNo").val(),
            depositorName:$("#depositorName").val(),
            legalName:$("#legalName").val(),
            acctType:$("#acctType").val(),
            regNo:$("#regNo").val(),
            beginDate:$("#beginDate").val(),
            endDate:$("#endDate").val(),
            recordsNo: $("#recordsNo").val(),
            customerName:$("#customerName").val(),
            businessType:$("#businessType").val(),
            username:$("#username").val(),
            recordType:$("#recordType").val(),
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        acctNo: $("#acctNo").val(),
        depositorName:$("#depositorName").val(),
        legalName:$("#legalName").val(),
        acctType:$("#acctType").val(),
        regNo:$("#regNo").val(),
        beginDate:$("#beginDate").val(),
        endDate:$("#endDate").val(),
        recordsNo: $("#recordsNo").val(),
        customerName:$("#customerName").val(),
        businessType:$("#businessType").val(),
        username:$("#username").val(),
        recordType:$("#recordType").val()
    };
    return temp;
};
apply.init = function () {

    apply.table = $('#' + apply.tableId).bootstrapTable({
        url: apply.baseUrl + '/search', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + apply.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: apply.order, //排序方式
        queryParams: apply.queryParams,//传递参数（*）
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
        uniqueId: apply.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: apply.columns(),
        // onDblClickRow:function (items,$element) {
        //     var id = items.id;
        //     var depositorName = items.depositorName;
        //     parent.tab.tabAdd({
        //         title: '开户-'+depositorName,
        //         href: 'bank/view.html?id='+id+'&depositorName='+encodeURI(depositorName)
        //     });
        // },
        onLoadError: function(status){
            ajaxError(status);
        }
    });
};

apply.select = function (layerTips) {
    var rows = apply.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        apply.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate','layer','common'], function () {
    var common = layui.common;
    apply.init();

    var editIndex;
    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form,
        layedit = layui.layedit,
        laydate = layui.laydate;


    var laydateArr = beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);
    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });
    $('#btn_query').on('click', function () {
        var queryParams = apply.queryParams();
        queryParams.pageNumber=1;
        apply.table.bootstrapTable('refresh', queryParams);
    });

    $('#btn_export_excel').on('click', function () {
        var queryParams = apply.queryParams();
        $("#downloadFrame").prop('src', apply.baseUrl+"/export?"+JsonToUrl(queryParams));
        return false;
    });

    $('#btn_video_open').click(function () {
        parent.tab.tabAdd({
            href: 'imageInfo/videoOpen.v2.html',
            icon: 'fa fa-calendar-times-o',
            title: '新建双录信息'
        });
    });
    $('#videoTable').on('click', '.view', function () {
        var id = $(this).attr('data-id');
        var data = $('#list').bootstrapTable('getData');
        var row = [];
        for (var i = 0; i < data.length; i++) {
            if (data[i].id + '' === id) {
                row = data[i];
                break;
            }
        }
        if (id==null) {
            layerTips.msg('视频信息获取失败！');
            return false;
        }
        parent.tab.tabAdd({
            title: '双录信息详情',
            href: '../ui/imageInfo/videoOpen.v2.html?type=view&id='+id
        });
        return false;
    });

    /**
     * 从预约成功页面拿过来，账户开户使用
     */
    $('#btn_xkh').on('click', function () {
        if (apply.select(layer)) {
            var name = apply.currentItem.depositorName;
            var type = apply.currentItem.acctType;
            var applyid = apply.currentItem.applyid==null?"":apply.currentItem.applyid;
            var source = apply.currentItem.source;
            var params = 'billType=ACCT_OPEN&applyid='+applyid+'&buttonType=saicValidate'+'&name='+encodeURI(name)+'&type='+type;
            // var params = '';

            //TODO 其他情况可能也不允许开户操作
            if(source==null||source=='apply'||source=='account'){
                layerTips.msg("不支持开户");
                return false;
            }

            if(type === 'jiben'){
                typeCode = '基本存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/jibenOpen.html?'+params
                });
            } else if (type === 'yiban'){
                typeCode  = '一般存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/yibanOpen.html?'+params
                });
            } else if (type === 'yusuan'){
                typeCode  = '预算单位专用存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/yusuanOpen.html?'+params
                });
            }  else if (type === 'feiyusuan'){
                typeCode  = '非预算单位专用存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/feiyusuanOpen.html?'+params
                });
            }  else if (type === 'linshi'){
                typeCode  = '临时机构临时存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/linshiOpen.html?'+params
                });
            }  else if (type === 'feilinshi'){
                typeCode  = '非临时机构临时存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/feilinshiOpen.html?'+params
                });
            }  else if (type === 'teshu'){
                typeCode  = '特殊单位专用存款账户';

                parent.tab.tabAdd({
                    title: typeCode+'-'+name,
                    href: 'accttype/teshuOpen.html?'+params
                });
            }else {
                layerTips.msg("开户类型不正确");
                return false;
            }
        }
    });

    /**
     * 从对公业务管理页面拿过来，账户开户使用
     */
    $('a[role="menuitem"]').on('click', function () {
        var type = $(this).attr("data-type");
        var href = ""
        if (type == "jiben" || type == "linshi" || type == "teshu") {
            href = "validate/detail.html?type=" + type;
        } else {
            href = "validate/detailPBOC.html?type=" + type;
        }
        parent.tab.tabAdd({
                title: '校验管理',
                href: href
            }
        );
        return false;
    });

});

function switchSource(value) {
    if (value==null||value==""){
        return '其他';
    } else if (value == "apply"){
        return '预约模块';
    } else if (value == "account"){
        return '账户模块';
    } else if (value == "image"){
        return '影像模块';
    }
}

function switchBusinessType(value) {
    if (value == "CORPORATE"){
        return '对公业务';
    } else if (value == "PERSONAL"){
        return '个人业务';
    } else {
        return '-';
    }
}

/**
 * -1： 未认证；0： 认证中；
 * 1： 认证通过；2： 认证不通过、3 无需认证
 * @param value
 * @returns {string}
 */
function switchFaceResult(value) {
    if (value == "-1"){
        return '未认证';
    } else if (value == "0"){
        return '认证中';
    } else if (value == "1"){
        return '认证通过';
    } else if (value == "2"){
        return '认证不通过';
    } else if (value == "3"){
        return '无需认证';
    } else {
        return '-';
    }
}

/**
 * -1： 未认证；0： 认证中；
 * 1： 认证通过；2： 认证不通过、3 无需认证
 * @param value
 * @returns {string}
 */
function switchRecordType(value) {
    if (value == "REMOTERECORD" || value == "remoteRecord"){
        return '远程双录';
    } else if (value == "LOCALUPLOAD" || value == "localUpload"){
        return '本地上传';
    } else if (value == "CLOSETRECORD" || value == "closetRecord"){
        return '临柜双录';
    } else {
        return '-';
    }
}