package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.bo.common.InstructedParty1;
import com.ideatech.ams.mivs.bo.common.InstructingParty2;
import com.ideatech.ams.mivs.bo.common.MessageHeader3;
import com.ideatech.ams.mivs.bo.mam.GetIdentityVerificationV1;
import com.ideatech.ams.mivs.bo.mam.MobilePhoneNumberApplyFactory;
import com.ideatech.ams.mivs.bo.mam.MobilePhoneNumberApplyMsg;
import com.ideatech.ams.mivs.bo.mam.VerificationDefinition1;
import com.ideatech.ams.mivs.dto.mad.MobilePhoneNumberApplyDto;
import com.ideatech.ams.mivs.util.XMLUtil;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 * @author jzh
 * @date 2019/7/17.
 */

@Service
public class MobilePhoneNumberServiceImpl implements MobilePhoneNumberService {

    @Override
    public String packMobilePhoneNumberApply(MobilePhoneNumberApplyDto mobilePhoneNumberDto) {

        MobilePhoneNumberApplyFactory objectFactory = new MobilePhoneNumberApplyFactory();
        //根节点
        MobilePhoneNumberApplyMsg document = objectFactory.createDocument();

        GetIdentityVerificationV1 getIdentityVerificationV1 = objectFactory.createGetIdentityVerificationV1();

        MessageHeader3 messageHeader3 = objectFactory.createMessageHeader3();
        messageHeader3.setMsgId("2019021413200197");
        try {
            messageHeader3.setCreDtTm(DatatypeFactory.newInstance().newXMLGregorianCalendar("2019-02-14T14:08:07"));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        InstructingParty2 instructingParty2 = objectFactory.createInstructingParty2();
        instructingParty2.setInstgDrctPty("313871000007");
        instructingParty2.setDrctPtyNm("宁夏银行");
        instructingParty2.setInstgPty("313871000007");
        instructingParty2.setPtyNm("宁夏银行");
        messageHeader3.setInstgPty(instructingParty2);
        InstructedParty1 instructedParty1 = objectFactory.createInstructedParty1();
        instructedParty1.setInstdDrctPty("0000");
        instructedParty1.setInstdPty("0000");
        messageHeader3.setInstdPty(instructedParty1);
        getIdentityVerificationV1.setMsgHdr(messageHeader3);

        VerificationDefinition1 verificationDefinition1 = objectFactory.createVerificationDefinition1();
        BeanCopierUtils.copyProperties(mobilePhoneNumberDto,verificationDefinition1);
        getIdentityVerificationV1.setVryDef(verificationDefinition1);

        document.setGetIdVrfctn(getIdentityVerificationV1);

        String xml = XMLUtil.toXML(document,objectFactory);
        System.out.println(xml);

        //TODO 报文体xml的检验

        //TODO MsgHeader（报文头）+ 数字签名域 +xml(报文体)

        return xml;
    }
}
