package com.ideatech.ams.image.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountPublicService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.image.dao.ImageTypeDao;
import com.ideatech.ams.image.dao.spec.ImageTypeSpec;
import com.ideatech.ams.image.dto.ImageTypeInfo;
import com.ideatech.ams.image.entity.ImageType;
import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import com.ideatech.ams.system.dict.dao.DictionaryDao;
import com.ideatech.ams.system.dict.dao.OptionDao;
import com.ideatech.ams.system.dict.entity.DictionaryPo;
import com.ideatech.ams.system.dict.entity.OptionPo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.BeanCopierUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("imageTypeService")
@Transactional
public class ImageTypeServiceImpl implements ImageTypeService {

    @Autowired
    private ImageTypeDao imageTypeDao;
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private DictionaryDao dictionaryDao;
    @Autowired
    private AccountBillsAllService accountBillsAllService;
    @Autowired
    private AccountPublicService accountPublicService;
    @Autowired
    private AccountPublicLogService accountPublicLogService;
    @Override
    public ImageTypeInfo save(ImageTypeInfo info) {
        ImageType imageType = new ImageType();
        if(info.getId()==null){
            List<ImageType> types = imageTypeDao.findByValueAndAcctTypeAndOperateTypeAndDepositorTypeCode(info.getValue(),
                    info.getAcctType(), info.getOperateType(),info.getDepositorTypeCode());
            if(types.size()>0){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"记录已存在，请确认后在提交！");
            }
        }
        if(info.getId()!=null){
            imageType = imageTypeDao.findOne(info.getId());
            if(imageType==null){
                imageType = new ImageType();
            }
        }
        ConverterService.convert(info, imageType);

        //1、当存款人非空时
        if(StringUtils.isNotBlank(info.getDepositorTypeCode())&&(!info.getDepositorTypeCode().equals("-1"))){
            DictionaryPo dictionaryPo = dictionaryDao.findByName("存款人类别(基本户)");
            OptionPo optionPo = optionDao.findByDictionaryIdAndValue(dictionaryPo.getId(), info.getDepositorTypeCode());
            if(optionPo!=null){
                imageType.setDepositorType(optionPo.getName());
            }else{
                dictionaryPo = dictionaryDao.findByName("存款人类别(特殊户)");
                optionPo = optionDao.findByDictionaryIdAndValue(dictionaryPo.getId(), info.getDepositorTypeCode());
                if(optionPo!=null){
                    imageType.setDepositorType(optionPo.getName());
                }else {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没找到对应字典值！");
                }
            }

        //2、当存款人为空时，AcctType为基本户，OperateType为开户操作
        }else if(info.getAcctType() == CompanyAcctType.jiben && info.getOperateType() == BillType.ACCT_OPEN){
            //2、不通过
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"存款人性质为空！");
        //3、当存款人为空时，AcctType为特殊户，OperateType为开户操作
        }else if(info.getAcctType() == CompanyAcctType.jiben && info.getOperateType() == BillType.ACCT_OPEN){
            //3、不通过
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"存款人性质为空！");
        }//4、当存款人为空时，通过。


        if(StringUtils.isNotBlank(info.getValue())&&(!info.getValue().equals("-1"))){
            DictionaryPo dictionaryPo = dictionaryDao.findByName("imageOption");
            if (dictionaryPo != null) {
                OptionPo optionPo = optionDao.findByDictionaryIdAndValue(dictionaryPo.getId(), info.getValue());
                imageType.setImageName(optionPo.getName());
            }
        }else{
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"证件种类为空！");
        }

        imageTypeDao.save(imageType);
        return info;
    }

    @Override
    public void deleteConfig(Long id) {
        imageTypeDao.delete(id);
    }

    @Override
    public TableResultResponse query(ImageTypeInfo info, Pageable pageable) {
        ImageTypeSpec spec = new ImageTypeSpec(info);
        Page<ImageType> page = imageTypeDao.findAll(spec, pageable);

        TableResultResponse response = new TableResultResponse((int)page.getTotalElements(),page.getContent());
        return response;
    }

    @Override
    public ImageTypeInfo getById(Long id) {
        ImageType imageType = imageTypeDao.findOne(id);
        ImageTypeInfo info = new ImageTypeInfo();
        BeanCopierUtils.copyProperties(imageType, info);
        return info;
    }

    @Override
    public List<ImageTypeInfo> getImageType(ImageTypeInfo info) {
        List<ImageType> list;
        if (StringUtils.isNotBlank(info.getDepositorTypeCode())){
            list = imageTypeDao.findByAcctTypeAndOperateTypeAndDepositorTypeCode(info.getAcctType(),info.getOperateType(),info.getDepositorTypeCode());
        }else{
            list = imageTypeDao.findByAcctTypeAndOperateType(info.getAcctType(),info.getOperateType());
        }

        return ConverterService.convertToList(list, ImageTypeInfo.class);
    }

    @Override
    public List<ImageTypeInfo> getByBillId(Long billId) {
        AccountBillsAllInfo bill = accountBillsAllService.getOne(billId);
        //----获取存款人性质start----
        String depositorTypeCode = "";
        AccountPublicInfo api = accountPublicService.findByAccountId(bill.getAccountId());
        if (api != null) {
            depositorTypeCode = api.getDepositorType();
        } else {
            AccountPublicLogInfo apli = accountPublicLogService.findByAccountIdBackup(bill.getAccountId());
            if (apli != null) {
                depositorTypeCode = apli.getDepositorType();
            }
        }
        //----获取存款人性质end----
        ImageTypeInfo info = new ImageTypeInfo();
        info.setAcctType(CompanyAcctType.str2enum(bill.getAcctType().name()));//设置账户类型
        info.setOperateType(BillType.str2enum(bill.getBillType().name()));//设置业务操作类型
        info.setDepositorTypeCode(depositorTypeCode);//设置存款人性质
        return this.getImageType(info);
    }

    @Override
    public ImageTypeInfo getByBillIdAndDocCode(Long billId, String docCode) {
        List<ImageTypeInfo> list = this.getByBillId(billId);
        ImageTypeInfo imageTypeInfo = new ImageTypeInfo();
        for (ImageTypeInfo iti : list) {
            if (iti.getValue().equals(docCode)) {
                imageTypeInfo = iti;
                break;
            }
        }
        return imageTypeInfo;
    }

}
