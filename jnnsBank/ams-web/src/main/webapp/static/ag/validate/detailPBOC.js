/**
 * Created by wanghongjie on 12/06/2018.
 */
layui.config({
    base: '../js/' //假设这是你存放拓展模块的根目录
});

layui.use(['common','loading','form','laytpl'], function () {
    var $ = layui.jquery,
        common = layui.common,loading= layui.loading,form=layui.form,laytpl = layui.laytpl;

    var layerTips = parent.layer === undefined ? layui.layer : parent.layer;
    var type = decodeURI(common.getReqParam("type"));
    var buttonType = decodeURI(common.getReqParam("buttonType"));
    var depositorName = decodeURI(common.getReqParam("depositorName"));
    var billId = decodeURI(common.getReqParam("billId"));
    var acctType =  decodeURI(common.getReqParam("acctType"));
    var depositorType='';
	$("#btn_next").hide();
    var name ="";
    function showPBOCInfo(accountKey,regAreaCode){
        loading.show();
        var flag = false;//默认走原来的
        if (type=="yiban"){
            flag = true;//一般户专用的人行开户校验接口
        }
        $.get('../../validate/pbc?accountKey='+accountKey+'&regAreaCode='+regAreaCode+'&flag='+flag).done(function (response) {
            loading.hide();
            var rel = response.rel;
            var msg = response.msg;
            var data = response.result;
            if(rel){
            	if(data){
            		name=data.depositorName;
                    depositorType=data.depositorType;
                    data.customerTitle = '客户信息';
                    data.parentTitle = '上机机构信息';
                    loadBasicTemplate(data,'#tab_0','customerPBOC.html');
                    loadBasicTemplate(data,'#tab_1','parentPBOC.html');
                	layer.alert("该客户基本户正常",{
                		title:"人行提示",
                		closeBtn:0
                	});    
                    $("#btn_next").show();
            	}
            }else{
                if(msg.indexOf("存款人有其他久悬银行结算账户") != -1){
                    $("#btn_next").show();
                }
            	layer.alert(msg,{
            		title:"人行提示",
            		closeBtn:0
            	});    
            }
        });
    }
    
    
    $("#btn_query").on('click',function(){
    	var accountKey = $("#accountKey").val();
    	var regAreaCode = $("#regAreaCode").val();
    	$("#btn_next").hide();
    	if(accountKey =="" ||regAreaCode==""){
            layerTips.msg('基本户核准号和基本户地区代码不能为空');
    	}else{
    		showPBOCInfo(accountKey,regAreaCode);
    	}
    });


    function loadBasicTemplate(data, element, url) {
        //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
        $.get(url, null, function (template) {
            if (template) {
                laytpl(template).render(data, function (html) {
                    $(element).html(html);
                });
            }
        });
    }
    
    $("#btn_next").on('click',function(){
    	var accountKey = $("#accountKey").val();
    	var regAreaCode = $("#regAreaCode").val();
    	parent.tab.tabAdd({
            title: '校验管理-'+name,
            href: 'validate/detail.html?name=' + encodeURI(name)
            + '&accountKey=' + accountKey
            + '&regAreaCode=' + regAreaCode
            + '&type=' + type
            + '&tabId=' + encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId()))
            + '&buttonType=' + buttonType
            + '&depositorName=' + depositorName
            + '&billId=' + billId
            + '&acctType=' + acctType
            + '&depositorType='+encodeURI(depositorType)
    	});
    });


    $("#btn_ignore").on('click',function(){
    	parent.tab.tabAdd({
            title: '校验管理-'+name,
            href: 'validate/detail.html?name=' + encodeURI(name)
            + '&type=' + type
            + '&tabId=' + encodeURI(common.encodeUrlChar(parent.tab.getCurrentTabId()))
            + '&buttonType=' + buttonType
            + '&depositorName=' + depositorName
            + '&billId=' + billId
            + '&acctType=' + acctType
    	});
    });


    
    $("#btn_back").on('click',function(){
    	 parent.tab.deleteTab(parent.tab.getCurrentTabId());
         return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可
    });
});