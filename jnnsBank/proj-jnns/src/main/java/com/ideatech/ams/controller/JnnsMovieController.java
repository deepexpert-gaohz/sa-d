package com.ideatech.ams.controller;

import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.dao.AccountsBillsAllDao;
import com.ideatech.ams.dao.JnnsImageAllDao;
import com.ideatech.ams.domain.JnnsImageAll;
import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;


@RequestMapping("/jnnsMovie")
@RestController
public class JnnsMovieController {
    protected Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private JnnsImageService testMovieService;
    @Autowired
    private JnnsImageAllDao jnnsImageAllDao;
    @Autowired
    private AccountsBillsAllDao accountsBillsAllDao;


    /**
     * 根据产品业务流水Id从影像文件表查询出影像文件url
     *
     * @param id 产品业务流水
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getImageByCondition")
    public ResultDto querys(String id) throws Exception {
        log.info("--------------------影像查询信息--------------------");
        String query;
        StringBuffer strBurrer = new StringBuffer();
        AccountBillsAll accountBillsAll = accountsBillsAllDao.findById(Long.parseLong(id));
        log.info("---------------accountBillsAll-------------------" + accountBillsAll.toString());
        if (accountBillsAll != null ) {
            List<JnnsImageAll> jnnsImageAllList = jnnsImageAllDao.findByBillId(id);
            log.info("jnnsImageAllList======================" + jnnsImageAllList);
            for (JnnsImageAll jnnsImageAll : jnnsImageAllList) {
                if (jnnsImageAll != null && StringUtils.isNotEmpty(jnnsImageAll.getImageCode())) {
                    log.info("--jnnsImageAll对象-:" + jnnsImageAll.toString());
                    log.info("BusiStartDate=====================" + jnnsImageAll.getBusiStartDate());
                    query = testMovieService.query(jnnsImageAll.getImageCode(), jnnsImageAll.getBusiStartDate());
                    if (StringUtils.isEmpty(query)){
                        query = testMovieService.query1(jnnsImageAll.getImageCode(), jnnsImageAll.getBusiStartDate());
                    }
                    log.info("根据影像批次号查询的影像文件：" + query);
                    strBurrer.append(query).append(",");
                }
            }
        }
        String result = strBurrer.toString();
        if (StringUtils.isNotBlank(result)) {
            result = StringUtils.substringBeforeLast(result, ",");
        }
        return ResultDtoFactory.toAckData(testMovieService.formatImageJson(result));
    }

    /**
     * 影像导入保存到本地后并上传到影像平台
     * 上传成功后保存到影像文件表
     *
     * @param files       影像文件
     * @param acctBillsId
     */
    @PostMapping(value = "/uploadImage")
    public void uploadImage(@RequestParam("file") MultipartFile[] files, Long acctBillsId) {
        List<String> list = new ArrayList();
        try {
            for (MultipartFile multipartFile : files) {
                String filename = multipartFile.getOriginalFilename();
                String path = this.testMovieService.uploadImage(multipartFile.getInputStream(), filename, Long.toString(acctBillsId));
                list.add(path);
            }
            //上传影像平台
            String s = testMovieService.uploadDsFile(list);
            AccountBillsAll accountBillsAll = accountsBillsAllDao.findById(acctBillsId);
            JnnsImageAll jnnsImageAll = new JnnsImageAll();
            //保存影像信息
            if (accountBillsAll != null && StringUtils.isNotEmpty(s)) {
                jnnsImageAll.setId(null);
                jnnsImageAll.setImageCode(s);
                jnnsImageAll.setAcctType(accountBillsAll.getAcctType());
                jnnsImageAll.setAcctName(accountBillsAll.getDepositorName());
                jnnsImageAll.setAcctNo(accountBillsAll.getAcctNo());
                jnnsImageAll.setBillType(accountBillsAll.getBillType());
                jnnsImageAll.setCustomerNo(accountBillsAll.getCustomerNo());
                jnnsImageAll.setBusiStartDate(getNowDateShort("yyyyMMdd"));
                jnnsImageAll.setBillId(Long.toString(accountBillsAll.getId()));
                jnnsImageAll.setOrganFullId(accountBillsAll.getOrganFullId());
                jnnsImageAll.setShifoubz("0");
                jnnsImageAllDao.save(jnnsImageAll);
            }
        } catch (Exception e) {
            log.info("影像上传异常", e);
        }
    }


    /**
     * 根据产品业务流水号删除所有对应的影像批次号
     *
     * @param acctBillsId 业务流水ID
     * @return
     */
    @PostMapping(value = "/deleteImage")
    public ResultDto deleteImage(@RequestParam String acctBillsId) {
        testMovieService.deleteByBillId(acctBillsId);
        return ResultDtoFactory.toAck();
    }


    public static void main(String[] args) {
        String s="12345678900";

        String a=s.substring(3, s.length());

        System.out.println(a);
    }
}
