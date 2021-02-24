package com.ideatech.ams.kyc.service.idcard;

import com.ideatech.ams.kyc.dao.idcard.IdCardBatchDao;
import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;
import com.ideatech.ams.kyc.entity.idcard.IdCardBatch;
import com.ideatech.ams.kyc.util.ImgUtil;
import com.ideatech.ams.kyc.util.SM3Digest;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.Base64Utils;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
@Transactional
@Slf4j
public class IdCardComperServiceImpl implements IdCardComperService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Value("${ams.image.path}")
    private String path;
    @Autowired
    private IdCardLocalService idCardLocalService;

    @Autowired
    private IdCheckLogService idCheckLogService;

    @Autowired
    private PbcAccountService pbcAccountService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private IdCardBatchDao idCardBatchDao;
    @Override
    public IdCheckLogDto comperIdCard(String idCardNo, String idCardName) {
        IdCheckLogDto checkLogInfo = new IdCheckLogDto();
        checkLogInfo.setIdCardNo(idCardNo);
        checkLogInfo.setIdCardName(idCardName);
        //找到平台配置配好的IP地址
        List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listByOrgIdAndType(SecurityUtils.getCurrentUser().getOrgId(), EAccountType.PICP);
        if(pbcAccountDtos == null || pbcAccountDtos.size() == 0) {
            log.info("[联网核查账户信息 ]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[联网核查账户信息 ]不能为空");
        }

        PbcAccountDto pbcAccountDto = pbcAccountDtos.get(0);
        String amsIDCardIp = pbcAccountDto.getIp();

        if(StringUtils.isBlank(amsIDCardIp)){
            log.info("[核查IP地址]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[核查IP地址 ]不能为空");
        }
        if(StringUtils.isBlank(idCardNo)){
            log.info("[身份证号码]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[身份证号码 ]不能为空");
        }
        if(StringUtils.isBlank(idCardName)){
            log.info("[身份证姓名]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[身份证姓名 ]不能为空");
        }
        if(StringUtils.isBlank(pbcAccountDto.getAccount())){
            log.info("[联网核查账户]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[联网核查账户 ]不能为空");
        }
        if(StringUtils.isBlank(pbcAccountDto.getPassword())){
            log.info("[联网核查密码]不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "[联网核查密码 ]不能为空");
        }
        //根据身份证姓名和省份证号码去本地库查询信息
        IdCardLocalDto idCardLocalInfo =  idCardLocalService.queryByIdCardNoAndIdCardName(idCardNo, idCardName);

        setUserMessage(checkLogInfo,amsIDCardIp);//设置公共信息

        //登录核查系统检测是否能成功登录
		/*try {
			Map<String, String> loginMap = getCookie(user.getCheckUserName(),user.getCheckPassword(),amsIDCardIp);
			if(loginMap==null){
				checkLogInfo.setCheckStatus("-1");
				checkLogInfo.setCheckResult("登录失败,无核对结果！");
				checkLogInfo.setCheckMessage("登录失败,无核对结果!");
				idCheckLogService.create(idCardLocalInfo, checkLogInfo);
				return checkLogInfo;
			}
				checkLogInfo.setCheckMessage("登录成功！");
		} catch (Exception e) {
			log.info("登录失败,无核对结果,开始记录日志！");
			e.printStackTrace();
		}*/
        //开始核查身份信息
        try {
            if(isCheckMockOpen()){
                log.info("联网核查挡板开启，默认核查成功！");
                checkLogInfo.setCheckStatus("1");
                checkLogInfo.setCheckResult("号码与姓名一致且照片存在");
                checkLogInfo.setCheckMessage("核查成功,有核查结果！");
                log.info("核查成功,开始记录日志！");
                idCheckLogService.create(idCardLocalInfo, checkLogInfo);
                return checkLogInfo;
            }
            Map<String, Object> map = isSame(pbcAccountDto.getAccount(), pbcAccountDto.getPassword(), idCardNo, idCardName, amsIDCardIp);
            //核查结果正常则将人行头像Base64字符串存到数据库
            String string="";
            if("1".equals(map.get("checkStatus"))){
                idCardLocalInfo.setIdCardPbcImageByte((String)map.get("idCardPbcImageByte"));
                idCardLocalService.save(idCardLocalInfo);
                string = Base64Utils.encodeBase64File((String)map.get("idCardPbcImageByte"));
            }


            checkLogInfo.setCheckStatus((String) map.get("checkStatus"));
            checkLogInfo.setCheckResult((String) map.get("checkResult"));
            checkLogInfo.setCheckMessage("核查成功,有核查结果！");
            log.info("核查成功,开始记录日志！");
            idCheckLogService.create(idCardLocalInfo, checkLogInfo);
            checkLogInfo.setIdCardPbcImageByte(string);
            return checkLogInfo;

        } catch (Exception e) {
            log.info("核查异常,开始记录日志！");
            checkLogInfo.setCheckStatus("-1");
            checkLogInfo.setCheckResult("核查异常,无核对结果！");
            checkLogInfo.setCheckMessage("核查异常,无核对结果！");
            idCheckLogService.create(idCardLocalInfo, checkLogInfo);
            e.printStackTrace();
            return checkLogInfo;
        }
    }

    @Override
    public Boolean login(String ip, String username, String password) {
        if (isLoginMockOpen()) {
            log.info("人行挡板开启，默认返回登录成功");
            return Boolean.TRUE;
        }
        try {
            return null != getCookie(username, password, ip);
        } catch (Exception e) {
            log.error("登录失败", e);
        }
        return Boolean.FALSE;
    }

    @Override
    public void save(String idCardNo, String idCardName) {
        IdCardBatch batch = new IdCardBatch();
        batch.setIdCardName(idCardName);
        batch.setIdCardNo(idCardNo);
        idCardBatchDao.save(batch);
    }

    @Override
    public void start() {
        log.info("开始批量联网核查");
        List<IdCardBatch> list = idCardBatchDao.findAll();
        for (IdCardBatch batch: list) {
            IdCardLocalDto dto = new IdCardLocalDto();
            dto.setIdCardName(batch.getIdCardName());
            dto.setIdCardNo(batch.getIdCardNo());
            try {
                //保存本地信息
                idCardLocalService.save(dto);
                IdCheckLogDto info = comperIdCard(batch.getIdCardNo(),batch.getIdCardName());
                //记录日志
                batch.setCheckResult(info.getCheckResult());
                idCardBatchDao.save(batch);
            }catch (Exception e){
                continue;
            }
        }
        log.info("批量联网核查执行结束");
    }

    @Override
    public List<IdCheckLogDto> findAll() {
        List<IdCheckLogDto> data = new ArrayList<>();
        List<IdCardBatch> list = idCardBatchDao.findAll();
        for (IdCardBatch idCardBatch:list) {
            IdCheckLogDto info = new IdCheckLogDto();
            BeanCopierUtils.copyProperties(idCardBatch,info);
            data.add(info);
        }
        return data;
    }

    @Override
    public void delete(String idcardName, String idcardNo) {
        IdCardBatch idCardBatch =idCardBatchDao.findByIdCardNameAndIdCardNo(idcardName,idcardNo);
        if(idCardBatch!=null){
            idCardBatchDao.delete(idCardBatch);
        }
    }

    private Boolean isLoginMockOpen() {
        List<ConfigDto> pbcLogin = configService.findByKey("idcardLoginEnabled");
        if (CollectionUtils.isNotEmpty(pbcLogin)) {
            return Boolean.valueOf(pbcLogin.get(0).getConfigValue());
        }
        return false;
    }
    private Boolean isCheckMockOpen() {
        List<ConfigDto> pbcLogin = configService.findByKey("idcardEnabled");
        if (CollectionUtils.isNotEmpty(pbcLogin)) {
            return Boolean.valueOf(pbcLogin.get(0).getConfigValue());
        }
        return false;
    }
    public void setUserMessage(IdCheckLogDto checkLogInfo,String amsIDCardIp){
        User user = SecurityUtils.getCurrentUser();
        checkLogInfo.setCheckDate(new Date());
        checkLogInfo.setCheckIP(amsIDCardIp);
        checkLogInfo.setCheckUserId(((SecurityUtils.UserInfo) user).getId());
        checkLogInfo.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
    }

    public Map<String, Object> isSame(String username, String password, String idCardNo,
                                      String idCardName, String ip) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> cookie = getCookie(username, password, ip);
        Map<String, String> map = new HashMap<String, String>();
        //String URLIDName = URLEncoder.encode(idCardName,"gb2312");
        map.put("method", "query");
        map.put("businesscode", "05");
        map.put("certname", idCardName);
        map.put("certno", idCardNo);
        Document doc = getHTML(map, cookie, ip);
        log.info(doc.toString());
        log.info(idCardNo+"+"+idCardName+"---------->核查结果页面---------->"+doc.toString());
        System.out.println(doc.title().toString()+"/////////////////////////////////////////////////////////");
        if(!doc.title().equals("单笔核对结果")){
            resultMap.put("checkStatus", "0");
            resultMap.put("checkResult", "未知错误，请确认对比帐号密码是否正确");
            log.info("未知错误，请确认对比帐号密码是否正确,0");
            //return "未知错误，请确认对比帐号密码是否正确,0";
        }else{
            String checkResul = doc.select("td:containsOwn(核对结果)+td div").text();
            //截取字符串
            String jsessionid = cookie.get("JSESSIONID");
            //联网核查系统的头像base64字符串
            //byte[] byteImage = ImgUtil.toByteArry("http://"+ip+"/picp/photoservlet1;jsessionid="+jsessionid+"?Index=0");
            //new ImgUtil().setImage("http://"+ip+"/picp/photoservlet1;jsessionid="+jsessionid+"?Index=0");
            String filename = new Date().getTime() + ".jpg";
            String filePath = createTempFilePath()+"/"+filename;
            File file = new File(createTempFilePath());
            if (!file.exists()) {
                file.mkdirs();
            }
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
            new ImgUtil().setImage("http://"+ip+"/picp/photoservlet1;jsessionid="+jsessionid+"?Index=0",filePath);
            //String string = Base64Utils.encodeBase64File(filePath);
            if (checkResul.equals("号码与姓名一致且照片存在")) {
                resultMap.put("checkStatus", "1");
                resultMap.put("checkResult", checkResul);
                log.info("号码与姓名一致且照片存在,1");

            } else if (checkResul.equals("号码存在但与姓名不匹配")) {
                resultMap.put("checkStatus", "0");
                resultMap.put("checkResult", checkResul);
                log.info("号码存在但与姓名不匹配,0");
            } else if (checkResul.equals("号码与姓名一致但照片不存在")) {
                resultMap.put("checkStatus", "0");
                resultMap.put("checkResult", checkResul);
                log.info("号码与姓名一致但照片不存在,0");
            } else {
                resultMap.put("checkStatus", "0");
                resultMap.put("checkResult", "号码不存在");
                log.info("号码不存在,0");
            }
            resultMap.put("idCardPbcImageByte", filePath);
        }
        return resultMap;
    }

    public  Map<String, String> getCookie(String username, String password, String ip) throws Exception{
        log.info(username+"开始登陆身份证联网核查系统,IP:"+ip);
        String SM3password = SM3Digest.SM3PassWord(password).toLowerCase();
        String loginURL = "http://" + ip + "/picp/SecurityAction.do";
        String method = "login";
        try {
            Response res = Jsoup
                    .connect(loginURL)
                    .data("method", method, "piUsersDto.usercode", username,
                            "piUsersDto.password", SM3password)
                    .userAgent(
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)")
                    .method(Method.POST).execute();
            log.info(username+"登陆联网核查系统成功！");
            return res.cookies();
        } catch (Exception e) {
            log.info(username+"登陆联网核查系统失败！");
            e.printStackTrace();
            return null;
        }
    }

    public Document getHTML(Map<String, String> map,
                            Map<String, String> cookie, String ip) throws Exception{
        log.info("开始身份证号【"+map.get("certno")+"】,姓名【"+map.get("certname")+"】的核查！");
        StringBuffer sb = new StringBuffer();
        sb.append("?method=query")
                .append("&businesscode=05")
                .append("&certname="+map.get("certname"))
                .append("&certno="+map.get("certno"));
        String queryURL = "http://" + ip + "/picp/SingleInquireAction.do"+sb;
        log.info(queryURL+"/////////////////////////////////////////////");
        try {
            Document doc = Jsoup
                    .connect(queryURL)
                    .cookies(cookie)
                    .userAgent(
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727)")
                    .get();
            log.info("结束身份证号【"+map.get("certno")+"】,姓名【"+map.get("certname")+"】的核查！");
            return doc;
        } catch (Exception e) {
            log.info("身份证号【"+map.get("certno")+"】,姓名【"+map.get("certname")+"】核查异常！");
            e.printStackTrace();
            return null;
        }
    }
    private String createTempFilePath() {
        return path +"/"+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
    }
}
