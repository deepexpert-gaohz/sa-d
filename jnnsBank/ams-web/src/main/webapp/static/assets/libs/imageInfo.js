function ImageInfo(_options) {
    this.options = {
        elem: '',
        url: '',
        ajaxType: 'GET',
        ajaxData: {},
        isEdit: false, //是否可编辑
        data: null, //数据
        responseHandler: function (res) { //数据处理
            return res;
        },
        itemHandler: function (item, list) { //类型数据处理
            return item;
        },
        imgHandler: function (img, item, list) { //图片数据处理
            return img;
        },
        operates: [] //操作列表
    };
    this.init(_options);
};

ImageInfo.prototype = {
    // 初始化
    init: function (_options) {
        var that = this;
        that.set(_options);
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

        that.set(_options);
        that.viewData();
        return this;
    },
    //显示数据
    viewData: function () {
        var that = this;
        $.ajax({
            url: that.options.url,
            type: that.options.ajaxType,
            data: that.options.ajaxData,
            success: function (data, status) {
                if (status == 'success') {
                    var list = that.options.responseHandler(data);
                    that.options.data = list;
                    if (list && list.length > 0) {
                        that.viewList(list);
                        that.bindEvent();
                    } else {
                        that.noRecords();
                    }
                } else {
                    that.noRecords();
                }
            },
            error: function () {
                that.noRecords();
            }
        });
    },
    viewList: function (list) {
        var that = this;
        var listHtml = '';
        for (var i = 0; i < list.length; i++) {
            var item = that.options.itemHandler(list[i], that.options.data);

            var imgsHtml = '';
            var imgsCount = 0;
            if (item.imgs && item.imgs.length > 0) {
                for (var j = 0; j < item.imgs.length; j++) {
                    var img = that.options.imgHandler(item.imgs[j], item, that.options.data);
                    imgsHtml += '<div class="img-item" data-id="' + img.id + '"><a data-magnify="gallery" data-caption="' + img.title + '" href="' + img.imgUrl + '">' +
                        '<img src="' + img.thumbUrl + '"></a></div>';
                }
                imgsCount = item.imgs.length;
            } else {
                imgsHtml = '<div class="no-img">暂无图片</div>';
            }

            var operatesHtml = '';

            for (var j = 0; j < that.options.operates.length; j++) {
                var operate = that.options.operates[j];
                if (operate.imgShow && operate.editShow) { //有图片并且可编辑显示该操作
                    if (imgsCount > 0 && that.options.isEdit) {
                        operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
                    }
                } else if (operate.editShow) { //可编辑显示该操作
                    if (that.options.isEdit) {
                        operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
                    }
                } else if (operate.editShow == false) { //查看操作
                    if (that.options.isEdit == false) {
                        operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
                    }
                } else if (operate.imgShow) { //有图片显示该操作
                    if (imgsCount > 0) {
                        operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
                    }
                } else {
                    operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
                }
            }

            if (operatesHtml) {
                operatesHtml = '<div class="image-item-operate"><i class="fa fa-cog"></i><ul>' + operatesHtml + '</ul></div>';
            }

            var itemHtml = '<div class="image-item" data-id="' + item.id + '">' +
                '<div class="image-item-header"><h4 class="image-item-title">' + item.title + '</h4>' + operatesHtml + '</div>' +
                '<div class="image-item-body">' +
                '<div class="img-list">' + imgsHtml + '</div>' +
                '<div class="img-count"><span>' + imgsCount + ' 张</span></div>' +
                '</div>' +
                '</div>';

            listHtml += itemHtml;
        }
        $(that.options.elem).html(listHtml);

        $(this.options.elem + ' [data-magnify]').magnify({
            fixedContent: false
        });

        var imgList = $(this.options.elem).find('img');
        for (var i = 0; i < imgList.length; i++) {
            var item = imgList[i];
            $(item).on('load', function () {
                that.setImgSize($(this), 200);
            });
        }

    },
    noRecords: function () {
        $(this.options.elem).html('<div class="no-records">暂无相关信息</div>');
    },
    bindEvent: function () {
        var that = this;

        $(this.options.elem).off();
        for (var i = 0; i < that.options.operates.length; i++) {
            var operate = that.options.operates[i];
            $(this.options.elem).on('click', '.' + operate.name, operate.handle);
        }

    },
    getItem: function (id) {
        var that = this;
        var list = that.options.data;
        for (var i = 0; i < list.length; i++) {
            var item = that.options.itemHandler(list[i], that.options.data);
            if (item.id == id) {
                return item;
            }
        }
        return null;
    },
    setImgSize: function (imgElem, size, fn) {
        var width = imgElem.width();
        var height = imgElem.height();
        if (width && height) {
            if (width > height) {
                imgElem.height(size);
                var w = imgElem.width();
                var ml = Math.round((w - size) / 2);
                imgElem.css('margin-left', '-' + ml + 'px');
                imgElem.css('margin-top', '0px');
            } else {
                imgElem.width(size);
                var h = imgElem.height();
                var mt = Math.round((h - size) / 2);
                imgElem.css('margin-top', '-' + mt + 'px');
                imgElem.css('margin-left', '0px');
            }
        } else {
            fn && fn();
        }
    },
    viewItem: function (item) {
        var that = this;
        $('#view_image_item_modal').next('.modal-backdrop').remove();
        $('#view_image_item_modal').remove();

        var contentHtml = '<table class="table table-bordered table-info">' +
            '<tr><th>证件名称:</th><td>' + (item.name || '') + '</td></tr>' +
            '<tr><th>证件编号:</th><td>' + (item.no || '') + '</td></tr>' +
            '<tr><th>证件日期:</th><td>' + (item.date || '') + '</td></tr>' +
            '</table>';

        var modalHtml = '<div class="modal fade" id="view_image_item_modal">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + item.title + '</h4>'
            + '</div>'
            + '<div class="modal-body">' + contentHtml + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#view_image_item_modal').modal('show');

    },
    editItem: function (item, handleEdit) {
        var that = this;
        $('#edit_image_item_modal').next('.modal-backdrop').remove();
        $('#edit_image_item_modal').remove();

        var list = that.options.data;
        var contentHtml = '';

        contentHtml += '<div class="form-item">' +
            '<label class="col-sm-3 control-label">证件名称</label>' +
            '<div class="col-sm-8"><input type="text" class="form-control" name="name" value="' + (item.name || '') + '"></div>' +
            '</div>';

        contentHtml += '<div class="form-item">' +
            '<label class="col-sm-3 control-label">证件编号</label>' +
            '<div class="col-sm-8"><input type="text" class="form-control" name="no" value="' + (item.no || '') + '"></div>' +
            '</div>';

        contentHtml += '<div class="form-item">' +
            '<label class="col-sm-3 control-label">证件日期</label>' +
            '<div class="col-sm-8"><input type="text" class="form-control" name="date" value="' + (item.date || '') + '"></div>' +
            '</div>';

        if (item.title == '未知类型' || item.title === '未知类型(未上报)' || item.title == '未知类型(已上报)') {

            contentHtml += '<div class="form-item">' +
                '<label class="col-sm-3 control-label">影像类型</label>' +
                '<div class="col-sm-8">' +
                '<select class="form-control" id="code" name="code">' +
                '<option value="">请选择</option>';

            var list = that.options.data;
            for (var i = 0; i < list.length; i++) {
                var item = that.options.itemHandler(list[i]);
                if (item.id == item.id) {
                    contentHtml += '<option value="' + item.code + '" selected>' + item.title + '</option>';
                } else {
                    contentHtml += '<option value="' + item.code + '">' + item.title + '</option>';
                }
            }
            contentHtml += '</select></div></div>';
        }

        contentHtml = '<div class="clearfix">' + contentHtml + '</div>'

        var modalHtml = '<div class="modal fade" id="edit_image_item_modal">'
            + '<div class="modal-dialog">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + item.title + '</h4>'
            + '</div>'
            + '<form class="form-horizontal" id="edit_form">'
            + '<div class="modal-body">' + contentHtml + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-primary" id="edit_image_item_modal_ok">保存</button>'
            + '<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            + '</div>'
            + '</form>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#edit_image_item_modal').modal('show');

        $('#edit_image_item_modal').on('shown.bs.modal', function () {
            $('#edit_image_item_modal').find('[name="date"]').datepicker({
                autoclose: true,
                language: 'zh-CN',
                format: 'yyyy-mm-dd'
            });
        });

        $('#edit_image_item_modal_ok').off().on('click', function () {

            var newItem = {
                name: $('#edit_image_item_modal').find('[name="name"]').val(),
                no: $('#edit_image_item_modal').find('[name="no"]').val(),
                date: $('#edit_image_item_modal').find('[name="date"]').val(),
                code: ''
            }
            if ($('#edit_image_item_modal').find('[name="code"]').length > 0) {
                newItem.code = $('#edit_image_item_modal').find('[name="code"]').val()
            }

            if (!newItem.name) {
                Util.message('请输入证件名称');
                return false;
            }
            if (!newItem.no) {
                Util.message('请输入证件编号');
                return false;
            }
            if (!newItem.date) {
                Util.message('请选择日期');
                return false;
            }
            handleEdit && handleEdit(newItem);
            $('#edit_image_item_modal').modal('hide');
        });

    },
    showImgs: function (item, handleMove, handleDelete) {
        var that = this;
        $('#imgs_modal').next('.modal-backdrop').remove();
        $('#imgs_modal').remove();

        var imgsHtml = '';
        if (item.imgs && item.imgs.length > 0) {
            for (var j = 0; j < item.imgs.length; j++) {
                var img = that.options.imgHandler(item.imgs[j], item, that.options.data);

                imgsHtml += '<div class="img-box" data-id="' + img.id + '">';
                if (that.options.isEdit) {
                    imgsHtml += '<i class="fa fa-circle"></i>';
                }
                imgsHtml += '<a data-magnify="gallery" data-caption="' + item.title + '" href="' + img.imgUrl + '">' +
                    '<img src="' + img.thumbUrl + '"></a></div>';
            }
        }

        var btnsHtml = '';
        if (that.options.isEdit) {
            btnsHtml += '<button type="button" class="btn btn-primary" id="imgs_modal_checkall">全选</button>';
            btnsHtml += '<button type="button" class="btn btn-primary" id="imgs_modal_cancel_checkall">取消全选</button>';
            btnsHtml += '<button type="button" class="btn btn-primary" id="imgs_modal_move">移动</button>';
            btnsHtml += '<button type="button" class="btn btn-primary" id="imgs_modal_delete">删除</button>';
        }
        var modalHtml = '<div class="modal fade" id="imgs_modal">'
            + '<div class="modal-dialog modal-lg">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + item.title + '</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + '<div class="image-item-imgs">' + imgsHtml + '</div>'
            + '</div>'
            + '<div class="modal-footer">'
            + btnsHtml
            + '<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#imgs_modal').modal('show');

        $('#imgs_modal').on('shown.bs.modal', function () {

            $('#imgs_modal [data-magnify]').magnify({
                fixedContent: false
            });

            var imgList = $('#imgs_modal').find('img');
            for (var i = 0; i < imgList.length; i++) {
                var item = imgList[i];

                that.setImgSize($(item), 260, function () {
                    $(item).on('load', function () {
                        that.setImgSize($(this), 260);
                    });
                });
            }

        });

        $('#imgs_modal').find('.img-box i').on('click', function () {
            var elem = $(this);
            if (elem.hasClass('fa-circle')) {
                elem.removeClass('fa-circle').addClass('fa-check-circle');
            } else {
                elem.removeClass('fa-check-circle').addClass('fa-circle');
            }
        });

        $('#imgs_modal_checkall').off().on('click', function () {
            var list = $('#imgs_modal').find('.img-box i.fa-circle');
            for (var i = 0; i < list.length; i++) {
                $(list[i]).removeClass('fa-circle').addClass('fa-check-circle');
            }
        });

        $('#imgs_modal_cancel_checkall').off().on('click', function () {
            var list = $('#imgs_modal').find('.img-box i.fa-check-circle');
            for (var i = 0; i < list.length; i++) {
                $(list[i]).removeClass('fa-check-circle').addClass('fa-circle');
            }
        });

        $('#imgs_modal_move').off().on('click', function () {
            var list = $('#imgs_modal').find('.img-box i.fa-check-circle');
            var imgIds = [];
            for (var i = 0; i < list.length; i++) {
                var id = $(list[i]).parent().data('id');
                imgIds.push(id);
            }

            if (imgIds.length == 0) {
                Util.message('请选择图片');
                return false;
            }

            that.imgsMove(imgIds, item, handleMove);
        });

        $('#imgs_modal_delete').off().on('click', function () {
            var list = $('#imgs_modal').find('.img-box i.fa-check-circle');
            var imgIds = [];
            for (var i = 0; i < list.length; i++) {
                var id = $(list[i]).parent().data('id');
                imgIds.push(id);
            }

            if (imgIds.length == 0) {
                Util.message('请选择图片');
                return false;
            }
            Util.confirm('确定删除选中图片吗？', function () {
                handleDelete && handleDelete(imgIds);
                $('#imgs_modal').modal('hide');
            });

        });

    },
    imgsMove: function (imgIds, item, handleMove) {
        var that = this;
        $('#imgs_move_modal').next('.modal-backdrop').remove();
        $('#imgs_move_modal').remove();

        var list = that.options.data;
        var contentHtml = '';
        contentHtml += '<select class="form-control" name="itemId">'
        contentHtml += '<option value="">请选择影像类型</option>';

        for (var i = 0; i < list.length; i++) {
            var _item = that.options.itemHandler(list[i]);
            if (item.id != _item.id) {
                contentHtml += '<option value="' + _item.id + '">' + _item.title + '</option>';
            }
        }
        contentHtml += '</select>';

        var modalHtml = '<div class="modal fade" id="imgs_move_modal">'
            + '<div class="modal-dialog modal-sm">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">移动图片</h4>'
            + '</div>'
            + '<div class="modal-body">' + contentHtml + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-primary" id="imgs_move_modal_ok">确定</button>'
            + '<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';


        $('body').append(modalHtml);
        $('#imgs_move_modal').modal('show');

        $('#imgs_move_modal_ok').off().on('click', function () {
            var newItemId = $('#imgs_move_modal').find('[name="itemId"]').val();
            if (!newItemId) {
                Util.message('请选择影像类型');
                return false;
            }
            handleMove && handleMove(imgIds, newItemId);
            $('#imgs_move_modal').modal('hide');
            $('#imgs_modal').modal('hide');
        });

    },
    updateImgs: function (url, count) {
        var that = this;
        count || (count = 10);
        $('#upload_modal').next('.modal-backdrop').remove();
        $('#upload_modal').remove();

        var uploadHtml = '<div class="modal fade" id="upload_modal">'
            + '<div class="modal-dialog modal-sm">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">上传图片</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + '<div id="uploader">'
            + '<div class="btns">'
            + '<div id="picker">选择图片</div>'
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

            var uploader = WebUploader.create({
                //选完文件后，是否自动上传
                auto: true,
                //swf文件路径
                swf: Config.baseUrl + '/plugins/webuploader/Uploader.swf',
                //文件接收服务端。
                server: url,
                //选择文件的按钮。可选。
                //内部根据当前运行是创建，可能是input元素，也可能是flash.
                pick: '#picker',
                //指定接受哪些类型的文件
                accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/*'
                },
                fileNumLimit: count,
                fileSizeLimit: 5 * 1024 * 1024,        // 200 M
                fileSingleSizeLimit: 1 * 1024 * 1024        // 50 M
            });

            //某个文件开始上传前触发，一个文件只会触发一次。
            uploader.on('uploadStart', function (file) {
                $('#uploader_msg').text('上传中...');
            });
            //当文件上传成功时触发。
            uploader.on('uploadSuccess', function (file, response) {
                $('#uploader_msg').text('已上传');
                that.refresh();
            });
            //当文件上传出错时触发。
            uploader.on('uploadError', function (file, reason) {
                $('#uploader_msg').text('上传失败');
            });
            //不管成功或者失败，文件上传完成时触发。
            uploader.on('uploadComplete', function (file) {
                $('#upload_modal').modal('hide');
            });

        });

    }
}
