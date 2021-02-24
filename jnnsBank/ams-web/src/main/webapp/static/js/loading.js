/**
 * Created by alven on 23/01/2018.
 */
/** loading.js */
layui.define(['jquery'], function(exports) {
    "use strict";

    var $ = layui.jquery;

    var loading = {
        /**
         * block element(indicate loading)
         * @param {Object} options
         */
        show: function(options) {
            options = $.extend(true, {}, options);
            var html = '<div class="loading-message loading-message-boxed"><img' +
                ' src="../images/loading-spinner-grey.gif"' +
                ' align=""><span>&nbsp;&nbsp;' + (options.message ? options.message : '加载中...') + '</span></div>';

            if (options.target) { // element blocking
                var el = $(options.target);
                if (el.height() <= ($(window).height())) {
                    options.cenrerY = true;
                }
                el.block({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    centerY: options.cenrerY !== undefined ? options.cenrerY : false,
                    css: {
                        top: '10%',
                        width:'100%',
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            } else { // page blocking
                $.blockUI({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 1000,
                    css: {
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: options.boxed ? 0.05 : 0.1,
                        cursor: 'wait'
                    }
                });
            }
        },
        /**
         * un-block element(finish loading)
         * @param {String} target
         */
        hide: function(target) {
            if (target) {
                $(target).unblock({
                    onUnblock: function() {
                        $(target).css('position', '');
                        $(target).css('zoom', '');
                    }
                });
            } else {
                $.unblockUI();
            }
        }
    };

    exports('loading', loading);
});