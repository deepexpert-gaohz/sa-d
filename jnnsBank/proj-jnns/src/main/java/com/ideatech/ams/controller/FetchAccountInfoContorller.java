package com.ideatech.ams.controller;

import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.customer.dao.CustomerPublicLogDao;
import com.ideatech.ams.customer.entity.CustomerPublicLog;
import com.ideatech.ams.domain.AccountInfo;

import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.utils.CopyDirectoryUtil;
import com.ideatech.ams.utils.ZipUtil;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Long.parseLong;


/**
 * @version V1.0
 *
 */

@Slf4j
@RequestMapping("/jnnsImg")
@RestController
public class FetchAccountInfoContorller {

    static int flag = 1;//用来判断文件是否删除成功

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private CustomerPublicLogDao customerPublicLogDao;

    @Autowired
    private AllBillsPublicService allBillsPublicService;


    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Value(("${jnns.yiqitong.zip.path}"))
    private String zipPath;

    @Value(("${jnns.yiqitong.folder.path}"))
    private String folderPath;


    @Value(("${jnns.yiqitong.download.path}"))
    private String downloadPath;

    /**
     * 当用户点击下载按钮时触发
     * 通过流水号ID数组下载相对应流水表中的数据
     * 将选中的文件夹，移动到规定的文件夹下，并将每个文件夹对应的账户信息生成XML并合并，然后删除单个账户生成的XML
     * 将移动后位置中的文件夹以及合并后的XML文件压缩成ZIP压缩包
     * 通过流的方式将压缩包进行下载
     * @param res
     * @param ids
     */
    @GetMapping("/download")
    public void downloadZip(HttpServletResponse res, @RequestParam("id") String ids ) {

        //定义压缩包名称
        String fileName = "accounts.zip";
        //所需文件夹的父文件夹路径
        String parentPath = zipPath;
        //新生成的文件夹路径
        String newPath =folderPath ;
        //删除一个文件夹下的所有文件(包括子目录内的文件)
        File file = new File(newPath);//输入要删除文件目录的绝对路径
        deleteFile(file);
        if (flag == 1){
            System.out.println("文件删除成功！");
        }
        File file1=new File(newPath);
        if(!file1.exists()){//如果文件夹不存在
            file1.mkdir();//创建文件夹
            System.out.println("创建文件夹成功");
        }
        //获取每个id放进数组
        String[] idList = ids.split(",");
        List<AccountInfo> accountInfos = getAccountInfos(idList);
        ArrayList<String> newIds = CopyDirectoryUtil.moveDir(accountInfos, parentPath, newPath);
        if (!(null == newIds) && !(newIds.isEmpty())){
            ZipUtil.download(res,fileName,newPath);
            for (String id : newIds) {
                log.info("id====="+id);
                accountBillsAllService.updateDownstatus(id);

                log.info("解压缩包成功，文件架名称为"+id);
            }

        }else {
            log.error("下载失败");
        }
    }

    /**
     * 获取影像系统所需要的信息实体类集合
     * @param ids
     * @return
     */
    @NotNull
    private List<AccountInfo> getAccountInfos(String[] ids) {
        List<AccountInfo> accountInfos = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            AccountBillsAllInfo accBillsAll = accountBillsAllService.findById(parseLong(ids[i]));
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setImgDir("account" + ids[i]);
            if (accBillsAll.getAcctType().equals(CompanyAcctType.jiben)){
                accountInfo.setAccKind("1");
            } else if (accBillsAll.getAcctType().equals(CompanyAcctType.linshi)){
                accountInfo.setAccKind("7");
            }else if (accBillsAll.getAcctType().equals(CompanyAcctType.yusuan)) {
                accountInfo.setAccKind("4");
            }else if (accBillsAll.getAcctType().equals(CompanyAcctType.feilinshi)){
                accountInfo.setAccKind("6");
            }

            if (accBillsAll.getBillType().equals(BillType.ACCT_OPEN)){
                accountInfo.setBizKind("1");
            }else if (accBillsAll.getBillType().equals(BillType.ACCT_CHANGE)){
                accountInfo.setBizKind("2");
            }else if (accBillsAll.getBillType().equals(BillType.ACCT_REVOKE)){
                accountInfo.setBizKind("3");
            }
            accountInfo.setDepKind(accBillsAll.getDepositorType());
            accountInfo.setAccNo(accBillsAll.getAcctNo());

            accountInfo.setDepName(accBillsAll.getDepositorName());

//            CustomerPublicLog customerPublicLog = customerPublicLogDao.findById(accBillsAll.getCustomerLogId());
//            log.info("accBillsAll.getCustomerLogId()"+accBillsAll.getCustomerLogId());

            AllBillsPublicDTO allBillsPublicDTO = allBillsPublicService.findOne(accBillsAll.getId());
                accountInfo.setCreditCode(accBillsAll.getFileNo());
                log.info("证明文件1种类"+accBillsAll.getFileNo());
            accountInfo.setAccName(allBillsPublicDTO.getAcctName());
            String bankCode = organizationDao.findCodeByFullId(accBillsAll.getOrganFullId());
            log.info("bankCode====="+bankCode);
            accountInfo.setAccBankCode(bankCode);
            accountInfo.setAgent("");
            accountInfo.setAgentIdno("");
            accountInfo.setAgentTel("");
            accountInfos.add(accountInfo);
        }
        return accountInfos;
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