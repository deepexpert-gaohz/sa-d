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
				uploadUrl: '', //上传地址
				downloadUrl: '',//下载地址
				downloadZipUrl: '',//批量下载地址
				editImageUrl: '',//编辑地址
				deleteImageUrl: '',//删除地址
				batchDeleteImageUrl: '',//批量删除地址
				batchMoveImageUrl: '',//批量移动地址
				sendImageUrl: '',//传送影像平台地址
				handleUpload: null, //上传
				handleDownload: null, //下载
				handleEdit: null, //编辑
				handleDelete: null, //删除
				handleBatchDelete: null, //批量删除
				handleBatchMove: null, //批量移动
				editBox: null,
				readOnly: false,
				downloadType: 1,
				isSend: false
			};
		};

	imageInfo.prototype = {
		renderUI: function () {
			var html = '';
			for (i = 0; i < this.config.data.length; i++) {

				var item = this.config.data[i];
				var imgsHtml = '';
				var imgsCount = 0;
				var operateHtml = '';
				var isReport = item.type.substring(item.type.length - 4, item.type.length - 1) === '已上报';
				var downloadUrl = this.config.downloadUrl;
				var editName = this.config.readOnly ? "查看" : "编辑";

				if (item.list && item.list.length > 0) {
					imgsCount = item.list.length;
					for (j = 0; j < item.list.length; j++) {
						var img = item.list[j];
						imgsHtml += '<div ' + (j === 0 ? '' : 'style="display:none;"') + ' class="swiper-slide" data-id="' + img.id + '">' +
							'<a data-magnify="gallery" data-caption="' + item.type + '" href="' + (isReport ? '' : downloadUrl + '?fileName=') + img.imgUrl + '">' +
							'<img src="' + (isReport ? '' : (downloadUrl + '?fileName=')) + img.imgUrl + '" alt="">' +
							'</a>' +
							'</div>';
					}
				}

				if (imgsHtml == '') {
					imgsHtml = '<div class="no-image">暂无图片</div>';
					operateHtml = '';
					if (!this.config.readOnly) {
						operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-update">上传</a></li>';

						if (this.config.isSend) {
							operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-send">传送影像平台</a></li>';
						}
					}
					operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" data-type="' + item.type + '" data-name="' + item.name + '" data-no="' + item.no + '" data-date="' + item.date + '" class="btn-image-edit">' + editName + '</a></li>';
				} else {
					operateHtml = '';
					operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" data-type="' + item.type + '" class="btn-image-view-all">查看全部</a></li>';
					if (!this.config.readOnly) {
						operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-update">上传</a></li>';

						if (this.config.isSend) {
							operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-send">传送影像平台</a></li>';
						}
					}
					operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" data-url="' + item.url + '" class="btn-image-download">下载</a></li>' +
						'<li><a href="javascript:;" data-id="' + item.id + '" data-type="' + item.type + '" data-name="' + item.name + '" data-no="' + item.no + '" data-date="' + item.date + '" class="btn-image-edit">' + editName + '</a></li>';
					if (!this.config.readOnly) {
						operateHtml += '<li><a href="javascript:;" data-id="' + item.id + '" class="btn-image-delete">删除</a></li>';
					}
				}

				var itemHtml = '<div class="image-item">' +
					'<div class="image-item-header">' +
					'<h4 class="image-item-title">' + item.type + '</h4>' +
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

			$(this.config.elem).find('img').on('load', function () {
				var width = $(this).width();
				var height = $(this).height();
				if (width > height) {
					$(this).height(200);
					var w = $(this).width();
					var ml = Math.round((w - 200)/2);
					$(this).css('margin-left', '-' + ml + 'px');
				} else {
					$(this).width(200);
					var h = $(this).height();
					var mt = Math.round((h - 200)/2);
					$(this).css('margin-top', '-' + mt + 'px');
				}
			});
		},
		bindUI: function () {
			var that = this;
			$('[data-magnify]').magnify({
				fixedContent: false
			});

			$('.btn-image-view-all').click(function () {
				if (that.config.viewAllBox) return;
				var elem = $(this);
				var imageTypeId = elem.data('id');
				var type = elem.data('type');
				
				var imgsHtml = '';
				for (i = 0; i < that.config.data.length; i++) {

					var item = that.config.data[i];
					if (item.id == imageTypeId) {
						var isReport = item.type.substring(item.type.length - 4, item.type.length - 1) === '已上报';
						var downloadUrl = that.config.downloadUrl;

						imgsHtml += '<div class="image-item-all">';

						for (j = 0; j < item.list.length; j++) {
							var img = item.list[j];

							imgsHtml += '<div class="img-box" data-id="' + img.id + '">';
							if (!that.config.readOnly) {
								imgsHtml += '<i class="fa fa-circle"></i>';
							}
							imgsHtml += '<a data-magnify-item="gallery" data-caption="' + item.type + '" href="' + (isReport ? '' : downloadUrl + '?fileName=') + img.imgUrl + '">' +
								'<img src="' + (isReport ? '' : (downloadUrl + '?fileName=')) + img.imgUrl + '" alt="">' +
								'</a>' +
								'</div>';
						}
						imgsHtml += '</div>';
					}
				}

				var btns = null;
				if (!that.config.readOnly) {
					btns = ['全选', '取消全选', '移动', '删除', '取消'];
				}
				

				that.config.viewAllBox = layer.open({
					id: 7,
					type: 1,
					title: type,
					zIndex: 100,
					content: imgsHtml,
					btn: btns,
					shade: false,
					area: ['100%', '100%'],
					yes: function(index, layero){ //全选

						var list = layero.find('.img-box i.fa-circle');
						for (var i = 0; i < list.length; i++) {
							$(list[i]).removeClass('fa-circle').addClass('fa-check-circle');
						}
						return false;
					},
					btn2: function(index, layero){ //取消全选
						var list = layero.find('.img-box i.fa-check-circle');
						for (var i = 0; i < list.length; i++) {
							$(list[i]).removeClass('fa-check-circle').addClass('fa-circle');
						}
						return false;
					},
					btn3: function(index, layero){ //移动
						var list = layero.find('.img-box i.fa-check-circle');
						var ids = [];
						for (var i = 0; i < list.length; i++) {
							var id = $(list[i]).parent().data('id');
							ids.push(id);
						}

						if(ids.length == 0) {
							layer.msg('请选择图片');
							return false;
						}

						that.batchMove(imageTypeId, ids);

						return false;
					},
					btn4: function(index, layero){ //删除

						var list = layero.find('.img-box i.fa-check-circle');
						var ids = [];
						for (var i = 0; i < list.length; i++) {
							var id = $(list[i]).parent().data('id');
							ids.push(id);
						}

						if(ids.length == 0) {
							layer.msg('请选择图片');
							return false;
						}

						layer.confirm('确定删除选中的图片吗？', {
							btn: ['确定', '取消']
						}, function () {
							$.ajax({
								url: that.config.batchDeleteImageUrl,
								type: 'POST',
								data:{ids:ids},
								dataType: 'json',
								success: function (data) {
									if (data.code === 'ACK') {
										layer.msg('删除成功');
									} else {
										layer.msg(data.message);
									}
									layer.closeAll();
									that.config.handleBatchDelete && that.config.handleBatchDelete(imageTypeId, ids);
								}
		
							});
						});
						
						
						return false;
					},
					success: function (layero, index) {

						$('[data-magnify-item]').magnify({
							fixedContent: false
						});

						layero.find('img').on('load', function () {
							var width = $(this).width();
							var height = $(this).height();
							if (width > height) {
								$(this).height(300);
								var w = $(this).width();
								var ml = Math.round((w - 300)/2);
								$(this).css('margin-left', '-' + ml + 'px');
							} else {
								$(this).width(300);
								var h = $(this).height();
								var mt = Math.round((h - 300)/2);
								$(this).css('margin-top', '-' + mt + 'px');
							}
						});

						layero.find('.img-box i').on('click', function () {

							var elem = $(this);
							if (elem.hasClass('fa-circle')) {
								elem.removeClass('fa-circle').addClass('fa-check-circle');
							} else {
								elem.removeClass('fa-check-circle').addClass('fa-circle');
							}

						});

					},
					end: function () {
						that.config.viewAllBox = null;
					}
				});

			});

			$('.btn-image-update').click(function () {
				var elem = $(this);
				// var id = elem.data('id');
				var imageTypeId = elem.data('id');
				var tempId = that.config.tempId;
				var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
				var acctBillsId = that.config.acctBillsId == undefined ? 0 : that.config.acctBillsId;

				layer.open({
					type: 1,
					title: '图片上传',
					skin: 'layui-layer-rim',
					area: '300px',
					content: '<div style="padding: 15px;" id="uploadimg"><div id="fileList" class="uploader-list"></div> <div id="imgPicker">选择图片</div> </div>'
				});
				var uploader = WebUploader.create({
					auto: true, // 选完文件后，是否自动上传
					swf: '../js/webuploader/Uploader.swf', // swf文件路径
					server: that.config.uploadUrl + '?imageTypeId=' + imageTypeId + "&tempId=" + tempId + "&acctId=" + acctId + "&acctBillsId=" + acctBillsId, // 文件接收服务端
					pick: '#imgPicker', // 选择文件的按钮。可选
					fileNumLimit: 10,//一次最多上传十张,
					compress: false, //图片在上传前不进行压缩
					// 只允许选择图片文件。
					accept: {
						title: 'Images',
						extensions: 'gif,jpg,jpeg,bmp,png',
						mimeTypes: 'image/*'
					}
				});
				uploader.on('uploadSuccess', function (file, res) {
					layer.msg(res.message);
					that.config.handleUpload && that.config.handleUpload(imageTypeId);
				});
				uploader.on('uploadError', function (file, reason) {
					layer.msg('上传失败');
				});
				uploader.on("error", function (err) {
					//Q_TYPE_DENIED
					if (err == "Q_TYPE_DENIED") {
						layer.msg('不支持该格式，请上传gif,jpg,jpeg,bmp,png格式的图片！');
					}else if(err == 'Q_EXCEED_NUM_LIMIT'){
                        layer.msg('单次影像上传不可多于十张');
					} else {
						layer.msg('上传失败:' + err);
					}
				});
			});

            $('.btn-image-download').click(function () {
                var elem = $(this);
                if (that.config.downloadType === 1) {
                    var imageTypeId = elem.data('id');
                    var tempId = that.config.tempId;
                    var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
                    var acctBillsId = that.config.acctBillsId == undefined ? 0 : that.config.acctBillsId;
                    var downloadZipUrl = that.config.downloadZipUrl + '?imageTypeId=' + imageTypeId + "&tempId=" + tempId + "&acctId=" + acctId + "&acctBillsId=" + acctBillsId;
                    $('#downloadFrame').attr('src', downloadZipUrl);
                    that.config.handleDownload && that.config.handleDownload(imageTypeId);
                } else if (that.config.downloadType === 2) {
                    var id = elem.data('id');
                    var form = document.createElement("form");
                    form.style.display = 'none';
                    form.action = that.config.downloadZipUrl;
                    form.method = "post";
                    document.body.appendChild(form);
                    for (var i = 0; i < that.config.data.length; i++) {
                        if (that.config.data[i].id === id) {
                            var fileNameInput = document.createElement("input");
                            fileNameInput.type = "hidden";
                            fileNameInput.name = "fileName";
                            fileNameInput.value = that.config.data[i].type;
                            form.appendChild(fileNameInput);
                            for (var j = 0; j < that.config.data[i].list.length; j++) {
                                var filePathInput = document.createElement("input");
                                filePathInput.type = "hidden";
                                filePathInput.name = "filePath";
                                filePathInput.value = escape(that.config.data[i].list[j].imgUrl);
                                form.appendChild(filePathInput);
                            }
                            break;
                        }
                    }
                    form.submit();
                    document.body.removeChild(form);
                }
            });

			$('.btn-image-delete').click(function (){
				var elem = $(this);
				var imageTypeId = elem.data('id');
				var tempId = that.config.tempId;
				var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
				var acctBillsId = that.config.acctBillsId == undefined ? 0 : that.config.acctBillsId;
				var deleteImageUrl = that.config.deleteImageUrl + '?imageTypeId=' + imageTypeId + "&tempId=" + tempId + "&acctId=" + acctId + "&acctBillsId=" + acctBillsId;


				layer.confirm('确定删除该证件类型的图片吗？', {
					btn: ['确定', '取消']
				}, function () {
					$.ajax({
						url: deleteImageUrl,
						type: 'POST',
						dataType: "json",
						success: function (data) {
							if (data.code === 'ACK') {
								layer.msg("删除成功");
							} else {
								layer.msg(data.message);
							}
							that.config.handleDelete && that.config.handleDelete(imageTypeId);
						}

					});
				});
			});

			$('.btn-image-edit').click(function () {
				if (that.config.editBox) return;
				var elem = $(this);
				var imageTypeId = elem.data('id');
				var type = elem.data('type');
				var name = elem.data('name');
				var no = elem.data('no');
				var date = elem.data('date');
				var docCode = elem.data('docCode');
				var tempId = that.config.tempId;
				var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
				var acctBillsId = that.config.acctBillsId == undefined ? 0 : that.config.acctBillsId;
				var editImageUrl = that.config.editImageUrl;
				var button;
				if (!that.config.readOnly) {
					button = ['保存', '取消'];
				}

				$.get('../account/imageEdit.html', null, function (form) {
					that.config.editBox = layer.open({
						type: 1,
						title: type,
						content: form,
						btn: button,
						shade: false,
						area: '500px',
						yes: function (index) {
							//触发表单的提交事件
							if (!that.config.readOnly) {
								$('form.layui-form').find('button[lay-filter=edit]').click();
							}
						},
						success: function (layero, index) {
							var form = layui.form;
							if (type === "未知类型" || type === "未知类型(未上报)" || type === "未知类型(已上报)") {
								layero.find('#docCodeDiv').show();
								layero.find('#docCode').empty();
								layero.find('#docCode').append("<option value=\"\">请选择</option>");
								for (i = 0; i < that.config.data.length; i++) {
									var item = that.config.data[i];
									layero.find('#docCode').append('<option value="' + item.docCode + '" >' + item.type + '</option>');
								}
							}
							form.render();
							layero.find('#imageTypeId').val(imageTypeId);
							layero.find('#acctId').val(acctId);
							layero.find('#acctBillsId').val(acctBillsId);
							layero.find('#type').val(type);
							layero.find('#tempId').val(tempId);
							layero.find('#fileName').val(name);
							layero.find('#fileNo').val(no);
							layero.find('#maturityDate').val(date);

							if(that.config.readOnly){
								layero.find('input, select, textarea').prop('disabled', true);
							}

							laydate.render({
								elem: '#maturityDate',
								format: 'yyyy-MM-dd'
							});

							form.on('submit(edit)', function (data) {
								layer.closeAll();
								$.ajax({
									url: editImageUrl,
									type: 'POST',
									data: data.field,
									dataType: "json",
									success: function (data) {
										if (data.code === 'ACK') {
											layer.msg('编辑成功');
										} else {
											layer.msg(data.message);
										}
										that.config.handleEdit && that.config.handleEdit(data.field);
									}

								});
								return false;
							});
						},
						end: function () {
							that.config.editBox = null;
						}
					});
				});

			});

			$('.btn-image-send').click(function () {
				var elem = $(this);
				var imageTypeId = elem.data('id');
				var tempId = that.config.tempId;
				var acctId = that.config.acctId == undefined ? 0 : that.config.acctId;
				var acctBillsId = that.config.acctBillsId == undefined ? 0 : that.config.acctBillsId;
				var sendImageUrl = that.config.sendImageUrl + '?imageTypeId=' + imageTypeId + "&tempId=" + tempId + "&acctId=" + acctId + "&acctBillsId=" + acctBillsId;


				layer.confirm('确定该证件类型的图片传送影像平台吗？', {
					btn: ['确定', '取消']
				}, function () {
					$.ajax({
						url: sendImageUrl,
						type: 'POST',
						dataType: "json",
						success: function (data) {
							if (data.code === 'ACK') {
								layer.msg("传送成功");
							} else {
								layer.msg(data.message);
							}
							that.config.handleSend && that.config.handleSend(imageTypeId);
						}

					});
				});
			});
		},
		batchMove: function (imageTypeId, imgIds) {
			var that = this;
			if (that.config.batchMoveBox) return;

			var html = '<div style="margin: 15px;"><form class="layui-form" action="">'+
       '<select name="type" class="layui-input" id="type"></select>'+
        '<button lay-filter="edit" lay-submit style="display: none;"></button>'+
    		'</form></div>';
			
			that.config.batchMoveBox = layer.open({
				type: 1,
				title: '移动图片',
				content: html,
				btn: ['确定', '取消'],
				shade: false,
				area: ['300px','300px'],
				yes: function (index) {
					//触发表单的提交事件
					if (!that.config.readOnly) {
						$('form.layui-form').find('button[lay-filter=edit]').click();
					}
				},
				success: function (layero, index) {
					var form = layui.form;
					
					var optionsHtml = '';
					optionsHtml += '<option value="">请选择影像类型</option>';
					for (i = 0; i < that.config.data.length; i++) {
						var item = that.config.data[i];
						if (item.id != imageTypeId) {
							optionsHtml += '<option value="' + item.id + '" >' + item.type + '</option>';
						}
					}
					layero.find('#type').html(optionsHtml);
					form.render();
					
					form.on('submit(edit)', function (data) {
						var typeId = layero.find('#type').val();
						console.log("啊实打实+"+typeId)
						if(typeId==""){
                            layer.msg('请选择影像类型');
							return false;
						}
						var params = {
							imageTypeId: typeId,
							imgIds: imgIds
						}

						layer.close(index);
						console.log(params);
						$.ajax({
							url: that.config.batchMoveImageUrl,
							type: 'POST',
							data: params,
							dataType: "json",
							success: function (data) {
								if (data.code === 'ACK') {
									layer.msg('移动成功');
								} else {
									layer.msg(data.message);
								}
								layer.closeAll();
								that.config.handleBatchMove && that.config.handleBatchMove(typeId, imgIds);
							}
						});
						return false;
					});
				},
				end: function () {
					that.config.batchMoveBox = null;
				}
			});
		},
		syncUI: function () {

		},
		destructor: function () {

		},
		refresh: function (cfg) {
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
			this.config.tempId = new Date().getTime() + "" + Math.floor(Math.random() * 100000);
			if ($("#tempId").length > 0) {
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