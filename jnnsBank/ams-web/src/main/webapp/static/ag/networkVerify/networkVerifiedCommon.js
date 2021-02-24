

/**************************************************phoneNumber**********************************************************/
//手机号码核查结果
function rsltSwitch(value) {
    if (value=='MCHD'){
        return '要素核查匹配正确';
    }else if (value=='WIDT') {
        return '要素核查条件中的电话号码存在，证件类型不一致';
    }else if (value=='WIDN') {
        return '要素核查条件中的电话号码存在，证件类型一致,证件号码不一致';
    }else if (value=='WNAM') {
        return '要素核查条件中的电话号码存在，证件类型一致,证件号码一致，姓名不一致';
    }else if (value=='NTFD') {
        return '电话号码匹配失败';
    }
}

function idTpSwitch(value) {
    if (value=='IC00'){
        return '身份证';
    }else if (value=='IC02') {
        return '军官证';
    }else if (value=='IC03') {
        return '士兵证';
    }else if (value=='IC04') {
        return '港澳往来通行证';
    }else if (value=='IC05') {
        return '临时身份证';
    }else if (value=='IC06') {
        return '户口簿';
    }else if (value=='IC08') {
        return '外国人永久居留证';
    }else if (value=='IC20') {
        return '台湾往来通行证';
    }else if (value=='IC21') {
        return '外国公民护照';
    }else if (value=='IC22') {
        return '港澳居民居住证';
    }else if (value=='IC23') {
        return '台湾居民居住证';
    }else if (value=='IC24') {
        return '中国人民武装警察身份证件';
    }
}

//客户类型
function cdTpSwitch(value) {
    if (value=='INDV'){
        return '个人用户';
    }else if (value=='ENTY') {
        return '单位用户';
    }
}

//手机号码状态
function stsSwitch(value) {
    if (value=='ENBL'){
        return '正常';
    }else if (value=='DSBL') {
        return '停机';
    }
}

//申请报文拒绝状态
function procStsSwitch(value) {
    if (value=='PR07'){
        return '已处理';
    }else if (value=='PR09') {
        return '已拒绝';
    }else if (value=='PR00'){
        return '已受理';
    }
}


//手机运营商 TODO 1.0.2 完善
function mobCrrSwitch(value) {
    if (value=='1000'){
        return '中国电信';
    }else if (value=='2000') {
        return '中国移动';
    }else if (value=='3000') {
        return '中国联通';
    }else if (value=='0000') {
        return '虚拟运营商整体';
    }else if (value=='0001') {
        return '苏州蜗牛数字科技股份有限公司';
    }else if (value=='0002') {
        return '远特（北京）通信技术有限公司';
    }else if (value=='0003') {
        return '北京分享在线网络技术有限公司';
    }else if (value=='0004') {
        return '北京迪信通通信服务有限公司';
    }else if (value=='0005') {
        return '深圳市爱施德股份有限公司';
    }else if (value=='0006') {
        return '天音通信有限公司';
    }else if (value=='0007') {
        return '浙江连连科技有限公司';
    }else if (value=='0008') {
        return '北京乐语世纪通信设备连锁有限公司';
    }else if (value=='0009') {
        return '北京华翔联信科技有限公司';
    }else if (value=='0010') {
        return '北京京东叁佰陆拾度电子商务有限公司';
    }else if (value=='0011') {
        return '北京北纬通信科技股份有限公司';
    }else if (value=='0012') {
        return '北京万网志成科技有限公司';
    }else if (value=='0013') {
        return '巴士在线控股有限公司';
    }else if (value=='0014') {
        return '话机世界数码连锁集团股份有限公司';
    }else if (value=='0015') {
        return '厦门三五互联科技股份有限公司';
    }else if (value=='0016') {
        return '北京国美电器有限公司';
    }else if (value=='0017') {
        return '苏宁云商集团股份有限公司';
    }else if (value=='0018') {
        return '中期集团有限公司';
    }else if (value=='0019') {
        return '长江时代通信股份有限公司';
    }else if (value=='0020') {
        return '贵阳朗玛信息技术股份有限公司';
    }else if (value=='0021') {
        return '深圳市中兴视通科技有限公司';
    }else if (value=='0022') {
        return '用友移动通信技术服务有限公司';
    }else if (value=='0023') {
        return '中邮世纪（北京）通信技术有限公司';
    }else if (value=='0024') {
        return '北京世纪互联宽带数据中心有限公司';
    }else if (value=='0025') {
        return '银盛电子支付科技有限公司';
    }else if (value=='0026') {
        return '红豆集团有限公司';
    }else if (value=='0027') {
        return '深圳星美圣典文化传媒集团有限公司';
    }else if (value=='0028') {
        return '合一信息技术（北京）有限公司';
    }else if (value=='0029') {
        return '青岛日日顺网络科技有限公司';
    }else if (value=='0030') {
        return '北京青牛科技有限公司';
    }else if (value=='0031') {
        return '小米科技有限责任公司';
    }else if (value=='0032') {
        return '郑州市讯捷贸易有限公司';
    }else if (value=='0033') {
        return '二六三网络通信股份有限公司';
    }else if (value=='0034') {
        return '海南海航信息技术有限公司';
    }else if (value=='0035') {
        return '北京联想调频科技有限公司';
    }else if (value=='0036') {
        return '广东恒大和通信科技股份有限公司';
    }else if (value=='0037') {
        return '青岛丰信通信有限公司';
    }else if (value=='0038') {
        return '凤凰资产管理有限公司';
    }else if (value=='0039') {
        return '深圳平安通信科技有限公司';
    }else if (value=='0040') {
        return '民生电子商务有限责任公司';
    }else if (value=='0041') {
        return '鹏博士电信传媒集团股份有限公司';
    }else if (value=='0042') {
        return '广州博元讯息服务有限公司';
    }
}


/**************************************************taxInformation**********************************************************/

//纳税信息核查结果
function taxRsltSwitch(value) {
    if (value=='MCHD'){
        return '要素核查匹配正确';
    } else if (value == 'NMCH'){
        return '要素核查条件中统一社会信用代码或纳税人识别号存在，单位名称不一致';
    } else if (value == 'NTFD'){
        return '统一社会信用代码或纳税人识别号匹配失败';
    }
}

//纳税人状态
function taxTxpyrStsSwitch(value) {
    if (value=='OPEN'){
        return '开业';
    } else if (value == 'CNCL'){
        return '注销';
    } else if (value == 'IMID'){
        return '非正常认定';
    } else if (value == 'IMCL'){
        return '非正常注销';
    }
}

/**************************************************businessAcceptTime**********************************************************/
function sysIndSwitch(value){
    if (value=='MIIT'){
        return '手机号码核查';
    } else if (value == 'CSAT'){
        return '纳税信息核查';
    } else if (value == 'SAMR'){
        return '工商登记信息核查';
    }
}

function svcIndSwitch(value) {
    if (value=='ENBL'){
        return '正常';
    } else if (value == 'DSBL'){
        return '关闭';
    }
}

/**************************************************registerInformation**********************************************************/
//登记信息核查结果
function registerRsltSwitch(value) {
    if (value=='MCHD'){
        return '要素核查匹配正确';
    }else if (value=='WCON') {
        return '要素核查条件中统一社会信用代码存在，企业名称/字号名称不一致';
    }else if (value=='WLPN') {
        return '要素核查条件中统一社会信用代码存在，企业名称/字号名称一致，法定代表人/单位负责人姓名/经营者姓名不一致';
    }else if (value=='WLPI') {
        return '要素核查条件中统一社会信用代码存在，企业名称/字号名称一致，法定代表人或单位负责人姓名/经营者姓名一致，法定代表人或单位负责人身份证件号码/经营者证件号不一致';
    }else if (value=='NTFD') {
        return '统一社会信用代码匹配失败';
    }
}

//正副本标识
function registerOrgnlOrCpSwitch(value) {
    if (value=='ORCY'){
        return '正副本';
    }else if (value=='ORGN') {
        return '正本';
    }else if (value=='COPY') {
        return '副本';
    }
}

//补领标识
function registerRplStsSwitch(value) {
    if (value=='RPLC'){
        return '补领';
    }else if (value=='NULL') {
        return '未补领';
    }
}

/**************************************************institutionAbnormal**********************************************************/

//异常核查类型
function abnmlTypeSwitch(value) {
    if (value=='MIIT'){
        return '手机号码核查异常';
    }else if (value=='CSAT') {
        return '纳税信息核查异常';
    }else if (value=='SAMR') {
        return '工商登记信息核查异常';
    }else if (value=='OTHR') {
        return '其他类型异常';
    }
}

/**************************************************announcementInformation**********************************************************/

//回复标识类型
function replyFlagCodeSwitch(value) {
    if (value=='RPLY'){
        return '需要回复';
    }else if (value=='NRPL') {
        return '无需回复';
    }
}

/**************************************************feedback**********************************************************/
//回复标识类型
function typeNameSwitch(value) {
    if (value=='taxInformation'){
        return '纳税信息疑义反馈';
    }else if (value=='registerInformation') {
        return '登记信息疑义反馈';
    }else if (value=='phoneNumber') {
        return '手机号码疑义反馈';
    }
}

/**************************************************openRevokeState**********************************************************/

//企业账户状态标识
function acctStsSwitch(value) {
    if (value=='OPEN'){
        return '已开户';
    }else if (value=='CLOS') {
        return '已销户';
    }
}