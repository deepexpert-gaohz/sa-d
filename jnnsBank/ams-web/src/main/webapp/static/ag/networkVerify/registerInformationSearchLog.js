
layui.config({
     base: '../js/' //假设这是你存放拓展模块的根目录
});

var list = {
    baseUrl: "../../networkVerify/registerInformation",
    entity: "registerInformationLog",
    tableId: "list",
    toolbarId: "toolbar",
    unique: "id",
    order: "asc",
    currentItem: {}
};
list.columns = function () {
    return [{
        field: 'selectItem',
        radio: true
    },{
        field: 'id',
        title: 'ID编号'
    }, {
        field: 'entNm',
        title: '企业名称'
    },{
        field: 'traNm',
        title: '字号名称'
    }, {
        field: 'uniSocCdtCd',
        title: '统一社会信用代码'
    }, {
        field: 'orgName',
        title: '机构名称'
    }, {
        field: 'createdUserName',
        title: '操作员'
    }, {
        field: 'createTime',
        title: '操作时间'
    },{
        field: 'id',
        title: '操作',
        formatter: function (value, row, index) {
            return '<a class="view" style="color: blue" href="#" data-id="'+value+'">查看详情</a>';
        }
    }];
};

list.queryParams = function (params) {
    if (!params)
        return {
            entNm: $.trim($("#entNm").val()),
            traNm: $.trim($("#traNm").val()),
            uniSocCdtCd: $.trim($("#uniSocCdtCd").val()),
            beginDate: $.trim($("#beginDate").val()),
            endDate: $.trim($("#endDate").val()),
            orgName: $.trim($("#orgName").val()),
            createdUserName: $.trim($("#createdUserName").val())
        };
    var temp = { //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        size: params.limit, //页面大小
        page: params.offset / params.limit, //页码
        entNm: $.trim($("#entNm").val()),
        traNm: $.trim($("#traNm").val()),
        uniSocCdtCd: $.trim($("#uniSocCdtCd").val()),
        beginDate: $.trim($("#beginDate").val()),
        endDate: $.trim($("#endDate").val()),
        orgName: $.trim($("#orgName").val()),
        createdUserName: $.trim($("#createdUserName").val())
    };
    return temp;
};

list.init = function () {

    list.table = $('#' + list.tableId).bootstrapTable({
        url: list.baseUrl + '/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        toolbar: '#' + list.toolbarId, //工具按钮用哪个容器
        striped: true, //是否显示行间隔色
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, //是否显示分页（*）
        sortable: false, //是否启用排序
        sortOrder: list.order, //排序方式
        queryParams: list.queryParams,//传递参数（*）
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
        uniqueId: list.unique, //每一行的唯一标识，一般为主键列
        showToggle: true, //是否显示详细视图和列表视图的切换按钮
        cardView: false, //是否显示详细视图
        detailView: false, //是否显示父子表
        columns: list.columns(),
        responseHandler: function (res) {
            return {total:res.total, rows: res.rows};
        }
        ,onLoadError: function(status){
            ajaxError(status);
        }
    });
};

list.select = function (layerTips) {
    var rows = list.table.bootstrapTable('getSelections');
    if (rows.length == 1) {
        list.currentItem = rows[0];
        return true;
    } else {
        layerTips.msg("请选中一行");
        return false;
    }
};

layui.use(['form', 'layedit', 'laydate', 'loading', 'laytpl'], function () {
    list.init();

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
        layer = layui.layer, //获取当前窗口的layer对象
        form = layui.form(),
        layedit = layui.layedit,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        loading = layui.loading;

    //初始化日期控件
    var laydateArr =  beginEndDateInit(laydate, 'beginDate', 'endDate', 'YYYY-MM-DD hh:mm:ss', true);

    //重置按钮
    $('button[type="reset"]').click(function () {
        for (var i = 0; i < laydateArr.length; i++) {
            delete laydateArr[i].max;
            delete laydateArr[i].min;
        }
    });

    var addBoxIndex = -1;

    $('.date-picker').change(function(){
        var value = $(this).val();
        $(this).val(dateFormat(value));
    });

    //查询
    $('#btn_query').on('click', function () {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        if(beginDate && endDate && beginDate > endDate) {
            layerTips.msg("时间筛选开始时间不能大于结束时间");
        } else {
            list.table.bootstrapTable('refresh', list.queryParams());
        }
    });

    //新建查询
    $('#btn_add').on('click', function () {
        parent.tab.tabAdd({
            title: "登记信息联网核查申请",
            href: 'networkVerify/registerInformation.html'
        });
    });

    //疑义反馈
    $('#btn_feedback').on('click', function () {
        if (list.select(layerTips)) {
            var id = list.currentItem.id;
            parent.tab.tabAdd({
                title: "登记信息疑义反馈申请",
                href: 'networkVerify/feedback.html?id='+id+'&type=registerInformation'
            });
        }
    });

    //反馈记录
    $('#btn_feedback_log').on('click', function () {
        parent.tab.tabAdd({
            title: "登记信息疑义反馈记录",
            href: 'networkVerify/feedbackLog.html?type=registerInformation'
        });
    });

    //查看详情点击事件
    $('#list').on('click','.view', function () {
        var Id = $(this).attr('data-id');
        if (Id && Id !== '') {
            showTableHTML(Id);
        } else {
            layer.alert('该条记录没有登记信息联网核查数据', {
                title: "提示信息",
                closeBtn: 0
            });
        }
        return false;
    });


    //展示详情table页
    function showTableHTML(id) {
        $.get(list.baseUrl+'/' + id, function (res) {
            if(res.code=="NACK"){
                layer.alert('登记信息联网核查详情获取异常', {
                    title: "提示信息",
                    closeBtn: 0
                });
                return;
            }
            var data = res.data;

            html = '<div style="margin: 10px"><table class="table table-bordered word-break"><tbody>';

            //1、请求信息
            html += '<tr><td class="td-label" colspan="4" align="center">请求信息</td></tr>';
            if (data.entNm!=null){//企业
                html += '<tr>\n' +
                    '<td class="td-label">企业名称：</td>\n' +
                    '<td class="td-label-right">'+(data.entNm||'')+'</td>\n' +
                    '<td class="td-label">统一社会信用代码：</td>\n' +
                    '<td class="td-label-right">'+(data.uniSocCdtCd||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">法定代表人或单位负责人姓名：</td>\n' +
                    '<td class="td-label-right">'+(data.nmOfLglPrsn||'')+'</td>\n' +
                    '<td class="td-label">法定代表人或单位负责人身份证件号：</td>\n' +
                    '<td class="td-label-right">'+(data.idOfLglPrsn||'')+'</td>\n' +
                    '</tr>\n';
            } else {//个体
                html += '<tr>\n' +
                    '<td class="td-label">字号名称：</td>\n' +
                    '<td class="td-label-right">'+(data.traNm||'')+'</td>\n' +
                    '<td class="td-label">统一社会信用代码：</td>\n' +
                    '<td class="td-label-right">'+(data.uniSocCdtCd||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">经营者姓名：</td>\n' +
                    '<td class="td-label-right">'+(data.nm||'')+'</td>\n' +
                    '<td class="td-label">经营者证件号：</td>\n' +
                    '<td class="td-label-right">'+(data.managerId||'')+'</td>\n' +
                    '</tr>\n';
            }
            //公共
            html += '<tr>\n' +
                '<td class="td-label">代理人姓名：</td>\n' +
                '<td class="td-label-right">'+(data.agtNm||'')+'</td>\n' +
                '<td class="td-label">代理人身份证件号码：</td>\n' +
                '<td class="td-label-right">'+(data.agtId||'')+'</td>\n' +
                '</tr>\n' +
                '<tr>\n' +
                '<td class="td-label">操作员姓名：</td>\n' +
                '<td class="td-label-right">'+(data.opNm||'')+'</td>\n' +
                '</tr>\n';


            //2、应答信息
            if (data.rslt!=null) {
                html += '<tr><td class="td-label" align="center" colspan="4">应答信息</td></tr>' +
                    '<tr>\n' +
                    '<td class="td-label">登记信息核查结果：</td>\n' +
                    '<td class="td-label-right" id="rslt">'+registerRsltSwitch(data.rslt)+'</td>\n' +
                    '<td class="td-label">数据源日期：</td>\n' +
                    '<td class="td-label-right"id="dataResrcDt">'+data.dataResrcDt+'</td>\n' +
                    '</tr>';
                if (data.basicInformationOfEnterpriseDto!=null){//企业照面
                    var dto = data.basicInformationOfEnterpriseDto;
                    html += '<tr><td class="td-label" align="center" colspan="4">企业照面信息部分</td></tr>';
                    html += '<tr>\n' +
                        '<td class="td-label">企业名称：</td>\n' +
                        '<td class="td-label-right">'+(dto.entNm || '')+'</td>\n' +
                        '<td class="td-label">统一社会信用代码：</td>\n' +
                        '<td class="td-label-right">'+(dto.uniSocCdtCd || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">市场主体类型：</td>\n' +
                        '<td class="td-label-right">'+(dto.coTp || '')+'</td>\n' +
                        '<td class="td-label">住所：</td>\n' +
                        '<td class="td-label-right">'+(dto.dom || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">注册资本(金)：</td>\n' +
                        '<td class="td-label-right">'+(dto.regCptl || '')+'</td>\n' +
                        '<td class="td-label">成立日期：</td>\n' +
                        '<td class="td-label-right">'+(dto.dtEst || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">登记状态：</td>\n' +
                        '<td class="td-label-right">'+(dto.regSts||'')+'</td>\n' +
                        '<td class="td-label">法定代表人或单位负责人姓名：</td>\n' +
                        '<td class="td-label-right">'+(dto.nmOfLglPrsn || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">登记机关：</td>\n' +
                        '<td class="td-label-right">'+(dto.regAuth || '')+'</td>\n' +
                        '<td class="td-label">经营范围：</td>\n' +
                        '<td class="td-label-right">'+(dto.bizScp || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">核准日期：</td>\n' +
                        '<td class="td-label-right">'+(dto.dtAppr || '')+'</td>\n' +
                        '</tr>\n';

                } else if (data.basicInformationOfSelfEmployedPeopleDto!=null){//个体户照面
                    var dto = data.basicInformationOfSelfEmployedPeopleDto;
                    html += '<tr><td class="td-label" align="center" colspan="4">个体户照面信息部分</td></tr>';
                    html += '<tr>\n' +
                        '<td class="td-label">字号名称：</td>\n' +
                        '<td class="td-label-right">'+(dto.traNm || '')+'</td>\n' +
                        '<td class="td-label">统一社会信用代码：</td>\n' +
                        '<td class="td-label-right">'+(dto.uniSocCdtCd || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">市场主体类型：</td>\n' +
                        '<td class="td-label-right">'+(dto.coTp || '')+'</td>\n' +
                        '<td class="td-label">经营场所：</td>\n' +
                        '<td class="td-label-right">'+(dto.opLoc || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">资金数额：</td>\n' +
                        '<td class="td-label-right">'+(dto.fdAmt || '')+'</td>\n' +
                        '<td class="td-label">成立日期：</td>\n' +
                        '<td class="td-label-right">'+(dto.dtReg || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">登记状态：</td>\n' +
                        '<td class="td-label-right">'+(dto.regSts||'')+'</td>\n' +
                        '<td class="td-label">经营者姓名：</td>\n' +
                        '<td class="td-label-right">'+(dto.nm || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">登记机关：</td>\n' +
                        '<td class="td-label-right">'+(dto.regAuth || '')+'</td>\n' +
                        '<td class="td-label">经营范围：</td>\n' +
                        '<td class="td-label-right">'+(dto.bizScp || '')+'</td>\n' +
                        '</tr>\n';
                    html += '<tr>\n' +
                        '<td class="td-label">核准日期：</td>\n' +
                        '<td class="td-label-right">'+(dto.dtAppr || '')+'</td>\n' +
                        '</tr>\n';
                }

                if (data.companyShareholdersAndFundingInformationDtoList!=null){//企业股东及出资信息部分，属于企业登记信息核查内容。
                    var dtoList = data.companyShareholdersAndFundingInformationDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">企业股东及出资信息部分</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">自然人标识:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].natlPrsnFlag || '')+'</td>\n' +
                            '<td class="td-label">投资人名称:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].invtrNm || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">投资人证件号码或证件编号:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].invtrId || '')+'</td>\n' +
                            '<td class="td-label">认缴出资额['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConAmt || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">实缴出资额:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].actlCptlConAmt || '')+'</td>\n' +
                            '<td class="td-label">认缴出资方式['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConFm || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">认缴出资日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].subscrCptlConDt || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }

                if (data.directorSupervisorSeniorManagerInformationDtoList!=null){//董事监事及高管信息，属于企业登记信息核查内容。
                    var dtoList = data.directorSupervisorSeniorManagerInformationDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">董事监事及高管信息部分</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">姓名:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].nm || '')+'</td>\n' +
                            '<td class="td-label">职务:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].posn || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }

                if (data.changeInformationDtoList!=null){//历史变更信息，属于企业/个体户登记信息核查内容。
                    var dtoList = data.changeInformationDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">历史变更信息</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">变更事项:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].chngItm || '')+'</td>\n' +
                            '<td class="td-label">变更日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].dtOfChng || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">变更前内容:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].bfChng || '')+'</td>\n' +
                            '<td class="td-label">变更后内容['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].aftChng || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }

                if (data.abnormalBusinessInformationDtoList!=null){//异常经营信息，属于企业登记信息核查内容。
                    var dtoList = data.abnormalBusinessInformationDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">异常经营信息</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">列入经营异常名录原因类型:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].abnmlCause || '')+'</td>\n' +
                            '<td class="td-label">列入日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].abnmlDate || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">列入决定机关:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].abnmlCauseDcsnAuth || '')+'</td>\n' +
                            '<td class="td-label">移出经营异常名录原因['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvCause || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">移出日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvDate || '')+'</td>\n' +
                            '<td class="td-label">移出决定机关['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvCauseDcsnAuth || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }

                if (data.illegalAndDiscreditInformationDtoList!=null){//严重违法失信信息，属于企业登记信息核查内容。
                    var dtoList = data.illegalAndDiscreditInformationDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">严重违法失信信息</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">列入事由或情形:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].illDscrtCause || '')+'</td>\n' +
                            '<td class="td-label">列入日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].abnmlDate || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">列入决定机关:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].abnmlCauseDcsnAuth || '')+'</td>\n' +
                            '<td class="td-label">移出事由['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvCause || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">移出日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvDate || '')+'</td>\n' +
                            '<td class="td-label">移出决定机关['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rmvCauseDcsnAuth || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }

                if (data.licenseNullifyDtoList!=null){//营业执照作废声明，属于企业登记信息核查内容。
                    var dtoList = data.licenseNullifyDtoList;
                    html += '<tr><td class="td-label" align="center" colspan="4">营业执照作废声明</td></tr>';
                    for (var i = 1;i<=dtoList.length;i++){
                        html += '<tr>\n' +
                            '<td class="td-label">正副本标识:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(registerOrgnlOrCpSwitch(dtoList[i-1].orgnlOrCp) || '')+'</td>\n' +
                            '<td class="td-label">声明内容:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].licNullStmCntt || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">声明日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].licNullStmDt || '')+'</td>\n' +
                            '<td class="td-label">补领标识['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(registerRplStsSwitch(dtoList[i-1].rplSts) || '')+'</td>\n' +
                            '</tr>\n' ;
                        html += '<tr>\n' +
                            '<td class="td-label">补领日期:['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].rplDt || '')+'</td>\n' +
                            '<td class="td-label">营业执照副本编号['+i+']：</td>\n' +
                            '<td class="td-label-right">'+(dtoList[i-1].licCpNb || '')+'</td>\n' +
                            '</tr>\n' ;
                    }
                }
            }else if (data.procSts!=null){//失败
                html += '<tr><td class="td-label" align="center" colspan="4">应答信息</td></tr>'+
                    '<tr>\n' +
                    '<td class="td-label">申请报文拒绝状态：</td>\n' +
                    '<td class="td-label-right" id="procSts">'+(procStsSwitch(data.procSts)||'')+'</td>\n' +
                    '<td class="td-label">申请报文拒绝码：</td>\n' +
                    '<td class="td-label-right" id="procCd">'+(data.procCd||'')+'</td>\n' +
                    '</tr>\n' +
                    '<tr>\n' +
                    '<td class="td-label">申请报文拒绝信息：</td>\n' +
                    '<td class="td-label-right" colspan="3" id="rjctInf">'+(data.rjctInf||'')+'</td>\n' +
                    '</tr>\n';
            }else {
                html += '<tr><td class="td-label" colspan="4" align="center">应答信息</td></tr>';
                html += '<tr><td class="td-label-right" colspan="4" align="center">无结果</td></tr>';
            }

            html += '</tbody></table></div>';

            layer.open({
                type: 1,
                area: '800px',
                title: '登记信息联网核查详情',
                content: html
            });
        });
    }
});
