package com.ideatech.ams.system.announcement.service;

import com.ideatech.ams.system.announcement.dao.AnnouncementDao;
import com.ideatech.ams.system.announcement.dao.AttachmentDao;
import com.ideatech.ams.system.announcement.dto.AnnouncementDto;
import com.ideatech.ams.system.announcement.entity.Announcement;
import com.ideatech.ams.system.announcement.entity.Attachment;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author jzh
 * @date 2019/2/25.
 */

@Service
@Transactional
@Slf4j
public class AnnouncementServiceImpl extends BaseServiceImpl<AnnouncementDao, Announcement, AnnouncementDto> implements AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;

    @Autowired
    private AttachmentDao attachmentDao;

    @Value("${ams.announcement.upload.path}")
    private String path;

    @Override
    public TableResultResponse page(Pageable pageable,String organfullId) {

        Collection<String> collection = new ArrayList<>();
        String[] temp;
        String delimeter = "-";
        temp = organfullId.split(delimeter);
        StringBuffer id = new StringBuffer("");

        // 获取所有相关机构organfullId
        for(int i =0; i < temp.length ; i++){
            log.info(temp[i]);
            id.append(temp[i]);
            collection.add(id.toString());
            id.append("-");
        }
        Page<Announcement> announcementPage = announcementDao.findAllByOrganfullIdInOrderByIdDesc(collection,pageable);
        return new TableResultResponse((int)announcementPage.getTotalElements(),announcementPage.getContent());
    }

    @Override
    public AnnouncementDto getTop(String organfullId) {

        List<Announcement> announcementList = new ArrayList<>();
        String[] temp;
        String delimeter = "-";
        temp = organfullId.split(delimeter);
        StringBuffer id = new StringBuffer("");

        // 查找所有上级及本机构的最新通告，并加到announcementList。
        for(int i =0; i < temp.length ; i++){
            log.info(temp[i]);
            id.append(temp[i]);
            Announcement announcement = announcementDao.findTopByOrganfullIdOrderByIdDesc(id.toString());
            if (announcement!=null){
                announcementList.add(announcement);
            }
            id.append("-");
        }

        //当所有上级及本机构都没有通告时
        if (announcementList.size()==0){
            return null;
        }

        //选取所有上级及本机构中最新的通告
        Announcement announcement = announcementList.get(0);
        for (Announcement a:announcementList){
            if(announcement.getId()<a.getId()){
                announcement = a;
            }
        }

        return ConverterService.convert(announcement,AnnouncementDto.class);
    }

    @Override
    public TableResultResponse page(Pageable pageable,String title,String organfullId){

        Collection<String> collection = new ArrayList<>();
        String[] temp;
        String delimeter = "-";
        temp = organfullId.split(delimeter);
        StringBuffer id = new StringBuffer("");

        // 获取所有相关机构organfullId
        for(int i =0; i < temp.length ; i++){
            id.append(temp[i]);
            collection.add(id.toString());
            id.append("-");
        }

        Page<Announcement> announcementPage = announcementDao.findAllByTitleLikeAndOrganfullIdInOrderByIdDesc("%"+title+"%",collection,pageable);

        return new TableResultResponse((int)announcementPage.getTotalElements(),announcementPage.getContent());
    }

    @Override
    public void delete(Long id) {
        Announcement announcement = announcementDao.findOne(id);
        announcement.setDeleted(true);
        announcementDao.save(announcement);
    }

    @Override
    public String upload(InputStream is, String fileName) {
        String url = path + File.separator + fileName;
        File file = new File(path);
        if (!file.exists()) {
            log.info("开始创建公告附件文件夹：" + path);
            file.mkdirs();
        }
        File finalFile = new File(url);
        try {
            FileUtils.copyInputStreamToFile(is,finalFile);
        } catch (Exception e) {
            log.error("公告附件保存异常", e);
        }
        return fileName;
    }

    @Override
    public Long saveAnnouncement(AnnouncementDto announcementDto) {
        Announcement announcement = ConverterService.convert(announcementDto,Announcement.class);
        return announcementDao.save(announcement).getId();
    }

    @Override
    public void download(HttpServletResponse response, Long id) {

        Attachment attachment =attachmentDao.findTopByAnnouncementIdOrderByIdDesc(id);
        if (attachment==null){
            log.error("附件下载异常：附件不存在！");
            return;
        }
        String fileURL = path+File.separator+attachment.getFullPath();
        log.info(fileURL);
        File file = new File(fileURL);
        InputStream is = null;
        OutputStream zos = null;
        try {
            zos = response.getOutputStream();
            is = new FileInputStream(file);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHddss");
            String format = sdf.format(new Date());
            String downFileName = format +"_"+ attachment.getFullPath();
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;fileName=" + downFileName);
            IOUtils.copy(is, zos);
            zos.flush();
        }catch (Exception e){
            log.error("附件下载异常："+e.getMessage());
        }finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(is);
        }
    }
}
