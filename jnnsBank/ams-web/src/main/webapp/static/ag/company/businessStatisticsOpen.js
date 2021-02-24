// var list = {
//   depositorTypeList: [
//     { value: '01', text: '企业法人', checked: true, isPbc: true },
//     { value: '02', text: '非法人企业', checked: true, isPbc: true },
//     { value: '03', text: '机关', checked: true },
//     { value: '04', text: '实行预算管理的事业单位', checked: true },
//     { value: '05', text: '非预算管理的事业单位', checked: true },
//     { value: '06', text: '团级(含)以上军队及分散执勤的支(分)队', checked: true },
//     { value: '07', text: '团级(含)以上武警部队及分散执勤的支(分)队', checked: true },
//     { value: '08', text: '社会团体', checked: true },
//     { value: '09', text: '宗教组织', checked: true },
//     { value: '10', text: '民办非企业组织', checked: true },
//     { value: '11', text: '外地常设机构', checked: true },
//     { value: '12', text: '外国驻华机构', checked: true },
//     { value: '13', text: '有字号的个体工商户', checked: true, isPbc: true },
//     { value: '14', text: '无字号的个体工商户', checked: true, isPbc: true },
//     { value: '15', text: '居民委员会、村民委员会、社区委员会', checked: true },
//     { value: '16', text: '单位设立的独立核算的附属机构', checked: true },
//     { value: '17', text: '其他组织', checked: true },
//     { value: '20', text: '境外组织', checked: true },
//     { value: '50', text: 'QFII', checked: true },
//     { value: '51', text: '境外贸易机构', checked: true },
//     { value: '52', text: '跨境清算', checked: true }
//   ],
//   acctTypeList: [
//     { value: 'jiben', text: '基本存款账户', checked: true, isPbc: true },
//     { value: 'yiban', text: '一般存款账户', checked: true },
//     { value: 'yusuan', text: '预算单位专用存款账户', checked: true },
//     { value: 'feiyusuan', text: '非预算单位专用存款账户', checked: true },
//     { value: 'teshu', text: '特殊单位专用存款账户', checked: true },
//     { value: 'linshi', text: '临时机构临时存款账户', checked: true },
//     { value: 'feilinshi', text: '非临时机构临时存款账户', checked: true, isPbc: true }
//   ]
// };
var list = {
    depositorTypeList: [],//存款人类别选项集合
    acctTypeList: []//账户类型选项集合
};
// 初始化存款人类别和账户类型多选框中的数据
$.ajax({
    url: '../../businessStatistics/open/getAcctTypeDepositorType',
    type: 'post',
    data: {},
    dataType: "json",
    async: false,
    success: function (res) {
        list.depositorTypeList = res.depositorTypeList;
        list.acctTypeList = res.acctTypeList;
    }
});

layui.config({
  base: '../js/'
});
layui.define(['jquery', 'element', 'layer', 'laydate', 'treeTable', 'form', 'laytpl'], function (exports) {
  var $ = layui.jquery,
      element = layui.element,
      layer = layui.layer,
      laydate = layui.laydate,
      form = layui.form,
      laytpl = layui.laytpl;

  //初始化日期控件
  var laydateArr = beginEndDateInit(laydate, 'createddatestart', 'createddateend', 'YYYY-MM-DD hh:mm:ss', true);

  //选择存款人类别
  $('#btn_depositorType').on('click', function () {
    var content = getChooseHtml('depositorType', list.depositorTypeList);
    layer.open({
      type: 1,
      title: '选择存款人类型',
      content: content,
      btn: ['确定', '重置', '取消'],
      shade: false,
      offset: '20px',
      area: '600px',
      maxmin: true,
      yes: function(index, layero){
        for (var i = 0; i < list.depositorTypeList.length; i++) {
          var item = list.depositorTypeList[i];
          item.checked = $('#depositorType' + item.value).prop('checked');
        }
        layer.close(index);
      },
      btn2: function(index, layero){
        var form = layui.form();
        for (var i = 0; i < list.depositorTypeList.length; i++) {
          var item = list.depositorTypeList[i];
          $('#depositorType' + item.value).prop('checked', false);
        }
        form.render();
        return false
      },
      success: function (layero, index) {
        var form = layui.form();
        form.render();
      }
    });
  });

  //选择账户性质
  $('#btn_acctType').on('click', function () {
    var content = getChooseHtml('acctType', list.acctTypeList);
    layer.open({
      type: 1,
      title: '选择存款人类型',
      content: content,
      btn: ['确定', '重置', '取消'],
      shade: false,
      offset: '20px',
      area: '600px',
      maxmin: true,
      yes: function(index, layero){
        for (var i = 0; i < list.acctTypeList.length; i++) {
          var item = list.acctTypeList[i];
          item.checked = $('#acctType' + item.value).prop('checked');
        }
        layer.close(index);
      },
      btn2: function(index, layero){
        var form = layui.form();
        for (var i = 0; i < list.acctTypeList.length; i++) {
          var item = list.acctTypeList[i];
          $('#acctType' + item.value).prop('checked', false);
        }
        form.render();
        return false
      },
      success: function (layero, index) {
        var form = layui.form();
        form.render();
      }
    });
  });

  //查询
  $('#btn_query').on('click', function () {
    var arrDepositorType = getDepositorType();
    if(arrDepositorType.length < 1) {
      layer.msg('请选择存款人类别');
      return false;
    }

    var arrAcctType = getAcctType();
    if(arrAcctType.length < 1) {
      layer.msg('请选择账户性质');
      return false;
    }

    var parameter = {
        depositorTypeList: arrDepositorType,
        acctTypeList: arrAcctType,
        beginDate: $('#createddatestart').val(),
        endDate: $('#createddateend').val()
    };
    $.ajax({
        url: '../../businessStatistics/open/list',
        type: 'post',
        data: parameter,
        dataType: "json",
        success: function (res) {
            viewList(res, parameter);
        }
    });

  });

    //人行上报数据统计
  $('#btn_query_pbc').on('click', function () {

    for (var i = 0; i < list.depositorTypeList.length; i++) {
      var item = list.depositorTypeList[i];
      if(item.isPbc) {
        item.checked = true;
      } else {
        item.checked = false;
      }
    }

    for (var i = 0; i < list.acctTypeList.length; i++) {
      var item = list.acctTypeList[i];
      if(item.isPbc) {
        item.checked = true;
      } else {
        item.checked = false;
      }
    }

    $('#btn_query').click();
  });

  //导出excel
  $('#btn_export').on('click', function () {
    var arrDepositorType = getDepositorType();
    if(arrDepositorType.length < 1) {
        layer.msg('请选择存款人类别');
        return false;
    }

    var arrAcctType = getAcctType();
    if(arrAcctType.length < 1) {
        layer.msg('请选择账户性质');
        return false;
    }

    var url = '../../businessStatistics/open/exportXLS?';
    var beginDate = $('#createddatestart').val();
    var endDate = $('#createddateend').val();
    if (beginDate !== '') {
      url += 'beginDate=' + beginDate;
    }
    if (endDate !== '') {
      url += 'endDate=' + endDate;
    }
    for (var i = 0; i < arrDepositorType.length; i++) {
      url += '&depositorTypeList=' + arrDepositorType[i];
    }
    for (var i = 0; i < arrAcctType.length; i++) {
      url += '&acctTypeList=' + arrAcctType[i];
    }
    $("#downloadFrame").prop('src', url);
  });

  //初始化表格数据
  $('#btn_query').click();



  //初始化多选弹出框内容
  function getChooseHtml(name, list) {
    var html = '<div style="margin: 15px;"><form class="layui-form" id="form_choose" action="">';

    for (var i = 0; i < list.length; i++) {
      var item = list[i];
      if(item.checked) {
        html += '<input type="checkbox" id="' + name + item.value + '" name="' + name + '" lay-skin="primary" value="' + item.value + '" title="' + item.text + '" checked></input>';
      } else {
        html += '<input type="checkbox" id="' + name + item.value + '" name="' + name + '" lay-skin="primary" value="' + item.value + '" title="' + item.text + '"></input>';
      }
      
    }
    return html + '</form></div>';
  }

  //初始化表格
  function viewList(data, parameter) {
    var jbCount = 0;
    var ybCount = 0;
    var zyCount = 0;
    var lsCount = 0;
    for (var i = 0; i < list.acctTypeList.length; i++) {
      var item = list.acctTypeList[i];
      if (item.checked){
        if(item.text == '基本存款账户') {
          jbCount++;
        } else if(item.text == '一般存款账户') {
          ybCount++;
        } else if(item.text == '预算单位专用存款账户' || item.text == '非预算单位专用存款账户' || item.text == '特殊单位专用存款账户') {
          zyCount++;
        } else if(item.text == '临时机构临时存款账户' || item.text == '非临时机构临时存款账户') {
          lsCount++;
        }
      }
    }

    var listHtml = '';
    listHtml += '<thead><tr><th rowspan="2" colspan="2">存款人类别</th>';

    jbCount && (listHtml += '<th rowspan="2">基本存款账户</th>');
    ybCount && (listHtml += '<th rowspan="2">一般存款账户</th>');
    zyCount && (listHtml += '<th colspan="' + zyCount + '">专用存款账户</th>');
    lsCount && (listHtml += '<th colspan="' + lsCount + '">临时存款账户</th>');
    listHtml += '</tr>';

    if(zyCount + lsCount) {
      listHtml += '<tr>';
      for (var i = 0; i < list.acctTypeList.length; i++) {
        var item = list.acctTypeList[i];
        if (item.checked){
          if(item.text == '预算单位专用存款账户' || item.text == '非预算单位专用存款账户' || item.text == '特殊单位专用存款账户' 
          || item.text == '临时机构临时存款账户' || item.text == '非临时机构临时存款账户') {
            listHtml += '<th>' + item.text + '</th>';
          }
        }
      }
      listHtml += '</tr>';
    }

    listHtml += '</thead>';

    listHtml += '<tbody>';


    var gtCount = 0;
    for (var i = 0; i < list.depositorTypeList.length; i++) {
      var item = list.depositorTypeList[i];
      if (item.checked){
        if(item.text == '有字号的个体工商户' || item.text == '无字号的个体工商户') {
          gtCount++;
        }
      }
    }

    for (var i = 0; i < list.depositorTypeList.length; i++) {

      var depositorType = list.depositorTypeList[i];
      if (depositorType.checked){

        if(depositorType.text == '有字号的个体工商户'){
          listHtml += '<tr><th rowspan="' + gtCount + '">个体工商户</th><th>有字号的个体工商户</th>';
        } else if(depositorType.text == '无字号的个体工商户') {
          if(gtCount == 1) {
            listHtml += '<tr><th>个体工商户</th><th>无字号的个体工商户</th>';
          } else {
            listHtml += '<tr><th>无字号的个体工商户</th>';
          }
        } else {
          listHtml += '<tr><th colspan="2">' + depositorType.text + '</th>';
        }
        

        for (var j = 0; j < list.acctTypeList.length; j++) {
          var acctType = list.acctTypeList[j];
          if (acctType.checked){
            listHtml += '<td id="' + acctType.value + depositorType.value + '">-</td>'
          }
        }
        listHtml += '</tr>';
      }
    }
    //添加合计行
    listHtml += '<tr><th colspan="2">合计</th>';
    for (var j = 0; j < list.acctTypeList.length; j++) {
        var acctType = list.acctTypeList[j];
        if (acctType.checked){
            listHtml += '<td id="' + acctType.value + 'count">-</td>'
        }
    }
    listHtml += '</tr>';

    listHtml += '</tbody>';
    $('#list').html(listHtml);

    for (var i = 0; i < data.length; i++) {
      var item = data[i];
      if (item.value === 0) {
        $('#' + item.acctType + item.depositorType).text(0);
      } else {
        var dataUrl = '?depositorType=' + encodeURI(item.depositorType)
            + '&acctType=' + encodeURI(item.acctType)
            + '&beginDate=' + encodeURI(parameter.beginDate)
            + '&endDate=' + encodeURI(parameter.endDate);
        $('#' + item.acctType + item.depositorType).html('<a href="javascript:numRenderTo(\''+dataUrl+'\')" style="color: blue;" class="numRenderClass blue">'+ item.value +'</ a>');
      }
    }
  }
  
  function getDepositorType() {
      var arrDepositorType = [];
      for (var i = 0; i < list.depositorTypeList.length; i++) {
          var item = list.depositorTypeList[i];
          item.checked && arrDepositorType.push(item.value);
      }
      return arrDepositorType;
  }
  function getAcctType() {
      var arrAcctType = [];
      for (var i = 0; i < list.acctTypeList.length; i++) {
          var item = list.acctTypeList[i];
          item.checked && arrAcctType.push(item.value);
      }
      return arrAcctType;
  }

  numRenderTo = function (param){
    parent.tab.tabAdd({
        href: 'company/statisticsOpenAccount.html'+param,
        icon: 'fa fa-calendar-plus-o',
        title: '账户开户统计明细列表'
    });
  }
});