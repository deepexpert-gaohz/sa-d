/** account.js  */
layui.define(['laytpl','picker'], function(exports) {
	"use strict";

	var $ = layui.jquery,
        laytpl = layui.laytpl,
        picker = layui.picker;

	var account = {
        /**
         * 获取账户基本信息模板
         * @param name
         * @param element
         * @param title
         */
        loadAccountTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/account.html',data,element);
        },
        /**
         * 获取客户信息模板
         * @param name
         * @param element
         * @param title
         */
        loadSaicTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/saic.html',data,element);
        },
        /**
         * 获取法人代表信息模板
         * @param name
         * @param element
         * @param title
         */
        loadLegalTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/legal.html',data,element);
        },
        /**
         * 获取组织结构代码信息模板
         * @param name
         * @param element
         * @param title
         */
        loadOrgTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/org.html',data,element);
        },
        /**
         * 获取税务登记信息模板
         * @param name
         * @param element
         * @param title
         */
        loadTaxTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/tax.html',data,element);
        },
        /**
         * 获取联系信息模板
         * @param name
         * @param element
         * @param title
         */
        loadContactTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/contact.html',data,element);
        },
        /**
         * 获取其他信息模板
         * @param name
         * @param element
         * @param title
         */
        loadOtherTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/other.html',data,element);
        },
        /**
         * 获取上级机构模板
         * @param name
         * @param element
         * @param title
         */
        loadSuperviserTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/superviser.html',data,element);
        },
        /**
         * 获取关联企业信息模板
         * @param name
         * @param element
         * @param title
         */
        loadRelateCompanyTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/relateCompany.html',data,element);
        },
        /**
         * 获取股东信息模板
         * @param name
         * @param element
         * @param title
         */
        loadStockHoderTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/stockHoder.html',data,element);
        },
        /**
         * 获取授权经办人信息模板
         * @param name
         * @param element
         * @param title
         */
        loadAgentTemplate: function (data,element) {
            this.loadTemplateBasic('/ui/account/agent.html',data,element);
        },
        loadTemplateBasic: function (url, data, element) {
            //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
            $.get(url, null, function (template) {
                if(template){
                    laytpl(template).render(data,function (html) {
                        $(element).html(html);
                    });
                }
            });
        }
	};

	exports('account', account);
});