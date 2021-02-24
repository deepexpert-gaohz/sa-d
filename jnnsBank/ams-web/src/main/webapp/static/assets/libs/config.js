var Config = {
    baseUrl: '../..',
    list: {
        //账户性质
        acctType: [
            { value: 'jiben', text: '基本存款账户' },
            { value: 'yiban', text: '一般存款账户' },
            { value: 'yusuan', text: '预算单位专用存款账户' },
            { value: 'feiyusuan', text: '非预算单位专用存款账户' },
            { value: 'teshu', text: '特殊单位专用存款账户' },
            { value: 'linshi', text: '临时机构临时存款账户' },
            { value: 'feilinshi', text: '非临时机构临时存款账户' },
            { value: 'yanzi', text: '验资户临时存款账户' },
            { value: 'zengzi', text: '增资户临时存款账户' }
        ],
        //账户状态
        accountStatus: [
            { value: 'normal', text: '正常' },
            { value: 'suspend', text: '久悬' },
            { value: 'revoke', text: '销户' },
            { value: 'notActive', text: '未激活' }
        ],
        //证件类型
        idCardType: [
            { value: '1', text: '1-身份证' },
            { value: '2', text: '2-军官证' },
            { value: '3', text: '3-文职干部证' },
            { value: '4', text: '4-警官证' },
            { value: '5', text: '5-士兵证' },
            { value: '6', text: '6-护照' },
            { value: '7', text: '7-港、澳、台通行证' },
            { value: '8', text: '8-户口簿' },
            { value: '9', text: '9-其它合法身份证件' }
        ],
        //法定代表人(负责人)
        legalType: [
            { value: '1', text: '1-法定代表人' },
            { value: '2', text: '2-单位负责人' }
        ],
        //未标明注册资金
        isIdentification: [
            { value: '1', text: '1-是' },
            { value: '0', text: '0-否' }
        ],
        //与注册地是否一致
        isSameRegistArea: [
            { value: '1', text: '1-是' },
            { value: '0', text: '0-否' }
        ],
        //机构状态
        orgStatus: [
            { value: '1', text: '1-正常' },
            { value: '2', text: '2-注销' },
            { value: '9', text: '9-其他' }
        ],
        //基本户状态
        basicAccountStatus: [
            { value: '1', text: '1-正常' },
            { value: '2', text: '2-久悬' },
            { value: '3', text: '3-注销' },
            { value: '9', text: '9-其他' }
        ],
        //工商注册类型
        regType: [
            { value: '01', text: '01-工商注册号' },
            { value: '02', text: '02-机关和事业单位登记号' },
            { value: '03', text: '03-社会团体登记号' },
            { value: '04', text: '04-民办非企业单位登记号' },
            { value: '05', text: '05-基金会登记号' },
            { value: '06', text: '06-宗教活动场所登记号' },
            { value: '07', text: '07-统一社会信用代码' },
            { value: '08', text: '08-商事与非商事登记证号' },
            { value: '99', text: '99-其他' }
        ],
        //注册资本币种
        regCurrencyType: [
            { value: 'CNY', text: 'CNY-人民币' },
            { value: 'USD', text: 'USD-美元' },
            { value: 'HKD', text: 'HKD-港元' },
            { value: 'EUR', text: 'EUR-欧元' },
            { value: 'KRW', text: 'KRW-韩元' },
            { value: 'JPY', text: 'JPY-日元' },
            { value: 'GBP', text: 'GBP-英镑' },
            { value: 'SGD', text: 'SGD-新加坡元' },
            { value: 'AUD', text: 'AUD-澳大利亚元' },
            { value: 'CAD', text: 'CAD-加拿大元' },
            { value: 'XEU', text: 'XEU-其它货币折美元' }
        ],
        //企业规模
        corpScale: [
            { value: '2', text: '2-大型企业' },
            { value: '3', text: '3-中型企业' },
            { value: '4', text: '4-小型企业' },
            { value: '5', text: '5-微型企业' },
            { value: '9', text: '9-其他' }
        ],
        //经济类型
        economyType: [
            { value: '10', text: '10-内资' },
            { value: '11', text: '11-国有全资' },
            { value: '12', text: '12-集体全资' },
            { value: '13', text: '13-股份合作' },
            { value: '14', text: '14-联营' },
            { value: '15', text: '15-有限责任公司' },
            { value: '16', text: '16-股份有限公司' },
            { value: '17', text: '17-私有' },
            { value: '19', text: '19-其它内资' },
            { value: '20', text: '20-港澳台投资' },
            { value: '21', text: '21-内地和港澳台投资' },
            { value: '22', text: '22-内地和港澳台合作' },
            { value: '23', text: '23-港澳台独资' },
            { value: '24', text: '24-港澳台投资股份有限公司' },
            { value: '29', text: '29-其他港澳台投资' },
            { value: '30', text: '30-国外投资' },
            { value: '31', text: '31-中外合资' },
            { value: '33', text: '33-外资' },
            { value: '32', text: '32-中外合作' },
            { value: '34', text: '34-国外投资股份有限公司' },
            { value: '39', text: '39-其他国外投资' },
            { value: '90', text: '90-其他' }
        ],
        //登记部门
        regOffice: [
            { value: 'G', text: 'G-工商部门' },
            { value: 'R', text: 'R-人民银行' },
            { value: 'M', text: 'M-民政部门' },
            { value: 'B', text: 'B-机构编制部门' },
            { value: 'S', text: 'S-司法行政部门' },
            { value: 'W', text: 'W-外交部门' },
            { value: 'Z', text: 'Z-宗教部门' },
            { value: 'Q', text: 'Q-其他' }
        ],
        //企业证件类型
        companyCertificateType: [
            { value: '01', text: '01-工商营业执照' },
            { value: '02', text: '02-批文' },
            { value: '03', text: '03-等级证书' },
            { value: '04', text: '04-开户证明' }
        ],
        //股东类型: 高管/股东
        partnerType: [
            { value: '1', text: '1-高管' },
            { value: '2', text: '2-股东' }
        ],
        //股东关系人类型
        partnerRoleType: [
            { value: '1', text: '1-实际控制人' },
            { value: '2', text: '2-董事长' },
            { value: '3', text: '3-总经理（主要负责人）' },
            { value: '4', text: '4-财务负责人' },
            { value: '5', text: '5-监事长' },
            { value: '6', text: '6-法定代表人' },
            { value: '7', text: '7-自然人' },
            { value: '8', text: '8-机构' }
        ],
        //行业归属
        industryCode: [
            { value: 'A$$农、林、牧、渔业$$第一产业                        $$1', text: 'A：农、林、牧、渔业' },
            { value: 'B$$采矿业$$第二产业                        $$2', text: 'B：采矿业' },
            { value: 'C$$制造业$$第二产业                        $$2', text: 'C：制造业' },
            { value: 'D$$电力、煤气及水的生产和供应业$$第二产业                        $$2', text: 'D：电力、煤气及水的生产和供应业' },
            { value: 'E$$建筑业$$第二产业                        $$2', text: 'E：建筑业' },
            { value: 'F$$交通运输、仓储及邮政业$$第一层次                        $$3', text: 'F：交通运输、仓库及邮政业' },
            { value: 'G$$信息传输、计算机服务和软件业$$第二层次                        $$4', text: 'G：信息传输、计算机服务和软件业' },
            { value: 'H$$批发和零售业$$第二层次                        $$4', text: 'H：批发和零售业' },
            { value: 'I$$住宿和餐饮业$$第二层次                        $$4', text: 'I：住宿和餐饮业' },
            { value: 'J$$金融业$$第二层次                        $$4', text: 'J：金融业' },
            { value: 'K$$房地产业$$第二层次                        $$4', text: 'K：房地产业' },
            { value: 'L$$租赁和商务服务业$$第二层次                        $$4', text: 'L：租赁和商务服务业' },
            { value: 'M$$科学研究、技术服务和地质勘查业$$第三层次                        $$5', text: 'M：科学研究、技术服务和地址勘察业' },
            { value: 'N$$水利、环境和公共设施管理业$$第三层次                        $$5', text: 'N：水利、环境和公共设施管理业' },
            { value: 'O$$居民服务和其他服务业$$第三层次                        $$5', text: 'O：居民服务和其他服务业' },
            { value: 'P$$教育$$第三层次                        $$5', text: 'P：教育' },
            { value: 'Q$$卫生、社会保障和社会福利业$$第三层次                        $$5', text: 'Q：卫生、社会保障和社会福利业' },
            { value: 'R$$文化、体育和娱乐业$$第三层次                        $$5', text: 'R：文化、体育和娱乐业' },
            { value: 'S$$公共管理和社会组织$$第四层次                        $$6', text: 'S：公共管理和社会组织' },
            { value: 'T$$国际组织（其他行业）$$第四层次                        $$6', text: 'T：国际组织（其他行业）' },
            { value: 'U$$其他$$第一层次                        $$3', text: 'U：其他' }
        ],
        //证明文件种类
        fileType: [
            { value: '01', text: '01-工商注册号' },
            { value: '02', text: '02-批文' },
            { value: '03', text: '03-登记证书' },
            { value: '04', text: '04-开户证明' },
            { value: '06', text: '06-借款合同' },
            { value: '07', text: '07-其他结算需要的证明' },
            { value: '08', text: '08-财政部门批复书' },
            { value: '09', text: '09-主管部门批文' },
            { value: '10', text: '10-相关部门证明' },
            { value: '11', text: '11-政府部门文件' },
            { value: '12', text: '12-证券从业资格证书' },
            { value: '13', text: '13-国家外汇管理局的批文' },
            { value: '14', text: '14-建筑主管部门核发的许可证' },
            { value: '15', text: '15-施工及安装合同' },
            { value: '16', text: '16-工商行政管理部门的证明' },
            { value: '17', text: '17-其他' }
        ],
        //证明文件(客户开立)
        customerFileType: [
            { value: '00', text: '社会统一信用代码' },
            { value: '01', text: '工商营业执照' },
            { value: '02', text: '批文' },
            { value: '03', text: '登记证书' },
            { value: '04', text: '开户证明' },
            { value: '05', text: '其他' }
        ],
        //证明文件2种类(客户开立)
        customerFileType2: [
            { value: '02', text: '02-批文' },
            { value: '03', text: '03-登记证书' },
            { value: '04', text: '04-开户证明' }
        ],
        //存款人类别
        depositorType: [
            { value: '01', text: '01-企业法人' },
            { value: '02', text: '02-非法人企业' },
            { value: '03', text: '03-机关' },
            { value: '04', text: '04-实行预算管理的事业单位' },
            { value: '05', text: '05-非预算管理的事业单位' },
            { value: '06', text: '06-团级(含)以上军队及分散执勤的支(分)队' },
            { value: '07', text: '07-团级(含)以上武警部队及分散执勤的支(分)队' },
            { value: '08', text: '08-社会团体' },
            { value: '09', text: '09-宗教组织' },
            { value: '10', text: '10-民办非企业组织' },
            { value: '11', text: '11-外地常设机构' },
            { value: '12', text: '12-外国驻华机构' },
            { value: '13', text: '13-有字号的个体工商户' },
            { value: '14', text: '14-无字号的个体工商户' },
            { value: '15', text: '15-居民委员会、村民委员会、社区委员会' },
            { value: '16', text: '16-单位设立的独立核算的附属机构' },
            { value: '17', text: '17-其他组织' },
            { value: '20', text: '20-境外组织' },
            { value: '50', text: '50-QFII' },
            { value: '51', text: '51-境外贸易机构' },
            { value: '52', text: '52-跨境清算' }
        ],
        //存款人类别(基本户, 一般户)
        jibenDepositorType: [
            { value: '01', text: '01-企业法人' },
            { value: '02', text: '02-非法人企业' },
            { value: '03', text: '03-机关' },
            { value: '04', text: '04-实行预算管理的事业单位' },
            { value: '05', text: '05-非预算管理的事业单位' },
            { value: '06', text: '06-团级(含)以上军队及分散执勤的支(分)队' },
            { value: '07', text: '07-团级(含)以上武警部队及分散执勤的支(分)队' },
            { value: '08', text: '08-社会团体' },
            { value: '09', text: '09-宗教组织' },
            { value: '10', text: '10-民办非企业组织' },
            { value: '11', text: '11-外地常设机构' },
            { value: '12', text: '12-外国驻华机构' },
            { value: '13', text: '13-有字号的个体工商户' },
            { value: '14', text: '14-无字号的个体工商户' },
            { value: '15', text: '15-居民委员会、村民委员会、社区委员会' },
            { value: '16', text: '16-单位设立的独立核算的附属机构' },
            { value: '17', text: '17-其他组织' },
            { value: '20', text: '20-境外组织' }
        ],
        //存款人类别(临时户)
        linshiDepositorType: [
            { value: '50', text: '50-QFII' },
            { value: '51', text: '51-境外贸易机构' },
            { value: '52', text: '52-跨境清算' }
        ],
        //组织机构类别
        orgType: [
            { value: '1', text: '1-企业' },
            { value: '2', text: '2-事业单位' },
            { value: '3', text: '3-机关' },
            { value: '4', text: '4-社会团体' },
            { value: '7', text: '7-个体工商户' },
            { value: '9', text: '9-其他' }
        ],
        //组织机构类别详细
        orgTypeDetail: {
            '1': [
                { value: '10', text: '10-企业法人' },
                { value: '11', text: '11-其他企业' },
                { value: '12', text: '12-农民专业合作社' },
                { value: '13', text: '13-个人独资、合伙企业' },
                { value: '14', text: '14-企业的分支机构' }
            ],
            '2': [
                { value: '20', text: '20-事业法人' },
                { value: '21', text: '21-未登记的事业单位' },
                { value: '24', text: '24-事业单位的分支机构' }
            ],
            '3': [
                { value: '30', text: '30-机关法人' },
                { value: '31', text: '31-机关的内设机构' },
                { value: '32', text: '32-机关的下设机构' }
            ],
            '4': [
                { value: '40', text: '40-社会团体法人' },
                { value: '41', text: '41-社会团体分支机构' }
            ],
            '7': [
                { value: '70', text: '70-个体工商户' }
            ],
            '9': [
                { value: '51', text: '51-民办非企业' },
                { value: '52', text: '52-基金会' },
                { value: '53', text: '53-居委会' },
                { value: '54', text: '54-村委会' },
                { value: '60', text: '60-律师事务所、司法鉴定所' },
                { value: '61', text: '61-宗教活动场所' },
                { value: '62', text: '62-境外在境内成立的组织机构' },
                { value: '99', text: '99-其他' }
            ]
        },
        //账户名称构成方式
        accountNameFrom: [
            { value: '0', text: '与存款人名称一致' },
            { value: '1', text: '存款人名称加内设部门' },
            { value: '2', text: '存款人名称加资金性质' }
        ],
        //资金性质
        capitalProperty: [
            { value: '01', text: '01-基本建设资金' },
            { value: '02', text: '02-更新改造资金' },
            { value: '03', text: '03-财政预算外资金' },
            { value: '04', text: '04-粮、棉、油收购资金' },
            { value: '05', text: '05-证券交易结算资金' },
            { value: '06', text: '06-期货交易保证金' },
            { value: '07', text: '07-金融机构存放同业资金' },
            { value: '08', text: '08-政策性房地产开发资金' },
            { value: '09', text: '09-单位银行卡备用金' },
            { value: '10', text: '10-住房基金' },
            { value: '11', text: '11-社会保障金' },
            { value: '12', text: '12-收入汇缴资金' },
            { value: '13', text: '13-业务支出资金' },
            { value: '14', text: '14-单位内部的党、团、工会经费' },
            { value: '16', text: '16-其他需要专项管理和使用的资金' }
        ],
        //取现标识
        enchashmentType: [
            { value: '1', text: '1-是' },
            { value: '0', text: '0-否' }
        ],
        //申请开户原因
        acctCreateReason: [
            { value: '1', text: '1-建筑施工及安装' },
            { value: '2', text: '2-从事临时经营活动' }
        ],
        //销户原因
        acctCancelReason: [
            { value: '2', text: '2-撤并' },
            { value: '3', text: '3-解散' },
            { value: '4', text: '4-宣告破产' },
            { value: '5', text: '5-关闭' },
            { value: '6', text: '6-被吊销营业执照或执业许可证' },
            { value: '7', text: '7-其它' }
        ],
        //操作类型
        operateType: [
            { value: 'keepForm', text: '保存' },
            { value: 'saveForm', text: '申请' },
            { value: 'verifyForm', text: '上报' },
            { value: 'addInfoForm', text: '补录' },
            { value: 'rejectForm', text: '驳回' },
            { value: 'verifyPass', text: '审核通过' },
            { value: 'verifyNotPass', text: '审核退回' },
            { value: 'syncForm', text: '审核并上报' }
        ],
        //流水操作类型
        billType: [
            { value: 'ACCT_OPEN', text: '开户' },
            { value: 'ACCT_CHANGE', text: '变更' },
            { value: 'ACCT_SUSPEND', text: '久悬' },
            { value: 'ACCT_REVOKE', text: '销户' },
            { value: 'ACCT_INIT', text: '存量' },
            { value: 'ACCT_EXTENSION', text: '展期' }
        ],
        //审核状态
        status: [
            { value: 'WAITING_SUPPLEMENT', text: '待补录' },
            { value: 'NEW', text: '新建' },
            { value: 'APPROVING', text: '审核中' },
            { value: 'APPROVED', text: '审核通过' },
            { value: 'REJECT', text: '驳回' },
            { value: 'WAITING_CORE', text: '待核心开户' }
        ],
        //上报状态
        syncStatus: [
            { value: 'buTongBu', text: '无需上报' },
            { value: 'weiTongBu', text: '未同步' },
            { value: 'tongBuChengGong', text: '上报成功' },
            { value: 'tongBuZhong', text: '处理中' },
            { value: 'tuiHui', text: '退回' },
            { value: 'tongBuShiBai', text: '上报失败' }
        ],
        //上报操作方式
        syncMethod: [
            { value: 'personSyncType', text: '手工上报' },
            { value: 'autoSyncType', text: '自动上报' },
            { value: 'halfSyncType', text: '手工补录' }
        ],
        //审核状态
        checkStatus: [
            { value: 'WaitCheck', text: '待审核' },
            { value: 'CheckPass', text: '审核通过' },
            { value: 'NoCheck', text: '无需审核' }
        ],
        //影像补录状态
        imageBuluStatus: [
            { value: '0', text: '未补录' },
            { value: '1', text: '已补录' }
        ],
        //预约操作类型
        applyBillType: [
            { value: 'ACCT_OPEN', text: '新开户' },
            { value: 'ACCT_CHANGE', text: '变更' },
            { value: 'ACCT_REVOKE', text: '销户' }
        ],
        //预约状态
        applyStatus: [
            { value: 'UnComplete', text: '待受理' },
            { value: 'SUCCESS', text: '受理成功' },
            { value: 'FAIL', text: '受理退回' },
            { value: 'REGISTER_SUCCESS', text: '开户成功' },
            { value: 'REGISTER_FAIL', text: '开户失败' },
            { value: 'BREAK_APPOINT', text: '爽约' }
        ],
        //本异地标识
        openAccountSiteType: [
            { value: 'LOCAL', text: '本地' },
            { value: 'ALLOPATRIC', text: '异地' }
        ],
        //国籍
        nationality: [
            { value: 'CHN', text: '中国' },
            { value: 'ABW', text: '阿鲁巴' },
            { value: 'AFG', text: '阿富汗' },
            { value: 'AGO', text: '安哥拉' },
            { value: 'AIA', text: '安圭拉' },
            { value: 'ALB', text: '阿尔巴尼亚' },
            { value: 'AND', text: '安道尔' },
            { value: 'ANT', text: '荷属安的列斯' },
            { value: 'ARE', text: '阿联酋' },
            { value: 'ARG', text: '阿根廷' },
            { value: 'ARM', text: '亚美尼亚' },
            { value: 'ASM', text: '美属萨摩亚' },
            { value: 'ATA', text: '南极洲' },
            { value: 'ATF', text: '法属南部领地' },
            { value: 'ATG', text: '安提瓜和巴布达' },
            { value: 'AUS', text: '澳大利亚' },
            { value: 'AUT', text: '奥地利' },
            { value: 'AZE', text: '阿塞拜疆' },
            { value: 'BDI', text: '布隆迪' },
            { value: 'BEL', text: '比利时' },
            { value: 'BEN', text: '贝宁' },
            { value: 'BFA', text: '布基纳法索' },
            { value: 'BGD', text: '孟加拉国' },
            { value: 'BGR', text: '保加利亚' },
            { value: 'BHR', text: '巴林' },
            { value: 'BHS', text: '巴哈马' },
            { value: 'BIH', text: '波黑' },
            { value: 'BLR', text: '白俄罗斯' },
            { value: 'BLZ', text: '伯利兹' },
            { value: 'BMU', text: '百慕大' },
            { value: 'BOL', text: '玻利维亚' },
            { value: 'BRA', text: '巴西' },
            { value: 'BRB', text: '巴巴多斯' },
            { value: 'BRN', text: '文莱' },
            { value: 'BTN', text: '不丹' },
            { value: 'BVT', text: '布维岛' },
            { value: 'BWA', text: '博茨瓦纳' },
            { value: 'CAF', text: '中非' },
            { value: 'CAN', text: '加拿大' },
            { value: 'CCK', text: '科科斯(基林)群岛' },
            { value: 'CHE', text: '瑞士' },
            { value: 'CHL', text: '智利' },
            { value: 'CIV', text: '科特迪瓦' },
            { value: 'CMR', text: '喀麦隆' },
            { value: 'COD', text: '刚果（金）' },
            { value: 'COG', text: '刚果（布）' },
            { value: 'COK', text: '库克群岛' },
            { value: 'COL', text: '哥伦比亚' },
            { value: 'COM', text: '科摩罗' },
            { value: 'CPV', text: '佛得角' },
            { value: 'CRI', text: '哥斯达黎加' },
            { value: 'CUB', text: '古巴' },
            { value: 'CXR', text: '圣诞岛' },
            { value: 'CYM', text: '开曼群岛' },
            { value: 'CYP', text: '塞浦路斯' },
            { value: 'CZE', text: '捷克' },
            { value: 'DEU', text: '德国' },
            { value: 'DJI', text: '吉布提' },
            { value: 'DMA', text: '多米尼克' },
            { value: 'DNK', text: '丹麦' },
            { value: 'DOM', text: '多米尼加' },
            { value: 'DZA', text: '阿尔及利亚' },
            { value: 'ECU', text: '厄瓜多尔' },
            { value: 'EGY', text: '埃及' },
            { value: 'ERI', text: '厄立特里亚' },
            { value: 'ESH', text: '西撒哈拉' },
            { value: 'ESP', text: '西班牙' },
            { value: 'EST', text: '爱沙尼亚' },
            { value: 'ETH', text: '埃塞俄比亚' },
            { value: 'FIN', text: '芬兰' },
            { value: 'FJI', text: '斐济' },
            { value: 'FLK', text: '福克兰群岛（马尔维纳斯群岛）' },
            { value: 'FRA', text: '法国' },
            { value: 'FRO', text: '法罗群岛' },
            { value: 'FSM', text: '密克罗尼西亚联邦' },
            { value: 'GAB', text: '加蓬' },
            { value: 'GBR', text: '英国' },
            { value: 'GEO', text: '格鲁吉亚' },
            { value: 'GHA', text: '加纳' },
            { value: 'GIB', text: '直布罗陀' },
            { value: 'GIN', text: '几内亚' },
            { value: 'GLP', text: '瓜德罗普' },
            { value: 'GMB', text: '冈比亚' },
            { value: 'GNB', text: '几内亚比绍' },
            { value: 'GNQ', text: '赤道几内亚' },
            { value: 'GRC', text: '希腊' },
            { value: 'GRD', text: '格林纳达' },
            { value: 'GRL', text: '格陵兰' },
            { value: 'GTM', text: '危地马拉' },
            { value: 'GUF', text: '法属圭亚那' },
            { value: 'GUM', text: '关岛' },
            { value: 'GUY', text: '圭亚那' },
            { value: 'HKG', text: '香港' },
            { value: 'HMD', text: '赫德岛和麦克唐纳岛' },
            { value: 'HND', text: '洪都拉斯' },
            { value: 'HRV', text: '克罗地亚' },
            { value: 'HTI', text: '海地' },
            { value: 'HUN', text: '匈牙利' },
            { value: 'IDN', text: '印度尼西亚' },
            { value: 'IND', text: '印度' },
            { value: 'IOT', text: '英属印度洋领地' },
            { value: 'IRL', text: '爱尔兰' },
            { value: 'IRN', text: '伊朗' },
            { value: 'IRQ', text: '伊拉克' },
            { value: 'ISL', text: '冰岛' },
            { value: 'ISR', text: '以色列' },
            { value: 'ITA', text: '意大利' },
            { value: 'JAM', text: '牙买加' },
            { value: 'JOR', text: '约旦' },
            { value: 'JPN', text: '日本' },
            { value: 'KAZ', text: '哈萨克斯坦' },
            { value: 'KEN', text: '肯尼亚' },
            { value: 'KGZ', text: '吉尔吉斯斯坦' },
            { value: 'KHM', text: '柬埔寨' },
            { value: 'KIR', text: '基里巴斯' },
            { value: 'KNA', text: '圣基茨和尼维斯' },
            { value: 'KOR', text: '韩国' },
            { value: 'KWT', text: '科威特' },
            { value: 'LAO', text: '老挝' },
            { value: 'LBN', text: '黎巴嫩' },
            { value: 'LBR', text: '利比里亚' },
            { value: 'LBY', text: '利比亚' },
            { value: 'LCA', text: '圣卢西亚' },
            { value: 'LIE', text: '列支敦士登' },
            { value: 'LKA', text: '斯里兰卡' },
            { value: 'LSO', text: '莱索托' },
            { value: 'LTU', text: '立陶宛' },
            { value: 'LUX', text: '卢森堡' },
            { value: 'LVA', text: '拉脱维亚' },
            { value: 'MAC', text: '澳门' },
            { value: 'MAR', text: '摩洛哥' },
            { value: 'MCO', text: '摩纳哥' },
            { value: 'MDA', text: '摩尔多瓦' },
            { value: 'MDG', text: '马达加斯加' },
            { value: 'MDV', text: '马尔代夫' },
            { value: 'MEX', text: '墨西哥' },
            { value: 'MHL', text: '马绍尔群岛' },
            { value: 'MKD', text: '前南马其顿' },
            { value: 'MLI', text: '马里' },
            { value: 'MLT', text: '马耳他' },
            { value: 'MMR', text: '缅甸' },
            { value: 'MNE', text: '黑山' },
            { value: 'MNG', text: '蒙古' },
            { value: 'MNP', text: '北马里亚纳' },
            { value: 'MOZ', text: '莫桑比克' },
            { value: 'MRT', text: '毛里塔尼亚' },
            { value: 'MSR', text: '蒙特塞拉特' },
            { value: 'MTQ', text: '马提尼克' },
            { value: 'MUS', text: '毛里求斯' },
            { value: 'MWI', text: '马拉维' },
            { value: 'MYS', text: '马来西亚' },
            { value: 'MYT', text: '马约特' },
            { value: 'NAM', text: '纳米比亚' },
            { value: 'NCL', text: '新喀里多尼亚' },
            { value: 'NER', text: '尼日尔' },
            { value: 'NFK', text: '诺福克岛' },
            { value: 'NGA', text: '尼日利亚' },
            { value: 'NIC', text: '尼加拉瓜' },
            { value: 'NIU', text: '纽埃' },
            { value: 'NLD', text: '荷兰' },
            { value: 'NOR', text: '挪威' },
            { value: 'NPL', text: '尼泊尔' },
            { value: 'NRU', text: '瑙鲁' },
            { value: 'NZL', text: '新西兰' },
            { value: 'OMN', text: '阿曼' },
            { value: 'PAK', text: '巴基斯坦' },
            { value: 'PAN', text: '巴拿马' },
            { value: 'PCN', text: '皮特凯恩' },
            { value: 'PER', text: '秘鲁' },
            { value: 'PHL', text: '菲律宾' },
            { value: 'PLW', text: '帕劳' },
            { value: 'PNG', text: '巴布亚新几内亚' },
            { value: 'POL', text: '波兰' },
            { value: 'PRI', text: '波多黎各' },
            { value: 'PRK', text: '朝鲜' },
            { value: 'PRT', text: '葡萄牙' },
            { value: 'PRY', text: '巴拉圭' },
            { value: 'PSE', text: '巴勒斯坦' },
            { value: 'PYF', text: '法属波利尼西亚' },
            { value: 'QAT', text: '卡塔尔' },
            { value: 'REU', text: '留尼汪' },
            { value: 'ROM', text: '罗马尼亚' },
            { value: 'RUS', text: '俄罗斯联邦' },
            { value: 'RWA', text: '卢旺达' },
            { value: 'SAU', text: '沙特阿拉伯' },
            { value: 'SDN', text: '苏丹' },
            { value: 'SEN', text: '塞内加尔' },
            { value: 'SGP', text: '新加坡' },
            { value: 'SGS', text: '南乔治亚岛和南桑德韦奇岛' },
            { value: 'SHN', text: '圣赫勒拿' },
            { value: 'SJM', text: '斯瓦尔巴岛和扬马延岛' },
            { value: 'SLB', text: '所罗门群岛' },
            { value: 'SLE', text: '塞拉利昂' },
            { value: 'SLV', text: '萨尔瓦多' },
            { value: 'SMR', text: '圣马力诺' },
            { value: 'SOM', text: '索马里' },
            { value: 'SPM', text: '圣皮埃尔和密克隆' },
            { value: 'SRB', text: '塞尔维亚' },
            { value: 'STP', text: '圣多美和普林西比' },
            { value: 'SUR', text: '苏里南' },
            { value: 'SVK', text: '斯洛伐克' },
            { value: 'SVN', text: '斯洛文尼亚' },
            { value: 'SWE', text: '瑞典' },
            { value: 'SWZ', text: '斯威士兰' },
            { value: 'SYC', text: '塞舌尔' },
            { value: 'SYR', text: '叙利亚' },
            { value: 'TCA', text: '特克斯和凯科斯群岛' },
            { value: 'TCD', text: '乍得' },
            { value: 'TGO', text: '多哥' },
            { value: 'THA', text: '泰国' },
            { value: 'TJK', text: '塔吉克斯坦' },
            { value: 'TKL', text: '托克劳' },
            { value: 'TKM', text: '土库曼斯坦' },
            { value: 'TMP', text: '东帝汶' },
            { value: 'TON', text: '汤加' },
            { value: 'TTO', text: '特立尼达和多巴哥' },
            { value: 'TUN', text: '突尼斯' },
            { value: 'TUR', text: '土耳其' },
            { value: 'TUV', text: '图瓦卢' },
            { value: 'TWN', text: '台湾' },
            { value: 'TZA', text: '坦桑尼亚' },
            { value: 'UGA', text: '乌干达' },
            { value: 'UKR', text: '乌克兰' },
            { value: 'UMI', text: '美国本土外小岛屿' },
            { value: 'URY', text: '乌拉圭' },
            { value: 'USA', text: '美国' },
            { value: 'UZB', text: '乌兹别克斯坦' },
            { value: 'VAT', text: '梵蒂冈' },
            { value: 'VCT', text: '圣文森特和格林纳丁斯' },
            { value: 'VEN', text: '委内瑞拉' },
            { value: 'VGB', text: '英属维尔京群岛' },
            { value: 'VIR', text: '美属维尔京群岛' },
            { value: 'VNM', text: '越南' },
            { value: 'VUT', text: '瓦努阿图' },
            { value: 'WLF', text: '瓦利斯和富图纳' },
            { value: 'WSM', text: '萨摩亚' },
            { value: 'YEM', text: '也门' },
            { value: 'ZAF', text: '南非' },
            { value: 'ZMB', text: '赞比亚' },
            { value: 'ZWE', text: '津巴布韦' }
        ]
    }
}