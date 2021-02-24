/** navbar.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.define(['element', 'common'], function (exports) {
    "use strict";
    var $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        element = layui.element(),
        common = layui.common,
        cacheName = 'tb_navbar';

    var Navbar = function () {
		/**
		 *  默认配置 
		 */
        this.config = {
            elem: undefined, //容器
            data: undefined, //数据源
            url: undefined, //数据源地址
            type: 'GET', //读取方式
            cached: false, //是否使用缓存
            spreadOne: false //设置是否只展开一个二级菜单
        };
        this.v = '1.0.0';
    };
    //渲染
    Navbar.prototype.render = function () {
        var _that = this;
        var _config = _that.config;
        if (typeof (_config.elem) !== 'string' && typeof (_config.elem) !== 'object') {
            common.throwError('Navbar error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if (typeof (_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
        }
        if (typeof (_config.elem) === 'object') {
            $container = _config.elem;
        }
        if ($container.length === 0) {
            common.throwError('Navbar error:找不到elem参数配置的容器，请检查.');
        }
        if (_config.data === undefined && _config.url === undefined) {
            common.throwError('Navbar error:请为Navbar配置数据源.')
        }
        if (_config.data !== undefined && typeof (_config.data) === 'object') {
            var html = getHtml(_config.data);
            $container.html(html);
            element.init();
            _that.config.elem = $container;
        } else {
            if (_config.cached) {
                var cacheNavbar = layui.data(cacheName);
                if (cacheNavbar.navbar === undefined) {
                    $.ajax({
                        type: _config.type,
                        url: _config.url,
                        async: false, //_config.async,
                        dataType: 'json',
                        success: function (result, status, xhr) {
                            //添加缓存
                            layui.data(cacheName, {
                                key: 'navbar',
                                value: result
                            });
                            var html = getHtml(result.data);
                            $container.html(html);
                            element.init();
                        },
                        error: function (xhr, status, error) {
                            common.msgError('Navbar error:' + error);
                        },
                        complete: function (xhr, status) {
                            _that.config.elem = $container;
                        }
                    });
                } else {
                    var html = getHtml(cacheNavbar.navbar);
                    $container.html(html);
                    element.init();
                    _that.config.elem = $container;
                }
            } else {
                //清空缓存
                layui.data(cacheName, null);
                $.ajax({
                    type: _config.type,
                    url: _config.url,
                    async: false, //_config.async,
                    dataType: 'json',
                    success: function (result, status, xhr) {
                        var html = getHtml(result.data);
                        $container.html(html);
                        element.init();
                    },
                    error: function (xhr, status, error) {
                        common.msgError('Navbar error:' + error);
                    },
                    complete: function (xhr, status) {
                        _that.config.elem = $container;
                    }
                });
            }
        }

        //只展开一个二级菜单
        if (_config.spreadOne) {
            var $ul = $container.children('ul');
            $ul.find('li.layui-nav-item').each(function () {
                $(this).on('click', function () {
                    $(this).siblings().removeClass('layui-nav-itemed');
                });
            });
        }
        return _that;
    };
	/**
	 * 配置Navbar
	 * @param {Object} options
	 */
    Navbar.prototype.set = function (options) {
        var that = this;
        that.config.data = undefined;
        $.extend(true, that.config, options);
        return that;
    };
	/**
	 * 绑定事件
	 * @param {String} events
	 * @param {Function} callback
	 */
    Navbar.prototype.on = function (events, callback) {
        var that = this;
        var _con = that.config.elem;
        if (typeof (events) !== 'string') {
            common.throwError('Navbar error:事件名配置出错，请参考API文档.');
        }
        var lIndex = events.indexOf('(');
        var eventName = events.substr(0, lIndex);
        var filter = events.substring(lIndex + 1, events.indexOf(')'));
        if (eventName === 'click') {
            if (_con.attr('lay-filter') !== undefined) {
                _con.children('ul').find('li').each(function () {
                    var $this = $(this);
                    if ($this.find('dl').length > 0) {
                        var $dd = $this.find('dd').each(function () {
                            $(this).on('click', function () {
                                var $a = $(this).children('a');
                                var href = $a.data('url');
                                var icon = $a.children('i:first').data('icon');
                                var title = $a.children('cite').text();
                                var data = {
                                    elem: $a,
                                    field: {
                                        href: href,
                                        icon: icon,
                                        title: title
                                    }
                                }
                                callback(data);
                            });
                        });
                    } else {
                        $this.on('click', function () {
                            var $a = $this.children('a');
                            var href = $a.data('url');
                            var icon = $a.children('i:first').data('icon');
                            var title = $a.children('cite').text();
                            var data = {
                                elem: $a,
                                field: {
                                    href: href,
                                    icon: icon,
                                    title: title
                                }
                            }
                            callback(data);
                        });
                    }
                });
            }
        }
    };
	/**
	 * 清除缓存
	 */
    Navbar.prototype.cleanCached = function () {
        layui.data(cacheName, null);
    };

    /**
     * 设置navbar
     */
    Navbar.prototype.setCurrent = function (url) {
        var that = this;
        var _con = that.config.elem;
        var currentMenu = _con.find('.layui-this');
        var currentMenuGroup = _con.find('.layui-nav-item.layui-nav-itemed');
        var allMenus = _con.find('.layui-nav-item a');
        if(currentMenu){
            var currentMenuUrl = currentMenu.children('a').data('url');
            if(currentMenuUrl === url){
                return;
            }
            currentMenu.removeClass('layui-this');
        }
        for(var i = 0; i< allMenus.length;i++){
            var $t = $(allMenus[i]);
            if($t.data('url') === url){
                $t.parent().addClass('layui-this');

                var layuiNavItem = $t.parents('.layui-nav-item');

                if(!layuiNavItem.hasClass('layui-nav-itemed')){
                    currentMenuGroup.removeClass('layui-nav-itemed');
                    layuiNavItem.addClass('layui-nav-itemed');
                }
            }
        }
    };

    /**
	 * 获取html字符串
	 * @param {Object} data
	 */
    function getHtml(data) {
        //debugger;
        var listHtml = '<ul class="layui-nav layui-nav-tree beg-navbar">';
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            var itemHtml = '';
            if (item.spread) {
                itemHtml += '<li class="layui-nav-item layui-nav-itemed">';
            } else {
                itemHtml += '<li class="layui-nav-item">';
            }
            if (item.children && item.children.length > 0) {
                itemHtml += '<a href="javascript:;" lay-tips="'+ item.title +'" lay-direction="2">';
                if (item.icon) {
                    itemHtml += '<i class="fa ' + item.icon + '"></i>';
                }
                itemHtml += '<cite>' + item.title + '</cite></a>';

                itemHtml += '<dl class="layui-nav-child">'
                for (var j = 0; j < item.children.length; j++) {
                    var child = item.children[j];
                    itemHtml += '<dd title="' + child.title + '">';
                    itemHtml += '<a href="javascript:;" data-url="' + child.href + '">';
                    
                    if (child.icon){
                        itemHtml += '<i class="fa ' + child.icon + '"></i>';
                    }
                    itemHtml += '<cite>' + child.title + '</cite></a></dd>';
                }
                itemHtml += '</dl>';
            } else {
                var dataUrl = item.href ? ' data-url="' + item.href + '"' : '';

                itemHtml += '<a href="javascript:;"' + dataUrl + ' lay-tips="'+ item.title +'" lay-direction="2">';
                if (item.icon) {
                    itemHtml += '<i class="fa ' + item.icon + '"></i>';
                }
                itemHtml += '<cite>' + item.title + '</cite></a>';
            }
            itemHtml += '</li>';

            listHtml += itemHtml;
        }
        listHtml += '</ul>';
        return listHtml;
    }

    // 解压
    function unzip(key) {
        var charData = [];
        var keyArray = key.split('');
        for(var i = 0; i < keyArray.length; i++){
            var item = keyArray[i];
            charData.push(item.charCodeAt(0));
        }

        // var binData = new Uint8Array(charData);
        // console.log('Uint8Array:' + binData);
        // 解压
        // var data = pako.inflate(binData);
        var data = pako.inflate(charData);

        // 将GunZip ByTAREAR转换回ASCII字符串
        // key = String.fromCharCode.apply(null, new Uint16Array(data));

        key = String.fromCharCode.apply(null, data);

        return decodeURIComponent(Base64.decode(key));
    }


// 压缩
    function zip(str) {
        //escape(str)  --->压缩前编码，防止中午乱码
        var binaryString = pako.gzip(escape(str), { to: 'string' });
        return binaryString;
    }



    var navbar = new Navbar();

    exports('navbar', function (options) {
        return navbar.set(options);
    });
});
