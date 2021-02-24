var districtConfig = {
    baseUrl: "../../area",
};

layui.define(['layer'], function(exports) {
	"use strict";

	var $ = layui.jquery;

	var district = {
		loadProvince: function(layero,form,element) {
			$.get(districtConfig.baseUrl+'/list?level=1').done(function (response) {
                var data = response.result;
                if (data) {
                    var html = '<option data-code="" data-text="—— 省 ——" value="">—— 省 ——</option>';
                    for(var i=0;i<data.length;i++){
                        var code = data[i].areaCode;
                        var name = data[i].areaName;
                        html += '<option value="'+code+'">'+name+'</option>';
                    }
                    layero.find(element).append(html);
                    form.render();
                }
            });
		}
	};

	exports('district', district);
});