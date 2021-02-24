package com.ideatech.ams.account.service.pbc;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.EccsMainService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class PbcEccsServiceImpl implements PbcEccsService {

    @Autowired
    private EccsMainService eccsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void eccsAccountSync(AllBillsPublicDTO billsPublic) throws Exception, SyncException {

        if (billsPublic.getAcctType() == CompanyAcctType.jiben && (billsPublic.getBillType() != BillType.ACCT_OPEN && billsPublic.getBillType() != BillType.ACCT_CHANGE)) {
            throw new BizServiceException(EErrorCode.PBC_SYNC_ECCS_NONSUPPORT, "信用代码证系统上报只支持基本户的开户与变更！");
        }
        PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganFullId(billsPublic.getOrganFullId(), EAccountType.ECCS);
        PbcUserAccount pbcUserAccount = validatePbcUser(pbcAccountDto);

        //转换人行上报对象
        AllAcct allAcct = allBillsPublicService.allBillsPublic2AllAcct(billsPublic);

        if (billsPublic.getBillType() == BillType.ACCT_OPEN) {
            eccsMainService.eccsAccountOpenSync(pbcUserAccount, allAcct);
        } else if (billsPublic.getBillType() == BillType.ACCT_CHANGE) {
            //查询要变更信息的查询条件,未变更前此账户信息的信息
            EccsSearchCondition condition = new EccsSearchCondition();
            BeanCopierUtils.copyProperties(allAcct, condition);
            if(org.apache.commons.lang.StringUtils.isNotBlank(billsPublic.getRegNo()) &&
                    org.apache.commons.lang.StringUtils.isNotBlank(billsPublic.getRegType())){
                log.info("设置查询类型：{}，设置查询号码：{}",billsPublic.getRegType(),billsPublic.getRegNo());
                condition.setRegNo(billsPublic.getRegNo());
                condition.setRegType(billsPublic.getRegType());
            }else{
                //删除证明文件种类1的判断
                //判断证明文件种类是否有值，有值就根据位数判断是工商注册类型还是统一社会代码证
                if(org.apache.commons.lang.StringUtils.isNotBlank(billsPublic.getFileNo())){
                    String fileNo = billsPublic.getFileNo().replaceAll("　+", "");
                    String regType="";
                    log.info("证明文件1编号：{}",fileNo);
                    condition.setRegNo(fileNo);
                    if(fileNo.length()==15){
                        regType="01";
                    }
                    if(fileNo.length()==18){
                        regType="07";
                    }
                    condition.setRegType(regType);
                    log.info("设置查询类型：{}，设置查询号码：{}",regType,fileNo);
                }
            }
//            condition.setAccountKey(billsPublic.getAccountKey());
            condition.setOrgCode(billsPublic.getOrgCode());
            eccsMainService.eccsAccountChangeSync(pbcUserAccount, allAcct, condition);
        }
    }

    private PbcUserAccount validatePbcUser(PbcAccountDto pbcAccountDto) {
        if (pbcAccountDto == null) {
            //转换成人行登录对象
            throw new BizServiceException(EErrorCode.ORGAN_ECCS_USER_NOTCONFIG, "该机构未维护对应人行账户！");
        }
        if (StringUtils.isEmpty(pbcAccountDto.getAccount()) || StringUtils.isEmpty(pbcAccountDto.getPassword())) {
            throw new BizServiceException(EErrorCode.ORGAN_ECCS_USER_EMPTY, "人行账管用户名或密码为空！");
        }
        if (StringUtils.isEmpty(pbcAccountDto.getIp())) {
            throw new BizServiceException(EErrorCode.ORGAN_ECCS_USER_IP_EMPTY, "人行IP不可为空！");
        }
        return allBillsPublicService.systemPbcUser2PbcUser(pbcAccountDto);
    }

}
