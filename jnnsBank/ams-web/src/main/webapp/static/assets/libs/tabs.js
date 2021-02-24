function Tabs(_options) {
    this.options = {
        stepLeft: -200
    };
    this.init(_options);
};

Tabs.prototype = {
    // 初始化
    init: function (_options) {
        var that = this;
        that.set(_options);
        that.bindEvent();
        return this;
    },
    //参数设置
    set: function (_options) {
        $.extend(true, this.options, _options);
        return this;
    },
    //绑定事件
    bindEvent: function () {
        var that = this;

        // 向前移动
        $('#main_tabs_prev').on('click', function () {
            that.movePrev();
        });

        // 向后移动
        $('#main_tabs_next').on('click', function () {
            that.moveNext();
        });

        // 关闭标签页
        $('#main_tabs_menu').on('click', 'a', function () {
            var type = $(this).data('type');

            if (type == 'closeCurTab') {
                that.closeCurTab();
            } else if (type == 'closeOtherTab') {
                that.closeOtherTab();
            } else if (type == 'closeAllTab') {
                that.closeAllTab();
            }
        });
    },
    // 新增标签页
    addTab: function (item) {
        var that = this;
        var title = item.title;
        var url = item.url;
        var id = that.getIdByUrl(url);
        var tab = $('#main_tab a[href="#' + id + '"]');
        if (tab.length > 0) {
            that.changeTab(id);
            that.refreshTab(id);
        } else {
            $('#main_tab').append('<li role="presentation">'
                + '<a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">'
                + title + '</a><i class="fa fa-close"></i></li>');
            var iframeHeight = $('.main-iframe').eq(0).height();
            $('#main_tab_content').append('<div role="tabpanel" class="tab-pane" id="' + id + '">'
                + '<iframe class="main-iframe" width="100%" src="' + url + '" style="height: ' + iframeHeight + 'px;"></iframe>'
                + '</div>');

            $('#main_tab .fa-close').off().on('click', function () {
                var elem = $(this).prev();
                var elemId = elem.attr('aria-controls');
                that.closeTab(elemId);
            });

            that.setTabWidth();
            that.changeTab(id);
        }
    },
    // 刷新标签页
    refreshTab: function (id) {
        var that = this;
        $('#' + id).find('iframe')[0].contentWindow.location.reload(true);
    },
    // 切换到指定标签页
    changeTab: function (id) {
        var that = this;
        $('#main_tab a[href="#' + id + '"]').tab('show');
        that.moveCurTab();
    },
    // 关闭指定标签页
    closeTab: function (id) {
        var that = this;
        var tab = $('#main_tab a[href="#' + id + '"]');
        if (tab.length > 0 && id != 'home') {
            var isActvie = tab.parent().hasClass('active');
            var prevId = tab.parent().prev().find('a').attr('aria-controls');
            tab.parent().remove();
            $('#' + id).remove();
            that.setTabWidth();
            if (isActvie) {
                that.changeTab(prevId);
            }
        }
    },
    // 关闭当前标签页
    closeCurTab: function () {
        var that = this;
        that.closeTab(that.getCurId());
    },
    // 关闭其他标签页
    closeOtherTab: function () {
        var that = this;
        var tabs = $('#main_tab li');
        tabs.each(function (i, item) {
            var elem = $(item);
            if (!elem.hasClass('active') && i > 0) {
                var id = elem.find('a').attr('aria-controls');
                elem.remove();
                $('#' + id).remove();
            }
        });
        that.setTabWidth();
        that.moveCurTab();
    },
    // 关闭全部标签页
    closeAllTab: function () {
        var that = this;

        var tabs = $('#main_tab li');
        tabs.each(function (i, item) {
            var elem = $(item);
            if (i > 0) {
                var id = elem.find('a').attr('aria-controls');
                elem.remove();
                $('#' + id).remove();
            }
        });
        that.setTabWidth();
        that.changeTab('home');
    },
    // 移动到当前标签页位置
    moveCurTab: function () {
        var that = this;

        var index = that.getCurIndex();
        // 可移动的最大值
        var maxLeft = that.getMaxLeft();

        var left = 0;
        if (index == -1) {
            left = maxLeft;
        } else {
            left = that.getLeftByIndex(index);
        }
        if (maxLeft > left) {
            left = maxLeft;
        }
        $('#main_tab').animate({ left: left + 'px' });
    },
    // 向前移动
    movePrev: function () {
        var that = this;

        var curLeft = that.getCurLeft();
        var left = curLeft - that.options.stepLeft;
        if (left > 0) {
            left = 0
        }
        $('#main_tab').animate({ left: left + 'px' });
    },
    // 向后移动
    moveNext: function () {
        var that = this;
        // 可移动的最大值
        var maxLeft = that.getMaxLeft();
        var curLeft = that.getCurLeft();
        var left = curLeft + that.options.stepLeft;

        if (left < maxLeft) {
            left = maxLeft
        }
        $('#main_tab').animate({ left: left + 'px' });
    },
    // url转化为id
    getIdByUrl: function (url) {
        return url.replace(/\./g, '_').replace(/\//g, '_').replace(/\?/g, '_').replace(/\=/g, '_').replace(/\&/g, '_').replace(/\%/g, '_').replace(/\s/g, '_').replace(/\:/g, '_');
    },
    // 获得当前标签页的id
    getCurId: function () {
        return $('#main_tab li.active').find('a').attr('aria-controls');
    },
    // 获得当前标签页的顺位
    getCurIndex: function () {
        return $('#main_tab li.active').index();
    },
    // 获得当前标签页的Left
    getCurLeft: function () {
        return $('#main_tab').position().left;
    },
    // 可移动的最大值
    getMaxLeft: function () {
        // 标签页的总宽度
        var tabWidth = $('#main_tab').width() - 200;
        // 标签页可见的宽度
        var viewWidth = $('#main_tab').parent().width();
        // 可移动的最大值
        var maxLeft = 0;
        if (tabWidth > viewWidth) {
            var maxLeft = -(tabWidth - viewWidth);
        }
        return maxLeft;
    },
    // 获得该标签页的Left
    getLeftByIndex: function (index) {
        var tab = $('#main_tab li').eq(index);
        return -(tab.position().left);
    },
    // 重置标签页总宽度
    setTabWidth: function () {
        var tabs = $('#main_tab li');
        var width = 0;
        tabs.each(function (i, item) {
            var elem = $(item);
            width += elem.width();
        });
        width += 200;
        $('#main_tab').css('width', width + 'px');
    }
}
