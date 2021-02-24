package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.system.template.dto.TemplateDto;
import com.ideatech.ams.system.template.service.TemplateService;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houmingwan on 2019/1/23.
 */
@Service
@Transactional
@Slf4j
public class PrintingApiServiceImpl implements PrintingApiService {

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private TemplateService templateService;

    @Value("${printfilepath.file.location}")
    private String printfilepath;

    @Override
    public ObjectRestResponse<AllBillsPublicDTO> getCompletePDF(Long billId, BillType billType, DepositorType depositorType) throws IOException {
        TemplateDto templateDto = templateService.findByBillTypeAndDepositorType(billType, depositorType);
        if(templateDto == null) {
            return new ObjectRestResponse<AllBillsPublicDTO>().rel(false).result("").msg("根据单据类型和存款人类别未找到对应打印模版");
        }

        AllBillsPublicDTO info = allBillsPublicService.findByBillId(billId);

        if (info != null) {
            /*
             * if ("ACCT_SUSPEND".equals(billType) || "ACCT_REVOKE".equals(billType) ||
             * "ACCT_CHANGE".equals(billType)) { info.setId(null); }
             */
            if (info.getOriginalBillId() == null) {
                info.setOriginalBillId(billId);
            }
            //处理创建时间后面的数字
            try {
                String createdDate = info.getCreatedDate();
                if (StringUtils.isNotBlank(createdDate) && StringUtils.length(createdDate) == 21) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Date date = sdf.parse(info.getCreatedDate());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    info.setCreatedDate(format.format(date));
                }
            } catch (Exception e) {

                e.printStackTrace();
            }


            setAcctCreateDate(info);
//            if ("ACCT_REVOKE".equals(billType)) { // 销户详情页字典转化
//                allBillsPublicService.conversion(info);
//            }

        }

        AllBillsPublicDTO allBillsPublicDTO = info;

        Map<String, String> describe = new HashMap<>();
        Map<String, Object> describe2 = new HashMap<>();
        for (Field f : allBillsPublicDTO.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.getType() == String.class && f.get(allBillsPublicDTO) == null) { //判断字段是否为空，并且对象属性中的基本都会转为对象类型来判断
                    f.set(allBillsPublicDTO, "");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            describe = BeanUtils.describe(allBillsPublicDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        describe2.putAll(describe);


        String filename=printfilepath+"/"+billId+".pdf";

        BufferedOutputStream stream = null;
        File file = null;
        try
        {
            file = new File(filename);
            final FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(PdfGenerator.generate(templateDto.getTemplaeContent(), describe2));//调试到这里文件已经生成
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return new ObjectRestResponse<AllBillsPublicDTO>().rel(true).result(filename).msg("打印模版生成成功");
    }

    private void setAcctCreateDate(AllBillsPublicDTO info) {
        if (StringUtils.isNotBlank(info.getAcctCreateDate())) {
            Date date = null;
            try {
                date = DateUtils.parse(info.getAcctCreateDate(), "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            info.setAcctCreateDate(DateUtils.DateToStr(date, "yyyy-MM-dd"));
        }

    }

}
