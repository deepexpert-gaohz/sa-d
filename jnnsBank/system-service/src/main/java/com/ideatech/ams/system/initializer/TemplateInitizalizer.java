package com.ideatech.ams.system.initializer;

import com.ideatech.ams.system.template.dao.TemplateDao;
import com.ideatech.ams.system.template.entity.TemplatePo;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class TemplateInitizalizer extends AbstractDataInitializer {

    @Autowired
    private TemplateDao templateDao;

    @Value("${ams.pdf.cancelBasicData}")
    private String cancelBasicData;
    @Value("${ams.pdf.CancelpassWord}")
    private String CancelpassWord;
    @Override
    protected void doInit() throws Exception {
//        Base64Utils.decoderBase64File(cancelBasicData,filePath+"/cancelBasicData.pdf");
//        Base64Utils.decoderBase64File(CancelpassWord,filePath+"/CancelpassWord.pdf");
        TemplatePo templatePo = new TemplatePo();

        templatePo.setTemplateName("取消核准基本户信息-正面");
//        templatePo.setTemplaeContent(IOUtils.toByteArray(new FileInputStream(new File(filePath+"/cancelBasicData.pdf"))));
        templatePo.setTemplaeContent(Base64Utils.decodeFromString(cancelBasicData));
        templatePo.setBillType(BillType.ACCT_OPEN);
        templatePo.setDepositorType(DepositorType.ALL);
        templateDao.save(templatePo);

        TemplatePo templatePo1 = new TemplatePo();
        templatePo1.setTemplateName("取消核准基本户交易密码-反面");
        templatePo1.setTemplaeContent(Base64Utils.decodeFromString(CancelpassWord));
//        templatePo1.setTemplaeContent(IOUtils.toByteArray(new FileInputStream(new File(filePath+"/CancelpassWord.pdf"))));
        templatePo1.setBillType(BillType.ACCT_OPEN);
        templatePo1.setDepositorType(DepositorType.ALL);
        templateDao.save(templatePo1);
    }

    @Override
    protected boolean isNeedInit() {
        return templateDao.count() < 1;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }
}
