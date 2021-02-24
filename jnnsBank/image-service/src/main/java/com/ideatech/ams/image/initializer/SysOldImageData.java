package com.ideatech.ams.image.initializer;

import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.ams.account.service.AccountImageService;
import com.ideatech.ams.image.dao.ImageAllDao;
import com.ideatech.ams.image.dao.ImageDao;
import com.ideatech.ams.image.dao.ImageTypeDao;
import com.ideatech.ams.image.entity.Image;
import com.ideatech.ams.image.entity.ImageAll;
import com.ideatech.ams.image.entity.ImageType;
import com.ideatech.ams.image.enums.IsUpload;
import com.ideatech.common.constant.DataInitializerConstant;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.initializer.AbstractDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 处理老数据
 */
@Component
@Slf4j
public class SysOldImageData extends AbstractDataInitializer {
    //影像本地存放地址
    @Value("${ams.accounts.upload.path}")
    private String path;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private AccountImageService accountImageService;
    @Autowired
    private ImageAllDao imageAllDao;
    @Autowired
    private ImageTypeDao imageTypeDao;
    @Override
    protected void doInit() throws Exception {
        dealBill();
        dealAcctountImage();
    }
    private void dealAcctountImage(){
        log.info("迁移yd_accounts_image表数据");
        List<AccountImageInfo> list = accountImageService.findAll();
        long num = 0;
        for (AccountImageInfo image:list) {
            if(StringUtils.isBlank(image.getFilePath())){
                continue;
            }
            if(image.getAcctBillsId()==null){
                continue;
            }
            String[] pathArray = image.getFilePath().split(",");
            for (String string:pathArray) {
                try {
                    String filePath =  path+"/"+string;
                    List<ImageAll> imageAllList = imageAllDao.findByImgPath(filePath);
                    if(imageAllList.size()>0){
                        continue;
                    }
                    ImageAll all = new ImageAll();
                    //all.setId(image.getId());
                    all.setBillsId(image.getAcctBillsId());
                    all.setImgPath(filePath);
                    File file = new File(filePath);
                    String fileName = file.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    all.setFileFormat(suffix);
                    ImageType type = imageTypeDao.findOne(image.getImageTypeId());
                    all.setDocCode(type.getValue());
                    all.setSyncStatus(image.getSyncStatus());

                    if(StringUtils.isNotBlank(image.getBatchNo())){
                        all.setBatchNumber(image.getBatchNo());
                    }
                    if(StringUtils.isNotBlank(image.getFileName())){
                        all.setFileNmeCN(image.getFileName());
                    }
                    if(StringUtils.isNotBlank(image.getFileNo())){
                        all.setFileNo(image.getFileNo());
                    }
                    if(StringUtils.isNotBlank(image.getMaturityDate())){
                        all.setExpireDate(image.getMaturityDate());
                    }
                    if(image.getAcctId()!=null){
                        all.setAcctId(image.getAcctId());
                    }
                    imageAllDao.save(all);
                }catch (Exception e){
                    log.error("yd_accounts_image数据异常："+e.getMessage());
                    continue;
                }
            }
            num++;
        }
        log.info("yd_accounts_image数据共："+list.size()+"条,迁移有效数据："+num+"条");
    }
    private void dealBill(){
        log.info("迁移yd_image表数据");
        List<Image> list = imageDao.findAll();
        long num = 0;
        for (Image image:list) {
            try {
                ImageAll all =imageAllDao.findOne(image.getId());
                if (all!=null){
                    continue;
                }
                all = new ImageAll();
                all.setBillsId(image.getRefBillId());
                all.setImgPath(image.getImgPath());
                all.setId(image.getId());
                all.setDocCode(String.valueOf(image.getDocCode()));
                if(StringUtils.isNotBlank(image.getBatchNumber())){
                    all.setBatchNumber(image.getBatchNumber());
                }
                if(StringUtils.isNotBlank(image.getNumber())){
                    all.setFileNo(image.getNumber());
                }
                if(StringUtils.isNotBlank(image.getExpireDateStr())){
                    all.setExpireDate(image.getExpireDateStr());
                }
                if(image.getIsUpload()==IsUpload.TRUE){
                    all.setSyncStatus(CompanyIfType.Yes);
                }else{
                    all.setSyncStatus(CompanyIfType.No);
                }
                if(StringUtils.isNotBlank(image.getFileName())){
                    all.setFileName(image.getFileName());
                }
                if(StringUtils.isNotBlank(image.getFileNmeCN())){
                    all.setFileNmeCN(image.getFileNmeCN());
                }
                if(image.getAcctId()!=null){
                    all.setAcctId(image.getAcctId());
                }
                all.setFileFormat("jpg");
                imageAllDao.save(all);
                num++;
            }catch (Exception e){
                log.error("yd_image数据异常："+e.getMessage());
                continue;
            }
        }
        log.info("yd_image数据共："+list.size()+"条,迁移有效数据："+num+"条");
    }
    @Override
    protected boolean isNeedInit() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return DataInitializerConstant.OPTION_INITIALIZER_INDEX;
    }
}
