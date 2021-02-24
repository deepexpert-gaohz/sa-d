package com.ideatech.ams.kyc.service.idcard;

import com.ideatech.ams.kyc.dao.idcard.IdCardLocalDao;
import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.entity.idcard.IdCardLocal;
import com.ideatech.common.util.Base64Utils;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IdCardLocalServiceImpl implements IdCardLocalService {
    //头像存放地址
    @Value("${ams.image.path}")
    private String path;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdCardLocalDao idCardLocalDao;

    @Override
    public void save(IdCardLocalDto info) {
        if(StringUtils.isBlank(info.getIdCardNo())){
            log.info("[身份证号码]不能为空！");
            throw new RuntimeException("[身份证号码 ]不能为空");
        }
        if(StringUtils.isBlank(info.getIdCardName())){
            log.info("[身份证姓名]不能为空！");
            throw new RuntimeException("[身份证姓名 ]不能为空");
        }
        IdCardLocal cardLocal = idCardLocalDao.findByIdCardNoAndIdCardName(info.getIdCardNo(),info.getIdCardName());
        //头像路径
        if(StringUtils.isNotBlank(info.getIdCardLocalImageByte())){
            String filename = new Date().getTime() + ".jpg";
            String filePath = createTempFilePath()+"/"+filename;
            try {
                File file = new File(createTempFilePath());
                if (!file.exists()) {
                    file.mkdirs();
                }
                File imageFile = new File(filePath);
                if (!imageFile.exists()) {
                    imageFile.createNewFile();
                }
                Base64Utils.decoderBase64File(info.getIdCardLocalImageByte(),filePath);//身份证头像数据
                info.setIdCardLocalImageByte(filePath);
            } catch (Exception e) {
                log.error("身份证头像数据处理：" + e);
            }
        }

        User user = SecurityUtils.getCurrentUser();
        if(cardLocal!=null){
            log.info("身份证号["+info.getIdCardNo()+"]姓名["+info.getIdCardName()+"],本地已有身份证信息！开始更新！");
            info.setId(cardLocal.getId());
            info.setOrganFullId(cardLocal.getOrganFullId());
            BeanUtils.copyProperties(info, cardLocal);
            cardLocal.setLastUpdateBy(user.getUsername());
            cardLocal.setLastUpdateDate(new Date());
            idCardLocalDao.save(cardLocal);
            log.info("身份证号["+info.getIdCardNo()+"]姓名["+info.getIdCardName()+"],本地已有身份证信息！结束更新！");
        }else{
            log.info("身份证号["+info.getIdCardNo()+"]姓名["+info.getIdCardName()+"],本地无身份证信息！开始创建！");
            IdCardLocal idCardLocal = new IdCardLocal();
            BeanUtils.copyProperties(info, idCardLocal);
            idCardLocal.setOrganFullId(SecurityUtils.getCurrentOrgFullId());//设置机构fullid
            idCardLocal.setCreatedBy(user.getUsername());//设置创建人id
            idCardLocalDao.save(idCardLocal);
            log.info("身份证号["+info.getIdCardNo()+"]姓名["+info.getIdCardName()+"],本地无身份证信息！结束创建！");
        }
    }

    @Override
    public List<IdCardLocalDto> query(IdCardLocalDto Dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public IdCardLocalDto queryByIdCardNoAndIdCardName(String idCardNo, String idCardName) {
        IdCardLocal cardLocal = idCardLocalDao.findByIdCardNoAndIdCardName(idCardNo, idCardName);
        IdCardLocalDto info = new IdCardLocalDto();
        BeanUtils.copyProperties(cardLocal, info);
        return info;
    }

    @Override
    public IdCardLocalDto getIdCardById(Long id) {
        return null;
    }

    @Override
    public IdCardLocalDto findOne(Long id) {
        return null;
    }

    @Override
    public List<IdCardLocalDto> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(idCardLocalDao.findByOrganFullIdLike(organFullId),IdCardLocalDto.class);
    }
    private String createTempFilePath() {
        return path +"/"+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
    }
}
