package com.ideatech.ams.compare.initializer;

import com.ideatech.ams.compare.dao.CompareFieldDao;
import com.ideatech.ams.compare.entity.CompareField;
import com.ideatech.ams.compare.enums.CompareFieldEnum;
import com.ideatech.ams.system.user.dao.UserDao;
import com.ideatech.ams.system.user.entity.UserPo;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CompareFieldsInitializer extends AbstractDataInitializer {
    @Autowired
    private CompareFieldDao compareFieldDao;
    @Autowired
    private UserDao userDao;
    @Override
    protected void doInit() throws Exception {
        create(1001L, CompareFieldEnum.ACCT_NO.getValue(),CompareFieldEnum.ACCT_NO.getField());
        create(1002L, CompareFieldEnum.DEPOSITOR_NAME.getValue(),CompareFieldEnum.DEPOSITOR_NAME.getField());
        create(1003L, CompareFieldEnum.ORG_CODE.getValue(),CompareFieldEnum.ORG_CODE.getField());
        create(1004L, CompareFieldEnum.ORGAN_CODE.getValue(),CompareFieldEnum.ORGAN_CODE.getField());
        create(1005L, CompareFieldEnum.LEGAL_NAME.getValue(),CompareFieldEnum.LEGAL_NAME.getField());
        create(1006L, CompareFieldEnum.REG_NO.getValue(),CompareFieldEnum.REG_NO.getField());
        create(1007L, CompareFieldEnum.BUSINESS_SCOPE.getValue(),CompareFieldEnum.BUSINESS_SCOPE.getField());
        create(1008L, CompareFieldEnum.REG_ADDRESS.getValue(),CompareFieldEnum.REG_ADDRESS.getField());
        create(1009L, CompareFieldEnum.REG_CAPITAL.getValue(),CompareFieldEnum.REG_CAPITAL.getField());
        create(1010L, CompareFieldEnum.ACCOUNT_STATUS.getValue(),CompareFieldEnum.ACCOUNT_STATUS.getField());
        create(1011L, CompareFieldEnum.STATE_TAXREG_NO.getValue(),CompareFieldEnum.STATE_TAXREG_NO.getField());
        create(1012L, CompareFieldEnum.TAXREG_NO.getValue(),CompareFieldEnum.TAXREG_NO.getField());
        create(1013L, CompareFieldEnum.BANK_NAME.getValue(),CompareFieldEnum.BANK_NAME.getField());
        create(1014L, CompareFieldEnum.ACCT_TYPE.getValue(),CompareFieldEnum.ACCT_TYPE.getField());
        create(1015L, CompareFieldEnum.ACCT_CREATE_DATE.getValue(),CompareFieldEnum.ACCT_CREATE_DATE.getField());
        create(1016L, CompareFieldEnum.ACCOUNT_KEY.getValue(),CompareFieldEnum.ACCOUNT_KEY.getField());
        create(1017L, CompareFieldEnum.FILE_TYPE.getValue(),CompareFieldEnum.FILE_TYPE.getField());
        create(1018L, CompareFieldEnum.FILE_NO.getValue(),CompareFieldEnum.FILE_NO.getField());
        create(1019L, CompareFieldEnum.TELEPHONE.getValue(),CompareFieldEnum.TELEPHONE.getField());
        //create(1020L, CompareFieldEnum.WORK_ADDRESS.getValue(),CompareFieldEnum.WORK_ADDRESS.getField());
        create(1021L, CompareFieldEnum.ZIP_CODE.getValue(),CompareFieldEnum.ZIP_CODE.getField());
        create(1022L, CompareFieldEnum.DEPOSITOR_TYPE.getValue(),CompareFieldEnum.DEPOSITOR_TYPE.getField());
        create(1023L, CompareFieldEnum.LEGAL_IDCARD_TYPE.getValue(),CompareFieldEnum.LEGAL_IDCARD_TYPE.getField());
        create(1024L, CompareFieldEnum.LEGAL_IDCARD_NO.getValue(),CompareFieldEnum.LEGAL_IDCARD_NO.getField());
        create(1025L, CompareFieldEnum.INDUSTRY_CODE.getValue(),CompareFieldEnum.INDUSTRY_CODE.getField());
        create(1026L, CompareFieldEnum.ECONOMY_INDUSTRY.getValue(),CompareFieldEnum.ECONOMY_INDUSTRY.getField());
        create(1027L, CompareFieldEnum.REG_AREA_CODE.getValue(),CompareFieldEnum.REG_AREA_CODE.getField());
        create(1028L, CompareFieldEnum.REG_OFFICE.getValue(),CompareFieldEnum.REG_OFFICE.getField());
        create(1029L, CompareFieldEnum.CANCEL_DATE.getValue(),CompareFieldEnum.CANCEL_DATE.getField());
        create(1030L,CompareFieldEnum.OPENDATE.getValue(),CompareFieldEnum.OPENDATE.getField());
        create(1031L,CompareFieldEnum.ENDDATE.getValue(),CompareFieldEnum.ENDDATE.getField());
    }

    private CompareField create(Long id,String name,String field){
        UserPo user = userDao.findByUsername("admin");
        CompareField fieldOjb = new CompareField();
        fieldOjb.setId(id);
        fieldOjb.setField(field);
        fieldOjb.setName(name);
        fieldOjb.setCreatedBy(String.valueOf(user.getId()));
        fieldOjb.setCreatedDate(new Date());
        compareFieldDao.save(fieldOjb);
        return fieldOjb;
    }
    @Override
    protected boolean isNeedInit() {
        return compareFieldDao.findAll().size()==0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
