layui.config({
    base: '../js/'
});
layui.define(['jquery', 'element', 'form', 'laydate', 'carousel'], function (exports) {
    var $ = layui.jquery,
        element = layui.element,
        form = layui.form,
        laydate = layui.laydate,
        carousel = layui.carousel,
        imageInfo = function () {
            this.config = {
                elem: '',
                data: [],
                tempId: '',
                acctId: 0,
                acctBillsId: 0,
                uploadUrl: '../../jnnsMovie/uploadImage', //上传地址
                downloadUrl: '',//下载地址
                downloadZipUrl: '',//批量下载地址
                editImageUrl: '',//编辑地址
                deleteImageUrl: '',//删除地址
                sendImageUrl: '',//传送影像平台地址
                handleUpload: null, //上传
                handleDownload: null, //下载
                handleEdit: null, //编辑
                handleDelete: null, //删除
                editBox: null,
                readOnly: true,
                downloadType: 1,
                isSend: false
            };
        };
    imageInfo.prototype = {
        renderUI: function () {
            var html = '';
            for(i=0; i<this.config.data.length; i++) {
                var item = this.config.data[i];
                var imgsHtml = '';
                var imgsCount = 0;
                var operateHtml = '';
                var isReport = item.type.substring(item.type.length - 4, item.type.length - 1) === '已上报';
                var downloadUrl = this.config.downloadUrl;
                var editName = this.config.readOnly ? "查看" : "编辑";

                if(item.list && item.list.length > 0) {
                    imgsCount = item.list.length;
                    for(j=0; j<item.list.length; j++) {
                        var img = item.list[j];
                        if (j == 0) {
                            imgsHtml += '<div class="swiper-slide" data-id="' + img.id + '"><a data-magnify="gallery"   href="' +img.imgUrl + '">'
                                + '<img width="200" src="' +img.imgUrl + '"></a></div>';
                        } else {
                            imgsHtml += '<div style="display:none;" class="swiper-slide" data-id="' + img.id + '"><a data-magnify="gallery"  href="' +img.imgUrl + '">'
                                + '<img width="200" src="' +img.imgUrl + '" alt=""></a></div>';
                        }
                    }
                }

                if(imgsHtml == '') {
                    imgsHtml = '<div class="no-image">暂无图片</div>';
                    operateHtml ='';
                    if(this.config.readOnly){
                        operateHtml +='<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-update">上传</a></li>';
                    }
                    operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" data-type="' + item.type + '" data-name="' + item.name + '" data-no="' + item.no + '" data-date="' + item.date + '" class="btn-image-edit">' + editName + '</a></li>';
                } else {
                    operateHtml ='';
                    if(this.config.readOnly){
                        operateHtml +='<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-update">上传</a></li>';
                    }
                }

                var itemHtml = '<div class="image-item">' +
                    '<div class="image-item-header">' +
                    '<div class="image-item-operate"><i class="layui-icon">&#xe716;</i>' +
                    '<ul>' + operateHtml + '</ul>' +
                    '</div>' +
                    '</div>' +
                    '<div class="image-item-body">' +
                    '<div class="swiper-container" id="swiper' + i + '">' +
                    '<div class="swiper-wrapper">' + imgsHtml + '</div>' +
                    '</div>' +
                    '<div class="image-count"><span>' + imgsCount + '张</span></div>' +
                    '</div>' +
                    '</div>';
                html += itemHtml;
            }


            html += '<iframe id="downloadFrame" src="" style="display: none;"></iframe>';
            $(this.config.elem).html(html);
        },
        bindUI: function () {
            var that = this;
            $('[data-magnify]').magnify({
                fixedContent: false
            });

            $('.btn-image-update').click(function (){
                var elem = $(this);
                var tempId = that.config.tempId;
                var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
                var acctBillsId = that.config.acctBillsId == undefined ? 0: that.config.acctBillsId;

                layer.open({
                    type : 1,
                    title : '图片上传',
                    skin : 'layui-layer-rim',
                    area : '300px',
                    content : '<div style="padding: 15px;" id="uploadimg"><div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择图片</div> </div>'
                });
                var uploader = WebUploader.create({
                    auto: true, // 选完文件后，是否自动上传
                    swf: '../js/webuploader/Uploader.swf', // swf文件路径
                    server: that.config.uploadUrl + '?acctBillsId=' + acctBillsId+"&tempId="+tempId+"&acctId="+acctId, // 文件接收服务端
                    pick: '#imgPicker', // 选择文件的按钮。可选
                    fileNumLimit: 100,//一次最多上传五张
                    // 只允许选择图片文件。
                    accept: {
                        title: 'Images',
                        extensions: 'jpg',
                        mimeTypes: 'image/!*'
                    }
                });
                uploader.on( 'uploadSuccess', function( file, res ) {
                    layer.closeAll();
                    layer.msg("影像导入成功，正在上传到影像平台，请稍等！上传成功后页面自动刷新");
                    //10秒后触发
                    setTimeout(function(){
                        window.location.reload();
                    },10000);
                });
                uploader.on( 'uploadError', function( file, reason ) {
                    layer.closeAll();
                    layer.msg('上传失败');
                });
                uploader.on("error",function(err){
                    //Q_TYPE_DENIED
                    if(err=="Q_TYPE_DENIED"){
                        layer.msg('不支持该格式，请上传jpg格式的图片！');
                    }else{
                        layer.msg('上传失败:'+err);
                    }
                });
            });
        },
        syncUI: function () {

        },
        destructor: function () {

        },
        refresh: function(cfg) {
            $.extend(this.config, cfg);
            this.renderUI();
            this.bindUI();
            this.syncUI();
            return this;
        },
        init: function (cfg) {
            $.extend(this.config, cfg);
            this.renderUI();
            this.bindUI();
            this.syncUI();
            this.config.tempId = new Date().getTime()+""+Math.floor(Math.random()*100000);
            if($("#tempId").length>0){
                $("#tempId").val(this.config.tempId);
            }
            return this;
        }
    }

    exports('imageInfo', function (options) {
        var o = new imageInfo();
        return o.init(options);
    });
});