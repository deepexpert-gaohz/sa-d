package com.ideatech.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum DepositorTypeSzsm {

    DEPOSITOR_TYPE_201("201", "国有企业"),
    DEPOSITOR_TYPE_202("202", "集体企业"),
    DEPOSITOR_TYPE_203("203", "股份合作企业"),
    DEPOSITOR_TYPE_204("204", "联营企业"),
    DEPOSITOR_TYPE_205("205", "有限责任公司"),
    DEPOSITOR_TYPE_206("206", "股份有限公司"),
    DEPOSITOR_TYPE_207("207", "私营企业"),
    DEPOSITOR_TYPE_208("208", "港、澳、台投资企业"),
    DEPOSITOR_TYPE_209("209", "外商投资企业"),
    DEPOSITOR_TYPE_210("210", "个体经营"),
    DEPOSITOR_TYPE_211("211", "乡镇企业"),
    DEPOSITOR_TYPE_301("301", "中国人民银行"),
    DEPOSITOR_TYPE_302("302", "国家外汇管理局"),
    DEPOSITOR_TYPE_303("303", "中国银行业监督管理委员会"),
    DEPOSITOR_TYPE_304("304", "中国证券监督管理委员会"),
    DEPOSITOR_TYPE_305("305", "中国保险监督管理委员会"),
    DEPOSITOR_TYPE_306("306", "国家开发银行"),
    DEPOSITOR_TYPE_307("307", "进出口银行"),
    DEPOSITOR_TYPE_308("308", "农村发展银行"),
    DEPOSITOR_TYPE_309("309", "国有商业银行"),
    DEPOSITOR_TYPE_310("310", "其他大型商业银行"),
    DEPOSITOR_TYPE_311("311", "全国性中小型商业银行"),
    DEPOSITOR_TYPE_312("312", "城市商业银行"),
    DEPOSITOR_TYPE_313("313", "农村商业银行"),
    DEPOSITOR_TYPE_314("314", "其他商业银行"),
    DEPOSITOR_TYPE_315("315", "邮储"),
    DEPOSITOR_TYPE_316("316", "村镇银行"),
    DEPOSITOR_TYPE_317("317", "外资银行法人"),
    DEPOSITOR_TYPE_318("318", "外国银行分行"),
    DEPOSITOR_TYPE_319("319", "多边开发银行"),
    DEPOSITOR_TYPE_320("320", "城市信用社"),
    DEPOSITOR_TYPE_321("321", "农村信用社"),
    DEPOSITOR_TYPE_322("322", "农村合作银行"),
    DEPOSITOR_TYPE_323("323", "农村资金互助社"),
    DEPOSITOR_TYPE_324("324", "财务公司"),
    DEPOSITOR_TYPE_325("325", "证券公司"),
    DEPOSITOR_TYPE_326("326", "证券投资基金管理公司"),
    DEPOSITOR_TYPE_327("327", "期货公司"),
    DEPOSITOR_TYPE_328("328", "投资咨询公司"),
    DEPOSITOR_TYPE_329("329", "基金公司"),
    DEPOSITOR_TYPE_330("330", "信托公司"),
    DEPOSITOR_TYPE_331("331", "金融资产管理公司(不含国有资产管理公司)"),
    DEPOSITOR_TYPE_332("332", "国有资产管理公司"),
    DEPOSITOR_TYPE_333("333", "金融租赁公司"),
    DEPOSITOR_TYPE_334("334", "汽车金融公司"),
    DEPOSITOR_TYPE_335("335", "贷款公司"),
    DEPOSITOR_TYPE_336("336", "货币经纪公司"),
    DEPOSITOR_TYPE_337("337", "财产保险公司"),
    DEPOSITOR_TYPE_338("338", "人身保险公司"),
    DEPOSITOR_TYPE_339("339", "再保险公司"),
    DEPOSITOR_TYPE_340("340", "保险资产管理公司"),
    DEPOSITOR_TYPE_341("341", "保险经纪公司"),
    DEPOSITOR_TYPE_342("342", "保险代理公司"),
    DEPOSITOR_TYPE_343("343", "保险公估公司"),
    DEPOSITOR_TYPE_344("344", "企业年金"),
    DEPOSITOR_TYPE_345("345", "养老及社保单位"),
    DEPOSITOR_TYPE_346("346", "证券交易所"),
    DEPOSITOR_TYPE_347("347", "期货交易所"),
    DEPOSITOR_TYPE_348("348", "登记结算类机构"),
    DEPOSITOR_TYPE_349("349", "中央金融控股公司"),
    DEPOSITOR_TYPE_350("350", "其他金融控股公司"),
    DEPOSITOR_TYPE_351("351", "小额贷款公司"),
    DEPOSITOR_TYPE_352("352", "其他境内特殊目的载体（SPV）"),
    DEPOSITOR_TYPE_353("353", "其他金融辅助机构（不含SPV）"),
    DEPOSITOR_TYPE_359("359", "其他金融机构"),
    DEPOSITOR_TYPE_401("401", "中央直属"),
    DEPOSITOR_TYPE_402("402", "省直属"),
    DEPOSITOR_TYPE_403("403", "市直属"),
    DEPOSITOR_TYPE_404("404", "事业单位"),
    DEPOSITOR_TYPE_405("405", "公积金单位"),
    DEPOSITOR_TYPE_406("406", "公共部门实体"),
    DEPOSITOR_TYPE_407("407", "社保单位"),
    DEPOSITOR_TYPE_499("499", "其他政府机关"),
    DEPOSITOR_TYPE_501("501", "社会团体"),
    DEPOSITOR_TYPE_502("502", "部队"),
    DEPOSITOR_TYPE_503("503", "民办非企业组织"),
    DEPOSITOR_TYPE_504("504", "居民、村民、社区委员会"),
    DEPOSITOR_TYPE_599("599", "其他"),
    DEPOSITOR_TYPE_601("601", "内部客户"),;


    static Map<String,String> enumMap=new HashMap<String, String>();
    static{
        for(DepositorTypeSzsm type: DepositorTypeSzsm.values()){
            enumMap.put(type.getValue(), type.getDisplayName());
        }
    }

    public static String getDisplayName(String value) {
        return enumMap.get(value);
    }

    private String value;
    private String displayName;

    DepositorTypeSzsm(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
