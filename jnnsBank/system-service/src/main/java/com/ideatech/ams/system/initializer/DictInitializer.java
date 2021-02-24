package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.dict.dao.DictionaryDao;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.entity.DictionaryPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liangding
 * @create 2018-05-28 上午9:37
 **/
@Component
public class DictInitializer extends AbstractDataInitializer {
    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private OptionDao optionDao;

    @Override
    protected void doInit() throws Exception {
		createDictionary(34510011L,"证件到期类型");
		createDictionary(34518957L,"账户性质(销户)");
		createDictionary(34520058L,"账户状态");
		createDictionary(34520060L,"账户同步状态/信用代码状态");
		createDictionary(76965711L,"组织机构类别");
		createDictionary(580885384L,"法人身份证件类型");
		createDictionary(801272422L,"审核状态");
		createDictionary(1057945102L,"证明文件1种类(非临时)");
		createDictionary(1224598403L,"机构状态");
		createDictionary(1348184164L,"证明文件1种类(基本户)");
		createDictionary(1396368840L,"data2amsRegCurrencyType");
		createDictionary(1493428733L,"证明文件1种类(专用户)");
		createDictionary(1908104365L,"业务操作类型");
		createDictionary(2004845588L,"登记部门");
		createDictionary(2192325203L,"上报状态");
		createDictionary(2326445218L,"行业归属");
		createDictionary(2326445219L,"industryCodeValue2Item");
		createDictionary(2483230823L,"证明文件1种类(一般户)");
		createDictionary(2621043762L,"data2eccsLegalIdcardType");
		createDictionary(3252559432L,"申请开户原因(非临时)");
		createDictionary(3550523904L,"acctTypeAms2db");
		createDictionary(3703736320L,"判断是否");
		createDictionary(3767582583L,"acctType2Pbc");
		createDictionary(4148943009L,"证明文件1种类(临时户)");
		createDictionary(4396140229L,"工商注册类型");
		createDictionary(4510851424L,"国家");
		createDictionary(4863883672L,"注册币种");
		createDictionary(4903780269L,"证明文件2种类(基本户)");
		createDictionary(5139679413L,"基本户状态");
		createDictionary(5352592938L,"资金性质");
		createDictionary(5751711480L,"企业规模");
		createDictionary(5877555630L,"operateType2Pbc");
		createDictionary(6914306530L,"销户原因");
		createDictionary(7019913186L,"账户性质");
		createDictionary(7713777178L,"存款人类别(基本户)");
		createDictionary(7959776041L,"法人类型");
		createDictionary(8804231399L,"经济类型");
		createDictionary(8830637851L,"账户构成方式");
		createDictionary(8991254354L,"存款人类别(特殊户)");
		createDictionary(9438341854L,"证明文件1种类(特殊户)");
		createDictionary(9693328930L,"证明文件2种类(专用户)");
		createDictionary(9902238703L,"证明文件2种类(特殊户)");
		createDictionary(17857276459723L,"core2dataOperateType");
		createDictionary(18457108189458L,"core2dataCapitalProperty");
		createDictionary(27476533824580L,"regTypeValue2Item");
		createDictionary(32119222946691L,"depositorTypeValue2Item");
		createDictionary(32648461014122L,"core2dataLegalIdCardType");
		createDictionary(39651326948056L,"core2dataCorpScale");
		createDictionary(48492452079412L,"core2dataRegCurrencyType");
		createDictionary(53603941347017L,"core2dataEconomyType");
		createDictionary(65257890581027L,"regCurrencyType2Item");
		createDictionary(66157031576049L,"账户性质(加未知)");
		createDictionary(73700519166947L,"core2dataAcctType");
		createDictionary(81778761613140L,"legalIdcardTypeValue2Item");
		createDictionary(83463770121813L,"上报操作方式");
		createDictionary(87920398843374L,"core2dataIndustryCode");
		createDictionary(94548303251508L,"core2dataRegType");
		createDictionary(96773262411837L,"core2dataDepositorType");

		createDictionary(32119222946693L,"legalTypeValue2Item");
		createDictionary(32119222946694L,"accountFileTypeYiBanValueItem");
		createDictionary(32119222946695L,"accountNameFromTypeValueItem");
		createDictionary(32119222946696L,"accountFileTypeYuSuanValueItem");
		createDictionary(32119222946697L,"accountFileTypeYuSuanValue2Item");
		createDictionary(32119222946698L,"capitalPropertyValueItem");
		createDictionary(32119222946699L,"fileTypeLinShiValueItem");
		createDictionary(32119222946700L,"fileTypeFeiLinShiValueItem");
		createDictionary(32119222946701L,"fileTypejiBenValueItem");
		createDictionary(32119222946702L,"fileTypejiBenValue2Item");
		createDictionary(32119222946703L,"industryCodeValueItem");
		createDictionary(32119222946704L,"createAccountReasonValueItem");
		createDictionary(32119222946705L,"accountFileTypeFeiLinShiValueItem");
		createDictionary(32119222946706L,"depositorTypeTeShuValueItem");
		createDictionary(32119222946707L,"fileTypeTeShuValueItem");
		createDictionary(32119222946708L,"fileTypeTeShuValue2Item");

		createDictionary(32119222946709L,"corpScaleValue2Item");
		createDictionary(32119222946710L,"capitalPropertyValue2Item");
		createDictionary(32119222946711L,"orgStatusValue2Item");
		createDictionary(32119222946712L,"basicAccountStatusValue2Item");
		createDictionary(32119222946722L,"regOfficeValue2Item");
		createDictionary(32119222946732L,"orgTypeValue2Item");

		createDictionary(40000001L,"core2pbc-acctType");
		createDictionary(40000002L,"core2pbc-accountStatus");
		createDictionary(40000003L,"core2pbc-depositorType");
		createDictionary(40000004L,"core2pbc-industryCode");
		createDictionary(40000005L,"core2pbc-corpScale");
		createDictionary(40000006L,"core2pbc-regCurrencyType");
		createDictionary(40000008L,"core2pbc-fileType-jiben");
		createDictionary(40000009L,"core2pbc-fileType-feilinshi");
		createDictionary(40000010L,"core2pbc-fileType-yiban");
		createDictionary(40000011L,"core2pbc-fileType-linshi");
		createDictionary(40000012L,"core2pbc-fileType-yusuan");
		createDictionary(40000013L,"core2pbc-legalType");//法定代表人类型
		createDictionary(40000014L,"core2pbc-legalIdcardType");//法人证件类型
		createDictionary(40000015L,"core2pbc-parLegalType");//上级法定代表人类型
		createDictionary(40000016L,"core2pbc-parLegalIdcardType");//上级法人证件类型
		createDictionary(40000017L,"core2pbc-accountNameFrom");//账户构成方式
		createDictionary(40000018L,"core2pbc-capitalProperty");//资金性质
		createDictionary(40000019L,"core2pbc-enchashmentType");//取现标识
		createDictionary(40000020L,"core2pbc-moneyManagerCtype");//资金管理人身份证种类
		createDictionary(40000021L,"core2pbc-insideSaccdepmanKind");//负责人身份证件种类
		createDictionary(40000022L,"core2pbc-createAccountReason");//申请开户原因
		createDictionary(40000023L,"core2pbc-flsFzrLegalIdcardType");//建筑施工安装及实时负责人身份证件种类
		createDictionary(40000024L,"core2eccs-regOffice");//登记部门
		createDictionary(40000025L,"core2eccs-regType");//登记注册号类型
		createDictionary(40000026L,"core2eccs-corpScale");//企业规模
		createDictionary(40000027L,"core2eccs-orgType");//组织机构类别
		createDictionary(40000028L,"core2eccs-orgTypeDetail");//组织机构类别细分
		createDictionary(40000029L,"core2eccs-economyType");//经济类型

		createDictionary(40000030L,"core2pbc-personType");
		createDictionary(40000031L,"core2pbc-fileType-zhuanyong");
		createDictionary(40000032L,"core2pbc-fileType-feiyusuan");
		createDictionary(40000033L,"core2pbc-fromSource");
		createDictionary(40000034L,"core2pbc-billType");
		createDictionary(40000035L,"core2pbc-customerCategory");
		createDictionary(40000036L,"core2pbc-orgType");
		createDictionary(40000037L,"core2pbc-orgTypeDetail");
		createDictionary(40000038L,"core2pbc-economyType");
		createDictionary(40000039L,"core2pbc-regType");
		createDictionary(40000040L,"core2pbc-orgStatus");
		createDictionary(40000041L,"core2pbc-isIdentification");
		createDictionary(40000042L,"core2pbc-basicAccountStatus");
		createDictionary(40000043L,"core2pbc-regOffice");
		createDictionary(40000044L,"core2pbc-fileType-teshu");

		createDictionary(40000045L,"core2pbc-acctCreateReason");
		createDictionary(40000046L,"core2pbc-acctBigType");

		createDictionary(40000047L,"core2pbc-fileType2-zhuanyong");
		createDictionary(40000048L,"core2pbc-fileType2-teshu");
		createDictionary(40000049L,"core2pbc-fileType2-jiben");
		createDictionary(40000050L,"core2pbc-fileType2-yusuan");
//		createDictionary(40000051L,"core2pbc-fileType2-feiyusuan");
		createDictionary(40000052L,"core2pbc-fileType2-linshi");

		createDictionary(40000053L,"core2pbc-acctFileType-yiban");
		createDictionary(40000054L,"core2pbc-acctFileType-yusuan");
		createDictionary(40000055L,"core2pbc-acctFileType-feiyusuan");
		createDictionary(40000056L,"core2pbc-acctFileType-feilinshi");
		createDictionary(40000057L,"core2pbc-acctFileType-linshi");
		createDictionary(40000058L,"core2pbc-acctFileType-zhuanyong");

		createDictionary(40000060L,"core2pbc-acctFileType2-yusuan");
		createDictionary(40000061L,"core2pbc-acctFileType2-zhuanyong");

		createDictionary(40000062L,"core2pbc-acctCancelReason");

		createDictionary(20000000L,"人行审核状态");
		//工商电子审核字典
		createDictionary(30000008L,"saicQuery-checkCode");
		createDictionary(30000009L,"saicQuery-errorCode");

		//人行电子审核字典
		createDictionary(30000010L,"pbcQuery-checkCode");
		createDictionary(30000011L,"pbcQuery-errorCode");

		//实时人行接口字典
		createDictionary(30000012L,"pbcRealTime-SyncCheckCode");
		createDictionary(30000013L,"pbcRealTiem-syncErrorCode");

		//实施信用接口字典
		createDictionary(30000014L,"eccsRealTime-SyncCheckCode");
		createDictionary(30000015L,"eccsRealTiem-syncErrorCode");

		createDictionary(90000000L,"imageType");

		createDictionary(34690862699725L,"审核状态(重构)");

		createDictionary(34690862699726L,"partnerTypeValueItem");
		createDictionary(34690862699727L, "roleTypeValueItem");

		createDictionary(32119222946713L,"economyTypeValue2Item");
		createDictionary(32119222946714L,"orgTypeDetailValue2Item");
		createDictionary(32119222946715L,"isIdentificationValue2Item");

		createDictionary(32119222946716L,"heabo2pbc-fromSource");

		createDictionary(32119222946717L,"imageOption");

		createDictionary(32119222946718L,"预约渠道类型");

		//联网核查人员类型
		createDictionary(32119222946719L,"idCardType");

		createDictionary(32119222946720L,"acctCancelReasonValueItem");

		//预约退回回执下拉选项
		createDictionary(32119222946721L,"applyNoteValue2Item");
		//双录差错类型
		createDictionary(42119222946722L,"imageVideoErrorType");
	}



    private void createDictionary(Long id, String name) {
        if (String.valueOf(id).length() > 14) {
            id = Long.valueOf(String.valueOf(id).substring(0, 14));
        }
        DictionaryPo dictionary = new DictionaryPo();
        dictionary.setId(id);
        dictionary.setName(name);
        dictionaryDao.save(dictionary);
    }

    @Override
    protected boolean isNeedInit() {
        return dictionaryDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.DICT_INITIALIZER_INDEX;
    }
}
