var area = {
    baseUrl: "../../area",
};

layui.define(['laytpl', 'form'], function(exports) {
    "use strict";
    var $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        form = layui.form,
        PROVINCE = 'province',
        CITY = 'city',
        AREA = 'area',
        PROVINCE_TIPS = '请选择省',
        CITY_TIPS = '请选择市',
        AREA_TIPS = '请选择县/区',
        pickerType = {
            province: 1, //省
            city: 2, //市
            area: 3 //区
        },
        DATA = [];
    var Picker = function() {
        //默认设置
        this.config = {
            vid:undefined, //返回area element后缀 比如vid=1, 获取的area1的值
            elem: undefined, //存在的容器 支持类选择器和ID选择器  e.g: [.class]  or  [#id]
            codeConfig: undefined, //初始值 e.g:{ code:440104,type:3 } 说明：code 为代码，type为类型 1：省、2：市、3：区/县
            /**
             * {
                    "code": "654325",                   //代码
                    "name": "青河县",                    //名称
                    "type": 3,                          //类型，1：省、2：市、3：区/县
                    "path": "650000,654300,654325",     //路径： 省,市,区/县
                    "parentCode": "654300"              //父代码
                }
             */
            data: undefined, //数据源，需要特定的数据结构,
            canSearch: false, //是否支持搜索
            provinceElementName: undefined, //省份element的"name"
            cityElementName: undefined, //城市element的"name"
            areaElementName: undefined,  //区域element的"name"
            thirdObject: undefined,
            disabled: false,
            // url: undefined,                          //远程地址，未用到
            // type: 'GET'                              //请求方式,未用到
        };
        this.v = '1.0.0';
        //渲染数据
    };
    Picker.fn = Picker.prototype;
    //设置
    Picker.fn.set = function(options) {
        var that = this;
        if (options.data)
            DATA = options.data;
        $.extend(true, that.config, options);
        return that;
    };
    //渲染
    Picker.fn.render = function() {
        var that = this;
        var config = that.config,
            $elem = $(config.elem),
            getDatas = function(type, parentCode, selectCode) {
                //console.log(type,parentCode,selectCode);
                // if (DATA.length === 0)
                //     throw new Error('PICKER ERROR:请设置数据源.');
                // var data = [];
                // var result = [];
                // for (var i = 0; i < DATA.length; i++) {
                //     var element = DATA[i];
                //     if (element.parentCode == parentCode)
                //         data.push(element);
                // }
                // for (var i = 0; i < data.length; i++) {
                //     var e = data[i];
                //     if (e.type == type) {
                //         var isSelected = selectCode == e.code;
                //         result.push({
                //             code: e.code,
                //             name: e.name,
                //             isSelected: isSelected
                //         });
                //     }
                // }
                var url = area.baseUrl+'/list?level='+type;
                if(parentCode){
                    url += '&areaCode='+parentCode;
                }
                var result = [];
                $.ajax({
                    type: "GET",
                    url: url,
                    async: false,
                    cache: true,
                    success: function (response) {
                        if(response.rel){
                            var data = response.result;
                            for (var i = 0; i < data.length; i++) {
                                var e = data[i];
                                if (e.level === type) {
                                    var isSelected = selectCode === e.areaCode;
                                    result.push({
                                        code: e.areaCode,
                                        name: e.areaName,
                                        regCode:e.regCode,
                                        isSelected: isSelected
                                    });
                                }
                            }

                        }
                    }
                });
                return result;
            },
            getAreaCodeByCode = function(options) {
                // if (DATA.length === 0)
                //     throw new Error('PICKER ERROR:请设置数据源.');
                // var data = DATA;
                // var result = undefined;
                // for (var i = 0; i < data.length; i++) {
                //     var e = data[i];
                //     if (e.type != options.type)
                //         continue;
                //     if (e.code == options.code) {
                //         result = e;
                //         break;
                //     }
                // }
                var path = [];
                if(options.code && options.code.length >= 6){
                    switch (options.type) {
                        case pickerType.province:
                            path.push(options.code);
                            break;
                        case pickerType.city:
                            var code = options.code;
                            var province = code.toString().substr(0, 2)+'0000';
                            path.push(province);
                            path.push(code);
                            break;
                        case pickerType.area:
                            var code = options.code;
                            var province = code.toString().substr(0, 2)+'0000';
                            var city = code.toString().substr(0, 4)+'00';
                            path.push(province);
                            path.push(city);
                            path.push(code);
                            break;
                    }
                }
                return path.join(",");
            },
            tempContent = function(vid) {
                return '' +
                    '<div data-action="picker_' + vid + '">' +
                    '<div class="layui-input-block">' +
                    '</div></div>' +
                    '';
            },
            temp = function(filterName, tipInfo) {

                var disabledStr = '';
                if (config.disabled) {
                    disabledStr = ' disabled'
                }

                var html = '<div class="layui-input-inline layui-form" data-action="' + filterName + '" lay-filter="' + filterName + '" >';
                if (config.canSearch) {
                    html += '<select id="' + filterName + '" name="' + filterName + '" lay-filter="' + filterName + '" ' + disabledStr + ' lay-search '+(config.thirdObject && config.thirdObject.validateObject ? 'required' : '')+'>';
                } else {
                    html += '<select id="' + filterName + '" name="' + filterName + '" lay-filter="' + filterName + '" ' + disabledStr + ' '+(config.thirdObject && config.thirdObject.validateObject ? 'required' : '')+'>';
                }
                html += '<option value="">' + tipInfo + '</option>';
                html += '{{# layui.each(d, function(index, item){ }}';
                html += '{{# if(item.isSelected){ }}';
                html += '<option value="{{ item.code }}" otherValue="{{ item.regCode }}" selected>{{ item.name }}</option>';
                html += '{{# }else{ }}'
                html += '<option value="{{ item.code }}" otherValue="{{ item.regCode }}" >{{ item.name }}</option>';
                html += '{{# }; }}';
                html += '{{#  }); }}';
                html += '</select>';
                html += '</div>';
                return html;
            },
            renderData = function(type, $picker, parentCode, selectCode, init) {
                var tempHtml = '';
                var filter = '';
                init = init === undefined ? true : init;
                var pickerFilter = {
                    province: config.provinceElementName || (PROVINCE + that.config.vid),
                    city: config.cityElementName || (CITY + that.config.vid),
                    area: config.areaElementName || (AREA + that.config.vid)
                };
                switch (type) {
                    case pickerType.province:
                        tempHtml = temp(pickerFilter.province, PROVINCE_TIPS);
                        filter = pickerFilter.province;
                        break;
                    case pickerType.city:
                        tempHtml = temp(pickerFilter.city, CITY_TIPS);
                        filter = pickerFilter.city;
                        break;
                    case pickerType.area:
                        tempHtml = temp(pickerFilter.area, AREA_TIPS);
                        filter = pickerFilter.area;
                        break;
                }
                layui.laytpl(tempHtml).render(getDatas(type, parentCode, selectCode), function(html) {
                    if (!init) {
                        var $has = $picker.find('div[data-action=' + filter + ']');
                        if ($has.length > 0) {
                            var $prev = $has.prev();
                            $prev.next().remove();
                            $prev.after(html);
                            if (filter === pickerFilter.city) {
                                var $hasArea = $picker.find('div[data-action=' + pickerFilter.area + ']');
                                if ($hasArea.length > 0) {
                                    $hasArea.find('select[name=' + pickerFilter.area + ']')
                                        .html('<option value="">请选择县/区</option>');
                                }
                            }
                        } else {
                            $picker.find('div[class=layui-input-block]').append(html);
                        }
                    } else {
                        $picker.find('div[class=layui-input-block]').append(html);
                    }
                    form.on('select(' + filter + ')', function(data) {
                        switch (data.elem.name) {
                            case pickerFilter.province:
                                renderData(pickerType.city, $picker, data.value, undefined, false);
                                break;
                            case pickerFilter.city:
                                renderData(pickerType.area, $picker, data.value, undefined, false);
                            	if(config.thirdObject && config.thirdObject.regCallback){
                            		config.thirdObject.regCallback();
                            	}
                                break;
                            case pickerFilter.area:
                            	if(config.thirdObject && config.thirdObject.regCallback){
                            		config.thirdObject.regCallback();
                            	}
                                //console.log('区/县');
                                break;
                        }
                    });
                    form.render('select',pickerFilter.province);
                    form.render('select',pickerFilter.city);
                    form.render('select',pickerFilter.area);
                    if(config.thirdObject && config.thirdObject.validateObject && !init){
                    	if ( $("#"+pickerFilter.province).length > 0 ) { 
                        	config.thirdObject.validateObject.element("#"+pickerFilter.province);
                    	} 
                    	if ( $("#"+pickerFilter.city).length > 0 ) { 
                        	config.thirdObject.validateObject.element("#"+pickerFilter.city);
                    	} 
                    	if ( $("#"+pickerFilter.area).length > 0 ) { 
                        	config.thirdObject.validateObject.element("#"+pickerFilter.area);
                    	} 
                    }
//                    form.render('select',pickerFilter.city);
                });
            };
        //config.vid = new Date().getTime();
        config.vid = config.vid || '';
        $elem.html(tempContent(config.vid));
        var $picker = $elem.find('div[data-action=picker_' + config.vid + ']');
        //如果需要初始化
        if (config.codeConfig) {
            var path = getAreaCodeByCode(config.codeConfig);
            var pType = config.codeConfig.type;
            var pCode = config.codeConfig.code;
            if(!pCode){
                //如果没有传code,默认省份开始
                pType = pickerType.province;
            }
            var arrPath = [];
            for (var i = 0; i < path.split(',').length; i++) {
                var e = path.split(',')[i];
                arrPath.push(e);
            }
            switch (pType) {
                case pickerType.province:
                    if(arrPath.length>0){
                        //渲染省
                        renderData(pickerType.province, $picker, null, arrPath[0]);
                    } else {
                        //渲染省
                        renderData(pickerType.province, $picker, null, undefined,true);
                    }

                    break;
                case pickerType.city:
                    //渲染省
                    renderData(pickerType.province, $picker, null, arrPath[0]);
                    //渲染市
                    renderData(pickerType.city, $picker, arrPath[0], arrPath[1]);
                    break;
                case pickerType.area:
                    //渲染省
                    renderData(pickerType.province, $picker, null, arrPath[0]);
                    //渲染市
                    renderData(pickerType.city, $picker, arrPath[0], arrPath[1]);
                    //渲染区/县
                    renderData(pickerType.area, $picker, arrPath[1], arrPath[2]);
                    break;
            }
        } else {
            renderData(pickerType.province, $picker, null, undefined, true);
        }
    };

    exports('picker', Picker);
});