package com.ideatech.ams.controller.image;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.apply.enums.AcctType;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.image.dto.*;
import com.ideatech.ams.image.enums.BusinessTypeEnum;
import com.ideatech.ams.image.enums.RecordTypeEnum;
import com.ideatech.ams.image.enums.StoreType;
import com.ideatech.ams.image.service.FaceRecognitionService;
import com.ideatech.ams.image.service.ImageSessionService;
import com.ideatech.ams.image.service.ImageSmsService;
import com.ideatech.ams.image.service.ImageVideoService;
import com.ideatech.ams.image.utils.MessageFormat;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.kyc.service.SaicInfoService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.ws.api.service.ImageRecordApiService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.entity.id.IdWorker;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/video")
@Slf4j
public class ImageVideoController {
    @Value("${ams.image.video.type}")
    private String type;
    @Value("${ams.image.video.maxSize}")
    private int maxSize;

    @Value("${ams.image.noticeMessage:远程双录短信通知模版}")
    private  String noticeMessage;

    @Value("${ams.image.notice.validTime:3600}")
    private Long validTime;

    @Autowired
    private ImageVideoService imageVideoService;

    @Autowired
    private ImageSessionService imageSessionService;

    @Autowired
    private ImageSmsService imageSmsService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private SaicInfoService saicInfoService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ImageRecordApiService imageRecordApiService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping(value = "/search")
    public TableResultResponse query(ImageVideoDto imageVideoDto, @PageableDefault(sort = {"lastUpdateDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return imageVideoService.query(imageVideoDto,pageable);
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public void upload(ImageVideoUpdateDto parms, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        String filename = file.getOriginalFilename();
        String suffixFilename =StringUtils.substringBeforeLast(filename,".");
        ResultDto resultDto = new ResultDto();
        try {
            ImageVideoDto dto = new ImageVideoDto();
            ConverterService.convert(parms, dto);
            if(StringUtils.equals(StringUtils.substringAfterLast(filename,"."),"zip")){
                imageVideoService.uploadZip(file,filename,dto);
            }else{
                String newFilename = createfileName(suffixFilename+".m4v");
                String path = imageVideoService.upload(file,newFilename);
                dto.setFilePath(path);
                dto.setFileFormat("m4v");
                dto.setFileName(newFilename);
                dto.setSyncStatus(CompanyIfType.No);
                dto.setRecordsNo(imageRecordApiService.createNo());
                dto.setType(StoreType.valueOf(type));
                dto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                dto.setUsername(SecurityUtils.getCurrentUsername());
                dto = imageVideoService.save(dto);
                resultDto.setMessage("上传成功");
                resultDto.setCode(ResultCode.ACK);
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(resultDto));
            }
        }catch (Exception e){
            log.error("视频上传异常：{}",e);
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage(e.getMessage());
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(resultDto));
        }
    }

    /**
     * 提供给新双录上传功能使用
     * @param parms
     * @param file
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/upload/v2",method = RequestMethod.POST)
    public void uploadV2(ImageVideoUpdateDto parms, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        String filename = file.getOriginalFilename();
        String suffixFilename =StringUtils.substringBeforeLast(filename,".");
        ResultDto resultDto = new ResultDto();
        try {
            ImageVideoDto dto = new ImageVideoDto();
            ConverterService.convert(parms, dto);
            if(StringUtils.equals(StringUtils.substringAfterLast(filename,"."),"zip")){
                imageVideoService.uploadZip(file,filename,dto);
            }else if (StringUtils.equals(StringUtils.substringAfterLast(filename,"."),"m4v")
                ||StringUtils.equals(StringUtils.substringAfterLast(filename,"."),"mp4")){
                String newFilename = createfileName(suffixFilename+".m4v");
                String path = imageVideoService.upload(file,newFilename);
                dto.setFilePath(path);
                dto.setFileFormat("m4v");
                dto.setFileName(newFilename);
                dto.setSyncStatus(CompanyIfType.No);
                dto.setRecordsNo(imageRecordApiService.createNo());//双录编号
                dto.setType(StoreType.valueOf(type));
                dto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                dto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                dto.setUsername(SecurityUtils.getCurrentUsername());
                dto.setRecordType(RecordTypeEnum.str2enum(parms.getRecordType()));
                dto.setCustomerName(parms.getCustomerName());
                dto.setLegalName(parms.getLegalName());
                dto.setDepositorName(parms.getDepositorName());
                dto.setBusinessType(BusinessTypeEnum.str2enum(parms.getBusinessType()));
                dto = imageVideoService.save(dto);
                resultDto.setMessage("上传成功");
                resultDto.setCode(ResultCode.ACK);
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(resultDto));
            }else {
                log.info("导入的格式不正确");
                resultDto.setCode(ResultCode.NACK);
                resultDto.setMessage("导入的格式不正确");
                response.setContentType("text/html; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(resultDto));
            }
        }catch (Exception e){
            log.error("视频上传异常：{}",e);
            resultDto.setCode(ResultCode.NACK);
            resultDto.setMessage(e.getMessage());
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(resultDto));
        }
    }

    /**
     * 将视频上传到影像平台
     * @param billId 流水id
     * @return
     */
    @RequestMapping(value = "/syncToImage",method = RequestMethod.GET)
    public ResultDto syncToImage(Long billId){
        int res = imageVideoService.syncToImage(billId);
        return ResultDtoFactory.toAck(null,res);
    }
    @RequestMapping(value = "/syncToImageByDepositorName",method = RequestMethod.GET)
    public ResultDto syncToImageByDepositorName(String depositorName){
        int res = imageVideoService.syncToImage(depositorName);
        return ResultDtoFactory.toAck(null,res);
    }
    /**
    /**
     * 删除视频
     * @param id 视频记录id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultDto delete(Long id){
        return imageVideoService.delete(id);
    }

    /**
     * 下载视频
     * @param id 流水id
     * @param response
     */
    @RequestMapping(value = "/downloadZip",method = RequestMethod.GET)
    public void downloadImageZip(Long id,HttpServletResponse response){
        imageVideoService.downloadImageZip(id,response);
    }
    @GetMapping(value = "/findByBillId")
    public ResultDto findByBillId(Long billId){
        return imageVideoService.findByBillId(billId);
    }
    @GetMapping(value = "/findByApplyId")
    public ResultDto findByApplyId(String applyId){
        return imageVideoService.findByApplyId(applyId);
    }
    @GetMapping(value = "/findByCondition")
    public ResultDto findByCondition(ImageVideoUpdateDto dto){
        return imageVideoService.findByCondition(dto);
    }
    @GetMapping(value = "/findByOne")
    public ResultDto findByOne(Long id){
        return imageVideoService.findByOne(id);
    }
    @GetMapping(value = "/findOneInfo")
    public ResultDto findOneInfo(Long id){
        return imageVideoService.findOneInfo(id);
    }
    /**
     * 查信息
     * @param id
     * @return
     */
    @GetMapping(value = "/findOne")
    public ResultDto findOne(Long id){
        return imageVideoService.findOne(id);
    }
    @GetMapping(value = "/getFromIdp")
    public ResultDto getFromIdp(String applyid,Long billId){
        imageVideoService.getFromIdp(applyid,billId);
        return ResultDtoFactory.toAck();
    }
    @GetMapping(value = "/getVideoSize")
    public int getVideoSize(){
        return maxSize;
    }

    @PostMapping(value = "/edit")
    public ResultDto edit(ImageVideoDto imageVideoDto){
        return imageVideoService.edit(imageVideoDto);
    }

    /**
     * 发送双录短信
     * 1、创建坐席访客的会话信息
     * 2、生成访客地址（可增加人脸识别），发送短信
     * @param noticeMessageDto
     * @return
     */
    @PostMapping(value = "/message")
    public ResultDto sendMessage(NoticeMessageDto noticeMessageDto){
        try {
//            String min = "【{bankName}】尊敬的{legalName}，您的企业{depositorName}正在我行进行{acctType}开户，请点击下方链接我行工作人员将与您进行法人远程双录：{url}；如有疑问请致电：{telephone}。";
//            noticeMessageDto.setBankName("交通银行高新支行");
//            noticeMessageDto.setAcctType("基本户");
//            noticeMessageDto.setUrl("http://www.baidu.com");
//            noticeMessageDto.setTelephone("880-4400-6666");

            // 1.1设置会话基本信息
            ImageSessionDTO imageSessionDTO = new ImageSessionDTO();
            imageSessionDTO.setId(idWorker.nextId());//提前生成id
            imageSessionDTO.setServerName(SecurityUtils.getCurrentUsername());//坐席端登录名
            imageSessionDTO.setClientName(noticeMessageDto.getLegalTelephone());//客户端登录名(法人手机号)
            imageSessionDTO.setDepositorName(noticeMessageDto.getDepositorName());//客户名称（企业名称）
            imageSessionDTO.setLegalName(noticeMessageDto.getLegalName());//法人姓名
            imageSessionDTO.setLegalIdcardNo(noticeMessageDto.getLegalIdcardNo());//法人证件号
            imageSessionDTO.setRandomFlag(noticeMessageDto.getRandomFlag());//是否随机坐席
            imageSessionDTO.setEndTimeStamp(System.currentTimeMillis()+validTime*1000);//过期时间戳（默认1小时）
            imageSessionDTO.setCustomerName(noticeMessageDto.getCustomerName());//客户姓名
            imageSessionDTO.setBusinessType(BusinessTypeEnum.str2enum(noticeMessageDto.getBusinessType()));//业务类型

            // 1.2设置会话开户行信息
            noticeMessageDto.setAcctType(AcctType.str2enum(noticeMessageDto.getAcctType())!=null?AcctType.str2enum(noticeMessageDto.getAcctType()).getDisplayName():null);
            OrganizationDto organizationDto = organizationService.findByOrganFullId(SecurityUtils.getCurrentOrgFullId());
            if (organizationDto!=null){
                noticeMessageDto.setBankName(organizationDto.getName());
                noticeMessageDto.setTelephone(organizationDto.getTelephone());
                imageSessionDTO.setBankName(organizationDto.getName());//开户行
            }else {
                throw new Exception("获取开户行信息失败");
            }

            //1.3设置短信中双录访客地址
            ConfigDto configDto = configService.findOneByConfigKey("dualRecordGuestUrl");
            if (configDto!=null){

                //是否调用人脸识别
                ConfigDto configDto2 = configService.findOneByConfigKey("faceRecognitionEnabled");
                if(configDto2!=null && Boolean.valueOf(configDto2.getConfigValue())){

                    // 生成人脸识别链接
                    Map<String,Object> map = faceRecognitionService.getVerifyUrl(noticeMessageDto.getLegalName(),
                            noticeMessageDto.getLegalIdcardNo(),
                            configDto.getConfigValue() + "/" + imageSessionDTO.getId(),
                            configDto.getConfigValue());

                    if (StringUtils.isNotBlank((String) map.get("realPersonVerifyUrl"))){
                        noticeMessageDto.setUrl((String) map.get("realPersonVerifyUrl"));
                        imageSessionDTO.setFaceResult("-1");//未认证
                        imageSessionDTO.setIdpRequestId((String) map.get("idpRequestId"));//结果查询地址
                    }else {
                        throw new Exception("人脸识别认证地址生成失败");
                    }

                //不启用人脸识别
                }else {
                    imageSessionDTO.setFaceResult("3");//无需识别
                    noticeMessageDto.setUrl(configDto.getConfigValue() + "/" + imageSessionDTO.getId());
                }
            }else {
                throw new Exception("双录访客地址未配置");
            }


            //1.4 短信模版信息渲染，发送短信。
            String formatMessage = MessageFormat.formatMessage(noticeMessage, noticeMessageDto);
            imageSmsService.sendMessage(noticeMessageDto.getLegalTelephone(), formatMessage);

            //1.5创建会话信息，提供给双录系统查询
            imageSessionDTO.setMessage(formatMessage);
            noticeMessageDto.setMessage(formatMessage);
            imageSessionDTO = imageSessionService.create(imageSessionDTO);

        } catch (Exception e) {
            log.warn("短信发送失败",e);
            return ResultDtoFactory.toNack("发送失败:{}",e.getMessage());
        }
        return ResultDtoFactory.toAckData(noticeMessageDto);
    }

    /**
     * 影像模块 双录视频信息保存
     * @param imageVideoDto
     * @return
     */
    @PostMapping("/imageVideo")
    public ResultDto saveByDualRecord(ImageVideoDto imageVideoDto,String clientName){
        imageVideoDto.setSyncStatus(CompanyIfType.No);
        imageVideoDto.setRecordsNo(imageRecordApiService.createNo());
        imageVideoDto.setType(StoreType.fastdfs);
        imageVideoDto.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
        imageVideoDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        imageVideoDto.setUsername(SecurityUtils.getCurrentUsername());
        VideoFileDTO videoFileDTO = imageVideoService.getVideoFileByCallId(imageVideoDto.getCallId());
        if (videoFileDTO!=null){
            imageVideoDto.setFileName(videoFileDTO.getFileName());
            imageVideoDto.setFilePath(videoFileDTO.getFileUrl());//视频播放地址
        }else {
            log.info("双录会议信息获取失败,callId:{}",imageVideoDto.getCallId());
            return ResultDtoFactory.toNack("保存失败");
        }

        ImageSessionDTO imageSessionDTO = imageSessionService.findOneByClientName(clientName);
        if (imageSessionDTO!=null){
            imageVideoDto.setLegalName(imageSessionDTO.getLegalName());
            imageVideoDto.setCustomerName(imageSessionDTO.getCustomerName());
            SaicIdpInfo saicInfoBaseLocal = saicInfoService.getSaicInfoBaseLocal(imageSessionDTO.getDepositorName());
            if (saicInfoBaseLocal!=null){
                imageVideoDto.setDepositorName(saicInfoBaseLocal.getName());
                imageVideoDto.setLegalName(saicInfoBaseLocal.getLegalperson());
                imageVideoDto.setRegNo(saicInfoBaseLocal.getRegistno());
            }else {
                log.info("工商信息获取失败:[{}]",imageSessionDTO.getDepositorName());
            }
        }else {
            log.info("会话信息获取失败");
        }

        final ImageVideoDto save = imageVideoService.save(imageVideoDto);

        //异步同步远程双录视频
        taskExecutor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                imageRecordApiService.syncRecord(save);
                return null;
            }
        });

        return ResultDtoFactory.toAck();
    }

    @GetMapping("/customer")
    public ResultDto getCustomerByClientName(String clientName){
        if (clientName==null){
            return ResultDtoFactory.toNack("clientName不能为空");
        }
        ImageSessionDTO imageSessionDTO = imageSessionService.findOneByClientName(clientName);
        if (imageSessionDTO!=null){
            //返回对公客户信息
            if (StringUtils.isNotBlank(imageSessionDTO.getDepositorName())){
                SaicIdpInfo saicInfoBaseLocal = saicInfoService.getSaicInfoBaseLocal(imageSessionDTO.getDepositorName());
                if (saicInfoBaseLocal!=null){
                    return ResultDtoFactory.toAckData(saicInfoBaseLocal);
                }else {
                    return ResultDtoFactory.toNack(imageSessionDTO.getDepositorName());
                }
            }else {//返回对私客户信息
                return ResultDtoFactory.toAckData(imageSessionDTO);
            }

        }
        return ResultDtoFactory.toNack(clientName);
    }

    /**
     * 双录视频列表数据 EXCEL导出
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(ImageVideoDto imageVideoDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IExcelExport export = imageVideoService.export(imageVideoDto);
        StringBuilder fileName = new StringBuilder();
        fileName.append(System.currentTimeMillis());
        response.setHeader("Content-disposition", "attachment; filename="+fileName+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",export);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 下载视频
     * @param videoUrl 视频地址URL
     * @param response
     */
    @RequestMapping(value = "/downloadVideo",method = RequestMethod.GET)
    public void downloadVideo(String videoUrl,HttpServletResponse response){
        // 1.获取链接
        URL url = null;
        try {
            url = new URL(videoUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        //2.下载网络视频
        URLConnection conn = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            conn = url.openConnection();
            inputStream = conn.getInputStream();
            outputStream = response.getOutputStream();

            String format = DateFormatUtils.format(System.currentTimeMillis(),"yyyy_MM_dd_HH_mm_ss");
            String downFileName = format + ".mp4";
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;fileName=" + new String(downFileName.getBytes(),"iso-8859-1"));
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    private String createfileName(String filename){
        StringBuffer recordsNo=new StringBuffer();
        recordsNo.append(DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMddHHmmss"));
        recordsNo.append("_");
        recordsNo.append(filename);
        log.info("双录编号：{}",recordsNo.toString());
        return recordsNo.toString();
    }
}
