function TreeTable(_options) {
    this.options = {
        elem: '', //table选择器
        url: '', //请求地址
        data: null, //tree数据
        getChildsData: null, //获得子数据回调方法
        head: true, //true: 需要设置表头，false: 不需要设置表头
        columns: [], //{ field: 'name', title: '名称', width:80, align: 'right', headAlign:'center', formatter: function(value, row){} }
        hasCheck: false, //是否显示勾选框
        hasSelected: true, //是否可选中项
        selectedParent: true, //false: 不能选择父类
        selectedId: null,
        selectedName: null,
        handleSelected: null, //选中一行，回调方法
        bindEvents: null, //处理事件
        queryParams: function (pid) { //请求参数
            var params = {}
            if (pid) {
                params.pid = pid;
            }
            return params;
        },
        responseHandler: function (res) { //数据处理
            return res;
        },
        itemHandler: function (item, parent) { //节点数据处理
            return {
                id: item.id, //ID字段
                pid: item.pid, //父ID字段
                name: item.name, //展开图标所在字段
                childs: item.childs, //子元素字段
                hasChilds: item.hasChilds, //是否含子元素字段
                checked: item.checked, //是否勾选字段
                disabled: item.disabled //不可操作字段
            }
        }
    };
    this.init(_options);
};

TreeTable.prototype = {
    // 初始化
    init: function (_options) {
        var that = this;
        that.set(_options);

        var headHtml = '';
        if (that.options.head) {
            headHtml = that.getHeadHtml();
            var html = '<thead>' + headHtml + '</thead><tbody></tbody>';
            $(that.options.elem).html(html);
        } else {
            $(that.options.elem + ' tbody').length || $(that.options.elem).append('<tbody></tbody>');
        }
        that.loading();
        that.viewData();
        return this;
    },
    //参数设置
    set: function (_options) {
        $.extend(true, this.options, _options);
        return this;
    },
    //刷新
    refresh: function (_options) {
        var that = this;
        that.set({
            selectedId: null,
            selectedName: null
        });
        that.set(_options);
        $(that.options.elem + ' tbody').html('');
        that.loading();
        that.viewData();
        return this;
    },
    //显示数据
    viewData: function () {
        var that = this;
        if(that.options.data) {
            that.addTreeChilds(null, 0, that.options.data);
        } else {
            var params = that.options.queryParams();
            $.ajax({
                url: that.options.url,
                type: 'GET',
                data: params,
                success: function (data, status) {
                    if (status == 'success') {
                        var list = that.options.responseHandler(data);
                        that.addTreeChilds(null, 0, list);
                    } else {
                        that.noRecords();
                    }
                },
                error: function () {
                    that.noRecords();
                }
            });
        }
    },
    //触发事件
    openTree: function (othis) {
        var that = this;
        var elemTr = othis.parents('tr');
        var hasChids = elemTr.attr('data-childs');
        if (hasChids) {

            var pid = elemTr.attr('data-id');
            var elemChilds = $(this.options.elem + ' tr[data-pid="' + pid + '"]');

            if (elemChilds && elemChilds.length > 0) {
                that.openTreeChilds(pid);
            } else {
                var level = elemTr.attr('data-level');
                level++;

                elemTr.find('i.js-unfold').removeClass('fa-plus-square-o').addClass('fa-spinner').addClass('fa-spin');
                if(that.options.data) {
                    that.options.getChildsData && that.options.getChildsData(pid, function (list) {
                        that.addTreeChilds(pid, level, list);
                        that.openTreeChilds(pid);
                    });

                } else {
                    var params = that.options.queryParams(pid);
                    
                    $.get(that.options.url, params, function (data) {
                        var list = that.options.responseHandler(data);
                        that.addTreeChilds(pid, level, list);
                        that.openTreeChilds(pid);
                    });
                }
            }
        }
    },
    openTreeChilds: function (pid) {
        var that = this;
        var elem = $(this.options.elem + ' tr[data-id="' + pid + '"]');
        var childs = $(this.options.elem + ' tr[data-pid="' + pid + '"]');
        if (elem.hasClass('child-hide')) {
            childs.removeClass('hidden');
            elem.removeClass('child-hide').find('i.js-unfold').removeClass('fa-spin').removeClass('fa-spinner').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        } else {
            that.closeTreeChilds(pid);
        }
    },
    closeTreeChilds: function (pid) {
        var that = this;
        var elem = $(this.options.elem + ' tr[data-id="' + pid + '"]');
        var childs = $(this.options.elem + ' tr[data-pid="' + pid + '"]');
        elem.addClass('child-hide').find('i.js-unfold').removeClass('fa-spin').removeClass('fa-spinner').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        if (childs && childs.length > 0) {
            childs.each(function () {
                var elem = $(this);
                elem.addClass('hidden');
                var id = elem.attr('data-id');
                that.closeTreeChilds(id);
            });
        }
    },
    addTreeChilds: function (pid, childlevel, childsData) {
        var that = this;

        if (childsData && childsData.length > 0) {

            var childsHtml = that.getChildsHtml(pid, childlevel, childsData);

            if (pid) {
                $(that.options.elem + ' tbody tr[data-id="' + pid + '"]').after(childsHtml);
            } else {
                if($(that.options.elem + ' tbody .loading').length > 0) {
                    $(that.options.elem + ' tbody').html('');
                }
                $(that.options.elem + ' tbody').append(childsHtml);
            }

            if (that.options.hasSelected) {
                $(that.options.elem + ' tbody tr').off().on('click', function () {
                    var elem = $(this);
                    var childs = elem.attr('data-childs');

                    if(!that.options.selectedParent && childs) { //设置不能选择父类
                        return false;
                    }

                    if (elem.hasClass('active')) {
                        that.options.selectedId = null;
                        that.options.selectedName = null;
                        elem.removeClass('active');
                    } else {
                        var id = elem.attr('data-id');
                        var name = elem.attr('data-name');
                        that.options.selectedId = id;
                        that.options.selectedName = name;
                        elem.addClass('active').siblings().removeClass('active');

                        that.options.handleSelected && that.options.handleSelected(id, name);
                    }
                });
            }

            $(that.options.elem + ' i.js-unfold').off().on('click', function () {
                that.openTree($(this));
                return false;
            });

            $(that.options.elem + ' i.js-check').off().on('click', function () {
                var elem = $(this);
                if (elem.hasClass('fa-check-square-o')) {
                    elem.removeClass('fa-check-square-o').addClass('fa-square-o');
                } else {
                    elem.removeClass('fa-square-o').addClass('fa-check-square-o');
                }
                return false;
            });

            that.options.bindEvents && that.options.bindEvents();

            if(that.options.url) {
                childlevel++;
                for (var i = 0; i < childsData.length; i++) {
                    var item = that.options.itemHandler(childsData[i]);
                    if (item.hasChilds && item.childs) {
                        that.addTreeChilds(item.id, childlevel, item.childs);
                        that.openTreeChilds(item.id);
                    }
                }
            }
        } else {
            if (childlevel == 0) {
                that.noRecords();
            }
        }
    },
    loading: function () {
        $(this.options.elem + ' tbody').html('<tr class="loading"><td colspan="' + this.options.columns.length + '"><i class="fa fa-spinner fa-spin"></i>加载中...</td></tr>');
    },
    noRecords: function () {
        $(this.options.elem + ' tbody').html('<tr class="no-records"><td colspan="' + this.options.columns.length + '">没有找到匹配的记录</td></tr>');
    },
    getHeadHtml: function () {
        var headerHtml = '<tr>';

        $.each(this.options.columns, function (index, row) {

            var widthHtml = '';
            if (row.width) widthHtml = ' width="' + row.width + '"';

            var alignHtml = '';
            if (row.align) alignHtml = ' style="text-align:' + row.align + '"';
            if (row.headAlign) alignHtml = ' style="text-align:' + row.headAlign + '"'

            headerHtml += '<th' + widthHtml + alignHtml + '>' + row.title + '</th>';
        });

        headerHtml += '</tr>';
        return headerHtml;
    },
    getChildsHtml: function (pid, childlevel, childsData) {
        var that = this;
        var childsHtml = '';
        for (var i = 0; i < childsData.length; i++) {
            var item = that.options.itemHandler(childsData[i]);

            childsHtml += '<tr data-id="' + item.id + '" data-name="' + item.name + '" data-pid="' + pid + '" data-level="' + childlevel + '"';

            if (item.hasChilds) {
                childsHtml += ' data-childs="true"';
            }
            if (pid) {
                childsHtml += ' class="child-hide hidden"';
            } else {
                childsHtml += ' class="child-hide"';
            }
            childsHtml += '>';

            $.each(this.options.columns, function (index, row) {

                if (row.field == 'name') {
                    childsHtml += '<td class="tree-table-name">';
                    for (var j = 0; j < childlevel; j++) {
                        childsHtml += '<span style="display: inline-block;width: 20px;"></span>';
                    }
                    if (item.hasChilds) {
                        childsHtml += '<i class="fa fa-plus-square-o js-unfold" style="cursor: pointer;"></i>';
                    } else {
                        childsHtml += '<i class="fa"></i>';
                    }
                    if (that.options.hasCheck) { 
                        if (item.checked) {
                            if (item.disabled) {
                                childsHtml += '<i class="fa fa-check-square-o text-muted"></i>';
                            } else {
                                childsHtml += '<i class="fa fa-check-square-o js-check" style="cursor: pointer;"></i>';
                            }
                        } else {
                            if (item.disabled) {
                                childsHtml += '<i class="fa fa-square-o text-muted"></i>';
                            } else {
                                childsHtml += '<i class="fa fa-square-o js-check" style="cursor: pointer;"></i>';
                            }
                        }
                    }
                    if (row.formatter) {
                        childsHtml += row.formatter(item[row.field], item);
                    } else {
                        childsHtml += item[row.field];
                    }
                    childsHtml += '</td>';
                } else {
                    var alignHtml = '';
                    if (row.align) alignHtml = ' style="text-align:' + row.align + '"';

                    if (row.formatter) {
                        childsHtml += '<td' + alignHtml + '>' + row.formatter(item[row.field], item) + '</td>';
                    } else {
                        childsHtml += '<td' + alignHtml + '>' + item[row.field] + '</td>';
                    }
                }
            });

            childsHtml += '</tr>';
        }
        return childsHtml;
    },
    getOneSelected: function (fn) {
        if(this.options.selectedId) {
            fn && fn(this.options.selectedId, this.options.selectedName);
        } else {
            Util.message('请选中一行')
        }
    },
    getCheckedIds: function (fn) {
        var list = $(this.options.elem).find('.js-check.fa-check-square-o');
        var ids = [];
        for (var i = 0; i < list.length; i++) {
            var elem = list[i];
            var trElem = $(elem).parent().parent();
            var id = trElem.data('id');
            var name = trElem.data('name');
            ids.push(id);
        }
        fn && fn(ids);
    }
}
