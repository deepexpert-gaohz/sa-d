package com.ideatech.ams.ws.listener;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.event.PushCoreDataEvent;
import com.ideatech.ams.account.service.PbcSyncListService;
import com.ideatech.ams.ws.api.service.AmsHzPushService;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@Transactional
public class PushCoreDataListener implements ApplicationListener<PushCoreDataEvent> {

    @Value("${ams.schedule.pushCancelHeZhunCore.file:/home/weblogic/idea/apprott/pushCancelHeZhunCoreFilePath}")
    private String pushCoreFilePath;

    @Autowired
    private AmsHzPushService amsHzPushService;

    @Autowired
    private PbcSyncListService pbcSyncListService;

    @Override
    public void onApplicationEvent(PushCoreDataEvent pushCoreDataEvent) {

        log.info("取消核准核心推送接口开始！");
        AllBillsPublicDTO allBillsPublicDTO = pushCoreDataEvent.getAllBillsPublicDTO();

        //拿到一个批次人行已核准的账户数据(批量)
        List<AllBillsPublicDTO> writeTxtList = new ArrayList<>(16);

        boolean needPush = true;

        if (allBillsPublicDTO.getBillType() == BillType.ACCT_CHANGE) {
            //如果是变更， 核准号修改后才推送
            if (allBillsPublicDTO.getAcctType() == CompanyAcctType.jiben) {
                //核准号无变更字段为空
                if (StringUtils.isBlank(allBillsPublicDTO.getAccountKey())) {
                    needPush = false;
                }
            }else {
                if (StringUtils.isBlank(allBillsPublicDTO.getAccountLicenseNo())) {
                    needPush = false;
                }
            }
        }
        //推送核心
        try {
            if (allBillsPublicDTO != null) {
                //执行推送方法
                if (needPush) {
                    log.info("调用核心推送接口");
                    writeTxtList.add(allBillsPublicDTO);
                    String accountKey = allBillsPublicDTO.getAcctType() == CompanyAcctType.jiben ? allBillsPublicDTO.getAccountKey() : allBillsPublicDTO.getAccountLicenseNo();
                    Object obj = amsHzPushService.push(allBillsPublicDTO.getAcctNo(), accountKey);
                    Object objAll = amsHzPushService.pushAll(DateFormatUtils.ISO_DATE_FORMAT.format(System.currentTimeMillis()), JSON.toJSONString(allBillsPublicDTO));
                    log.info("推送完成");
                }
            }
        } catch (Exception e) {
            log.info("账号："+allBillsPublicDTO.getAcctNo()+",账户性质："+allBillsPublicDTO.getAcctType()+"，核心推送异常，数据保存pbcSyncList{}" + e.getMessage());
            pbcSyncListService.saveCancelHeZhunPbcSyncList(allBillsPublicDTO);
        }


        //获取list集合  存放txt文件到目录下 实现类去读取文件解析
        if (CollectionUtils.isNotEmpty(writeTxtList)) {
            log.info("人行本次核准数量为:" + writeTxtList.size() + "笔...");
            File file = new File(pushCoreFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File batchFile = new File(pushCoreFilePath + File.separator + System.currentTimeMillis() + ".txt");
            try {
                log.info("人行本次核推送文件名称:" + file.getName());
                if (!batchFile.exists()) {
                    batchFile.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile, true));
                for (AllBillsPublicDTO allBillsPublicDTO1 : writeTxtList) {
                    String writeString = "";
                    String accountKey = allBillsPublicDTO1.getAcctType() == CompanyAcctType.jiben ? allBillsPublicDTO1.getAccountKey() : allBillsPublicDTO1.getAccountLicenseNo();
                    writeString = allBillsPublicDTO1.getAcctNo() + "," + accountKey + "," + allBillsPublicDTO1.getAcctCreateDate();
                    //临时非临时户推送临时户的到期日
                    if (allBillsPublicDTO1.getAcctType() == CompanyAcctType.feilinshi || allBillsPublicDTO1.getAcctType() == CompanyAcctType.linshi) {
                        writeString += "," + allBillsPublicDTO1.getEffectiveDate();
                    }
                    writer.write(writeString);
                    writer.newLine();
                    writer.flush();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //推送
            log.info("人行核准文件路径 :" + batchFile.getAbsolutePath());
            try {
                Object obj = amsHzPushService.pushCoreFile(batchFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("推送文件路径异常", e);
                for (AllBillsPublicDTO allBillsPublicDTO1: writeTxtList) {
                    log.info(allBillsPublicDTO1.getAcctNo() + "人行核准文件推送失败");
                }
            }
        }
    }
}
