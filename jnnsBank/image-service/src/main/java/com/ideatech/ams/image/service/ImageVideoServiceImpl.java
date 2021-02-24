package com.ideatech.ams.image.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.image.dao.ImageVideoDao;
import com.ideatech.ams.image.dao.spec.ImageVideoSpec;
import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.dto.ImageVideoUpdateDto;
import com.ideatech.ams.image.dto.VideoFileDTO;
import com.ideatech.ams.image.dto.VideoResultDto;
import com.ideatech.ams.image.entity.ImageVideo;
import com.ideatech.ams.image.enums.BusinessTypeEnum;
import com.ideatech.ams.image.enums.StoreType;
import com.ideatech.ams.image.poi.ImageVideoExport;
import com.ideatech.ams.image.poi.ImageVideoPoi;
import com.ideatech.ams.image.utils.ZipUtils;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.ws.api.service.ImageVideoSyncService;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import com.ideatech.common.util.HttpRequest;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
//@Transactional
public class ImageVideoServiceImpl extends BaseServiceImpl<ImageVideoDao, ImageVideo, ImageVideoDto> implements ImageVideoService{
    @Value("${ams.image.video.nginx.path}")
    private String filePath;
    //nginx的ip
    @Value("${ams.image.video.nginx.IP}")
    private String nginxIp;
    //nginx的端口
    @Value("${ams.image.video.nginx.port}")
    private String nginxPort;
    //双录视频最大字节数
    @Value("${ams.image.video.maxSize}")
    private long maxSize;
    //双录视频格式
    @Value("${ams.image.video.format}")
    private String format;
    @Value("${ams.image.video.type}")
    private String type;


    /**
     * 获取会议视频接口
     */
    @Value("${ams.image.videoFile.url}")
    private String videoFileUrl;

    @Autowired
    private ImageVideoSyncService imageVideoSyncService;

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private HttpRequest httpRequest;

    private final static String FINISH_EXTENSION = "m4v";

    public static final String HTTP_PRODOCOL = "http://";
    private String createFilePath() {
        String path = filePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    @Override
    public TableResultResponse query(ImageVideoDto imageVideoDto, Pageable pageable) {
        SecurityUtils.UserInfo current = SecurityUtils.getCurrentUser();
        OrganizationDto organizationDto = organizationService.findById(current.getOrgId());
        imageVideoDto.setOrganFullId(organizationDto.getFullId());
        Page<ImageVideo> page = getBaseDao().findAll(new ImageVideoSpec(imageVideoDto),pageable);
        return new TableResultResponse((int)page.getTotalElements(), page.getContent());
    }

    @Override
    public IExcelExport export(ImageVideoDto imageVideoDto) {
        imageVideoDto.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        List<ImageVideo> all = getBaseDao().findAll(new ImageVideoSpec(imageVideoDto), new Sort(Sort.Direction.DESC,"dateTime"));
        List<ImageVideoPoi> imageVideoPoiList = new ArrayList<>(64);
        ImageVideoPoi imageVideoPoi = null;
        for (ImageVideo imageVideo:all) {
            imageVideoPoi = ConverterService.convert(imageVideo,ImageVideoPoi.class);
            imageVideoPoi.setBusinessType(imageVideo.getBusinessType()!=null?imageVideo.getBusinessType().getName():"-");
            imageVideoPoi.setRecordType(imageVideo.getRecordType()!=null?imageVideo.getRecordType().getName():"-");
            imageVideoPoiList.add(imageVideoPoi);
        }
        IExcelExport iExcelExport = new ImageVideoExport();
        iExcelExport.setPoiList(imageVideoPoiList);
        return iExcelExport;
    }


    @Override
    public String upload(MultipartFile multipartFile,String filename) throws Exception {
        validate(multipartFile);
        String url=null;
        if(StringUtils.isBlank(type)){
            log.info("请配置视频存储方式");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"请配置视频存储方式");
        }
        if(StringUtils.equals("nginx",type)){
            String diskPath = createFilePath();
            //由于前端页面播放只能是m4v格式的视频，接收到视频之后统一转为m4v视频格式
             url = diskPath + "/"+ filename;
            File file = new File(diskPath);
            if (!file.exists()) {
                log.info("开始创建视频存放目录：{}",diskPath);
                file.mkdirs();
            }
            File fi = new File(url);
            boolean  table= fi.setExecutable(true);
            log.info("是否有执行权限："+table);
            boolean  Readable= fi.setReadable(true);
            log.info("是否有读取权限："+Readable);
            boolean  Writable= fi.setWritable(true,true);
            log.info("是否有写权限："+Writable);
            if(!fi.exists()){
                fi.createNewFile();
            }
            multipartFile.transferTo(fi);
        }
        if(StringUtils.equals("fastdfs",type)){
            //TODO:直接上传到文件服务器并返回url
        }

        return url;
    }

    @Override
    public void uploadZip(MultipartFile multipartFile, String filename,ImageVideoDto dto) throws Exception {
        String url=null;
        if(StringUtils.isBlank(type)){
            log.info("请配置视频存储方式");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"请配置视频存储方式");
        }
        if(StringUtils.equals("nginx",type)){
            String diskPath = createFilePath();
            //由于前端页面播放只能是m4v格式的视频，接收到视频之后统一转为m4v视频格式
            url = diskPath + "/"+ filename;
            File file = new File(diskPath);
            if (!file.exists()) {
                file.mkdirs();
                log.info("开始创建视频存放目录：{}",diskPath);
            }
            File fi = new File(url);
            multipartFile.transferTo(fi);
            //解压得到文件
            File tempFile = new File(diskPath);
            List<File> fileList = com.ideatech.common.util.ZipUtils.unzipFile(new File(url),tempFile);
            InputStream is = null;
            try {
                for (File f:fileList) {
                    is = new  FileInputStream(f);
                    String tempFileName = createfileName(StringUtils.substringBeforeLast(f.getName(),".")+"."+FINISH_EXTENSION);
                    String path = diskPath+"/"+tempFileName;
                    //FileUtils.moveFileToDirectory(f, new File(finishFolder), true);
                    FileUtils.copyInputStreamToFile(is, new File(path));
                    ImageVideo v = new ImageVideo();
                    ConverterService.convert(dto, v);
                    v.setFilePath(path);
                    v.setFileFormat(FINISH_EXTENSION);
                    v.setFileName(tempFileName);
                    v.setSyncStatus(CompanyIfType.No);
                    v.setRecordsNo(createNo());
                    v.setType(StoreType.valueOf(type));
                    v.setDateTime(DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
                    v.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    v.setUsername(SecurityUtils.getCurrentUsername());
                    getBaseDao().save(v);
                }
            }catch (Exception e){
                log.error("视频解压存储异常：{}",e);
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"视频解压存储异常："+e.getMessage());
            }finally {
                IOUtils.closeQuietly(is);
                for (File f:fileList) {
                    if(f.exists()){
                        f.delete();
                    }
                }
            }
        }
        if(StringUtils.equals("fastdfs",type)){
            //TODO:直接上传到文件服务器并返回url
        }
    }

    @Override
    public int syncToImage(Long billId) {
        List<ImageVideo> data = getBaseDao().findByBillsIdAndSyncStatus(billId,CompanyIfType.No);
        return sys(data);
    }

    @Override
    public int syncToImage(String depositorName) {
        List<ImageVideo> data = getBaseDao().findByDepositorNameAndSyncStatus(depositorName,CompanyIfType.No);
         return sys(data);
    }

    @Override
    public void schedule() {
        List<ImageVideo> data = getBaseDao().findBySyncStatus(CompanyIfType.No);
        sys(data);
    }

    @Override
    public ResultDto delete(Long id) {
        ResultDto dto = new ResultDto();
        ImageVideo imageVideo = getBaseDao().findOne(id);
        if(imageVideo.getSyncStatus()==CompanyIfType.Yes){
            dto.setCode(ResultCode.NACK);
            dto.setMessage("视频已同步到影像平台，不可删除");
        }else{
            if(imageVideo.getType()==StoreType.nginx){
                //如果是存储在应用服务器，先删除文件
                File file = new File(imageVideo.getFilePath());
                if(!file.exists()){
                    log.info("视频不存在，视频路径："+imageVideo.getFilePath());
                }else{
                    file.delete();
                    log.info("本地视频删除成功，视频路径："+imageVideo.getFilePath());
                }
            }else if(imageVideo.getType()==StoreType.fastdfs){
                //TODO:需要删除文件服务器上面的视频
            }

            getBaseDao().delete(id);
            dto.setCode(ResultCode.ACK);
        }
        return dto;
    }

    @Override
    public void downloadImageZip(Long id, HttpServletResponse response) {
        String zipUrl = createFilePath()+"/"+System.currentTimeMillis() + ".zip";
        File fileZip = new File(zipUrl);
        InputStream is = null;
        OutputStream zos = null;
        ImageVideo v = getBaseDao().findOne(id);
        List<File> tempFile = new ArrayList<>();
        if(v.getSyncStatus()==CompanyIfType.Yes){
            log.info("下载影像平台视频");
            tempFile = imageVideoSyncService.download(v.getBatchNumber());
        }else{
            if(v.getType()==StoreType.nginx){
                log.info("下载本地视频");
                File file = new File(v.getFilePath());
                tempFile.add(file);
            }else if(v.getType()==StoreType.fastdfs){
                log.info("下载文件服务器视频");
                //TODO:需要实现下载文件服务器的视频
            }

        }
        File[] files = new File[tempFile.size()];
        if(tempFile.size()<=0){
            log.info("没有查到视频");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有查到视频");
        }else{
            for (int i = 0 ; i < tempFile.size() ; i++){
                files[i] = tempFile.get(i);
            }
        }
        try {
            zos = response.getOutputStream();
            ZipUtils.compressFilesZip(files, zipUrl); // 压缩文件
            is = new FileInputStream(fileZip);
            String format = DateFormatUtils.format(System.currentTimeMillis(),"yyyy_MM_dd_HH_mm_ss");
            String downFileName = format + ".zip";
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;fileName=" + new String(downFileName.getBytes(),"iso-8859-1"));
            IOUtils.copy(is, zos);
            zos.flush();
        }catch (Exception e){
            log.error("视频下载："+e.getMessage());
        }finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(is);
            File deleteZip = new File(zipUrl);
            if(deleteZip.exists()){
                deleteZip.delete();
            }
        }
    }

    @Override
    public List<ImageVideoDto> searchByOut(ImageVideoDto dto) {
        List<ImageVideo> data = getBaseDao().findAll(new ImageVideoSpec(dto));
        List<ImageVideoDto> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            //result = ConverterService.convertToList(data, ImageVideoDto.class);
            for (ImageVideo v:data) {
                ImageVideoDto dto1 = new ImageVideoDto();
                ConverterService.convert(v, dto1);
                if(v.getType()==StoreType.nginx){
                    dto1.setFilePath(getUrl(v));
                }
                result.add(dto1);
            }
        }
        return result;
    }

    @Override
    public ResultDto findByBillId(Long billId) {
        List<ImageVideo> data = getBaseDao().findByBillsId(billId);
        return find(data);
    }

    @Override
    public ResultDto findByApplyId(String applyId) {
        List<ImageVideo> data = getBaseDao().findByApplyid(applyId);
        return find(data);
    }

    @Override
    public ResultDto findByCondition(ImageVideoUpdateDto condition) {
        ImageVideoDto dto = new ImageVideoDto();
        ConverterService.convert(condition, dto);
        List<ImageVideo> data = getBaseDao().findAll(new ImageVideoSpec(dto));
        return find(data);
    }

    @Override
    public ResultDto findByOne(Long id) {
        ImageVideo imageVideo = getBaseDao().findOne(id);
        List<ImageVideo> data = new ArrayList<>();
        if(imageVideo!=null){
            data.add(imageVideo);
        }
        return find(data);
    }

    @Override
    public ResultDto findOneInfo(Long id) {
        ImageVideo imageVideo = getBaseDao().findOne(id);
        return ResultDtoFactory.toAck(null,imageVideo);
    }

    @Override
    public ResultDto findOne(Long id) {
        ImageVideo v = getBaseDao().findOne(id);
        return ResultDtoFactory.toAck(null,v);
    }

    private ResultDto find(List<ImageVideo> data){
        List<VideoResultDto> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            for (ImageVideo v:data) {
                VideoResultDto dto = new VideoResultDto();
                dto.setId(v.getId());
                StringBuffer sb = new StringBuffer();
                if(StringUtils.isNotBlank(v.getVErrorValue())){
                    sb.append("(");
                    sb.append(v.getVErrorValue());
                    if(StringUtils.isNotBlank(v.getRemarks())){
                        sb.append("_"+v.getRemarks());
                    }
                    sb.append(")");
                }
                sb.append(v.getFileName());
                dto.setTitle(sb.toString());
                dto.setImgUrl("../../config/download?configKey=thumbnail.small");
                if(v.getSyncStatus()==CompanyIfType.Yes){
                    String url = imageVideoSyncService.searchUrl(v.getBatchNumber());
                    log.info("【{}】已上传影像平台，去影像平台查询访问url:{}",v.getFileName(),url);
                    dto.setVideoUrl(url);
                }else{
                    if(v.getType()==StoreType.nginx){
                        dto.setVideoUrl(getUrl(v));
                    }
                    if(v.getType()==StoreType.fastdfs){
                        dto.setVideoUrl(v.getFilePath());
                    }
                }
                result.add(dto);
            }

        }else{
            log.info("{}流水没找到视频");
        }
        return ResultDtoFactory.toAck(null,result);
    }
    @Override
    public void getFromIdp(String applyid, Long billId) {
        //TODO:待实现该接口
        log.info("获取idp视频资源");
    }

    @Override
    public ResultDto edit(ImageVideoDto imageVideoDto) {
        if(imageVideoDto.getId()==null){
            return ResultDtoFactory.toNack("视频ID不能为空");
        }
        ImageVideo v = getBaseDao().findOne(imageVideoDto.getId());
        if(v!=null && StringUtils.isNotBlank(imageVideoDto.getVErrorCode())){
            String name = dictionaryService.getNameByValue("imageVideoErrorType",imageVideoDto.getVErrorCode());
            v.setVErrorCode(imageVideoDto.getVErrorCode());
            v.setVErrorValue(name);
            v.setRemarks(imageVideoDto.getRemarks());
            getBaseDao().save(v);
        }
        return ResultDtoFactory.toAck();
    }

    @Override
    public VideoFileDTO getVideoFileByCallId(String callId) {

        log.info("callId:[{}]",callId);
        //TODO 重复5次
        for (int i = 0;i<5;i++){
            try {
                //无法立即获取会议视频信息等待三秒。
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RestTemplate restTemplate = httpRequest.getRestTemplate(videoFileUrl+callId);
            String result = restTemplate.getForObject(videoFileUrl + callId, String.class);
            log.info(result);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONArray data = jsonObject.getJSONArray("data");
            if (data!=null && data.size()>0){
                return data.getJSONObject(0).toJavaObject(VideoFileDTO.class);
            }
        }
        log.info("获取会议视频信息失败");
        return null;
    }

    private int sys(List<ImageVideo> data){
        log.info("视频同步数量：{}",data.size());
        int res = 0;
        if(CollectionUtils.isNotEmpty(data)){
            for (ImageVideo v:data) {
                ImageVideoDto dto = new ImageVideoDto();
                ConverterService.convert(v, dto);
                //TODO:如果存储方式是fastdfs，需要给dto给一个从文件服务器下载视频的地址
                if(v.getType()==StoreType.fastdfs){

                }
                String batchNumber;
                try {
                    batchNumber = imageVideoSyncService.sync(dto);
                    if(StringUtils.isNotBlank(batchNumber)){
                        File file = new File(v.getFilePath());
                        if(!file.exists()){
                            log.info("视频不存在，视频路径："+v.getFilePath());
                        }else{
                            file.delete();
                            log.info("本地视频删除成功，视频路径："+v.getFilePath());
                        }
                        v.setFilePath("");
                        v.setBatchNumber(batchNumber);
                        v.setSyncStatus(CompanyIfType.Yes);
                        getBaseDao().save(v);
                    }
                }catch (Exception e){
                    res++;
                    log.error("本地【{}】视频上传到影像平台失败，失败原因：{}",v.getFilePath(),e);
                    continue;
                }
            }
        }else{
            log.info("没有需要同步的视频");
        }
        return res;
    }

    private void validate(MultipartFile multipartFile) throws Exception {
        String filename = multipartFile.getOriginalFilename();
        String suffixFilename =StringUtils.substringAfterLast(filename,".");
        if(!format.contains(suffixFilename)){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"视频格式不对,只支持["+format+"]格式,当前格式："+suffixFilename);
        }
       byte[] arrar = multipartFile.getBytes();
        long max = maxSize* 1024*1024;
        if(arrar.length>max){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"视频超过最大限制,最大"+maxSize+"M,请压缩后上传，压缩格式为zip");
        }
    }
    private String getUrl(ImageVideo v){
        StringBuffer sb = new StringBuffer();
        sb.append(HTTP_PRODOCOL);
        sb.append(nginxIp);
        sb.append(":");
        sb.append(nginxPort);
        sb.append("/");
        sb.append(v.getFileName());
        log.info("视频访问url："+sb.toString());
        return sb.toString();
    }
    private String createNo(){
        StringBuffer recordsNo=new StringBuffer();
        recordsNo.append(DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMddHHmmss"));
        recordsNo.append(Integer.toString((int)((Math.random()*9+1)*100000)));
        return recordsNo.toString();
    }
    private String createfileName(String filename){
        StringBuffer recordsNo=new StringBuffer();
        recordsNo.append(DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMddHHmmss"));
        recordsNo.append("_");
        recordsNo.append(filename);
        return recordsNo.toString();
    }
}
