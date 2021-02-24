/** saic.js  */
var kyc = {
    baseUrl: "../../kyc",
};
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录Ø
})
layui.define(['laytpl','linkSelect'], function(exports) {
	"use strict";

	var $ = layui.jquery,
        linkSelect = layui.linkSelect,
        laytpl = layui.laytpl;

    var common=layui.common;

	var saic = {
        /**
         * 获取工商基本信息模板
         * @param name
         * @param element
         * @param title
         */
        loadBasic: function(name,element,title) {
            var _that = this;
            if(name){
                $.get(kyc.baseUrl+'/saic/basic?keyword='+encodeURI(name)).done(function (response) {
                    var data = response.result;
                    if (data) {
                        _that.loadBasicTemplate(data,element,title);
                    }
                });
            }
		},
        /**
         * 获取工商基本信息历史模板
         * @param name
         * @param element
         * @param title
         */
        loadBasicHistory: function(name,element,title) {
            var _that = this;
            if(name){
                $.get(kyc.baseUrl+'/history/saic//basic?keyword='+encodeURI(name)).done(function (response) {
                    var data = response.result;
                    if (data) {
                        _that.loadBasicTemplate(data,element,title);
                    } else {
                        //若没有工商历史 则先从基本信息获取一次
                        _that.loadBasic(name,element,title);
                    }
                });
            }
        },
        loadBasicData: function(name,success,fail) {
            if(name){
                $.get(kyc.baseUrl+'/saic/basic?keyword='+encodeURI(name)).done(function (response) {
                    var data = response.result;
                    if (data) {
                        if(success) success(data);
                    }
                }).fail(function (error) {
                    if(fail) fail(error);
                });
            }
        },
        loadBasicTemplate: function (data,element,title) {
            data.portletTitle = title || '基本信息';
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get('basic.html', null, function (template) {
                if(template){
                    laytpl(template).render(data,function (html) {
                        $(element).html(html);
                    });
                }
            });
        },
        loadImageView: function (images,element) {
            var _that = this;
            if(images && images.length > 0){
                var html = '<div class="image-row"><div class="image-set">';
                for(var i =0; i< images.length; i++){
                    var title = _that.imageType(images[i].doccode);
                    var imgPath = images[i].imgpath;
                    html += '<a class="example-image-link" href="'+imgPath+'" data-lightbox="example-set"' +
                        ' data-title="'+title+'"><img class="example-image" src="'+imgPath+'"' +
                        ' alt="'+title+'"></a>'
                }
                $(element).html(html);
            }
            else{
                $(element).html("<span>暂无相关信息</span>");
            }
        },
        imageType:function (doccode) {
            if(doccode==="1"){
                return "开户申请书";
            }
            else if(doccode === "2"){
                return "工商营业执照";
            }
            else if(doccode === "4"){
                return "组织机构代码证";
            }
            else if(doccode === "26"){
                return "法人身份证";
            }
            return "";
        }
	};

	exports('saic', saic);
});

function initLinkSelect(elementId,data,selectedArr,selectedCallback,disabled) {
    linkSelect.render({
        id:elementId,
        elem: '#'+elementId,
        data:data,
        lableName:'',			//自定义表单名称    默认：'级联选择'
        placeholderText:'请选择...',		//自定义holder名称    默认：'请选择'
        replaceId:"ids",				//替换字段id   默认id
        replaceName:"names",			//替换字段名称  默认name
        replaceChildren:"childrens",	//替换字段名称  默认 children
        disabled:disabled,					//初始禁用         默认false
        selectWidth:200,
        selectedArr: selectedArr,
        selected:function(item,dom){
            if(selectedCallback) selectedCallback(item);
        }
    });
}

function initAddress(type,picker,code,codeType,thirdObject,disabled) {
    if(code && codeType){
        picker.set({
            elem: '#'+type+'AddressDiv',
            provinceElementName: type+'Province', //省份element的"name"
            cityElementName: type+'City', //城市element的"name"
            areaElementName: type+'Area',  //区域element的"name"
            thirdObject:thirdObject,
            disabled:disabled,
            codeConfig: {
                code: code,
                type: codeType
            }
        }).render();
    } else {
        picker.set({
            elem: '#'+type+'AddressDiv',
            provinceElementName: type+'Province', //省份element的"name"
            cityElementName: type+'City', //城市element的"name"
            areaElementName: type+'Area',  //区域element的"name"
            thirdObject:thirdObject,
            disabled:disabled,
        }).render();
    }
}