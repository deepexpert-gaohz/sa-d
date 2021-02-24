/** index.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */

function doKey(e){
    var ev = e || window.event;//获取event对象
    var obj = ev.target || ev.srcElement;//获取事件源
    var t = obj.type || obj.getAttribute('type');//获取事件源类型
    if(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea"){
        return false;
    }
}
//禁止后退键 作用于Firefox、Opera
document.onkeypress=doKey;
//禁止后退键  作用于IE、Chrome
document.onkeydown=doKey;

var tab;
var navbar;

var resvrConfigEnabled;
var tempConfigEnabled;
var fileDueConfigEnabled;
var legalDueConfigEnabled;
var operatorDueConfigEnabled;

layui.config({
    base: 'js/',
    version: new Date().getTime()
}).use(['element', 'layer', 'navbar', 'tab'], function () {
    var element = layui.element(),
        $ = layui.jquery,
        layer = layui.layer;
    navbar = layui.navbar();
    tab = layui.tab({
        elem: '.admin-nav-card' //设置选项卡容器
        ,
        maxSetting: 20,
        contextMenu: true,
        onSwitch: function (data) {

            //变更menu
            navbar.setCurrent(data.id);
            if (data.id=="../ui/imageInfo/blank.html") {
                $("#dualRecordPage").show();
            }else {
                $("#dualRecordPage").hide();
            }
            //console.log(data.index); //得到当前Tab的所在下标
            //console.log(data.elem); //得到当前的Tab大容器
            //
            //console.log(tab.getCurrentTabId())
        }
    });

    $.get("../config/findValueByKey?configKey=titleName", null, function (data) {
        if (data) {  //主页标签名称
            document.title = data;
        } else {
            document.title = "后台管理系统";
        }
    })

    if (isDownloadChromeFrame()) {
        var text = '我们了解到您使用的可能是低版本的IE；使用中可能会出现部分显示问题；请下载插件来提高适配；安装后关闭浏览器重新打开！';
        layer.open({
            type: 1,
            content: '<div style="padding: 20px 30px;">'+ text +'</div>',
            btn: ['确定', '跳过'],
            yes: function(){
                window.location.href = '../ui/download/GoogleChromeframeStandaloneEnterprise.msi';
                layer.closeAll();
            },
            btn2: function(){
                layer.closeAll();
            }
        });
    }

    //iframe自适应
    $(window).on('resize', function () {
        var $content = $('.admin-nav-card .layui-tab-content');
        $content.height($(this).height()-90);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
    }).resize();

    //设置navbar
    navbar.set({
        spreadOne: true,
        elem: '#admin-navbar-side',
        cached: true,
        //data: navs
		cached:false,
		//url: '/admin/user/menu'
        url: '../security/menus'
    });
    //渲染navbar
    navbar.render();
    //监听点击事件
    navbar.on('click(side)', function (data) {
        if (data.field.href=="../ui/imageInfo/blank.html"){
            $("#dualRecordPage").show();
        }
        tab.tabAdd(data.field);
    });
    // $.get("/admin/initialSystem",null,function(data){
    // // $.get("/admin/user/system",null,function(data){
    //     var data = eval("("+data+")");
    //     for(var i=0;i<data.length;i++){
    //         $('#menuSys').append('<dd><a href="javascript:;" onclick="javascript:refreshMenu('+data[i].id+')"><i class="fa '+data[i].icon+'" aria-hidden="true"></i>  '+data[i].title+'</a></dd>');
    //         $('#menuSysMobile').prepend('<dd><a href="javascript:;" onclick="javascript:refreshMenu('+data[i].id+')"><i class="fa '+data[i].icon+'" aria-hidden="true"></i> '+data[i].title+'</a></dd>');
    //
    //     }
    // });
    //清除缓存
    $('#clearCached').on('click', function () {
        navbar.cleanCached();
        layer.alert('清除完成!', { icon: 1, title: '系统提示' }, function () {
            location.reload();//刷新
        });
    });

    // $('.admin-side-toggle').on('click', function () {
    //     var sideWidth = $('#admin-side').width();
    //     if (sideWidth === 200) {
    //         $('#admin-body').animate({
    //             left: '0'
    //         }); //admin-footer
    //         $('#admin-footer').animate({
    //             left: '0'
    //         });
    //         $('#admin-side').animate({
    //             width: '0'
    //         });
    //     } else {
    //         $('#admin-body').animate({
    //             left: '200px'
    //         });
    //         $('#admin-footer').animate({
    //             left: '200px'
    //         });
    //         $('#admin-side').animate({
    //             width: '200px'
    //         });
    //     }
    // });

    //side bar toggle
    $('.admin-event-toggle').on('click', function () {
        var shrinkClass = 'layadmin-side-shrink',
            bodyClass = 'layui-layout-body',
            layuiBody = $('.'+bodyClass),
            toggleElement = $('#LAY_app_flexible'),
            iconRightClass = 'layui-icon-shrink-right',
            iconLeftClass = 'layui-icon-spread-left';

        var isShrink = layuiBody.hasClass(shrinkClass);

        if(isShrink){
            toggleElement.removeClass(iconLeftClass).addClass(iconRightClass);
            layuiBody.removeClass(shrinkClass);
        } else {
            toggleElement.removeClass(iconRightClass).addClass(iconLeftClass);
            layuiBody.addClass(shrinkClass);
        }
    });

    //首页
    $('.admin-event-home').on('click', function () {
        $('ul.layui-tab-title').find('li').first().click();
    });

    //刷新
    $('.admin-event-refresh').on('click', function () {
        tab.refresh();
    });

    //配置前一页 后一页
    $('.layui-icon-prev').on('click',function () {
        tab.prev();
    });

    $('.layui-icon-next').on('click',function () {
        tab.next();
    });

    // tab action
    $('.layui-tab-actions').on('click',function () {
        //获取点击的target值
        var target = $(this).data('event');
        tab.actions(target);

        //关闭菜单
        $(this).parent().removeClass('layui-show');
    });

    $('#admin-side').on('click',function () {
        var shrinkClass = 'layadmin-side-shrink',
            bodyClass = 'layui-layout-body',
            layuiBody = $('.'+bodyClass),
            toggleElement = $('#LAY_app_flexible'),
            iconRightClass = 'layui-icon-shrink-right',
            iconLeftClass = 'layui-icon-spread-left';

        var isShrink = layuiBody.hasClass(shrinkClass);

        if(isShrink){
            toggleElement.removeClass(iconLeftClass).addClass(iconRightClass);
            layuiBody.removeClass(shrinkClass);
        }
    });

    //鼠标提示
    $('#admin-navbar-side').on("mouseenter", "*[lay-tips]", function() {
        var e = $(this);
        if (!e.parent().hasClass("layui-nav-item") || $('.layui-layout-body').hasClass('layadmin-side-shrink')) {
            var i = e.attr("lay-tips")
                , t = e.attr("lay-offset")
                , n = e.attr("lay-direction")
                , s = layer.tips(i, this, {
                tips: n || 1,
                time: -1,
                success: function(e, a) {
                    t && e.css("margin-left", t + "px")
                }
            });
            e.data("index", s)
        }
    }).on("mouseleave", "*[lay-tips]", function() {
        layer.close($(this).data("index"))
    });


    $('.admin-side-full').on('click', function () {
        var docElm = document.documentElement;
        //W3C  
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
        }
        //FireFox  
        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
        }
        //Chrome等  
        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
        }
        //IE11
        else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
        layer.msg('按Esc即可退出全屏');
    });

    $('#setting').on('click', function () {
        tab.tabAdd({
            href: '/Manage/Account/Setting/',
            icon: 'fa-gear',
            title: '设置'
        });
    });

    $('#changePwdBtn').on('click', function () {
        tab.tabAdd({
            href: 'user/change.html',
            icon: 'fa-key',
            title: '修改密码'
        });
    });
    $('#resvrUnprocessList').on('click', function () {
        tab.tabAdd({
            href: 'notice/list.html?noticeType=resvrUnprocess',
            title: '预约未及时处理列表'
        });
    });
    $('#tempAcctList').on('click', function () {
        tab.tabAdd({
            href: 'notice/list.html?noticeType=tempAcct',
            title: '临时户到期提醒列表'
        });
    });

    $('#fileDueNoticeList').on('click', function () {
        tab.tabAdd({
            href: 'notice/legalDueList.html?noticeType=fileDueNotice',
            title: '证明文件到期提醒列表'
        });
    });
    $('#legalDueNoticeList').on('click', function () {
        tab.tabAdd({
            href: 'notice/legalDueList.html?noticeType=legalDueNotice',
            title: '法人证件到期提醒列表'
        });
    });
    $('#operatorDueNoticeList').on('click', function () {
        tab.tabAdd({
            href: 'notice/operatorIdcardDueList.html?noticeType=operatorDueNotice',
            title: '经办人证件到期提醒列表'
        });
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile'),
        shadeMobile = $('.site-mobile-shade');
    treeMobile.on('click', function () {
        $('body').addClass('site-mobile');
    });
    shadeMobile.on('click', function () {
        $('body').removeClass('site-mobile');
    });

    $.ajax({
        url: "../security/info",
        type: "GET",
        success: function (data) {
            if (data.code === 'ACK' && data.data) {
                $("#username").text('机构号:' + data.data.bankCode + ' ' + data.data.cname);
            }
        }
        // error: function (xhr, textStatus, errorThrown) {
        //     if (xhr.status == 401) {
        //         window.top.location.href = "./";
        //     }
        // }
    });

    $.ajax({
       url: '../notice/permission',
       type: 'GET',
        async: false,
        success: function (data) {
            data = data.data;
            if(data.resvrConfigEnabled == false) {
                $('#resvrUnprocessList').hide();
            }
            if(data.tempConfigEnabled == false) {
                $('#tempAcctList').hide();
            }
            if(data.fileDueConfigEnabled == false) {
                $('#fileDueNoticeList').hide();
            }
            if(data.legalDueConfigEnabled == false) {
                $('#legalDueNoticeList').hide();
            }
            if(data.operatorDueConfigEnabled == false) {
                $('#operatorDueNoticeList').hide();
            }

            resvrConfigEnabled = data.resvrConfigEnabled;
            tempConfigEnabled = data.tempConfigEnabled;
            fileDueConfigEnabled = data.fileDueConfigEnabled;
            legalDueConfigEnabled = data.legalDueConfigEnabled;
            operatorDueConfigEnabled = data.operatorDueConfigEnabled;
        }
        // error: function (xhr, textStatus, errorThrown) {
        //     if (xhr.status == 401) {
        //         window.top.location.href = "./";
        //     }
        // }
    });

    $.ajax({
        url: "../notice/count",
        type: "GET",
        success: function (data) {
            if (data.code === 'ACK' && data.data) {
                var resvrUnprocess = data.data['resvrUnprocess'];
                if(!resvrUnprocess || resvrConfigEnabled == false){
                    resvrUnprocess = 0;
                }
                var tempAcct = data.data['tempAcct'];
                if (!tempAcct || tempConfigEnabled == false) {
                    tempAcct = 0;
                }

                var fileDueNoticeDay = data.data['fileDueNoticeDay'];
                if (!fileDueNoticeDay || fileDueConfigEnabled == false) {
                    fileDueNoticeDay = 0;
                }
                var legalDueNoticeDay = data.data['legalDueNoticeDay'];
                if (!legalDueNoticeDay || legalDueConfigEnabled == false) {
                    legalDueNoticeDay = 0;
                }
                var operatorDueNoticeDay = data.data['operatorDueNoticeDay'];
                if (!operatorDueNoticeDay || operatorDueConfigEnabled == false) {
                    operatorDueNoticeDay = 0;
                }

                var noticeTotal = resvrUnprocess + tempAcct + fileDueNoticeDay + legalDueNoticeDay + operatorDueNoticeDay;

                $("#noticeTotal").text(noticeTotal);
                $("#resvrUnprocess").text(resvrUnprocess);
                $("#tempAcct").text(tempAcct);
                $("#fileDueNoticeDay").text(fileDueNoticeDay);
                $("#legalDueNoticeDay").text(legalDueNoticeDay);
                $("#operatorDueNoticeDay").text(operatorDueNoticeDay);
            }
        }
        // error: function (xhr, textStatus, errorThrown) {
        //     if (xhr.status == 401) {
        //         window.top.location.href = "./";
        //     }
        // }
    });

    //工商网络状态
    $.get('../config/findByKey?configKey=saicState', null, function (res) {
        if (res){
            $("#saic_network_state").show();
            setInterval(getSaicNetworkState, 10000);
        } else {
            $("#saic_network_state").hide();
        }
    });


    function loadImage(url, callback) {
        var img = new Image();
        img.src = url;

        img.onload = function () {
            callback.call(img);
        };
    };

    var logoHtml = '';
    var logoBigUrl = '../config/download?configKey=lindex.big';
    loadImage(logoBigUrl, function (img) {
        logoHtml += '<img class="logo-big" src="'+logoBigUrl+'">';
        $('#logo').html(logoHtml);
    });

    var logoSmallUrl = '../config/download?configKey=index.small';
    loadImage(logoSmallUrl, function (img) {
        logoHtml += '<img class="logo-small" src="'+logoSmallUrl+'">';
        $('#logo').html(logoHtml);
    });

});
function refreshMenu(parentId){
//设置navbar
    navbar.set({
        spreadOne: true,
        elem: '#admin-navbar-side',
        cached: true,
        //data: navs
        cached:false,
        // url: '/admin/user/menu?parentId='+parentId
        url: '../security/menus?'
    });
    navbar.render();
    //监听点击事件
    navbar.on('click(side)', function (data) {
        tab.tabAdd(data.field);
    });
}

function isDownloadChromeFrame () {
    var DEFAULT_VERSION = 8.0;
    var ua = navigator.userAgent.toLowerCase();
    var isIE = ua.indexOf("msie")>-1;
    var safariVersion;
    if(isIE){
        safariVersion =  ua.match(/msie ([\d.]+)/)[1];
    }
    if(safariVersion && (safariVersion <= DEFAULT_VERSION)){
        return true;
    } else {
        return false;
    }
}

//工商网络状态
function getSaicNetworkState () {
    $.get('../saicMonitor/state', null, function (res) {
        if (res.code=="ACK") {
            var data = res.data;
            if(data.state) {
                $('#saic_network_state').html(
                    '<a href="javascript:void(0);">工商网络状态：<i class="fa fa-wifi"></i></a>'
                    +'<dl class="layui-nav-child">'
                    +'<dd><a href="javasrcipt:;">网络延时：<span class="text-danger">'+data.speed+'ms</span></a></dd>'
                    +'</dl>');
            } else if(data.speed==9999){
                $('#saic_network_state').html(
                    '<a href="javascript:void(0);">工商网络状态：<i class="fa fa-wifi"></i> <i class="fa fa-exclamation"></i></a>'
                    +'<dl class="layui-nav-child">'
                    +'<dd><a href="javasrcipt:;">获取失败</a></dd>'
                    +'</dl>');
            } else {
                $('#saic_network_state').html(
                    '<a href="javascript:void(0);">工商网络状态：<i class="fa fa-wifi"></i> <i class="fa fa-close"></i></a>'
                    +'<dl class="layui-nav-child">'
                    +'<dd><a href="javasrcipt:;">工商服务器异常</a></dd>'
                    +'</dl>');
            }
        }else {
            $('#saic_network_state').html(
                '<a href="javascript:void(0);">工商网络状态：<i class="fa fa-wifi"></i> <i class="fa fa-exclamation"></i></a>'
                +'<dl class="layui-nav-child">'
                +'<dd><a href="javasrcipt:;">获取失败</a></dd>'
                +'</dl>');
        }
    });
}