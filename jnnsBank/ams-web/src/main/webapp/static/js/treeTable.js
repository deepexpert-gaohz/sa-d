layui.define(['jquery', 'element'], function (exports) {
  var $ = layui.jquery,
  element = layui.element,
  treeTable = function () {
    this.config = {
      elem: '', //table选择器
      url: '', //请求地址
      fieldId: 'id', //ID字段
      fieldPid: 'pid', //父ID字段
      fieldName: 'name', //展开图标所在字段
      fieldRows: 'rows', //子元素字段
      fieldHasRows: 'hasRows', //是否含子元素字段
      cols: [], //{ field: 'name', title: '名称', width:80, align: 'right', headAlign:'center', render: function(value, rows){} }
	  head: true, //true: 需要设置表头，false: 不需要设置表头 cols里width、headAlign失效
	  handleSelected: null //选中一行，回调方法
    };
  };

  // 参数设置[options]
  treeTable.prototype.set = function (options) {
    if (typeof (options) == 'string' && options != "") {
      this.config.elem = options;
    } else if (typeof (options) == 'object') {
      $.extend(true, this.config, options);
    }
    return this;
  };

  // 初始化对象
  treeTable.prototype.init = function(options) {
    var that = this;
    that.set(options);

    var headHtml = '';
    if (that.config.head)  {
      headHtml = that.getHeadHtml();
      var html = '<thead>' + headHtml + '</thead><tbody></tbody>';
      $(that.config.elem).html(html);
    } else {
      $(that.config.elem + ' tbody').length || $(that.config.elem).append('<tbody></tbody>');
    }

    $.get(that.config.url, function(data){
      that.addTreeChilds(data[that.config.fieldPid], 0, data);
    });
    return this;
  };

  treeTable.prototype.refresh = function(options) {
    var that = this;
    that.set(options);
    $(that.config.elem + ' tbody').html('');

    $.get(that.config.url, function(data){
      that.addTreeChilds(data[that.config.fieldPid], 0, data);
    });
  }
  
  // 触发事件
	treeTable.prototype.openTree = function (othis) {
    var that = this;
    var elemTr = othis.parents('tr');
    var hasChids =  elemTr.attr('data-childs');
    if (hasChids) {

      var pid = elemTr.attr('data-id');
      var elemChilds = $('tr[data-pid="' + pid + '"]');
    
      if (elemChilds && elemChilds.length > 0 ){
        that.openTreeChilds(pid);
      } else {
        if (elemTr.hasClass('loading')) {
          return false;
        }
        elemTr.addClass('loading');
        
        var level = elemTr.attr('data-level');
        level++;

        //需删除以下两行代码
        // that.config.url = '../ag/json/treeTable11.json';
        // if(pid == 12) that.config.url = '../ag/json/treeTable12.json';

          var param;
          if(that.config.url.indexOf("?")!=-1) {
              param = '&pid=' + pid;
          } else {
              param = '?pid=' + pid;
          }

        $.get(that.config.url + param, function(data){
          that.addTreeChilds(data[that.config.fieldPid], level, data);
          elemTr.removeClass('loading');
        });
      }
    }
  };
  
  treeTable.prototype.openTreeChilds = function (pid) {
    var that = this;
    var elem = $('tr[data-id="' + pid + '"]');
    var childs = $('tr[data-pid="' + pid + '"]');
    if(elem.hasClass("child-hide")) {
      childs.removeClass('layui-hide');
      elem.removeClass('child-hide').find('i.layui-tree-spread').text('');
    } else {
      that.closeTreeChilds(pid);
    }
  }

  treeTable.prototype.closeTreeChilds = function (pid) {
    var that = this;
    var elem = $('tr[data-id="' + pid + '"]');
    var childs = $('tr[data-pid="' + pid + '"]');
    elem.addClass('child-hide').find('i.layui-tree-spread').text('');
    if(childs && childs.length > 0) {
      childs.each(function(){
        var elem = $(this);
        elem.addClass('layui-hide');
        var id = elem.attr('data-id');
        that.closeTreeChilds(id);
      });
    }
  }

  treeTable.prototype.addTreeChilds = function (pid, childlevel, childsData) {
    var that = this;

    if(childsData && childsData[that.config.fieldRows] && childsData[that.config.fieldRows].length > 0) {
      
      var childsHtml = that.getChildsHtml(pid, childlevel, childsData);
      
      if (pid>0) $(this.config.elem + ' tbody tr[data-id="' + pid + '"]').after(childsHtml).removeClass('child-hide').find('i.layui-tree-spread').text('');
	  else $(this.config.elem + ' tbody').append(childsHtml);
	  
	  if(that.config.handleSelected) {
		$(this.config.elem + ' tbody tr').off().on('click', function () {
			var elem = $(this);
			var id = elem.attr('data-id');
			that.config.handleSelected(id);
			elem.addClass('active').siblings().removeClass('active');
			return false;
		});
	  }

      $(this.config.elem + ' i.layui-tree-spread').off().on('click', function () {
		that.openTree($(this));
		return false;
      });

      childlevel++;
      for (var i = 0; i < childsData[that.config.fieldRows].length; i++) {
        var item = childsData[that.config.fieldRows][i];
        if (item[that.config.fieldHasRows] && item[that.config.fieldRows]) {
          that.addTreeChilds(item[that.config.fieldId], childlevel, item);
        }
      }
    }
  }

  treeTable.prototype.getHeadHtml = function () {
    var headerHtml = '<tr>';

    $.each(this.config.cols,function(index,col){

      var widthHtml = '';
        if (col.width) widthHtml = ' width="' + col.width + '"';

        var alignHtml = '';
        if (col.align) alignHtml = ' style="text-align:' + col.align + '"';
        if (col.headAlign) alignHtml = ' style="text-align:' + col.headAlign + '"'


      headerHtml += '<th' + widthHtml + alignHtml + '>' + col.title + '</th>';
    });

    headerHtml += '</tr>';
    return headerHtml;
  }

  treeTable.prototype.getChildsHtml = function (pid, childlevel, childsData) {
    var that = this;
    var childsHtml = '';
    for (var i = 0; i < childsData[that.config.fieldRows].length; i++) {
      var item = childsData[that.config.fieldRows][i];
      childsHtml += '<tr'
      if (item[that.config.fieldHasRows]) {
        childsHtml += ' data-childs="true"';
        if (!item[that.config.fieldRows]) childsHtml += ' class="childs-hide"';
      }
      childsHtml += ' data-id="' + item[that.config.fieldId] + '" data-pid="' + pid + '" data-level="' + childlevel + '">'

      $.each(this.config.cols,function(index,col){

        if(that.config.fieldName == col.field) {
          childsHtml += '<td>'
          for (var j = 0; j < childlevel; j++) {
            childsHtml += '<span style="display: inline-block;width: 20px;"></span>'
          }
          if (item[that.config.fieldHasRows]) {
            if (!item[that.config.fieldRows]) childsHtml += '<i class="layui-icon layui-tree-spread" style="cursor: pointer;"></i>'
            else childsHtml += '<i class="layui-icon layui-tree-spread" style="cursor: pointer;"></i>'
          }
          if(col.render) {
            childsHtml += col.render(item[col.field], item);
          } else {
            childsHtml += item[col.field];
          }
          childsHtml += '</td>';
        } else {
          var alignHtml = '';
          if (col.align) alignHtml = ' style="text-align:' + col.align + '"';

          if(col.render) {
            childsHtml += '<td' + alignHtml + '>' + col.render(item[col.field], item) + '</td>';
          } else { 
            childsHtml += '<td' + alignHtml + '>' + item[col.field] + '</td>';
          }
        }
      });

      childsHtml += '</tr>';
    }
    return childsHtml;
  }

  exports('treeTable', function (options) {
    var t = new treeTable();
    return t.init(options);
  });
});