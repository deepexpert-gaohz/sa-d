function VideoInfo(_options) {
    this.options = {
        elem: '',
        url: '',
        ajaxType: 'GET',
        ajaxData: {},
        isEdit: true, //是否可编辑
        data: null, //数据
        responseHandler: function (res) { //处理数据
            return res;
        },
        itemHandler: function (item, list) { //处理数据
            return item;
        },
        operates: [] //操作列表
    };
    this.init(_options);
};

VideoInfo.prototype = {
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

                    var data = {
                        code: 'ACK',
                        data: [
                            {
                                id: 11111,
                                title: 'Big Buck Bunny Trailer',
                                videoUrl: 'http://www.jplayer.org/video/m4v/Big_Buck_Bunny_Trailer.m4v',
                                imgUrl: 'http://www.jplayer.org/video/poster/Big_Buck_Bunny_Trailer_480x270.png',
                            },
                            {
                                id: 2222,
                                title: 'Incredibles Teaser',
                                videoUrl: 'http://www.jplayer.org/video/m4v/Incredibles_Teaser.m4v',
                                imgUrl: 'http://www.jplayer.org/video/poster/Incredibles_Teaser_640x272.png',
                            },
                            {
                                id: 3333,
                                title: 'Finding Nemo Teaser',
                                videoUrl: 'http://www.jplayer.org/video/m4v/Finding_Nemo_Teaser.m4v',
                                imgUrl: 'http://www.jplayer.org/video/poster/Finding_Nemo_Teaser_640x352.png',
                            }
                        ]
                    }

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

            var operatesHtml = '';

            for (var j = 0; j < that.options.operates.length; j++) {
                var operate = that.options.operates[j];
                operatesHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="' + operate.name + '">' + operate.text + '</a></li>';
            }

            if (operatesHtml) {
                operatesHtml = '<div class="video-item-operate"><i class="fa fa-cog"></i><ul>' + operatesHtml + '</ul></div>';
            }

            var itemHtml = '<div class="video-item">' +
                '<div class="video-item-header"><h4 class="video-item-title">' + item.title + '</h4>' + operatesHtml + '</div>' +
                '<div class="video-item-body">' +
                '<img src="' + item.imgUrl + '">' +
                '<i class="fa fa-play-circle-o video-play" data-id="' + item.id + '"></i>' +
                '</div>' +
                '</div>';

            listHtml += itemHtml;
        }
        $(that.options.elem).html(listHtml);

        var imgList = $(this.options.elem).find('img');
        for (var i = 0; i < imgList.length; i++) {
            var item = imgList[i];
            $(item).on('load', function () {
                that.setImgSize($(this), 320, 180);
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

        $(this.options.elem).on('click', '.video-play', function () {
            var id = $(this).data('id');
            var item = that.getItem(id);
            that.videoPlay(item);
        });

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
    setImgSize: function (imgElem, width, height, fn) {
        var imgW = imgElem.width();
        var imgH = imgElem.height();
        if (imgW && imgH) {
            if (imgW/width > imgH/height) {
                imgElem.height(height);
                var w = imgElem.width();
                var ml = Math.round((w - width) / 2);
                imgElem.css('margin-left', '-' + ml + 'px');
                imgElem.css('margin-top', '0px');
            } else {
                imgElem.width(width);
                var h = imgElem.height();
                var mt = Math.round((h - height) / 2);
                imgElem.css('margin-top', '-' + mt + 'px');
                imgElem.css('margin-left', '0px');
            }
        } else {
            fn && fn();
        }
    },
    videoPlay: function (item) {
        var that = this;
        $('#video_play_modal').next('.modal-backdrop').remove();
        $('#video_play_modal').remove();

        var contentHtml = '<div id="jp_container_1" class="jp-video jp-video-360p" role="application" aria-label="media player">' +
            '    <div class="jp-type-single">' +
            '        <div id="jquery_jplayer_1" class="jp-jplayer"></div>' +
            '        <div class="jp-gui">' +
            '            <div class="jp-video-play">' +
            '                <button class="jp-video-play-icon" role="button" tabindex="0">play</button>' +
            '            </div>' +
            '            <div class="jp-interface">' +
            '                <div class="jp-progress">' +
            '                    <div class="jp-seek-bar">' +
            '                        <div class="jp-play-bar"></div>' +
            '                    </div>' +
            '                </div>' +
            '                <div class="jp-current-time" role="timer" aria-label="time">&nbsp;</div>' +
            '                <div class="jp-duration" role="timer" aria-label="duration">&nbsp;</div>' +
            '                <div class="jp-controls-holder">' +
            '                    <div class="jp-controls">' +
            '                        <button class="jp-play" role="button" tabindex="0">play</button>' +
            '                        <button class="jp-stop" role="button" tabindex="0">stop</button>' +
            '                    </div>' +
            '                    <div class="jp-volume-controls">' +
            '                        <button class="jp-mute" role="button" tabindex="0">mute</button>' +
            '                        <button class="jp-volume-max" role="button" tabindex="0">max volume</button>' +
            '                        <div class="jp-volume-bar">' +
            '                            <div class="jp-volume-bar-value"></div>' +
            '                        </div>' +
            '                    </div>' +
            '                    <div class="jp-toggles">' +
            '                        <button class="jp-repeat" role="button" tabindex="0">repeat</button>' +
            '                        <button class="jp-full-screen" role="button" tabindex="0">full screen</button>' +
            '                    </div>' +
            '                </div>' +
            '                <div class="jp-details">' +
            '                    <div class="jp-title" aria-label="title">&nbsp;</div>' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="jp-no-solution">' +
            '            <span>Update Required</span>' +
            '            To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.' +
            '        </div>' +
            '    </div>' +
            '</div>';

        var modalHtml = '<div class="modal fade" id="video_play_modal">'
            + '<div class="modal-dialog modal-video-play">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">' + item.title + '</h4>'
            + '</div>'
            + '<div class="modal-body">' + contentHtml + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#video_play_modal').modal('show');

        $('#video_play_modal').on('shown.bs.modal', function () {

            $('#jquery_jplayer_1').jPlayer({
                ready: function () {
                    $(this).jPlayer('setMedia', {
                        title: item.title,
                        m4v: item.videoUrl,
                        poster: item.imgUrl
                    });
                },
                swfPath: '../plugins/jPlayer/jplayer',
                supplied: 'webmv, ogv, m4v',
                size: {
                    width: '640px',
                    height: '360px',
                    cssClass: 'jp-video-360p'
                },
                useStateClassSkin: true,
                autoBlur: false,
                smoothPlayBar: true,
                keyEnabled: true,
                remainingDuration: true,
                toggleDuration: true
            });
        });
        
        $('#video_play_modal').on('hidden.bs.modal', function () {
            $('#jquery_jplayer_1').jPlayer('stop');
        });
    },
    //发起双录
    videoRecord: function (item) {
        var that = this;
        $('#video_record_modal').next('.modal-backdrop').remove();
        $('#video_record_modal').remove();

        list = [
            { name:'1、您是否为XX公司代表人XXX？' },
            { name:'2、现XXXXXX公司将在本行开立基本存款账户，您是否知晓。' },
            { name:'3、您是否XX公司法定代表人XXX？' },
            { name:'4、现XXXX公司在本行开立基本存款户，您是否为XX公司法定代表人XXX？' },
            { name:'5、您是否为XX公司代表人XXX？' },
            { name:'6、现XXXXXX公司将在本行开立基本存款账户，您是否知晓。' }
        ]

        var contentHtml = '';
        contentHtml +=    '<div class="clearfix"><div class="video-remind"><h5>双录意愿核实：</h5><ul id="video_remind_list">';

        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            contentHtml +=    '<li>' + item.name + '</li>'
        }
        contentHtml += '</ul></div>';
    
        contentHtml += '<object class="video-record" id="video_record">' +
            '双录拍摄区域' +
            '</object></div>';

        var modalHtml = '<div class="modal fade" id="video_record_modal">'
            + '<div class="modal-dialog modal-lg modal-video-record">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">双录拍摄</h4>'
            + '</div>'
            + '<div class="modal-body">' + contentHtml + '</div>'
            + '<div class="modal-footer">'
            + '<button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>'
            + '<button type="button" class="btn btn-primary" id="video_record_modal_ok">确定</button>'
            + '</div>'
            + '</div>'
            + '</div>'
            + '</div>';

        $('body').append(modalHtml);
        $('#video_record_modal').modal('show');

        //确定
        $('#video_record_modal_ok').off().on('click', function () {
            $('#video_record_modal').modal('hide');
        });
    },
    //本地上传
    updateVideo: function (url) {
        var that = this;

        $('#upload_modal').next('.modal-backdrop').remove();
        $('#upload_modal').remove();

        var uploadHtml = '<div class="modal fade" id="upload_modal">'
            + '<div class="modal-dialog modal-sm">'
            + '<div class="modal-content">'
            + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>'
            + '<h4 class="modal-title">上传视频</h4>'
            + '</div>'
            + '<div class="modal-body">'
            + '<div id="uploader">'
            + '<div class="btns">'
            + '<div id="picker">选择视频</div>'
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
                    title: 'Videos',
                    extensions: 'mp4, avi, rmvb',
                    mimeTypes: 'video/*'
                }    
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
