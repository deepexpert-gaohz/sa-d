package com.ideatech.ams.initializer;

import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zoulang
 * @create 2019-06-16 上午9:52
 **/
@Component
@Log4j
public class JnnsOptionInitializer extends AbstractDataInitializer {

    @Autowired
    private OptionDao optionDao;

    @Override
    protected void doInit() throws Exception {

        //core2dataIndustryCode 行业归属，柜面转账管系统
        createOption(313159211L, 87920398843374L, 15, "P", "P$$教育$$第三层次            $$5");
        createOption(313159212L, 87920398843374L, 3, "D", "D$$电力、煤气及水的生产和供应业$$第二产业            $$2");
        createOption(313159213L, 87920398843374L, 13, "N", "N$$水利、环境和公共设施管理业$$第三层次            $$5");
        createOption(313159214L, 87920398843374L, 14, "O", "O$$居民服务和其他服务业$$第三层次            $$5");
        createOption(313159215L, 87920398843374L, 18, "S", "S$$公共管理和社会组织$$第四层次            $$6");
        createOption(313159216L, 87920398843374L, 2, "C", "C$$制造业$$第二产业            $$2");
        createOption(313159217L, 87920398843374L, 10, "K", "K$$房地产业$$第二层次            $$4");
        createOption(313159218L, 87920398843374L, 9, "J", "J$$金融业$$第二层次            $$4");
        createOption(313159219L, 87920398843374L, 1, "B", "B$$采矿业$$第二产业            $$2");
        createOption(313159220L, 87920398843374L, 16, "Q", "Q$$卫生、社会保障和社会福利业$$第三层次            $$5");
        createOption(313159221L, 87920398843374L, 5, "F", "F$$交通运输、仓储及邮政业$$第一层次            $$3");
        createOption(313159222L, 87920398843374L, 7, "H", "H$$批发和零售业$$第二层次            $$4");
        createOption(313159223L, 87920398843374L, 6, "G", "G$$信息传输、计算机服务和软件业$$第二层次            $$4");
        createOption(313159224L, 87920398843374L, 11, "L", "L$$租赁和商务服务业$$第二层次            $$4");
        createOption(313159225L, 87920398843374L, 4, "E", "E$$建筑业$$第二产业            $$2");
        createOption(313159226L, 87920398843374L, 0, "A", "A$$农、林、牧、渔业$$第一产业            $$1");
        createOption(313159227L, 87920398843374L, 8, "I", "I$$住宿和餐饮业$$第二层次            $$4");
        createOption(313159228L, 87920398843374L, 12, "M", "M$$科学研究、技术服务和地质勘查业$$第三层次            $$5");
        createOption(313159229L, 87920398843374L, 19, "T", "T$$国际组织（其他行业）$$第四层次            $$6");
        createOption(313159230L, 87920398843374L, 17, "R", "R$$文化、体育和娱乐业$$第三层次            $$5");
        createOption(313159231L, 87920398843374L, 20, "U", "U$$其他$$第一层次            $$3");
        //core2dataLegalIdCardType 法人证件类型
        createOption(31515921L, 32648461014122L, 1, "10101", "1");
        createOption(31515922L, 32648461014122L, 1, "10103", "8");
        createOption(31515923L, 32648461014122L, 1, "10401", "6");
        createOption(31515924L, 32648461014122L, 1, "10408", "6");
        createOption(31515925L, 32648461014122L, 1, "10501", "5");
        createOption(31515926L, 32648461014122L, 1, "10502", "2");
        createOption(31515927L, 32648461014122L, 1, "10503", "3");
        createOption(31515928L, 32648461014122L, 1, "10504", "3");
        createOption(31515929L, 32648461014122L, 1, "10505", "3");
        createOption(31515930L, 32648461014122L, 1, "10506", "3");
        createOption(31515931L, 32648461014122L, 1, "10507", "3");
        createOption(31515932L, 32648461014122L, 1, "10601", "4");
        createOption(31515933L, 32648461014122L, 1, "10602", "4");
        createOption(31515934L, 32648461014122L, 1, "10603", "4");
        createOption(31515935L, 32648461014122L, 1, "10604", "4");
        createOption(31515936L, 32648461014122L, 1, "10605", "4");
        createOption(31515937L, 32648461014122L, 1, "10606", "4");
        createOption(31515938L, 32648461014122L, 1, "10701", "7");
        createOption(31515939L, 32648461014122L, 1, "10703", "7");
        createOption(31515940L, 32648461014122L, 1, "19999", "9");
        createOption(31515941L, 32648461014122L, 1, "10708", "9");
        createOption(31515942L, 32648461014122L, 1, "10709", "9");
        createOption(31515943L, 32648461014122L, 1, "10900", "9");
        createOption(31515444L, 32648461014122L, 1, "A0006", "9");
        //core2pbc-acctBigType 账户状态 柜面转账管系统
        createOption(31215921L, 40000046L, 1, "0001", "jiben");
        createOption(31215922L, 40000046L, 2, "0002", "yiban");
        createOption(31215923L, 40000046L, 3, "0003", "linshi");
        createOption(31215924L, 40000046L, 4, "0004", "zhuanyong");
        createOption(31215925L, 40000046L, 5, "0006", "jiben");
        createOption(31215926L, 40000046L, 6, "0008", "jiben");
        createOption(31215927L, 40000046L, 7, "0009", "yiban");
        createOption(31215928L, 40000046L, 8, "0010", "zhuanyong");
        //core2pbc-accountStatus 账户状态 柜面转账管系统
        createOption(31415921L, 40000002L, 1, "A", "normal");
        createOption(31415922L, 40000002L, 2, "C", "revoke");
        createOption(31415923L, 40000002L, 3, "D", "suspend");
        createOption(31415924L, 40000002L, 4, "J", "unKnow");
        createOption(31415925L, 40000002L, 5, "E", "notExist");
        createOption(31415926L, 40000002L, 6, "G", "normal");

    }

    private void createOption(Long id, Long dictId, Integer index, String name, String value) {
        if (String.valueOf(id).length() > 14) {
            id = Long.valueOf(String.valueOf(id).substring(0, 14));
        }
        if (String.valueOf(dictId).length() > 14) {
            dictId = Long.valueOf(String.valueOf(dictId).substring(0, 14));
        }
        OptionPo option = new OptionPo();
        if (dictId != null && dictId > 0) {
            option.setDictionaryId(dictId);
        }
        option.setName(name);
        option.setValue(value);
        optionDao.save(option);
    }

    @Override
    protected boolean isNeedInit() {
        List<OptionPo> option = optionDao.findByDictionaryId(32648461014122L);
        if (option == null || option.size() < 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX + 1;
    }
}
