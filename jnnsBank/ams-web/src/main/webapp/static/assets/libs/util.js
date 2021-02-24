var Util = {
    //打开iframe页面
    openIframe: function (item) {
        parent.tab.tabAdd({
            title: item.title,
            href: '../ui' + item.url
        });
    },
    //关闭当前iframe页面
    closeCurIframe: function (item) {
        parent.tab.deleteTab(parent.tab.getCurrentTabId());
    },
    //获得URL参数
    getUrlParam: function (paraName) {
        var url = document.location.toString();
        var arrObj = url.split('?');
        if (arrObj.length > 1) {
            var arrPara = arrObj[1].split('&');
            var arr;
            for (var i = 0; i < arrPara.length; i++) {
                arr = arrPara[i].split('=');
                if (arr != null && arr[0] == paraName) {
                    return arr[1];
                }
            }
            return '';
        } else {
            return '';
        }
    },
    //消息框
    message: function (content, pauseTime) {
        pauseTime || (pauseTime = 2000);

        var messageHtml = '<div class="message-modal" id="message_modal"><div class="modal-body">' + content + '</div></div>';

        $('body').append(messageHtml);

        setTimeout(function () {
            $('#message_modal').remove();
        }, pauseTime);
    },
    //弹出框
    alert: function (options, handleAlert) {
        var title = '提示信息';
        var content = '';
        var closeBtn = true;
        var sizeClass = 'modal-sm';
        var alertText = '确定';

        if (typeof options === 'string') {
            content = options;
        } else {
            options.title && (title = options.title);
            options.content && (content = options.content);
            (options.closeBtn == false) && (closeBtn = false);
            options.sizeClass && (sizeClass = options.sizeClass);
            options.alertText && (alertText = options.alertText);
            options.handleAlert && (handleAlert = options.handleAlert);
            
        }

        Util.modal({
            id: 'alert',
            title: title,
            content: content,
            closeBtn: closeBtn,
            sizeClass: sizeClass,
            btns: [
                {
                    name: alertText,
                    btnClass: 'btn-primary',
                    handle: handleAlert
                }
            ]
        });
    },
    //确认框
    confirm: function (options, handleConfirm, handleCancel) {
        $('#confirm_modal').next('.modal-backdrop').remove();
        $('#confirm_modal').remove();

        var title = '提示信息';
        var content = '';
        var closeBtn = true;
        var sizeClass = 'modal-sm';
        var confirmText = '确定';
        var cancelText = '取消';

        if (typeof options === 'string') {
            content = options;
        } else {
            options.title && (title = options.title);
            options.content && (content = options.content);
            (options.closeBtn == false) && (closeBtn = false);
            options.sizeClass && (sizeClass = options.sizeClass);
            options.confirmText && (confirmText = options.confirmText);
            options.cancelText && (cancelText = options.cancelText);
            options.handleConfirm && (handleConfirm = options.handleConfirm);
            options.handleCancel && (handleCancel = options.handleCancel);
        }

        Util.modal({
            id: 'confirm',
            title: title,
            content: content,
            closeBtn: closeBtn,
            sizeClass: sizeClass,
            btns: [
                {
                    name: confirmText,
                    btnClass: 'btn-primary',
                    handle: handleConfirm
                },
                {
                    name: cancelText,
                    btnClass: 'btn-default',
                    handle: handleCancel
                }
            ]
        });
    },
    //信息框
    infoModal: function (options) {
        $('#info_modal').next('.modal-backdrop').remove();
        $('#info_modal').remove();

        var title = '信息';
        var content = '';
        var sizeClass = ''; //modal-sm,modal-md,modal-lg

        if (typeof options === 'string') {
            content = options;
        } else {
            options.content && (content = options.content);
            options.title && (title = options.title);
            options.sizeClass && (sizeClass = options.sizeClass);
        }

        Util.modal({
            id: 'info',
            title: title,
            content: content,
            sizeClass: sizeClass
        });
    },
    modal: function (options) {
        var id = 1;
        var title = '信息';
        var content = '';
        var closeBtn = true;
        var sizeClass = ''; //modal-sm,modal-md,modal-lg
        var btns = null;

        options.id && (title = options.id);
        options.title && (title = options.title);
        options.content && (content = options.content);
        (options.closeBtn == false) && (closeBtn = false);
        options.sizeClass && (sizeClass = options.sizeClass);
        options.btns && (btns = options.btns);

        var elemId = 'modal_' + options.id;
        $('#' + elemId).next('.modal-backdrop').remove();
        $('#' + elemId).remove();

        var closeBtnHtml = '';
        if (closeBtn) {
            closeBtnHtml = '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
                + '<span aria-hidden="true">&times;</span></button>';
        }

        var btnsHtml = '';
        if (btns) {
            for (var i = 0; i < btns.length; i++) {
                var btn = btns[i];
                var btnId = 'modal_' + options.id + '_btn_' + i;
                btn.btnClass || (btn.btnClass = 'btn-primary');
                btnsHtml += '<button type="button" class="btn ' + btn.btnClass + '" id="' + btnId + '">' + btn.name + '</button>'
            }
            btnsHtml = '<div class="modal-footer">' + btnsHtml + '</div>';
        }

        var modalHtml = '<div class="modal fade" id="' + elemId + '">'
            + '<div class="modal-dialog ' + sizeClass + '">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + closeBtnHtml
            + '<h4 class="modal-title">' + title + '</h4>'
            + '</div>'
            + '<div class="modal-body">' + content + '</div>'
            + btnsHtml
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#' + elemId).modal('show');

        if (btns) {
            for (var i = 0; i < btns.length; i++) {
                (function(i) {
                    var btn = btns[i];
                    var btnId = 'modal_' + options.id + '_btn_' + i;
                    $('#' + btnId).off().on('click', function () {
                        var sign = true;
                        if (btn.handle) {
                            sign = btn.handle(id);
                        }
                        if (sign !== false) {
                            $('#' + elemId).modal('hide');
                        }
                    });
                })(i);
            }
        }
    },
    closeModal: function (id) {
        var elemId = 'modal_' + id;
        $('#' + elemId).modal('hide');
    },
    //下载
    download: function (url) {
        if ($('#download_frame').length < 1) {
            $('body').append('<iframe id="download_frame" src="" style="display: none;"></iframe>');
        }
        $('#download_frame').prop('src', url);
    },
    //下载
    postDownload: function (url, params) {
        var form = document.createElement('form');
        form.style.display = 'none';
        form.action = url;
        form.method = 'post';
        document.body.appendChild(form);

        for (var key in params) {
            if (!params[key]) {
                continue;
            }
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }

        form.submit();
        document.body.removeChild(form);
    },
    //上传文件
    uploadFile: function (options) {
        $('#upload_modal').next('.modal-backdrop').remove();
        $('#upload_modal').remove();

        var uploadHtml = '<div class="modal fade" id="upload_modal">'
            + '<div class="modal-dialog modal-sm">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">上传文件</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + '<div id="uploader">'
            + '<div class="btns">'
            + '<div id="picker">选择文件</div>'
            + '<div id="uploader_msg"></div>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(uploadHtml);
        $('#upload_modal').modal('show');
        $('#upload_modal').on('shown.bs.modal', function () {

            var fileTypeMap = {
                'excel': {
                    title: 'Excel',
                    extensions: 'xls,xlsx',
                    mimeTypes: 'application/vnd.ms-excel'
                },
                'pdf': {
                    title: 'pdf',
                    extensions: 'pdf',
                    mimeTypes: 'application/pdf'
                }
            }

            options.fileType || (options.fileType = 'excel');

            var uploader = WebUploader.create({
                //选完文件后，是否自动上传
                auto: true,
                //swf文件路径
                swf: Config.baseUrl + '/ui/plugins/webuploader/Uploader.swf',
                //文件接收服务端。
                server: options.url,
                //选择文件的按钮。可选。
                //内部根据当前运行是创建，可能是input元素，也可能是flash.
                pick: '#picker',
                //指定接受哪些类型的文件
                accept: fileTypeMap[options.fileType]
            });

            //某个文件开始上传前触发，一个文件只会触发一次。
            uploader.on('uploadStart', function (file) {
                $('#uploader_msg').text('上传中...');
            });
            //当文件上传成功时触发。
            uploader.on('uploadSuccess', function (file, response) {
                $('#uploader_msg').text('已上传');
                options.uploadSuccess && options.uploadSuccess(file, response);
            });
            //当文件上传出错时触发。
            uploader.on('uploadError', function (file, reason) {
                $('#uploader_msg').text('上传失败');
                options.uploadError && options.uploadError(file, reason);
            });
            //不管成功或者失败，文件上传完成时触发。
            uploader.on('uploadComplete', function (file) {
                $('#upload_modal').modal('hide');
                options.uploadComplete && options.uploadComplete(file);
            });

        });
    },
    //模版打印
    printTemplate: function (billType, depositorType, acctType, url) {
        $('#print_modal').next('.modal-backdrop').remove();
        $('#print_modal').remove();

        $.get(Config.baseUrl + '/template/getTemplateNameList?billType=' + billType + '&depositorType=' + depositorType + '&acctType=' + acctType, function (data) {

            if (data == '' || data == null) {
                Util.alert('对应打印模版未配置');
            } else {

                var optionsHtml = '';
                for (var i = 0; i < data.length; i++) {
                    optionsHtml += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }

                var printHtml = '<div class="modal fade" id="print_modal">'
                    + '<div class="modal-dialog modal-sm">'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
                    + '<span aria-hidden="true">&times;</span></button>'
                    + '<h4 class="modal-title">请选择打印模版</h4>'
                    + '</div>'
                    + '<div class="modal-body">'
                    + '<select class="form-control" id="print_template">'
                    + '<option value="">请选择</option>'
                    + optionsHtml
                    + '</select>'
                    + '</div>'
                    + '<div class="modal-footer">'
                    + '<button type="button" class="btn btn-primary" id="btn_print_template">确定</button>'
                    + '</div>'
                    + '</div>'
                    + '</div>'
                    + '</div>';

                $('body').append(printHtml);
                $('#print_modal').modal('show');

                $('#btn_print_template').off().on('click', function () {
                    $('#print_modal').modal('hide');

                    var templateName = $('#print_template').val();
                    Util.printPdf(url + '&billType=' + billType + '&depositorType=' + depositorType + '&acctType=' + acctType + '&templateName=' + encodeURI(templateName));
                });

            }
        });
    },
    //打印PDF
    printPdf: function (url) {
        $('#print_pdf_modal').next('.modal-backdrop').remove();
        $('#print_pdf_modal').remove();

        var printPdfHtml = '<div class="modal fade" id="print_pdf_modal">'
            + '<div class="modal-dialog modal-lg">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">请选择打印模版</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + '<iframe src="' + url + '" frameborder="0" width="100%" height="500" type="application/pdf"></iframe>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(printPdfHtml);
        $('#print_pdf_modal').modal('show');
    },
    //input提示信息
    placeholder: function (selector) {
        var elem = $(selector);
        var value = elem.prev().val();
        if (value) {
            elem.hide();
        } else {
            elem.show();
        }
        elem.on('click', function () {
            $(this).prev().focus();
        });
        elem.prev().on('change', function () {
            if ($(this).val() == '') {
                $(this).next().show();
            } else {
                $(this).next().hide();
            }
        });
        elem.prev().on('keyup', function () {
            if ($(this).val() == '') {
                $(this).next().show();
            } else {
                $(this).next().hide();
            }
        });
    },
    //加载图片
    loadImage: function (url, callback) {
        var img = new Image();
        img.src = url;

        img.onload = function () {
            callback.call(img);
        };
    },
    //array转成object对象
    getObjectByArray: function (arr) {
        var o = {};
        $.each(arr, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    },
    //指定错误信息位置
    errorPlacement: function (error, element) {
        if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
            var eid = element.attr('name'); //获取元素的name属性
            error.appendTo(element.parent().parent().parent()); //将错误信息添加当前元素的父结点后面
        } else {
            if (element.hasClass('select-tree-name')) {
                error.appendTo(element.parent().parent());
            } else {
                error.insertAfter(element);
            }
        }
    },
    showErrors: function (errorMap, errorList) {
        this.defaultShowErrors();
        $('.tab-pane').each(function (index, element) {
            if ($(this).has('.error:not(label)').length > 0) {
                $('a[href="#' + element.id + '"]').addClass('text-red');
            } else {
                $('a[href="#' + element.id + '"]').removeClass('text-red');
            }
        });

    },
    //兼容IE8 array.indexOf()方法
    arrayIndexOf: function (arr, value) {
        for (var i = 0; i < arr.length; i++) {
            var item = arr[i];
            if (item == value) {
                return i;
            }
        }
        return -1;
    },
    setDataValue: function (selector, data) {
        if (data) {
            for (var key in data) {
                var value = data[key];
                if (value || value == 0) {
                    var elem = null;
                    if (selector) {
                        elem = $(selector).find('#' + key);
                        console.log(elem.length);
                        elem.length || (elem = $(selector).find('[name="' + key + '"]'));
                    } else {
                        elem = $('#' + key);
                        elem.length || (elem = $('[name="' + key + '"]'));
                    }
                    if (elem.is(':checkbox')) {
                        elem.prop('checked', value);
                    } else if (elem.is(':radio')) {
                        if (selector) {
                            $(selector).find('input[name="' + key + '"][value="' + value + '"]').prop('checked', true);
                        } else {
                            $('input[name="' + key + '"][value="' + value + '"]').prop('checked', true);
                        }
                    } else {
                        elem.val(value);
                    }
                }
            }
        }
    },
    setDataText: function (selector, data) {
        if (data) {
            for (var key in data) {
                var value = data[key];
                if (value || value == 0) {
                    if (selector) {
                        $(selector).find('#' + key).text(value);
                    } else {
                        $('#' + key).text(value);
                    }
                }
            }
        }
    },
    showField: function (formSelector, field, value) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
            $(elem).parent().parent().parent().parent().parent().removeClass('hidden');
        } else {
            $(elem).parent().parent().parent().removeClass('hidden');
        }
        if (value !== undefined) {
            $(elem).val(value);
        }
    },
    hideField: function (formSelector, field, value) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
            $(elem).parent().parent().parent().parent().parent().addClass('hidden');
        } else {
            $(elem).parent().parent().parent().addClass('hidden');
        }
        if (value !== undefined) {
            $(elem).val(value);
        }
    },
    showModalField: function (formSelector, field, value) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
            $(elem).parent().parent().parent().parent().removeClass('hidden');
        } else {
            $(elem).parent().parent().removeClass('hidden');
        }
        if (value !== undefined) {
            $(elem).val(value);
        }
    },
    hideModalField: function (formSelector, field, value) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
            $(elem).parent().parent().parent().parent().addClass('hidden');
        } else {
            $(elem).parent().parent().addClass('hidden');
        }
        if (value !== undefined) {
            $(elem).val(value);
        }
    },
    //添加必填字段*
    addRequiredFieldsLabel: function (formSelector, requiredFields) {
        for (var i = 0; i < requiredFields.length; i++) {
            var item = requiredFields[i];
            var elem = $(formSelector).find('[name="' + item + '"]')[0];
            if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
                $(elem).parent().parent().parent().prev().append(' <span style="color:red;">*</span>');
            } else {
                $(elem).parent().prev().append(' <span style="color:red;">*</span>');
            }
        }
    },
    //添加字段验证
    addRules: function (formSelector, field, rules, isRequired) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).length > 0) {
            $(elem).rules('add', rules);
            if (isRequired) {
                if ($(elem).is(':radio') || $(elem).is(':checkbox')) {
                    var labelElem = $(elem).parent().parent().parent().prev();
                    if (labelElem.text().indexOf('*') < 0) {
                        labelElem.append(' <span style="color:red;">*</span>');
                    }
                    
                } else {
                    var labelElem = $(elem).parent().prev();
                    if (labelElem.text().indexOf('*') < 0) {
                        labelElem.append(' <span style="color:red;">*</span>');
                    }
                }
            }
        }
    },
    //删除字段验证
    removeRules: function (formSelector, field, ruleName, isRequired) {
        var elem = $(formSelector).find('[name="' + field + '"]')[0];
        if ($(elem).length > 0) {
            if (ruleName) {
                $(elem).rules('remove', ruleName);
            } else {
                $(elem).rules('remove');
            }
            if (isRequired) {
                if ($(elem).prop('type') == 'radio' || $(elem).prop('type') == 'checkbox') {
                    $(elem).parent().parent().parent().prev().find('span').remove();
                } else {
                    $(elem).parent().prev().find('span').remove();
                }
            }
        }
    },
    //格式化百分比
    formatPercent: function (value) {
        if (value) {
            var str = Number(value * 100).toFixed(2);
            str += '%';
            return str;
        }
        return null;
    },
    //格式化时间戳
    formatDate: function (timestamp) {
        if (timestamp) {
            return new Date(timestamp).Format('yyyy-MM-dd');
        } else return '';
    },
    //格式化时间戳
    formatDateTime: function (timestamp) {
        if (timestamp) {
            return new Date(timestamp).Format('yyyy-MM-dd hh:mm:ss');
        } else return '';
    },
    //年月日时间转成 xxxx-xx-xx格式
    formatDateByCn: function (cnDate) {
        if (cnDate != null) {
            if (cnDate.indexOf('年') > 0) {
                var date = cnDate.replace(new RegExp('年|月|日', 'gm'), '-');
                return date.substring(0, date.length - 1);
            } else {
                return cnDate;
            }
        } else {
            return '';
        }
    },
    //中文金额转成数字
    formatCurrencyByCn: function (cnCurrency) {
        if (cnCurrency) {
            var funds = cnCurrency.split('万', -1);
            return funds[0] * 10000;
        }
        return '';
    },
    //中文币种转换
    formatCurrencyTypeByCn: function (cnCurrencyType) {
        if (cnCurrencyType) {
            if (cnCurrencyType.indexOf('人') > 0) {
                return 'CNY';
            } else if (cnCurrencyType.indexOf('美')) {
                return 'USD';
            } else if (cnCurrencyType.indexOf('港')) {
                return 'HKD';
            } else if (cnCurrencyType.indexOf('欧')) {
                return 'EUR';
            } else if (cnCurrencyType.indexOf('韩')) {
                return 'KRW';
            } else if (cnCurrencyType.indexOf('日')) {
                return 'JPY';
            } else if (cnCurrencyType.indexOf('英')) {
                return 'GBP';
            } else if (cnCurrencyType.indexOf('新')) {
                return 'SGD';
            } else if (cnCurrencyType.indexOf('澳')) {
                return 'AUD';
            } else if (cnCurrencyType.indexOf('加')) {
                return 'CAD';
            } else {
                return '';
            }
        }
        return '';
    },
    //格式化类型状态
    formatType: function (value, list, fieldValue, fieldText) {
        fieldValue || (fieldValue = 'value');
        fieldText || (fieldText = 'text');
        if (list && list.length > 0) {
            for (i = 0; i < list.length; i++) {
                var item = list[i];
                if (item[fieldValue] == value) {
                    return item[fieldText];
                }
            }
        }
        return null;
    },
    //加载下拉框数据
    loadSelectOptions: function (selector, list, fieldValue, fieldText, firstItem) {

        if (!fieldText) {
            if (!fieldValue && fieldValue != false) {
                fieldValue = 'value';
                fieldText = 'text';
            } else {
                firstItem = fieldValue;
                fieldValue = 'value';
                fieldText = 'text';
            }
        }

        var optionsHtml = '';
        if (firstItem || firstItem == false) {
            if (typeof firstItem === 'string') {
                optionsHtml = '<option value="">' + firstItem + '</option>';
            } else if (firstItem != false) {
                optionsHtml = '<option value="' + firstItem[fieldValue] + '">' + firstItem[fieldText] + '</option>';
            }
        } else {
            optionsHtml = '<option value="">请选择</option>';
        }

        if (list && list.length > 0) {
            for (i = 0; i < list.length; i++) {
                var item = list[i];
                optionsHtml += '<option value="' + item[fieldValue] + '">' + item[fieldText] + '</option>';
            }
        }
        $(selector).html(optionsHtml);
    },
    //获得省市区
    getAreaName: function (areaCode, level, fn) {
        if (areaCode) {
            var areaCode1 = areaCode.substring(0, 2) + '0000';
            var areaCode2 = areaCode.substring(0, 4) + '00';
            var areaCode3 = areaCode;

            if (level == 1) {

                Util.getArea(null, 1, function (list) {
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (areaCode1 == item.areaCode) {
                            fn && fn(item.areaName);
                            return true;
                        }
                    }
                });
            } else if (level == 2) {

                Util.getArea(areaCode1, 2, function (list) {
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (areaCode2 == item.areaCode) {
                            fn && fn(item.areaName);
                            return true;
                        }
                    }
                });

            } else if (level == 3) {

                Util.getArea(areaCode2, 3, function (list) {
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (areaCode3 == item.areaCode) {
                            fn && fn(item.areaName);
                            return true;
                        }
                    }
                });

            } else {
                fn && fn('');
                return false;
            }
        } else {
            fn && fn('');
            return false;
        }
    },
    //获得省市区数据
    getArea: function (areaCode, level, fn) {
        $.get(Config.baseUrl + '/area/list?level=' + level + '&areaCode=' + areaCode, function (data) {
            if (data.rel) {
                fn && fn(data.result);
            }
        });
    },
    //省市区
    selectArea: function (provinceSelector, citySelector, areaSelector, areaCode, fn) {
        var provinceCode = areaCode && areaCode.substring(0, 2) + '0000';
        var cityCode = areaCode && areaCode.substring(0, 4) + '00';

        $(provinceSelector).on('change', function () {
            var value = $(this).val();
            fn && fn(1, value);
            Util.loadSelectOptions(citySelector, null);
            Util.loadSelectOptions(areaSelector, null);
            if (value) {
                Util.getArea(value, 2, function (cityList) {
                    Util.loadSelectOptions(citySelector, cityList, 'areaCode', 'areaName');
                });
            }
        });

        $(citySelector).on('change', function () {
            var value = $(this).val();
            fn && fn(2, value);
            Util.loadSelectOptions(areaSelector, null);
            if (value) {
                Util.getArea(value, 3, function (areaList) {
                    Util.loadSelectOptions(areaSelector, areaList, 'areaCode', 'areaName');
                });
            }
        });

        $(areaSelector).on('change', function () {
            var value = $(this).val();
            fn && fn(3, value);
        });

        Util.loadSelectOptions(provinceSelector, null);
        Util.loadSelectOptions(citySelector, null);
        Util.loadSelectOptions(areaSelector, null);

        Util.getArea(null, 1, function (provinceList) {
            Util.loadSelectOptions(provinceSelector, provinceList, 'areaCode', 'areaName');

            if (provinceCode) {
                $(provinceSelector).val(provinceCode);

                Util.getArea(provinceCode, 2, function (cityList) {
                    Util.loadSelectOptions(citySelector, cityList, 'areaCode', 'areaName');

                    if (cityCode) {
                        $(citySelector).val(cityCode);

                        Util.getArea(cityCode, 3, function (areaList) {
                            Util.loadSelectOptions(areaSelector, areaList, 'areaCode', 'areaName');

                            if (areaCode) {
                                $(areaSelector).val(areaCode);
                            }
                        });
                    }
                });
            }
        });

    },
    //选择机构
    selectOrg: function (options, handleConfirm, handleCancel) {
        $('#select_tree_modal').next('.modal-backdrop').remove();
        $('#select_tree_modal').remove();

        var title = '选择机构';
        var confirmText = '确定';
        var cancelText = '取消';

        if (typeof options === 'string') {
            title = options;
        } else {
            options.title && (title = options.title);
            options.confirmText && (confirmText = options.confirmText);
            options.cancelText && (cancelText = options.cancelText);
            options.handleConfirm && (handleConfirm = options.handleConfirm);
            options.handleCancel && (handleCancel = options.handleCancel);
        }

        var selectOrgHtml = '<div class="modal fade" id="select_tree_modal">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + title + '</h4>'
            + '</div>'
            + '<div class="modal-body" style="height: 380px; overflow: auto;">'
            + '<table class="table table-bordered tree-table" id="select_tree_list"></table>'
            + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-default pull-left" id="select_tree_modal_cancel" data-dismiss="modal">' + cancelText + '</button>'
            + '<button type="button" class="btn btn-primary" id="select_tree_modal_confirm">' + confirmText + '</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(selectOrgHtml);
        $('#select_tree_modal').modal('show');

        var treeList = new TreeTable({
            elem: '#select_tree_list',
            url: Config.baseUrl + '/organization/orgTree',
            head: false,
            columns: [
                {
                    field: 'name',
                    title: '机构',
                    formatter: function (value, row) {
                        return '<i class="fa fa-university text-primary"></i> ' + value
                    }
                }
            ],
            queryParams: function (pid) {
                var temp = {}
                if (pid) {
                    temp.parentId = pid;
                }
                return temp;
            },
            responseHandler: function (res) {
                if (res && res.data) {
                    return res.data;
                } else {
                    return res;
                }
            },
            itemHandler: function (item) {
                return {
                    id: item.id,
                    pid: item.parentId,
                    name: item.name,
                    hasChilds: item.childs
                }
            }
        });

        $('#select_tree_modal_confirm').off().on('click', function () {
            treeList.getOneSelected(function (id, name) {

                $('#select_tree_modal').modal('hide');
                handleConfirm && handleConfirm(id, name);
            });
        });

        $('#select_tree_modal_cancel').off().on('click', function () {
            $('#select_tree_modal').modal('hide');
            handleCancel && handleCancel();
        });

    },
    //选择经济行业分类
    selectIndustry: function (options, handleConfirm, handleCancel) {
        $('#select_tree_modal').next('.modal-backdrop').remove();
        $('#select_tree_modal').remove();

        var title = '选择经济行业分类';
        var confirmText = '确定';
        var cancelText = '取消';

        if (typeof options === 'string') {
            title = options;
        } else {
            options.title && (title = options.title);
            options.confirmText && (confirmText = options.confirmText);
            options.cancelText && (cancelText = options.cancelText);
            options.handleConfirm && (handleConfirm = options.handleConfirm);
            options.handleCancel && (handleCancel = options.handleCancel);
        }

        var selectTreeHtml = '<div class="modal fade" id="select_tree_modal">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + title + '</h4>'
            + '</div>'
            + '<div class="modal-body" style="height: 380px; overflow: auto;">'
            + '<table class="table table-bordered tree-table" id="select_tree_list"></table>'
            + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-default pull-left" id="select_tree_modal_cancel" data-dismiss="modal">' + cancelText + '</button>'
            + '<button type="button" class="btn btn-primary" id="select_tree_modal_confirm">' + confirmText + '</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(selectTreeHtml);
        $('#select_tree_modal').modal('show');

        var treeList = new TreeTable({
            elem: '#select_tree_list',
            data: Industry,
            getChildsData: function (pid, fn) {
                Util.getIndustryChilds(Industry, pid, 1, function (childs) {
                    fn && fn(childs);
                });
            },
            selectedParent: false,
            head: false,
            columns: [
                {
                    field: 'name',
                    title: '经济行业分类',
                    formatter: function (value, row) {
                        return '<i class="fa fa-briefcase text-primary"></i> ' + value
                    }
                }
            ],
            responseHandler: function (res) {
                if (res && res.data) {
                    return res.data;
                } else {
                    return res;
                }
            },
            itemHandler: function (item) {
                return {
                    id: item.ids,
                    name: item.names,
                    childs: item.childrens,
                    hasChilds: (item.childrens && item.childrens.length > 0) ? true : false
                }
            }
        });

        $('#select_tree_modal_confirm').off().on('click', function () {
            treeList.getOneSelected(function (id, name) {

                $('#select_tree_modal').modal('hide');
                handleConfirm && handleConfirm(id, name);
            });
        });

        $('#select_tree_modal_cancel').off().on('click', function () {
            $('#select_tree_modal').modal('hide');
            handleCancel && handleCancel();
        });

    },
    //获得经济行业分类ChildsData
    getIndustryChilds: function (list, code, level, fn) {

        var industryCode = '';
        if (level == 1) {
            industryCode = code.substr(0, 1);
        } else if (level == 2) {
            industryCode = code.substr(0, 3);
        } else {
            industryCode = code.substr(0, level + 1);
        }

        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if (item.ids == industryCode) {
                if (item.ids == code) {
                    fn && fn(item.childrens);
                } else {
                    level++;
                    Util.getIndustryChilds(item.childrens, code, level, fn);
                }
                return false;
            }
        }
    },
    //获得经济行业分类名称
    getIndustryName: function (code, fn) {
        var name = '';
        Util.getIndustryItem(Industry, name, code, 1, function (name) {

            fn && fn(name.substring(3));
        });
    },
    //获得经济行业分类名称
    getIndustryItem: function (list, name, code, level, fn) {
        var industryCode = '';
        if (level == 1) {
            industryCode = code.substr(0, 1);
        } else if (level == 2) {
            industryCode = code.substr(0, 3);
        } else {
            industryCode = code.substr(0, level + 1);
        }

        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if (item.ids == industryCode) {
                name += ' > ' + item.names;
                if (item.childrens) {
                    level++;
                    Util.getIndustryItem(item.childrens, name, code, level, fn);
                } else {
                    fn && fn(name);
                }
                return false;
            }
        }
    },
    //获得配置项
    getConfigByKey: function (key, fn) {
        $.get(Config.baseUrl + '/config/findByKey?configKey=' + key, function (data) {
            fn && fn(data);
        });
    },
    //列表
    bootstrapTable: function (elem, options) {
        return $(elem).bootstrapTable($.extend({}, {
            url: '',                                //请求后台的URL（*）
            method: 'get',                          //请求方式（*）
            toolbar: '#toolbar',                    //工具按钮用哪个容器
            striped: true,                          //是否显示行间隔色
            cache: false,                           //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                       //是否显示分页（*）
            sortable: false,                        //是否启用排序
            sortOrder: 'asc',                       //排序方式        
            sidePagination: 'server',               //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                          //初始化加载第一页，默认第一页
            pageSize: 10,                           //每页的记录行数（*）
            pageList: [10, 25, 50, 100],            //可供选择的每页的行数（*）
            search: false,                          //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                      //是否显示所有的列
            showRefresh: true,                      //是否显示刷新按钮
            minimumCountColumns: 2,                 //最少允许的列数
            clickToSelect: false,                   //是否启用点击选中行
            //height: 525,                          //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: 'id',                         //每一行的唯一标识，一般为主键列
            showToggle: true,                       //是否显示详细视图和列表视图的切换按钮
            cardView: false,                        //是否显示详细视图
            detailView: false,                      //是否显示父子表onEditableSave
            columns: [],
            queryParams: function (params) {        //传递参数（*）
                var temp = {
                    size: params.limit, //页面大小
                    page: params.offset / params.limit, //页码
                };
                return temp;
            },
            responseHandler: function (res) {       //{"total":100, "rows":[]}
                if (res && res.data) {
                    return { total: res.data.total, rows: res.data.list }
                } else {
                    return res;
                }
            }
        }, options));
    },
    getIdSelections: function (elem, fieldName) {
        fieldName || (fieldName = 'id');
        return $.map($(elem).bootstrapTable('getSelections'), function (row) {
            return row[fieldName];
        });
    },
    getRowSelections: function (elem) {
        return $.map($(elem).bootstrapTable('getSelections'), function (row) {
            return row
        });
    },
    getOneSelections: function (elem, fn) {
        var rows = this.getRowSelections(elem);
        if (rows.length == 1) {
            fn & fn(rows[0]);
        } else {
            this.message('请选中一行');
            return false;
        }
    },
    getMultiSelections: function (elem, fieldName, fn) {
        if (!fn) {
            fn = fieldName;
            fieldName = 'id';
        }
        var ids = this.getIdSelections(elem);
        var rows = this.getRowSelections(elem, fieldName);
        if (ids.length > 0) {
            fn && fn(ids, rows);
        } else {
            this.message('至少选中一行');
            return false;
        }
    }
}

//时间戳转为日期格式
Date.prototype.Format = function (fmt) {
    var o = {
        'M+': this.getMonth() + 1, // 月份
        'd+': this.getDate(), // 日
        'h+': this.getHours(), // 小时
        'm+': this.getMinutes(), // 分
        's+': this.getSeconds(), // 秒
        'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
        'S': this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + ''));
    for (var k in o)
        if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    return fmt;
}