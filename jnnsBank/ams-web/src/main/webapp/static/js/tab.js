/** tab.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['element', 'common'], function (exports) {
    "use strict";

    var mod_name = 'tab',
        $ = layui.jquery,
        element = layui.element(),
        commo = layui.common,
        globalTabIdIndex = 0,
        layer = layui.layer,
        Tab = function () {
            this.config = {
                elem: undefined,
                closed: true, //是否包含删除按钮
                autoRefresh: true,
                contextMenu: false,
                onSwitch: undefined,
                openWait: true
            };
            //获取有权限的按钮
            var that = this;
            $.get('../security/permissions',function (data) {
                that.config.authBtns = {};
                for(var i = 0; i<data.length; i++){
                    if(data[i].permissionType === 'button'){
                        that.config.authBtns[data[i].code] = data[i];
                    }
                }
            });
        };
    var ELEM = {};
    //版本号
    Tab.prototype.v = '0.1.5';
	/**
	 * 参数设置
	 * @param {Object} options
	 */
    Tab.prototype.set = function (options) {
        var that = this;
        $.extend(true, that.config, options);
        return that;
    };
	/**
	 * 初始化
	 */
    Tab.prototype.init = function () {
        var that = this;
        var _config = that.config;
        if (typeof (_config.elem) !== 'string' && typeof (_config.elem) !== 'object') {
            common.throwError('Tab error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if (typeof (_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
        }
        if (typeof (_config.elem) === 'object') {
            $container = _config.elem;
        }
        if ($container.length === 0) {
            common.throwError('Tab error:找不到elem参数配置的容器，请检查.');
        }
        var filter = $container.attr('lay-filter');
        if (filter === undefined || filter === '') {
            common.throwError('Tab error:请为elem容器设置一个lay-filter过滤器');
        }
        _config.elem = $container;
        ELEM.titleBox = $container.children('ul.layui-tab-title');
        ELEM.contentBox = $container.children('div.layui-tab-content');
        ELEM.tabFilter = filter;
        return that;
    };
	/**
	 * 查询tab是否存在，如果存在则返回索引值，不存在返回-1
	 * @param {String} 标题
	 */
    Tab.prototype.exists = function (title) {
        var that = ELEM.titleBox === undefined ? this.init() : this,
            tabIndex = -1;
        ELEM.titleBox.find('li').each(function (i, e) {
            var $cite = $(this).children('cite');
            if ($cite.text() === title) {
                tabIndex = i;
            };
        });
        return tabIndex;
    };

    /**
     * 查询tab是否存在，如果存在则返回索引值，不存在返回-1
     * @param {String} 标题
     */
    Tab.prototype.existsIframe = function (url) {
        var that = ELEM.contentBox === undefined ? this.init() : this,
            tabIndex = -1;
        ELEM.contentBox.find('iframe').each(function (i, e) {
            var iframeUrl = $(this).attr('src');
            if (iframeUrl === url) {
                tabIndex = i;
            };
        });
        return tabIndex;
    };

    /**
     * 当前tab刷新
     */
    Tab.prototype.refresh = function () {
        var that = ELEM.contentBox === undefined ? this.init() : this;
        var currentIframe = ELEM.contentBox.find('.layui-show>iframe');
        var iframeUrl = $(currentIframe).attr('src');

        $(currentIframe).attr('src',iframeUrl);
    };

    /**
     * 刷新指定Tab页
     * @param data
     */
    Tab.prototype.tabRefresh = function (data) {
        var that = this;
        var _config = that.config;
        var tabIndex = -1;
        if(data.href){
            tabIndex = that.existsIframe(data.href);
        } else {
            tabIndex = that.exists(data.title);
        }
        (_config.elem.find('div.layui-tab-content > div').eq(tabIndex).children('iframe')[0]).src = data.href;
    };

    /**
     * 前一个tab
     */
    Tab.prototype.prev = function () {
        var that = this;
        var _config = that.config;
        var prevElement = $(_config.elem).find('ul.layui-tab-title').children('li.layui-this').prev();
        if(prevElement){
            element.tabChange(ELEM.tabFilter, prevElement.attr('lay-id'));
        }
    };

    /**
     * 下一个tab
     */
    Tab.prototype.next = function () {
        var that = this;
        var _config = that.config;
        var nextElement = $(_config.elem).find('ul.layui-tab-title').children('li.layui-this').next();
        if(nextElement){
            element.tabChange(ELEM.tabFilter, nextElement.attr('lay-id'));
        }

    };

    /**
     * tab 操作
     */
    Tab.prototype.actions = function (target) {
        var that = this;
        switch (target) {
            case 'closeThisTab': //关闭当前
                var tabIndex = that.getCurrentTabId();
                if (tabIndex !== -1) {
                    that.deleteTab(tabIndex);
                }
                break;
            case 'closeOtherTabs': //关闭其他
                var tabCurrentIndex = that.getCurrentTabId();
                ELEM.titleBox.children('li').each(function () {
                    var $t = $(this);
                    var layId = $t.attr('lay-id');
                    if (layId !== '/' && layId !== tabCurrentIndex) {
                        that.deleteTab(layId);
                    }
                });
                ELEM.titleBox.css('left', '0px');
                break;
            case 'closeAllTabs': //全部关闭
                ELEM.titleBox.children('li').each(function () {
                    var $t = $(this);
                    var layId = $t.attr('lay-id');
                    if (layId !== '/') {
                        that.deleteTab(layId);
                    }
                });
                ELEM.titleBox.css('left', '0px');
                break;
        }
    };

    /**
	 * 获取tabid
	 * @param {String} 标题
	 */
    Tab.prototype.getTabId = function (title) {
        var that = ELEM.titleBox === undefined ? this.init() : this,
            tabId = -1;
        ELEM.titleBox.find('li').each(function (i, e) {
            var $cite = $(this).children('cite');
            if ($cite.text() === title) {
                tabId = $(this).attr('lay-id');
            };
        });
        return tabId;
    };

    /**
	 * 获取tabid
	 * @param {Number} 位置
	 */
    Tab.prototype.getTabIdByIndex = function (index) {
        var that = ELEM.titleBox === undefined ? this.init() : this,
            tabId = -1;
        ELEM.titleBox.find('li').each(function (i, e) {
            var $cite = $(this).children('cite');
            if (index === i) {
                tabId = $(this).attr('lay-id');
            };
        });
        return tabId;
    };

	/**
	 * 添加选择卡，如果选择卡存在则获取焦点
	 * @param {Object} data
	 */
    Tab.prototype.tabAdd = function (data) {
        var that = this;
        var _config = that.config;
        var tabIndex = -1;
        if(data.href){
            tabIndex = that.existsIframe(data.href);
        } else {
            tabIndex = that.exists(data.title);
        }
        var waitLoadIndex;
        if (tabIndex === -1) {
            if (_config.openWait) {
                waitLoadIndex = layer.load(2);
            }
            //设置只能同时打开多少个tab选项卡
            if (_config.maxSetting !== 'undefined') {
                var currentTabCount = _config.elem.children('ul.layui-tab-title').children('li').length;
                currentTabCount --; //减去首页
                if (typeof _config.maxSetting === 'number') {
                    if (currentTabCount === _config.maxSetting) {
                        //layer.msg('为了系统的流畅度，只能同时打开' + _config.maxSetting + '个选项卡。');
                        //return;
                        that.deleteTab(that.getTabIdByIndex(1));
                    }
                }
            }
            globalTabIdIndex++;
            var content = '<iframe src="' + data.href + '" data-id="' + globalTabIdIndex + '"></iframe>';
            var title = '';
            // if (data.icon !== undefined) {
            //     if (data.icon.indexOf('fa-') !== -1) {
            //         title += '<i class="fa ' + data.icon + '" aria-hidden="true"></i>';
            //     } else {
            //         title += '<i class="layui-icon">' + data.icon + '</i>';
            //     }
            // }
            title += '<cite title="'+ data.title +'">' + data.title + '</cite>';
            if (_config.closed) {
                title += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + globalTabIdIndex + '">&#x1006;</i>';
            }
            //添加tab
            element.tabAdd(ELEM.tabFilter, {
                title: title,
                content: content,
                id: data.href
            });
            //iframe 自适应
            ELEM.contentBox.find('iframe[data-id=' + globalTabIdIndex + ']').each(function () {
                $(this).height(ELEM.contentBox.height());
            });
            if (_config.closed) {
                //监听关闭事件
                ELEM.titleBox.find('li').children('i.layui-tab-close[data-id=' + globalTabIdIndex + ']').on('click', function () {
                    // var iframeWindow = $("iframe[data-id='" + $(this).attr('data-id') + "']")[0].contentWindow;
                    // if ($.isFunction(iframeWindow.onDeleteTab)) {
                    //     iframeWindow.onDeleteTab();
                    // }
                    if ("../ui/imageInfo/blank.html" == $(this).parent('li').attr('lay-id')) {
                        $("#dualRecordPage").hide();
                    }
                    that.deleteTab($(this).parent('li').attr('lay-id'));
                    // element.tabDelete(ELEM.tabFilter, $(this).parent('li').attr('lay-id')).init();
                    if (_config.contextMenu) {
                        $(document).find('div.uiba-contextmenu').remove(); //移除右键菜单dom
                    }
                });
            };
            //切换到当前打开的选项卡
            element.tabChange(ELEM.tabFilter, data.href);

            ELEM.contentBox.find('iframe[data-id=' + globalTabIdIndex + ']').on('load', function () {
                //debugger;
                if (_config.openWait) {
                    layer.close(waitLoadIndex);
                }

                //btn权限过滤
                if(_config.authBtns){
                    /**
                     * 按钮权限控制
                     */
                    var allButtons = $('button[data-code]',$(this).contents());
                    removeDn(allButtons, _config)

                    allButtons = $('div[data-code]',$(this).contents());
                    if(allButtons.length != 0) {
                        removeDn(allButtons, _config);
                    }

                    allButtons = $('a[data-code]',$(this).contents());
                    if(allButtons.length != 0) {
                        removeDn(allButtons, _config);
                    }

                }

            });
        } else {
            element.tabChange(ELEM.tabFilter, data.href);
            //自动刷新
            if (_config.autoRefresh) {
                //_config.elem.find('div.layui-tab-content > div').eq(tabIndex).children('iframe')[0].contentWindow.location.reload();
                (_config.elem.find('div.layui-tab-content > div').eq(tabIndex).children('iframe')[0]).src = data.href;
            }
        }

        if (_config.onSwitch) {
            element.on('tab(' + ELEM.tabFilter + ')', function (data) {
                _config.onSwitch({
                    index: data.index,
                    elem: data.elem,
                    id: ELEM.titleBox.children('li').eq(data.index).attr('lay-id')
                });
            });
        }
    };
    /**
	 * 获取当前获得焦点的tabid
	 */
    Tab.prototype.getCurrentTabId = function () {
        var that = this;
        var _config = that.config;
        return $(_config.elem).find('ul.layui-tab-title').children('li.layui-this').attr('lay-id');
    };

    /**
	 * 删除指定的tab选项卡
	 * @param {String} id
	 */
    Tab.prototype.deleteTab = function (id,isRefresh) {
        var that = this;
        var data_id = that.config.elem.find('li[lay-id="' + id + '"]').children('i').attr('data-id');

        if ($("iframe[data-id='" + data_id + "']").length > 0 ) {
            var iframeWindow = $("iframe[data-id='" + data_id + "']")[0].contentWindow;
            if ($.isFunction(iframeWindow.onDeleteTab)) {
                iframeWindow.onDeleteTab();
            }

            element.tabDelete(ELEM.tabFilter, id);
            if(isRefresh){
                that.refresh();
            }
        }
        return that;
    };

    var tab = new Tab();
    exports(mod_name, function (options) {
        return tab.set(options);
    });
});

function removeDn(allButtons, _config) {
    for(var i = 0; i < allButtons.length; i++){
        var elementBtn = $(allButtons[i]);
        var authCode = elementBtn.attr('data-code');
        if(_config.authBtns[authCode]){
            elementBtn.removeClass('dn');
        } else {
            elementBtn.remove();
        }
    }
}