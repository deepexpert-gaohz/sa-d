/**
 * Created by alven on 23/01/2018.
 */
layui.define(function(exports) {
    "use strict";

    var alert = {
        /**
         * 成功提示
         * @param {Object} options
         */
        showSuccess: function (title,text) {
            swal({
                title:title,
                text:text||'',
                type:'success',
                showConfirmButton: false,
                timer: 2000
            });
        },
        /**
         * 失败提示
         * @param title
         * @param text
         */
        showError:function (title,text) {
            swal({
                title:title,
                text:text||'',
                type:'error',
                showConfirmButton: false,
                timer: 2000
            });
        },
        /**
         * 消息提示
         * @param title
         * @param text
         */
        showInfo: function (title,text) {
            swal(title, text, "info");
        },
        /**
         * 警告提示
         * @param title
         * @param text
         */
        showWarning:function (title,text) {
            swal(title, text, "warning");
        }
    };

    exports('alert', alert);
});
