package com.ideatech.ams.controller.pbc;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsFeilinshiSyncCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.service.ams.cancel.AmsFeilinshiOpenBeiAnService;
import com.ideatech.ams.pbc.service.ams.cancel.AmsJibenOpenBeiAnService;
import com.ideatech.ams.pbc.spi.AmsMainService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fantao
 * @date 2019-05-23 16:23
 */
@RestController
@RequestMapping("/pbc")
public class PbcController {


    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private AmsJibenOpenBeiAnService amsJibenOpenBeiAnService;

    @Autowired
    private AmsFeilinshiOpenBeiAnService amsFeilinshiOpenBeiAnService;

    @GetMapping("/jbzx")
    public Object jibenZhenxun(String u, String p, String ip, AllAcct allAcct) throws Exception {

        LoginAuth auth = login(u, p, ip);

        AllAcct jiben = createJiben();
        copyNotNull(allAcct, jiben);

        amsJibenOpenBeiAnService.openAccountFirstStep(auth, jiben);

        return allAcct.toString();
    }

    @GetMapping("/flszx")
    public Object feilinshiZhenxun(String u, String p, String ip, AmsFeilinshiSyncCondition condition) throws Exception {
        LoginAuth auth = login(u, p, ip);

        AmsFeilinshiSyncCondition feilinshi = createFeilinshi();
        copyNotNull(condition, feilinshi);

        amsFeilinshiOpenBeiAnService.openAccountFirstStep(auth, feilinshi);

        return feilinshi.toString();
    }

    @GetMapping("/flskh")
    public Object feilinshiOpen(String u, String p, String ip, AmsFeilinshiSyncCondition condition) throws Exception {
        LoginAuth auth = login(u, p, ip);

        AmsFeilinshiSyncCondition feilinshi = createFeilinshi();
        copyNotNull(condition, feilinshi);

        amsFeilinshiOpenBeiAnService.openAccountFirstStep(auth, feilinshi);
        amsFeilinshiOpenBeiAnService.openAccountSecondStep(auth, feilinshi);

        return feilinshi.toString();
    }


    private LoginAuth login(String u, String p, String ip) {
        PbcUserAccount pbcUserAccount = new PbcUserAccount();
        pbcUserAccount.setLoginIp(ip);
        pbcUserAccount.setLoginUserName(u);
        pbcUserAccount.setLoginPassWord(p);
        return amsMainService.amsLogin(pbcUserAccount);
    }

    private AllAcct createJiben() {
        AllAcct allAcct = new AllAcct();
        allAcct.setAcctType(SyncAcctType.jiben);
        allAcct.setOperateType(SyncOperateType.ACCT_OPEN);
        allAcct.setAcctCreateDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));

        allAcct.setDepositorType("01");
        allAcct.setDepositorName("爱丽丝股份有限公司");

        allAcct.setFileNo("125456854754812563");

        String randomAcctNo = RandomStringUtils.randomNumeric(12);
        allAcct.setAcctNo(randomAcctNo);

        return allAcct;
    }

    private AmsFeilinshiSyncCondition createFeilinshi() {

        AmsFeilinshiSyncCondition allAcct = new AmsFeilinshiSyncCondition();
        allAcct.setAcctType(SyncAcctType.feilinshi);
        allAcct.setOperateType(SyncOperateType.ACCT_OPEN);
        allAcct.setAcctCreateDate(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()));

        String randomAcctNo = RandomStringUtils.randomNumeric(12);
        allAcct.setAcctNo(randomAcctNo);

        allAcct.setCreateAccountReason("1");
        allAcct.setEffectiveDate("2019-05-30");

        return allAcct;
    }

    private void copyNotNull(Object source, Object target) {
        if (source == null) {
            return;
        }
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }


    public static String[] getNullPropertyNames(Object source) {

        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else if (srcValue.toString().equals("")) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[emptyNames.size()]);
    }


}
