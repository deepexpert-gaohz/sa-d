$(function () {
    var global = new Global();
});

var Global = function (_options) {
    this.options = {

    };
    this.init(_options);
}

Global.prototype = {
    init: function (_options) {
        //参数设置
        this.set(_options);
        //初始化元素
        this.initElem();
        //显示数据
        this.viewData();
        //绑定事件
        this.bindEvent();
        return this;
    },
    //参数设置
    set: function (_options) {
        $.extend(true, this.options, _options);
        return this;
    },
    //初始化元素
    initElem: function () {

        if ($('.js-datepicker').length > 0) {
            $('.js-datepicker').datepicker({
                autoclose: true,
                language: 'zh-CN',
                format: 'yyyy-mm-dd'
            });
        }

        if ($('.js-datepicker-month').length > 0) {
            $('.js-datepicker-month').datepicker({
                autoclose: true,
                language: 'zh-CN',
                format: 'yyyy-mm',
                startView: 'months', //开始视图层，为月视图层
                maxViewMode:'years', //最大视图层，为年视图层
                minViewMode:'months' //最小视图层，为月视图层
            });
        }

        if ($('.js-timepicker').length > 0) {
            $('.js-timepicker').timepicker({
                showMeridian: false
            });
        }

        if ($('.js-datetimepicker').length > 0) {
            $('.js-datetimepicker').datetimepicker({
                autoclose: true,
                todayBtn: true,
                language: 'zh-CN',
                format: 'yyyy-mm-dd hh:ii:ss'
            });
        }

    },
    //显示数据
    viewData: function () {

    },
    //绑定事件
    bindEvent: function () {
        var that = this;

        //页面跳转
        $('a[admin-href]').on('click', function () {
            var url = $(this).attr('admin-href');
            var title = $(this).attr('admin-title');
            Util.openIframe({
                title: title,
                url: url
            });
            return false;
        });

    }
};
