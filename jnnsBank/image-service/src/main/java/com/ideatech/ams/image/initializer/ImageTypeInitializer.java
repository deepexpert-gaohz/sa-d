package com.ideatech.ams.image.initializer;

import com.ideatech.ams.image.dao.ImageTypeDao;
import com.ideatech.ams.image.entity.ImageType;
import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageTypeInitializer extends AbstractDataInitializer {
    @Autowired
    private ImageTypeDao imageTypeDao;
    @Override
    protected void doInit() throws Exception {
        createImageType(1001001L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"01-企业法人","01","开户申请书","1","1");
        createImageType(1001002L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"02-非法人企业","02","开户申请书","1","1");
        createImageType(1001003L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"13-有字号的个体工商户","13","开户申请书","1","1");
        createImageType(1001004L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"14-无字号的个体工商户","14","开户申请书","1","1");
        createImageType(1001005L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"03-机关","03","开户申请书","1","1");
        createImageType(1001006L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"04-实行预算管理的事业单位","04","开户申请书","1","1");

        createImageType(1001011L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"01-企业法人","01","营业执照正本","2","1");
        createImageType(1001012L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"02-非法人企业","02","营业执照正本","2","1");
        createImageType(1001013L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"13-有字号的个体工商户","13","营业执照正本","2","1");
        createImageType(1001014L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"14-无字号的个体工商户","14","营业执照正本","2","1");
        createImageType(1001015L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"03-机关","03","营业执照正本","2","1");
        createImageType(1001016L,CompanyAcctType.jiben,BillType.ACCT_OPEN,"04-实行预算管理的事业单位","04","营业执照正本","2","1");

        createImageType(1001021L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"01-企业法人","01","开户申请书","1","1");
        createImageType(1001022L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"02-非法人企业","02","开户申请书","1","1");
        createImageType(1001023L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"13-有字号的个体工商户","13","开户申请书","1","1");
        createImageType(1001024L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"14-无字号的个体工商户","14","开户申请书","1","1");
        createImageType(1001025L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"03-机关","03","开户申请书","1","1");
        createImageType(1001026L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"04-实行预算管理的事业单位","04","开户申请书","1","1");

        createImageType(1001031L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"01-企业法人","01","营业执照正本","2","1");
        createImageType(1001032L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"02-非法人企业","02","营业执照正本","2","1");
        createImageType(1001033L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"13-有字号的个体工商户","13","营业执照正本","2","1");
        createImageType(1001034L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"14-无字号的个体工商户","14","营业执照正本","2","1");
        createImageType(1001035L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"03-机关","03","营业执照正本","2","1");
        createImageType(1001036L,CompanyAcctType.yiban,BillType.ACCT_OPEN,"04-实行预算管理的事业单位","04","营业执照正本","2","1");

    }
    private void createImageType(Long id,CompanyAcctType acctType,BillType operateType,String depositorType ,String depositorTypeCode,String imageName,String value,String choose){
        ImageType imageType = new ImageType();
        imageType.setId(id);
        imageType.setAcctType(acctType);
        imageType.setOperateType(operateType);
        imageType.setDepositorType(depositorType);
        imageType.setDepositorTypeCode(depositorTypeCode);
        imageType.setImageName(imageName);
        imageType.setValue(value);
        imageType.setChoose(choose);
        imageTypeDao.save(imageType);
    }
    @Override
    protected boolean isNeedInit() {
        return imageTypeDao.findAll().size() == 0;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
