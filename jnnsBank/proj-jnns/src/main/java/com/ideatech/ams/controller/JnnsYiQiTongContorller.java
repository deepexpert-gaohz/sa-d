package com.ideatech.ams.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.customer.dao.CustomerPublicLogDao;
import com.ideatech.ams.dao.AccountsBillsAllDao;
import com.ideatech.ams.dto.gmsp.GMSP;
import com.ideatech.ams.dto.jnnsYiQiTong.AccountBillsAllResponse;
import com.ideatech.ams.dto.jnnsYiQiTong.OrganCode;
import com.ideatech.ams.dto.jnnsYiQiTong.YiQiTong;

import com.ideatech.ams.service.JnnsImageService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.utils.ZipDownLoadUtils;
import com.ideatech.ams.utils.ZipUtil;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ideatech.ams.utils.DateUtils.getNowDateShort;

@RequestMapping("/jnnsYiQiTongMovie")
@RestController
public class JnnsYiQiTongContorller {
    @Autowired
    private JnnsImageService jnnsImageService;


    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private OrganizationDao organizationDao;




    @Autowired
    private CustomerPublicLogDao customerPublicLogDao;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Value(("${jnns.yiqitong.download.path}"))
    private String downloadPath;

    @Value(("${jnns.yiqitong.zip.path}"))
    private String zipPath;


    protected Logger log = LoggerFactory.getLogger(getClass());


    static int flag = 1;//用来判断文件是否删除成功


    @PostMapping("/postImage")
    public ResultDto getPostImage(@RequestBody YiQiTong billsPublics, HttpServletResponse response) throws Exception {
        ResultDto resultDto=new ResultDto();
        try {


            accountBillsAllService.updateUploadstatus(billsPublics.getId());
            String urlResult = jnnsImageService.queryYiQiTong(billsPublics.getImageBillno());

        if (urlResult!=null){
            resultDto.setCode("1");
            resultDto.setMessage("影像查询成功");
        }else {
            resultDto.setCode("999999");
            resultDto.setMessage("影像查询失败");
        }
            File file = new File(zipPath+"/account"+billsPublics.getId());//输入要删除文件目录的绝对路径
            deleteFile(file);
            if (flag == 1){
                System.out.println("文件删除成功！");
            }

            ZipDownLoadUtils.downLoadFromUrl(urlResult, billsPublics.getId()+".zip", downloadPath);
            ZipUtil.decompressZip((downloadPath+"/"+billsPublics.getId()+".zip"),zipPath+"/");
        } catch (Exception e) {
            log.error("下载失败" + e.getMessage());
            resultDto.setCode("400");
            resultDto.setMessage(e.getMessage());
        }
        return resultDto;
    }



    @PostMapping("/postAccountBillsAll")
    public  ResultDto getAccountBillsAll(@RequestBody OrganCode organcode, HttpServletResponse response) {
        ResultDto resultDto=new ResultDto();

        log.info("行内机构号"+organcode);
        String fullId = organizationDao.findFullIdByCode(organcode.getOrganCode());

        log.info("orgfullid======"+fullId);
        String docDate = getNowDateShort("yyyy-MM-dd");
        log.info("查询时间=="+docDate);
        List<AccountBillsAllInfo> accountBillsInfoList = accountBillsAllService.findByOrganFullIdAndBillId(fullId,docDate);
        log.info("查询json"+accountBillsInfoList);
        List <AccountBillsAllResponse> accountBillsAllResponseslist =new ArrayList<>();
        for (AccountBillsAllInfo accountBillsAllInfo : accountBillsInfoList) {
            AccountBillsAllResponse accountBillsAllResponse=new AccountBillsAllResponse();
            accountBillsAllResponse.setId(accountBillsAllInfo.getId());
            accountBillsAllResponse.setAcctNo(accountBillsAllInfo.getAcctNo());
            accountBillsAllResponse.setAcctType(accountBillsAllInfo.getAcctType());
            accountBillsAllResponse.setBillDate(accountBillsAllInfo.getBillDate());
            accountBillsAllResponse.setCustomerNo(accountBillsAllInfo.getCustomerNo());
            accountBillsAllResponse.setBillNo(accountBillsAllInfo.getBillNo());
            accountBillsAllResponse.setDepositorName(accountBillsAllInfo.getDepositorName());
            accountBillsAllResponse.setDepositorType(accountBillsAllInfo.getDepositorType());
            accountBillsAllResponse.setStatus(accountBillsAllInfo.getStatus());
            accountBillsAllResponse.setPbcSyncStatus(accountBillsAllInfo.getPbcSyncStatus());
            accountBillsAllResponse.setPbcCheckStatus(accountBillsAllInfo.getPbcCheckStatus());
            accountBillsAllResponse.setOrganFullId(accountBillsAllInfo.getOrganFullId());
            if (accountBillsAllInfo.getBillType().equals(BillType.ACCT_OPEN)){
                accountBillsAllResponse.setBillType("开户");
            }else if (accountBillsAllInfo.getBillType().equals(BillType.ACCT_CHANGE)){
                accountBillsAllResponse.setBillType("变更");
            }else if (accountBillsAllInfo.getBillType().equals(BillType.ACCT_REVOKE)){
                accountBillsAllResponse.setBillType("销户");
            }

            accountBillsAllResponseslist.add(accountBillsAllResponse);
        }
        log.info("生成的json"+accountBillsAllResponseslist.toString());
        if (accountBillsInfoList.size()==0){
          resultDto.setCode("999999");
          resultDto.setMessage("查询无数据");
          resultDto.setData(null);
        }else {
            resultDto.setCode("1");
            resultDto.setMessage("查询成功");
            resultDto.setData(accountBillsAllResponseslist);
        }
        return resultDto;
    }

    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            flag = 0;
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

}

