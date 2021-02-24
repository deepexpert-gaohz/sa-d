function doKey(e){
    var ev = e || window.event;//获取event对象
    var obj = ev.target || ev.srcElement;//获取事件源
    var t = obj.type || obj.getAttribute('type');//获取事件源类型
    if(ev.keyCode == 8 && t == "text" && obj.readOnly) {
        return false;
    }
    if(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea"){
        return false;
    }
    if((window.event.shiftKey && (ev.keyCode == 188 || ev.keyCode == 190)) && (t == "password" || t == "text" || t == "textarea")) {
        return false;
    }
}
//禁止后退键 作用于Firefox、Opera
document.onkeypress=doKey;
//禁止后退键  作用于IE、Chrome
document.onkeydown=doKey;

//防止XSS攻击(去除特殊字符)
$(function () {
    $('input').on('blur', function () {
        var value=$(this).val();
        value = value.replaceAll('<', '');
        value = value.replaceAll('>', '');
        $(this).val(value)
    })
})

var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t=""; var c1;var n=0;var r= c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){var c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);var c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}

/**
 * 设置表单值
 * @param el
 * @param data
 */
function setFromValues(el, data) {
    for (var p in data) {
        var element = el.find(":input[name='" + p + "']");
        if(element.length === 1){
            if(data[p] != null) element.val(data[p]);
        } else if (element.length > 1){
            // radio
            if(data[p] != null) el.find(":input:radio[name='" + p + "'][value='"+data[p]+"']").prop("checked",true);
        }
    }
}


/**
 * 时间格式化
 * @param formatStr
 */
Date.prototype.Format = function(formatStr)
{
	var str = formatStr;
	var Week = ['日','一','二','三','四','五','六'];
	
	str=str.replace(/yyyy|YYYY/,this.getFullYear());
	str=str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));
	
	str=str.replace(/MM/,(this.getMonth()+1)>9?(this.getMonth()+1).toString():'0' + (this.getMonth()+1));
	str=str.replace(/M/g,this.getMonth()+1);
	
	str=str.replace(/w|W/g,Week[this.getDay()]);
	
	str=str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());
	str=str.replace(/d|D/g,this.getDate());
	
	str=str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());
	str=str.replace(/h|H/g,this.getHours());
	str=str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());
	str=str.replace(/m/g,this.getMinutes());
	
	str=str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());
	str=str.replace(/s|S/g,this.getSeconds());
	
	return str;
}

function showDataNotNull(value) {
    if(value == null || value == 'null'){
        return "";
    }else{
        return value;
    }
}

function changeDateFormat(value) {
	if(value =="" || value == null){
		return "";
	}else{
	    return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
	}
}

function changeDateStr(value) {
    if(value && value.indexOf(".") != -1) {
        var arr1 = value.split(".");
        var dateStr = arr1[0];
        return dateStr;
    } else {
        return value;
    }
}

function dateFormat(value) {
    if(value && value.length == 8) {
        return value.substr(0,4) + '-' + value.substr(4,2) + '-' +  value.substr(6,2);
    } else {
        return value;
    }
}

function changeAcctType(value) {
    if(value == 'jiben') {
        return '基本存款账户';
    } else if(value == 'yiban') {
        return '一般存款账户';
    } else if(value == 'yusuan') {
        return '预算单位专用存款账户';
    } else if(value == 'feiyusuan') {
        return '非预算单位专用存款账户';
    } else if(value == 'linshi') {
        return '临时机构临时存款账户';
    } else if(value == 'feilinshi') {
        return '非临时机构临时存款账户';
    } else if(value == 'teshu') {
        return '特殊单位专用存款账户';
    } else if(value == 'yanzi') {
        return '验资户临时存款账户';
    } else if(value == 'zengzi') {
        return '增资户临时存款账户';
    } else if(value == 'specialAcct') {
        return '专用存款账户';
    } else if(value == 'tempAcct') {
        return '临时存款账户';
    } else if(value == 'unknow') {
        return '未知账户性质';
    }
}

function changeCustomerStatus(value) {
    if(value == 0) {
        return '待审批';
    } else if(value == 1) {
        return '审批中';
    } else if(value == 2) {
        return '审批通过';
    } else if(value == 3) {
        return '审批退回';
    }
}

function changeFileType(value) {
    if(value == '03') {
        return '登记证书';
    } else if(value == '01') {
        return '工商营业执照';
    } else if(value == '02') {
        return '批文';
    } else if(value == '04') {
        return '开户证明文件';
    } else if(value == '09') {
        return '主管部门批文';
    } else if(value == '11') {
        return '政府部门文件';
    } else if(value == '10') {
        return '相关部门证明';
    } else if(value == '06') {
        return '借款合同';
    } else if(value == '07') {
        return '其他结算需要的证明';
    } else if(value == '12') {
        return '证券从业资格证书';
    } else {
        return '其他';
    }
}

var acctTypeMap = {
    jiben: '基本存款账户',
    yiban: '一般存款账户',
    yusuan: '预算单位专用存款账户',
    feiyusuan: '非预算单位专用存款账户',
    teshu: '特殊单位专用存款账户',
    linshi: '临时机构临时存款账户',
    feilinshi: '非临时机构临时存款账户',
    yanzi: '验资户临时存款账户',
    zengzi: '增资户临时存款账户'
};

var billTypeMap = {
    ACCT_OPEN: '新开户',
    ACCT_CHANGE: '变更',
    ACCT_SUSPEND: '久悬',
    ACCT_REVOKE: '销户'
};
function getBillTypeName(key) {
    var value;
    if (key === "ALL") {
        value = "所有";
    } else if (key === "ACCT_INIT") {
        value = "存量";
    } else if (key === "ACCT_OPEN") {
        value = "新开户";
    } else if (key === "ACCT_CHANGE") {
        value = "变更";
    } else if (key === "ACCT_SUSPEND") {
        value = "久悬";
    } else if (key === "ACCT_SEARCH") {
        value = "查询";
    } else if (key === "ACCT_REVOKE") {
        value = "销户";
    } else if (key === "ACCT_SAIC") {
        value = "工商信息";
    } else if (key === "APPT_UNCOMPLETE") {
        value = "接洽打印";
    } else if (key === "APPT_AFTERCOMPLETE") {
        value = "已受理详情";
    }else{
        value = "新开户";
    }
    return value;
}
function illegalStatusName(value){
    if(value=='EMPTY'){
        return '查询无结果';
    }else if(value == 'NORMAL'){
        return '正常';
    }else if(value == 'ILLEGAL'){
        return '严重违法';
    }else if(value == 'CHANGEMESS'){
        return '经营异常';
    }
}

function fileDue(value){
    if(value=='EXPIRED'){
        return '已过期';
    }else if(value == 'NOTEXPIRED'){
        return '未过期';
    }else if(value == 'EMPTY'){
        return '未查到';
    }
}

function isNotNull(value){
    if(value=='1'){
        return '是';
    }else if(value == '0'){
        return '否';
    }
}

function formatCheckPass(value) {
    if(value==true){
        return '成功';
    }else if(value == false){
        return '失败';
    } else {
        return "";
    }
}

function formatType(value) {
    if(value=='1'){
        return '预约人通知短信';
    } else if(value == '2'){
        return '柜员通知短信';
    } else {
        return "";
    }
}

/**
 * array转成json对象
 * @param obj
 */
function toJson(obj)      
{      
   var o = {};      
   $.each(obj, function() {      
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
};

/**
 * 从请求中获取参数
 * @param str
 * @returns {*}
 */
function getReqParam(str) {
    var reg = new RegExp("(^|&)" + str + "=([^&]*)(&|$)","i");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) {
        var reg=/%3C|%3E|<|>/g;
        return r[2].replace(reg, '');
    }
    return "";
}

/**
 * jquery-validate初始提示
 * @param error
 * @param element
 */
function errorPlacementCallback(error, element) {  //该element对象输出错误
	if(element.is("select")){
		element.parent().find(".layui-select-title").addClass("error");
        error.appendTo(element.parent());
	}else{
        error.appendTo(element.parent());
	}
};

/**
 * jquery-validate元素有error
 * @param element
 * @param errorClass
 * @param validClass
 */
function highlight(element, errorClass, validClass) {  //该element对象有error
	if($(element).is("select") && $(element).parent().find(".layui-form-select").length >0){//layui的select元素
		$(element).parent().find(".layui-select-title").addClass(errorClass);
	}else{
		$(element).addClass(errorClass);
	}
};

/**
 * 展示errors
 * @param errorMap
 * @param errorList
 */
function showErrors(errorMap, errorList){
	this.defaultShowErrors();
	validateAllTabs("tab-pane","invalid-tab");
}


/**
 * 验证tabs
 * @param className
 * @param errorClassName
 */
function validateAllTabs(className,errorClassName){
	$("."+className).each(function(index,element){
		if($(this).has(".error:not(label)").length>0){
			$("a[href='#"+element.id+"']").addClass(errorClassName);
		}else{
			$("a[href='#"+element.id+"']").removeClass(errorClassName);
		}
	});
}

/**
 * 显示tabs
 * @param tabs
 */
function showInnerTabArray(tabs,form){
	var tabArrays = tabs.split(",");
	for(j=0; j<tabArrays.length; j++) {
		var element = $("a[href='#"+tabArrays[j]+"']").parent();
		if($(element).is("li")){
			$(element).show();
        }
		if($("#"+tabArrays[j]).length >0){
			$("#"+tabArrays[j]).find("input,select,textarea").removeAttr("disabled");
		}
	}
	form.render("select");
}


/**
 * 隐藏tabs
 * @param tabs
 */
function hideInnerTabArray(tabs,disableFlag,form){
	var tabArrays = tabs.split(",");
	for(j=0; j<tabArrays.length; j++) {
		var element = $("a[href='#"+tabArrays[j]+"']").parent();
		if($(element).is("li")){
			$(element).hide();
			if($(element).hasClass("active")){
				$('a[href="#tab_0"]').tab('show');
			}
		}
		if(disableFlag){
			if($("#"+tabArrays[j]).length >0){
				$("#"+tabArrays[j]).find("input,select,textarea").attr("disabled","disabled");
			}
		}
	}
	form.render("select");
}


/**
 * jquery-validate元素正常
 * @param element
 * @param errorClass
 * @param validClass
 */
function unhighlight(element, errorClass) {    //该element对象正常，无error
	if($(element).is("select") && $(element).parent().find(".layui-form-select").length >0){//layui的select元素
		$(element).parent().find(".layui-select-title").removeClass(errorClass);
	}else{
		$(element).removeClass(errorClass);
	}
};

/**
 * validate失焦
 * @param element
 */
function onFocusOutValidate(element){
	this.element( element );
}


/**
 * validate keyup事件
 * @param element
 */
function onKeyUpValidate(element){
	this.element( element );
}

/**
 * 增加label的必填标识符
 * @param validLabel
 */
function addValidateFlag(validLabel){
	for (x in validLabel){
		if(typeof validLabel[x] ==="string"){
		    if($("#"+validLabel[x]).parent().prev().find("span").html() != "*"){
		        if($("#"+validLabel[x]).parent().prev().find("em").length > 0){
                    $("#"+validLabel[x]).parent().prev().find("br").before('<span style="color: red; padding-left: 5px">*</span>');
                }else{
                    $("#"+validLabel[x]).parent().prev().append('<span style="color: red; padding-left: 5px">*</span>');
                }
		    }
		}
	}
}

/**
 * 删除label的必填标识符
 * @param validLabel
 */
function removeValidateFlag(validLabel){
	for (x in validLabel){
		if(typeof validLabel[x] ==="string"){
		    if($("#"+validLabel[x]).parent().prev().children().html() == "*"){
		    	$("#"+validLabel[x]).parent().prev().children().remove();
		    }
		}
	}
}

/**
 * 删除
 * 
 */
function removeAll(){
	// $("label").children().remove();
    $("label").find("span").remove();
	$("input,select,textarea").removeAttr("required");
}

var treeViewHelper = {};
/**
 * tree view遍历节点值
 * @param node
 * @returns {Array}
 */
treeViewHelper.getChildrenNodeIdArr = function ( node ){
    var ts = [];
    if(node.nodes){
        for(x in node.nodes){
            ts.push(node.nodes[x].nodeId)
            if(node.nodes[x].nodes){
                var getNodeDieDai = this.getChildrenNodeIdArr(node.nodes[x]);
                for(j in getNodeDieDai){
                    ts.push(getNodeDieDai[j]);
                }
            }
        }
    }else{
        ts.push(node.nodeId);
    }
    return ts;
}
/**
 * 获取treeview的父级节点
 * @param treeId
 * @param node
 * @returns {Array}
 */
treeViewHelper.getParentIdArr = function (treeId,node){
    var ts = [];
    var parent  =   $('#'+treeId).treeview('getParent', node);
    while(parent.id&&parent.id!=0){
        ts.push(parent);
        parent = $('#'+treeId).treeview('getParent', parent);
    }
    return ts;
}
var tip = {
    alert: function (info, iconIndex) {
        parent.layer.msg(info, {
            icon: iconIndex
        });
    }
} ;

var diag = {
    alert: function (info, iconIndex) {
        parent.layer.open({
            title: "登陆超时",
            content: "登陆超时，请重新登陆",
            btn: ['确定'],
            yes: function () {
                window.top.location.href = "./";
            }
        });
    }
};

$(function(){
    // 设置jQuery Ajax全局的参数
    $.ajaxSetup({
        type: "POST",
        error: function(jqXHR, textStatus, errorThrown){
            switch (jqXHR.status){
                case(500):
                    tip.alert("服务器系统内部错误");
                    break;
                case(401):
                    diag.alert("未登录");
                    // window.top.location.href = "./";
                    break;
                case(403):
                    tip.alert("无权限执行此操作");
                    break;
                case(408):
                    tip.alert("请求超时");
                    break;
                default:
                    tip.alert("未知错误");
            }
        }
    });

    $("input[readOnly]").keydown(function(e) {
        e.preventDefault();
    });
});

function ajaxError(status) {
    console.log(status);
    switch (status){
        case(500):
            tip.alert("服务器系统内部错误");
            break;
        case(401):
            diag.alert("未登录");
            // window.top.location.href = "./";
            break;
        case(403):
            tip.alert("无权限执行此操作");
            break;
        case(408):
            tip.alert("请求超时");
            break;
        case(0):
            // tip.alert("客户端关闭");
            break;
        default:
            tip.alert("未知错误");
    }
}

//SELECT 隐藏框字段的上传，需要插入disabledElement类名
function loopSelectedDisabled(dataForm){
	$("select.disableElement").each(function(){
		var name = $(this).attr("name");
		if(name != undefined && name.length >0){
			dataForm[name] = $(this).find("option:selected").val();
		}
	});
}

//Input 隐藏框字段的上传，需要插入disabledElement类名
function loopInputDisabled(dataForm){
	$("input.disableElement").each(function(){
		var name = $(this).attr("name");
		if(name != undefined && name.length >0){
			dataForm[name] = $(this).val();
		}
	});
}

//Textarea 隐藏框字段的上传，需要插入disabledElement类名
function loopTextAreaDisabled(dataForm){
	$("textarea.disableElement").each(function(){
		var name = $(this).attr("name");
		if(name != undefined && name.length >0){
			dataForm[name] = $(this).val();
		}
	});
}

//SELECT enable框
function selectedEnabled(form,element){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		$("#" + element).removeAttr("disabled");
		$("#" + element).removeClass("disableElement");
	    form.render('select',parentFilter);
	}
};

//SELECT disabled框
function selectedDisabled(form,element){
	if($("#" + element).length ==0  || !$("#" + element).is("select") || form == undefined){
		return;
	}
	if($("#" + element).parent().is("div")){
		$("#" + element).parent().addClass("layui-form");
		var parentFilter = $("#" + element).parent().attr("lay-filter");
		if(parentFilter == undefined || parentFilter.length ==0){
			parentFilter = element+"DivFilter";
			$("#" + element).parent().attr("lay-filter",parentFilter);
		}
		$("#" + element).attr("disabled","disabled");
		$("#" + element).addClass("disableElement");
	    form.render('select',parentFilter);
	}
};


function getAjaxUrl(url,success,fail) {
    $.get(url).done(function (response) {
        var data = response.result;
        if (data) {
            if(success) success(data);
        }
    }).fail(function (error) {
        if(fail) fail(error);
    });
};

//判断字符是否为空的方法
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

String.prototype.replaceAll = function (findText, repText) {
    regExp = new RegExp(findText, "g");
    return this.replace(regExp, repText);
}


function setFormValueReplace(html, data) {
    for (var p in data) {
        html = html.replaceAll("{"+p+"}",data[p]);
    }
	return html;
}

function Map() {   
    this.elements = new Array();   
     
    //获取MAP元素个数   
    this.size = function() {   
        return this.elements.length;   
    }   
     
    //判断MAP是否为空   
    this.isEmpty = function() {   
        return(this.elements.length < 1);   
    }   
     
    //删除MAP所有元素   
    this.clear = function() {   
        this.elements = new Array();   
    }   
     
    //向MAP中增加元素（key, value)    
    this.put = function(_key, _value) {   
        this.elements.push( {   
            key : _key,   
            value : _value   
        });   
    }   
     
    //删除指定KEY的元素，成功返回True，失败返回False   
    this.remove = function(_key) {   
        var bln = false;   
        try{   
            for(i = 0; i < this.elements.length; i++) {
                if(this.elements[i].key == _key) {   
                    this.elements.splice(i, 1);   
                    return true;   
                }   
            }   
        } catch(e) {   
            bln = false;   
        }   
        return bln;   
    }   
     
    //获取指定KEY的元素值VALUE，失败返回NULL   
    this.get = function(_key) {   
        try{   
            for(i = 0; i < this.elements.length; i++) {
                if(this.elements[i].key == _key) {   
                    return this.elements[i].value;   
                }   
            }   
        } catch(e) {   
            return null;   
        }   
    }   
     
    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL   
    this.element = function(_index) {   
        if(_index < 0 || _index >= this.elements.length) {   
            return null;   
        }   
        return this.elements[_index];   
    }   
     
    //判断MAP中是否含有指定KEY的元素   
    this.containsKey = function(_key) {   
        var bln = false;   
        try{   
            for(i = 0; i < this.elements.length; i++) {
                if(this.elements[i].key == _key) {   
                    bln = true;   
                }   
            }   
        } catch(e) {   
            bln = false;   
        }   
        return bln;   
    }   
     
    //判断MAP中是否含有指定VALUE的元素   
    this.containsValue = function(_value) {   
        var bln = false;   
        try{   
            for(i = 0; i < this.elements.length; i++) {
                if(this.elements[i].value == _value) {   
                    bln = true;   
                }   
            }   
        } catch(e) {   
            bln = false;   
        }   
        return bln;   
    }   
     
    //获取MAP中所有VALUE的数组（ARRAY）   
    this.values = function() {   
        var arr = new Array();   
        for(i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);   
        }   
        return arr;   
    }   
     
    //获取MAP中所有KEY的数组（ARRAY）   
    this.keys = function() {   
        var arr = new Array();   
        for(i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);   
        }   
        return arr;   
    }

    //遍历
    this.iterations = function () {
        var arr = this.keys();
        var result = {};
        for (var i = 0; i < arr.length; i++) {
            var key = arr[i];
            var value = this.get(key);
            result[key] = value;
        }
        return result;
    }
}

//兼容IE8的console方法
window._console = window.console;//将原始console对象缓存
window.console = (function (orgConsole) {
    return {//构造的新console对象
        log: getConsoleFn("log"),
        debug: getConsoleFn("debug"),
        info: getConsoleFn("info"),
        warn: getConsoleFn("warn"),
        exception: getConsoleFn("exception"),
        assert: getConsoleFn("assert"),
        dir: getConsoleFn("dir"),
        dirxml: getConsoleFn("dirxml"),
        trace: getConsoleFn("trace"),
        group: getConsoleFn("group"),
        groupCollapsed: getConsoleFn("groupCollapsed"),
        groupEnd: getConsoleFn("groupEnd"),
        profile: getConsoleFn("profile"),
        profileEnd: getConsoleFn("profileEnd"),
        count: getConsoleFn("count"),
        clear: getConsoleFn("clear"),
        time: getConsoleFn("time"),
        timeEnd: getConsoleFn("timeEnd"),
        timeStamp: getConsoleFn("timeStamp"),
        table: getConsoleFn("table"),
        error: getConsoleFn("error"),
        memory: getConsoleFn("memory"),
        markTimeline: getConsoleFn("markTimeline"),
        timeline: getConsoleFn("timeline"),
        timelineEnd: getConsoleFn("timelineEnd")
    };
    function getConsoleFn(name) {
        return function actionConsole() {
            if (typeof (orgConsole) !== "object") return;
            if (typeof (orgConsole[name]) !== "function") return;//判断原始console对象中是否含有此方法，若没有则直接返回
            return orgConsole[name].apply(orgConsole, Array.prototype.slice.call(arguments));//调用原始函数
        };
    }
}(window._console));

//指定日期格式转换
var formatDate = function(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
}

//当前日期减去指定天数
function dateBefore(nowDate, days) {
    var nowDate_s = nowDate.getTime();//转化为时间戳毫秒数
    var startDate = nowDate.setTime(nowDate_s - 1000 * 60 * 60 * 24 * days);

    return startDate;
}

function placeholder(elem) {

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
        if($(this).val() == '') {
            $(this).next().show();
        } else {
            $(this).next().hide();
        }
    });

    elem.prev().on('keyup', function () {
        if($(this).val() == '') {
            $(this).next().show();
        } else {
            $(this).next().hide();
        }
    });

}

function postExcelFile(params, url) { //params是post请求需要的参数，url是请求url地址
    var form = document.createElement("form");
    form.style.display = 'none';
    form.action = url;
    form.method = "post";
    document.body.appendChild(form);

    for (var key in params) {
        if (!params[key]) {
            continue;
        }
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = params[key];
        form.appendChild(input);
    }

    form.submit();
    document.body.removeChild(form);
}

/**
 * 相对路径转绝对路径
 */
var toAbsURL = function(){
    var directlink = function(url){
        var a = document.createElement('a');
        a.href = url;
        return a.href;
    };
    return directlink('') === '' ? function(url){
        var div = document.createElement('div');
        div.innerHTML = '<a href="' + url.replace(/"/g, '%22') + '"/>';
        return div.firstChild.href;
    } : directlink;
}();

var JsonToUrl = function(data) {
    var tempArr = [];
    for(var i in data) {
        var key = encodeURIComponent(i);
        var value = encodeURIComponent(data[i]);
        if (value=="null" || value=="undefined"){
            tempArr.push(key + '=');
        }else {
            tempArr.push(key + '=' + value);
        }
    }
    var urlParamsStr = tempArr.join('&');
    return urlParamsStr;
};